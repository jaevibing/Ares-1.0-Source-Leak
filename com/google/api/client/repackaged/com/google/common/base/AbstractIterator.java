package com.google.api.client.repackaged.com.google.common.base;

import java.util.NoSuchElementException;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;
import com.google.api.client.repackaged.com.google.common.annotations.GwtCompatible;
import java.util.Iterator;

@GwtCompatible
abstract class AbstractIterator<T> implements Iterator<T>
{
    private State state;
    private T next;
    
    protected AbstractIterator() {
        this.state = State.NOT_READY;
    }
    
    protected abstract T computeNext();
    
    @Nullable
    @CanIgnoreReturnValue
    protected final T endOfData() {
        /*SL:49*/this.state = State.DONE;
        /*SL:50*/return null;
    }
    
    @Override
    public final boolean hasNext() {
        /*SL:55*/Preconditions.checkState(this.state != State.FAILED);
        /*SL:56*/switch (this.state) {
            case READY: {
                /*SL:58*/return true;
            }
            case DONE: {
                /*SL:60*/return false;
            }
            default: {
                /*SL:63*/return this.tryToComputeNext();
            }
        }
    }
    
    private boolean tryToComputeNext() {
        /*SL:67*/this.state = State.FAILED;
        /*SL:68*/this.next = this.computeNext();
        /*SL:69*/if (this.state != State.DONE) {
            /*SL:70*/this.state = State.READY;
            /*SL:71*/return true;
        }
        /*SL:73*/return false;
    }
    
    @Override
    public final T next() {
        /*SL:78*/if (!this.hasNext()) {
            /*SL:79*/throw new NoSuchElementException();
        }
        /*SL:81*/this.state = State.NOT_READY;
        final T v1 = /*EL:82*/this.next;
        /*SL:83*/this.next = null;
        /*SL:84*/return v1;
    }
    
    @Override
    public final void remove() {
        /*SL:89*/throw new UnsupportedOperationException();
    }
    
    private enum State
    {
        READY, 
        NOT_READY, 
        DONE, 
        FAILED;
    }
}
