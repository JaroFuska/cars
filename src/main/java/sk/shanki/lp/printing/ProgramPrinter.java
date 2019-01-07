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
public interface ProgramPrinter {

    public ProgramPrinter printRule(String name, NafLiterals head, NafLiterals body);    
    public ProgramPrinter printSeparator(String separator);
    public ProgramPrinter printNot(Literal literal);
    public ProgramPrinter printNeg(Atom matom);
    public ProgramPrinter printTuple(String symbol, Terms terms);
    public ProgramPrinter printKeyword(String keyword);
    public ProgramPrinter printNumber(BigDecimal number);
    public ProgramPrinter printObject(Object value);
    public ProgramPrinter printStringConstant(String string);
    public ProgramPrinter printVariable(String name);
    public ProgramPrinter printConstraint(NafLiterals body);
    public ProgramPrinter printWeakConstraint(NafLiterals body, BigDecimal weight, BigDecimal level, Terms terms);
    public ProgramPrinter printPrefence(Rule less, Rule more);
    public ProgramPrinter printAnswerSet(Iterable<Literal> literals);
    public ProgramPrinter printAnswerSets(Iterable<AnswerSet> answerSets);
    public ProgramPrinter printNafLiterals(Iterable<NafLiteral> nafLiterals, String separator);
    public ProgramPrinter printLiterals(Iterable<Literal> literals, String separator);
    public ProgramPrinter printRules(Iterable<Rule> rules);
    public ProgramPrinter printConstraints(Iterable<Constraint> constraints);
    public ProgramPrinter printWeakConstraints(Iterable<WeakConstraint> weaks);
    public ProgramPrinter printPreferences(ArrayList<Preference> preferences);

}