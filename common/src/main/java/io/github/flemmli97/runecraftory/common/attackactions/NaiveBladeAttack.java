package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.ComboContainer;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class NaiveBladeAttack extends AttackAction {

    private final ComboContainer combo = ComboContainer.Builder.builder()
            .addCombo(handler -> handler.getAnimation().isPast("prepared"))
            .build();

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        if (comboIdx == 1)
            return PlayerModelAnimations.NAIVE_BLADE_SUCCESS.create(speed);
        return PlayerModelAnimations.NAIVE_BLADE.create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (handler.getComboCount() == 2) {
            if (anim.isAt("jump")) {
                entity.setDeltaMovement(new Vec3(0, 0.37, 0));
            }
            if (anim.isAt("attack_1")) {
                entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                if (!entity.level.isClientSide) {
                    CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.circleTargets(entity.getYRot() - 150, entity.getYRot() + 150, 0))
                            .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))
                            .doOnSuccess(target -> CombatUtils.knockBackEntity(entity, target, 1.3f))
                            .executeAttack();
                    if (entity instanceof ServerPlayer player)
                        player.sweepAttack();
                }
            }
            if (anim.isAt("attack_2")) {
                entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                if (!entity.level.isClientSide) {
                    CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.obbTargets(entity.getYRot(), 0, 3, 0, false))
                            .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))
                            .executeAttack();
                }
            }

        } else if (anim.isAt("prepared")) {
            entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.CHAIN_PLACE, entity.getSoundSource(), 1.5f, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        }
    }


    @Override
    public void onStart(LivingEntity entity, WeaponHandler handler) {
        super.onStart(entity, handler);
        if (handler.getComboCount() == 2) {
            entity.playSound(ModSounds.SPELL_NAIVE_BLADE.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        }
    }

    @Override
    public boolean isInvulnerable(LivingEntity entity, WeaponHandler handler) {
        return handler.getComboCount() == 2;
    }

    public static boolean canCounter(WeaponHandler handler) {
        AnimatedAction anim = handler.getAnimation();
        return handler.getCurrentAction() instanceof NaiveBladeAttack
                && anim != null && handler.getComboCount() == 1 && anim.isPast("prepared") && !anim.done(0);
    }

    @Override
    public ComboContainer combos() {
        return this.combo;
    }
}
