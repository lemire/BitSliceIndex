package org.roaringbitmap.circuits.threshold;

import org.roaringbitmap.*;

/**
 * This implements the BitSlice Sum from Rinfret and O'Neil and O'Neil a
 * greater-than from O'Neil and Quass It can be viewed as a threshold version of
 * the BSTM top-k term matching algorithm as described by Rinfret, O'Neil and
 * O'Neil.
 * 
 */

public class NaiveScanCountThreshold implements Thresholder<RoaringBitmap> {
    int K; // threshold
    int universeSize;
    int[] counters;

    public NaiveScanCountThreshold() {
    }

    @Override
    public void setup(int n, int threshVal, int maxVal) {
        if (threshVal <= 0)
            throw new IllegalArgumentException("Threshold should be postive");
        K = threshVal;
        universeSize = maxVal;
        if ((counters == null) || (counters.length != universeSize))
            counters = new int[universeSize];
    }

    @Override
    public RoaringBitmap threshold(RoaringBitmap... set) {
        if (set.length == 0)
            return new RoaringBitmap();
        for (int k = 0; k < counters.length; ++k)
            counters[k] = 0;
        for (RoaringBitmap rb : set) {
            IntIterator i = rb.getIntIterator();
            while (i.hasNext())
                counters[i.next()]++;
        }
        RoaringBitmap answer = new RoaringBitmap();
        for (int k = 0; k < counters.length; ++k)
            if (counters[k] >= K)
                answer.add(k);
        return answer;
    }

    @Override
    public String name() {
        return "NaiveScanCountThreshold";
    }

}