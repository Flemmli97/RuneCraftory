package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.attachment.player.LevelExpPair;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public class S2CSkillLevelPkt implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CSkillLevelPkt> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_skill_level"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CSkillLevelPkt> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CSkillLevelPkt decode(RegistryFriendlyByteBuf buf) {
            return new S2CSkillLevelPkt(buf.readEnum(EnumSkills.class), new LevelExpPair(buf), buf.readInt(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CSkillLevelPkt pkt) {
            buf.writeEnum(pkt.skill);
            pkt.level.toPacket(buf);
            buf.writeInt(pkt.rp);
            buf.writeFloat(pkt.rpMax);
            buf.writeFloat(pkt.str);
            buf.writeFloat(pkt.intel);
            buf.writeFloat(pkt.vit);
        }
    };

    private final EnumSkills skill;
    private final LevelExpPair level;
    private final int rp;
    private final float rpMax;
    private final float str;
    private final float intel;
    private final float vit;

    private S2CSkillLevelPkt(EnumSkills skill, LevelExpPair xp, int rp, float rpMax, float str, float intel, float vit) {
        this.skill = skill;
        this.level = xp;
        this.rp = rp;
        this.rpMax = rpMax;
        this.str = str;
        this.intel = intel;
        this.vit = vit;
    }

    public S2CSkillLevelPkt(PlayerData cap, EnumSkills skill) {
        this.skill = skill;
        this.level = cap.getSkillLevel(skill);
        this.rp = cap.getRunePoints();
        this.rpMax = cap.getMaxRunePointsRaw();
        this.str = cap.getStr();
        this.intel = cap.getIntel();
        this.vit = cap.getVit();
    }

    public static void handle(S2CSkillLevelPkt pkt, Player player) {
        if (player == null)
            return;
        PlayerData data = Platform.INSTANCE.getPlayerData(player);
        data.getSkillLevel(pkt.skill).from(pkt.level);
        data.setRunePoints(pkt.rp);
        data.setMaxRunePoints(pkt.rpMax);
        data.setStr(pkt.str);
        data.setIntel(pkt.intel);
        data.setVit(pkt.vit);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
