package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.components.NPCSpawnData;
import io.github.flemmli97.runecraftory.common.items.creative.NPCSpawnEgg;
import io.github.flemmli97.runecraftory.common.items.creative.RuneCraftoryEggItem;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryDataComponentTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class C2SSpawnEgg implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<C2SSpawnEgg> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("c2s_spawn_egg"));

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SSpawnEgg> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public C2SSpawnEgg decode(RegistryFriendlyByteBuf buf) {
            return new C2SSpawnEgg(buf.readEnum(InteractionHand.class), buf.readInt(), buf.readBoolean() ? buf.readResourceLocation() : null);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, C2SSpawnEgg pkt) {
            buf.writeEnum(pkt.hand);
            buf.writeInt(pkt.level);
            buf.writeBoolean(pkt.npcID != null);
            if (pkt.npcID != null)
                buf.writeResourceLocation(pkt.npcID);
        }
    };

    private final InteractionHand hand;
    private final int level;
    private final ResourceLocation npcID;

    public C2SSpawnEgg(InteractionHand hand, int level, @Nullable ResourceLocation npcID) {
        this.hand = hand;
        this.level = level;
        this.npcID = npcID;
    }

    public static void handle(C2SSpawnEgg pkt, ServerPlayer sender) {
        ItemStack stack = sender.getItemInHand(pkt.hand);
        if (stack.getItem() instanceof RuneCraftoryEggItem) {
            stack.set(RuneCraftoryDataComponentTypes.SPAWN_EGG_LEVEL.get(), Math.max(1, pkt.level));
            if (stack.getItem() instanceof NPCSpawnEgg) {
                NPCSpawnData data = stack.getOrDefault(RuneCraftoryDataComponentTypes.NPC_SPAWN_DATA.get(), NPCSpawnData.DEFAULT);
                stack.set(RuneCraftoryDataComponentTypes.NPC_SPAWN_DATA.get(), data.withId(pkt.npcID));
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
