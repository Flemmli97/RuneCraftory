package io.github.flemmli97.runecraftory.common.effects;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.network.S2CEntityDataSync;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ParalysisEffect extends UncurableEffect {

    private static final ResourceLocation MODIFIER = RuneCraftory.modRes("paralysis_effect");

    public ParalysisEffect() {
        super(MobEffectCategory.HARMFUL, 0, S2CEntityDataSync.DataType.PARALYSIS);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, MODIFIER, -0.32, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
}
