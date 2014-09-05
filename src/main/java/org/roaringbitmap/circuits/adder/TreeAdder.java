package org.roaringbitmap.circuits.adder;

import java.util.ArrayList;

import org.roaringbitmap.RoaringBitmap;

/**
 * A adder will add the bitmaps as 0-1 counters.
 * 
 */
public class TreeAdder implements Adder {

    @Override
    public RoaringBitmap[] add(RoaringBitmap... set) {
        if (set.length == 0)
            return new RoaringBitmap[0];
        if (set.length == 1) {
            RoaringBitmap[] answer = new RoaringBitmap[1];
            answer[0] = set[0].clone();
            return answer;
        }
        ArrayList<RoaringBitmap[]> answer = new ArrayList<RoaringBitmap[]>();
        for (int k = 0; k < set.length; ++k) {
            RoaringBitmap[] tmp = new RoaringBitmap[1];
            tmp[0] = set[k];
            answer.add(tmp);
        }
        while (answer.size() > 1) {
            boolean isodd = ((answer.size() & 1) == 1);
            ArrayList<RoaringBitmap[]> newanswer = new ArrayList<RoaringBitmap[]>(
                    isodd ? answer.size() / 2 + 1 : answer.size() / 2);
            for (int k = 0; k + 1 < answer.size(); k += 2) {
                newanswer
                        .add(RippleCarry.add(answer.get(k), answer.get(k + 1)));
            }
            if ((answer.size() & 1) == 1)
                newanswer.add(answer.get(answer.size() - 1));
            answer = newanswer;
        }
        return answer.get(0);
    }

}
