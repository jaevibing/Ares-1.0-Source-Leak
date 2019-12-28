package javassist;

final class CtArray extends CtClass
{
    protected ClassPool pool;
    private CtClass[] interfaces;
    
    CtArray(final String a1, final ClassPool a2) {
        super(a1);
        this.interfaces = null;
        this.pool = a2;
    }
    
    @Override
    public ClassPool getClassPool() {
        /*SL:32*/return this.pool;
    }
    
    @Override
    public boolean isArray() {
        /*SL:36*/return true;
    }
    
    @Override
    public int getModifiers() {
        int v1 = /*EL:42*/16;
        try {
            /*SL:44*/v1 |= (this.getComponentType().getModifiers() & 0x7);
        }
        catch (NotFoundException ex) {}
        /*SL:48*/return v1;
    }
    
    @Override
    public CtClass[] getInterfaces() throws NotFoundException {
        /*SL:52*/if (this.interfaces == null) {
            final Class[] v0 = /*EL:53*/Object[].class.getInterfaces();
            /*SL:56*/this.interfaces = new CtClass[v0.length];
            /*SL:57*/for (int v = 0; v < v0.length; ++v) {
                /*SL:58*/this.interfaces[v] = this.pool.get(v0[v].getName());
            }
        }
        /*SL:61*/return this.interfaces;
    }
    
    @Override
    public boolean subtypeOf(final CtClass v2) throws NotFoundException {
        /*SL:65*/if (super.subtypeOf(v2)) {
            /*SL:66*/return true;
        }
        final String v3 = /*EL:68*/v2.getName();
        /*SL:69*/if (v3.equals("java.lang.Object")) {
            /*SL:70*/return true;
        }
        final CtClass[] v4 = /*EL:72*/this.getInterfaces();
        /*SL:73*/for (int a1 = 0; a1 < v4.length; ++a1) {
            /*SL:74*/if (v4[a1].subtypeOf(v2)) {
                /*SL:75*/return true;
            }
        }
        /*SL:78*/return v2.isArray() && this.getComponentType().subtypeOf(v2.getComponentType());
    }
    
    @Override
    public CtClass getComponentType() throws NotFoundException {
        final String v1 = /*EL:82*/this.getName();
        /*SL:83*/return this.pool.get(v1.substring(0, v1.length() - 2));
    }
    
    @Override
    public CtClass getSuperclass() throws NotFoundException {
        /*SL:87*/return this.pool.get("java.lang.Object");
    }
    
    @Override
    public CtMethod[] getMethods() {
        try {
            /*SL:92*/return this.getSuperclass().getMethods();
        }
        catch (NotFoundException v1) {
            /*SL:95*/return super.getMethods();
        }
    }
    
    @Override
    public CtMethod getMethod(final String a1, final String a2) throws NotFoundException {
        /*SL:102*/return this.getSuperclass().getMethod(a1, a2);
    }
    
    @Override
    public CtConstructor[] getConstructors() {
        try {
            /*SL:107*/return this.getSuperclass().getConstructors();
        }
        catch (NotFoundException v1) {
            /*SL:110*/return super.getConstructors();
        }
    }
}
