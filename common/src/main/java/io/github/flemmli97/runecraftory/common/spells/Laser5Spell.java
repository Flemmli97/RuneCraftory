package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityThunderboltBeam;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.MathsHelper;
import io.github.flemmli97.runecraftory.common.utils.ProjectileUtils;
import io.github.flemmli97.tenshilib.common.utils.math.MathUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

public class Laser5Spell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        Vec3 target = ProjectileUtils.getAimTarget(entity);
        Vec3 dir;
        if (target != null) {
            dir = target.subtract(entity.getEyePosition());
        } else {
            dir = entity.calculateViewVector(entity.getXRot() * 0.5f, entity.getYRot());
        }
        Vec3 up = MathsHelper.getUp(dir);
        for (Vector3d vec : MathUtils.rotatedVecs(new Vector3d(dir.x(), dir.y(), dir.z()), new Vector3d(up.x(), up.y(), up.z()), -50, 50, 25)) {
            EntityThunderboltBeam beam = new EntityThunderboltBeam(level, entity);
            beam.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.9f));
            beam.setRotationToDir(vec.x(), vec.y(), vec.z(), 0);
            level.addFreshEntity(beam);
        }
        playSound(entity, ModSounds.SPELL_GENERIC_ELECTRIC_ZAP.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        return true;
    }
}
