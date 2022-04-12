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
    
    public static void deleteChapter(String type, Connection conn, Scanner scan) throws Exception {
    	PreparedStatement stmt = null;
    	try {
    		System.out.println("Please choose the book you would like to remove a chapter from: ");
    		int ItemID = Searcher.pickChapter(type, conn, scan);
    		System.out.println("What is the name of the chapter that you would like to remove?");
    		String chapterName = scan.nextLine();
    		stmt = conn.prepareStatement(Maps.chapterDeleterMap.get(type));
    		stmt.setString(2, chapterName);
    		stmt.setInt(1, ItemID);
    		stmt.executeUpdate();
    		
    	} catch(Exception e) {
    		System.out.println(e.getMessage());
            throw e;
    	}finally {
            Util.closeStmt(stmt);
        }
    }
}
