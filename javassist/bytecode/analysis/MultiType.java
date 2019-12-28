package javassist.bytecode.analysis;

import java.util.Iterator;
import java.util.HashMap;
import javassist.CtClass;
import java.util.Map;

public class MultiType extends Type
{
    private Map interfaces;
    private Type resolved;
    private Type potentialClass;
    private MultiType mergeSource;
    private boolean changed;
    
    public MultiType(final Map a1) {
        this(a1, null);
    }
    
    public MultiType(final Map a1, final Type a2) {
        super(null);
        this.changed = false;
        this.interfaces = a1;
        this.potentialClass = a2;
    }
    
    @Override
    public CtClass getCtClass() {
        /*SL:71*/if (this.resolved != null) {
            /*SL:72*/return this.resolved.getCtClass();
        }
        /*SL:74*/return Type.OBJECT.getCtClass();
    }
    
    @Override
    public Type getComponent() {
        /*SL:81*/return null;
    }
    
    @Override
    public int getSize() {
        /*SL:88*/return 1;
    }
    
    @Override
    public boolean isArray() {
        /*SL:95*/return false;
    }
    
    @Override
    boolean popChanged() {
        final boolean v1 = /*EL:102*/this.changed;
        /*SL:103*/this.changed = false;
        /*SL:104*/return v1;
    }
    
    @Override
    public boolean isAssignableFrom(final Type a1) {
        /*SL:108*/throw new UnsupportedOperationException("Not implemented");
    }
    
    public boolean isAssignableTo(final Type a1) {
        /*SL:112*/if (this.resolved != null) {
            /*SL:113*/return a1.isAssignableFrom(this.resolved);
        }
        /*SL:115*/if (Type.OBJECT.equals(a1)) {
            /*SL:116*/return true;
        }
        /*SL:118*/if (this.potentialClass != null && !a1.isAssignableFrom(this.potentialClass)) {
            /*SL:119*/this.potentialClass = null;
        }
        final Map v1 = /*EL:121*/this.mergeMultiAndSingle(this, a1);
        /*SL:123*/if (v1.size() == 1 && this.potentialClass == null) {
            /*SL:125*/this.resolved = Type.get(v1.values().iterator().next());
            /*SL:126*/this.propogateResolved();
            /*SL:128*/return true;
        }
        /*SL:132*/if (v1.size() >= 1) {
            /*SL:133*/this.interfaces = v1;
            /*SL:134*/this.propogateState();
            /*SL:136*/return true;
        }
        /*SL:139*/if (this.potentialClass != null) {
            /*SL:140*/this.resolved = this.potentialClass;
            /*SL:141*/this.propogateResolved();
            /*SL:143*/return true;
        }
        /*SL:146*/return false;
    }
    
    private void propogateState() {
        /*SL:151*/for (MultiType v1 = this.mergeSource; v1 != null; /*SL:154*/v1 = v1.mergeSource) {
            v1.interfaces = this.interfaces;
            v1.potentialClass = this.potentialClass;
        }
    }
    
    private void propogateResolved() {
        /*SL:160*/for (MultiType v1 = this.mergeSource; v1 != null; /*SL:162*/v1 = v1.mergeSource) {
            v1.resolved = this.resolved;
        }
    }
    
    @Override
    public boolean isReference() {
        /*SL:172*/return true;
    }
    
    private Map getAllMultiInterfaces(final MultiType v2) {
        final Map v3 = /*EL:176*/new HashMap();
        /*SL:178*/for (final CtClass a1 : v2.interfaces.values()) {
            /*SL:181*/v3.put(a1.getName(), a1);
            /*SL:182*/this.getAllInterfaces(a1, v3);
        }
        /*SL:185*/return v3;
    }
    
    private Map mergeMultiInterfaces(final MultiType a1, final MultiType a2) {
        final Map v1 = /*EL:190*/this.getAllMultiInterfaces(a1);
        final Map v2 = /*EL:191*/this.getAllMultiInterfaces(a2);
        /*SL:193*/return this.findCommonInterfaces(v1, v2);
    }
    
    private Map mergeMultiAndSingle(final MultiType a1, final Type a2) {
        final Map v1 = /*EL:197*/this.getAllMultiInterfaces(a1);
        final Map v2 = /*EL:198*/this.getAllInterfaces(a2.getCtClass(), null);
        /*SL:200*/return this.findCommonInterfaces(v1, v2);
    }
    
