import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        // Load config
        Properties config = new Properties();
        try {
            config = BaseConfig.getPropValues();
        } catch (IOException ex) {
            System.out.println("Error loading config: " + ex.getMessage());
            System.exit(-1);
        }
        String user = config.getProperty("user");
        String password = config.getProperty("password");
        String dbname = config.getProperty("dbname");
        String ip = config.getProperty("ip");
        // - - - - -

        Connection conn = null;

        Properties props = new Properties();
        props.put("user", user);
        props.put("password", password);

        try {
            conn = JDBCService.connect("jdbc:oracle:thin:@" + ip + "/" + dbname, props);

            int modifiedCount = JDBCService.changeSalary(conn, 100, 500);
            System.out.println(modifiedCount + " records udpated");

            JDBCService.disconnect(conn);

        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            System.exit(-1);
        }
    }
}
