/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.gsolver;

import sk.shanki.lp.compilation.CompiledLiteralCache;
import java.util.List;
import sk.shanki.lp.Rule;
import sk.shanki.lp.compilation.CompilationFactory;
import sk.shanki.lp.Literal;

/**
 *
 * @author shanki
 */
public class GFactory implements CompilationFactory<GProgram, GRule, GLiteral, GLiterals> {

    @Override
    public GProgram createProgram(CompiledLiteralCache cache) {
        return new GProgram(cache);
    }

    @Override
    public GRule createRule(Rule aThis, GLiterals head, GLiterals posBody, GLiterals negBody) {
        return new GRule(aThis, head, posBody, negBody);
    }

    @Override
    public GLiterals createLiterals(List<GLiteral> literals) {
        return new GLiterals(literals);
    }

    @Override
    public GLiteral createLiteral(Literal literal) {
        return new GLiteral(literal);
    }

}