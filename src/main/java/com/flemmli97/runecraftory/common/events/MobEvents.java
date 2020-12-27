package com.flemmli97.runecraftory.common.events;

import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.flemmli97.runecraftory.common.config.GeneralConfig;
import com.flemmli97.runecraftory.common.config.MobConfig;
import com.flemmli97.runecraftory.common.entities.GateEntity;
import com.flemmli97.runecraftory.common.utils.CombatUtils;
import com.flemmli97.runecraftory.common.utils.CustomDamage;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MobEvents {

    @SubscribeEvent
    public void disableNatural(LivingSpawnEvent.CheckSpawn event) {
        if (MobConfig.disableNaturalSpawn) {
            if ((event.getSpawnReason() == SpawnReason.CHUNK_GENERATION || event.getSpawnReason() == SpawnReason.NATURAL) && !(event.getEntity() instanceof GateEntity))
                event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public void damageCalculation(LivingHurtEvent event) {
        float damage = CombatUtils.reduceDamageFromStats(event.getEntityLiving(), event.getSource(), event.getAmount());
        if (damage < 0) {
            if (event.getEntityLiving() instanceof PlayerEntity)
                event.getEntityLiving().getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> cap.regenHealth((PlayerEntity) event.getEntityLiving(), -damage));
            else
                event.getEntityLiving().heal(-damage);
        }
        event.setAmount(damage);
        if (event.getSource() instanceof CustomDamage)
            event.getEntityLiving().hurtResistantTime = ((CustomDamage) event.getSource()).hurtProtection();
    }

    @SubscribeEvent
    public void livingDamageEvent(LivingDamageEvent event) {
        if (event.getSource().getTrueSource() instanceof PlayerEntity && !(event.getSource() instanceof CustomDamage)) {
            PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
            if (!event.getSource().isMagicDamage())
                event.setAmount(player.getCapability(CapabilityInsts.PlayerCap).map(cap ->
                        Math.max(0, cap.getAttributeValue(player, Attributes.GENERIC_ATTACK_DAMAGE) - GeneralConfig.startingStr)).orElse(0));
        }
    }
}
