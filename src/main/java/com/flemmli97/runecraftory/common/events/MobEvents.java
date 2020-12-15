package com.flemmli97.runecraftory.common.events;

import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.common.MobModule;
import com.flemmli97.runecraftory.common.capability.PlayerCapProvider;
import com.flemmli97.runecraftory.common.config.MobConfig;
import com.flemmli97.runecraftory.common.entities.GateEntity;
import com.flemmli97.runecraftory.common.registry.ModEntities;
import com.flemmli97.runecraftory.common.world.GateSpawning;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MobEvents {

    @SubscribeEvent
    public void registerSpawn(BiomeLoadingEvent event) {
        MobModule.spawnConfig.getEntityFromBiome(event.getName()).forEach(entity -> GateSpawning.addSpawn(event.getName(), entity));
        for (BiomeDictionary.Type type : BiomeDictionary.getTypes(RegistryKey.of(Registry.BIOME_KEY, event.getName())))
            MobModule.spawnConfig.getEntityFromBiomeType(type).forEach(entity -> GateSpawning.addSpawn(event.getName(), entity));
        event.getSpawns().spawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntities.gate.get(), 100, 1, 1));
    }

    @SubscribeEvent
    public void disableNatural(LivingSpawnEvent.CheckSpawn event) {
        if (MobConfig.disableNaturalSpawn) {
            if ((event.getSpawnReason() == SpawnReason.CHUNK_GENERATION || event.getSpawnReason() == SpawnReason.NATURAL) && !(event.getEntity() instanceof GateEntity))
                event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public void livingDamageEvent(LivingDamageEvent event) {
        if (event.getSource().getTrueSource() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
            if (!(player.getHeldItemMainhand().getItem() instanceof IItemUsable)) {
                event.setAmount(player.getCapability(PlayerCapProvider.PlayerCap).map(cap -> Math.max(0, cap.getAttributeValue(player, Attributes.GENERIC_ATTACK_DAMAGE)-4)).orElse(0));
            }
        }
    }
}
