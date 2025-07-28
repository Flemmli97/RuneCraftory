package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.BigRaccoonLeafEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ProjectileUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class BigLeafSpell extends Spell {

    private final boolean doubleShot;

    public BigLeafSpell(boolean doubleShot) {
        this.doubleShot = doubleShot;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        int leafs = this.doubleShot ? 2 : 1;
        for (int i = 0; i < leafs; i++) {
            BigRaccoonLeafEntity leaf = new BigRaccoonLeafEntity(level, entity);
            leaf.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, this));
            ProjectileUtils.shoot(entity, leaf, 1, 0);
            leaf.setCenter(i % 2 == 0 ? 2.5f : 4);
            leaf.withRightSpin(i % 2 == 0);
            level.addFreshEntity(leaf);
        }
        playSound(entity, RuneCraftorySounds.ENTITY_FLOWER_LILY_STEP.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.2f);
        return true;
    }
}
