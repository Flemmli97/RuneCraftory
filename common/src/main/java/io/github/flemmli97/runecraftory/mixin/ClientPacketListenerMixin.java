package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.common.items.creative.ItemProp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {

    @Shadow
    private ClientLevel level;

    @Inject(method = "handleSetEquipment", at = @At("TAIL"))
    private void onSetEquip(ClientboundSetEquipmentPacket packet, CallbackInfo info) {
        Entity entity = this.level.getEntity(packet.getEntity());
        if (entity != null && entity != Minecraft.getInstance().player) {
            packet.getSlots().forEach((pair) -> {
                ItemStack stack = pair.getSecond();
                if (stack.getItem() instanceof ItemProp mimic) {
                    entity.setItemSlot(pair.getFirst(), mimic.clientItemStack());
                }
            });
        }
    }
}
