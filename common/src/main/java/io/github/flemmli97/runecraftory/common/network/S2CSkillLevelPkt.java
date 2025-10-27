package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.attachment.player.XpLevelHolder;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.tenshilib.loader.registry.AttachmentRegister;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public class S2CSkillLevelPkt implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CSkillLevelPkt> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_skill_level"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CSkillLevelPkt> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CSkillLevelPkt decode(RegistryFriendlyByteBuf buf) {
            return new S2CSkillLevelPkt(buf.readEnum(Skills.class), new XpLevelHolder(buf), buf.readInt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CSkillLevelPkt pkt) {
            buf.writeEnum(pkt.skill);
            pkt.level.toPacket(buf);
            buf.writeInt(pkt.rp);
        }
    };

    private final Skills skill;
    private final XpLevelHolder level;
    private final int rp;

    private S2CSkillLevelPkt(Skills skill, XpLevelHolder xp, int rp) {
        this.skill = skill;
        this.level = xp;
        this.rp = rp;
    }

    public S2CSkillLevelPkt(PlayerData cap, Skills skill) {
        this.skill = skill;
        this.level = cap.getSkillLevel(skill);
        this.rp = cap.getRunePoints();
    }

    public static void handle(S2CSkillLevelPkt pkt, Player player) {
        if (player == null)
            return;
        PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(player);
        data.getSkillLevel(pkt.skill).from(pkt.level);
        data.setRunePoints(pkt.rp);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
