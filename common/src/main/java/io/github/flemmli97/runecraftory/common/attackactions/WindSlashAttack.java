package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.action.ComboContainer;
import io.github.flemmli97.runecraftory.api.registry.action.DataKey;
import io.github.flemmli97.runecraftory.api.registry.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.common.attachment.AttackActionHandler;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class WindSlashAttack extends AttackAction {

    private final ComboContainer combo = ComboContainer.Builder.builder()
            .addCombo(handler -> handler.getAnimation().isPast("chain_start") && !handler.getAnimation().isPast("spin_end"))
            .build();

    @Override
    public AnimationState getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (EntityUtils.attackSpeedModifier(entity));
        return AttackAction.create(PlayerModelAnimations.WIND_SLASH.get(comboIdx), speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, AttackActionHandler handler, AnimationState anim) {
        handler.store(DataKey.FIXED_LOOK, true);
        if (anim.isAt("spin_start")) {
            handler.store(DataKey.SPIN_ROTATION, entity.getYRot());
            handler.resetHitEntityTracker();
            entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                    SoundEvents.ENDER_DRAGON_FLAP, entity.getSoundSource(), 0.7f, 0.5f);
            Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
            handler.store(DataKey.MOVE_DIRECTION, dir.scale(0.35));
        }
        if (anim.isAt("reset")) {
            handler.resetHitEntityTracker();
            entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                    SoundEvents.ENDER_DRAGON_FLAP, entity.getSoundSource(), 1, 0.7f);
        }
        if (anim.isAt("leap")) {
            Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
            handler.store(DataKey.MOVE_DIRECTION, dir.scale(0.35).add(0, 0.3, 0));
        }
        if (anim.isAt("spin_end")) {
            handler.store(DataKey.MOVE_DIRECTION, null);
        }
        if (anim.isPast("spin_start") && !anim.isPast("spin_end")) {
            entity.resetFallDistance();
            if (!entity.level().isClientSide) {
                handler.addHitEntityTracker(CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.aabbTargets(entity.getBoundingBox().inflate(0.75)))
                        .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack, RuneCraftorySpells.WIND_SLASH))
                        .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                        .executeAttack());
            }
        }
        handler.applyMoveDirection();
    }

    @Override
    public void onStart(LivingEntity entity, AttackActionHandler handler) {
        super.onStart(entity, handler);
        if (handler.getComboCount() == 2) {
            handler.store(DataKey.SPIN_ROTATION, entity.getYRot());
            handler.resetHitEntityTracker();
            entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                    SoundEvents.ENDER_DRAGON_FLAP, entity.getSoundSource(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 0.7f);
            Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
            handler.store(DataKey.MOVE_DIRECTION, dir.scale(0.35));
        }
    }

    @Override
    public ComboContainer combos() {
        return this.combo;
    }

    @Override
    public boolean usableOnMounts(int targetCombo) {
        return false;
    }
}
