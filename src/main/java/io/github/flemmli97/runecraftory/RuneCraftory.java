package io.github.flemmli97.runecraftory;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.client.ClientEvents;
import io.github.flemmli97.runecraftory.common.capability.CapabilityInsts;
import io.github.flemmli97.runecraftory.common.capability.EntityCapImpl;
import io.github.flemmli97.runecraftory.common.capability.IEntityCap;
import io.github.flemmli97.runecraftory.common.capability.IPlayerCap;
import io.github.flemmli97.runecraftory.common.capability.IStaffCap;
import io.github.flemmli97.runecraftory.common.capability.PlayerCapImpl;
import io.github.flemmli97.runecraftory.common.capability.StaffCapImpl;
import io.github.flemmli97.runecraftory.common.config.ClientConfig;
import io.github.flemmli97.runecraftory.common.config.ClientConfigSpec;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.config.GeneralConfigSpec;
import io.github.flemmli97.runecraftory.common.config.GenerationConfig;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.config.SpawnConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.GateEntity;
import io.github.flemmli97.runecraftory.common.events.MobEvents;
import io.github.flemmli97.runecraftory.common.events.PlayerEvents;
import io.github.flemmli97.runecraftory.common.events.WorldEvents;
import io.github.flemmli97.runecraftory.common.network.PacketHandler;
import io.github.flemmli97.runecraftory.common.registry.ModActivities;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.registry.ModContainer;
import io.github.flemmli97.runecraftory.common.registry.ModCrafting;
import io.github.flemmli97.runecraftory.common.registry.ModEffects;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModFeatures;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModLootModifier;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.registry.ModStructures;
import io.github.flemmli97.runecraftory.common.world.GateSpawning;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Path;

@Mod(value = RuneCraftory.MODID)
public class RuneCraftory {

    public static final String MODID = "runecraftory";
    public static final Logger logger = LogManager.getLogger(RuneCraftory.MODID);

    public static Path defaultConfPath;

    public static SpawnConfig spawnConfig;

    public static IForgeRegistry<Spell> spellRegistry;

    public RuneCraftory() {
        Path confDir = FMLPaths.CONFIGDIR.get().resolve(MODID);

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::common);
        modBus.addListener(this::newReg);
        modBus.addListener(this::conf);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientEvents::register);

        registries(modBus);

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(DataPackHandler::registerDataPackHandler);
        forgeBus.register(new MobEvents());
        forgeBus.register(new PlayerEvents());
        forgeBus.register(new WorldEvents());

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, GeneralConfigSpec.generalSpec, RuneCraftory.MODID + "/general.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfigSpec.clientSpec, RuneCraftory.MODID + "/client.toml");

        spawnConfig = new SpawnConfig(confDir);
        MobConfig.MobConfigSpec.config.loadConfig();
        GenerationConfig.GenerationConfigSpec.config.loadConfig();
    }

    public static void registries(IEventBus modBus) {
        ModBlocks.BLOCKS.register(modBus);
        ModBlocks.TILES.register(modBus);
        ModContainer.CONTAINERS.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModEntities.ENTITIES.register(modBus);
        ModAttributes.ATTRIBUTES.register(modBus);
        ModEffects.EFFECTS.register(modBus);
        ModCrafting.RECIPESERIALIZER.register(modBus);
        modBus.addGenericListener(IRecipeSerializer.class, ModCrafting::register);
        ModLootModifier.SERIALZER.register(modBus);
        modBus.addGenericListener(GlobalLootModifierSerializer.class, ModLootModifier::register);
        ModFeatures.FEATURES.register(modBus);
        ModSpells.SPELLS.register(modBus);
        ModStructures.STRUCTURES.register(modBus);
        ModParticles.PARTICLES.register(modBus);
        ModActivities.ACTIVITIES.register(modBus);
    }

    public void newReg(RegistryEvent.NewRegistry event) {
        spellRegistry = new RegistryBuilder<Spell>().setName(new ResourceLocation(RuneCraftory.MODID, "spell_registry"))
                .setType(Spell.class).setDefaultKey(new ResourceLocation(RuneCraftory.MODID, "empty_spell")).create();
    }

    public void common(FMLCommonSetupEvent event) {
        PacketHandler.register();
        event.enqueueWork(() -> {
            ModEntities.registerAttributes();
            EntitySpawnPlacementRegistry.register(ModEntities.gate.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GateEntity::canSpawnAt);
            ModFeatures.registerConfiguredFeatures();
        });
        CapabilityManager.INSTANCE.register(IPlayerCap.class, new CapabilityInsts.PlayerCapNetwork(), PlayerCapImpl::new);
        CapabilityManager.INSTANCE.register(IStaffCap.class, new CapabilityInsts.StaffCapNetwork(), StaffCapImpl::new);
        CapabilityManager.INSTANCE.register(IEntityCap.class, new CapabilityInsts.EntityCapNetwork(), EntityCapImpl::new);
        this.tweakVanillaAttribute(Attributes.MAX_HEALTH, 250000);
        this.tweakVanillaAttribute(Attributes.ATTACK_DAMAGE, 250000);
        GateSpawning.setupStructureSpawns();
    }

    public void conf(ModConfig.ModConfigEvent event) {
        if (event.getConfig().getSpec() == GeneralConfigSpec.generalSpec)
            GeneralConfig.load();
        if (event.getConfig().getSpec() == ClientConfigSpec.clientSpec)
            ClientConfig.load();
    }

    private void tweakVanillaAttribute(Attribute attribute, double value) {
        if (attribute instanceof RangedAttribute) {
            try {
                //maximumValue
                Field f = ObfuscationReflectionHelper.findField(RangedAttribute.class, "field_111118_b");
                f.setAccessible(true);
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
                f.set(attribute, value);
                modifiersField.setInt(f, f.getModifiers() & Modifier.FINAL);
                f.setAccessible(false);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1) {
                logger.error("Error modifying attribute {}", attribute.getRegistryName());
                e1.printStackTrace();
            }
        }
    }
}
