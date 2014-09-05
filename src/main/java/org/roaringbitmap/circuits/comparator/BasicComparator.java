package org.roaringbitmap.circuits.comparator;

import org.roaringbitmap.RoaringBitmap;

public class BasicComparator implements GEQComparator {

    /**
     * Treating the bitmaps as a bit-sliced index, solve for >=K.
     * 
     * @param acc
     * @param K
     * @param universeSize
     * @return
     */
    @Override
    public RoaringBitmap compare(RoaringBitmap[] acc, int K, int universeSize) {
        int lastBitSlice = acc.length - 1;
        // use the O'Neil and Quass
        // comparison. (Algo 4.2)
        int beGtrThan = K - 1;

        RoaringBitmap beq = new RoaringBitmap();
        beq.flip(0, universeSize); // all ones bitmap
        RoaringBitmap bgt = new RoaringBitmap();

        int numberOfLeadingZeroSlices = 32 - (lastBitSlice + 1);
        if (Integer.numberOfLeadingZeros(beGtrThan) < numberOfLeadingZeroSlices)
            return bgt;

        // an improvement is that we can stop as soon as there are only trailing
        // ones in the constant.
        int finalSlice = 0;
        finalSlice = Integer.numberOfTrailingZeros(~beGtrThan); // omitted
                                                                // optimization

        for (int i = lastBitSlice; i >= finalSlice; --i)
            if ((beGtrThan & (1 << i)) != 0)
                beq.and(acc[i]);
            else {
                bgt.or(RoaringBitmap.and(beq, acc[i]));
                beq.andNot(acc[i]);
            }

        return bgt;

    }

    @Override
    public String name() {
        return "BasicGEQComparator";
    }

}
