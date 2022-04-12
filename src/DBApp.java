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
                "Enter the type of the record to be searched for (Album, Track, Interview, Movie, Audiobook, AudiobookChapter, PhysicalBook, PhysicalBookChapter, Actor, Artist, Director, Writer, or Person): ");
        String type = scan.nextLine().toLowerCase();

        switch (type) {
            case "album":
            case "track":
            case "interview":
            case "movie":
            case "audiobook":
            case "audiobookchapter":
            case "physicalbookchapter":
            case "physicalbook":
                // TODO: search for item
                Searcher.pickItem(type, conn, scan);
                break;
            case "actor":
            case "director":
            case "artist":
            case "writer":
                Searcher.pickCreator(type, conn, scan);
                break;
            case "person":
                Searcher.pickPerson(conn, scan);
                break;
            default:
                // print invalid
                System.out.println(type + " is an Invalid input");
        }
    }

    public void add() throws Exception {
        // ask for which type of item to add
        System.out.println(
                "What would you like to add? (Album, Track, Interview, Movie, Audiobook, AudiobookChapter, PhysicalBook, PhysicalBookChapter, Actor, Artist, Director, Writer, Person, or Relationship)");
        // get the type
        String type = scan.nextLine().toLowerCase();
        // add depending on the type
        switch (type) {
            case "album":
            case "track":
            case "interview":
            case "movie":
            case "audiobook":
            case "physicalbook":
                Adder.addItem(Util.changeToDBString(type), conn, scan);
                break;
            case "audiobookchapter":
            case "physicalbookchapter":
                // TODO: add chapters as attributes
                break;
            case "actor":
            case "director":
            case "artist":
            case "writer":
                Adder.addCreator(Util.changeToDBString(type), conn, scan);
                break;
            case "relationship":
                Adder.addRelationship(conn, scan);
                break;
            case "person":
                Adder.addPerson(conn, scan);
                break;
            default:
                // print invalid
                System.out.println(type + " is an Invalid input");
        }
    }

    public void delete() throws Exception {
        // Ask the user which record they want to delete
        System.out.println(
                "Enter the type of the record to be deleted (Album, Track, Interview, Movie, Audiobook, AudiobookChapter, PhysicalBook, PhysicalBookChapter, Actor, Artist, Director, Writer, or Person): ");
        String type = scan.nextLine().toLowerCase();

        switch (type) {
            case "album":
            case "track":
            case "interview":
            case "movie":
            case "audiobook":
            case "physicalbook":
                // TODO: delete item
                break;
            case "audiobookchapter":
            case "physicalbookchapter":
                // TODO: delete chapter
                break;
            case "actor":
            case "director":
            case "artist":
            case "writer":
                Deleter.deleteCreator(type, conn, scan);
                break;
            case "person":
                Deleter.deletePerson(conn, scan);
                break;
            default:
                // print invalid
                System.out.println(type + " is an Invalid input");
        }
    }

    public void edit() throws Exception {
        // Ask the user which record they want to edit
        System.out.println(
                "Enter the type of the record to be edited (Album, Track, Interview, Movie, Audiobook, AudiobookChapter, PhysicalBook, PhysicalBookChapter, Actor, Artist, Director, Writer, Person, Stars, Writes, Interviewed, Performs, or Directs): ");
        String type = scan.nextLine().toLowerCase();

        switch (type) {
            case "album":
            case "track":
            case "interview":
            case "movie":
            case "audiobook":
            case "physicalbook":
                Editor.editItem(type, conn, scan);
                break;
            case "audiobookchapter":
            case "physicalbookchapter":
                // TODO: edit chapter
                break;
            case "actor":
            case "director":
            case "artist":
            case "writeer":
                Editor.editCreator(type, conn, scan);
                break;
            case "person":
                Editor.editPerson(type, conn, scan);
                break;
            case "stars":
            case "writes":
            case "interviewed":
            case "performs":
            case "directs":
                Editor.editRelationship(type, conn, scan);
                break;
            default:
                // print invalid
                System.out.println(type + " is an Invalid input");
        }
    }
    
    public void checkout() {
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
                Checkout.checkoutItem(type,conn,scan);
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
                    break;
                case 'c':
                    // TODO: implement checkout
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
        }
    }

    private void getConnection() throws SQLException {
        conn = DriverManager.getConnection(CONNECTIONPATH);
    }

    private void cleanUp() {
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
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
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            app.cleanUp();
        }
    }

}