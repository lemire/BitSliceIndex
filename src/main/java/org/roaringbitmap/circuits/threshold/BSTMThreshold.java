package org.roaringbitmap.circuits.threshold;

import org.roaringbitmap.*;
import org.roaringbitmap.circuits.adder.BSTMAdder;
import org.roaringbitmap.circuits.comparator.GEQComparator;

/**
 * This implements the BitSlice Sum from Rinfret and O'Neil and O'Neil a
 * greater-than from O'Neil and Quass It can be viewed as a threshold version of
 * the BSTM top-k term matching algorithm as described by Rinfret, O'Neil and
 * O'Neil.
 * 
 */

public class BSTMThreshold implements Thresholder<RoaringBitmap> {
    int K; // threshold
    int universeSize;
    GEQComparator comp;

    public BSTMThreshold(GEQComparator c) {
        comp = c;
    }

    @Override
    public void setup(int n, int threshVal, int maxVal) {
        if (threshVal <= 0)
            throw new IllegalArgumentException("Threshold should be postive");
        K = threshVal;
        universeSize = maxVal;
    }

    @Override
    public RoaringBitmap threshold(RoaringBitmap... set) {
        if (set.length == 0)
            return new RoaringBitmap();
        if (K == 1)
            return FastAggregation.horizontal_or(set);
        if (K == set.length)
            return FastAggregation.and(set);

        RoaringBitmap[] acc = (new BSTMAdder()).add(set);
        return comp.compare(acc, K, universeSize);

    }

    @Override
    public String name() {
        return "BSTM+" + comp.name();
    }

}