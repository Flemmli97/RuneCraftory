package com.flemmli97.runecraftory.common.events;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.datapack.FoodProperties;
import com.flemmli97.runecraftory.api.datapack.SimpleEffect;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.flemmli97.runecraftory.common.config.GeneralConfig;
import com.flemmli97.runecraftory.common.datapack.DataPackHandler;
import com.flemmli97.runecraftory.common.entities.IBaseMob;
import com.flemmli97.runecraftory.common.items.consumables.ItemMedicine;
import com.flemmli97.runecraftory.common.network.PacketHandler;
import com.flemmli97.runecraftory.common.network.S2CCalendar;
import com.flemmli97.runecraftory.common.network.S2CCapSync;
import com.flemmli97.runecraftory.common.network.S2CDataPackSync;
import com.flemmli97.runecraftory.common.network.S2CEntityDataSyncAll;
import com.flemmli97.runecraftory.common.registry.ModBlocks;
import com.flemmli97.runecraftory.common.registry.ModItems;
import com.flemmli97.runecraftory.common.registry.ModTags;
import com.flemmli97.runecraftory.common.utils.CombatUtils;
import com.flemmli97.runecraftory.common.utils.EntityUtils;
import com.flemmli97.runecraftory.common.world.WorldHandler;
import com.flemmli97.tenshilib.api.event.AOEAttackEvent;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class PlayerEvents {

    @SubscribeEvent
    public void join(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getPlayer().world.isRemote) {
            PacketHandler.sendToClient(new S2CDataPackSync(), (ServerPlayerEntity) event.getPlayer());
            PacketHandler.sendToClient(new S2CCalendar(WorldHandler.get((ServerWorld) event.getPlayer().world).getCalendar()), (ServerPlayerEntity) event.getPlayer());
            PacketHandler.sendToClient(new S2CCapSync(event.getPlayer().getCapability(CapabilityInsts.PlayerCap).orElseThrow(() -> new NullPointerException("Error getting capability"))), (ServerPlayerEntity) event.getPlayer());
            CompoundNBT playerData = event.getPlayer().getPersistentData();
            if (!playerData.getBoolean(RuneCraftory.MODID + ":starting")) {
                playerData.putBoolean(RuneCraftory.MODID + ":starting", true);
                event.getPlayer().getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> {
                    cap.setMaxHealth(event.getPlayer(), GeneralConfig.startingHealth);
                    cap.setHealth(event.getPlayer(), cap.getMaxHealth(event.getPlayer()));
                });
            }
        }
    }

    @SubscribeEvent
    public void trackEntity(PlayerEvent.StartTracking event) {
        if (event.getPlayer() instanceof ServerPlayerEntity && event.getTarget() instanceof LivingEntity)
            PacketHandler.sendToClient(new S2CEntityDataSyncAll((LivingEntity) event.getTarget()), (ServerPlayerEntity) event.getPlayer());
    }

    @SubscribeEvent
    public void updateEquipment(LivingEquipmentChangeEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            event.getEntityLiving().getCapability(CapabilityInsts.PlayerCap).ifPresent(cap ->
                    cap.updateEquipmentStats((PlayerEntity) event.getEntityLiving(), event.getSlot()));
        }
    }

    @SubscribeEvent
    public void playerAttack(AttackEntityEvent event) {
        PlayerEntity player = event.getPlayer();
        if (!player.world.isRemote && player.getHeldItemMainhand().getItem() instanceof IItemUsable) {
            event.setCanceled(true);
            CombatUtils.playerAttackWithItem(player, event.getTarget(), true, true, true);
        }
    }

    @SubscribeEvent
    public void playerAttack(AOEAttackEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player.getHeldItemMainhand().getItem() instanceof IItemUsable) {
            event.setCanceled(true);
            List<Entity> entityList = event.attackList();
            for (int i = 0; i < entityList.size(); ++i) {
                CombatUtils.playerAttackWithItem(player, entityList.get(i), i == entityList.size() - 1, true, i == entityList.size() - 1);
            }
        }
    }

    @SubscribeEvent
    public void playerDeath(LivingDeathEvent event) {
        if (!event.getEntityLiving().world.isRemote) {
            if (!event.getSource().canHarmInCreative()) {
                ItemStack deathProt = ItemStack.EMPTY;
                for (ItemStack stack : event.getEntityLiving().getEquipmentAndArmor()) {
                    if (stack.getItem() == ModItems.lawn.get()) {
                        deathProt = stack;
                    }
                }
                if (deathProt.isEmpty() && event.getEntityLiving() instanceof PlayerEntity) {
                    for (ItemStack stack : ((PlayerEntity) event.getEntityLiving()).inventory.mainInventory) {
                        if (stack.getItem() == ModItems.lawn.get()) {
                            deathProt = stack;
                        }
                    }
                }
                if (!deathProt.isEmpty()) {
                    if (event.getEntityLiving() instanceof ServerPlayerEntity) {
                        ServerPlayerEntity player = (ServerPlayerEntity) event.getEntityLiving();
                        player.addStat(Stats.ITEM_USED.get(deathProt.getItem()));
                        CriteriaTriggers.CONSUME_ITEM.trigger(player, deathProt);
                    }
                    event.getEntityLiving().setHealth(event.getEntityLiving().getMaxHealth() * 0.33f);
                    event.getEntityLiving().clearActivePotions();
                    event.getEntityLiving().addPotionEffect(new EffectInstance(Effects.RESISTANCE, 5, 100));
                    event.getEntityLiving().world.setEntityState(event.getEntityLiving(), (byte) 35);
                    deathProt.shrink(1);
                    event.setCanceled(true);
                    return;
                }
            }
            if (event.getEntityLiving() instanceof PlayerEntity) {
                event.getEntityLiving().getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> cap.getInv().dropItemsAt(event.getEntityLiving()));
                /*if (!player.world.isRemote) {
                    PacketHandler.sendTo(new PacketUpdateClient(capSync), (EntityPlayerMP)player);
                }*/
            }
        }
    }

    @SubscribeEvent
    public void clone(PlayerEvent.Clone event) {
        if (event.isWasDeath() && !event.getPlayer().world.isRemote) {
            event.getOriginal().getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> {
                cap.useMoney(event.getOriginal(), (int) (cap.getMoney() * 0.2));
                event.getPlayer().getCapability(CapabilityInsts.PlayerCap).ifPresent(newCap -> newCap.readFromNBT(cap.writeToNBT(new CompoundNBT(), event.getOriginal()), event.getPlayer()));
            });
            PacketHandler.sendToClient(new S2CCapSync(event.getPlayer().getCapability(CapabilityInsts.PlayerCap).orElseThrow(() -> new NullPointerException("Error getting capability"))), (ServerPlayerEntity) event.getPlayer());
        }
    }
