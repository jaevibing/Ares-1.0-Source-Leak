package javassist;

public abstract class CtMember
{
    CtMember next;
    protected CtClass declaringClass;
    
    protected CtMember(final CtClass a1) {
        this.declaringClass = a1;
        this.next = null;
    }
    
    final CtMember next() {
        /*SL:134*/return this.next;
    }
    
    void nameReplaced() {
    }
    
    @Override
    public String toString() {
        final StringBuffer v1 = /*EL:145*/new StringBuffer(this.getClass().getName());
        /*SL:146*/v1.append("@");
        /*SL:147*/v1.append(Integer.toHexString(this.hashCode()));
        /*SL:148*/v1.append("[");
        /*SL:149*/v1.append(Modifier.toString(this.getModifiers()));
        /*SL:150*/this.extendToString(v1);
        /*SL:151*/v1.append("]");
        /*SL:152*/return v1.toString();
    }
    
    protected abstract void extendToString(final StringBuffer p0);
    
    public CtClass getDeclaringClass() {
        /*SL:167*/return this.declaringClass;
    }
    
    public boolean visibleFrom(final CtClass v-1) {
        final int v0 = /*EL:173*/this.getModifiers();
        /*SL:174*/if (Modifier.isPublic(v0)) {
            /*SL:175*/return true;
        }
        /*SL:176*/if (Modifier.isPrivate(v0)) {
            /*SL:177*/return v-1 == this.declaringClass;
        }
        final String v = /*EL:179*/this.declaringClass.getPackageName();
        final String v2 = /*EL:180*/v-1.getPackageName();
        final boolean v3;
        /*SL:182*/if (v == null) {
            final boolean a1 = /*EL:183*/v2 == null;
        }
        else {
            /*SL:185*/v3 = v.equals(v2);
        }
        /*SL:187*/if (!v3 && Modifier.isProtected(v0)) {
            /*SL:188*/return v-1.subclassOf(this.declaringClass);
        }
        /*SL:190*/return v3;
    }
    
    public abstract int getModifiers();
    
    public abstract void setModifiers(final int p0);
    
    public boolean hasAnnotation(final Class a1) {
        /*SL:218*/return this.hasAnnotation(a1.getName());
    }
    
    public abstract boolean hasAnnotation(final String p0);
    
    public abstract Object getAnnotation(final Class p0) throws ClassNotFoundException;
    
    public abstract Object[] getAnnotations() throws ClassNotFoundException;
    
    public abstract Object[] getAvailableAnnotations();
    
    public abstract String getName();
    
    public abstract String getSignature();
    
    public abstract String getGenericSignature();
    
    public abstract void setGenericSignature(final String p0);
    
    public abstract byte[] getAttribute(final String p0);
    
    public abstract void setAttribute(final String p0, final byte[] p1);
    
    static class Cache extends CtMember
    {
        private CtMember methodTail;
        private CtMember consTail;
        private CtMember fieldTail;
        
        @Override
        protected void extendToString(final StringBuffer a1) {
        }
        
        @Override
        public boolean hasAnnotation(final String a1) {
            /*SL:33*/return false;
        }
        
        @Override
        public Object getAnnotation(final Class a1) throws ClassNotFoundException {
            /*SL:35*/return null;
        }
        
        @Override
        public Object[] getAnnotations() throws ClassNotFoundException {
            /*SL:37*/return null;
        }
        
        @Override
        public byte[] getAttribute(final String a1) {
            /*SL:38*/return null;
        }
        
        @Override
        public Object[] getAvailableAnnotations() {
            /*SL:39*/return null;
        }
        
        @Override
        public int getModifiers() {
            /*SL:40*/return 0;
        }
        
        @Override
        public String getName() {
            /*SL:41*/return null;
        }
        
        @Override
        public String getSignature() {
            /*SL:42*/return null;
        }
        
        @Override
        public void setAttribute(final String a1, final byte[] a2) {
        }
        
        @Override
        public void setModifiers(final int a1) {
        }
        
        @Override
        public String getGenericSignature() {
            /*SL:45*/return null;
        }
        
        @Override
        public void setGenericSignature(final String a1) {
        }
        
        Cache(final CtClassType a1) {
            /*SL:46*/super(a1);
            this.methodTail = this;
            this.consTail = this;
            this.fieldTail = this;
            this.fieldTail.next = this;
        }
        
        CtMember methodHead() {
            /*SL:60*/return this;
        }
        
        CtMember lastMethod() {
            /*SL:61*/return this.methodTail;
        }
        
        CtMember consHead() {
            /*SL:62*/return this.methodTail;
        }
        
        CtMember lastCons() {
            /*SL:63*/return this.consTail;
        }
        
        CtMember fieldHead() {
            /*SL:64*/return this.consTail;
        }
        
        CtMember lastField() {
            /*SL:65*/return this.fieldTail;
        }
        
        void addMethod(final CtMember a1) {
            /*SL:68*/a1.next = this.methodTail.next;
            /*SL:69*/this.methodTail.next = a1;
            /*SL:70*/if (this.methodTail == this.consTail) {
                /*SL:71*/this.consTail = a1;
                /*SL:72*/if (this.methodTail == this.fieldTail) {
                    /*SL:73*/this.fieldTail = a1;
                }
            }
            /*SL:76*/this.methodTail = a1;
        }
        
        void addConstructor(final CtMember a1) {
            /*SL:82*/a1.next = this.consTail.next;
            /*SL:83*/this.consTail.next = a1;
            /*SL:84*/if (this.consTail == this.fieldTail) {
                /*SL:85*/this.fieldTail = a1;
            }
            /*SL:87*/this.consTail = a1;
        }
        
        void addField(final CtMember a1) {
            /*SL:91*/a1.next = this;
            /*SL:92*/this.fieldTail.next = a1;
            /*SL:93*/this.fieldTail = a1;
        }
        
        static int count(CtMember a1, final CtMember a2) {
            int v1 = /*EL:97*/0;
            /*SL:98*/while (a1 != a2) {
                /*SL:99*/++v1;
                /*SL:100*/a1 = a1.next;
            }
            /*SL:103*/return v1;
        }
        
        void remove(final CtMember a1) {
            CtMember v1 = /*EL:107*/this;
            CtMember v2;
            /*SL:109*/while ((v2 = v1.next) != this) {
                /*SL:110*/if (v2 == a1) {
                    /*SL:111*/v1.next = v2.next;
                    /*SL:112*/if (v2 == this.methodTail) {
                        /*SL:113*/this.methodTail = v1;
                    }
                    /*SL:115*/if (v2 == this.consTail) {
                        /*SL:116*/this.consTail = v1;
                    }
                    /*SL:118*/if (v2 == this.fieldTail) {
                        /*SL:119*/this.fieldTail = v1;
                        break;
                    }
                    break;
                }
                else {
                    /*SL:124*/v1 = v1.next;
                }
            }
        }
    }
}
