package org.roaringbitmap.circuits.threshold;

import org.roaringbitmap.FastAggregation;
import org.roaringbitmap.RoaringBitmap;
import org.roaringbitmap.circuits.adder.SSUMAdder;
import org.roaringbitmap.circuits.comparator.GEQComparator;

public class SSUMThreshold  implements Thresholder<RoaringBitmap> {
    int K; // threshold
    int universeSize;
    GEQComparator comp;

    public SSUMThreshold(GEQComparator c) {
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

        RoaringBitmap[] acc = (new SSUMAdder()).add(set);
        return comp.compare(acc, K, universeSize);

    }

    @Override
    public String name() {
        return "SSUM+" + comp.name();
    }

}