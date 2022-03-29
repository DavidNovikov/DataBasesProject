import java.sql.*;
import java.util.*;

public class Adder {

    public static void addItem(String item, Connection conn, Scanner scan) {
        int newItemID;
        try {
            newItemID = addItemBase(item, conn, scan);
            addItemSuper(item, conn, scan, newItemID);
            // addItemGenre(item, conn, scan, newItemID);
        } catch (Exception e) {
            System.out.println("failed to insert");
        }
    }

    private static int addItemBase(String item, Connection conn, Scanner scan) throws Exception {
        PreparedStatement stmt = null;

        int itemID = Util.nextIDFrom("item", conn);

        try {

            System.out.println("Please enter the title");
            String title = scan.nextLine();

            System.out.println("Please enter the year released");
            int year = Integer.valueOf(scan.nextLine());

            String query = "insert into item values (?,?,?,?,?);";
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, itemID);
            stmt.setString(2, title);
            stmt.setInt(3, year);
            stmt.setString(4, Util.getTypeForInsert(item));
            stmt.setBoolean(5, Util.getStatus(scan));

            stmt.executeUpdate();
        } catch (SQLException | NumberFormatException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
        return itemID;
    }

    private static void addItemSuper(String item, Connection conn, Scanner scan, int newItemID) throws Exception {

        switch (item) {
            case "album":
                addAlbum(item, conn, scan, newItemID);
                break;
            // case "track":
            // addTrack(item, conn, scan, newItemID);
            // break;
            // case "interview":
            // addInterview(item, conn, scan, newItemID);
            // break;
            // case "movie":
            // addMovie(item, conn, scan, newItemID);
            // break;
            // case "audiobook":
            // addAudiobook(item, conn, scan, newItemID);
            // break;
            // case "physicalbook":
            // addPhysicalbook(item, conn, scan, newItemID);
            // break;
            default:
                System.err.println(item + " isn't a valid new item insert type");
        }

    }

