package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class CycloneAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
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
        if (!entity.level.isClientSide && anim.isPastTick(0.2) && !anim.isPastTick(1.04)) {
            int start = Mth.ceil(0.2 * 20.0D);
            int end = Mth.ceil(1.04 * 20.0D);
            float len = (end - start) / anim.getSpeed();
            float f = (anim.getTick() - start) / anim.getSpeed();
            float angleInc = -(360 * 5) / len;
            float rot = handler.getSpinStartRot();
            handler.addHitEntityTracker(CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.circleTargets((rot + f * angleInc), (rot + (f + 1) * angleInc), 0))
                    .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                    .withBonusAttributesMultiplier(Map.of(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack)))
                    .doOnSuccess(e -> CombatUtils.knockBackEntity(entity, e, 1.5f))
                    .executeAttack());
        }
    }

    @Override
    public boolean isInvulnerable(LivingEntity entity, WeaponHandler handler) {
        return true;
    }

    @Override
    public boolean disableMovement(AnimatedAction current) {
        return false;
    }
}
