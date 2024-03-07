package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityFireball;
import io.github.flemmli97.runecraftory.common.registry.ModAttackActions;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
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
        EntityFireball ball = new EntityFireball(level, entity, this.big);
        if (entity instanceof Mob mob && mob.getTarget() != null) {
            ball.shootAtEntity(mob.getTarget(), 1, 0, 0.2f);
        } else {
            ball.shoot(entity, entity.getXRot(), entity.getYRot(), 0, 1, 0);
        }
        ball.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, this.big ? 1 : 0.8f));
        level.addFreshEntity(ball);
        playSound(entity, ModSounds.SPELL_GENERIC_FIRE_BALL.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        if (entity instanceof Player player) {
            return !Platform.INSTANCE.getPlayerData(player).map(cap -> cap.getWeaponHandler().canExecuteAction(player, this.useAction())).orElse(false);
        }
        return true;
    }

    @Override
    public AttackAction useAction() {
        return this.big ? ModAttackActions.FIREBALL_BIG_USE.get() :
                ModAttackActions.FIREBALL_USE.get();
    }
}
