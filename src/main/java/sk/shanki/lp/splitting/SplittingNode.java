/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.splitting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import sk.shanki.lp.Literal;

/**
 *
 * @author shanki
 */
public class SplittingNode implements Iterable<SplittingNode> {
    private final Literal literal;
    
    private boolean hasIncommingEdge        = false;
    private final List<SplittingNode> next  = new ArrayList<>();
    
    private Integer index = null;
    private Integer lowLink = null;
    private Component component = null;
    
    SplittingNode(Literal literal) {
        this.literal = literal;
    }

    void addNext(SplittingNode node) {
        next.add(node);
        node.markHasIncommingEdge();
    }
    
    boolean hasIncommingEdge() {
        return hasIncommingEdge;
    }    
    
    private void markHasIncommingEdge() {
        hasIncommingEdge = true;
    }
    
    boolean hasIndex() {
        return index != null;
    }
    
    void setIndex(int index) {
        this.index = index;
    }
    
    int getIndex() {
        return index;
    }    
    
    void setLowLink(int lowLink) {
        this.lowLink = lowLink;
    }    

    @Override
    public Iterator<SplittingNode> iterator() {
        return next.iterator();
    }
    
    int getLowLink() {
        return lowLink;
    }
    
    boolean isRootNode() {
        return Objects.equals(lowLink, index);
    }
    
    void setComponent(Component component) {
        if (component == null) {
            throw new IllegalArgumentException("component cannot be null");
        }
        
        this.component = component;
    }

    Component getComponent() {
        return component;
    }    

    Literal literal() {
        return literal;
    }

}