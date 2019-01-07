/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

import java.util.Objects;
import sk.shanki.lp.printing.ProgramPrinter;

/**
 *
 * @author shanki
 */
public class ObjectConstant extends BaseConstant {
    
    private final Object value;

    public ObjectConstant(Object value) {
        this.value  = value;
    }
    
    public Object getValue() {
        return value;
    }

    @Override
    public int compareTo(Term other, ObjectConstantMapping cache) {
        if (other instanceof ObjectConstant) {
            ObjectConstant o = (ObjectConstant)other;
            
            return cache.toId(this).compareTo(cache.toId(o), null);
        } else {
            return compareToDifferent(other);
        }
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.value);        
        
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ObjectConstant other = (ObjectConstant) obj;
        
        if (this.hashCode() != other.hashCode()) {
            return false;
        }
        
        return Objects.equals(this.value, other.value);
    }

    @Override
    public ProgramPrinter print(ProgramPrinter printer) {
        return printer.printObject(value);
    }

    @Override
    public int getPriority() {
        return 3;
    }
    
}