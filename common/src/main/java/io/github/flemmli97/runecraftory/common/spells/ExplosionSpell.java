package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.ExplosionSpellEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.common.utils.ProjectileUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ExplosionSpell extends Spell {

    @Override
    public void levelSkill(ServerPlayer player) {
        LevelCalc.levelSkill(RunecraftoryAttachments.PLAYER_DATA.get().get(player), Skills.FIRE, 10);
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        ExplosionSpellEntity spell = new ExplosionSpellEntity(level, entity);
        spell.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, this));
        ProjectileUtils.shoot(entity, spell, 1.3f, 0);
        level.addFreshEntity(spell);
        playSound(entity, RuneCraftorySounds.SPELL_GENERIC_FIRE_BALL.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        return true;
    }
}
