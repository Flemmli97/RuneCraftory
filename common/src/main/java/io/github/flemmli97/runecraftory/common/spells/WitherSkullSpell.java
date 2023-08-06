package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class WitherSkullSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this.rpCost()))
            return false;
        Vec3 look;
        if (entity instanceof Mob mob && mob.getTarget() != null)
            look = new Vec3(mob.getTarget().getX() - entity.getX(), mob.getTarget().getY() - entity.getY(), mob.getTarget().getZ() - entity.getZ()).normalize();
        else
            look = entity.getLookAngle();
        WitherSkull skull = new WitherSkull(level, entity, look.x, look.y, look.z);
        skull.setPos(entity.getX(), entity.getEyeY(), entity.getZ());
        level.addFreshEntity(skull);
        return true;
    }
}
