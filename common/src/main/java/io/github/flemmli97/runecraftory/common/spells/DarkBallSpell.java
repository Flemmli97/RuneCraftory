package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.DarkBallEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ProjectileUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class DarkBallSpell extends Spell {

    private final DarkBallEntity.Type type;

    public DarkBallSpell(DarkBallEntity.Type type) {
        this.type = type;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        DarkBallEntity ball = new DarkBallEntity(level, entity, this.type);
        ball.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, this));
        float vel = this.type == DarkBallEntity.Type.BALL ? 0.09f : 0.23f;
        Vec3 target = ProjectileUtils.getAimTarget(entity, ball.position());
        if (target != null) {
            ball.shootAtPos(target, vel, 0);
        } else {
            ball.shoot(entity, entity.getXRot(), entity.getYRot(), 0, vel, 0);
        }
        level.addFreshEntity(ball);
        playSound(entity, RuneCraftorySounds.SPELL_GENERIC_DARK.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        return true;
    }
}
