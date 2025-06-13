package io.github.flemmli97.runecraftory.common.effects;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class SteelHeartEffect extends MobEffect {

    private static final ResourceLocation MODIFIER = RuneCraftory.modRes("steel_heart_modifier");

    public SteelHeartEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x727a87);
        this.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, MODIFIER, 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
}