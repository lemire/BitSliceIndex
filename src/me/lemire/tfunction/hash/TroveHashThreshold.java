package me.lemire.tfunction.hash;

import me.lemire.tfunction.ThresholdFunction;
import gnu.trove.map.hash.*;
import gnu.trove.procedure.TIntIntProcedure;

public class TroveHashThreshold implements ThresholdFunction {

	@Override
	public  int[] threshold(final int min, int[]... set) {
		TIntIntHashMap hm = new TIntIntHashMap();
		for(int k = 0; k<set.length;++k) {
			for(int i : set[k]) {
				hm.adjustOrPutValue(i, 1, 1);
			}
		}
		hm.retainEntries(new TIntIntProcedure() {
			public boolean execute(int a, int b ){
				return (b>=min);
			}
			
		});
		return hm.keys();
	}
	
	@Override
	public String name() {
		return "TroveHash";
	}

}
