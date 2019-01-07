/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.solvers;

import sk.shanki.lp.exceptions.SolverException;
import sk.shanki.lp.AnswerSets;
import sk.shanki.lp.Program;

/**
 *
 * @author shanki
 */
public class NonGroundSolver implements Solver {
    
    private final Solver groundSolver;

    public NonGroundSolver(Solver groundSolver) {
        this.groundSolver   = groundSolver;
    }
    
    @Override
    public AnswerSets evaluate(Program program, int maxAnswerSets) throws SolverException {
        return groundSolver.evaluate(program.ground(), maxAnswerSets);
    }

    @Override
    public String debug(Program program) {
        return program.toString();
    }

}