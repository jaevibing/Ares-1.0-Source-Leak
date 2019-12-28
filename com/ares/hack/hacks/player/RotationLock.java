package com.ares.hack.hacks.player;

import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import com.ares.event.client.movement.TurnPlayerFromMouseInput;
import com.ares.hack.settings.settings.EnumSetting;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Rotation Lock", description = "Lock your rotation", category = EnumCategory.PLAYER)
public class RotationLock extends BaseHack
{
    private final Setting<Facing> facing;
    
    public RotationLock() {
        this.facing = new EnumSetting<Facing>("Facing", this, Facing.NORTH);
    }
    
    public void onEnabled() {
        RotationLock.mc.field_71439_g.func_70034_d(/*EL:28*/(float)this.getYaw());
    }
    
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onTurnPlayer(final TurnPlayerFromMouseInput a1) {
        /*SL:48*/if (this.getEnabled() && a1.entity instanceof EntityPlayerSP) {
            /*SL:50*/a1.setCanceled(true);
        }
    }
    
    public void onLogic() {
        /*SL:57*/if (this.getEnabled()) {
            RotationLock.mc.field_71439_g.field_70177_z = /*EL:59*/this.getYaw();
        }
    }
    
    private int getYaw() {
        int v1 = 0;
        /*SL:67*/switch (this.facing.getValue()) {
            default: {
                /*SL:71*/v1 = 0;
                /*SL:72*/break;
            }
            case EAST: {
                /*SL:74*/v1 = 90;
                /*SL:75*/break;
            }
            case SOUTH: {
                /*SL:77*/v1 = 180;
                /*SL:78*/break;
            }
            case WEST: {
                /*SL:80*/v1 = -90;
                break;
            }
        }
        /*SL:84*/return v1;
    }
    
    enum Facing
    {
        NORTH, 
        EAST, 
        SOUTH, 
        WEST;
    }
}
