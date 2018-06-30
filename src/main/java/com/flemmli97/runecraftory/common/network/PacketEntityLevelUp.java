package com.flemmli97.runecraftory.common.network;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.entity.EntityMobBase;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketEntityLevelUp  implements IMessage{

	public int entityID;
	public String att;
	public double value;
	
	public PacketEntityLevelUp(){}
	
	public PacketEntityLevelUp(int entityID)
	{
		this.entityID=entityID;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityID=buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.entityID);
	}
	
	public static class Handler implements IMessageHandler<PacketEntityLevelUp, IMessage> {

        @Override
        public IMessage onMessage(PacketEntityLevelUp msg, MessageContext ctx) {
        	EntityPlayer player = RuneCraftory.proxy.getPlayerEntity(ctx);
	        if(player!=null)
			{
				Entity entity=player.world.getEntityByID(msg.entityID);
				if(entity instanceof EntityMobBase)
					((EntityMobBase)entity).entityLevelUp();
			}	
            return null;
        }
    }
}