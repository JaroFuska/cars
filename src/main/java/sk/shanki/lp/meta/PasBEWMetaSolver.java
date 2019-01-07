/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.meta;

import java.io.IOException;
import sk.shanki.lp.Program;
import sk.shanki.lp.exceptions.SemanticException;
import sk.shanki.lp.parser.ProgramFactory;
import sk.shanki.lp.solvers.Solver;

/**
 *
 * @author shanki
 */
public class PasBEWMetaSolver extends BaseGroundMetaSolver {
        
    public PasBEWMetaSolver(Solver solver) {
        super(PasBEWMetaInterpreter, solver);
    }
    
    private static final Program PasBEWMetaInterpreter;
    private static final String raw = "" +
        "in_AS(X) :- head(X,R), pos_body_true(R), not neg_body_false(R)." + 
        "pos_body_exists(R) :- pbl(X,R)." +
        "pos_body_true(R) :- rule(R), not pos_body_exists(R)." + 
        "pbl_inbetween(X,Y,R) :- pbl(X,R), pbl(Y,R), pbl(Z,R), less(X, Z), less(Z, Y)." +
        "pbl_notlast(X,R) :- pbl(X,R), pbl(Y,R), less(X, Y)." +
        "pbl_notfirst(X,R) :- pbl(X,R), pbl(Y,R), less(Y, X)." + 
        "pos_body_true_upto(R,X) :- pbl(X,R), not pbl_notfirst(X,R), in_AS(X)." +
        "pos_body_true_upto(R,X) :- pos_body_true_upto(R,Y), pbl(X,R)," +
        "   in_AS(X), less(Y, X), not pbl_inbetween(Y,X,R)." +
        "pos_body_true(R) :- pos_body_true_upto(R,X), not pbl_notlast(X,R)." +
        "neg_body_false(R) :- nbl(X,R), in_AS(X)." +
        ":- compl(X,Y), in_AS(X), in_AS(Y)." +
        "pr(X,Y) v pr(Y,X) :- rule(X),rule(Y), neq(X, Y)." +
        "pr(X,Z) :- pr(X,Y), pr(Y,Z)." +
        ":- pr(X,X)." +
        "pr1(X,Y) v pr1(Y,X) :- rule(X),rule(Y), neq(X, Y)." +
        "pr1(X,Z) :- pr1(X,Y), pr1(Y,Z)." +
        ":- pr1(X,X)." +
        ":~ rule(X), rule(Y), pr(X,Y), pr1(Y,X). [1@1, X,Y]" +
        "lit(X,Y) :- head(X,Y), pos_body_true(Y)," + 
        "   not defeat_local(Y), not in_AS(X)." +
        "   lit(X,Y) :- head(X,Y), pos_body_true(Y)," +
        "   not defeat_local(Y), not defeat_global(Y)." +
        "   defeat_local(Y)  :- nbl(X,Y), lit(X,Y1), pr1(Y1,Y)." +
        "   defeat_global(Y) :- nbl(X,Y), in_AS(X)." +
        "   in_sem(X) :- lit(X,Y)." +
        "   :- in_sem(X), not in_AS(X).";

    static {
        try {
            PasBEWMetaInterpreter = new ProgramFactory().fromString(raw);
        } catch (SemanticException | IOException ex) {
            throw new RuntimeException("This should not happen. There is a bug in a program.", ex);
        }
    }

}