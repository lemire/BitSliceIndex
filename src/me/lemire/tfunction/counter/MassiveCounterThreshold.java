package me.lemire.tfunction.counter;

import java.util.Arrays;

import me.lemire.tfunction.ThresholdFunction;

public class MassiveCounterThreshold implements ThresholdFunction {
	
	@Override
	public  int[] threshold(int min, int[]... set) {
		int maxl = 0;
		for(int k = 0; k<set.length;++k)
			if(set[k].length>0)
				if(set[k][set[k].length-1]>=maxl) maxl = set[k][set[k].length-1]+1;
		int[] counter = new int[maxl];
		for(int k = 0; k<set.length;++k) {
			for(int i : set[k])
				counter[i]++;
		}
		int pos = 0;
		for(int k = 0; k<counter.length;++k) {
			if(counter[k]>=min) counter[pos++] = k;
		}
		return Arrays.copyOf(counter, pos);
	}

	@Override
	public String name() {
		return "ScanCount";
	}
}
