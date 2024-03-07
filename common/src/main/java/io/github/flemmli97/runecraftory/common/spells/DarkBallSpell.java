package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityDarkBall;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

public class DarkBallSpell extends Spell {

    private final EntityDarkBall.Type type;

    public DarkBallSpell(EntityDarkBall.Type type) {
        this.type = type;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        EntityDarkBall ball = new EntityDarkBall(level, entity, this.type);
        ball.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, this.type == EntityDarkBall.Type.BALL ? 1 : 0.9f));
        float vel = this.type == EntityDarkBall.Type.BALL ? 0.09f : 0.23f;
        if (entity instanceof Mob mob && mob.getTarget() != null) {
            ball.shootAtEntity(mob.getTarget(), vel * 1.3f, 0, 0.2f, 0.3);
        } else
            ball.shoot(entity, entity.getXRot(), entity.getYRot(), 0, vel, 0);
        level.addFreshEntity(ball);
        playSound(entity, ModSounds.SPELL_GENERIC_DARK.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        return true;
    }
}
