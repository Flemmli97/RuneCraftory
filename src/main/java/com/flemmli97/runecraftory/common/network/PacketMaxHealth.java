package com.flemmli97.runecraftory.common.network;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketMaxHealth  implements IMessage{

	public float healthMax;	
	public PacketMaxHealth(){}
	
	public PacketMaxHealth(float hp)
	{
		this.healthMax = hp;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.healthMax = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeFloat(this.healthMax);
	}
	
	public static class Handler implements IMessageHandler<PacketMaxHealth, IMessage> {

        @Override
        public IMessage onMessage(PacketMaxHealth msg, MessageContext ctx) {
        	EntityPlayer player = RuneCraftory.proxy.getPlayerEntity(ctx);
        if(player!=null)
		{
			player.getCapability(PlayerCapProvider.PlayerCap, null).setMaxHealth(player, msg.healthMax);;				     	
		}	
            return null;
        }
    }
}