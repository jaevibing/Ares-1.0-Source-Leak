package org.spongepowered.asm.lib.util;

import org.spongepowered.asm.lib.Label;
import java.util.Map;

public interface ASMifiable
{
    void asmify(StringBuffer p0, String p1, Map<Label, String> p2);
}
