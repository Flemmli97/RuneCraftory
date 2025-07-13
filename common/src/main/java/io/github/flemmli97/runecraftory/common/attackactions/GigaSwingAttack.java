package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.action.DataKey;
import io.github.flemmli97.runecraftory.api.registry.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.common.attachment.AttackActionHandler;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class GigaSwingAttack extends AttackAction {

    @Override
    public AnimationState getAnimation(LivingEntity entity, int comboIdx) {
        double speed = ItemNBT.attackSpeedModifier(entity);
        return AttackAction.create(PlayerModelAnimations.GIGA_SWING, speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, AttackActionHandler handler, AnimationState anim) {
        if (anim.isAt("attack_start")) {
            handler.store(DataKey.SPIN_ROTATION, entity.getYRot() - 50);
            Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1)).scale(-1);
            handler.store(DataKey.MOVE_DIRECTION, dir.scale(2));
            entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH_HEAVY.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        }
        if (!entity.level().isClientSide) {
            CombatUtils.EntityAttack attack = spinAttack(entity, anim, anim.getMarker("attack_start", 0), anim.getMarker("attack_end", 0),
                    handler.get(DataKey.SPIN_ROTATION), handler.get(DataKey.SPIN_ROTATION) + 250, p -> Mth.sin(p * Mth.PI) * 50, 0);
            if (attack != null) {
                handler.addHitEntityTracker(attack
                        .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                        .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))
                        .doOnSuccess(e -> CombatUtils.knockBackEntity(entity, e, 3f))
                        .executeAttack());
            }
        }
    }

    @Override
    public boolean isInvulnerable(LivingEntity entity, AttackActionHandler handler) {
        return true;
    }

    @Override
    public boolean usableOnMounts(int targetCombo) {
        return false;
    }
}
