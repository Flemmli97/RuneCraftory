package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.ComboContainer;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class TornadoSwingAttack extends AttackAction {

    private final ComboContainer combo;

    public TornadoSwingAttack() {
        Predicate<WeaponHandler> MAIN = handler -> handler.getAnimation().isAt("attack_end_1");
        this.combo = ComboContainer.Builder.builder()
                .addCombo(MAIN)
                .addCombo(MAIN)
                .addCombo(MAIN)
                .build();
    }

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        if (comboIdx > 0) {
            float offset = (float) (PlayerModelAnimations.TORNADO_SWING.getMarker("chain_offset", 0) * 20);
            return PlayerModelAnimations.TORNADO_SWING.create(0, AnimationHandler.FALLBACK_TRANSIT_TIME, offset, speed);
        }
        return PlayerModelAnimations.TORNADO_SWING.create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (anim.isAt("attack_start_1")) {
            entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH_HEAVY.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 0.8f);
        }
        if (anim.isAt("attack_start_2")) {
            handler.resetHitEntityTracker();
            entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH_HEAVY.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 0.8f);
        }
        CombatUtils.EntityAttack attack = spinAttack(entity, anim, anim.getMarker("attack_start_1", 0), anim.getMarker("attack_end_1", 0),
                handler.getSpinStartRot() + 110, handler.getSpinStartRot() - 285, 0.5f);
        if (attack != null) {
            handler.addHitEntityTracker(attack
                    .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                    .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))
                    .executeAttack());
        }
        attack = spinAttack(entity, anim, anim.getMarker("attack_start_2", 0), anim.getMarker("attack_end_2", 0),
                handler.getSpinStartRot() + 75, handler.getSpinStartRot() - 35, 0.5f);
        if (attack != null) {
            handler.addHitEntityTracker(attack
                    .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                    .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))
                    .executeAttack());
        }
    }

    @Override
    public void onStart(LivingEntity entity, WeaponHandler handler) {
        super.onStart(entity, handler);
        handler.setSpinStartRot(entity.getYRot());
        if (handler.getComboCount() != 1) {
            entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH_HEAVY.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 0.8f);
        }
    }

    @Override
    public float movementReduction(AnimatedAction current) {
        return 0.6f;
    }

    @Override
    public ComboContainer combos() {
        return this.combo;
    }
}
