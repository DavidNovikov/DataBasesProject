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
                    String name = rSet.getString(Maps.creatorNameMap.get(type));
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
                stmt = conn.prepareStatement(Maps.itemSearcherMap.get(type));
                stmt.setString(1, itemName);

                rSet = stmt.executeQuery();
                ArrayList<Integer> potentialIDs = Util.searchPrint(rSet, "Item_ID");

                System.out.println(
                        "What entry would you like to select? enter the number before the entry (1, 2, 3... etc): ");
                int entry = Integer.parseInt(scan.nextLine());
                ItemID = potentialIDs.get(entry - 1);
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

    public static Relationship pickRelationship(String type, Connection conn, Scanner scan) {
        Relationship relationship = new Relationship();
        boolean found = false;
        while (!found) {
            String itemType = getRelationshipItemType(type, scan);
            String creatorType = getRelationshipCreatorType(type);

            int itemID = Searcher.pickItem(itemType, conn, scan);
            int creatorID = Searcher.pickCreator(creatorType, conn, scan);

            relationship.setCreatorID(creatorID);
            relationship.setItemID(itemID);

            PreparedStatement stmt = null;
            ResultSet rSet = null;
            try {
                stmt = conn.prepareStatement(Maps.relationshipSearcherMap.get(type));
                stmt.setInt(1, relationship.getCreatorID());
                stmt.setInt(2, relationship.getItemID());
                rSet = stmt.executeQuery();

                ArrayList<Integer> potentialIDs = Util.searchPrint(rSet, "Item_ID");
                if (potentialIDs.size() == 1) { // we have found the unique id
                    found = true;
                } else {
                    System.out.println("relationship not found try again");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                Util.closeStmt(stmt);
                Util.closeRSet(rSet);
            }
        }

        return relationship;
    }

    private static String getRelationshipCreatorType(String relationshipType) {
        return Maps.relationshipOptionMap.get(relationshipType)[0];
    }

    private static String getRelationshipItemType(String relationshipType, Scanner scan) {
        if (Maps.relationshipOptionMap.get(relationshipType).length > 2) {
            // there are multiple options for item type
            String[] optionArray = Maps.relationshipOptionMap.get(relationshipType);
            int option = 0;
            while (option == 0) {
                for (int i = 1; i < optionArray.length; i++) {
                    System.out.println("Enter " + i + " if the item type is " + optionArray[i]);
                }
                option = Integer.valueOf(scan.nextLine());
                if (!(option == 1 || option == 2)) {
                    option = 0;
                }
            }
            return Maps.relationshipOptionMap.get(relationshipType)[option];
        } else {
            // there is just one item type
            return Maps.relationshipOptionMap.get(relationshipType)[1];
        }
    }
}
