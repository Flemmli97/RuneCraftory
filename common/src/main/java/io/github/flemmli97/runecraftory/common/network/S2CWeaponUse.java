package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttackActions;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class S2CWeaponUse implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CWeaponUse> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_weapon_use"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CWeaponUse> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CWeaponUse decode(RegistryFriendlyByteBuf buf) {
            return new S2CWeaponUse(ByteBufCodecs.registry(RuneCraftoryAttackActions.ATTACK_ACTION_KEY).decode(buf), buf.readInt(), buf.readInt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CWeaponUse pkt) {
            ByteBufCodecs.registry(RuneCraftoryAttackActions.ATTACK_ACTION_KEY).encode(buf, pkt.action);
            buf.writeInt(pkt.count);
            buf.writeInt(pkt.entity);
        }
    };

    private final AttackAction action;
    private final int count, entity;

    public S2CWeaponUse(AttackAction action, int count, LivingEntity entity) {
        this.action = action;
        this.count = count;
        this.entity = entity.getId();
    }

    private S2CWeaponUse(AttackAction action, int count, int entity) {
        this.action = action;
        this.count = count;
        this.entity = entity;
    }

    public static void handle(S2CWeaponUse pkt, Player client) {
        Entity target = client.level().getEntity(pkt.entity);
        if (target instanceof Player player) {
            RunecraftoryAttachments.PLAYER_DATA.get().get(player).getWeaponHandler().updateState(pkt.action, pkt.count);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
