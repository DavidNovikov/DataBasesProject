import java.sql.*;
import java.util.*;

public class Searcher {
    public static int pickCreator(String type, Connection conn, Scanner scan) throws Exception {
        int creatorID;
        PreparedStatement stmt = null;
        ResultSet rSet = null;
        try {
            System.out.println("Please enter the " + type + "'s name");
            String cName = scan.nextLine();

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
        return creatorID;
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


            try {
                if(type.equals("physicalbook")) {
                    type = "physical_book";
                }
                stmt = conn.prepareStatement(Maps.itemSearcherMap.get(type));
                stmt.setString(1, itemName);
                

                rSet = stmt.executeQuery();
                ArrayList<Integer> potentialIDs = Util.searchPrint(rSet, "Item_ID");
                ItemID = Util.itemListPick(potentialIDs, scan);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                Util.closeStmt(stmt);
                Util.closeRSet(rSet);
            }
            rSet = stmt.executeQuery();
            ArrayList<Integer> potentialIDs = Util.searchPrint(rSet, "Item_ID");


            System.out.println(
                    "What entry would you like to select? enter the number before the entry (1, 2, 3... etc): ");
            int entry = Integer.parseInt(scan.nextLine());
            ItemID = potentialIDs.get(entry - 1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
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
    		if(type.equals("audiobookchapter")) {
    			bookGrab = "audiobook";
    		} else if(type.equals("physicalbookchapter")) {
    			bookGrab = "physicalbook";
    		}
    		System.out.println("Please select a " + bookGrab + " to search a chapter from: ");
    		ItemID = pickItem(bookGrab, conn, scan);
    		stmt = conn.prepareStatement(Maps.chapterSearcherMap.get(type));
    		stmt.setInt(1, ItemID);
    		rSet = stmt.executeQuery();
    		Util.searchPrint(rSet, "BookID");
    	}catch (Exception e) {
        System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
            Util.closeRSet(rSet);
        }
        return ItemID;
    }
    
    public static int pickGenre(Connection conn, Scanner scan)throws Exception {
    	ArrayList<Integer> genreList = new ArrayList<Integer>();
    	PreparedStatement stmt = null;
        ResultSet rSet = null;
        int genreID = -1;
        boolean listFlag = true;
        
        System.out.println("Enter the name of the genre you would like to search for, or 1 to list all genres:");
        String genre = scan.nextLine();
        
        try {
        	while(listFlag) {
		        if (genre.equals("1")) {
		        	stmt = conn.prepareStatement(Maps.genreSearcherMap.get("genres"));
		        	rSet = stmt.executeQuery();
		        	Util.searchPrintNoRet(rSet);
		        	System.out.println("Enter the name of the genre you would like to search for, or 1 to list all genres:");
		            genre = scan.nextLine();
		        }else {
		        	stmt = conn.prepareStatement(Maps.genreSearcherMap.get("search"));
		        	stmt.setString(1, genre);
		        	rSet = stmt.executeQuery();
		        	genreList = Util.searchPrint(rSet, "Item_ID");
		        	if (genreList.size() !=0 ) {
			        	System.out.println("What entry would you like to select? enter the number before the entry (1, 2, 3... etc): ");
		    	        int entry = Integer.parseInt(scan.nextLine());
		    	        genreID = genreList.get(entry-1);
		    	        listFlag = false;
		    	        
		        	} else {
		        		System.out.println("Query returned no entries");
		        		System.out.println("Enter the name of the genre you would like to search for, or 1 to list all genres:");
			            genre = scan.nextLine();
		        	}
	    	        
		        }
		        
        	} 
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            Util.closeStmt(stmt);
            Util.closeRSet(rSet);
        }
      return genreID;
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
