package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record C2SSpellKey(int num, boolean release) implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "c2s_cast_spell");

    public static C2SSpellKey read(FriendlyByteBuf buf) {
        return new C2SSpellKey(buf.readInt(), buf.readBoolean());
    }

    public static void handle(C2SSpellKey pkt, ServerPlayer sender) {
        if (sender != null) {
            if (sender.getVehicle() instanceof BaseMonster)
                ((BaseMonster) sender.getVehicle()).handleRidingCommand(pkt.num);
            else {
                Platform.INSTANCE.getPlayerData(sender).ifPresent(data -> {
                    if (pkt.release)
                        data.getInv().onRelease();
                    else
                        data.getInv().useSkill(sender, pkt.num);
                });
            }
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.num);
        buf.writeBoolean(this.release);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
