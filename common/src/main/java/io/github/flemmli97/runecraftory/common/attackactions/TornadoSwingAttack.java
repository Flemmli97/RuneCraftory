package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.action.ComboContainer;
import io.github.flemmli97.runecraftory.api.registry.action.DataKey;
import io.github.flemmli97.runecraftory.api.registry.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.common.attachment.AttackActionHandler;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinition;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class TornadoSwingAttack extends AttackAction {

    private final ComboContainer combo;

    public TornadoSwingAttack() {
        Predicate<AttackActionHandler> MAIN = handler -> handler.getAnimation().isAt("attack_end_1");
        this.combo = ComboContainer.Builder.builder()
                .addCombo(MAIN)
                .addCombo(MAIN)
                .addCombo(MAIN)
                .build();
    }

    @Override
    public AnimationState getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        if (comboIdx > 0) {
            AnimationDefinition definition = PlayerModelAnimations.ANIMS.get(PlayerModelAnimations.TORNADO_SWING);
            double offset = definition.marker("chain_offset", 0) * 20;
            return AnimationState.create(definition, 0, AnimationHandler.FALLBACK_TRANSIT_TIME, offset, speed);
        }
        return AttackAction.create(PlayerModelAnimations.TORNADO_SWING, speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, AttackActionHandler handler, AnimationState anim) {
        if (anim.isAt("attack_start_1")) {
            entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH_HEAVY.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 0.8f);
        }
        if (anim.isAt("attack_start_2")) {
            handler.resetHitEntityTracker();
            entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH_HEAVY.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 0.8f);
        }
        CombatUtils.EntityAttack attack = spinAttack(entity, anim, anim.getMarker("attack_start_1", 0), anim.getMarker("attack_end_1", 0),
                handler.get(DataKey.SPIN_ROTATION) + 110, handler.get(DataKey.SPIN_ROTATION) - 285, 0.5f);
        if (attack != null) {
            handler.addHitEntityTracker(attack
                    .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                    .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))
                    .executeAttack());
        }
        attack = spinAttack(entity, anim, anim.getMarker("attack_start_2", 0), anim.getMarker("attack_end_2", 0),
                handler.get(DataKey.SPIN_ROTATION) + 75, handler.get(DataKey.SPIN_ROTATION) - 35, 0.5f);
        if (attack != null) {
            handler.addHitEntityTracker(attack
                    .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                    .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))
                    .executeAttack());
        }
    }

    @Override
    public void onStart(LivingEntity entity, AttackActionHandler handler) {
        super.onStart(entity, handler);
        handler.store(DataKey.SPIN_ROTATION, entity.getYRot());
        if (handler.getComboCount() != 1) {
            entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH_HEAVY.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 0.8f);
        }
    }

    @Override
    public float movementReduction(AnimationState current) {
        return 0.6f;
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
