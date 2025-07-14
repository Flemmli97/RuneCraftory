package io.github.flemmli97.runecraftory.common.effects;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class BlitzEffect extends MobEffect {

    private static final ResourceLocation MODIFIER = RuneCraftory.modRes("blitz_effect");

    public BlitzEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x1348ba);
        this.addAttributeModifier(RuneCraftoryAttributes.ATTACK_SPEED.asHolder(), MODIFIER, 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
}