    private static void addAlbum(String item, Connection conn, Scanner scan, int newItemID) throws Exception {
        PreparedStatement stmt = null;

        try {
            System.out.println("Please enter the number of songs");
            int numSongs = Integer.valueOf(scan.nextLine());

            System.out.println("Please enter the length in minutes");
            int lenInMin = Integer.valueOf(scan.nextLine());

            String query = "insert into album values (?,?,?);";
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, numSongs);
            stmt.setInt(2, lenInMin);
            stmt.setInt(3, newItemID);

            stmt.executeUpdate();
        } catch (SQLException | NumberFormatException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    // private static void addTrack(String item, Connection conn, Scanner scan, int
    // newItemID) {
    // PreparedStatement stmt = null;

    // try {
    // System.out.println("Please enter the number of songs");
    // int numSongs = Integer.valueOf(scan.nextLine());

    // System.out.println("Please enter the length in minutes");
    // int lenInMin = Integer.valueOf(scan.nextLine());

    // String query = "insert into album values (?,?,?);";
    // stmt = conn.prepareStatement(query);

    // stmt.setInt(1, numSongs);
    // stmt.setInt(2, lenInMin);
    // stmt.setInt(3, newItemID);

    // stmt.executeUpdate();
    // } catch (SQLException | NumberFormatException e) {
    // System.out.println(e.getMessage());
    // throw e;
    // } finally {
    // Util.closeStmt(stmt);
    // }
    // }

    // private static void addInterview(String item, Connection conn, Scanner scan,
    // int newItemID) {
    // PreparedStatement stmt = null;

    // try {
    // System.out.println("Please enter the number of songs");
    // int numSongs = Integer.valueOf(scan.nextLine());

    // System.out.println("Please enter the length in minutes");
    // int lenInMin = Integer.valueOf(scan.nextLine());

    // String query = "insert into album values (?,?,?);";
    // stmt = conn.prepareStatement(query);

    // stmt.setInt(1, numSongs);
    // stmt.setInt(2, lenInMin);
    // stmt.setInt(3, newItemID);

    // stmt.executeUpdate();
    // } catch (SQLException | NumberFormatException e) {
    // System.out.println(e.getMessage());
    // throw e;
    // } finally {
    // Util.closeStmt(stmt);
    // }
    // }

    // private static void addMovie(String item, Connection conn, Scanner scan, int
    // newItemID) {
    // PreparedStatement stmt = null;

    // try {
    // System.out.println("Please enter the number of songs");
    // int numSongs = Integer.valueOf(scan.nextLine());

    // System.out.println("Please enter the length in minutes");
    // int lenInMin = Integer.valueOf(scan.nextLine());

    // String query = "insert into album values (?,?,?);";
    // stmt = conn.prepareStatement(query);

    // stmt.setInt(1, numSongs);
    // stmt.setInt(2, lenInMin);
    // stmt.setInt(3, newItemID);

    // stmt.executeUpdate();
    // } catch (SQLException | NumberFormatException e) {
    // System.out.println(e.getMessage());
    // throw e;
    // } finally {
    // Util.closeStmt(stmt);
    // }
    // }

    // private static void addAudiobook(String item, Connection conn, Scanner scan,
    // int newItemID) {
    // PreparedStatement stmt = null;

    // try {
    // System.out.println("Please enter the number of songs");
    // int numSongs = Integer.valueOf(scan.nextLine());

    // System.out.println("Please enter the length in minutes");
    // int lenInMin = Integer.valueOf(scan.nextLine());

    // String query = "insert into album values (?,?,?);";
    // stmt = conn.prepareStatement(query);

    // stmt.setInt(1, numSongs);
    // stmt.setInt(2, lenInMin);
    // stmt.setInt(3, newItemID);

    // stmt.executeUpdate();
    // } catch (SQLException | NumberFormatException e) {
    // System.out.println(e.getMessage());
    // throw e;
    // } finally {
    // Util.closeStmt(stmt);
    // }
    // }

    // private static void addPhysicalbook(String item, Connection conn, Scanner
    // scan, int newItemID) {
    // PreparedStatement stmt = null;

    // try {
    // System.out.println("Please enter the number of songs");
    // int numSongs = Integer.valueOf(scan.nextLine());

    // System.out.println("Please enter the length in minutes");
    // int lenInMin = Integer.valueOf(scan.nextLine());

    // String query = "insert into album values (?,?,?);";
    // stmt = conn.prepareStatement(query);

    // stmt.setInt(1, numSongs);
    // stmt.setInt(2, lenInMin);
    // stmt.setInt(3, newItemID);

    // stmt.executeUpdate();
    // } catch (SQLException | NumberFormatException e) {
    // System.out.println(e.getMessage());
    // throw e;
    // } finally {
    // Util.closeStmt(stmt);
    // }
    // }

    // public static void addItemGenre(String item, Connection conn, Scanner scan,
    // int newItemID) {
    // }

    // public static void addItemCheckedOut() {
    // PreparedStatement stmt = null;

    // try {
    // System.out.println("Please enter the number of songs");
    // int numSongs = Integer.valueOf(scan.nextLine());

    // System.out.println("Please enter the length in minutes");
    // int lenInMin = Integer.valueOf(scan.nextLine());

    // String query = "insert into album values (?,?,?);";
    // stmt = conn.prepareStatement(query);

    // stmt.setInt(1, numSongs);
    // stmt.setInt(2, lenInMin);
    // stmt.setInt(3, newItemID);

    // stmt.executeUpdate();
    // } catch (SQLException | NumberFormatException e) {
    // System.out.println(e.getMessage());
    // throw e;
    // } finally {
    // Util.closeStmt(stmt);
    // }
    // }

    public static void addPersonAndCard(Connection conn, Scanner scan) {
        int newCardID;
        try {
            newCardID = addLibraryCard(conn, scan);
            addPerson(conn, scan, newCardID);
        } catch (Exception e) {
            System.out.println("failed to insert");
        }
    }

    private static int addLibraryCard(Connection conn, Scanner scan) throws Exception {
        PreparedStatement stmt = null;

        int cardID = Util.nextIDFrom("library_card", conn);

        try {
            String query = "insert into library_card values (?);";
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, cardID);
        
            stmt.executeUpdate();
        } catch (SQLException | NumberFormatException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
        return cardID;
    }

    private static int addPerson(Connection conn, Scanner scan, int cardID) throws Exception {
        PreparedStatement stmt = null;

        try {
            //get the email
            String email = Util.getEmail(scan);

            System.out.println("Please enter the first name");
            String fname = scan.nextLine();

            System.out.println("Please enter the last name");
            String lname = scan.nextLine();

            System.out.println("Please enter the address");
            String address = scan.nextLine();
            
            String query = "insert into person values (?,?,?,?,?);";
            stmt = conn.prepareStatement(query);

            stmt.setString(1, email);
            stmt.setString(2, fname);
            stmt.setString(3, lname);
            stmt.setString(4, address);
            stmt.setInt(5, cardID);
        
            stmt.executeUpdate();
        } catch (SQLException | NumberFormatException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
        return cardID;
    }
}
