package org.roaringbitmap.circuits;

import org.roaringbitmap.*;
import org.roaringbitmap.circuits.adder.TreeAdder;
import org.roaringbitmap.circuits.comparator.*;
import org.roaringbitmap.circuits.threshold.*;
import org.roaringbitmap.circuits.topk.NaiveScanCountTopK;
import org.roaringbitmap.circuits.topk.RinfretONeilTopK;
import org.roaringbitmap.circuits.topk.SSUMTopK;
import org.roaringbitmap.circuits.topk.TopK;
import org.roaringbitmap.circuits.topk.TreeTopK;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("rawtypes")
public class BasicTest {

    public static Thresholder[] T = {
            new SSUMThreshold(new OwenHorizontalComparator()),
            new TreeThreshold(new OwenHorizontalComparator()),
            new BSTMThreshold(new OwenHorizontalComparator()),
            new SSUMThreshold(new OwenComparator()),
            new TreeThreshold(new OwenComparator()),
            new BSTMThreshold(new OwenComparator()),
            new NaiveScanCountThreshold(),
            new SSUMThreshold(new BasicComparator()),
            new TreeThreshold(new BasicComparator()),
            new BSTMThreshold(new BasicComparator()) };

    public static TopK[] Top = { new RinfretONeilTopK(),new TreeTopK(),new SSUMTopK(),
            new NaiveScanCountTopK() };

    @Test
    public void topK() {
        System.out.println("[top k test]");

        int N = 100;
        RoaringBitmap[] b = new RoaringBitmap[N];
        for (int k = 0; k < b.length; ++k) {
            b[k] = new RoaringBitmap();
            for (int l = 0; l < k; ++l)
                b[k].add(l);
        }
        for (TopK t : Top) {
            for (int j = 0; j < N; ++j) {
                RoaringBitmap answer = t.top(j, N, b);
                Assert.assertTrue(answer.equals(b[j]));
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void basic() {
        System.out.println("[basic threshold test]");
        Thresholder ref = new NaiveScanCountThreshold();
        for (Thresholder t : T) {
            System.out.println("testing " + t.name());
            for (int N = 1; N <= 100; N *= 10) {
                for (int universe = N; universe <= 10000; universe *= 10) {
                    RoaringBitmap[] b = new RoaringBitmap[N];
                    for (int k = 0; k < b.length; ++k) {
                        b[k] = new RoaringBitmap();
                        b[k].flip(k, universe);
                    }
                    for (int threshVal = 1; threshVal <= N; ++threshVal) {
                        t.setup(N, threshVal, universe);
                        ref.setup(N, threshVal, universe);
                        RoaringBitmap ans = (RoaringBitmap) t.threshold(b);
                        Assert.assertEquals(ans.getCardinality(), universe
                                - threshVal + 1);
                        RoaringBitmap anstrue = (RoaringBitmap) ref
                                .threshold(b);
                        Assert.assertTrue(ans.equals(anstrue));
                    }
                }
            }
        }
    }

}