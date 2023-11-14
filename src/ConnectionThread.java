import java.sql.SQLException;

public class ConnectionThread extends Thread {
    private PgConnection connection;

    public int id;
    public String state;

    public ConnectionThread(int id) {
        super();
        this.id = id;
        this.state = "Running";
    }

    @Override
    public void run() {
        PropertiesHandler props = new PropertiesHandler("querys");
        
        String query = props.get("Select-From", new String[] { "nombre, hc, profesor", "materia" });
        try {
            connection = new PgConnection();
            connection.executeQuery(query);
            this.state = "Done";
        } catch (SQLException e) {
            this.state = "Error";
            this.interrupt();
        }
    }

    public String getStatus() {
        return String.format("state", this.id, this.state);
    }
}