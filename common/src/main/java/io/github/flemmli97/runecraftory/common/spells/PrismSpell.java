package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityLightBall;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PrismSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        List<Entity> lights = level.getEntities(entity, entity.getBoundingBox().inflate(4), e -> e instanceof EntityLightBall light && light.getOwner() == entity);
        lights.forEach(e -> e.remove(Entity.RemovalReason.KILLED));
        EntityLightBall.createQuadLights(level, entity, true, CombatUtils.getAbilityDamageBonus(lvl, 0.9f));
        return true;
    }
}
