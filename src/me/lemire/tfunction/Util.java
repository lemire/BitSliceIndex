package me.lemire.tfunction;

import java.util.Arrays;

public class Util {
	public static int maxlength(int[]... set) {
		if (set.length == 0)
			return 0;
		int answer = set[0].length;
		for (int k = 1; k < set.length; ++k) {
			if (set[k].length > answer)
				answer = set[k].length;
		}
		return answer;
	}
	
	public static int maxvalue(int[]... set) {
                if (set.length == 0)
                        return 0;
                int answer = set[0][set[0].length-1];
                for (int k = 1; k < set.length; ++k) {
                        if (set[k][set[k].length-1] > answer)
                                answer = set[k][set[k].length-1];
                }
                return answer;
        }
	
	public static double min(double[] d) {
		double dmin = Double.MAX_VALUE;
		for(double x : d)
			if(x<dmin) dmin = x;
		return dmin;
	}
	public static double max(double[] d) {
		double dmax = Double.MIN_VALUE;
		for(double x : d)
			if(x>dmax) dmax = x;
		return dmax;
	}
	public static int sumlength(int[]... set) {
		if (set.length == 0)
			return 0;
		long answer = set[0].length;
		for (int k = 1; k < set.length; ++k) {
			answer += set[k].length;
		}
		int canswer = (int) answer;
		if(canswer != answer) throw new RuntimeException("too much data");
		return canswer;
	}
	public static String toString(int[] x) {
		String answer = "{";
		for (int i : x) {
			answer += i + ",";
		}
		return answer + "}";
	}
	
	//stolen from stackoverflow
	public static double median(double[] l) {
	    Arrays.sort(l);
	    int middle = l.length / 2;
	    if (l.length % 2 == 0)
	    {
	      double left = l[middle - 1];
	      double right = l[middle];
	      return (left + right) / 2;
	    }
	    else
	    {
	      return l[middle];
	    }
	  }
}
