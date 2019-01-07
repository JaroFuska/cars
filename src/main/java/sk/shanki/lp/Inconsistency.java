/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

/**
 *
 * @author shanki
 */
public class Inconsistency {
    private static final Atom instance = new Atom("inconsistency");
    
    public static Atom getInstance() {
        return instance;
    }
}
