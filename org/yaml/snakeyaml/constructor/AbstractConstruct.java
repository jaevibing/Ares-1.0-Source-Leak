package org.yaml.snakeyaml.constructor;

import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.Node;

public abstract class AbstractConstruct implements Construct
{
    @Override
    public void construct2ndStep(final Node a1, final Object a2) {
        /*SL:35*/if (a1.isTwoStepsConstruction()) {
            /*SL:36*/throw new IllegalStateException("Not Implemented in " + this.getClass().getName());
        }
        /*SL:38*/throw new YAMLException("Unexpected recursive structure for Node: " + a1);
    }
}
