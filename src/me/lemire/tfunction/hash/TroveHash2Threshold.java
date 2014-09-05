package me.lemire.tfunction.hash;

import java.util.Arrays;
import me.lemire.tfunction.ThresholdFunction;
import gnu.trove.iterator.TIntIntIterator;
import gnu.trove.map.hash.*;

public class TroveHash2Threshold implements ThresholdFunction {

	@Override
	public  int[] threshold(final int min, int[]... set) {
		TIntIntHashMap hm = new TIntIntHashMap();
		for(int k = 0; k<set.length;++k) {
			for(int i : set[k]) {
				hm.adjustOrPutValue(i, 1, 1);
			}
		}
		int[] array = new int[hm.size()];
		int pos = 0;

		TIntIntIterator i = hm.iterator();
		while(i.hasNext()) {
			i.advance();
			if(i.value()>=min)
				array[pos++] = i.key();
		}
		return Arrays.copyOf(array, pos);
	}


	@Override
	public String name() {
		return "TroveHash2";
	}

}
