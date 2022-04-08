import java.util.*;

public class Maps {
    public static Map<String, String[]> itemEditorMap;
    public static Map<String, String> itemAdderMap;
    public static Map<String, String> creatorAdderMap;
    public static Map<String, String> creatorSearcherMap;
    public static Map<String, String[]> creatorEditorMap;
    public static Map<String, String> creatorDeleteMap;
    public static Map<String, String> nextIDMap;
    public static Map<String, String> nextIDColumnMap;
    public static Map<String, String> itemSearcherMap;
    public static Map<String, String[]> personEditorMap;
    public static Map<String, String[]> relationshipEditorMap;
    public static Map<String, String[]> relationshipOptionMap;
    public static Map<String, String> relationshipAdderMap;
    public static Map<String, String> relationshipSearcherMap;
    public static Map<String, String> relationshipDeleterMap;

    // Instantiating the static maps
    static {
        creatorEditorMap = new HashMap<>();
        itemAdderMap = new HashMap<>();
        creatorAdderMap = new HashMap<>();
        creatorSearcherMap = new HashMap<>();
        creatorDeleteMap = new HashMap<>();
        nextIDMap = new HashMap<>();
        nextIDColumnMap = new HashMap<>();
        personEditorMap = new HashMap<>();
        relationshipEditorMap = new HashMap<>();
        relationshipOptionMap = new HashMap<>();
        itemEditorMap = new HashMap<>();
        itemSearcherMap = new HashMap<>();
        relationshipAdderMap = new HashMap<>();
        relationshipSearcherMap = new HashMap<>();
        relationshipDeleterMap = new HashMap<>();

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

        String[] starsList = { "UPDATE stars SET Creator_ID = ? WHERE Creator_ID = ? AND Item_ID = ?;",
                "UPDATE stars SET Item_ID = ? WHERE Creator_ID = ? AND Item_ID = ?;",
                "UPDATE stars SET Role = ? WHERE Creator_ID = ? AND Item_ID = ?;" };
        String[] writesList = { "UPDATE writes SET Creator_ID = ? WHERE Creator_ID = ? AND Item_ID = ?;",
                "UPDATE writes SET Item_ID = ? WHERE Creator_ID = ? AND Item_ID = ?;" };
        String[] interviewedList = {
                "UPDATE interviewed SET Creator_ID = ? WHERE Creator_ID = ? AND Item_ID = ?;",
                "UPDATE interviewed SET Item_ID = ? WHERE Creator_ID = ? AND Item_ID = ?;" };
        String[] performsList = { "UPDATE performs SET Creator_ID = ? WHERE Creator_ID = ? AND Item_ID = ?;",
                "UPDATE performs SET Item_ID = ? WHERE Creator_ID = ? AND Item_ID = ?;" };
        String[] directsList = { "UPDATE directs SET Creator_ID = ? WHERE Creator_ID = ? AND Item_ID = ?;",
                "UPDATE directs SET Item_ID = ? WHERE Creator_ID = ? AND Item_ID = ?;" };
        relationshipEditorMap.put("stars", starsList);
        relationshipEditorMap.put("writes", writesList);
        relationshipEditorMap.put("interviewed", interviewedList);
        relationshipEditorMap.put("performs", performsList);
        relationshipEditorMap.put("directs", directsList);

        relationshipAdderMap.put("stars", "insert into stars values (?,?,?);");
        relationshipAdderMap.put("writes", "insert into writes values (?,?);");
        relationshipAdderMap.put("interviewed", "insert into interviewed values (?,?);");
        relationshipAdderMap.put("performs", "insert into performs values (?,?);");
        relationshipAdderMap.put("directs", "insert into directs values (?,?);");

        relationshipSearcherMap.put("stars", "select * from stars where Creator_ID = ? and Item_ID = ?;");
        relationshipSearcherMap.put("writes", "select * from writes where Creator_ID = ? and Item_ID = ?;");
        relationshipSearcherMap.put("interviewed",
                "select * from interviewed where Creator_ID = ? and Item_ID = ?;");
        relationshipSearcherMap.put("performs", "select * from performs where Creator_ID = ? and Item_ID = ?;");
        relationshipSearcherMap.put("directs", "select * from directs where Creator_ID = ? and Item_ID = ?;");

        relationshipDeleterMap.put("stars", "delete from stars where Creator_ID = ? and Item_ID = ?;");
        relationshipDeleterMap.put("writes", "delete from writes where Creator_ID = ? and Item_ID = ?;");
        relationshipDeleterMap.put("interviewed",
                "delete from interviewed where Creator_ID = ? and Item_ID = ?;");
        relationshipDeleterMap.put("performs", "delete from performs where Creator_ID = ? and Item_ID = ?;");
        relationshipDeleterMap.put("directs", "delete from directs where Creator_ID = ? and Item_ID = ?;");

        String[] personList = { "UPDATE person SET Email = ? WHERE CardID = ?;",
                "UPDATE person SET Fname = ? WHERE CardID = ?;",
                "UPDATE person SET Lname = ? WHERE CardID = ?;",
                "UPDATE person SET Address = ? WHERE CardID = ?;" };
        personEditorMap.put("person", personList);

        String[] albumList = { "UPDATE album SET NumberSongs = ? WHERE ItemID = ?;",
                "UPDATE album SET NumberMinutes = ? WHERE ItemID = ?;" };
        String[] trackList = { "UPDATE track SET NumberSeconds = ? WHERE ItemID = ?;",
                "UPDATE track SET AlbumID = ? WHERE ItemID = ?;" };
        String[] interviewList = { "UPDATE interview SET NumberMinutes = ? WHERE ItemID = ?;" };
        String[] movieList = { "UPDATE movie SET Runtime = ? WHERE ItemID = ?;",
                "UPDATE movie SET Rating = ? WHERE ItemID = ?;" };
        String[] audiobookList = { "UPDATE audiobook SET NumberChapters = ? WHERE ItemID = ?;",
                "UPDATE audiobook SET NumberMinutes = ? WHERE ItemID = ?;" };
        String[] physicalbookList = { "UPDATE physical_book SET NumberChapters = ? WHERE ItemID = ?;",
                "UPDATE physical_book SET NumberPages = ? WHERE ItemID = ?;" };
        itemEditorMap.put("album", albumList);
        itemEditorMap.put("track", trackList);
        itemEditorMap.put("interview", interviewList);
        itemEditorMap.put("movie", movieList);
        itemEditorMap.put("audiobook", audiobookList);
        itemEditorMap.put("physicalbook", physicalbookList);

        String[] actorList = { "UPDATE actor SET ar_name = ? WHERE creator_id = ?;",
                "UPDATE actor SET Date_Of_Birth = ? WHERE creator_id = ?;" };
        String[] artistList = { "UPDATE artist SET ar_name = ? WHERE creator_id = ?;",
                "UPDATE artist SET Date_Of_Birth = ? WHERE creator_id = ?;" };
        String[] directorList = { "UPDATE director SET ar_name = ? WHERE creator_id = ?;",
                "UPDATE director SET Date_Of_Birth = ? WHERE creator_id = ?;" };
        String[] writerList = { "UPDATE writer SET ar_name = ? WHERE creator_id = ?;",
                "UPDATE writer SET Date_Of_Birth = ? WHERE creator_id = ?;" };
        creatorEditorMap.put("actor", actorList);
        creatorEditorMap.put("artist", artistList);
        creatorEditorMap.put("director", directorList);
        creatorEditorMap.put("writer", writerList);

        itemAdderMap.put("item", "insert into item values (?,?,?,?,?);");
        itemAdderMap.put("album", "insert into album values (?,?,?);");
        itemAdderMap.put("track", "insert into track values (?,?,?);");
        itemAdderMap.put("interview", "insert into interview values (?,?);");
        itemAdderMap.put("movie", "insert into movie values (?,?,?);");
        itemAdderMap.put("audiobook", "insert into audiobook values (?,?,?);");
        itemAdderMap.put("physical_book", "insert into physical_book values (?,?,?);");

        creatorAdderMap.put("creator", "insert into creator values (?);");
        creatorAdderMap.put("artist", "insert into artist values (?,?,?);");
        creatorAdderMap.put("actor", "insert into actor values (?,?,?);");
        creatorAdderMap.put("director", "insert into director values (?,?,?);");
        creatorAdderMap.put("writer", "insert into writer values (?,?,?);");

        creatorSearcherMap.put("artist", "SELECT * FROM artist where ar_name = ?;");
        creatorSearcherMap.put("actor", "SELECT * FROM actor where ar_name = ?;");
        creatorSearcherMap.put("director", "SELECT * FROM director where ar_name = ?;");
        creatorSearcherMap.put("writer", "SELECT * FROM writer where ar_name = ?;");

        creatorDeleteMap.put("creator", "delete from creator where Creator_ID = ?;");
        creatorDeleteMap.put("artist", "delete from artist where Creator_ID = ?;");
        creatorDeleteMap.put("actor", "delete from actor where Creator_ID = ?;");
        creatorDeleteMap.put("director", "delete from director where Creator_ID = ?;");
        creatorDeleteMap.put("writer", "delete from writer where Creator_ID = ?;");

        nextIDMap.put("item", "SELECT Max(Item_ID) FROM item;");
        nextIDMap.put("creator", "SELECT Max(creator_ID) FROM creator;");
        nextIDMap.put("library_card", "SELECT Max(cardID) FROM library_card;");

        nextIDColumnMap.put("item", "Max(Item_ID)");
        nextIDColumnMap.put("creator", "Max(creator_ID)");
        nextIDColumnMap.put("library_card", "Max(cardID)");

        itemSearcherMap.put("album",
                "SELECT * FROM ITEM ,ALBUM WHERE title = ? AND ITEM.Item_ID = ALBUM.ItemID ");
        itemSearcherMap.put("track",
                "SELECT * FROM ITEM ,TRACK WHERE title = ? AND ITEM.Item_ID = TRACK.ItemID ");
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
