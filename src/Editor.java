import java.sql.*;
import java.util.*;

public class Editor {
    public static void editCreator(String type, Connection conn, Scanner scan) {
        int creatorID = Searcher.pickCreator(type, conn, scan);
        PreparedStatement stmt = null;
        try {
            int editing = whatToEditCreator(scan);

            stmt = conn.prepareStatement(Maps.creatorEditMap.get(type)[editing - 1]);

            switch (editing) {
                case 1:
                    System.out.println("enter the new value");
                    String newName = scan.nextLine();
                    stmt.setString(1, newName);
                    break;
                case 2:
                    String dateOfBirth = Util.getDate(scan, "date of birth");
                    stmt.setString(1, dateOfBirth);
                    break;
            }
            stmt.setInt(2, creatorID);
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
