package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class S2CPlayerAnimation implements CustomPacketPayload {

    public static final Type<S2CPlayerAnimation> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RuneCraftory.MODID, "s2c_player_animation"));
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CPlayerAnimation> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CPlayerAnimation decode(RegistryFriendlyByteBuf buf) {
            return new S2CPlayerAnimation(buf.readInt(), buf.readBoolean() ? AnimationState.SyncableState.STREAM_CODEC.decode(buf) : null);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CPlayerAnimation pkt) {
            buf.writeInt(pkt.entityID);
            buf.writeBoolean(pkt.state != null);
            if (pkt.state != null) {
                AnimationState.SyncableState.STREAM_CODEC.encode(buf, pkt.state);
            }
        }
    };

    private final int entityID;
    private final AnimationState.SyncableState state;

    private S2CPlayerAnimation(int entityID, @Nullable AnimationState.SyncableState state) {
        this.entityID = entityID;
        this.state = state;
    }

    private S2CPlayerAnimation(Player player, @Nullable AnimationState.SyncableState state) {
        this(player.getId(), state);
        AnimationHandler<?> handler = RunecraftoryAttachments.PLAYER_DATA.get().get(player).getWeaponHandler()
                .getAnimationHandler();
        if (state != null && handler.getAnimations().get(state.id()) == null) {
            RuneCraftory.LOGGER.error("This animation is not registered for {}. Registered animations are {} but set animation is {}", player, handler.getAnimations().all(), state.id());
        }
    }

    public static S2CPlayerAnimation create(Player player) {
        AnimationState state = RunecraftoryAttachments.PLAYER_DATA.get().get(player).getWeaponHandler()
                .getAnimationHandler().getAnimation();
        return new S2CPlayerAnimation(player, state != null ? state.forSync() : null);
    }

    public static S2CPlayerAnimation create(Player player, double offset) {
        AnimationState state = RunecraftoryAttachments.PLAYER_DATA.get().get(player).getWeaponHandler()
                .getAnimationHandler().getAnimation();
        return new S2CPlayerAnimation(player, state != null ? state.forSync().withOffset(offset) : null);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(S2CPlayerAnimation pkt, Player player) {
        Entity entity = player.level().getEntity(pkt.entityID);
        if (entity instanceof Player target) {
            AnimationHandler<?> handler = RunecraftoryAttachments.PLAYER_DATA.get().get(target).getWeaponHandler()
                    .getAnimationHandler();
            if (pkt.state == null) {
                handler.setAnimationDef(null);
            } else {
                handler.setAnimation(handler.getAnimations().get(pkt.state.id()),
                        pkt.state.startTransition(), pkt.state.endTransition(), pkt.state.offset(), pkt.state.speed());
            }
        }
    }
}