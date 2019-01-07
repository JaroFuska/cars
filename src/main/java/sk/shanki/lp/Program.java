/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

import sk.shanki.lp.exceptions.NotSafeException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import sk.shanki.lp.predicates.GroundingPredicateFactory;
import sk.shanki.lp.grounder.GroundingProgram;
import sk.shanki.lp.smodels.SmodelsLiteralCache;
import sk.shanki.lp.smodels.SmodelsProgram;
import sk.shanki.lp.smodels.SmodelsRule;
import sk.shanki.lp.compilation.CompilationFactory;
import sk.shanki.lp.compilation.CompiledLiteral;
import sk.shanki.lp.compilation.CompiledLiteralCache;
import sk.shanki.lp.compilation.CompiledLiterals;
import sk.shanki.lp.compilation.CompiledProgram;
import sk.shanki.lp.compilation.CompiledRule;
import sk.shanki.lp.printing.StandardProgramPrinter;
import sk.shanki.lp.printing.ProgramPrinter;

/**
 *
 * @author shanki
 * 
 * Mutable
 */
public class Program implements Iterable<Rule> {
    private final Rules rules;
    private final Constraints constraints;
    private final WeakConstraints weaks;
    private final Preferences preferences;
    
    public Program() {
        rules       = new Rules();
        constraints = new Constraints();
        weaks       = new WeakConstraints();
        preferences = new Preferences();
    }    
    
    public Program(Rules rules) {
        this.rules          = new Rules(rules);
        this.constraints    = new Constraints();
        this.weaks          = new WeakConstraints();
        this.preferences    = new Preferences();
    }    

    public Program(Rules rules, WeakConstraints weak) {
        this.rules          = new Rules(rules);
        this.constraints    = new Constraints();
        this.weaks          = new WeakConstraints(weak);
        this.preferences    = new Preferences();
    }
    
    public Program(Rules rules, Constraints constraints, WeakConstraints weak, Preferences preferences) {
        this.rules          = new Rules(rules);
        this.constraints    = new Constraints(constraints);
        this.weaks          = new WeakConstraints(weak);
        this.preferences    = new Preferences(preferences);
    }
    
    public Program addNegConstraints() {
        Constraints newConstraints = new Constraints(constraints);
        
        Set<Atom> atoms            = collectAllAtoms();

        for (Atom atom : atoms) {
            newConstraints.add(Constraint.forNegation(atom));
        }

        return new Program(rules, newConstraints, weaks, preferences);
    }
    
    public Program normalizeConstraints() {
        Rules newRules = new Rules(rules);
        Rules cRules = constraints.toRules();
        newRules.addAll(cRules);
                
        return new Program(newRules, weaks);
    }

    public Set<Atom> collectAllAtoms() {
        Set<Atom> atoms = new HashSet<>();

        atoms.addAll(rules.collectAllAtoms());
        atoms.addAll(constraints.collectAllAtoms());
        atoms.addAll(weaks.collectAllAtoms());

        return atoms;
    }    
    
    @Override
    public String toString() {
        return print(new StandardProgramPrinter()).toString();
    }
        
