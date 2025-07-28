package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.StatusBallEntity;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ProjectileUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class StatusBallSpell extends Spell {

    private final StatusBallEntity.Type type;

    public StatusBallSpell(StatusBallEntity.Type type) {
        this.type = type;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        StatusBallEntity ball = new StatusBallEntity(level, entity);
        ball.setType(this.type);
        ball.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, this));
        ball.setPos(entity.getX(), entity.getY() + 0.4, entity.getZ());
        Vec3 target = ProjectileUtils.getAimTarget(entity);
        if (target != null) {
            ball.shootAtPos(target, 0.13f, 7 - level.getDifficulty().getId() * 2);
        } else {
            ball.shootFromRotation(entity, entity.getXRot() + 5, entity.getYRot(), 0.0F, 0.13f, 1.0F);
        }
        level.addFreshEntity(ball);
        return true;
    }
}
