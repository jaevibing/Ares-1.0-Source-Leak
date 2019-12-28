package org.spongepowered.tools.obfuscation.mapping.mcp;

import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import com.google.common.io.Files;
import java.io.IOException;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.asm.mixin.throwables.MixinException;
import org.spongepowered.asm.obfuscation.mapping.mcp.MappingFieldSrg;
import com.google.common.base.Strings;
import com.google.common.collect.BiMap;
import com.google.common.io.LineProcessor;
import java.nio.charset.Charset;
import java.io.File;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import org.spongepowered.tools.obfuscation.mapping.common.MappingProvider;

public class MappingProviderSrg extends MappingProvider
{
    public MappingProviderSrg(final Messager a1, final Filer a2) {
        super(a1, a2);
    }
    
    @Override
    public void read(final File a1) throws IOException {
        final BiMap<String, String> v1 = /*EL:59*/this.packageMap;
        final BiMap<String, String> v2 = /*EL:60*/this.classMap;
        final BiMap<MappingField, MappingField> v3 = /*EL:61*/this.fieldMap;
        final BiMap<MappingMethod, MappingMethod> v4 = /*EL:62*/this.methodMap;
        /*SL:64*/Files.<Object>readLines(a1, Charset.defaultCharset(), (LineProcessor<Object>)new LineProcessor<String>() {
            @Override
            public String getResult() {
                /*SL:67*/return null;
            }
            
            @Override
            public boolean processLine(final String a1) throws IOException {
                /*SL:72*/if (Strings.isNullOrEmpty(a1) || a1.startsWith("#")) {
                    /*SL:73*/return true;
                }
                final String v1 = /*EL:76*/a1.substring(0, 2);
                final String[] v2 = /*EL:77*/a1.substring(4).split(" ");
                /*SL:79*/if (v1.equals("PK")) {
                    /*SL:80*/v1.forcePut(v2[0], v2[1]);
                }
                else/*SL:81*/ if (v1.equals("CL")) {
                    /*SL:82*/v2.forcePut(v2[0], v2[1]);
                }
                else/*SL:83*/ if (v1.equals("FD")) {
                    /*SL:84*/v3.forcePut(new MappingFieldSrg(v2[0]).copy(), new MappingFieldSrg(v2[1]).copy());
                }
                else {
                    /*SL:85*/if (!v1.equals("MD")) {
                        /*SL:88*/throw new MixinException("Invalid SRG file: " + a1);
                    }
                    v4.forcePut(new MappingMethod(v2[0], v2[1]), new MappingMethod(v2[2], v2[3]));
                }
                /*SL:91*/return true;
            }
        });
    }
    
    @Override
    public MappingField getFieldMapping(MappingField a1) {
        /*SL:99*/if (a1.getDesc() != null) {
            /*SL:100*/a1 = new MappingFieldSrg(a1);
        }
        /*SL:102*/return this.fieldMap.get(a1);
    }
}
