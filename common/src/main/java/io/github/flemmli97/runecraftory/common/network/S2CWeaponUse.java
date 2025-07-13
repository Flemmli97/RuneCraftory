package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.common.registry.ModAttackActions;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class S2CWeaponUse implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CWeaponUse> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_weapon_use"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CWeaponUse> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CWeaponUse decode(RegistryFriendlyByteBuf buf) {
            return new S2CWeaponUse(ByteBufCodecs.registry(ModAttackActions.ATTACK_ACTION_KEY).decode(buf), ItemStack.OPTIONAL_STREAM_CODEC.decode(buf), buf.readInt(), buf.readInt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CWeaponUse pkt) {
            ByteBufCodecs.registry(ModAttackActions.ATTACK_ACTION_KEY).encode(buf, pkt.action);
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, pkt.stack);
            buf.writeInt(pkt.count);
            buf.writeInt(pkt.entity);
        }
    };

    private final AttackAction action;
    private final ItemStack stack;
    private final int count, entity;

    public S2CWeaponUse(AttackAction action, ItemStack hand, int count, LivingEntity entity) {
        this.action = action;
        this.stack = hand;
        this.count = count;
        this.entity = entity.getId();
    }

    private S2CWeaponUse(AttackAction action, ItemStack hand, int count, int entity) {
        this.action = action;
        this.stack = hand;
        this.count = count;
        this.entity = entity;
    }

    public static void handle(S2CWeaponUse pkt, Player client) {
        Entity target = client.level().getEntity(pkt.entity);
        if (target instanceof Player player)
            Platform.INSTANCE.getPlayerData(player).getWeaponHandler().clientSideUpdate(pkt.action, pkt.stack, pkt.count);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
