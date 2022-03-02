package examples;

import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

/**
 * This class implements the COMPOSITE class from the file composite.e in the Eiffel project
 * AutoProof
 * 
 * @author Facundo Molina (with modifications by N. Aguirre)
 */
public class Composite {
 
    /**
     * List of child nodes
     */ 
    private List<Composite> children; 

    /**
     * Parent node
     */
    private Composite parent; 

    /**
     * Value of the node
     */
    private int value; 

    /**
     * Initial value (at node creation)
     */
    private int init_value;

    /**
     * set of ancestor nodes (transitive closure of parent)
     */
    private Set<Composite> ancestors; 

    /**
     * Node from the set of children with max value greater than init_value (null otherwise)
     */
    private Composite max_child; 
                                
    /**
     * Creates a singleton node with initial value `v'
     */
    public Composite(int v) {
        children = new LinkedList<Composite>();
        init_value = v;
        value = v;
        ancestors = new HashSet<Composite>();
    }

    /**
     * Set of child nodes.
     */
    public Set<Composite> children() {
        if (children == null) throw new IllegalStateException();
        Set<Composite> output = new HashSet<Composite>();
        output.addAll(children);
        return output;
    }


    /**
     * Adds c to children.
     */
    public void addChild(Composite c) {
        if (c == null) throw new IllegalArgumentException();
        if ((c == this) || (parent.parent != null) || (!c.children.isEmpty())) throw new IllegalArgumentException();
        c.setParent(this);
        children.add(c);
        update(c);
    }

    /**
     * Sets `parent' to `p'.
     */
    private void setParent(Composite p) {
        if (p == null) throw new IllegalArgumentException();
        parent = p;
        ancestors.addAll(p.ancestors);
        ancestors.add(p);
    }

    /**
     * Updates `value' of this node and its ancestors taking into account an updated child `c'.
     */
    private void update(Composite c) {
        if (c == null) throw new IllegalArgumentException();
        if (value < c.value) {
            value = c.value;
            max_child = c;
            if (parent != null) {
                parent.update(c);
            }
        }
    }

}
