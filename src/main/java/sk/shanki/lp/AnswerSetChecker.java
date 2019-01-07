/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

import sk.shanki.lp.exceptions.SolverException;

/**
 *
 * @author shanki
 */
public interface AnswerSetChecker {
    public boolean isAnswerSetOk(AnswerSet answerSet) throws SolverException;
}
