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
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class StardustUpperAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.STARDUST_UPPER.create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (anim.isAtTick(0.12)) {
            handler.setSpinStartRot(entity.getYRot() - 90);
        }
        if (anim.isAtTick(0.24) || anim.isAtTick(0.88)) {
            Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
            handler.setMoveTargetDir(dir.scale(0.8), anim, anim.getTick() + 1);
        }
        if (anim.isAtTick(0.84)) {
            handler.resetHitEntityTracker();
        }
        if (!entity.level.isClientSide) {
            if (anim.isPastTick(0.16) && !anim.isPastTick(1.52)) {
                int start = Mth.ceil(0.16 * 20.0D);
                int end = Mth.ceil(1.52 * 20.0D);
                float len = (end - start) / anim.getSpeed();
                float f = (anim.getTick() - start) / anim.getSpeed();
                float angleInc = 690 / len;
                float rot = handler.getSpinStartRot();
                handler.addHitEntityTracker(CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.circleTargets((rot + f * angleInc), (rot + (f + 1) * angleInc), 0.5f))
                        .withBonusAttributesMultiplier(Map.of(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack)))
                        .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                        .executeAttack());
            }
        }
    }
}
