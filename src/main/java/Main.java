import javax.json.*;
import javax.json.stream.JsonGenerator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main class contains the main method to execute the cache simulation.
 * It reads cache configurations and memory trace from files, simulates cache behavior,
 * and generates a JSON report of cache hits, misses, and main memory accesses.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Usage: ./my-cache-simulator <config.json> <tracefile.trace>");
            return;
        }
        String configFilePath = args[0];
        String traceFilePath = args[1];

        List<Cache> caches = new ArrayList<>();

        // Read memory addresses from trace file
        TraceFileReader trace = new TraceFileReader();
        long[] addresses = trace.readFromFile(traceFilePath);
//        long[] addresses = trace.readFromFile("TestData/TraceFile/leela.out");

        // Read cache configurations from JSON file
        CacheConfigReader reader = new CacheConfigReader();
        List<CacheConfigReader.CacheConfig> configs = reader.readConfig(configFilePath);
//        List<CacheConfigReader.CacheConfig> configs = reader.readConfig("TestData/ConfigurationFile/l1l2l3.json");

        // Create caches based on configurations
        for (CacheConfigReader.CacheConfig config : configs) {
            if (config.kind.equals("direct")){
                caches.add(CacheFactory.plantCacheDirect(config.size, config.lineSize));


            } else{
                int groupNum;
                ReplacementPolicy rp;
                switch (config.kind) {
                    case "full":
                        groupNum = 1;
                        break;
                    case "2way":
                        groupNum = 2;
                        break;
                    case "4way":
                        groupNum = 4;
                        break;
                    case "8way":
                        groupNum = 8;
                        break;
                    default:
                        groupNum = -1;
                        break;
                }

                switch (config.replacementPolicy) {
                    case "rr":
                        rp = ReplacementPolicy.RR;
                        break;
                    case "lfu":
                        rp = ReplacementPolicy.LFU;
                        break;
                    case "lru":
                        rp = ReplacementPolicy.LRU;
                        break;
                    default:

                        rp = null;
                        break;
                }
                caches.add(CacheFactory.plantCacheGroup(config.size, config.lineSize, groupNum, rp));
            }
        }

        // Simulate cache behavior for each memory address
        for (long addr : addresses) {
            boolean hit = false;
            for (Cache cache : caches) {
                hit = cache.visit(addr);
                if (hit) {
                    break;
                }
            }
        }

        // Calculate main memory accesses
        Cache lastCache = caches.get(caches.size() - 1);
        int mainMemoryAccesses = lastCache.getVisitNumber() - lastCache.getHitNumber();

        // Build JSON report
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

        for (Cache cache : caches) {
            JsonObjectBuilder cacheJsonBuilder = Json.createObjectBuilder()
                    .add("hits", cache.getHitNumber())
                    .add("misses", cache.getVisitNumber() - cache.getHitNumber())
                    .add("name", "L" + (caches.indexOf(cache) + 1));

            jsonArrayBuilder.add(cacheJsonBuilder);
        }

        jsonBuilder.add("caches", jsonArrayBuilder);
        jsonBuilder.add("main_memory_accesses", mainMemoryAccesses);

        // Create JSON writer factory with pretty printing enabled
        Map<String, Object> properties = new HashMap<>();
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);

        // Write JSON report to standard output
        try (JsonWriter writer = writerFactory.createWriter(System.out)) {
            writer.writeObject(jsonBuilder.build());
        }
    }
}
