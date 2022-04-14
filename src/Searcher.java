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
                String cName = Util.getString(scan, type + "'s name");

                stmt = conn.prepareStatement(Maps.creatorSearcherMap.get(type));
                stmt.setString(1, cName);
                rSet = stmt.executeQuery();

                if (Util.resultSetContainsData(rSet)) {
                    ArrayList<Integer> potentialIDs = Util.searchPrint(rSet, "creator_ID");
                    creatorID = itemListPick(potentialIDs, scan);
                    found = true;
                } else {
                    System.out.println("None Found");
                }
            } catch (Exception e) {
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
                    "Options:\n(album)\n(track)\n(interview)\n(movie)\n(audiobook)\n(physicalbook)\n(audiobookchapter)\n(physicalbookchapter)\n(genre)\n(q to quit)\nPlease enter the item you're searching for:");
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
                    case "q":
                        throw new Exception("User quit during search");
                    default:
                        System.err.println(itemType + " isn't a valid item type");
                }
            } catch (Exception e) {
                throw e;
            }
        }
        return itemID;
    }

    public static int pickItem(String type, Connection conn, Scanner scan) throws Exception {
        int ItemID = -1;
        PreparedStatement stmt = null;
        ResultSet rSet = null;
        boolean found = false;
        while (!found) {
            try {
                String itemName = Util.getString(scan, type + "'s name");

                stmt = conn.prepareStatement(Maps.itemSearcherMap.get(type));
                stmt.setString(1, itemName);
                rSet = stmt.executeQuery();

                if (Util.resultSetContainsData(rSet)) {
                    ArrayList<Integer> potentialIDs = Util.searchPrint(rSet, "Item_ID");
                    ItemID = itemListPick(potentialIDs, scan);
                    found = true;
                } else {
                    System.out.println("None Found");
                }
            } catch (Exception e) {
                throw e;
            } finally {
                Util.closeStmt(stmt);
                Util.closeRSet(rSet);
            }
        }
        return ItemID;
    }

    public static int pickPerson(Connection conn, Scanner scan) throws Exception {
        int CardID = -1;
        PreparedStatement stmt = null;
        ResultSet rSet = null;
        boolean found = false;
        while (!found) {
            try {
                String email = Util.getString(scan, "Person's email");

                stmt = conn.prepareStatement(Maps.searchPersonString);
                stmt.setString(1, email);
                rSet = stmt.executeQuery();

                if (Util.resultSetContainsData(rSet)) {
                    ArrayList<Integer> potentialIDs = Util.searchPrint(rSet, "CardID");
                    CardID = itemListPick(potentialIDs, scan);
                    found = true;
                } else {
                    System.out.println("None Found");
                }
            } catch (Exception e) {
                throw e;
            } finally {
                Util.closeStmt(stmt);
                Util.closeRSet(rSet);
            }
        }
        return CardID;
    }

    public static int pickChapter(String type, Connection conn, Scanner scan) throws Exception {
        int ItemID = -1;
        PreparedStatement stmt = null;
        ResultSet rSet = null;
        String bookGrab = null;
        boolean found = false;
        while (!found) {
            try {
                if (type.equals("audiobookchapter")) {
                    bookGrab = "audiobook";
                } else if (type.equals("physicalbookchapter")) {
                    bookGrab = "physicalbook";
                }
                ItemID = pickItem(bookGrab, conn, scan);

                stmt = conn.prepareStatement(Maps.chapterSearcherMap.get(type));
                stmt.setInt(1, ItemID);
                rSet = stmt.executeQuery();

                if (Util.resultSetContainsData(rSet)) {
                    Util.searchPrint(rSet, "BookID");
                    found = true;
                } else {
                    System.out.println("None Found");
                }
            } catch (Exception e) {
                throw e;
            } finally {
                Util.closeStmt(stmt);
                Util.closeRSet(rSet);
            }
        }

        return ItemID;
    }

    public static GenreIDPair pickGenre(Connection conn, Scanner scan)throws Exception {
    	ArrayList<Integer> genreList = new ArrayList<Integer>();
    	Map<Integer, String> genreStringMap = new HashMap<>();
    	PreparedStatement stmt = null;
        ResultSet rSet = null;
        PreparedStatement stmt2 = null;
        ResultSet rSet2 = null;
        int itemID = -1;
        boolean listFlag = false;
        GenreIDPair genreIDPair = new GenreIDPair(-1, null);
        
        while(!listFlag) {
        	try {
                String genre = Util.getString(scan, "name of the genre you would like to search for, 1 to list all genres,");
                if (genre.equals("1")) {
                	stmt = conn.prepareStatement(Maps.genreSearcherMap.get("genres"));
                    rSet = stmt.executeQuery();
                    Util.searchPrintNoRet(rSet);
                } else {
                	 stmt = conn.prepareStatement(Maps.genreSearcherMap.get("search"));
                	 stmt2 = conn.prepareStatement(Maps.genreSearcherMap.get("search"));
                     stmt.setString(1, genre);
                     stmt2.setString(1, genre);
                     rSet = stmt.executeQuery();
                     rSet2 = stmt2.executeQuery();
                     
                     if (Util.resultSetContainsData(rSet)) {
                         genreList = Util.searchPrint(rSet, "Item_ID");
                         itemID = itemListPick(genreList, scan); 
                         listFlag = true;
                         if (Util.resultSetContainsData(rSet2)) {
                        	while (rSet2.next()) {
	                        	int genreListIDs = rSet2.getInt("Item_ID");
	                        	String genreNew = rSet2.getString("Genre");
	     		        		genreStringMap.put(genreListIDs, genreNew);
                        	}
                        	genreIDPair.setItemID(itemID);
                        	genreIDPair.setGenre(genreStringMap.get(itemID));
                         }
                     } else {
                    	 System.out.println("None Found");
                     }
                }
        	} catch(Exception e){
        		throw e;
        	} finally {
        		 Util.closeStmt(stmt);
                 Util.closeRSet(rSet);
                 Util.closeStmt(stmt2);
                 Util.closeRSet(rSet2);
        	}
        }
      
      return genreIDPair;
    }
    
    public static Relationship pickRelationship(Connection conn, Scanner scan) throws Exception {
        Relationship relationship = null;
        boolean found = false;
        while (!found) {
            System.out.println(
                    "Options:\n(stars)\n(writes)\n(interviewed)\n(performs)\n(directs)\n(q to quit)\nPlease enter the relationship you're searching for:");
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
                    case "q":
                        throw new Exception("User quit during search");
                    default:
                        System.err.println(relationshipType + " isn't a valid relationship type");
                }
            } catch (Exception e) {
                throw e;
            }
        }
        return relationship;
    }

    public static Relationship pickRelationship(String type, Connection conn, Scanner scan) throws Exception {
        Relationship relationship = new Relationship();
        PreparedStatement stmt = null;
        ResultSet rSet = null;
        boolean found = false;
        while (!found) {
            String itemType = getRelationshipItemType(type, scan);
            String creatorType = getRelationshipCreatorType(type);

            int itemID = pickItem(itemType, conn, scan);
            int creatorID = pickCreator(creatorType, conn, scan);

            relationship.setCreatorID(creatorID);
            relationship.setItemID(itemID);
            try {
                stmt = conn.prepareStatement(Maps.relationshipSearcherMap.get(type));
                stmt.setInt(1, relationship.getCreatorID());
                stmt.setInt(2, relationship.getItemID());
                rSet = stmt.executeQuery();

                if (Util.resultSetContainsData(rSet)) {
                    ArrayList<Relationship> potentialIDs = Util.searchPrintRelationships(rSet);
                    relationship = relationshipListPick(potentialIDs, scan);
                    found = true;
                } else {
                    System.out.println("None Found");
                }
            } catch (Exception e) {
                throw e;
            } finally {
                Util.closeStmt(stmt);
                Util.closeRSet(rSet);
            }
        }
        return relationship;
    }

    public static String getRelationshipCreatorType(String relationshipType) {
        return Maps.relationshipOptionMap.get(relationshipType)[0];
    }

    public static String getRelationshipItemType(String relationshipType, Scanner scan) throws Exception {
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

    public static Relationship relationshipListPick(ArrayList<Relationship> IDs, Scanner scan) throws Exception {
        boolean picked = false;
        Relationship rel = new Relationship();
        while (!picked) {
            System.out.println(
                    "What entry would you like to select? enter the number before the entry (1, 2, 3... etc)(q to quit): ");
            try {
                String response = scan.nextLine();
                if (response.toLowerCase().equals("q"))
                    throw new Exception("User quit during search");

                int entry = Integer.parseInt(response);
                if (entry < 1 || entry > IDs.size()) {
                    System.out.println("Invalid choice, try again");
                } else {
                    picked = true;
                    rel = IDs.get(entry - 1);
                }
            } catch (Exception e) {
                throw e;
            }
        }
        return rel;
    }

    public static int itemListPick(ArrayList<Integer> IDs, Scanner scan) throws Exception {
        boolean picked = false;
        int newID = -1;
        while (!picked) {
            try {
                Integer response = Util.getInteger(scan, "entry you would like to search for (1,2,3...etc)");
                
                if (response < 1 || response > IDs.size()) {
                    System.out.println("Invalid choice, try again");
                } else {
                    picked = true;
                    newID = IDs.get(response - 1);
                }
            } catch (Exception e) {
                throw e;
            }
        }
        return newID;
    }
    
    public static int pickItemCheckedOut(String type, Connection conn, Scanner scan) throws Exception {
    	ArrayList<String> allReturnDates = new ArrayList<String>();
    	int itemID = -1;
    	PreparedStatement stmt = null;
    	ResultSet rSet = null;
    	itemID = Searcher.pickItem(type, conn, scan);
    	try {
    		String sql = Maps.searchItemCheckoutsString;
    		stmt = conn.prepareStatement(sql);
    		stmt.setInt(1, itemID);
    		
    		rSet = stmt.executeQuery();
    		
    		while (rSet.next()) {
    			
    			String returnDate = rSet.getString("Returned_Date");
    			if (returnDate == null) {
    				returnDate = "";
    			}
    			allReturnDates.add(returnDate);
    			itemID = rSet.getInt("ItemID");
    	    	
    		}
    	} catch (Exception e) {
    		throw e;
    	} finally {
    		Util.closeRSet(rSet);
            Util.closeStmt(stmt);            
        }
    	Maps.checkoutReturnDates = allReturnDates;
    	

    	return itemID;
    }

}
