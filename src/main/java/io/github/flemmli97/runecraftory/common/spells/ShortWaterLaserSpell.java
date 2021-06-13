package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.capability.CapabilityInsts;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWaterLaser;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.server.ServerWorld;

public class ShortWaterLaserSpell extends Spell {

    @Override
    public void update(PlayerEntity player, ItemStack stack) {

    }

    @Override
    public void levelSkill(ServerPlayerEntity player) {
        player.getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> LevelCalc.levelSkill(player, cap, EnumSkills.FIRE, 1));
    }

    @Override
    public int coolDown() {
        return 20;
    }

    @Override
    public boolean use(ServerWorld world, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int level) {
        boolean rp = !(entity instanceof PlayerEntity) || entity.getCapability(CapabilityInsts.PlayerCap).map(cap -> cap.decreaseRunePoints((PlayerEntity) entity, this.rpCost(), true)).orElse(false);
        if (rp) {
            EntityWaterLaser laser = new EntityWaterLaser(world, entity);
            laser.setMaxTicks(5);
            laser.setDamageMultiplier(1 + (level - 1) / 10);
            if (entity instanceof MobEntity && ((MobEntity) entity).getAttackTarget() != null) {
                LivingEntity target = ((MobEntity) entity).getAttackTarget();
                laser.setRotationTo(target.getPosX(), target.getPosYEye(), target.getPosZ(), 0);
            }
            world.addEntity(laser);
            return true;
        }
        return false;
    }

    @Override
    public int rpCost() {
        return 10;
    }
}
