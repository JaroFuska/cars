/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.compilation;

import java.util.ArrayList;
import java.util.List;
import sk.shanki.lp.Literal;

/**
 *
 * @author shanki
 */
public class CompiledLiteral<R extends CompiledRule> {

    protected final Literal literal;
    
    protected final List<R> inHead       = new ArrayList<>();
    
    protected final List<R> inPosBody    = new ArrayList<>();
    protected final List<R> inNegBody    = new ArrayList<>();
        
    // Constructors
    
    public CompiledLiteral(Literal literal) {
        this.literal = literal;
    }
    
    // main methods
    
    public void addInHead(R rule) {
        inHead.add(rule);
    }
    
    public Iterable<R> inHead() {
        return inHead;
    }    

    void addInPosBody(R rule) {
        inPosBody.add(rule);
    }

    void addInNegBody(R rule) {
        inNegBody.add(rule);
    }

}