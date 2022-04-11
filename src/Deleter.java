import java.sql.*;
import java.util.*;

public class Deleter {
    public static void deleteCreator(String type, Connection conn, Scanner scan) {
        int cID = Searcher.pickCreator(type, conn, scan);
        deleteCreatorSuper(cID, type, conn, scan);
        deleteCreatorBase(cID, type, conn, scan);
    }

    public static void deleteRelationship(Connection conn, Scanner scan) {

        System.out.println("Do you know the creator ID and the item ID? y/n");
        char response = scan.nextLine().charAt(0);
        if (response != 'y') {
            System.out.println("Use the search function to find the creator and item IDs to establish a relationship.");
            return;
        }

        System.out.println(
                "Enter the type of relationship you are adding: (stars, writes, interviewed, performs, or directs)");
        String relationshipType = scan.nextLine().toLowerCase();

        try {

            switch (relationshipType) {
                case "stars":
                case "writes":
                case "interviewed":
                case "performs":
                case "directs":
                    deleteRelationship(relationshipType, conn, scan);
                    break;
                default:
                    System.err.println(relationshipType + " isn't a valid relationship type");
            }
        } catch (Exception e) {
            System.out.println("Unable to create relationship. Exception:" + e);
        }
    }

    private static void deleteRelationship(String type, Connection conn, Scanner scan) {
        Relationship rel = Searcher.pickRelationship(type, conn, scan);
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(Maps.relationshipDeleterMap.get(type));
            stmt.setInt(1, rel.getCreatorID());
            stmt.setInt(2, rel.getItemID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            Util.closeStmt(stmt);
        }
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

    public static void deletePerson(Connection conn, Scanner scan) {
        int cardID = Searcher.pickPerson(conn, scan);
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(Maps.deletePersonString);
            stmt.setInt(1, cardID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            Util.closeStmt(stmt);
        }
    }
}
