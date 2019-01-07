/*
 * Copyright shanki. All rights reserved.
 */
package sk.shanki.lp.integration.disjunction;

import java.io.IOException;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import sk.shanki.lp.AnswerSet;
import sk.shanki.lp.AnswerSets;
import sk.shanki.lp.Atom;
import sk.shanki.lp.Neg;
import sk.shanki.lp.Program;
import sk.shanki.lp.solvers.DisjunctiveTransSolver;
import sk.shanki.lp.exceptions.SemanticException;
import sk.shanki.lp.exceptions.SolverException;
import sk.shanki.lp.parser.ProgramFactory;
import sk.shanki.lp.solvers.BuiltInSmodelsSolver;
import sk.shanki.lp.solvers.Solver;

/**
 *
 * @author shanki
 */
public class DisjunctiveGroundPrograms {
    
    private static Solver solver;
        
    @BeforeClass
    public static void setUpClass() throws SolverException, IOException {
        solver = new DisjunctiveTransSolver(new BuiltInSmodelsSolver());
    }
    
    @AfterClass
    public static void tearDownClass() {
        solver = null;
    }    
    
    @Test
    public void testProgram01() throws SolverException, SemanticException, IOException {
        Program program = new ProgramFactory().fromString(
                "a v b."
        );
        AnswerSet as1   = new AnswerSet();
        as1.add(new Atom("a"));

        AnswerSet as2   = new AnswerSet();
        as2.add(new Atom("b"));
        
        AnswerSets expected = new AnswerSets();
        expected.add(as1);
        expected.add(as2);
        
        AnswerSets result   = solver.evaluate(program, 0);
        
        assertEquals(expected, result);
    }    

    @Test
    public void testProgram02() throws SolverException, SemanticException, IOException {
        Program program = new ProgramFactory().fromString(
                "a v -a."
        );
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
    
    @Test
    public void testProgram03() throws SolverException, SemanticException, IOException {
        Program program = new ProgramFactory().fromString(
                "a v b. a:- b. b :- a."
        );
        AnswerSet as1   = new AnswerSet();
        as1.add(new Atom("a"));
        as1.add(new Atom("b"));
        
        AnswerSets expected = new AnswerSets();
        expected.add(as1);
        
        AnswerSets result   = solver.evaluate(program, 0);
        
        assertEquals(expected, result);
    }      
    
    @Test
    public void testProgram04() throws SolverException, SemanticException, IOException {
        Program program = new ProgramFactory().fromString(
                "p v pp." +
                "q v qq." +
                "not_sat :- p,q." +
                "not_sat :- pp,qq." +
                "q :- not_sat." +
                "qq :- not_sat."
        );
        AnswerSet as1   = new AnswerSet();
        as1.add(new Atom("p"));
        as1.add(new Atom("qq"));

        AnswerSet as2   = new AnswerSet();
        as2.add(new Atom("pp"));
        as2.add(new Atom("q"));
        
        AnswerSets expected = new AnswerSets();
        expected.add(as1);
        expected.add(as2);
        
        AnswerSets result   = solver.evaluate(program, 0);
        
        assertEquals(expected, result);
    }    
    
    @Test
    public void testProgram05() throws SolverException, SemanticException, IOException {
        Program program = new ProgramFactory().fromString(
                "p v pp." +
                "q v qq." +
                "not_sat :- p,q." +
                "not_sat :- pp,qq." +
                "q :- not_sat." +
                "qq :- not_sat." +
                "sat :- not not_sat."
        );
        AnswerSet as1   = new AnswerSet();
        as1.add(new Atom("p"));
        as1.add(new Atom("qq"));
        as1.add(new Atom("sat"));

        AnswerSet as2   = new AnswerSet();
        as2.add(new Atom("pp"));
        as2.add(new Atom("q"));
        as2.add(new Atom("sat"));
        
        AnswerSets expected = new AnswerSets();
        expected.add(as1);
        expected.add(as2);
        
        AnswerSets result   = solver.evaluate(program, 0);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testProgram06() throws SolverException, SemanticException, IOException {
        Program program = new ProgramFactory().fromString(
                "a v b." +
                "a v c." +
                ":- a, not b, not c." +
                ":- not a, b, c."
        );
        
        AnswerSets expected = new AnswerSets();
        
        AnswerSets result   = solver.evaluate(program, 0);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testProgram07() throws SolverException, SemanticException, IOException {
        Program program = new ProgramFactory().fromString(
                "a v b. :~ a. [1@1]"
        );
        
        AnswerSet as2   = new AnswerSet();
        as2.add(new Atom("b"));
        
        AnswerSets expected = new AnswerSets();
        expected.add(as2);
        
        AnswerSets result   = solver.evaluate(program, 0);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testProgram09() throws SolverException, SemanticException, IOException {
        Program program = new ProgramFactory().fromString(
                "a v b :- not c."
        );

        AnswerSet as1   = new AnswerSet();
        as1.add(new Atom("a"));
        
        AnswerSet as2   = new AnswerSet();
        as2.add(new Atom("b"));
        
        AnswerSets expected = new AnswerSets();
        expected.add(as1);
        expected.add(as2);
        
        AnswerSets result   = solver.evaluate(program, 0);
        
        assertEquals(expected, result);
    }

}