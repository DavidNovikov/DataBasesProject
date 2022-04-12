import java.sql.*;
import java.text.*;
import java.util.*;

public class Util {

	public static int startTransaction(Connection conn) {
		PreparedStatement stmt = null;
		int status = 0;
		try {
			stmt = conn.prepareStatement(Maps.startTransactionString);
			stmt.execute();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			status = -1;
		} finally {
			closeStmt(stmt);
		}
		return status;
	}

	public static void endTransaction(Connection conn) {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(Maps.endTransactionString);
			stmt.execute();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeStmt(stmt);
		}
	}

	public static void forceRollBack(Connection conn) {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(Maps.forceRollBackString);
			stmt.execute();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeStmt(stmt);
		}
	}

	public static int nextIDFrom(String tableType, Connection conn) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rSet = null;
		int nextID = -1;
		try {
			tableType = tableType.toLowerCase();
			stmt = conn.prepareStatement(Maps.nextIDMap.get(tableType));
			rSet = stmt.executeQuery();
			nextID = rSet.getInt(Maps.nextIDColumnMap.get(tableType)) + 1;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw e;
		} finally {
			closeStmt(stmt);
			closeRSet(rSet);
		}
		return nextID;
	}

	public static String creatorNameString(String type) {
		type = type.toLowerCase();
		switch (type) {
			case "artist":
			case "actor":
			case "director":
			case "writer":
				return "Ar_Name";
			default:
				// print invalid
				System.err.println(type + " isn't a creator type");
		}
		return null;
	}

	public static void closeStmt(PreparedStatement stmt) {

		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void closeRSet(ResultSet rSet) {
		if (rSet != null) {
			try {
				rSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static String changeToDBString(String request) {
		request = request.toLowerCase();
		// add depending on the type
		switch (request) {
			case "audiobookchapter":
				request = "chapter_ab";
				break;
			case "physicalbookchapter":
				request = "chapter_pb";
				break;
			case "physicalbook":
				request = "physicalbook";
				break;
		}
		return request;
	}

	public static boolean getStatus(Scanner scan) {
		boolean active = false;
		String response = " ";
		while (!(response.equals("y") || response.equals("n"))) {
			System.out.println("is the item active? y/n");
			response = scan.nextLine();
			if (response.equals("y")) {
				active = true;
			} else if (!(response.equals("y") || response.equals("n"))) {
				System.out.println("Invalid input: enter y or n");
			}
		}
		return active;
	}

	public static String getDate(Scanner scan, String dateName) throws Exception {
		boolean successful = false;
		String response = "";
		while (!successful) {
			System.out.println("Enter the " + dateName + " in the form YYYY-MM-DD:");
			response = scan.nextLine();
			if(!dateIsValid(response)){
				System.out.println("Not a valid date!");
			}else{
				successful = true;
			}
		}
		return response;
	}

	private static boolean dateIsValid(String date) {
        try {
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            dateFormatter.setLenient(false);
            dateFormatter.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

	public static ArrayList<Integer> searchPrint(ResultSet rSet, String columnName) throws SQLException {
		ResultSetMetaData rSetmd = rSet.getMetaData();
		int columnCount = rSetmd.getColumnCount();
		ArrayList<Integer> IDmap = new ArrayList<Integer>();
		for (int i = 1; i <= columnCount; i++) {
			String value = rSetmd.getColumnName(i);
			System.out.print(value);
			if (i < columnCount)
				System.out.print(",  ");
		}
		System.out.print("\n");
		int rsetCount = 0;
		while (rSet.next()) {
			rsetCount++;
			for (int i = 1; i <= columnCount; i++) {
				String columnValue = rSet.getString(i);
				if (i == 1) {
					System.out.print("(" + rsetCount + ")");
				}
				System.out.print(columnValue);
				int newID = rSet.getInt(columnName);
				if (!IDmap.contains(newID)) {
					IDmap.add(newID);
				}
				if (i < columnCount)
					System.out.print(",  ");
			}
			System.out.print("\n");
		}
		return IDmap;
	}

	public static String getEmail(Scanner scan) {
		boolean successful = false;
		String response = "";
		while (!successful) {
			System.out.println("Enter the email:");
			response = scan.nextLine();
			int atIndex = response.indexOf("@");
			int dotIndex = response.lastIndexOf(".");
			if (atIndex != -1 && dotIndex != -1 && atIndex < dotIndex) {
				successful = true;
			} else {
				System.out.println("Invalid email!");
			}
		}
		return response;
	}
	
	public static int itemListPick(ArrayList<Integer> IDs, Scanner scan) {
		int flag = 1;
		int newID = -1;
        while(flag == 1) {
        	//TODO throw error when transactions implemented
            System.out.println("What entry would you like to select? enter the number before the entry (1, 2, 3... etc): ");
	        int entry = Integer.parseInt(scan.nextLine());
	        if (entry < 1 || entry > IDs.size()) {
	        	System.out.println("Invalid choice, try again");
	        	
	        } else {
	        	flag = 0;
	        	newID = IDs.get(entry-1);
	        }
        }
        return newID;
	}

	public static String getTypeFromList(Scanner scan, List<String> validTypes) throws Exception{
		boolean successful = false;
		String response = "";
		while (!successful) {
			System.out.print("Enter the type: (");
			for(String s: validTypes){
				System.out.print(s+" ");
			}
			System.out.println(") or q to quit");
			response = scan.nextLine();
			if (validTypes.contains(response)) {
				successful = true;
			} else if(response.equals("q")){
				throw new Exception("User quit during operation!");
			}else{
				System.out.println("Invalid type!");
			}
		}
		return response;
	}

	public static String getTypeColumnInItemFromItemID(int itemID, Connection conn) throws Exception{
		PreparedStatement stmt = null;
		ResultSet rSet = null;
		String type = null;
		try {
			stmt = conn.prepareStatement(Maps.getTypeColumnInItemFromItemIDString);
			stmt.setInt(1,itemID);
			rSet = stmt.executeQuery();
			type = rSet.getString("Type");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw e;
		} finally {
			closeStmt(stmt);
			closeRSet(rSet);
		}
		return type;
	}

	public static String changeToJavaString(String request) {
		request = request.toLowerCase();
		// add depending on the type
		switch (request) {
			case "chapter_ab":
				request = "audiobookchapter";
				break;
			case "chapter_pb":
				request = "physicalbookchapter";
				break;
			case "pbook":
				request = "physicalbook";
				break;
			case "abook":
				request = "audiobook";
				break;
		}
		return request;
	}

	public static boolean itemIsInItemsOrdered(int itemID, Connection conn) throws Exception{
		PreparedStatement stmt = null;
		ResultSet rSet = null;
		boolean result = true;
		try {
			stmt = conn.prepareStatement(Maps.checkItemInOrderedString);
			stmt.setInt(1,itemID);
			rSet = stmt.executeQuery();
			result = rSet.next();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw e;
		} finally {
			closeStmt(stmt);
			closeRSet(rSet);
		}
		return result;
	}
}
