import java.sql.*;
import java.util.*;

public class checkout {
	private static int itemID;
    public static void addCheckoutItem(String type, Connection conn, Scanner scan) throws Exception {
    	ArrayList<String> alreadyCheckedOut = searchItemsCheckedOut(type, conn, scan);
    	if (alreadyCheckedOut.isEmpty()) {
    		System.out.println("Your item has not been checked out yet.");
    		PreparedStatement stmt = null;
    		int cardID = Searcher.pickPerson(conn, scan);
    		String formattedCurrentDate = Util.getDate(scan, "Checkout Date");
    		String formattedDueDate = Util.getDate(scan, "Due Date");
        
    		try {
    			String sql = Maps.addItemCheckoutString;
    			stmt = conn.prepareStatement(sql);
    			stmt.setInt(1, itemID);
    			stmt.setInt(2, cardID);
    			stmt.setString(3, formattedDueDate);
    			stmt.setNull(4, Types.NULL);
    			stmt.setString(5, formattedCurrentDate);
    			stmt.executeUpdate();
            
            
    		} catch (SQLException | NumberFormatException e) {
    			System.out.println(e.getMessage());
    			throw e;
    		} finally {
    			Util.closeStmt(stmt);
    		}
    		System.out.println("Your item has been checked out. Your due date is: " + formattedDueDate);
    		return;
    	}
    	else {
    		System.out.println("Your item has already been checked out. These are the due dates of all copies of the item.");
    		while (!alreadyCheckedOut.isEmpty()) {
    			System.out.println(alreadyCheckedOut.remove(0));
    		}
    	}
    }
        
    public static ArrayList<String> searchItemsCheckedOut(String type, Connection conn, Scanner scan) throws Exception {
    	ArrayList<String> allDueDates = new ArrayList<String>();
    	
    	PreparedStatement stmt = null;
    	ResultSet rSet = null;
    	itemID = Searcher.pickItem(type, conn, scan);
    	try {
    		String sql = Maps.searchItemCheckoutsString;
    		stmt = conn.prepareStatement(sql);
    		stmt.setInt(1, itemID);
    		
    		rSet = stmt.executeQuery();
    		while (rSet.next()) {
    			String dueDate = rSet.getString("Due_Date");
    			allDueDates.add(dueDate);
    			itemID = rSet.getInt("ItemID");
    		}
    	} catch (SQLException | NumberFormatException e) {
    		System.out.println(e.getMessage());
    		throw e;
    	} finally {
    		Util.closeRSet(rSet);
            Util.closeStmt(stmt);            
        }
    	
    	return allDueDates;
    } 
    
    public static void deleteItemCheckedOut(Connection conn, Scanner scan) throws Exception {
    	System.out.println("Enter the type of record that was checked out, to be deleted. (Album, Track, Interview, Movie, Audiobook, or PhysicalBook): ");
        String type = scan.nextLine().toLowerCase();
        int itemID = -1;
    	switch (type) {
        	case "album":
        	case "track":
        	case "interview":
        	case "movie":
        	case "audiobook":
        	case "physicalbook":
        		itemID = Searcher.pickItem(type,conn, scan);
            break;
        	default:
        		// print invalid
        		System.out.println("In default");
        		System.out.println(type + " is an Invalid input");
        }
    	String checkoutDate = Util.getDate(scan, "Checkout Date");
    	System.out.println("Debug2");
    	PreparedStatement stmt = null;
    	try {
    		String sql = Maps.deleteItemCheckoutsString;
    		stmt = conn.prepareStatement(sql);
    		stmt.setInt(1, itemID);
    		stmt.setString(2,checkoutDate);
    		stmt.executeUpdate();
    	} catch (SQLException | NumberFormatException e) {
    		System.out.println(e.getMessage());
    		throw e;
    	} finally {
            Util.closeStmt(stmt);            
        }  	
    }
    
    //TODO: update
    //TODO: delete
}