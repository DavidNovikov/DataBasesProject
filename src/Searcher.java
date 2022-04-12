import java.sql.*;
import java.util.*;

public class Searcher {
    public static int pickCreator(String type, Connection conn, Scanner scan) throws Exception {
        int creatorID = -1;
        PreparedStatement stmt = null;
        ResultSet rSet = null;
        try {
            System.out.println("Please enter the " + type + "'s name");
            String cName = scan.nextLine();

            stmt = conn.prepareStatement(Maps.creatorSearcherMap.get(type));
            stmt.setString(1, cName);

            rSet = stmt.executeQuery();
            System.out.println("Name, dob, creator id");
            while (rSet.next() && creatorID == -1) {
                String name = rSet.getString(Maps.creatorNameMap.get(type));
                String dob = rSet.getString("date_of_birth");
                creatorID = rSet.getInt("creator_id");
                System.out.println(name + ", " + dob + ", " + creatorID);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
            Util.closeRSet(rSet);
        }
        return creatorID;
    }

    public static int pickItem(String type, Connection conn, Scanner scan) throws Exception {
        int ItemID = -1;
        PreparedStatement stmt = null;
        ResultSet rSet = null;
        try {
            System.out.println("Please enter the " + type + "'s name");
            String itemName = scan.nextLine();
            stmt = conn.prepareStatement(Maps.itemSearcherMap.get(type));
            stmt.setString(1, itemName);


            try {
                if(type.equals("physicalbook")) {
                    type = "physical_book";
                }
                stmt = conn.prepareStatement(Maps.itemSearcherMap.get(type));
                stmt.setString(1, itemName);
                

                rSet = stmt.executeQuery();
                ArrayList<Integer> potentialIDs = Util.searchPrint(rSet, "Item_ID");
                ItemID = Util.itemListPick(potentialIDs, scan);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                Util.closeStmt(stmt);
                Util.closeRSet(rSet);
            }
            rSet = stmt.executeQuery();
            ArrayList<Integer> potentialIDs = Util.searchPrint(rSet, "Item_ID");


            System.out.println(
                    "What entry would you like to select? Enter the number before the entry (1, 2, 3... etc): ");
            int entry = Integer.parseInt(scan.nextLine());
            ItemID = potentialIDs.get(entry - 1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
            Util.closeRSet(rSet);
        }
        return ItemID;
    }

    public static int pickPerson(Connection conn, Scanner scan) throws Exception {
        int CardID = -1;
        PreparedStatement stmt = null;
        ResultSet rSet = null;
        try {
            System.out.println("Please enter the Person's email");
            String email = scan.nextLine();
            String sql = Maps.searchPersonString;
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            // stmt.setString(2, type);

            rSet = stmt.executeQuery();
            ResultSetMetaData rSetmd = rSet.getMetaData();
            int columnCount = rSetmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String value = rSetmd.getColumnName(i);
                System.out.print(value);
                if (i < columnCount)
                    System.out.print(",  ");
            }
            System.out.print("\n");
            while (rSet.next() && CardID == -1) {
                for (int i = 1; i <= columnCount; i++) {
                    String columnValue = rSet.getString(i);
                    System.out.print(columnValue);
                    CardID = rSet.getInt("CardID");
                    if (i < columnCount)
                        System.out.print(",  ");
                }
                System.out.print("\n");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
            Util.closeRSet(rSet);
        }
        return CardID;
    }
}
