import java.sql.*;
import java.util.*;

public class Adder {

    public static void addPerson(Connection conn, Scanner scan) throws Exception {
        PreparedStatement stmt = null;
        try {
            int cardID = Util.nextIDFrom("library_card", conn);
            String email = Util.getEmail(scan);
            String fname = Util.getString(scan, "first name");
            String lname = Util.getString(scan, "last name");
            String address = Util.getString(scan, "address");

            String query = Maps.addPersonString;
            stmt = conn.prepareStatement(query);

            stmt.setString(1, email);
            stmt.setString(2, fname);
            stmt.setString(3, lname);
            stmt.setString(4, address);
            stmt.setInt(5, cardID);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Unable to insert person");
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    public static void addItem(Connection conn, Scanner scan) throws Exception {
        System.out.println(
                "Options:\n(album)\n(track)\n(interview)\n(movie)\n(audiobook)\n(physicalbook)\n(audiobookchapter)\n(physicalbookchapter)\n(genre)\nPlease enter the item you're adding:");
        String itemType = scan.nextLine().toLowerCase();
        try {
            switch (itemType) {
                case "album":
                case "track":
                case "interview":
                case "movie":
                case "audiobook":
                case "physicalbook":
                    addItem(itemType, conn, scan);
                    break;
                case "audiobookchapter":
                case "physicalbookchapter":
                    addChapter(itemType, conn, scan);
                    break;
                case "genre":
                    addGenre(conn, scan);
                    break;
                default:
                    System.err.println(itemType + " isn't a valid item type");
            }
        } catch (Exception e) {
            System.out.println("failed to insert item");
            throw e;
        }
    }

    private static int addItem(String item, Connection conn, Scanner scan) throws Exception {
        int newItemID;
        try {
            newItemID = addItemBase(item, conn, scan);
            addItemSuper(item, conn, scan, newItemID);
            if (!item.equals("album")) {
                ArrayList<Integer> cIDs = getOrMakeCreatorsForItem(item, conn, scan);
                for (int i = 0; i < cIDs.size(); i++) {
                    String rType = Maps.itemToRelationshipMap.get(item)[i];
                    addRelationship(rType, cIDs.get(i), newItemID, conn, scan);
                }
            }
            // TODO: add tracks/add chapters
            // addItemGenre(item, conn, scan, newItemID);
        } catch (Exception e) {
            throw e;
        }
        return newItemID;
    }

    private static int addItemBase(String item, Connection conn, Scanner scan) throws Exception {
        PreparedStatement stmt = null;
        int itemID = Util.nextIDFrom("item", conn);
        try {
            String title = Util.getString(scan, "title");
            int year = Util.getInteger(scan, "year released");

            stmt = conn.prepareStatement(Maps.itemAdderMap.get("item"));

            stmt.setInt(1, itemID);
            stmt.setString(2, title);
            stmt.setInt(3, year);
            String insertType = "";
            if (item.equals("itemordered")) {
                insertType = Util.getTypeFromList(scan,
                        Arrays.asList("album", "track", "movie", "interview", "audiobook", "physicalbook"));
                addItemSuper(insertType, conn, scan, itemID);
            } else {
                insertType = item;
            }
            stmt.setString(4, Maps.itemInsertType.get(insertType));

            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
        return itemID;
    }

    private static void addItemSuper(String item, Connection conn, Scanner scan, int newItemID) throws Exception {

        switch (item) {
            case "album":
                addAlbum(conn, scan, newItemID);
                break;
            case "track":
                addTrack(conn, scan, newItemID);
                break;
            case "interview":
                addInterview(conn, scan, newItemID);
                break;
            case "movie":
                addMovie(conn, scan, newItemID);
                break;
            case "audiobook":
                addAudiobook(conn, scan, newItemID);
                break;
            case "physicalbook":
                addPhysicalbook(conn, scan, newItemID);
                break;
            case "itemordered":
                addItemOrdered(conn, scan, newItemID);
                break;
            default:
                System.err.println(item + " isn't a valid new item insert type");
        }

    }

    private static void addItemOrdered(Connection conn, Scanner scan, int newItemID) throws Exception {
        PreparedStatement stmt = null;

        try {
            double price = Util.getPrice(scan, "price of the item");

            int quantity = Util.getInteger(scan, "quantity ordered");

            String arrivalDate = Util.getDate(scan, "Estimated Arrival Date");

            stmt = conn.prepareStatement(Maps.itemAdderMap.get("itemordered"));

            stmt.setInt(1, newItemID);
            stmt.setDouble(2, price);
            stmt.setInt(3, quantity);
            stmt.setString(4, arrivalDate);
            stmt.setNull(5, Types.DATE);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static void addAlbum(Connection conn, Scanner scan, int newItemID) throws Exception {
        PreparedStatement stmt = null;
        try {
            int numSongs = Util.getInteger(scan, "number of songs");
            int lenInMin = Util.getInteger(scan, "length in minutes");
            stmt = conn.prepareStatement(Maps.itemAdderMap.get("album"));

            stmt.setInt(1, numSongs);
            stmt.setInt(2, lenInMin);
            stmt.setInt(3, newItemID);

            stmt.executeUpdate();
            for (int i = 0; i < numSongs; i++) {
                addTrackForAlbum(conn, scan, newItemID);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static void addTrack(Connection conn, Scanner scan, int newItemID) throws Exception {
        PreparedStatement stmt = null;
        try {
            int albumID = getOrMakeAlbumForTrack(conn, scan);
            int lenInSec = Util.getInteger(scan, "length in seconds");

            stmt = conn.prepareStatement(Maps.itemAdderMap.get("track"));

            stmt.setInt(1, lenInSec);
            stmt.setInt(2, albumID);
            stmt.setInt(3, newItemID);

            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static void addTrackForAlbum(Connection conn, Scanner scan, int albumID) throws Exception {
        PreparedStatement stmt = null;
        try {
            int newTrackID = addItemBase("track", conn, scan);
            addTrackOnlyNeedDuration(conn, scan, albumID, newTrackID);

            int cID = getOrMakeCreator("artist", conn, scan);
            addRelationship("performs", cID, newTrackID, conn, scan);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static void addTrackOnlyNeedDuration(Connection conn, Scanner scan, int albumID, int newTrackID)
            throws Exception {
        PreparedStatement stmt = null;
        try {
            int lenInSec = Util.getInteger(scan, "length in seconds");

            stmt = conn.prepareStatement(Maps.itemAdderMap.get("track"));

            stmt.setInt(1, lenInSec);
            stmt.setInt(2, albumID);
            stmt.setInt(3, newTrackID);

            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static int getOrMakeAlbumForTrack(Connection conn, Scanner scan) throws Exception {
        int aNum = -1;
        boolean gotANum = false;
        while (!gotANum) {
            System.out.println(
                    "Do you have an Album in the database? (s to search for an Albums/a to add an Album) q to quit");
            String response = scan.nextLine().toLowerCase();
            switch (response) {
                case "s":
                    aNum = Searcher.pickItem("album", conn, scan);
                    gotANum = true;
                    break;
                case "a":
                    aNum = addItem("album", conn, scan);
                    gotANum = true;
                    break;
                case "q":
                    throw new Exception("User quit during operation!");
                default:
                    System.out.println("Invalid input");
            }
        }
        return aNum;
    }

    private static void getOrMakeBookForChapter() throws Exception {

    }

    private static void addInterview(Connection conn, Scanner scan,
            int newItemID) throws Exception {
        PreparedStatement stmt = null;

        try {

            int lenInMin = Util.getInteger(scan, "length in minutes");

            stmt = conn.prepareStatement(Maps.itemAdderMap.get("interview"));

            stmt.setInt(1, lenInMin);
            stmt.setInt(2, newItemID);

            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static void addMovie(Connection conn, Scanner scan, int newItemID) throws Exception {
        PreparedStatement stmt = null;

        try {
            int runtime = Util.getInteger(scan, "runtime in minutes");

            System.out.println("Please enter the rating");
            String rating = Util.getTypeFromList(scan, Arrays.asList("G", "PG", "PG-13", "R", "NC-17"));

            stmt = conn.prepareStatement(Maps.itemAdderMap.get("movie"));

            stmt.setInt(1, runtime);
            stmt.setString(2, rating);
            stmt.setInt(3, newItemID);

            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static void addAudiobook(Connection conn, Scanner scan,
            int newItemID) throws Exception {
        PreparedStatement stmt = null;

        try {
            int lenInMin = Util.getInteger(scan, "length in minutes");

            stmt = conn.prepareStatement(Maps.itemAdderMap.get("audiobook"));

            stmt.setInt(1, lenInMin);
            stmt.setInt(2, newItemID);

            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static void addPhysicalbook(Connection conn, Scanner scan, int newItemID) throws Exception {
        PreparedStatement stmt = null;

        try {
            int pages = Util.getInteger(scan, "number of pages");

            stmt = conn.prepareStatement(Maps.itemAdderMap.get("physical_book"));

            stmt.setInt(1, pages);
            stmt.setInt(2, newItemID);

            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    public static void addRelationship(String rType, int cID, int iID, Connection conn, Scanner scan)
            throws Exception {
        switch (rType) {
            case "stars":
                addStarsRelationship(cID, iID, conn, scan);
                break;
            case "writes":
            case "interviewed":
            case "performs":
            case "directs":
                addGenericRelationships(cID, iID, conn, scan, rType);
                break;
            default:
                System.err.println(rType + " isn't a valid new relationship insert type");
        }
    }

    public static void addRelationship(Connection conn, Scanner scan) throws Exception {
        System.out.println(
                "Options:\n(stars)\n(writes)\n(interviewed)\n(performs)\n(directs)\nPlease enter the relationship you're adding:");
        String relationshipType = scan.nextLine().toLowerCase();
        try {
            switch (relationshipType) {
                case "stars":
                case "writes":
                case "interviewed":
                case "performs":
                case "directs":
                    addRelationship(relationshipType, conn, scan);
                    break;
                default:
                    System.err.println(relationshipType + " isn't a valid relationship type");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private static void addRelationship(String relationshipType, Connection conn, Scanner scan) throws Exception {
        try {
            switch (relationshipType) {
                case "stars":
                    addStarsRelationship(conn, scan);
                    break;
                case "writes":
                case "interviewed":
                case "performs":
                case "directs":
                    addGenericRelationships(conn, scan, relationshipType);
                    break;
                default:
                    System.err.println(relationshipType + " isn't a valid new relationship insert type");
            }
        } catch (Exception e) {
            System.out.println("Unable to create relationship");
            throw e;
        }
    }

    private static void addStarsRelationship(int cID, int iID, Connection conn, Scanner scan) throws Exception {
        PreparedStatement stmt = null;
        try {
            String role = Util.getString(scan, "role for the actor");
            stmt = conn.prepareStatement(Maps.relationshipAdderMap.get("stars"));

            stmt.setInt(1, cID);
            stmt.setString(2, role);
            stmt.setInt(3, iID);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static void addGenericRelationships(int cID, int iID, Connection conn, Scanner scan,
            String relationshipType) throws Exception {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(Maps.relationshipAdderMap.get(relationshipType));
            stmt.setInt(1, cID);
            stmt.setInt(2, iID);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static void addStarsRelationship(Connection conn, Scanner scan) throws Exception {
        PreparedStatement stmt = null;
        try {
            int creatorID = Searcher.pickCreator("stars", conn, scan);
            int itemID = Searcher.pickItem("movie", conn, scan);
            String role = Util.getString(scan, "role");

            stmt = conn.prepareStatement(Maps.relationshipAdderMap.get("stars"));

            stmt.setInt(1, creatorID);
            stmt.setString(2, role);
            stmt.setInt(3, itemID);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static void addGenericRelationships(Connection conn, Scanner scan, String relationshipType)
            throws Exception {
        PreparedStatement stmt = null;
        try {
            String creatorType = Searcher.getRelationshipCreatorType(relationshipType);
            int creatorID = Searcher.pickCreator(creatorType, conn, scan);
            String itemType = Searcher.getRelationshipItemType(relationshipType, scan);
            int itemID = Searcher.pickItem(itemType, conn, scan);

            stmt = conn.prepareStatement(Maps.relationshipAdderMap.get(relationshipType));
            stmt.setInt(1, creatorID);
            stmt.setInt(2, itemID);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    public static void addCreator(Connection conn, Scanner scan) throws Exception {
        System.out.println(
                "Options:\n(actor)\n(director)\n(artist)\n(writer)\nPlease enter the creator you're adding:");
        String creatorType = scan.nextLine().toLowerCase();
        try {
            switch (creatorType) {
                case "actor":
                case "director":
                case "artist":
                case "writer":
                    addCreator(creatorType, conn, scan);
                    break;
                default:
                    System.err.println(creatorType + " isn't a valid creator type");
            }
        } catch (Exception e) {
            System.out.println("Unable to insert creator");
            throw e;
        }
    }

    private static int addCreator(String creatorType, Connection conn, Scanner scan) throws Exception {
        int newCreatorID;
        try {
            newCreatorID = addCreatorBase(creatorType, conn, scan);
            addCreatorSuper(creatorType, conn, scan, newCreatorID);
        } catch (Exception e) {
            throw e;
        }
        return newCreatorID;
    }

    private static int addCreatorBase(String creatorType, Connection conn, Scanner scan) throws Exception {
        PreparedStatement stmt = null;
        int creatorID = Util.nextIDFrom("creator", conn);
        try {
            stmt = conn.prepareStatement(Maps.creatorAdderMap.get("creator"));
            stmt.setInt(1, creatorID);
            stmt.executeUpdate();
        } catch (Exception e) {
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
            String name = Util.getString(scan, creatorType + " name");
            String dateOfBirth = Util.getDate(scan, "date of birth");

            stmt = conn.prepareStatement(Maps.creatorAdderMap.get(creatorType));

            stmt.setString(1, name);
            stmt.setString(2, dateOfBirth);
            stmt.setInt(3, newCreatorID);

            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static void addChapter(String type, Connection conn, Scanner scan) throws Exception {
        PreparedStatement stmt = null;
        try {
            System.out.println("Please choose the book you would like to add a chapter to: ");
            int ItemID = Searcher.pickChapter(type, conn, scan);
            String chapterName = Util.getString(scan, "chapter name");
            stmt = conn.prepareStatement(Maps.chapterAdderMap.get(type));
            stmt.setString(1, chapterName);
            stmt.setInt(2, ItemID);
            stmt.executeUpdate();

        } catch (Exception e) {
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static void addGenre(Connection conn, Scanner scan) throws Exception {
        System.out.println("Select an entry to add a genre to");
        int itemID = Searcher.pickGenre(conn, scan);
        PreparedStatement stmt = null;
        try {
            String newGenre = Util.getString(scan, "new genre");
            stmt = conn.prepareStatement(Maps.genreAdderMap.get("item"));
            stmt.setInt(1, itemID);
            stmt.setString(2, newGenre);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Unable to insert genre");
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static ArrayList<Integer> getOrMakeCreatorsForItem(String iType, Connection conn, Scanner scan)
            throws Exception {
        ArrayList<Integer> cIDs = new ArrayList<Integer>();
        String[] cTypes = Maps.itemToCreatorMap.get(iType);
        for (String cType : cTypes)
            cIDs.add(getOrMakeCreator(cType, conn, scan));
        return cIDs;
    }

    private static int getOrMakeCreator(String cType, Connection conn, Scanner scan) throws Exception {
        int cID = -1;
        boolean gotCID = false;
        while (!gotCID) {
            System.out.println("Do you have a " + cType + "? (s to search for a " + cType + "/a to add a " + cType
                    + ") q to quit");
            String response = scan.nextLine().toLowerCase();
            switch (response) {
                case "s":
                    cID = Searcher.pickCreator(cType, conn, scan);
                    gotCID = true;
                    break;
                case "a":
                    cID = addCreator(cType, conn, scan);
                    gotCID = true;
                    break;
                case "q":
                    throw new Exception("User quit during operation!");
                default:
                    System.out.println("Invalid input");
            }
        }
        return cID;
    }
}
