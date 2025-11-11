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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;
import java.util.function.Predicate;

public class RushAttack extends AttackAction {

    private final ComboContainer combo;

    public RushAttack() {
        Predicate<WeaponHandler<?>> MAIN = handler -> handler.matches(state ->
                (state.isPast("chain_1_start") && !state.isPast("chain_1_end"))
                        || (state.isPast("chain_2_start") && !state.isPast("chain_2_end")));
        Function<Integer, ComboContainer.ComboGetter> IDX = idx -> handler -> !handler.matches(state -> state.isPast("chain_1_end")) ? idx : 6;
        this.combo = ComboContainer.Builder.builder()
                .addCombo(new ComboContainer.ComboHandler.Builder(MAIN).advanceTo(IDX))
                .addCombo(new ComboContainer.ComboHandler.Builder(MAIN).advanceTo(IDX))
                .addCombo(new ComboContainer.ComboHandler.Builder(MAIN).advanceTo(IDX))
                .addCombo(new ComboContainer.ComboHandler.Builder(MAIN).advanceTo(IDX))
                .addCombo(new ComboContainer.ComboHandler.Builder(MAIN).advanceTo(IDX))
                .addCombo(handler -> handler.matches(state -> state.isPast("chain_2_start") && !state.isPast("chain_2_end")), 0)
                .build();
    }

    @Override
    public AnimationState getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (EntityUtils.attackSpeedModifier(entity));
        if (comboIdx < 6)
            comboIdx = 0;
        else
            comboIdx = 1;
        return AttackAction.create(PlayerModelAnimations.RUSH_ATTACK.get(comboIdx), speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler<?> handler, AnimationState state) {
        if (handler.getComboCount() == 7) {
            if (state.isAt("leap")) {
                Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                entity.setDeltaMovement(dir.scale(1.6).add(0, -0.6, 0));
            }
            if (state.isAt("attack_start")) {
                entity.playSound(RuneCraftorySounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
            }
            entity.fallDistance = 0;
            if (!entity.level().isClientSide && state.isPast("attack_start") && !state.isPast("attack_end")) {
                handler.addHitEntityTracker(CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.obbTargets(entity.getYRot(), 0, CombatUtils.getWidth(entity, 1.5f), -1f, false))
                        .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack, RuneCraftorySpells.RUSH_ATTACK))
                        .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                        .doOnSuccess(e -> CombatUtils.knockBackEntity(entity, e, 0.8f))
                        .executeAttack());
            }
        } else {
            if (state.isAt("step")) {
                Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                entity.setDeltaMovement(dir.scale(0.2));
            }
            if (state.isAt("jump")) {
                Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                entity.setDeltaMovement(dir.scale(0.5).add(0, 0.5, 0));
                entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 12, 2, true, false, false));
            }
            entity.fallDistance = 0;
            if (state.isAt("attack")) {
                if (!entity.level().isClientSide) {
                    CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.obbTargets(entity.getYRot(), 0, CombatUtils.getWidth(entity, 0.5f), 0.5f, false))
                            .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack, RuneCraftorySpells.RUSH_ATTACK))
                            .executeAttack();
                }
                entity.playSound(RuneCraftorySounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
            }
        }
    }

    @Override
    public void onSetup(LivingEntity entity, WeaponHandler<?> handler) {
        if (handler.getComboCount() < 7 && handler.matches(state -> state.isPast("chain_2_start")))
            handler.setComboCount(6);
    }

    @Override
    public ComboContainer combos() {
        return this.combo;
    }
}
