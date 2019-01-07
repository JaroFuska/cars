/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.splitting;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import sk.shanki.lp.Literal;

/**
 *
 * @author shanki
 */
public class SplittingGraph {
    private final Map<Literal, SplittingNode> nodes    = new HashMap<>();

    SplittingNode ensureNode(Literal literal) {
        SplittingNode node = nodes.get(literal);
        
        if (node == null) {
            node = new SplittingNode(literal);
            nodes.put(literal, node);
        }
        
        return node;
    }
    
    public ComponentGraph createComponents() {
        Counter counter             = new Counter();
        Stack<SplittingNode> stack  = new Stack<>();

        ComponentGraph components   = new ComponentGraph();

        for (SplittingNode node : nodes.values()) {
            if (node.hasIndex() == false) {
                stronglyConnect(node, counter, stack, components);
            }
        }
        
        components.makeConnections();

        return components;
    }
    
    private void stronglyConnect(SplittingNode node, Counter counter, Stack<SplittingNode> stack, ComponentGraph components) {
        node.setIndex(counter.getValue());
        node.setLowLink(counter.getValue());

        counter.increment();
        stack.push(node);

        for (SplittingNode next : node) {
            if (next.hasIndex() == false) {
                stronglyConnect(next, counter, stack, components);
                node.setLowLink(Math.min(node.getLowLink(), next.getLowLink()));
            } else if (stack.contains(next)) {
                node.setLowLink(Math.min(node.getLowLink(), next.getIndex()));
            }
        }

        if (node.isRootNode()) {
            Set<SplittingNode> componentNodes   = new HashSet<>();
            Component component                 = new Component();

            SplittingNode last;
            do {
                last = stack.pop();
                componentNodes.add(last);
                last.setComponent(component);

            } while (node != last);
            
            component.setNodes(componentNodes);

            components.add(component);
        }        
    }  

}