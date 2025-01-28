package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.ComboContainer;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;
import java.util.function.Predicate;

public class RushAttack extends AttackAction {

    private final ComboContainer combo;

    public RushAttack() {
        Predicate<WeaponHandler> MAIN = handler -> (!handler.getCurrentAnim().isPastTick(0.84) && handler.getCurrentAnim().isPastTick(0.6)) || (handler.getCurrentAnim().isPastTick(1.16) && !handler.getCurrentAnim().isPastTick(1.48));
        Function<Integer, ComboContainer.ComboGetter> IDX = idx -> handler -> (!handler.getCurrentAnim().isPastTick(0.84)) ? idx : 6;
        this.combo = ComboContainer.Builder.builder()
                .addCombo(new ComboContainer.ComboHandler.Builder(MAIN).advanceTo(IDX))
                .addCombo(new ComboContainer.ComboHandler.Builder(MAIN).advanceTo(IDX))
                .addCombo(new ComboContainer.ComboHandler.Builder(MAIN).advanceTo(IDX))
                .addCombo(new ComboContainer.ComboHandler.Builder(MAIN).advanceTo(IDX))
                .addCombo(new ComboContainer.ComboHandler.Builder(MAIN).advanceTo(IDX))
                .addCombo(handler -> handler.getCurrentAnim().isPastTick(1.16) && !handler.getCurrentAnim().isPastTick(1.48), 0)
                .build();
    }

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        if (comboIdx < 6)
            comboIdx = 0;
        else
            comboIdx = 1;
        return PlayerModelAnimations.RUSH_ATTACK.get(comboIdx).create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (handler.getComboCount() == 7) {
            if (anim.isAtTick(0.28)) {
                Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                handler.setMoveTargetDir(dir.scale(3).add(0, -1.5, 0), anim, 0.4);
                entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
            }
            entity.fallDistance = 0;
            if (!entity.level.isClientSide && anim.isPastTick(0.2) && !anim.isPastTick(0.52)) {
                handler.addHitEntityTracker(CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.obbTargets(entity.getYRot(), 0, CombatUtils.getWidth(entity, 1.5f), -1f, false))
                        .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))
                        .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                        .doOnSuccess(e -> CombatUtils.knockBackEntity(entity, e, 0.8f))
                        .executeAttack());
            }
        } else {
            if (anim.isAtTick(0.32) || anim.isAtTick(0.48)) {
                Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                handler.setMoveTargetDir(dir.scale(0.2), anim, anim.getTick());
                entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
            }
            if (anim.isAtTick(0.92)) {
                Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                handler.setMoveTargetDir(dir.scale(0.5).add(0, 1.5, 0), anim, 1.4);
                entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
            }
            entity.fallDistance = 0;
            if (!entity.level.isClientSide) {
                if (anim.canAttack() || anim.isAtTick(0.52) || anim.isAtTick(1.08)) {
                    CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.obbTargets(entity.getYRot(), 0, CombatUtils.getWidth(entity, 0.5f), 0.5f, false))
                            .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))
                            .executeAttack();

                }
            }
        }
    }

    @Override
    public void onSetup(LivingEntity entity, WeaponHandler handler) {
        if (handler.getCurrentAnim() != null && handler.getComboCount() < 7 && handler.getCurrentAnim().isPastTick(1.12))
            handler.setComboCount(6);
    }

    @Override
    public ComboContainer combos() {
        return this.combo;
    }
}
