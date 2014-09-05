package org.roaringbitmap.circuits.adder;

import org.roaringbitmap.RoaringBitmap;

/**
 * A adder will add the bitmaps as 0-1 counters.
 * 
 */
public interface Adder {
    public RoaringBitmap[] add(RoaringBitmap... set);
}