/*
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
            player.getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> cap.update(player));
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
            ItemStack stack = event.getItem();
            FoodProperties prop = DataPackHandler.getFoodStat(stack.getItem());
            if (prop == null)
                return;
            LivingEntity e = event.getEntityLiving();
            if (e instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) e;
                player.getCooldownTracker().setCooldown(stack.getItem(), 3);
                player.getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> {
                    boolean medicine = stack.getItem() instanceof ItemMedicine;
                    cap.applyFoodEffect(player, stack);
                    int healthGain = medicine ? ((ItemMedicine) stack.getItem()).healthRegen(stack, prop) : prop.getHPGain();
                    cap.regenHealth(player, healthGain);
                    cap.refreshRunePoints(player, prop.getRPRegen());
                    int healthPercent = medicine ? ((ItemMedicine) stack.getItem()).healthRegenPercent(stack, prop) : prop.getHPGain();
                    cap.regenHealth(player, cap.getMaxHealth(player) * healthPercent * 0.01F);
                    cap.refreshRunePoints(player, (int) (cap.getMaxRunePoints() * prop.getRpPercentRegen() * 0.01));
                });
            } else if (e instanceof IBaseMob) {
                ((IBaseMob) e).applyFoodEffect(stack);
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

    @SubscribeEvent
    public void hoeTill(BlockEvent.BlockToolInteractEvent event) {
        if (event.getToolType() == ToolType.HOE && event.getFinalState() != null && event.getFinalState().isIn(ModTags.farmlandTill)) {
            event.setFinalState(ModBlocks.farmland.get().getDefaultState());
        }
    }
}