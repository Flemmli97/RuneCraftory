package io.github.flemmli97.runecraftory.common.registry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.GateSpawnData;
import io.github.flemmli97.runecraftory.common.RFCreativeTabs;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.config.values.EntityProperties;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.GateEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.ElementBallBarrageSummoner;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityAmbrosiaWave;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityAppleProjectile;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityBaseSpellBall;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityBigPlate;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityBullet;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityButterfly;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityButterflySummoner;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityCards;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityCustomFishingHook;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityDarkBall;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityDarkBeam;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityDarkBullet;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityDarkBulletSummoner;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityDarkness;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityExplosionSpell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityFireball;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityFurniture;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityLightBall;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityMarionettaTrap;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityMobArrow;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityPoisonNeedle;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityPollen;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityPollenPuff;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityRockSpear;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityRuneOrb;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityRuney;
import io.github.flemmli97.runecraftory.common.entities.misc.EntitySlashResidue;
import io.github.flemmli97.runecraftory.common.entities.misc.EntitySleepAura;
import io.github.flemmli97.runecraftory.common.entities.misc.EntitySpiderWeb;
import io.github.flemmli97.runecraftory.common.entities.misc.EntitySpore;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityStatusBall;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityStone;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityThiccLightningBolt;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityThrownItem;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityThunderboltBeam;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityTreasureChest;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWaterLaser;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWindBlade;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWindGust;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWispFlame;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityAggressiveWooly;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityAnt;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityBeetle;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityBigAnt;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityBigMuck;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityBuffamoo;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityChipsqueek;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityCluckadoodle;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityDuck;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityFairy;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityFlowerLily;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityFlowerLion;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityGhost;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityGhostRay;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityGoblin;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityGoblinArcher;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityGoblinGangster;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityGoblinPirate;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityHornet;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityLeafBall;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityMimic;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityOrc;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityOrcArcher;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityOrcHunter;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityPalmCat;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityPanther;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityPommePomme;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityScorpion;
import io.github.flemmli97.runecraftory.common.entities.monster.EntitySkyFish;
import io.github.flemmli97.runecraftory.common.entities.monster.EntitySpider;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityTortas;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityTrickyMuck;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityTroll;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityVeggieGhost;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityWeagle;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityWolf;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityWooly;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityAmbrosia;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityChimera;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityDeadTree;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityMarionetta;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityThunderbolt;
import io.github.flemmli97.runecraftory.common.entities.monster.wisp.EntityIgnis;
import io.github.flemmli97.runecraftory.common.entities.monster.wisp.EntitySpirit;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.items.NPCSpawnEgg;
import io.github.flemmli97.runecraftory.common.items.RuneCraftoryEggItem;
import io.github.flemmli97.runecraftory.common.items.TreasureChestSpawnegg;
import io.github.flemmli97.runecraftory.common.lib.LibAttributes;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class ModEntities {

    public static final PlatformRegistry<EntityType<?>> ENTITIES = PlatformUtils.INSTANCE.of(Registry.ENTITY_TYPE_REGISTRY, RuneCraftory.MODID);
    public static final RegistryEntrySupplier<EntityType<GateEntity>> GATE = reg(EntityType.Builder.of(GateEntity::new, MobCategory.MONSTER).sized(0.9f, 0.9f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "gate"));

    private static final List<RegistryEntrySupplier<EntityType<?>>> MONSTERS = new ArrayList<>();
    /**
     * Used for registering attributes
     */
    private static final List<RegistryEntrySupplier<EntityType<?>>> FLYING_MONSTERS = new ArrayList<>();

    private static final Map<ResourceLocation, GateSpawnData> DEFAULT_SPAWN_DATA = new HashMap<>();

    public static final RegistryEntrySupplier<EntityType<EntityWooly>> WOOLY = regMonster(EntityType.Builder.of(EntityWooly::new, MobCategory.MONSTER).sized(0.7f, 1.55f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "wooly"),
            0xffffcc, 0xffffff,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 14).putLevelGains(LibAttributes.MAX_HEALTH, 10)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 3).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.6)
                    .putAttributes(LibAttributes.MAGIC, 2).putLevelGains(LibAttributes.MAGIC, 1.2)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.3)
                    .xp(5).money(3).tamingChance(0.15f).setRidable().build(),
            new GateSpawnData.Builder(0, 0).addToBiomeTag(60, ModTags.IS_PLAINS, ModTags.IS_BEACH, BiomeTags.IS_FOREST, BiomeTags.IS_HILL,
                    ModTags.IS_MAGICAL).addToBiomeTag(44, ModTags.IS_SAVANNA, BiomeTags.IS_MOUNTAIN));
    public static final RegistryEntrySupplier<EntityType<EntityOrc>> ORC = regMonster(EntityType.Builder.of(EntityOrc::new, MobCategory.MONSTER).sized(0.73f, 2.3f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "orc"),
            0x663300, 0xffbf80,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 18).putLevelGains(LibAttributes.MAX_HEALTH, 10.5)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 4.5).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.3)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.45)
                    .putAttributes(LibAttributes.MAGIC, 3).putLevelGains(LibAttributes.MAGIC, 0.9)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.2)
                    .xp(10).money(4).tamingChance(0.15f).setRidable().build(),
            new GateSpawnData.Builder(0, 0).addToBiomeTag(50, ModTags.IS_PLAINS, ModTags.IS_BEACH, BiomeTags.IS_FOREST, BiomeTags.IS_HILL,
                    ModTags.IS_MAGICAL).addToBiomeTag(44, ModTags.IS_SAVANNA, BiomeTags.IS_MOUNTAIN));
    public static final RegistryEntrySupplier<EntityType<EntityOrcArcher>> ORC_ARCHER = regMonster(EntityType.Builder.of(EntityOrcArcher::new, MobCategory.MONSTER).sized(0.73f, 2.3f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "orc_archer"),
            0x663300, 0xffbf80,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 18).putLevelGains(LibAttributes.MAX_HEALTH, 10.5)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 6).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.2)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.3)
                    .putAttributes(LibAttributes.MAGIC, 3).putLevelGains(LibAttributes.MAGIC, 1)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.2)
                    .xp(10).money(4).tamingChance(0.15f).setRidable().build(),
            new GateSpawnData.Builder(0, 0).addToBiomeTag(50, ModTags.IS_PLAINS, ModTags.IS_BEACH, BiomeTags.IS_FOREST, BiomeTags.IS_HILL,
                    ModTags.IS_MAGICAL, BiomeTags.IS_MOUNTAIN, ModTags.IS_SAVANNA));
    public static final RegistryEntrySupplier<EntityType<EntityAnt>> ANT = regMonster(EntityType.Builder.of(EntityAnt::new, MobCategory.MONSTER).sized(1.1f, 0.44f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "ant"),
            0x800000, 0x1a0000,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 18).putLevelGains(LibAttributes.MAX_HEALTH, 13)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 6).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.1)
                    .putAttributes(LibAttributes.DEFENCE, 1).putLevelGains(LibAttributes.DEFENCE, 1.35)
                    .putAttributes(LibAttributes.MAGIC, 4).putLevelGains(LibAttributes.MAGIC, 1)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.2)
                    .xp(10).money(5).tamingChance(0.1f).build(),
            new GateSpawnData.Builder(0, 0).addToBiomeTag(50, ModTags.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, ModTags.IS_LUSH, ModTags.IS_SAVANNA));
    public static final RegistryEntrySupplier<EntityType<EntityBeetle>> BEETLE = regMonster(EntityType.Builder.of(EntityBeetle::new, MobCategory.MONSTER).sized(0.7f, 1.7f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "beetle"),
            0x9c6a43, 0x244a69,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 24).putLevelGains(LibAttributes.MAX_HEALTH, 10.5)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 7).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.5)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.22)
                    .putAttributes(LibAttributes.MAGIC, 4).putLevelGains(LibAttributes.MAGIC, 1.2)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.25)
                    .xp(15).money(3).tamingChance(0.05f).setRidable().setFlying().build(),
            new GateSpawnData.Builder(0, 0).addToBiomeTag(50, ModTags.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, ModTags.IS_LUSH));
    public static final RegistryEntrySupplier<EntityType<EntityBigMuck>> BIG_MUCK = regMonster(EntityType.Builder.of(EntityBigMuck::new, MobCategory.MONSTER).sized(0.9f, 1.6f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "big_muck"),
            0xd7ce4a, 0xad5c25,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 19).putLevelGains(LibAttributes.MAX_HEALTH, 9.5)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 3).putLevelGains(LibAttributes.ATTACK_DAMAGE, 1.3)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.2)
                    .putAttributes(LibAttributes.MAGIC, 7).putLevelGains(LibAttributes.MAGIC, 2.4)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 1).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.4)
                    .xp(20).money(3).tamingChance(0.05f).setRidable().build(),
            new GateSpawnData.Builder(0, 0).addToBiomeTag(40, ModTags.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, ModTags.IS_LUSH, ModTags.IS_MAGICAL, ModTags.IS_MUSHROOM));
    public static final RegistryEntrySupplier<EntityType<EntityBuffamoo>> BUFFAMOO = regMonster(EntityType.Builder.of(EntityBuffamoo::new, MobCategory.MONSTER).sized(1.2f, 1.45f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "buffamoo"),
            0xd8d8d0, 0x4e4e4c,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 15).putLevelGains(LibAttributes.MAX_HEALTH, 11)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 5).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.8)
                    .putAttributes(LibAttributes.MAGIC, 4).putLevelGains(LibAttributes.MAGIC, 1.5)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.4)
                    .xp(20).money(5).tamingChance(0.15f).setRidable().build(),
            new GateSpawnData.Builder(0, 0).addToBiomeTag(50, ModTags.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, BiomeTags.IS_MOUNTAIN, ModTags.IS_LUSH));
    public static final RegistryEntrySupplier<EntityType<EntityChipsqueek>> CHIPSQUEEK = regMonster(EntityType.Builder.of(EntityChipsqueek::new, MobCategory.MONSTER).sized(0.65f, 0.95f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "chipsqueek"),
            0xff3b5b, 0xf9ffbb,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 18).putLevelGains(LibAttributes.MAX_HEALTH, 9)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 5).putLevelGains(LibAttributes.ATTACK_DAMAGE, 1.25)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.2)
                    .putAttributes(LibAttributes.MAGIC, 5).putLevelGains(LibAttributes.MAGIC, 1.25)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.2)
                    .xp(15).money(1).tamingChance(0.15f).build(),
            new GateSpawnData.Builder(0, 0).addToBiomeTag(40, ModTags.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, BiomeTags.IS_MOUNTAIN, ModTags.IS_LUSH, ModTags.IS_SAVANNA));
    public static final RegistryEntrySupplier<EntityType<EntityCluckadoodle>> CLUCKADOODLE = regMonster(EntityType.Builder.of(EntityCluckadoodle::new, MobCategory.MONSTER).sized(0.6f, 1.1f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "cluckadoodle"),
            0xc2c2c2, 0xdc2121,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 16).putLevelGains(LibAttributes.MAX_HEALTH, 9)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 5).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.1)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 0.8)
                    .putAttributes(LibAttributes.MAGIC, 3).putLevelGains(LibAttributes.MAGIC, 1)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 0.7)
                    .xp(20).money(2).tamingChance(0.15f).build(),
            new GateSpawnData.Builder(0, 0).addToBiomeTag(50, ModTags.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, BiomeTags.IS_MOUNTAIN));
    public static final RegistryEntrySupplier<EntityType<EntityPommePomme>> POMME_POMME = regMonster(EntityType.Builder.of(EntityPommePomme::new, MobCategory.MONSTER).sized(1.0f, 1.6f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "pomme_pomme"),
            0xff1c2b, 0xf7b4b8,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 22).putLevelGains(LibAttributes.MAX_HEALTH, 11)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 4).putLevelGains(LibAttributes.ATTACK_DAMAGE, 1.4)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.9)
                    .putAttributes(LibAttributes.MAGIC, 2).putLevelGains(LibAttributes.MAGIC, 1.2)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.2)
                    .xp(30).money(4).tamingChance(0.1f).setRidable().build(),
            new GateSpawnData.Builder(0, 0).addToBiomeTag(50, ModTags.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, ModTags.IS_MAGICAL));
    public static final RegistryEntrySupplier<EntityType<EntityTortas>> TORTAS = regMonster(EntityType.Builder.of(EntityTortas::new, MobCategory.MONSTER).sized(1.4f, 0.70f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "tortas"),
            0x5c6682, 0xa5848c,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 27).putLevelGains(LibAttributes.MAX_HEALTH, 12)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 7).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.3)
                    .putAttributes(LibAttributes.DEFENCE, 1.2).putLevelGains(LibAttributes.DEFENCE, 2)
                    .putAttributes(LibAttributes.MAGIC, 2).putLevelGains(LibAttributes.MAGIC, 1)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.4)
                    .putAttributes(LibAttributes.RES_FIRE, -25)
                    .xp(50).money(10).tamingChance(0.05f).setRidable().setMinLevel(5).build(),
            new GateSpawnData.Builder(0, 4).addToBiomeTag(50, ModTags.IS_BEACH, ModTags.IS_WATER));
    public static final RegistryEntrySupplier<EntityType<EntitySkyFish>> SKY_FISH = regMonster(EntityType.Builder.of(EntitySkyFish::new, MobCategory.MONSTER).sized(1.2f, 0.7f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "sky_fish"),
            0x8fa4c5, 0x5a3536,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 18).putLevelGains(LibAttributes.MAX_HEALTH, 11)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 3).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.3)
                    .putAttributes(LibAttributes.MAGIC, 6).putLevelGains(LibAttributes.MAGIC, 2.65)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.7)
                    .putAttributes(LibAttributes.RES_FIRE, -25)
                    .xp(50).money(6).tamingChance(0.05f).setRidable().setFlying().build(),
            new GateSpawnData.Builder(0, 5).addToBiomeTag(40, ModTags.IS_BEACH, ModTags.IS_WATER));
    public static final RegistryEntrySupplier<EntityType<EntityWeagle>> WEAGLE = regMonster(EntityType.Builder.of(EntityWeagle::new, MobCategory.MONSTER).sized(0.8f, 1.1f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "weagle"),
            0x8e127b, 0xdb9dd2, true,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 15).putLevelGains(LibAttributes.MAX_HEALTH, 11)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 4).putLevelGains(LibAttributes.ATTACK_DAMAGE, 1.4)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.2)
                    .putAttributes(LibAttributes.MAGIC, 5).putLevelGains(LibAttributes.MAGIC, 1.5)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1)
                    .xp(45).money(4).tamingChance(0.05f).setRidable().doesntNeedBarnRoof().setFlying().build(),
            new GateSpawnData.Builder(500, 7).addToBiomeTag(30, ModTags.IS_PLAINS, BiomeTags.IS_MOUNTAIN, BiomeTags.IS_HILL));
    public static final RegistryEntrySupplier<EntityType<EntityGoblin>> GOBLIN = regMonster(EntityType.Builder.of(EntityGoblin::new, MobCategory.MONSTER).sized(0.6f, 1.6f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "goblin"),
            0x21b322, 0x462f2a,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 21).putLevelGains(LibAttributes.MAX_HEALTH, 10.5)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 6).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.4)
                    .putAttributes(LibAttributes.DEFENCE, 0.5).putLevelGains(LibAttributes.DEFENCE, 1.4)
                    .putAttributes(LibAttributes.MAGIC, 4).putLevelGains(LibAttributes.MAGIC, 2)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.3)
                    .xp(55).money(8).tamingChance(0.05f).setRidable().build(),
            new GateSpawnData.Builder(500, 7).addToBiomeTag(30, ModTags.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, BiomeTags.IS_MOUNTAIN, ModTags.IS_SAVANNA, ModTags.IS_SANDY));
    public static final RegistryEntrySupplier<EntityType<EntityGoblinArcher>> GOBLIN_ARCHER = regMonster(EntityType.Builder.of(EntityGoblinArcher::new, MobCategory.MONSTER).sized(0.6f, 1.6f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "goblin_archer"),
            0x21b322, 0x462f2a,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 21).putLevelGains(LibAttributes.MAX_HEALTH, 10.5)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 6).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.5)
                    .putAttributes(LibAttributes.DEFENCE, 0.5).putLevelGains(LibAttributes.DEFENCE, 1.3)
                    .putAttributes(LibAttributes.MAGIC, 4).putLevelGains(LibAttributes.MAGIC, 1.8)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.2)
                    .xp(55).money(8).tamingChance(0.05f).setRidable().build(),
            new GateSpawnData.Builder(500, 7).addToBiomeTag(30, ModTags.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, BiomeTags.IS_MOUNTAIN, ModTags.IS_SAVANNA, ModTags.IS_SANDY));
    public static final RegistryEntrySupplier<EntityType<EntityDuck>> DUCK = regMonster(EntityType.Builder.of(EntityDuck::new, MobCategory.MONSTER).sized(0.65f, 1.35f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "duck"),
            0xdabf33, 0x845242,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 19).putLevelGains(LibAttributes.MAX_HEALTH, 11)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 6).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.8)
                    .putAttributes(LibAttributes.MAGIC, 2).putLevelGains(LibAttributes.MAGIC, 2)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.8)
                    .xp(50).money(5).tamingChance(0.1f).setRidable().build(),
            new GateSpawnData.Builder(0, 0).addToBiomeTag(30, ModTags.IS_PLAINS, ModTags.IS_WATER, BiomeTags.IS_BEACH));
    public static final RegistryEntrySupplier<EntityType<EntityFairy>> FAIRY = regMonster(EntityType.Builder.of(EntityFairy::new, MobCategory.MONSTER).sized(0.45f, 1.1f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "fairy"),
            0x4dad2a, 0xcdc41f, true,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 17).putLevelGains(LibAttributes.MAX_HEALTH, 10)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 3).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.5)
                    .putAttributes(LibAttributes.MAGIC, 7.3).putLevelGains(LibAttributes.MAGIC, 2.6)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 1).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.8)
                    .xp(66).money(6).tamingChance(0.05f).setFlying().build(),
            new GateSpawnData.Builder(0, 4).addToBiomeTag(30, ModTags.IS_PLAINS, BiomeTags.IS_FOREST, ModTags.IS_MAGICAL));
    public static final RegistryEntrySupplier<EntityType<EntityGhost>> GHOST = regMonster(EntityType.Builder.of(EntityGhost::new, MobCategory.MONSTER).sized(0.8f, 2.1f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "ghost"),
            0x4d3d35, 0x838383, true,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 22).putLevelGains(LibAttributes.MAX_HEALTH, 12)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 11.6).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.5)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.7)
                    .putAttributes(LibAttributes.MAGIC, 5).putLevelGains(LibAttributes.MAGIC, 2.9)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 2)
                    .xp(70).money(7).tamingChance(0.05f).setFlying().build(),
            new GateSpawnData.Builder(0, 10).addToBiomeTag(60, ModTags.IS_SPOOKY, ModTags.IS_DEAD, ModTags.IS_SWAMP));
    public static final RegistryEntrySupplier<EntityType<EntitySpirit>> SPIRIT = regMonster(EntityType.Builder.of(EntitySpirit::new, MobCategory.MONSTER).sized(0.5f, 0.6f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "spirit"),
            0xfdfdfd, 0xc3f8f7, true,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 18).putLevelGains(LibAttributes.MAX_HEALTH, 10.7)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 4).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.6)
                    .putAttributes(LibAttributes.MAGIC, 7.1).putLevelGains(LibAttributes.MAGIC, 2.9)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 2.1)
                    .xp(75).money(5).tamingChance(0.05f).setFlying().build(),
            new GateSpawnData.Builder(500, 3).addToBiomeTag(50, ModTags.IS_SPOOKY, ModTags.IS_DEAD, ModTags.IS_SWAMP, ModTags.IS_MAGICAL, ModTags.IS_END));
    public static final RegistryEntrySupplier<EntityType<EntityGhostRay>> GHOST_RAY = regMonster(EntityType.Builder.of(EntityGhostRay::new, MobCategory.MONSTER).sized(1f, 3.2f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "ghost_ray"),
            0x552217, 0x905a5a, true,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 32).putLevelGains(LibAttributes.MAX_HEALTH, 14)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 14.3).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.9)
                    .putAttributes(LibAttributes.DEFENCE, 1.5).putLevelGains(LibAttributes.DEFENCE, 2.1)
                    .putAttributes(LibAttributes.MAGIC, 9).putLevelGains(LibAttributes.MAGIC, 3.1)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 1.5).putLevelGains(LibAttributes.MAGIC_DEFENCE, 2.1)
                    .xp(150).money(10).tamingChance(0.02f).setBarnOccupancy(2).setFlying().build(),
            new GateSpawnData.Builder(500, 10).addToBiomeTag(10, ModTags.IS_SPOOKY, ModTags.IS_DEAD, ModTags.IS_SWAMP));
    public static final RegistryEntrySupplier<EntityType<EntitySpider>> SPIDER = regMonster(EntityType.Builder.of(EntitySpider::new, MobCategory.MONSTER).sized(1.1f, 0.7f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "spider"),
            0x6f6751, 0x404148,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 17).putLevelGains(LibAttributes.MAX_HEALTH, 11)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 8.5).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.2)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.5)
                    .putAttributes(LibAttributes.MAGIC, 4).putLevelGains(LibAttributes.MAGIC, 2)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.4)
                    .xp(65).money(8).tamingChance(0.05f).build(),
            new GateSpawnData.Builder(0, 0).addToBiomeTag(60, ModTags.IS_SPOOKY, BiomeTags.IS_FOREST, BiomeTags.IS_JUNGLE, ModTags.IS_LUSH));
    public static final RegistryEntrySupplier<EntityType<EntityPanther>> SHADOW_PANTHER = regMonster(EntityType.Builder.of(EntityPanther::new, MobCategory.MONSTER).sized(1.3f, 2.2f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "shadow_panther"),
            0x27375b, 0x733838,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 26).putLevelGains(LibAttributes.MAX_HEALTH, 11.6)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 11.5).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.66)
                    .putAttributes(LibAttributes.DEFENCE, 2).putLevelGains(LibAttributes.DEFENCE, 1.8)
                    .putAttributes(LibAttributes.MAGIC, 3).putLevelGains(LibAttributes.MAGIC, 1.9)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 2).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.6)
                    .xp(100).money(9).tamingChance(0.05f).setRidable().build(),
            new GateSpawnData.Builder(500, 9).addToBiomeTag(10, ModTags.IS_SPOOKY, ModTags.IS_SWAMP, ModTags.IS_PEAK, ModTags.IS_SLOPE));
    public static final RegistryEntrySupplier<EntityType<EntityMimic>> MONSTER_BOX = regMonster(EntityType.Builder.of(EntityMimic::new, MobCategory.MONSTER).sized(1, 1).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "monster_box"),
            0xac935e, 0x462f10,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 25).putLevelGains(LibAttributes.MAX_HEALTH, 10.9)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 6.1).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.4)
                    .putAttributes(LibAttributes.DEFENCE, 3).putLevelGains(LibAttributes.DEFENCE, 2.1)
                    .putAttributes(LibAttributes.MAGIC, 3).putLevelGains(LibAttributes.MAGIC, 2)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 2)
                    .xp(300).money(9).tamingChance(0.02f).build(),
            new GateSpawnData.Builder(0, 0));
    public static final RegistryEntrySupplier<EntityType<EntityMimic>> GOBBLE_BOX = regMonster(EntityType.Builder.of(EntityMimic::new, MobCategory.MONSTER).sized(1, 1).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "gobble_box"),
            0x8f9cc4, 0x343843,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 30).putLevelGains(LibAttributes.MAX_HEALTH, 11.4)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 8.3).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.5)
                    .putAttributes(LibAttributes.DEFENCE, 3.5).putLevelGains(LibAttributes.DEFENCE, 2.15)
                    .putAttributes(LibAttributes.MAGIC, 4).putLevelGains(LibAttributes.MAGIC, 2.3)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0.5).putLevelGains(LibAttributes.MAGIC_DEFENCE, 2.1)
                    .xp(500).money(9).tamingChance(0.015f).build(),
            new GateSpawnData.Builder(0, 10));
    public static final RegistryEntrySupplier<EntityType<EntityBigAnt>> KILLER_ANT = regMonster(EntityType.Builder.of(EntityBigAnt::new, MobCategory.MONSTER).sized(1.35f, 0.54f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "killer_ant"),
            0x0f0e0e, 0x754848,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 160).putLevelGains(LibAttributes.MAX_HEALTH, 13)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 25).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.2)
                    .putAttributes(LibAttributes.DEFENCE, 15).putLevelGains(LibAttributes.DEFENCE, 1.4)
                    .putAttributes(LibAttributes.MAGIC, 11).putLevelGains(LibAttributes.MAGIC, 1)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 13).putLevelGains(LibAttributes.DEFENCE, 1.1)
                    .xp(120).money(5).tamingChance(0.05f).setMinLevel(10).build(),
            new GateSpawnData.Builder(0, 10).addToBiomeTag(30, ModTags.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, ModTags.IS_LUSH, ModTags.IS_SAVANNA));
    public static final RegistryEntrySupplier<EntityType<EntityOrc>> HIGH_ORC = regMonster(EntityType.Builder.of(EntityOrc::new, MobCategory.MONSTER).sized(0.73f, 2.3f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "high_orc"),
            0x9f6c4e, 0x333e78,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 220).putLevelGains(LibAttributes.MAX_HEALTH, 12)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 24).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.35)
                    .putAttributes(LibAttributes.DEFENCE, 12).putLevelGains(LibAttributes.DEFENCE, 1.5)
                    .putAttributes(LibAttributes.MAGIC, 13).putLevelGains(LibAttributes.MAGIC, 0.9)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 12).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.25)
                    .xp(155).money(4).tamingChance(0.05f).setRidable().setMinLevel(10).build(),
            new GateSpawnData.Builder(0, 10).addToBiomeTag(30, ModTags.IS_PLAINS, ModTags.IS_BEACH, BiomeTags.IS_FOREST, BiomeTags.IS_HILL,
                    ModTags.IS_MAGICAL).addToBiomeTag(44, ModTags.IS_SAVANNA, BiomeTags.IS_MOUNTAIN));
    public static final RegistryEntrySupplier<EntityType<EntityOrcHunter>> ORC_HUNTER = regMonster(EntityType.Builder.of(EntityOrcHunter::new, MobCategory.MONSTER).sized(0.73f, 2.3f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "orc_hunter"),
            0x9f6c4e, 0x333e78,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 220).putLevelGains(LibAttributes.MAX_HEALTH, 12)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 26).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.24)
                    .putAttributes(LibAttributes.DEFENCE, 12).putLevelGains(LibAttributes.DEFENCE, 1.33)
                    .putAttributes(LibAttributes.MAGIC, 13).putLevelGains(LibAttributes.MAGIC, 1)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 12).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.22)
                    .xp(155).money(4).tamingChance(0.05f).setRidable().setMinLevel(10).build(),
            new GateSpawnData.Builder(0, 10).addToBiomeTag(30, ModTags.IS_PLAINS, ModTags.IS_BEACH, BiomeTags.IS_FOREST, BiomeTags.IS_HILL,
                    ModTags.IS_MAGICAL, BiomeTags.IS_MOUNTAIN, ModTags.IS_SAVANNA));
    public static final RegistryEntrySupplier<EntityType<EntityHornet>> HORNET = regMonster(EntityType.Builder.of(EntityHornet::new, MobCategory.MONSTER).sized(0.7f, 0.85f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "hornet"),
            0x627d73, 0x20201f, true,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 90).putLevelGains(LibAttributes.MAX_HEALTH, 12.5)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 15).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2)
                    .putAttributes(LibAttributes.DEFENCE, 14).putLevelGains(LibAttributes.DEFENCE, 1.2)
                    .putAttributes(LibAttributes.MAGIC, 13).putLevelGains(LibAttributes.MAGIC, 1)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 13).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.15)
                    .xp(135).money(4).tamingChance(0.05f).setRidable().setMinLevel(5).build(),
            new GateSpawnData.Builder(0, 5).addToBiomeTag(30, ModTags.IS_PLAINS, BiomeTags.IS_FOREST));
    public static final RegistryEntrySupplier<EntityType<EntityWolf>> SILVER_WOLF = regMonster(EntityType.Builder.of(EntityWolf::new, MobCategory.MONSTER).sized(0.8f, 1.15f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "silver_wolf"),
            0x9bb9c3, 0x436ea1,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 250).putLevelGains(LibAttributes.MAX_HEALTH, 13.5)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 6).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.5)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.2)
                    .putAttributes(LibAttributes.MAGIC, 3).putLevelGains(LibAttributes.MAGIC, 1)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.2)
                    .xp(35).money(4).tamingChance(0.05f).setRidable().setMinLevel(10).build(),
            new GateSpawnData.Builder(0, 10).addToBiomeTag(20, ModTags.IS_PLAINS, BiomeTags.IS_FOREST, ModTags.IS_SNOWY));
    public static final RegistryEntrySupplier<EntityType<EntityLeafBall>> LEAF_BALL = regMonster(EntityType.Builder.of(EntityLeafBall::new, MobCategory.MONSTER).sized(0.8f, 1.2f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "leaf_ball"),
            0xdcb5f0, 0xb72fd3, true,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 230).putLevelGains(LibAttributes.MAX_HEALTH, 12)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 17).putLevelGains(LibAttributes.ATTACK_DAMAGE, 1.5)
                    .putAttributes(LibAttributes.DEFENCE, 11).putLevelGains(LibAttributes.DEFENCE, 1.1)
                    .putAttributes(LibAttributes.MAGIC, 27).putLevelGains(LibAttributes.MAGIC, 2.5)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 13).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.1)
                    .xp(35).money(4).tamingChance(0.05f).setRidable().setMinLevel(10).build(),
            new GateSpawnData.Builder(0, 10).addToBiomeTag(50, ModTags.IS_LUSH, BiomeTags.IS_FOREST, ModTags.IS_MAGICAL));
    public static final RegistryEntrySupplier<EntityType<EntityChipsqueek>> FURPY = regMonster(EntityType.Builder.of(EntityChipsqueek::new, MobCategory.MONSTER).sized(0.65f, 0.95f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "furpy"),
            0xab8620, 0xf9ffbb,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 70).putLevelGains(LibAttributes.MAX_HEALTH, 10)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 14).putLevelGains(LibAttributes.ATTACK_DAMAGE, 1.3)
                    .putAttributes(LibAttributes.DEFENCE, 8).putLevelGains(LibAttributes.DEFENCE, 1.2)
                    .putAttributes(LibAttributes.MAGIC, 13).putLevelGains(LibAttributes.MAGIC, 1.3)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 8).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.2)
                    .xp(35).money(1).tamingChance(0.1f).setMinLevel(5).build(),
            new GateSpawnData.Builder(0, 5).addToBiomeTag(40, BiomeTags.IS_FOREST, ModTags.IS_SANDY, ModTags.IS_SAVANNA));
    public static final RegistryEntrySupplier<EntityType<EntityPalmCat>> PALM_CAT = regMonster(EntityType.Builder.of(EntityPalmCat::new, MobCategory.MONSTER).sized(0.6f, 1.8f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "palm_cat"),
            0xc98f2d, 0xb46d28,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 20).putLevelGains(LibAttributes.MAX_HEALTH, 12)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 7).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.25)
                    .putAttributes(LibAttributes.MAGIC, 5).putLevelGains(LibAttributes.MAGIC, 1.3)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.25)
                    .xp(35).money(1).tamingChance(0.05f).build(),
            new GateSpawnData.Builder(0, 0).addToBiomeTag(10, BiomeTags.IS_FOREST, BiomeTags.IS_TAIGA));
    public static final RegistryEntrySupplier<EntityType<EntityPommePomme>> MINO = regMonster(EntityType.Builder.of(EntityPommePomme::new, MobCategory.MONSTER).sized(0.9f, 1.8f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "mino"),
            0x8b573d, 0xc0916d,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 65).putLevelGains(LibAttributes.MAX_HEALTH, 12)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 12).putLevelGains(LibAttributes.ATTACK_DAMAGE, 1.5)
                    .putAttributes(LibAttributes.DEFENCE, 7).putLevelGains(LibAttributes.DEFENCE, 1.8)
                    .putAttributes(LibAttributes.MAGIC, 10).putLevelGains(LibAttributes.MAGIC, 1.25)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 7).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.25)
                    .xp(40).money(4).tamingChance(0.05f).setMinLevel(5).setRidable().build(),
            new GateSpawnData.Builder(0, 5).addToBiomeTag(10, BiomeTags.IS_FOREST, BiomeTags.IS_TAIGA, ModTags.IS_MAGICAL));
    public static final RegistryEntrySupplier<EntityType<EntityTrickyMuck>> TRICKY_MUCK = regMonster(EntityType.Builder.of(EntityTrickyMuck::new, MobCategory.MONSTER).sized(0.9f, 1.6f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "tricky_muck"),
            0x207316, 0x90d681,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 20).putLevelGains(LibAttributes.MAX_HEALTH, 12)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 3).putLevelGains(LibAttributes.ATTACK_DAMAGE, 1.2)
                    .putAttributes(LibAttributes.DEFENCE, 1).putLevelGains(LibAttributes.DEFENCE, 1.2)
                    .putAttributes(LibAttributes.MAGIC, 7).putLevelGains(LibAttributes.MAGIC, 2.4)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 2).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.5)
                    .xp(40).money(3).tamingChance(0.05f).setRidable().build(),
            new GateSpawnData.Builder(0, 0).addToBiomeTag(5, BiomeTags.IS_FOREST, ModTags.IS_LUSH, ModTags.IS_MAGICAL, ModTags.IS_MUSHROOM));
    public static final RegistryEntrySupplier<EntityType<EntityFlowerLily>> FLOWER_LILY = regMonster(EntityType.Builder.of(EntityFlowerLily::new, MobCategory.MONSTER).sized(0.75f, 1.65f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "flower_lily"),
            0xe8b3e7, 0x156e12,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 18).putLevelGains(LibAttributes.MAX_HEALTH, 11)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 8).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.1)
                    .putAttributes(LibAttributes.DEFENCE, 1).putLevelGains(LibAttributes.DEFENCE, 1.3)
                    .putAttributes(LibAttributes.MAGIC, 4).putLevelGains(LibAttributes.MAGIC, 1.7)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 1).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.3)
                    .xp(40).money(3).tamingChance(0.06f).setRidable().build(),
            new GateSpawnData.Builder(0, 0).addToBiomeTag(20, ModTags.IS_LUSH, ModTags.IS_MAGICAL, BiomeTags.IS_JUNGLE));
    public static final RegistryEntrySupplier<EntityType<EntityAggressiveWooly>> KING_WOOLY = regMonster(EntityType.Builder.of(EntityAggressiveWooly::new, MobCategory.MONSTER).sized(1.8f, 3.9f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "king_wooly"),
            0xffffcc, 0xffffff,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 25).putLevelGains(LibAttributes.MAX_HEALTH, 11)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 5).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2)
                    .putAttributes(LibAttributes.DEFENCE, 1).putLevelGains(LibAttributes.DEFENCE, 1.6)
                    .putAttributes(LibAttributes.MAGIC, 2).putLevelGains(LibAttributes.MAGIC, 1.2)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 1).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.3)
                    .xp(25).money(3).tamingChance(0.05f).setRidable().build(),
            new GateSpawnData.Builder(0, 0).addToBiomeTag(1, ModTags.IS_PLAINS, ModTags.IS_BEACH, BiomeTags.IS_FOREST, BiomeTags.IS_HILL,
                    ModTags.IS_MAGICAL).addToBiomeTag(1, ModTags.IS_SAVANNA, BiomeTags.IS_MOUNTAIN));
    public static final RegistryEntrySupplier<EntityType<EntityBuffamoo>> BUFFALOO = regMonster(EntityType.Builder.of(EntityBuffamoo::new, MobCategory.MONSTER).sized(1.2f, 1.45f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "buffaloo"),
            0x8a8a5e, 0xb5b489,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 80).putLevelGains(LibAttributes.MAX_HEALTH, 12)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 17).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2)
                    .putAttributes(LibAttributes.DEFENCE, 2).putLevelGains(LibAttributes.DEFENCE, 1.85)
                    .putAttributes(LibAttributes.MAGIC, 7).putLevelGains(LibAttributes.MAGIC, 1.5)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 2).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.35)
                    .xp(40).money(6).tamingChance(0.05f).setRidable().setMinLevel(5).build(),
            new GateSpawnData.Builder(0, 5).addToBiomeTag(20, ModTags.IS_HOT, BiomeTags.IS_MOUNTAIN, BiomeTags.IS_HILL, BiomeTags.IS_BADLANDS));
    public static final RegistryEntrySupplier<EntityType<EntityGoblinPirate>> GOBLIN_PIRATE = regMonster(EntityType.Builder.of(EntityGoblinPirate::new, MobCategory.MONSTER).sized(0.6f, 1.6f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "goblin_pirate"),
            0x484209, 0x29307f,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 22).putLevelGains(LibAttributes.MAX_HEALTH, 13)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 8).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.4)
                    .putAttributes(LibAttributes.DEFENCE, 0.5).putLevelGains(LibAttributes.DEFENCE, 1.35)
                    .putAttributes(LibAttributes.MAGIC, 4).putLevelGains(LibAttributes.MAGIC, 2)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.3)
                    .xp(50).money(8).tamingChance(0.05f).setRidable().build(),
            new GateSpawnData.Builder(500, 7).addToBiomeTag(30, ModTags.IS_HOT, ModTags.IS_SAVANNA, ModTags.IS_SANDY, BiomeTags.IS_BADLANDS));
    public static final RegistryEntrySupplier<EntityType<EntityGoblinGangster>> GOBLIN_GANGSTER = regMonster(EntityType.Builder.of(EntityGoblinGangster::new, MobCategory.MONSTER).sized(0.6f, 1.6f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "goblin_gangster"),
            0x6e5d2d, 0x316275,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 22).putLevelGains(LibAttributes.MAX_HEALTH, 13)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 8).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.4)
                    .putAttributes(LibAttributes.DEFENCE, 0.5).putLevelGains(LibAttributes.DEFENCE, 1.35)
                    .putAttributes(LibAttributes.MAGIC, 4).putLevelGains(LibAttributes.MAGIC, 2)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.3)
                    .xp(50).money(8).tamingChance(0.05f).setRidable().build(),
            new GateSpawnData.Builder(500, 7).addToBiomeTag(30, ModTags.IS_HOT, ModTags.IS_SAVANNA, ModTags.IS_SANDY, BiomeTags.IS_BADLANDS));
    public static final RegistryEntrySupplier<EntityType<EntityIgnis>> IGNIS = regMonster(EntityType.Builder.of(EntityIgnis::new, MobCategory.MONSTER).sized(0.5f, 0.6f).fireImmune().clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "ignis"),
            0xaa3100, 0x9f5e3f, true,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 20).putLevelGains(LibAttributes.MAX_HEALTH, 12)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 6).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2)
                    .putAttributes(LibAttributes.DEFENCE, 1).putLevelGains(LibAttributes.DEFENCE, 1.6)
                    .putAttributes(LibAttributes.MAGIC, 7.8).putLevelGains(LibAttributes.MAGIC, 3)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 1.5).putLevelGains(LibAttributes.MAGIC_DEFENCE, 2.1)
                    .xp(75).money(5).tamingChance(0.05f).setFlying().build(),
            new GateSpawnData.Builder(500, 3).addToBiomeTag(30, ModTags.IS_SPOOKY, ModTags.IS_DEAD, ModTags.IS_HOT, BiomeTags.IS_BADLANDS));
    public static final RegistryEntrySupplier<EntityType<EntityScorpion>> SCORPION = regMonster(EntityType.Builder.of(EntityScorpion::new, MobCategory.MONSTER).sized(1.1f, 0.6f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "scorpion"),
            0x606060, 0xacacac,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 17).putLevelGains(LibAttributes.MAX_HEALTH, 12)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 6).putLevelGains(LibAttributes.ATTACK_DAMAGE, 1.9)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.8)
                    .putAttributes(LibAttributes.MAGIC, 3).putLevelGains(LibAttributes.MAGIC, 1.5)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 2)
                    .xp(75).money(5).tamingChance(0.05f).setFlying().build(),
            new GateSpawnData.Builder(500, 3).addToBiomeTag(40, ModTags.IS_HOT, ModTags.IS_SAVANNA, ModTags.IS_SANDY, BiomeTags.IS_BADLANDS));
    public static final RegistryEntrySupplier<EntityType<EntityTroll>> TROLL = regMonster(EntityType.Builder.of(EntityTroll::new, MobCategory.MONSTER).sized(1.5f, 3f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "troll"),
            0xac924b, 0xcfcbbc,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 26).putLevelGains(LibAttributes.MAX_HEALTH, 14)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 9).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2)
                    .putAttributes(LibAttributes.DEFENCE, 1).putLevelGains(LibAttributes.DEFENCE, 1.7)
                    .putAttributes(LibAttributes.MAGIC, 1.1).putLevelGains(LibAttributes.MAGIC, 0.9)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.4)
                    .xp(100).money(5).tamingChance(0.05f).setFlying().build(),
            new GateSpawnData.Builder(500, 3).addToBiomeTag(10, BiomeTags.IS_MOUNTAIN, ModTags.IS_SPARSE, BiomeTags.IS_HILL));
    public static final RegistryEntrySupplier<EntityType<EntityFlowerLion>> FLOWER_LION = regMonster(EntityType.Builder.of(EntityFlowerLion::new, MobCategory.MONSTER).sized(0.75f, 1.65f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "flower_lion"),
            0xf2ad7a, 0x893a1d,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 17).putLevelGains(LibAttributes.MAX_HEALTH, 11)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 7).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.1)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.3)
                    .putAttributes(LibAttributes.MAGIC, 3).putLevelGains(LibAttributes.MAGIC, 1.6)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 1).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.4)
                    .xp(100).money(3).tamingChance(0.05f).setRidable().build(),
            new GateSpawnData.Builder(0, 0).addToBiomeTag(10, ModTags.IS_HOT, ModTags.IS_SAVANNA));
    public static final RegistryEntrySupplier<EntityType<EntityVeggieGhost>> TOMATO_GHOST = regMonster(EntityType.Builder.of(EntityVeggieGhost::new, MobCategory.MONSTER).sized(0.75f, 1.65f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "tomato_ghost"),
            0x902323, 0x85268b, true,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 19).putLevelGains(LibAttributes.MAX_HEALTH, 12)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 4).putLevelGains(LibAttributes.ATTACK_DAMAGE, 1.2)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.3)
                    .putAttributes(LibAttributes.MAGIC, 8).putLevelGains(LibAttributes.MAGIC, 2.35)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 1).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.4)
                    .xp(100).money(3).tamingChance(0.05f).setRidable().build(),
            new GateSpawnData.Builder(0, 0).addToBiomeTag(20, ModTags.IS_HOT, ModTags.IS_DEAD, ModTags.IS_WASTELAND));

    public static final RegistryEntrySupplier<EntityType<EntityAmbrosia>> AMBROSIA = regMonster(EntityType.Builder.of(EntityAmbrosia::new, MobCategory.MONSTER).sized(0.85f, 2.3f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "ambrosia"),
            0x00ff00, 0xe600e6,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 210).putLevelGains(LibAttributes.MAX_HEALTH, 27)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 10).putLevelGains(LibAttributes.ATTACK_DAMAGE, 3.25)
                    .putAttributes(LibAttributes.DEFENCE, 4).putLevelGains(LibAttributes.DEFENCE, 2.2)
                    .putAttributes(LibAttributes.MAGIC, 14).putLevelGains(LibAttributes.MAGIC, 3.6)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 4).putLevelGains(LibAttributes.MAGIC_DEFENCE, 2.45)
                    .putAttributes(LibAttributes.RES_EARTH, 25)
                    .putAttributes(LibAttributes.RES_WIND, -25)
                    .putAttributes(LibAttributes.RES_LOVE, -25)
                    .putAttributes(LibAttributes.RES_PARA, 85)
                    .putAttributes(LibAttributes.RES_POISON, 97)
                    .putAttributes(LibAttributes.RES_SEAL, 97)
                    .putAttributes(LibAttributes.RES_SLEEP, 100)
                    .putAttributes(LibAttributes.RES_DIZZY, 60)
                    .putAttributes(LibAttributes.RES_CRIT, 25)
                    .putAttributes(LibAttributes.RES_STUN, 50)
                    .putAttributes(LibAttributes.RES_FAINT, 100)
                    .putAttributes(LibAttributes.RES_DRAIN, 10)
                    .xp(500).money(50).tamingChance(0.005f).setBarnOccupancy(2).setRidable().setFlying().build());
    public static final RegistryEntrySupplier<EntityType<EntityThunderbolt>> THUNDERBOLT = regMonster(EntityType.Builder.of(EntityThunderbolt::new, MobCategory.MONSTER).sized(1.6f, 1.8f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "thunderbolt"),
            0x212121, 0x2f1177,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 250).putLevelGains(LibAttributes.MAX_HEALTH, 27)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 16).putLevelGains(LibAttributes.ATTACK_DAMAGE, 3.45)
                    .putAttributes(LibAttributes.DEFENCE, 4).putLevelGains(LibAttributes.DEFENCE, 2.3)
                    .putAttributes(LibAttributes.MAGIC, 13).putLevelGains(LibAttributes.MAGIC, 3.1)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 2).putLevelGains(LibAttributes.MAGIC_DEFENCE, 2.1)
                    .putAttributes(LibAttributes.RES_WIND, 25)
                    .putAttributes(LibAttributes.RES_PARA, 85)
                    .putAttributes(LibAttributes.RES_POISON, 97)
                    .putAttributes(LibAttributes.RES_SEAL, 97)
                    .putAttributes(LibAttributes.RES_SLEEP, 100)
                    .putAttributes(LibAttributes.RES_DIZZY, 60)
                    .putAttributes(LibAttributes.RES_CRIT, 25)
                    .putAttributes(LibAttributes.RES_STUN, 50)
                    .putAttributes(LibAttributes.RES_FAINT, 100)
                    .putAttributes(LibAttributes.RES_DRAIN, 10)
                    .xp(650).money(75).tamingChance(0.005f).setBarnOccupancy(2).setRidable().build());
    public static final RegistryEntrySupplier<EntityType<EntityMarionetta>> MARIONETTA = regMonster(EntityType.Builder.of(EntityMarionetta::new, MobCategory.MONSTER).sized(0.8f, 2.6f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "marionetta"),
            0xb86b13, 0xd8d7d7,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 300).putLevelGains(LibAttributes.MAX_HEALTH, 30.5)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 18).putLevelGains(LibAttributes.ATTACK_DAMAGE, 3.3)
                    .putAttributes(LibAttributes.DEFENCE, 5).putLevelGains(LibAttributes.DEFENCE, 2.3)
                    .putAttributes(LibAttributes.MAGIC, 13.3).putLevelGains(LibAttributes.MAGIC, 3.3)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 3.9).putLevelGains(LibAttributes.MAGIC_DEFENCE, 2.3)
                    .putAttributes(LibAttributes.RES_DARK, 25)
                    .putAttributes(LibAttributes.RES_LIGHT, -15)
                    .putAttributes(LibAttributes.RES_PARA, 85)
                    .putAttributes(LibAttributes.RES_POISON, 97)
                    .putAttributes(LibAttributes.RES_SEAL, 97)
                    .putAttributes(LibAttributes.RES_SLEEP, 100)
                    .putAttributes(LibAttributes.RES_DIZZY, 60)
                    .putAttributes(LibAttributes.RES_CRIT, 25)
                    .putAttributes(LibAttributes.RES_STUN, 50)
                    .putAttributes(LibAttributes.RES_FAINT, 100)
                    .putAttributes(LibAttributes.RES_DRAIN, 10)
                    .xp(900).money(75).tamingChance(0.005f).setBarnOccupancy(2).setRidable().build());
    public static final RegistryEntrySupplier<EntityType<EntityDeadTree>> DEAD_TREE = regMonster(EntityType.Builder.of(EntityDeadTree::new, MobCategory.MONSTER).sized(1.8f, 7f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "dead_tree"),
            0x3e4a40, 0x227904,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 300).putLevelGains(LibAttributes.MAX_HEALTH, 30.5)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 18).putLevelGains(LibAttributes.ATTACK_DAMAGE, 3.3)
                    .putAttributes(LibAttributes.DEFENCE, 5).putLevelGains(LibAttributes.DEFENCE, 2.3)
                    .putAttributes(LibAttributes.MAGIC, 13.3).putLevelGains(LibAttributes.MAGIC, 3.3)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 3.9).putLevelGains(LibAttributes.MAGIC_DEFENCE, 2.3)
                    .putAttributes(LibAttributes.RES_FIRE, -15)
                    .putAttributes(LibAttributes.RES_PARA, 85)
                    .putAttributes(LibAttributes.RES_POISON, 97)
                    .putAttributes(LibAttributes.RES_SEAL, 97)
                    .putAttributes(LibAttributes.RES_SLEEP, 100)
                    .putAttributes(LibAttributes.RES_DIZZY, 60)
                    .putAttributes(LibAttributes.RES_CRIT, 25)
                    .putAttributes(LibAttributes.RES_STUN, 50)
                    .putAttributes(LibAttributes.RES_FAINT, 100)
                    .putAttributes(LibAttributes.RES_DRAIN, 10)
                    .xp(950).money(100).tamingChance(0.005f).setBarnOccupancy(2).setRidable().build());
    public static final RegistryEntrySupplier<EntityType<EntityChimera>> CHIMERA = regMonster(EntityType.Builder.of(EntityChimera::new, MobCategory.MONSTER).sized(1.45f, 1.45f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "chimera"),
            0x536983, 0xaaa7c8,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 300).putLevelGains(LibAttributes.MAX_HEALTH, 28.5)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 18).putLevelGains(LibAttributes.ATTACK_DAMAGE, 3.3)
                    .putAttributes(LibAttributes.DEFENCE, 4.7).putLevelGains(LibAttributes.DEFENCE, 2.3)
                    .putAttributes(LibAttributes.MAGIC, 14.2).putLevelGains(LibAttributes.MAGIC, 3.35)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 3.6).putLevelGains(LibAttributes.MAGIC_DEFENCE, 2.25)
                    .putAttributes(LibAttributes.RES_LOVE, 25)
                    .putAttributes(LibAttributes.RES_PARA, 85)
                    .putAttributes(LibAttributes.RES_POISON, 97)
                    .putAttributes(LibAttributes.RES_SEAL, 97)
                    .putAttributes(LibAttributes.RES_SLEEP, 100)
                    .putAttributes(LibAttributes.RES_DIZZY, 60)
                    .putAttributes(LibAttributes.RES_CRIT, 25)
                    .putAttributes(LibAttributes.RES_STUN, 50)
                    .putAttributes(LibAttributes.RES_FAINT, 100)
                    .putAttributes(LibAttributes.RES_DRAIN, 10)
                    .xp(950).money(100).tamingChance(0.005f).setBarnOccupancy(2).setRidable().build());

    public static final RegistryEntrySupplier<EntityType<EntityNPCBase>> NPC = npc(EntityType.Builder.of(EntityNPCBase::new, MobCategory.MISC).sized(0.6f, 1.8f).clientTrackingRange(8), new ResourceLocation(RuneCraftory.MODID, "npc"));

    public static final RegistryEntrySupplier<EntityType<EntityTreasureChest>> TREASURE_CHEST = treasureChest(EntityType.Builder.of(EntityTreasureChest::new, MobCategory.MISC).sized(1, 1).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "treasure_chest"));
    public static final RegistryEntrySupplier<EntityType<EntityMobArrow>> ARROW = reg(EntityType.Builder.<EntityMobArrow>of(EntityMobArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20), new ResourceLocation(RuneCraftory.MODID, "arrow"));
    public static final RegistryEntrySupplier<EntityType<EntitySpore>> SPORE = reg(EntityType.Builder.<EntitySpore>of(EntitySpore::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "spore"));
    public static final RegistryEntrySupplier<EntityType<EntityWindGust>> GUST = reg(EntityType.Builder.<EntityWindGust>of(EntityWindGust::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "gust"));
    public static final RegistryEntrySupplier<EntityType<EntityStone>> STONE = reg(EntityType.Builder.<EntityStone>of(EntityStone::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "stone"));
    public static final RegistryEntrySupplier<EntityType<EntityStatusBall>> STATUS_BALL = reg(EntityType.Builder.<EntityStatusBall>of(EntityStatusBall::new, MobCategory.MISC).sized(0.4f, 0.6f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "status_ball"));
    public static final RegistryEntrySupplier<EntityType<EntityAmbrosiaWave>> AMBROSIA_WAVE = reg(EntityType.Builder.<EntityAmbrosiaWave>of(EntityAmbrosiaWave::new, MobCategory.MISC).sized(0.01f, 0.01f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "ambrosia_wave"));
    public static final RegistryEntrySupplier<EntityType<EntityButterfly>> BUTTERFLY = reg(EntityType.Builder.<EntityButterfly>of(EntityButterfly::new, MobCategory.MISC).sized(0.2f, 0.2f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "butterfly"));
    public static final RegistryEntrySupplier<EntityType<EntityPollenPuff>> POLLEN_PUFF = reg(EntityType.Builder.<EntityPollenPuff>of(EntityPollenPuff::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "pollen_puff"));
    public static final RegistryEntrySupplier<EntityType<EntityPollen>> POLLEN = reg(EntityType.Builder.<EntityPollen>of(EntityPollen::new, MobCategory.MISC).sized(0.01f, 0.01f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "pollen"));
    public static final RegistryEntrySupplier<EntityType<EntityThiccLightningBolt>> LIGHTNING_ORB_BOLT = reg(EntityType.Builder.<EntityThiccLightningBolt>of(EntityThiccLightningBolt::new, MobCategory.MISC).sized(0.8f, 0.8f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "lightning_orb_bolt"));
    public static final RegistryEntrySupplier<EntityType<EntityThunderboltBeam>> LIGHTNING_BEAM = reg(EntityType.Builder.<EntityThunderboltBeam>of(EntityThunderboltBeam::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "lightning_beam"));
    public static final RegistryEntrySupplier<EntityType<EntityWispFlame>> WISP_FLAME = reg(EntityType.Builder.<EntityWispFlame>of(EntityWispFlame::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "wisp_flame"));
    public static final RegistryEntrySupplier<EntityType<EntitySpiderWeb>> SPIDER_WEB = reg(EntityType.Builder.<EntitySpiderWeb>of(EntitySpiderWeb::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "spider_web"));
    public static final RegistryEntrySupplier<EntityType<EntityDarkBeam>> DARK_BEAM = reg(EntityType.Builder.<EntityDarkBeam>of(EntityDarkBeam::new, MobCategory.MISC).sized(0.01f, 0.01f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "dark_beam"));
    public static final RegistryEntrySupplier<EntityType<EntityCards>> CARDS = reg(EntityType.Builder.<EntityCards>of(EntityCards::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "cards"));
    public static final RegistryEntrySupplier<EntityType<EntityFurniture>> FURNITURE = reg(EntityType.Builder.<EntityFurniture>of(EntityFurniture::new, MobCategory.MISC).sized(0.9f, 0.9f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "furniture"));
    public static final RegistryEntrySupplier<EntityType<EntityMarionettaTrap>> TRAP_CHEST = reg(EntityType.Builder.<EntityMarionettaTrap>of(EntityMarionettaTrap::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "trap_chest"));
    public static final RegistryEntrySupplier<EntityType<EntityBaseSpellBall>> STAFF_BASE_PROJECTILE = reg(EntityType.Builder.<EntityBaseSpellBall>of(EntityBaseSpellBall::new, MobCategory.MISC).sized(0.2f, 0.2f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "staff_base_projectile"));
    public static final RegistryEntrySupplier<EntityType<EntityFireball>> FIRE_BALL = reg(EntityType.Builder.<EntityFireball>of(EntityFireball::new, MobCategory.MISC).sized(0.2f, 0.2f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "fireball"));
    public static final RegistryEntrySupplier<EntityType<EntityExplosionSpell>> EXPLOSION = reg(EntityType.Builder.<EntityExplosionSpell>of(EntityExplosionSpell::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "explosion"));
    public static final RegistryEntrySupplier<EntityType<EntityWaterLaser>> WATER_LASER = reg(EntityType.Builder.<EntityWaterLaser>of(EntityWaterLaser::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "water_laser"));
    public static final RegistryEntrySupplier<EntityType<EntityRockSpear>> ROCK_SPEAR = reg(EntityType.Builder.<EntityRockSpear>of(EntityRockSpear::new, MobCategory.MISC).sized(0.01f, 0.01f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "rock_spear"));
    public static final RegistryEntrySupplier<EntityType<EntityWindBlade>> WIND_BLADE = reg(EntityType.Builder.<EntityWindBlade>of(EntityWindBlade::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "wind_blade"));
    public static final RegistryEntrySupplier<EntityType<EntityLightBall>> LIGHT_BALL = reg(EntityType.Builder.<EntityLightBall>of(EntityLightBall::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "light_ball"));
    public static final RegistryEntrySupplier<EntityType<EntityDarkBall>> DARK_BALL = reg(EntityType.Builder.<EntityDarkBall>of(EntityDarkBall::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "dark_ball"));
    public static final RegistryEntrySupplier<EntityType<EntityDarkness>> DARKNESS = reg(EntityType.Builder.<EntityDarkness>of(EntityDarkness::new, MobCategory.MISC).sized(0.01f, 0.01f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "darkness"));
    public static final RegistryEntrySupplier<EntityType<EntityBigPlate>> BIG_PLATE = reg(EntityType.Builder.<EntityBigPlate>of(EntityBigPlate::new, MobCategory.MISC).sized(1.5f, 0.3f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "big_plate"));
    public static final RegistryEntrySupplier<EntityType<EntityDarkBullet>> DARK_BULLET = reg(EntityType.Builder.<EntityDarkBullet>of(EntityDarkBullet::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "dark_bullet"));
    public static final RegistryEntrySupplier<EntityType<EntityPoisonNeedle>> POISON_NEEDLE = reg(EntityType.Builder.<EntityPoisonNeedle>of(EntityPoisonNeedle::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "poison_needle"));
    public static final RegistryEntrySupplier<EntityType<EntitySleepAura>> SLEEP_AURA = reg(EntityType.Builder.<EntitySleepAura>of(EntitySleepAura::new, MobCategory.MISC).sized(1.5f, 1).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "sleep_aura"));
    public static final RegistryEntrySupplier<EntityType<EntityBullet>> CIRCLING_BULLET = reg(EntityType.Builder.<EntityBullet>of(EntityBullet::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "circling_bullet"));
    public static final RegistryEntrySupplier<EntityType<EntityThrownItem>> THROWN_ITEM = reg(EntityType.Builder.<EntityThrownItem>of(EntityThrownItem::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "thrown_item"));
    public static final RegistryEntrySupplier<EntityType<EntityAppleProjectile>> APPLE = reg(EntityType.Builder.<EntityAppleProjectile>of(EntityAppleProjectile::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "apple"));
    public static final RegistryEntrySupplier<EntityType<EntitySlashResidue>> SLASH_RESIDUE = reg(EntityType.Builder.<EntitySlashResidue>of(EntitySlashResidue::new, MobCategory.MISC).sized(1.2f, 1.2f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "slash_residue"));

    public static final RegistryEntrySupplier<EntityType<EntityRuney>> RUNEY = reg(EntityType.Builder.of(EntityRuney::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "runey"));
    public static final RegistryEntrySupplier<EntityType<EntityRuneOrb>> STAT_BONUS = reg(EntityType.Builder.of(EntityRuneOrb::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "rune_orb"));

    public static final RegistryEntrySupplier<EntityType<EntityButterflySummoner>> BUTTERFLY_SUMMONER = reg(EntityType.Builder.<EntityButterflySummoner>of(EntityButterflySummoner::new, MobCategory.MISC).sized(0.01f, 0.01f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "butterfly_summoner"));
    public static final RegistryEntrySupplier<EntityType<EntityDarkBulletSummoner>> DARK_BULLET_SUMMONER = reg(EntityType.Builder.<EntityDarkBulletSummoner>of(EntityDarkBulletSummoner::new, MobCategory.MISC).sized(0.01f, 0.01f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "dark_bullet_summoner"));
    public static final RegistryEntrySupplier<EntityType<ElementBallBarrageSummoner>> ELEMENTAL_BARRAGE_SUMMONER = reg(EntityType.Builder.<ElementBallBarrageSummoner>of(ElementBallBarrageSummoner::new, MobCategory.MISC).sized(0.01f, 0.01f).clientTrackingRange(4), new ResourceLocation(RuneCraftory.MODID, "elemental_barrage_summoner"));

    public static final RegistryEntrySupplier<EntityType<EntityCustomFishingHook>> FISHING_HOOK = reg(EntityType.Builder.<EntityCustomFishingHook>of(EntityCustomFishingHook::new, MobCategory.MISC).noSave().noSummon().sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(5), new ResourceLocation(RuneCraftory.MODID, "fishing_hook"));

    public static List<RegistryEntrySupplier<EntityType<?>>> getMonsters() {
        return ImmutableList.copyOf(MONSTERS);
    }

    public static Map<ResourceLocation, GateSpawnData> getDefaultGateSpawns() {
        return ImmutableMap.copyOf(DEFAULT_SPAWN_DATA);
    }

    @SuppressWarnings("unchecked")
    public static void registerAttributes(BiConsumer<EntityType<? extends LivingEntity>, AttributeSupplier.Builder> cons) {
        cons.accept(GATE.get(), GateEntity.createAttributes());
        for (RegistryEntrySupplier<EntityType<?>> reg : MONSTERS) {
            EntityType<? extends LivingEntity> type = (EntityType<? extends LivingEntity>) reg.get();
            if (FLYING_MONSTERS.contains(reg))
                cons.accept(type, BaseMonster.createAttributes(ModAttributes.ENTITY_ATTRIBUTES).add(Attributes.FLYING_SPEED));
            else
                cons.accept(type, BaseMonster.createAttributes(ModAttributes.ENTITY_ATTRIBUTES));
        }
        cons.accept(NPC.get(), EntityNPCBase.createAttributes(ModAttributes.ENTITY_ATTRIBUTES));
    }

    public static <V extends Entity> RegistryEntrySupplier<EntityType<V>> reg(EntityType.Builder<V> v, ResourceLocation name) {
        return ENTITIES.register(name.getPath(), () -> v.build(name.getPath()));
    }

    public static <V extends Entity> RegistryEntrySupplier<EntityType<V>> regWithEgg(EntityType.Builder<V> v, ResourceLocation name, int primary, int secondary) {
        RegistryEntrySupplier<EntityType<V>> reg = reg(v, name);
        ModItems.ITEMS.register(name.getPath() + "_spawn_egg", () -> new RuneCraftoryEggItem(reg, primary, secondary, new Item.Properties().tab(RFCreativeTabs.monsters)));
        return reg;
    }

    public static <V extends Entity> RegistryEntrySupplier<EntityType<V>> treasureChest(EntityType.Builder<V> v, ResourceLocation name) {
        RegistryEntrySupplier<EntityType<V>> reg = reg(v, name);
        ModItems.ITEMS.register(name.getPath() + "_spawn_egg", () -> new TreasureChestSpawnegg(reg, new Item.Properties().tab(RFCreativeTabs.monsters)));
        return reg;
    }

    public static <V extends Entity> RegistryEntrySupplier<EntityType<V>> npc(EntityType.Builder<V> v, ResourceLocation name) {
        RegistryEntrySupplier<EntityType<V>> reg = reg(v, name);
        ModItems.ITEMS.register(name.getPath() + "_spawn_egg", () -> new NPCSpawnEgg(reg, new Item.Properties().tab(RFCreativeTabs.monsters)));
        return reg;
    }

    public static <V extends BaseMonster> RegistryEntrySupplier<EntityType<V>> regMonster(EntityType.Builder<V> v, ResourceLocation name, int primary, int secondary, EntityProperties props) {
        return regMonster(v, name, primary, secondary, false, props);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <V extends BaseMonster> RegistryEntrySupplier<EntityType<V>> regMonster(EntityType.Builder<V> v, ResourceLocation name, int primary, int secondary, boolean flying, EntityProperties props) {
        MobConfig.propertiesMap.put(name, props);
        RegistryEntrySupplier<EntityType<V>> sup = regWithEgg(v, name, primary, secondary);
        MONSTERS.add((RegistryEntrySupplier) sup);
        if (flying)
            FLYING_MONSTERS.add((RegistryEntrySupplier) sup);
        return sup;
    }

    public static <V extends BaseMonster> RegistryEntrySupplier<EntityType<V>> regMonster(EntityType.Builder<V> v, ResourceLocation name, int primary, int secondary, EntityProperties props, GateSpawnData.Builder builder) {
        return regMonster(v, name, primary, secondary, false, props, builder);
    }

    public static <V extends BaseMonster> RegistryEntrySupplier<EntityType<V>> regMonster(EntityType.Builder<V> v, ResourceLocation name, int primary, int secondary, boolean flying, EntityProperties props, GateSpawnData.Builder builder) {
        DEFAULT_SPAWN_DATA.put(name, builder.build(name));
        return regMonster(v, name, primary, secondary, flying, props);
    }
}
