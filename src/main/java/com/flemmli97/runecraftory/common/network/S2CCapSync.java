package com.flemmli97.runecraftory.common.network;

import com.flemmli97.runecraftory.api.enums.EnumSkills;
import com.flemmli97.runecraftory.client.ClientHandlers;
import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.flemmli97.runecraftory.common.capability.IPlayerCap;
import com.google.common.collect.Maps;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Map;
import java.util.function.Supplier;

public class S2CCapSync {

    private int money;
    private int runePoints;
    private int runePointsMax;
    private float str;
    private float vit;
    private float intel;
    private int[] level = new int[]{1, 0};

    private Map<EnumSkills, int[]> skillMap = Maps.newHashMap();
    private CompoundNBT spells;

    private CompoundNBT foodData;

    private S2CCapSync() {
    }

    public S2CCapSync(IPlayerCap cap) {
        this.money = cap.getMoney();
        this.runePoints = cap.getRunePoints();
        this.runePointsMax = cap.getMaxRunePoints();
        this.str = cap.getStr();
        this.intel = cap.getIntel();
        this.vit = cap.getVit();
        this.level = cap.getPlayerLevel();
        for (EnumSkills skill : EnumSkills.values())
            this.skillMap.put(skill, cap.getSkillLevel(skill));
        this.spells = cap.getInv().writeToNBT(new CompoundNBT());
        this.foodData = cap.foodBuffNBT();
    }

    public static S2CCapSync read(PacketBuffer buf) {
        S2CCapSync pkt = new S2CCapSync();
        pkt.money = buf.readInt();
        pkt.runePoints = buf.readInt();
        pkt.runePointsMax = buf.readInt();
        pkt.str = buf.readFloat();
        pkt.intel = buf.readFloat();
        pkt.vit = buf.readFloat();
        pkt.level = new int[]{buf.readInt(), buf.readInt()};
        int l = buf.readInt();
        for (int i = 0; i < l; i++) {
            EnumSkills skill = buf.readEnumValue(EnumSkills.class);
            pkt.skillMap.put(skill, new int[]{buf.readInt(), buf.readInt()});
        }
        pkt.spells = buf.readCompoundTag();
        pkt.foodData = buf.readCompoundTag();
        return pkt;
    }

    public static void write(S2CCapSync pkt, PacketBuffer buf) {
        buf.writeInt(pkt.money);
        buf.writeInt(pkt.runePoints);
        buf.writeInt(pkt.runePointsMax);
        buf.writeFloat(pkt.str);
        buf.writeFloat(pkt.intel);
        buf.writeFloat(pkt.vit);
        buf.writeInt(pkt.level[0]);
        buf.writeInt(pkt.level[1]);
        buf.writeInt(EnumSkills.values().length);
        for (EnumSkills skill : EnumSkills.values()) {
            buf.writeEnumValue(skill);
            int[] i = pkt.skillMap.getOrDefault(skill, new int[]{1, 0});
            buf.writeInt(i[0]);
            buf.writeInt(i[1]);
        }
        buf.writeCompoundTag(pkt.spells);
        buf.writeCompoundTag(pkt.foodData);
    }

    public static void handle(S2CCapSync pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHandlers::getPlayer);
            if (player == null)
                return;
            player.getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> {
                cap.setMoney(player, pkt.money);
                cap.setRunePoints(player, pkt.runePoints);
                cap.setMaxRunePoints(player, pkt.runePointsMax);
                cap.setStr(player, pkt.str);
                cap.setVit(player, pkt.vit);
                cap.setIntel(player, pkt.intel);
                cap.setPlayerLevel(player, pkt.level[0], pkt.level[1]);
                pkt.skillMap.forEach((skill, val) -> cap.setSkillLevel(skill, player, val[0], val[1]));
                cap.getInv().readFromNBT(pkt.spells);
                cap.readFoodBuffFromNBT(pkt.foodData);
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
