package me.lemire.tfunction;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Unions {


	/**
	 * Priority-queue approach. This may use a lot of buffer memory.
	 * 
	 * @param set
	 *            the sets to be united
	 * @return the union of the sets
	 */
	public static int[] unite(int[]... set) {
		if (set.length == 0)
			throw new RuntimeException("nothing");
		if (set.length == 1)
			return set[0];
		int[] buffer = new int[0];
		if (set.length == 2) {
			if (buffer.length < set[0].length + set[1].length)
				buffer = new int[set[0].length + set[1].length];
			int buflength = unite2by2(set[0], set[0].length, set[1],
					set[1].length, buffer);
			return Arrays.copyOf(buffer, buflength);
		}

		PriorityQueue<int[]> pq = new PriorityQueue<int[]>(set.length,
				new Comparator<int[]>() {
					@Override
					public int compare(int[] a, int[] b) {
						return a.length - b.length;
					}
				});
		for (int[] x : set)
			pq.add(x);
		while (pq.size() > 1) {
			int[] x1 = pq.poll();
			int[] x2 = pq.poll();
			if (buffer.length < x1.length + x2.length)
				buffer = new int[x1.length + x2.length];
			int buflength = unite2by2(x1, x1.length, x2, x2.length, buffer);
			x1 = null;
			x2 = null;
			pq.add(Arrays.copyOf(buffer, buflength));
		}
		return pq.poll();
	}


	static private int unite2by2(final int[] set1, final int length1,
			final int[] set2, final int length2, final int[] buffer) {
		int pos = 0;
		int k1 = 0, k2 = 0;
		if (0 == length1) {
			for (int k = 0; k < length1; ++k)
				buffer[k] = set1[k];
			return length1;
		}
		if (0 == length2) {
			for (int k = 0; k < length2; ++k)
				buffer[k] = set2[k];
			return length2;
		}
		while (true) {
			if (set1[k1] < set2[k2]) {
				buffer[pos++] = set1[k1];
				++k1;
				if (k1 >= length1) {
					for (; k2 < length2; ++k2)
						buffer[pos++] = set2[k2];
					break;
				}
			} else if (set1[k1] == set2[k2]) {
				buffer[pos++] = set1[k1];
				++k1;
				++k2;
				if (k1 >= length1) {
					for (; k2 < length2; ++k2)
						buffer[pos++] = set2[k2];
					break;
				}
				if (k2 >= length2) {
					for (; k1 < length1; ++k1)
						buffer[pos++] = set1[k1];
					break;
				}
			} else {// if (set1[k1]>set2[k2]) {
				buffer[pos++] = set2[k2];
				++k2;
				if (k2 >= length2) {
					for (; k1 < length1; ++k1)
						buffer[pos++] = set1[k1];
					break;
				}
			}
		}
		return pos;
	}
}
