import java.sql.*;

public class TelephoneDirectoryDB {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/telephonedb";
        String user = "root";
        String password = "**password**"; // Palitan password

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();

            stmt.executeUpdate("DELETE FROM directory");

            stmt.executeUpdate("INSERT INTO directory VALUES ('Buan', 'Jana Sophia', 'R.', '123 Rizal Ave, Manila', '09171234567')");
            stmt.executeUpdate("INSERT INTO directory VALUES ('Calaquian', 'Louise', 'D.', '456 Katipunan, Quezon City', '09281234567')");
            stmt.executeUpdate("INSERT INTO directory VALUES ('Eval', 'Bradley', 'F.', '789 Taft Ave, Pasay', '09391234567')");
            stmt.executeUpdate("INSERT INTO directory VALUES ('Gusto', 'Ariane Mae', 'M.', '22 C Sto. Nino', '09918739234')");

            System.out.println("Records inserted successfully!");

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("SQL Error during setup or insertion:");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found.");
            e.printStackTrace();
        }
    }
}
