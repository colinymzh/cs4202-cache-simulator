import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * TraceFileReader is responsible for reading memory trace from a file.
 */
public class TraceFileReader {

    /**
     * Reads memory addresses from the specified file.
     *
     * @param filePath Path to the file containing memory trace
     * @return Array of memory addresses read from the file
     * @throws Exception if an error occurs while reading the file
     */
    public long[] readFromFile(String filePath) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        long[] memoryAddresses = new long[lines.size()];

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.trim().split("\\s+");
            if (parts.length >= 2) {
                memoryAddresses[i] = Long.parseLong(parts[1], 16);
            }
        }

        return memoryAddresses;
    }
}
