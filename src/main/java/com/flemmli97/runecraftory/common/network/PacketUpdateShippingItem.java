package com.flemmli97.runecraftory.common.network;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class PacketUpdateShippingItem implements IMessage
{
	public Item item;
    
    public PacketUpdateShippingItem() {}
    
    public PacketUpdateShippingItem(Item item) {
        this.item=item;
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        this.item=ByteBufUtils.readRegistryEntry(buf, ForgeRegistries.ITEMS);
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeRegistryEntry(buf, this.item);
    }
    
    public static class Handler implements IMessageHandler<PacketUpdateShippingItem, IMessage>
    {
        @Override
		public IMessage onMessage(PacketUpdateShippingItem msg, MessageContext ctx) {
            EntityPlayer player = RuneCraftory.proxy.getPlayerEntity(ctx);
            if (player != null) {
            	player.getCapability(PlayerCapProvider.PlayerCap, null).addShippingItem(player, msg.item);
            }
            return null;
        }
    }
}
