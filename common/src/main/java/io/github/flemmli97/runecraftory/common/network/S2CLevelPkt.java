package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.attachment.player.LevelExpPair;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class S2CLevelPkt implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_level");

    private final LevelExpPair level;
    private final int rp;
    private final float rpMax;
    private final float str;
    private final float intel;
    private final float vit;

    private S2CLevelPkt(LevelExpPair xp, int rp, float rpMax, float str, float intel, float vit) {
        this.level = xp;
        this.rp = rp;
        this.rpMax = rpMax;
        this.str = str;
        this.intel = intel;
        this.vit = vit;
    }

    public S2CLevelPkt(PlayerData data) {
        this.level = data.getPlayerLevel();
        this.rp = data.getRunePoints();
        this.rpMax = data.getMaxRunePointsRaw();
        this.str = data.getStr();
        this.intel = data.getIntel();
        this.vit = data.getVit();
    }

    public static S2CLevelPkt read(FriendlyByteBuf buf) {
        return new S2CLevelPkt(new LevelExpPair(buf), buf.readInt(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
    }

    public static void handle(S2CLevelPkt pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
            data.getPlayerLevel().from(pkt.level);
            data.setRunePoints(player, pkt.rp);
            data.setMaxRunePoints(player, pkt.rpMax);
            data.setStr(player, pkt.str);
            data.setIntel(player, pkt.intel);
            data.setVit(player, pkt.vit);
        });
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        this.level.toPacket(buf);
        buf.writeInt(this.rp);
        buf.writeFloat(this.rpMax);
        buf.writeFloat(this.str);
        buf.writeFloat(this.intel);
        buf.writeFloat(this.vit);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

}
