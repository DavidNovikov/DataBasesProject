import java.sql.*;
import java.util.*;

public class Editor {

    public static void editRelationship(String relationshipType, Connection conn, Scanner scan) {
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
                    stmt = editRelationshipExecuteStars(stmt, scan, editing);
                    break;
                case "writes":
                case "interviewed":
                case "performs":
                case "directs":
                    stmt = editRelationshipExecuteGeneric(stmt, scan, editing);
                    break;
                default:
                    System.out.println(relationshipType + " is an invalid input");
            }

            stmt.setInt(2, rel.getCreatorID());
            stmt.setInt(3, rel.getItemID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static PreparedStatement editRelationshipExecuteGeneric(PreparedStatement stmt, Scanner scan, int editing) {
        try {
            switch (editing) {
                case 1:
                    System.out.println("enter the new " + "creatorID");
                    int newValue = Integer.parseInt(scan.nextLine());
                    stmt.setInt(1, newValue);
                    break;
                case 2:
                    System.out.println("enter the new " + "itemID");
                    int newValue2 = Integer.parseInt(scan.nextLine());
                    stmt.setInt(1, newValue2);
                    break;
            }
            return stmt;
        } catch (Exception e) {
            System.out.println("Exception entering new value:" + e);
            return null;
        }
    }

    private static PreparedStatement editRelationshipExecuteStars(PreparedStatement stmt, Scanner scan, int editing) {
        try {
            switch (editing) {
                case 1:
                    System.out.println("enter the new " + "creatorID");
                    int newValue = Integer.parseInt(scan.nextLine());
                    stmt.setInt(1, newValue);
                    break;
                case 2:
                    System.out.println("enter the new " + "itemID");
                    int newValue2 = Integer.parseInt(scan.nextLine());
                    stmt.setInt(1, newValue2);
                    break;
                case 3:
                    System.out.println("enter the new " + "role");
                    String newRole = scan.nextLine();
                    stmt.setString(1, newRole);
                    break;
            }
            return stmt;
        } catch (Exception e) {
            System.out.println("Exception entering new value:" + e);
            return null;
        }

    }

    private static int whatToEditRelationshipGeneric(Scanner scan) {
        int option = 0;
        while (option == 0) {
            System.out.println("Please enter 1 to edit the creatorID, 2 to edit the itemID:");
            option = Integer.valueOf(scan.nextLine());
            if (!(option == 1 || option == 2))
                option = 0;
        }
        return option;
    }

    private static int whatToEditRelationshipStars(Scanner scan) {
        int option = 0;
        while (option == 0) {
            System.out.println("Please enter 1 to edit the creatorID, 2 to edit the itemID, 3 to edit the role:");
            option = Integer.valueOf(scan.nextLine());
            if (!(option == 1 || option == 2 || option == 3))
                option = 0;
        }
        return option;
    }

    public static void editPerson(String type, Connection conn, Scanner scan) {
        int cardID = Searcher.pickPerson(conn, scan);
        PreparedStatement stmt = null;
        try {
            int editing = whatToEditPerson(scan);
            stmt = conn.prepareStatement(Maps.personEditorMap.get(type)[editing - 1]);
            // 1 for email, 2 for fname, 3 for lname, 4 for address, 5 for address
            switch (editing) {
                case 1:
                    String newEmail = Util.getEmail(scan);
                    stmt.setString(1, newEmail);
                    break;
                case 2:
                case 3:
                case 4:
                    System.out.println("enter the new value:");
                    String newValue = scan.nextLine();
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
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static int whatToEditPerson(Scanner scan) {
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

    public static void editCreator(String type, Connection conn, Scanner scan) {
        int creatorID = Searcher.pickCreator(type, conn, scan);
        PreparedStatement stmt = null;
        try {
            int editing = whatToEditCreator(scan);

            stmt = conn.prepareStatement(Maps.creatorEditorMap.get(type)[editing - 1]);

            switch (editing) {
                case 1:
                    System.out.println("enter the new value");
                    String newName = scan.nextLine();
                    stmt.setString(1, newName);
                    break;
                case 2:
                    String dateOfBirth = Util.getDate(scan, "date of birth");
                    stmt.setString(1, dateOfBirth);
                    break;
            }
            stmt.setInt(2, creatorID);
            stmt.executeUpdate();
        } catch (SQLException | NumberFormatException e) {
            System.out.println(e.getMessage());
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static int whatToEditCreator(Scanner scan) {
        int option = 0;
        while (option == 0) {
            System.out.println("Please enter 1 to edit the artist name, 2 to edit the artist dob");
            option = Integer.valueOf(scan.nextLine());
            if (!(option == 1 || option == 2))
                option = 0;
        }
        return option;
    }

    public static void editItem(String type, Connection conn, Scanner scan) {
        int itemID = Searcher.pickItem(type, conn, scan);
        PreparedStatement stmt = null;
        try {
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
                default:
                    // print invalid
                    System.out.println(type + " is an Invalid input");
            }
            stmt.setInt(2, itemID);
            stmt.executeUpdate();
        } catch (SQLException | NumberFormatException e) {
            System.out.println(e.getMessage());
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static int whatToEditItemGeneric(Scanner scan, String option1, String option2) {
        int option = 0;
        while (option == 0) {
            System.out.println("Please enter 1 to edit " + option1 + ", 2 to edit the " + option2);
            option = Integer.valueOf(scan.nextLine());
            if (!(option == 1 || option == 2))
                option = 0;
        }
        return option;
    }

    private static PreparedStatement editItemExecuteGeneric(PreparedStatement stmt, Scanner scan, int editing,
            String option1, String option2) {
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
            System.out.println("Exception entering new value:" + e);
            return null;
        }

    }

    // use this one for movie because we have a string we want to edit
    private static PreparedStatement editItemExecuteIntString(PreparedStatement stmt, Scanner scan, int editing,
            String option1, String option2) {
        try {
            switch (editing) {
                case 1:
                    System.out.println("enter the new " + option1);
                    int newValue = Integer.parseInt(scan.nextLine());
                    stmt.setInt(1, newValue);
                    break;
                case 2:
                    System.out.println("enter the new " + option2);
                    String newValue2 = scan.nextLine();
                    stmt.setString(1, newValue2);
                    break;
            }
            return stmt;
        } catch (Exception e) {
            System.out.println("Exception entering new value:" + e);
            return null;
        }
    }

    // use this one for interview because theres just one thing to edit
    private static PreparedStatement editItemExecuteSingle(PreparedStatement stmt, Scanner scan, int editing,
            String option1) {
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
            System.out.println("Exception entering new value:" + e);
            return null;
        }
    }

}
