/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

import sk.shanki.lp.exceptions.UnboundVariableException;
import java.util.ArrayList;
import java.util.List;
import sk.shanki.lp.predicates.GroundingPredicateFactory;
import sk.shanki.lp.compilation.CompilationFactory;
import sk.shanki.lp.compilation.CompiledLiteral;
import sk.shanki.lp.compilation.CompiledLiteralCache;
import sk.shanki.lp.compilation.CompiledLiterals;
import sk.shanki.lp.compilation.CompiledProgram;
import sk.shanki.lp.compilation.CompiledRule;
import sk.shanki.lp.printing.StandardProgramPrinter;
import sk.shanki.lp.smodels.SmodelsLiteral;
import sk.shanki.lp.smodels.SmodelsLiteralCache;
import sk.shanki.lp.printing.ProgramPrinter;

/**
 *
 * @author shanki
 */
public class Literals extends ArrayList<Literal> {
    
    Literals(List<Literal> literals) {
        super(literals);
    }

    public boolean isGround() {
        for (Literal literal : this) {
            if (literal.isGround() == false) {
                return false;
            }
        }
        
        return false;
    }

    public Literals fullySubstitute(Substitution substitution) throws UnboundVariableException {
        List<Literal> substituted = new ArrayList<>();
        
        for (Literal literal : this) {
            substituted.add(literal.fullySubstitute(substitution));
        }
        
        return new Literals(substituted);
    }
    
    public List<SmodelsLiteral> toSmodelsLiteral(SmodelsLiteralCache cache) {
        List<SmodelsLiteral> compiled = new ArrayList<>();
        
        for (Literal literal : this) {
                compiled.add(cache.ensure(literal));
        }
        
        return compiled;
    }    

    public <P extends CompiledProgram, R extends CompiledRule, L extends CompiledLiteral, LS extends CompiledLiterals> LS compile(CompilationFactory<P,R,L,LS> factory, CompiledLiteralCache<L> cache) {
        List<L> compiled = new ArrayList<>();
        
        for (Literal literal : this) {
            compiled.add(cache.ensure(literal));
        }

        return factory.createLiterals(compiled);
    }
    
    @Override
    public String toString() {
        return print(new StandardProgramPrinter(), ", ").toString();
    }
    
    public ProgramPrinter print(ProgramPrinter printer, String separator) {
        return printer.printLiterals(this, separator);
    }

    Carret split(GroundingPredicateFactory factory) {
        List<Literal> lits = new ArrayList<>();
        List<GroundingPredicate> predicates = new ArrayList<>();
        
        for (Literal literal : this) {
            GroundingPredicate p = factory.create(literal);
            
            if (p == null) {
                lits.add(literal);
            } else {
                predicates.add(p);
            }
        }
        
        return new Carret(new Literals(lits), new GroundingPredicates(predicates));
    }

    public static class Carret {

        public Literals literals;
        public GroundingPredicates predicates;
        
        public Carret(Literals literals, GroundingPredicates predicates) {
            this.literals   = literals;
            this.predicates = predicates;
        }
    }
}