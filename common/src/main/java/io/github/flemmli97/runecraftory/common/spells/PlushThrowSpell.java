package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityFurniture;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class PlushThrowSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        int plushAmount = entity.getRandom().nextInt(8) + 12;
        for (int i = 0; i < plushAmount; ++i) {
            EntityFurniture furniture = new EntityFurniture(level, entity, entity.getRandom().nextBoolean() ? EntityFurniture.Type.WOOLYPLUSH : EntityFurniture.Type.CHIPSQUEEKPLUSH);
            furniture.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.9f));
            if (entity instanceof Mob mob && mob.getTarget() != null) {
                Vec3 dir = mob.getTarget().position().subtract(entity.position()).scale(0.35 + entity.getRandom().nextDouble() * 0.2);
                furniture.shootAtPosition(entity.getX() + dir.x(), mob.getTarget().getY() + 10, entity.getZ() + dir.z(), 0.95f + entity.getRandom().nextFloat() * 0.2f, 9);
            } else
                furniture.shoot(entity, entity.getXRot(), entity.getYRot(), -55, 0.95f + entity.getRandom().nextFloat() * 0.2f, 9);
            level.addFreshEntity(furniture);
        }
        return true;
    }
}
