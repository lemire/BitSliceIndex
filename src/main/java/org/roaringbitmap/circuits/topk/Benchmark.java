package org.roaringbitmap.circuits.topk;

import org.roaringbitmap.RoaringBitmap;
import org.roaringbitmap.circuits.comparator.BasicComparator;
import org.roaringbitmap.circuits.comparator.OwenComparator;
import org.roaringbitmap.circuits.comparator.OwenHorizontalComparator;

public class Benchmark {

    public static TopK[] Top = { new RinfretONeilTopK(),new TreeTopK(),new SSUMTopK(),
            new NaiveScanCountTopK() };

    public static void main(String[] args) {
        System.out.println("Generating the data");
        final int N = 2000;
        final int universe = 1000000;
        RoaringBitmap[] b = new RoaringBitmap[N];
        for (int k = 0; k < b.length; ++k) {
            b[k] = new RoaringBitmap();
            b[k].flip(0,k+1);
        }
        for (TopK t : Top) {
            System.out.println("benchmarking " + t.name());
            System.out.println("dry run... ");

            for (int time = 0; time < 3; ++time)
                // JIT
                for (int threshVal = 1; threshVal <= 10; ++threshVal) {
                    if(t.top(threshVal, universe,b).getCardinality()!=threshVal)
                        throw new RuntimeException("bug");
                }
            System.out.println("actual benchmark... ");
            for (int k = 0; k < 3; ++k) {
                long bef = System.currentTimeMillis();
                for (int time = 0; time < 3; ++time)
                    for (int threshVal = 1; threshVal <= 10; ++threshVal) {
                        if(t.top(threshVal, universe,b).getCardinality()!=threshVal)
                        throw new RuntimeException("bug");
                    }
                long aft = System.currentTimeMillis();
                System.out.println(t.name() + " time : " + (aft - bef));
            }
        }

    }

}
