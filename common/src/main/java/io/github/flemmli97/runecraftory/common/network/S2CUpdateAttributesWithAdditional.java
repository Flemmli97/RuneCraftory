package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;

/**
 * Using a custom packet for this to update attributes and if gui is open notify gui of change
 */
public class S2CUpdateAttributesWithAdditional implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CUpdateAttributesWithAdditional> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_attributes_and_additional"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CUpdateAttributesWithAdditional> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CUpdateAttributesWithAdditional decode(RegistryFriendlyByteBuf buf) {
            return new S2CUpdateAttributesWithAdditional(ClientboundUpdateAttributesPacket.STREAM_CODEC.decode(buf));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CUpdateAttributesWithAdditional pkt) {
            ClientboundUpdateAttributesPacket.STREAM_CODEC.encode(buf, pkt.pkt);
        }
    };

    private final ClientboundUpdateAttributesPacket pkt;

    private S2CUpdateAttributesWithAdditional(ClientboundUpdateAttributesPacket pkt) {
        this.pkt = pkt;
    }

    public S2CUpdateAttributesWithAdditional(Collection<AttributeInstance> attributes) {
        this.pkt = new ClientboundUpdateAttributesPacket(0, attributes);
    }

    public static void handle(S2CUpdateAttributesWithAdditional pkt, Player player) {
        if (player == null)
            return;
        AttributeMap attributeMap = player.getAttributes();
        for (ClientboundUpdateAttributesPacket.AttributeSnapshot attributeSnapshot : pkt.pkt.getValues()) {
            AttributeInstance attributeInstance = attributeMap.getInstance(attributeSnapshot.attribute());
            if (attributeInstance == null) {
                continue;
            }
            attributeInstance.setBaseValue(attributeSnapshot.base());
            attributeInstance.removeModifiers();
            for (AttributeModifier attributeModifier : attributeSnapshot.modifiers()) {
                attributeInstance.addTransientModifier(attributeModifier);
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}