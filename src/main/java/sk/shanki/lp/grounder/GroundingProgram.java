/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.grounder;

import java.util.HashSet;
import java.util.Set;
import sk.shanki.lp.ObjectConstantMapping;
import sk.shanki.lp.Preferences;
import sk.shanki.lp.Program;
import sk.shanki.lp.exceptions.NotSafeException;
import sk.shanki.lp.Literal;

/**
 *
 * @author shanki
 */
public class GroundingProgram {
    private final GroundingRules rules;
    private final GroundingConstraints constraints;
    private final GroundingWeakConstraints weaks;
    private final Preferences preferences;
    
    public GroundingProgram() {
        rules       = new GroundingRules();
        constraints = new GroundingConstraints();
        weaks       = new GroundingWeakConstraints();
        preferences = new Preferences();
    }
    
    public void add(GroundingRule rule) {
        rules.add(rule);
    }
    
    public void add(GroundingConstraint constraint) {
        constraints.add(constraint);
    }
    
    public void add(GroundingWeakConstraint weak) {
        weaks.add(weak);
    }
    
    public void add(Preferences preferences) {
        this.preferences.addAll(preferences);
    }
       
    public Program ground() throws NotSafeException {
        Instances instances         = groundInstances();
        
        Program gProgram            = instances.toProgram();
        Preferences gPreferences    = instances.groundPreferences(preferences);
        
        gProgram.add(gPreferences);
        
        return gProgram;
    }
    
    public Instances groundInstances() throws NotSafeException {
        return groundInstances(new HashSet<>());
    }    
    
    public Instances groundInstances(Set<Literal> inLiterals) throws NotSafeException {

        Instances groundFacts           = rules.facts();
        
        GroundingDatabase db            = new GroundingDatabase();
        db.addAll(inLiterals);
        db.addHeads(groundFacts);
        
        GroundingRules rulesToInstantiate     = rules.nonFacts();
        Instances instances             = new Instances();
  
        ObjectConstantMapping cache             = new ObjectConstantMapping();
        
        boolean newLiteral;
        
        do {
            // z pohladu groundovania su pravidla, constrainty a weak constrainty to iste, dat tomu spolocnu nadtriedu a robit to cez to
            Instances newInstances  = rulesToInstantiate.groundInstances(db, cache);
            Instances newConstraints = constraints.groundInstances(db, cache);
            Instances newWeaks       = weaks.groundInstances(db, cache);
            
            db.updateLastUsed();            
            newLiteral              = db.addHeads(newInstances);            
            instances.addAll(newInstances);
            instances.addAll(newConstraints);
            instances.addAll(newWeaks);
        } while(newLiteral);
        
        instances.addAll(groundFacts);
        
        for (Literal literal : inLiterals) {
            instances.add(literal.toFactRule());
        }
        
        return instances;
    }  
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(rules.toString());
        sb.append(constraints.toString());
        sb.append(weaks.toString());
        sb.append(preferences.toString());
        
        return sb.toString();
    }

}