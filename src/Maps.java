import java.util.*;

public class Maps {
    public static Map<String, String[]> itemEditorMap;
    public static Map<String, String> itemAdderMap;
    public static Map<String, String> creatorAdderMap;
    public static Map<String, String> creatorSearcherMap;
    public static Map<String, String[]> creatorEditorMap;
    public static Map<String, String> creatorDeleteMap;
    public static Map<String, String> itemDeleteMap;
    public static Map<String, String[]> creatorDeleteRelationshipMap;
    public static Map<String, String> nextIDMap;
    public static Map<String, String> nextIDColumnMap;
    public static Map<String, String> creatorNameMap;
    public static Map<String, String> itemInsertType;
    public static Map<String, String> itemSearcherMap;
    public static Map<String, String[]> personEditorMap;
    public static Map<String, String[]> relationshipEditorMap;
    public static Map<String, String[]> relationshipOptionMap;
    public static Map<String, String> relationshipAdderMap;
    public static Map<String, String> relationshipSearcherMap;
    public static Map<String, String> relationshipDeleterMap;
    public static String addPersonString = "insert or rollback into person values (?,?,?,?,?);";
    public static String searchPersonString = "SELECT * FROM PERSON WHERE email = ?;";
    public static String deletePersonString = "delete FROM PERSON WHERE CardID = ?;";
    public static String startTransactionString = "begin transaction;";
    public static String endTransactionString = "commit;";
    public static String forceRollBackString = "ROLLBACK;";
    public static String getTypeColumnInItemFromItemIDString = "SELECT Type FROM Item WHERE Item_ID = ?;";
    public static String checkItemInOrderedString = "SELECT * FROM Item_Ordered WHERE Item_ID = ?;";

