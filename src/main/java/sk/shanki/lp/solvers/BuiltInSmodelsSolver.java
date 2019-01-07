/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.solvers;

import sk.shanki.lp.AnswerSets;
import sk.shanki.lp.Program;

/**
 *
 * @author shanki
 */
public class BuiltInSmodelsSolver implements Solver {

    @Override
    public AnswerSets evaluate(Program program, int maxAnswerSets) {
        if (program.hasDisjunction()) {
            throw new IllegalArgumentException("Disjunction is not supported");
        }

        // TODO: signature
        // TODO: je toto volanie na dobrom mieste. nedat to rovno do toSmodelsProgram() ?
        Program normalized = program.addNegConstraints().normalizeConstraints();

        return normalized.toSmodelsProgram().evaluate(maxAnswerSets);
    }

    @Override
    public String debug(Program program) {
        return program.toString();
    }

}