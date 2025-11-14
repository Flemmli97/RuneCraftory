package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.action.ComboContainer;
import io.github.flemmli97.runecraftory.api.registry.action.DataKey;
import io.github.flemmli97.runecraftory.api.registry.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.common.attachment.WeaponHandler;
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
            .addCombo(handler -> handler.matches(state -> state.isPast("chain_start") && !state.isPast("spin_end")))
            .build();

    @Override
    public AnimationState getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (EntityUtils.attackSpeedModifier(entity));
        return AttackAction.create(PlayerModelAnimations.WIND_SLASH.get(comboIdx), speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler<?> handler, AnimationState state) {
        handler.store(DataKey.FIXED_LOOK, true);
        if (state.isAt("spin_start")) {
            handler.store(DataKey.SPIN_ROTATION, entity.getYRot());
            handler.resetHitEntityTracker();
            playSound(entity, SoundEvents.ENDER_DRAGON_FLAP, 0.7f, 0.5f);
            Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
            handler.store(DataKey.MOVE_DIRECTION, dir.scale(0.5));
        }
        if (state.isAt("reset")) {
            handler.resetHitEntityTracker();
            playSound(entity, SoundEvents.ENDER_DRAGON_FLAP, 1, 0.7f);
        }
        if (state.isAt("leap")) {
            Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
            handler.store(DataKey.MOVE_DIRECTION, dir.scale(0.5).add(0, 0.3, 0));
        }
        if (state.isAt("spin_end")) {
            handler.store(DataKey.MOVE_DIRECTION, null);
        }
        if (state.isPast("spin_start") && !state.isPast("spin_end")) {
            entity.resetFallDistance();
            if (!entity.level().isClientSide) {
                handler.addHitEntityTracker(CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.aabbTargets(entity.getBoundingBox().inflate(0.75)))
                        .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack, RuneCraftorySpells.WIND_SLASH))
                        .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                        .executeAttack());
            }
        }
    }

    @Override
    public void onStart(LivingEntity entity, WeaponHandler<?> handler) {
        super.onStart(entity, handler);
        if (handler.getComboCount() == 2) {
            handler.store(DataKey.SPIN_ROTATION, entity.getYRot());
            handler.resetHitEntityTracker();
            playSound(entity, SoundEvents.ENDER_DRAGON_FLAP, 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 0.7f);
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
