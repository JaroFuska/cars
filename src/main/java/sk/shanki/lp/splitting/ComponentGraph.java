/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.splitting;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import sk.shanki.lp.Literal;

/**
 *
 * @author shanki
 */
public class ComponentGraph {
    private final Set<Component> components   = new HashSet<>();

    void add(Component component) {
        components.add(component);
    }

    void makeConnections() {                
        for (Component component : components) {
            for (SplittingNode node : component.nodes()) {
                for (SplittingNode nextNode : node) {
                    Component nextComponent = nextNode.getComponent();

                    if (component != nextComponent) {
                        nextComponent.addNext(component);
                    }
                }
            }
        }

        Component start = new Component();
        components.add(start);

        Component end = new Component();
        components.add(end);        

        for (Component component : components) {

            if (component != start && component != end) {
                if (component.hasIncomming() == false) {
                    start.addNext(component);
                }

                if (component.hasOutgoing() == false) {
                    component.addNext(end);
                }
            }
        }
    }
    
    public Set<Literal>[] layer() {

        int lastLevel = 0;

        Stack<Component> queue = new Stack<>();

        for (Component component : components) {
            component.setLevel(0);

            if (component.hasIncomming() == false) {
                queue.add(component);
            }
        }

        while(queue.isEmpty() == false) {
            Component component = queue.pop();
            int actualLevel = component.getLevel();

            for (Component next : component) {
                int nextLevel = next.getLevel();

                if (nextLevel <= actualLevel) {
                    int newLevel = actualLevel + 1;
                    next.setLevel(newLevel);

                    lastLevel = Math.max(lastLevel, newLevel);

                    if (queue.contains(next) == false) {
                        queue.push(next);
                    }
                }
            }
        }

        Set<Literal>[] layers = new Set[lastLevel + 1];
        
        for (int i = 0; i < layers.length; ++i) {
            layers[i] = new HashSet<>();
        }

        for (Component component : components) {
            int level = component.getLevel();
            layers[level].addAll(component.literals());
        }

        return layers;
    }    
    
    
    public List<Literal> litOrder() {
        List<Literal> order = new ArrayList<>();
        
        for (Set<Literal> layer : layer()) {
            order.addAll(layer);
        }
        
        return order;
    }

}