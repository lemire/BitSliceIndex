package me.lemire.tfunction.heap;

public class ArrayPointer{
	public int[] array;
	int pos = 0;
    //        long  processMeMark = 0;  // long so it can never reasonably wrap around
	
	public ArrayPointer(int[] s) {
		array = s;
	}
	
	public boolean hasValue() {
		return pos<array.length;
	}

	public ArrayPointer() {
		array = null;
	}
	public int value() {
		return array[pos];
	}

	/**
	 * Advance to a location where array[pos] >=min. Return true if value found.
	 * @param min
	 * @return whether the value was found
	 */
	public boolean searchForward(int min) {
		pos = gallopingSearch(array, pos, array.length, min);
		if(pos == array.length) return false;
		return array[pos] == min;
	}
	/**
         * Advance to a location where array[pos] >=min. 
         */
        public void moveForward(int min) {
                pos = gallopingSearch(array, pos, array.length, min);
        }
	/**
	 * Find the smallest integer larger than pos such that array[pos]>= min. If
	 * none can be found, return length. Based on code by O. Kaser.
	 * 
	 * @param array
	 * @param pos
	 * @param min
	 * @return x greater than pos such that array[pos] is at least as large as min, pos is is equal to length if it is not possible.
	 */
	public static int gallopingSearch(int[] array, int pos, int length, int min) {
		int lower = pos ;

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


	public boolean advance() {
		pos++;
		return pos < array.length;
	}
}