/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

import java.math.BigDecimal;
import java.util.Objects;
import sk.shanki.lp.printing.ProgramPrinter;

/**
 *
 * @author shanki
 */
public class NumberConstant extends BaseConstant {
    private final BigDecimal number;
    
    public NumberConstant(BigDecimal number) {
        this.number = number;
    }
    
    public NumberConstant(int number) {
        this.number = new BigDecimal(number);
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.number);        
        
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
        final NumberConstant other = (NumberConstant) obj;
        
        if (this.hashCode() != other.hashCode()) {
            return false;
        }
        
        return Objects.equals(this.number, other.number);
    }

    public NumberConstant abs() {
        return new NumberConstant(number.abs());
    }

    public NumberConstant add(NumberConstant other) {
        return new NumberConstant(this.number.add(other.number));
    }

    public NumberConstant subtract(NumberConstant other) {
        return new NumberConstant(this.number.subtract(other.number));
    }
    
    public NumberConstant pow(NumberConstant other) {
        return new NumberConstant(this.number.pow(other.number.intValueExact()));
    }
    
    public NumberConstant and(NumberConstant other) {
        return new NumberConstant(new BigDecimal(this.number.toBigIntegerExact().and(other.number.toBigIntegerExact())));
    }    
    
    @Override
    public int compareTo(Term other, ObjectConstantMapping cache) {
        if (other instanceof NumberConstant) {
            NumberConstant o = (NumberConstant)other;
            
            return this.number.compareTo(o.number);
        } else {
            return compareToDifferent(other);
        }
    }
    
    public int intValue() {
        return number.intValue();
    }

    @Override
    public ProgramPrinter print(ProgramPrinter printer) {
        return printer.printNumber(number);
    }

    @Override
    public int getPriority() {
        return 1;
    }
    
}