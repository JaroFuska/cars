/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.integration;

import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import sk.shanki.lp.AnswerSets;
import sk.shanki.lp.Program;
import sk.shanki.lp.solvers.DisjunctiveTransSolver;
import sk.shanki.lp.exceptions.SemanticException;
import sk.shanki.lp.gsolver.PasGTransSolver;
import sk.shanki.lp.meta.PasBEMetaSolver;
import sk.shanki.lp.meta.PasBEWMetaSolver;
import sk.shanki.lp.parser.ProgramFactory;
import sk.shanki.lp.solvers.BuiltInSmodelsSolver;
import sk.shanki.lp.solvers.NonGroundSolver;
import sk.shanki.lp.solvers.Solver;
import sk.shanki.lp.exceptions.SolverException;

/**
 *
 * @author shanki
 */
public class ProgramInconsistentTest {
    
    private static Program program;    
    private static Solver solver;
    
    public ProgramInconsistentTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws SemanticException, IOException {
        program = new ProgramFactory().fromString(
                "a. -a."
        );
        
        solver = new NonGroundSolver(new BuiltInSmodelsSolver());
    }
    
    @AfterClass
    public static void tearDownClass() {
        program = null;
        solver = null;
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testAS() throws SolverException {
        AnswerSets expected = new AnswerSets();        
        AnswerSets result   = solver.evaluate(program, 0);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testPAS_BE() throws SolverException {
        AnswerSets expected = new AnswerSets();
        AnswerSets result   = new NonGroundSolver(new PasBEMetaSolver(solver)).evaluate(program, 0);
        
        assertEquals(expected, result);
    }     

    public void testPAS_BEW() throws SolverException {
        AnswerSets expected = new AnswerSets();
        AnswerSets result   = new NonGroundSolver(new PasBEWMetaSolver(new DisjunctiveTransSolver(solver))).evaluate(program, 0);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testPAS_INDIRECT_TRANS() throws SolverException {
        AnswerSets expected = new AnswerSets();
        AnswerSets result   = new NonGroundSolver(new PasGTransSolver(solver,solver)).evaluate(program, 0);
        
        assertEquals(expected, result);
    }      
}