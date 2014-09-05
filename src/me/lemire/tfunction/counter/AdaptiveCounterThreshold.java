package me.lemire.tfunction.counter;

import java.util.Arrays;

import me.lemire.tfunction.ThresholdFunction;

public class AdaptiveCounterThreshold implements ThresholdFunction{
	@Override
	public int[] threshold(int min, int[]... set) {
		if(set.length<=Byte.MAX_VALUE)
			return threshold8(min,set);
		if(set.length<=Short.MAX_VALUE)
			return threshold16(min,set);
		return threshold32(min,set);
	}

	private static int[] threshold8(int min, int[]... set) {
		int maxl = 0;
		for(int k = 0; k<set.length;++k)
			if(set[k].length>0)
				if(set[k][set[k].length-1]>=maxl) maxl = set[k][set[k].length-1]+1;
		byte[] counter = new byte[maxl];
		for(int k = 0; k<set.length;++k) {
			for(int i : set[k])
				counter[i]++;
		}
		int pos = 0;
		int[] answer = new int[counter.length];
		for(int k = 0; k<counter.length;++k) {
			if(counter[k]>=min) answer[pos++] = k;
		}
		return Arrays.copyOf(answer, pos);
	}
	private static int[] threshold16(int min, int[]... set) {
		int maxl = 0;
		for(int k = 0; k<set.length;++k)
			if(set[k].length>0)
				if(set[k][set[k].length-1]>=maxl) maxl = set[k][set[k].length-1]+1;
		short[] counter = new short[maxl];
		for(int k = 0; k<set.length;++k) {
			for(int i : set[k])
				counter[i]++;
		}
		int pos = 0;
		int[] answer = new int[counter.length];
		for(int k = 0; k<counter.length;++k) {
			if(counter[k]>=min) answer[pos++] = k;
		}
		return Arrays.copyOf(answer, pos);
	}
	
	public static int[] threshold32(int min, int[]... set) {
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
		return "AdaptiveScanCount";
	}
}
