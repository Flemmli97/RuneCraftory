package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.action.ComboContainer;
import io.github.flemmli97.runecraftory.api.registry.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.common.attachment.WeaponHandler;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class NaiveBladeAttack extends AttackAction {

    private final ComboContainer combo = ComboContainer.Builder.builder()
            .addCombo(handler -> handler.matches(state -> state.isPast("prepared")))
            .build();

    @Override
    public AnimationState getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (EntityUtils.attackSpeedModifier(entity));
        if (comboIdx == 1)
            return AttackAction.create(PlayerModelAnimations.NAIVE_BLADE_SUCCESS, speed);
        return AttackAction.create(PlayerModelAnimations.NAIVE_BLADE, speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler<?> handler, AnimationState state) {
        if (handler.getComboCount() == 2) {
            if (state.isAt("jump")) {
                handler.applyDelta(new Vec3(0, 0.37, 0));
            }
            if (state.isAt("attack_1")) {
                playSound(entity, RuneCraftorySounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                if (!entity.level().isClientSide) {
                    CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.circleTargets(entity.getYRot() - 150, entity.getYRot() + 150, 0))
                            .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack, RuneCraftorySpells.NAIVE_BLADE))
                            .doOnSuccess(target -> CombatUtils.knockBackEntity(entity, target, 1.3f))
                            .executeAttack();
                    if (entity instanceof ServerPlayer player)
                        player.sweepAttack();
                }
            }
            if (state.isAt("attack_2")) {
                playSound(entity, RuneCraftorySounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                if (!entity.level().isClientSide) {
                    CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.obbTargets(entity.getYRot(), 0, 3, 0, false))
                            .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack, RuneCraftorySpells.NAIVE_BLADE))
                            .executeAttack();
                }
            }
        } else if (state.isAt("prepared")) {
            playSound(entity, SoundEvents.CHAIN_PLACE, 1.5f, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        }
    }

    @Override
    public void onStart(LivingEntity entity, WeaponHandler<?> handler) {
        super.onStart(entity, handler);
        if (handler.getComboCount() == 2) {
            playSound(entity, RuneCraftorySounds.SPELL_NAIVE_BLADE.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        }
    }

    @Override
    public boolean isInvulnerable(LivingEntity entity, WeaponHandler<?> handler) {
        return handler.getComboCount() == 2;
    }

    public static boolean canCounter(WeaponHandler<?> handler) {
        return handler.getCurrentAction() instanceof NaiveBladeAttack
                && handler.getComboCount() == 1 && handler.matches(state -> state.isPast("prepared"));
    }

    @Override
    public ComboContainer combos() {
        return this.combo;
    }
}
