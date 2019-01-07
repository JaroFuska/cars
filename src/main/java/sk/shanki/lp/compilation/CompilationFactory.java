package sk.shanki.lp.compilation;

/*
 * Copyright shanki. All rights reserved.
 */


import java.util.List;
import sk.shanki.lp.Rule;

/**
 *
 * @author shanki
 */
public interface CompilationFactory<P,R,L,LS> extends CompiledLiteralFactory<L>{

    public P createProgram(CompiledLiteralCache cache);
    public R createRule(Rule aThis, LS head, LS posBody, LS negBody);
    public LS createLiterals(List<L> compiled);
    
}