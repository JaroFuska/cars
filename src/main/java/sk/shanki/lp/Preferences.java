/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

import java.util.ArrayList;
import java.util.Collection;
import sk.shanki.lp.printing.StandardProgramPrinter;
import sk.shanki.lp.printing.ProgramPrinter;

/**
 *
 * @author shanki
 */
public class Preferences extends ArrayList<Preference> {

    public Preferences(Collection<Preference> preferences) {
        super(preferences);
    }
    
    public Preferences() {
    }

    Program toMetaProgram() {
        Program meta = new Program();
        for (Preference preference : this) {
            meta.add(preference.toMetaRule());
        }
        
        return meta;
    }

    @Override
    public String toString() {
        return print(new StandardProgramPrinter()).toString();
    }
    
    public ProgramPrinter print(ProgramPrinter printer) {
        return printer.printPreferences(this);
    }

}