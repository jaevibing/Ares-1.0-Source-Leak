package com.ares.hack.hacks.player;

import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.server.SPacketChat;
import com.ares.event.packet.PacketRecieved;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketTabComplete;
import java.util.UUID;
import net.minecraftforge.common.ForgeHooks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import java.time.temporal.Temporal;
import java.time.Duration;
import com.ares.hack.settings.settings.number.IntegerSetting;
import com.ares.hack.settings.settings.BooleanSetting;
import com.ares.hack.settings.Setting;
import java.time.Instant;
import java.util.Random;
import java.util.regex.Pattern;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "AntiAFK", description = "prevent being afk kicked", category = EnumCategory.PLAYER)
public class AntiAFK extends BaseHack
{
    private static final Pattern whisperPattern;
    private final Random random;
    private Instant lastRan;
    private Instant lastKey;
    private Setting<Boolean> autoReply;
    private Setting<Integer> delay;
    
    public AntiAFK() {
        this.random = new Random();
        this.lastRan = Instant.EPOCH;
        this.lastKey = Instant.EPOCH;
        this.autoReply = new BooleanSetting("Auto Reply", this, true);
        this.delay = new IntegerSetting("Delay ms", this, 1000, 100, 5000);
    }
    
    @Override
    protected void onLogic() {
        /*SL:46*/if (this.getEnabled()) {
            /*SL:48*/if (Duration.between(this.lastRan, Instant.now()).toMillis() >= this.delay.getValue()) {
                /*SL:50*/this.lastRan = Instant.now();
                /*SL:52*/switch (this.random.nextInt(10)) {
                    case 0: {
                        AntiAFK.mc.field_71439_g.field_71174_a.func_147297_a(/*EL:55*/(Packet)new CPacketPlayer.Rotation(this.random.nextBoolean() ? /*EL:56*/((float)this.random.nextInt(90)) : ((float)(-this.random.nextInt(90))), this.random.nextBoolean() ? /*EL:57*/((float)this.random.nextInt(90)) : ((float)(-this.random.nextInt(90))), AntiAFK.mc.field_71439_g.field_70122_E));
                        /*SL:60*/break;
                    }
                    case 1: {
                        AntiAFK.mc.field_71439_g.field_71174_a.func_147297_a(/*EL:62*/(Packet)new CPacketAnimation(this.random.nextBoolean() ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND));
                        /*SL:63*/break;
                    }
                    case 2: {
                        /*SL:65*/if (AntiAFK.mc.field_71476_x != null) {
                            /*SL:67*/switch (AntiAFK.mc.field_71476_x.field_72313_a) {
                                case ENTITY: {
                                    AntiAFK.mc.field_71442_b.func_78764_a((EntityPlayer)AntiAFK.mc.field_71439_g, AntiAFK.mc.field_71476_x.field_72308_g);
                                    /*SL:71*/break;
                                }
                                case BLOCK: {
                                    final BlockPos v1 = AntiAFK.mc.field_71476_x.func_178782_a();
                                    /*SL:75*/if (!AntiAFK.mc.field_71441_e.func_175623_d(v1)) {
                                        AntiAFK.mc.field_71442_b.func_180511_b(/*EL:77*/v1, AntiAFK.mc.field_71476_x.field_178784_b);
                                        /*SL:78*/break;
                                    }
                                }
                                case MISS: {
                                    /*SL:81*/ForgeHooks.onEmptyLeftClick((EntityPlayer)AntiAFK.mc.field_71439_g);
                                    AntiAFK.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
                                    break;
                                }
                            }
                            /*SL:82*/break;
                        }
                        break;
                    }
                    case 3: {
                        AntiAFK.mc.field_71439_g.func_70095_a(/*EL:87*/this.random.nextBoolean());
                        /*SL:88*/break;
                    }
                    case 4: {
                        AntiAFK.mc.field_71439_g.field_71174_a.func_147297_a(/*EL:90*/(Packet)new CPacketTabComplete("/" + /*EL:92*/UUID.randomUUID().toString().replace('-', 'v'), AntiAFK.mc.field_71439_g.func_180425_c(), /*EL:93*/false));
                        /*SL:97*/break;
                    }
                    case 5: {
                        AntiAFK.mc.field_71439_g.func_70664_aZ();
                        /*SL:100*/break;
                    }
                    case 6: {
                        AntiAFK.mc.field_71439_g.func_71165_d(/*EL:102*/"/" + UUID.randomUUID().toString().replace('-', 'v'));
                        /*SL:103*/break;
                    }
                    case 7: {
                        AntiAFK.mc.field_71439_g.field_71174_a.func_147297_a(/*EL:105*/(Packet)new CPacketClickWindow(1, 1, 1, ClickType.CLONE, new ItemStack(Blocks.field_150357_h), (short)1));
                        /*SL:106*/break;
                    }
                    case 8: {
                        AntiAFK.mc.field_71439_g.field_71174_a.func_147297_a(/*EL:108*/(Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.SWAP_HELD_ITEMS, AntiAFK.mc.field_71439_g.func_180425_c(), EnumFacing.DOWN));
                        /*SL:109*/break;
                    }
                    case 9: {
                        AntiAFK.mc.field_71439_g.field_71174_a.func_147297_a(/*EL:111*/(Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, AntiAFK.mc.field_71439_g.func_180425_c(), EnumFacing.DOWN));
                        /*SL:112*/break;
                    }
                    case 10: {
                        final int v2 = /*EL:114*/this.random.nextInt(9);
                        AntiAFK.mc.field_71439_g.field_71071_by.field_70461_c = /*EL:115*/v2;
                        AntiAFK.mc.field_71439_g.field_71174_a.func_147297_a(/*EL:116*/(Packet)new CPacketHeldItemChange(v2));
                        break;
                    }
                }
            }
            /*SL:121*/if (Duration.between(this.lastKey, Instant.now()).getSeconds() >= 6L) {
                /*SL:123*/this.lastKey = Instant.now();
                /*SL:124*/KeyBinding.func_74510_a(AntiAFK.mc.field_71474_y.field_74370_x.func_151463_i(), this.random.nextBoolean());
                /*SL:125*/KeyBinding.func_74510_a(AntiAFK.mc.field_71474_y.field_74351_w.func_151463_i(), this.random.nextBoolean());
                /*SL:126*/KeyBinding.func_74510_a(AntiAFK.mc.field_71474_y.field_74366_z.func_151463_i(), this.random.nextBoolean());
                /*SL:127*/KeyBinding.func_74510_a(AntiAFK.mc.field_71474_y.field_74368_y.func_151463_i(), this.random.nextBoolean());
            }
        }
    }
    
    @SubscribeEvent
    public void onPacket(final PacketRecieved v-1) {
        /*SL:135*/if (this.getEnabled() && this.autoReply.getValue() && v-1.packet instanceof SPacketChat) {
            final SPacketChat a1 = /*EL:137*/(SPacketChat)v-1.packet;
            final String v1 = /*EL:138*/a1.func_148915_c().func_150260_c();
            /*SL:140*/if (AntiAFK.whisperPattern.matcher(v1).matches()) {
                AntiAFK.mc.field_71439_g.field_71174_a.func_147297_a(/*EL:142*/(Packet)new CPacketChatMessage("/r I am currently afk but still online thanks to Ares\u2122 always online technology"));
            }
        }
    }
    
    static {
        whisperPattern = Pattern.compile("/^([A-z_])+ whispers.*/gm");
    }
}
