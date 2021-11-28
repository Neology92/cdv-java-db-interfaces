import java.sql.*;
import java.util.Properties;

public class JDBCService {
    public JDBCService() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Class.forName("org.relique.jdbc.csv.CsvDriver");
        } catch (ClassNotFoundException ex) {
            System.out.println("Unable to load the class. Terminating the program");
            System.exit(-1);
        }
    }

    public static Connection connect(String url, Properties properties) throws SQLException {
        Connection connection = DriverManager.getConnection(url, properties);
        System.out.println("Connection ( " + url + " ) established");
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
        ResultSet rs = getEmployees(conn);

        while (rs.next()) {
            String id_prac = rs.getString("id_prac");
            String nazwisko = rs.getString("nazwisko");
            String placa_pod = rs.getString("placa_pod");

            System.out.println(id_prac + ", " + nazwisko + ", " + placa_pod);
        }

        rs = null;
    }


    public static void showEmployeesBetween(Connection conn, int startPosition, int endPosition) throws SQLException {
        if(endPosition >= startPosition) {
            String sqlText = """
                SELECT
                    nazwisko, placa_pod, placa_rank
                FROM (
                    SELECT
                        nazwisko,
                        placa_pod,
                        rank() OVER ( ORDER BY placa_pod DESC ) AS placa_rank
                    FROM pracownicy
                )
                WHERE placa_rank >= ? AND placa_rank <= ?
            """;

            PreparedStatement statement = conn.prepareStatement(sqlText);
            statement.setInt(1, startPosition);
            statement.setInt(2, endPosition);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String nazwisko = rs.getString("nazwisko");
                String placaPod = rs.getString("placa_pod");
                String rankPosition =   rs.getString("placa_rank");

                System.out.println(rankPosition + ": " + nazwisko + ", " + placaPod);
            }

            rs = null;
            sqlText = null;
            statement.close();
        } else {
            System.out.println("Error: Incorrect range!");
            return;
        }
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

    public static int copyData (Connection dbConn, Connection fileConn) throws SQLException {
        ResultSet csvRs = getEmployees(fileConn);

        String insertQuery = "INSERT INTO pracownicy (id_prac, imie, nazwisko, placa_pod) VALUES (?, ?, ?, ?)";
        dbConn.setAutoCommit(false);
        PreparedStatement insertPs = dbConn.prepareStatement(insertQuery);

        while (csvRs.next()) {
            int id_prac = csvRs.getInt("id");
            String nazwisko = csvRs.getString("nazwisko");
            String imie = csvRs.getString("imie");
            double placa_pod = csvRs.getDouble("placa");

            System.out.println("Row: " + id_prac + ", " + nazwisko + ", " + placa_pod);

            insertPs.setInt(1, id_prac);
            insertPs.setString(2, imie);
            insertPs.setString(3, nazwisko);
            insertPs.setDouble(4, placa_pod);
            insertPs.addBatch();
            System.out.println("Record added to batch!");
        }
        int[] counts = insertPs.executeBatch();
        dbConn.commit();
        dbConn.setAutoCommit(true);

        return counts.length;
    }

    private static ResultSet getEmployees(Connection conn) throws SQLException {
        String sqlText = "SELECT * FROM pracownicy ORDER BY nazwisko";
        Statement statement = conn.createStatement();
        return executeQuery(statement, sqlText);
    }
}