    private boolean inMergeSource(MultiType a1) {
        /*SL:204*/while (a1 != null) {
            /*SL:205*/if (a1 == this) {
                /*SL:206*/return true;
            }
            /*SL:208*/a1 = a1.mergeSource;
        }
        /*SL:211*/return false;
    }
    
    @Override
    public Type merge(final Type v0) {
        /*SL:215*/if (this == v0) {
            /*SL:216*/return this;
        }
        /*SL:218*/if (v0 == MultiType.UNINIT) {
            /*SL:219*/return this;
        }
        /*SL:221*/if (v0 == MultiType.BOGUS) {
            /*SL:222*/return MultiType.BOGUS;
        }
        /*SL:224*/if (v0 == null) {
            /*SL:225*/return this;
        }
        /*SL:227*/if (this.resolved != null) {
            /*SL:228*/return this.resolved.merge(v0);
        }
        /*SL:230*/if (this.potentialClass != null) {
            final Type a1 = /*EL:231*/this.potentialClass.merge(v0);
            /*SL:232*/if (!a1.equals(this.potentialClass) || a1.popChanged()) {
                /*SL:233*/this.potentialClass = (Type.OBJECT.equals(a1) ? null : a1);
                /*SL:234*/this.changed = true;
            }
        }
        Map v2;
        /*SL:240*/if (v0 instanceof MultiType) {
            final MultiType v = /*EL:241*/(MultiType)v0;
            /*SL:243*/if (v.resolved != null) {
                /*SL:244*/v2 = this.mergeMultiAndSingle(this, v.resolved);
            }
            else {
                /*SL:246*/v2 = this.mergeMultiInterfaces(v, this);
                /*SL:247*/if (!this.inMergeSource(v)) {
                    /*SL:248*/this.mergeSource = v;
                }
            }
        }
        else {
            /*SL:251*/v2 = this.mergeMultiAndSingle(this, v0);
        }
        /*SL:255*/if (v2.size() > 1 || (v2.size() == 1 && this.potentialClass != null)) {
            /*SL:257*/if (v2.size() != this.interfaces.size()) {
                /*SL:258*/this.changed = true;
            }
            else/*SL:259*/ if (!this.changed) {
                final Iterator v3 = /*EL:260*/v2.keySet().iterator();
                /*SL:261*/while (v3.hasNext()) {
                    /*SL:262*/if (!this.interfaces.containsKey(v3.next())) {
                        /*SL:263*/this.changed = true;
                    }
                }
            }
            /*SL:266*/this.interfaces = v2;
            /*SL:267*/this.propogateState();
            /*SL:269*/return this;
        }
        /*SL:272*/if (v2.size() == 1) {
            /*SL:273*/this.resolved = Type.get(v2.values().iterator().next());
        }
        else/*SL:274*/ if (this.potentialClass != null) {
            /*SL:275*/this.resolved = this.potentialClass;
        }
        else {
            /*SL:277*/this.resolved = MultiType.OBJECT;
        }
        /*SL:280*/this.propogateResolved();
        /*SL:282*/return this.resolved;
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:286*/if (!(a1 instanceof MultiType)) {
            /*SL:287*/return false;
        }
        final MultiType v1 = /*EL:289*/(MultiType)a1;
        /*SL:290*/if (this.resolved != null) {
            /*SL:291*/return this.resolved.equals(v1.resolved);
        }
        /*SL:292*/return v1.resolved == null && /*EL:295*/this.interfaces.keySet().equals(v1.interfaces.keySet());
    }
    
    @Override
    public String toString() {
        /*SL:299*/if (this.resolved != null) {
            /*SL:300*/return this.resolved.toString();
        }
        final StringBuffer v1 = /*EL:302*/new StringBuffer("{");
        final Iterator v2 = /*EL:303*/this.interfaces.keySet().iterator();
        /*SL:304*/while (v2.hasNext()) {
            /*SL:305*/v1.append(v2.next());
            /*SL:306*/v1.append(", ");
        }
        /*SL:308*/v1.setLength(v1.length() - 2);
        /*SL:309*/if (this.potentialClass != null) {
            /*SL:310*/v1.append(", *").append(this.potentialClass.toString());
        }
        /*SL:311*/v1.append("}");
        /*SL:312*/return v1.toString();
    }
}
