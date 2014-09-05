package me.lemire.tfunction.twobytwo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Stack;

import me.lemire.tfunction.Intersections;
import me.lemire.tfunction.ThresholdFunction;
import me.lemire.tfunction.Unions;
import me.lemire.tfunction.Util;

public class TwoCountersThreshold implements ThresholdFunction {

	Trimming mode;

	public enum Trimming {
		NONE, AFTER, INTEGRATED
	}
	public TwoCountersThreshold(Trimming t) {
		mode = t;
	}

	@Override
	public int[] threshold(int min, int[]... set) {
		if (mode.equals(TwoCountersThreshold.Trimming.NONE))
			return threshold1(min, set);
		if (mode.equals(TwoCountersThreshold.Trimming.AFTER))
			return threshold2(min, set);
		else if(mode.equals(TwoCountersThreshold.Trimming.INTEGRATED))
			return threshold3(min, set);
		else throw new RuntimeException("unsupported");
	}
        
	public static int[] threshold1(int min, int[]... set) {
		if (set.length == 0 || min > set.length)
			return new int[0];
		if (set.length == 1)
			if (min < 2)
				return set[0];
			else
				return new int[0];
		if (min == set.length)
			return Intersections.smartintersect(set);
		if (min == 1)
			return Unions.unite(set);
		SortedArrayWithCounter sol = SortedArrayWithCounter.merge(set);
		int pos = 0;
		int[] buffer = new int[sol.length];
		for (int k = 0; k < sol.length; ++k) {
			if (sol.counter[k] >= min)
				buffer[pos++] = sol.array[k];
		}
		return Arrays.copyOf(buffer, pos);
	}

	/**
	 * Same as threshold1 but includes some trimming.
	 * 
	 * @param min
	 * @param set
	 * @return
	 */
	public static int[] threshold2(int min, int[]... set) {
		if (set.length == 0 || min > set.length)
			return new int[0];
		if (set.length == 1)
			if (min < 2)
				return set[0];
			else
				return new int[0];
		if (min == set.length)
			return Intersections.smartintersect(set);
		if (min == 1)
			return Unions.unite(set);
		int[] buffer = new int[Util.maxlength(set)];
		int[] bcounter = new int[Util.maxlength(set)];

		int[] defaultcounter = new int[Util.maxlength(set)];
		Arrays.fill(defaultcounter, 1);

		ArrayList<SortedArrayWithCounter> pq = new ArrayList<SortedArrayWithCounter>(
				set.length);
		for (int[] x : set) {
			SortedArrayWithCounter c = new SortedArrayWithCounter();
			c.array = x;
			c.counter = null;//defaultcounter;
			c.length = c.array.length;
			pq.add(c);
		}
		Collections.sort(pq, new Comparator<SortedArrayWithCounter>() {
			@Override
			public int compare(SortedArrayWithCounter a,
					SortedArrayWithCounter b) {
				return b.length - a.length;
			}
		});
		if(pq.size() > 1) {
			SortedArrayWithCounter x1 = pq.remove(pq.size() - 1);
			SortedArrayWithCounter x2 = pq.remove(pq.size() - 1);

                        // owen added this because he had a runtime error when processing imdb 3grams
                        // it seems that even the first twos union could sometimes be larger than the  biggest
			if (buffer.length < x1.length + x2.length) {
				buffer = new int[x1.length + x2.length];
				bcounter = new int[x1.length + x2.length];
			}
                        //end owen

			int buflength = CounterUnions.unite2by2withcounter(x1.array, 
					x1.length, x2.array, x2.length, buffer,
					bcounter);
			x1 = null;
			x2 = null;
			SortedArrayWithCounter c = new SortedArrayWithCounter();
			c.array = Arrays.copyOf(buffer, buflength);
			c.counter = Arrays.copyOf(bcounter, buflength);
			c.length = buflength;
			if (min > pq.size() + 1)
				c.trim(min - pq.size());
			pq.add(c);
		}
		while (pq.size() > 1) {
			SortedArrayWithCounter x1 = pq.remove(pq.size() - 1);
			SortedArrayWithCounter x2 = pq.remove(pq.size() - 1);
			if (buffer.length < x1.length + x2.length) {
				buffer = new int[x1.length + x2.length];
				bcounter = new int[x1.length + x2.length];
			}
			
			int buflength = CounterUnions.unite2by2withcounter(x1.array, x1.counter,
					x1.length, x2.array, x2.length, buffer,
					bcounter);
			x1 = null;
			x2 = null;
			SortedArrayWithCounter c = new SortedArrayWithCounter();
			c.array = Arrays.copyOf(buffer, buflength);
			c.counter = Arrays.copyOf(bcounter, buflength);
			c.length = buflength;
			if (min > pq.size() + 1)
				c.trim(min - pq.size());
			pq.add(c);
		}
		SortedArrayWithCounter sol = pq.get(0);
		return sol.length < sol.array.length ? Arrays.copyOf(sol.array,
				sol.length) : sol.array;
	}

