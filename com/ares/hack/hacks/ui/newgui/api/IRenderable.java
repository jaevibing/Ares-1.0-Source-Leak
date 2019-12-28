package com.ares.hack.hacks.ui.newgui.api;

import com.ares.utils.data.Vec2i;

public interface IRenderable
{
    void onRender(Vec2i p0);
    
    void onMouseDown(Vec2i p0, int p1);
    
    void onMouseRelease(Vec2i p0, int p1);
    
    void mouseClickMove(Vec2i p0, int p1);
}
