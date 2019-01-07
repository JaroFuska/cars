/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.splitting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import sk.shanki.lp.Literal;

/**
 *
 * @author shanki
 */
public class Component implements Iterable<Component> {
    
    private Set<SplittingNode> nodes    = new HashSet<>();
    private final Set<Component> next   = new HashSet<>();
    private boolean hasIncomming        = false;
    
    private int level;    

    void setNodes(Set<SplittingNode> nodes) {
        this.nodes = nodes;
    }
    
    Iterable<SplittingNode> nodes() {
        return nodes;
    }

    @Override
    public Iterator<Component> iterator() {
        return next.iterator();
    }

    void addNext(Component component) {
        next.add(component);
        component.makeHasIncomming();
    }

    private void makeHasIncomming() {
        hasIncomming = true;
    }

    boolean hasIncomming() {
        return hasIncomming;
    }

    boolean hasOutgoing() {
        return next.isEmpty() == false;
    }
    
    int getLevel() {
        return level;
    }

    void setLevel(int level) {
        this.level = level;
    }    

    Collection<Literal> literals() {
        List<Literal> literals = new ArrayList<>();
        
        for (SplittingNode node : nodes) {
            literals.add(node.literal());
        }
        
        return literals;
    }
    
}