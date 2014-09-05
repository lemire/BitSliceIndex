package org.roaringbitmap.circuits.topk;

import org.roaringbitmap.IntIterator;
import org.roaringbitmap.RoaringBitmap;
import org.roaringbitmap.circuits.adder.BSTMAdder;

// Algorithm 4.1 from Bit-Sliced Index Arithmetic by Rinfret, O'Neil and O'Neil
public class RinfretONeilTopK implements TopK {

    public String name() {
        return "RinfretONeilTopK";
    }

    @Override
    public RoaringBitmap top(final int k, int universe, RoaringBitmap... b) {
        if ((k < 0) || (k > universe))
            throw new IllegalArgumentException("bad k");
        BSTMAdder adder = new BSTMAdder();
        
        RoaringBitmap[] bsi = adder.add(b);
        RoaringBitmap G = new RoaringBitmap();
        RoaringBitmap E = null;
        for (int i = bsi.length - 1; i >= 0; i--) {

            RoaringBitmap X;
            if (E == null) {
                X = bsi[i];
            } else {
                X = RoaringBitmap.or(G, RoaringBitmap.and(E, bsi[i]));
            }
            int n = X.getCardinality();
            if (n > k)
                if (E == null)
                    E = bsi[i].clone();
                else
                    E.and(bsi[i]);
            else if (n < k) {
                G = X;
                if (E == null)
                    E = RoaringBitmap.flip(bsi[i], 0, universe);
                else
                    E.andNot(bsi[i]);
            } else {
                if (E == null)
                    E = bsi[i];
                else
                    E.and(bsi[i]);
                break;
            }
        }
        G.or(E);
        int n = G.getCardinality() - k;
        if (n > 0) {
            // TODO: make faster
            IntIterator i = G.getIntIterator();
            RoaringBitmap turnoff = new RoaringBitmap();
            while (i.hasNext() && n > 0) {
                turnoff.add(i.next());
                --n;
            }
            G.andNot(turnoff);
        }
        if (G.getCardinality() != k)
            throw new RuntimeException("bug");
        return G;
    }
}