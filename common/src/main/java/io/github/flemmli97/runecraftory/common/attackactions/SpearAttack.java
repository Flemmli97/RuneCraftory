package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.AttackAction;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class SpearAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.SPEAR.get(chain).create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (anim.canAttack() && handler.getChainCount() != 5) {
            CombatUtils.attack(entity, stack);
            entity.swing(InteractionHand.MAIN_HAND, true);
        }
        Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
        switch (handler.getChainCount()) {
            case 1, 3, 4 -> {
                if (anim.isAtTick(0.28)) {
                    handler.setMoveTargetDir(dir.scale(0.15), anim, anim.getTick());
                }
            }
            case 2 -> {
                if (anim.isAtTick(0.2)) {
                    handler.setMoveTargetDir(dir.scale(0.2), anim, anim.getTick());
                }
            }
            case 5 -> {
                if (anim.isAtTick(0.12)) {
                    handler.setSpinStartRot(entity.getYRot() + 180);
                    handler.resetHitEntityTracker();
                    entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                }
                if (anim.isAtTick(0.6)) {
                    handler.resetHitEntityTracker();
                    entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                }
                if (anim.isPastTick(0.12)) {
                    int start = Mth.ceil(0.12 * 20.0D);
                    int end = Mth.ceil(1.08 * 20.0D);
                    float len = (end - start) / anim.getSpeed();
                    float f = (anim.getTick() - start) / anim.getSpeed();
                    float angleInc = 720 / len;
                    float rot = handler.getSpinStartRot();
                    handler.addHitEntityTracker(CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.circleTargets((rot + f * angleInc), (rot + (f + 1) * angleInc), 0))
                            .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                            .executeAttack());
                }
                if (anim.isAtTick(1.2))
                    handler.setMoveTargetDir(dir.scale(1.8).add(0, 1.5, 0), anim, 1.4);
                if (anim.isAtTick(1.4))
                    handler.setMoveTargetDir(dir.scale(2.5).add(0, -1.5, 0), anim, 1.64);
                if (anim.isAtTick(1.63)) {
                    Vec3 look = entity.getLookAngle();
                    look = new Vec3(look.x(), 0, look.z()).scale(1.2);
                    Vec3 attackPos = entity.position().add(0, 0.2, 0).add(look);
                    CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.aabbTargets(new AABB(-0.8, -1.2, -0.8, 0.8, 1.2, 0.8).move(attackPos)))
                            .executeAttack();
                    Vec3 pos = entity.position().add(0, -1, 0);
                    BlockPos.MutableBlockPos mut = new BlockPos.MutableBlockPos();
                    Vec3 axis = new Vec3(0, 1, 0);
                    Vec3 dir2 = new Vec3(0, 0, 1).scale(1);
                    for (int i = -180; i < 180; i += 15) {
                        Vec3 scaled = MathUtils.rotate(axis, dir2, i);
                        mut.set(Mth.floor(pos.x() + dir2.x()), Mth.floor(pos.y()), Mth.floor(pos.z() + dir2.z()));
                        BlockState state = entity.level.getBlockState(mut);
                        if (state.getRenderShape() != RenderShape.INVISIBLE && entity.getLevel() instanceof ServerLevel serverLevel)
                            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state), attackPos.x() + scaled.x() + entity.getDeltaMovement().x(), entity.getY() + 0.1, attackPos.z() + scaled.z() + entity.getDeltaMovement().z(), 0, (float) scaled.x(), 1.5f, (float) scaled.z(), 1);
                    }
                    entity.playSound(SoundEvents.DRAGON_FIREBALL_EXPLODE, 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                }
            }
        }
        if (handler.getChainCount() == 5) {
            handler.lockLook(anim.isPastTick(01.16) && !anim.isPastTick(1.6));
        }
    }

    @Override
    public void onStart(LivingEntity entity, WeaponHandler handler) {
        if (handler.getChainCount() != 5) {
            entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH_LIGHT.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        } else if (entity instanceof ServerPlayer player)
            Platform.INSTANCE.getPlayerData(player).ifPresent(d -> LevelCalc.useRP(player, d, GeneralConfig.spearUltimate, true, false, false));
    }

    @Override
    public boolean isInvulnerable(LivingEntity entity, WeaponHandler handler) {
        return handler.getChainCount() == 5;
    }

    @Override
    public AttackChain attackChain(LivingEntity entity, int chain) {
        return new AttackChain(CombatUtils.canPerform(entity, EnumSkills.SPEAR, 20) ? 5 : 4, chain == 5 ? 0 : 8);
    }

    @Override
    public boolean disableMovement(AnimatedAction current) {
        return !GeneralConfig.allowMoveOnAttack.get() && super.disableMovement(current);
    }
}
