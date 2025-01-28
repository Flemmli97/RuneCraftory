package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class RoundBreakAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.ROUND_BREAK.create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1)).scale(0.4);
        if (anim.isAtTick(0.2)) {
            handler.setMoveTargetDir(dir.scale(1.8).add(0, 1.6, 0), anim, 0.48);
            handler.setSpinStartRot(entity.getYRot() + 90);
        }
        if (anim.isAtTick(0.24))
            entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH_LIGHT.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        if (anim.isAtTick(0.48))
            handler.setMoveTargetDir(dir.scale(3.3).add(0, -1.6, 0), anim, 0.68);
        CombatUtils.EntityAttack attack = spinAttack(entity, anim, 0.24, 0.6,
                handler.getSpinStartRot(), handler.getSpinStartRot() - 360, 0);
        if (attack != null) {
            handler.addHitEntityTracker(attack
                    .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))
                    .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                    .doOnSuccess(e -> CombatUtils.knockBackEntity(entity, e, 1.5f))
                    .executeAttack());
        }
    }

    @Override
    public boolean isInvulnerable(LivingEntity entity, WeaponHandler handler) {
        return true;
    }
}
