package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.items.NPCSpawnEgg;
import io.github.flemmli97.runecraftory.common.items.RuneCraftoryEggItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class C2SSpawnEgg implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "c2s_spawn_egg");

    private final InteractionHand hand;
    private final int level;
    private final ResourceLocation npcID;

    public C2SSpawnEgg(InteractionHand hand, int level, @Nullable ResourceLocation npcID) {
        this.hand = hand;
        this.level = level;
        this.npcID = npcID;
    }

    public static C2SSpawnEgg read(FriendlyByteBuf buf) {
        return new C2SSpawnEgg(buf.readEnum(InteractionHand.class), buf.readInt(), buf.readBoolean() ? buf.readResourceLocation() : null);
    }

    public static void handle(C2SSpawnEgg pkt, ServerPlayer sender) {
        if (sender != null) {
            ItemStack stack = sender.getItemInHand(pkt.hand);
            if (stack.getItem() instanceof RuneCraftoryEggItem) {
                RuneCraftoryEggItem.setMobLevel(stack, pkt.level);
                if (stack.getItem() instanceof NPCSpawnEgg) {
                    NPCSpawnEgg.setNpcID(stack, pkt.npcID);
                }
            }
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeEnum(this.hand);
        buf.writeInt(this.level);
        buf.writeBoolean(this.npcID != null);
        if (this.npcID != null)
            buf.writeResourceLocation(this.npcID);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
