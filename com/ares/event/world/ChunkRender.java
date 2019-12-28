package com.ares.event.world;

import net.minecraft.client.renderer.chunk.RenderChunk;
import com.ares.event.AresEvent;

public abstract class ChunkRender extends AresEvent
{
    public static class Pre extends ChunkRender
    {
        public RenderChunk renderChunk;
        
        public Pre(final RenderChunk a1) {
            this.renderChunk = a1;
        }
    }
    
    public static class SetOrigin extends ChunkRender
    {
        public RenderChunk renderChunk;
        public int x;
        public int y;
        public int z;
        
        public SetOrigin(final RenderChunk a1, final int a2, final int a3, final int a4) {
            this.renderChunk = a1;
            this.x = a2;
            this.y = a3;
            this.z = a4;
        }
    }
}
