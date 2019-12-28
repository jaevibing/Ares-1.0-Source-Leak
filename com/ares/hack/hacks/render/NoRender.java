package com.ares.hack.hacks.render;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.network.play.server.SPacketExplosion;
import com.ares.event.packet.PacketRecieved;
import com.ares.hack.settings.settings.BooleanSetting;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "No Render", description = "Dont render things", category = EnumCategory.RENDER)
public class NoRender extends BaseHack
{
    private Setting<Boolean> stopExplosions;
    private Setting<Boolean> stopParticles;
    private Setting<Boolean> HELMET;
    private Setting<Boolean> PORTAL;
    private Setting<Boolean> CROSSHAIRS;
    private Setting<Boolean> BOSSHEALTH;
    private Setting<Boolean> BOSSINFO;
    private Setting<Boolean> ARMOR;
    private Setting<Boolean> HEALTH;
    private Setting<Boolean> FOOD;
    private Setting<Boolean> AIR;
    private Setting<Boolean> HOTBAR;
    private Setting<Boolean> EXPERIENCE;
    private Setting<Boolean> TEXT;
    private Setting<Boolean> HEALTHMOUNT;
    private Setting<Boolean> JUMPBAR;
    private Setting<Boolean> CHAT;
    private Setting<Boolean> PLAYER_LIST;
    private Setting<Boolean> POTION_ICONS;
    private Setting<Boolean> SUBTITLES;
    private Setting<Boolean> FPS_GRAPH;
    private Setting<Boolean> VIGNETTE;
    
    public NoRender() {
        this.stopExplosions = new BooleanSetting("Stop Explosions", this, true);
        this.stopParticles = new BooleanSetting("Stop Particles", this, true);
        this.HELMET = new BooleanSetting("helmet", this, false);
        this.PORTAL = new BooleanSetting("portal", this, false);
        this.CROSSHAIRS = new BooleanSetting("crosshair", this, false);
        this.BOSSHEALTH = new BooleanSetting("bosshealth", this, false);
        this.BOSSINFO = new BooleanSetting("bossinfo", this, false);
        this.ARMOR = new BooleanSetting("armor", this, false);
        this.HEALTH = new BooleanSetting("health", this, false);
        this.FOOD = new BooleanSetting("food", this, false);
        this.AIR = new BooleanSetting("air", this, false);
        this.HOTBAR = new BooleanSetting("hotbar", this, false);
        this.EXPERIENCE = new BooleanSetting("experience", this, false);
        this.TEXT = new BooleanSetting("text", this, false);
        this.HEALTHMOUNT = new BooleanSetting("horse health", this, false);
        this.JUMPBAR = new BooleanSetting("horse jump", this, false);
        this.CHAT = new BooleanSetting("chat", this, false);
        this.PLAYER_LIST = new BooleanSetting("playerlist", this, false);
        this.POTION_ICONS = new BooleanSetting("potion icon", this, false);
        this.SUBTITLES = new BooleanSetting("subtitles", this, false);
        this.FPS_GRAPH = new BooleanSetting("fps graph", this, false);
        this.VIGNETTE = new BooleanSetting("vignette", this, false);
    }
    
    @SubscribeEvent
    public void onPacket(final PacketRecieved a1) {
        /*SL:43*/if (this.getEnabled() && this.stopExplosions.getValue() && a1.packet instanceof SPacketExplosion) {
            /*SL:45*/a1.setCanceled(true);
        }
        /*SL:48*/if (this.getEnabled() && this.stopParticles.getValue() && a1.packet instanceof SPacketParticles) {
            /*SL:50*/a1.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public void onOverlay(final RenderGameOverlayEvent a1) {
        /*SL:57*/switch (a1.getType()) {
            case HELMET: {
                /*SL:60*/if (this.HELMET.getValue()) {
                    a1.setCanceled(true);
                    break;
                }
                break;
            }
            case PORTAL: {
                /*SL:63*/if (this.PORTAL.getValue()) {
                    a1.setCanceled(true);
                    break;
                }
                break;
            }
            case CROSSHAIRS: {
                /*SL:66*/if (this.CROSSHAIRS.getValue()) {
                    a1.setCanceled(true);
                    break;
                }
                break;
            }
            case BOSSHEALTH: {
                /*SL:69*/if (this.BOSSHEALTH.getValue()) {
                    a1.setCanceled(true);
                    break;
                }
                break;
            }
            case BOSSINFO: {
                /*SL:72*/if (this.BOSSINFO.getValue()) {
                    a1.setCanceled(true);
                    break;
                }
                break;
            }
            case ARMOR: {
                /*SL:75*/if (this.ARMOR.getValue()) {
                    a1.setCanceled(true);
                    break;
                }
                break;
            }
            case HEALTH: {
                /*SL:78*/if (this.HEALTH.getValue()) {
                    a1.setCanceled(true);
                    break;
                }
                break;
            }
            case FOOD: {
                /*SL:81*/if (this.FOOD.getValue()) {
                    a1.setCanceled(true);
                    break;
                }
                break;
            }
            case AIR: {
                /*SL:84*/if (this.AIR.getValue()) {
                    a1.setCanceled(true);
                    break;
                }
                break;
            }
            case HOTBAR: {
                /*SL:87*/if (this.HOTBAR.getValue()) {
                    a1.setCanceled(true);
                    break;
                }
                break;
            }
            case EXPERIENCE: {
                /*SL:90*/if (this.EXPERIENCE.getValue()) {
                    a1.setCanceled(true);
                    break;
                }
                break;
            }
            case TEXT: {
                /*SL:93*/if (this.TEXT.getValue()) {
                    a1.setCanceled(true);
                    break;
                }
                break;
            }
            case HEALTHMOUNT: {
                /*SL:96*/if (this.HEALTHMOUNT.getValue()) {
                    a1.setCanceled(true);
                    break;
                }
                break;
            }
            case JUMPBAR: {
                /*SL:99*/if (this.JUMPBAR.getValue()) {
                    a1.setCanceled(true);
                    break;
                }
                break;
            }
            case CHAT: {
                /*SL:102*/if (this.CHAT.getValue()) {
                    a1.setCanceled(true);
                    break;
                }
                break;
            }
            case PLAYER_LIST: {
                /*SL:105*/if (this.PLAYER_LIST.getValue()) {
                    a1.setCanceled(true);
                    break;
                }
                break;
            }
            case POTION_ICONS: {
                /*SL:108*/if (this.POTION_ICONS.getValue()) {
                    a1.setCanceled(true);
                    break;
                }
                break;
            }
            case SUBTITLES: {
                /*SL:111*/if (this.SUBTITLES.getValue()) {
                    a1.setCanceled(true);
                    break;
                }
                break;
            }
            case FPS_GRAPH: {
                /*SL:114*/if (this.FPS_GRAPH.getValue()) {
                    a1.setCanceled(true);
                    break;
                }
                break;
            }
            case VIGNETTE: {
                /*SL:117*/if (this.VIGNETTE.getValue()) {
                    a1.setCanceled(true);
                    break;
                }
                break;
            }
        }
    }
}
