package pool;

import global.PropertiesHandler;
import global.Controller;
import global.PgConnection;

import java.util.List;
import java.util.ArrayList;

public class Pool { 
    private PropertiesHandler props = new PropertiesHandler("pool");

    protected int sizeWhenStable = Integer.parseInt(props.get("size-base"));
    protected int sizeWhenLow = Integer.parseInt(props.get("size-when-low"));
    protected int sizeWhenHigh = Integer.parseInt(props.get("size-when-high"));
    protected int flagLow = Integer.parseInt(props.get("flag-low"));
    protected int flagHigh = Integer.parseInt(props.get("flag-high"));

    protected List<PgConnection> connPool;
    
    private static Pool instance = null;
    
    @SuppressWarnings("unused")
    private Pool() {
        connPool = new ArrayList<PgConnection>();
        for (int __ : Controller.range(1, sizeWhenStable)) {
            connPool.add(new PgConnection());
        }
    }
 
    public static synchronized Pool getInstance() {
        if (instance == null) instance = new Pool();

        return instance;
    }
    
}