package io.github.flemmli97.runecraftory.common.effects;

import io.github.flemmli97.runecraftory.common.network.S2CEntityDataSync;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.EffectType;

public class ParalysisEffect extends PermanentEffect {

    public ParalysisEffect() {
        super(EffectType.HARMFUL, 0, S2CEntityDataSync.Type.COLD);
        this.addAttributesModifier(Attributes.MOVEMENT_SPEED, "C1400D29-BF42-4F2e-B8C3-F9975B6B41AC", -0.32, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}
