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
    public static Map<String, String> creatorNameMap;
    public static Map<String, String> itemInsertType;

    // Instantiating the static maps
    static {
        creatorEditorMap = new HashMap<>();
        itemAdderMap = new HashMap<>();
        creatorAdderMap = new HashMap<>();
        creatorSearcherMap = new HashMap<>();
        creatorDeleteMap = new HashMap<>();
        nextIDMap = new HashMap<>();
        nextIDColumnMap = new HashMap<>();
        itemEditorMap = new HashMap<>();
        creatorNameMap = new HashMap<>();
        itemInsertType = new HashMap<>();

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

        creatorNameMap.put("artist", "Ar_Name");
        creatorNameMap.put("actor", "Ar_Name");
        creatorNameMap.put("director", "Ar_Name");
        creatorNameMap.put("writer", "Ar_Name");

        nextIDMap.put("item", "SELECT Max(Item_ID) FROM item;");
        nextIDMap.put("creator", "SELECT Max(creator_ID) FROM creator;");
        nextIDMap.put("library_card", "SELECT Max(cardID) FROM library_card;");

        nextIDColumnMap.put("item", "Max(Item_ID)");
        nextIDColumnMap.put("creator", "Max(creator_ID)");
        nextIDColumnMap.put("library_card", "Max(cardID)");

        itemInsertType.put("album", "ALBUM");
        itemInsertType.put("track", "Track");
        itemInsertType.put("interview", "interview");
        itemInsertType.put("movie", "MOVIE");
        itemInsertType.put("audiobook", "ABook");
        itemInsertType.put("physicalbook", "PBook");
    }

}
