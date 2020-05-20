package com.flemmli97.runecraftory.common.network;

import java.util.Map;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.entities.IEntityBase;
import com.flemmli97.runecraftory.common.utils.ItemUtils;
import com.google.common.collect.Maps;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketFoodUpdateEntity  implements IMessage{
	
	private int entityID;
	private Map<IAttribute, Integer> gain;
	private Map<IAttribute, Float> gainMulti;

	private int duration;
	
	public PacketFoodUpdateEntity() {}

	public PacketFoodUpdateEntity(EntityLiving e, Map<IAttribute, Integer> gain, Map<IAttribute, Float> gainMulti, int duration)
	{
		this.entityID=e.getEntityId();
		this.gain=gain;
		this.gainMulti=gainMulti;
		this.duration=duration;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound compound = ByteBufUtils.readTag(buf);
		this.entityID=compound.getInteger("EntityID");
		this.duration=compound.getInteger("Duration");
		NBTTagCompound tag = compound.getCompoundTag("Map");
		this.gain=Maps.newHashMap();
		tag.getKeySet().forEach(s->
                this.gain.put(ItemUtils.getAttFromName(s), tag.getInteger(s)));
		NBTTagCompound tag2 = compound.getCompoundTag("MapMulti");
		this.gainMulti=Maps.newHashMap();
		tag.getKeySet().forEach(s->
                this.gainMulti.put(ItemUtils.getAttFromName(s), tag2.getFloat(s)));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger("Duration", this.duration);
		NBTTagCompound tag = new NBTTagCompound();
		this.gain.forEach((att,i)-> tag.setInteger(att.getName(), i));
		compound.setTag("Map", tag);
		NBTTagCompound tag2 = new NBTTagCompound();
		this.gainMulti.forEach((att,f)-> tag.setFloat(att.getName(), f));
		compound.setTag("MapMulti", tag2);
		compound.setInteger("EntityID", this.entityID);
		ByteBufUtils.writeTag(buf, compound);
	}
	
	public static class Handler implements IMessageHandler<PacketFoodUpdateEntity, IMessage> {

        @Override
        public IMessage onMessage(PacketFoodUpdateEntity msg, MessageContext ctx) {
        	EntityPlayer player = RuneCraftory.proxy.getPlayerEntity(ctx);
        if(player!=null)
		{
			Entity e = player.world.getEntityByID(msg.entityID);	
			if(e instanceof IEntityBase)
			{
				((IEntityBase)e).applyFoodEffect(msg.gain, msg.gainMulti, msg.duration);
			}
		}	
            return null;
        }
    }
}
