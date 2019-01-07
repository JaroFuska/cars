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
import sk.shanki.lp.grammar.ClingoOutGrammarBaseVisitor;
import sk.shanki.lp.grammar.ClingoOutGrammarParser;
import sk.shanki.lp.Term;
import sk.shanki.lp.Literal;

/**
 *
 * @author shanki
 */
public class ClingoOutParseVisitor extends ClingoOutGrammarBaseVisitor<Object> {

    @Override
    public AnswerSets visitOut(ClingoOutGrammarParser.OutContext ctx) {
        
        boolean isSat = (Boolean)visit(ctx.sat());
        
        if (isSat) {
            return (AnswerSets)visit(ctx.answer_sets());
        } else {
            return null;
        }
    }

    @Override
    public AnswerSets visitAnswer_sets(ClingoOutGrammarParser.Answer_setsContext ctx) {
        AnswerSets ass = new AnswerSets();
        
        for (ClingoOutGrammarParser.Answer_setContext context : ctx.answer_set()) {
            ass.add((AnswerSet)visit(context));
        }
        
        return ass;
    }

    @Override
    public AnswerSet visitAnswer_set(ClingoOutGrammarParser.Answer_setContext ctx) {
        AnswerSet as = new AnswerSet();
        
        for (ClingoOutGrammarParser.LiteralContext context : ctx.literal()) {
            as.add((Literal)visit(context));
        }
        
        return as;
    }

    @Override
    public Atom visitPositive_literal(ClingoOutGrammarParser.Positive_literalContext ctx) {
        return (Atom)visit(ctx.atom());
    }

    @Override
    public Neg visitNegative_literal(ClingoOutGrammarParser.Negative_literalContext ctx) {
        return new Neg((Atom)visit(ctx.atom()));
    }

    @Override
    public Atom visitAtom(ClingoOutGrammarParser.AtomContext ctx) {
        String predicateSymbol = ctx.predicateSymbol.getText();
        Terms terms = new Terms();
        
        if (ctx.terms() != null) {
            terms = (Terms)visit(ctx.terms());
        }
        
        return new Atom(predicateSymbol, terms);
    }

    @Override
    public Terms visitTerms(ClingoOutGrammarParser.TermsContext ctx) {
        ArrayList<Term> terms = new ArrayList<>();

        for (ClingoOutGrammarParser.TermContext context : ctx.term()) {
            Term term = (Term)visit(context);
            terms.add(term);
        }

        return new Terms(terms);
    }

    @Override
    public NumberConstant visitNumber_term(ClingoOutGrammarParser.Number_termContext ctx) {
        BigDecimal decimal = new BigDecimal(ctx.NUMBER().getText());
        return new NumberConstant(decimal);
    }

    @Override
    public StringConstant visitString_term(ClingoOutGrammarParser.String_termContext ctx) {
        String str = ctx.STRING().getText();

        str = str.substring(1, str.length()-1); //TODO: transformovat escape sekvencie
        return new StringConstant(str);
    }

    @Override
    public FunctionalTerm visitFunctional_term(ClingoOutGrammarParser.Functional_termContext ctx) {
        String functionSymbol = ctx.functionSymbol.getText();
        Terms terms = new Terms();
        
        if (ctx.terms() != null) {
            terms = (Terms)visit(ctx.terms());
        }
        
        return new FunctionalTerm(functionSymbol, terms);
    }

    @Override
    public Boolean visitSat_sat(ClingoOutGrammarParser.Sat_satContext ctx) {
        return true;
    }

    @Override
    public Boolean visitUnsat_sat(ClingoOutGrammarParser.Unsat_satContext ctx) {
        return false;
    }
    
}