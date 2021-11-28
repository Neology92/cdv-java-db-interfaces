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

    public static void showEmployees(Connection conn) throws SQLException {
        String sqlText = "SELECT id_prac, nazwisko, placa_pod FROM pracownicy ORDER BY nazwisko";
        Statement statement = conn.createStatement();
        ResultSet rs = executeQuery(statement, sqlText);

        while (rs.next()) {
            String id_prac = rs.getString("id_prac");
            String nazwisko = rs.getString("nazwisko");
            String placa_pod = rs.getString("placa_pod");

            System.out.println(id_prac + ", " + nazwisko + ", " + placa_pod);
        }

        rs = null;
        sqlText = null;
        statement.close();
    }


    public static int changeSalary(Connection conn, long employeeId, double newSalary) throws SQLException {
        String sql = "UPDATE pracownicy" +
                " SET placa_pod = ?" +
                " WHERE id_prac = ?";

        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setDouble(1, newSalary);
        statement.setLong(2, employeeId);
        return statement.executeUpdate();
    }
}