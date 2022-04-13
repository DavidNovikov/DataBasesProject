import java.sql.*;
import java.util.*;

public class Adder {

    public static void addPerson(Connection conn, Scanner scan) throws Exception {
        PreparedStatement stmt = null;
        try {
            int cardID = Util.nextIDFrom("library_card", conn);
            // get the email
            String email = Util.getEmail(scan);

            System.out.println("Please enter the first name");
            String fname = scan.nextLine();

            System.out.println("Please enter the last name");
            String lname = scan.nextLine();

            System.out.println("Please enter the address");
            String address = scan.nextLine();

            String query = Maps.addPersonString;
            stmt = conn.prepareStatement(query);

            stmt.setString(1, email);
            stmt.setString(2, fname);
            stmt.setString(3, lname);
            stmt.setString(4, address);
            stmt.setInt(5, cardID);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    public static void addItem(String item, Connection conn, Scanner scan) throws Exception {
        int newItemID;
        try {
        	boolean genreFlag = true;
            newItemID = addItemBase(item, conn, scan);
            addItemSuper(item, conn, scan, newItemID);
            
            while (genreFlag) {
            	System.out.println("Enter 'q' to stop adding genres, or 'y' to add a genre");
                String choice = scan.nextLine();
	            switch (choice) {
	            case "y":
	            	addGenreBase(newItemID, conn, scan);
	            	break;
	            case "q":
	            	genreFlag = false;
	            	break;
	            default:
	            	System.out.println("Invalid choice, try again");
	            }
            }
        } catch (Exception e) {
            System.out.println("failed to insert");
            throw e;
        }
    }

    private static int addItemBase(String item, Connection conn, Scanner scan) throws Exception {
        PreparedStatement stmt = null;

        int itemID = Util.nextIDFrom("item", conn);

        try {

            System.out.println("Please enter the title");
            String title = scan.nextLine();

            int year = Util.getInteger(scan, "year released");

            stmt = conn.prepareStatement(Maps.itemAdderMap.get("item"));

            stmt.setInt(1, itemID);
            stmt.setString(2, title);
            stmt.setInt(3, year);
            String insertType = "";
            if(item.equals("itemordered")){
                insertType = Util.getTypeFromList(scan, Arrays.asList("album", "track", "movie", "interview", "audiobook", "physicalbook"));
                addItemSuper(insertType, conn, scan, itemID);
            }else{
                insertType = item;
            }
            stmt.setString(4, Maps.itemInsertType.get(insertType));

            stmt.executeUpdate();
        } catch (Exception e) {
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
            case "itemordered":
                addItemOrdered(conn, scan, newItemID);
                break;
            default:
                System.err.println(item + " isn't a valid new item insert type");
        }

    }

    private static void addItemOrdered(Connection conn, Scanner scan, int newItemID) throws Exception{
        PreparedStatement stmt = null;

        try {
            double price = Util.getPrice(scan, "price of the item");

            int quantity = Util.getInteger(scan, "quantity ordered");

            String arrivalDate = Util.getDate(scan, "Estimated Arrival Date");

            stmt = conn.prepareStatement(Maps.itemAdderMap.get("itemordered"));

            stmt.setInt(1,newItemID);
            stmt.setDouble(2, price);
            stmt.setInt(3, quantity);
            stmt.setString(4, arrivalDate);
            stmt.setNull(5, Types.DATE);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static void addAlbum(String item, Connection conn, Scanner scan, int newItemID) throws Exception {
        PreparedStatement stmt = null;

        try {
            int numSongs = Util.getInteger(scan, "number of songs");

            int lenInMin = Util.getInteger(scan, "length in minutes");

            stmt = conn.prepareStatement(Maps.itemAdderMap.get("album"));

            stmt.setInt(1, numSongs);
            stmt.setInt(2, lenInMin);
            stmt.setInt(3, newItemID);

            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static void addTrack(String item, Connection conn, Scanner scan, int newItemID) throws Exception {
        PreparedStatement stmt = null;

        try {
            int albumID = Util.getInteger(scan, "AlbumID corresponding to this track");

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

    private static void addInterview(String item, Connection conn, Scanner scan,
            int newItemID) throws Exception {
        PreparedStatement stmt = null;

        try {

            int lenInMin = Util.getInteger(scan, "length in minutes");

            stmt = conn.prepareStatement(Maps.itemAdderMap.get("interview"));

            stmt.setInt(1, lenInMin);
            stmt.setInt(2, newItemID);

            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static void addMovie(String item, Connection conn, Scanner scan, int newItemID) throws Exception {
        PreparedStatement stmt = null;

        try {
            int runtime = Util.getInteger(scan, "runtime in minutes");

            System.out.println("Please enter the rating");
            String rating = Util.getTypeFromList(scan, Arrays.asList("G","PG","PG-13","R","NC-17"));

            stmt = conn.prepareStatement(Maps.itemAdderMap.get("movie"));

            stmt.setInt(1, runtime);
            stmt.setString(2, rating);
            stmt.setInt(3, newItemID);

            stmt.executeUpdate();
        } catch (Exception e) {
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
            int lenInMin = Util.getInteger(scan, "length in minutes");

            stmt = conn.prepareStatement(Maps.itemAdderMap.get("audiobook"));

            stmt.setInt(1, lenInMin);
            stmt.setInt(2, newItemID);

            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static void addPhysicalbook(String item, Connection conn, Scanner scan, int newItemID) throws Exception {
        PreparedStatement stmt = null;

        try {
            int pages = Util.getInteger(scan, "number of pages");

            stmt = conn.prepareStatement(Maps.itemAdderMap.get("physical_book"));

            stmt.setInt(1, pages);
            stmt.setInt(2, newItemID);

            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    public static void addRelationship(Connection conn, Scanner scan) throws Exception {
        System.out.println(
                "Enter the type of relationship you are adding: (stars, writes, interviewed, performs, or directs)");
        String relationshipType = scan.nextLine().toLowerCase();

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
            System.out.println("Unable to create relationship. Exception:" + e);
            throw e;
        }
    }

    public static void addStarsRelationship(Connection conn, Scanner scan) throws Exception {
        PreparedStatement stmt = null;

        try {
            int creatorID = Searcher.pickCreator("stars", conn, scan);

            int itemID = Searcher.pickItem("movie", conn, scan);

            System.out.println("Please enter the role");
            String role = scan.nextLine();

            stmt = conn.prepareStatement(Maps.relationshipAdderMap.get("stars"));

            stmt.setInt(1, creatorID);
            stmt.setString(2, role);
            stmt.setInt(3, itemID);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    public static void addGenericRelationships(Connection conn, Scanner scan, String relationshipType)
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
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }

    public static void addCreator(String creatorType, Connection conn, Scanner scan) throws Exception {
        int newCreatorID;
        try {
            newCreatorID = addCreatorBase(creatorType, conn, scan);
            addCreatorSuper(creatorType, conn, scan, newCreatorID);
        } catch (Exception e) {
            System.out.println("failed to insert");
            throw e;
        }
    }

    private static int addCreatorBase(String creatorType, Connection conn, Scanner scan) throws Exception {
        PreparedStatement stmt = null;

        int creatorID = Util.nextIDFrom("creator", conn);

        try {

            stmt = conn.prepareStatement(Maps.creatorAdderMap.get("creator"));

            stmt.setInt(1, creatorID);

            stmt.executeUpdate();
        } catch (Exception e) {
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
    }
    

    public static void addChapter(String type, Connection conn, Scanner scan) throws Exception {
    	PreparedStatement stmt = null;
    	try {
    		System.out.println("Please choose the book you would like to add a chapter to: ");
    		int ItemID = Searcher.pickChapter(type, conn, scan);
    		System.out.println("What is the name of the chapter that you would like to add?");
    		String chapterName = scan.nextLine();
    		stmt = conn.prepareStatement(Maps.chapterAdderMap.get(type));
    		stmt.setString(1, chapterName);
    		stmt.setInt(2, ItemID);
    		stmt.executeUpdate();
    		
    	} catch(Exception e) {
    		System.out.println(e.getMessage());
            throw e;
    	}finally {
            Util.closeStmt(stmt);
        }
}
    public static void addGenre(Connection conn, Scanner scan) throws Exception {
    	System.out.println("Select an entry to add a genre to");
    	GenreIDPair genreID = Searcher.pickGenre(conn, scan);
    	int itemID = genreID.getItemID();
    	try {
	        addGenreBase(itemID, conn, scan);
        } catch (Exception e) {
            System.out.println("Failed to insert");
            throw e;
        }
    }
    private static void addGenreBase(int itemID, Connection conn, Scanner scan) throws Exception {
    	PreparedStatement stmt = null;
    	try {
	        System.out.println("What is the new genre?");
	        String newGenre = scan.nextLine();
	        stmt = conn.prepareStatement(Maps.genreAdderMap.get("item"));
	        stmt.setInt(1, itemID);
	        stmt.setString(2, newGenre);
	        stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
        }
	}
}
