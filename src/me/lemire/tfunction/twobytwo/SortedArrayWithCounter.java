package me.lemire.tfunction.twobytwo;

import java.util.Arrays;
import java.util.Stack;

import me.lemire.tfunction.Util;

public class SortedArrayWithCounter {
        public int[] array;
	public int[] counter;
	public int length;

	public String toString() {
		String answer = "{";
		for (int i = 0; i < length; ++i) {
			answer += array[i] + " (" + counter[i] + "),";
		}
		answer += "}";
		return answer;
	}

	public void trim(int threshold) {
		if (threshold <= 1)
			throw new RuntimeException("unadvisable");
		int pos = 0;
		for (int k = 0; k < length; ++k) {
			if (counter[k] >= threshold) {
				counter[pos] = counter[k];
				array[pos++] = array[k];
			}
		}
		length = pos;
	}
	
	public int[] applyThreshold(int threshold) {
	        int[] answer = new int[length];
	        int pos = 0;
                for (int k = 0; k < length; ++k) {
                        if (counter[k] >= threshold) {
                                answer[pos++] = array[k];
                        }
                }
                return Arrays.copyOf(answer, pos);
	}
        public static SortedArrayWithCounter merge(int[]...set) {
                if(set.length == 0) return new SortedArrayWithCounter();
                int[] defaultcounter = new int[Util.maxlength(set)];
                int[] buffer = new int[Util.maxlength(set)];
                int[] bcounter = new int[Util.maxlength(set)];

                Arrays.fill(defaultcounter, 1);
                Stack<SortedArrayWithCounter> mydata = new Stack<SortedArrayWithCounter>(); 
                for (int[] x : set) {
                         SortedArrayWithCounter c = new SortedArrayWithCounter();
                         c.array = x;
                         c.counter = defaultcounter;
                         c.length = c.array.length;
                         mydata.add(c);
                 }
                 SortedArrayWithCounter x1 = mydata.pop();
                 while(!mydata.empty()) {
                         SortedArrayWithCounter x2 = mydata.pop();
                         if (buffer.length < x1.length + x2.length) {
                                 buffer = new int[x1.length + x2.length];
                                 bcounter = new int[x1.length + x2.length];
                         }
                         int buflength = CounterUnions.unite2by2withcounter(x1.array, x1.counter,
                                         x1.length, x2.array, x2.counter, x2.length, buffer,
                                         bcounter);
                         x1 = new SortedArrayWithCounter();
                         x1.array = Arrays.copyOf(buffer, buflength);
                         x1.counter = Arrays.copyOf(bcounter, buflength);
                         x1.length = buflength;
                 }
                return x1;
        }

	
	public boolean equals(Object o) {
	        if(o instanceof SortedArrayWithCounter) {
	                SortedArrayWithCounter sa = (SortedArrayWithCounter) o;
                        if(length != sa.length) return false;
	                for (int k = 0; k < length ; ++k) {
	                        if(counter[k]!=sa.counter[k]) return false;
                                if(array[k]!=sa.array[k]) return false;
	                }
	                return true;
	        }
	        return false;
	}

}
