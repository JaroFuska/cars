/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.shanki.lp.integration.prefprograms;

import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import sk.shanki.lp.AnswerSet;
import sk.shanki.lp.AnswerSets;
import sk.shanki.lp.Atom;
import sk.shanki.lp.Neg;
import sk.shanki.lp.Program;
import sk.shanki.lp.solvers.DisjunctiveTransSolver;
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
public class ProgramSimpleCycleTest {
    
    private static Program program;
    private static Solver solver;
    
    public ProgramSimpleCycleTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws SolverException, IOException {
        program = new ProgramFactory().fromString(
                "@name(r1) a :- not -a." +
                "@name(r2) -a :- not a." +
                "r2 < r1."
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

    public void testAS() throws SolverException {
        AnswerSet as1   = new AnswerSet();
        as1.add(new Atom("a"));
        
        AnswerSet as2   = new AnswerSet();
        as2.add(new Neg(new Atom("a")));
        
        AnswerSets expected = new AnswerSets();
        expected.add(as1);
        expected.add(as2);
        
        AnswerSets result   = solver.evaluate(program, 0);
        
        assertEquals(expected, result);
    }

    public void testPAS_BE() throws SolverException {
        AnswerSet as1   = new AnswerSet();
        as1.add(new Atom("a"));
        
        AnswerSets expected = new AnswerSets();
        expected.add(as1);
        
        AnswerSets result   = new NonGroundSolver(new PasBEMetaSolver(solver)).evaluate(program, 0);
        
        assertEquals(expected, result);
    }

    public void testPAS_BEW() throws SolverException {
        AnswerSet as1   = new AnswerSet();
        as1.add(new Atom("a"));
        
        AnswerSets expected = new AnswerSets();
        expected.add(as1);
        
        AnswerSets result   = new NonGroundSolver(new PasBEWMetaSolver(new NonGroundSolver(new DisjunctiveTransSolver(solver)))).evaluate(program, 0);
        
        assertEquals(expected, result);
    }    

    public void testPAS_INDIRECT_TRANS() throws SolverException {
        AnswerSet as1   = new AnswerSet();
        as1.add(new Atom("a"));
        
        AnswerSets expected = new AnswerSets();
        expected.add(as1);
        
        AnswerSets result   = new NonGroundSolver(new PasGTransSolver(solver,new DisjunctiveTransSolver(solver))).evaluate(program, 0);
        
        assertEquals(expected, result);
    }    

}