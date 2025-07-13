package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.action.ComboContainer;
import io.github.flemmli97.runecraftory.api.registry.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.common.attachment.AttackActionHandler;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemAxeBase;
import io.github.flemmli97.runecraftory.common.network.S2CScreenShake;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

public class GrandImpactAttack extends AttackAction {

    private final ComboContainer combos = ComboContainer.Builder.builder()
            .addCombo(handler -> handler.getAnimation().isPast("attack_1") && !handler.getAnimation().isPast("combo_end"))
            .addCombo(handler -> handler.getAnimation().isPast("attack_1") && !handler.getAnimation().isPast("combo_end"))
            .addCombo(handler -> handler.getAnimation().isPast("attack_1") && !handler.getAnimation().isPast("combo_end"))
            .build();

    @Override
    public AnimationState getAnimation(LivingEntity entity, int comboIdx) {
        double speed = ItemNBT.attackSpeedModifier(entity);
        return AttackAction.create(PlayerModelAnimations.GRAND_IMPACT, speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, AttackActionHandler handler, AnimationState anim) {
        if (!entity.level().isClientSide && (anim.isAt("attack_1") || anim.isAt("attack_2"))) {
            float reach = (float) entity.getAttributeValue(ModAttributes.ATTACK_RANGE.asHolder());
            S2CScreenShake.sendAround(entity, 16, 6, 3);
            entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.PLAYER_ATTACK_STRONG, entity.getSoundSource(), 1.0f, 1.0f);
            CombatUtils.applyTempAttribute(entity, Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack));
            ItemAxeBase.performRightClickAction(stack, entity, reach + 1, 0.1f);
            CombatUtils.removeTempAttribute(entity, Attributes.ATTACK_DAMAGE);
        }
    }

    @Override
    public ComboContainer combos() {
        return this.combos;
    }
}
