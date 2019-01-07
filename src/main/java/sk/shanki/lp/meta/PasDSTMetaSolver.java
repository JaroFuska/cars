/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.meta;

import java.io.IOException;
import sk.shanki.lp.Program;
import sk.shanki.lp.parser.ProgramFactory;
import sk.shanki.lp.solvers.Solver;
import sk.shanki.lp.exceptions.SemanticException;

/**
 *
 * @author shanki
 */
public class PasDSTMetaSolver extends BaseGroundMetaSolver {
    
    public PasDSTMetaSolver(Solver solver) {
        super(PasDSTMetaInterpreter, solver);
    }

    private static final Program PasDSTMetaInterpreter;
    private static final String raw = "" +
        "lit(L) :- head(L,R)." +
        "lit(L) :- pbl(L,R)." +
        "lit(L) :- nbl(L,R)." +
        "in_S(L) :- lit(L), not notin_S(L)." +
        "notin_S(L) :- lit(L), not in_S(L)." +
        ":- compl(X,Y), in_S(X), in_S(Y)." +
        "pr(X,Z) :- pr(X,Y), pr(Y,Z)." +
        ":- pr(X,X)." +
        "stage(S) :- rule(S)." +
        "pos_body_false_S(R) :- rule(R), pbl(X,R), not in_S(X)." +
        "pos_body_false_Si(R,Si) :- pbl(L,R), stage(Si), not in_Si(L,Si)." +
        "pos_body_false_S0(R) :- pbl(L,R)." +
        "neg_body_false_S(R) :- rule(R), nbl(X,R), in_S(X)." +
        "neg_body_false_Si(R,Si) :- nbl(L,R), in_Si(L,Si)." +
        "active(R,Si) :- rule(R), stage(Si), not pos_body_false_Si(R,Si), not neg_body_false_S(R)." +
        "active_Si(R,Si) :- rule(R), stage(Si), not pos_body_false_S(R), not neg_body_false_Si(R,Si)." +
        "active_S0(R) :- rule(R), not pos_body_false_S0(R), not neg_body_false_S(R)." +
        "rule_not_generating_in_Si(R,Si) :- stage(Si), head(H,R), not in_rule_Si(H,R,Si)." +
        "preferred_generating_rule_exists(R,Si) :- pr(R1,R), active_Si(R1,Si), rule_not_generating_in_Si(R1,Si)." +
        "preferred_generating_rule_exists_S0(R) :- pr(R1,R), not pos_body_false_S(R1)." +
        "in_rule_Si(H,R,Si) :- head(H,R), active(R,Sj), stage(Sj), stage(Si), more(Si, Sj), not preferred_generating_rule_exists(R,Sj)." +
        "in_rule_Si(H,R,Si) :- head(H,R), active_S0(R), stage(Si), not preferred_generating_rule_exists_S0(R)." +
        "in_Si(H,Si) :- in_rule_Si(H,R,Si)." +
        "in_sem(L) :- in_Si(L,S)." + 
        ":- in_sem(L), not in_S(L)." +
        ":- in_S(L), not in_sem(L).";
    
    static {
        try {
            PasDSTMetaInterpreter = new ProgramFactory().fromString(raw);
        } catch (SemanticException | IOException ex) {
            throw new RuntimeException("Error.", ex);
        }
    }

}