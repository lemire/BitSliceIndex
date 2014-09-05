package org.roaringbitmap.circuits.topk;

import org.roaringbitmap.IntIterator;
import org.roaringbitmap.RoaringBitmap;

// top-k from 
public class NaiveScanCountTopK implements TopK {

    @Override
    public RoaringBitmap top(int k, int universe, RoaringBitmap... bsi) {
        if ((k < 0) || (k > universe))
            throw new IllegalArgumentException("bad k");
        if(bsi.length == 0)
            throw new IllegalArgumentException("no bitmap");
        int[] counters = new int[universe];
        for (RoaringBitmap rb : bsi) {
            IntIterator i = rb.getIntIterator();
            while (i.hasNext())
                counters[i.next()]++;
        }
        RoaringBitmap[] rb = new RoaringBitmap[bsi.length+1];
        for (int l = 0; l < rb.length; ++l)
            rb[l] = new RoaringBitmap();
        for (int l = 0; l < counters.length; ++l)
            rb[counters[l]].add(l);
        RoaringBitmap answer = rb[rb.length-1];
        int J = rb.length-1;
        while(answer.getCardinality()<k) {
            J--;
            answer.or(rb[J]);
        }
        int n = answer.getCardinality() - k;
        if (n > 0) {
            IntIterator i = answer.getIntIterator();
            RoaringBitmap turnoff = new RoaringBitmap();
            while (i.hasNext() && n > 0) {
                turnoff.add(i.next());
                --n;
            }
            answer.andNot(turnoff);
        }
        if (answer.getCardinality() != k)
            throw new RuntimeException("bug");
        return answer;
    }

    @Override
    public String name() {
        return "NaiveScanCountTopK";
    }

}
