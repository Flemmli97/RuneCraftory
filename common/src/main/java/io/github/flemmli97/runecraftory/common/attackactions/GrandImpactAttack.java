package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.ComboContainer;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemAxeBase;
import io.github.flemmli97.runecraftory.common.network.S2CScreenShake;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
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
    public AnimatedAction getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.GRAND_IMPACT.create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (!entity.level.isClientSide && (anim.isAt("attack_1") || anim.isAt("attack_2"))) {
            float reach = (float) entity.getAttributeValue(ModAttributes.ATTACK_RANGE.get());
            S2CScreenShake.sendAround(entity, 16, 6, 3);
            entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.PLAYER_ATTACK_STRONG, entity.getSoundSource(), 1.0f, 1.0f);
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
