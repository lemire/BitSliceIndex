package org.roaringbitmap.circuits.comparator;

import java.util.ArrayList;

import org.roaringbitmap.FastAggregation;
import org.roaringbitmap.RoaringBitmap;

//this as per Owen's tech report, section 4.4.2 but uses horizontal aggregation
public class OwenHorizontalComparator implements GEQComparator {

    /**
     * Treating the bitmaps as a bit-sliced index, solve for >=K.
     * 
     * @param acc
     *            bitmaps in bit-sliced format
     * @param K
     *            threshold (>=)
     * @param universeSize
     *            useless here
     * @return
     */
    @Override
    public RoaringBitmap compare(RoaringBitmap[] acc, int K, int universeSize) {
        RoaringBitmap lastSpineGate = null;
        int beGtrThan = K - 1;
        ArrayList<RoaringBitmap> orInputs = new ArrayList<RoaringBitmap>();
        int leastSignifZero = Long.numberOfTrailingZeros(~beGtrThan);
        // work from most significant bit down to the last 1.
        for (int workingBit = acc.length - 1; workingBit >= leastSignifZero; --workingBit) {
            if ((beGtrThan & (1L << workingBit)) == 0L) {
                if (lastSpineGate == null) // don't make a singleton AND!
                    orInputs.add(acc[workingBit]);
                else {
                    // really make the AND
                    orInputs.add(RoaringBitmap.and(lastSpineGate,
                            acc[workingBit]));
                }
            } else {
                if (lastSpineGate == null)
                    lastSpineGate = acc[workingBit];
                else
                    lastSpineGate = RoaringBitmap.and(lastSpineGate,
                            acc[workingBit]);
            }
        }
        return FastAggregation.horizontal_or(orInputs.toArray(new RoaringBitmap[0]));
    }

    @Override
    public String name() {
        return "OwenHorizontalGEQComparator";
    }


}
