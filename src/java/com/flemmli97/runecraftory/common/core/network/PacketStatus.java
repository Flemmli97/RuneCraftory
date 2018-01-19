package com.flemmli97.runecraftory.common.core.network;

import java.util.ArrayList;
import java.util.List;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.enums.EnumStatusEffect;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketStatus  implements IMessage{

	public List<String> status = new ArrayList<String>();	
	public PacketStatus(){}
	
	public PacketStatus(IPlayer playerCap)
	{
		for(EnumStatusEffect effect : playerCap.getActiveStatus())
			this.status.add(effect.getName());
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound compound = ByteBufUtils.readTag(buf);
		for(int i = 0; i < compound.getInteger("size");i++)
		{
			this.status.add(compound.getString("status"+i));
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound compound = new NBTTagCompound();
		for(int i = 0; i < this.status.size(); i++)
		{
			compound.setString("status"+i, this.status.get(i));
		}
		compound.setInteger("size", this.status.size());
		ByteBufUtils.writeTag(buf, compound);
	}
	
	public static class Handler implements IMessageHandler<PacketStatus, IMessage> {

        @Override
        public IMessage onMessage(PacketStatus msg, MessageContext ctx) {
        	EntityPlayer player = RuneCraftory.proxy.getPlayerEntity(ctx);
        if(player!=null)
		{
			IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
			if(capSync != null)
		    {
				capSync.clearEffect(player);
				for(String effect : msg.status)
				{
					if(EnumStatusEffect.fromName(effect)!=null)
						capSync.addStatus(player, EnumStatusEffect.fromName(effect));
				}
		    }					
		}	
            return null;
        }
    }
}