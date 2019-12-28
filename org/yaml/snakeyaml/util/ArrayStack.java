package org.yaml.snakeyaml.util;

import java.util.ArrayList;

public class ArrayStack<T>
{
    private ArrayList<T> stack;
    
    public ArrayStack(final int a1) {
        this.stack = new ArrayList<T>(a1);
    }
    
    public void push(final T a1) {
        /*SL:28*/this.stack.add(a1);
    }
    
    public T pop() {
        /*SL:32*/return this.stack.remove(this.stack.size() - 1);
    }
    
    public boolean isEmpty() {
        /*SL:36*/return this.stack.isEmpty();
    }
    
    public void clear() {
        /*SL:40*/this.stack.clear();
    }
}
