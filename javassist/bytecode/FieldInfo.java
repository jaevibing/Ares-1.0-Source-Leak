package javassist.bytecode;

import java.io.DataOutputStream;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;
import java.util.ArrayList;

public final class FieldInfo
{
    ConstPool constPool;
    int accessFlags;
    int name;
    String cachedName;
    String cachedType;
    int descriptor;
    ArrayList attribute;
    
    private FieldInfo(final ConstPool a1) {
        this.constPool = a1;
        this.accessFlags = 0;
        this.attribute = null;
    }
    
    public FieldInfo(final ConstPool a1, final String a2, final String a3) {
        this(a1);
        this.name = a1.addUtf8Info(a2);
        this.cachedName = a2;
        this.descriptor = a1.addUtf8Info(a3);
    }
    
    FieldInfo(final ConstPool a1, final DataInputStream a2) throws IOException {
        this(a1);
        this.read(a2);
    }
    
    @Override
    public String toString() {
        /*SL:79*/return this.getName() + " " + this.getDescriptor();
    }
    
    void compact(final ConstPool a1) {
        /*SL:91*/this.name = a1.addUtf8Info(this.getName());
        /*SL:92*/this.descriptor = a1.addUtf8Info(this.getDescriptor());
        /*SL:93*/this.attribute = AttributeInfo.copyAll(this.attribute, a1);
        /*SL:94*/this.constPool = a1;
    }
    
    void prune(final ConstPool a1) {
        final ArrayList v1 = /*EL:98*/new ArrayList();
        AttributeInfo v2 = /*EL:99*/this.getAttribute("RuntimeInvisibleAnnotations");
        /*SL:101*/if (v2 != null) {
            /*SL:102*/v2 = v2.copy(a1, null);
            /*SL:103*/v1.add(v2);
        }
        AttributeInfo v3 = /*EL:106*/this.getAttribute("RuntimeVisibleAnnotations");
        /*SL:108*/if (v3 != null) {
            /*SL:109*/v3 = v3.copy(a1, null);
            /*SL:110*/v1.add(v3);
        }
        AttributeInfo v4 = /*EL:113*/this.getAttribute("Signature");
        /*SL:115*/if (v4 != null) {
            /*SL:116*/v4 = v4.copy(a1, null);
            /*SL:117*/v1.add(v4);
        }
        int v5 = /*EL:120*/this.getConstantValue();
        /*SL:121*/if (v5 != 0) {
            /*SL:122*/v5 = this.constPool.copy(v5, a1, null);
            /*SL:123*/v1.add(new ConstantAttribute(a1, v5));
        }
        /*SL:126*/this.attribute = v1;
        /*SL:127*/this.name = a1.addUtf8Info(this.getName());
        /*SL:128*/this.descriptor = a1.addUtf8Info(this.getDescriptor());
        /*SL:129*/this.constPool = a1;
    }
    
    public ConstPool getConstPool() {
        /*SL:137*/return this.constPool;
    }
    
    public String getName() {
        /*SL:144*/if (this.cachedName == null) {
            /*SL:145*/this.cachedName = this.constPool.getUtf8Info(this.name);
        }
        /*SL:147*/return this.cachedName;
    }
    
    public void setName(final String a1) {
        /*SL:154*/this.name = this.constPool.addUtf8Info(a1);
        /*SL:155*/this.cachedName = a1;
    }
    
    public int getAccessFlags() {
        /*SL:164*/return this.accessFlags;
    }
    
    public void setAccessFlags(final int a1) {
        /*SL:173*/this.accessFlags = a1;
    }
    
    public String getDescriptor() {
        /*SL:182*/return this.constPool.getUtf8Info(this.descriptor);
    }
    
    public void setDescriptor(final String a1) {
        /*SL:191*/if (!a1.equals(this.getDescriptor())) {
            /*SL:192*/this.descriptor = this.constPool.addUtf8Info(a1);
        }
    }
    
    public int getConstantValue() {
        /*SL:202*/if ((this.accessFlags & 0x8) == 0x0) {
            /*SL:203*/return 0;
        }
        final ConstantAttribute v1 = /*EL:205*/(ConstantAttribute)this.getAttribute("ConstantValue");
        /*SL:207*/if (v1 == null) {
            /*SL:208*/return 0;
        }
        /*SL:210*/return v1.getConstantValue();
    }
    
    public List getAttributes() {
        /*SL:224*/if (this.attribute == null) {
            /*SL:225*/this.attribute = new ArrayList();
        }
        /*SL:227*/return this.attribute;
    }
    
    public AttributeInfo getAttribute(final String a1) {
        /*SL:243*/return AttributeInfo.lookup(this.attribute, a1);
    }
    
    public AttributeInfo removeAttribute(final String a1) {
        /*SL:254*/return AttributeInfo.remove(this.attribute, a1);
    }
    
    public void addAttribute(final AttributeInfo a1) {
        /*SL:264*/if (this.attribute == null) {
            /*SL:265*/this.attribute = new ArrayList();
        }
        /*SL:267*/AttributeInfo.remove(this.attribute, a1.getName());
        /*SL:268*/this.attribute.add(a1);
    }
    
    private void read(final DataInputStream v2) throws IOException {
        /*SL:272*/this.accessFlags = v2.readUnsignedShort();
        /*SL:273*/this.name = v2.readUnsignedShort();
        /*SL:274*/this.descriptor = v2.readUnsignedShort();
        final int v3 = /*EL:275*/v2.readUnsignedShort();
        /*SL:276*/this.attribute = new ArrayList();
        /*SL:277*/for (int a1 = 0; a1 < v3; ++a1) {
            /*SL:278*/this.attribute.add(AttributeInfo.read(this.constPool, v2));
        }
    }
    
    void write(final DataOutputStream a1) throws IOException {
        /*SL:282*/a1.writeShort(this.accessFlags);
        /*SL:283*/a1.writeShort(this.name);
        /*SL:284*/a1.writeShort(this.descriptor);
        /*SL:285*/if (this.attribute == null) {
            /*SL:286*/a1.writeShort(0);
        }
        else {
            /*SL:288*/a1.writeShort(this.attribute.size());
            /*SL:289*/AttributeInfo.writeAll(this.attribute, a1);
        }
    }
}
