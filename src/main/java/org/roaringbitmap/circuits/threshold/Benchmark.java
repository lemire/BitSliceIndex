package org.roaringbitmap.circuits.threshold;

import org.roaringbitmap.RoaringBitmap;
import org.roaringbitmap.circuits.comparator.BasicComparator;
import org.roaringbitmap.circuits.comparator.OwenComparator;
import org.roaringbitmap.circuits.comparator.OwenHorizontalComparator;

public class Benchmark {
    // TODO: actually, given a adder and a comparator, we ought to be able to do
    // a threshold, so too many classes here
    public static Thresholder[] T = { new SSUMThreshold(new BasicComparator()),
            new TreeThreshold(new BasicComparator()),
            new BSTMThreshold(new BasicComparator()),
            new SSUMThreshold(new OwenComparator()),
            new TreeThreshold(new OwenComparator()),
            new BSTMThreshold(new OwenComparator()),
            new SSUMThreshold(new OwenHorizontalComparator()),
            new TreeThreshold(new OwenHorizontalComparator()),
            new BSTMThreshold(new OwenHorizontalComparator()),
            new NaiveScanCountThreshold() };

    public static void main(String[] args) {
        System.out.println("Generating the data");
        final int N = 200;
        final int universe = 100000;
        RoaringBitmap[] b = new RoaringBitmap[N];
        for (int k = 0; k < b.length; ++k) {
            b[k] = new RoaringBitmap();
            b[k].flip(k, universe);
        }
        for (Thresholder t : T) {
            System.out.println("benchmarking " + t.name());
            System.out.println("dry run... ");

            for (int time = 0; time < 3; ++time)
                // JIT
                for (int threshVal = 1; threshVal <= N; ++threshVal) {
                    t.setup(N, threshVal, universe);
                    if (((RoaringBitmap) t.threshold(b)).getCardinality() != universe
                            - threshVal + 1)
                        throw new RuntimeException("bug");

                }
            System.out.println("actual benchmark... ");
            for (int k = 0; k < 3; ++k) {
                long bef = System.currentTimeMillis();
                for (int time = 0; time < 3; ++time)
                    for (int threshVal = 1; threshVal <= N; ++threshVal) {
                        t.setup(N, threshVal, universe);
                        if (((RoaringBitmap) t.threshold(b)).getCardinality() != universe
                                - threshVal + 1)
                            throw new RuntimeException("bug");

                    }
                long aft = System.currentTimeMillis();
                System.out.println(t.name() + " time : " + (aft - bef));
            }
        }

    }

}
