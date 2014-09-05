package me.lemire.tfunction.heap;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

import me.lemire.tfunction.ThresholdFunction;

public class HeapThreshold implements ThresholdFunction {
	@Override
	public int[] threshold(int min, int[]... set) {
		int maxl = 0;
		for (int[] s : set)
			if (s.length > maxl)
				maxl = s.length;
		int[] answer = new int[set.length]; // heuristic
		int cardinality = 0;
		PriorityQueue<ArrayPointer> pq = new PriorityQueue<ArrayPointer>(
				set.length, new Comparator<ArrayPointer>() {
					@Override
					public int compare(ArrayPointer o1, ArrayPointer o2) {
						return o1.value() - o2.value();
					}
				});
		ArrayPointer[] pos = new ArrayPointer[set.length];
		for (int k = 0; k < set.length; ++k) {
			pos[k] = new ArrayPointer();
			pos[k].array = set[k];
			if (pos[k].pos < pos[k].array.length)
				pq.add(pos[k]);
		}
		while (!pq.isEmpty()) {
			ArrayPointer p = pq.poll();
			final int candidate = p.value();
			if (p.advance())
				pq.add(p);
			int weight = 1;
			while (weight < min && !pq.isEmpty()
					&& pq.peek().value() == candidate) {
				p = pq.poll();
				if (p.advance())
					pq.add(p);
				++weight;
			}
			if (weight == min) {
				if (cardinality == answer.length)
					answer = Arrays.copyOf(answer, answer.length * 3 / 2);
				answer[cardinality++] = candidate;
				while (!pq.isEmpty() && pq.peek().value() == candidate) {
					p = pq.poll();
					if (p.advance())
						pq.add(p);
				}
			}
		}
		return Arrays.copyOf(answer, cardinality);
	}

	@Override
	public String name() {
		return "heap";
	}

}
