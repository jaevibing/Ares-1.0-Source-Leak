package org.yaml.snakeyaml.serializer;

import java.text.NumberFormat;
import org.yaml.snakeyaml.nodes.Node;

public class NumberAnchorGenerator implements AnchorGenerator
{
    private int lastAnchorId;
    
    public NumberAnchorGenerator(final int a1) {
        this.lastAnchorId = 0;
        this.lastAnchorId = a1;
    }
    
    @Override
    public String nextAnchor(final Node a1) {
        /*SL:31*/++this.lastAnchorId;
        final NumberFormat v1 = /*EL:32*/NumberFormat.getNumberInstance();
        /*SL:33*/v1.setMinimumIntegerDigits(3);
        /*SL:34*/v1.setMaximumFractionDigits(0);
        /*SL:35*/v1.setGroupingUsed(false);
        final String v2 = /*EL:36*/v1.format(this.lastAnchorId);
        /*SL:37*/return "id" + v2;
    }
}
