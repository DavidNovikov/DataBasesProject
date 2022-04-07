import java.util.*;
import java.text.*;
import java.sql.*;

class Item {
    public static int itemCount = 1;
    int itemID;
    String title;
    int yearReleased;
    String genre;
    String type;
    boolean active;

    Item(String title, int yearReleased, String genre, String type, boolean active) {
        this.itemID = itemCount;
        itemCount++;
        this.title = title;
        this.yearReleased = yearReleased;
        this.genre = genre;
        this.type = type;
        this.active = active;
    }

}

class Person {
    int libraryCardID;
    String email;
    String firstName;
    String lastName;
    String address;

    public Person(int libraryCardID, String email, String firstName, String lastName, String address) {
        this.libraryCardID = libraryCardID;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    public String toString() {
        return "Name: " + firstName + " " + lastName + " Email: " + email + " Address: " + address + " LibraryCardID: "
                + libraryCardID;
    }
}

class People {
    int libraryCardRunningMax;
    ArrayList<Person> people;
    Scanner scan;

    public People(Scanner scan) {
        libraryCardRunningMax = 0;
        people = new ArrayList<Person>();
        this.scan = scan;
    }

    public void addPerson(String email, String firstName, String lastName, String address) {

        if (emailUnique(email)) {
            libraryCardRunningMax++;
            people.add(new Person(libraryCardRunningMax, email, firstName, lastName, address));
        } else
            System.out.println(email + " is not a unique email address, new person not added");
    }

    public boolean emailUnique(String email) {
        boolean emailUnique = true;
        for (Person p : people) {
            emailUnique &= !p.email.equals(email);
        }
        return emailUnique;
    }

    public void editPerson(String name) {
        ArrayList<Person> possiblePeople = search(name);
        if (possiblePeople.isEmpty())
            System.out.println("Error: " + name + " could not be found in the database");
        else {
            Person p = pickPerson(possiblePeople);
            if (p == null)
                System.out.println("Invalid index, aborting edit");
            else
                editPerson(p);
        }
    }

    public void searchForPerson(String name) {
        ArrayList<Person> possiblePeople = search(name);
        if (possiblePeople.isEmpty())
            System.out.println("Error: " + name + " could not be found in the database");
        else {
            for (Person p : possiblePeople) {
                System.out.println(p.toString());
            }
        }
    }

    private ArrayList<Person> search(String name) {
        ArrayList<Person> possiblePeople = new ArrayList<Person>();
        for (Person p : people) {
            if (name.contains(p.firstName) || name.contains(p.lastName))
                possiblePeople.add(p);
        }
        return possiblePeople;
    }

    private Person pickPerson(ArrayList<Person> possiblePeople) {
        int picked = 0;
        Person person = null;
        for (Person p : possiblePeople) {
            System.out.println(picked + "." + p.toString());
            picked++;
        }
        System.out.println("Please enter which person you want to edit between 0 and " + (picked - 1));
        picked = Integer.valueOf(scan.nextLine());
        if (picked >= 0 && picked < possiblePeople.size())
            person = possiblePeople.get(picked);
        return person;
    }

    private void editPerson(Person personToEdit) {
        char input = 'e';
        while (input != 'q') {

            System.out.println(
                    "Enter e to edit email, f for first name, l for last name, a for address, d to delete the person, c to get a new library card, and q to quit: ");
            input = scan.nextLine().charAt(0);

            switch (input) {
                case 'e':
                    System.out.println("Enter the email:");
                    String email = scan.nextLine();
                    if (emailUnique(email))
                        personToEdit.email = email;
                    else
                        System.out.println(email + " is not a unique email address, email not updated");
                    break;
                case 'f':
                    System.out.println("Enter the new first name:");
                    String firstName = scan.nextLine();
                    personToEdit.firstName = firstName;
                    break;
                case 'l':
                    System.out.println("Enter the new last name:");
                    String lastName = scan.nextLine();
                    personToEdit.lastName = lastName;
                    break;
                case 'a':
                    System.out.println("Enter the new address:");
                    String address = scan.nextLine();
                    personToEdit.address = address;
                    break;
                case 'd':
                    people.remove(personToEdit);
                    input = 'q';
                    break;
                case 'c':
                    libraryCardRunningMax++;
                    personToEdit.libraryCardID = libraryCardRunningMax;
                    break;
                case 'q':
                    // quit the program
                    break;
                default:
                    // print invalid
                    System.out.println("Invalid input");
            }
        }
    }

}

enum creatorType {
    ARTIST, ACTOR, DIRECTOR, WRITER
}

class Creator {
    int creatorID;
    String name;
    String dob;
    creatorType type;

