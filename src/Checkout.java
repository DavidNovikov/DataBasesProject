import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;

public class Checkout {
	public static void checkoutItem(String type, Connection conn, Scanner scan) throws Exception {
		PreparedStatement stmt = null;
		int itemID = Searcher.pickItem(type, conn, scan);
		int cardID = Searcher.pickPerson(conn, scan);
		java.sql.Date currentDate = new java.sql.Date(new java.util.Date().getTime());
		LocalDate ld = currentDate.toLocalDate();
		LocalDate threeWeeksLater = ld.plusWeeks(3);
		java.sql.Date dueDate = java.sql.Date.valueOf(threeWeeksLater);
		
		try {
			String sql = "insert into item_checked_out values (?,?,?,?,?);";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, itemID);
			stmt.setInt(2, cardID);
			stmt.setDate(3, (java.sql.Date) currentDate);
			//Temporary check if this gives current date or not
			stmt.setNull(4, Types.NULL);
			//Temporary check if this gives current date or not
			stmt.setDate(5, (java.sql.Date) dueDate);
			stmt.executeUpdate();
            
			
        } catch (SQLException | NumberFormatException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
		System.out.println("Your item has been checked out. Your due date is: " + dueDate);
		return;
	}
}
