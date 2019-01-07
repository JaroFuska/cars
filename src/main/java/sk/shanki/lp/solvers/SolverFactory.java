/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.solvers;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import sk.shanki.lp.exceptions.UnknownSolverException;
import sk.shanki.lp.meta.AsMetaSolver;
import sk.shanki.lp.meta.PasBEMetaSolver;
import sk.shanki.lp.meta.PasBEWMetaSolver;
import sk.shanki.lp.meta.PasDMetaSolver;
import sk.shanki.lp.meta.PasDSTMetaSolver;
import sk.shanki.lp.meta.PasCnfMetaSolver;
import sk.shanki.lp.meta.PasGMetaSolver;
import sk.shanki.lp.meta.PasNoMetaSolver;
import sk.shanki.lp.meta.PasWZLMetaSolver;

/**
 *
 * @author shanki
 */
public class SolverFactory {

    public SolverFactory() {
    }
    
    public Solver create(Collection<String> solverNames) throws UnknownSolverException {
        return createInner(new LinkedList<>(solverNames));
    }

    private Solver createInner(Queue<String> solverNames) throws UnknownSolverException {
        
        if (solverNames.isEmpty()) {
            throw new IllegalArgumentException("Not enought solver names.");
        }
        
        String solverName = solverNames.remove();
        
        switch (solverName) {
            case "dlv":
                return new NonGroundSolver(new DlvSolver());
            case "clingo":
                return new NonGroundSolver(new ClingoSolver());
            case "builtin":
                return new NonGroundSolver(new BuiltInSmodelsSolver());
            case "disj":
                return new NonGroundSolver(new DisjunctiveTransSolver(create(solverNames)));
            case "mas":
                return new NonGroundSolver(new AsMetaSolver(create(solverNames)));
            case "mbe":
                return new NonGroundSolver(new PasBEMetaSolver(create(solverNames)));
            case "mbew":
                return new NonGroundSolver(new PasBEWMetaSolver(create(solverNames)));
            case "mdst":
                return new NonGroundSolver(new PasDSTMetaSolver(create(solverNames)));
            case "mwzl":
                return new NonGroundSolver(new PasWZLMetaSolver(create(solverNames)));
            case "md":
                return new NonGroundSolver(new PasDMetaSolver(create(solverNames)));
            case "mg":
                return new NonGroundSolver(new PasGMetaSolver(create(solverNames)));
            case "mno":
                return new NonGroundSolver(new PasNoMetaSolver(create(solverNames)));
            case "mcnf":
                return new NonGroundSolver(new PasCnfMetaSolver(create(solverNames)));
            default:
                throw new UnknownSolverException(solverName);
        }
    }

}