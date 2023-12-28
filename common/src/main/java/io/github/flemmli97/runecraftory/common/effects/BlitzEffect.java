package io.github.flemmli97.runecraftory.common.effects;

import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class BlitzEffect extends MobEffect {

    private static final String MODIFIER = "fc623c63-000f-4e36-a123-a97f16880859";

    public BlitzEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x1348ba);
        this.addAttributeModifier(ModAttributes.ATTACK_SPEED.get(), MODIFIER, 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}
