package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityThiccLightningBolt;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

public class BigLightningBoltSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        EntityThiccLightningBolt bolt = new EntityThiccLightningBolt(level, entity);
        bolt.setDamageMultiplier(0.95f + lvl * 0.05f);
        if (entity instanceof Mob mob && mob.getTarget() != null) {
            bolt.shootAtEntity(mob.getTarget(), 0.15f, 0, 0);
        } else
            bolt.shoot(entity, 17, entity.getYRot(), 0, 0.15f, 0);
        level.addFreshEntity(bolt);
        return true;
    }
}
