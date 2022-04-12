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

    public static void deleteItem(String type, Connection conn, Scanner scan) throws Exception {
        try {
            int itemID = Searcher.pickItem(type, conn, scan);
            //if the type is itemordered, get the corresponding item type for the itemID
            //and call deleteItemSuper on that
            if(type.equals("itemordered")){
                String databaseType = Util.getTypeColumnInItemFromItemID(itemID, conn);
                System.out.println("dbtype "+databaseType);
                //convert the database type to the types we use for java program
                String javaType = Util.changeToJavaString(databaseType);
                System.out.println("jtype "+javaType);
                //delete item super from that type
                deleteItemSuper(itemID, javaType, conn, scan);
            }
            deleteItemSuper(itemID, type, conn, scan);
            deleteItemBase(itemID, type, conn, scan);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    private static void deleteItemBase(int itemID, String type, Connection conn, Scanner scan) throws Exception {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(Maps.itemDeleteMap.get("item"));
            stmt.setInt(1, itemID);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static void deleteItemSuper(int itemID, String type, Connection conn, Scanner scan) throws Exception {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(Maps.itemDeleteMap.get(type));
            stmt.setInt(1, itemID);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }
}
