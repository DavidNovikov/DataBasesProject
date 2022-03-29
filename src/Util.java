import java.sql.*;
import java.util.*;

public class Util {
	public static int nextIDFrom(String tableType, Connection conn) {
		PreparedStatement stmt = null;
		ResultSet rSet = null;
		int nextID = -1;
		try {
			stmt = conn.prepareStatement("SELECT Max(" + idName(tableType) + ") FROM " + tableType + ";");
			rSet = stmt.executeQuery();
			nextID = rSet.getInt("Max(" + idName(tableType) + ")") + 1;
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
				request = "physical_book";
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
}
