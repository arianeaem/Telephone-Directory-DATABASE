import java.sql.*;
import java.util.Scanner;

public class TelephoneDirectoryDB {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/telephonedb";
        String user = "root";
        String password = ""; // Update to your actual MySQL password

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            Scanner scanner = new Scanner(System.in);

            // Display current directory
            displayDirectory(conn);

            System.out.println("\nEnter update instruction (I for insert, D for delete):");
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equals("I")) {
                // INSERT
                System.out.print("Last Name:    ");
                String lastName = scanner.nextLine().trim();
                System.out.print("First Name:   ");
                String firstName = scanner.nextLine().trim();
                System.out.print("Middle Initial:      ");
                String middle = scanner.nextLine().trim();
                System.out.print("Address:     ");
                String address = scanner.nextLine().trim();
                System.out.print("Phone Number: ");
                String phoneNumber = scanner.nextLine().trim();

                try {
                    String insertSQL = "INSERT INTO directory VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement pstmt = conn.prepareStatement(insertSQL);
                    pstmt.setString(1, lastName);
                    pstmt.setString(2, firstName);
                    pstmt.setString(3, middle);
                    pstmt.setString(4, address);
                    pstmt.setString(5, phoneNumber);

                    int rowsInserted = pstmt.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Record inserted successfully!");
                    } else {
                        System.out.println("Insert failed.");
                    }

                    pstmt.close();
                } catch (SQLException ex) {
                    System.out.println("Error inserting record:");
                    ex.printStackTrace();
                }

            } else if (input.equals("D")) {
                // DELETE
                System.out.print("Last Name:    ");
                String lastName = scanner.nextLine().trim();
                System.out.print("First Name:   ");
                String firstName = scanner.nextLine().trim();
                System.out.print("Middle Initial:      ");
                String middle = scanner.nextLine().trim();
                System.out.print("Phone Number (optional, press enter to skip): ");
                String phoneNumber = scanner.nextLine().trim();

                String deleteSQL;
                PreparedStatement pstmt;

                if (!phoneNumber.isEmpty()) {
                    deleteSQL = "DELETE FROM directory WHERE lastName = ? AND firstName = ? AND middle = ? AND phoneNumber = ?";
                    pstmt = conn.prepareStatement(deleteSQL);
                    pstmt.setString(1, lastName);
                    pstmt.setString(2, firstName);
                    pstmt.setString(3, middle);
                    pstmt.setString(4, phoneNumber);
                } else {
                    deleteSQL = "DELETE FROM directory WHERE lastName = ? AND firstName = ? AND middle = ?";
                    pstmt = conn.prepareStatement(deleteSQL);
                    pstmt.setString(1, lastName);
                    pstmt.setString(2, firstName);
                    pstmt.setString(3, middle);
                }

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Record deleted successfully!");
                } else {
                    System.out.println("No matching record found.");
                }

                pstmt.close();
            } else {
                System.out.println("Invalid operation. Please enter only 'I' or 'D'.");
            }

            // Display updated directory
            displayDirectory(conn);

            conn.close();
            scanner.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void displayDirectory(Connection conn) throws SQLException {
        System.out.println("\nTelephone Directory:");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM directory ORDER BY lastName, firstName");

        boolean hasRecords = false;
        while (rs.next()) {
            hasRecords = true;
            System.out.println(
                    rs.getString("lastName") + ", " +
                            rs.getString("firstName") + " " +
                            rs.getString("middle") + " | " +
                            rs.getString("address") + " | " +
                            rs.getString("phoneNumber"));
        }

        if (!hasRecords) {
            System.out.println("No records found.");
        }

        rs.close();
        stmt.close(); // Keep the connection open
    }
}
