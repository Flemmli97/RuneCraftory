package io.github.flemmli97.runecraftory.common.effects;

import io.github.flemmli97.runecraftory.common.network.S2CEntityDataSync;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ParalysisEffect extends PermanentEffect {

    public ParalysisEffect() {
        super(MobEffectCategory.HARMFUL, 0, S2CEntityDataSync.Type.PARALYSIS);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "C1400D29-BF42-4F2e-B8C3-F9975B6B41AC", -0.32, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}
