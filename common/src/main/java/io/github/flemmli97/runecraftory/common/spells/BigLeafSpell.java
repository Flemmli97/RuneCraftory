package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityBigRaccoonLeaf;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
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
            EntityBigRaccoonLeaf leaf = new EntityBigRaccoonLeaf(level, entity);
            leaf.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 1.1f));
            float vel = i % 2 == 0 ? 1 : 0.7f;
            if (entity instanceof Mob mob && mob.getTarget() != null) {
                leaf.shootAtEntity(mob.getTarget(), vel, 0, 0, 0);
            } else
                leaf.shoot(entity, entity.getXRot(), entity.getYRot(), 0, vel, 0);
            leaf.setDiameter(i % 2 == 0 ? 4 : 7);
            leaf.withRightSpin(i % 2 == 0);
            level.addFreshEntity(leaf);
        }
        playSound(entity, ModSounds.ENTITY_FLOWER_LILY_STEP.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.2f);
        return true;
    }
}
