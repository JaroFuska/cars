/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.grounder;

import java.util.ArrayList;
import sk.shanki.lp.ObjectConstantMapping;
import sk.shanki.lp.exceptions.NotSafeException;
import sk.shanki.lp.exceptions.UnboundVariableException;

/**
 *
 * @author shanki
 */
class GroundingWeakConstraints {

    private final ArrayList<GroundingWeakConstraint> weaks = new ArrayList<>();

    void add(GroundingWeakConstraint weak) {
        weaks.add(weak);
    }

    Instances groundInstances(GroundingDatabase db, ObjectConstantMapping cache) throws NotSafeException {
       final Instances instances            = new Instances();
        
        for (GroundingWeakConstraint weak : weaks) {
            try {
                instances.addAll(weak.ground(db, cache));
            } catch (UnboundVariableException ex) {
                throw new NotSafeException(weak, ex);
            }
        }        
        
        return instances;
    }

    @Override
    public String toString() {
        return "";
    }

}