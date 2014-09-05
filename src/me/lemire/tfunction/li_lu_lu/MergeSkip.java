package me.lemire.tfunction.li_lu_lu;

import java.util.Arrays;

import me.lemire.tfunction.ThresholdFunction;
import me.lemire.tfunction.heap.ArrayPointer;
import me.lemire.tfunction.heap.ArrayPointerHeap;

/**
 * From :
 * 
 * Chen Li, Jiaheng Lu, Yiming Lu Efficient Merging and Filtering Algorithms for
 * Approximate String Search In ICDE'08
 * 
 * cited 130 times as of July 2013 Implemented by Owen Kaser from a skeleton by
 * D. Lemire
 * 
 */
public class MergeSkip implements ThresholdFunction {


        @Override
        public int[] threshold(int min, int[]... set) {
                if (min < 1)
                        throw new IllegalArgumentException("must be >=1");
                if (min > set.length)
                        return new int[0];
                final ArrayPointer[] hasBeenPopped = new ArrayPointer[set.length];
                int hasBeenPoppedListCtr = 0;

                final ArrayPointerHeap h = new ArrayPointerHeap();
                for (int k = 0; k < set.length; ++k) {
                        h.insert(new ArrayPointer(set[k]));
                }
                int[] answer = new int[32];
                int pos = 0;
                while (h.size() >= min) { 
                        hasBeenPoppedListCtr = 0;
                        ArrayPointer t = h.popWithoutAdvancing();
                        hasBeenPopped[hasBeenPoppedListCtr++] = t;
                        int n = 1;
                        while (!h.isEmpty() && h.peek() == t.value()) {
                                ++n;
                                hasBeenPopped[hasBeenPoppedListCtr++] =  h.popWithoutAdvancing();
                        }
                        if (n >= min) {
                                if (pos == answer.length)
                                        answer = Arrays.copyOf(answer,
                                                answer.length * 3 / 2);
                                answer[pos++] = t.value();
                                for (int i = 0; i < hasBeenPoppedListCtr; ++i) {
                                        ArrayPointer ap = hasBeenPopped[i];
                                        if (ap.advance())
                                                h.insert(ap);
                                }
                        } else {
                                // do the extra popping
                                for (int i = 0; i < min - 1 - n; ++i)
                                      hasBeenPopped[hasBeenPoppedListCtr++] =  h.popWithoutAdvancing();
                                int t1 = h.peek();
                                for (int i = 0; i < hasBeenPoppedListCtr; ++i) {
                                        ArrayPointer ap = hasBeenPopped[i];
                                        ap.moveForward(t1);
                                        if (ap.hasValue())
                                                h.insert(ap);
                                }
                        }
                }
                return Arrays.copyOf(answer, pos);
        }

        @Override
        public String name() {
                return "MergeSkip";
        }

}
