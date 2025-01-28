package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class StardustUpperAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.STARDUST_UPPER.create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (anim.isAtTick(0.12)) {
            handler.setSpinStartRot(entity.getYRot() - 110);
        }
        if (anim.isAtTick(0.28))
            entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH_HEAVY.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1f);
        if (anim.isAtTick(0.24) || anim.isAtTick(0.88)) {
            Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
            handler.setMoveTargetDir(dir.scale(0.8), anim, anim.getTick() + 1);
        }
        if (anim.isAtTick(0.84)) {
            handler.resetHitEntityTracker();
        }
        if (anim.isAtTick(0.92))
            entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH_HEAVY.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1f);

        CombatUtils.EntityAttack attack = spinAttack(entity, anim, 0.16, 0.88,
                handler.getSpinStartRot(), handler.getSpinStartRot() + 410, 0);
        if (attack != null) {
            handler.addHitEntityTracker(attack
                    .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))
                    .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                    .executeAttack());
        }
        CombatUtils.EntityAttack attack2 = spinAttack(entity, anim, 0.88, 1.48,
                handler.getSpinStartRot() + 410, handler.getSpinStartRot() + 680, p -> Mth.sin(p * Mth.PI) * 50, 0);
        if (attack2 != null) {
            handler.addHitEntityTracker(attack2
                    .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))
                    .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                    .doOnSuccess(target -> {
                        CombatUtils.knockBackEntity(entity, target, 0.4f);
                        target.setDeltaMovement(target.getDeltaMovement().add(0, 0.3, 0));
                    })
                    .executeAttack());
        }
    }
}
