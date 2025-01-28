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

public class CycloneAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.CYCLONE.create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (!anim.isPastTick(0.2) || anim.isPastTick(1.16)) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(0, 1, 0));
            entity.xxa = 0;
            entity.zza = 0;
        }
        if (anim.isAtTick(0.16)) {
            handler.setSpinStartRot(entity.getYRot() + 170);
            handler.resetHitEntityTracker();
            entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        }
        if (anim.isAtTick(0.36) || anim.isAtTick(0.56) || anim.isAtTick(0.72) || anim.isAtTick(0.88)) {
            handler.resetHitEntityTracker();
            entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        }
        CombatUtils.EntityAttack attack = spinAttack(entity, anim, 0.2, 1.04,
                handler.getSpinStartRot(), handler.getSpinStartRot() - 360 * 4.5f, 0);
        if (attack != null) {
            handler.addHitEntityTracker(attack
                    .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                    .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))
                    .doOnSuccess(e -> CombatUtils.knockBackEntity(entity, e, 1.5f))
                    .executeAttack());
        }
    }

    @Override
    public boolean isInvulnerable(LivingEntity entity, WeaponHandler handler) {
        return true;
    }

    @Override
    public float movementReduction(AnimatedAction current) {
        return 1;
    }
}
