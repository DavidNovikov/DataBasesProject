import java.sql.*;
import java.util.*;

public class Deleter {

    public static void deleteCreator(Connection conn, Scanner scan) throws Exception {
        System.out.println(
                "Options:\n(actor)\n(director)\n(artist)\n(writer)\nPlease enter the creator you're deleting:");
        String creatorType = scan.nextLine().toLowerCase();
        try {
            switch (creatorType) {
                case "actor":
                case "director":
                case "artist":
                case "writer":
                    deleteCreator(creatorType, conn, scan);
                    break;
                default:
                    System.err.println(creatorType + " isn't a valid creator type");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    private static void deleteCreator(String type, Connection conn, Scanner scan) throws Exception {
        try {
            int cID = Searcher.pickCreator(type, conn, scan);
            deleteCreatorRelationships(cID, type, conn);
            deleteCreatorSuper(cID, type, conn);
            deleteCreatorBase(cID, type, conn);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    private static void deleteCreatorBase(int cID, String type, Connection conn) throws Exception {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(Maps.creatorDeleteMap.get("creator"));
            stmt.setInt(1, cID);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static void deleteCreatorSuper(int cID, String type, Connection conn) throws Exception {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(Maps.creatorDeleteMap.get(type));
            stmt.setInt(1, cID);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static void deleteCreatorRelationships(int cID, String type, Connection conn) throws Exception {
        PreparedStatement stmt = null;
        try {
            String[] vals = Maps.creatorDeleteRelationshipMap.get(type);
            for (String val : vals) {
                stmt = conn.prepareStatement(val);
                stmt.setInt(1, cID);
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    public static void deleteRelationship(Connection conn, Scanner scan) throws Exception {

        System.out.println(
                "Options:\n(stars)\n(writes)\n(interviewed)\n(performs)\n(directs)\nPlease enter the relationship you're deleting:");
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
            System.out.println(e.getMessage());
            throw e;
        }
    }

    private static void deleteRelationship(String type, Connection conn, Scanner scan) throws Exception {
        Relationship rel = Searcher.pickRelationship(type, conn, scan);
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(Maps.relationshipDeleterMap.get(type));
            stmt.setInt(1, rel.getCreatorID());
            stmt.setInt(2, rel.getItemID());
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    public static void deletePerson(Connection conn, Scanner scan) throws Exception {
        PreparedStatement stmt = null;
        try {
            int cardID = Searcher.pickPerson(conn, scan);
            stmt = conn.prepareStatement(Maps.deletePersonString);
            stmt.setInt(1, cardID);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }
}
