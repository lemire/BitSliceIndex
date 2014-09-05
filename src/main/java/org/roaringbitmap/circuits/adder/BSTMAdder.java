package org.roaringbitmap.circuits.adder;

import java.util.Arrays;

import org.roaringbitmap.RoaringBitmap;

/**
 * A adder will add the bitmaps as 0-1 counters.
 * 
 */
public class BSTMAdder implements Adder {
    @Override
    public RoaringBitmap[] add(RoaringBitmap... set) {
        final int numSumBitmaps = 32 - Integer.numberOfLeadingZeros(set.length);
        int lastBitSlice = 0;
        RoaringBitmap[] acc = new RoaringBitmap[numSumBitmaps + 1]; // null
                                                                    // sentinel
                                                                    // at end
        acc[0] = set[0];
        for (int k = 1; k < set.length; ++k) {
            RoaringBitmap c = RoaringBitmap.and(set[k], acc[0]);
            if (k == 1)
                acc[0] = RoaringBitmap.xor(acc[0], set[k]);
            else
                acc[0].xor(set[k]);
            int i;
            for (i = 1; acc[i] != null; ++i) {
                RoaringBitmap temp1 = RoaringBitmap.xor(acc[i], c);
                c.and(acc[i]);
                acc[i] = temp1;
            }

            if (!c.isEmpty()) {
                acc[i] = c;
                lastBitSlice = i;
            }
        }
        return Arrays.copyOf(acc, lastBitSlice + 1);

    }
}
