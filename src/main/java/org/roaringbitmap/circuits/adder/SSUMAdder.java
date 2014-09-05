package org.roaringbitmap.circuits.adder;

import java.util.ArrayList;
import java.util.List;

import org.roaringbitmap.FastAggregation;
import org.roaringbitmap.RoaringBitmap;

// fancy adder by Knuth.
public class SSUMAdder implements Adder {
    private List<RoaringBitmap> slowbuildHalfAdder(RoaringBitmap inputA,
            RoaringBitmap inputB) {
        List<RoaringBitmap> ans = new ArrayList<RoaringBitmap>();
        RoaringBitmap carryOut = RoaringBitmap.and(inputA, inputB);
        RoaringBitmap sumBit = RoaringBitmap.xor(inputA, inputB);
        ans.add(carryOut);
        ans.add(sumBit);
        return ans;
    }

    private List<RoaringBitmap> buildHalfAdder(RoaringBitmap inputA,
            RoaringBitmap inputB) {
        List<RoaringBitmap> ans = new ArrayList<RoaringBitmap>();
        RoaringBitmap carryOut = RoaringBitmap.and(inputA, inputB);
        inputA.xor(inputB);
        //RoaringBitmap sumBit = RoaringBitmap.xor(inputA, inputB);
        RoaringBitmap sumBit = inputA;
        ans.add(carryOut);
        ans.add(sumBit);
        return ans;
    }

    // output: carry first, then sum
    private List<RoaringBitmap> slowbuildFullAdder(RoaringBitmap inputA,
            RoaringBitmap inputB, RoaringBitmap inputC) {
        List<RoaringBitmap> ans = new ArrayList<RoaringBitmap>();
        RoaringBitmap temp1 = RoaringBitmap.xor(inputA, inputB);
        RoaringBitmap sumBit = RoaringBitmap.xor(temp1, inputC);
        RoaringBitmap temp2 = RoaringBitmap.and(temp1, inputC);
        RoaringBitmap temp3 = RoaringBitmap.and(inputA, inputB);
        RoaringBitmap carryOut = RoaringBitmap.or(temp2, temp3);
        ans.add(carryOut);
        ans.add(sumBit); // added after carryOut
        return ans;
    }

    // output: carry first, then sum
    private List<RoaringBitmap> buildFullAdder(RoaringBitmap inputA,
            RoaringBitmap inputB, RoaringBitmap inputC) {
        List<RoaringBitmap> ans = new ArrayList<RoaringBitmap>();
        RoaringBitmap temp1 = RoaringBitmap.xor(inputA, inputB);
        RoaringBitmap sumBit = RoaringBitmap.xor(temp1, inputC);
        temp1.and(inputC);
        inputA.and(inputB);
        temp1.or(inputA);
        RoaringBitmap carryOut = temp1;
        ans.add(carryOut);
        ans.add(sumBit); // added after carryOut
        return ans;
    }

    @Override
    public RoaringBitmap[] add(RoaringBitmap... set) {
        if (set.length == 0)
            return new RoaringBitmap[0];
        if (set.length == 1) {
            RoaringBitmap[] answer = new RoaringBitmap[1];
            answer[0] = set[0].clone();
            return answer;
        }
        List<RoaringBitmap> zBits = new ArrayList<RoaringBitmap>();
        List<RoaringBitmap> currentLevelOutputs = new ArrayList<RoaringBitmap>();
        for (RoaringBitmap input : set) {
            currentLevelOutputs.add(input.clone()); // clone maybe?
        }
        while (true) { // currentLevelInputs.size() > 1) {
            List<RoaringBitmap> currentLevelInputs = currentLevelOutputs;

            if (currentLevelInputs.size() < 4) {
                List<RoaringBitmap> cOutAndSum = null;
                if (currentLevelInputs.size() == 2)
                    cOutAndSum = buildHalfAdder(currentLevelInputs.get(0),
                            currentLevelInputs.get(1));
                else
                    cOutAndSum = buildFullAdder(currentLevelInputs.get(0),
                            currentLevelInputs.get(1),
                            currentLevelInputs.get(2));
                zBits.add(cOutAndSum.get(1)); // sum
                zBits.add(cOutAndSum.get(0)); // cout
                return zBits.toArray(new RoaringBitmap[0]); // our only exit.
            }

            currentLevelOutputs = new ArrayList<RoaringBitmap>();
            // else we start with a bunch of FAs and maybe a HA at end
            // at least one FA
            List<RoaringBitmap> firstFA = buildFullAdder(
                    currentLevelInputs.get(0), currentLevelInputs.get(1),
                    currentLevelInputs.get(2));
            currentLevelOutputs.add(firstFA.get(0)); // cOut
            int curPos = 3;
            RoaringBitmap curChainIn = firstFA.get(1); // sum
            while (curPos + 2 < currentLevelInputs.size()) { // not last FA (or
                                                             // HA)
                List<RoaringBitmap> chainFA = buildFullAdder(curChainIn,
                        currentLevelInputs.get(curPos),
                        currentLevelInputs.get(curPos + 1));
                curPos += 2;
                currentLevelOutputs.add(chainFA.get(0));
                curChainIn = chainFA.get(1);
            }
            // end of chain, may be either a FA or a HA
            List<RoaringBitmap> finalAdderOutputs = null;
            if (curPos + 2 == currentLevelInputs.size()) // ends in FA
                finalAdderOutputs = buildFullAdder(curChainIn,
                        currentLevelInputs.get(curPos),
                        currentLevelInputs.get(curPos + 1));
            else
                // ends in half adder
                finalAdderOutputs = buildHalfAdder(curChainIn,
                        currentLevelInputs.get(curPos));
            currentLevelOutputs.add(finalAdderOutputs.get(0));
            zBits.add(finalAdderOutputs.get(1)); // final sum bit
        }
    }

}