    public Creator(int creatorID, String name, String dob, creatorType type) {
        this.creatorID = creatorID;
        this.name = name;
        this.dob = dob;
        this.type = type;
    }

    public String toString() {
        return " creatorID: " + creatorID + " name: " + name + " DOB: " + dob + " type: "
                + type.toString();
    }
}

class Creators {
    int creatorIDRunningMax;
    ArrayList<Creator> creators;
    Scanner scan;

    public Creators(Scanner scan) {
        creatorIDRunningMax = 0;
        creators = new ArrayList<Creator>();
        this.scan = scan;
    }

    public void addCreator(String name, String dob, String type) {
        creatorType cType = parseType(type);
        if (dateIsValid(dob) && cType != null) {
            creatorIDRunningMax++;
            creators.add(new Creator(creatorIDRunningMax, name, dob, cType));
        } else {
            System.out.println(
                    dob + " is not a valid dob or " + type + " is not a valid creator type, new creator not added");
        }
    }

    public creatorType parseType(String type) {
        type = type.toUpperCase();
        creatorType validType;
        switch (type) {
            case "ARTIST":
                validType = creatorType.ARTIST;
                break;
            case "ACTOR":
                validType = creatorType.ACTOR;
                break;
            case "DIRECTOR":
                validType = creatorType.DIRECTOR;
                break;
            case "WRITER":
                validType = creatorType.WRITER;
                break;
            default:
                validType = null;
        }
        return validType;
    }

