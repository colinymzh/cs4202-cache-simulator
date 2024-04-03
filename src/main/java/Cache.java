/**
 * Cache is an abstract class representing a memory cache simulator.
 * It defines the basic structure and behavior of a cache, such as block size,
 * cache size, replacement policy, associativity, hit number, and visit number.
 * Subclasses must implement the visit method for cache simulation.
 */
public abstract class Cache {
    protected int blockSize; // Size of each cache block in bytes
    protected int size; // Total size of the cache in bytes
    protected ReplacementPolicy rp; // Replacement policy for cache eviction
    protected int associativity; // Associativity of the cache
    protected int hitNumber = 0; // Number of cache hits
    protected int visitNumber = 0; // Number of cache visits

    /**
     * Abstract method to simulate cache visit for a given memory address.
     *
     * @param addr Memory address to visit
     * @return True if the visit results in a cache hit, false otherwise
     */
    public abstract boolean visit(long addr);

    /**
     * Calculates the hit rate of the cache.
     *
     * @return Hit rate of the cache as a decimal value
     */
    public double hitRate() {
        return (double) hitNumber / visitNumber;
    }

    public int getHitNumber(){
        return hitNumber;
    }

    /**
     * Retrieves the number of cache visits.
     *
     * @return Number of cache visits
     */
    public int getVisitNumber(){
        return visitNumber;
    }

}
