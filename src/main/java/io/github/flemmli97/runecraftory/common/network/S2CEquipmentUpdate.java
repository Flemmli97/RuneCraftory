package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.capability.CapabilityInsts;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CEquipmentUpdate {

    private final EquipmentSlotType slot;

    public S2CEquipmentUpdate(EquipmentSlotType slot) {
        this.slot = slot;
    }

    public static S2CEquipmentUpdate read(PacketBuffer buf) {
        return new S2CEquipmentUpdate(buf.readEnumValue(EquipmentSlotType.class));
    }

    public static void write(S2CEquipmentUpdate pkt, PacketBuffer buf) {
        buf.writeEnumValue(pkt.slot);
    }

    public static void handle(S2CEquipmentUpdate pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHandlers::getPlayer);
            if (player == null)
                return;
            player.getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> cap.updateEquipmentStats(player, pkt.slot));
        });
        ctx.get().setPacketHandled(true);
    }
}
