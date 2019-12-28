package com.ares.hack.hacks.hud.api;

import net.minecraft.client.gui.ScaledResolution;
import com.ares.utils.render.Render2dUtils;
import java.awt.Color;
import com.ares.Ares;
import com.ares.hack.hacks.hud.impl.GuiEditHud;
import com.ares.utils.data.Vec2i;

public abstract class MoveableHudElement extends AbstractHudElement
{
    private boolean dragging;
    protected Vec2i mouseOffset;
    
    public MoveableHudElement(final String a1) {
        this.dragging = false;
        this.mouseOffset = new Vec2i(0, 0);
        this.setName(a1);
        this.setVisible(true);
    }
    
    @Override
    public void render(final int v1, final int v2, final float v3) {
        /*SL:25*/if (!this.shouldRender()) {
            return;
        }
        /*SL:26*/if (MoveableHudElement.mc.field_71462_r instanceof GuiEditHud) {
            /*SL:28*/if (Ares.hudManager.getMouseOver(v1, v2) == this || this.dragging) {
                /*SL:30*/Render2dUtils.drawRect(this.getPos().x - 1, this.getPos().y - 1, this.getPos().x + this.getSize().x + 1, this.getPos().y + this.getSize().y + 1, Color.DARK_GRAY.getRGB());
                /*SL:32*/if (!this.dragging) {
                    final int a1 = MoveableHudElement.mc.field_71466_p.func_78256_a(/*EL:35*/this.getName()) + 1;
                    final int a2 = MoveableHudElement.mc.field_71466_p.field_78288_b + /*EL:36*/1;
                    /*SL:38*/Render2dUtils.drawRect(v1 + 5, v2 + 5, v1 + 5 + a1, v2 + 5 + a2, Color.WHITE.getRGB());
                    MoveableHudElement.mc.field_71466_p.func_78276_b(/*EL:39*/this.getName(), v1 + 6, v2 + 6, 0);
                }
            }
            else {
                /*SL:44*/Render2dUtils.drawRect(this.getPos().x - 1, this.getPos().y - 1, this.getPos().x + this.getSize().x + 1, this.getPos().y + this.getSize().y + 1, Color.GRAY.getRGB());
            }
        }
        /*SL:48*/this.forceInView();
    }
    
    @Override
    public void onMouseClick(final int a1, final int a2, final int a3) {
        final boolean v1 = Ares.hudManager.getMouseOver(/*EL:54*/a1, a2) == this;
        /*SL:56*/if (a3 == 1 && v1) {
            /*SL:58*/this.setVisible(!this.isVisible());
        }
        /*SL:61*/if (a3 == 0 && v1) {
            /*SL:63*/this.dragging = true;
            /*SL:64*/this.mouseOffset.x = a1 - this.getPos().x;
            /*SL:65*/this.mouseOffset.y = a2 - this.getPos().y;
            Ares.hudManager.bringToFront(/*EL:67*/this);
        }
    }
    
    @Override
    public void onMouseRelease(final int a1, final int a2, final int a3) {
        /*SL:74*/if (a3 == 0) {
            /*SL:76*/this.dragging = false;
        }
    }
    
    @Override
    public void mouseClickMove(final int a1, final int a2, final int a3) {
        /*SL:83*/if (a3 == 0 && this.dragging) {
            /*SL:85*/this.getPos().x = a1 - this.mouseOffset.x;
            /*SL:86*/this.getPos().y = a2 - this.mouseOffset.y;
            /*SL:88*/this.forceInView();
        }
    }
    
    protected void forceInView() {
        /*SL:97*/if (this.getPos().x < 0) {
            /*SL:99*/this.getPos().x = 0;
        }
        /*SL:101*/if (this.getPos().y < 0) {
            /*SL:103*/this.getPos().y = 0;
        }
        final ScaledResolution v1 = /*EL:106*/new ScaledResolution(MoveableHudElement.mc);
        /*SL:108*/if (this.getPos().x + this.getSize().x > v1.func_78326_a()) {
            /*SL:110*/this.getPos().x = v1.func_78326_a() - this.getSize().x;
        }
        /*SL:112*/if (this.getPos().y + this.getSize().y > v1.func_78328_b()) {
            /*SL:114*/this.getPos().y = v1.func_78328_b() - this.getSize().y;
        }
    }
    
    public void setDragging(final boolean a1) {
        /*SL:120*/this.dragging = a1;
    }
    
    public boolean shouldRender() {
        /*SL:125*/return this.isVisible() || MoveableHudElement.mc.field_71462_r instanceof GuiEditHud;
    }
}
