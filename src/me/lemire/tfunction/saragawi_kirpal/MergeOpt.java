package me.lemire.tfunction.saragawi_kirpal;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;

import me.lemire.tfunction.ThresholdFunction;
import me.lemire.tfunction.heap.ArrayPointer;
import me.lemire.tfunction.heap.ArrayPointerHeap;

/**
 * From :
 * 
 * Sunita Sarawagi and Alok Kirpal. Efficient set joins on similarity predicates. 
 * In Proceedings of the 2004 ACM SIGMOD international conference on Management of
 * data, SIGMOD 04, New York, NY, USA, 2004. ACM.
 * 
 * Implemented by D. Lemire
 */
public class MergeOpt implements ThresholdFunction{

	@Override
	public int[] threshold(int min, int[]... set) {
		if(min < 1) throw new IllegalArgumentException("must be >=1");
		if(min > set.length) return new int[0];
		/*
		 *  the longest min-1 arrays are treated separately
		 *  we put the other arrays in heap
		 */
		Arrays.sort(set, new Comparator<int[]>() {
			@Override
			public int compare(int[] a, int[] b) {
				return b.length - a.length;
			}});
		ArrayPointer[] longlists = new ArrayPointer[min-1];
		for(int k = 0; k<min-1;++k) {
			longlists[k] = new ArrayPointer(set[k]);
		}
		ArrayPointerHeap h = new ArrayPointerHeap();
		for(int k = min - 1; k < set.length; ++k) {
			h.insert(new ArrayPointer(set[k]));
		}
		if(h.isEmpty()) return new int[0];
		int[] answer = new int[32];
		int pos = 0;
		int val = h.pop();
		int c = 1;
		while(!h.isEmpty()) {
			final int v = h.pop();
			if(v == val) {
				c++;
			} else {
				if(isBeyondThreshold(val,longlists ,c,min)) {
					if(pos == answer.length)
						answer = Arrays.copyOf(answer, answer.length*3/2);
					answer[pos++] = val;
				}
				val = v;
				c = 1;
			}
		}
		if(isBeyondThreshold(val,longlists ,c,min)) {
			if(pos == answer.length)
				answer = Arrays.copyOf(answer, answer.length*3/2);
			answer[pos++] = val;
		}
		
		return Arrays.copyOf(answer, pos);

	}
	
        private static boolean isBeyondThreshold(int val,
                ArrayPointer[] longlists, int countsofar, int min) {
                for (ArrayPointer ap : longlists) {
                        if (ap.searchForward(val))
                                ++countsofar;

                        if (countsofar >= min)
                                return true;
                }
                return false;
        }
	


	@Override
	public String name() {
		return "MergeOpt";
	}

}
