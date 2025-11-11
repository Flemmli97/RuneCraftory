package io.github.flemmli97.runecraftory.mixinhelper;

import io.github.flemmli97.runecraftory.api.registry.ArmorEffect;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.blocks.GiantCropBlock;
import io.github.flemmli97.runecraftory.common.entities.utils.IBaseMob;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryArmorEffects;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.runecraftory.common.world.data.farming.FarmlandHandler;
import io.github.flemmli97.runecraftory.mixin.CropBlockAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class MixinUtils {

    public static boolean playerPose(Player player) {
        PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(player);
        Pose pose = data.getWeaponHandler().getCurrentAction().getPose(player, data.getWeaponHandler());
        if (pose != null) {
            if (player.getPose() != pose)
                player.setPose(pose);
            return true;
        }
        return false;
    }

    public static void onPlayerThrowItem(Player player, ItemEntity entity) {
        if (!player.isDeadOrDying()) {
            entity.setThrower(player);
            PrevEntityPosition pos = (PrevEntityPosition) player;
            double dX = player.getX() - pos.runecraftory$getOldPlayerX();
            double dZ = player.getZ() - pos.runecraftory$getOldPlayerZ();
            double spd = dX * dX + dZ * dZ;
            if (spd > 0.01) {
                double scale = ArmorEffect.hasArmorEffect(player, RuneCraftoryArmorEffects.THROWING_RING.asHolder()) ? 2.5 : 1.7;
                entity.setDeltaMovement(entity.getDeltaMovement().scale(scale));
            }
        }
    }

    public static boolean handleEntityCollision(ItemEntity entity) {
        if (entity.isInWater() || entity.isInLava() || entity.getOwner() == null || entity.level().isClientSide)
            return true;
        HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(entity, t -> canHitEntity(entity, t));
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            return true;
        }
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            EntityHitResult result = (EntityHitResult) hitResult;
            if (result.getEntity() instanceof IBaseMob mob) {
                ItemStack stack = entity.getItem();
                Entity e = entity.getOwner();
                if (e instanceof Player thrower) {
                    if (mob.onGivingItem(thrower, stack)) {
                        if (stack.isEmpty())
                            entity.discard();
                    }
                }
            }
            return true;
        }
        return false;
    }

    protected static boolean canHitEntity(ItemEntity entity, Entity target) {
        Entity owner;
        if (target.isSpectator() || !target.isAlive() || !target.isPickable() || (owner = entity.getOwner()) == null) {
            return false;
        }
        return !target.getUUID().equals(owner.getUUID());
    }

    public static void onBlockStateChange(ServerLevel level, BlockPos pos, BlockState blockState, BlockState newState) {
        //If related to farmblocks notify
        if (FarmlandHandler.isFarmBlock(newState)) {
            if (!FarmlandHandler.isFarmBlock(blockState) || FarmlandHandler.get(level.getServer()).getData(level, pos).isEmpty())
                FarmlandHandler.get(level.getServer()).onFarmlandPlace(level, pos);
        } else if (FarmlandHandler.isFarmBlock(blockState)) {
            FarmlandHandler.get(level.getServer()).onFarmlandRemove(level, pos);
        } else {
            //Handling crop blockState changes
            if (blockState.getBlock() instanceof CropBlock pre) {
                if (newState.getBlock() instanceof GiantCropBlock giant && giant.isGiantOf(blockState, newState)) {
                    return;
                }
                // Crop got broken
                if (!(newState.getBlock() instanceof CropBlock post)) {
                    FarmlandHandler.get(level.getServer()).getData(level, pos.below())
                            .ifPresent(d -> d.onCropRemove(level, pos, newState));
                } else if (blockState.getValue(((CropBlockAccessor) pre).cropAgeProperty()) > newState.getValue(((CropBlockAccessor) post).cropAgeProperty())) {
                    //Crop got reset (e.g. via right click harvesting)
                    FarmlandHandler.get(level.getServer()).getData(level, pos.below())
                            .ifPresent(d -> d.onRegrowableHarvest(level, pos, newState));
                }
            }
        }
    }

    public static void recheckFarmland(ServerLevel level, BlockState state, BlockPos pos) {
        if (FarmlandHandler.get(level.getServer()).getData(level, pos).map(d -> !d.isFarmBlock()).orElse(true)) {
            FarmlandHandler.get(level.getServer()).onFarmlandPlace(level, pos);
        }
    }

    public static void triggerArmorStepEffect(LivingEntity living) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR)
                continue;
            ItemStack stack = living.getItemBySlot(slot);
            ArmorEffect.runArmorEffectFor(stack, effect -> effect.onStep(living, stack));
        }
    }
}
