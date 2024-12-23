package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWindGust;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.ProjectileUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class GustSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        EntityWindGust gust = new EntityWindGust(level, entity);
        gust.setPos(gust.getX(), gust.getY() + 1.5, gust.getZ());
        Vec3 target = ProjectileUtil.getAimTarget(entity);
        if (target != null) {
            Vec3 dir = (new Vec3(target.x() - gust.getX(), target.y() - gust.getY(), target.z() - gust.getZ()));
            gust.setRotationToDir(dir.x, dir.y, dir.z, 0);
        } else {
            gust.setYRot(entity.getYRot());
            gust.setXRot(entity.getXRot());
        }
        level.addFreshEntity(gust);
        playSound(entity, ModSounds.SPELL_GENERIC_WIND_LONG.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        return true;
    }
}
