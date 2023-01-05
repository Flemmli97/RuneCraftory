package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

/**
 * Simple triggers from server to client where client does some predefined stuff.
 * E.g. adding bonemeal particles. (Instead of sending multiple particle packets)
 */
public record S2CTriggers(Type type, BlockPos pos) implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_triggers");

    public static S2CTriggers read(FriendlyByteBuf buf) {
        return new S2CTriggers(buf.readEnum(S2CTriggers.Type.class), new BlockPos(buf.readInt(), buf.readInt(), buf.readInt()));
    }

    public static void handle(S2CTriggers pkt) {
        ClientHandlers.handleTriggers(pkt.type, pkt.pos);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeEnum(this.type);
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public enum Type {
        FERTILIZER
    }
}
