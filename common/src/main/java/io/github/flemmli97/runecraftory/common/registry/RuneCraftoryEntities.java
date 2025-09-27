package io.github.flemmli97.runecraftory.common.registry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.EntityProperties;
import io.github.flemmli97.runecraftory.api.datapack.EntityRideActionCosts;
import io.github.flemmli97.runecraftory.api.datapack.GateSpawnData;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.GateEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.AmbrosiaWaveEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.AppleProjectileEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.BigPlateEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.BigRaccoonLeafEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.BoneNeedleEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.BulletEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.ButterflyEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.CardsEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.CustomFishingHookEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.DarkBallEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.DarkBeamEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.DarkBulletEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.DarknessEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.ElementalBallEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.ElementalTrailEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.ExplosionSpellEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.FireballEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.FurnitureEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.GroundShakeParticleSpawner;
import io.github.flemmli97.runecraftory.common.entities.misc.GustRocksEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.HoeTillableItemEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.HomingEnergyOrbEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.LightBallEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.LightBeamEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.MarionettaTrapEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.MissileEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.MobArrowEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.PoisonNeedleEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.PollenEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.PollenPuffEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.PowerWaveEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.RockSpearEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.RuneOrbEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.RuneyEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.SarcophagusTeleporter;
import io.github.flemmli97.runecraftory.common.entities.misc.SlashResidueEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.SleepAuraEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.SmallRaccoonLeafEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.SpiderWebEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.SpikeEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.SporeEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.StarfallEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.StatusBallEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.StoneEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.SwipingWaterLaserEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.ThiccLightningBoltEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.ThrownItemEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.ThunderboltBeamEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.TornadoEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.TreasureChestEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.WaterLaserEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.WindBladeEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.WindGustEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.summoners.AppleRainSummoner;
import io.github.flemmli97.runecraftory.common.entities.misc.summoners.BlazeBarrageSummoner;
import io.github.flemmli97.runecraftory.common.entities.misc.summoners.ButterflySummonerEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.summoners.DarkBulletSummonerEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.summoners.ElementBallBarrageSummoner;
import io.github.flemmli97.runecraftory.common.entities.misc.summoners.ElementalCircleSummoner;
import io.github.flemmli97.runecraftory.common.entities.misc.summoners.FireWallSummoner;
import io.github.flemmli97.runecraftory.common.entities.misc.summoners.RafflesiaBreathSummoner;
import io.github.flemmli97.runecraftory.common.entities.misc.summoners.RafflesiaCircleSummoner;
import io.github.flemmli97.runecraftory.common.entities.misc.summoners.RootSpikeSummoner;
import io.github.flemmli97.runecraftory.common.entities.misc.summoners.SporeCircleSummoner;
import io.github.flemmli97.runecraftory.common.entities.misc.summoners.StarFallSummoner;
import io.github.flemmli97.runecraftory.common.entities.misc.summoners.WindBladeBarrageSummoner;
import io.github.flemmli97.runecraftory.common.entities.monster.Ant;
import io.github.flemmli97.runecraftory.common.entities.monster.Beetle;
import io.github.flemmli97.runecraftory.common.entities.monster.BigMuck;
import io.github.flemmli97.runecraftory.common.entities.monster.Buffamoo;
import io.github.flemmli97.runecraftory.common.entities.monster.Chipsqueek;
import io.github.flemmli97.runecraftory.common.entities.monster.Cluckadoodle;
import io.github.flemmli97.runecraftory.common.entities.monster.Demon;
import io.github.flemmli97.runecraftory.common.entities.monster.Duck;
import io.github.flemmli97.runecraftory.common.entities.monster.Fairy;
import io.github.flemmli97.runecraftory.common.entities.monster.FlowerLily;
import io.github.flemmli97.runecraftory.common.entities.monster.FlowerLion;
import io.github.flemmli97.runecraftory.common.entities.monster.Ghost;
import io.github.flemmli97.runecraftory.common.entities.monster.Goblin;
import io.github.flemmli97.runecraftory.common.entities.monster.GoblinArcher;
import io.github.flemmli97.runecraftory.common.entities.monster.GoblinGangster;
import io.github.flemmli97.runecraftory.common.entities.monster.GoblinPirate;
import io.github.flemmli97.runecraftory.common.entities.monster.Hornet;
import io.github.flemmli97.runecraftory.common.entities.monster.KingWooly;
import io.github.flemmli97.runecraftory.common.entities.monster.LeafBall;
import io.github.flemmli97.runecraftory.common.entities.monster.Mage;
import io.github.flemmli97.runecraftory.common.entities.monster.Mimic;
import io.github.flemmli97.runecraftory.common.entities.monster.MineralSqueek;
import io.github.flemmli97.runecraftory.common.entities.monster.Minotaur;
import io.github.flemmli97.runecraftory.common.entities.monster.Nappie;
import io.github.flemmli97.runecraftory.common.entities.monster.Orc;
import io.github.flemmli97.runecraftory.common.entities.monster.OrcArcher;
import io.github.flemmli97.runecraftory.common.entities.monster.OrcHunter;
import io.github.flemmli97.runecraftory.common.entities.monster.PalmCat;
import io.github.flemmli97.runecraftory.common.entities.monster.Panther;
import io.github.flemmli97.runecraftory.common.entities.monster.PommePomme;
import io.github.flemmli97.runecraftory.common.entities.monster.Scorpion;
import io.github.flemmli97.runecraftory.common.entities.monster.SkyFish;
import io.github.flemmli97.runecraftory.common.entities.monster.Spider;
import io.github.flemmli97.runecraftory.common.entities.monster.Tortas;
import io.github.flemmli97.runecraftory.common.entities.monster.TrickyMuck;
import io.github.flemmli97.runecraftory.common.entities.monster.Troll;
import io.github.flemmli97.runecraftory.common.entities.monster.VeggieGhost;
import io.github.flemmli97.runecraftory.common.entities.monster.Weagle;
import io.github.flemmli97.runecraftory.common.entities.monster.Wolf;
import io.github.flemmli97.runecraftory.common.entities.monster.Wooly;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.Ambrosia;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.Chimera;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.DeadTree;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.Grimoire;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.Handonetta;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.Marionetta;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.Raccoon;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.Sano;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.Sarcophagus;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.Skelefang;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.Thunderbolt;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.Uno;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.rafflesia.Rafflesia;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.rafflesia.RafflesiaFlower;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.rafflesia.RafflesiaHorseTail;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.rafflesia.RafflesiaPart;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.rafflesia.RafflesiaPitcher;
import io.github.flemmli97.runecraftory.common.entities.monster.ensemble.SanoAndUnoDuo;
import io.github.flemmli97.runecraftory.common.entities.monster.wisp.Ignis;
import io.github.flemmli97.runecraftory.common.entities.monster.wisp.Spirit;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.items.creative.EnsembleEggItem;
import io.github.flemmli97.runecraftory.common.items.creative.NPCSpawnEgg;
import io.github.flemmli97.runecraftory.common.items.creative.RuneCraftoryEggItem;
import io.github.flemmli97.runecraftory.common.items.creative.TreasureChestSpawnegg;
import io.github.flemmli97.runecraftory.common.lib.LibAdvancements;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.tenshilib.common.entity.MultiPartEntity;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.TenshiLibCrossPlat;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class RuneCraftoryEntities {

    public static final LoaderRegister<EntityType<?>> ENTITIES = LoaderRegistryAccess.INSTANCE.of(Registries.ENTITY_TYPE, RuneCraftory.MODID);

    private static final List<RegistryEntrySupplier<EntityType<?>, EntityType<?>>> MONSTERS = new ArrayList<>();
    private static final List<RegistryEntrySupplier<EntityType<?>, EntityType<?>>> BOSSES = new ArrayList<>();
    /**
     * Used for registering attributes
     */
    private static final List<RegistryEntrySupplier<EntityType<?>, EntityType<?>>> FLYING_MONSTERS = new ArrayList<>();

    // For datagen stuff
    private static final Map<ResourceLocation, GateSpawnData> DEFAULT_SPAWN_DATA = new HashMap<>();
    private static final Map<ResourceLocation, EntityProperties.Builder> DEFAULT_MOB_PROPERTIES = new HashMap<>();

    private static final float BOSS_TAMING_CHANCE = 0.005f;

    public static final RegistryEntrySupplier<EntityType<?>, EntityType<GateEntity>> GATE = reg(EntityType.Builder.of(GateEntity::new, MobCategory.MONSTER).sized(0.9f, 0.9f).clientTrackingRange(8), RuneCraftory.modRes("gate"));

    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Wooly>> WOOLY = regMonster(EntityType.Builder.of(Wooly::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 13.5 / 16, -6 / 16d)).sized(0.7f, 1.55f).clientTrackingRange(8), RuneCraftory.modRes("wooly"),
            0xffffcc, 0xffffff,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 18).putLevelGains(Attributes.MAX_HEALTH, 530)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 3).putLevelGains(Attributes.ATTACK_DAMAGE, 260)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 0).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 240)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 1).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 230)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 0).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 240)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .xp(5).tamingChance(0.2f).setRideable(),
            new GateSpawnData.Builder(0, 0).addToBiomeTag(100, RunecraftoryTags.Biomes.IS_PLAINS, BiomeTags.IS_BEACH, BiomeTags.IS_FOREST, BiomeTags.IS_HILL,
                    RunecraftoryTags.Biomes.IS_MAGICAL).addToBiomeTag(60, BiomeTags.IS_SAVANNA, BiomeTags.IS_MOUNTAIN, BiomeTags.IS_TAIGA));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<KingWooly>> KING_WOOLY = regMonster(EntityType.Builder.of(KingWooly::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 13.5 / 16, -6 / 16d)).sized(0.7f, 1.55f).spawnDimensionsScale(2.5f).clientTrackingRange(8), RuneCraftory.modRes("king_wooly"),
            0xffffcc, 0xffffff,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 100).putLevelGains(Attributes.MAX_HEALTH, 550)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 20).putLevelGains(Attributes.ATTACK_DAMAGE, 286)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 6).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 255)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 16).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 246)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 6).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 255)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 7)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 2)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 7)
                    .putAttributes(RuneCraftoryAttributes.FIRE_RESISTANCE.asHolder(), -10)
                    .putAttributes(RuneCraftoryAttributes.LOVE_RESISTANCE.asHolder(), -10)
                    .putAttributes(RuneCraftoryAttributes.WATER_RESISTANCE.asHolder(), 10)
                    .xp(25).tamingChance(0.05f).setRideable(),
            new GateSpawnData.Builder(0, 40).addToBiomeTag(8, RunecraftoryTags.Biomes.IS_PLAINS, BiomeTags.IS_BEACH, BiomeTags.IS_FOREST, BiomeTags.IS_HILL,
                    RunecraftoryTags.Biomes.IS_MAGICAL).addToBiomeTag(60, BiomeTags.IS_SAVANNA, BiomeTags.IS_MOUNTAIN, BiomeTags.IS_TAIGA));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Orc>> ORC = regMonster(EntityType.Builder.of(Orc::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 17.5 / 16d, -7 / 16d)).vehicleAttachment(new Vec3(0, 12 / 16d, 0)).sized(0.73f, 2.3f).clientTrackingRange(8), RuneCraftory.modRes("orc"),
            0x663300, 0xffbf80,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 20).putLevelGains(Attributes.MAX_HEALTH, 480)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 9).putLevelGains(Attributes.ATTACK_DAMAGE, 277)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 0).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 216)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 4).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 245)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 0).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 210)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 10)
                    .xp(10).tamingChance(0.15f).setRideable(),
            new GateSpawnData.Builder(0, 0).addToBiomeTag(100, RunecraftoryTags.Biomes.IS_PLAINS, BiomeTags.IS_BEACH, BiomeTags.IS_FOREST, BiomeTags.IS_HILL,
                    RunecraftoryTags.Biomes.IS_MAGICAL).addToBiomeTag(60, BiomeTags.IS_SAVANNA, BiomeTags.IS_MOUNTAIN, BiomeTags.IS_TAIGA));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<OrcArcher>> ORC_ARCHER = regMonster(EntityType.Builder.of(OrcArcher::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 17.5 / 16d, -7 / 16d)).vehicleAttachment(new Vec3(0, 12 / 16d, 0)).sized(0.73f, 2.3f).clientTrackingRange(8), RuneCraftory.modRes("orc_archer"),
            0x663300, 0xffbf80,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 20).putLevelGains(Attributes.MAX_HEALTH, 480)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 9).putLevelGains(Attributes.ATTACK_DAMAGE, 277)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 0).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 216)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 4).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 245)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 0).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 210)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 10)
                    .xp(10).tamingChance(0.15f).setRideable(),
            new GateSpawnData.Builder(0, 0).addToBiomeTag(100, RunecraftoryTags.Biomes.IS_PLAINS, BiomeTags.IS_BEACH, BiomeTags.IS_FOREST, BiomeTags.IS_HILL,
                    RunecraftoryTags.Biomes.IS_MAGICAL).addToBiomeTag(60, BiomeTags.IS_SAVANNA, BiomeTags.IS_MOUNTAIN, BiomeTags.IS_TAIGA));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Orc>> HIGH_ORC = regMonster(EntityType.Builder.of(Orc::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 17.5 / 16d, -7 / 16d)).vehicleAttachment(new Vec3(0, 12 / 16d, 0)).sized(0.73f, 2.3f).clientTrackingRange(8), RuneCraftory.modRes("high_orc"),
            0x9f6c4e, 0x333e78,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 28).putLevelGains(Attributes.MAX_HEALTH, 505)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 17).putLevelGains(Attributes.ATTACK_DAMAGE, 289)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 5).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 231)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 9).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 254)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 5).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 229)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 15)
                    .xp(155).tamingChance(0.05f).setRideable().setMinLevel(10),
            new GateSpawnData.Builder(0, 20).addToBiomeTag(70, RunecraftoryTags.Biomes.IS_PLAINS, BiomeTags.IS_BEACH, BiomeTags.IS_FOREST, BiomeTags.IS_HILL,
                    RunecraftoryTags.Biomes.IS_MAGICAL).addToBiomeTag(44, BiomeTags.IS_SAVANNA, BiomeTags.IS_MOUNTAIN));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<OrcHunter>> ORC_HUNTER = regMonster(EntityType.Builder.of(OrcHunter::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 17.5 / 16d, -7 / 16d)).vehicleAttachment(new Vec3(0, 12 / 16d, 0)).sized(0.73f, 2.3f).clientTrackingRange(8), RuneCraftory.modRes("orc_hunter"),
            0x9f6c4e, 0x333e78,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 28).putLevelGains(Attributes.MAX_HEALTH, 505)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 17).putLevelGains(Attributes.ATTACK_DAMAGE, 289)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 5).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 231)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 9).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 254)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 5).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 229)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 15)
                    .xp(155).tamingChance(0.05f).setRideable().setMinLevel(10),
            new GateSpawnData.Builder(0, 20).addToBiomeTag(70, RunecraftoryTags.Biomes.IS_PLAINS, BiomeTags.IS_BEACH, BiomeTags.IS_FOREST, BiomeTags.IS_HILL,
                    RunecraftoryTags.Biomes.IS_MAGICAL, BiomeTags.IS_MOUNTAIN, BiomeTags.IS_SAVANNA));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Ant>> ANT = regMonster(EntityType.Builder.of(Ant::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 6 / 16d, 0)).sized(1.4f, 0.5f).spawnDimensionsScale(0.7f).clientTrackingRange(8), RuneCraftory.modRes("ant"),
            0x800000, 0x1a0000,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 22).putLevelGains(Attributes.MAX_HEALTH, 503)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 7).putLevelGains(Attributes.ATTACK_DAMAGE, 267)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 1).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 229)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 4).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 218)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 0).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 225)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.WATER_RESISTANCE.asHolder(), -10)
                    .xp(10).tamingChance(0.1f),
            new GateSpawnData.Builder(0, 0).addToBiomeTag(80, RunecraftoryTags.Biomes.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, RunecraftoryTags.Biomes.IS_LUSH, BiomeTags.IS_SAVANNA));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Ant>> KILLER_ANT = regMonster(EntityType.Builder.of(Ant::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 6 / 16d, 0)).sized(1.4f, 0.5f).clientTrackingRange(8), RuneCraftory.modRes("killer_ant"),
            0x0f0e0e, 0x754848,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 28).putLevelGains(Attributes.MAX_HEALTH, 525)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 16).putLevelGains(Attributes.ATTACK_DAMAGE, 282)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 4).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 238)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 11).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 221)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 4).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 227)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.FIRE_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 15)
                    .xp(120).tamingChance(0.05f).setMinLevel(10),
            new GateSpawnData.Builder(0, 20).addToBiomeTag(40, RunecraftoryTags.Biomes.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, RunecraftoryTags.Biomes.IS_LUSH, BiomeTags.IS_SAVANNA));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Beetle>> BEETLE = regMonster(EntityType.Builder.of(Beetle::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 18 / 16d, -10 / 16d)).sized(0.7f, 1.7f).clientTrackingRange(8), RuneCraftory.modRes("beetle"),
            0x9c6a43, 0x244a69,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 23).putLevelGains(Attributes.MAX_HEALTH, 498)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 11).putLevelGains(Attributes.ATTACK_DAMAGE, 273)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 0).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 219)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 5).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 209)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 0).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 214)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.FIRE_RESISTANCE.asHolder(), -10)
                    .putAttributes(RuneCraftoryAttributes.WIND_RESISTANCE.asHolder(), 10)
                    .xp(15).tamingChance(0.05f).setRideable().setFlying(),
            new GateSpawnData.Builder(0, 0).addToBiomeTag(70, RunecraftoryTags.Biomes.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, RunecraftoryTags.Biomes.IS_LUSH));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<BigMuck>> BIG_MUCK = regMonster(EntityType.Builder.of(BigMuck::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 26 / 16d, -3 / 16d)).sized(0.9f, 1.6f).clientTrackingRange(8), RuneCraftory.modRes("big_muck"),
            0xd7ce4a, 0xad5c25,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 20).putLevelGains(Attributes.MAX_HEALTH, 486)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 8).putLevelGains(Attributes.ATTACK_DAMAGE, 234)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 0).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 210)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 12).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 275)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 1).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 215)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.LOVE_RESISTANCE.asHolder(), 10)
                    .putAttributes(RuneCraftoryAttributes.DARK_RESISTANCE.asHolder(), -5)
                    .xp(20).tamingChance(0.05f).setRideable()
                    .withRideActionCosts(new EntityRideActionCosts.Builder().secondCost(0.5f, true)
                            .thirdCost(0.5f, true)),
            new GateSpawnData.Builder(0, 5).addToBiomeTag(60, RunecraftoryTags.Biomes.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, RunecraftoryTags.Biomes.IS_LUSH, RunecraftoryTags.Biomes.IS_MAGICAL, RunecraftoryTags.Biomes.IS_MUSHROOM));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<TrickyMuck>> TRICKY_MUCK = regMonster(EntityType.Builder.of(TrickyMuck::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 26 / 16d, -3 / 16d)).sized(0.9f, 1.6f).clientTrackingRange(8), RuneCraftory.modRes("tricky_muck"),
            0x207316, 0x90d681,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 29).putLevelGains(Attributes.MAX_HEALTH, 498)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 13).putLevelGains(Attributes.ATTACK_DAMAGE, 254)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 1).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 223)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 19).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 291)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 2).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 239)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 15)
                    .putAttributes(RuneCraftoryAttributes.LOVE_RESISTANCE.asHolder(), -10)
                    .xp(40).tamingChance(0.05f).setRideable()
                    .withRideActionCosts(new EntityRideActionCosts.Builder().secondCost(0.5f, true)
                            .thirdCost(0.5f, true)),
            new GateSpawnData.Builder(0, 25).addToBiomeTag(40, BiomeTags.IS_FOREST, RunecraftoryTags.Biomes.IS_LUSH, RunecraftoryTags.Biomes.IS_MAGICAL, RunecraftoryTags.Biomes.IS_MUSHROOM));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Buffamoo>> BUFFAMOO = regMonster(EntityType.Builder.of(Buffamoo::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 23 / 16d, -2 / 16d)).sized(1.2f, 1.45f).clientTrackingRange(8), RuneCraftory.modRes("buffamoo"),
            0xd8d8d0, 0x4e4e4c,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 25).putLevelGains(Attributes.MAX_HEALTH, 506)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 11).putLevelGains(Attributes.ATTACK_DAMAGE, 263)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 1).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 222)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 9).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 220)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 0).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 222)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.LOVE_RESISTANCE.asHolder(), 10)
                    .xp(20).tamingChance(0.15f).setRideable(),
            new GateSpawnData.Builder(0, 5).addToBiomeTag(70, RunecraftoryTags.Biomes.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, BiomeTags.IS_MOUNTAIN, RunecraftoryTags.Biomes.IS_LUSH));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Buffamoo>> BUFFALOO = regMonster(EntityType.Builder.of(Buffamoo::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 23 / 16d, -2 / 16d)).sized(1.2f, 1.45f).clientTrackingRange(8), RuneCraftory.modRes("buffaloo"),
            0x8a8a5e, 0xb5b489,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 35).putLevelGains(Attributes.MAX_HEALTH, 521)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 18).putLevelGains(Attributes.ATTACK_DAMAGE, 286)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 2).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 243)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 13).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 231)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 2).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 243)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.LOVE_RESISTANCE.asHolder(), -10)
                    .xp(40).tamingChance(0.05f).setRideable().setMinLevel(5),
            new GateSpawnData.Builder(0, 25).addToBiomeTag(60, RunecraftoryTags.Biomes.IS_MOUNTAIN_SLOPE, RunecraftoryTags.Biomes.IS_HOT, BiomeTags.IS_MOUNTAIN, BiomeTags.IS_HILL, BiomeTags.IS_BADLANDS));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Chipsqueek>> CHIPSQUEEK = regMonster(EntityType.Builder.of(Chipsqueek::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 9.5 / 16d, -5 / 16d)).sized(0.65f, 0.95f).clientTrackingRange(8), RuneCraftory.modRes("chipsqueek"),
            0xff3b5b, 0xf9ffbb,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 21).putLevelGains(Attributes.MAX_HEALTH, 489)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 11).putLevelGains(Attributes.ATTACK_DAMAGE, 261)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 0).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 213)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 9).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 205)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 1).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 210)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.LOVE_RESISTANCE.asHolder(), 10)
                    .xp(15).tamingChance(0.15f),
            new GateSpawnData.Builder(0, 5).addToBiomeTag(70, RunecraftoryTags.Biomes.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, BiomeTags.IS_MOUNTAIN, RunecraftoryTags.Biomes.IS_LUSH, BiomeTags.IS_SAVANNA));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Chipsqueek>> FURPY = regMonster(EntityType.Builder.of(Chipsqueek::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 9.5 / 16d, -5 / 16d)).sized(0.65f, 0.95f).clientTrackingRange(8), RuneCraftory.modRes("furpy"),
            0xab8620, 0xf9ffbb,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 28).putLevelGains(Attributes.MAX_HEALTH, 505)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 16).putLevelGains(Attributes.ATTACK_DAMAGE, 279)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 3).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 237)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 11).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 223)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 3).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 231)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 15)
                    .putAttributes(RuneCraftoryAttributes.LOVE_RESISTANCE.asHolder(), -10)
                    .xp(35).tamingChance(0.1f).setMinLevel(5),
            new GateSpawnData.Builder(0, 30).addToBiomeTag(60, BiomeTags.IS_FOREST, RunecraftoryTags.Biomes.IS_SANDY, BiomeTags.IS_SAVANNA));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<MineralSqueek>> MINERAL_SQUEEK = regMonster(EntityType.Builder.of(MineralSqueek::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 9.5 / 16d, -5 / 16d)).sized(0.65f, 0.95f).clientTrackingRange(8), RuneCraftory.modRes("mineral_squeek"),
            0xfa5a74, 0xf9ffbb,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 4)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 13).putLevelGains(Attributes.ATTACK_DAMAGE, 250)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 0)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 9).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 200)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 0)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .xp(15).tamingChance(0.15f),
            new GateSpawnData.Builder(0, 60).addToBiomeTag(12, BiomeTags.IS_HILL, BiomeTags.IS_MOUNTAIN));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Cluckadoodle>> CLUCKADOODLE = regMonster(EntityType.Builder.of(Cluckadoodle::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 11.5 / 16d, -2 / 16d)).sized(0.6f, 1.1f).clientTrackingRange(8), RuneCraftory.modRes("cluckadoodle"),
            0xc2c2c2, 0xdc2121,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 19).putLevelGains(Attributes.MAX_HEALTH, 495)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 13).putLevelGains(Attributes.ATTACK_DAMAGE, 265)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 0).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 231)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 3).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 200)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 0).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 225)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.FIRE_RESISTANCE.asHolder(), -25)
                    .putAttributes(RuneCraftoryAttributes.LOVE_RESISTANCE.asHolder(), 10)
                    .xp(20).tamingChance(0.15f),
            new GateSpawnData.Builder(0, 5).addToBiomeTag(70, RunecraftoryTags.Biomes.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, BiomeTags.IS_MOUNTAIN));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<PommePomme>> POMME_POMME = regMonster(EntityType.Builder.of(PommePomme::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 16 / 16d, -5 / 16d)).sized(1.0f, 1.6f).clientTrackingRange(8), RuneCraftory.modRes("pomme_pomme"),
            0xff1c2b, 0xf7b4b8,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 24).putLevelGains(Attributes.MAX_HEALTH, 515)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 10).putLevelGains(Attributes.ATTACK_DAMAGE, 248)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 0).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 251)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 9).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 215)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 0).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 242)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 15)
                    .putAttributes(RuneCraftoryAttributes.WIND_RESISTANCE.asHolder(), -25)
                    .xp(30).tamingChance(0.1f).setRideable(),
            new GateSpawnData.Builder(0, 5).addToBiomeTag(70, RunecraftoryTags.Biomes.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, RunecraftoryTags.Biomes.IS_MAGICAL));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<PommePomme>> MINO = regMonster(EntityType.Builder.of(PommePomme::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 16 / 16d, -5 / 16d)).sized(0.9f, 1.8f).clientTrackingRange(8), RuneCraftory.modRes("mino"),
            0x8b573d, 0xc0916d,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 28).putLevelGains(Attributes.MAX_HEALTH, 525)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 14).putLevelGains(Attributes.ATTACK_DAMAGE, 255)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 2).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 268)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 10).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 220)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 2).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 259)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 15)
                    .putAttributes(RuneCraftoryAttributes.WIND_RESISTANCE.asHolder(), -10)
                    .xp(40).tamingChance(0.05f).setMinLevel(5).setRideable(),
            new GateSpawnData.Builder(0, 15).addToBiomeTag(80, BiomeTags.IS_FOREST, BiomeTags.IS_TAIGA, RunecraftoryTags.Biomes.IS_MAGICAL));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Nappie>> NAPPIE = regMonster(EntityType.Builder.of(Nappie::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 16 / 16d, -5 / 16d)).sized(1.0f, 1.6f).clientTrackingRange(8), RuneCraftory.modRes("nappie"),
            0xb4843c, 0x1b5a0d,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 30).putLevelGains(Attributes.MAX_HEALTH, 525)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 13).putLevelGains(Attributes.ATTACK_DAMAGE, 249)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 3).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 259)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 14).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 247)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 2).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 268)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 15)
                    .putAttributes(RuneCraftoryAttributes.WIND_RESISTANCE.asHolder(), -25)
                    .xp(30).tamingChance(0.1f).setRideable(),
            new GateSpawnData.Builder(0, 25).addToBiomeTag(70, RunecraftoryTags.Biomes.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, BiomeTags.IS_BEACH, RunecraftoryTags.Biomes.IS_MAGICAL));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Tortas>> TORTAS = regMonster(EntityType.Builder.of(Tortas::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 11 / 16d, -4 / 16d)).sized(1.4f, 0.70f).clientTrackingRange(8), RuneCraftory.modRes("tortas"),
            0x5c6682, 0xa5848c,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 28).putLevelGains(Attributes.MAX_HEALTH, 520)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 15).putLevelGains(Attributes.ATTACK_DAMAGE, 269)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 2).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 261)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 9).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 208)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 0).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 244)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.FIRE_RESISTANCE.asHolder(), -25)
                    .putAttributes(RuneCraftoryAttributes.WATER_RESISTANCE.asHolder(), 15)
                    .xp(50).tamingChance(0.05f).setRideable().setMinLevel(5),
            new GateSpawnData.Builder(0, 12).canSpawnUnderwater().addToBiomeTag(70, BiomeTags.IS_BEACH, RunecraftoryTags.Biomes.IS_AQUATIC));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<SkyFish>> SKY_FISH = regMonster(EntityType.Builder.of(SkyFish::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 12 / 16d, -2 / 16d)).sized(1.2f, 0.7f).clientTrackingRange(8), RuneCraftory.modRes("sky_fish"),
            0x8fa4c5, 0x5a3536,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 23).putLevelGains(Attributes.MAX_HEALTH, 497)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 9).putLevelGains(Attributes.ATTACK_DAMAGE, 203)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 0).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 218)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 14).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 285)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 1).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 231)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.FIRE_RESISTANCE.asHolder(), -25)
                    .putAttributes(RuneCraftoryAttributes.WATER_RESISTANCE.asHolder(), 15)
                    .xp(50).tamingChance(0.05f).setRideable().setFlying()
                    .withRideActionCosts(new EntityRideActionCosts.Builder().secondCost(0.5f, true)),
            new GateSpawnData.Builder(0, 7).canSpawnUnderwater().addToBiomeTag(60, BiomeTags.IS_BEACH, RunecraftoryTags.Biomes.IS_AQUATIC));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Weagle>> WEAGLE = regMonster(EntityType.Builder.of(Weagle::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 16 / 16d, -1 / 16d)).sized(0.8f, 1.1f).clientTrackingRange(8), RuneCraftory.modRes("weagle"),
            0x8e127b, 0xdb9dd2, true,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 22).putLevelGains(Attributes.MAX_HEALTH, 510)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 11).putLevelGains(Attributes.ATTACK_DAMAGE, 255)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 0).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 231)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 8).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 210)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 1).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 227)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.WIND_RESISTANCE.asHolder(), 15)
                    .putAttributes(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), -10)
                    .xp(45).tamingChance(0.05f).setRideable().doesntNeedBarnRoof().setFlying(),
            new GateSpawnData.Builder(0, 7).addToBiomeTag(50, RunecraftoryTags.Biomes.IS_PLAINS, RunecraftoryTags.Biomes.IS_MOUNTAIN_PEAK, RunecraftoryTags.Biomes.IS_MOUNTAIN_SLOPE, BiomeTags.IS_MOUNTAIN, BiomeTags.IS_HILL));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Goblin>> GOBLIN = regMonster(EntityType.Builder.of(Goblin::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 13 / 16d, -5 / 16d)).vehicleAttachment(new Vec3(0, 7 / 16d, 0)).sized(0.6f, 1.6f).clientTrackingRange(8), RuneCraftory.modRes("goblin"),
            0x21b322, 0x462f2a,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 24).putLevelGains(Attributes.MAX_HEALTH, 500)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 13).putLevelGains(Attributes.ATTACK_DAMAGE, 267)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 0).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 240)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 10).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 228)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 0).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 220)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 10)
                    .xp(55).tamingChance(0.05f).setRideable(),
            new GateSpawnData.Builder(0, 7).addToBiomeTag(70, RunecraftoryTags.Biomes.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, BiomeTags.IS_MOUNTAIN, BiomeTags.IS_SAVANNA, RunecraftoryTags.Biomes.IS_SANDY));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<GoblinArcher>> GOBLIN_ARCHER = regMonster(EntityType.Builder.of(GoblinArcher::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 13 / 16d, -5 / 16d)).vehicleAttachment(new Vec3(0, 7 / 16d, 0)).sized(0.6f, 1.6f).clientTrackingRange(8), RuneCraftory.modRes("goblin_archer"),
            0x21b322, 0x462f2a,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 24).putLevelGains(Attributes.MAX_HEALTH, 500)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 13).putLevelGains(Attributes.ATTACK_DAMAGE, 267)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 0).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 240)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 10).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 228)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 0).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 220)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 10)
                    .xp(55).tamingChance(0.05f).setRideable(),
            new GateSpawnData.Builder(0, 7).addToBiomeTag(70, RunecraftoryTags.Biomes.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, BiomeTags.IS_MOUNTAIN, BiomeTags.IS_SAVANNA, RunecraftoryTags.Biomes.IS_SANDY));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<GoblinPirate>> GOBLIN_PIRATE = regMonster(EntityType.Builder.of(GoblinPirate::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 13 / 16d, -5 / 16d)).vehicleAttachment(new Vec3(0, 7 / 16d, 0)).sized(0.6f, 1.6f).clientTrackingRange(8), RuneCraftory.modRes("goblin_pirate"),
            0x484209, 0x29307f,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 26).putLevelGains(Attributes.MAX_HEALTH, 521)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 17).putLevelGains(Attributes.ATTACK_DAMAGE, 288)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 1).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 258)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 4).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 237)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 1).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 246)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 10)
                    .putAttributes(RuneCraftoryAttributes.WATER_RESISTANCE.asHolder(), 5)
                    .xp(50).tamingChance(0.05f).setRideable(),
            new GateSpawnData.Builder(0, 30).addToBiomeTag(50, RunecraftoryTags.Biomes.IS_HOT, BiomeTags.IS_SAVANNA, RunecraftoryTags.Biomes.IS_SANDY, BiomeTags.IS_BADLANDS));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<GoblinGangster>> GOBLIN_GANGSTER = regMonster(EntityType.Builder.of(GoblinGangster::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 13 / 16d, -5 / 16d)).vehicleAttachment(new Vec3(0, 7 / 16d, 0)).sized(0.6f, 1.6f).clientTrackingRange(8), RuneCraftory.modRes("goblin_gangster"),
            0x6e5d2d, 0x316275,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 26).putLevelGains(Attributes.MAX_HEALTH, 521)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 17).putLevelGains(Attributes.ATTACK_DAMAGE, 288)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 1).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 258)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 4).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 237)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 1).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 246)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 10)
                    .putAttributes(RuneCraftoryAttributes.FIRE_RESISTANCE.asHolder(), 5)
                    .xp(50).tamingChance(0.05f).setRideable(),
            new GateSpawnData.Builder(0, 30).addToBiomeTag(50, RunecraftoryTags.Biomes.IS_HOT, BiomeTags.IS_SAVANNA, RunecraftoryTags.Biomes.IS_SANDY, BiomeTags.IS_BADLANDS));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<GoblinPirate>> GOBLIN_CAPTAIN = regMonster(EntityType.Builder.of(GoblinPirate::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 13 / 16d, -5 / 16d)).vehicleAttachment(new Vec3(0, 7 / 16d, 0)).sized(0.6f, 1.6f).clientTrackingRange(8), RuneCraftory.modRes("captain_goblin"),
            0x452621, 0xe9e9e9,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 30).putLevelGains(Attributes.MAX_HEALTH, 544)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 21).putLevelGains(Attributes.ATTACK_DAMAGE, 301)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 2).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 255)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 16).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 253)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 1).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 249)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 10)
                    .putAttributes(RuneCraftoryAttributes.WATER_RESISTANCE.asHolder(), 10)
                    .xp(50).tamingChance(0.05f).setRideable(),
            new GateSpawnData.Builder(0, 50).addToBiomeTag(50, RunecraftoryTags.Biomes.IS_HOT, BiomeTags.IS_SAVANNA, RunecraftoryTags.Biomes.IS_SANDY, BiomeTags.IS_BADLANDS, BiomeTags.IS_HILL));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<GoblinGangster>> GOBLIN_DON = regMonster(EntityType.Builder.of(GoblinGangster::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 13 / 16d, -5 / 16d)).vehicleAttachment(new Vec3(0, 7 / 16d, 0)).sized(0.6f, 1.6f).clientTrackingRange(8), RuneCraftory.modRes("goblin_don"),
            0x474133, 0x286c83,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 30).putLevelGains(Attributes.MAX_HEALTH, 544)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 21).putLevelGains(Attributes.ATTACK_DAMAGE, 301)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 2).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 255)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 16).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 253)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 1).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 249)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 10)
                    .putAttributes(RuneCraftoryAttributes.FIRE_RESISTANCE.asHolder(), 10)
                    .xp(50).tamingChance(0.05f).setRideable(),
            new GateSpawnData.Builder(0, 50).addToBiomeTag(40, RunecraftoryTags.Biomes.IS_HOT, BiomeTags.IS_SAVANNA, RunecraftoryTags.Biomes.IS_SANDY, BiomeTags.IS_BADLANDS, BiomeTags.IS_HILL));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Duck>> DUCK = regMonster(EntityType.Builder.of(Duck::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 24.6 / 16d, -6 / 16d)).sized(0.9f, 1.6f).spawnDimensionsScale(0.85f).clientTrackingRange(8), RuneCraftory.modRes("duck"),
            0xdabf33, 0x845242,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 23).putLevelGains(Attributes.MAX_HEALTH, 530)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 13).putLevelGains(Attributes.ATTACK_DAMAGE, 263)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 0).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 225)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 6).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 240)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 0).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 235)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), -10)
                    .putAttributes(RuneCraftoryAttributes.WATER_RESISTANCE.asHolder(), 10)
                    .xp(50).tamingChance(0.1f).setRideable(),
            new GateSpawnData.Builder(0, 7).addToBiomeTag(40, RunecraftoryTags.Biomes.IS_PLAINS, RunecraftoryTags.Biomes.IS_AQUATIC, BiomeTags.IS_BEACH));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Fairy>> FAIRY = regMonster(EntityType.Builder.of(Fairy::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 10 / 16d, -6 / 16d)).sized(0.55f, 1.5f).spawnDimensionsScale(0.75f).clientTrackingRange(8), RuneCraftory.modRes("fairy"),
            0x4dad2a, 0xcdc41f, true,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 19).putLevelGains(Attributes.MAX_HEALTH, 485)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 10).putLevelGains(Attributes.ATTACK_DAMAGE, 210)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 0).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 215)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 16).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 280)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 2).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 228)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.LOVE_RESISTANCE.asHolder(), 10)
                    .putAttributes(RuneCraftoryAttributes.LIGHT_RESISTANCE.asHolder(), 10)
                    .putAttributes(RuneCraftoryAttributes.DARK_RESISTANCE.asHolder(), -10)
                    .xp(66).tamingChance(0.05f).setFlying()
                    .withRideActionCosts(new EntityRideActionCosts.Builder(0.5f, true).secondCost(0.5f, true)),
            new GateSpawnData.Builder(0, 7).addToBiomeTag(50, RunecraftoryTags.Biomes.IS_PLAINS, BiomeTags.IS_FOREST, RunecraftoryTags.Biomes.IS_MAGICAL));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Ghost>> GHOST = regMonster(EntityType.Builder.of(Ghost::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 11.5 / 16d, -10 / 16d)).sized(1, 2.6f).spawnDimensionsScale(0.8f).clientTrackingRange(8), RuneCraftory.modRes("ghost"),
            0x4d3d35, 0x838383, true,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 23).putLevelGains(Attributes.MAX_HEALTH, 510)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 10).putLevelGains(Attributes.ATTACK_DAMAGE, 271)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 1).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 213)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 12).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 263)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 1).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 220)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.LIGHT_RESISTANCE.asHolder(), -10)
                    .putAttributes(RuneCraftoryAttributes.DARK_RESISTANCE.asHolder(), 10)
                    .xp(70).tamingChance(0.05f).setFlying(),
            new GateSpawnData.Builder(0, 10).addToBiomeTag(75, RunecraftoryTags.Biomes.IS_SPOOKY, RunecraftoryTags.Biomes.IS_DEAD, RunecraftoryTags.Biomes.IS_SWAMP));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Ghost>> GHOST_RAY = regMonster(EntityType.Builder.of(Ghost::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 11.5 / 16d, -10 / 16d)).sized(1, 2.6f).spawnDimensionsScale(1.1f).clientTrackingRange(8), RuneCraftory.modRes("ghost_ray"),
            0x552217, 0x905a5a, true,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 45).putLevelGains(Attributes.MAX_HEALTH, 540)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 18).putLevelGains(Attributes.ATTACK_DAMAGE, 296)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 2).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 232)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 19).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 289)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 2).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 241)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.LIGHT_RESISTANCE.asHolder(), -10)
                    .putAttributes(RuneCraftoryAttributes.DARK_RESISTANCE.asHolder(), 10)
                    .setMinLevel(27)
                    .xp(150).tamingChance(0.02f).setBarnOccupancy(2).setFlying(),
            new GateSpawnData.Builder(0, 15).addToBiomeTag(15, RunecraftoryTags.Biomes.IS_SPOOKY, RunecraftoryTags.Biomes.IS_DEAD, RunecraftoryTags.Biomes.IS_SWAMP));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Spirit>> SPIRIT = regMonster(EntityType.Builder.of(Spirit::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 10 / 16d, -3 / 16d)).sized(0.5f, 0.6f).clientTrackingRange(8), RuneCraftory.modRes("spirit"),
            0xfdfdfd, 0xc3f8f7, true,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 21).putLevelGains(Attributes.MAX_HEALTH, 497)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 9).putLevelGains(Attributes.ATTACK_DAMAGE, 210)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 1).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 210)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 17).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 284)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 2).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 224)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.LIGHT_RESISTANCE.asHolder(), -10)
                    .putAttributes(RuneCraftoryAttributes.DARK_RESISTANCE.asHolder(), 10)
                    .xp(75).tamingChance(0.05f).setFlying(),
            new GateSpawnData.Builder(0, 15).addToBiomeTag(60, RunecraftoryTags.Biomes.IS_SPOOKY, RunecraftoryTags.Biomes.IS_DEAD, RunecraftoryTags.Biomes.IS_SWAMP, RunecraftoryTags.Biomes.IS_MAGICAL, BiomeTags.IS_END));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Ignis>> IGNIS = regMonster(EntityType.Builder.of(Ignis::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 10 / 16d, -3 / 16d)).sized(0.5f, 0.6f).fireImmune().clientTrackingRange(8), RuneCraftory.modRes("ignis"),
            0xaa3100, 0x9f5e3f, true,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 23).putLevelGains(Attributes.MAX_HEALTH, 502)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 14).putLevelGains(Attributes.ATTACK_DAMAGE, 214)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 2).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 219)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 19).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 291)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 3).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 228)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.FIRE_RESISTANCE.asHolder(), 25)
                    .putAttributes(RuneCraftoryAttributes.WATER_RESISTANCE.asHolder(), -25)
                    .xp(75).tamingChance(0.05f).setFlying(),
            new GateSpawnData.Builder(0, 15).addToBiomeTag(60, RunecraftoryTags.Biomes.IS_SPOOKY, RunecraftoryTags.Biomes.IS_DEAD, RunecraftoryTags.Biomes.IS_HOT, BiomeTags.IS_BADLANDS));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Spider>> SPIDER = regMonster(EntityType.Builder.of(Spider::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 10 / 16d, 3.5 / 16d)).sized(1.1f, 0.7f).clientTrackingRange(8), RuneCraftory.modRes("spider"),
            0x6f6751, 0x404148,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 19).putLevelGains(Attributes.MAX_HEALTH, 489)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 13).putLevelGains(Attributes.ATTACK_DAMAGE, 267)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 0).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 227)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 11).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 209)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 0).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 222)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.LIGHT_RESISTANCE.asHolder(), -10)
                    .putAttributes(RuneCraftoryAttributes.FIRE_RESISTANCE.asHolder(), -10)
                    .putAttributes(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 10)
                    .putAttributes(RuneCraftoryAttributes.DARK_RESISTANCE.asHolder(), 10)
                    .xp(65).tamingChance(0.05f)
                    .withRideActionCosts(new EntityRideActionCosts.Builder(0.5f, true).secondCost(0.5f, true)),
            new GateSpawnData.Builder(0, 15).addToBiomeTag(80, RunecraftoryTags.Biomes.IS_SPOOKY, BiomeTags.IS_FOREST, BiomeTags.IS_JUNGLE, RunecraftoryTags.Biomes.IS_LUSH));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Panther>> SHADOW_PANTHER = regMonster(EntityType.Builder.of(Panther::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 21.5 / 16d, -13 / 16d)).sized(1.3f, 2.2f).clientTrackingRange(8), RuneCraftory.modRes("shadow_panther"),
            0x27375b, 0x733838,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 28).putLevelGains(Attributes.MAX_HEALTH, 515)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 17).putLevelGains(Attributes.ATTACK_DAMAGE, 275)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 2).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 225)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 10).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 238)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 2).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 233)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.LIGHT_RESISTANCE.asHolder(), -10)
                    .putAttributes(RuneCraftoryAttributes.DARK_RESISTANCE.asHolder(), 10)
                    .xp(100).tamingChance(0.05f).setRideable(),
            new GateSpawnData.Builder(0, 15).addToBiomeTag(30, RunecraftoryTags.Biomes.IS_SPOOKY, RunecraftoryTags.Biomes.IS_SWAMP, RunecraftoryTags.Biomes.IS_MOUNTAIN_PEAK, RunecraftoryTags.Biomes.IS_MOUNTAIN_SLOPE));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Mimic>> MONSTER_BOX = regMonster(EntityType.Builder.of(Mimic::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 7 / 16d, 7 / 16d)).sized(1, 1).clientTrackingRange(8), RuneCraftory.modRes("monster_box"),
            0xac935e, 0x462f10,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 30).putLevelGains(Attributes.MAX_HEALTH, 530)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 15).putLevelGains(Attributes.ATTACK_DAMAGE, 265)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 2).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 235)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 13).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 265)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 1).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 235)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.LIGHT_RESISTANCE.asHolder(), 10)
                    .putAttributes(RuneCraftoryAttributes.DARK_RESISTANCE.asHolder(), 10)
                    .putAttributes(RuneCraftoryAttributes.LOVE_RESISTANCE.asHolder(), 10)
                    .xp(300).tamingChance(0.02f),
            new GateSpawnData.Builder(0, 0));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Mimic>> GOBBLE_BOX = regMonster(EntityType.Builder.of(Mimic::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 7 / 16d, 7 / 16d)).sized(1, 1).clientTrackingRange(8), RuneCraftory.modRes("gobble_box"),
            0x8f9cc4, 0x343843,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 50).putLevelGains(Attributes.MAX_HEALTH, 550)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 20).putLevelGains(Attributes.ATTACK_DAMAGE, 279)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 3).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 254)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 16).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 279)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 2).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 254)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.LIGHT_RESISTANCE.asHolder(), 15)
                    .putAttributes(RuneCraftoryAttributes.DARK_RESISTANCE.asHolder(), 15)
                    .putAttributes(RuneCraftoryAttributes.LOVE_RESISTANCE.asHolder(), 15)
                    .xp(500).tamingChance(0.015f),
            new GateSpawnData.Builder(0, 20));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Hornet>> HORNET = regMonster(EntityType.Builder.of(Hornet::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 13.5 / 16d, -4 / 16d)).sized(0.7f, 0.85f).clientTrackingRange(8), RuneCraftory.modRes("hornet"),
            0x627d73, 0x20201f, true,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 19).putLevelGains(Attributes.MAX_HEALTH, 499)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 11).putLevelGains(Attributes.ATTACK_DAMAGE, 258)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 3).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 226)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 8).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 210)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 4).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 229)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.FIRE_RESISTANCE.asHolder(), -15)
                    .xp(135).tamingChance(0.05f).setRideable().setMinLevel(5).setFlying(),
            new GateSpawnData.Builder(0, 20).addToBiomeTag(55, RunecraftoryTags.Biomes.IS_PLAINS, BiomeTags.IS_FOREST));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Wolf>> SILVER_WOLF = regMonster(EntityType.Builder.of(Wolf::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 17 / 16d, -6 / 16d)).sized(0.8f, 1.15f).clientTrackingRange(8), RuneCraftory.modRes("silver_wolf"),
            0x9bb9c3, 0x436ea1,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 23).putLevelGains(Attributes.MAX_HEALTH, 515)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 12).putLevelGains(Attributes.ATTACK_DAMAGE, 267)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 4).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 225)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 7).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 210)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 2).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 225)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.LIGHT_RESISTANCE.asHolder(), -5)
                    .putAttributes(RuneCraftoryAttributes.DARK_RESISTANCE.asHolder(), -5)
                    .putAttributes(RuneCraftoryAttributes.LOVE_RESISTANCE.asHolder(), 5)
                    .xp(35).tamingChance(0.05f).setRideable().setMinLevel(10),
            new GateSpawnData.Builder(0, 20).addToBiomeTag(45, RunecraftoryTags.Biomes.IS_PLAINS, BiomeTags.IS_FOREST, RunecraftoryTags.Biomes.IS_SNOWY));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<LeafBall>> LEAF_BALL = regMonster(EntityType.Builder.of(LeafBall::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 16 / 16d, -6 / 16d)).sized(0.8f, 1.2f).clientTrackingRange(8), RuneCraftory.modRes("leaf_ball"),
            0xdcb5f0, 0xb72fd3, true,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 24).putLevelGains(Attributes.MAX_HEALTH, 520)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 9).putLevelGains(Attributes.ATTACK_DAMAGE, 250)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 3).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 215)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 14).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 277)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 4).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 215)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.FIRE_RESISTANCE.asHolder(), -15)
                    .putAttributes(RuneCraftoryAttributes.WIND_RESISTANCE.asHolder(), 15)
                    .xp(35).tamingChance(0.05f).setRideable().setMinLevel(10).setFlying()
                    .withRideActionCosts(new EntityRideActionCosts.Builder(0.5f, true).secondCost(0, false)),
            new GateSpawnData.Builder(0, 20).addToBiomeTag(70, RunecraftoryTags.Biomes.IS_LUSH, BiomeTags.IS_FOREST, RunecraftoryTags.Biomes.IS_MAGICAL));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<PalmCat>> PALM_CAT = regMonster(EntityType.Builder.of(PalmCat::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 15.75 / 16d, -4 / 16d)).sized(0.6f, 1.9f).clientTrackingRange(8), RuneCraftory.modRes("palm_cat"),
            0xc98f2d, 0xb46d28,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 24).putLevelGains(Attributes.MAX_HEALTH, 509)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 13).putLevelGains(Attributes.ATTACK_DAMAGE, 280)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 2).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 233)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 8).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 230)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 4).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 233)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 15)
                    .putAttributes(RuneCraftoryAttributes.WIND_RESISTANCE.asHolder(), 15)
                    .xp(35).tamingChance(0.05f),
            new GateSpawnData.Builder(0, 30).addToBiomeTag(40, BiomeTags.IS_FOREST, BiomeTags.IS_TAIGA));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<PalmCat>> MALM_TIGER = regMonster(EntityType.Builder.of(PalmCat::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 15.75 / 16d, -4 / 16d)).sized(0.6f, 1.9f).clientTrackingRange(8), RuneCraftory.modRes("malm_tiger"),
            0x8596ae, 0x3a5573,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 29).putLevelGains(Attributes.MAX_HEALTH, 511)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 20).putLevelGains(Attributes.ATTACK_DAMAGE, 281)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 3).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 227)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 13).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 230)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 4).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 238)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 20)
                    .putAttributes(RuneCraftoryAttributes.WIND_RESISTANCE.asHolder(), 15)
                    .xp(35).tamingChance(0.05f),
            new GateSpawnData.Builder(0, 60).addToBiomeTag(20, BiomeTags.IS_FOREST, BiomeTags.IS_TAIGA, BiomeTags.IS_HILL, BiomeTags.IS_MOUNTAIN, RunecraftoryTags.Biomes.IS_SNOWY));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<FlowerLily>> FLOWER_LILY = regMonster(EntityType.Builder.of(FlowerLily::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 14 / 16d, -4 / 16d)).sized(0.75f, 1.65f).clientTrackingRange(8), RuneCraftory.modRes("flower_lily"),
            0xe8b3e7, 0x156e12,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 23).putLevelGains(Attributes.MAX_HEALTH, 513)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 16).putLevelGains(Attributes.ATTACK_DAMAGE, 273)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 1).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 248)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 14).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 238)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 1).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 232)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.WIND_RESISTANCE.asHolder(), 7)
                    .putAttributes(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 7)
                    .putAttributes(RuneCraftoryAttributes.FIRE_RESISTANCE.asHolder(), -10)
                    .xp(40).tamingChance(0.06f).setRideable()
                    .withRideActionCosts(new EntityRideActionCosts.Builder(0.5f, true).secondCost(0, false)),
            new GateSpawnData.Builder(0, 30).addToBiomeTag(60, RunecraftoryTags.Biomes.IS_LUSH, RunecraftoryTags.Biomes.IS_MAGICAL, BiomeTags.IS_JUNGLE));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<FlowerLion>> FLOWER_LION = regMonster(EntityType.Builder.of(FlowerLion::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 14 / 16d, -4 / 16d)).sized(0.75f, 1.65f).clientTrackingRange(8), RuneCraftory.modRes("flower_lion"),
            0xf2ad7a, 0x893a1d,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 26).putLevelGains(Attributes.MAX_HEALTH, 520)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 18).putLevelGains(Attributes.ATTACK_DAMAGE, 285)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 2).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 258)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 16).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 245)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 2).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 251)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.FIRE_RESISTANCE.asHolder(), -5)
                    .putAttributes(RuneCraftoryAttributes.WIND_RESISTANCE.asHolder(), -5)
                    .xp(100).tamingChance(0.05f).setRideable()
                    .withRideActionCosts(new EntityRideActionCosts.Builder().secondCost(0.5f, true)
                            .thirdCost(0, false)),
            new GateSpawnData.Builder(0, 40).addToBiomeTag(60, RunecraftoryTags.Biomes.IS_HOT, BiomeTags.IS_SAVANNA));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Scorpion>> SCORPION = regMonster(EntityType.Builder.of(Scorpion::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 6 / 16d, -3 / 16d)).sized(1.1f, 0.6f).clientTrackingRange(8), RuneCraftory.modRes("scorpion"),
            0x606060, 0xacacac,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 21).putLevelGains(Attributes.MAX_HEALTH, 505)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 13).putLevelGains(Attributes.ATTACK_DAMAGE, 271)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 2).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 244)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 9).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 220)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 0).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 238)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.POISON.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.FIRE_RESISTANCE.asHolder(), -10)
                    .xp(75).tamingChance(0.05f),
            new GateSpawnData.Builder(0, 20).addToBiomeTag(50, RunecraftoryTags.Biomes.IS_HOT, BiomeTags.IS_SAVANNA, RunecraftoryTags.Biomes.IS_SANDY, BiomeTags.IS_BADLANDS));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Troll>> TROLL = regMonster(EntityType.Builder.of(Troll::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 37 / 16d, -6 / 16d)).sized(1.5f, 3f).clientTrackingRange(8), RuneCraftory.modRes("troll"),
            0xac924b, 0xcfcbbc,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 35).putLevelGains(Attributes.MAX_HEALTH, 540)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 21).putLevelGains(Attributes.ATTACK_DAMAGE, 290)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 3).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 245)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 4).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 200)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 2).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 235)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 8)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .xp(100).tamingChance(0.05f),
            new GateSpawnData.Builder(0, 40).addToBiomeTag(40, BiomeTags.IS_MOUNTAIN, RunecraftoryTags.Biomes.IS_SPARSE_VEGETATION, RunecraftoryTags.Biomes.IS_MOUNTAIN_SLOPE, BiomeTags.IS_HILL, RunecraftoryTags.Biomes.IS_DEAD));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<VeggieGhost>> TOMATO_GHOST = regMonster(EntityType.Builder.of(VeggieGhost::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 12 / 16d, -4 / 16d)).sized(0.8f, 1.6f).clientTrackingRange(8), RuneCraftory.modRes("tomato_ghost"),
            0x902323, 0x85268b, true,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 22).putLevelGains(Attributes.MAX_HEALTH, 510)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 16).putLevelGains(Attributes.ATTACK_DAMAGE, 260)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 1).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 230)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 18).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 260)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 2).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 230)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.WIND_RESISTANCE.asHolder(), -5)
                    .putAttributes(RuneCraftoryAttributes.DARK_RESISTANCE.asHolder(), 10)
                    .putAttributes(RuneCraftoryAttributes.LIGHT_RESISTANCE.asHolder(), 10)
                    .xp(100).tamingChance(0.05f).setRideable().setFlying()
                    .withRideActionCosts(new EntityRideActionCosts.Builder().secondCost(0.5f, true)),
            new GateSpawnData.Builder(0, 40).addToBiomeTag(75, RunecraftoryTags.Biomes.IS_HOT, RunecraftoryTags.Biomes.IS_DEAD, RunecraftoryTags.Biomes.IS_WASTELAND, RunecraftoryTags.Biomes.IS_MAGICAL));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Mage>> LITTLE_EMPEROR = regMonster(EntityType.Builder.of(Mage::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 13.5 / 16d, -5 / 16d)).sized(0.6f, 1.7f).clientTrackingRange(8), RuneCraftory.modRes("little_emperor"),
            0x49ab5f, 0xdedede,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 21).putLevelGains(Attributes.MAX_HEALTH, 520)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 13).putLevelGains(Attributes.ATTACK_DAMAGE, 258)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 1).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 225)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 19).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 271)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 4).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 252)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.LIGHT_RESISTANCE.asHolder(), 15)
                    .putAttributes(RuneCraftoryAttributes.DARK_RESISTANCE.asHolder(), 15)
                    .xp(35).tamingChance(0.05f),
            new GateSpawnData.Builder(0, 60).addToBiomeTag(40, RunecraftoryTags.Biomes.IS_SPOOKY, RunecraftoryTags.Biomes.IS_MAGICAL, BiomeTags.IS_MOUNTAIN));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Demon>> DEMON = regMonster(EntityType.Builder.of(Demon::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 15.5 / 16d, -5 / 16d)).sized(0.6f, 2.1f).spawnDimensionsScale(0.85f).clientTrackingRange(8), RuneCraftory.modRes("demon"),
            0xba8b84, 0x6a5450,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 27).putLevelGains(Attributes.MAX_HEALTH, 515)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 19).putLevelGains(Attributes.ATTACK_DAMAGE, 264)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 3).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 235)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 18).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 269)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 4).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 235)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.LIGHT_RESISTANCE.asHolder(), -15)
                    .putAttributes(RuneCraftoryAttributes.DARK_RESISTANCE.asHolder(), 15)
                    .xp(35).tamingChance(0.05f),
            new GateSpawnData.Builder(0, 45).addToBiomeTag(70, RunecraftoryTags.Biomes.IS_SPOOKY, RunecraftoryTags.Biomes.IS_MAGICAL, RunecraftoryTags.Biomes.IS_DEAD, BiomeTags.IS_MOUNTAIN));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Demon>> ARCH_DEMON = regMonster(EntityType.Builder.of(Demon::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 15.5 / 16d, -5 / 16d)).sized(0.6f, 2.1f).clientTrackingRange(8), RuneCraftory.modRes("arch_demon"),
            0x9f6a63, 0x372321,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 40).putLevelGains(Attributes.MAX_HEALTH, 540)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 22).putLevelGains(Attributes.ATTACK_DAMAGE, 279)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 3).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 244)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 20).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 284)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 5).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 244)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .putAttributes(RuneCraftoryAttributes.LIGHT_RESISTANCE.asHolder(), -10)
                    .putAttributes(RuneCraftoryAttributes.DARK_RESISTANCE.asHolder(), 10)
                    .xp(35).tamingChance(0.05f),
            new GateSpawnData.Builder(0, 50).addToBiomeTag(30, RunecraftoryTags.Biomes.IS_SPOOKY, RunecraftoryTags.Biomes.IS_MAGICAL, RunecraftoryTags.Biomes.IS_DEAD, BiomeTags.IS_MOUNTAIN));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Minotaur>> MINOTAUR = regMonster(EntityType.Builder.of(Minotaur::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 37 / 16d, -5 / 16d)).sized(1.4f, 2.9f).clientTrackingRange(8), RuneCraftory.modRes("minotaur"),
            0x61423d, 0x2c2825,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 40).putLevelGains(Attributes.MAX_HEALTH, 525)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 23).putLevelGains(Attributes.ATTACK_DAMAGE, 290)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 4).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 245)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 14).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 230)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 3).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 235)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .xp(35).tamingChance(0.05f),
            new GateSpawnData.Builder(0, 50).addToBiomeTag(60, BiomeTags.IS_MOUNTAIN));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Minotaur>> MINOTAUR_KING = regMonster(EntityType.Builder.of(Minotaur::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 37 / 16d, -5 / 16d)).sized(1.4f, 2.9f).clientTrackingRange(8), RuneCraftory.modRes("minotaur_king"),
            0x344a53, 0x907822,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 70).putLevelGains(Attributes.MAX_HEALTH, 550)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 30).putLevelGains(Attributes.ATTACK_DAMAGE, 305)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 5).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 256)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 14).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 235)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 5).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 248)
                    .putAttributes(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL.asHolder(), 1)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                    .xp(35).tamingChance(0.05f),
            new GateSpawnData.Builder(0, 75).addToBiomeTag(18, BiomeTags.IS_MOUNTAIN));

    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Chimera>> CHIMERA = regBoss(EntityType.Builder.of(Chimera::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 22.75 / 16d, -5 / 16d)).sized(1.45f, 1.45f).clientTrackingRange(8), RuneCraftory.modRes("chimera"),
            0x536983, 0xaaa7c8,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 315).putLevelGains(Attributes.MAX_HEALTH, 615)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 17).putLevelGains(Attributes.ATTACK_DAMAGE, 302)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 4).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 242)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 14).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 289)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 3).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 248)
                    .putAttributes(RuneCraftoryAttributes.POISON.asHolder(), 2)
                    .putAttributes(RuneCraftoryAttributes.PARALYSIS.asHolder(), 2)
                    .putAttributes(RuneCraftoryAttributes.SEAL.asHolder(), 2)
                    .putAttributes(RuneCraftoryAttributes.LOVE_RESISTANCE.asHolder(), 15)
                    .putAttributes(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), -25)
                    .putAttributes(RuneCraftoryAttributes.WIND_RESISTANCE.asHolder(), -25)
                    .putAttributes(RuneCraftoryAttributes.FIRE_RESISTANCE.asHolder(), 50)
                    .putAttributes(RuneCraftoryAttributes.WATER_RESISTANCE.asHolder(), 50)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 25)
                    .putAttributes(RuneCraftoryAttributes.DRAIN_RESISTANCE.asHolder(), 50)
                    .putAttributes(RuneCraftoryAttributes.DIZZY_RESISTANCE.asHolder(), 100)
                    .putAttributes(Attributes.KNOCKBACK_RESISTANCE, 1)
                    .putAttributes(RuneCraftoryAttributes.STUN_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.PARALYSIS_RESISTANCE.asHolder(), 95)
                    .putAttributes(RuneCraftoryAttributes.POISON_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.SEAL_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.SLEEP_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.FATIGUE_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.COLD_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.FAINT_RESISTANCE.asHolder(), 100)
                    .xp(150).tamingChance(BOSS_TAMING_CHANCE).setBarnOccupancy(2).setRideable()
                    .withLevelIncrease(1, 5)
                    .withLevelIncrease(2, 1)
                    .withLevelIncrease(7, 3)
                    .withLevelIncrease(15, 5)
                    .setMinLevel(12)
                    .withSpawnerPredicate(LibAdvancements.playerAdvancementCheck(LibAdvancements.GREATER_DEMON))
                    .withRideActionCosts(new EntityRideActionCosts.Builder().secondCost(0.5f, true)
                            .thirdCost(0.5f, true)));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Rafflesia>> RAFFLESIA = regBoss(EntityType.Builder.of(Rafflesia::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 43 / 16d, 3.5 / 16d)).sized(1.15f, 2.8f).clientTrackingRange(8), RuneCraftory.modRes("rafflesia"),
            0x9b58ba, 0x0a8414,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 330).putLevelGains(Attributes.MAX_HEALTH, 625)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 13).putLevelGains(Attributes.ATTACK_DAMAGE, 271)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 4).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 238)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 16).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 291)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 6).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 249)
                    .putAttributes(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 15)
                    .putAttributes(RuneCraftoryAttributes.WATER_RESISTANCE.asHolder(), 15)
                    .putAttributes(RuneCraftoryAttributes.FIRE_RESISTANCE.asHolder(), 15)
                    .putAttributes(RuneCraftoryAttributes.WIND_RESISTANCE.asHolder(), -25)
                    .putAttributes(RuneCraftoryAttributes.LIGHT_RESISTANCE.asHolder(), -15)
                    .putAttributes(RuneCraftoryAttributes.LOVE_RESISTANCE.asHolder(), -25)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 25)
                    .putAttributes(RuneCraftoryAttributes.DRAIN_RESISTANCE.asHolder(), 25)
                    .putAttributes(RuneCraftoryAttributes.DIZZY_RESISTANCE.asHolder(), 100)
                    .putAttributes(Attributes.KNOCKBACK_RESISTANCE, 1)
                    .putAttributes(RuneCraftoryAttributes.STUN_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.PARALYSIS_RESISTANCE.asHolder(), 97)
                    .putAttributes(RuneCraftoryAttributes.POISON_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.SEAL_RESISTANCE.asHolder(), 80)
                    .putAttributes(RuneCraftoryAttributes.SLEEP_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.FATIGUE_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.COLD_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.FAINT_RESISTANCE.asHolder(), 100)
                    .xp(350).tamingChance(0).setBarnOccupancy(3).setRideable()
                    .withLevelIncrease(1, 7)
                    .withLevelIncrease(2, 1)
                    .withLevelIncrease(7, 3)
                    .withLevelIncrease(15, 5)
                    .setMinLevel(20)
                    .withSpawnerPredicate(LibAdvancements.playerAdvancementCheck(LibAdvancements.CHIMERA)));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Grimoire>> GRIMOIRE = regBoss(EntityType.Builder.of(Grimoire::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 39 / 16d, 11.5 / 16d)).sized(2, 2.8f).spawnDimensionsScale(1.5f).clientTrackingRange(8), RuneCraftory.modRes("grimoire"),
            0x3b785c, 0x2e4d3f,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 350).putLevelGains(Attributes.MAX_HEALTH, 625)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 18).putLevelGains(Attributes.ATTACK_DAMAGE, 292)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 5.5).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 253)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 16).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 278)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 4).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 253)
                    .putAttributes(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 10)
                    .putAttributes(RuneCraftoryAttributes.WIND_RESISTANCE.asHolder(), -100)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 25)
                    .putAttributes(RuneCraftoryAttributes.DRAIN_RESISTANCE.asHolder(), 25)
                    .putAttributes(RuneCraftoryAttributes.DIZZY_RESISTANCE.asHolder(), 100)
                    .putAttributes(Attributes.KNOCKBACK_RESISTANCE, 1)
                    .putAttributes(RuneCraftoryAttributes.STUN_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.PARALYSIS_RESISTANCE.asHolder(), 97)
                    .putAttributes(RuneCraftoryAttributes.POISON_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.SEAL_RESISTANCE.asHolder(), 80)
                    .putAttributes(RuneCraftoryAttributes.SLEEP_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.FATIGUE_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.COLD_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.FAINT_RESISTANCE.asHolder(), 100)
                    .xp(450).tamingChance(BOSS_TAMING_CHANCE).setBarnOccupancy(3).setRideable()
                    .withLevelIncrease(1, 10)
                    .withLevelIncrease(2, 1)
                    .withLevelIncrease(7, 3)
                    .withLevelIncrease(15, 5)
                    .setMinLevel(50)
                    .withSpawnerPredicate(LibAdvancements.playerAdvancementCheck(LibAdvancements.RAFFLESIA)));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<DeadTree>> DEAD_TREE = regBoss(EntityType.Builder.of(DeadTree::new, MobCategory.MONSTER)
                    .sized(0.9f, 3.5f).eyeHeight(0.9f).spawnDimensionsScale(2).clientTrackingRange(8), RuneCraftory.modRes("dead_tree"),
            0x3e4a40, 0x227904,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 300).putLevelGains(Attributes.MAX_HEALTH, 640)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 17).putLevelGains(Attributes.ATTACK_DAMAGE, 287)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 4).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 246)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 15).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 282)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 3).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 246)
                    .putAttributes(RuneCraftoryAttributes.FIRE_RESISTANCE.asHolder(), -100)
                    .putAttributes(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 50)
                    .putAttributes(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 50)
                    .putAttributes(RuneCraftoryAttributes.WIND_RESISTANCE.asHolder(), -10)
                    .putAttributes(RuneCraftoryAttributes.LOVE_RESISTANCE.asHolder(), 25)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 50)
                    .putAttributes(RuneCraftoryAttributes.DRAIN_RESISTANCE.asHolder(), 20)
                    .putAttributes(RuneCraftoryAttributes.DIZZY_RESISTANCE.asHolder(), 100)
                    .putAttributes(Attributes.KNOCKBACK_RESISTANCE, 1)
                    .putAttributes(RuneCraftoryAttributes.STUN_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.PARALYSIS_RESISTANCE.asHolder(), 90)
                    .putAttributes(RuneCraftoryAttributes.POISON_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.SEAL_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.SLEEP_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.FATIGUE_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.COLD_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.FAINT_RESISTANCE.asHolder(), 100)
                    .xp(70).tamingChance(BOSS_TAMING_CHANCE).setBarnOccupancy(3).setRideable()
                    .withLevelIncrease(1, 3)
                    .withLevelIncrease(2, 1)
                    .withLevelIncrease(7, 3)
                    .withLevelIncrease(15, 5)
                    .setMinLevel(5)
                    .withRideActionCosts(new EntityRideActionCosts.Builder().secondCost(0.5f, true)
                            .thirdCost(0.5f, true)));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Raccoon>> RACCOON = regBoss(EntityType.Builder.of(Raccoon::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 17 / 16d, -5 / 16d)).sized(0.9f, 1.5f).clientTrackingRange(8), RuneCraftory.modRes("raccoon"),
            0xcb8055, 0x6d4342,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 290).putLevelGains(Attributes.MAX_HEALTH, 615)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 17).putLevelGains(Attributes.ATTACK_DAMAGE, 283)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 4.5).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 249)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 14).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 278)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 3).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 255)
                    .putAttributes(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 25)
                    .putAttributes(RuneCraftoryAttributes.WIND_RESISTANCE.asHolder(), 25)
                    .putAttributes(RuneCraftoryAttributes.LOVE_RESISTANCE.asHolder(), -25)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 25)
                    .putAttributes(RuneCraftoryAttributes.DRAIN_RESISTANCE.asHolder(), 10)
                    .putAttributes(RuneCraftoryAttributes.DIZZY_RESISTANCE.asHolder(), 100)
                    .putAttributes(Attributes.KNOCKBACK_RESISTANCE, 1)
                    .putAttributes(RuneCraftoryAttributes.STUN_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.PARALYSIS_RESISTANCE.asHolder(), 90)
                    .putAttributes(RuneCraftoryAttributes.POISON_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.SEAL_RESISTANCE.asHolder(), 80)
                    .putAttributes(RuneCraftoryAttributes.SLEEP_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.FATIGUE_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.COLD_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.FAINT_RESISTANCE.asHolder(), 100)
                    .xp(70).tamingChance(BOSS_TAMING_CHANCE).setBarnOccupancy(2).setRideable()
                    .withLevelIncrease(1, 3)
                    .withLevelIncrease(2, 1)
                    .withLevelIncrease(7, 3)
                    .withLevelIncrease(15, 5)
                    .setMinLevel(5)
                    .withRideActionCosts(new EntityRideActionCosts.Builder().secondCost(10, false)
                            .thirdCost(0.5f, true)));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Skelefang>> SKELEFANG = regBoss(EntityType.Builder.of(Skelefang::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 30 / 16d, -3 / 16d)).sized(1.95f, 3).clientTrackingRange(8), RuneCraftory.modRes("skelefang"),
            0x615237, 0xc2a982,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 225).putLevelGains(Attributes.MAX_HEALTH, 600)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 18).putLevelGains(Attributes.ATTACK_DAMAGE, 294)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 5).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 261)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 16).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 264)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 4).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 237)
                    .putAttributes(RuneCraftoryAttributes.FIRE_RESISTANCE.asHolder(), -25)
                    .putAttributes(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 50)
                    .putAttributes(RuneCraftoryAttributes.DARK_RESISTANCE.asHolder(), 200)
                    .putAttributes(RuneCraftoryAttributes.LIGHT_RESISTANCE.asHolder(), -50)
                    .putAttributes(RuneCraftoryAttributes.LOVE_RESISTANCE.asHolder(), -25)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 25)
                    .putAttributes(RuneCraftoryAttributes.DRAIN_RESISTANCE.asHolder(), 30)
                    .putAttributes(RuneCraftoryAttributes.DIZZY_RESISTANCE.asHolder(), 100)
                    .putAttributes(Attributes.KNOCKBACK_RESISTANCE, 1)
                    .putAttributes(RuneCraftoryAttributes.STUN_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.PARALYSIS_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.POISON_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.SEAL_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.SLEEP_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.FATIGUE_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.COLD_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.FAINT_RESISTANCE.asHolder(), 100)
                    .xp(150).tamingChance(BOSS_TAMING_CHANCE).setBarnOccupancy(3).setRideable()
                    .withLevelIncrease(1, 5)
                    .withLevelIncrease(2, 1)
                    .withLevelIncrease(7, 3)
                    .withLevelIncrease(15, 5)
                    .setMinLevel(12)
                    .withSpawnerPredicate(LibAdvancements.playerAdvancementCheck(LibAdvancements.RACCOON)));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Ambrosia>> AMBROSIA = regBoss(EntityType.Builder.of(Ambrosia::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 29 / 16d, -5 / 16d)).sized(0.85f, 2.3f).clientTrackingRange(8), RuneCraftory.modRes("ambrosia"),
            0x00ff00, 0xe600e6,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 300).putLevelGains(Attributes.MAX_HEALTH, 630)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 16).putLevelGains(Attributes.ATTACK_DAMAGE, 275)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 4).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 230)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 13).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 280)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 4).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 245)
                    .putAttributes(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 25)
                    .putAttributes(RuneCraftoryAttributes.WIND_RESISTANCE.asHolder(), -25)
                    .putAttributes(RuneCraftoryAttributes.LOVE_RESISTANCE.asHolder(), -25)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 25)
                    .putAttributes(RuneCraftoryAttributes.DRAIN_RESISTANCE.asHolder(), 50)
                    .putAttributes(RuneCraftoryAttributes.DIZZY_RESISTANCE.asHolder(), 100)
                    .putAttributes(Attributes.KNOCKBACK_RESISTANCE, 1)
                    .putAttributes(RuneCraftoryAttributes.STUN_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.PARALYSIS_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.POISON_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.SEAL_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.SLEEP_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.FATIGUE_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.COLD_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.FAINT_RESISTANCE.asHolder(), 100)
                    .xp(70).tamingChance(BOSS_TAMING_CHANCE).setBarnOccupancy(2).setRideable().setFlying()
                    .withLevelIncrease(1, 3)
                    .withLevelIncrease(2, 1)
                    .withLevelIncrease(7, 3)
                    .withLevelIncrease(15, 5)
                    .setMinLevel(5)
                    .withRideActionCosts(new EntityRideActionCosts.Builder().secondCost(0.5f, true)
                            .thirdCost(0.5f, true)));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Thunderbolt>> THUNDERBOLT = regBoss(EntityType.Builder.of(Thunderbolt::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 27 / 16d, -4 / 16d)).sized(1.6f, 1.8f).clientTrackingRange(8), RuneCraftory.modRes("thunderbolt"),
            0x212121, 0x2f1177,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 325).putLevelGains(Attributes.MAX_HEALTH, 610)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 19).putLevelGains(Attributes.ATTACK_DAMAGE, 280)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 4).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 240)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 16).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 272)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 3).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 240)
                    .putAttributes(RuneCraftoryAttributes.WIND_RESISTANCE.asHolder(), 50)
                    .putAttributes(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), -25)
                    .putAttributes(RuneCraftoryAttributes.LOVE_RESISTANCE.asHolder(), -25)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 25)
                    .putAttributes(RuneCraftoryAttributes.DRAIN_RESISTANCE.asHolder(), 30)
                    .putAttributes(RuneCraftoryAttributes.DIZZY_RESISTANCE.asHolder(), 100)
                    .putAttributes(Attributes.KNOCKBACK_RESISTANCE, 1)
                    .putAttributes(RuneCraftoryAttributes.STUN_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.PARALYSIS_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.POISON_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.SEAL_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.SLEEP_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.FATIGUE_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.COLD_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.FAINT_RESISTANCE.asHolder(), 100)
                    .xp(150).tamingChance(BOSS_TAMING_CHANCE).setBarnOccupancy(2).setRideable()
                    .withLevelIncrease(1, 5)
                    .withLevelIncrease(2, 1)
                    .withLevelIncrease(7, 3)
                    .withLevelIncrease(15, 5)
                    .setMinLevel(12)
                    .withSpawnerPredicate(LibAdvancements.playerAdvancementCheck(LibAdvancements.AMBROSIA))
                    .withRideActionCosts(new EntityRideActionCosts.Builder()
                            .thirdCost(0.5f, true)));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Marionetta>> MARIONETTA = regBoss(EntityType.Builder.of(Marionetta::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 17.25 / 16d, -6 / 16d)).sized(0.8f, 2.6f).clientTrackingRange(8), RuneCraftory.modRes("marionetta"),
            0xb86b13, 0xd8d7d7,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 320).putLevelGains(Attributes.MAX_HEALTH, 620)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 20).putLevelGains(Attributes.ATTACK_DAMAGE, 286)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 5).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 247)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 17).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 279)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 4).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 247)
                    .putAttributes(RuneCraftoryAttributes.DARK_RESISTANCE.asHolder(), 25)
                    .putAttributes(RuneCraftoryAttributes.LIGHT_RESISTANCE.asHolder(), -25)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 25)
                    .putAttributes(RuneCraftoryAttributes.DRAIN_RESISTANCE.asHolder(), 30)
                    .putAttributes(RuneCraftoryAttributes.DIZZY_RESISTANCE.asHolder(), 100)
                    .putAttributes(Attributes.KNOCKBACK_RESISTANCE, 1)
                    .putAttributes(RuneCraftoryAttributes.STUN_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.PARALYSIS_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.POISON_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.SEAL_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.SLEEP_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.FATIGUE_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.COLD_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.FAINT_RESISTANCE.asHolder(), 100)
                    .xp(250).tamingChance(0.005f).setBarnOccupancy(2).setRideable()
                    .withLevelIncrease(1, 7)
                    .withLevelIncrease(2, 1)
                    .withLevelIncrease(7, 3)
                    .withLevelIncrease(15, 5)
                    .setMinLevel(20)
                    .withSpawnerPredicate(LibAdvancements.playerAdvancementCheck(LibAdvancements.THUNDERBOLT))
                    .withRideActionCosts(new EntityRideActionCosts.Builder()
                            .thirdCost(0.5f, true)));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Handonetta>> HANDONETTA = regBoss(EntityType.Builder.of(Handonetta::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 23.25 / 16d, -6 / 16d)).sized(2.3f, 2.8f).clientTrackingRange(8), RuneCraftory.modRes("handonetta"),
            0xffffff, 0x631123, true,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 300).putLevelGains(Attributes.MAX_HEALTH, 610)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 21).putLevelGains(Attributes.ATTACK_DAMAGE, 290)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 4).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 245)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 17).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 276)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 4).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 245)
                    .putAttributes(RuneCraftoryAttributes.DARK_RESISTANCE.asHolder(), 25)
                    .putAttributes(RuneCraftoryAttributes.LIGHT_RESISTANCE.asHolder(), -25)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 25)
                    .putAttributes(RuneCraftoryAttributes.DRAIN_RESISTANCE.asHolder(), 30)
                    .putAttributes(RuneCraftoryAttributes.DIZZY_RESISTANCE.asHolder(), 100)
                    .putAttributes(Attributes.KNOCKBACK_RESISTANCE, 1)
                    .putAttributes(RuneCraftoryAttributes.STUN_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.PARALYSIS_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.POISON_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.SEAL_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.SLEEP_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.FATIGUE_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.COLD_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.FAINT_RESISTANCE.asHolder(), 100)
                    .xp(250).tamingChance(0.005f).setBarnOccupancy(2).setRideable().setFlying()
                    .withLevelIncrease(1, 7)
                    .withLevelIncrease(2, 1)
                    .withLevelIncrease(7, 3)
                    .withLevelIncrease(15, 5)
                    .setMinLevel(20)
                    .withSpawnerPredicate(LibAdvancements.playerAdvancementCheck(LibAdvancements.THUNDERBOLT))
                    .withRideActionCosts(new EntityRideActionCosts.Builder()
                            .thirdCost(0.5f, true)));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Sano>> SANO = regBoss(EntityType.Builder.of(Sano::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 30.5 / 16d, 8 / 16d)).sized(1.7f, 2.1f).spawnDimensionsScale(2).clientTrackingRange(8), RuneCraftory.modRes("sano"),
            0xa18c4a, 0xa82626,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 275).putLevelGains(Attributes.MAX_HEALTH, 650)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 14).putLevelGains(Attributes.ATTACK_DAMAGE, 260)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 6).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 259)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 17).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 282)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 5).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 249)
                    .putAttributes(RuneCraftoryAttributes.FIRE_RESISTANCE.asHolder(), 50)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 25)
                    .putAttributes(RuneCraftoryAttributes.DRAIN_RESISTANCE.asHolder(), 25)
                    .putAttributes(RuneCraftoryAttributes.DIZZY_RESISTANCE.asHolder(), 100)
                    .putAttributes(Attributes.KNOCKBACK_RESISTANCE, 1)
                    .putAttributes(RuneCraftoryAttributes.STUN_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.PARALYSIS_RESISTANCE.asHolder(), 97)
                    .putAttributes(RuneCraftoryAttributes.POISON_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.SEAL_RESISTANCE.asHolder(), 80)
                    .putAttributes(RuneCraftoryAttributes.SLEEP_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.FATIGUE_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.COLD_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.FAINT_RESISTANCE.asHolder(), 100)
                    .xp(200).setBarnOccupancy(3).setRideable()
                    .withLevelIncrease(1, 6)
                    .withLevelIncrease(2, 1)
                    .withLevelIncrease(7, 3)
                    .withLevelIncrease(15, 5)
                    .setMinLevel(50)
                    .withSpawnerPredicate(LibAdvancements.playerAdvancementCheck(LibAdvancements.MARIONETTA)));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Uno>> UNO = regBoss(EntityType.Builder.of(Uno::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 30.5 / 16d, 8 / 16d)).sized(1.7f, 2.1f).spawnDimensionsScale(2).clientTrackingRange(8), RuneCraftory.modRes("uno"),
            0xa18c4a, 0x1b7f9c,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 275).putLevelGains(Attributes.MAX_HEALTH, 650)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 14).putLevelGains(Attributes.ATTACK_DAMAGE, 260)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 5).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 259)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 17).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 282)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 6).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 249)
                    .putAttributes(RuneCraftoryAttributes.WATER_RESISTANCE.asHolder(), 50)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 25)
                    .putAttributes(RuneCraftoryAttributes.DRAIN_RESISTANCE.asHolder(), 25)
                    .putAttributes(RuneCraftoryAttributes.DIZZY_RESISTANCE.asHolder(), 100)
                    .putAttributes(Attributes.KNOCKBACK_RESISTANCE, 1)
                    .putAttributes(RuneCraftoryAttributes.STUN_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.PARALYSIS_RESISTANCE.asHolder(), 97)
                    .putAttributes(RuneCraftoryAttributes.POISON_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.SEAL_RESISTANCE.asHolder(), 80)
                    .putAttributes(RuneCraftoryAttributes.SLEEP_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.FATIGUE_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.COLD_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.FAINT_RESISTANCE.asHolder(), 100)
                    .xp(200).setBarnOccupancy(3).setRideable()
                    .withLevelIncrease(1, 6)
                    .withLevelIncrease(2, 1)
                    .withLevelIncrease(7, 3)
                    .withLevelIncrease(15, 5)
                    .setMinLevel(50)
                    .withSpawnerPredicate(LibAdvancements.playerAdvancementCheck(LibAdvancements.MARIONETTA)));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<SanoAndUnoDuo>> SANO_AND_UNO = regEnsemble(EntityType.Builder.of(SanoAndUnoDuo::new, MobCategory.MISC).noSummon().noSave().sized(0.01f, 0.01f), RuneCraftory.modRes("sano_and_uno"), 0xa18c4a, 0xa236d9);
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<Sarcophagus>> SARCOPHAGUS = regBoss(EntityType.Builder.of(Sarcophagus::new, MobCategory.MONSTER)
                    .passengerAttachments(new Vec3(0, 29.5 / 16d, -8 / 16d)).sized(1.1f, 3.5f).clientTrackingRange(8), RuneCraftory.modRes("sarcophagus"),
            0x482f27, 0xf3d07f,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 350).putLevelGains(Attributes.MAX_HEALTH, 633)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 17).putLevelGains(Attributes.ATTACK_DAMAGE, 276)
                    .putAttributes(RuneCraftoryAttributes.DEFENCE.asHolder(), 6).putLevelGains(RuneCraftoryAttributes.DEFENCE.asHolder(), 243)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 20).putLevelGains(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 280)
                    .putAttributes(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 6).putLevelGains(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 243)
                    .putAttributes(RuneCraftoryAttributes.LOVE_RESISTANCE.asHolder(), -25)
                    .putAttributes(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 25)
                    .putAttributes(RuneCraftoryAttributes.DRAIN_RESISTANCE.asHolder(), 25)
                    .putAttributes(RuneCraftoryAttributes.DIZZY_RESISTANCE.asHolder(), 100)
                    .putAttributes(Attributes.KNOCKBACK_RESISTANCE, 1)
                    .putAttributes(RuneCraftoryAttributes.STUN_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.PARALYSIS_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.POISON_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.SEAL_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.SLEEP_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.FATIGUE_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.COLD_RESISTANCE.asHolder(), 100)
                    .putAttributes(RuneCraftoryAttributes.FAINT_RESISTANCE.asHolder(), 100)
                    .xp(450).tamingChance(BOSS_TAMING_CHANCE).setBarnOccupancy(2).setRideable()
                    .withLevelIncrease(1, 12)
                    .withLevelIncrease(2, 1)
                    .withLevelIncrease(7, 3)
                    .withLevelIncrease(15, 5)
                    .setMinLevel(70)
                    .withSpawnerPredicate(LibAdvancements.playerAdvancementCheck(LibAdvancements.MARIONETTA)));

    public static final RegistryEntrySupplier<EntityType<?>, EntityType<NPCEntity>> NPC = npc(EntityType.Builder.of(NPCEntity::new, MobCategory.MISC).sized(0.6f, 1.8f).clientTrackingRange(8), RuneCraftory.modRes("npc"));

    public static final RegistryEntrySupplier<EntityType<?>, EntityType<TreasureChestEntity>> TREASURE_CHEST = treasureChest(EntityType.Builder.of(TreasureChestEntity::new, MobCategory.MISC).sized(1, 1).clientTrackingRange(4), RuneCraftory.modRes("treasure_chest"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<MobArrowEntity>> ARROW = reg(EntityType.Builder.<MobArrowEntity>of(MobArrowEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20), RuneCraftory.modRes("arrow"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<SporeEntity>> SPORE = reg(EntityType.Builder.<SporeEntity>of(SporeEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(4), RuneCraftory.modRes("spore"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<WindGustEntity>> GUST = reg(EntityType.Builder.<WindGustEntity>of(WindGustEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(4), RuneCraftory.modRes("gust"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<StoneEntity>> STONE = reg(EntityType.Builder.<StoneEntity>of(StoneEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("stone"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<StatusBallEntity>> STATUS_BALL = reg(EntityType.Builder.<StatusBallEntity>of(StatusBallEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(4), RuneCraftory.modRes("status_ball"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<AmbrosiaWaveEntity>> AMBROSIA_WAVE = reg(EntityType.Builder.<AmbrosiaWaveEntity>of(AmbrosiaWaveEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4), RuneCraftory.modRes("ambrosia_wave"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<ButterflyEntity>> BUTTERFLY = reg(EntityType.Builder.<ButterflyEntity>of(ButterflyEntity::new, MobCategory.MISC).sized(0.2f, 0.2f).clientTrackingRange(4), RuneCraftory.modRes("butterfly"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<PollenPuffEntity>> POLLEN_PUFF = reg(EntityType.Builder.<PollenPuffEntity>of(PollenPuffEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("pollen_puff"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<PollenEntity>> POLLEN = reg(EntityType.Builder.<PollenEntity>of(PollenEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4), RuneCraftory.modRes("pollen"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<ThiccLightningBoltEntity>> LIGHTNING_ORB_BOLT = reg(EntityType.Builder.<ThiccLightningBoltEntity>of(ThiccLightningBoltEntity::new, MobCategory.MISC).sized(0.8f, 0.8f).clientTrackingRange(4), RuneCraftory.modRes("lightning_orb_bolt"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<ThunderboltBeamEntity>> LIGHTNING_BEAM = reg(EntityType.Builder.<ThunderboltBeamEntity>of(ThunderboltBeamEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("lightning_beam"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<ElementalTrailEntity>> ELEMENTAL_TRAIL = reg(EntityType.Builder.<ElementalTrailEntity>of(ElementalTrailEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("elemental_trail"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<SpiderWebEntity>> SPIDER_WEB = reg(EntityType.Builder.<SpiderWebEntity>of(SpiderWebEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(4), RuneCraftory.modRes("spider_web"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<DarkBeamEntity>> DARK_BEAM = reg(EntityType.Builder.<DarkBeamEntity>of(DarkBeamEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4), RuneCraftory.modRes("dark_beam"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<CardsEntity>> CARDS = reg(EntityType.Builder.<CardsEntity>of(CardsEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(4), RuneCraftory.modRes("cards"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<FurnitureEntity>> FURNITURE = reg(EntityType.Builder.<FurnitureEntity>of(FurnitureEntity::new, MobCategory.MISC).sized(1f, 1f).clientTrackingRange(4), RuneCraftory.modRes("furniture"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<MarionettaTrapEntity>> TRAP_CHEST = reg(EntityType.Builder.<MarionettaTrapEntity>of(MarionettaTrapEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(4), RuneCraftory.modRes("trap_chest"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<ElementalBallEntity>> ELEMENTAL_BALL = reg(EntityType.Builder.<ElementalBallEntity>of(ElementalBallEntity::new, MobCategory.MISC).sized(0.2f, 0.2f).clientTrackingRange(4), RuneCraftory.modRes("elemental_ball"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<FireballEntity>> FIRE_BALL = reg(EntityType.Builder.<FireballEntity>of(FireballEntity::new, MobCategory.MISC).sized(0.2f, 0.2f).clientTrackingRange(4), RuneCraftory.modRes("fireball"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<ExplosionSpellEntity>> EXPLOSION = reg(EntityType.Builder.<ExplosionSpellEntity>of(ExplosionSpellEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("explosion"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<WaterLaserEntity>> WATER_LASER = reg(EntityType.Builder.<WaterLaserEntity>of(WaterLaserEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("water_laser"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<SwipingWaterLaserEntity>> SWIPING_WATER_LASER = reg(EntityType.Builder.<SwipingWaterLaserEntity>of(SwipingWaterLaserEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("swiping_water_laser"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<RockSpearEntity>> ROCK_SPEAR = reg(EntityType.Builder.<RockSpearEntity>of(RockSpearEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4), RuneCraftory.modRes("rock_spear"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<WindBladeEntity>> WIND_BLADE = reg(EntityType.Builder.<WindBladeEntity>of(WindBladeEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("wind_blade"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<LightBallEntity>> LIGHT_BALL = reg(EntityType.Builder.<LightBallEntity>of(LightBallEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(4), RuneCraftory.modRes("light_ball"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<DarkBallEntity>> DARK_BALL = reg(EntityType.Builder.<DarkBallEntity>of(DarkBallEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("dark_ball"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<DarknessEntity>> DARKNESS = reg(EntityType.Builder.<DarknessEntity>of(DarknessEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4), RuneCraftory.modRes("darkness"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<BigPlateEntity>> BIG_PLATE = reg(EntityType.Builder.<BigPlateEntity>of(BigPlateEntity::new, MobCategory.MISC).sized(1.5f, 0.3f).clientTrackingRange(4), RuneCraftory.modRes("big_plate"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<DarkBulletEntity>> DARK_BULLET = reg(EntityType.Builder.<DarkBulletEntity>of(DarkBulletEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("dark_bullet"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<PoisonNeedleEntity>> POISON_NEEDLE = reg(EntityType.Builder.<PoisonNeedleEntity>of(PoisonNeedleEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("poison_needle"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<SleepAuraEntity>> SLEEP_AURA = reg(EntityType.Builder.<SleepAuraEntity>of(SleepAuraEntity::new, MobCategory.MISC).sized(1.5f, 1).clientTrackingRange(4), RuneCraftory.modRes("sleep_aura"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<BulletEntity>> CIRCLING_BULLET = reg(EntityType.Builder.<BulletEntity>of(BulletEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("circling_bullet"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<ThrownItemEntity>> THROWN_ITEM = reg(EntityType.Builder.<ThrownItemEntity>of(ThrownItemEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("thrown_item"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<AppleProjectileEntity>> APPLE = reg(EntityType.Builder.<AppleProjectileEntity>of(AppleProjectileEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("apple"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<SlashResidueEntity>> SLASH_RESIDUE = reg(EntityType.Builder.<SlashResidueEntity>of(SlashResidueEntity::new, MobCategory.MISC).sized(1, 1).clientTrackingRange(4), RuneCraftory.modRes("slash_residue"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<SmallRaccoonLeafEntity>> SMALL_RACCOON_LEAF = reg(EntityType.Builder.<SmallRaccoonLeafEntity>of(SmallRaccoonLeafEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("small_raccoon_leaf"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<BigRaccoonLeafEntity>> BIG_RACCOON_LEAF = reg(EntityType.Builder.<BigRaccoonLeafEntity>of(BigRaccoonLeafEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(4), RuneCraftory.modRes("big_raccoon_leaf"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<BoneNeedleEntity>> BONE_NEEDLE = reg(EntityType.Builder.<BoneNeedleEntity>of(BoneNeedleEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("bone_needle"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<HomingEnergyOrbEntity>> ENERGY_ORB = reg(EntityType.Builder.<HomingEnergyOrbEntity>of(HomingEnergyOrbEntity::new, MobCategory.MISC).sized(0.9f, 0.9f).clientTrackingRange(4), RuneCraftory.modRes("energy_orb"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<SpikeEntity>> HOMING_SPIKES = reg(EntityType.Builder.<SpikeEntity>of(SpikeEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("homing_spikes"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<PowerWaveEntity>> POWER_WAVE = reg(EntityType.Builder.<PowerWaveEntity>of(PowerWaveEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("power_wave"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<GustRocksEntity>> GUST_ROCK = reg(EntityType.Builder.<GustRocksEntity>of(GustRocksEntity::new, MobCategory.MISC).sized(0.01f, 0.01f).clientTrackingRange(4), RuneCraftory.modRes("gust_rocks"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<TornadoEntity>> TORNADO = reg(EntityType.Builder.<TornadoEntity>of(TornadoEntity::new, MobCategory.MISC).sized(1.5f, 4.5f).clientTrackingRange(4), RuneCraftory.modRes("tornado"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<LightBeamEntity>> LIGHT_BEAM = reg(EntityType.Builder.<LightBeamEntity>of(LightBeamEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4), RuneCraftory.modRes("light_beam"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<MissileEntity>> MISSILE = reg(EntityType.Builder.<MissileEntity>of(MissileEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("missile"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<StarfallEntity>> STARFALL = reg(EntityType.Builder.<StarfallEntity>of(StarfallEntity::new, MobCategory.MISC).sized(0.35f, 0.35f).clientTrackingRange(4), RuneCraftory.modRes("star_fall"));

    public static final RegistryEntrySupplier<EntityType<?>, EntityType<RuneyEntity>> RUNEY = reg(EntityType.Builder.of(RuneyEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("runey"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<RuneOrbEntity>> STAT_BONUS = reg(EntityType.Builder.of(RuneOrbEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("rune_orb"));

    public static final RegistryEntrySupplier<EntityType<?>, EntityType<SporeCircleSummoner>> SPORE_CIRCLE_SUMMONER = reg(EntityType.Builder.<SporeCircleSummoner>of(SporeCircleSummoner::new, MobCategory.MISC).sized(0.01f, 0.01f).noSummon().clientTrackingRange(4), RuneCraftory.modRes("spore_circle_summoner"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<ButterflySummonerEntity>> BUTTERFLY_SUMMONER = reg(EntityType.Builder.<ButterflySummonerEntity>of(ButterflySummonerEntity::new, MobCategory.MISC).sized(0.01f, 0.01f).noSummon().clientTrackingRange(4), RuneCraftory.modRes("butterfly_summoner"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<DarkBulletSummonerEntity>> DARK_BULLET_SUMMONER = reg(EntityType.Builder.<DarkBulletSummonerEntity>of(DarkBulletSummonerEntity::new, MobCategory.MISC).sized(0.01f, 0.01f).noSummon().clientTrackingRange(4), RuneCraftory.modRes("dark_bullet_summoner"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<ElementBallBarrageSummoner>> ELEMENTAL_BARRAGE_SUMMONER = reg(EntityType.Builder.<ElementBallBarrageSummoner>of(ElementBallBarrageSummoner::new, MobCategory.MISC).sized(0.01f, 0.01f).noSummon().clientTrackingRange(4), RuneCraftory.modRes("elemental_barrage_summoner"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<RootSpikeSummoner>> ROOT_SPIKE_SUMMONER = reg(EntityType.Builder.<RootSpikeSummoner>of(RootSpikeSummoner::new, MobCategory.MISC).sized(0.01f, 0.01f).noSummon().clientTrackingRange(4), RuneCraftory.modRes("root_spike_summoner"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<RafflesiaBreathSummoner>> RAFFLESIA_BREATH_SUMMONER = reg(EntityType.Builder.<RafflesiaBreathSummoner>of(RafflesiaBreathSummoner::new, MobCategory.MISC).sized(0.01f, 0.01f).noSummon().clientTrackingRange(4), RuneCraftory.modRes("rafflesia_breath_summoner"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<RafflesiaCircleSummoner>> RAFFLESIA_CIRCLE_SUMMONER = reg(EntityType.Builder.<RafflesiaCircleSummoner>of(RafflesiaCircleSummoner::new, MobCategory.MISC).sized(0.01f, 0.01f).noSummon().clientTrackingRange(4), RuneCraftory.modRes("rafflesia_circle_summoner"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<BlazeBarrageSummoner>> BLAZE_BARRAGE = reg(EntityType.Builder.<BlazeBarrageSummoner>of(BlazeBarrageSummoner::new, MobCategory.MISC).sized(0.01f, 0.01f).noSummon().clientTrackingRange(4), RuneCraftory.modRes("blaze_barrage"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<WindBladeBarrageSummoner>> WIND_BLADE_BARRAGE_SUMMONER = reg(EntityType.Builder.<WindBladeBarrageSummoner>of(WindBladeBarrageSummoner::new, MobCategory.MISC).sized(0.01f, 0.01f).noSummon().clientTrackingRange(4), RuneCraftory.modRes("wind_blade_barrage_summoner"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<FireWallSummoner>> FIRE_WALL_SUMMONER = reg(EntityType.Builder.<FireWallSummoner>of(FireWallSummoner::new, MobCategory.MISC).sized(0.01f, 0.01f).noSummon().clientTrackingRange(4), RuneCraftory.modRes("fire_wall_summoner"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<ElementalCircleSummoner>> ELEMENTAL_CIRCLE_SUMMONER = reg(EntityType.Builder.<ElementalCircleSummoner>of(ElementalCircleSummoner::new, MobCategory.MISC).sized(0.01f, 0.01f).noSummon().clientTrackingRange(4), RuneCraftory.modRes("elemental_circle_summoner"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<StarFallSummoner>> STARFALL_SUMMONER = reg(EntityType.Builder.<StarFallSummoner>of(StarFallSummoner::new, MobCategory.MISC).sized(0.01f, 0.01f).noSummon().clientTrackingRange(4), RuneCraftory.modRes("star_fall_summoner"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<GroundShakeParticleSpawner>> GROUND_SHAKE_PARTICLES = reg(EntityType.Builder.<GroundShakeParticleSpawner>of(GroundShakeParticleSpawner::new, MobCategory.MISC).sized(0.01f, 0.01f).noSummon().clientTrackingRange(4), RuneCraftory.modRes("ground_shake_particles"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<AppleRainSummoner>> APPLE_RAIN_SUMMONER = reg(EntityType.Builder.<AppleRainSummoner>of(AppleRainSummoner::new, MobCategory.MISC).sized(0.01f, 0.01f).noSummon().clientTrackingRange(4), RuneCraftory.modRes("apple_rain_summoner"));

    public static final RegistryEntrySupplier<EntityType<?>, EntityType<SarcophagusTeleporter>> SARCOPHAGUS_TELEPORTER = reg(EntityType.Builder.of(SarcophagusTeleporter::new, MobCategory.MISC).sized(1f, 1f).clientTrackingRange(4), RuneCraftory.modRes("sarcophagus_teleporter"));

    public static final RegistryEntrySupplier<EntityType<?>, EntityType<CustomFishingHookEntity>> FISHING_HOOK = reg(EntityType.Builder.<CustomFishingHookEntity>of(CustomFishingHookEntity::new, MobCategory.MISC).noSave().noSummon().sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(5), RuneCraftory.modRes("fishing_hook"));

    public static final RegistryEntrySupplier<EntityType<?>, EntityType<MultiPartEntity>> MULTIPART = reg(EntityType.Builder.<MultiPartEntity>of(MultiPartEntity::new, MobCategory.MISC).noSave().noSummon().sized(0.25F, 0.25F), RuneCraftory.modRes("multipart_entity"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<RafflesiaHorseTail>> RAFFLESIA_HORSETAIL = reg(EntityType.Builder.<RafflesiaHorseTail>of(RafflesiaHorseTail::new, MobCategory.MISC).noSummon().sized(0.5F, 2.1F), RuneCraftory.modRes("rafflesia_horse_tail"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<RafflesiaFlower>> RAFFLESIA_FLOWER = reg(EntityType.Builder.<RafflesiaFlower>of(RafflesiaFlower::new, MobCategory.MISC).noSummon().sized(0.5F, 1.2F), RuneCraftory.modRes("rafflesia_flower"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<RafflesiaPitcher>> RAFFLESIA_PITCHER = reg(EntityType.Builder.<RafflesiaPitcher>of(RafflesiaPitcher::new, MobCategory.MISC).noSummon().sized(0.5F, 1.8F), RuneCraftory.modRes("rafflesia_pitcher"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<HoeTillableItemEntity>> HOE_TILLABLE_ITEM_ENTITY = reg(EntityType.Builder.<HoeTillableItemEntity>of(HoeTillableItemEntity::new, MobCategory.MISC).noSummon().sized(0.4F, 0.4F).eyeHeight(0.4f * 0.85f).clientTrackingRange(6).updateInterval(20), RuneCraftory.modRes("hoe_tillable_item_entity"));

    public static List<RegistryEntrySupplier<EntityType<?>, EntityType<?>>> getMonsters() {
        return ImmutableList.copyOf(MONSTERS);
    }

    public static List<RegistryEntrySupplier<EntityType<?>, EntityType<?>>> getBosses() {
        return ImmutableList.copyOf(BOSSES);
    }

    public static Map<ResourceLocation, GateSpawnData> getDefaultGateSpawns() {
        return ImmutableMap.copyOf(DEFAULT_SPAWN_DATA);
    }

    public static Map<ResourceLocation, EntityProperties.Builder> getDefaultMobProperties() {
        return ImmutableMap.copyOf(DEFAULT_MOB_PROPERTIES);
    }

    @SuppressWarnings("unchecked")
    public static void registerAttributes(BiConsumer<EntityType<? extends LivingEntity>, AttributeSupplier.Builder> cons) {
        cons.accept(GATE.get(), GateEntity.createAttributes());
        for (RegistryEntrySupplier<EntityType<?>, EntityType<?>> reg : MONSTERS) {
            EntityType<? extends LivingEntity> type = (EntityType<? extends LivingEntity>) reg.get();
            if (FLYING_MONSTERS.contains(reg))
                cons.accept(type, BaseMonster.createAttributes().add(Attributes.FLYING_SPEED));
            else
                cons.accept(type, BaseMonster.createAttributes());
        }

        cons.accept(NPC.get(), NPCEntity.createAttributes());

        cons.accept(RAFFLESIA_HORSETAIL.get(), RafflesiaPart.createAttributes());
        cons.accept(RAFFLESIA_FLOWER.get(), RafflesiaPart.createAttributes());
        cons.accept(RAFFLESIA_PITCHER.get(), RafflesiaPart.createAttributes());
    }

    public static <V extends Entity> RegistryEntrySupplier<EntityType<?>, EntityType<V>> reg(EntityType.Builder<V> v, ResourceLocation name) {
        return ENTITIES.register(name.getPath(), () -> v.build(name.getPath()));
    }

    public static <V extends Entity> RegistryEntrySupplier<EntityType<?>, EntityType<V>> regEnsemble(EntityType.Builder<V> v, ResourceLocation name, int primary, int secondary) {
        RegistryEntrySupplier<EntityType<?>, EntityType<V>> reg = reg(v, name);
        RuneCraftoryItems.register(name.getPath() + "_spawn_egg", () -> new EnsembleEggItem(reg, primary, secondary, new Item.Properties().rarity(Rarity.RARE)), RuneCraftoryCreativeTabs.MONSTERS);
        return reg;
    }

    public static <V extends Mob> RegistryEntrySupplier<EntityType<?>, EntityType<V>> regWithEgg(EntityType.Builder<V> v, ResourceLocation name, int primary, int secondary, @Nullable Consumer<Item.Properties> props) {
        RegistryEntrySupplier<EntityType<?>, EntityType<V>> reg = reg(v, name);
        RuneCraftoryItems.register(name.getPath() + "_spawn_egg", () -> {
            Item.Properties properties = new Item.Properties();
            if (props != null) {
                props.accept(properties);
            }
            return new RuneCraftoryEggItem(reg, primary, secondary, properties);
        }, RuneCraftoryCreativeTabs.MONSTERS);
        return reg;
    }

    public static <V extends Entity> RegistryEntrySupplier<EntityType<?>, EntityType<V>> treasureChest(EntityType.Builder<V> v, ResourceLocation name) {
        RegistryEntrySupplier<EntityType<?>, EntityType<V>> reg = reg(v, name);
        RuneCraftoryItems.register(name.getPath() + "_spawn_egg", () -> new TreasureChestSpawnegg(reg, new Item.Properties()), RuneCraftoryCreativeTabs.MONSTERS);
        return reg;
    }

    public static <V extends Mob> RegistryEntrySupplier<EntityType<?>, EntityType<V>> npc(EntityType.Builder<V> v, ResourceLocation name) {
        RegistryEntrySupplier<EntityType<?>, EntityType<V>> reg = reg(v, name);
        RuneCraftoryItems.register(name.getPath() + "_spawn_egg", () -> new NPCSpawnEgg(reg, new Item.Properties()), RuneCraftoryCreativeTabs.MONSTERS);
        return reg;
    }

    public static <V extends BaseMonster> RegistryEntrySupplier<EntityType<?>, EntityType<V>> regBoss(EntityType.Builder<V> v, ResourceLocation name, int primary, int secondary, EntityProperties.Builder props) {
        return regBoss(v, name, primary, secondary, false, props);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <V extends BaseMonster> RegistryEntrySupplier<EntityType<?>, EntityType<V>> regBoss(EntityType.Builder<V> v, ResourceLocation name, int primary, int secondary, boolean flying, EntityProperties.Builder props) {
        RegistryEntrySupplier<EntityType<?>, EntityType<V>> sup = regMonster(v, name, primary, secondary, flying, props, prop -> prop.rarity(Rarity.RARE));
        if (TenshiLibCrossPlat.INSTANCE.isDatagen())
            BOSSES.add((RegistryEntrySupplier) sup);
        return sup;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <V extends BaseMonster> RegistryEntrySupplier<EntityType<?>, EntityType<V>> regMonster(EntityType.Builder<V> v, ResourceLocation name, int primary, int secondary, boolean flying, EntityProperties.Builder props, @Nullable Consumer<Item.Properties> itemProps) {
        RegistryEntrySupplier<EntityType<?>, EntityType<V>> sup = regWithEgg(v, name, primary, secondary, itemProps);
        MONSTERS.add((RegistryEntrySupplier) sup);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen())
            DEFAULT_MOB_PROPERTIES.put(name, props);
        if (flying)
            FLYING_MONSTERS.add((RegistryEntrySupplier) sup);
        return sup;
    }

    public static <V extends BaseMonster> RegistryEntrySupplier<EntityType<?>, EntityType<V>> regMonster(EntityType.Builder<V> v, ResourceLocation name, int primary, int secondary, EntityProperties.Builder props, GateSpawnData.Builder builder) {
        return regMonster(v, name, primary, secondary, false, props, builder);
    }

    public static <V extends BaseMonster> RegistryEntrySupplier<EntityType<?>, EntityType<V>> regMonster(EntityType.Builder<V> v, ResourceLocation name, int primary, int secondary, boolean flying, EntityProperties.Builder props, GateSpawnData.Builder builder) {
        if (TenshiLibCrossPlat.INSTANCE.isDatagen())
            DEFAULT_SPAWN_DATA.put(name, builder.build(name));
        return regMonster(v, name, primary, secondary, flying, props, (Consumer<Item.Properties>) null);
    }
}
