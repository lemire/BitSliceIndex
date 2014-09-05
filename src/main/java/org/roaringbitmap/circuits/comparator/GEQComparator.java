package org.roaringbitmap.circuits.comparator;

import org.roaringbitmap.RoaringBitmap;

public interface GEQComparator {
    /**
     * Treating the bitmaps as a bit-sliced index, solve for >=K.
     * 
     * @param acc
     * @param K
     * @param universeSize
     * @return
     */
    public RoaringBitmap compare(RoaringBitmap[] acc, int K, int universeSize);

    public String name();
}
