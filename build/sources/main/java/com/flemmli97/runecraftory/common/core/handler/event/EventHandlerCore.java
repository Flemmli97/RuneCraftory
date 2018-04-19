package com.flemmli97.runecraftory.common.core.handler.event;

import java.util.List;

import com.flemmli97.runecraftory.api.entities.IEntityAdvanced;
import com.flemmli97.runecraftory.api.entities.IEntityBase;
import com.flemmli97.runecraftory.api.items.IRpUseItem;
import com.flemmli97.runecraftory.common.core.handler.CustomDamage;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayerAnim;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.core.network.PacketHandler;
import com.flemmli97.runecraftory.common.core.network.PacketJump;
import com.flemmli97.runecraftory.common.core.network.PacketUpdateClient;
import com.flemmli97.runecraftory.common.core.network.PacketWeaponHit;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumStatusEffect;
import com.flemmli97.runecraftory.common.utils.ItemUtils;
import com.flemmli97.runecraftory.common.utils.RFCalculations;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

/**
 * 
 * Events dealing with the core of this mod.
 *
 */
public class EventHandlerCore {
	
	public static final ResourceLocation PlayerCap = new ResourceLocation(LibReference.MODID, "playerCap");
	public static final ResourceLocation PlayerAnim = new ResourceLocation(LibReference.MODID, "playerAnim");

