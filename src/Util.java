import java.sql.*;
import java.util.*;

public class Util {
	public static int nextIDFrom(String tableType, Connection conn) {
		PreparedStatement stmt = null;
		ResultSet rSet = null;
		int nextID = -1;
		try {
			tableType = tableType.toLowerCase();
			stmt = conn.prepareStatement(Maps.nextIDMap.get(tableType));
			rSet = stmt.executeQuery();
			nextID = rSet.getInt(Maps.nextIDColumnMap.get(tableType)) + 1;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeStmt(stmt);
			closeRSet(rSet);
		}
		return nextID;
	}

	private static String idName(String type) {
		type = type.toLowerCase();
		switch (type) {
			case "item":
				return "Item_ID";
			case "creator":
				return "creator_ID";
			case "library_card":
				return "cardID";
			default:
				// print invalid
				System.err.println(type + " isn't a table type");
		}
		return null;
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

	public static String getTypeForInsert(String type) {
		type = type.toLowerCase();
		// add depending on the type
		switch (type) {
			case "album":
				type = "ALBUM";
				break;
			case "track":
				type = "Track";
				break;
			case "interview":
				type = "interview";
				break;
			case "movie":
				type = "MOVIE";
				break;
			case "audiobook":
				type = "ABook";
				break;
			case "physicalbook":
				type = "PBook";
				break;
			default:
				System.err.println(type + " isn't a type");
		}
		return type;
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

	public static String getDate(Scanner scan, String dateName) {
		boolean successful = false;
		String response = "";
		while (!successful) {
			System.out.println("Enter the " + dateName + " in the form YYYY-MM-DD:");
			response = scan.nextLine();
			String[] date = response.split("-");
			if (date.length != 3) {
				System.out.println("Date does not have a year, month, and day");
			} else if (date[0].length() != 4) {
				System.out.println("Year needs to be 4 digits");
			} else if (date[1].length() != 2) {
				System.out.println("Month needs to be 4 digits");
			} else if (date[2].length() != 2) {
				System.out.println("Day needs to be 2 digits");
			} else {
				try {
					int year = Integer.parseInt(date[0]);
					int month = Integer.parseInt(date[1]);
					int day = Integer.parseInt(date[2]);
					// if these all work, it is successful
					successful = true;
				} catch (NumberFormatException e) {
					System.out.println("Either the year, month, or day is not a valid number.");
				}
			}
		}
		return response;
	}
}
