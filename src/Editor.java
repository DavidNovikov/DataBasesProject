import java.sql.*;
import java.util.*;

public class Editor {
    public static void editCreator(String type, Connection conn, Scanner scan) {
        int creatorID = Searcher.pickCreator(type, conn, scan);
        PreparedStatement stmt = null;
        try {
            int editing = whatToEditCreator(scan);

            stmt = conn.prepareStatement(Maps.creatorEditorMap.get(type)[editing - 1]);

            switch (editing) {
                case 1:
                    System.out.println("enter the new value");
                    String newName = scan.nextLine();
                    stmt.setString(1, newName);
                    break;
                case 2:
                    String dateOfBirth = Util.getDate(scan, "date of birth");
                    stmt.setString(1, dateOfBirth);
                    break;
            }
            stmt.setInt(2, creatorID);
            stmt.executeUpdate();
        } catch (SQLException | NumberFormatException e) {
            System.out.println(e.getMessage());
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static int whatToEditCreator(Scanner scan) {
        int option = 0;
        while (option == 0) {
            System.out.println("Please enter 1 to edit the artist name, 2 to edit the artist dob");
            option = Integer.valueOf(scan.nextLine());
            if (!(option == 1 || option == 2))
                option = 0;
        }
        return option;
    }

    public static void editItem(String type, Connection conn, Scanner scan) {
        int itemID = Searcher.pickItem(type, conn, scan);
        PreparedStatement stmt = null;
        try {
            int editing = 0;
            switch(type){
                case "album":
                    editing = whatToEditItemGeneric(scan,"number of songs","number of minutes");
                    break;
                case "track":
                    editing = whatToEditItemGeneric(scan,"number of seconds","album ID");
                    break;
                case "interview":
                    editing = 1;
                    break;
                case "movie":
                    editing = whatToEditItemGeneric(scan,"runtime","rating");
                    break;
                case "audiobook":
                    editing = whatToEditItemGeneric(scan,"number of chapters","number of minutes");
                    break;
                case "physicalbook":
                    editing = whatToEditItemGeneric(scan,"number of chapters","number of pages");
                    break;
                default:
                    // print invalid
                    System.out.println(type + " is an Invalid input");
            }
            

            stmt = conn.prepareStatement(Maps.itemEditorMap.get(type)[editing - 1]);

            
            switch(type){
                case "album":
                    stmt = editItemExecuteGeneric(stmt, scan, editing,"number of songs","number of minutes");
                    break;
                case "track":
                    stmt = editItemExecuteGeneric(stmt, scan, editing,"number of seconds","album ID");
                    break;
                case "interview":
                    stmt = editItemExecuteSingle(stmt, scan, editing,"number of minutes");
                    break;
                case "movie":
                    stmt = editItemExecuteIntString(stmt, scan, editing,"runtime","rating");
                    break;
                case "audiobook":
                    stmt = editItemExecuteGeneric(stmt, scan, editing,"number of chapters","number of minutes");
                    break;
                case "physicalbook":
                    stmt = editItemExecuteGeneric(stmt, scan, editing,"number of chapters","number of pages");
                    break;
                default:
                    // print invalid
                    System.out.println(type + " is an Invalid input");
            }
            stmt.setInt(2, itemID);
            stmt.executeUpdate();
        } catch (SQLException | NumberFormatException e) {
            System.out.println(e.getMessage());
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static int whatToEditItemGeneric(Scanner scan, String option1, String option2) {
        int option = 0;
        while (option == 0) {
            System.out.println("Please enter 1 to edit "+option1+", 2 to edit the "+option2);
            option = Integer.valueOf(scan.nextLine());
            if (!(option == 1 || option == 2))
                option = 0;
        }
        return option;
    }

    private static PreparedStatement editItemExecuteGeneric(PreparedStatement stmt, Scanner scan, int editing, String option1, String option2){
        try{
            switch (editing) {
                case 1:
                    System.out.println("enter the new " + option1);
                    int newValue = Integer.parseInt(scan.nextLine());
                    stmt.setInt(1, newValue);
                    break;
                case 2:
                    System.out.println("enter the new " + option2);
                    int newValue2 = Integer.parseInt(scan.nextLine());
                    stmt.setInt(1, newValue2);
                    break;
            }
            return stmt;
        }catch(Exception e){
            System.out.println("Exception entering new value:"+e);
            return null;
        }  
            
    }

    //use this one for movie because we have a string we want to edit
    private static PreparedStatement editItemExecuteIntString(PreparedStatement stmt, Scanner scan, int editing, String option1, String option2){
        try{
            switch (editing) {
                case 1:
                    System.out.println("enter the new " + option1);
                    int newValue = Integer.parseInt(scan.nextLine());
                    stmt.setInt(1, newValue);
                    break;
                case 2:
                    System.out.println("enter the new " + option2);
                    String newValue2 = scan.nextLine();
                    stmt.setString(1, newValue2);
                    break;
            }
            return stmt;
        }catch(Exception e){
            System.out.println("Exception entering new value:"+e);
            return null;
        }      
    }

    //use this one for interview because theres just one thing to edit
    private static PreparedStatement editItemExecuteSingle(PreparedStatement stmt, Scanner scan, int editing, String option1){
        try{
            switch (editing) {
                case 1:
                    System.out.println("enter the new " + option1);
                    int newValue = Integer.parseInt(scan.nextLine());
                    stmt.setInt(1, newValue);
                    break;
            }
            return stmt;
        }catch(Exception e){
            System.out.println("Exception entering new value:"+e);
            return null;
        }      
    }

}
