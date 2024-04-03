import java.util.Arrays;

/**
 * DirectCache represents a direct-mapped cache implementation.
 * It extends the Cache class and implements the visit method for cache simulation.
 */
public class DirectCache extends Cache {
    private final long[] tags; // Array to store tags for cache blocks
    private final int indexSize; // Size of cache index field in bits
    private final int offsetSize; // Size of cache offset field in bits

    /**
     * Constructs a DirectCache object with the specified size and block size.
     *
     * @param size      Size of the cache in bytes
     * @param blockSize Size of each cache block in bytes
     */
    public DirectCache(int size, int blockSize) {
        this.blockSize = blockSize;
        this.size = size;
        this.rp = ReplacementPolicy.LRU;
        this.associativity = 0;
        int blockNum = size / blockSize;
        this.tags = new long[blockNum];
        Arrays.fill(this.tags, -1);

        this.offsetSize = (int)(Math.log(blockSize) / Math.log(2));
        this.indexSize = (int)(Math.log(blockNum) / Math.log(2));
    }

    /**
     * Simulates visiting a memory address in the cache.
     * Updates hit number and visit number accordingly.
     *
     * @param addr Memory address to visit
     * @return True if the visit results in a cache hit, false otherwise
     */
    @Override
    public boolean visit(long addr) {
        visitNumber++;
        long index = (addr >> offsetSize) & ((1L << indexSize) - 1);
        long tag = addr >> (indexSize + offsetSize);
        if (tags[(int)index] == tag) {
            hitNumber++;
            return true;
        } else {
            tags[(int)index] = tag;
            return false;
        }
    }
}