package com.flemmli97.runecraftory.common.network;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.core.handler.capabilities.CapabilityProvider;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayerAnim;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSwingArm  implements IMessage{

	public PacketSwingArm(){}
	
	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}
	
	public static class Handler implements IMessageHandler<PacketSwingArm, IMessage> {

        @Override
        public IMessage onMessage(PacketSwingArm msg, MessageContext ctx) {
        	EntityPlayer player = RuneCraftory.proxy.getPlayerEntity(ctx);
        if(player!=null)
		{
    		IPlayerAnim anim = player.getCapability(CapabilityProvider.PlayerCapProvider.PlayerAnim, null);
    		int i = 0;
    		if (player.isPotionActive(MobEffects.HASTE))
            {
                i= 6 - (1 + player.getActivePotionEffect(MobEffects.HASTE).getAmplifier());
            }
            else
            {
                i= player.isPotionActive(MobEffects.MINING_FATIGUE) ? 6 + (1 + player.getActivePotionEffect(MobEffects.MINING_FATIGUE).getAmplifier()) * 2 : 6;
            }
            if (!player.isSwingInProgress || player.swingProgressInt >= i / 2 || player.swingProgressInt < 0)
            {
            		player.swingProgressInt = -1;
            		player.isSwingInProgress = true;
            		player.swingingHand = anim.getPrevSwung()==EnumHand.MAIN_HAND?EnumHand.OFF_HAND:EnumHand.MAIN_HAND;
            		anim.setPrevSwung(player.swingingHand);
            }					
		}	
            return null;
        }
    }
}