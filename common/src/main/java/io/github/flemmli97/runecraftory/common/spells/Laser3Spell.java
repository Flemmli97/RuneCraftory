package io.github.flemmli97.runecraftory.common.spells;

import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityThunderboltBeam;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class Laser3Spell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        Vec3 dir;
        if (entity instanceof Mob mob && mob.getTarget() != null) {
            Vec3 pos = entity.position().add(0, entity.getEyeHeight(), 0);
            dir = EntityUtil.getStraightProjectileTarget(pos, mob.getTarget()).subtract(pos).normalize();
        } else {
            dir = entity.getLookAngle();
        }
        for (Vector3f vec : RayTraceUtils.rotatedVecs(dir, new Vec3(0, 1, 0), -20, 20, 20)) {
            EntityThunderboltBeam beam = new EntityThunderboltBeam(level, entity);
            beam.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.9f));
            beam.setRotationToDir(vec.x(), vec.y(), vec.z(), 0);
            level.addFreshEntity(beam);
        }
        playSound(entity, ModSounds.SPELL_GENERIC_ELECTRIC_ZAP.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        return true;
    }
}
