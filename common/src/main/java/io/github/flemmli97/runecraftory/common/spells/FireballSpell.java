package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.action.AttackAction;
import io.github.flemmli97.runecraftory.api.action.AttackActions;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityFireball;
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
        if (!Spell.tryUseWithCost(entity, stack, this.rpCost(), EnumSkills.FIRE))
            return false;
        EntityFireball ball = new EntityFireball(level, entity, this.big);
        if (entity instanceof Mob mob && mob.getTarget() != null) {
            ball.shootAtEntity(mob.getTarget(), 1, 0, 0.2f);
        } else {
            ball.shoot(entity, entity.getXRot(), entity.getYRot(), 0, 1, 0);
        }
        ball.setDamageMultiplier(0.95f + lvl * 0.05f + (this.big ? 0.1f : 0));
        level.addFreshEntity(ball);
        if (entity instanceof Player player) {
            return !Platform.INSTANCE.getPlayerData(player).map(cap -> cap.getWeaponHandler().canConsecutiveExecute(player, this.useAction())).orElse(false);
        }
        return true;
    }

    @Override
    public AttackAction useAction() {
        return this.big ? AttackActions.FIREBALL_BIG_USE : AttackActions.FIREBALL_USE;
    }
}
