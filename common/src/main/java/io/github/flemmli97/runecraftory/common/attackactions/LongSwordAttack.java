package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class LongSwordAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.LONG_SWORD.get(chain).create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (!entity.level.isClientSide && anim.canAttack() && handler.getChainCount() != 4) {
            CombatUtils.attack(entity, stack);
        }
        Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
        if (handler.getChainCount() != 4 && anim.isAtTick(0.24)) {
            entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 0.8f);
        }
        switch (handler.getChainCount()) {
            case 2 -> {
                if (anim.isAtTick(0.4)) {
                    handler.setMoveTargetDir(dir.scale(0.4), anim, anim.getTick());
                }
            }
            case 3 -> {
                if (anim.isAtTick(0.44)) {
                    handler.setMoveTargetDir(dir.scale(0.4), anim, anim.getTick());
                }
            }
            case 4 -> {
                if (anim.isAtTick(0.2)) {
                    handler.setSpinStartRot(entity.getYRot() + 150);
                    handler.resetHitEntityTracker();
                    entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 0.8f);
                }
                if (anim.isAtTick(0.68)) {
                    handler.resetHitEntityTracker();
                    entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 0.8f);
                }
                if (anim.isPastTick(0.2)) {
                    int start = Mth.ceil(0.2 * 20.0D);
                    int end = Mth.ceil(1.15 * 20.0D);
                    float len = (end - start) / anim.getSpeed();
                    float f = (anim.getTick() - start) / anim.getSpeed();
                    float angleInc = -690 / len;
                    float rot = handler.getSpinStartRot();
                    handler.addHitEntityTracker(CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.circleTargets((rot + f * angleInc), (rot + (f + 1) * angleInc), 0.25f))
                            .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                            .executeAttack());
                }
            }
        }
    }

    @Override
    public void onStart(LivingEntity entity, WeaponHandler handler) {
        if (handler.getChainCount() == 4 && entity instanceof ServerPlayer player)
            Platform.INSTANCE.getPlayerData(player).ifPresent(d -> LevelCalc.useRP(player, d, GeneralConfig.longSwordUltimate, true, false, false));
    }

    @Override
    public boolean isInvulnerable(LivingEntity entity, WeaponHandler handler) {
        return handler.getChainCount() == 4;
    }

    @Override
    public AttackChain attackChain(LivingEntity entity, int chain) {
        return new AttackChain(CombatUtils.canPerform(entity, EnumSkills.LONGSWORD, 20) ? 4 : 3, chain == 4 ? 0 : 8);
    }

    @Override
    public boolean disableMovement(AnimatedAction current) {
        return !GeneralConfig.allowMoveOnAttack.get() && super.disableMovement(current);
    }
}