	@SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof EntityPlayer)
        {
		    event.addCapability(PlayerCap, new PlayerCapProvider());
		    event.addCapability(PlayerAnim, new PlayerCapProvider());
        }
    }
	
	@SubscribeEvent
	public void updateJoin(PlayerLoggedInEvent event)
	{
		if(event.player instanceof EntityPlayerMP)
		{
			EntityPlayer player = (EntityPlayer) event.player;
			IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
			if(capSync!=null)
				PacketHandler.sendTo(new PacketUpdateClient(capSync), (EntityPlayerMP) player);
		}
		NBTTagCompound playerData = event.player.getEntityData();
		NBTTagCompound persistent = event.player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
		if(!persistent.getBoolean(LibReference.MODID+":starterItems")) 
		{
			ItemUtils.starterItems(event.player);
			persistent.setBoolean(LibReference.MODID+":starterItems", true);
	        playerData.setTag(EntityPlayer.PERSISTED_NBT_TAG, persistent);
		}
	}

	@SubscribeEvent
	public void syncCapabilities(EntityJoinWorldEvent event)
	{
		if(event.getEntity()!=null && event.getEntity().world!=null && event.getEntity() instanceof EntityPlayerMP)
		{
			EntityPlayer player = (EntityPlayer) event.getEntity();
			IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
			if(capSync!=null)
				PacketHandler.sendTo(new PacketUpdateClient(capSync), (EntityPlayerMP) player);
		}
	}
	
	/**Override generic player attack with custom one*/
	@SubscribeEvent
	public void playerAttack(AttackEntityEvent event)
	{
		EntityPlayer player = event.getEntityPlayer();

		if(event.getTarget() instanceof EntityLivingBase && player.getHeldItemMainhand()!=ItemStack.EMPTY && player.getHeldItemMainhand().getItem() instanceof IRpUseItem)
		{
			event.setCanceled(true);
			IRpUseItem item = (IRpUseItem) player.getHeldItemMainhand().getItem();
			List<EntityLivingBase> entityList = RFCalculations.calculateEntitiesFromLook(player, item.getWeaponType().getRange(), item.getWeaponType().getAOE());
			if(!entityList.isEmpty())
		    		for (EntityLivingBase e: entityList)
		    		{
		    			RFCalculations.doPlayerDamagePhys(player, e);
		    		}
			else
				RFCalculations.doPlayerDamagePhys(player, (EntityLivingBase) event.getTarget());
		}
	}
	
	@SubscribeEvent
	public void cancelNoDamage(LivingAttackEvent event)
	{
		if(event.getEntityLiving() instanceof IEntityBase)
		{
			if(event.getSource().getTrueSource() instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
				if(!(player.getHeldItemMainhand().getItem() instanceof IRpUseItem) && event.getAmount()<2)
				{
					event.setCanceled(true);
				}
			}
		}
	}
	
	/**damage calculation for player, 2. part: remove hurt resistance time*/
	@SubscribeEvent
	public void damageCalculation(LivingHurtEvent event)
    {
		if(event.getEntityLiving() instanceof EntityPlayer)
		{
			if(event.getSource().getTrueSource() instanceof IEntityAdvanced || event.getSource().getClass()==CustomDamage.class)
			{
				event.setCanceled(true);
				RFCalculations.getPlayerDamageReduction((EntityPlayer) event.getEntityLiving(), event.getSource(), event.getAmount());
			}
			else if(event.getSource().getTrueSource() instanceof EntityPlayer)
			{
				ItemStack stack = ((EntityPlayer) event.getSource().getTrueSource()).getHeldItemMainhand();
				if(stack !=ItemStack.EMPTY && stack.getItem() instanceof IRpUseItem)
				{
					event.setCanceled(true);
					RFCalculations.getPlayerDamageReduction((EntityPlayer) event.getEntityLiving(), event.getSource(), event.getAmount());
				}
			}
			else if(event.getSource().damageType.equals("rfExhaust"))
			{
				event.setCanceled(true);
				IPlayer capSync = event.getEntityLiving().getCapability(PlayerCapProvider.PlayerCap, null);
				capSync.damage((EntityPlayer) event.getEntityLiving(), CustomDamage.EXHAUST, event.getAmount());
			}
		}
		else if (event.getEntityLiving() instanceof EntityLivingBase)
		{
			if(event.getSource().getTrueSource() instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
				if(player.getHeldItemMainhand().getItem() instanceof IRpUseItem)
				{
					event.getEntityLiving().hurtResistantTime=0;
				}
			}
			else if(event.getSource().getTrueSource() instanceof IEntityAdvanced)
				event.getEntityLiving().hurtResistantTime=0;
		}
    }
	
	@SubscribeEvent
	public void refreshRunePoints(PlayerWakeUpEvent event)
    {
		if(event.getEntityPlayer()!=null)
		{
			IPlayer capSync = event.getEntityPlayer().getCapability(PlayerCapProvider.PlayerCap, null);
			capSync.refreshRunePoints(event.getEntityPlayer());
			capSync.setHealth(event.getEntityPlayer(), capSync.getMaxHealth());
			capSync.clearEffect(event.getEntityPlayer());
		}
    }
	
	@SubscribeEvent
	public void applyStatusEffect(LivingUpdateEvent event)
	{
		if(event.getEntityLiving() instanceof EntityPlayer)
		{
			IPlayer capSync = event.getEntityLiving().getCapability(PlayerCapProvider.PlayerCap, null);
			if(!capSync.getActiveStatus().isEmpty())
				for(EnumStatusEffect effect : capSync.getActiveStatus())
				{
					effect.update();
				}
			IPlayerAnim anim = event.getEntityLiving().getCapability(PlayerCapProvider.PlayerAnim, null);
			if(anim.getSpearTick()>0)
				anim.spearTicker();
		}
		else if(event.getEntityLiving() instanceof IEntityAdvanced)
		{
			if(((IEntityAdvanced) event.getEntityLiving()).getActiveStatus()!=null)
			for(EnumStatusEffect effect : ((IEntityAdvanced) event.getEntityLiving()).getActiveStatus())
			{
				effect.update();
			}
		}
	}
	
	/** clone data after death*/
	@SubscribeEvent
	public void clone(PlayerEvent.Clone event)
	{
		if(event.isWasDeath())
		{
			IPlayer capSync = event.getOriginal().getCapability(PlayerCapProvider.PlayerCap, null);
			capSync.useMoney(event.getOriginal(), (int) (capSync.getMoney()*0.2));
			NBTTagCompound oldNBT = new NBTTagCompound();
			capSync.writeToNBT(oldNBT, true);
			event.getEntityPlayer().getCapability(PlayerCapProvider.PlayerCap, null).readFromNBT(oldNBT);
		}
	}
	
	@SubscribeEvent
    public void extendedReach(PlayerInteractEvent.LeftClickEmpty event)
    {
	    	ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
	    	if(stack!=null)
		    	if(stack.getItem() instanceof IRpUseItem)
		    	{
			    	PacketHandler.sendToServer(new PacketWeaponHit());
		    	}
    }
	
	@SubscribeEvent
	public void jump(LivingUpdateEvent e)
	{
		if(e.getEntityLiving() instanceof EntityPlayerSP &&	e.getEntityLiving().isRiding() && ((EntityPlayerSP)e.getEntityLiving()).movementInput.jump)
		{
			PacketHandler.sendToServer(new PacketJump());
		}
	}
}
