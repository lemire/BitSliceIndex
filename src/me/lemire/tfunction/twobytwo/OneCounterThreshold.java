package me.lemire.tfunction.twobytwo;

import java.util.Arrays;
import java.util.Comparator;

import me.lemire.tfunction.Intersections;
import me.lemire.tfunction.ThresholdFunction;
import me.lemire.tfunction.Unions;
import me.lemire.tfunction.Util;

public class OneCounterThreshold implements ThresholdFunction {

	Trimming mode;
	final static boolean reverseorder = false;

	public enum Trimming {
		NONE, AFTER, INTEGRATED, GALLOPINGINTEGRATED
	}
	
	public OneCounterThreshold(Trimming t) {
		mode = t;
	}

	@Override
	public int[] threshold(int min, int[]... set) {
		if (mode.equals(OneCounterThreshold.Trimming.NONE))
			return threshold1s(min, set);
		if (mode.equals(OneCounterThreshold.Trimming.AFTER))
			return threshold2_1s(min, set);
		else if(mode.equals(OneCounterThreshold.Trimming.INTEGRATED))
			return threshold3_1s(min, set);
		else if(mode.equals(OneCounterThreshold.Trimming.GALLOPINGINTEGRATED))
		        return threshold4_1s(min, set);
		else throw new RuntimeException("unsupported");
	}

	/**
	 * Same as threshold but we process the data from left to right after a
	 * sort.
	 * 
	 * @param min
	 * @param set
	 * @return
	 */
	public static int[] threshold1s(int min, int[]... set) {
		if (set.length == 0 || min > set.length)
			return new int[0];
		if (set.length == 1)
			if (min < 2)
				return set[0];
			else
				return new int[0];
		if (min == set.length)
			return Intersections.smartintersect(set);
		if (min == 1)
			return Unions.unite(set);
		Arrays.sort(set, new Comparator<int[]>() {
			@Override
			public int compare(int[] a, int[] b) {
				return reverseorder ? b.length - a.length : a.length - b.length;
			}
		});
                int[] buffer = Arrays.copyOf(set[0], Util.maxlength(set));
                int[] bcounter = new int[Util.maxlength(set)];
                int[] tmpbuffer = new int[Util.maxlength(set)];
                int[] tmpbcounter = new int[Util.maxlength(set)];
                int blength = set[0].length;
                for(int k = 0; k < blength; ++k)
                        bcounter[k] = 1;


		for (int k = 1; k < set.length; ++k) {
			if (tmpbuffer.length < buffer.length + set[k].length) {
				tmpbuffer = new int[buffer.length + set[k].length + 1024];
				tmpbcounter = new int[buffer.length + set[k].length + 1024];
			}
			blength = CounterUnions.unite2by2withcounter(buffer, bcounter, blength, set[k],
					set[k].length, tmpbuffer, tmpbcounter);
			int[] oldb = buffer;
			int[] oldc = bcounter;
			buffer = tmpbuffer;
			bcounter = tmpbcounter;
			tmpbuffer = oldb;
			tmpbcounter = oldc;
		}
		if (tmpbuffer.length < buffer.length)
			tmpbuffer = new int[buffer.length];
		int pos = 0;
		for (int k = 0; k < blength; ++k) {
			if (bcounter[k] >= min)
				tmpbuffer[pos++] = buffer[k];
		}

		return Arrays.copyOf(tmpbuffer, pos);
	}


	/**
	 * Same as threshold1s but we do some extra trimming
	 * 
	 * @param min
	 * @param set
	 * @return
	 */
	public static int[] threshold2_1s(int min, int[]... set) {
		if (set.length == 0 || min > set.length)
			return new int[0];
		if (set.length == 1)
			if (min < 2)
				return set[0];
			else
				return new int[0];
		if (min == set.length)
			return Intersections.smartintersect(set);
		if (min == 1)
			return Unions.unite(set);

		Arrays.sort(set, new Comparator<int[]>() {
			@Override
			public int compare(int[] a, int[] b) {
				return reverseorder ? b.length - a.length : a.length - b.length;
			}
		});
                int[] buffer = Arrays.copyOf(set[0], Util.maxlength(set));
                int[] bcounter = new int[Util.maxlength(set)];
                int[] tmpbuffer = new int[Util.maxlength(set)];
                int[] tmpbcounter = new int[Util.maxlength(set)];
                int blength = set[0].length;
                for(int k = 0; k < blength; ++k)
                        bcounter[k] = 1;


		for (int k = 1; k < set.length; ++k) {
			if (tmpbuffer.length < buffer.length + set[k].length) {
				tmpbuffer = new int[buffer.length + set[k].length + 1024];
				tmpbcounter = new int[buffer.length + set[k].length + 1024];
			}
			blength = CounterUnions.unite2by2withcounter(buffer, bcounter, blength, set[k],
					set[k].length, tmpbuffer, tmpbcounter);
			if (min > (set.length - 1 - k) + 1) {
				final int tmin = min - (set.length - 1 - k);
				int pos = 0;
				for (int j = 0; j < blength; ++j) {
					if (tmpbcounter[j] >= tmin) {
					        tmpbcounter[pos] = tmpbcounter[j];
						tmpbuffer[pos++] = tmpbuffer[j];
					}
				}
				blength = pos;
			}
			int[] oldb = buffer;
			int[] oldc = bcounter;
			buffer = tmpbuffer;
			bcounter = tmpbcounter;
			tmpbuffer = oldb;
			tmpbcounter = oldc;
		}
		// check
	              for (int j = 0; j < blength; ++j) {
	                      if(bcounter[j]<min) throw new RuntimeException("bug");
	              }
		return blength < buffer.length ? Arrays.copyOf(buffer, blength)
				: buffer;
	}

