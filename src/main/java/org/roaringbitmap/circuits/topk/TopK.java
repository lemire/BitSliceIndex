package org.roaringbitmap.circuits.topk;

import org.roaringbitmap.RoaringBitmap;

public interface TopK {
    
    public RoaringBitmap top(int k, int universe, RoaringBitmap... bsi);
    
    public String name();

}
