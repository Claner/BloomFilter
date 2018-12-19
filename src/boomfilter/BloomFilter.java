package boomfilter;

import java.util.BitSet;

public class BloomFilter {

    /**
     * 根据需要修改size的大小
     */
    private final int size = 1 << 20;
    private int count;
    private BitSet bits;
    private Hash[] func;
    private final int[] seeds = new int[]{3, 5, 7, 11, 13, 31, 37, 61};

    public BloomFilter() {
        bits = new BitSet(size);
        func = new Hash[seeds.length];
        for (int i = 0; i < seeds.length; i++) {
            func[i] = new Hash(size, seeds[i]);
        }
    }

    public void add(String value) {
        if (!"".equals(value) && !contains(value)) {
            for (Hash f : func) {
                bits.set(f.hash(value), true);
            }
            count++;
        }
    }

    public boolean contains(String value) {
        if ("".equals(value)) {
            return false;
        }
        for (Hash f : func) {
            if (!bits.get(f.hash(value))) {
                return false;
            }
        }
        return true;
    }

    private static class Hash {
        private int size;
        private int seed;

        public Hash(int size, int seed) {
            this.size = size;
            this.seed = seed;
        }

        private int hash(String value) {
            int result = 0;
            int len = value.length();
            for (int i = 0; i < len; i++) {
                result = seed * result + value.charAt(i);
            }
            return (size - 1) & result;
        }
    }

    public int getCount() {
        return count;
    }
}
