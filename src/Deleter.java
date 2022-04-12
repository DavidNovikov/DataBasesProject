import java.sql.*;
import java.util.*;

public class Deleter {
    public static void deleteCreator(String type, Connection conn, Scanner scan) throws Exception {
        try {
            int cID = Searcher.pickCreator(type, conn, scan);
            deleteCreatorSuper(cID, type, conn, scan);
            deleteCreatorBase(cID, type, conn, scan);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    private static void deleteCreatorBase(int cID, String type, Connection conn, Scanner scan) throws Exception {
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

    private static void deleteCreatorSuper(int cID, String type, Connection conn, Scanner scan) throws Exception {
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
    
    public static void deleteGenre(Connection conn, Scanner scan) throws Exception {
    	System.out.println("Select an entry to delete a genre from");
    	int itemID = Searcher.pickGenre(conn, scan);
    	PreparedStatement stmt = null;
    	try {
	        System.out.println("What is the genre you wish to delete?");
	        String newGenre = scan.nextLine();
	        stmt = conn.prepareStatement(Maps.genreDeleterMap.get("item"));
	        stmt.setInt(1, itemID);
	        stmt.setString(2, newGenre);
	        stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }
}
