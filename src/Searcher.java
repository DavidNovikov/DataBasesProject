import java.sql.*;
import java.util.*;

public class Searcher {
    public static int pickCreator(String type, Connection conn, Scanner scan) {
        int creatorID = -1;
        while (creatorID == -1) {
            PreparedStatement stmt = null;
            ResultSet rSet = null;

            System.out.println("Please enter the " + type + "'s name");
            String cName = scan.nextLine();

            try {
                stmt = conn.prepareStatement("SELECT * FROM " + type + " where ar_name = ?;");
                stmt.setString(1, cName);

                rSet = stmt.executeQuery();
                System.out.println("Name, dob, creator id");
                while (rSet.next() && creatorID == -1) {
                    String name = rSet.getString(Util.creatorNameString(type));
                    String dob = rSet.getString("date_of_birth");
                    creatorID = rSet.getInt("creator_id");
                    System.out.println(name + ", " + dob + ", " + creatorID);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                Util.closeStmt(stmt);
                Util.closeRSet(rSet);
            }

            if (creatorID == -1) {
                System.out.println(cName + " not found");
            }
        }
        return creatorID;
    }
}
