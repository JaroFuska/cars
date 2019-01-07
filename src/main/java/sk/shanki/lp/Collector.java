/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

/**
 *
 * @author shanki
 */
public class Collector {
    private final WeakConstraints weaks;
    private final int maxAnswerSets;
    
    private final AnswerSets ass = new AnswerSets();
    private ViolationDegree bestDegree;
    
    public Collector(WeakConstraints weaks, int maxAnswerSets) {
        this.weaks          = weaks;
        this.maxAnswerSets  = maxAnswerSets;
    }

    public void add(AnswerSet as) {
        ViolationDegree degree = weaks.computeViolationDegreeOf(as);
        
        int c = degree.compareTo(bestDegree, -1);

        if (c < 0) {
            ass.clear();
            ass.add(as);
            bestDegree = degree;
        } else if (c == 0) {
            ass.add(as);
        }
    }

    public boolean searchNext() {
        if (weaks.isEmpty()) {
            return maxAnswerSets == 0 || ass.size() < maxAnswerSets;
        } else {
            return true;
        }
    }

    public AnswerSets getAnswerSets() {
        if (weaks.isEmpty() == false || maxAnswerSets == 0) {
            return ass;
        } else {
            AnswerSets sel = new AnswerSets();

            for (AnswerSet a : ass) {
                if (sel.size() < maxAnswerSets) {
                    sel.add(a);
                } else {
                    break;
                }
            }
            return sel;
        }
    }
}