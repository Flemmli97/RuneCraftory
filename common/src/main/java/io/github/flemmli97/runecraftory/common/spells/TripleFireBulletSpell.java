package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityBullet;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ProjectileUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

public class TripleFireBulletSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        EntityBullet projectile = new EntityBullet(level, entity);
        Vec3 target = ProjectileUtil.getAimTarget(entity);
        Vec3 dir;
        if (target != null) {
            dir = target.subtract(projectile.position());
        } else {
            dir = entity.getLookAngle();
        }
        projectile.setElement(EnumElement.FIRE);
        projectile.setStraight();
        projectile.shoot(dir.x, dir.y, dir.z, 1, 0);
        projectile.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.9f));
        level.addFreshEntity(projectile);

        Vec3 up = entity.getUpVector(1);
        for (float y = -15; y <= 15; y += 30) {
            Vector3d newDir = new Vector3d(dir.x(), dir.y(), dir.z())
                    .rotateAxis(y * Mth.DEG_TO_RAD, up.x(), up.y(), up.z());
            EntityBullet other = new EntityBullet(level, entity);
            other.setStraight();
            other.setElement(EnumElement.FIRE);
            other.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.85f));
            other.shoot(newDir.x(), newDir.y(), newDir.z(), 1, 0);
            level.addFreshEntity(other);
        }
        playSound(entity, ModSounds.SPELL_GENERIC_FIRE_BALL.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        return true;
    }
}