	/**
	 * Same as threshold2, but with "integrated" trimming.
	 * 
	 * @param min
	 * @param set
	 * @return
	 */
	public static int[] threshold3(int min, int[]... set) {
		if (set.length == 0 || min > set.length)
			return new int[0];
		if (set.length == 1)
			if (min < 2)
				return set[0];
			else
				return new int[0];
		if (min == set.length)
			return Intersections.smartintersect(set);
		if (min == 1)
			return Unions.unite(set);
		int[] buffer = new int[Util.maxlength(set)];
		int[] bcounter = new int[Util.maxlength(set)];

		int[] defaultcounter = new int[Util.maxlength(set)];
		Arrays.fill(defaultcounter, 1);

		ArrayList<SortedArrayWithCounter> pq = new ArrayList<SortedArrayWithCounter>(
				set.length);
		for (int[] x : set) {
			SortedArrayWithCounter c = new SortedArrayWithCounter();
			c.array = x;
			c.counter = null;//defaultcounter;
			c.length = c.array.length;
			pq.add(c);
		}
		Collections.sort(pq, new Comparator<SortedArrayWithCounter>() {
			@Override
			public int compare(SortedArrayWithCounter a,
					SortedArrayWithCounter b) {
				return b.length - a.length;
			}
		});
		if(pq.size() > 1) {
			SortedArrayWithCounter x1 = pq.remove(pq.size() - 1);
			SortedArrayWithCounter x2 = pq.remove(pq.size() - 1);

                        // owen added this preemptively
			if (buffer.length < x1.length + x2.length) {
				buffer = new int[x1.length + x2.length];
				bcounter = new int[x1.length + x2.length];
			}
                        //end owen

			int buflength =  (min > pq.size() + 1) ? CounterUnions.unite2by2withcounterandthreshold(x1.array, 
					x1.length, x2.array, x2.length, buffer,
					bcounter,min) : CounterUnions.unite2by2withcounter(x1.array, 
					x1.length, x2.array, x2.length, buffer,
					bcounter);
			x1 = null;
			x2 = null;
			SortedArrayWithCounter c = new SortedArrayWithCounter();
			c.array = Arrays.copyOf(buffer, buflength);
			c.counter = Arrays.copyOf(bcounter, buflength);
			c.length = buflength;
			pq.add(c);
		}
		while (pq.size() > 1) {
			SortedArrayWithCounter x1 = pq.remove(pq.size() - 1);
			SortedArrayWithCounter x2 = pq.remove(pq.size() - 1);
			if (buffer.length < x1.length + x2.length) {
				buffer = new int[x1.length + x2.length];
				bcounter = new int[x1.length + x2.length];
			}
			int buflength =  (min > pq.size() + 1) ? CounterUnions.unite2by2withcounterandthreshold(x1.array,x1.counter, 
					x1.length, x2.array, x2.length, buffer,
					bcounter,min-pq.size()) : CounterUnions.unite2by2withcounter(x1.array, x1.counter,
					x1.length, x2.array, x2.length, buffer,
					bcounter);
			x1 = null;
			x2 = null;
			SortedArrayWithCounter c = new SortedArrayWithCounter();
			c.array = Arrays.copyOf(buffer, buflength);
			c.counter = Arrays.copyOf(bcounter, buflength);
			c.length = buflength;
			if (min > pq.size() + 1)
				c.trim(min - pq.size());
			pq.add(c);
		}
		SortedArrayWithCounter sol = pq.get(0);
		return sol.length < sol.array.length ? Arrays.copyOf(sol.array,
				sol.length) : sol.array;
	}

	@Override
	public String name() {
		return "TwoCounters" + mode.toString();
	}

}
