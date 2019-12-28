package com.ares.hack.hacks.render;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import com.ares.hack.settings.settings.BooleanSetting;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Anti Overlay", description = "Prevents Overlay", category = EnumCategory.RENDER)
public class AntiOverlay extends BaseHack
{
    private Setting<Boolean> allowFire;
    private Setting<Boolean> allowBlocks;
    private Setting<Boolean> allowWater;
    
    public AntiOverlay() {
        this.allowFire = new BooleanSetting("Fire", this, true);
        this.allowBlocks = new BooleanSetting("Blocks", this, true);
        this.allowWater = new BooleanSetting("Water", this, true);
    }
    
    @SubscribeEvent
    public void renderBlockOverlay(final RenderBlockOverlayEvent a1) {
        /*SL:37*/if (!this.getEnabled()) {
            return;
        }
        boolean v1 = /*EL:39*/false;
        /*SL:40*/switch (a1.getOverlayType()) {
            case FIRE: {
                /*SL:43*/if (this.allowFire.getValue()) {
                    v1 = true;
                    break;
                }
                break;
            }
            case BLOCK: {
                /*SL:46*/if (this.allowBlocks.getValue()) {
                    v1 = true;
                    break;
                }
                break;
            }
            case WATER: {
                /*SL:49*/if (this.allowWater.getValue()) {
                    v1 = true;
                    break;
                }
                break;
            }
        }
        /*SL:52*/a1.setCanceled(v1);
    }
}
