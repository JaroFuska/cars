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
public interface Solver {

    public AnswerSets evaluate(Program program, int maxAnswerSets) throws SolverException;
    public String debug(Program program);

}