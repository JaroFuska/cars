/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.solvers;

import sk.shanki.lp.exceptions.SolverException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import sk.shanki.lp.AnswerSets;
import sk.shanki.lp.ObjectConstantMapping;
import sk.shanki.lp.Program;
import sk.shanki.lp.parser.DlvOutFactory;
import sk.shanki.lp.printing.DlvProgramPrinter;

/**
 *
 * @author shanki
 */
public class DlvSolver implements Solver {
    
    @Override
    public AnswerSets evaluate(Program program, int maxAnswerSets) throws SolverException {
        if (program.hasWeakConstraints()) {
            throw new SolverException("dlv does not support ASP-CORE-2 weak constraint semantics");
        }
        
        ObjectConstantMapping mapping      = new ObjectConstantMapping();
        Program rewritten           = program.rewriteElementsToIds(mapping);
        DlvProgramPrinter printer   = new DlvProgramPrinter();
        String raw                  = rewritten.print(printer).toString();
        
        AnswerSets ass              = evaluateRaw(raw, maxAnswerSets);
        
        return ass.rewriteIdsToObjects(mapping);
    }
    
    public AnswerSets evaluateRaw(String program, int maxAnswerSets) {
        try {
            String out              = run(program, maxAnswerSets);
            DlvOutFactory factory   = new DlvOutFactory();
            
            return factory.fromString(out);
        } catch (IOException ex) {
            throw new RuntimeException("this should not have happen", ex);
        }        
    }

    public String run(String program, int maxAnswerSets) throws IOException {
        String number = getNumber(maxAnswerSets);
        Process p = Runtime.getRuntime().exec("dlv " + number + " -silent --");
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
    
    private String getNumber(int maxAnswerSets) {
        return "-n=" + maxAnswerSets;
    }

    @Override
    public String debug(Program program) {
        return program.toString();
    }

    public boolean hasAnswerSetGround(Program program) throws SolverException {
        return evaluate(program, 1).isEmpty() == false;
    }

}