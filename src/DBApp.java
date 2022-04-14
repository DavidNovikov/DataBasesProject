import java.util.*;
import java.sql.*;

public class DBApp {
    final String CONNECTIONPATH = "jdbc:sqlite:LIBRARY.db";

    // use a scanner to read input
    private Scanner scan;

    // connection and statement variables
    private Connection conn;

    public DBApp() {
        scan = new Scanner(System.in);
        conn = null;
    }

    public void search() throws Exception {
        // Ask the user which record they want to edit
        System.out.println(
                "Searching options:\n(Media)\n(Creator)\n(Person)\n(Relationship)\n(audiobookchapter)\n(physicalbookchapter)\n(genre)\n(q to quit)\nPlease select one:");
        String type = scan.nextLine().toLowerCase();

        switch (type) {
            case "media":
                Searcher.pickItem(conn, scan);
                break;
            case "creator":
                Searcher.pickCreator(conn, scan);
                break;
            case "person":
                Searcher.pickPerson(conn, scan);
                break;
            case "relationship":
                Searcher.pickRelationship(conn, scan);
                break;
            case "audiobookchapter":
            case "physicalbookchapter":
                Searcher.pickChapter(type, conn, scan);
                break;
            case "genre":
                Searcher.pickGenre(conn, scan);
                break;
            case "q":
                break;
            default:
                // print invalid
                System.out.println(type + " is an Invalid input");
        }
    }

    public void add() throws Exception {
        // ask for which type of item to add
        System.out.println(
                "Adding options:\n(Media)\n(Creator)\n(Person)\n(Relationship)\n(q to quit)\nPlease select one:");
        // get the type
        String type = scan.nextLine().toLowerCase();
        // add depending on the type
        switch (type) {
            case "media":
                Adder.addItem(conn, scan);
                break;
            case "creator":
                Adder.addCreator(conn, scan);
                break;
            case "person":
                Adder.addPerson(conn, scan);
                break;
            case "relationship":
                Adder.addRelationship(conn, scan);
            case "q":
                break;
            default:
                // print invalid
                System.out.println(type + " is an Invalid input");
        }

    }

    public void delete() throws Exception {
        // Ask the user which record they want to delete
        System.out.println(
                "Deleting options:\n(Media)\n(Creator)\n(Person)\n(Relationship)\n(Checkout Record)\n(q to quit)\nPlease select one:");
        String type = scan.nextLine().toLowerCase();

        switch (type) {
            case "media":
                Deleter.deleteItem(conn, scan);
                break;
            case "creator":
                Deleter.deleteCreator(conn, scan);
                break;
            case "person":
                Deleter.deletePerson(conn, scan);
                break;
            case "relationship":
                Deleter.deleteRelationship(conn, scan);
                break;
            case "checkoutrecord":
            	Deleter.deleteItemCheckedOut(conn,scan);
            case "q":
                break;
            default:
                // print invalid
                System.out.println(type + " is an Invalid input");
        }
    }

    public void edit() throws Exception {
        // Ask the user which record they want to edit
        System.out.println(
                "Editing options:\n(Media)\n(Creator)\n(Person)\n(Relationship)\n(Checkout Record)\n(q to quit)\nPlease select one:");
        String type = scan.nextLine().toLowerCase();

        switch (type) {
            case "media":
                Editor.editItem(conn, scan);
                break;
            case "creator":
                Editor.editCreator(conn, scan);
                break;
            case "person":
                Editor.editPerson(conn, scan);
                break;
            case "relationship":
                Editor.editRelationship(conn, scan);
                break;
            case "checkout record":
            	Editor.editItemCheckedOut(conn, scan);
            case "q":
                break;
            default:
                // print invalid
                System.out.println(type + " is an Invalid input");
        }
    }
    
    public void checkout() throws Exception {
        // Ask the user what type of record they want to check out
        System.out.println(
                "Enter the type of the record to be checked out (Album, Track, Interview, Movie, Audiobook, or PhysicalBook): ");
        String type = scan.nextLine().toLowerCase();
        try {
            switch (type) {
            case "album":
            case "track":
            case "interview":
            case "movie":
            case "audiobook":
            case "physicalbook":
                Adder.addCheckoutItem(type,conn,scan);
                break;
            default:
                // print invalid
                System.out.println(type + " is an Invalid input");
            }
        } catch (Exception e) {
            System.out.println("failed to insert");
        }
        
    }

    private void execute() {
        char input = 'h';
        while (input != 'q') {
            // ask the user for input
            System.out.println(
                    "Enter 'a' to add new records, 'e' to edit a record, 's' to search for a record, 'd' to delete a record, 'c' to check out a record, 'o' to manage orders, 'p' to manage library cards/patrons, and 'q' to quit.");

            // get user input
            input = scan.nextLine().charAt(0);

            // do the input based on what they put in
            if (Util.startTransaction(conn) == 0) {
                try {
                    switch (input) {
                        case 'a':
                            // ask the user for a record to add and add it to the list
                            add();
                            break;
                        case 'e':
                            // ask the user which record to edit and change it in the list
                            edit();
                            break;
                        case 's':
                            // search for a record and print it
                            search();
                        case 'c':
                        	//ask the user for a record to checkout and add it to the list of items checked out.
                            checkout();
                            break;
                        case 'd':
                            delete();
                            break;
                        case 'o':
                            // TODO: order/edit/delete records
                            break;
                        case 'p':
                            // TODO: manage people func
                            break;
                        case 'q':
                            // quit the program
                            break;
                        default:
                            // print invalid
                            System.out.println(input + " is Invalid input");
                    }
                    Util.endTransaction(conn);
                } catch (Exception e) {
                    Util.forceRollBack(conn);
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    private void getConnection() throws Exception {
        conn = DriverManager.getConnection(CONNECTIONPATH);
    }

    private void cleanUp() {
        try {
            if (conn != null)
                conn.close();
        } catch (Exception e) {
            // connection close failed.
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        DBApp app = new DBApp();
        try {
            // create a database connection
            app.getConnection();
            // These can be uncommented to perform a sanity check that you're connecting to
            // the db correctly
            try {
                System.out.println(Util.nextIDFrom("Item", app.conn));
                System.out.println(Util.nextIDFrom("creator", app.conn));
                System.out.println(Util.nextIDFrom("library_card", app.conn));
            } catch (Exception e) {
                e.printStackTrace();
            }
            app.execute();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            app.cleanUp();
        }
    }

}