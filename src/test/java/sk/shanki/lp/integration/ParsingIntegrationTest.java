/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.integration;

import sk.shanki.lp.parser.ProgramFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author shanki
 */
public class ParsingIntegrationTest {
    
    private  ProgramFactory instance;
    
    @Before
    public void setUp() {
        instance = new ProgramFactory();
    }
    
    @After
    public void tearDown() {
        instance = null;
    }

    @Test
    public void testEmptyProgram() throws Exception {
        String program  = "";
        String expected = "";
        String result   = instance.fromString(program).toString();
        assertEquals(expected, result);
    }
    
    @Test
    public void testOnePropositionalRule() throws Exception {
        String program  = "a :- x,not b.";
        String expected = "a :- x, not b.\n";
        String result   = instance.fromString(program).toString();
        assertEquals(expected, result);
    }
    
    @Test
    public void testMorePropositionalRules() throws Exception {
        String program  = "a :- x,not b.\n b :- y,not a.";
        String expected = "a :- x, not b.\nb :- y, not a.\n";
        String result   = instance.fromString(program).toString();
        assertEquals(expected, result);
    }
    
    @Test
    public void testRuleWithName() throws Exception {
        String program  = "@name(r1) a :- x,not b.";
        String expected = "@name(r1) a :- x, not b.\n";
        String result   = instance.fromString(program).toString();
        assertEquals(expected, result);
    }
    
    @Test
    public void testNegatedFact() throws Exception {
        String program  = "-a.";
        String expected = "-a.\n";
        String result   = instance.fromString(program).toString();
        assertEquals(expected, result);
    }
    
    @Test
    public void testFunctionalSymbols() throws Exception {
        String program  = "p(u(a)).";
        String expected = "p(u(a)).\n";
        String result   = instance.fromString(program).toString();
        assertEquals(expected, result);
    }
    
    @Test
    public void testFunctionalSymbols2() throws Exception {
        String program  = "p(u(a, b)).";
        String expected = "p(u(a, b)).\n";
        String result   = instance.fromString(program).toString();
        assertEquals(expected, result);
    }
    
    @Test
    public void testFunctionalSymbols3() throws Exception {
        String program  = "p(u(t(a))).";
        String expected = "p(u(t(a))).\n";
        String result   = instance.fromString(program).toString();
        assertEquals(expected, result);
    }       
    
}
