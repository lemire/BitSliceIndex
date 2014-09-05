package me.lemire.tfunction.heap;

import java.util.Arrays;


/**
 * Probably not useful.
 *
 */
public class MinIntHeap {

	int[] minHeap = new int[32];
	int size = 0;

	public void insert(int i) {
		if (minHeap.length == size)
			minHeap = Arrays.copyOf(minHeap, 2 * size);
		size++;
		int index = size - 1;
		while (index > 0) {
			final int parentkey = (index - 1) >>> 1;
			if (minHeap[parentkey] <= i)
				break;
			minHeap[index] = minHeap[parentkey];
			index = parentkey;
		}
		minHeap[index] = i;
	}

	public MinIntHeap() {
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public int peek() {
		return minHeap[0];
	}

	public int pop() {
		final int answer = minHeap[0];
		--size;
		if (size == 0)
			return answer;
		final int lostvalue = minHeap[size];
		int index = 0;
		while (index < (size >>> 1)) {
			int smallestdescendant = (index << 1) + 1;// left
			int right = smallestdescendant + 1;
			if (right < size && minHeap[right] < minHeap[smallestdescendant]) {
				smallestdescendant = right;
			}
			if (lostvalue <= minHeap[smallestdescendant])
				break;
			minHeap[index] = minHeap[smallestdescendant];
			index = smallestdescendant;
		}
		minHeap[index] = lostvalue;
		return answer;
	}

}