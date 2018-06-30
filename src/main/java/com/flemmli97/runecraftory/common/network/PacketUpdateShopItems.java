package com.flemmli97.runecraftory.common.network;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.entities.IShop;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdateShopItems implements IMessage
{
    public int entityID;
    public NonNullList<ItemStack> list = NonNullList.create();
    
    public PacketUpdateShopItems() {}
    
    public PacketUpdateShopItems(int entityID, NonNullList<ItemStack> shopItems) {
        this.list = NonNullList.create();
        this.entityID = entityID;
        this.list = shopItems;
    }
    
    public void fromBytes(ByteBuf buf) {
        NBTTagCompound compound = ByteBufUtils.readTag(buf);
        NBTTagList list = compound.getTagList("Items", 10);
        for (int i = 0; i < list.tagCount(); ++i) {
            this.list.add(new ItemStack(list.getCompoundTagAt(i)));
        }
        this.entityID = compound.getInteger("entityID");
    }
    
    public void toBytes(ByteBuf buf) {
        NBTTagCompound compound = new NBTTagCompound();
        NBTTagList tagList = new NBTTagList();
        for (ItemStack stack : this.list) {
            tagList.appendTag((NBTBase)stack.writeToNBT(new NBTTagCompound()));
        }
        compound.setTag("Items", (NBTBase)tagList);
        compound.setInteger("entityID", this.entityID);
        ByteBufUtils.writeTag(buf, compound);
    }
    
    public static class Handler implements IMessageHandler<PacketUpdateShopItems, IMessage>
    {
        public IMessage onMessage(PacketUpdateShopItems msg, MessageContext ctx) {
            EntityPlayer player = RuneCraftory.proxy.getPlayerEntity(ctx);
            if (player != null) {
                Entity entity = player.world.getEntityByID(msg.entityID);
                if (entity instanceof IShop) {
                    ((IShop)entity).updateShopItems(msg.list);
                }
            }
            return null;
        }
    }
}
