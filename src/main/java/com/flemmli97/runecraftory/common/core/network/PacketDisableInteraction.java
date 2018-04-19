package com.flemmli97.runecraftory.common.core.network;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.lib.LibReference;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketDisableInteraction  implements IMessage{

	public PacketDisableInteraction(){}
	
	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}
	
	public static class Handler implements IMessageHandler<PacketDisableInteraction, IMessage> {

        @Override
        public IMessage onMessage(PacketDisableInteraction msg, MessageContext ctx) {
	        	EntityPlayer player = RuneCraftory.proxy.getPlayerEntity(ctx);
	        if(player!=null)
			{
				player.openGui(RuneCraftory.instance, LibReference.guiSleep, player.world, (int)player.posX, (int)player.posY, (int)player.posZ);
			}	
            return null;
        }
    }
}