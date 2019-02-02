package com.flemmli97.runecraftory.common.network;

import java.util.Map;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.utils.ItemUtils;
import com.google.common.collect.Maps;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketFoodUpdate  implements IMessage{
	
	private Map<IAttribute, Integer> gain;
	private int duration;
	public PacketFoodUpdate() {};
	public PacketFoodUpdate(Map<IAttribute, Integer> gain, int duration)
	{
		this.gain=gain;
		this.duration=duration;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound compound = ByteBufUtils.readTag(buf);
		this.duration=compound.getInteger("Duration");
		NBTTagCompound tag = compound.getCompoundTag("Map");
		this.gain=Maps.newHashMap();
		for(String s : tag.getKeySet())
		{
			this.gain.put(ItemUtils.getAttFromName(s), tag.getInteger(s));
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger("Duration", this.duration);
		NBTTagCompound tag = new NBTTagCompound();
		this.gain.forEach((att,i)->{
			tag.setInteger(att.getName(), i);
		});
		compound.setTag("Map", tag);
		ByteBufUtils.writeTag(buf, compound);
	}
	
	public static class Handler implements IMessageHandler<PacketFoodUpdate, IMessage> {

        @Override
        public IMessage onMessage(PacketFoodUpdate msg, MessageContext ctx) {
        	EntityPlayer player = RuneCraftory.proxy.getPlayerEntity(ctx);
        if(player!=null)
		{
			player.getCapability(PlayerCapProvider.PlayerCap, null).applyFoodEffect(player, msg.gain, Maps.newHashMap(), msg.duration);;				     	
		}	
            return null;
        }
    }
}
