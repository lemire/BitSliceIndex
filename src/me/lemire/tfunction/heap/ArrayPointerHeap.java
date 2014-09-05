package me.lemire.tfunction.heap;

import java.util.Arrays;

public class ArrayPointerHeap {
	ArrayPointer[] minHeap = new ArrayPointer[32];
	int size = 0;

	public void insert(ArrayPointer p) {
		if(!p.hasValue()) return;
		if (minHeap.length == size)
			minHeap = Arrays.copyOf(minHeap, 2 * size);
		size++;
		int index = size - 1;
		while (index > 0) {
			final int parentkey = (index - 1) >>> 1;
			if (minHeap[parentkey].value() <= p.value())
				break;
			minHeap[index] = minHeap[parentkey];
			index = parentkey;
		}
		minHeap[index] = p;
	}

	public ArrayPointerHeap() {
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public int peek() {
		return minHeap[0].value();
	}

	public int pop() {
		final int answer = minHeap[0].value();
		ArrayPointer lostvalue = minHeap[0];
		if(!minHeap[0].advance()) {
			--size;
			if (size == 0)
				return answer;
			lostvalue = minHeap[size];
		}
                percolateDown(lostvalue);
		return answer;
        }
    
    private void percolateDown( ArrayPointer lostvalue) {
        int index = 0;
        while (index < (size >>> 1)) {
            int smallestdescendant = (index << 1) + 1;// left
            final int right = smallestdescendant + 1;
            if (right < size && minHeap[right].value() < minHeap[smallestdescendant].value()) {
                smallestdescendant = right;
            }
            if (lostvalue.value() <= minHeap[smallestdescendant].value())
                break;
            minHeap[index] = minHeap[smallestdescendant];
            index = smallestdescendant;
        }
        minHeap[index] = lostvalue;
    }

    /**
     * Special method used by the MergeSkip algorithm. It pops without
     * advancing the pointer (effectively removing the ArrayPointer).
     * 
     * @return popped value
     */
    public ArrayPointer popWithoutAdvancing() {
        ArrayPointer answer = minHeap[0];
        --size;
        if (size == 0) return answer;
        percolateDown(minHeap[size]);
        return answer;
    }

    /**
     * Special method used by the MergeSkip algorithm. It pops without
     * advancing the pointer (effectively removing the ArrayPointer), 
     * but also records in the log popped at logPosition in the provided
     * array (so that it can be added back).
     * 
     * @param log array used to record which ArrayPointer are popped.
     * @param logPosition where to record
     * @return popped value
     */
    /*public int popWithoutAdvancingAndRecord( ArrayPointer [] log, int logPosition) {
        final int answer = minHeap[0].value();
        log[logPosition] = minHeap[0];
        --size;
        if (size == 0) return answer;
        percolateDown(minHeap[size]);
        return answer;
    }*/

}
