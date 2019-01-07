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
public class GroundingConstraints {
    
    private final ArrayList<GroundingConstraint> constraints = new ArrayList<>();

    void add(GroundingConstraint constraint) {
        constraints.add(constraint);
    }

    Instances groundInstances(GroundingDatabase db, ObjectConstantMapping cache) throws NotSafeException {
       final Instances instances            = new Instances();
        
        for (GroundingConstraint constraint : constraints) {
            try {
                instances.addAll(constraint.ground(db, cache));
            } catch (UnboundVariableException ex) {
                throw new NotSafeException(constraint, ex);
            }
        }        
        
        return instances;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        for (GroundingConstraint constraint : constraints) {
            sb.append(constraint.toString());
        }
        
        return sb.toString();
    }

}