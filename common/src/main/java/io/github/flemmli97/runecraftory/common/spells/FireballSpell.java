package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.common.entities.misc.FireballEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttackActions;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ProjectileUtils;
import io.github.flemmli97.tenshilib.loader.registry.AttachmentRegister;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class FireballSpell extends Spell {

    public final boolean big;

    public FireballSpell(boolean big) {
        this.big = big;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        FireballEntity ball = new FireballEntity(level, entity, this.big);
        ProjectileUtils.shoot(entity, ball, 1, 0);
        ball.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, this));
        level.addFreshEntity(ball);
        playSound(entity, RuneCraftorySounds.SPELL_GENERIC_FIRE_BALL.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        if (entity instanceof Player player) {
            return !RunecraftoryAttachments.PLAYER_DATA.get().get(player).getWeaponHandler().canExecuteAction(this.useAction());
        }
        return true;
    }

    @Override
    public AttackAction useAction() {
        return this.big ? RuneCraftoryAttackActions.FIREBALL_BIG_USE.get() :
                RuneCraftoryAttackActions.FIREBALL_USE.get();
    }
}
