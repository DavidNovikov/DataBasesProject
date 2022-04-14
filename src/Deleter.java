import java.sql.*;
import java.util.*;

public class Deleter {
    public static void deleteItemCheckedOut(Connection conn, Scanner scan) throws Exception {
        System.out.println(
                "Enter the type of record that was checked out, to be deleted. (Album, Track, Interview, Movie, Audiobook, or PhysicalBook): ");
        String type = scan.nextLine().toLowerCase();
        int itemID = -1;
        switch (type) {
            case "album":
            case "track":
            case "interview":
            case "movie":
            case "audiobook":
            case "physicalbook":
                itemID = Searcher.pickItem(type, conn, scan);
                break;
            default:
                // print invalid
                System.out.println(type + " is an Invalid input");
        }
        String checkoutDate = Util.getDate(scan, "Checkout Date");
        PreparedStatement stmt = null;
        try {
            String sql = Maps.deleteItemCheckoutsString;
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, itemID);
            stmt.setString(2, checkoutDate);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }

        System.out.println("Record deleted");
    }

    public static void deleteCreator(Connection conn, Scanner scan) throws Exception {
        System.out.println(
                "Options:\n(actor)\n(director)\n(artist)\n(writer)\nPlease enter the creator you're deleting:");
        String creatorType = scan.nextLine().toLowerCase();
        try {
            switch (creatorType) {
                case "actor":
                case "director":
                case "artist":
                case "writer":
                    deleteCreator(creatorType, conn, scan);
                    break;
                default:
                    System.err.println(creatorType + " isn't a valid creator type");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private static void deleteCreator(String type, Connection conn, Scanner scan) throws Exception {
        try {
            int cID = Searcher.pickCreator(type, conn, scan);
            // TODO delete the items that the creator created but only if they're the only
            // creator
            // maybe delete relationships and then clean up items that are left
            // TODO delete relationship
            deleteCreatorRelationships(cID, type, conn);
            deleteCreatorSuper(cID, type, conn);
            deleteCreatorBase(cID, type, conn);
        } catch (Exception e) {
            throw e;
        }
    }

    private static void deleteCreatorBase(int cID, String type, Connection conn) throws Exception {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(Maps.creatorDeleteMap.get("creator"));
            stmt.setInt(1, cID);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static void deleteCreatorSuper(int cID, String type, Connection conn) throws Exception {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(Maps.creatorDeleteMap.get(type));
            stmt.setInt(1, cID);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static void deleteCreatorRelationships(int cID, String type, Connection conn) throws Exception {
        PreparedStatement stmt = null;
        try {
            String[] vals = Maps.creatorDeleteRelationshipMap.get(type);
            for (String val : vals) {
                stmt = conn.prepareStatement(val);
                stmt.setInt(1, cID);
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    public static void deleteRelationship(Connection conn, Scanner scan) throws Exception {

        System.out.println(
                "Options:\n(stars)\n(writes)\n(interviewed)\n(performs)\n(directs)\nPlease enter the relationship you're deleting:");
        String relationshipType = scan.nextLine().toLowerCase();

        try {

            switch (relationshipType) {
                case "stars":
                case "writes":
                case "interviewed":
                case "performs":
                case "directs":
                    deleteRelationship(relationshipType, conn, scan);
                    break;
                default:
                    System.err.println(relationshipType + " isn't a valid relationship type");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private static void deleteRelationship(String type, Connection conn, Scanner scan) throws Exception {
        Relationship rel = Searcher.pickRelationship(type, conn, scan);
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(Maps.relationshipDeleterMap.get(type));
            stmt.setInt(1, rel.getCreatorID());
            stmt.setInt(2, rel.getItemID());
            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    public static void deletePerson(Connection conn, Scanner scan) throws Exception {
        int cardID = Searcher.pickPerson(conn, scan);
        deleteItemsCheckedOutEntrysByPerson(conn, cardID);
        deletePersonFromLibrary(conn, cardID);
    }

    private static void deletePersonFromLibrary(Connection conn, int cardID)
            throws Exception {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(Maps.deletePersonString);
            stmt.setInt(1, cardID);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static void deleteItemsCheckedOutEntrysByPerson(Connection conn, int cardID)
            throws Exception {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(Maps.deletePersonsCheckoutsString);
            stmt.setInt(1, cardID);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static void deleteChapter(String type, Connection conn, Scanner scan) throws Exception {
        PreparedStatement stmt = null;
        try {
            System.out.println("Please choose the book you would like to remove a chapter from: ");
            int ItemID = Searcher.pickChapter(type, conn, scan);
            String chapterName = Util.getString(scan, "name of chapter to remove");
            stmt = conn.prepareStatement(Maps.chapterDeleterMap.get(type));
            stmt.setString(2, chapterName);
            stmt.setInt(1, ItemID);
            stmt.executeUpdate();

        } catch (Exception e) {
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    public static void deleteGenreWithItemID(int itemID, Connection conn) throws Exception {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(Maps.genreDeleterMap.get("itemIDOnly"));
            stmt.setInt(1, itemID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    public static void deleteGenre(Connection conn, Scanner scan) throws Exception {
        System.out.println("Select an entry to delete a genre from");
        GenreIDPair genreID = Searcher.pickGenre(conn, scan);
        int itemID = genreID.getItemID();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(Maps.genreDeleterMap.get("item"));
            stmt.setInt(1, itemID);
            stmt.setString(2, genreID.getItemGenre());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    public static void deleteItem(Connection conn, Scanner scan) throws Exception {
        System.out.println(
                "Options:\n(album)\n(track)\n(interview)\n(movie)\n(audiobook)\n(physicalbook)\n(audiobookchapter)\n(physicalbookchapter)\n(genre)\nPlease enter the item you're deleteing:");
        String itemType = scan.nextLine().toLowerCase();
        try {
            switch (itemType) {
                case "album":
                case "track":
                case "interview":
                case "movie":
                case "audiobook":
                case "physicalbook":
                    deleteItem(itemType, conn, scan);
                    break;
                case "audiobookchapter":
                case "physicalbookchapter":
                    deleteChapter(itemType, conn, scan);
                    break;
                case "genre":
                    deleteGenre(conn, scan);
                    break;
                default:
                    System.err.println(itemType + " isn't a valid item type");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private static void deleteItem(String type, Connection conn, Scanner scan) throws Exception {
        try {
            int itemID = Searcher.pickItem(type, conn, scan);
            deleteItemWithItemID(itemID, type, conn);
        } catch (Exception e) {
            throw e;
        }
    }

    private static void deleteItemWithItemID(int itemID, String type, Connection conn) throws Exception {
        try {
            // if the type is itemordered, get the corresponding item type for the itemID
            // and call deleteItemSuper on that
            if (Util.itemIsInItemsOrdered(itemID, conn)) {// this catches things not bassed as itmeordered
                deleteItemSuper(itemID, "itemordered", conn);
            }
            if (type.equals("itemordered")) {// this catches things that were ordered and gets their other super class
                String databaseType = Util.getTypeColumnInItemFromItemID(itemID, conn);
                // convert the database type to the types we use for java program
                String javaType = Util.changeToJavaString(databaseType);
                // delete item super from that type
                deleteItemSuper(itemID, javaType, conn);
            }

            deleteTracksAndChapters(itemID, type, conn);
            if (!type.equals("album"))
                deleteRelationshipWithItemID(itemID, type, conn);
            deleteGenreWithItemID(itemID, conn);
            deleteItemsCheckedOutEntrysByItemID(conn, itemID);
            deleteItemSuper(itemID, type, conn);
            deleteItemBase(itemID, type, conn);
        } catch (Exception e) {
            throw e;
        }
    }

    private static void deleteItemsCheckedOutEntrysByItemID(Connection conn, int itemID)
            throws Exception {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(Maps.deleteItemCheckedoutString);
            stmt.setInt(1, itemID);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static void deleteRelationshipWithItemID(int itemID, String type, Connection conn) throws Exception {
        PreparedStatement stmt = null;
        String[] vals = Maps.itemDeleteRelationshipMap.get(type);
        for (String val : vals) {
            try {
                stmt = conn.prepareStatement(val);
                stmt.setInt(1, itemID);
                stmt.executeUpdate();
            } catch (Exception e) {
                throw e;
            } finally {
                Util.closeStmt(stmt);
            }
        }
    }

    private static void deleteTracksAndChapters(int itemID, String type, Connection conn)
            throws Exception {
        switch (type) {
            case "album":
                deleteTracksWithAlbumID(itemID, conn);
                break;
            case "audiobook":
            case "physicalbook":
                deleteChaptersWithBookID(itemID, type, conn);
                break;
        }
    }

    private static void deleteChaptersWithBookID(int bookID, String type, Connection conn)
            throws Exception {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(Maps.itemDependDeleteMap.get(type));
            stmt.setInt(1, bookID);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static void deleteTracksWithAlbumID(int albumID, Connection conn)
            throws Exception {
        ArrayList<Integer> trackIDs = Searcher.getTracksFromAlbumID(albumID, conn);
        for (Integer trackID : trackIDs) {
            deleteItemWithItemID(trackID, "track", conn);
        }
    }

    private static void deleteItemBase(int itemID, String type, Connection conn) throws Exception {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(Maps.itemDeleteMap.get("item"));
            stmt.setInt(1, itemID);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static void deleteItemSuper(int itemID, String type, Connection conn) throws Exception {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(Maps.itemDeleteMap.get(type));
            stmt.setInt(1, itemID);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }
}
