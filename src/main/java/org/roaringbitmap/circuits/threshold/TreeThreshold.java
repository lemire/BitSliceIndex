package org.roaringbitmap.circuits.threshold;

import org.roaringbitmap.FastAggregation;
import org.roaringbitmap.RoaringBitmap;
import org.roaringbitmap.circuits.adder.TreeAdder;
import org.roaringbitmap.circuits.comparator.GEQComparator;

public class TreeThreshold implements Thresholder<RoaringBitmap> {
    int K; // threshold

    int universeSize;

    GEQComparator comp;

    public TreeThreshold(GEQComparator c) {
        comp = c;
    }

    @Override
    public void setup(int numInputs, int threshold, int maxValue) {
        if (threshold <= 0)
            throw new IllegalArgumentException("Threshold should be postive");
        K = threshold;
        universeSize = maxValue;
    }

    @Override
    public RoaringBitmap threshold(RoaringBitmap... set) {
        if (set.length == 0)
            return new RoaringBitmap();
        if (K == 1)
            return FastAggregation.horizontal_or(set);
        if (K == set.length)
            return FastAggregation.and(set);
        RoaringBitmap[] acc = (new TreeAdder()).add(set);
        return comp.compare(acc, K, universeSize);
    }

    @Override
    public String name() {
        return "tree+" + comp.name();
    }

}
