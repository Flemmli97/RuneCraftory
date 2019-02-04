package com.flemmli97.runecraftory.common.network;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdateClient  implements IMessage{

	public NBTTagCompound compound = new NBTTagCompound();	
	public PacketUpdateClient(){}
	
	public PacketUpdateClient(IPlayer playerCap)
	{
		playerCap.writeToNBT(this.compound, null);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.compound = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, this.compound);
	}
	
	public static class Handler implements IMessageHandler<PacketUpdateClient, IMessage> {

        @Override
        public IMessage onMessage(PacketUpdateClient msg, MessageContext ctx) {
        	EntityPlayer player = RuneCraftory.proxy.getPlayerEntity(ctx);
        if(player!=null)
		{
			player.getCapability(PlayerCapProvider.PlayerCap, null).readFromNBT(msg.compound, null);		     	
		}	
            return null;
        }
    }
}