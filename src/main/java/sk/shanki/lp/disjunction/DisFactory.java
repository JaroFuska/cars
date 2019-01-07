/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.disjunction;

import java.util.List;
import sk.shanki.lp.Rule;
import sk.shanki.lp.compilation.CompilationFactory;
import sk.shanki.lp.compilation.CompiledLiteralCache;
import sk.shanki.lp.Literal;

/**
 *
 * @author shanki
 */
public class DisFactory implements CompilationFactory<DisProgram, DisRule, DisLiteral, DisLiterals> {

    @Override
    public DisProgram createProgram(CompiledLiteralCache cache) {
        return new DisProgram(cache);
    }

    @Override
    public DisRule createRule(Rule aThis, DisLiterals head, DisLiterals posBody, DisLiterals negBody) {
        return new DisRule(aThis, head, posBody, negBody);
    }

    @Override
    public DisLiterals createLiterals(List<DisLiteral> compiled) {
        return new DisLiterals(compiled);
    }

    @Override
    public DisLiteral createLiteral(Literal literal) {
        return new DisLiteral(literal);
    }

}