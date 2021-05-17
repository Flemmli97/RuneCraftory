package com.flemmli97.runecraftory.common.effects;

import com.flemmli97.runecraftory.common.network.PacketHandler;
import com.flemmli97.runecraftory.common.network.S2CEntityDataSync;
import net.minecraft.entity.LivingEntity;
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
        entity.setSilent(true);
        sendSleepPacket(entity, true);
        super.applyAttributesModifiersToEntity(entity, manager, amplifier);
    }

    @Override
    public void removeAttributesModifiersFromEntity(LivingEntity entity, AttributeModifierManager manager, int amplifier) {
        entity.setSilent(false);
        sendSleepPacket(entity, false);
        super.removeAttributesModifiersFromEntity(entity, manager, amplifier);
    }

    private static void sendSleepPacket(LivingEntity entity, boolean flag) {
        PacketHandler.sendToTrackingAndSelf(new S2CEntityDataSync(entity.getEntityId(), S2CEntityDataSync.Type.SLEEP, flag), entity);
    }
}
