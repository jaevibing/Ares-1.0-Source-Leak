package com.ares.hack.hacks.render;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import com.ares.event.client.render.RenderItemFromModel;
import com.ares.event.client.render.RenderItemInFirstPerson;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import com.ares.hack.settings.settings.number.FloatSetting;
import com.ares.hack.settings.settings.number.DoubleSetting;
import com.ares.hack.settings.settings.EnumSetting;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "No Hands", description = "Dont render your hands", category = EnumCategory.RENDER)
public class NoHands extends BaseHack
{
    private Setting<Blacklist> blacklist;
    private Setting<Double> mainOffset;
    private Setting<Double> offOffset;
    private Setting<Float> transparency;
    
    public NoHands() {
        this.blacklist = new EnumSetting<Blacklist>("Blacklist", this, Blacklist.NOHANDS);
        this.mainOffset = new DoubleSetting("Mainhand Offset", this, 1.0, 0.0, 2.0);
        this.offOffset = new DoubleSetting("Offhand Offset", this, 1.0, 0.0, 2.0);
        this.transparency = new FloatSetting("Transparency", this, 1.0f, 0.0f, 1.0f);
    }
    
    @SubscribeEvent
    public void onRenderHand(final RenderSpecificHandEvent a1) {
        /*SL:38*/if (this.getEnabled()) {
            /*SL:40*/if (this.blacklist.getValue() == Blacklist.NOHANDS) {
                /*SL:42*/a1.setCanceled(true);
            }
            /*SL:45*/if (this.blacklist.getValue() == Blacklist.NOLEFT && a1.getHand() == EnumHand.OFF_HAND) {
                /*SL:47*/a1.setCanceled(true);
            }
            /*SL:50*/if (this.blacklist.getValue() == Blacklist.NORIGHT && a1.getHand() == EnumHand.MAIN_HAND) {
                /*SL:52*/a1.setCanceled(true);
            }
        }
    }
    
    @SubscribeEvent
    public void renderItemInFirstPerson(final RenderItemInFirstPerson a1) {
        /*SL:60*/if (this.getEnabled()) {
            /*SL:62*/if (a1.hand == EnumHand.MAIN_HAND) {
                /*SL:64*/a1.equipProgress = (float)(Object)this.mainOffset.getValue() - 1.0f;
            }
            /*SL:66*/if (a1.hand == EnumHand.OFF_HAND) {
                /*SL:68*/a1.equipProgress = (float)(Object)this.offOffset.getValue() - 1.0f;
            }
            /*SL:73*/this.doTransparency();
        }
    }
    
    @SubscribeEvent
    public void onRenderItemFromModel(final RenderItemFromModel a1) {
        /*SL:80*/if (this.getEnabled()) {
            /*SL:82*/this.doTransparency();
        }
    }
    
    private void doTransparency() {
        /*SL:88*/GL11.glEnable(3042);
        /*SL:89*/GL11.glBlendFunc(770, 771);
        final FloatBuffer v1 = /*EL:91*/BufferUtils.createFloatBuffer(16);
        /*SL:92*/GL11.glGetFloat(2816, v1);
        /*SL:94*/GL11.glColor4f(v1.get(0), v1.get(1), v1.get(2), (float)this.transparency.getValue());
    }
    
    enum Blacklist
    {
        NOHANDS, 
        NOLEFT, 
        NORIGHT, 
        ALL;
    }
}
