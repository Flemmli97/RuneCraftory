package com.flemmli97.runecraftory.common.network;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketWeaponAnimation  implements IMessage{

	public int tick;	
	public PacketWeaponAnimation(){}
	
	public PacketWeaponAnimation(int startTick)
	{
		this.tick = startTick;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.tick = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.tick);
	}
	
	public static class Handler implements IMessageHandler<PacketWeaponAnimation, IMessage> {

        @Override
        public IMessage onMessage(PacketWeaponAnimation msg, MessageContext ctx) {
        	EntityPlayer player = RuneCraftory.proxy.getPlayerEntity(ctx);
        if(player!=null)
		{
			player.getCapability(PlayerCapProvider.PlayerCap, null).startAnimation(msg.tick);	     	
		}	
            return null;
        }
    }
}