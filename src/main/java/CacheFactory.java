/**
 * CacheFactory is a factory class responsible for creating different types of caches.
 */
public class CacheFactory {

    /**
     * Creates a direct-mapped cache.
     *
     * @param size      Size of the cache in bytes
     * @param blockSize Size of each cache block in bytes
     * @return DirectCache object representing a direct-mapped cache
     */
    public static Cache plantCacheDirect(int size, int blockSize) {
        return new DirectCache(size, blockSize);
    }

    /**
     * Creates a set-associative cache.
     *
     * @param size          Size of the cache in bytes
     * @param blockSize     Size of each cache block in bytes
     * @param associativity Associativity of the cache (number of sets)
     * @param rp            ReplacementPolicy object representing the cache replacement policy
     * @return GroupCache object representing a set-associative cache
     */
    public static Cache plantCacheGroup(int size, int blockSize, int associativity, ReplacementPolicy rp) {
        return new GroupCache(size, blockSize, associativity, rp);
    }
}