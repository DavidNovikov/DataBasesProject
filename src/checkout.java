import java.sql.*;
import java.util.*;

public class checkout {
    public static void checkoutItem(String type, Connection conn, Scanner scan) throws Exception {
        PreparedStatement stmt = null;
        int itemID = Searcher.pickItem(type, conn, scan);
        int cardID = Searcher.pickPerson(conn, scan);
        String formattedCurrentDate = Util.getDate(scan, "Checkout Date");
        String formattedDueDate = Util.getDate(scan, "Due Date");
        
        try {
            String sql = Maps.checkoutItemString;
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
    
    //TODO: update
    //TODO: search
    //TODO: delete
}