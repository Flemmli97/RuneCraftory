package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.FurnitureEntity;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ProjectileUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class PlushThrowSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        int plushAmount = entity.getRandom().nextInt(7) + 7;
        for (int i = 0; i < plushAmount; ++i) {
            FurnitureEntity furniture = new FurnitureEntity(level, entity, entity.getRandom().nextBoolean() ? FurnitureEntity.Type.WOOLYPLUSH : FurnitureEntity.Type.CHIPSQUEEKPLUSH);
            furniture.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.8f));

            Vec3 target = ProjectileUtils.getAimTarget(entity);
            Vec3 dir;
            if (target != null) {
                dir = target.subtract(furniture.position()).normalize();
            } else {
                dir = Vec3.directionFromRotation(entity.getXRot(), entity.getYRot());
            }
            dir = dir.scale(0.45 + entity.getRandom().nextDouble() * 0.2).add(0, 2.5, 0);
            furniture.shoot(dir.x, dir.y, dir.z, 0.9f + entity.getRandom().nextFloat() * 0.25f, 7);
            level.addFreshEntity(furniture);
        }
        return true;
    }
}
