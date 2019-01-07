/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.printing;

import java.math.BigDecimal;
import java.util.ArrayList;
import sk.shanki.lp.AnswerSet;
import sk.shanki.lp.Constraint;
import sk.shanki.lp.NafLiterals;
import sk.shanki.lp.Preference;
import sk.shanki.lp.Rule;
import sk.shanki.lp.Terms;
import sk.shanki.lp.Atom;
import sk.shanki.lp.WeakConstraint;
import sk.shanki.lp.NafLiteral;
import sk.shanki.lp.Literal;

/**
 *
 * @author shanki
 */
public class StandardProgramPrinter implements ProgramPrinter {
    
    private final StringBuilder sb      = new StringBuilder();
    
    public ProgramPrinter print(String text) {
        sb.append(text);
        return this;
    }

    @Override
    public ProgramPrinter printRule(String name, NafLiterals head, NafLiterals body) {
        if (name.isEmpty() == false) {
            sb.append("@name(");
            sb.append(name);
            sb.append(") ");
        }
        head.print(this, " v ");
        if (body.isEmpty() == false) {
            sb.append(" :- ");
            body.print(this, ", ");
        }
        sb.append(".");
        return this;
    }    

    @Override
    public ProgramPrinter printSeparator(String separator) {
        sb.append(separator);
        return this;
    }

    @Override
    public ProgramPrinter printNot(Literal literal) {
        sb.append("not ");
        literal.print(this);
        return this;
    }

    @Override
    public ProgramPrinter printNeg(Atom matom) {
        sb.append("-");
        matom.print(this);
        return this;
    }

    @Override
    public ProgramPrinter printTuple(String predicate, Terms terms) {
        sb.append(predicate);
        if (terms.isEmpty() == false) {
            sb.append("(");
            terms.print(this, ", ");
            sb.append(")");
        }
        return this;
    }

    @Override
    public ProgramPrinter printKeyword(String keyword) {
        sb.append(keyword);
        return this;
    }

    @Override
    public ProgramPrinter printNumber(BigDecimal number) {
        sb.append(number);
        return this;
    }

    @Override
    public ProgramPrinter printObject(Object value) {
        sb.append(value);
        return this;
    }

    @Override
    public ProgramPrinter printStringConstant(String string) {
        sb.append("\"");
        sb.append(string);
        sb.append("\"");
        
        return this;
    }

    @Override
    public ProgramPrinter printVariable(String name) {
        sb.append(name);
        return this;
    }

    @Override
    public ProgramPrinter printConstraint(NafLiterals body) {
        sb.append(":- ");
        body.print(this, ", ");
        sb.append(".");
        return this;
    }

    @Override
    public ProgramPrinter printWeakConstraint(NafLiterals body, BigDecimal weight, BigDecimal level, Terms terms) {
        sb.append(" :~ ");
        body.print(this, ", ");
        sb.append(".");        
        sb.append("[");
        sb.append(weight);
        sb.append("@");
        sb.append(level);
        if (terms.isEmpty() == false) {
            sb.append(",");
            terms.print(this, ", ");
        }
        sb.append("]");
        return this;
    }

    @Override
    public ProgramPrinter printPrefence(Rule less, Rule more) {
        sb.append(less.getName());
        sb.append(" < ");
        sb.append(more.getName());
        sb.append(".");
        return this;
    }

    @Override
    public String toString() {
        return sb.toString();
    }

    @Override
    public ProgramPrinter printAnswerSet(Iterable<Literal> literals) {
        sb.append("[");
        
        this.printLiterals(literals, " ");
        
        sb.append("]");
        
        return this;
    }

    @Override
    public ProgramPrinter printAnswerSets(Iterable<AnswerSet> answerSets) {
        sb.append("[");
        
        boolean isFirst = true;
        
        for (AnswerSet as : answerSets) {
            if (isFirst == false) {
                sb.append(" ");
            } else {
                isFirst = false;
            }
            
            as.print(this);
        }
        
        sb.append("]");
        
        return this;
    }

    @Override
    public ProgramPrinter printNafLiterals(Iterable<NafLiteral> nafLiterals, String separator) {
        boolean isFirst = true;

        for (NafLiteral nafLiteral : nafLiterals) {
            if (isFirst == false) {
                this.printSeparator(separator);
            } else {
                isFirst = false;
            }

            nafLiteral.print(this);
         }

         return this;
    }

    @Override
    public ProgramPrinter printLiterals(Iterable<Literal> literals, String separator) {
        boolean isFirst = true;
        
        for (Literal literal : literals) {
            if (isFirst == false) {
                sb.append(separator);
            } else {
                isFirst = false;
            }
            
            literal.print(this);
        }
        
        return this;
    }

    @Override
    public ProgramPrinter printRules(Iterable<Rule> rules) {
        for (Rule rule : rules) {
            rule.print(this);
            this.printNewLine();
        }
        return this;
    }

    private ProgramPrinter printNewLine() {
        sb.append("\n");
        return this;
    }

    @Override
    public ProgramPrinter printConstraints(Iterable<Constraint> constraints) {
        for (Constraint constraint : constraints) {
            constraint.print(this);
            this.printNewLine();
        }
        
        return this;
    }

    @Override
    public ProgramPrinter printWeakConstraints(Iterable<WeakConstraint> weaks) {
        for (WeakConstraint weak : weaks) {
            weak.print(this);
            this.printNewLine();
        }
        
        return this;
    }

    @Override
    public ProgramPrinter printPreferences(ArrayList<Preference> preferences) {
        for (Preference preference : preferences) {
            preference.print(this);
            this.printNewLine();
        }
    
        return this;
    }

}