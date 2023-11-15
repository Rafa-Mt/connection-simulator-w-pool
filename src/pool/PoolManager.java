package pool;

import java.util.List;
import java.util.stream.Collectors;

import global.PgConnection;

public class PoolManager {
    private Pool pool;
    private AccessLogger logger = new AccessLogger();
    private int id;
    public int getId() { return this.id; }

    public PoolManager(int id) {
        this.id = id;
        this.logger = new AccessLogger();
        this.pool = Pool.getInstance();
    }

    public synchronized PgConnection getConnection() {
        logger.log(id, "Requested");
        int count = getFreeCount();
        if (count == 0) {
            logger.log(id, "Failed");
            return null;
        };

        if (count+1 == pool.flagHigh) 
            growPool(pool.sizeWhenHigh, true);

        if (count+1 == pool.flagLow) 
            shrinkPool(pool.sizeWhenLow, true);
            
        if (pool.flagLow < count+1 && count+1 < pool.flagHigh) 
            stablePool();

        PgConnection selected = getFreeConns().get(0);
        selected.setAvailability(false);

        logger.log(id, "Got");
        return selected;
    }

    public synchronized void releaseConnection(PgConnection connection) {
        connection.setAvailability(true);
        logger.log(id, "Released");
    }

    private synchronized void growPool(int target, boolean stabilize) {
        while (pool.connPool.size() < target) {
            pool.connPool.add(new PgConnection());
        }
        if (stabilize) logger.log(id, "Grew");
    }

    private synchronized void shrinkPool(int target, boolean stabilize) {
        while (pool.connPool.size() > target) {
            PgConnection deleted = getFreeConns().get(0);
            deleted.disconnect();
            pool.connPool.remove(deleted);
        }
        if (stabilize) logger.log(id, "Shrank");
    }

    private synchronized void stablePool() {
        if (pool.connPool.size() == pool.sizeWhenStable) return;

        logger.log(id, "Stabilized");
        if (pool.connPool.size() > pool.sizeWhenStable) {
            shrinkPool(pool.sizeWhenStable, false);
            return;
        }
        
        growPool(pool.sizeWhenStable,false);
    }

    
    private List<PgConnection> getFreeConns() {
        return pool.connPool.stream()
            .collect(Collectors.toList());
    }

    public int getFreeCount() {
        return getFreeConns().size();
    }
}
