package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;

/**
 * Using a custom packet for this to update attributes and if gui is open notify gui of change
 */
public class S2CUpdateAttributesWithAdditional implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_attributes_and_additional");

    private final ClientboundUpdateAttributesPacket pkt;

    private S2CUpdateAttributesWithAdditional(ClientboundUpdateAttributesPacket pkt) {
        this.pkt = pkt;
    }

    public S2CUpdateAttributesWithAdditional(Collection<AttributeInstance> attributes) {
        this.pkt = new ClientboundUpdateAttributesPacket(0, attributes);
    }

    public static S2CUpdateAttributesWithAdditional read(FriendlyByteBuf buf) {
        return new S2CUpdateAttributesWithAdditional(new ClientboundUpdateAttributesPacket(buf));
    }

    public static void handle(S2CUpdateAttributesWithAdditional pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        AttributeMap attributeMap = player.getAttributes();
        for (ClientboundUpdateAttributesPacket.AttributeSnapshot attributeSnapshot : pkt.pkt.getValues()) {
            AttributeInstance attributeInstance = attributeMap.getInstance(attributeSnapshot.getAttribute());
            if (attributeInstance == null) {
                continue;
            }
            attributeInstance.setBaseValue(attributeSnapshot.getBase());
            attributeInstance.removeModifiers();
            for (AttributeModifier attributeModifier : attributeSnapshot.getModifiers()) {
                attributeInstance.addTransientModifier(attributeModifier);
            }
        }
        ClientHandlers.onAttributePkt();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        this.pkt.write(buf);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}