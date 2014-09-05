package me.lemire.tfunction.sort;

import java.util.Arrays;

import me.lemire.tfunction.*;

public class SortBasedThreshold implements ThresholdFunction {
	
	@Override
	public  int[] threshold(int min, int[]... set) {
		int[] massive = new int[Util.sumlength(set)];
		int pos = 0;
		for(int[] s : set) {
			for(int v : s)
				massive[pos++] = v;
		}
		Arrays.sort(massive);
		int[] buffer = new int[Util.sumlength(set)];
		pos = 0;
		if(massive.length == 0) return new int[0];
		int c = 1;
		int v = massive[0];
		for(int k = 1; k<massive.length;++k) {
			if(massive[k]== v) ++c;
			else {
				if(c>=min) buffer[pos++] = v;
				v = massive[k];
				c = 1;
			}
		}
		if(c>=min) {
			buffer[pos++] = v;
		}
		return Arrays.copyOf(buffer, pos);
	}

	@Override
	public String name() {
		return "sort";
	}
}
