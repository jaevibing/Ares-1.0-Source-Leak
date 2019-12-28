package com.ares.utils.data;

public class Vec2i
{
    public int x;
    public int y;
    
    public Vec2i(final int a1, final int a2) {
        this.x = a1;
        this.y = a2;
    }
    
    public Vec2i add(final Vec2i a1) {
        final int v1 = /*EL:16*/this.x + a1.x;
        final int v2 = /*EL:17*/this.y + a1.y;
        /*SL:18*/return new Vec2i(v1, v2);
    }
    
    public Vec2i subtract(final Vec2i a1) {
        final int v1 = /*EL:23*/this.x - a1.x;
        final int v2 = /*EL:24*/this.y - a1.y;
        /*SL:25*/return new Vec2i(v1, v2);
    }
    
    public Vec2i clone() {
        /*SL:30*/return new Vec2i(this.x, this.y);
    }
    
    public Vec2i copy(final Vec2i a1) {
        /*SL:35*/this.x = a1.x;
        /*SL:36*/this.y = a1.y;
        /*SL:37*/return this;
    }
    
    @Override
    public boolean equals(final Object v2) {
        /*SL:43*/if (v2 instanceof Vec2i) {
            final Vec2i a1 = /*EL:45*/(Vec2i)v2;
            /*SL:47*/return a1.x == this.x && a1.y == this.y;
        }
        /*SL:49*/return super.equals(v2);
    }
    
    @Override
    public String toString() {
        /*SL:55*/return "(" + this.x + ", " + this.y + ")";
    }
}
