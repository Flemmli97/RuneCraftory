package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.AttackAction;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class TornadoSwingAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        if (chain != 0)
            chain = 1;
        return PlayerModelAnimations.TORNADO_SWING.get(chain).create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        entity.setDeltaMovement(entity.getDeltaMovement().scale(0.6));
        entity.xxa *= 0.6f;
        entity.zza *= 0.6f;
        if (!entity.level.isClientSide) {
            if (handler.getChainCount() == 1) {
                if (anim.isPastTick(0.16) && !anim.isPastTick(1.2)) {
                    int start = Mth.ceil(0.88 * 20.0D);
                    int end = Mth.ceil(1.2 * 20.0D);
                    float len = (end - start) / anim.getSpeed();
                    float f = (anim.getTick() - start) / anim.getSpeed();
                    float angleInc = -450 / len;
                    float rot = handler.getSpinStartRot();
                    handler.addHitEntityTracker(CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.circleTargets((rot + f * angleInc), (rot + (f + 1) * angleInc), 0.5f))
                            .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                            .withBonusAttributesMultiplier(Map.of(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack)))
                            .executeAttack());
                }
            } else {
                if (anim.isPastTick(0) && !anim.isPastTick(0.96)) {
                    int start = 0;
                    int end = Mth.ceil(0.96 * 20.0D);
                    float len = (end - start) / anim.getSpeed();
                    float f = (anim.getTick() - start) / anim.getSpeed();
                    float angleInc = -380 / len;
                    float rot = handler.getSpinStartRot();
                    handler.addHitEntityTracker(CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.circleTargets((rot + f * angleInc), (rot + (f + 1) * angleInc), 0.5f))
                            .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                            .withBonusAttributesMultiplier(Map.of(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack)))
                            .executeAttack());
                }
            }
        }
    }

    @Override
    public void onStart(LivingEntity entity, WeaponHandler handler) {
        super.onStart(entity, handler);
        handler.setSpinStartRot(entity.getYRot() + (handler.getChainCount() == 1 ? 90 : 20));
    }

    @Override
    public boolean canOverride(LivingEntity entity, WeaponHandler handler) {
        if (handler.getChainCount() == 1)
            return handler.getCurrentAnim().isPastTick(0.80) && !handler.getCurrentAnim().isPastTick(1.0);
        return handler.getCurrentAnim().isPastTick(0.56) && !handler.getCurrentAnim().isPastTick(0.72);
    }

    @Override
    public AttackChain attackChain(LivingEntity entity, int chain) {
        return new AttackChain(4, 0);
    }

    @Override
    public boolean disableMovement(AnimatedAction current) {
        return false;
    }
}
