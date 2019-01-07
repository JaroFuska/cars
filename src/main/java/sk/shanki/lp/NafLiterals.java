/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

import sk.shanki.lp.exceptions.UnboundVariableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import sk.shanki.lp.printing.StandardProgramPrinter;
import sk.shanki.lp.printing.ProgramPrinter;

/**
 *
 * @author shanki
 * 
 * Immmutable
 */
public class NafLiterals extends ArrayList<NafLiteral> {

    public NafLiterals(NafLiteral... nafliterals) {
        this(Arrays.asList(nafliterals));
    }
    
    public NafLiterals(Collection<? extends NafLiteral> nafLiterals) {        
        super(nafLiterals);
    }

    public NafLiterals() {
    }

    public NafLiterals(Literal literal) {
        add(literal);
    }

    public boolean isGround() {
        for (NafLiteral nafLiteral : this) {
            if (nafLiteral.isGround() == false) {
                return false;
            }
        }
        
        return true;
    }

    public NafLiterals fullySubstitute(Substitution substitution) throws UnboundVariableException {
        List<NafLiteral> substituted = new ArrayList<>();
        
        for (NafLiteral nafLiteral : this) {
            substituted.add(nafLiteral.fullySubstitute(substitution));
        }
        
        return new NafLiterals(substituted);
    }
    
    public NafLiterals partiallySubstitute(Substitution substitution) {
        List<NafLiteral> substituted = new ArrayList<>();
        
        for (NafLiteral nafLiteral : this) {
            substituted.add(nafLiteral.partiallySubstitute(substitution));
        }
        
        return new NafLiterals(substituted);
    }    

    public boolean isSingleton() {
        return size() == 1;
    }

    public NafLiteral getSingleton() {
        return get(0);
    }

    Set<Atom> collectAllAtoms() {
        Set<Atom> atoms = new HashSet<>();
        
        for (NafLiteral nafLiteral : this) {
            atoms.add(nafLiteral.getAtom());
        }
        
        return atoms;
    }
    
    Set<Literal> collectAllLiterals() {
        Set<Literal> literals = new HashSet<>();
        
        for (NafLiteral nafLiteral : this) {
            literals.add(nafLiteral.getLiteral());
        }
        
        return literals;
    }

    boolean containsNot() {
        for (NafLiteral nafLiteral : this) {
            if (nafLiteral instanceof Not) {
                return true;
            }
        }
        
        return false;
    }

    public Literals getPositiveLiterals() {
        ArrayList<Literal> literals = new ArrayList<>();
        
        for (NafLiteral nafLiteral : this) {
            if (nafLiteral instanceof Literal) {
                literals.add((Literal)nafLiteral);
            }
        }
        
        return new Literals(literals);
    }

    public Literals getNegativeLiterals() {
        ArrayList<Literal> literals = new ArrayList<>();
        
        for (NafLiteral nafLiteral : this) {
            if (nafLiteral instanceof Not) {
                literals.add(((Not)nafLiteral).getLiteral());
            }
        }
        
        return new Literals(literals);
    }
    
    boolean isSatisfiedIn(AnswerSet answerSet) {
        for (NafLiteral nafLiteral : this) {
            if (nafLiteral.isSatisfiedIn(answerSet) == false) {
                return false;
            }
        }
        
        return true;
    }

    public ProgramPrinter print(ProgramPrinter printer, String separator) {
        return printer.printNafLiterals(this, separator);
    }

    @Override
    public String toString() {
        return print(new StandardProgramPrinter(), " ").toString();
    }
    
    public NafLiterals rewriteElementsToIds(ObjectConstantMapping mapping) {
        NafLiterals ret = new NafLiterals();
        
        for (NafLiteral nafLiteral : this) {
            ret.add(nafLiteral.rewriteObjectConstantsToIds(mapping));
        }
        
        return ret;
    }

    public NafLiterals getPositive() {
        ArrayList<NafLiteral> literals = new ArrayList<>();
        
        for (NafLiteral nafLiteral : this) {
            if (nafLiteral instanceof Literal) {
                literals.add(nafLiteral);
            }
        }
        
        return new NafLiterals(literals);
    }

    boolean willChange(Substitution substitution) {
        for (NafLiteral nafLiteral : this) {
            if (nafLiteral.willChange(substitution)) {
                return true;
            }
        }
        
        return false;
    }

}