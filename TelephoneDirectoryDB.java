import java.sql.*;
import java.util.Scanner;

/**
 * TelephoneDirectoryDB manages a simple telephone directory database.
 * Allows users to insert and delete records via console input.
 *
 * @author BUAN, JANA SOPHIA R.
 * @author CALAQUIAN, LOUISE JAVIER D.
 * @author EVAL. BRADLEY JAMES F.
 * @author GUSTO, ARIANE MAE B.
 */
public class TelephoneDirectoryDB {
    /**
     * Main entry point. Connects to the database and handles user operations.
     *
     * @param args Command-line arguments (not used)
     * @throws SQLException if a database access error occurs
     * @throws ClassNotFoundException if the JDBC driver is not found
     */
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/telephonedb";
        String user = "root";
        String password = ""; // Update to your actual MySQL password

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            Scanner scanner = new Scanner(System.in);

            // Show current directory records
            displayDirectory(conn);

            System.out.println("\nEnter update instruction (I for insert, D for delete):");
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equals("I")) {
                // --- Insert Operation ---
                // Prompt user for new record details
                System.out.print("Last Name:        ");
                String lastName = scanner.nextLine().trim();
                System.out.print("First Name:       ");
                String firstName = scanner.nextLine().trim();
                System.out.print("Middle Initial:   ");
                String middle = scanner.nextLine().trim();
                System.out.print("Address:          ");
                String address = scanner.nextLine().trim();
                System.out.print("Phone Number:     ");
                String phoneNumber = scanner.nextLine().trim();

                // Insert the new record into the database
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
                // --- Delete Operation ---
                // Prompt user for record details to delete
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

                // Delete by full name and phone if provided, else by name only
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

            // Show updated directory records
            displayDirectory(conn);

            conn.close();
            scanner.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays all records in the telephone directory, formatted in columns.
     *
     * @param conn Active database connection
     * @throws SQLException if a database access error occurs
     */
    public static void displayDirectory(Connection conn) throws SQLException {
        System.out.println("\nTelephone Directory:");
        System.out.printf("%-28s %-32s %-15s\n", "Name", "Address", "Telephone");
        System.out.println("-------------------------------------------------------------------------------");
        boolean hasRecords = false;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM directory ORDER BY lastName, firstName");

        while (rs.next()) {
            hasRecords = true;
            String middle = rs.getString("middle");
            String middleFormatted = "";
            if (middle != null && !middle.trim().isEmpty()) {
                middleFormatted = " " + (middle.endsWith(".") ? middle : middle + ".");
            }
            String name = rs.getString("lastName") + ", " + rs.getString("firstName") + middleFormatted;
            System.out.printf("%-28s %-32s %-15s\n",
                name,
                rs.getString("address"),
                rs.getString("phoneNumber")
            );
        }

        if (!hasRecords) {
            System.out.println("No records found.");
        }

        rs.close();
        stmt.close(); // Keep the connection open
    }
}
