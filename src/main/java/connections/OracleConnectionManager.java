package connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleConnectionManager {
    private static final String JDBC_URL = "jdbc:oracle:thin:@(description= (retry_count=20)(retry_delay=3)" +
            "(address=(protocol=tcps)(port=1522)(host=adb.eu-frankfurt-1.oraclecloud.com))" +
            "(connect_data=(service_name=g35f0685faf01cb_bankingsystem_medium.adb.oraclecloud.com))" +
            "(security=(ssl_server_dn_match=yes)))";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "DruzynaDamyRade1";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }
}
