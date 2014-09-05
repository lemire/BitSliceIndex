package me.lemire.tfunction;

import java.text.DecimalFormat;
import java.util.Arrays;

import me.lemire.tfunction.bitset.BitSetThreshold;
import me.lemire.tfunction.counter.AdaptiveCounterThreshold;
import me.lemire.tfunction.counter.MassiveCounterThreshold;
import me.lemire.tfunction.counter.SaturatedAdaptiveCounterThreshold;
import me.lemire.tfunction.heap.FastHeapThreshold;
import me.lemire.tfunction.li_lu_lu.BitmapDivideSkip;
import me.lemire.tfunction.li_lu_lu.DivideSkip;
import me.lemire.tfunction.li_lu_lu.MergeSkip;
import me.lemire.tfunction.saragawi_kirpal.BitmapMergeOpt;
import me.lemire.tfunction.saragawi_kirpal.MergeOpt;
import me.lemire.tfunction.sort.SortBasedThreshold;
import me.lemire.tfunction.synth.ClusteredDataGenerator;
import me.lemire.tfunction.twobytwo.OneCounterThreshold;
import me.lemire.tfunction.twobytwo.TwoCountersThreshold;

public class SimpleBenchmark {

        public static ThresholdFunction[] functions = {
				new FastHeapThreshold(),
				new MergeOpt(),
				new BitmapMergeOpt(),
                                new MergeSkip(),
                                new DivideSkip(1),
                                new BitmapDivideSkip(1),
                                //new DivideSkip(2),
                                new DivideSkip(4),
                                new BitmapDivideSkip(4),
                                //new DivideSkip(8),
                                new DivideSkip(16),
                                new BitmapDivideSkip(16),
                                
                                //new DivideSkip(32),
                                new DivideSkip(64),
                                new BitmapDivideSkip(64),
                                //new DivideSkip(128),
                                new DivideSkip(256),
                                new BitmapDivideSkip(256),
                                new DivideSkip(0.0367),  // mu for pgdvd
                                new BitmapDivideSkip(0.0367),  // mu for pgdvd
                                new DivideSkip(0.0286),  // mu for imdb (only about right for T=26)
                                new BitmapDivideSkip(0.0286),  // mu for imdb (only about right for T=26)

                                //new DivideSkip(512),
                                // new DivideSkip(1024),
                                //new DivideSkip(2048),
                                //new DivideSkip(4096),
                                //new DivideSkip(8192),
                                //new DivideSkip(16384),
                                //new DivideSkip(32768),
                                //new DivideSkip(65536),
				new BitSetThreshold(),
                                new SaturatedAdaptiveCounterThreshold(),
				new AdaptiveCounterThreshold(),
				new MassiveCounterThreshold(),
				new SortBasedThreshold(),
				new TwoCountersThreshold(TwoCountersThreshold.Trimming.NONE),
				new TwoCountersThreshold(TwoCountersThreshold.Trimming.AFTER),
				new TwoCountersThreshold(TwoCountersThreshold.Trimming.INTEGRATED),
				new OneCounterThreshold(OneCounterThreshold.Trimming.NONE),
				new OneCounterThreshold(OneCounterThreshold.Trimming.AFTER),
				new OneCounterThreshold(OneCounterThreshold.Trimming.INTEGRATED),
	                        new OneCounterThreshold(OneCounterThreshold.Trimming.GALLOPINGINTEGRATED)
				}; 
	public static void main(String[] args) {
	        System.out.print("Warming up");
	        System.out.flush();
	        for(int z = 0; z<10;++z)  {
	                System.out.print(".");
	                System.out.flush();
	                test(5, 10, 12, 14, functions, false);
	        }
	        System.out.println();	        
	        final long memory = Runtime.getRuntime().maxMemory();
	        if(memory / (1024 * 1024 * 1024) > 2) {
	                System.out.println("You have a lot of memory.");
	                test(5, 14, 18, 28, functions, true);
	        } else { 
	                System.out.println("Your memory is limited.");
	                test(5, 10, 12, 14, functions, true);
	        }
	}

	public static String toFixed(String x, int length) {
		if (x.length() > length)
			return x.substring(0, length);
		String ans = x;
		while (ans.length() < length)
			// very bad code, don't do this
			ans = ans + " ";
		return ans;
	}

	public static void test(int N, int arrsize, int bigarrsize, int maxbit,
			ThresholdFunction[] functions, boolean verbose) {
		if (verbose)
			System.out.println("# N = " + N);
		DecimalFormat df = new DecimalFormat("0.#");
		ClusteredDataGenerator cdg = new ClusteredDataGenerator();// 34);
		int[][] data = new int[2 * N][];
		double ints = 0;

		int TIMES = 10;
		int[] common = cdg.generateClustered(10,// 128,
				1 << maxbit);
		for (int k = 0; k < N; ++k) {
			data[k] = Unions.unite(
					cdg.generateClustered(1 << arrsize, 1 << maxbit), common);
			ints += data[k].length;

		}
		for (int k = 0; k < N; ++k) {
			data[k + N] = Unions
					.unite(cdg.generateClustered(1 << bigarrsize, 1 << maxbit),
							common);
			ints += data[k + N].length;

		}
		for(ThresholdFunction tf: functions)
		        if(tf instanceof IndexableThresholdFunction)
		                ((IndexableThresholdFunction)tf).prepare(data);

		if (verbose)
			System.out.println("#" + toFixed("nameofmethod", 39)
					+ "\tK\tN\tmin speed(mis)\tmax speed\tmedian speed");
		long bef, aft;
		ThresholdFunction reference = new TwoCountersThreshold(
				TwoCountersThreshold.Trimming.NONE);
		for (int K = 2; K <= 2*N - 1; K += (2*N + 4) / 5) {
			int[] actualanswer = reference.threshold(K, data);
			for (ThresholdFunction tf : functions) {
				double[] times = new double[TIMES];
				for (int T = 0; T < TIMES; ++T) {
					bef = System.nanoTime();
					int[] answer = tf.threshold(K, data);
					aft = System.nanoTime();
					if (!Arrays.equals(answer, actualanswer)) {
						System.out.println(answer.length+" "+actualanswer.length);
						if(Math.max(answer.length, actualanswer.length)<=25) {
						        for (int i=0; i < Math.max(answer.length, actualanswer.length); ++i) {
						                if(answer.length>i)
						                        System.out.print(answer[i]+"\t");
						                else 
						                        System.out.print("NA\t");
                                                                if(actualanswer.length>i)
                                                                        System.out.print(actualanswer[i]);
                                                                else 
                                                                        System.out.print("NA");
                                                                System.out.println();
						                        
						        }        
						}
                                                
                                                // does not handle the case where one is a prefix of another
                                                for (int i=0; i < Math.min(answer.length, actualanswer.length); ++i)
                                                    if (answer[i] != actualanswer[i]) {
                                                        System.out.println("for position i="+i+" answer has "+
                                                                           answer[i]+" but actual answer has "+
                                                                           actualanswer[i]);
                                                        break;
                                                    }

						throw new RuntimeException("bug with " + tf.name());
					}
					times[T]=ints / ((aft - bef) * 0.001);
				}
				if (verbose) 
					System.out.println(toFixed(tf.name(), 40) + "\t" + K
							+ "\t" + 2*N + "\t" 
							+ df.format(Util.min(times))+"\t"+df.format(Util.max(times))+"\t"+df.format(Util.median(times)));

			}
			if(verbose) System.out.println();
		}

	}
}
