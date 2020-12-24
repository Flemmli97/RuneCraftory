package com.flemmli97.runecraftory.network;

import com.flemmli97.runecraftory.client.ClientHandlers;
import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.flemmli97.runecraftory.common.capability.IPlayerCap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CLevelPkt {

    private final Type type;
    private final int[] level;
    private int rp, rpMax;
    private float str, intel, vit;

    private S2CLevelPkt(Type type, int[] level) {
        this.type = type;
        this.level = level;
    }

    private S2CLevelPkt(Type type, int[] level, int rp, int rpMax, float str, float intel, float vit) {
        this(type, level);
        this.rp = rp;
        this.rpMax = rpMax;
        this.str = str;
        this.intel = intel;
        this.vit = vit;
    }

    public S2CLevelPkt(IPlayerCap cap, Type type) {
        this.type = type;
        this.level = cap.getPlayerLevel();
        if (type == Type.LEVELUP) {
            this.rp = cap.getRunePoints();
            this.rpMax = cap.getMaxRunePoints();
            this.str = cap.getStr();
            this.intel = cap.getIntel();
            this.vit = cap.getVit();
        }
    }

    public static S2CLevelPkt read(PacketBuffer buf) {
        Type type = buf.readEnumValue(Type.class);
        if (type == Type.SET)
            return new S2CLevelPkt(type, new int[]{buf.readInt(), buf.readInt()});
        return new S2CLevelPkt(type, new int[]{buf.readInt(), buf.readInt()}, buf.readInt(), buf.readInt(), buf.readFloat(), buf.readFloat(), buf.readFloat());
    }

    public static void write(S2CLevelPkt pkt, PacketBuffer buf) {
        buf.writeEnumValue(pkt.type);
        buf.writeInt(pkt.level[0]);
        buf.writeInt(pkt.level[1]);
        if (pkt.type == Type.LEVELUP) {
            buf.writeInt(pkt.rp);
            buf.writeInt(pkt.rpMax);
            buf.writeFloat(pkt.str);
            buf.writeFloat(pkt.intel);
            buf.writeFloat(pkt.vit);
        }
    }

    public static void handle(S2CLevelPkt pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHandlers::getPlayer);
            if (player == null)
                return;
            player.getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> {
                cap.setPlayerLevel(player, pkt.level[0], pkt.level[1]);
                if (pkt.type == S2CLevelPkt.Type.LEVELUP) {
                    cap.setRunePoints(player, pkt.rp);
                    cap.setMaxRunePoints(player, pkt.rpMax);
                    cap.setStr(player, pkt.str);
                    cap.setIntel(player, pkt.intel);
                    cap.setVit(player, pkt.vit);
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }

    public enum Type {
        SET,
        LEVELUP
    }
}