    public ProgramPrinter print(ProgramPrinter printer) {
        rules.print(printer);
        constraints.print(printer);
        weaks.print(printer);
        preferences.print(printer);
        
        return printer;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.rules);
        hash = 59 * hash + Objects.hashCode(this.constraints);
        hash = 59 * hash + Objects.hashCode(this.weaks);
        hash = 59 * hash + Objects.hashCode(this.preferences);
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
        final Program other = (Program) obj;
        if (!Objects.equals(this.rules, other.rules)) {
            return false;
        }
        if (!Objects.equals(this.constraints, other.constraints)) {
            return false;
        }
        if (!Objects.equals(this.weaks, other.weaks)) {
            return false;
        }
        return Objects.equals(this.preferences, other.preferences);
    }


    
    public void addLiterals(Collection<Literal> literals) {
        for(Literal literal : literals) {
            addLiteral(literal);
        } 
    }
    
    public void addLiteral(Literal literal) {
        add(literal.toFactRule());
    }

    public void add(Program program) {
        rules.addAll(program.rules);
        constraints.addAll(program.constraints);
        weaks.addAll(program.weaks);
        preferences.addAll(program.preferences);
    }
    
    public void add(Collection<Rule> rules) {
        this.rules.addAll(rules);
    }
    
    public void add(Rules rules) {
        this.rules.addAll(rules);
    }
    
    public void add(Rule rule) {
        rules.add(rule);
    }
    
    public void add(Constraint constraint) {
        constraints.add(constraint);
    }
    
    public void add(Preferences preferences) {
        this.preferences.addAll(preferences);
    }

    public Program toMetaProgram() {
        
        Program meta  = new Program();

        meta.add(rules.toMetaProgram());
        
        Set<Atom> atoms = collectAllAtoms();
        
        for (Atom atom : atoms) {
            meta.add(atom.toMetaComplement());
        }
        
        meta.add(preferences.toMetaProgram());
        
        

        return meta;
    }

    public Set<Rule> generatingRules(AnswerSet answerSet) {
        Set<Rule> generating = new HashSet<>();
        
        for (Rule rule : rules) {
            if (rule.isGenerating(answerSet)) {
                generating.add(rule);
            }
        }
        
        return generating;
    }

    @Override
    public Iterator<Rule> iterator() {
        return rules.iterator();
    }

    public Set<Literal> collectAllLiterals() {
        Set<Literal> literals = new HashSet<>();
        
        literals.addAll(rules.collectAllLiterals());
        literals.addAll(constraints.collectAllLiterals());
        literals.addAll(weaks.collectAllLiterals());

        return literals;
    }

    public Program ground() throws NotSafeException {
        return toGroundingProgram().ground();
    }

    GroundingProgram toGroundingProgram() {        
        GroundingPredicateFactory factory = new GroundingPredicateFactory();
        
        GroundingProgram program = new GroundingProgram();
        for (Rule rule : rules) {
            program.add(rule.toGroundingRule(factory));
        }
        
        for (Constraint constraint : constraints) {
            program.add(constraint.toGroundingConstraint(factory));
        }
        
        for (WeakConstraint w : weaks) {
            program.add(w.toGroundingWeakConstraint(factory));
        }
        
        program.add(preferences);
        
        return program;
    }

    public boolean hasDisjunction() {
        for (Rule rule : rules) {
            if (rule.hasDisjunction()) {
                return true;
            }
        }
        
        return false;
    }

    public <P extends CompiledProgram, R extends CompiledRule, L extends CompiledLiteral, LS extends CompiledLiterals> P compile(CompilationFactory<P, R, L, LS> factory, CompiledLiteralCache<L> cache) {
        P trans = rules.compile(factory, cache);
        trans.add(preferences);
        
        return trans;
    }
    
    public SmodelsProgram toSmodelsProgram() {
        
        SmodelsLiteralCache cache = new SmodelsLiteralCache();
        
        SmodelsProgram program = new SmodelsProgram(cache);        

        for (Rule rule : rules) {
            SmodelsRule cRule = rule.toSmodelsRule(cache);
            
            cRule.wire();
            
            program.add(cRule);
        }
        
        for (WeakConstraint weak : weaks) {
//            SmodelsWeakConstraint sWeak = weak.toSmodelsWeakConstraint(cache);
            program.add(weak);
        }
        
        return program;        
    }
    
    public Program rewriteElementsToIds(ObjectConstantMapping mapping) {
        return new Program(
                rules.rewriteElementsToIds(mapping),
                constraints.rewriteElementsToIds(mapping),
                weaks.rewriteElementsToIds(mapping),
                preferences
        );
    }

    public boolean hasWeakConstraints() {
        return weaks.isEmpty() == false;
    }
    
    public WeakConstraints getWeakConstraints() {
        return weaks;
    }

    Rule getRule(int i) {
        return rules.get(i);
    }

}