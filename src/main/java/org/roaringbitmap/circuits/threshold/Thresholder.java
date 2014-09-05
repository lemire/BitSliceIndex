package org.roaringbitmap.circuits.threshold;

public interface Thresholder<T> {
    public void setup(int numInputs, int threshold, int maxValue);

    public T threshold(T... set);

    public String name();
}