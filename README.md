==== BitSliceIndex ====

This software is meant to serve as a basis to experiment with Bit-Slice indexing.

For our purposes, we use compressed bitmaps (using the Roaring format).


References:

Denis Rinfret, Patrick O'Neil, and Elizabeth O'Neil. 2001. Bit-sliced index arithmetic. SIGMOD Rec. 30, 2 (May 2001), 47-57. 
http://dl.acm.org/citation.cfm?id=375669

Owen Kaser and Daniel Lemire, Compressed bitmap indexes: beyond unions and intersections, Software: Practice and Experience, 2014 (to appear)
http://arxiv.org/abs/1402.4466

Samy Chambi, Daniel Lemire, Owen Kaser, Robert Godin, Better bitmap performance with Roaring bitmaps, in preparation
http://arxiv.org/abs/1402.6407


== usage ==


* install java
* install maven 2

type :
* maven package
* cd target
* java -cp RoaringCircuits-0.0.1-SNAPSHOT.jar org.roaringbitmap.circuits.threshold.Benchmark
* java -cp RoaringCircuits-0.0.1-SNAPSHOT.jar org.roaringbitmap.circuits.comparator.Benchmark
* java -cp RoaringCircuits-0.0.1-SNAPSHOT.jar org.roaringbitmap.circuits.topk.Benchmark

