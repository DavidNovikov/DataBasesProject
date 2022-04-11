import java.sql.*;
import java.util.*;

public class Searcher {
    public static int pickCreator(String type, Connection conn, Scanner scan) {
        int creatorID = -1;
        while (creatorID == -1) {
            PreparedStatement stmt = null;
            ResultSet rSet = null;

            System.out.println("Please enter the " + type + "'s name");
            String cName = scan.nextLine();

            try {
                stmt = conn.prepareStatement(Maps.creatorSearcherMap.get(type));
                stmt.setString(1, cName);

                rSet = stmt.executeQuery();
                System.out.println("Name, dob, creator id");
                while (rSet.next() && creatorID == -1) {
                    String name = rSet.getString(Util.creatorNameString(type));
                    String dob = rSet.getString("date_of_birth");
                    creatorID = rSet.getInt("creator_id");
                    System.out.println(name + ", " + dob + ", " + creatorID);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                Util.closeStmt(stmt);
                Util.closeRSet(rSet);
            }

            if (creatorID == -1) {
                System.out.println(cName + " not found");
            }
        }
        return creatorID;
    }

    public static int pickItem(String type, Connection conn, Scanner scan) {
        int ItemID = -1;
        while (ItemID == -1) {
            PreparedStatement stmt = null;
            ResultSet rSet = null;

            System.out.println("Please enter the " + type + "'s name");
            String itemName = scan.nextLine();

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

        }
        
        return ItemID;
    }

    public static int pickPerson(Connection conn, Scanner scan) {
        int CardID = -1;
        while (CardID == -1) {
            PreparedStatement stmt = null;
            ResultSet rSet = null;

            System.out.println("Please enter the Person's email");
            String email = scan.nextLine();

            try {
                
                String sql = "SELECT * FROM PERSON WHERE email = ?;";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, email);
                //stmt.setString(2, type);

                rSet = stmt.executeQuery();
                ResultSetMetaData rSetmd = rSet.getMetaData();
                int columnCount = rSetmd.getColumnCount();
                for (int i = 1; i <= columnCount; i++){
                    String value = rSetmd.getColumnName(i);
                    System.out.print(value);
                    if (i < columnCount) System.out.print(",  ");
                }
                System.out.print("\n");
                while (rSet.next() && CardID == -1) {
                    for (int i = 1; i <= columnCount; i++){
                        String columnValue = rSet.getString(i);
                        System.out.print(columnValue);
                        CardID = rSet.getInt("CardID");
                        if (i < columnCount) System.out.print(",  ");
                    }
                    System.out.print("\n");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                Util.closeStmt(stmt);
                Util.closeRSet(rSet);
            }

            if (CardID == -1) {
                System.out.println(email + " CardID not found");
            }
        }
        return CardID;
    }
}
