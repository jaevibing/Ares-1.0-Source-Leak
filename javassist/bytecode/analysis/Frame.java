package javassist.bytecode.analysis;

public class Frame
{
    private Type[] locals;
    private Type[] stack;
    private int top;
    private boolean jsrMerged;
    private boolean retMerged;
    
    public Frame(final int a1, final int a2) {
        this.locals = new Type[a1];
        this.stack = new Type[a2];
    }
    
    public Type getLocal(final int a1) {
        /*SL:49*/return this.locals[a1];
    }
    
    public void setLocal(final int a1, final Type a2) {
        /*SL:59*/this.locals[a1] = a2;
    }
    
    public Type getStack(final int a1) {
        /*SL:70*/return this.stack[a1];
    }
    
    public void setStack(final int a1, final Type a2) {
        /*SL:80*/this.stack[a1] = a2;
    }
    
    public void clearStack() {
        /*SL:87*/this.top = 0;
    }
    
    public int getTopIndex() {
        /*SL:99*/return this.top - 1;
    }
    
    public int localsLength() {
        /*SL:109*/return this.locals.length;
    }
    
    public Type peek() {
        /*SL:118*/if (this.top < 1) {
            /*SL:119*/throw new IndexOutOfBoundsException("Stack is empty");
        }
        /*SL:121*/return this.stack[this.top - 1];
    }
    
    public Type pop() {
        /*SL:130*/if (this.top < 1) {
            /*SL:131*/throw new IndexOutOfBoundsException("Stack is empty");
        }
        final Type[] stack = /*EL:132*/this.stack;
        final int top = this.top - 1;
        this.top = top;
        return stack[top];
    }
    
    public void push(final Type a1) {
        /*SL:141*/this.stack[this.top++] = a1;
    }
    
    public Frame copy() {
        final Frame v1 = /*EL:152*/new Frame(this.locals.length, this.stack.length);
        /*SL:153*/System.arraycopy(this.locals, 0, v1.locals, 0, this.locals.length);
        /*SL:154*/System.arraycopy(this.stack, 0, v1.stack, 0, this.stack.length);
        /*SL:155*/v1.top = this.top;
        /*SL:156*/return v1;
    }
    
    public Frame copyStack() {
        final Frame v1 = /*EL:166*/new Frame(this.locals.length, this.stack.length);
        /*SL:167*/System.arraycopy(this.stack, 0, v1.stack, 0, this.stack.length);
        /*SL:168*/v1.top = this.top;
        /*SL:169*/return v1;
    }
    
    public boolean mergeStack(final Frame v-3) {
        boolean b = /*EL:180*/false;
        /*SL:181*/if (this.top != v-3.top) {
            /*SL:182*/throw new RuntimeException("Operand stacks could not be merged, they are different sizes!");
        }
        /*SL:184*/for (int i = 0; i < this.top; ++i) {
            /*SL:185*/if (this.stack[i] != null) {
                final Type a1 = /*EL:186*/this.stack[i];
                final Type v1 = /*EL:187*/a1.merge(v-3.stack[i]);
                /*SL:188*/if (v1 == Type.BOGUS) {
                    /*SL:189*/throw new RuntimeException("Operand stacks could not be merged due to differing primitive types: pos = " + i);
                }
                /*SL:191*/this.stack[i] = v1;
                /*SL:193*/if (!v1.equals(a1) || v1.popChanged()) {
                    /*SL:194*/b = true;
                }
            }
        }
        /*SL:199*/return b;
    }
    
    public boolean merge(final Frame v-3) {
        boolean b = /*EL:210*/false;
        /*SL:213*/for (int i = 0; i < this.locals.length; ++i) {
            /*SL:214*/if (this.locals[i] != null) {
                final Type a1 = /*EL:215*/this.locals[i];
                final Type v1 = /*EL:216*/a1.merge(v-3.locals[i]);
                /*SL:218*/this.locals[i] = v1;
                /*SL:219*/if (!v1.equals(a1) || v1.popChanged()) {
                    /*SL:220*/b = true;
                }
            }
            else/*SL:222*/ if (v-3.locals[i] != null) {
                /*SL:223*/this.locals[i] = v-3.locals[i];
                /*SL:224*/b = true;
            }
        }
        /*SL:228*/b |= this.mergeStack(v-3);
        /*SL:229*/return b;
    }
    
    @Override
    public String toString() {
        final StringBuffer v0 = /*EL:233*/new StringBuffer();
        /*SL:235*/v0.append("locals = [");
        /*SL:236*/for (int v = 0; v < this.locals.length; ++v) {
            /*SL:237*/v0.append((this.locals[v] == null) ? "empty" : this.locals[v].toString());
            /*SL:238*/if (v < this.locals.length - 1) {
                /*SL:239*/v0.append(", ");
            }
        }
        /*SL:241*/v0.append("] stack = [");
        /*SL:242*/for (int v = 0; v < this.top; ++v) {
            /*SL:243*/v0.append(this.stack[v]);
            /*SL:244*/if (v < this.top - 1) {
                /*SL:245*/v0.append(", ");
            }
        }
        /*SL:247*/v0.append("]");
        /*SL:249*/return v0.toString();
    }
    
    boolean isJsrMerged() {
        /*SL:258*/return this.jsrMerged;
    }
    
    void setJsrMerged(final boolean a1) {
        /*SL:267*/this.jsrMerged = a1;
    }
    
    boolean isRetMerged() {
        /*SL:277*/return this.retMerged;
    }
    
    void setRetMerged(final boolean a1) {
        /*SL:287*/this.retMerged = a1;
    }
}
