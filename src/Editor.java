import java.sql.*;
import java.util.*;

public class Editor {

    public static void editRelationship(Connection conn, Scanner scan) throws Exception {
        System.out.println(
                "Options:\n(stars)\n(writes)\n(interviewed)\n(performs)\n(directs)\nPlease enter the relationship you're editing:");
        String relationshipType = scan.nextLine().toLowerCase();
        try {
            switch (relationshipType) {
                case "stars":
                case "writes":
                case "interviewed":
                case "performs":
                case "directs":
                    editRelationship(relationshipType, conn, scan);
                    break;
                default:
                    System.err.println(relationshipType + " isn't a valid relationship type");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private static void editRelationship(String relationshipType, Connection conn, Scanner scan) throws Exception {
        // ask if item type is audiobook or album
        Relationship rel = Searcher.pickRelationship(relationshipType, conn, scan);
        PreparedStatement stmt = null;
        try {
            int editing = 0;
            switch (relationshipType) {
                case "stars":
                    editing = whatToEditRelationshipStars(scan);
                    break;
                case "writes":
                case "interviewed":
                case "performs":
                case "directs":
                    editing = whatToEditRelationshipGeneric(scan);
                    break;
                default:
                    System.out.println(relationshipType + " is an invalid input");
            }
            stmt = conn.prepareStatement(Maps.relationshipEditorMap.get(relationshipType)[editing - 1]);
            switch (relationshipType) {
                case "stars":
                    stmt = editRelationshipExecuteStars(stmt, scan, editing, conn);
                    break;
                case "writes":
                case "interviewed":
                case "performs":
                case "directs":
                    stmt = editRelationshipExecuteGeneric(stmt, scan, editing, conn, relationshipType);
                    break;
                default:
                    System.out.println(relationshipType + " is an invalid input");
            }

            stmt.setInt(2, rel.getCreatorID());
            stmt.setInt(3, rel.getItemID());
            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static PreparedStatement editRelationshipExecuteGeneric(PreparedStatement stmt, Scanner scan, int editing,
            Connection conn, String relationshipType)
            throws Exception {
        try {
            switch (editing) {
                case 1:
                    String creatorType = Searcher.getRelationshipCreatorType(relationshipType);
                    int newCreatorID = Searcher.pickCreator(creatorType, conn, scan);
                    stmt.setInt(1, newCreatorID);
                    break;
                case 2:
                    String itemType = Searcher.getRelationshipItemType(relationshipType, scan);
                    int newItemID = Searcher.pickItem(itemType, conn, scan);
                    stmt.setInt(1, newItemID);
                    break;
            }
            return stmt;
        } catch (Exception e) {
            throw e;
        }
    }

    private static PreparedStatement editRelationshipExecuteStars(PreparedStatement stmt, Scanner scan, int editing,
            Connection conn)
            throws Exception {
        try {
            switch (editing) {
                case 1:
                    String creatorType = Searcher.getRelationshipCreatorType("stars");
                    int newCreatorID = Searcher.pickCreator(creatorType, conn, scan);
                    stmt.setInt(1, newCreatorID);
                    break;
                case 2:
                    String itemType = Searcher.getRelationshipItemType("stars", scan);
                    int newItemID = Searcher.pickItem(itemType, conn, scan);
                    stmt.setInt(1, newItemID);
                    break;
                case 3:
                    String newRole = Util.getString(scan, "new role");
                    stmt.setString(1, newRole);
                    break;
            }
            return stmt;
        } catch (Exception e) {
            throw e;
        }

    }

    private static int whatToEditRelationshipGeneric(Scanner scan) throws Exception {
        int option = 0;
        while (option == 0) {
            System.out.println("Please enter 1 to edit the creator, 2 to edit the item:");
            option = Integer.valueOf(scan.nextLine());
            if (!(option == 1 || option == 2))
                option = 0;
        }
        return option;
    }

    private static int whatToEditRelationshipStars(Scanner scan) throws Exception {
        int option = 0;
        while (option == 0) {
            System.out.println("Please enter 1 to edit the creator, 2 to edit the item, 3 to edit the role:");
            option = Integer.valueOf(scan.nextLine());
            if (!(option == 1 || option == 2 || option == 3))
                option = 0;
        }
        return option;
    }

    public static void editPerson(Connection conn, Scanner scan) throws Exception {
        PreparedStatement stmt = null;
        try {
            int cardID = Searcher.pickPerson(conn, scan);
            int editing = whatToEditPerson(scan);
            stmt = conn.prepareStatement(Maps.personEditorArr[editing - 1]);
            // 1 for email, 2 for fname, 3 for lname, 4 for address, 5 for address
            switch (editing) {
                case 1:
                    String newEmail = Util.getEmail(scan);
                    stmt.setString(1, newEmail);
                    break;
                case 2:
                case 3:
                case 4:
                    String newValue = Util.getString(scan, "new value");
                    stmt.setString(1, newValue);
                    break;
                case 5:
                    int newCardID = Util.nextIDFrom("library_card", conn);
                    System.out.println("The new library card number is: " + newCardID);
                    stmt.setInt(1, newCardID);
                    break;
                default:
                    System.out.println("Invalid editing option!");
            }
            stmt.setInt(2, cardID);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static int whatToEditPerson(Scanner scan) throws Exception {
        int option = 0;
        while (option == 0) {
            System.out.println(
                    "Please enter 1 to edit the email, 2 to edit the first name, 3 to edit the last name, 4 to edit the address, 5 to edit the library card number: ");
            option = Integer.valueOf(scan.nextLine());
            if (!(option == 1 || option == 2 || option == 3 || option == 4 || option == 5))
                option = 0;
        }
        return option;
    }

    public static void editCreator(Connection conn, Scanner scan) throws Exception {
        System.out.println(
                "Options:\n(actor)\n(director)\n(artist)\n(writer)\nPlease enter the creator you're editing:");
        String creatorType = scan.nextLine().toLowerCase();
        try {
            switch (creatorType) {
                case "actor":
                case "director":
                case "artist":
                case "writer":
                    editCreator(creatorType, conn, scan);
                    break;
                default:
                    System.err.println(creatorType + " isn't a valid creator type");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private static void editCreator(String type, Connection conn, Scanner scan) throws Exception {
        PreparedStatement stmt = null;
        try {
            int creatorID = Searcher.pickCreator(type, conn, scan);
            int editing = whatToEditCreator(scan);

            stmt = conn.prepareStatement(Maps.creatorEditorMap.get(type)[editing - 1]);

            switch (editing) {
                case 1:
                    String newName = Util.getString(scan, "new name");
                    stmt.setString(1, newName);
                    break;
                case 2:
                    String dateOfBirth = Util.getDate(scan, "date of birth");
                    stmt.setString(1, dateOfBirth);
                    break;
            }
            stmt.setInt(2, creatorID);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static int whatToEditCreator(Scanner scan) throws Exception {
        int option = 0;
        while (option == 0) {
            System.out.println("Please enter 1 to edit the artist name, 2 to edit the artist dob");
            option = Integer.valueOf(scan.nextLine());
            if (!(option == 1 || option == 2))
                option = 0;
        }
        return option;
    }

    public static void editItem(Connection conn, Scanner scan) throws Exception {
        System.out.println(
                "Options:\n(album)\n(track)\n(interview)\n(movie)\n(audiobook)\n(physicalbook)\n(audiobookchapter)\n(physicalbookchapter)\n(genre)\nPlease enter the item you're editing:");
        String itemType = scan.nextLine().toLowerCase();
        try {
            switch (itemType) {
                case "album":
                case "track":
                case "interview":
                case "movie":
                case "audiobook":
                case "physicalbook":
                    editItem(itemType, conn, scan);
                    break;
                case "audiobookchapter":
                case "physicalbookchapter":
                    editChapter(itemType, conn, scan);
                    break;
                case "genre":
                    editGenre(itemType, conn, scan);
                    break;
                default:
                    System.err.println(itemType + " isn't a valid item type");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private static void editItem(String type, Connection conn, Scanner scan) throws Exception {
        PreparedStatement stmt = null;
        try {
            int itemID = Searcher.pickItem(type, conn, scan);
            int editing = 0;
            switch (type) {
                case "album":
                    editing = whatToEditItemGeneric(scan, "number of songs", "number of minutes");
                    break;
                case "track":
                    editing = whatToEditItemGeneric(scan, "number of seconds", "album ID");
                    break;
                case "interview":
                    editing = 1;
                    break;
                case "movie":
                    editing = whatToEditItemGeneric(scan, "runtime", "rating");
                    break;
                case "audiobook":
                    editing = 1;
                    break;
                case "physicalbook":
                    editing = 1;
                    break;
                case "itemordered":
                    editing = whatToEditItemOrdered(scan);
                    break;
                default:
                    // print invalid
                    System.out.println(type + " is an Invalid input");
            }

            stmt = conn.prepareStatement(Maps.itemEditorMap.get(type)[editing - 1]);

            switch (type) {
                case "album":
                    stmt = editItemExecuteGeneric(stmt, scan, editing, "number of songs", "number of minutes");
                    break;
                case "track":
                    stmt = editItemExecuteGeneric(stmt, scan, editing, "number of seconds", "album ID");
                    break;
                case "interview":
                    stmt = editItemExecuteSingle(stmt, scan, editing, "number of minutes");
                    break;
                case "movie":
                    stmt = editItemExecuteIntString(stmt, scan, editing, "runtime", "rating");
                    break;
                case "audiobook":
                    stmt = editItemExecuteSingle(stmt, scan, editing, "number of minutes");
                    break;
                case "physicalbook":
                    stmt = editItemExecuteSingle(stmt, scan, editing, "number of pages");
                    break;
                case "itemordered":
                    stmt = editItemOrderedExecute(stmt, scan, editing);
                    break;
                default:
                    // print invalid
                    System.out.println(type + " is an Invalid input");
            }
            stmt.setInt(2, itemID);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static void editGenre(String type, Connection conn, Scanner scan) throws Exception {
        int itemID = Searcher.pickGenre(conn, scan);
        PreparedStatement stmt = null;
        try {
            String newGenre = Util.getString(scan, "new genre");
            stmt = conn.prepareStatement(Maps.genreEditorMap.get("item"));
            stmt.setString(1, newGenre);
            stmt.setInt(2, itemID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static int whatToEditItemOrdered(Scanner scan) throws Exception {
        int option = 0;
        while (option == 0) {
            System.out.println(
                    "Please enter 1 to edit Item_ID, 2 to edit the price, 3 to edit the quantity ordered,\n 4 to edit the expected arrival date, 5 to edit the actual arrival date");
            option = Integer.valueOf(scan.nextLine());
            if (!(option == 1 || option == 2 || option == 3 || option == 4 || option == 5))
                option = 0;
        }
        return option;
    }

    private static int whatToEditItemGeneric(Scanner scan, String option1, String option2) throws Exception {
        int option = 0;
        while (option == 0) {
            System.out.println("Please enter 1 to edit " + option1 + ", 2 to edit the " + option2);
            option = Integer.valueOf(scan.nextLine());
            if (!(option == 1 || option == 2))
                option = 0;
        }
        return option;
    }

    private static PreparedStatement editItemOrderedExecute(PreparedStatement stmt, Scanner scan, int editing)
            throws Exception {
        try {
            switch (editing) {
                case 1:
                    System.out.println("enter the new Item_ID");
                    int itemID = Integer.parseInt(scan.nextLine());
                    stmt.setInt(1, itemID);
                    break;
                case 2:
                    System.out.println("enter the new price in the format dollars.cents");
                    double price = Double.parseDouble(scan.nextLine());
                    stmt.setDouble(1, price);
                    break;
                case 3:
                    System.out.println("enter the new quantity ordered");
                    int quantity = Integer.parseInt(scan.nextLine());
                    stmt.setInt(1, quantity);
                    break;
                case 4:
                    String estimatedArrivalDate = Util.getDate(scan, "new estimated arrival date");
                    stmt.setString(1, estimatedArrivalDate);
                    break;
                case 5:
                    String actualArrivalDate = Util.getDate(scan, "new actual arrival date");
                    stmt.setString(1, actualArrivalDate);
                    break;
            }
            return stmt;
        } catch (Exception e) {
            throw e;
        }
    }

    private static PreparedStatement editItemExecuteGeneric(PreparedStatement stmt, Scanner scan, int editing,
            String option1, String option2) throws Exception {
        try {
            switch (editing) {
                case 1:
                    System.out.println("enter the new " + option1);
                    int newValue = Integer.parseInt(scan.nextLine());
                    stmt.setInt(1, newValue);
                    break;
                case 2:
                    System.out.println("enter the new " + option2);
                    int newValue2 = Integer.parseInt(scan.nextLine());
                    stmt.setInt(1, newValue2);
                    break;
            }
            return stmt;
        } catch (Exception e) {
            throw e;
        }
    }

    // use this one for movie because we have a string we want to edit
    private static PreparedStatement editItemExecuteIntString(PreparedStatement stmt, Scanner scan, int editing,
            String option1, String option2) throws Exception {
        try {
            switch (editing) {
                case 1:
                    int newValue = Util.getInteger(scan, "new " + option1);
                    stmt.setInt(1, newValue);
                    break;
                case 2:
                    String newValue2 = Util.getString(scan, "new " + option2);
                    stmt.setString(1, newValue2);
                    break;
            }
            return stmt;
        } catch (Exception e) {
            throw e;
        }
    }

    // use this one for interview because theres just one thing to edit
    private static PreparedStatement editItemExecuteSingle(PreparedStatement stmt, Scanner scan, int editing,
            String option1) throws Exception {
        try {
            switch (editing) {
                case 1:
                    System.out.println("enter the new " + option1);
                    int newValue = Integer.parseInt(scan.nextLine());
                    stmt.setInt(1, newValue);
                    break;
            }
            return stmt;
        } catch (Exception e) {
            throw e;
        }
    }

    private static void editChapter(String type, Connection conn, Scanner scan) throws Exception {
        PreparedStatement stmt = null;
        try {
            int ItemID = Searcher.pickChapter(type, conn, scan);
            String chapterName = Util.getString(scan, "chapter you would like to rename");
            String newChapterName = Util.getString(scan, "new chapter name");
            stmt = conn.prepareStatement(Maps.chapterEditorMap.get(type));
            stmt.setString(1, newChapterName);
            stmt.setString(2, chapterName);
            stmt.setInt(3, ItemID);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }
}
