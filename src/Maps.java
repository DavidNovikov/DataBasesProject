import java.util.*;

public class Maps {
    public static Map<String, String[]> creatorEditMap;
    public static Map<String, String> itemAdderMap;

    // Instantiating the static maps
    static {
        creatorEditMap = new HashMap<>();
        String[] actorList = { "UPDATE actor SET ar_name = ? WHERE creator_id = ?;",
                "UPDATE actor SET Date_Of_Birth = ? WHERE creator_id = ?;" };
        String[] artistList = { "UPDATE artist SET ar_name = ? WHERE creator_id = ?;",
                "UPDATE artist SET Date_Of_Birth = ? WHERE creator_id = ?;" };
        String[] directorList = { "UPDATE director SET ar_name = ? WHERE creator_id = ?;",
                "UPDATE director SET Date_Of_Birth = ? WHERE creator_id = ?;" };
        String[] writerList = { "UPDATE writer SET ar_name = ? WHERE creator_id = ?;",
                "UPDATE writer SET Date_Of_Birth = ? WHERE creator_id = ?;" };
        creatorEditMap.put("actor", actorList);
        creatorEditMap.put("artist", artistList);
        creatorEditMap.put("director", directorList);
        creatorEditMap.put("writer", writerList);
    }

}
