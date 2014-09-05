package me.lemire.tfunction.bitset;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;

import me.lemire.tfunction.IndexableThresholdFunction;
import me.lemire.tfunction.ThresholdFunction;
import me.lemire.tfunction.Util;

public class BitSetThreshold implements IndexableThresholdFunction {
	
        HashMap<int[][],BitSet[]> index = new HashMap<int[][],BitSet[]>();
        
	public static BitSet toBitSet(int[] d) {
		BitSet b = new BitSet();
		for(int i : d)
			b.set(i);
		return b;
	}
	
	public static int[] toArray(BitSet bs) {
		int[] answer = new int[bs.cardinality()];
		int pos = 0;
		for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i+1)) {
		    answer[pos++] = i;
		 }
		return answer;
	}

        public static int[] toArray(BitSet bs,int estimatedlength) {
                int[] answer = new int[estimatedlength];
                int pos = 0;
                for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i+1)) {
                    answer[pos++] = i;
                 }
                return Arrays.copyOf(answer, pos);
        }
        
	public static String toString(BitSet x) {
		String answer = "{";
		for (int i = x.nextSetBit(0); i >= 0; i = x.nextSetBit(i+1)) {
		     answer += i+",";
		 }
		return answer + "}";
	}
	public static BitSet threshold(int min, BitSet ... set) {
		if(min>set.length || set.length == 0) return new BitSet();
		BitSet[] buffers = new BitSet[min];
		buffers[0] = (BitSet) set[0].clone();
		for(int k = 1; k < min ; ++k)
			buffers[k] = new BitSet();
		for(int k = 1 ; k < set.length; ++k) {
			for(int j = Math.min(buffers.length-1,k); j >=1; --j) {
				BitSet copy = (BitSet) set[k].clone();
				copy.and(buffers[j-1]);
				buffers[j].or(copy);
			}
			buffers[0].or(set[k]);// buffers[0] could be a wide aggregation
		}

		return buffers[buffers.length-1];
	}

	@Override
	public int[] threshold(int min, int[]... set) {
		BitSet[] bset;
		if(index.containsKey(set))
	                bset = index.get(set);
		else {
		        bset = new BitSet[set.length];
		        for(int k = 0; k< bset.length;++k)
		                bset[k] = toBitSet(set[k]);
		        // we could, potentially, save it for later?
		        index.put(set,bset);
		}
		return toArray(threshold(min,bset));
	}

	@Override
	public String name() {
		return "BitSet";
	}

        @Override
        public void prepare(int[]... set) {
                if(index.containsKey(set)) return;
                BitSet[] bset = new BitSet[set.length];
                for(int k = 0; k< bset.length;++k)
                        bset[k] = toBitSet(set[k]);
                index.put(set, bset);
        }
}
