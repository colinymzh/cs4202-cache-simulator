import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * GroupCache represents a set-associative cache implementation.
 * It extends the Cache class and implements the visit method for cache simulation.
 */
public class GroupCache extends Cache {
    private final LinkedList[] blockGroups; // Array of linked lists representing cache block groups
    private final int[] roundRobinPointers; // Array to store round-robin replacement pointers
    private final Map<Long, Integer> frequencyMap; // Map to store tag frequencies
    private final int indexSize; // Size of cache index field in bits
    private final int offsetSize; // Size of cache offset field in bits

    /**
     * Constructs a GroupCache object with the specified size, block size, group size, and replacement policy.
     *
     * @param size      Size of the cache in bytes
     * @param blockSize Size of each cache block in bytes
     * @param groupSize Size of each cache group (associativity)
     * @param rp        ReplacementPolicy object representing the cache replacement policy
     */
    public GroupCache(int size, int blockSize, int groupSize, ReplacementPolicy rp) {
        this.blockSize = blockSize;
        this.rp = rp;
        this.size = size;
        this.associativity = groupSize;
        int groupNum = size / blockSize / groupSize;
        blockGroups = new LinkedList[groupNum];
        for (int i = 0; i < groupNum; i++) {
            blockGroups[i] = new LinkedList<Long>();
        }

        this.offsetSize = (int)(Math.log(blockSize) / Math.log(2));
        this.indexSize = (int)(Math.log(groupNum) / Math.log(2));
        this.roundRobinPointers = new int[groupNum];
        this.frequencyMap = new HashMap<>();
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
        LinkedList<Long> blockGroup = blockGroups[(int)index];

        if (blockGroup.contains(tag)) {
            hitNumber++;
            if (rp.equals(ReplacementPolicy.LRU)) {
                blockGroup.remove(tag);
                blockGroup.addFirst(tag);
            } else if (rp.equals(ReplacementPolicy.LFU)) {
                frequencyMap.put(tag, frequencyMap.getOrDefault(tag, 0) + 1);
            }
            return true;
        } else {
            if (blockGroup.size() == associativity) {
                if (rp.equals(ReplacementPolicy.RR)) {
                    int rrIndex = roundRobinPointers[(int)index];
                    blockGroup.remove(rrIndex);
                    roundRobinPointers[(int)index] = (rrIndex + 1) % associativity;
                } else if (rp.equals(ReplacementPolicy.LFU)) {
                    Long leastFrequentTag = findLeastFrequentTag(blockGroup);
                    blockGroup.remove(leastFrequentTag);
                    frequencyMap.remove(leastFrequentTag);
                } else {
                    blockGroup.removeLast();
                }
            }
            blockGroup.addFirst(tag);
            frequencyMap.put(tag, frequencyMap.getOrDefault(tag, 0) + 1);
            return false;
        }
    }

    /**
     * Finds the least frequent tag in the given block group based on frequency map.
     *
     * @param blockGroup LinkedList representing a cache block group
     * @return The least frequent tag in the block group
     */
    private Long findLeastFrequentTag(LinkedList<Long> blockGroup) {
        Long leastFrequentTag = null;
        int minFreq = Integer.MAX_VALUE;
        for (Long tag : blockGroup) {
            int freq = frequencyMap.getOrDefault(tag, 0);
            if (freq < minFreq) {
                minFreq = freq;
                leastFrequentTag = tag;
            }
        }
        return leastFrequentTag;
    }

}
