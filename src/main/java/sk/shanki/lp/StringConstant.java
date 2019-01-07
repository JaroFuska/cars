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
public class StringConstant extends BaseConstant {
    private final String string;

    public StringConstant(String string) {
        this.string = string;
    }
    
    public StringConstant(char c) {
        this.string = "" + c;
    }

    public String getString() {
        return string;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.string);        
        
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
        final StringConstant other = (StringConstant) obj;
        
        if (this.hashCode() != other.hashCode()) {
            return false;
        }
        
        return Objects.equals(this.string, other.string);
    }

    @Override
    public int compareTo(Term other, ObjectConstantMapping cache) {
        if(other instanceof StringConstant) {
            StringConstant o = (StringConstant)other;
            return o.string.compareTo(o.string);
        } else {
            return compareToDifferent(other);
        }
    }

    public StringConstant append(StringConstant other) {
        String ret = string + other.string;
        return new StringConstant(ret);
    }

    public StringConstant remove() {
        String ret;
        
        if (string.isEmpty()) {
            ret = "";
        } else {
            ret = string.substring(0, string.length() - 1);
        }
        
        return new StringConstant(ret);
    }

    @Override
    public ProgramPrinter print(ProgramPrinter printer) {
        return printer.printStringConstant(string);
    }

    @Override
    public int getPriority() {
        return 2;
    }

}