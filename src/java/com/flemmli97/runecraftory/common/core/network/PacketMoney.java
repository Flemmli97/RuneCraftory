package com.flemmli97.runecraftory.common.core.network;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketMoney  implements IMessage{

	public int money;	
	public PacketMoney(){}
	
	public PacketMoney(IPlayer playerCap)
	{
		this.money = playerCap.getMoney();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.money = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.money);
	}
	
	public static class Handler implements IMessageHandler<PacketMoney, IMessage> {

        @Override
        public IMessage onMessage(PacketMoney msg, MessageContext ctx) {
        	EntityPlayer player = RuneCraftory.proxy.getPlayerEntity(ctx);
        if(player!=null)
		{
			IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
			if(capSync != null)
		    {
				capSync.setMoney(player, msg.money);	     	
		    }					
		}	
            return null;
        }
    }
}