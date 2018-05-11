package com.flemmli97.runecraftory.common.core.network;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.core.handler.time.CalendarHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketCalendar  implements IMessage{

	public NBTTagCompound nbt;
	public PacketCalendar(){}
	
	public PacketCalendar(CalendarHandler handler)
	{
		nbt = handler.writeToNBT(new NBTTagCompound());
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.nbt = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, this.nbt);
	}
	
	public static class Handler implements IMessageHandler<PacketCalendar, IMessage> {

        @Override
        public IMessage onMessage(PacketCalendar msg, MessageContext ctx) {
        	EntityPlayer player = RuneCraftory.proxy.getPlayerEntity(ctx);
	        if(player!=null)
			{
        		CalendarHandler.get(player.world).readFromNBT(msg.nbt);			
			}	
            return null;
        }
    }
}