/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.solvers;

import java.util.Set;
import sk.shanki.lp.AnswerSet;
import sk.shanki.lp.AnswerSetChecker;
import sk.shanki.lp.AnswerSets;
import sk.shanki.lp.Program;
import sk.shanki.lp.exceptions.SolverException;
import sk.shanki.lp.compilation.CompiledLiteralCache;
import sk.shanki.lp.disjunction.DisFactory;
import sk.shanki.lp.disjunction.DisLiteral;
import sk.shanki.lp.disjunction.DisProgram;
import sk.shanki.lp.Literal;

/**
 *
 * @author shanki
 */
public class DisjunctiveTransSolver implements Solver {
    
    private final Solver groundSolver;
    
    public DisjunctiveTransSolver(Solver groundSolver) {
        this.groundSolver               = groundSolver;
    }

    @Override
    public AnswerSets evaluate(Program program, int maxAnswerSets) throws SolverException {        
        Program normalized                      = program.addNegConstraints().normalizeConstraints();
        
        DisFactory factory                      = new DisFactory();
        CompiledLiteralCache<DisLiteral> cache  = new CompiledLiteralCache<>(factory);
        DisProgram transformer                  = normalized.compile(factory, cache);
        AnswerSetChecker checker                = new Checker(transformer, groundSolver);
        
        Set<Literal> literals                  = program.collectAllLiterals();
        
        Program generator                       = transformer.generator();
        
        AnswerSets ass                          = groundSolver.evaluate(generator, 0);
        
        return ass.restrictToLiterals(literals).filter(checker).optimal(program.getWeakConstraints());
    }

    @Override
    public String debug(Program program) {
        return program.toString();
    }
    
    private class Checker implements AnswerSetChecker {

        private final DisProgram transformer;
        private final Solver groundSolver;
        
        public Checker(DisProgram transformer, Solver groundSolver) {
            this.transformer                    = transformer;
            this.groundSolver                   = groundSolver;
        }

        @Override
        public boolean isAnswerSetOk(AnswerSet answerSet) throws SolverException {
            Program transformed         = transformer.tester(answerSet);
            AnswerSets counterExamples  = groundSolver.evaluate(transformed, 1);
        
            return counterExamples.isEmpty();
        }
        
    }

}