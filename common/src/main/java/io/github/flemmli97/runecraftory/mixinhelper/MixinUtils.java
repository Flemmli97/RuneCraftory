package io.github.flemmli97.runecraftory.mixinhelper;

import io.github.flemmli97.runecraftory.api.items.IItemUsable;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.entities.IBaseMob;
import io.github.flemmli97.runecraftory.common.world.farming.FarmlandHandler;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class MixinUtils {

    public static boolean stop(Player player, ItemStack stack, HitResult hitResult) {
        Item item = stack.getItem();
        return hitResult.getType() != HitResult.Type.BLOCK && item instanceof IItemUsable && player.getCooldowns().isOnCooldown(item);
    }

    public static boolean playerPose(Player player) {
        if (Platform.INSTANCE.getEntityData(player).map(EntityData::isSleeping).orElse(false)) {
            if (player.getPose() != Pose.SLEEPING)
                player.setPose(Pose.SLEEPING);
            return true;
        }
        Pose pose = Platform.INSTANCE.getPlayerData(player).map(d -> d.getWeaponHandler().getCurrentAction().getPose(player, d.getWeaponHandler())).orElse(null);
        if (pose != null) {
            if (player.getPose() != pose)
                player.setPose(pose);
            return true;
        }
        return false;
    }

    public static void onPlayerThrowItem(Player player, ItemEntity entity) {
        if (!player.isDeadOrDying()) {
            entity.setThrower(player.getUUID());
            PrevEntityPosition pos = (PrevEntityPosition) player;
            double dX = player.getX() - pos.getOldPlayerX();
            double dZ = player.getZ() - pos.getOldPlayerZ();
            double spd = dX * dX + dZ * dZ;
            if (spd > 0.01) {
                entity.setDeltaMovement(entity.getDeltaMovement().scale(2));
            }
        }
    }

    public static boolean handleEntityCollision(ItemEntity entity) {
        if (entity.isInWater() || entity.isInLava() || entity.getThrower() == null || entity.level.isClientSide)
            return true;
        HitResult hitResult = ProjectileUtil.getHitResult(entity, t -> canHitEntity(entity, t));
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            return true;
        }
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            EntityHitResult result = (EntityHitResult) hitResult;
            if (result.getEntity() instanceof IBaseMob mob) {
                ItemStack stack = entity.getItem();
                Entity e = ((ServerLevel) entity.level).getEntity(entity.getThrower());
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
        if (target.isSpectator() || !target.isAlive() || !target.isPickable()) {
            return false;
        }
        return !target.getUUID().equals(entity.getThrower());
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
                //Crop got broken
                if (!(newState.getBlock() instanceof CropBlock post)) {
                    FarmlandHandler.get(level.getServer()).getData(level, pos.below())
                            .ifPresent(d -> d.onCropRemove(level, pos, newState));
                } else if (blockState.getValue(pre.getAgeProperty()) > newState.getValue(post.getAgeProperty())) {
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
            if (slot.getType() != EquipmentSlot.Type.ARMOR)
                continue;
            ItemStack stack = living.getItemBySlot(slot);
            if (!stack.isEmpty())
                Platform.INSTANCE.getArmorEffects(stack).ifPresent(d -> d.triggerEvent(stack, e -> e.onStep(living, stack)));
        }
    }
}
