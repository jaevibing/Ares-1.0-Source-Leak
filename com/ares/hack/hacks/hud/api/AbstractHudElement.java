package com.ares.hack.hacks.hud.api;

import com.ares.utils.data.Vec2i;
import com.ares.hack.hacks.BaseHack;

public abstract class AbstractHudElement extends BaseHack
{
    private String name;
    private boolean visible;
    private Vec2i pos;
    private Vec2i size;
    
    AbstractHudElement() {
        this.visible = true;
        this.pos = new Vec2i(0, 0);
        this.size = new Vec2i(0, 0);
    }
    
    public AbstractHudElement(final int a1, final int a2, final int a3, final int a4) {
        this.visible = true;
        this.pos = new Vec2i(0, 0);
        this.size = new Vec2i(0, 0);
        this.pos = new Vec2i(a1, a2);
        this.size = new Vec2i(a3, a4);
    }
    
    public boolean isMouseOver(final int a1, final int a2) {
        /*SL:25*/return a1 >= this.getPos().x && /*EL:26*/a2 >= this.getPos().y && /*EL:28*/a1 <= this.getPos().x + /*EL:30*/this.getSize().x && a2 <= this.getPos().y + /*EL:32*/this.getSize().y;
    }
    
    public abstract void render(final int p0, final int p1, final float p2);
    
    public abstract void onMouseClick(final int p0, final int p1, final int p2);
    
    public abstract void onMouseRelease(final int p0, final int p1, final int p2);
    
    public abstract void mouseClickMove(final int p0, final int p1, final int p2);
    
    public String getName() {
        /*SL:47*/return this.name;
    }
    
    public void setName(final String a1) {
        /*SL:52*/this.name = a1;
    }
    
    public boolean isVisible() {
        /*SL:57*/return this.getEnabled();
    }
    
    public void setVisible(final boolean a1) {
        /*SL:62*/this.visible = a1;
    }
    
    public Vec2i getPos() {
        /*SL:67*/return this.pos;
    }
    
    public void setPos(final Vec2i a1) {
        /*SL:72*/this.pos = a1;
    }
    
    public Vec2i getSize() {
        /*SL:77*/return this.size;
    }
    
    public void setSize(final Vec2i a1) {
        /*SL:82*/this.size = a1;
    }
    
    @Override
    public String toString() {
        /*SL:88*/return this.getName();
    }
}
