package com.flemmli97.runecraftory.common.network;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.inventory.ContainerShop;
import com.flemmli97.runecraftory.common.lib.enums.EnumShopResult;
import com.flemmli97.runecraftory.common.utils.ItemUtils;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketBuy implements IMessage
{
    public ItemStack item;
    public int amount;
    
    public PacketBuy() {
    }
    
    public PacketBuy(ItemStack stack) {
        this.item = stack;
        this.amount = stack.getCount();
    }
    
    @Override
	public void fromBytes(ByteBuf buf) {
        this.amount = buf.readInt();
        this.item = ByteBufUtils.readItemStack(buf);
    }
    
    @Override
	public void toBytes(ByteBuf buf) {
        buf.writeInt(this.amount);
        ByteBufUtils.writeItemStack(buf, this.item);
    }
    
    public static class Handler implements IMessageHandler<PacketBuy, IMessage>
    {
        @Override
		public IMessage onMessage(PacketBuy msg, MessageContext ctx) {
            EntityPlayer player = RuneCraftory.proxy.getPlayerEntity(ctx);
            if (player != null && player instanceof EntityPlayerMP) 
            {
                ItemStack stack = msg.item;
                stack.setCount(msg.amount);
                Container container = player.openContainer;
                if (container instanceof ContainerShop) 
                {
                    ContainerShop shop = (ContainerShop)container;
                    EnumShopResult result = ItemUtils.buyItem(player, stack);  
                    PacketHandler.sendTo(new PacketShopResult(shop.shopOwner().purchase(result)), (EntityPlayerMP)player);
                    if (result == EnumShopResult.SUCCESS) 
                    {
                        shop.resetSlot();
                    }
                }
            }
            return null;
        }
    }
}
