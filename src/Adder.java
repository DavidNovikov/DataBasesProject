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

            stmt = conn.prepareStatement(Maps.itemAdderMap.get("item"));

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
            case "track":
                addTrack(item, conn, scan, newItemID);
                break;
            case "interview":
                addInterview(item, conn, scan, newItemID);
                break;
            case "movie":
                addMovie(item, conn, scan, newItemID);
                break;
            case "audiobook":
                addAudiobook(item, conn, scan, newItemID);
                break;
            case "physicalbook":
                addPhysicalbook(item, conn, scan, newItemID);
                break;
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

            stmt = conn.prepareStatement(Maps.itemAdderMap.get("album"));

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

    private static void addTrack(String item, Connection conn, Scanner scan, int newItemID) throws Exception {
        PreparedStatement stmt = null;

        try {
            System.out.println("Please enter the AlbumID corresponding to this track");
            int albumID = Integer.valueOf(scan.nextLine());

            System.out.println("Please enter the length in seconds");
            int lenInSec = Integer.valueOf(scan.nextLine());

            stmt = conn.prepareStatement(Maps.itemAdderMap.get("track"));

            stmt.setInt(1, lenInSec);
            stmt.setInt(2, albumID);
            stmt.setInt(3, newItemID);

            stmt.executeUpdate();
        } catch (SQLException | NumberFormatException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static void addInterview(String item, Connection conn, Scanner scan,
            int newItemID) throws Exception {
        PreparedStatement stmt = null;

        try {

            System.out.println("Please enter the length in minutes");
            int lenInMin = Integer.valueOf(scan.nextLine());

            stmt = conn.prepareStatement(Maps.itemAdderMap.get("interview"));

            stmt.setInt(1, lenInMin);
            stmt.setInt(2, newItemID);

            stmt.executeUpdate();
        } catch (SQLException | NumberFormatException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static void addMovie(String item, Connection conn, Scanner scan, int newItemID) throws Exception {
        PreparedStatement stmt = null;

        try {
            System.out.println("Please enter the runtime in minutes");
            int runtime = Integer.valueOf(scan.nextLine());

            System.out.println("Please enter the rating");
            String rating = scan.nextLine();

            stmt = conn.prepareStatement(Maps.itemAdderMap.get("movie"));

            stmt.setInt(1, runtime);
            stmt.setString(2, rating);
            stmt.setInt(3, newItemID);

            stmt.executeUpdate();
        } catch (SQLException | NumberFormatException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static void addAudiobook(String item, Connection conn, Scanner scan,
            int newItemID) throws Exception {
        PreparedStatement stmt = null;

        try {
            System.out.println("Please enter the number of chapters");
            int numChapters = Integer.valueOf(scan.nextLine());

            System.out.println("Please enter the length in minutes");
            int lenInMin = Integer.valueOf(scan.nextLine());

            stmt = conn.prepareStatement(Maps.itemAdderMap.get("audiobook"));

            stmt.setInt(1, numChapters);
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

    private static void addPhysicalbook(String item, Connection conn, Scanner scan, int newItemID) throws Exception {
        PreparedStatement stmt = null;

        try {
            System.out.println("Please enter the number of chapters");
            int numChapters = Integer.valueOf(scan.nextLine());

            System.out.println("Please enter the number of pages");
            int pages = Integer.valueOf(scan.nextLine());

            stmt = conn.prepareStatement(Maps.itemAdderMap.get("physical_book"));

            stmt.setInt(1, numChapters);
            stmt.setInt(2, pages);
            stmt.setInt(3, newItemID);

            stmt.executeUpdate();
        } catch (SQLException | NumberFormatException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

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

    public static void addCreator(String creatorType, Connection conn, Scanner scan) {
        int newCreatorID;
        try {
            newCreatorID = addCreatorBase(creatorType, conn, scan);
            addCreatorSuper(creatorType, conn, scan, newCreatorID);

        } catch (Exception e) {
            System.out.println("failed to insert");
        }
    }

    private static int addCreatorBase(String creatorType, Connection conn, Scanner scan) throws Exception {
        PreparedStatement stmt = null;

        int creatorID = Util.nextIDFrom("creator", conn);

        try {

            stmt = conn.prepareStatement(Maps.creatorAdderMap.get("creator"));

            stmt.setInt(1, creatorID);

            stmt.executeUpdate();
        } catch (SQLException | NumberFormatException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
        return creatorID;
    }

    private static void addCreatorSuper(String creatorType, Connection conn, Scanner scan, int newCreatorID)
            throws Exception {

        switch (creatorType) {
            case "artist":
            case "actor":
            case "director":
            case "writer":
                addCreatorNameDOB(creatorType, conn, scan, newCreatorID);
                break;
            default:
                System.err.println(creatorType + " isn't a valid new item insert type");
        }

    }

    private static void addCreatorNameDOB(String creatorType, Connection conn, Scanner scan, int newCreatorID)
            throws Exception {
        PreparedStatement stmt = null;

        try {
            System.out.println("Please enter the " + creatorType + " name");
            String name = scan.nextLine();

            String dateOfBirth = Util.getDate(scan, "date of birth");

            stmt = conn.prepareStatement(Maps.creatorAdderMap.get(creatorType));

            stmt.setString(1, name);
            stmt.setString(2, dateOfBirth);
            stmt.setInt(3, newCreatorID);

            stmt.executeUpdate();
        } catch (SQLException | NumberFormatException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }
}
