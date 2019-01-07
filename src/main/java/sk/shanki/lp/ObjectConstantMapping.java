/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author shanki
 */
public class ObjectConstantMapping {
    private final Map<ObjectConstant,FunctionalTerm> toId = new HashMap<>();
    private final Map<FunctionalTerm,ObjectConstant> fromId = new HashMap<>();
    
    private final String prefix;
    private long nextId         = 0;
    
    public ObjectConstantMapping() {
        prefix = "id";
    }
    
    public ObjectConstantMapping(String prefix) {
        this.prefix = prefix;
    }
        
    public FunctionalTerm toId(ObjectConstant constant) {
        FunctionalTerm idTerm;
        
        if (toId.containsKey(constant)) {
            idTerm = toId.get(constant);
        } else {
            String id = prefix + nextId;
            idTerm = new FunctionalTerm(id);
            toId.put(constant, idTerm);
            fromId.put(idTerm, constant);
            ++nextId;
        }
        
        return idTerm;
    }
    
    public ObjectConstant fromId(FunctionalTerm idTerm) {
        return fromId.get(idTerm);
    }

    @Override
    public String toString() {
        return toId.toString();
    }

    int compare(ObjectConstant x, ObjectConstant y) {
        return toId(x).compareTo(toId(y), null);
    }

}