package com.flemmli97.runecraftory.common.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class SleepEffect extends Effect {

    public SleepEffect() {
        super(EffectType.HARMFUL, 0);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }
    @Override
    public void performEffect(LivingEntity ent, int amplifier) {
        //if(!(ent instanceof EntityPlayer) || !((EntityPlayer)ent).capabilities.disableDamage)
        //    ent.motionY -= 0.08;
    }
    @Override
    public void applyAttributesModifiersToEntity(LivingEntity entity, AttributeModifierManager manager, int amplifier) {
        if (entity instanceof MobEntity) {
            //((MobEntity)entity).goalSelector.addGoal(0, (EntityAIBase)new EntityAIDisable((EntityLiving)entity));
            entity.setSilent(true);
        }
        super.applyAttributesModifiersToEntity(entity, manager, amplifier);
    }

    @Override
    public void removeAttributesModifiersFromEntity(LivingEntity entity, AttributeModifierManager manager, int amplifier) {
        entity.setSilent(false);
        super.removeAttributesModifiersFromEntity(entity, manager, amplifier);
    }
}
