package com.flemmli97.runecraftory.common.core.handler.event;

import com.flemmli97.runecraftory.api.entities.IEntityAdvanced;
import com.flemmli97.runecraftory.api.entities.IEntityBase;
import com.flemmli97.runecraftory.api.items.CropProperties;
import com.flemmli97.runecraftory.api.items.FoodProperties;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.api.mappings.CropMap;
import com.flemmli97.runecraftory.api.mappings.ItemFoodMap;
import com.flemmli97.runecraftory.common.blocks.tile.TileFarmland;
import com.flemmli97.runecraftory.common.core.handler.CustomDamage;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.core.handler.config.ConfigHandler;
import com.flemmli97.runecraftory.common.core.handler.quests.ObjectiveKill;
import com.flemmli97.runecraftory.common.core.handler.quests.QuestMission;
import com.flemmli97.runecraftory.common.core.handler.time.CalendarHandler;
import com.flemmli97.runecraftory.common.core.handler.time.WeatherData;
import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.ai.EntityAIDisable;
import com.flemmli97.runecraftory.common.init.ModBlocks;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.init.PotionRegistry;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.network.PacketCalendar;
import com.flemmli97.runecraftory.common.network.PacketHandler;
import com.flemmli97.runecraftory.common.network.PacketJump;
import com.flemmli97.runecraftory.common.network.PacketUpdateClient;
import com.flemmli97.runecraftory.common.utils.ItemUtils;
import com.flemmli97.runecraftory.common.utils.RFCalculations;
import com.flemmli97.tenshilib.common.config.ConfigUtils.LoadState;
import com.flemmli97.tenshilib.common.events.AOEAttackEvent;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.INpc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class EventHandlerCore
{
    public static final ResourceLocation PlayerCap = new ResourceLocation(LibReference.MODID, "playerCap");

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) 
        {
            event.addCapability(EventHandlerCore.PlayerCap, new PlayerCapProvider());
        }
    }
    
	@SubscribeEvent
	public void config(OnConfigChangedEvent event)
	{
		if(event.getModID().equals(LibReference.MODID))
			ConfigHandler.load(LoadState.SYNC);
	}
    
    @SubscribeEvent
    public void loot(LootTableLoadEvent event) 
    {
        if (event.getName() == LootTableList.GAMEPLAY_FISHING) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public void updateJoin(PlayerLoggedInEvent event) 
    {
        if (event.player instanceof EntityPlayerMP) 
        {
            EntityPlayer player = event.player;
            IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
            if (capSync != null) 
                PacketHandler.sendTo(new PacketUpdateClient(capSync), (EntityPlayerMP)player);
            PacketHandler.sendTo(new PacketCalendar(CalendarHandler.get(event.player.world)), (EntityPlayerMP)player);
            NBTTagCompound playerData = event.player.getEntityData();
            NBTTagCompound persistent = event.player.getEntityData().getCompoundTag("PlayerPersisted");
            if (!persistent.getBoolean("runecraftory:starterItems")) {
                ItemUtils.starterItems(event.player);
                persistent.setBoolean("runecraftory:starterItems", true);
                playerData.setTag("PlayerPersisted", persistent);
                capSync.setMaxHealth(player, 25);
                capSync.regenHealth(player, capSync.getMaxHealth(player));
            }
        }
    }
    
    @SubscribeEvent
    public void joinWorld(EntityJoinWorldEvent event) 
    {
        if (event.getEntity() != null && event.getEntity().world != null) 
        {
            if (event.getEntity() instanceof EntityPlayerMP) 
            {
                EntityPlayer player = (EntityPlayer)event.getEntity();
                PacketHandler.sendTo(new PacketUpdateClient(player.getCapability(PlayerCapProvider.PlayerCap, null)), (EntityPlayerMP)player);
            }
            else if (event.getEntity() instanceof EntityLiving) 
            {
                EntityLiving e = (EntityLiving)event.getEntity();
                if (e.isPotionActive(PotionRegistry.sleep)) 
                {
                    e.tasks.addTask(0, new EntityAIDisable(e));
                }
            }
        }
    }
    
    @SubscribeEvent
    public void playerAttack(AttackEntityEvent event) 
    {
        EntityPlayer player = event.getEntityPlayer();
        if (event.getTarget() instanceof EntityLivingBase && player.getHeldItemMainhand().getItem() instanceof IItemUsable) 
        {
            event.setCanceled(true);
        	RFCalculations.doPlayerAttack(player, (EntityLivingBase)event.getTarget(), true, true, true);
        }
    }
    
    @SubscribeEvent
    public void playerAttack(AOEAttackEvent event) 
    {
    	EntityPlayer player = event.getEntityPlayer();
        if (player.getHeldItemMainhand().getItem() instanceof IItemUsable) 
        {
            event.setCanceled(true);
            List<EntityLivingBase> entityList = event.attackList();
            for (int i = 0; i < entityList.size(); ++i) 
            {
                RFCalculations.doPlayerAttack(player, entityList.get(i), i == entityList.size() - 1, true, i == entityList.size() - 1);
            }
        }
    }
    
    /**
     * Disables mob damage + knockback when not holding a damaging weapon
     */
    @SubscribeEvent
    public void livingAttackEvent(LivingAttackEvent event) 
    {
        if (event.getEntityLiving() instanceof IEntityBase && event.getSource().getTrueSource() instanceof EntityPlayer) 
        {
            EntityPlayer player = (EntityPlayer)event.getSource().getTrueSource();
            if (!(player.getHeldItemMainhand().getItem() instanceof IItemUsable) && event.getAmount() < 2.0f) 
            {
                event.setCanceled(true);
            }
        }
    }
    
    /**
     * Disables knockback from weapons from this mod, since its calculated differently
     */
    @SubscribeEvent
    public void knockBack(LivingKnockBackEvent event) 
    {
        if (event.getAttacker() instanceof IEntityAdvanced || event.getAttacker() instanceof INpc) 
        {
            event.setCanceled(true);
        }
        else if (event.getAttacker() instanceof EntityPlayer && ((EntityPlayer)event.getAttacker()).getHeldItemMainhand().getItem() instanceof IItemUsable) 
        {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public void updateEquipment(LivingEquipmentChangeEvent event)
    {
    	if(event.getEntityLiving() instanceof EntityPlayer)
    	{
        	IPlayer cap = event.getEntityLiving().getCapability(PlayerCapProvider.PlayerCap, null);
        	cap.updateEquipmentStats((EntityPlayer) event.getEntityLiving(), event.getSlot());
    	}
    }
    
    /**
     * Reroutes appropriate damage sources to this mods calculation (for players)
     */
    @SubscribeEvent
    public void damageCalculation(LivingHurtEvent event) {
        if (event.getSource() instanceof CustomDamage) 
        {
            CustomDamage source = (CustomDamage)event.getSource();
            if (event.getEntityLiving() instanceof EntityPlayer) 
            {
            	event.setAmount(RFCalculations.getPlayerDamageAfterReduction((EntityPlayer)event.getEntityLiving(), event.getSource(), event.getAmount()));
            }
            event.getEntityLiving().hurtResistantTime = source.hurtProtection();
        }
    }
    
    @SubscribeEvent
    public void refreshRunePoints(PlayerWakeUpEvent event) {
        if (event.getEntityPlayer() != null) 
        {
            IPlayer capSync = event.getEntityPlayer().getCapability(PlayerCapProvider.PlayerCap, null);
            capSync.refreshRunePoints(event.getEntityPlayer(), capSync.getMaxRunePoints());
            capSync.removeFoodEffect(event.getEntityPlayer());
            event.getEntityPlayer().heal(event.getEntityPlayer().getMaxHealth());
            event.getEntityPlayer().clearActivePotions();
        }
    }
    
    /**
     * updates stuff and disables hunger
     */
    @SubscribeEvent
    public void updateLivingTick(LivingEvent.LivingUpdateEvent event) 
    {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)event.getEntityLiving();
            IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
            capSync.update(player);
            capSync.getInv().update(player);
            int food = player.isPotionActive(PotionRegistry.paralysis) ? 3 : 7;
            player.getFoodStats().setFoodLevel(food);
            if(player.isPotionActive(MobEffects.HUNGER))
            {
            	player.removePotionEffect(MobEffects.HUNGER);
            	player.addPotionEffect(new PotionEffect(PotionRegistry.poison, 600, 1));
            }
        }
    }
    
    @SubscribeEvent
    public void playerDeath(LivingDeathEvent event) 
    {
    	if(!event.getSource().canHarmInCreative())
    	{
    		ItemStack prevent=null;
    		for(ItemStack stack : event.getEntityLiving().getEquipmentAndArmor())
    		{
    			if(stack.getItem()==ModItems.lawn)
    			{
    				prevent=stack;
    				break;
    			}
    		}
    		if(prevent==null && event.getEntityLiving() instanceof EntityPlayer)
    		{
                EntityPlayer player = (EntityPlayer)event.getEntityLiving();
                for(ItemStack stack : player.inventory.mainInventory)
        		{
                	if(stack.getItem()==ModItems.lawn)
        			{
        				prevent=stack;
                		break;
        			}
        		}
    		}
    		if(prevent!=null)
    		{
    			if (event.getEntityLiving() instanceof EntityPlayerMP)
                {
                    EntityPlayerMP entityplayermp = (EntityPlayerMP)event.getEntityLiving();
                    entityplayermp.addStat(StatList.getObjectUseStats(Items.TOTEM_OF_UNDYING));
                    CriteriaTriggers.CONSUME_ITEM.trigger(entityplayermp, prevent);
                }
    			event.getEntityLiving().setHealth(event.getEntityLiving().getMaxHealth()*0.33f);
    			event.getEntityLiving().clearActivePotions();
    			event.getEntityLiving().world.setEntityState(event.getEntityLiving(), (byte)35);
    			return;
    		}
    	}
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)event.getEntityLiving();
            IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
            capSync.getInv().dropItemsAt((EntityLivingBase)player);
            if (!player.world.isRemote) {
                PacketHandler.sendTo(new PacketUpdateClient(capSync), (EntityPlayerMP)player);
            }
        }
    }
    
    @SubscribeEvent
    public void clone(PlayerEvent.Clone event) 
    {
        if (event.isWasDeath()) 
        {
            IPlayer capSync = event.getOriginal().getCapability(PlayerCapProvider.PlayerCap, null);
            capSync.useMoney(event.getOriginal(), (int)(capSync.getMoney() * 0.2));
            NBTTagCompound oldNBT = new NBTTagCompound();
            capSync.writeToNBT(oldNBT, event.getOriginal());
            event.getEntityPlayer().getCapability(PlayerCapProvider.PlayerCap, null).readFromNBT(oldNBT, event.getEntityPlayer());
            if (!event.getEntityPlayer().world.isRemote && event.getEntityPlayer() instanceof EntityPlayerMP) {
                PacketHandler.sendTo(new PacketUpdateClient(event.getEntityPlayer().getCapability(PlayerCapProvider.PlayerCap, null)), (EntityPlayerMP)event.getEntityPlayer());
            }
        }
    }
    
    /**
     * Crop right click harvest handler
     */
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void cropRightClickHarvest(PlayerInteractEvent.RightClickBlock event) 
    {
        EntityPlayer player = event.getEntityPlayer();
        IBlockState state = player.world.getBlockState(event.getPos());
        if (state.getBlock() instanceof BlockCrops) 
        {
            event.setCanceled(true);
            BlockCrops crop = (BlockCrops)state.getBlock();
            if (crop.isMaxAge(state)) 
            {
                CropProperties props = CropMap.getProperties(crop.getItem(player.world, event.getPos(), state));
                if (!player.world.isRemote) 
                {
                    crop.harvestBlock(player.world, player, event.getPos(), state, (TileEntity)null, ItemStack.EMPTY);
                }
                if (props != null && props.regrowable()) 
                {
                    player.world.setBlockState(event.getPos(), crop.withAge(0), 2);
                }
                else 
                {
                    player.world.setBlockToAir(event.getPos());
                }
                if (player.world.isRemote) {
                    FMLClientHandler.instance().getClientPlayerEntity().connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(event.getPos(), event.getFace(), event.getHand(), (float)event.getHitVec().x, (float)event.getHitVec().y, (float)event.getHitVec().z));
                }
            }
        }
    }
    
    /**
     * Modify harvest drops
     */
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void harvest(BlockEvent.HarvestDropsEvent event) 
    {
        IBlockState state = event.getState();
        if (state.getBlock() instanceof BlockCrops) 
        {
            BlockCrops crop = (BlockCrops)state.getBlock();
            CropProperties props = CropMap.getProperties(crop.getItem(event.getWorld(), event.getPos(), state));
            if (props != null) 
            {
                event.getDrops().clear();
                if (crop.isMaxAge(state)) 
                {
                    event.getDrops().add(new ItemStack(crop.getItemDropped(state, event.getWorld().rand, 0), props.maxDrops(), crop.damageDropped(state)));
                }
            }
        }
    }
    
    /**
     * Update daily stuff
     */
    @SubscribeEvent
    public void daily(TickEvent.ServerTickEvent e) 
    {
        if (e.phase == TickEvent.Phase.END) 
        {
            World[] worlds = FMLCommonHandler.instance().getMinecraftServerInstance().worlds;
            if (RFCalculations.canUpdateDaily(worlds[0])) 
            {
                CalendarHandler.get(worlds[0]).increaseDay();           
            }
            WeatherData.get(worlds[0]).update(worlds[0]);
        }
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void jump(LivingEvent.LivingUpdateEvent e) {
        if (e.getEntityLiving() instanceof EntityPlayerSP && e.getEntityLiving().getRidingEntity() instanceof EntityMobBase && ((EntityPlayerSP)e.getEntityLiving()).movementInput.jump) 
        {
            PacketHandler.sendToServer(new PacketJump());
        }
    }
    
    @SubscribeEvent
    public void objKill(LivingDeathEvent event) 
    {
        if (event.getSource().getTrueSource() instanceof EntityPlayer) 
        {
            EntityPlayer player = (EntityPlayer)event.getSource().getTrueSource();
            IPlayer cap = player.getCapability(PlayerCapProvider.PlayerCap, null);
            QuestMission quest = cap.currentMission();
            if (quest != null && quest.questObjective() instanceof ObjectiveKill) 
            {
            	ObjectiveKill obj = (ObjectiveKill) cap.currentMission().questObjective();
            	obj.updateProgress(player, event.getEntityLiving());
            	if(obj.isFinished())
            		player.world.playSound(null,player.getPosition(), SoundEvents.BLOCK_NOTE_BELL,SoundCategory.PLAYERS, 1, 1);
            }
        }
    }
    
    @SubscribeEvent
    public void disableVanillaCrop(BlockEvent.CropGrowEvent.Pre event) 
    {
        if (event.getState().getBlock() instanceof IGrowable && event.getState().getBlock() instanceof IPlantable) 
        {
            IPlantable growable = (IPlantable)event.getState().getBlock();
            if (growable.getPlantType(event.getWorld(), event.getPos()) == EnumPlantType.Crop) 
            {
                event.setResult(Event.Result.DENY);
            }
        }
    }
    
	
	@SubscribeEvent
	public void test(PlayerInteractEvent.LeftClickBlock event)
	{
		//event.setCanceled(true);
	}
	
	@SubscribeEvent
	public void test(PlayerInteractEvent.RightClickBlock event)
	{
		//event.getEntityPlayer().capabilities.allowEdit=false;
	
	}
    
    /**
     * Food handling. Modifies benefits from eating food (Hp, rp gain etc.)
     */
    @SubscribeEvent
    public void foodHandling(LivingEntityUseItemEvent.Finish event)
    {
        if (event.getItem().getItem() instanceof ItemFood && !event.getEntityLiving().world.isRemote) 
        {
        	FoodProperties prop = ItemFoodMap.get(event.getItem());
        	if(prop==null)
        		return;
    		EntityLivingBase e = event.getEntityLiving();
    		if(e instanceof EntityPlayer)
    		{
    			EntityPlayer player = (EntityPlayer) e;
                player.getCooldownTracker().setCooldown(event.getItem().getItem(), 3);
	        	IPlayer cap = player.getCapability(PlayerCapProvider.PlayerCap, null);
	        	cap.applyFoodEffect(player, prop.effects(), prop.effectsMultiplier(), prop.duration());
	        	cap.regenHealth(player, prop.getHPGain());
	        	cap.refreshRunePoints(player, cap.getRunePoints()+prop.getRPRegen());
	        	cap.regenHealth(player, cap.getMaxHealth(player)*prop.getHpPercentGain()*0.01F);
	        	cap.refreshRunePoints(player, (int) (cap.getRunePoints()+cap.getMaxRunePoints()*prop.getRpPercentRegen()*0.01));
    		}
    		else if(e instanceof IEntityBase)
    		{
   				 ((IEntityBase)e).applyFoodEffect(event.getItem());
    		}
    		if(prop.potionHeals()!=null)
    			for(String s : prop.potionHeals())
    			{
    				event.getEntityLiving().removePotionEffect(Potion.getPotionFromResourceLocation(s));
    			}
    		if(prop.potionApply()!=null)
    			for(String s : prop.potionApply())
    			{
    				String[] sub = s.split(";");
    				event.getEntityLiving().addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation(sub[0]), sub.length<2?600:Integer.parseInt(sub[1]), sub.length<3?1:Integer.parseInt(sub[2])));
    			}
        }
    }
    
    /**
     * Bonemeal as a farmland fertilizer, diable vanilla bonemealing
     */
    @SubscribeEvent
    public void boneMealHandling(BonemealEvent event) {
        if (event.getBlock().getBlock() == ModBlocks.farmland) 
        {
            TileFarmland tile = (TileFarmland)event.getWorld().getTileEntity(event.getPos());
            if (tile != null) 
            {
                tile.applyGrowthFertilizer(0.2f);
                event.setResult(Event.Result.ALLOW);
            }
        }
        else if (event.getWorld().getBlockState(event.getPos().down()).getBlock() == ModBlocks.farmland) 
        {
            TileFarmland tile = (TileFarmland)event.getWorld().getTileEntity(event.getPos().down());
            if (tile != null) 
            {
                tile.applyGrowthFertilizer(0.2f);
                event.setResult(Event.Result.ALLOW);
            }
        }
    }
    
    /**
     * Always allow sleep
     */
    @SubscribeEvent
    public void sleep(SleepingTimeCheckEvent event)
    {
    	event.setResult(Result.ALLOW);
    }
}
