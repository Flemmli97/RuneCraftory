package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.action.ComboContainer;
import io.github.flemmli97.runecraftory.api.registry.action.DataKey;
import io.github.flemmli97.runecraftory.api.registry.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.common.attachment.AttackActionHandler;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.item.AOEWeapon;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class HammerAxeAttack extends AttackAction {

    private final ComboContainer combo = ComboContainer.Builder.builder()
            .addCombo(ComboContainer.past("done"), 2)
            .addCombo(handler -> handler.isCurrentAnimationDone() && CombatUtils.canPerform(handler.getEntity(), Skills.HAMMERAXE, 20), 0)
            .build();

    @Override
    public AnimationState getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (EntityUtils.attackSpeedModifier(entity));
        return AttackAction.create(PlayerModelAnimations.HAMMER_AXE.get(comboIdx), speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, AttackActionHandler handler, AnimationState anim) {
        if (anim.isAt("attack") && handler.getComboCount() != 3) {
            CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.obbTargets(AOEWeapon.createOBB(entity,
                            CombatUtils.getRange(entity, 0),
                            CombatUtils.getWidth(entity, 0), 0.5)))
                    .executeAttack();
            entity.playSound(RuneCraftorySounds.PLAYER_ATTACK_SWOOSH_HEAVY.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 0.8f);
        }
        if (handler.getComboCount() == 3) {
            if (anim.isAt("spin_start")) {
                handler.store(DataKey.SPIN_ROTATION, entity.getYRot());
                handler.resetHitEntityTracker();
                entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                        SoundEvents.ENDER_DRAGON_FLAP, entity.getSoundSource(), 0.7f, 0.5f);
            }
            if (anim.isAt("reset")) {
                handler.resetHitEntityTracker();
                entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                        SoundEvents.ENDER_DRAGON_FLAP, entity.getSoundSource(), 1, 0.7f);
            }
            if (anim.isPast("spin_start") && !anim.isPast("spin_end")) {
                Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                if (anim.isAt("spin_start"))
                    handler.store(DataKey.MOVE_DIRECTION, dir.scale(0.35).add(0, 0.15, 0));
                if (anim.isAt("spin_middle"))
                    handler.store(DataKey.MOVE_DIRECTION, dir.scale(0.35).add(0, -0.15, 0));
                entity.resetFallDistance();
                if (!entity.level().isClientSide) {
                    handler.addHitEntityTracker(CombatUtils.EntityAttack.create(entity,
                                    CombatUtils.EntityAttack.aabbTargets(entity.getBoundingBox().inflate(1)))
                            .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                            .withAttackSound(SoundEvents.PLAYER_ATTACK_STRONG)
                            .executeAttack());
                }
            } else
                handler.store(DataKey.MOVE_DIRECTION, null);
            handler.applyMoveDirection();
            handler.store(DataKey.FIXED_LOOK, anim.isPast("spin_start") && !anim.isPast("spin_end"));
        }
    }

    @Override
    public void onStart(LivingEntity entity, AttackActionHandler handler) {
        if (handler.getComboCount() == 3 && entity instanceof ServerPlayer player)
            LevelCalc.useRP(Platform.INSTANCE.getPlayerData(player), GeneralConfig.hammerAxeUltimate, true, 0, false);
    }

    @Override
    public boolean isInvulnerable(LivingEntity entity, AttackActionHandler handler) {
        return handler.getComboCount() == 3;
    }

    @Override
    public float movementReduction(AnimationState current) {
        return GeneralConfig.MOVE_SPEED_ATTACK.get().floatValue();
    }

    @Override
    public ComboContainer combos() {
        return this.combo;
    }

    @Override
    public boolean usableOnMounts(int targetCombo) {
        return targetCombo != 3;
    }
}
