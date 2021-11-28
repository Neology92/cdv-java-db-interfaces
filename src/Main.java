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
            int modifiedCount = 0;
            conn = JDBCService.connect("jdbc:oracle:thin:@" + ip + "/" + dbname, props);

            JDBCService.showEmployees(conn);

            System.out.println("");
            modifiedCount = JDBCService.changeSalary(conn, 120, 4000);
            System.out.println(modifiedCount + " records udpated");

            modifiedCount = JDBCService.changeSalary(conn, 170, 3000);
            System.out.println(modifiedCount + " records udpated\n");

            JDBCService.showEmployees(conn);

            JDBCService.disconnect(conn);

        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            System.exit(-1);
        }
    }
}

