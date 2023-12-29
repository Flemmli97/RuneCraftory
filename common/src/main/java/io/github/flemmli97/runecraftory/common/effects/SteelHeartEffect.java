package io.github.flemmli97.runecraftory.common.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class SteelHeartEffect extends MobEffect {

    private static final String MODIFIER = "22e57e0c-3ed5-4aad-9478-52c9c0706c37";

    public SteelHeartEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x727a87);
        this.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, MODIFIER, 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}