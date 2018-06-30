package com.flemmli97.runecraftory.common.potion;

import net.minecraft.entity.SharedMonsterAttributes;

public class PotionParalysis extends PotionPermanent
{
    public PotionParalysis() {
        super("paralysis", 0);
        this.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "C1400D29-BF42-4F2e-B8C3-F9975B6B41AC", -0.32, 2);
    }
}
