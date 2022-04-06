import java.sql.*;
import java.util.*;

public class Deleter {
    public static void deleteCreator(String type, Connection conn, Scanner scan) {
        int cID = Searcher.pickCreator(type, conn, scan);
        deleteCreatorSuper(cID, type, conn, scan);
        deleteCreatorBase(cID, type, conn, scan);
    }

    private static void deleteCreatorBase(int cID, String type, Connection conn, Scanner scan) {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(Maps.creatorDeleteMap.get("creator"));
            stmt.setInt(1, cID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static void deleteCreatorSuper(int cID, String type, Connection conn, Scanner scan) {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(Maps.creatorDeleteMap.get(type));
            stmt.setInt(1, cID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            Util.closeStmt(stmt);
        }
    }
}
