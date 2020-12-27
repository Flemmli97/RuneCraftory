package com.flemmli97.runecraftory.common.events;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.flemmli97.runecraftory.common.config.GeneralConfig;
import com.flemmli97.runecraftory.common.config.GenerationConfig;
import com.flemmli97.runecraftory.common.config.MobConfig;
import com.flemmli97.runecraftory.common.config.values.GenConfig;
import com.flemmli97.runecraftory.common.entities.GateEntity;
import com.flemmli97.runecraftory.common.registry.ModEntities;
import com.flemmli97.runecraftory.common.registry.ModFeatures;
import com.flemmli97.runecraftory.common.utils.CombatUtils;
import com.flemmli97.runecraftory.common.utils.CustomDamage;
import com.flemmli97.runecraftory.common.world.GateSpawning;
import com.flemmli97.runecraftory.common.world.features.ChancedBlockCluster;
import com.flemmli97.runecraftory.common.world.features.WeightedClusterProvider;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Features;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;

public class MobEvents {

    @SubscribeEvent
    public void registerSpawn(BiomeLoadingEvent event) {
        RuneCraftory.spawnConfig.getEntityFromBiome(event.getName()).forEach(entity -> GateSpawning.addSpawn(event.getName(), entity));
        Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(RegistryKey.of(Registry.BIOME_KEY, event.getName()));
        for (BiomeDictionary.Type type : types)
            RuneCraftory.spawnConfig.getEntityFromBiomeType(type).forEach(entity -> GateSpawning.addSpawn(event.getName(), entity));
        event.getSpawns().spawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntities.gate.get(), 100, 1, 1));

        List<GenConfig> mineralConf = GenerationConfig.mineralGenFrom(types);
        if(!mineralConf.isEmpty()) {
            WeightedClusterProvider cluster = new WeightedClusterProvider();
            for (GenConfig conf : mineralConf)
                cluster.add(new ChancedBlockCluster(new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(ForgeRegistries.BLOCKS.getValue(conf.getBlock()).getDefaultState()), SimpleBlockPlacer.INSTANCE).tries(conf.maxAmount()).spreadX(conf.xSpread()).spreadY(conf.ySpread()).spreadZ(conf.zSpread()).build(), conf.chance()), conf.weight());
            event.getGeneration().feature(GenerationStage.Decoration.SURFACE_STRUCTURES, ModFeatures.MINERALFEATURE.get().configure(cluster).decorate(Features.Placements.SQUARE_TOP_SOLID_HEIGHTMAP));
        }
    }

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
        if(damage < 0) {
            if(event.getEntityLiving() instanceof PlayerEntity)
                event.getEntityLiving().getCapability(CapabilityInsts.PlayerCap).ifPresent(cap->cap.regenHealth((PlayerEntity) event.getEntityLiving(), -damage));
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
            if(!event.getSource().isMagicDamage())
                event.setAmount(player.getCapability(CapabilityInsts.PlayerCap).map(cap ->
                    Math.max(0, cap.getAttributeValue(player, Attributes.GENERIC_ATTACK_DAMAGE) - GeneralConfig.startingStr)).orElse(0));
        }
    }
}
