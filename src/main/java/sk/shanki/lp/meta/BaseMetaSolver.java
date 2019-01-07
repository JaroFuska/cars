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
public abstract class BaseMetaSolver  implements Solver {
    
    private final Solver solver;
    private final Program metaInterpreter;
    
    public BaseMetaSolver(Program metaInterpreter, Solver solver) {
        this.metaInterpreter    = metaInterpreter;
        this.solver             = solver;
    } 
    
    protected AnswerSets evaluateMeta(Program metaProgram, int maxAnswerSets) throws SolverException {
        Program total       = new Program();
        total.add(metaInterpreter);
        total.add(metaProgram);

        AnswerSets ass              = solver.evaluate(total, maxAnswerSets);

        MetaFilter filter           = new MetaFilter();
        AnswerSets filtered         = filter.filterAss(ass);

        return filtered;
    }

    protected String debugMeta(Program metaProgram) {
        Program total       = new Program();
        
        total.add(metaInterpreter);
        total.add(metaProgram);        
        
        return total.toString();
    }

}