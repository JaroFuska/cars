/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.gsolver;

import java.util.Set;
import sk.shanki.lp.AnswerSet;
import sk.shanki.lp.AnswerSetChecker;
import sk.shanki.lp.AnswerSets;
import sk.shanki.lp.Program;
import sk.shanki.lp.Rule;
import sk.shanki.lp.solvers.Solver;
import sk.shanki.lp.exceptions.SolverException;
import sk.shanki.lp.compilation.CompiledLiteralCache;

/**
 *
 * @author shanki
 */
public class PasGTransSolver implements Solver {
    
    private final Solver groundSolver;
    private final Solver disjunctiveGroundSolver;
    
    public PasGTransSolver(Solver groundSolver, Solver disjunctiveGroundSolver) {
        this.groundSolver               = groundSolver;
        this.disjunctiveGroundSolver    = disjunctiveGroundSolver;
    }

    @Override
    public AnswerSets evaluate(Program program, int maxAnswerSets) throws SolverException {
        AnswerSetChecker checker    = new Checker(program, disjunctiveGroundSolver);
        AnswerSets ass              = groundSolver.evaluate(program, maxAnswerSets);
        
        return ass.filter(checker);
    }

    @Override
    public String debug(Program program) {
        return program.toString();
    }
    
    private class Checker implements AnswerSetChecker {
        
        private final Program program;
        private final GProgram transformer;
        private final Solver disjunctiveGroundSolver;
        
        public Checker(Program program, Solver disjunctiveGroundSolver) {
            this.program                        = program;
            
            GFactory factory                    = new GFactory();
            CompiledLiteralCache<GLiteral> cache   = new CompiledLiteralCache<>(factory);
            
            this.transformer                    = program.compile(factory, cache);
            this.disjunctiveGroundSolver        = disjunctiveGroundSolver;
        }

        @Override
        public boolean isAnswerSetOk(AnswerSet answerSet) throws SolverException {
            Set<Rule> generatingRules   = program.generatingRules(answerSet);
            Program transformed         = transformer.transform(generatingRules);
            AnswerSets counterExamples  = disjunctiveGroundSolver.evaluate(transformed, 1);
        
            return counterExamples.isEmpty();
        }
        
    }

}