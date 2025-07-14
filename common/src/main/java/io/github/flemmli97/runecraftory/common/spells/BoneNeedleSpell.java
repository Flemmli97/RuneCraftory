package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.BoneNeedleEntity;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.MathsHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

public class BoneNeedleSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        Vec3 dir;
        if (entity instanceof Mob mob && mob.getTarget() != null) {
            Vec3 pos = new BoneNeedleEntity(level, entity).position().add(0, -entity.getBbHeight() * 0.25, 0);
            dir = EntityUtils.getStraightProjectileTarget(pos, mob.getTarget()).subtract(pos);
        } else
            dir = Vec3.directionFromRotation(entity.getXRot(), entity.getYRot());
        float[] yxRot = MathsHelper.YXRotFrom(dir);
        Vec3 side = Vec3.directionFromRotation(yxRot[1], yxRot[0] + 90);
        Vec3 up = Vec3.directionFromRotation(yxRot[1] + 90, yxRot[0]);
        double offset = -3;
        double max = Math.abs(offset);
        double inc = max * 2.7 / 16;
        while (offset <= max) {
            BoneNeedleEntity needle = new BoneNeedleEntity(level, entity);
            Vector3d direction = new Vector3d(dir.x(), dir.y(), dir.z())
                    .rotateAxis(Mth.DEG_TO_RAD * 15 * offset, up.x(), up.y(), up.z());
            needle.shoot(direction.x(), direction.y(), direction.z(), entity.getRandom().nextFloat() * 0.1f + 0.6f, 2);
            Vec3 random = needle.position().add(0, -entity.getBbHeight() * 0.25, 0).add(side.multiply(offset * 0.5, 0, offset * 0.5));
            offset += entity.getRandom().nextDouble() * inc * 0.5 + inc * 0.5;
            needle.setPos(random);
            needle.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.65f));
            level.addFreshEntity(needle);
        }
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ARROW_SHOOT, entity.getSoundSource(), 1.0f, 1.2f + level.getRandom().nextFloat() * 0.1f);
        return true;
    }
}
