package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class EvokerFangSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        Vec3 pos = entity.position();
        Vec3 look;
        if (entity instanceof Mob mob && mob.getTarget() != null)
            look = new Vec3(mob.getTarget().getX() - entity.getX(), 0, mob.getTarget().getZ() - entity.getZ()).normalize();
        else
            look = Vec3.directionFromRotation(0, entity.getYRot());
        for (int i = 0; i < 7; i++) {
            pos = pos.add(look.x, 0, look.z);
            EvokerFangs fang = new EvokerFangs(level, pos.x(), pos.y(), pos.z(), entity.getYRot(), 10, entity);
            level.addFreshEntity(fang);
        }
        return true;
    }
}
