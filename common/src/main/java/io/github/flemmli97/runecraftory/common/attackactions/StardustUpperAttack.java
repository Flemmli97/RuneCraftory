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
        if (anim.isAt("attack_start_1")) {
            handler.setSpinStartRot(entity.getYRot() - 110);
            entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH_HEAVY.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1f);
            Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
            entity.setDeltaMovement(dir.scale(0.6));
        }
        if (anim.isAt("attack_start_2")) {
            handler.resetHitEntityTracker();
            entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH_HEAVY.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1f);
            Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
            entity.setDeltaMovement(dir.scale(0.6));
        }
        if (!entity.level.isClientSide) {
            CombatUtils.EntityAttack attack = spinAttack(entity, anim, anim.getMarker("attack_start_1", 0), anim.getMarker("attack_end_1", 0),
                    handler.getSpinStartRot(), handler.getSpinStartRot() + 410, 0);
            if (attack != null) {
                handler.addHitEntityTracker(attack
                        .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))
                        .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                        .executeAttack());
            }
            CombatUtils.EntityAttack attack2 = spinAttack(entity, anim, anim.getMarker("attack_start_2", 0), anim.getMarker("attack_end_2", 0),
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
}
