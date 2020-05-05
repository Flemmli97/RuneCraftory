package com.flemmli97.runecraftory.common.network;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketPlayerLevel  implements IMessage{

	public int[] level;	
	public PacketPlayerLevel(){}
	
	public PacketPlayerLevel(IPlayer playerCap)
	{
		this.level = playerCap.getPlayerLevel();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound compound = ByteBufUtils.readTag(buf);
		this.level = compound.getIntArray("level");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setIntArray("level", this.level);
		ByteBufUtils.writeTag(buf, compound);
	}
	
	public static class Handler implements IMessageHandler<PacketPlayerLevel, IMessage> {

        @Override
        public IMessage onMessage(PacketPlayerLevel msg, MessageContext ctx) {
        	EntityPlayer player = RuneCraftory.proxy.getPlayerEntity(ctx);
        if(player!=null)
		{
			player.getCapability(PlayerCapProvider.PlayerCap, null).setPlayerLevel(player, msg.level[0], msg.level[1]);				     	
		}	
            return null;
        }
    }
}