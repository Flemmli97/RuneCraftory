package io.github.flemmli97.runecraftory.common.events;

import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.api.datapack.FoodProperties;
import io.github.flemmli97.runecraftory.api.datapack.SimpleEffect;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.api.enums.EnumWeather;
import io.github.flemmli97.runecraftory.common.blocks.BlockCrop;
import io.github.flemmli97.runecraftory.common.blocks.BlockMineral;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.IBaseMob;
import io.github.flemmli97.runecraftory.common.entities.ai.DisableGoal;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolHammer;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolSickle;
import io.github.flemmli97.runecraftory.common.lib.LibConstants;
import io.github.flemmli97.runecraftory.common.network.S2CCalendar;
import io.github.flemmli97.runecraftory.common.network.S2CCapSync;
import io.github.flemmli97.runecraftory.common.network.S2CDataPackSync;
import io.github.flemmli97.runecraftory.common.network.S2CEntityDataSyncAll;
import io.github.flemmli97.runecraftory.common.network.S2CRuneyWeatherData;
import io.github.flemmli97.runecraftory.common.network.S2CTriggers;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModCrafting;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.utils.ItemUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.common.world.WorldHandler;
import io.github.flemmli97.runecraftory.common.world.farming.FarmlandHandler;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
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
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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
                if (GeneralConfig.recipeSystem == GeneralConfig.RecipeSystem.SKILLNOLOCK ||
                        GeneralConfig.recipeSystem == GeneralConfig.RecipeSystem.BASENOLOCK) {
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

    public static void onPlayerLoad(ServerPlayer serverPlayer) {
        //Load the chunks of unloaded party members. They will then teleport to the player themselves
        serverPlayer.getServer().tell(new TickTask(2, () -> {
            Set<WorldHandler.UnloadedPartyMember> party = WorldHandler.get(serverPlayer.getServer()).getUnloadedPartyMembersFor(serverPlayer);
            party.forEach(p -> {
                GlobalPos pos = p.pos();
                ServerLevel level = serverPlayer.getLevel();
                if (level.dimension() != p.pos().dimension())
                    level = serverPlayer.getServer().getLevel(pos.dimension());
                if (level != null)
                    level.getChunkSource().addRegionTicket(TicketType.PORTAL, new ChunkPos(pos.pos()), 3, pos.pos());
            });
            party.clear();
        }));
        //If the party member still got killed somehow remove them here
        Set<UUID> toRemove = WorldHandler.get(serverPlayer.getServer()).removedPartyMembersFor(serverPlayer);
        Platform.INSTANCE.getPlayerData(serverPlayer)
                .ifPresent(d -> toRemove.forEach(d.party::removePartyMember));
        toRemove.clear();
    }

    public static void trackEntity(Player player, Entity target) {
        if (player instanceof ServerPlayer serverPlayer && target instanceof LivingEntity living)
            Platform.INSTANCE.sendToClient(new S2CEntityDataSyncAll(living), serverPlayer);
    }

    public static void onLoadEntity(LivingEntity living) {
        if (living instanceof Mob mob) {
            mob.goalSelector.addGoal(-1, new DisableGoal(mob));
        }
        if (living instanceof ServerPlayer player) {
            onPlayerLoad(player);
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> Platform.INSTANCE.sendToClient(new S2CCapSync(data), player));
        }
    }

    /**
     * Using vanilla attribute system instead of saving it custom on the player. Also now makes it possible to affect generic entities.
     * We are doing this roundabout way cause the attack damage bonus of equipment should only come into play when the player has a fitting weapon in
     * the main hand.
     */
    public static void updateEquipmentNew(LivingEntity entity, Map<EquipmentSlot, ItemStack> changed, ItemStack lastMainhandItem) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            //Readd attack damage to unchanged slots. This is to make sure the client gets send the correct data
            if (!changed.containsKey(slot)) {
                reAddAttackDamage(entity, entity.getItemBySlot(slot), slot);
            }
        }
        boolean hasWeapon = ItemNBT.isWeapon(entity.getMainHandItem());
        float shieldEfficiency = ItemUtils.getShieldEfficiency(entity);
        if (changed.containsKey(EquipmentSlot.MAINHAND)) {
            float lastShieldEfficiency = ItemUtils.getShieldEfficiency(lastMainhandItem);
            //Recalc offhand stats if mainhand changed but offhand did not
            if (!changed.containsKey(EquipmentSlot.OFFHAND) && shieldEfficiency != lastShieldEfficiency) {
                recalcOffhandBonus(entity, entity.getOffhandItem(), shieldEfficiency);
            }
        }
        ItemStack off = changed.get(EquipmentSlot.OFFHAND);
        if (off != null)
            recalcOffhandBonus(entity, off, shieldEfficiency);
        //Telling the client the attribute values before damage is removed so players know the potential damage
        if (entity instanceof ServerPlayer serverPlayer) {
            EntityUtils.sendAttributesTo(serverPlayer, serverPlayer);
        }
        //If player doesnt have a weapon now we remove all attack damage modifiers
        if (!hasWeapon) {
            AttributeInstance inst = entity.getAttribute(Attributes.ATTACK_DAMAGE);
            if (inst != null)
                for (EquipmentSlot slot : EquipmentSlot.values())
                    inst.removeModifier(LibConstants.EQUIPMENT_MODIFIERS[slot.ordinal()]);
        }
    }

    private static void reAddAttackDamage(LivingEntity entity, ItemStack stack, EquipmentSlot slot) {
        stack.getAttributeModifiers(slot).get(Attributes.ATTACK_DAMAGE).forEach(mod -> {
            AttributeInstance inst = entity.getAttribute(Attributes.ATTACK_DAMAGE);
            if (inst != null) {
                inst.removeModifier(mod);
                inst.addTransientModifier(mod);
            }
        });
    }

    private static void recalcOffhandBonus(LivingEntity entity, ItemStack stack, float efficiency) {
        stack.getAttributeModifiers(EquipmentSlot.OFFHAND).forEach((att, mod) -> {
            AttributeInstance inst = entity.getAttribute(att);
            if (inst != null) {
                inst.removeModifier(mod);
                inst.addTransientModifier(new AttributeModifier(mod.getId(), mod.getName(), mod.getAmount() * efficiency, mod.getOperation()));
            }
        });
    }

    public static boolean cancelLivingAttack(DamageSource source, Entity target, float amount) {
        Entity attacker = source.getEntity();
        if (attacker instanceof Player || source instanceof CustomDamage) {
            return false;
        }
        if (attacker instanceof LivingEntity living && living.getMainHandItem().is(ModTags.UPGRADABLE_HELD) && !living.getType().is(ModTags.HELD_WEAPON_EXEMPT)) {
            CombatUtils.mobAttack(living, target);
            return true;
        }
        return false;
    }

    public static boolean playerAttack(Player player, Entity target) {
        if (!player.level.isClientSide && ItemNBT.isWeapon(player.getMainHandItem())) {
            CombatUtils.playerAttackWithItem(player, target, true, true, true);
            return true;
        }
        return false;
    }

    public static boolean playerAoeAttack(Player player, ItemStack stack, List<Entity> list) {
        if (ItemNBT.isWeapon(stack)) {
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
                LevelCalc.addXP(attacker, (int) Math.log(m.getMaxHealth() + 1) * 10, 0, 0, false);
            }
        }
        return false;
    }

    public static void dropInventoryDeath(LivingEntity entity) {
        if (entity instanceof ServerPlayer player && !player.level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY))
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
                CropProperties props = DataPackHandler.SERVER_PACK.cropManager().get(crop.getCloneItemStack(player.level, pos, state).getItem());
                BlockCrop.harvestCropRightClick(state, player.level, pos, player, player.getMainHandItem(),
                        props, null);
            }
        }
    }

    public static boolean onTryBonemeal(Level level, ItemStack stack, BlockState state, BlockPos pos, @Nullable Player player) {
        if (level instanceof ServerLevel serverLevel) {
            BlockPos targetPos = null;
            boolean swing = false;
            if (state.getBlock() instanceof CropBlock crop) {
                CropProperties props = DataPackHandler.SERVER_PACK.cropManager().get(crop.getCloneItemStack(level, pos, state).getItem());
                if (props != null) {
                    BlockPos below = pos.below();
                    if (FarmlandHandler.isFarmBlock(level.getBlockState(below)))
                        targetPos = below;
                }
            } else if (FarmlandHandler.isFarmBlock(state)) {
                targetPos = pos;
                swing = true;
            }
            if (targetPos != null) {
                BlockPos target = targetPos;
                if (player != null) {
                    if (swing) {
                        player.swing(player.getOffhandItem().equals(stack) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND, true);
                    }
                }
                FarmlandHandler.get(serverLevel.getServer())
                        .getData(serverLevel, target).ifPresent(d -> {
                            if (d.canUseBonemeal()) {
                                d.applyBonemeal(serverLevel);
                                stack.shrink(1);
                                Platform.INSTANCE.sendToAll(new S2CTriggers(S2CTriggers.Type.FERTILIZER, target), level.getServer());
                            }
                        });
                return true;
            }
        }
        return player == null && FarmlandHandler.isFarmBlock(state);
    }

    public static void updateLivingTick(Player player) {
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> data.tick(player));
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
            if (entity instanceof IBaseMob mob) {
                mob.applyFoodEffect(stack);
                return;
            }
            FoodProperties prop = DataPackHandler.SERVER_PACK.foodManager().get(stack.getItem());
            if (prop == null) {
                if (entity instanceof ServerPlayer player && stack.isEdible()) {
                    Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
                        LevelCalc.levelSkill(player, data, EnumSkills.EATING, 5);
                        data.refreshRunePoints(player, EntityUtils.getRPFromVanillaFood(stack));
                    });
                }
                return;
            }
            if (entity instanceof ServerPlayer player) {
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
                    if (data.foodBuffDuration() <= 0)
                        data.getDailyUpdater().onFoodEaten(player);
                    data.applyFoodEffect(player, stack);
                    data.refreshRunePoints(player, prop.getRPRegen() + (int) (data.getMaxRunePoints() * prop.getRpPercentRegen() * 0.01));
                });
            }
            Pair<Map<Attribute, Double>, Map<Attribute, Double>> map = ItemNBT.foodStats(stack);
            int healthGain = map.getFirst().getOrDefault(ModAttributes.HEALTHGAIN.get(), 0d).intValue();
            EntityUtils.foodHealing(entity, healthGain);
            int healthPercent = map.getSecond().getOrDefault(ModAttributes.HEALTHGAIN.get(), 0d).intValue();
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

    public static void wakeUp(Player player) {
        if (GeneralConfig.healOnWakeUp && player instanceof ServerPlayer serverPlayer) {
            player.heal(player.getMaxHealth());
            Platform.INSTANCE.getPlayerData(serverPlayer).ifPresent(data -> {
                data.refreshRunePoints(player, data.getMaxRunePoints());
                LevelCalc.levelSkill(serverPlayer, data, EnumSkills.SLEEPING, 75);
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
        else if (damage > 0 && source != DamageSource.OUT_OF_WORLD && entity instanceof ServerPlayer player) {
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.DEFENCE, Math.min(7, (float) (0.5 + Math.log(damage * 0.25))) * 1.5f));
        }
        if (source instanceof CustomDamage)
            entity.invulnerableTime = ((CustomDamage) source).hurtProtection() + 10;
        return damage;
    }

    public static void postDamage(LivingEntity entity, DamageSource src, float amount) {
        Entity attacker = src.getEntity();
        if (amount > 0 && attacker instanceof LivingEntity living) {
            float drainPercent = (float) (CombatUtils.statusEffectChance(living, ModAttributes.RF_DRAIN.get(), entity));
            if (drainPercent > 0f) {
                if (attacker instanceof Player player)
                    player.heal(drainPercent * amount);
                else
                    living.heal(drainPercent * amount);
            }
        }
    }

    public static void onBlockBreak(ServerPlayer player, BlockState state, BlockPos pos) {
        if (!player.hasCorrectToolForDrops(state))
            return;
        if (state.is(ModTags.HAMMER_BREAKABLE)) {
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
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(serverPlayer, data, EnumSkills.SEARCHING, 15));
        }
    }

    public static boolean shouldPreventFarmlandTrample(Entity entity, LevelAccessor world) {
        return GeneralConfig.disableFarmlandTrample;
    }
}