    // Instantiating the static maps
    static {
        creatorEditorMap = new HashMap<>();
        itemAdderMap = new HashMap<>();
        creatorAdderMap = new HashMap<>();
        creatorSearcherMap = new HashMap<>();
        creatorDeleteMap = new HashMap<>();
        itemDeleteMap = new HashMap<>();
        nextIDMap = new HashMap<>();
        nextIDColumnMap = new HashMap<>();
        personEditorMap = new HashMap<>();
        relationshipEditorMap = new HashMap<>();
        relationshipOptionMap = new HashMap<>();
        itemEditorMap = new HashMap<>();
        creatorNameMap = new HashMap<>();
        itemInsertType = new HashMap<>();
        itemSearcherMap = new HashMap<>();
        relationshipAdderMap = new HashMap<>();
        relationshipSearcherMap = new HashMap<>();
        relationshipDeleterMap = new HashMap<>();
        creatorDeleteRelationshipMap = new HashMap<>();

        String[] starsOptionList = { "actor", "movie" };
        String[] writesOptionList = { "writer", "audiobook", "physicalbook" };
        String[] interviewedOptionList = { "actor", "interview" };
        String[] performsOptionList = { "artist", "album", "track" };
        String[] directsOptionList = { "director", "movie" };
        relationshipOptionMap.put("stars", starsOptionList);
        relationshipOptionMap.put("writes", writesOptionList);
        relationshipOptionMap.put("interviewed", interviewedOptionList);
        relationshipOptionMap.put("performs", performsOptionList);
        relationshipOptionMap.put("directs", directsOptionList);

        String[] starsList = {
                "update or rollback stars SET Creator_ID = ? WHERE Creator_ID = ? AND Item_ID = ?;",
                "update or rollback stars SET Item_ID = ? WHERE Creator_ID = ? AND Item_ID = ?;",
                "update or rollback stars SET Role = ? WHERE Creator_ID = ? AND Item_ID = ?;" };
        String[] writesList = {
                "update or rollback writes SET Creator_ID = ? WHERE Creator_ID = ? AND Item_ID = ?;",
                "update or rollback writes SET Item_ID = ? WHERE Creator_ID = ? AND Item_ID = ?;" };
        String[] interviewedList = {
                "update or rollback interviewed SET Creator_ID = ? WHERE Creator_ID = ? AND Item_ID = ?;",
                "update or rollback interviewed SET Item_ID = ? WHERE Creator_ID = ? AND Item_ID = ?;" };
        String[] performsList = {
                "update or rollback performs SET Creator_ID = ? WHERE Creator_ID = ? AND Item_ID = ?;",
                "update or rollback performs SET Item_ID = ? WHERE Creator_ID = ? AND Item_ID = ?;" };
        String[] directsList = {
                "update or rollback directs SET Creator_ID = ? WHERE Creator_ID = ? AND Item_ID = ?;",
                "update or rollback directs SET Item_ID = ? WHERE Creator_ID = ? AND Item_ID = ?;" };
        relationshipEditorMap.put("stars", starsList);
        relationshipEditorMap.put("writes", writesList);
        relationshipEditorMap.put("interviewed", interviewedList);
        relationshipEditorMap.put("performs", performsList);
        relationshipEditorMap.put("directs", directsList);

        relationshipAdderMap.put("stars", "insert or rollback into stars values (?,?,?);");
        relationshipAdderMap.put("writes", "insert or rollback into writes values (?,?);");
        relationshipAdderMap.put("interviewed", "insert or rollback into interviewed values (?,?);");
        relationshipAdderMap.put("performs", "insert or rollback into performs values (?,?);");
        relationshipAdderMap.put("directs", "insert or rollback into directs values (?,?);");

        relationshipSearcherMap.put("stars", "select * from stars where Creator_ID = ? and Item_ID = ?;");
        relationshipSearcherMap.put("writes", "select * from writes where Creator_ID = ? and Item_ID = ?;");
        relationshipSearcherMap.put("interviewed",
                "select * from interviewed where Creator_ID = ? and Item_ID = ?;");
        relationshipSearcherMap.put("performs", "select * from performs where Creator_ID = ? and Item_ID = ?;");
        relationshipSearcherMap.put("directs", "select * from directs where Creator_ID = ? and Item_ID = ?;");

        relationshipDeleterMap.put("stars",
                "delete from stars where Creator_ID = ? and Item_ID = ?;");
        relationshipDeleterMap.put("writes",
                "delete from writes where Creator_ID = ? and Item_ID = ?;");
        relationshipDeleterMap.put("interviewed",
                "delete from interviewed where Creator_ID = ? and Item_ID = ?;");
        relationshipDeleterMap.put("performs",
                "delete from performs where Creator_ID = ? and Item_ID = ?;");
        relationshipDeleterMap.put("directs",
                "delete from directs where Creator_ID = ? and Item_ID = ?;");

        String[] personList = { "update or rollback person SET Email = ? WHERE CardID = ?;",
                "update or rollback person SET Fname = ? WHERE CardID = ?;",
                "update or rollback person SET Lname = ? WHERE CardID = ?;",
                "update or rollback person SET Address = ? WHERE CardID = ?;",
                "update or rollback person SET CardID = ? WHERE CardID = ?;" };
        personEditorMap.put("person", personList);

        String[] albumList = { "update or rollback album SET NumberSongs = ? WHERE ItemID = ?;",
                "update or rollback album SET NumberMinutes = ? WHERE ItemID = ?;" };
        String[] trackList = { "update or rollback track SET NumberSeconds = ? WHERE ItemID = ?;",
                "update or rollback track SET AlbumID = ? WHERE ItemID = ?;" };
        String[] interviewList = { "update or rollback interview SET NumberMinutes = ? WHERE ItemID = ?;" };
        String[] movieList = { "update or rollback movie SET Runtime = ? WHERE ItemID = ?;",
                "update or rollback movie SET Rating = ? WHERE ItemID = ?;" };
        String[] audiobookList = { "update or rollback audiobook SET NumberMinutes = ? WHERE ItemID = ?;" };
        String[] physicalbookList = { "update or rollback physical_book SET NumberPages = ? WHERE ItemID = ?;" };
        String[] itemorderedList = { "update or rollback item_ordered SET Item_ID = ? WHERE Item_ID = ?;", 
                "update or rollback item_ordered SET price = ? WHERE Item_ID = ?;", 
                "update or rollback item_ordered SET Quantity_ordered = ? WHERE Item_ID = ?;", 
                "update or rollback item_ordered SET EArrivalDate = ? WHERE Item_ID = ?;", 
                "update or rollback item_ordered SET ActualArrivalDate = ? WHERE Item_ID = ?;"};
        itemEditorMap.put("itemordered",itemorderedList);
        itemEditorMap.put("album", albumList);
        itemEditorMap.put("track", trackList);
        itemEditorMap.put("interview", interviewList);
        itemEditorMap.put("movie", movieList);
        itemEditorMap.put("audiobook", audiobookList);
        itemEditorMap.put("physicalbook", physicalbookList);

        String[] actorList = { "update or rollback actor SET ar_name = ? WHERE creator_id = ?;",
                "update or rollback actor SET Date_Of_Birth = ? WHERE creator_id = ?;" };
        String[] artistList = { "update or rollback artist SET ar_name = ? WHERE creator_id = ?;",
                "update or rollback artist SET Date_Of_Birth = ? WHERE creator_id = ?;" };
        String[] directorList = { "update or rollback director SET ar_name = ? WHERE creator_id = ?;",
                "update or rollback director SET Date_Of_Birth = ? WHERE creator_id = ?;" };
        String[] writerList = { "update or rollback writer SET ar_name = ? WHERE creator_id = ?;",
                "update or rollback writer SET Date_Of_Birth = ? WHERE creator_id = ?;" };
        creatorEditorMap.put("actor", actorList);
        creatorEditorMap.put("artist", artistList);
        creatorEditorMap.put("director", directorList);
        creatorEditorMap.put("writer", writerList);

        itemAdderMap.put("itemordered", "insert or rollback into item_ordered values (?,?,?,?,?);");
        itemAdderMap.put("item", "insert or rollback into item values (?,?,?,?);");
        itemAdderMap.put("album", "insert or rollback into album values (?,?,?);");
        itemAdderMap.put("track", "insert or rollback into track values (?,?,?);");
        itemAdderMap.put("interview", "insert or rollback into interview values (?,?);");
        itemAdderMap.put("movie", "insert or rollback into movie values (?,?,?);");
        itemAdderMap.put("audiobook", "insert or rollback into audiobook values (?,?);");
        itemAdderMap.put("physical_book", "insert or rollback into physical_book values (?,?);");

        creatorAdderMap.put("creator", "insert or rollback into creator values (?);");
        creatorAdderMap.put("artist", "insert or rollback into artist values (?,?,?);");
        creatorAdderMap.put("actor", "insert or rollback into actor values (?,?,?);");
        creatorAdderMap.put("director", "insert or rollback into director values (?,?,?);");
        creatorAdderMap.put("writer", "insert or rollback into writer values (?,?,?);");

        creatorSearcherMap.put("artist", "SELECT * FROM artist where ar_name = ?;");
        creatorSearcherMap.put("actor", "SELECT * FROM actor where ar_name = ?;");
        creatorSearcherMap.put("director", "SELECT * FROM director where ar_name = ?;");
        creatorSearcherMap.put("writer", "SELECT * FROM writer where ar_name = ?;");

        creatorDeleteMap.put("creator", "delete from creator where Creator_ID = ?;");
        creatorDeleteMap.put("artist", "delete from artist where Creator_ID = ?;");
        creatorDeleteMap.put("actor", "delete from actor where Creator_ID = ?;");
        creatorDeleteMap.put("director", "delete from director where Creator_ID = ?;");
        creatorDeleteMap.put("writer", "delete from writer where Creator_ID = ?;");


        itemDeleteMap.put("item", "delete from item where Item_ID = ?;");
        itemDeleteMap.put("itemordered", "delete from item_ordered where Item_ID = ?;");
        itemDeleteMap.put("album", "delete from album where ItemID = ?;");
        itemDeleteMap.put("track", "delete from track where ItemID = ?;");
        itemDeleteMap.put("movie", "delete from movie where ItemID = ?;");
        itemDeleteMap.put("audiobook", "delete from audiobook where ItemID = ?;");
        itemDeleteMap.put("physicalbook", "delete from physical_book where ItemID = ?;");
        itemDeleteMap.put("audiobookchapter", "delete from chapter_ab where ItemID = ?;");
        itemDeleteMap.put("physicalbookchapter", "delete from chapter_pb where ItemID = ?;");


        String[] creatorDeleteRelArtist = { "delete from performs where Creator_ID = ?;" };
        String[] creatorDeleteRelActor = { "delete from stars where Creator_ID = ?;",
                "delete from interviewed where Creator_ID = ?;" };
        String[] creatorDeleteRelDirector = { "delete from directs where Creator_ID = ?;" };
        String[] creatorDeleteRelWriter = { "delete from writes where Creator_ID = ?;" };

        creatorDeleteRelationshipMap.put("artist", creatorDeleteRelArtist);
        creatorDeleteRelationshipMap.put("actor", creatorDeleteRelActor);
        creatorDeleteRelationshipMap.put("director", creatorDeleteRelDirector);
        creatorDeleteRelationshipMap.put("writer", creatorDeleteRelWriter);


        creatorNameMap.put("artist", "Ar_Name");
        creatorNameMap.put("actor", "Ar_Name");
        creatorNameMap.put("director", "Ar_Name");
        creatorNameMap.put("writer", "Ar_Name");

        nextIDMap.put("item", "SELECT Max(Item_ID) FROM item;");
        nextIDMap.put("creator", "SELECT Max(creator_ID) FROM creator;");
        nextIDMap.put("library_card", "SELECT Max(cardID) FROM person;");

        nextIDColumnMap.put("item", "Max(Item_ID)");
        nextIDColumnMap.put("creator", "Max(creator_ID)");
        nextIDColumnMap.put("library_card", "Max(cardID)");

        itemInsertType.put("album", "ALBUM");
        itemInsertType.put("track", "Track");
        itemInsertType.put("interview", "interview");
        itemInsertType.put("movie", "MOVIE");
        itemInsertType.put("audiobook", "ABook");
        itemInsertType.put("physicalbook", "PBook");

        itemSearcherMap.put("album", "SELECT * FROM ITEM ,ALBUM WHERE title = ? AND ITEM.Item_ID = ALBUM.ItemID ");
        itemSearcherMap.put("track", "SELECT * FROM ITEM ,TRACK WHERE title = ? AND ITEM.Item_ID = TRACK.ItemID ");
        itemSearcherMap.put("itemordered", "SELECT * FROM ITEM, item_ordered WHERE title = ? AND ITEM.Item_ID = Item_Ordered.Item_ID ");

        itemSearcherMap.put("interview",
                "SELECT * FROM ITEM ,interview WHERE title = ? AND ITEM.Item_ID = interview.ItemID ");
        itemSearcherMap.put("movie",
                "SELECT * FROM ITEM ,movie WHERE title = ? AND ITEM.Item_ID = movie.ItemID ");
        itemSearcherMap.put("audiobook",
                "SELECT * FROM ITEM ,audiobook WHERE title = ? AND ITEM.Item_ID = audiobook.ItemID ");
        itemSearcherMap.put("physicalbook",
                "SELECT * FROM ITEM ,physical_book WHERE title = ? AND ITEM.Item_ID = physical_book.ItemID ");

    }

}
