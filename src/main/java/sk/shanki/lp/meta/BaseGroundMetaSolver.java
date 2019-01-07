/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.meta;

import sk.shanki.lp.AnswerSets;
import sk.shanki.lp.Program;
import sk.shanki.lp.solvers.Solver;
import sk.shanki.lp.exceptions.SolverException;

/**
 *
 * @author shanki
 */
public class BaseGroundMetaSolver extends BaseMetaSolver {
    
    public BaseGroundMetaSolver(Program metaInterpreter, Solver solver)  {
        super(metaInterpreter, solver);
    }
   
    @Override
    public AnswerSets evaluate(Program program, int maxAnswerSets) throws SolverException {
        return evaluateMeta(program.toMetaProgram(), maxAnswerSets);
    }

    @Override
    public String debug(Program program) {
        return debugMeta(program.toMetaProgram());
    }
        
}