	/**
	 * Same as threshold2_1s but we integrate the trimming
	 * 
	 * @param min
	 * @param set
	 * @return
	 */
	public static int[] threshold3_1s(int min, int[]... set) {
		if (set.length == 0 || min > set.length)
			return new int[0];
		if (set.length == 1)
			if (min < 2)
				return set[0];
			else
				return new int[0];
		if (min == set.length)
			return Intersections.smartintersect(set);
		if (min == 1)
			return Unions.unite(set);
		Arrays.sort(set, new Comparator<int[]>() {
			@Override
			public int compare(int[] a, int[] b) {
				return reverseorder ? b.length - a.length : a.length - b.length;
			}
		});
                int[] buffer = Arrays.copyOf(set[0], Util.maxlength(set));
                int[] bcounter = new int[Util.maxlength(set)];
                int[] tmpbuffer = new int[Util.maxlength(set)];
                int[] tmpbcounter = new int[Util.maxlength(set)];
                int blength = set[0].length;
                for(int k = 0; k < blength; ++k)
                        bcounter[k] = 1;


		for (int k = 1; k < set.length; ++k) {
			if (tmpbuffer.length < buffer.length + set[k].length) {
				tmpbuffer = new int[buffer.length + set[k].length + 1024];
				tmpbcounter = new int[buffer.length + set[k].length + 1024];
			}
			blength = (min > (set.length - 1 - k) + 1) ? CounterUnions.unite2by2withcounterandthreshold(
					buffer, bcounter, blength, set[k], set[k].length,
					tmpbuffer, tmpbcounter, min - (set.length - 1 - k))
					: CounterUnions.unite2by2withcounter(buffer, bcounter, blength, set[k],
							set[k].length, tmpbuffer, tmpbcounter);
			int[] oldb = buffer;
			int[] oldc = bcounter;
			buffer = tmpbuffer;
			bcounter = tmpbcounter;
			tmpbuffer = oldb;
			tmpbcounter = oldc;
		}
		return blength < buffer.length ? Arrays.copyOf(buffer, blength)
				: buffer;
	}
        /**
         * Same as threshold3_1s but we use galloping
         * 
         * @param min
         * @param set
         * @return
         */
        public static int[] threshold4_1s(int min, int[]... set) {
                if (set.length == 0 || min > set.length)
                        return new int[0];
                if (set.length == 1)
                        if (min < 2)
                                return set[0];
                        else
                                return new int[0];
                if (min == set.length)
                        return Intersections.smartintersect(set);
                if (min == 1)
                        return Unions.unite(set);
                int[] buffer = Arrays.copyOf(set[0], Util.maxlength(set));
                int[] bcounter = new int[Util.maxlength(set)];
                int[] tmpbuffer = new int[Util.maxlength(set)];
                int[] tmpbcounter = new int[Util.maxlength(set)];
                int blength = set[0].length;
                Arrays.fill(bcounter, 1);

                Arrays.sort(set, new Comparator<int[]>() {
                        @Override
                        public int compare(int[] a, int[] b) {
                                return reverseorder ? b.length - a.length : a.length - b.length;
                        }
                });
                for (int k = 1; k < set.length; ++k) {
                        if (tmpbuffer.length < buffer.length + set[k].length) {
                                tmpbuffer = new int[buffer.length + set[k].length + 1024];
                                tmpbcounter = new int[buffer.length + set[k].length + 1024];
                        }
                        blength = (min > (set.length - 1 - k) + 1) ? CounterUnions.smartunite2by2withcounterandthreshold(
                                        buffer, bcounter, blength, set[k], set[k].length,
                                        tmpbuffer, tmpbcounter, min - (set.length - 1 - k))
                                        : CounterUnions.unite2by2withcounter(buffer, bcounter, blength, set[k],
                                                        set[k].length, tmpbuffer, tmpbcounter);
                        int[] oldb = buffer;
                        int[] oldc = bcounter;
                        buffer = tmpbuffer;
                        bcounter = tmpbcounter;
                        tmpbuffer = oldb;
                        tmpbcounter = oldc;
                }
                return blength < buffer.length ? Arrays.copyOf(buffer, blength)
                                : buffer;
        }

	@Override
	public String name() {
		return "OneCounter" + mode.toString();
	}

}
