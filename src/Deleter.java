import java.sql.*;
import java.util.*;

public class Deleter {
    public static void deleteCreator(String type, Connection conn, Scanner scan) throws Exception {
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
                "Enter the type of relationship you are deleting: (stars, writes, interviewed, performs, or directs)");
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
            System.out.println("Unable to delete relationship. Exception:" + e.getMessage());
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

    public static void deleteGenre(Connection conn, Scanner scan) throws Exception {
    	System.out.println("Select an entry to delete a genre from");
    	GenreIDPair genreID = Searcher.pickGenre(conn, scan);
    	int itemID = genreID.getItemID();
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
        
    public static void deleteItem(String type, Connection conn, Scanner scan) throws Exception {
        try {
            int itemID = Searcher.pickItem(type, conn, scan);
            
            //if the type is itemordered, get the corresponding item type for the itemID
            //and call deleteItemSuper on that
            if(type.equals("itemordered")){
                String databaseType = Util.getTypeColumnInItemFromItemID(itemID, conn);
                //convert the database type to the types we use for java program
                String javaType = Util.changeToJavaString(databaseType);
                //delete item super from that type
                deleteItemSuper(itemID, javaType, conn, scan);
            }else if(Util.itemIsInItemsOrdered(itemID, conn)){
                //check to see if the item is in ordered items. If so, make them delete it as an itemOrdered
                throw new Exception("This item was ordered. To delete it, delete it as an \"itemOrdered\"\n");
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
    private static void deleteItemSet(ArrayList<Integer> IDList, Connection conn, Scanner scan) throws Exception {
    	try {
    		for (int itemIDToDelete : IDList) {
    			String idType = Util.getTypeColumnInItemFromItemID(itemIDToDelete, conn);
    			String itemType = Util.changeToJavaString(idType);
    			deleteItemSuper(itemIDToDelete, itemType, conn, scan);
                deleteItemBase(itemIDToDelete, itemType, conn, scan);
        	}
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }
}
