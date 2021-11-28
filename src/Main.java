import javax.swing.*;
import java.io.File;
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


        Properties props = new Properties();
        props.put("user", user);
        props.put("password", password);

        try {
            Connection dbConn = JDBCService.connect("jdbc:oracle:thin:@" + ip + "/" + dbname, props);
            Connection fileConn = JDBCService.connect("jdbc:relique:csv:data", new Properties());

            int count = JDBCService.copyData(dbConn, fileConn);
            System.out.println(count);

            JDBCService.disconnect(dbConn);
            JDBCService.disconnect(fileConn);

        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            System.exit(-1);
        }
    }
}

