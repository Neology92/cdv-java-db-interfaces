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


            String sqlText = "SELECT id_prac, nazwisko, placa_pod FROM pracownicy ORDER BY nazwisko";
            Statement statement = conn.createStatement();
            ResultSet rs = JDBCService.executeQuery(statement, sqlText);

            while (rs.next()) {
                String id_prac = rs.getString("id_prac");
                String nazwisko = rs.getString("nazwisko");
                String placa_pod = rs.getString("placa_pod");

                System.out.println(id_prac + ", " + nazwisko + ", " + placa_pod);
            }

            rs = null;
            sqlText = null;
            statement.close();
            JDBCService.disconnect(conn);

        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            System.exit(-1);
        }
    }
}
