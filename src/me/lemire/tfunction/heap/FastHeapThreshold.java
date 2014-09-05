package me.lemire.tfunction.heap;

import java.util.Arrays;
import me.lemire.tfunction.ThresholdFunction;

public class FastHeapThreshold implements ThresholdFunction {
	

	@Override
	public int[] threshold(int min, int[]... set) {
		ArrayPointerHeap h = new ArrayPointerHeap();
		
		for(int[] s : set)
			h.insert(new ArrayPointer(s));
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
				if(c>=min) {
					if(pos == answer.length)
						answer = Arrays.copyOf(answer, answer.length*3/2);
					answer[pos++] = val;
				}
				val = v;
				c= 1;
			}
		}
		if(c>=min) {
			if(pos == answer.length)
				answer = Arrays.copyOf(answer, answer.length*3/2);
			answer[pos++] = val;
		}
		
		return Arrays.copyOf(answer, pos);
	}

	@Override
	public String name() {
		return "fastheap";
	}

}