    public boolean dateIsValid(String date) {
        try {
            DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
            dateFormatter.setLenient(false);
            dateFormatter.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public void editCreator(String name) {
        ArrayList<Creator> possibleCreators = search(name);
        if (possibleCreators.isEmpty())
            System.out.println("Error: " + name + " could not be found in the database");
        else {
            Creator c = pickCreator(possibleCreators);
            if (c == null)
                System.out.println("Invalid index, aborting edit");
            else
                editCreator(c);
        }
    }

    public void searchForCreator(String name) {
        ArrayList<Creator> possibleCreators = search(name);
        if (possibleCreators.isEmpty())
            System.out.println("Error: " + name + " could not be found in the database");
        else {
            for (Creator c : possibleCreators) {
                System.out.println(c.toString());
            }
        }
    }

    private ArrayList<Creator> search(String name) {
        ArrayList<Creator> possibleCreators = new ArrayList<Creator>();
        for (Creator c : creators) {
            if (name.contains(c.name) || c.name.contains(name))
                possibleCreators.add(c);
        }
        return possibleCreators;
    }

    private Creator pickCreator(ArrayList<Creator> possibleCreators) {
        int picked = 0;
        Creator creator = null;
        for (Creator c : possibleCreators) {
            System.out.println(picked + "." + c.toString());
            picked++;
        }
        System.out.println("Please enter which creator you want to edit between 0 and " + (picked - 1));
        picked = Integer.valueOf(scan.nextLine());
        if (picked >= 0 && picked < possibleCreators.size())
            creator = possibleCreators.get(picked);
        return creator;
    }

    private void editCreator(Creator creatorToEdit) {
        char input = 'e';
        while (input != 'q') {

            System.out.println(
                    "Enter n to edit name, d for dob, t for type, r to remove the creator, and q to quit: ");
            input = scan.nextLine().charAt(0);

            switch (input) {
                case 'n':
                    System.out.println("Enter the new name:");
                    String name = scan.nextLine();
                    creatorToEdit.name = name;
                    break;
                case 'd':
                    System.out.println("Enter the new dob:");
                    String dob = scan.nextLine();
                    if (dateIsValid(dob))
                        creatorToEdit.dob = dob;
                    else
                        System.out.println(dob + " is not a valid dob, creator not updated");
                    break;
                case 't':
                    System.out.println("Enter the new type:");
                    String t = scan.nextLine();
                    creatorType cT = parseType(t);
                    if (cT != null)
                        creatorToEdit.type = cT;
                    else
                        System.out.println(t + " is not a valid creator type, creator not updated");
                    break;
                case 'r':
                    creators.remove(creatorToEdit);
                    input = 'q';
                    break;
                case 'q':
                    // quit the program
                    break;
                default:
                    // print invalid
                    System.out.println("Invalid input");
            }
        }
    }

}

class Album extends Item {
    int numSongs;
    int numMinutes;
    ArrayList<Track> tracks;

    Album(String title, int yearReleased, String genre, boolean active, int numSongs, int numMinutes) {
        super(title, yearReleased, genre, "Album", active);
        this.numSongs = numSongs;
        this.numMinutes = numMinutes;
        this.tracks = new ArrayList<Track>();
    }
}

class Track extends Item {
    int numMinutes;

    Track(String title, int yearReleased, String genre, boolean active, int numMinutes) {
        super(title, yearReleased, genre, "Track", active);
        this.numMinutes = numMinutes;
    }
}

class Interview extends Item {
    int numMinutes;

    Interview(String title, int yearReleased, String genre, boolean active, int numMinutes) {
        super(title, yearReleased, genre, "Interview", active);
        this.numMinutes = numMinutes;
    }
}

class Movie extends Item {
    int runTime;
    String rating;

    Movie(String title, int yearReleased, String genre, boolean active, int runTime, String rating) {
        super(title, yearReleased, genre, "Movie", active);
        this.runTime = runTime;
        this.rating = rating;
    }
}

class Book extends Item {
    int numChapters;
    ArrayList<Chapter> chapters;

    Book(String title, int yearReleased, String genre, String type, boolean active, int numChapters) {
        super(title, yearReleased, genre, type, active);
        this.numChapters = numChapters;
    }
}

class PhysicalBook extends Book {
    int numPages;
    ArrayList<Chapter> chapters;

    PhysicalBook(String title, int yearReleased, String genre, boolean active, int numChapters, int numPages) {
        super(title, yearReleased, genre, "PhysicalBook", active, numChapters);
        this.numPages = numPages;
        this.chapters = new ArrayList<Chapter>();
    }
}

class Audiobook extends Book {
    int numMinutes;
    ArrayList<Chapter> chapters;

    Audiobook(String title, int yearReleased, String genre, boolean active, int numChapters, int numMinutes) {
        super(title, yearReleased, genre, "Audiobook", active, numChapters);
        this.numMinutes = numMinutes;
        this.chapters = new ArrayList<Chapter>();
    }
}

class Chapter {
    String chapterTitle;
    int length;

    Chapter(String chapterTitle, int length) {
        this.chapterTitle = chapterTitle;
        this.length = length;
    }
}

public class DBApp {

    //final String CONNECTIONPATH = "jdbc:sqlite:/Users/davidnovikov/Desktop/LIBRARY.db";
    //final String CONNECTIONPATH = "jdbc:sqlite:/Users/djcje/Documents/Databases/Library.db";
    final String CONNECTIONPATH = "jdbc:sqlite:LIBRARY.db";

    // use a scanner to read input
    private Scanner scan;

    // connection and statement variables
    private Connection conn;

    public DBApp() {
        scan = new Scanner(System.in);
        conn = null;
    }

    public void search() {
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

    // public void searchForCreator() {
    // System.out.println("Enter the name of a creator to search for: ");
    // String nameOfCreator = scan.nextLine();
    // creators.searchForCreator(nameOfCreator);
    // }

    // public void searchForPerson() {
    // System.out.println("Enter the name of a person to search for: ");
    // String nameOfPerson = scan.nextLine();
    // people.searchForPerson(nameOfPerson);
    // }

    // public void searchForMedia() {
    // // ask the user which record search for and find it
    // System.out.println("Enter the title of the item you are searching for: ");
    // // get the title
    // String titleSearchingFor = scan.nextLine();
    // // go through the items and print out the ones with a matching title
    // boolean found = false;
    // for (Item i : items) {
    // if (i.title.equals(titleSearchingFor)) {
    // found = true;
    // // print out the record
    // System.out.println("Title: " + i.title + "\nYear Released: " + i.yearReleased
    // + "\nGenre: " + i.genre
    // + "\nType: " + i.type + "\nType" + i.active + "\nItemID: " + i.itemID);
    // }
    // }
    // if (!found)
    // System.out.println("Record not found");
    // }

    // public Item editSearch(String title) {
    // // go through the items and print out the ones with a matching title
    // boolean found = false;
    // int count = 1;

    // ArrayList<Item> possibleItems = new ArrayList<Item>();
    // while (!found) {
    // for (Item i : items) {
    // if (i.title.equals(title)) {
    // found = true;
    // // print out the record
    // System.out.println(
    // count + ". Title: " + i.title + "\nYear Released: " + i.yearReleased +
    // "\nGenre: " + i.genre
    // + "\nType: " + i.type + "\nActive: " + i.active + "\nItemID: " + i.itemID);
    // possibleItems.add(i);
    // }
    // }
    // if (!found) {
    // System.out.println("Record not found. Press q to quit, press anything else to
    // try again.");
    // String userChoice = scan.nextLine();
    // if (userChoice.equals("q")) {
    // return null;
    // } else {
    // System.out.println("Enter the title of a record to be edited: ");
    // String nameOfRecord = scan.nextLine();
    // return editSearch(nameOfRecord);
    // }
    // }
    // }

    // System.out.println("Enter the number corresponding to the item that you would
    // like to edit.");
    // String itemToEdit = scan.nextLine();
    // int numToEdit = Integer.parseInt(itemToEdit);

    // return possibleItems.get(numToEdit - 1);
    // }

    // public static Item getItemInfo(String type, Scanner scan) {
    // // get everything we need
    // System.out.println("Enter the title:");
    // String title = scan.nextLine();
    // System.out.println("Enter the release year:");
    // int year = Integer.valueOf(scan.nextLine());
    // System.out.println("Enter the item genre:");
    // String genre = scan.nextLine();

    // String response = " ";
    // boolean active = false;
    // while (!(response.equals("y") || response.equals("n"))) {
    // System.out.println("Is it in the inventory(n indicates its being ordered)?
    // y/n");
    // response = scan.nextLine();
    // if (response.equals("y") || response.equals("Y"))
    // active = true;
    // if (!(response.equals("y") || response.equals("n") || response.equals("Y") ||
    // response.equals("N")))
    // System.out.println("Invalid input: enter y or n");
    // }
    // return new Item(title, year, genre, type, active);
    // }

    // public ArrayList<Item> addAlbum(Scanner scan) {
    // Item temp = getItemInfo("Track", scan);

    // System.out.println("Enter the number of tracks:");
    // int numSongs = Integer.valueOf(scan.nextLine());
    // System.out.println("Enter the number of minutes:");
    // int minutes = Integer.valueOf(scan.nextLine());

    // Album albumToAdd = new Album(temp.title, temp.yearReleased, temp.genre,
    // temp.active, numSongs, minutes);
    // // add the album to items
    // items.add(albumToAdd);
    // System.out.println(albumToAdd.title + " was added successfully");
    // return items;
    // }

    // public ArrayList<Item> addTrack(Scanner scan) {
    // Item temp = getItemInfo("Track", scan);

    // System.out.println("Enter the number of minutes:");
    // int minutes = Integer.valueOf(scan.nextLine());

    // Track newTrack = new Track(temp.title, temp.yearReleased, temp.genre,
    // temp.active, minutes);
    // // add the album to items
    // items.add(newTrack);
    // System.out.println(newTrack.title + " was added successfully");
    // return items;
    // }

    // public ArrayList<Item> addInterview(Scanner scan) {
    // Item temp = getItemInfo("Interview", scan);

    // System.out.println("Enter the number of minutes:");
    // int minutes = Integer.valueOf(scan.nextLine());

    // Interview newInterview = new Interview(temp.title, temp.yearReleased,
    // temp.genre, temp.active, minutes);
    // // add the album to items
    // items.add(newInterview);
    // System.out.println(newInterview.title + " was added successfully");
    // return items;
    // }

    // public ArrayList<Item> addMovie(Scanner scan) {
    // Item temp = getItemInfo("Movie", scan);

    // System.out.println("Enter the runtime in minutes:");
    // int runtime = Integer.valueOf(scan.nextLine());
    // // TODO: add a while loop here for checking to make sure its valid rating
    // System.out.println("Enter the rating (PG, G, R, PG13, etc):");
    // ArrayList<String> ratings = new ArrayList<String>();
    // ratings.add("PG");
    // ratings.add("G");
    // ratings.add("PG13");
    // ratings.add("R");
    // ratings.add("NC-17");
    // String rating = scan.nextLine();
    // while (!ratings.contains(rating)) {
    // System.out.println("Rating not valid. Try again");
    // System.out.println("Enter the rating (PG, G, R, PG13, etc):");
    // rating = scan.nextLine();
    // }

    // Movie newMovie = new Movie(temp.title, temp.yearReleased, temp.genre,
    // temp.active, runtime, rating);
    // // add the album to items
    // items.add(newMovie);
    // System.out.println(newMovie.title + " was added successfully");
    // return items;
    // }

    // public Audiobook addABChapters(Audiobook b, Scanner scan) {
    // int numChapters = b.numChapters;
    // for (int i = 1; i <= numChapters; i++) {
    // System.out.println("Enter chapter " + i + "'s title:");
    // String title = scan.nextLine();
    // System.out.println("Enter chapter " + i + "'s length in minutes:");
    // int length = Integer.valueOf(scan.nextLine());
    // // add the chapter to the book
    // b.chapters.add(new Chapter(title, length));
    // }
    // return b;
    // }

    // public PhysicalBook addPBChapters(PhysicalBook b, Scanner scan) {
    // int numChapters = b.numChapters;
    // for (int i = 1; i <= numChapters; i++) {
    // System.out.println("Enter chapter " + i + "'s title:");
    // String title = scan.nextLine();
    // System.out.println("Enter chapter " + i + "'s length in pages:");
    // int length = Integer.valueOf(scan.nextLine());
    // // add the chapter to the book
    // b.chapters.add(new Chapter(title, length));
    // }
    // return b;
    // }

    // public ArrayList<Item> addAudiobook(Scanner scan) {
    // Item temp = getItemInfo("Audiobook", scan);

    // System.out.println("Enter the number of chapters:");
    // int numChapters = Integer.valueOf(scan.nextLine());
    // System.out.println("Enter the number of minutes:");
    // int numMinutes = Integer.valueOf(scan.nextLine());

    // Audiobook newAudiobook = new Audiobook(temp.title, temp.yearReleased,
    // temp.genre, temp.active, numChapters,
    // numMinutes);

    // // add all the chapters
    // newAudiobook = addABChapters(newAudiobook, scan);

    // // add the book to items
    // items.add(newAudiobook);
    // System.out.println(newAudiobook.title + " was added successfully");
    // return items;
    // }

    // public ArrayList<Item> addPhysicalBook(Scanner scan) {
    // Item temp = getItemInfo("PhysicalBook", scan);

    // System.out.println("Enter the number of chapters:");
    // int numChapters = Integer.valueOf(scan.nextLine());
    // System.out.println("Enter the number of pages:");
    // int numPages = Integer.valueOf(scan.nextLine());

    // PhysicalBook newPhysicalBook = new PhysicalBook(temp.title,
    // temp.yearReleased, temp.genre, temp.active,
    // numChapters, numPages);

    // // add all the chapters
    // newPhysicalBook = addPBChapters(newPhysicalBook, scan);

    // // add the book to items
    // items.add(newPhysicalBook);
    // System.out.println(newPhysicalBook.title + " was added successfully");
    // return items;
    // }

    // public void addPerson() {
    // System.out.println("Enter the email:");
    // String email = scan.nextLine();
    // System.out.println("Enter the first name:");
    // String firstName = scan.nextLine();
    // System.out.println("Enter the last name:");
    // String lastName = scan.nextLine();
    // System.out.println("Enter the address:");
    // String address = scan.nextLine();
    // people.addPerson(email, firstName, lastName, address);
    // }

    // public void addCreator() {
    // System.out.println("Enter the name:");
    // String name = scan.nextLine();
    // System.out.println("Enter the dob:");
    // String dob = scan.nextLine();
    // System.out.println("Enter the type:");
    // String type = scan.nextLine();
    // creators.addCreator(name, dob, type);
    // }

    public void add() {
        // ask for which type of item to add
        System.out.println(
                "What would you like to add? (Album, Track, Interview, Movie, Audiobook, AudiobookChapter, PhysicalBook, PhysicalBookChapter, Actor, Artist, Director, Writer, or Relationship)");
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
            default:
                // print invalid
                System.out.println(type + " is an Invalid input");
        }
    }

    // public void editMovie() {
    // System.out.println("Enter the title of a movie to be edited: ");
    // String nameOfMovie = scan.nextLine();

    // Movie editedMovie = (Movie) editSearch(nameOfMovie);
    // if (!(editedMovie == null)) {
    // boolean doneChanging = false;
    // while (!doneChanging) {
    // System.out.println(
    // "What part of the record should be changed: Press 1 for the title, 2 for the
    // year released, 3 for the genre, 4 for the active in inventory status, 5 for
    // the runtime, and 6 for the rating.");
    // String userChoice = scan.nextLine();
    // System.out.println(
    // "What would you like the new field to be? Enter a title, year released,
    // genre, y/n for active versus inactive, a number of minutes, or a rating (G,
    // PG, PG-13, R, NC-17).");
    // String newField = scan.nextLine();
    // switch (userChoice) {
    // case "1":
    // editedMovie.title = newField;
    // break;
    // case "2":
    // int newReleaseYear = Integer.parseInt(newField);
    // editedMovie.yearReleased = newReleaseYear;
    // break;
    // case "3":
    // editedMovie.genre = newField;
    // break;
    // case "4":
    // if (newField.equals("y")) {
    // editedMovie.active = true;
    // } else if (newField.equals("n")) {
    // editedMovie.active = false;
    // } else {
    // System.out.println(
    // "Please enter only 'y' if the item should be active, or 'n' if it should be
    // inactive.");
    // }
    // break;
    // case "5":
    // int newRuntime = Integer.parseInt(newField);
    // editedMovie.runTime = newRuntime;
    // break;
    // case "6":
    // if (!(newField.equals("G") || newField.equals("PG") ||
    // newField.equals("PG-13")
    // || newField.equals("R") || newField.equals("NC-17"))) {
    // System.out.println("Please only enter one of 'G', 'PG', 'PG-13', 'R', or
    // 'NC-17'.");
    // } else {
    // editedMovie.rating = newField;
    // }
    // break;
    // default:
    // System.out.println("Please only enter a number between 1 and 6.");
    // }
    // System.out.println("The new movie in the record is: ");
    // System.out.println("Title: " + editedMovie.title + "\nYear Released: " +
    // editedMovie.yearReleased
    // + "\nGenre: " + editedMovie.genre
    // + "\nType: " + editedMovie.type + "\nActive: " + editedMovie.active +
    // "\nItemID: "
    // + editedMovie.itemID + "\nRuntime: " + editedMovie.runTime + "\nRating: " +
    // editedMovie.rating);
    // System.out.println(
    // "If you are done changing, press q. If you are not done changing, press
    // anything else.");
    // userChoice = scan.nextLine();
    // if (userChoice.equals("q")) {
    // doneChanging = true;
    // }
    // }
    // }
    // }

    // public void editAudiobook() {
    // System.out.println("Enter the title of an Audiobook to be edited: ");
    // String nameOfAudiobook = scan.nextLine();

    // Audiobook editedAudiobook = (Audiobook) editSearch(nameOfAudiobook);
    // if (!(editedAudiobook == null)) {
    // boolean doneChanging = false;
    // while (!doneChanging) {
    // System.out.println(
    // "What part of the record should be changed: Press 1 for the title, 2 for the
    // year released, 3 for the genre, 4 for the active in inventory status, 5 for
    // the number of minutes, and 6 for the number of chapters.");
    // String userChoice = scan.nextLine();
    // System.out.println(
    // "What would you like the new field to be? Enter a title, year released,
    // genre, y/n for active versus inactive, a number of minutes, or a number of
    // chapters.");
    // String newField = scan.nextLine();
    // switch (userChoice) {
    // case "1":
    // editedAudiobook.title = newField;
    // break;
    // case "2":
    // int newReleaseYear = Integer.parseInt(newField);
    // editedAudiobook.yearReleased = newReleaseYear;
    // break;
    // case "3":
    // editedAudiobook.genre = newField;
    // break;
    // case "4":
    // if (newField.equals("y")) {
    // editedAudiobook.active = true;
    // } else if (newField.equals("n")) {
    // editedAudiobook.active = false;
    // } else {
    // System.out.println(
    // "Please enter only 'y' if the item should be active, or 'n' if it should be
    // inactive.");
    // }
    // break;
    // case "5":
    // int newNumMin = Integer.parseInt(newField);
    // editedAudiobook.numMinutes = newNumMin;
    // break;
    // case "6":
    // int newNumChapters = Integer.parseInt(newField);
    // editedAudiobook.numChapters = newNumChapters;
    // break;
    // default:
    // System.out.println("Please only enter a number between 1 and 6.");
    // }
    // System.out.println("The new Audiobook in the record is: ");
    // System.out.println("Title: " + editedAudiobook.title + "\nYear Released: "
    // + editedAudiobook.yearReleased + "\nGenre: " + editedAudiobook.genre
    // + "\nType: " + editedAudiobook.type + "\nActive: " + editedAudiobook.active +
    // "\nItemID: "
    // + editedAudiobook.itemID + "\nNumber of Minutes: " +
    // editedAudiobook.numMinutes
    // + "\nNumber of Chapters: " + editedAudiobook.numChapters);
    // System.out.println(
    // "If you are done changing, press q. If you are not done changing, press
    // anything else.");
    // userChoice = scan.nextLine();
    // if (userChoice.equals("q")) {
    // doneChanging = true;
    // }
    // }
    // }
    // }

    // public void editAlbum() {
    // System.out.println("Enter the name of the album that you want to edit: ");
    // String nameOfAlbum = scan.nextLine();

    // // get the album that they need
    // Album albumToEdit = (Album) editSearch(nameOfAlbum);
    // if (albumToEdit == null)
    // return;

    // // loop editing until theyre done
    // char input = 'x';
    // while (input != 'q') {
    // // ask what part they want to edit String title, int yearReleased, String
    // genre,
    // // boolean active, int numSongs, int numMinutes
    // System.out.println(
    // "Enter t to edit title, y for year released, g for genre, a for active, s for
    // number of songs, m for number of minutes, and q to quit: ");
    // input = scan.nextLine().charAt(0);

    // switch (input) {
    // case 't':
    // System.out.println("Enter the new title:");
    // String title = scan.nextLine();
    // albumToEdit.title = title;
    // break;
    // case 'y':
    // System.out.println("Enter the new release year:");
    // scan.nextLine();
    // int year;
    // try {
    // year = Integer.parseInt(scan.nextLine());
    // albumToEdit.yearReleased = year;
    // } catch (Exception e) {
    // System.out.println("Invalid year, try again");
    // }
    // break;
    // case 'g':
    // System.out.println("Enter the new genre:");
    // scan.nextLine();
    // String genre = scan.nextLine();
    // albumToEdit.genre = genre;
    // break;
    // case 'a':
    // boolean active = false;
    // String response = " ";
    // while (!(response.equals("y") || response.equals("n"))) {
    // System.out.println("Enter new active status: y/n");
    // scan.nextLine();
    // response = scan.nextLine();
    // if (response.equals("y")) {
    // active = true;
    // } else if (!(response.equals("y") || response.equals("n"))) {
    // System.out.println("Invalid input: enter y or n");
    // }
    // }
    // albumToEdit.active = active;
    // break;
    // case 's':
    // System.out.println("Enter the new number of songs:");
    // scan.nextLine();
    // int songs;
    // try {
    // songs = Integer.parseInt(scan.nextLine());
    // albumToEdit.numSongs = songs;
    // } catch (Exception e) {
    // System.out.println("Invalid number, try again");
    // }
    // break;
    // case 'm':
    // System.out.println("Enter the new number of minutes:");
    // scan.nextLine();
    // int minutes;
    // try {
    // minutes = Integer.parseInt(scan.nextLine());
    // albumToEdit.numMinutes = minutes;
    // } catch (Exception e) {
    // System.out.println("Invalid number, try again");
    // }
    // break;
    // case 'q':
    // // quit the program
    // break;
    // default:
    // // print invalid
    // System.out.println("Invalid input");
    // }
    // }

    // }

    // public void editTrack() {
    // System.out.println("Enter the name of the track that you want to edit: ");
    // scan.nextLine();
    // String nameOfTrack = scan.nextLine();

    // // get the album that they need
    // Track trackToEdit = (Track) editSearch(nameOfTrack);
    // if (trackToEdit == null)
    // return;

    // // loop editing until theyre done
    // char input = 'x';
    // while (input != 'q') {
    // // ask what part they want to edit (String title, int yearReleased, String
    // // genre, boolean active, int numMinutes
    // System.out.println(
    // "Enter t to edit title, y for year released, g for genre, a for active, m for
    // number of minutes, and q to quit: ");
    // input = scan.next().charAt(0);

    // switch (input) {
    // case 't':
    // System.out.println("Enter the new title:");
    // String title = scan.nextLine();
    // trackToEdit.title = title;
    // break;
    // case 'y':
    // System.out.println("Enter the new release year:");
    // scan.nextLine();
    // int year;
    // try {
    // year = Integer.parseInt(scan.next());
    // trackToEdit.yearReleased = year;
    // } catch (Exception e) {
    // System.out.println("Invalid year, try again");
    // }
    // break;
    // case 'g':
    // System.out.println("Enter the new genre:");
    // scan.nextLine();
    // String genre = scan.nextLine();
    // trackToEdit.genre = genre;
    // break;
    // case 'a':
    // boolean active = false;
    // String response = " ";
    // while (!(response.equals("y") || response.equals("n"))) {
    // System.out.println("Enter new active status: y/n");
    // scan.nextLine();
    // response = scan.nextLine();
    // if (response.equals("y")) {
    // active = true;
    // } else if (!(response.equals("y") || response.equals("n"))) {
    // System.out.println("Invalid input: enter y or n");
    // }
    // }
    // trackToEdit.active = active;
    // break;
    // case 'm':
    // System.out.println("Enter the new number of minutes:");
    // scan.nextLine();
    // int minutes;
    // try {
    // minutes = Integer.parseInt(scan.next());
    // trackToEdit.numMinutes = minutes;
    // } catch (Exception e) {
    // System.out.println("Invalid number, try again");
    // }
    // break;
    // case 'q':
    // // quit the program
    // break;
    // default:
    // // print invalid
    // System.out.println("Invalid input");
    // }
    // }

    // }

    public void delete() {
        // Ask the user which record they want to delete
        System.out.println(
                "Enter the type of the record to be deleted (Album, Track, Interview, Movie, Audiobook, AudiobookChapter, PhysicalBook, PhysicalBookChapter, Actor, Artist, Director, or Writer): ");
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
            default:
                // print invalid
                System.out.println(type + " is an Invalid input");
        }
    }

    public void edit() {
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

    // public void editInterview() {
    // System.out.println("Enter the name of the Interview you would like to edit");
    // String title = scan.nextLine();
    // Interview interviewToEdit = (Interview) editSearch(title);
    // if (interviewToEdit == null)
    // return;
    // char input = 's';
    // while (input != 'q') {
    // System.out.println(
    // "Enter t to edit title, y for year released, g for genre, a for active, n for
    // number of minutes, and q to quit");
    // input = scan.nextLine().charAt(0);
    // switch (input) {
    // case 't':
    // // edit title
    // System.out.println("Enter new title: ");
    // interviewToEdit.title = scan.nextLine();
    // break;
    // case 'y':
    // // edit year
    // System.out.println("Enter new year: ");
    // try {
    // interviewToEdit.yearReleased = Integer.parseInt(scan.nextLine());
    // } catch (Exception e) {
    // System.out.println("Invalid year, try again");
    // }
    // break;
    // case 'g':
    // // edit genre
    // System.out.println("Enter new genre: ");
    // interviewToEdit.genre = scan.nextLine();
    // break;
    // case 'a':
    // // edit active
    // System.out.println("Item active? y/n");
    // char activeInput = scan.nextLine().charAt(0);
    // switch (activeInput) {
    // case 'y':
    // interviewToEdit.active = true;
    // break;
    // case 'n':
    // interviewToEdit.active = false;
    // break;
    // default:
    // System.out.println("Invalid input, try again");
    // }
    // break;
    // case 'n':
    // // edit number of minutes
    // System.out.println("Enter new number of minutes: ");
    // try {
    // interviewToEdit.numMinutes = Integer.parseInt(scan.nextLine());
    // } catch (Exception e) {
    // System.out.println("Invalid year, try again");
    // }
    // break;
    // case 'q':
    // break;
    // default:
    // System.out.println("Invalid input");
    // }
    // }
    // }

    // public void editPhysicalBook() {
    // System.out.println("Enter the name of the PhysicalBook you would like to
    // edit");
    // String title = scan.nextLine();
    // PhysicalBook bookToEdit = (PhysicalBook) editSearch(title);
    // if (bookToEdit == null)
    // return;
    // char input = 's';
    // while (input != 'q') {
    // System.out.println(
    // "Enter t to edit title, y for year released, g for genre, a for active, n for
    // number of pages, and q to quit");
    // input = scan.nextLine().charAt(0);
    // switch (input) {
    // case 't':
    // // edit title
    // System.out.println("Enter new title: ");
    // bookToEdit.title = scan.nextLine();
    // break;
    // case 'y':
    // // edit year
    // System.out.println("Enter new year: ");
    // try {
    // bookToEdit.yearReleased = Integer.parseInt(scan.nextLine());
    // } catch (Exception e) {
    // System.out.println("Invalid year, try again");
    // }
    // break;
    // case 'g':
    // // edit genre
    // System.out.println("Enter new genre: ");
    // bookToEdit.genre = scan.nextLine();
    // break;
    // case 'a':
    // // edit active
    // System.out.println("Item active? y/n");
    // char activeInput = scan.nextLine().charAt(0);
    // switch (activeInput) {
    // case 'y':
    // bookToEdit.active = true;
    // break;
    // case 'n':
    // bookToEdit.active = false;
    // break;
    // default:
    // System.out.println("Invalid input, try again");
    // }
    // break;
    // case 'n':
    // // edit number of minutes
    // System.out.println("Enter new number of chapters: ");
    // try {
    // bookToEdit.numChapters = Integer.parseInt(scan.nextLine());
    // } catch (Exception e) {
    // System.out.println("Invalid year, try again");
    // }
    // break;
    // case 'q':
    // break;
    // default:
    // System.out.println("Invalid input");
    // }
    // }
    // }

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
            System.out.println(Util.nextIDFrom("Item", app.conn));
            System.out.println(Util.nextIDFrom("creator", app.conn));
            System.out.println(Util.nextIDFrom("library_card", app.conn));
            app.execute();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            app.cleanUp();
        }
    }

}