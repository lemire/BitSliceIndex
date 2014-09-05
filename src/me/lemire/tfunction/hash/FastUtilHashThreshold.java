package me.lemire.tfunction.hash;

import java.util.Arrays;
import me.lemire.tfunction.ThresholdFunction;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

public class FastUtilHashThreshold implements ThresholdFunction {
	@Override
	public  int[] threshold(final int min, int[]... set) {
		Int2IntOpenHashMap hm = new Int2IntOpenHashMap(set.length);
		for(int k = 0; k<set.length;++k) {
			for(int i : set[k]) {
				hm.put(i, hm.get(i)+1);
			}
		}
		int[] array = new int[hm.size()];
		int pos = 0;
		ObjectIterator<it.unimi.dsi.fastutil.ints.Int2IntMap.Entry> i = hm.int2IntEntrySet().fastIterator();
		while(i.hasNext()) {
			it.unimi.dsi.fastutil.ints.Int2IntMap.Entry e = i.next();
			if(e.getIntValue()>=min)
				array[pos++] = e.getIntKey();
		}
		return Arrays.copyOf(array, pos);
	}


	@Override
	public String name() {
		return "FastUtilHash";
	}

}
