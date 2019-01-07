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
public class Preference {
    private final Rule less;
    private final Rule more;
    
    public Preference(Rule less, Rule more) {
        this.less = less;
        this.more = more;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.less);
        hash = 97 * hash + Objects.hashCode(this.more);        
        
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
        final Preference other = (Preference) obj;
        
        if (this.hashCode() != other.hashCode()) {
            return false;
        }
        
        if (!Objects.equals(this.less, other.less)) {
            return false;
        }
        return Objects.equals(this.more, other.more);
    }

    public Rule getLess() {
        return less;
    }

    public Rule getMore() {
        return more;
    }

    Rule toMetaRule() {
        return new Rule(new Atom("pr", new ObjectConstant(more), new ObjectConstant(less)));
    }

    @Override
    public String toString() {
        return less.getName() + " < " + more.getName();
    }

    public void print(ProgramPrinter printer) {
        printer.printPrefence(less, more);
    }
    
}