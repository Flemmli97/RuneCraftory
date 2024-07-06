package io.github.flemmli97.runecraftory.common.spells;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityFireball;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class MultiFireballSpell extends Spell {

    public final int amount;
    public final float damage, angle;

    public MultiFireballSpell(int amount, float damage, float angle) {
        this.amount = amount;
        this.damage = damage;
        this.angle = angle;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        Vec3 pos = entity.position().add(0, entity.getEyeHeight() - 0.1, 0);
        Vec3 dir;
        if (entity instanceof Mob mob && mob.getTarget() != null) {
            Vec3 targetPos = EntityUtil.getStraightProjectileTarget(pos, mob.getTarget());
            dir = new Vec3(targetPos.x() - pos.x(), targetPos.y() - pos.y(), targetPos.z() - pos.z());
        } else {
            dir = entity.getLookAngle();
        }
        Vec3 up = new Vec3(0, 1, 0);
        float angle = -this.angle;
        float inc = (this.angle * 2) / (this.amount - 1);
        for (float y = angle; y <= this.angle; y += inc) {
            Quaternion quaternion = new Quaternion(new Vector3f(up), y, true);
            Vector3f newDir = new Vector3f(dir);
            newDir.transform(quaternion);
            EntityFireball other = new EntityFireball(level, entity, false);
            other.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, this.damage));
            other.shoot(newDir.x(), newDir.y(), newDir.z(), 1, 0);
            level.addFreshEntity(other);
        }
        playSound(entity, ModSounds.SPELL_GENERIC_FIRE_BALL.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        return true;
    }
}
