import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * CacheConfigReader is responsible for reading cache configuration from a JSON file.
 * It parses the JSON file and constructs CacheConfig objects for each cache defined in the file.
 */
public class CacheConfigReader {

    /**
     * CacheConfig is a nested class representing the configuration of a cache.
     * It contains fields for cache name, size, line size, cache kind, and replacement policy.
     */
    public static class CacheConfig {
        public String name;
        public int size;
        public int lineSize;
        public String kind;
        public String replacementPolicy;

    }

    /**
     * Reads cache configuration from the specified JSON file.
     *
     * @param filePath Path to the JSON file containing cache configuration
     * @return List of CacheConfig objects representing cache configurations
     * @throws FileNotFoundException if the specified file is not found
     */
    public List<CacheConfig> readConfig(String filePath) throws FileNotFoundException {
        JsonReader reader = Json.createReader(new FileInputStream(filePath));
        JsonObject jsonObject = reader.readObject();
        JsonArray cachesArray = jsonObject.getJsonArray("caches");

        List<CacheConfig> cacheConfigs = new ArrayList<>();

        // Iterate through each cache object in the JSON array
        for (JsonObject cacheObject : cachesArray.getValuesAs(JsonObject.class)) {
            CacheConfig config = new CacheConfig();
            config.name = cacheObject.getString("name");
            config.size = cacheObject.getInt("size");
            config.lineSize = cacheObject.getInt("line_size");
            config.kind = cacheObject.getString("kind");
            if (cacheObject.containsKey("replacement_policy")) {
                config.replacementPolicy = cacheObject.getString("replacement_policy");
            }
            cacheConfigs.add(config);
        }

        reader.close();
        return cacheConfigs;
    }
}
