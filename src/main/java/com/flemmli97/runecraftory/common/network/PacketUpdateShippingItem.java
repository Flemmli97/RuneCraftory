package com.flemmli97.runecraftory.common.network;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.utils.ItemNBT;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdateShippingItem implements IMessage
{
	public ItemStack item;
    
    public PacketUpdateShippingItem() {}
    
    public PacketUpdateShippingItem(ItemStack item, int level) {
        this.item=ItemNBT.getLeveledItem(item, level);
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        this.item=ByteBufUtils.readItemStack(buf);
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, this.item);
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
