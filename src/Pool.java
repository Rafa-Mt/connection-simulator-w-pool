import java.util.ArrayList;

public enum Pool {
    
    INSTANCE(); 
 
    protected ArrayList<ConnectionThread> connPool;
    private PropertiesHandler props;
    public int size;

    private Pool() {
        props = new PropertiesHandler("pool");
        size = Integer.parseInt(props.Get("size"));
        for (int thread : Utils.Range(1, size)) {
            connPool.add(new ConnectionThread(thread, false));
        }
    }
 
    public Pool getInstance() {
        return INSTANCE;
    }
    
}