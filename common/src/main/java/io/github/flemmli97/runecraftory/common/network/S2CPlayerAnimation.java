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

public class S2CPlayerAnimation implements CustomPacketPayload {

    public static final Type<S2CPlayerAnimation> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RuneCraftory.MODID, "s2c_player_animation"));
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CPlayerAnimation> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CPlayerAnimation decode(RegistryFriendlyByteBuf buf) {
            return new S2CPlayerAnimation(buf.readInt(), buf.readUtf(), buf.readInt(), buf.readInt(), buf.readDouble());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CPlayerAnimation pkt) {
            buf.writeInt(pkt.entityID);
            buf.writeUtf(pkt.animID);
            buf.writeInt(pkt.startTransition);
            buf.writeInt(pkt.endTransition);
            buf.writeDouble(pkt.start);
        }
    };

    private final int entityID;
    private final String animID;
    private final double start;

    private final int startTransition, endTransition;

    private S2CPlayerAnimation(int entityID, String animID, int startTransition, int endTransition, double start) {
        this.entityID = entityID;
        this.animID = animID;
        this.start = start;
        this.startTransition = startTransition;
        this.endTransition = endTransition;
    }

    public static S2CPlayerAnimation create(Player player, int startTransition, int endTransition, double start) {
        return new S2CPlayerAnimation(player, startTransition, endTransition, start);
    }

    private S2CPlayerAnimation(Player player, int startTransition, int endTransition, double start) {
        this.entityID = player.getId();
        this.start = start;
        this.startTransition = startTransition;
        this.endTransition = endTransition;
        AnimationHandler<?> handler = RunecraftoryAttachments.PLAYER_DATA.get().get(player).getWeaponHandler()
                .getAnimationHandler();
        AnimationState state = handler.getAnimation();
        this.animID = state != null ? state.getID() : "";
        if (!this.animID.isEmpty() && handler.getAnimations().get(this.animID) == null) {
            RuneCraftory.LOGGER.error("This animation is not registered for {}. Registered animations are {} but set animation is {}", player, handler.getAnimations().all(), this.animID);
        }
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
            handler.setAnimation(pkt.animID.isEmpty() ? null : handler.getAnimations().get(pkt.animID),
                    pkt.startTransition, pkt.endTransition, pkt.start);
        }
    }
}