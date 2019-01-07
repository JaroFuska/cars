/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.printing;

import java.math.BigDecimal;
import sk.shanki.lp.NafLiterals;
import sk.shanki.lp.Terms;

/**
 *
 * @author shanki
 */
public class DlvProgramPrinter extends StandardProgramPrinter {

    @Override
    public ProgramPrinter printWeakConstraint(NafLiterals body, BigDecimal weight, BigDecimal level, Terms terms) {
        print(" :~ ");
        body.print(this, ", ");
        print(".");        
        print("[");
        print(weight.toString());
        print(":");
        print(level.toString());
        print("]");
        return this;
    }

}