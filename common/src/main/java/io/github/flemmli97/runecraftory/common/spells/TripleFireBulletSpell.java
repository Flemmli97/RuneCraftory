package io.github.flemmli97.runecraftory.common.spells;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityBullet;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class TripleFireBulletSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        EntityBullet projectile = new EntityBullet(level, entity);
        Vec3 dir;
        if (entity instanceof Mob mob && mob.getTarget() != null) {
            dir = (new Vec3(mob.getTarget().getX() - mob.getX(), mob.getTarget().getY(0.2) - mob.getY(), mob.getTarget().getZ() - mob.getZ()));
        } else {
            dir = entity.getLookAngle();
        }
        projectile.setElement(EnumElement.FIRE);
        projectile.setStraight();
        projectile.shoot(dir.x, dir.y, dir.z, 1, 0);
        projectile.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.85f));
        level.addFreshEntity(projectile);

        Vec3 up = entity.getUpVector(1);
        for (float y = -15; y <= 15; y += 30) {
            Quaternion quaternion = new Quaternion(new Vector3f(up), y, true);
            Vector3f newDir = new Vector3f(dir);
            newDir.transform(quaternion);
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