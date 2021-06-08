package com.flemmli97.runecraftory.common.events;

import com.flemmli97.runecraftory.api.enums.EnumSkills;
import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.flemmli97.runecraftory.common.config.MobConfig;
import com.flemmli97.runecraftory.common.entities.GateEntity;
import com.flemmli97.runecraftory.common.entities.monster.ai.DisableGoal;
import com.flemmli97.runecraftory.common.utils.CombatUtils;
import com.flemmli97.runecraftory.common.utils.CustomDamage;
import com.flemmli97.runecraftory.common.utils.LevelCalc;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
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
        if (event.getEntityLiving() instanceof PlayerEntity) {
            event.getEntityLiving().getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> {
                if (damage < 0)
                    cap.regenHealth((PlayerEntity) event.getEntityLiving(), -damage);
                else if (damage >= 1)
                    LevelCalc.levelSkill((ServerPlayerEntity) event.getEntityLiving(), cap, EnumSkills.DEFENCE, (float) (0.5 + Math.log(damage)));
            });
        } else if (damage < 0)
            event.getEntityLiving().heal(-damage);
        event.setAmount(damage);
        if (event.getSource() instanceof CustomDamage)
            event.getEntityLiving().hurtResistantTime = ((CustomDamage) event.getSource()).hurtProtection() + 10;
    }

    @SubscribeEvent
    public void onSpawn(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof MobEntity) {
            MobEntity mob = (MobEntity) event.getEntity();
            mob.goalSelector.addGoal(-1, new DisableGoal(mob));
        }
    }
}
