import java.sql.*;

public class TelephoneDirectoryDB {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/telephonedb";
        String user = "root";
        String password = "password mu"; // Palitan password

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();

            stmt.executeUpdate("DELETE FROM directory");

            stmt.executeUpdate("INSERT INTO directory VALUES ('Buan', 'Jana Sophia', 'R.', '123 Rizal Ave, Manila', '09171234567')");
            stmt.executeUpdate("INSERT INTO directory VALUES ('Calaquian', 'Louise', 'D.', '456 Katipunan, Quezon City', '09281234567')");
            stmt.executeUpdate("INSERT INTO directory VALUES ('Eval', 'Bradley', 'F.', '789 Taft Ave, Pasay', '09391234567')");
            stmt.executeUpdate("INSERT INTO directory VALUES ('Gusto', 'Ariane Mae', 'M.', '22 C Sto. Nino, Manila', '09918739234')");

            System.out.println("Records inserted successfully!");

            //Retrieve and Display Initial Directory
            ResultSet rs = stmt.executeQuery("SELECT * FROM directory ORDER BY lastName, firstName");
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            System.out.println("\nTelephone Directory:");
            while (rs.next()) {
                System.out.println("lastName:    " + rs.getString("lastName"));
                System.out.println("firstName:   " + rs.getString("firstName"));
                System.out.println("middle:      " + rs.getString("middle"));
                System.out.println("address:     " + rs.getString("address"));
                System.out.println("phoneNumber: " + rs.getString("phoneNumber"));
                System.out.println();
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL Error during setup or insertion:");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found.");
            e.printStackTrace();
        }

        /**
         * Handles user input for updating a telephone directory stored in a MySQL database.
         * 
         * @throws Exception if there is an error reading input or connecting to the database.
         * @see java.sql.Connection
         * @see java.sql.DriverManager
         * @see java.sql.Statement
         * @see java.sql.ResultSet
         * @see java.util.Scanner
         */
        try {
            java.util.Scanner scanner = new java.util.Scanner(System.in);
            System.out.println("Enter update instruction (I for insert, D for delete):");
            String input = scanner.nextLine();
            String[] parts = input.split(",");
            if (parts.length > 0) {
                String op = parts[0].trim().toUpperCase();
                if (op.equals("I")) {
                    // Prompt for each field individually
                    System.out.print("lastName:    ");
                    String lastName = scanner.nextLine().trim();
                    System.out.print("firstName:   ");
                    String firstName = scanner.nextLine().trim();
                    System.out.print("middle:     ");
                    String middle = scanner.nextLine().trim();
                    System.out.print("address:     ");
                    String address = scanner.nextLine().trim();
                    System.out.print("phoneNumber: ");
                    String phoneNumber = scanner.nextLine().trim();
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection conn = DriverManager.getConnection(url, user, password);
                        Statement stmt = conn.createStatement();
                        String sql = String.format(
                            "INSERT INTO directory VALUES ('%s', '%s', '%s', '%s', '%s')",
                            lastName, firstName, middle, address, phoneNumber
                        );
                        stmt.executeUpdate(sql);
                        System.out.println("Record inserted successfully!");
                        // Display updated directory
                        ResultSet rs = stmt.executeQuery("SELECT * FROM directory ORDER BY lastName, firstName");
                        System.out.println("\nUpdated Telephone Directory:");
                        while (rs.next()) {
                            System.out.println("lastName:    " + rs.getString("lastName"));
                            System.out.println("firstName:   " + rs.getString("firstName"));
                            System.out.println("middle:     " + rs.getString("middle"));
                            System.out.println("address:     " + rs.getString("address"));
                            System.out.println("phoneNumber: " + rs.getString("phoneNumber"));
                            System.out.println();
                        }
                        rs.close();
                        stmt.close();
                        conn.close();
                    } catch (Exception ex) {
                        System.out.println("Error during insertion:");
                        ex.printStackTrace();
                    }
                } else if (op.equals("D")) {
                    System.out.println("wla pang deletion");
                } else {
                    System.out.println("Invalid input format.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading input:");
            e.printStackTrace();
        }
    }
}
