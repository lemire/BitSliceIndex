package me.lemire.tfunction.li_lu_lu;

import java.util.Arrays;
import java.util.Comparator;

import me.lemire.tfunction.ThresholdFunction;
import me.lemire.tfunction.heap.ArrayPointer;
import me.lemire.tfunction.heap.ArrayPointerHeap;

/**
 * From :
 * 
 * Chen Li, Jiaheng Lu, Yiming Lu
 * Efficient Merging and Filtering Algorithms for Approximate String Search
 * In ICDE'08
 * 
 * cited 130 times as of July 2013
 * Implemented by Owen Kaser from a skeleton by D. Lemire.
 * 
 * Similar to MergeSkip and MergeOpt.  Code was copied from each.
 *
 */
public class DivideSkip implements ThresholdFunction{
    public static final boolean verbose = false; // stuff Owen might want to know
    static final int IMPOSSIBLE=-1;
    int USER_L=IMPOSSIBLE;
    int L=IMPOSSIBLE;
    double mu = IMPOSSIBLE;
    public DivideSkip( int L) {
        this.USER_L = L;
    }
    public DivideSkip (double mu) {
        this.mu = mu;
    }
    private DivideSkip(){}  // want either L or mu, please

	@Override
	public int[] threshold(final int min, final int[]... set) {
		if(min < 1) throw new IllegalArgumentException("must be >=1");
		if(min > set.length) return new int[0];
                if ( (USER_L != IMPOSSIBLE) &&       
                     (USER_L < 0 || USER_L >= set.length || USER_L >= min)) {
                    // Daniel: does not seem necessary to output an error message
                    return (new MergeSkip()).threshold(min,set);
                }
                

		/*
		 *  the longest L arrays are treated separately
		 *  we put the other arrays in heap
		 */
		Arrays.sort(set, new Comparator<int[]>() {
			@Override
			public int compare(int[] a, int[] b) {
				return b.length - a.length;
			}});
                if (USER_L == IMPOSSIBLE) {
                    L = (int) (min / (mu*Math.log(set[0].length)+1));
		    // if all bitmaps are size 1, L could be set to T, a no-no
		    if (L == min) L = min-1;
		    assert L < min;  // bug otherwise.
                    if (verbose) System.out.println("computing L as "+L);
                }
                else
                    L = USER_L;

		ArrayPointer[] longlists = new ArrayPointer[L];
		for(int k = 0; k<L; ++k) {
			longlists[k] = new ArrayPointer(set[k]);
		}

                final ArrayPointer [] hasBeenPopped = new ArrayPointer[ set.length];
                int hasBeenPoppedListCtr = 0;

		final ArrayPointerHeap h = new ArrayPointerHeap();
		for(int k= L; k<set.length; ++k) {
			h.insert(new ArrayPointer(set[k]));
		}
		int[] answer = new int[32];
		int pos = 0;
                while (h.size() >= min-L) { // still could hit min
                    hasBeenPoppedListCtr = 0;
                    final ArrayPointer t = h.popWithoutAdvancing();
                    hasBeenPopped[hasBeenPoppedListCtr++] = t;
                    int n = 1;
                    while (!h.isEmpty() && h.peek() == t.value()) {
                        ++n;
                        hasBeenPopped[hasBeenPoppedListCtr++] = h.popWithoutAdvancing();
                    }
                    if(n>=min-L) {
                        // maybe it can fit...
                        if (isBeyondThreshold(t.value(), longlists,n,min)) {
                            if(pos == answer.length)
                                answer = Arrays.copyOf(answer, answer.length*3/2);
                            answer[pos++] = t.value();
                        }
                        for (int i=0; i < hasBeenPoppedListCtr; ++i) {
                            ArrayPointer ap = hasBeenPopped[i];
                            if (ap.advance()) 
                                h.insert(ap);
                            
                        }
                    }
                    else {
                        // do the extra popping
                        for (int i=0; i < (min-L) - 1 - n; ++i)
                                    hasBeenPopped[hasBeenPoppedListCtr++] = h.popWithoutAdvancing();
                        int t1 = h.peek();
                        for (int i=0; i < hasBeenPoppedListCtr; ++i) {
                            ArrayPointer ap = hasBeenPopped[i];
                            ap.searchForward(t1);
                            if (ap.hasValue())
                                h.insert(ap);
                        }
                    }
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
            if (USER_L != IMPOSSIBLE)
		return "DivideSkip("+USER_L+")";
            else return "DivideSkip(u="+mu+")";
	}

}
