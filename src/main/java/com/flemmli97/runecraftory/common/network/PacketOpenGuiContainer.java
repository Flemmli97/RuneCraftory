package com.flemmli97.runecraftory.common.network;

import com.flemmli97.runecraftory.RuneCraftory;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketOpenGuiContainer  implements IMessage{

	public int guiId;	
	public PacketOpenGuiContainer(){}
	
	public PacketOpenGuiContainer(int guiId)
	{
		this.guiId = guiId;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.guiId = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.guiId);
	}
	
	public static class Handler implements IMessageHandler<PacketOpenGuiContainer, IMessage> {

        @Override
        public IMessage onMessage(PacketOpenGuiContainer msg, MessageContext ctx) {
        	EntityPlayer player = RuneCraftory.proxy.getPlayerEntity(ctx);
	        if(player!=null)
			{
				player.openGui(RuneCraftory.instance, msg.guiId, player.world, (int)player.posX, (int)player.posY, (int)player.posZ);
			}	
            return null;
        }
    }
}