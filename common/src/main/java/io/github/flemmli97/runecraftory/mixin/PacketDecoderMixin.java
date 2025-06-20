package io.github.flemmli97.runecraftory.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.PacketDecoder;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PacketDecoder.class)
public class PacketDecoderMixin {

    /**
     * Adds more debugging info to the error for {@link CustomPacketPayload} types
     */
    @WrapOperation(method = "decode", at = @At(value = "INVOKE", target = "Ljava/lang/String;valueOf(Ljava/lang/Object;)Ljava/lang/String;", ordinal = 0))
    private String updatedInfo(Object obj, Operation<String> original, @Local Packet<?> type) {
        String info = original.call(obj);
        if (type instanceof ClientboundCustomPayloadPacket(CustomPacketPayload payload)) {
            info += " (Type: " + payload.type() + ")";
        }
        if (type instanceof ServerboundCustomPayloadPacket(CustomPacketPayload payload)) {
            info += " (Type: " + payload.type() + ")";
        }
        return info;
    }
}
