/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import sk.shanki.lp.AnswerSet;
import sk.shanki.lp.AnswerSets;
import sk.shanki.lp.Atom;
import sk.shanki.lp.FunctionalTerm;
import sk.shanki.lp.Neg;
import sk.shanki.lp.NumberConstant;
import sk.shanki.lp.StringConstant;
import sk.shanki.lp.Terms;
import sk.shanki.lp.grammar.DlvOutGrammarBaseVisitor;
import sk.shanki.lp.grammar.DlvOutGrammarParser;
import sk.shanki.lp.Term;
import sk.shanki.lp.Literal;

/**
 *
 * @author shanki
 */
public class DlvOutParseVisitor extends DlvOutGrammarBaseVisitor<Object> {

    @Override
    public AnswerSets visitAnswer_sets(DlvOutGrammarParser.Answer_setsContext ctx) {
        AnswerSets ass = new AnswerSets();
        
        for (DlvOutGrammarParser.AsContext context : ctx.as()) {
            ass.add((AnswerSet)visit(context));
        }
        
        return ass;

    }

    @Override
    public AnswerSet visitOptimal_answer_set(DlvOutGrammarParser.Optimal_answer_setContext ctx) {
        return (AnswerSet)visit(ctx.answer_set());
    }

    @Override
    public AnswerSet visitAnswer_set(DlvOutGrammarParser.Answer_setContext ctx) {
        AnswerSet as = new AnswerSet();
        
        for (DlvOutGrammarParser.LiteralContext context : ctx.literal()) {
            as.add((Literal)visit(context));
        }
        
        return as;
    }

    @Override
    public Atom visitPositive_literal(DlvOutGrammarParser.Positive_literalContext ctx) {
        return (Atom)visit(ctx.atom());
    }

    @Override
    public Neg visitNegative_literal(DlvOutGrammarParser.Negative_literalContext ctx) {
        return new Neg((Atom)visit(ctx.atom()));
    }

    @Override
    public Atom visitAtom(DlvOutGrammarParser.AtomContext ctx) {
        String predicateSymbol = ctx.predicateSymbol.getText();
        Terms terms = new Terms();
        
        if (ctx.terms() != null) {
            terms = (Terms)visit(ctx.terms());
        }
        
        return new Atom(predicateSymbol, terms);
    }

    @Override
    public Terms visitTerms(DlvOutGrammarParser.TermsContext ctx) {
        ArrayList<Term> terms = new ArrayList<>();

        for (DlvOutGrammarParser.TermContext context : ctx.term()) {
            Term term = (Term)visit(context);
            terms.add(term);
        }

        return new Terms(terms);
    }

    @Override
    public NumberConstant visitNumber_term(DlvOutGrammarParser.Number_termContext ctx) {
        BigDecimal decimal = new BigDecimal(ctx.NUMBER().getText());
        return new NumberConstant(decimal);
    }

    @Override
    public StringConstant visitString_term(DlvOutGrammarParser.String_termContext ctx) {
        String str = ctx.STRING().getText();

        str = str.substring(1, str.length()-1); //TODO: transformovat escape sekvencie
        return new StringConstant(str);
    }

    @Override
    public FunctionalTerm visitFunctional_term(DlvOutGrammarParser.Functional_termContext ctx) {
        String functionSymbol = ctx.functionSymbol.getText();
        Terms terms = new Terms();
        
        if (ctx.terms() != null) {
            terms = (Terms)visit(ctx.terms());
        }
        
        return new FunctionalTerm(functionSymbol, terms);
    }

}