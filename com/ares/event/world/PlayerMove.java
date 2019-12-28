package com.ares.event.world;

import com.ares.event.AresEvent;

public abstract class PlayerMove extends AresEvent
{
    public float yaw;
    public float pitch;
    public boolean onGround;
    
    public PlayerMove(final float a1, final float a2, final boolean a3) {
        this.yaw = a1;
        this.pitch = a2;
        this.onGround = a3;
    }
    
    public static class Pre extends PlayerMove
    {
        public Pre(final float a1, final float a2, final boolean a3) {
            super(a1, a2, a3);
        }
    }
    
    public static class Post extends PlayerMove
    {
        public Post(final float a1, final float a2, final boolean a3) {
            super(a1, a2, a3);
        }
    }
}
