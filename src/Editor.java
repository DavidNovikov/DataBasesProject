import java.sql.*;
import java.util.*;

public class Editor {
    public static void editCreator(String type, Connection conn, Scanner scan) {
        int creatorID = Searcher.pickCreator(type, conn, scan);
        PreparedStatement stmt = null;
        String update = "UPDATE ? SET ? = ? WHERE creator_id = ?;";
        try {

            int editing = whatToEditCreator(scan);

            stmt = conn.prepareStatement(update);
            stmt.setString(1, type);

            switch (editing) {
                case 1:
                    System.out.println("enter the new value");
                    String newName = scan.nextLine();
                    stmt.setString(2, "ar_name");
                    stmt.setString(3, newName);
                    break;
                case 2:
                    String dateOfBirth = Util.getDate(scan, "date of birth");
                    stmt.setString(2, "Date_Of_Birth");
                    stmt.setString(3, dateOfBirth);
                    break;
            }
            stmt.setInt(4, creatorID);
            stmt.executeUpdate();
        } catch (SQLException | NumberFormatException e) {
            System.out.println(e.getMessage());
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static int whatToEditCreator(Scanner scan) {
        int option = 0;
        while (option == 0) {
            System.out.println("Please enter 1 to edit the artist name, 2 to edit the artist dob");
            option = Integer.valueOf(scan.nextLine());
            if (!(option == 1 || option == 2))
                option = 0;
        }
        return option;
    }
}
