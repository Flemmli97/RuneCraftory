package com.flemmli97.runecraftory.common.network;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketMaxRunePoints  implements IMessage{

	public int runePointsMax;	
	public PacketMaxRunePoints(){}
	
	public PacketMaxRunePoints(int rp)
	{
		this.runePointsMax = rp;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.runePointsMax = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.runePointsMax);
	}
	
	public static class Handler implements IMessageHandler<PacketMaxRunePoints, IMessage> {

        @Override
        public IMessage onMessage(PacketMaxRunePoints msg, MessageContext ctx) {
        	EntityPlayer player = RuneCraftory.proxy.getPlayerEntity(ctx);
        if(player!=null)
		{
			player.getCapability(PlayerCapProvider.PlayerCap, null).setMaxRunePoints(player, msg.runePointsMax);	     	
		}	
            return null;
        }
    }
}