/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.solvers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import sk.shanki.lp.AnswerSets;
import sk.shanki.lp.ObjectConstantMapping;
import sk.shanki.lp.Program;
import sk.shanki.lp.exceptions.SolverException;
import sk.shanki.lp.parser.ClingoOutFactory;
import sk.shanki.lp.printing.StandardProgramPrinter;

/**
 *
 * @author shanki
 */
public class ClingoSolver implements Solver { 
    
    @Override
    public AnswerSets evaluate(Program program, int maxAnswerSets) throws SolverException {
//        if (program.hasWeakConstraints()) {
//            throw new SolverException("clingo does not support weak constraints");
//        }
        
        ObjectConstantMapping mapping      = new ObjectConstantMapping();
        Program rewritten           = program.rewriteElementsToIds(mapping);
        StandardProgramPrinter printer      = new StandardProgramPrinter();
        String raw                  = rewritten.print(printer).toString();
        
        AnswerSets ass              = evaluateRaw(raw, maxAnswerSets);
        
        return ass.rewriteIdsToObjects(mapping);
    }
    
    @Override
    public String debug(Program program) {
        return program.toString();
    }
    
    public AnswerSets evaluateRaw(String program, int maxAnswerSets) {
        try {
            String out                  = run(program, maxAnswerSets);
            ClingoOutFactory factory    = new ClingoOutFactory();
            
            return factory.fromString(out);
        } catch (IOException ex) {
            throw new RuntimeException("this should not have happen", ex);
        }        
    }

    public String run(String program, int maxAnswerSets) throws IOException {
        Process p = Runtime.getRuntime().exec("C:\\bin/clingo --verbose=0 " + maxAnswerSets);
        try (BufferedWriter w = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()))) {
            w.append(program);
        }
        
        StringBuilder output;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            output = new StringBuilder();
            String line;
            while ((line = reader.readLine())!= null) {
                output.append(line).append("\n");
            }
        }
        
        return output.toString();
    }
    
}