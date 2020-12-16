package com.flemmli97.runecraftory.common.events;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.datapack.FoodProperties;
import com.flemmli97.runecraftory.api.datapack.SimpleEffect;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.common.capability.PlayerCapProvider;
import com.flemmli97.runecraftory.common.datapack.DataPackHandler;
import com.flemmli97.runecraftory.common.entities.IBaseMob;
import com.flemmli97.runecraftory.common.utils.CombatUtils;
import com.flemmli97.runecraftory.common.utils.EntityUtils;
import com.flemmli97.runecraftory.common.world.WorldHandler;
import com.flemmli97.runecraftory.network.PacketHandler;
import com.flemmli97.runecraftory.network.S2CCalendar;
import com.flemmli97.runecraftory.network.S2CDataPackSync;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerEvents {

    public static final ResourceLocation PlayerCap = new ResourceLocation(RuneCraftory.MODID, "player_cap");

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(PlayerCap, new PlayerCapProvider());
        }
    }

    @SubscribeEvent
    public void join(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getPlayer().world.isRemote) {
            PacketHandler.sendToClient(new S2CDataPackSync(), (ServerPlayerEntity) event.getPlayer());
            PacketHandler.sendToClient(new S2CCalendar(WorldHandler.get((ServerWorld) event.getPlayer().world).getCalendar()), (ServerPlayerEntity) event.getPlayer());
        }
    }

    @SubscribeEvent
    public void updateEquipment(LivingEquipmentChangeEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            event.getEntityLiving().getCapability(PlayerCapProvider.PlayerCap).ifPresent(cap ->
                    cap.updateEquipmentStats((PlayerEntity) event.getEntityLiving(), event.getSlot()));
        }
    }

    @SubscribeEvent
    public void playerAttack(AttackEntityEvent event) {
        PlayerEntity player = event.getPlayer();
        if (event.getTarget() instanceof LivingEntity && player.getHeldItemMainhand().getItem() instanceof IItemUsable) {
            event.setCanceled(true);
            CombatUtils.doPlayerAttack(player, (LivingEntity) event.getTarget(), true, true, true);
        }
    }
    /*
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
    */

    @SubscribeEvent
    public void updateLivingTick(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            player.getCapability(PlayerCapProvider.PlayerCap).ifPresent(cap -> {
                cap.update(player);
            });
            int food = EntityUtils.paralysed(player) ? 3 : 7;
            player.getFoodStats().setFoodLevel(food);
            player.getFoodStats().setFoodSaturationLevel(0);
            /*if(player.isPotionActive(Effects.HUNGER))
            {
                player.removePotionEffect(Effects.HUNGER);
                //player.addPotionEffect(new PotionEffect(PotionRegistry.poison, 600, 1));
            }*/
        }
    }

    @SubscribeEvent
    public void foodHandling(LivingEntityUseItemEvent.Finish event) {
        if (!event.getEntityLiving().world.isRemote) {
            FoodProperties prop = DataPackHandler.getFoodStat(event.getItem().getItem());
            if (prop == null)
                return;
            LivingEntity e = event.getEntityLiving();
            if (e instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) e;
                player.getCooldownTracker().setCooldown(event.getItem().getItem(), 3);
                player.getCapability(PlayerCapProvider.PlayerCap).ifPresent(cap -> {
                    cap.applyFoodEffect(player, event.getItem());
                    cap.regenHealth(player, prop.getHPGain());
                    cap.refreshRunePoints(player, cap.getRunePoints() + prop.getRPRegen());
                    cap.regenHealth(player, cap.getMaxHealth(player) * prop.getHpPercentGain() * 0.01F);
                    cap.refreshRunePoints(player, (int) (cap.getRunePoints() + cap.getMaxRunePoints() * prop.getRpPercentRegen() * 0.01));
                });
            } else if (e instanceof IBaseMob) {
                ((IBaseMob) e).applyFoodEffect(event.getItem());
            }
            if (prop.potionHeals() != null)
                for (Effect s : prop.potionHeals()) {
                    event.getEntityLiving().removePotionEffect(s);
                }
            if (prop.potionApply() != null)
                for (SimpleEffect s : prop.potionApply()) {
                    event.getEntityLiving().addPotionEffect(s.create());
                }
        }
    }
}
