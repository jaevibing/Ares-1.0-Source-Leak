package org.spongepowered.tools.obfuscation.mapping.mcp;

import java.util.Iterator;
import java.io.PrintWriter;
import java.io.IOException;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import org.spongepowered.tools.obfuscation.mapping.IMappingConsumer;
import org.spongepowered.tools.obfuscation.ObfuscationType;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import org.spongepowered.tools.obfuscation.mapping.common.MappingWriter;

public class MappingWriterSrg extends MappingWriter
{
    public MappingWriterSrg(final Messager a1, final Filer a2) {
        super(a1, a2);
    }
    
    @Override
    public void write(final String a3, final ObfuscationType a4, final IMappingConsumer.MappingSet<MappingField> v1, final IMappingConsumer.MappingSet<MappingMethod> v2) {
        /*SL:51*/if (a3 == null) {
            /*SL:52*/return;
        }
        PrintWriter v3 = /*EL:55*/null;
        try {
            /*SL:58*/v3 = this.openFileWriter(a3, a4 + " output SRGs");
            /*SL:59*/this.writeFieldMappings(v3, v1);
            /*SL:60*/this.writeMethodMappings(v3, v2);
        }
        catch (IOException a5) {
            /*SL:62*/a5.printStackTrace();
        }
        finally {
            /*SL:64*/if (v3 != null) {
                try {
                    /*SL:66*/v3.close();
                }
                catch (Exception ex) {}
            }
        }
    }
    
    protected void writeFieldMappings(final PrintWriter v1, final IMappingConsumer.MappingSet<MappingField> v2) {
        /*SL:75*/for (final IMappingConsumer.MappingSet.Pair<MappingField> a1 : v2) {
            /*SL:76*/v1.println(this.formatFieldMapping(a1));
        }
    }
    
    protected void writeMethodMappings(final PrintWriter v1, final IMappingConsumer.MappingSet<MappingMethod> v2) {
        /*SL:81*/for (final IMappingConsumer.MappingSet.Pair<MappingMethod> a1 : v2) {
            /*SL:82*/v1.println(this.formatMethodMapping(a1));
        }
    }
    
    protected String formatFieldMapping(final IMappingConsumer.MappingSet.Pair<MappingField> a1) {
        /*SL:87*/return String.format("FD: %s/%s %s/%s", ((MappingField)a1.from).getOwner(), ((MappingField)a1.from).getName(), ((MappingField)a1.to).getOwner(), ((MappingField)a1.to).getName());
    }
    
    protected String formatMethodMapping(final IMappingConsumer.MappingSet.Pair<MappingMethod> a1) {
        /*SL:91*/return String.format("MD: %s %s %s %s", ((MappingMethod)a1.from).getName(), ((MappingMethod)a1.from).getDesc(), ((MappingMethod)a1.to).getName(), ((MappingMethod)a1.to).getDesc());
    }
}
