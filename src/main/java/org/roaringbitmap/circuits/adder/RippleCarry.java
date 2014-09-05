package org.roaringbitmap.circuits.adder;

import java.util.ArrayList;

import org.roaringbitmap.RoaringBitmap;

/**
 * Standard algorithm to add bit-sliced bitmaps.
 * 
 */
public class RippleCarry {

    /**
     * Returns as few bitmaps as needed, the first one carrying the least
     * significant bits.
     * 
     * @param a
     * @param b
     * @return
     */
    public static RoaringBitmap[] add(RoaringBitmap[] a, RoaringBitmap[] b) {
        int mini = Math.min(a.length, b.length);
        int maxi = Math.max(a.length, b.length);
        ArrayList<RoaringBitmap> answer = new ArrayList<RoaringBitmap>(maxi + 1);
        int i = 0;
        RoaringBitmap carry = new RoaringBitmap();
        if (mini > 0) {
            answer.add(RoaringBitmap.xor(a[i], b[i]));
            carry = RoaringBitmap.and(a[i], b[i]);
            ++i;
        }
        for (; i < mini; ++i) {
            RoaringBitmap tmp = RoaringBitmap.xor(a[i], b[i]);
            answer.add(RoaringBitmap.xor(carry, tmp));
            carry.and(tmp);
            carry.or(RoaringBitmap.and(a[i], b[i]));
        }
        if (mini != maxi) {
            RoaringBitmap[] longest = a.length > b.length ? a : b;
            for (; i < maxi; ++i) {
                if (carry.isEmpty()) {
                    answer.add(longest[i].clone());
                } else {
                    answer.add(RoaringBitmap.xor(carry, longest[i]));
                    carry.and(longest[i]);
                }
            }
        }
        if (!carry.isEmpty())
            answer.add(carry);
        return answer.toArray(a);
    }

}
