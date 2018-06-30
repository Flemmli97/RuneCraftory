package com.flemmli97.runecraftory.common.network;

import java.util.List;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.common.utils.RFCalculations;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketWeaponHit  implements IMessage{
	
	public PacketWeaponHit(){}
	
	@Override
	public void fromBytes(ByteBuf buf) {}

	@Override
	public void toBytes(ByteBuf buf) {}
	
	public static class Handler implements IMessageHandler<PacketWeaponHit, IMessage> {

        @Override
        public IMessage onMessage(PacketWeaponHit msg, MessageContext ctx) 
        {
    		EntityPlayer player =  RuneCraftory.proxy.getPlayerEntity(ctx);
    		ItemStack stack = player.getHeldItemMainhand();
			if(stack.getItem() instanceof IItemUsable)
			{
				IItemUsable item = (IItemUsable) stack.getItem();
				item.levelSkillOnHit(player);
		    		List<EntityLivingBase> entityList = RFCalculations.calculateEntitiesFromLook(player, item.getWeaponType().getRange(), item.getWeaponType().getAOE());
				if(!entityList.isEmpty())
				{
		    		for (int i = 0; i < entityList.size(); i++)
		    		{
		    			RFCalculations.doPlayerAttack(player, entityList.get(i), i==entityList.size()-1, true, i==entityList.size()-1);
		    		}
				}
				else
				{
					EntityLivingBase entity = RFCalculations.calculateSingleEntityFromLook(player, item.getWeaponType().getRange());
					if(entity!=null)
						RFCalculations.doPlayerAttack(player, entity, true, true, true);
				}
			}
            return null;
        }
    }
}