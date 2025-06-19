package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.AttackActionHandler;
import io.github.flemmli97.runecraftory.api.action.ComboContainer;
import io.github.flemmli97.runecraftory.api.action.DataKey;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.item.AOEWeapon;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class SpearAttack extends AttackAction {

    private final ComboContainer combo = ComboContainer.Builder.builder()
            .addCombo(ComboContainer.past("done"), 2)
            .addCombo(ComboContainer.past("done"), 2)
            .addCombo(ComboContainer.past("done"), 2)
            .addCombo(handler -> handler.isCurrentAnimationDone() && CombatUtils.canPerform(handler.getEntity(), EnumSkills.SPEAR, 20), 0)
            .build();

    @Override
    public AnimationState getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return AttackAction.create(PlayerModelAnimations.SPEAR.get(comboIdx), speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, AttackActionHandler handler, AnimationState anim) {
        if (handler.getComboCount() != 5) {
            if (anim.isAt("attack")) {
                if (!entity.level().isClientSide) {
                    CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.obbTargets(AOEWeapon.createOBB(entity,
                                    CombatUtils.getRange(entity, 0),
                                    CombatUtils.getWidth(entity, 0), 0.5)))
                            .executeAttack();
                }
                entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH_LIGHT.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
            }
        }
        Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
        switch (handler.getComboCount()) {
            case 1, 3, 4 -> {
                if (anim.isAt("step")) {
                    entity.setDeltaMovement(dir.scale(0.3));
                }
            }
            case 2 -> {
                if (anim.isAt("step")) {
                    entity.setDeltaMovement(dir.scale(0.15));
                }
            }
            case 5 -> {
                if (anim.isAt("spin_start")) {
                    handler.store(DataKey.SPIN_ROTATION, entity.getYRot());
                    handler.resetHitEntityTracker();
                    entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                }
                if (anim.isAt("reset")) {
                    handler.resetHitEntityTracker();
                    entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                }
                CombatUtils.EntityAttack attack = spinAttack(entity, anim, anim.getMarker("spin_start", 0), anim.getMarker("spin_end", 0),
                        handler.get(DataKey.SPIN_ROTATION) + 180, handler.get(DataKey.SPIN_ROTATION) + 1260, -1);
                if (attack != null) {
                    handler.addHitEntityTracker(attack
                            .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                            .executeAttack());
                }
                if (anim.isAt("leap"))
                    entity.setDeltaMovement(dir.scale(1.3).add(0, 0.4, 0));
                if (anim.isAt("slam")) {
                    Vec3 look = entity.getLookAngle();
                    look = new Vec3(look.x(), 0, look.z()).scale(1.2);
                    Vec3 attackPos = entity.position().add(0, 0.2, 0).add(look);
                    CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.aabbTargets(new AABB(-1, -1.2, 0, 1, 1.2, 2.5), false))
                            .executeAttack();
                    Vec3 pos = entity.position().add(0, -1, 0);
                    BlockPos.MutableBlockPos mut = new BlockPos.MutableBlockPos();
                    Vec3 axis = new Vec3(0, 1, 0);
                    Vec3 dir2 = new Vec3(0, 0, 1).scale(1);
//                    for (int i = -180; i < 180; i += 15) {
//                        Vec3 scaled = MathUtils.rotate(axis, dir2, i);
//                        mut.set(Mth.floor(pos.x() + dir2.x()), Mth.floor(pos.y()), Mth.floor(pos.z() + dir2.z()));
//                        BlockState state = entity.level().getBlockState(mut);
//                        if (state.getRenderShape() != RenderShape.INVISIBLE && entity.level() instanceof ServerLevel serverLevel)
//                            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state), attackPos.x() + scaled.x() + entity.getDeltaMovement().x(), entity.getY() + 0.1, attackPos.z() + scaled.z() + entity.getDeltaMovement().z(), 0, (float) scaled.x(), 1.5f, (float) scaled.z(), 1);
//                    }
                    entity.playSound(SoundEvents.DRAGON_FIREBALL_EXPLODE, 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                }
            }
        }
        if (handler.getComboCount() == 5) {
            handler.store(DataKey.FIXED_LOOK, anim.isPast("leap") && !anim.isPast("leap_end"));
        }
    }

    @Override
    public void onStart(LivingEntity entity, AttackActionHandler handler) {
        if (handler.getComboCount() == 5 && entity instanceof ServerPlayer player)
            LevelCalc.useRP(Platform.INSTANCE.getPlayerData(player), GeneralConfig.spearUltimate, true, 0, false);
    }

    @Override
    public boolean isInvulnerable(LivingEntity entity, AttackActionHandler handler) {
        return handler.getComboCount() == 5;
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
        return targetCombo != 5;
    }
}
