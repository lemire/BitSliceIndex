package me.lemire.tfunction;

public interface ThresholdFunction {
	public int[] threshold(int min, int[]... set);
	
	public String name();

}
