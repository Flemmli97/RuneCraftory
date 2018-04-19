package com.flemmli97.runecraftory.common.core.network;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketCastSpell  implements IMessage{

	public int index;	
	public PacketCastSpell(){}
	
	public PacketCastSpell(int index)
	{
		this.index = index;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.index = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.index);
	}
	
	public static class Handler implements IMessageHandler<PacketCastSpell, IMessage> {

        @Override
        public IMessage onMessage(PacketCastSpell msg, MessageContext ctx) {
        	EntityPlayer player = RuneCraftory.proxy.getPlayerEntity(ctx);
        if(player!=null)
		{
			IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
			if(capSync != null)
		    {
				capSync.getInv().useSkill(player, msg.index);			     	
		    }					
		}	
            return null;
        }
    }
}