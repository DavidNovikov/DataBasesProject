import java.sql.*;
import java.util.*;

public class Editor {
    public static void editCreator(String type, Connection conn, Scanner scan) {
        int creatorID = Searcher.pickCreator(type, conn, scan);
        PreparedStatement stmt = null;
        String update = "UPDATE ? SET ? = ? WHERE creator_id = ?;";
        try {

            int editing = whatToEditCreator(scan);

            System.out.println("enter the new value");
            String newVal = scan.nextLine();

            switch (editing) {
                case 1:
                    break;
                case 2:
                    break;
            }

            stmt = conn.prepareStatement(update);

            stmt.setInt(1, itemID);
            stmt.setString(2, title);
            stmt.setInt(3, year);
            stmt.setString(4, Util.getTypeForInsert(item));
            stmt.setBoolean(5, Util.getStatus(scan));

            stmt.executeUpdate();
        } catch (SQLException | NumberFormatException e) {
            System.out.println(e.getMessage());
            throw e;
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
