import java.sql.*;
import java.util.*;

public class Searcher {

    public static int pickCreator(Connection conn, Scanner scan) throws Exception {
        int creatorID = -1;
        boolean found = false;
        while (!found) {
            System.out.println(
                    "Options:\n(actor)\n(director)\n(artist)\n(writer)\n(q to quit)\nPlease enter the creator you're searching for:");
            String creatorType = scan.nextLine().toLowerCase();
            try {
                switch (creatorType) {
                    case "actor":
                    case "director":
                    case "artist":
                    case "writer":
                        creatorID = pickCreator(creatorType, conn, scan);
                        found = true;
                        break;
                    case "q":
                        throw new Exception("User quit during search");
                    default:
                        System.err.println(creatorType + " isn't a valid creator type");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw e;
            }
        }
        return creatorID;
    }

    public static int pickCreator(String type, Connection conn, Scanner scan) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rSet = null;
        int creatorID = -1;
        boolean found = false;
        while (!found) {
            try {
                System.out.println("Please enter the " + type + "'s name or q to quit");
                String cName = scan.nextLine();
                if (cName.toLowerCase().equals("q"))
                    throw new Exception("User quit during search");

                stmt = conn.prepareStatement(Maps.creatorSearcherMap.get(type));
                stmt.setString(1, cName);

                rSet = stmt.executeQuery();

                ArrayList<Integer> potentialIDs = Util.searchPrint(rSet, "creator_ID");
                creatorID = Util.itemListPick(potentialIDs, scan);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw e;
            } finally {
                Util.closeStmt(stmt);
                Util.closeRSet(rSet);
            }
        }
        return creatorID;
    }

    public static int pickItem(Connection conn, Scanner scan) throws Exception {
        int itemID = -1;
        boolean found = false;
        while (!found) {
            System.out.println(
                    "Options:\n(album)\n(track)\n(interview)\n(movie)\n(audiobook)\n(physicalbook)\n(audiobookchapter)\n(physicalbookchapter)\n(genre)\nPlease enter the item you're searching for:");
            String itemType = scan.nextLine().toLowerCase();
            try {
                switch (itemType) {
                    case "album":
                    case "track":
                    case "interview":
                    case "movie":
                    case "audiobook":
                    case "physicalbook":
                        itemID = pickItem(itemType, conn, scan);
                        found = true;
                        break;
                    default:
                        System.err.println(itemType + " isn't a valid item type");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw e;
            }
        }
        return itemID;
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

            rSet = stmt.executeQuery();
            ArrayList<Integer> potentialIDs = Util.searchPrint(rSet, "Item_ID");
            ItemID = Util.itemListPick(potentialIDs, scan);
        } catch (Exception e) {
            System.out.println(e.getMessage());
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

    public static int pickChapter(String type, Connection conn, Scanner scan) throws Exception {
        int ItemID = -1;
        PreparedStatement stmt = null;
        ResultSet rSet = null;
        String bookGrab = null;
        try {
            if (type.equals("audiobookchapter")) {
                bookGrab = "audiobook";
            } else if (type.equals("physicalbookchapter")) {
                bookGrab = "physicalbook";
            }
            System.out.println("Please select a " + bookGrab + " to search a chapter from: ");
            ItemID = pickItem(bookGrab, conn, scan);
            stmt = conn.prepareStatement(Maps.chapterSearcherMap.get(type));
            stmt.setInt(1, ItemID);
            rSet = stmt.executeQuery();
            Util.searchPrint(rSet, "BookID");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
            Util.closeRSet(rSet);
        }
        return ItemID;
    }

    public static int pickGenre(Connection conn, Scanner scan) throws Exception {
        ArrayList<Integer> genreList = new ArrayList<Integer>();
        PreparedStatement stmt = null;
        ResultSet rSet = null;
        int genreID = -1;
        boolean listFlag = true;

        System.out.println("Enter the name of the genre you would like to search for, or 1 to list all genres:");
        String genre = scan.nextLine();

        try {
            while (listFlag) {
                if (genre.equals("1")) {
                    stmt = conn.prepareStatement(Maps.genreSearcherMap.get("genres"));
                    rSet = stmt.executeQuery();
                    Util.searchPrintNoRet(rSet);
                    System.out.println(
                            "Enter the name of the genre you would like to search for, or 1 to list all genres:");
                    genre = scan.nextLine();
                } else {
                    stmt = conn.prepareStatement(Maps.genreSearcherMap.get("search"));
                    stmt.setString(1, genre);
                    rSet = stmt.executeQuery();
                    genreList = Util.searchPrint(rSet, "Item_ID");
                    if (genreList.size() != 0) {
                        System.out.println(
                                "What entry would you like to select? enter the number before the entry (1, 2, 3... etc): ");
                        int entry = Integer.parseInt(scan.nextLine());
                        genreID = genreList.get(entry - 1);
                        listFlag = false;

                    } else {
                        System.out.println("Query returned no entries");
                        System.out.println(
                                "Enter the name of the genre you would like to search for, or 1 to list all genres:");
                        genre = scan.nextLine();
                    }

                }

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
            Util.closeRSet(rSet);
        }
        return genreID;
    }

    public static Relationship pickRelationship(Connection conn, Scanner scan) throws Exception {
        Relationship relationship = null;
        boolean found = false;
        while (!found) {
            System.out.println(
                    "Options:\n(stars)\n(writes)\n(interviewed)\n(performs)\n(directs)\nPlease enter the relationship you're searching for:");
            String relationshipType = scan.nextLine().toLowerCase();
            try {
                switch (relationshipType) {
                    case "stars":
                    case "writes":
                    case "interviewed":
                    case "performs":
                    case "directs":
                        relationship = pickRelationship(relationshipType, conn, scan);
                        found = true;
                        break;
                    default:
                        System.err.println(relationshipType + " isn't a valid relationship type");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw e;
            }
        }
        return relationship;
    }

    public static Relationship pickRelationship(String type, Connection conn, Scanner scan) throws Exception {
        Relationship relationship = new Relationship();
        boolean found = false;
        while (!found) {
            String itemType = getRelationshipItemType(type, scan);
            String creatorType = getRelationshipCreatorType(type);

            int itemID = pickItem(itemType, conn, scan);
            int creatorID = pickCreator(creatorType, conn, scan);

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
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw e;
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

    private static String getRelationshipItemType(String relationshipType, Scanner scan) throws Exception {
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
