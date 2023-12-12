package connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleConnectionManager {

    private static final String TRUST_STORE_PATH = "../Wallet_BankingSystem/truststore.jks";
    private static final String KEY_STORE_PATH = "../Wallet_BankingSystem/keystore.jks";
    private static final String KEY_STORE_PASSWORD = "DruzynaDamyRade1";
    private static final String JDBC_URL = "jdbc:oracle:thin:@(description= (retry_count=20)(retry_delay=3)(address=(protocol=tcps)(port=1522)(host=adb.eu-frankfurt-1.oraclecloud.com))(connect_data=(service_name=g35f0685faf01cb_bankingsystem_medium.adb.oraclecloud.com))(security=(ssl_server_dn_match=yes)))";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "DruzynaDamyRade1";

    static {
        System.setProperty("oracle.net.ssl_server_dn_match", "true");
        System.setProperty("javax.net.ssl.trustStore", getResourcePath(TRUST_STORE_PATH));
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        System.setProperty("javax.net.ssl.keyStore", getResourcePath(KEY_STORE_PATH));
        System.setProperty("javax.net.ssl.keyStoreType", "JKS");
        System.setProperty("javax.net.ssl.keyStorePassword", KEY_STORE_PASSWORD);
    }

    private static String getResourcePath(String resource) {
        return OracleConnectionManager.class.getResource(resource).getPath();
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }


}
