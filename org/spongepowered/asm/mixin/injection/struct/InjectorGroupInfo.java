package org.spongepowered.asm.mixin.injection.struct;

import org.spongepowered.asm.lib.tree.AnnotationNode;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.injection.Group;
import org.spongepowered.asm.lib.tree.MethodNode;
import java.util.HashMap;
import java.util.Iterator;
import org.spongepowered.asm.mixin.injection.throwables.InjectionValidationException;
import org.apache.logging.log4j.LogManager;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class InjectorGroupInfo
{
    private final String name;
    private final List<InjectionInfo> members;
    private final boolean isDefault;
    private int minCallbackCount;
    private int maxCallbackCount;
    
    public InjectorGroupInfo(final String a1) {
        this(a1, false);
    }
    
    InjectorGroupInfo(final String a1, final boolean a2) {
        this.members = new ArrayList<InjectionInfo>();
        this.minCallbackCount = -1;
        this.maxCallbackCount = Integer.MAX_VALUE;
        this.name = a1;
        this.isDefault = a2;
    }
    
    @Override
    public String toString() {
        /*SL:168*/return String.format("@Group(name=%s, min=%d, max=%d)", this.getName(), this.getMinRequired(), this.getMaxAllowed());
    }
    
    public boolean isDefault() {
        /*SL:172*/return this.isDefault;
    }
    
    public String getName() {
        /*SL:176*/return this.name;
    }
    
    public int getMinRequired() {
        /*SL:180*/return Math.max(this.minCallbackCount, 1);
    }
    
    public int getMaxAllowed() {
        /*SL:184*/return Math.min(this.maxCallbackCount, Integer.MAX_VALUE);
    }
    
    public Collection<InjectionInfo> getMembers() {
        /*SL:193*/return Collections.<InjectionInfo>unmodifiableCollection((Collection<? extends InjectionInfo>)this.members);
    }
    
    public void setMinRequired(final int a1) {
        /*SL:205*/if (a1 < 1) {
            /*SL:206*/throw new IllegalArgumentException("Cannot set zero or negative value for injector group min count. Attempted to set min=" + a1 + " on " + this);
        }
        /*SL:209*/if (this.minCallbackCount > 0 && this.minCallbackCount != a1) {
            /*SL:210*/LogManager.getLogger("mixin").warn("Conflicting min value '{}' on @Group({}), previously specified {}", new Object[] { a1, this.name, this.minCallbackCount });
        }
        /*SL:213*/this.minCallbackCount = Math.max(this.minCallbackCount, a1);
    }
    
    public void setMaxAllowed(final int a1) {
        /*SL:225*/if (a1 < 1) {
            /*SL:226*/throw new IllegalArgumentException("Cannot set zero or negative value for injector group max count. Attempted to set max=" + a1 + " on " + this);
        }
        /*SL:229*/if (this.maxCallbackCount < Integer.MAX_VALUE && this.maxCallbackCount != a1) {
            /*SL:230*/LogManager.getLogger("mixin").warn("Conflicting max value '{}' on @Group({}), previously specified {}", new Object[] { a1, this.name, this.maxCallbackCount });
        }
        /*SL:233*/this.maxCallbackCount = Math.min(this.maxCallbackCount, a1);
    }
    
    public InjectorGroupInfo add(final InjectionInfo a1) {
        /*SL:243*/this.members.add(a1);
        /*SL:244*/return this;
    }
    
    public InjectorGroupInfo validate() throws InjectionValidationException {
        /*SL:254*/if (this.members.size() == 0) {
            /*SL:256*/return this;
        }
        int n = /*EL:259*/0;
        /*SL:260*/for (final InjectionInfo v1 : this.members) {
            /*SL:261*/n += v1.getInjectedCallbackCount();
        }
        final int v2 = /*EL:264*/this.getMinRequired();
        final int v3 = /*EL:265*/this.getMaxAllowed();
        /*SL:266*/if (n < v2) {
            /*SL:267*/throw new InjectionValidationException(this, String.format("expected %d invocation(s) but only %d succeeded", v2, n));
        }
        /*SL:268*/if (n > v3) {
            /*SL:269*/throw new InjectionValidationException(this, String.format("maximum of %d invocation(s) allowed but %d succeeded", v3, n));
        }
        /*SL:272*/return this;
    }
    
    public static final class Map extends HashMap<String, InjectorGroupInfo>
    {
        private static final long serialVersionUID = 1L;
        private static final InjectorGroupInfo NO_GROUP;
        
        @Override
        public InjectorGroupInfo get(final Object a1) {
            /*SL:56*/return this.forName(a1.toString());
        }
        
        public InjectorGroupInfo forName(final String a1) {
            InjectorGroupInfo v1 = /*EL:67*/super.get(a1);
            /*SL:68*/if (v1 == null) {
                /*SL:69*/v1 = new InjectorGroupInfo(a1);
                /*SL:70*/this.put(a1, v1);
            }
            /*SL:72*/return v1;
        }
        
        public InjectorGroupInfo parseGroup(final MethodNode a1, final String a2) {
            /*SL:84*/return this.parseGroup(Annotations.getInvisible(a1, Group.class), a2);
        }
        
        public InjectorGroupInfo parseGroup(final AnnotationNode a1, final String a2) {
            /*SL:96*/if (a1 == null) {
                /*SL:97*/return Map.NO_GROUP;
            }
            String v1 = /*EL:100*/Annotations.<String>getValue(a1, "name");
            /*SL:101*/if (v1 == null || v1.isEmpty()) {
                /*SL:102*/v1 = a2;
            }
            final InjectorGroupInfo v2 = /*EL:104*/this.forName(v1);
            final Integer v3 = /*EL:106*/Annotations.<Integer>getValue(a1, "min");
            /*SL:107*/if (v3 != null && v3 != -1) {
                /*SL:108*/v2.setMinRequired(v3);
            }
            final Integer v4 = /*EL:111*/Annotations.<Integer>getValue(a1, "max");
            /*SL:112*/if (v4 != null && v4 != -1) {
                /*SL:113*/v2.setMaxAllowed(v4);
            }
            /*SL:116*/return v2;
        }
        
        public void validateAll() throws InjectionValidationException {
            /*SL:125*/for (final InjectorGroupInfo v1 : ((HashMap<K, InjectorGroupInfo>)this).values()) {
                /*SL:126*/v1.validate();
            }
        }
        
        static {
            NO_GROUP = new InjectorGroupInfo("NONE", true);
        }
    }
}
