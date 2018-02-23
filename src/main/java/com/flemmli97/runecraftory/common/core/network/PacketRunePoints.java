package com.flemmli97.runecraftory.common.core.network;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRunePoints  implements IMessage{

	public int runePoints;	
	public PacketRunePoints(){}
	
	public PacketRunePoints(IPlayer playerCap)
	{
		this.runePoints = playerCap.getRunePoints();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.runePoints = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.runePoints);
	}
	
	public static class Handler implements IMessageHandler<PacketRunePoints, IMessage> {

        @Override
        public IMessage onMessage(PacketRunePoints msg, MessageContext ctx) {
        	EntityPlayer player = RuneCraftory.proxy.getPlayerEntity(ctx);
        if(player!=null)
		{
			IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
			if(capSync != null)
		    {
				capSync.setRunePoints(player, msg.runePoints);				     	
		    }					
		}	
            return null;
        }
    }
}