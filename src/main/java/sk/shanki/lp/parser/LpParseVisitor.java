/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import org.antlr.v4.runtime.tree.ParseTree;
import sk.shanki.lp.Atom;
import sk.shanki.lp.Constraint;
import sk.shanki.lp.Constraints;
import sk.shanki.lp.FunctionalTerm;
import sk.shanki.lp.NafLiterals;
import sk.shanki.lp.Neg;
import sk.shanki.lp.Not;
import sk.shanki.lp.NumberConstant;
import sk.shanki.lp.Program;
import sk.shanki.lp.Rule;
import sk.shanki.lp.Rules;
import sk.shanki.lp.StringConstant;
import sk.shanki.lp.Terms;
import sk.shanki.lp.Variable;
import sk.shanki.lp.WeakConstraint;
import sk.shanki.lp.WeakConstraints;
import sk.shanki.lp.grammar.LpGrammarBaseVisitor;
import sk.shanki.lp.grammar.LpGrammarParser;
import sk.shanki.lp.grammar.LpGrammarParser.TermContext;
import sk.shanki.lp.Term;
import sk.shanki.lp.NafLiteral;
import sk.shanki.lp.Literal;

/**
 *
 * @author shanki
 */
public class LpParseVisitor extends LpGrammarBaseVisitor<Object> {
    
    private Rules rules;
    private NamePreferences preferences;
    private Constraints constraints;
    private WeakConstraints weaks;
    
    
    public Program transform(ParseTree pt) throws MultipleRulesWithSameNameException {
        rules       = new Rules();
        preferences = new NamePreferences();
        constraints = new Constraints();
        weaks        = new WeakConstraints();
        
        visit(pt);
    
        UniqueRuleNameMap ruleMap = new UniqueRuleNameMap(rules);
        
        return new Program(rules, constraints, weaks, preferences.toPreferences(ruleMap));
    }

    @Override
    public Object visitProgram(LpGrammarParser.ProgramContext ctx) {
        
        preferences = new NamePreferences();
        
        
        for (LpGrammarParser.StatementContext context : ctx.statement()) {
            Object obj = visit(context);
            
            if (obj instanceof Rule) {
                rules.add((Rule)obj);
            } else if (obj instanceof NamePreference) {
                preferences.add((NamePreference)obj);
            } else if (obj instanceof Constraint) {
                constraints.add((Constraint)obj);
            } else if (obj instanceof WeakConstraint) {
                weaks.add((WeakConstraint)obj);
            } else {
                throw new RuntimeException("unsupported type of object");
            }
        }
        
        return null;
    }

    @Override
    public Rule visitLprule(LpGrammarParser.LpruleContext ctx) {
        
        boolean partial = ctx.partialannotation() != null;
        
        String name = "";
        if (ctx.nameannotation() != null) {
            name = (String)visit(ctx.nameannotation());
        }
        
        NafLiterals head = (NafLiterals)visit(ctx.head());
        
        NafLiterals body = new NafLiterals();
        if (ctx.body() != null) {
            body = (NafLiterals)visit(ctx.body());
        }
        
        return new Rule(partial, name, head, body);
    }

    @Override
    public NafLiterals visitBody(LpGrammarParser.BodyContext ctx) {
        ArrayList<NafLiteral> literals = new ArrayList<>();
        
        for(LpGrammarParser.NafliteralContext context : ctx.nafliteral()) {
            NafLiteral nafLiteral = (NafLiteral)visit(context);
            
            literals.add(nafLiteral);
        }
        
        return new NafLiterals(literals);
    }

    @Override
    public NafLiterals visitHead(LpGrammarParser.HeadContext ctx) {
        ArrayList<NafLiteral> literals = new ArrayList<>();
        
        for(LpGrammarParser.NafliteralContext context : ctx.nafliteral()) {
            NafLiteral nafLiteral = (NafLiteral)visit(context);
            
            literals.add(nafLiteral);
        }
        
        return new NafLiterals(literals);
    }

    @Override
    public Literal visitPositive_nafliteral(LpGrammarParser.Positive_nafliteralContext ctx) {
        return (Literal)visit(ctx.literal());
    }

    @Override
    public Not visitNegative_nafliteral(LpGrammarParser.Negative_nafliteralContext ctx) {
        return new Not((Literal)visit(ctx.literal()));
    }

    @Override
    public Atom visitPositive_literal(LpGrammarParser.Positive_literalContext ctx) {
        return (Atom)visit(ctx.atom());
    }

    @Override
    public Neg visitNegative_literal(LpGrammarParser.Negative_literalContext ctx) {
        return new Neg((Atom)visit(ctx.atom()));
    }
    
    @Override
    public Atom visitAtom(LpGrammarParser.AtomContext ctx) {
        String predicateSymbol = ctx.predicateSymbol.getText();
        Terms terms = new Terms();
        
        if (ctx.terms() != null) {
            terms = (Terms)visit(ctx.terms());
        }
        
        return new Atom(predicateSymbol, terms);
    }

    @Override
    public Terms visitTerms(LpGrammarParser.TermsContext ctx) {
        ArrayList<Term> terms = new ArrayList<>();

        for (TermContext context : ctx.term()) {
            Term term = (Term)visit(context);
            terms.add(term);
        }

        return new Terms(terms);
    }

    @Override
    public NumberConstant visitNumber_term(LpGrammarParser.Number_termContext ctx) {
        BigDecimal decimal = new BigDecimal(ctx.NUMBER().getText());
        return new NumberConstant(decimal);
    }

    @Override
    public StringConstant visitString_term(LpGrammarParser.String_termContext ctx) {
        String str = ctx.STRING().getText();
        
        str = str.substring(1, str.length()-1); //TODO: transformovat escape sekvencie
        return new StringConstant(str);
    }

    @Override
    public Variable visitVariable_term(LpGrammarParser.Variable_termContext ctx) {
        return new Variable(ctx.variable.getText());
    }

    @Override
    public FunctionalTerm visitFunctional_term(LpGrammarParser.Functional_termContext ctx) {
        String functionSymbol = ctx.functionSymbol.getText();
        Terms terms = new Terms();
        
        if (ctx.terms() != null) {
            terms = (Terms)visit(ctx.terms());
        }
        
        return new FunctionalTerm(functionSymbol, terms);
    }

    @Override
    public String visitNameannotation(LpGrammarParser.NameannotationContext ctx) {
        return ctx.name.getText();
    }    
    

    @Override
    public NamePreference visitPreference(LpGrammarParser.PreferenceContext ctx) {
        return new NamePreference(ctx.r1.getText(), ctx.r2.getText());
    }

    @Override
    public Constraint visitConstraint(LpGrammarParser.ConstraintContext ctx) {
        
        NafLiterals body = new NafLiterals();
        
        if (ctx.body() != null) {
            body = (NafLiterals)visit(ctx.body());
        }
        
        return new Constraint(body);
    }

    @Override
    public WeakConstraint visitWeak_constraint(LpGrammarParser.Weak_constraintContext ctx) {
        NafLiterals body = new NafLiterals();
        
        if (ctx.body() != null) {
            body = (NafLiterals)visit(ctx.body());
        }
        
        BigDecimal weight   = new BigDecimal(ctx.weight.getText());
        BigDecimal level    = new BigDecimal(ctx.weight.getText());
        Terms terms         = new Terms();
        
        if (ctx.terms() != null) {
            terms = (Terms)visit(ctx.terms());
        }
        
        return new WeakConstraint(body, weight, level, terms);
    }

}