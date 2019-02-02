package com.flemmli97.runecraftory.common.network;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdateEquipmentStat  implements IMessage{
	
	private EntityEquipmentSlot slot;
	public PacketUpdateEquipmentStat() {};
	public PacketUpdateEquipmentStat(EntityEquipmentSlot slot)
	{
		this.slot=slot;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.slot=EntityEquipmentSlot.values()[buf.readInt()];
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.slot.ordinal());
	}
	
	public static class Handler implements IMessageHandler<PacketUpdateEquipmentStat, IMessage> {

        @Override
        public IMessage onMessage(PacketUpdateEquipmentStat msg, MessageContext ctx) {
        	EntityPlayer player = RuneCraftory.proxy.getPlayerEntity(ctx);
        if(player!=null)
		{
			player.getCapability(PlayerCapProvider.PlayerCap, null).updateEquipmentStats(player, msg.slot);				     	
		}	
            return null;
        }
    }
}
