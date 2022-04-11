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
}
