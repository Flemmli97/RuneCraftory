package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWindBlade;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;

public class DoubleWindBladeSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        for (int i = 0; i < 2; i++) {
            EntityWindBlade wind = new EntityWindBlade(level, entity);
            wind.setDamageMultiplier(0.95f + lvl * 0.05f);
            wind.shoot(entity, 0, entity.getYRot() - (i == 0 ? 1 : -1) * 40, 0, 0.45f, 0);
            if (entity instanceof Mob mob && mob.getTarget() != null) {
                wind.setTarget(mob.getTarget());
            } else if (entity instanceof Player) {
                EntityHitResult res = WindBladeSpell.calculateEntityFromLook(entity, 10);
                if (res != null) {
                    wind.setTarget(res.getEntity());
                }
            }
            level.addFreshEntity(wind);
        }
        return true;
    }
}
