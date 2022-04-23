package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record C2SSpellKey(int num) implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "c2s_cast_spell");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.num);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public static C2SSpellKey read(FriendlyByteBuf buf) {
        return new C2SSpellKey(buf.readInt());
    }

    public static void handle(C2SSpellKey pkt, ServerPlayer sender) {
        if (sender != null) {
            if (sender.getVehicle() instanceof BaseMonster)
                ((BaseMonster) sender.getVehicle()).handleRidingCommand(pkt.num);
            else
                Platform.INSTANCE.getPlayerData(sender).ifPresent(cap -> cap.getInv().useSkill(sender, pkt.num));
        }
    }
}
