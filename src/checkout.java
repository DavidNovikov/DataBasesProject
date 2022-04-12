import java.sql.*;
import java.util.*;

public class checkout {
    public static void addCheckoutItem(String type, Connection conn, Scanner scan) throws Exception {
    	ArrayList<String> alreadyCheckedOut = searchItemsCheckedOut(type, conn, scan);
    	if (alreadyCheckedOut.isEmpty()) {
    		PreparedStatement stmt = null;
    		int itemID = Searcher.pickItem(type, conn, scan);
    		int cardID = Searcher.pickPerson(conn, scan);
    		String formattedCurrentDate = Util.getDate(scan, "Checkout Date");
    		String formattedDueDate = Util.getDate(scan, "Due Date");
        
    		try {
    			String sql = Maps.addItemCheckoutString;
    			stmt = conn.prepareStatement(sql);
    			stmt.setInt(1, itemID);
    			stmt.setInt(2, cardID);
    			stmt.setString(3, formattedCurrentDate);
    			stmt.setNull(4, Types.NULL);
    			stmt.setString(5, formattedDueDate);
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
    	int itemID = Searcher.pickItem(type, conn, scan);
    	System.out.println("Debug");
    	try {
    		String sql = Maps.searchItemCheckoutsString;
    		stmt = conn.prepareStatement(sql);
    		stmt.setInt(1, itemID);
    		
    		rSet = stmt.executeQuery();
    		while (rSet.next() && itemID == -1) {
    			String dueDate = rSet.getString("Due_Date");
    			allDueDates.add(dueDate);
    			itemID = rSet.getInt("ItemID");
    		}
    	} catch (SQLException | NumberFormatException e) {
    		System.out.println(e.getMessage());
    		throw e;
    	} 	
    	
    	return allDueDates;
    }
    
    //TODO: update
    //TODO: search
    //TODO: delete
}