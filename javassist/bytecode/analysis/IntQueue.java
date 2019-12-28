package javassist.bytecode.analysis;

import java.util.NoSuchElementException;

class IntQueue
{
    private Entry head;
    private Entry tail;
    
    void add(final int a1) {
        final Entry v1 = /*EL:33*/new Entry(a1);
        /*SL:34*/if (this.tail != null) {
            /*SL:35*/this.tail.next = v1;
        }
        /*SL:36*/this.tail = v1;
        /*SL:38*/if (this.head == null) {
            /*SL:39*/this.head = v1;
        }
    }
    
    boolean isEmpty() {
        /*SL:43*/return this.head == null;
    }
    
    int take() {
        /*SL:47*/if (this.head == null) {
            /*SL:48*/throw new NoSuchElementException();
        }
        final int v1 = /*EL:50*/this.head.value;
        /*SL:51*/this.head = this.head.next;
        /*SL:52*/if (this.head == null) {
            /*SL:53*/this.tail = null;
        }
        /*SL:55*/return v1;
    }
    
    private static class Entry
    {
        private Entry next;
        private int value;
        
        private Entry(final int a1) {
            this.value = a1;
        }
    }
}
