package me.lemire.tfunction;

public interface IndexableThresholdFunction extends ThresholdFunction {
	public int[] threshold(int min, int[]... set);
        public void prepare(int[]... set);// this gives a chance to index the result ahead of time 	
	public String name();

}
