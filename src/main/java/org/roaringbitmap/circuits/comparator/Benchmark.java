package org.roaringbitmap.circuits.comparator;

import org.roaringbitmap.RoaringBitmap;
import org.roaringbitmap.circuits.adder.TreeAdder;
import org.roaringbitmap.circuits.comparator.BasicComparator;
import org.roaringbitmap.circuits.comparator.OwenComparator;

public class Benchmark {
    public static GEQComparator[] T = { new BasicComparator(), new OwenComparator(),
         new OwenHorizontalComparator()};

    public static void main(String[] args) {
        System.out.println("Generating the data");
        final int N = 200;
        final int universe = 100000;
        RoaringBitmap[] b = new RoaringBitmap[N];
        for (int k = 0; k < b.length; ++k) {
            b[k] = new RoaringBitmap();
            b[k].flip(k, universe);
        }
        TreeAdder adder = new TreeAdder();
        b = adder.add(b);//to BitSliced
        for (GEQComparator t : T) {
            System.out.println("benchmarking " + t.name());
            System.out.println("dry run... ");
                
            for (int time = 0; time < 30; ++time)
                // JIT
                for (int threshVal = 1; threshVal <= N; ++threshVal) {
                    
                    if (t.compare(b, threshVal, universe).getCardinality() != universe
                            - threshVal + 1)
                        throw new RuntimeException("bug");

                }
            System.out.println("actual benchmark... ");
            for (int k = 0; k < 3; ++k) {
                long bef = System.currentTimeMillis();
                for (int time = 0; time < 100; ++time)
                    for (int threshVal = 1; threshVal <= N; ++threshVal) {
                        if (t.compare(b, threshVal, universe).getCardinality() != universe
                                - threshVal + 1)
                            throw new RuntimeException("bug");

                    }
                long aft = System.currentTimeMillis();
                System.out.println(t.name() + " time : " + (aft - bef));
            }
        }

    }

}
