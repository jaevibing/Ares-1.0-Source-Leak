package com.ares.hack.hacks.player;

import java.util.Iterator;
import java.util.Collection;
import net.minecraft.world.chunk.Chunk;
import com.google.common.collect.UnmodifiableIterator;
import com.ares.utils.chat.ChatUtils;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Date;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.server.SPacketChunkData;
import com.ares.event.packet.PacketRecieved;
import net.minecraft.tileentity.TileEntity;
import java.util.List;
import java.io.File;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Chunk Logger", description = "Log chunks that contain a specified ammount of chest", category = EnumCategory.PLAYER)
public class ChunkLogger extends BaseHack
{
    private File file;
    
    public ChunkLogger() {
        this.file = new File("Ares/ChunkLogs.txt");
    }
    
    public void onEnabled() {
        /*SL:30*/this.setEnabled(false);
    }
    
    public void onLogic() {
        final List<TileEntity> v1 = (List<TileEntity>)ChunkLogger.mc.field_71441_e.field_147482_g;
        /*SL:38*/v1.stream().filter(a1 -> ChunkLogger.mc.field_71439_g.func_70011_f((double)a1.func_174877_v().func_177958_n(), ChunkLogger.mc.field_71439_g.field_70163_u, (double)a1.func_174877_v().func_177952_p()) < /*EL:42*/500.0);
    }
    
    @SubscribeEvent
    public void onPacket(final PacketRecieved a1) {
        /*SL:47*/if (a1.packet instanceof SPacketChunkData) {
            final SPacketChunkData sPacketChunkData = /*EL:49*/(SPacketChunkData)a1.packet;
        }
    }
    
    @SubscribeEvent
    public void onLoadChunk(final ChunkEvent.Load v-7) {
        /*SL:57*/if (!this.getEnabled()) {
            return;
        }
        /*SL:59*/for (final ChunkPos chunkPos : ChunkLogger.mc.field_71441_e.getPersistentChunks().keys()) {
            final Chunk func_72964_e = ChunkLogger.mc.field_71441_e.func_72964_e(/*EL:61*/chunkPos.field_77276_a, chunkPos.field_77275_b);
            final Collection<TileEntity> values = /*EL:63*/func_72964_e.func_177434_r().values();
            System.out.println(/*EL:67*/func_72964_e.field_76635_g * 16 + " " + func_72964_e.field_76647_h * 16);
            System.out.println(/*EL:68*/func_72964_e.func_177434_r().size());
            System.out.println(/*EL:69*/func_72964_e.func_177434_r().values().size());
            /*SL:71*/if (func_72964_e.func_177434_r().size() < 1) {
                return;
            }
            int n = /*EL:73*/0;
            System.out.println(/*EL:74*/"tiles: " + func_72964_e.func_177434_r().size());
            /*SL:75*/for (final TileEntity a1 : values) {
                System.out.println(/*EL:77*/a1 instanceof TileEntityChest);
                System.out.println(/*EL:78*/a1.func_174877_v());
                /*SL:79*/if (a1 instanceof TileEntityChest) {
                    /*SL:81*/++n;
                }
            }
            int n2 = /*EL:93*/0;
            /*SL:94*/for (final TileEntity v1 : values) {
                /*SL:96*/if (v1 instanceof TileEntityBed) {
                    /*SL:98*/++n2;
                }
            }
            boolean v2 = /*EL:110*/false;
            /*SL:111*/for (final TileEntity v3 : values) {
                /*SL:113*/if (v3 instanceof TileEntityEndPortal) {
                    /*SL:115*/v2 = true;
                    /*SL:116*/break;
                }
            }
            System.out.printf(/*EL:120*/"\nChunk Loaded %d %d %s", n, n2, String.valueOf(v2));
            /*SL:122*/if (n <= 0 && n2 <= 0 && !v2) {
                continue;
            }
            final long v4 = /*EL:124*/System.currentTimeMillis();
            final Date v5 = /*EL:125*/new Date(v4);
            String v6 = /*EL:127*/"Singleplayer";
            /*SL:128*/if (ChunkLogger.mc.func_147104_D() != null) {
                /*SL:130*/v6 = ChunkLogger.mc.func_147104_D().field_78845_b;
            }
            final String v7 = /*EL:133*/String.format("(%s) %s %s: %d chests, %d beds, %d end portals", v5, v6, "(" + func_72964_e.field_76635_g * 16 + ", " + func_72964_e.field_76647_h * 16 + ")", n, /*EL:138*/n2, /*EL:139*/(int)(v2 ? 1 : 0));
            try {
                final FileWriter v8 = /*EL:145*/new FileWriter(this.file, true);
                final BufferedWriter v9 = /*EL:146*/new BufferedWriter(v8);
                /*SL:147*/v9.write(v7 + "\n");
                /*SL:149*/v9.close();
                /*SL:150*/v8.close();
                System.out.println(/*EL:152*/"Found Chunk " + v7);
                /*SL:153*/ChatUtils.printMessage("Found Chunk " + v7);
            }
            catch (Exception v10) {
                System.out.println(/*EL:157*/v7);
                /*SL:158*/v10.printStackTrace();
            }
        }
    }
}
