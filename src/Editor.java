import java.sql.*;
import java.util.*;

public class Editor {
    public static String getRelationshipCreatorType(String relationshipType){
        return Maps.relationshipOptionMap.get(relationshipType)[0];
    }

    public static String getRelationshipItemType(String relationshipType, Scanner scan){
        if(Maps.relationshipOptionMap.get(relationshipType).length>2){
            //there are multiple options for item type
            String [] optionArray = Maps.relationshipOptionMap.get(relationshipType);
            int option = 0;
            while (option == 0) {
                for(int i = 1; i<optionArray.length; i++){
                    System.out.println("Enter "+i+" if the item type is "+ optionArray[i]);
                }
                option = Integer.valueOf(scan.nextLine());
                if (!(option == 1 || option == 2)){
                    option = 0;
                }
            }
            return Maps.relationshipOptionMap.get(relationshipType)[option];
        }else{
            //there is just one item type
            return Maps.relationshipOptionMap.get(relationshipType)[1];
        }
    }

    public static void editRelationship(String relationshipType, Connection conn, Scanner scan) {
        //ask if item type is audiobook or album
        String itemType = getRelationshipItemType(relationshipType, scan);
        String creatorType = getRelationshipCreatorType(relationshipType);

        int itemID = Searcher.pickItem(itemType, conn, scan); 
        int creatorID = Searcher.pickCreator(creatorType, conn, scan); 
        PreparedStatement stmt = null;
        try {
            int editing = 0;
            switch(relationshipType){
                case "stars":
                    editing = whatToEditRelationshipStars(scan);
                    break;
                case "writes":
                case "interviewed":
                case "performs":
                case "directs":
                    editing = whatToEditRelationshipGeneric(scan);
                    break;
                default:
                    System.out.println(relationshipType+" is an invalid input");
            }
            stmt = conn.prepareStatement(Maps.relationshipEditorMap.get(relationshipType)[editing - 1]);
            switch(relationshipType){
                case "stars":
                    stmt = editRelationshipExecuteStars(stmt, scan, editing);
                    break;
                case "writes":
                case "interviewed":
                case "performs":
                case "directs":
                    stmt = editRelationshipExecuteGeneric(stmt, scan, editing);
                    break;
                default:
                    System.out.println(relationshipType+" is an invalid input");
            }

            stmt.setInt(2, creatorID);
            stmt.setInt(3, itemID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static PreparedStatement editRelationshipExecuteGeneric(PreparedStatement stmt, Scanner scan, int editing){
        try{
            switch (editing) {
                case 1:
                    System.out.println("enter the new " + "creatorID");
                    int newValue = Integer.parseInt(scan.nextLine());
                    stmt.setInt(1, newValue);
                    break;
                case 2:
                    System.out.println("enter the new " + "itemID");
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

    private static PreparedStatement editRelationshipExecuteStars(PreparedStatement stmt, Scanner scan, int editing){
        try{
            switch (editing) {
                case 1:
                    System.out.println("enter the new " + "creatorID");
                    int newValue = Integer.parseInt(scan.nextLine());
                    stmt.setInt(1, newValue);
                    break;
                case 2:
                    System.out.println("enter the new " + "itemID");
                    int newValue2 = Integer.parseInt(scan.nextLine());
                    stmt.setInt(1, newValue2);
                    break;
                case 3:
                    System.out.println("enter the new " + "role");
                    String newRole = scan.nextLine();
                    stmt.setString(1, newRole);
                    break;
            }
            return stmt;
        }catch(Exception e){
            System.out.println("Exception entering new value:"+e);
            return null;
        }  

    }

    private static int whatToEditRelationshipGeneric(Scanner scan) {
        int option = 0;
        while (option == 0) {
            System.out.println("Please enter 1 to edit the creatorID, 2 to edit the itemID:");
            option = Integer.valueOf(scan.nextLine());
            if (!(option == 1 || option == 2))
                option = 0;
        }
        return option;
    }

    private static int whatToEditRelationshipStars(Scanner scan) {
        int option = 0;
        while (option == 0) {
            System.out.println("Please enter 1 to edit the creatorID, 2 to edit the itemID, 3 to edit the role:");
            option = Integer.valueOf(scan.nextLine());
            if (!(option == 1 || option == 2 || option == 3))
                option = 0;
        }
        return option;
    }

    public static void editPerson(String type, Connection conn, Scanner scan) {
        int cardID = Searcher.pickPerson(conn, scan);
        PreparedStatement stmt = null;
        try {
            int editing = whatToEditPerson(scan);
            stmt = conn.prepareStatement(Maps.personEditorMap.get(type)[editing - 1]);
            //1 for email, 2 for fname, 3 for lname, 4 for address
            switch (editing) {
                case 1:
                case 2:
                case 3:
                case 4:
                    System.out.println("enter the new value:");
                    String newValue = scan.nextLine();
                    stmt.setString(1, newValue);
                    break;
                default:
                    System.out.println("Invalid editing option!");
            }
            stmt.setInt(2, cardID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            Util.closeStmt(stmt);
        }
    }

    private static int whatToEditPerson(Scanner scan) {
        int option = 0;
        while (option == 0) {
            System.out.println("Please enter 1 to edit the email, 2 to edit the first name, 3 to edit the last name, 4 to edit the address");
            option = Integer.valueOf(scan.nextLine());
            if (!(option == 1 || option == 2|| option == 3|| option == 4))
                option = 0;
        }
        return option;
    }
    
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
}
