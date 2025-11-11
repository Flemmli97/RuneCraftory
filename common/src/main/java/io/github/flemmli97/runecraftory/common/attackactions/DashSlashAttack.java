package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.action.ComboContainer;
import io.github.flemmli97.runecraftory.api.registry.action.DataKey;
import io.github.flemmli97.runecraftory.api.registry.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.common.attachment.WeaponHandler;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class DashSlashAttack extends AttackAction {

    private final ComboContainer combo = ComboContainer.Builder.builder()
            .addCombo(handler -> handler.matches(state -> state.isPast("attack_start") && !state.isPast("attack_end")), 0)
            .build();

    @Override
    public AnimationState getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (EntityUtils.attackSpeedModifier(entity));
        return AttackAction.create(PlayerModelAnimations.DASH_SLASH.get(comboIdx), speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler<?> handler, AnimationState state) {
        if (handler.getComboCount() == 2) {
            handler.store(DataKey.MOVE_DIRECTION, null);
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.95, 1, 0.95));
            if (state.isAt("attack")) {
                if (!entity.level().isClientSide) {
                    OrientedBoundingBox obb = new OrientedBoundingBox(new AABB(-entity.getBbWidth(), 0, 0, entity.getBbWidth(), 1, entity.getBbWidth() + 1)
                            .inflate(0.3), entity.getYRot(), 0, entity.position());
                    CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.obbTargets(obb))
                            .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack, RuneCraftorySpells.DASH_SLASH))
                            .doOnSuccess(e -> CombatUtils.knockBackEntity(entity, e, 1))
                            .executeAttack();
                }
                entity.playSound(SoundEvents.PLAYER_ATTACK_STRONG, 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
            }
        } else {
            handler.store(DataKey.FIXED_LOOK, true);
            if (state.isAt("move_start")) {
                Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                handler.store(DataKey.MOVE_DIRECTION, dir.scale(0.5).add(0, 0.3, 0));
            }
            if (state.isPast("attack_start")) {
                if (state.isAt("attack_start")) {
                    Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
                    handler.store(DataKey.MOVE_DIRECTION, dir.scale(0.5));
                }
                handler.applyMoveDirection();
                if (state.isAt("sound"))
                    entity.playSound(RuneCraftorySounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
                if (!entity.level().isClientSide && !state.isPast("attack_end")) {
                    double range = CombatUtils.getRange(entity, -1);
                    handler.addHitEntityTracker(CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.aabbTargets(entity.getBoundingBox().inflate(range * 0.5, 0, 0)
                                    .expandTowards(0, 0, range)))
                            .withBonusAttributes(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack, RuneCraftorySpells.DASH_SLASH))
                            .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                            .executeAttack());
                }
            }
            if (state.isAt("attack_end")) {
                handler.store(DataKey.MOVE_DIRECTION, null);
            }
            handler.applyMoveDirection();
        }
    }

    @Override
    public void onEnd(LivingEntity entity, WeaponHandler<?> handler) {
        if (handler.getComboCount() != 1)
            return;
        Vec3 mot = entity.getDeltaMovement();
        double lenHor = mot.x * mot.x + mot.z * mot.z;
        entity.setDeltaMovement(mot.multiply(lenHor > 0.5 ? 0.5 : 1, 1, lenHor > 0.5 ? 0.5 : 1));
    }

    @Override
    public boolean isInvulnerable(LivingEntity entity, WeaponHandler<?> handler) {
        return handler.getComboCount() == 1;
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
