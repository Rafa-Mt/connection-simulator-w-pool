package global;

import java.sql.SQLException;

import pool.PoolManager;

public class ConnectionThread extends Thread {
    private PgConnection connection;

    public int id;
    public String state;
    private int attempts;

    public ConnectionThread(int id) {
        super();
        this.id = id;
        this.state = "Running";
        PropertiesHandler configProps = new PropertiesHandler("config");
        this.attempts = Integer.parseInt(configProps.get("connection-attempts"));
    }

    @Override
    public void run() {
        PropertiesHandler props = new PropertiesHandler("querys");
        PoolManager manager = new PoolManager(id);

        String query = props.get("Select-From", new String[] { "nombre, hc, profesor", "materia" });
        try {
            for (int attempt : Controller.range(1, this.attempts)) {
                connection = manager.getConnection();
                if (connection != null) break;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (attempt == this.attempts) throw new SQLException("Failed to get connection");
            }
            connection.executeQuery(query);
            this.state = "Done";
            manager.releaseConnection(connection);
        } catch (SQLException e) {
            this.state = "Error";
            this.interrupt();
        }
    }

    public String getStatus() {
        return String.format("state", this.id, this.state);
    }
}