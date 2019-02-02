package com.flemmli97.runecraftory.common.network;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.lib.enums.EnumSkills;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSkills  implements IMessage{

	public int[] skillLevel;	
	public EnumSkills skill;
	public PacketSkills(){}
	
	public PacketSkills(IPlayer playerCap, EnumSkills skill)
	{
		this.skillLevel = playerCap.getSkillLevel(skill);
		this.skill = skill;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound compound = ByteBufUtils.readTag(buf);
		this.skillLevel = compound.getIntArray("level");
		this.skill = EnumSkills.getFromString(compound.getString("skill"));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setIntArray("level", skillLevel);
		compound.setString("skill", this.skill.getIdentifier());
		ByteBufUtils.writeTag(buf, compound);
	}
	
	public static class Handler implements IMessageHandler<PacketSkills, IMessage> {

        @Override
        public IMessage onMessage(PacketSkills msg, MessageContext ctx) {
        	EntityPlayer player = RuneCraftory.proxy.getPlayerEntity(ctx);
        if(player!=null)
		{
			player.getCapability(PlayerCapProvider.PlayerCap, null).setSkillLevel(msg.skill, player, msg.skillLevel[0], msg.skillLevel[1]);			     	
		}	
            return null;
        }
    }
}