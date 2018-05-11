package com.flemmli97.runecraftory.common.core.handler.event;

import java.util.List;

import com.flemmli97.runecraftory.api.entities.IEntityAdvanced;
import com.flemmli97.runecraftory.api.entities.IEntityBase;
import com.flemmli97.runecraftory.api.items.IRpUseItem;
import com.flemmli97.runecraftory.common.core.handler.CustomDamage;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.core.handler.quests.IObjective;
import com.flemmli97.runecraftory.common.core.handler.quests.ObjectiveKill;
import com.flemmli97.runecraftory.common.core.handler.quests.QuestMission;
import com.flemmli97.runecraftory.common.core.handler.time.CalendarHandler;
import com.flemmli97.runecraftory.common.core.handler.time.DailyBlockTickHandler;
import com.flemmli97.runecraftory.common.core.network.PacketCalendar;
import com.flemmli97.runecraftory.common.core.network.PacketHandler;
import com.flemmli97.runecraftory.common.core.network.PacketJump;
import com.flemmli97.runecraftory.common.core.network.PacketUpdateClient;
import com.flemmli97.runecraftory.common.core.network.PacketWeaponHit;
import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.init.ModBlocks;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumStatusEffect;
import com.flemmli97.runecraftory.common.utils.ItemUtils;
import com.flemmli97.runecraftory.common.utils.RFCalculations;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.INpc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.world.BlockEvent.CropGrowEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
			PacketHandler.sendTo(new PacketCalendar(CalendarHandler.get(event.player.world)), (EntityPlayerMP) player);

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

		if(event.getTarget() instanceof EntityLivingBase && !player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() instanceof IRpUseItem)
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
	public void livingAttackEvent(LivingAttackEvent event)
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
	
	@SubscribeEvent
	public void knockBack(LivingKnockBackEvent event)
	{
		if(event.getAttacker()instanceof IEntityAdvanced || event.getAttacker()instanceof INpc)
		{
			event.setCanceled(true);
		}
		else if(event.getAttacker() instanceof EntityPlayer)
		{
			if(((EntityPlayer) event.getAttacker()).getHeldItemMainhand().getItem() instanceof IRpUseItem)
				event.setCanceled(true);
		}
	}
	
	/**damage calculation for player, 2. part: remove hurt resistance time*/
	@SubscribeEvent
	public void damageCalculation(LivingHurtEvent event)
    {
		if(event.getSource().damageType.equals("rfAttack"))
		{
			CustomDamage source = (CustomDamage) event.getSource();
			if(event.getEntityLiving() instanceof EntityPlayer)
			{
				event.setCanceled(true);
				RFCalculations.getPlayerDamageReduction((EntityPlayer) event.getEntityLiving(), event.getSource(), event.getAmount());
			}
			event.getEntityLiving().hurtResistantTime=source.hurtProtection();
		}
		else if(event.getSource().damageType.equals("rfExhaust") && event.getEntityLiving() instanceof EntityPlayer)
		{
			event.setCanceled(true);
			IPlayer capSync = event.getEntityLiving().getCapability(PlayerCapProvider.PlayerCap, null);
			capSync.damage((EntityPlayer) event.getEntityLiving(), CustomDamage.EXHAUST, event.getAmount());
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
	public void updateLivingTick(LivingUpdateEvent event)
	{
		if(event.getEntityLiving() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
			if(!capSync.getActiveStatus().isEmpty())
				for(EnumStatusEffect effect : capSync.getActiveStatus())
				{
					effect.update();
				}
			player.getCapability(PlayerCapProvider.PlayerAnim, null).update(player);
			capSync.getInv().update(player);
			//Disables hunger and natural regen
			player.getFoodStats().setFoodLevel(7);
			player.getFoodStats().setFoodSaturationLevel(1);;
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
	
	@SubscribeEvent
	public void playerDeath(LivingDeathEvent event)
	{
		if(event.getEntityLiving() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
			capSync.getInv().dropItemsAt(player);
			if(!player.world.isRemote)
			{
				PacketHandler.sendTo(new PacketUpdateClient(capSync), (EntityPlayerMP) player);
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
			if(!event.getEntityPlayer().world.isRemote && event.getEntityPlayer() instanceof EntityPlayerMP)
			{
				PacketHandler.sendTo(new PacketUpdateClient(event.getEntityPlayer().getCapability(PlayerCapProvider.PlayerCap, null)), (EntityPlayerMP) event.getEntityPlayer());
			}
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
	public void daily(ServerTickEvent e)
	{
		if(e.phase==TickEvent.Phase.END)
		{
			World[] worlds = FMLCommonHandler.instance().getMinecraftServerInstance().worlds;
			if(worlds[0].getWorldTime()%24000==1)
			{
				for(World world : worlds)
					DailyBlockTickHandler.instance((WorldServer) world).update(world);
				CalendarHandler.get(worlds[0]).increaseDay();
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void jump(LivingUpdateEvent e)
	{
		if(e.getEntityLiving() instanceof EntityPlayerSP &&	(e.getEntityLiving().getRidingEntity() instanceof EntityMobBase) && ((EntityPlayerSP)e.getEntityLiving()).movementInput.jump)
		{
			PacketHandler.sendToServer(new PacketJump());
		}
	}
	
	@SubscribeEvent
	public void objKill(LivingDeathEvent event) {
		if (event.getSource().getTrueSource() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
			IPlayer cap = player.getCapability(PlayerCapProvider.PlayerCap, null);
			QuestMission quest = cap.currentMission();
			if(quest!=null && quest.questObjective() instanceof ObjectiveKill)
			{
				IObjective obj =cap.currentMission().questObjective();
				if(EntityList.isMatchingName(event.getEntityLiving(), new ResourceLocation(obj.objGoalID())))
				{
					obj.updateProgress(player);
				}
			}
		}
	}
	@SubscribeEvent
	public void disableVanillaCrop(CropGrowEvent.Pre event)
	{
		event.setCanceled(true);
	}
	
	//Convert food heals to rp heals
	@SubscribeEvent
	public void foodHandling(LivingEntityUseItemEvent.Finish event)
	{
		if(event.getItem().getItem() instanceof ItemFood)
		{
			
		}
	}
	
	//Bonemeal as fertilizer for soil, not crops
	@SubscribeEvent
	public void boneMealHandling(BonemealEvent event)
	{

	}
	
	@SubscribeEvent
	public void disableVanillaTilling(UseHoeEvent event)
	{
		IBlockState iblockstate = event.getWorld().getBlockState(event.getPos());
        Block block = iblockstate.getBlock();
        if (event.getWorld().isAirBlock(event.getPos().up()) && block==Blocks.DIRT)
        {
			event.setResult(Result.DENY);
			switch ((BlockDirt.DirtType)iblockstate.getValue(BlockDirt.VARIANT))
			{
				case DIRT:
					this.setBlock(event.getCurrent(), event.getEntityPlayer(), event.getWorld(), event.getPos(), ModBlocks.farmland.getDefaultState());
					break;
				case COARSE_DIRT:
					this.setBlock(event.getCurrent(), event.getEntityPlayer(), event.getWorld(), event.getPos(), Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
					break;
				default:
					break;
			}
		}
	}
    protected void setBlock(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, IBlockState state)
    {
        worldIn.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

        if (!worldIn.isRemote)
        {
            worldIn.setBlockState(pos, state, 11);
            stack.damageItem(1, player);
        }
    }
}
