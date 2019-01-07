/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
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
 */
public class Rules extends ArrayList<Rule> {

    public Rules() {
    }
    
    public Rules(Collection<Rule> rules) {
        super(rules);
    }    

    Collection<Atom> collectAllAtoms() {
        Set<Atom> atoms = new HashSet<>();

        for (Rule rule : this) {
            atoms.addAll(rule.collectAllAtoms());
        }
        

        return atoms;
    }

    Collection<? extends Literal> collectAllLiterals() {
        Set<Literal> literals = new HashSet<>();
        
        for (Rule rule : this) {
            literals.addAll(rule.collectAllLiterals());
        }
        
        return literals;
    }
    
    <P extends CompiledProgram, R extends CompiledRule, L extends CompiledLiteral, LS extends CompiledLiterals> P compile(CompilationFactory<P,R,L,LS> factory, CompiledLiteralCache<L> cache) {
        P program = factory.createProgram(cache);
        for (Rule rule : this) {
            program.add(rule.compile(factory, cache));
        }
        
        return program;
    }    

    Rules toMetaProgram() {
        Rules meta = new Rules();
        
        for (Rule rule : this) {
            meta.addAll(rule.toMetaProgram());
        }
        
        meta.add(new Rule(new Atom("rule_count", new NumberConstant(size()))));
        
        return meta;
    }

    public ProgramPrinter print(ProgramPrinter printer) {    
        return printer.printRules(this);
    }

    @Override
    public String toString() {
        return print(new StandardProgramPrinter()).toString();
    }
    
    public Rules rewriteElementsToIds(ObjectConstantMapping mapping) {
        Rules ret = new Rules();
        
        for (Rule rule : this) {
            ret.add(rule.rewriteTermsToIds(mapping));
        }
        
        return ret;
    }
    
    Rules getPartial() {
        Rules rules = new Rules();
        
        for (Rule rule : this) {
            if (rule.isPartial()) {
                rules.add(rule);
            }
        }
        
        return rules;
    }
    
    Rules getFull() {
        Rules rules = new Rules();
        
        for (Rule rule : this) {
            if (rule.isPartial() == false) {
                rules.add(rule);
            }
        }
        
        return rules;
    }

}