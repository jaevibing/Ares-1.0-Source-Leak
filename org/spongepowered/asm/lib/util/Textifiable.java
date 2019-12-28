package org.spongepowered.asm.lib.util;

import org.spongepowered.asm.lib.Label;
import java.util.Map;

public interface Textifiable
{
    void textify(StringBuffer p0, Map<Label, String> p1);
}
