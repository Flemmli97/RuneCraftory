package com.flemmli97.runecraftory.common.network;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketHealth  implements IMessage{

	public float health;	
	public PacketHealth(){}
	
	public PacketHealth(IPlayer playerCap)
	{
		this.health = playerCap.getHealth();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.health = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeFloat(this.health);
	}
	
	public static class Handler implements IMessageHandler<PacketHealth, IMessage> {

        @Override
        public IMessage onMessage(PacketHealth msg, MessageContext ctx) {
        	EntityPlayer player = RuneCraftory.proxy.getPlayerEntity(ctx);
        if(player!=null)
		{
			player.getCapability(PlayerCapProvider.PlayerCap, null).setHealth(player, msg.health);;				     	
		}	
            return null;
        }
    }
}