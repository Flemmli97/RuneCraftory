package io.github.flemmli97.runecraftory.common.events;

import com.google.common.collect.Sets;
import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.api.datapack.FoodProperties;
import io.github.flemmli97.runecraftory.api.datapack.SimpleEffect;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.api.enums.EnumWeather;
import io.github.flemmli97.runecraftory.api.items.IItemUsable;
import io.github.flemmli97.runecraftory.common.blocks.BlockFarm;
import io.github.flemmli97.runecraftory.common.blocks.BlockMineral;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.IBaseMob;
import io.github.flemmli97.runecraftory.common.entities.ai.DisableGoal;
import io.github.flemmli97.runecraftory.common.items.consumables.ItemMedicine;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolHammer;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolSickle;
import io.github.flemmli97.runecraftory.common.network.S2CCalendar;
import io.github.flemmli97.runecraftory.common.network.S2CCapSync;
import io.github.flemmli97.runecraftory.common.network.S2CDataPackSync;
import io.github.flemmli97.runecraftory.common.network.S2CEntityDataSyncAll;
import io.github.flemmli97.runecraftory.common.network.S2CRuneyWeatherData;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModCrafting;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.common.world.WorldHandler;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class EntityCalls {

    public static void joinPlayer(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            Platform.INSTANCE.sendToClient(new S2CDataPackSync(), serverPlayer);
            Platform.INSTANCE.sendToClient(new S2CCalendar(WorldHandler.get(serverPlayer.getServer()).getCalendar()), serverPlayer);
            Platform.INSTANCE.sendToClient(new S2CRuneyWeatherData(WorldHandler.get(serverPlayer.getServer()).currentWeather() == EnumWeather.RUNEY), serverPlayer);
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
                data.recalculateStats(serverPlayer, false);
                if (!data.starting) {
                    data.starting = true;
                    data.setMaxHealth(player, GeneralConfig.startingHealth, true);
                    player.setHealth(player.getMaxHealth());
                }
                if (GeneralConfig.recipeSystem > 1) {
                    if (!data.unlockedRecipes) {
                        data.unlockedRecipes = true;
                        Set<ResourceLocation> allRecipes = Sets.newHashSet();
                        player.level.getRecipeManager().getAllRecipesFor(ModCrafting.FORGE.get()).forEach(r -> allRecipes.add(r.getId()));
                        player.level.getRecipeManager().getAllRecipesFor(ModCrafting.CHEMISTRY.get()).forEach(r -> allRecipes.add(r.getId()));
                        player.level.getRecipeManager().getAllRecipesFor(ModCrafting.ARMOR.get()).forEach(r -> allRecipes.add(r.getId()));
                        player.level.getRecipeManager().getAllRecipesFor(ModCrafting.COOKING.get()).forEach(r -> allRecipes.add(r.getId()));
                        data.getRecipeKeeper().unlockRecipesRes(player, allRecipes);
                    }
                } else if (data.unlockedRecipes)
                    data.unlockedRecipes = false;
            });
        }
    }

    public static void trackEntity(Player player, Entity target) {
        if (player instanceof ServerPlayer serverPlayer && target instanceof LivingEntity living)
            Platform.INSTANCE.sendToClient(new S2CEntityDataSyncAll(living), serverPlayer);
    }

    public static void updateEquipment(LivingEntity entity, EquipmentSlot slot) {
        if (entity instanceof Player player) {
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> data.updateEquipmentStats(player, slot));
        }
    }

    public static boolean playerAttack(Player player, Entity target) {
        if (!player.level.isClientSide && player.getMainHandItem().getItem() instanceof IItemUsable) {
            CombatUtils.playerAttackWithItem(player, target, true, true, true);
            return true;
        }
        return false;
    }

    public static boolean playerAoeAttack(Player player, ItemStack stack, List<Entity> list) {
        if (stack.getItem() instanceof IItemUsable) {
            for (int i = 0; i < list.size(); ++i) {
                CombatUtils.playerAttackWithItem(player, list.get(i), i == list.size() - 1, true, i == list.size() - 1);
            }
            return true;
        }
        return false;
    }

    public static boolean playerDeath(LivingEntity entity, DamageSource source) {
        if (!entity.level.isClientSide) {
            if (!source.isBypassInvul()) {
                ItemStack deathProt = ItemStack.EMPTY;
                for (ItemStack stack : entity.getAllSlots()) {
                    if (stack.getItem() == ModItems.lawn.get()) {
                        deathProt = stack;
                    }
                }
                if (deathProt.isEmpty() && entity instanceof Player player) {
                    for (ItemStack stack : player.getInventory().items) {
                        if (stack.getItem() == ModItems.lawn.get()) {
                            deathProt = stack;
                        }
                    }
                }
                if (!deathProt.isEmpty()) {
                    if (entity instanceof ServerPlayer player) {
                        player.awardStat(Stats.ITEM_USED.get(deathProt.getItem()));
                        CriteriaTriggers.CONSUME_ITEM.trigger(player, deathProt);
                    }
                    entity.setHealth(entity.getMaxHealth() * 0.33f);
                    entity.removeAllEffects();
                    entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 5, 100));
                    entity.level.broadcastEntityEvent(entity, (byte) 35);
                    deathProt.shrink(1);
                    return true;
                }
            }
            if (MobConfig.vanillaGiveXp && entity instanceof Mob m && !(entity instanceof IBaseMob) && source.getEntity() instanceof LivingEntity attacker) {
                LevelCalc.addXP(attacker, (int) m.getMaxHealth(), 0, 0, false);
            }
        }
        return false;
    }

    public static void dropInventoryDeath(LivingEntity entity) {
        if (entity instanceof ServerPlayer player)
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> data.getInv().dropItemsAt(player));
    }

    public static void clone(Player origin, Player player, boolean death) {
        if (player instanceof ServerPlayer) {
            Platform.INSTANCE.getPlayerData(origin).ifPresent(data -> {
                if (death)
                    data.useMoney(origin, (int) (data.getMoney() * 0.2));
                Platform.INSTANCE.getPlayerData(player).ifPresent(newData -> newData.readFromNBT(data.writeToNBT(new CompoundTag(), origin, death), player));
            });
        }
    }

    public static void cropRightClickHarvest(Player player, BlockState state, BlockPos pos) {
        if (!player.level.isClientSide && state.getBlock() instanceof CropBlock crop) {
            if (crop.isMaxAge(state)) {
                CropProperties props = DataPackHandler.getCropStat(crop.getCloneItemStack(player.level, pos, state).getItem());
                if (!player.level.isClientSide) {
                    crop.playerDestroy(player.level, player, pos, state, null, ItemStack.EMPTY);
                }
                if (props != null && props.regrowable()) {
                    player.level.setBlock(pos, crop.getStateForAge(0), Block.UPDATE_ALL);
                } else {
                    player.level.removeBlock(pos, false);
                }
                player.swing(InteractionHand.MAIN_HAND, true);
                if (props != null && player instanceof ServerPlayer serverPlayer)
                    Platform.INSTANCE.getPlayerData(serverPlayer).ifPresent(data -> LevelCalc.levelSkill(serverPlayer, data, EnumSkills.FARMING, 2f));
            }
        }
    }

    public static boolean onTryBonemeal(Level level, ItemStack stack, BlockState state, BlockPos pos) {
        if (!level.isClientSide && state.getBlock() instanceof CropBlock crop) {
            CropProperties props = DataPackHandler.getCropStat(crop.getCloneItemStack(level, pos, state).getItem());
            if (props != null) {
                BlockPos below = pos.below();
                BlockState belowState = level.getBlockState(below);
                if (belowState.getBlock() instanceof BlockFarm farm && farm.isValidBonemealTarget(level, below, belowState, false)) {
                    if (farm.isBonemealSuccess(level, level.random, below, belowState)) {
                        farm.performBonemeal((ServerLevel) level, level.random, below, belowState);
                    }
                    stack.shrink(1);
                    level.levelEvent(1505, pos, 0);
                }
                return true;
            }
        }
        return false;
    }

    public static void updateLivingTick(Player player) {
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> data.update(player));
        if (GeneralConfig.disableHunger) {
            int food = EntityUtils.paralysed(player) ? 6 : 14;
            player.getFoodData().setFoodLevel(food);
            player.getFoodData().setSaturation(0);
            /*if (player.hasEffect(MobEffects.HUNGER)) {
                player.removeEffect(MobEffects.HUNGER);
                player.addEffect(new MobEffectInstance(ModEffects.poison.get(), 600, 1));
            }*/
        }
    }

    public static void foodHandling(LivingEntity entity, ItemStack stack) {
        if (!entity.level.isClientSide) {
            if (entity instanceof IBaseMob) {
                ((IBaseMob) entity).applyFoodEffect(stack);
                return;
            }
            FoodProperties prop = DataPackHandler.getFoodStat(stack.getItem());
            if (prop == null) {
                if (entity instanceof ServerPlayer player && stack.isEdible()) {
                    Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.EATING, 0.1f));
                }
                return;
            }
            if (entity instanceof ServerPlayer player) {
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
                    if (data.foodBuffDuration() <= 0)
                        data.getDailyUpdater().onFoodEaten(player);
                    data.applyFoodEffect(player, stack);
                    data.refreshRunePoints(player, prop.getRPRegen());
                    data.refreshRunePoints(player, (int) (data.getMaxRunePoints() * prop.getRpPercentRegen() * 0.01));
                });
            }
            boolean medicine = stack.getItem() instanceof ItemMedicine;
            int healthGain = medicine ? ((ItemMedicine) stack.getItem()).healthRegen(stack, prop) : prop.getHPGain();
            EntityUtils.foodHealing(entity, healthGain);
            int healthPercent = medicine ? ((ItemMedicine) stack.getItem()).healthRegenPercent(stack, prop) : prop.getHpPercentGain();
            EntityUtils.foodHealing(entity, entity.getMaxHealth() * healthPercent * 0.01F);
            if (prop.potionHeals() != null)
                for (MobEffect s : prop.potionHeals()) {
                    entity.removeEffect(s);
                }
            if (prop.potionApply() != null)
                for (SimpleEffect s : prop.potionApply()) {
                    entity.addEffect(s.create());
                }
        }
    }

    /**
     * Forge call is also not implemented for hoes so rn this is useless
     */
    public static BlockState hoeTill(Supplier<Boolean> match, BlockState state) {
        /*if (GeneralConfig.disableDatapack && match.get() && state.is(ModTags.farmlandTill)) {
            return ModBlocks.farmland.get().defaultBlockState();
        }*/
        return state;
    }

    public static void wakeUp(Player player) {
        if (GeneralConfig.healOnWakeUp && player instanceof ServerPlayer serverPlayer) {
            player.heal(player.getMaxHealth());
            Platform.INSTANCE.getPlayerData(serverPlayer).ifPresent(data -> {
                data.refreshRunePoints(player, data.getMaxRunePoints());
                LevelCalc.levelSkill(serverPlayer, data, EnumSkills.SLEEPING, 1);
            });
        }
    }

    public static boolean disableNatural(MobSpawnType spawnType, EntityType<?> entity) {
        if (MobConfig.disableNaturalSpawn) {
            return (spawnType == MobSpawnType.CHUNK_GENERATION || spawnType == MobSpawnType.NATURAL) && entity != ModEntities.gate.get();
        }
        return false;
    }

    public static float damageCalculation(LivingEntity entity, DamageSource source, float dmg) {
        float damage = CombatUtils.reduceDamageFromStats(entity, source, dmg);
        if (damage < 0)
            entity.heal(-damage);
        else if (damage > 0 && source != DamageSource.OUT_OF_WORLD && entity instanceof Player player) {
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill((ServerPlayer) entity, data, EnumSkills.DEFENCE, Math.min(5, (float) (0.5 + Math.log(damage * 0.5)))));
        }
        if (source instanceof CustomDamage)
            entity.invulnerableTime = ((CustomDamage) source).hurtProtection() + 10;
        return damage;
    }

    public static void postDamage(LivingEntity entity, DamageSource src, float amount) {
        Entity attacker = src.getEntity();
        if (amount > 0 && attacker instanceof LivingEntity living) {
            float drainPercent = CombatUtils.getAttributeValue(attacker, ModAttributes.RFDRAIN.get(), entity) * 0.01f;
            if (drainPercent > 0f) {
                if (attacker instanceof Player player)
                    player.heal(drainPercent * amount);
                else
                    living.heal(drainPercent * amount);
            }
        }
    }

    public static void onSpawn(LivingEntity living) {
        if (living instanceof Mob mob) {
            mob.goalSelector.addGoal(-1, new DisableGoal(mob));
        }
        if (living instanceof ServerPlayer player)
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> Platform.INSTANCE.sendToClient(new S2CCapSync(data), player));
    }

    public static void onBlockBreak(ServerPlayer player, BlockState state, BlockPos pos) {
        if (!player.hasCorrectToolForDrops(state))
            return;
        if (state.is(ModTags.hammerBreakable)) {
            ItemToolHammer.onHammering(player, true);
        } else if (state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.MINING, state.getBlock() instanceof BlockMineral ? 10 : 1));
        }
        if (state.is(BlockTags.MINEABLE_WITH_AXE)) {
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.LOGGING, 1));
        }
        if (state.is(BlockTags.MINEABLE_WITH_HOE)) {
            if (!(player.getMainHandItem().getItem() instanceof ItemToolSickle))
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.FARMING, 1));
        }
        if (state.getBlock() instanceof BushBlock) {
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.FARMING, 0.5f));
        }
    }

    public static void onLootTableBlockGen(Player player, BlockEntity blockEntity) {
        if (player instanceof ServerPlayer serverPlayer) {
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(serverPlayer, data, EnumSkills.SEARCHING, 1.3f));
        }
    }
}