package global;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;

public class PgConnection {
    private Connection connection;
    private ResultSet lastResultSet;
    private boolean available;

    public PgConnection() {
        try {
            this.available = true;
            connection = connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection connect() throws SQLException {
        Connection conn;
        PropertiesHandler props = new PropertiesHandler("connection");

        String url = MessageFormat.format("jdbc:postgresql://{0}/{1}"
            , props.get("host"), props.get("name"));
        String user = props.get("user");
        String passwd = props.get("passwd");

        conn = DriverManager.getConnection(url, user, passwd);
        return conn;
    }

    public void executeQuery(String query) throws SQLException {
        String queryType = query.split(" ")[0].toUpperCase();
        Statement state = connection.createStatement(
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY
        )
        ;
        if (queryType.equals("SELECT")) {
            ResultSet result = state.executeQuery(query);
            this.lastResultSet = result;
        }
        else state.execute(query);

    }

    public ResultSet getLastRS() {
        try {
            return this.lastResultSet;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean getAvailability() {
        return this.available;
    }

    public void setAvailability(boolean value) {
        this.available = value;
    }

    public synchronized void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

