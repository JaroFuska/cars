/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

/**
 *
 * @author shanki
 */
public abstract class BaseConstant extends BaseTerm {

    @Override
    public boolean unify(Term other, Substitution substitution) {
		return this.equals(other);
    }

    @Override
    public boolean isGround() {
        return true;
    }

    @Override
    public Term fullySubstitute(Substitution substitution) {
        return this;
    }

    @Override
    public Term partiallySubstitute(Substitution substitution) {
        return this;
    }

    @Override
    public boolean willChange(Substitution substitution) {
        return false;
    }
    
    @Override
    public Term rewriteIdsToObjectConstants(ObjectConstantMapping mapping) {
        return this;
    }

    @Override
    public Term rewriteObjectConstantsToIds(ObjectConstantMapping mapping) {
        return this;
    }

}