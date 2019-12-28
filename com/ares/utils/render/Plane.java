package com.ares.utils.render;

public class Plane
{
    private final double x;
    private final double y;
    private final boolean visible;
    
    public Plane(final double a1, final double a2, final boolean a3) {
        this.x = a1;
        this.y = a2;
        this.visible = a3;
    }
    
    public double getX() {
        /*SL:17*/return this.x;
    }
    
    public double getY() {
        /*SL:21*/return this.y;
    }
    
    public boolean isVisible() {
        /*SL:25*/return this.visible;
    }
}
