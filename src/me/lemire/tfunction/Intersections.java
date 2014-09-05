package me.lemire.tfunction;

import java.util.Arrays;
import java.util.Comparator;

public class Intersections {
	public static int[] smartintersect(int[]... set) {
		if (set.length == 0)
			throw new RuntimeException("nothing");
		if (set.length == 1)
			return set[0];
		Arrays.sort(set, new Comparator<int[]>() {
			@Override
			public int compare(int[] x, int[] y) {
				return x.length - y.length;
			}
		});
		int[] buffer = new int[set[0].length];
		int[] answer = set[0];
		int answerlength = answer.length;
		if (answerlength * 64 < set[1].length) {
			answerlength = onesidedgallopingintersect2by2(set[0], answerlength,
					set[1], set[1].length, buffer);
		} else {
			answerlength = localintersect2by2(set[0], answerlength, set[1],
					set[1].length, buffer);
		}
		for (int k = 2; k < set.length; ++k) {
			if (answerlength * 64 < set[k].length)
				answerlength = onesidedgallopingintersect2by2(buffer,
						answerlength, set[k], set[k].length, buffer);
			else
				answerlength = localintersect2by2(buffer, answerlength, set[k],
						set[k].length, buffer);
		}
		return Arrays.copyOf(buffer, answerlength);
	}

	public static int localintersect2by2(final int[] set1, final int length1,
			final int[] set2, final int length2, final int[] buffer) {
		if ((0 == length1) || (0 == length2))
			return 0;
		int k1 = 0;
		int k2 = 0;
		int pos = 0;

		mainwhile: while (true) {
			if (set2[k2] < set1[k1]) {
				do {
					++k2;
					if (k2 == length2)
						break mainwhile;
				} while (set2[k2] < set1[k1]);
			}
			if (set1[k1] < set2[k2]) {
				do {
					++k1;
					if (k1 == length1)
						break mainwhile;
				} while (set1[k1] < set2[k2]);
			} else {
				// (set2[k2] == set1[k1])
				buffer[pos++] = set1[k1];
				++k1;
				if (k1 == length1)
					break;
				++k2;
				if (k2 == length2)
					break;

			}

		}
		return pos;
	}

	public static int onesidedgallopingintersect2by2(final int[] smallset,
			final int smalllength, final int[] largeset, final int largelength,
			final int[] buffer) {
		if (0 == smalllength)
			return 0;
		int k1 = 0;
		int k2 = 0;
		int pos = 0;
		mainwhile: while (true) {
			if (largeset[k1] < smallset[k2]) {
				k1 = advanceUntil(largeset, k1, largelength, smallset[k2]);
				if (k1 == largelength)
					break mainwhile;
			}
			if (smallset[k2] < largeset[k1]) {
				++k2;
				if (k2 == smalllength)
					break mainwhile;
			} else {
				// (set2[k2] == set1[k1])
				buffer[pos++] = smallset[k2];
				++k2;
				if (k2 == smalllength)
					break;
				k1 = advanceUntil(largeset, k1, largelength, smallset[k2]);
				if (k1 == largelength)
					break mainwhile;

			}

		}
		return pos;
	}

	/**
	 * Find the smallest integer larger than pos such that array[pos]>= min. If
	 * none can be found, return length. Based on code by O. Kaser.
	 * 
	 * @param array
	 * @param pos
	 * @param min
	 * @return x greater than pos such that array[pos] is at least as large as
	 *         min, pos is is equal to length if it is not possible.
	 */
	public static int advanceUntil(int[] array, int pos, int length, int min) {
		int lower = pos + 1;

		// special handling for a possibly common sequential case
		if (lower >= length || array[lower] >= min) {
			return lower;
		}

		int spansize = 1; // could set larger
		// bootstrap an upper limit

		while (lower + spansize < length && array[lower + spansize] < min)
			spansize *= 2; // hoping for compiler will reduce to shift
		int upper = (lower + spansize < length) ? lower + spansize : length - 1;

		// maybe we are lucky (could be common case when the seek ahead expected
		// to be small and sequential will otherwise make us look bad)
		if (array[upper] == min) {
			return upper;
		}

		if (array[upper] < min) {// means array has no item >= min
			// pos = array.length;
			return length;
		}

		// we know that the next-smallest span was too small
		lower += (spansize / 2);

		// else begin binary search
		// invariant: array[lower]<min && array[upper]>min
		int mid = 0;
		while (lower + 1 != upper) {
			mid = (lower + upper) / 2;
			if (array[mid] == min) {
				return mid;
			} else if (array[mid] < min)
				lower = mid;
			else
				upper = mid;
		}
		return upper;

	}

}
