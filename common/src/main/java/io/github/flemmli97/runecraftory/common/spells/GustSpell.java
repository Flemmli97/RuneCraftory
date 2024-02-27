package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWindGust;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class GustSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        EntityWindGust gust = new EntityWindGust(level, entity);
        gust.setPos(gust.getX(), gust.getY() + 1.5, gust.getZ());
        if (entity instanceof Mob mob && mob.getTarget() != null) {
            LivingEntity target = mob.getTarget();
            Vec3 dir = target.position().subtract(entity.position());
            double len = dir.length();
            if (len > 6)
                len = 1;
            dir = new Vec3(dir.x(), 0, dir.z()).normalize().scale(len);
            gust.setRotationTo(target.getX() + dir.x(), target.getY() + target.getBbHeight() * 0.3, target.getZ() + dir.z(), 0);
        }
        level.addFreshEntity(gust);
        playSound(entity, ModSounds.SPELL_GENERIC_WIND_LONG.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        return true;
    }
}
