import java.sql.*;
import java.util.Properties;

public class JDBCService {
    public JDBCService() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException ex) {
            System.out.println("Unable to load the class. Terminating the program");
            System.exit(-1);
        }
    }

    public static Connection connect(String url, Properties properties) throws SQLException {
        Connection connection = DriverManager.getConnection(url, properties);
        System.out.println("Connection established");
        return connection;
    }

    public static void disconnect(Connection connection) throws SQLException {
        connection.close();
        System.out.println("Disconnected success");
    }

    public static ResultSet executeQuery(Statement statement, String sql) throws SQLException {
        return statement.executeQuery(sql);
    }


//    public static int changeSalary(Connection conn, long employeeId, double newSalary) throws SQLException {
//        String sql = "UPDATE pracownicy" +
//                "SET placa_pod = ?" +
//                "WHERE id_"
//        PreparedStatement statement = conn.prepareStatement()
//    }
}