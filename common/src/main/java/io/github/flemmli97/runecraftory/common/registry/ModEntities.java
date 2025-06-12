package io.github.flemmli97.runecraftory.common.registry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.EntityProperties;
import io.github.flemmli97.runecraftory.api.datapack.EntityRideActionCosts;
import io.github.flemmli97.runecraftory.api.datapack.GateSpawnData;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.GateEntity;
import io.github.flemmli97.runecraftory.common.entities.MultiPartEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.BlazeBarrageSummoner;
import io.github.flemmli97.runecraftory.common.entities.misc.ElementBallBarrageSummoner;
import io.github.flemmli97.runecraftory.common.entities.misc.ElementalCircleSummoner;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityAmbrosiaWave;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityAppleProjectile;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityBigPlate;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityBigRaccoonLeaf;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityBoneNeedle;
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
import io.github.flemmli97.runecraftory.common.entities.misc.EntityElementalBall;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityElementalTrail;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityExplosionSpell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityFireball;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityFurniture;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityGustRocks;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityHomingEnergyOrb;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityLightBall;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityLightBeam;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityMarionettaTrap;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityMissile;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityMobArrow;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityPoisonNeedle;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityPollen;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityPollenPuff;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityPowerWave;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityRockSpear;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityRuneOrb;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityRuney;
import io.github.flemmli97.runecraftory.common.entities.misc.EntitySlashResidue;
import io.github.flemmli97.runecraftory.common.entities.misc.EntitySleepAura;
import io.github.flemmli97.runecraftory.common.entities.misc.EntitySmallRaccoonLeaf;
import io.github.flemmli97.runecraftory.common.entities.misc.EntitySpiderWeb;
import io.github.flemmli97.runecraftory.common.entities.misc.EntitySpike;
import io.github.flemmli97.runecraftory.common.entities.misc.EntitySpore;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityStarfall;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityStatusBall;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityStone;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityThiccLightningBolt;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityThrownItem;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityThunderboltBeam;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityTornado;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityTreasureChest;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWaterLaser;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWindBlade;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWindGust;
import io.github.flemmli97.runecraftory.common.entities.misc.FireWallSummoner;
import io.github.flemmli97.runecraftory.common.entities.misc.GroundShakeParticleSpawner;
import io.github.flemmli97.runecraftory.common.entities.misc.RafflesiaBreathSummoner;
import io.github.flemmli97.runecraftory.common.entities.misc.RafflesiaCircleSummoner;
import io.github.flemmli97.runecraftory.common.entities.misc.RootSpikeSummoner;
import io.github.flemmli97.runecraftory.common.entities.misc.SarcophagusTeleporter;
import io.github.flemmli97.runecraftory.common.entities.misc.SporeCircleSummoner;
import io.github.flemmli97.runecraftory.common.entities.misc.StarFallSummoner;
import io.github.flemmli97.runecraftory.common.entities.misc.WindBladeBarrageSummoner;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityAnt;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityBeetle;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityBigMuck;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityBuffamoo;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityChipsqueek;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityCluckadoodle;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityDemon;
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
import io.github.flemmli97.runecraftory.common.entities.monster.EntityKingWooly;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityLeafBall;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityMage;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityMimic;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityMineralSqueek;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityMinotaur;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityNappie;
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
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityGrimoire;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityHandonetta;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityMarionetta;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityRaccoon;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntitySano;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntitySarcophagus;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntitySkelefang;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityThunderbolt;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityUno;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.rafflesia.EntityRafflesia;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.rafflesia.EntityRafflesiaFlower;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.rafflesia.EntityRafflesiaHorseTail;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.rafflesia.EntityRafflesiaPart;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.rafflesia.EntityRafflesiaPitcher;
import io.github.flemmli97.runecraftory.common.entities.monster.ensemble.SanoAndUnoDuo;
import io.github.flemmli97.runecraftory.common.entities.monster.wisp.EntityIgnis;
import io.github.flemmli97.runecraftory.common.entities.monster.wisp.EntitySpirit;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.items.creative.EnsembleEggItem;
import io.github.flemmli97.runecraftory.common.items.creative.NPCSpawnEgg;
import io.github.flemmli97.runecraftory.common.items.creative.RuneCraftoryEggItem;
import io.github.flemmli97.runecraftory.common.items.creative.TreasureChestSpawnegg;
import io.github.flemmli97.runecraftory.common.lib.LibAdvancements;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class ModEntities {

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

    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityWooly>> WOOLY = regMonster(EntityType.Builder.of(EntityWooly::new, MobCategory.MONSTER).sized(0.7f, 1.55f).clientTrackingRange(8), RuneCraftory.modRes("wooly"),
            0xffffcc, 0xffffff,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 18).putLevelGains(Attributes.MAX_HEALTH, 530)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 3).putLevelGains(Attributes.ATTACK_DAMAGE, 260)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 0).putLevelGains(ModAttributes.DEFENCE.asHolder(), 240)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 1).putLevelGains(ModAttributes.MAGIC.asHolder(), 230)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 0).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 240)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 5)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .xp(5).tamingChance(0.2f).setRideable(),
            new GateSpawnData.Builder(0, 0).addToBiomeTag(100, RunecraftoryTags.IS_PLAINS, RunecraftoryTags.IS_BEACH, BiomeTags.IS_FOREST, BiomeTags.IS_HILL,
                    RunecraftoryTags.IS_MAGICAL).addToBiomeTag(60, RunecraftoryTags.IS_SAVANNA, BiomeTags.IS_MOUNTAIN, BiomeTags.IS_TAIGA));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityKingWooly>> KING_WOOLY = regMonster(EntityType.Builder.of(EntityKingWooly::new, MobCategory.MONSTER).sized(1.8f, 3.9f).clientTrackingRange(8), RuneCraftory.modRes("king_wooly"),
            0xffffcc, 0xffffff,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 100).putLevelGains(Attributes.MAX_HEALTH, 550)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 20).putLevelGains(Attributes.ATTACK_DAMAGE, 286)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 6).putLevelGains(ModAttributes.DEFENCE.asHolder(), 255)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 16).putLevelGains(ModAttributes.MAGIC.asHolder(), 246)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 6).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 255)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 7)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 2)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 7)
                    .putAttributes(ModAttributes.RES_FIRE.asHolder(), -10)
                    .putAttributes(ModAttributes.RES_LOVE.asHolder(), -10)
                    .putAttributes(ModAttributes.RES_WATER.asHolder(), 10)
                    .xp(25).tamingChance(0.05f).setRideable(),
            new GateSpawnData.Builder(0, 40).addToBiomeTag(8, RunecraftoryTags.IS_PLAINS, RunecraftoryTags.IS_BEACH, BiomeTags.IS_FOREST, BiomeTags.IS_HILL,
                    RunecraftoryTags.IS_MAGICAL).addToBiomeTag(60, RunecraftoryTags.IS_SAVANNA, BiomeTags.IS_MOUNTAIN, BiomeTags.IS_TAIGA));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityOrc>> ORC = regMonster(EntityType.Builder.of(EntityOrc::new, MobCategory.MONSTER).sized(0.73f, 2.3f).clientTrackingRange(8), RuneCraftory.modRes("orc"),
            0x663300, 0xffbf80,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 20).putLevelGains(Attributes.MAX_HEALTH, 480)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 9).putLevelGains(Attributes.ATTACK_DAMAGE, 277)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 0).putLevelGains(ModAttributes.DEFENCE.asHolder(), 216)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 4).putLevelGains(ModAttributes.MAGIC.asHolder(), 245)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 0).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 210)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_EARTH.asHolder(), 10)
                    .xp(10).tamingChance(0.15f).setRideable(),
            new GateSpawnData.Builder(0, 0).addToBiomeTag(100, RunecraftoryTags.IS_PLAINS, RunecraftoryTags.IS_BEACH, BiomeTags.IS_FOREST, BiomeTags.IS_HILL,
                    RunecraftoryTags.IS_MAGICAL).addToBiomeTag(60, RunecraftoryTags.IS_SAVANNA, BiomeTags.IS_MOUNTAIN, BiomeTags.IS_TAIGA));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityOrcArcher>> ORC_ARCHER = regMonster(EntityType.Builder.of(EntityOrcArcher::new, MobCategory.MONSTER).sized(0.73f, 2.3f).clientTrackingRange(8), RuneCraftory.modRes("orc_archer"),
            0x663300, 0xffbf80,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 20).putLevelGains(Attributes.MAX_HEALTH, 480)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 9).putLevelGains(Attributes.ATTACK_DAMAGE, 277)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 0).putLevelGains(ModAttributes.DEFENCE.asHolder(), 216)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 4).putLevelGains(ModAttributes.MAGIC.asHolder(), 245)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 0).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 210)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_EARTH.asHolder(), 10)
                    .xp(10).tamingChance(0.15f).setRideable(),
            new GateSpawnData.Builder(0, 0).addToBiomeTag(100, RunecraftoryTags.IS_PLAINS, RunecraftoryTags.IS_BEACH, BiomeTags.IS_FOREST, BiomeTags.IS_HILL,
                    RunecraftoryTags.IS_MAGICAL).addToBiomeTag(60, RunecraftoryTags.IS_SAVANNA, BiomeTags.IS_MOUNTAIN, BiomeTags.IS_TAIGA));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityOrc>> HIGH_ORC = regMonster(EntityType.Builder.of(EntityOrc::new, MobCategory.MONSTER).sized(0.73f, 2.3f).clientTrackingRange(8), RuneCraftory.modRes("high_orc"),
            0x9f6c4e, 0x333e78,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 28).putLevelGains(Attributes.MAX_HEALTH, 505)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 17).putLevelGains(Attributes.ATTACK_DAMAGE, 289)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 5).putLevelGains(ModAttributes.DEFENCE.asHolder(), 231)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 9).putLevelGains(ModAttributes.MAGIC.asHolder(), 254)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 5).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 229)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_EARTH.asHolder(), 15)
                    .xp(155).tamingChance(0.05f).setRideable().setMinLevel(10),
            new GateSpawnData.Builder(0, 20).addToBiomeTag(70, RunecraftoryTags.IS_PLAINS, RunecraftoryTags.IS_BEACH, BiomeTags.IS_FOREST, BiomeTags.IS_HILL,
                    RunecraftoryTags.IS_MAGICAL).addToBiomeTag(44, RunecraftoryTags.IS_SAVANNA, BiomeTags.IS_MOUNTAIN));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityOrcHunter>> ORC_HUNTER = regMonster(EntityType.Builder.of(EntityOrcHunter::new, MobCategory.MONSTER).sized(0.73f, 2.3f).clientTrackingRange(8), RuneCraftory.modRes("orc_hunter"),
            0x9f6c4e, 0x333e78,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 28).putLevelGains(Attributes.MAX_HEALTH, 505)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 17).putLevelGains(Attributes.ATTACK_DAMAGE, 289)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 5).putLevelGains(ModAttributes.DEFENCE.asHolder(), 231)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 9).putLevelGains(ModAttributes.MAGIC.asHolder(), 254)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 5).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 229)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_EARTH.asHolder(), 15)
                    .xp(155).tamingChance(0.05f).setRideable().setMinLevel(10),
            new GateSpawnData.Builder(0, 20).addToBiomeTag(70, RunecraftoryTags.IS_PLAINS, RunecraftoryTags.IS_BEACH, BiomeTags.IS_FOREST, BiomeTags.IS_HILL,
                    RunecraftoryTags.IS_MAGICAL, BiomeTags.IS_MOUNTAIN, RunecraftoryTags.IS_SAVANNA));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityAnt>> ANT = regMonster(EntityType.Builder.of(EntityAnt::new, MobCategory.MONSTER).sized(1.1f, 0.44f).clientTrackingRange(8), RuneCraftory.modRes("ant"),
            0x800000, 0x1a0000,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 22).putLevelGains(Attributes.MAX_HEALTH, 503)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 7).putLevelGains(Attributes.ATTACK_DAMAGE, 267)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 1).putLevelGains(ModAttributes.DEFENCE.asHolder(), 229)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 4).putLevelGains(ModAttributes.MAGIC.asHolder(), 218)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 0).putLevelGains(ModAttributes.DEFENCE.asHolder(), 225)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_WATER.asHolder(), -10)
                    .xp(10).tamingChance(0.1f),
            new GateSpawnData.Builder(0, 0).addToBiomeTag(80, RunecraftoryTags.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, RunecraftoryTags.IS_LUSH, RunecraftoryTags.IS_SAVANNA));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityAnt>> KILLER_ANT = regMonster(EntityType.Builder.of(EntityAnt::new, MobCategory.MONSTER).sized(1.35f, 0.54f).clientTrackingRange(8), RuneCraftory.modRes("killer_ant"),
            0x0f0e0e, 0x754848,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 28).putLevelGains(Attributes.MAX_HEALTH, 525)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 16).putLevelGains(Attributes.ATTACK_DAMAGE, 282)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 4).putLevelGains(ModAttributes.DEFENCE.asHolder(), 238)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 11).putLevelGains(ModAttributes.MAGIC.asHolder(), 221)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 4).putLevelGains(ModAttributes.DEFENCE.asHolder(), 227)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_FIRE.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_EARTH.asHolder(), 15)
                    .xp(120).tamingChance(0.05f).setMinLevel(10),
            new GateSpawnData.Builder(0, 20).addToBiomeTag(40, RunecraftoryTags.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, RunecraftoryTags.IS_LUSH, RunecraftoryTags.IS_SAVANNA));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityBeetle>> BEETLE = regMonster(EntityType.Builder.of(EntityBeetle::new, MobCategory.MONSTER).sized(0.7f, 1.7f).clientTrackingRange(8), RuneCraftory.modRes("beetle"),
            0x9c6a43, 0x244a69,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 23).putLevelGains(Attributes.MAX_HEALTH, 498)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 11).putLevelGains(Attributes.ATTACK_DAMAGE, 273)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 0).putLevelGains(ModAttributes.DEFENCE.asHolder(), 219)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 5).putLevelGains(ModAttributes.MAGIC.asHolder(), 209)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 0).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 214)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_FIRE.asHolder(), -10)
                    .putAttributes(ModAttributes.RES_WIND.asHolder(), 10)
                    .xp(15).tamingChance(0.05f).setRideable().setFlying(),
            new GateSpawnData.Builder(0, 0).addToBiomeTag(70, RunecraftoryTags.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, RunecraftoryTags.IS_LUSH));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityBigMuck>> BIG_MUCK = regMonster(EntityType.Builder.of(EntityBigMuck::new, MobCategory.MONSTER).sized(0.9f, 1.6f).clientTrackingRange(8), RuneCraftory.modRes("big_muck"),
            0xd7ce4a, 0xad5c25,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 20).putLevelGains(Attributes.MAX_HEALTH, 486)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 8).putLevelGains(Attributes.ATTACK_DAMAGE, 234)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 0).putLevelGains(ModAttributes.DEFENCE.asHolder(), 210)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 12).putLevelGains(ModAttributes.MAGIC.asHolder(), 275)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 1).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 215)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_LOVE.asHolder(), 10)
                    .putAttributes(ModAttributes.RES_DARK.asHolder(), -5)
                    .xp(20).tamingChance(0.05f).setRideable()
                    .withRideActionCosts(new EntityRideActionCosts.Builder().secondCost(0.5f, true)
                            .thirdCost(0.5f, true)),
            new GateSpawnData.Builder(0, 5).addToBiomeTag(60, RunecraftoryTags.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, RunecraftoryTags.IS_LUSH, RunecraftoryTags.IS_MAGICAL, RunecraftoryTags.IS_MUSHROOM));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityTrickyMuck>> TRICKY_MUCK = regMonster(EntityType.Builder.of(EntityTrickyMuck::new, MobCategory.MONSTER).sized(0.9f, 1.6f).clientTrackingRange(8), RuneCraftory.modRes("tricky_muck"),
            0x207316, 0x90d681,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 29).putLevelGains(Attributes.MAX_HEALTH, 498)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 13).putLevelGains(Attributes.ATTACK_DAMAGE, 254)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 1).putLevelGains(ModAttributes.DEFENCE.asHolder(), 223)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 19).putLevelGains(ModAttributes.MAGIC.asHolder(), 291)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 2).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 239)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_EARTH.asHolder(), 15)
                    .putAttributes(ModAttributes.RES_LOVE.asHolder(), -10)
                    .xp(40).tamingChance(0.05f).setRideable()
                    .withRideActionCosts(new EntityRideActionCosts.Builder().secondCost(0.5f, true)
                            .thirdCost(0.5f, true)),
            new GateSpawnData.Builder(0, 25).addToBiomeTag(40, BiomeTags.IS_FOREST, RunecraftoryTags.IS_LUSH, RunecraftoryTags.IS_MAGICAL, RunecraftoryTags.IS_MUSHROOM));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityBuffamoo>> BUFFAMOO = regMonster(EntityType.Builder.of(EntityBuffamoo::new, MobCategory.MONSTER).sized(1.2f, 1.45f).clientTrackingRange(8), RuneCraftory.modRes("buffamoo"),
            0xd8d8d0, 0x4e4e4c,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 25).putLevelGains(Attributes.MAX_HEALTH, 506)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 11).putLevelGains(Attributes.ATTACK_DAMAGE, 263)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 1).putLevelGains(ModAttributes.DEFENCE.asHolder(), 222)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 9).putLevelGains(ModAttributes.MAGIC.asHolder(), 220)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 0).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 222)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_LOVE.asHolder(), 10)
                    .xp(20).tamingChance(0.15f).setRideable(),
            new GateSpawnData.Builder(0, 5).addToBiomeTag(70, RunecraftoryTags.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, BiomeTags.IS_MOUNTAIN, RunecraftoryTags.IS_LUSH));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityBuffamoo>> BUFFALOO = regMonster(EntityType.Builder.of(EntityBuffamoo::new, MobCategory.MONSTER).sized(1.2f, 1.45f).clientTrackingRange(8), RuneCraftory.modRes("buffaloo"),
            0x8a8a5e, 0xb5b489,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 35).putLevelGains(Attributes.MAX_HEALTH, 521)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 18).putLevelGains(Attributes.ATTACK_DAMAGE, 286)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 2).putLevelGains(ModAttributes.DEFENCE.asHolder(), 243)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 13).putLevelGains(ModAttributes.MAGIC.asHolder(), 231)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 2).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 243)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_LOVE.asHolder(), -10)
                    .xp(40).tamingChance(0.05f).setRideable().setMinLevel(5),
            new GateSpawnData.Builder(0, 25).addToBiomeTag(60, RunecraftoryTags.IS_SLOPE, RunecraftoryTags.IS_HOT, BiomeTags.IS_MOUNTAIN, BiomeTags.IS_HILL, BiomeTags.IS_BADLANDS));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityChipsqueek>> CHIPSQUEEK = regMonster(EntityType.Builder.of(EntityChipsqueek::new, MobCategory.MONSTER).sized(0.65f, 0.95f).clientTrackingRange(8), RuneCraftory.modRes("chipsqueek"),
            0xff3b5b, 0xf9ffbb,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 21).putLevelGains(Attributes.MAX_HEALTH, 489)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 11).putLevelGains(Attributes.ATTACK_DAMAGE, 261)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 0).putLevelGains(ModAttributes.DEFENCE.asHolder(), 213)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 9).putLevelGains(ModAttributes.MAGIC.asHolder(), 205)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 1).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 210)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_LOVE.asHolder(), 10)
                    .xp(15).tamingChance(0.15f),
            new GateSpawnData.Builder(0, 5).addToBiomeTag(70, RunecraftoryTags.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, BiomeTags.IS_MOUNTAIN, RunecraftoryTags.IS_LUSH, RunecraftoryTags.IS_SAVANNA));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityChipsqueek>> FURPY = regMonster(EntityType.Builder.of(EntityChipsqueek::new, MobCategory.MONSTER).sized(0.65f, 0.95f).clientTrackingRange(8), RuneCraftory.modRes("furpy"),
            0xab8620, 0xf9ffbb,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 28).putLevelGains(Attributes.MAX_HEALTH, 505)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 16).putLevelGains(Attributes.ATTACK_DAMAGE, 279)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 3).putLevelGains(ModAttributes.DEFENCE.asHolder(), 237)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 11).putLevelGains(ModAttributes.MAGIC.asHolder(), 223)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 3).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 231)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_EARTH.asHolder(), 15)
                    .putAttributes(ModAttributes.RES_LOVE.asHolder(), -10)
                    .xp(35).tamingChance(0.1f).setMinLevel(5),
            new GateSpawnData.Builder(0, 30).addToBiomeTag(60, BiomeTags.IS_FOREST, RunecraftoryTags.IS_SANDY, RunecraftoryTags.IS_SAVANNA));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityMineralSqueek>> MINERAL_SQUEEK = regMonster(EntityType.Builder.of(EntityMineralSqueek::new, MobCategory.MONSTER).sized(0.65f, 0.95f).clientTrackingRange(8), RuneCraftory.modRes("mineral_squeek"),
            0xfa5a74, 0xf9ffbb,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 10)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 13).putLevelGains(Attributes.ATTACK_DAMAGE, 250)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 0)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 9).putLevelGains(ModAttributes.MAGIC.asHolder(), 200)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 0)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .xp(15).tamingChance(0.15f),
            new GateSpawnData.Builder(0, 60).addToBiomeTag(12, BiomeTags.IS_HILL, BiomeTags.IS_MOUNTAIN));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityCluckadoodle>> CLUCKADOODLE = regMonster(EntityType.Builder.of(EntityCluckadoodle::new, MobCategory.MONSTER).sized(0.6f, 1.1f).clientTrackingRange(8), RuneCraftory.modRes("cluckadoodle"),
            0xc2c2c2, 0xdc2121,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 19).putLevelGains(Attributes.MAX_HEALTH, 495)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 13).putLevelGains(Attributes.ATTACK_DAMAGE, 265)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 0).putLevelGains(ModAttributes.DEFENCE.asHolder(), 231)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 3).putLevelGains(ModAttributes.MAGIC.asHolder(), 200)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 0).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 225)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_FIRE.asHolder(), -25)
                    .putAttributes(ModAttributes.RES_LOVE.asHolder(), 10)
                    .xp(20).tamingChance(0.15f),
            new GateSpawnData.Builder(0, 5).addToBiomeTag(70, RunecraftoryTags.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, BiomeTags.IS_MOUNTAIN));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityPommePomme>> POMME_POMME = regMonster(EntityType.Builder.of(EntityPommePomme::new, MobCategory.MONSTER).sized(1.0f, 1.6f).clientTrackingRange(8), RuneCraftory.modRes("pomme_pomme"),
            0xff1c2b, 0xf7b4b8,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 24).putLevelGains(Attributes.MAX_HEALTH, 515)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 10).putLevelGains(Attributes.ATTACK_DAMAGE, 248)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 0).putLevelGains(ModAttributes.DEFENCE.asHolder(), 251)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 9).putLevelGains(ModAttributes.MAGIC.asHolder(), 215)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 0).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 242)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_EARTH.asHolder(), 15)
                    .putAttributes(ModAttributes.RES_WIND.asHolder(), -25)
                    .xp(30).tamingChance(0.1f).setRideable(),
            new GateSpawnData.Builder(0, 5).addToBiomeTag(70, RunecraftoryTags.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, RunecraftoryTags.IS_MAGICAL));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityPommePomme>> MINO = regMonster(EntityType.Builder.of(EntityPommePomme::new, MobCategory.MONSTER).sized(0.9f, 1.8f).clientTrackingRange(8), RuneCraftory.modRes("mino"),
            0x8b573d, 0xc0916d,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 28).putLevelGains(Attributes.MAX_HEALTH, 525)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 14).putLevelGains(Attributes.ATTACK_DAMAGE, 255)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 2).putLevelGains(ModAttributes.DEFENCE.asHolder(), 268)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 10).putLevelGains(ModAttributes.MAGIC.asHolder(), 220)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 2).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 259)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_EARTH.asHolder(), 15)
                    .putAttributes(ModAttributes.RES_WIND.asHolder(), -10)
                    .xp(40).tamingChance(0.05f).setMinLevel(5).setRideable(),
            new GateSpawnData.Builder(0, 15).addToBiomeTag(80, BiomeTags.IS_FOREST, BiomeTags.IS_TAIGA, RunecraftoryTags.IS_MAGICAL));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityNappie>> NAPPIE = regMonster(EntityType.Builder.of(EntityNappie::new, MobCategory.MONSTER).sized(1.0f, 1.6f).clientTrackingRange(8), RuneCraftory.modRes("nappie"),
            0xb4843c, 0x1b5a0d,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 30).putLevelGains(Attributes.MAX_HEALTH, 525)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 13).putLevelGains(Attributes.ATTACK_DAMAGE, 249)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 3).putLevelGains(ModAttributes.DEFENCE.asHolder(), 259)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 14).putLevelGains(ModAttributes.MAGIC.asHolder(), 247)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 2).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 268)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_EARTH.asHolder(), 15)
                    .putAttributes(ModAttributes.RES_WIND.asHolder(), -25)
                    .xp(30).tamingChance(0.1f).setRideable(),
            new GateSpawnData.Builder(0, 25).addToBiomeTag(70, RunecraftoryTags.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, RunecraftoryTags.IS_BEACH, RunecraftoryTags.IS_MAGICAL));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityTortas>> TORTAS = regMonster(EntityType.Builder.of(EntityTortas::new, MobCategory.MONSTER).sized(1.4f, 0.70f).clientTrackingRange(8), RuneCraftory.modRes("tortas"),
            0x5c6682, 0xa5848c,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 28).putLevelGains(Attributes.MAX_HEALTH, 520)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 15).putLevelGains(Attributes.ATTACK_DAMAGE, 269)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 2).putLevelGains(ModAttributes.DEFENCE.asHolder(), 261)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 9).putLevelGains(ModAttributes.MAGIC.asHolder(), 208)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 0).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 244)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_FIRE.asHolder(), -25)
                    .putAttributes(ModAttributes.RES_WATER.asHolder(), 15)
                    .xp(50).tamingChance(0.05f).setRideable().setMinLevel(5),
            new GateSpawnData.Builder(0, 12).canSpawnUnderwater().addToBiomeTag(70, RunecraftoryTags.IS_BEACH, RunecraftoryTags.IS_WATER));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntitySkyFish>> SKY_FISH = regMonster(EntityType.Builder.of(EntitySkyFish::new, MobCategory.MONSTER).sized(1.2f, 0.7f).clientTrackingRange(8), RuneCraftory.modRes("sky_fish"),
            0x8fa4c5, 0x5a3536,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 23).putLevelGains(Attributes.MAX_HEALTH, 497)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 9).putLevelGains(Attributes.ATTACK_DAMAGE, 203)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 0).putLevelGains(ModAttributes.DEFENCE.asHolder(), 218)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 14).putLevelGains(ModAttributes.MAGIC.asHolder(), 285)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 1).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 231)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_FIRE.asHolder(), -25)
                    .putAttributes(ModAttributes.RES_WATER.asHolder(), 15)
                    .xp(50).tamingChance(0.05f).setRideable().setFlying()
                    .withRideActionCosts(new EntityRideActionCosts.Builder().secondCost(0.5f, true)),
            new GateSpawnData.Builder(0, 7).canSpawnUnderwater().addToBiomeTag(60, RunecraftoryTags.IS_BEACH, RunecraftoryTags.IS_WATER));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityWeagle>> WEAGLE = regMonster(EntityType.Builder.of(EntityWeagle::new, MobCategory.MONSTER).sized(0.8f, 1.1f).clientTrackingRange(8), RuneCraftory.modRes("weagle"),
            0x8e127b, 0xdb9dd2, true,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 22).putLevelGains(Attributes.MAX_HEALTH, 510)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 11).putLevelGains(Attributes.ATTACK_DAMAGE, 255)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 0).putLevelGains(ModAttributes.DEFENCE.asHolder(), 231)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 8).putLevelGains(ModAttributes.MAGIC.asHolder(), 210)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 1).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 227)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_WIND.asHolder(), 15)
                    .putAttributes(ModAttributes.RES_EARTH.asHolder(), -10)
                    .xp(45).tamingChance(0.05f).setRideable().doesntNeedBarnRoof().setFlying(),
            new GateSpawnData.Builder(0, 7).addToBiomeTag(50, RunecraftoryTags.IS_PLAINS, RunecraftoryTags.IS_PEAK, RunecraftoryTags.IS_SLOPE, BiomeTags.IS_MOUNTAIN, BiomeTags.IS_HILL));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityGoblin>> GOBLIN = regMonster(EntityType.Builder.of(EntityGoblin::new, MobCategory.MONSTER).sized(0.6f, 1.6f).clientTrackingRange(8), RuneCraftory.modRes("goblin"),
            0x21b322, 0x462f2a,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 24).putLevelGains(Attributes.MAX_HEALTH, 500)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 13).putLevelGains(Attributes.ATTACK_DAMAGE, 267)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 0).putLevelGains(ModAttributes.DEFENCE.asHolder(), 240)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 10).putLevelGains(ModAttributes.MAGIC.asHolder(), 228)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 0).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 220)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_EARTH.asHolder(), 10)
                    .xp(55).tamingChance(0.05f).setRideable(),
            new GateSpawnData.Builder(0, 7).addToBiomeTag(70, RunecraftoryTags.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, BiomeTags.IS_MOUNTAIN, RunecraftoryTags.IS_SAVANNA, RunecraftoryTags.IS_SANDY));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityGoblinArcher>> GOBLIN_ARCHER = regMonster(EntityType.Builder.of(EntityGoblinArcher::new, MobCategory.MONSTER).sized(0.6f, 1.6f).clientTrackingRange(8), RuneCraftory.modRes("goblin_archer"),
            0x21b322, 0x462f2a,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 24).putLevelGains(Attributes.MAX_HEALTH, 500)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 13).putLevelGains(Attributes.ATTACK_DAMAGE, 267)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 0).putLevelGains(ModAttributes.DEFENCE.asHolder(), 240)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 10).putLevelGains(ModAttributes.MAGIC.asHolder(), 228)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 0).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 220)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_EARTH.asHolder(), 10)
                    .xp(55).tamingChance(0.05f).setRideable(),
            new GateSpawnData.Builder(0, 7).addToBiomeTag(70, RunecraftoryTags.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, BiomeTags.IS_MOUNTAIN, RunecraftoryTags.IS_SAVANNA, RunecraftoryTags.IS_SANDY));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityGoblinPirate>> GOBLIN_PIRATE = regMonster(EntityType.Builder.of(EntityGoblinPirate::new, MobCategory.MONSTER).sized(0.6f, 1.6f).clientTrackingRange(8), RuneCraftory.modRes("goblin_pirate"),
            0x484209, 0x29307f,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 26).putLevelGains(Attributes.MAX_HEALTH, 521)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 17).putLevelGains(Attributes.ATTACK_DAMAGE, 288)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 1).putLevelGains(ModAttributes.DEFENCE.asHolder(), 258)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 4).putLevelGains(ModAttributes.MAGIC.asHolder(), 237)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 1).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 246)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_EARTH.asHolder(), 10)
                    .putAttributes(ModAttributes.RES_WATER.asHolder(), 5)
                    .xp(50).tamingChance(0.05f).setRideable(),
            new GateSpawnData.Builder(0, 30).addToBiomeTag(50, RunecraftoryTags.IS_HOT, RunecraftoryTags.IS_SAVANNA, RunecraftoryTags.IS_SANDY, BiomeTags.IS_BADLANDS));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityGoblinGangster>> GOBLIN_GANGSTER = regMonster(EntityType.Builder.of(EntityGoblinGangster::new, MobCategory.MONSTER).sized(0.6f, 1.6f).clientTrackingRange(8), RuneCraftory.modRes("goblin_gangster"),
            0x6e5d2d, 0x316275,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 26).putLevelGains(Attributes.MAX_HEALTH, 521)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 17).putLevelGains(Attributes.ATTACK_DAMAGE, 288)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 1).putLevelGains(ModAttributes.DEFENCE.asHolder(), 258)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 4).putLevelGains(ModAttributes.MAGIC.asHolder(), 237)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 1).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 246)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_EARTH.asHolder(), 10)
                    .putAttributes(ModAttributes.RES_FIRE.asHolder(), 5)
                    .xp(50).tamingChance(0.05f).setRideable(),
            new GateSpawnData.Builder(0, 30).addToBiomeTag(50, RunecraftoryTags.IS_HOT, RunecraftoryTags.IS_SAVANNA, RunecraftoryTags.IS_SANDY, BiomeTags.IS_BADLANDS));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityGoblinPirate>> GOBLIN_CAPTAIN = regMonster(EntityType.Builder.of(EntityGoblinPirate::new, MobCategory.MONSTER).sized(0.6f, 1.6f).clientTrackingRange(8), RuneCraftory.modRes("captain_goblin"),
            0x452621, 0xe9e9e9,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 30).putLevelGains(Attributes.MAX_HEALTH, 544)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 21).putLevelGains(Attributes.ATTACK_DAMAGE, 301)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 2).putLevelGains(ModAttributes.DEFENCE.asHolder(), 255)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 16).putLevelGains(ModAttributes.MAGIC.asHolder(), 253)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 1).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 249)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_EARTH.asHolder(), 10)
                    .putAttributes(ModAttributes.RES_WATER.asHolder(), 10)
                    .xp(50).tamingChance(0.05f).setRideable(),
            new GateSpawnData.Builder(0, 50).addToBiomeTag(50, RunecraftoryTags.IS_HOT, RunecraftoryTags.IS_SAVANNA, RunecraftoryTags.IS_SANDY, BiomeTags.IS_BADLANDS, BiomeTags.IS_HILL));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityGoblinGangster>> GOBLIN_DON = regMonster(EntityType.Builder.of(EntityGoblinGangster::new, MobCategory.MONSTER).sized(0.6f, 1.6f).clientTrackingRange(8), RuneCraftory.modRes("goblin_don"),
            0x474133, 0x286c83,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 30).putLevelGains(Attributes.MAX_HEALTH, 544)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 21).putLevelGains(Attributes.ATTACK_DAMAGE, 301)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 2).putLevelGains(ModAttributes.DEFENCE.asHolder(), 255)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 16).putLevelGains(ModAttributes.MAGIC.asHolder(), 253)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 1).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 249)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_EARTH.asHolder(), 10)
                    .putAttributes(ModAttributes.RES_FIRE.asHolder(), 10)
                    .xp(50).tamingChance(0.05f).setRideable(),
            new GateSpawnData.Builder(0, 50).addToBiomeTag(40, RunecraftoryTags.IS_HOT, RunecraftoryTags.IS_SAVANNA, RunecraftoryTags.IS_SANDY, BiomeTags.IS_BADLANDS, BiomeTags.IS_HILL));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityDuck>> DUCK = regMonster(EntityType.Builder.of(EntityDuck::new, MobCategory.MONSTER).sized(0.65f, 1.35f).clientTrackingRange(8), RuneCraftory.modRes("duck"),
            0xdabf33, 0x845242,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 23).putLevelGains(Attributes.MAX_HEALTH, 530)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 13).putLevelGains(Attributes.ATTACK_DAMAGE, 263)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 0).putLevelGains(ModAttributes.DEFENCE.asHolder(), 225)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 6).putLevelGains(ModAttributes.MAGIC.asHolder(), 240)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 0).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 235)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_EARTH.asHolder(), -10)
                    .putAttributes(ModAttributes.RES_WATER.asHolder(), 10)
                    .xp(50).tamingChance(0.1f).setRideable(),
            new GateSpawnData.Builder(0, 7).addToBiomeTag(40, RunecraftoryTags.IS_PLAINS, RunecraftoryTags.IS_WATER, BiomeTags.IS_BEACH));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityFairy>> FAIRY = regMonster(EntityType.Builder.of(EntityFairy::new, MobCategory.MONSTER).sized(0.45f, 1.1f).clientTrackingRange(8), RuneCraftory.modRes("fairy"),
            0x4dad2a, 0xcdc41f, true,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 19).putLevelGains(Attributes.MAX_HEALTH, 485)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 10).putLevelGains(Attributes.ATTACK_DAMAGE, 210)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 0).putLevelGains(ModAttributes.DEFENCE.asHolder(), 215)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 16).putLevelGains(ModAttributes.MAGIC.asHolder(), 280)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 2).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 228)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_LOVE.asHolder(), 10)
                    .putAttributes(ModAttributes.RES_LIGHT.asHolder(), 10)
                    .putAttributes(ModAttributes.RES_DARK.asHolder(), -10)
                    .xp(66).tamingChance(0.05f).setFlying()
                    .withRideActionCosts(new EntityRideActionCosts.Builder(0.5f, true).secondCost(0.5f, true)),
            new GateSpawnData.Builder(0, 7).addToBiomeTag(50, RunecraftoryTags.IS_PLAINS, BiomeTags.IS_FOREST, RunecraftoryTags.IS_MAGICAL));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityGhost>> GHOST = regMonster(EntityType.Builder.of(EntityGhost::new, MobCategory.MONSTER).sized(0.8f, 2.1f).clientTrackingRange(8), RuneCraftory.modRes("ghost"),
            0x4d3d35, 0x838383, true,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 23).putLevelGains(Attributes.MAX_HEALTH, 510)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 10).putLevelGains(Attributes.ATTACK_DAMAGE, 271)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 1).putLevelGains(ModAttributes.DEFENCE.asHolder(), 213)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 12).putLevelGains(ModAttributes.MAGIC.asHolder(), 263)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 1).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 220)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_LIGHT.asHolder(), -10)
                    .putAttributes(ModAttributes.RES_DARK.asHolder(), 10)
                    .xp(70).tamingChance(0.05f).setFlying(),
            new GateSpawnData.Builder(0, 10).addToBiomeTag(75, RunecraftoryTags.IS_SPOOKY, RunecraftoryTags.IS_DEAD, RunecraftoryTags.IS_SWAMP));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityGhostRay>> GHOST_RAY = regMonster(EntityType.Builder.of(EntityGhostRay::new, MobCategory.MONSTER).sized(1f, 3.2f).clientTrackingRange(8), RuneCraftory.modRes("ghost_ray"),
            0x552217, 0x905a5a, true,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 45).putLevelGains(Attributes.MAX_HEALTH, 540)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 18).putLevelGains(Attributes.ATTACK_DAMAGE, 296)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 2).putLevelGains(ModAttributes.DEFENCE.asHolder(), 232)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 19).putLevelGains(ModAttributes.MAGIC.asHolder(), 289)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 2).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 241)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_LIGHT.asHolder(), -10)
                    .putAttributes(ModAttributes.RES_DARK.asHolder(), 10)
                    .setMinLevel(27)
                    .xp(150).tamingChance(0.02f).setBarnOccupancy(2).setFlying(),
            new GateSpawnData.Builder(0, 15).addToBiomeTag(15, RunecraftoryTags.IS_SPOOKY, RunecraftoryTags.IS_DEAD, RunecraftoryTags.IS_SWAMP));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntitySpirit>> SPIRIT = regMonster(EntityType.Builder.of(EntitySpirit::new, MobCategory.MONSTER).sized(0.5f, 0.6f).clientTrackingRange(8), RuneCraftory.modRes("spirit"),
            0xfdfdfd, 0xc3f8f7, true,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 21).putLevelGains(Attributes.MAX_HEALTH, 497)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 9).putLevelGains(Attributes.ATTACK_DAMAGE, 210)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 1).putLevelGains(ModAttributes.DEFENCE.asHolder(), 210)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 17).putLevelGains(ModAttributes.MAGIC.asHolder(), 284)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 2).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 224)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_LIGHT.asHolder(), -10)
                    .putAttributes(ModAttributes.RES_DARK.asHolder(), 10)
                    .xp(75).tamingChance(0.05f).setFlying(),
            new GateSpawnData.Builder(0, 15).addToBiomeTag(60, RunecraftoryTags.IS_SPOOKY, RunecraftoryTags.IS_DEAD, RunecraftoryTags.IS_SWAMP, RunecraftoryTags.IS_MAGICAL, RunecraftoryTags.IS_END));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityIgnis>> IGNIS = regMonster(EntityType.Builder.of(EntityIgnis::new, MobCategory.MONSTER).sized(0.5f, 0.6f).fireImmune().clientTrackingRange(8), RuneCraftory.modRes("ignis"),
            0xaa3100, 0x9f5e3f, true,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 23).putLevelGains(Attributes.MAX_HEALTH, 502)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 14).putLevelGains(Attributes.ATTACK_DAMAGE, 214)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 2).putLevelGains(ModAttributes.DEFENCE.asHolder(), 219)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 19).putLevelGains(ModAttributes.MAGIC.asHolder(), 291)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 3).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 228)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_FIRE.asHolder(), 25)
                    .putAttributes(ModAttributes.RES_WATER.asHolder(), -25)
                    .xp(75).tamingChance(0.05f).setFlying(),
            new GateSpawnData.Builder(0, 15).addToBiomeTag(60, RunecraftoryTags.IS_SPOOKY, RunecraftoryTags.IS_DEAD, RunecraftoryTags.IS_HOT, BiomeTags.IS_BADLANDS));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntitySpider>> SPIDER = regMonster(EntityType.Builder.of(EntitySpider::new, MobCategory.MONSTER).sized(1.1f, 0.7f).clientTrackingRange(8), RuneCraftory.modRes("spider"),
            0x6f6751, 0x404148,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 19).putLevelGains(Attributes.MAX_HEALTH, 489)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 13).putLevelGains(Attributes.ATTACK_DAMAGE, 267)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 0).putLevelGains(ModAttributes.DEFENCE.asHolder(), 227)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 11).putLevelGains(ModAttributes.MAGIC.asHolder(), 209)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 0).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 222)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_LIGHT.asHolder(), -10)
                    .putAttributes(ModAttributes.RES_FIRE.asHolder(), -10)
                    .putAttributes(ModAttributes.RES_EARTH.asHolder(), 10)
                    .putAttributes(ModAttributes.RES_DARK.asHolder(), 10)
                    .xp(65).tamingChance(0.05f)
                    .withRideActionCosts(new EntityRideActionCosts.Builder(0.5f, true).secondCost(0.5f, true)),
            new GateSpawnData.Builder(0, 15).addToBiomeTag(80, RunecraftoryTags.IS_SPOOKY, BiomeTags.IS_FOREST, BiomeTags.IS_JUNGLE, RunecraftoryTags.IS_LUSH));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityPanther>> SHADOW_PANTHER = regMonster(EntityType.Builder.of(EntityPanther::new, MobCategory.MONSTER).sized(1.3f, 2.2f).clientTrackingRange(8), RuneCraftory.modRes("shadow_panther"),
            0x27375b, 0x733838,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 28).putLevelGains(Attributes.MAX_HEALTH, 515)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 17).putLevelGains(Attributes.ATTACK_DAMAGE, 275)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 2).putLevelGains(ModAttributes.DEFENCE.asHolder(), 225)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 10).putLevelGains(ModAttributes.MAGIC.asHolder(), 238)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 2).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 233)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_LIGHT.asHolder(), -10)
                    .putAttributes(ModAttributes.RES_DARK.asHolder(), 10)
                    .xp(100).tamingChance(0.05f).setRideable(),
            new GateSpawnData.Builder(0, 15).addToBiomeTag(30, RunecraftoryTags.IS_SPOOKY, RunecraftoryTags.IS_SWAMP, RunecraftoryTags.IS_PEAK, RunecraftoryTags.IS_SLOPE));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityMimic>> MONSTER_BOX = regMonster(EntityType.Builder.of(EntityMimic::new, MobCategory.MONSTER).sized(1, 1).clientTrackingRange(8), RuneCraftory.modRes("monster_box"),
            0xac935e, 0x462f10,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 30).putLevelGains(Attributes.MAX_HEALTH, 530)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 15).putLevelGains(Attributes.ATTACK_DAMAGE, 265)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 2).putLevelGains(ModAttributes.DEFENCE.asHolder(), 235)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 13).putLevelGains(ModAttributes.MAGIC.asHolder(), 265)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 1).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 235)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_LIGHT.asHolder(), 10)
                    .putAttributes(ModAttributes.RES_DARK.asHolder(), 10)
                    .putAttributes(ModAttributes.RES_LOVE.asHolder(), 10)
                    .xp(300).tamingChance(0.02f),
            new GateSpawnData.Builder(0, 0));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityMimic>> GOBBLE_BOX = regMonster(EntityType.Builder.of(EntityMimic::new, MobCategory.MONSTER).sized(1, 1).clientTrackingRange(8), RuneCraftory.modRes("gobble_box"),
            0x8f9cc4, 0x343843,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 50).putLevelGains(Attributes.MAX_HEALTH, 550)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 20).putLevelGains(Attributes.ATTACK_DAMAGE, 279)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 3).putLevelGains(ModAttributes.DEFENCE.asHolder(), 254)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 16).putLevelGains(ModAttributes.MAGIC.asHolder(), 279)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 2).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 254)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_LIGHT.asHolder(), 15)
                    .putAttributes(ModAttributes.RES_DARK.asHolder(), 15)
                    .putAttributes(ModAttributes.RES_LOVE.asHolder(), 15)
                    .xp(500).tamingChance(0.015f),
            new GateSpawnData.Builder(0, 20));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityHornet>> HORNET = regMonster(EntityType.Builder.of(EntityHornet::new, MobCategory.MONSTER).sized(0.7f, 0.85f).clientTrackingRange(8), RuneCraftory.modRes("hornet"),
            0x627d73, 0x20201f, true,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 19).putLevelGains(Attributes.MAX_HEALTH, 499)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 11).putLevelGains(Attributes.ATTACK_DAMAGE, 258)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 3).putLevelGains(ModAttributes.DEFENCE.asHolder(), 226)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 8).putLevelGains(ModAttributes.MAGIC.asHolder(), 210)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 4).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 229)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_FIRE.asHolder(), -15)
                    .xp(135).tamingChance(0.05f).setRideable().setMinLevel(5).setFlying(),
            new GateSpawnData.Builder(0, 20).addToBiomeTag(55, RunecraftoryTags.IS_PLAINS, BiomeTags.IS_FOREST));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityWolf>> SILVER_WOLF = regMonster(EntityType.Builder.of(EntityWolf::new, MobCategory.MONSTER).sized(0.8f, 1.15f).clientTrackingRange(8), RuneCraftory.modRes("silver_wolf"),
            0x9bb9c3, 0x436ea1,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 23).putLevelGains(Attributes.MAX_HEALTH, 515)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 12).putLevelGains(Attributes.ATTACK_DAMAGE, 267)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 4).putLevelGains(ModAttributes.DEFENCE.asHolder(), 225)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 7).putLevelGains(ModAttributes.MAGIC.asHolder(), 210)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 2).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 225)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_LIGHT.asHolder(), -5)
                    .putAttributes(ModAttributes.RES_DARK.asHolder(), -5)
                    .putAttributes(ModAttributes.RES_LOVE.asHolder(), 5)
                    .xp(35).tamingChance(0.05f).setRideable().setMinLevel(10),
            new GateSpawnData.Builder(0, 20).addToBiomeTag(45, RunecraftoryTags.IS_PLAINS, BiomeTags.IS_FOREST, RunecraftoryTags.IS_SNOWY));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityLeafBall>> LEAF_BALL = regMonster(EntityType.Builder.of(EntityLeafBall::new, MobCategory.MONSTER).sized(0.8f, 1.2f).clientTrackingRange(8), RuneCraftory.modRes("leaf_ball"),
            0xdcb5f0, 0xb72fd3, true,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 24).putLevelGains(Attributes.MAX_HEALTH, 520)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 9).putLevelGains(Attributes.ATTACK_DAMAGE, 250)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 3).putLevelGains(ModAttributes.DEFENCE.asHolder(), 215)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 14).putLevelGains(ModAttributes.MAGIC.asHolder(), 277)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 4).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 215)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_FIRE.asHolder(), -15)
                    .putAttributes(ModAttributes.RES_WIND.asHolder(), 15)
                    .xp(35).tamingChance(0.05f).setRideable().setMinLevel(10).setFlying()
                    .withRideActionCosts(new EntityRideActionCosts.Builder(0.5f, true).secondCost(0, false)),
            new GateSpawnData.Builder(0, 20).addToBiomeTag(70, RunecraftoryTags.IS_LUSH, BiomeTags.IS_FOREST, RunecraftoryTags.IS_MAGICAL));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityPalmCat>> PALM_CAT = regMonster(EntityType.Builder.of(EntityPalmCat::new, MobCategory.MONSTER).sized(0.6f, 1.9f).clientTrackingRange(8), RuneCraftory.modRes("palm_cat"),
            0xc98f2d, 0xb46d28,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 24).putLevelGains(Attributes.MAX_HEALTH, 509)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 13).putLevelGains(Attributes.ATTACK_DAMAGE, 280)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 2).putLevelGains(ModAttributes.DEFENCE.asHolder(), 233)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 8).putLevelGains(ModAttributes.MAGIC.asHolder(), 230)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 4).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 233)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_EARTH.asHolder(), 15)
                    .putAttributes(ModAttributes.RES_WIND.asHolder(), 15)
                    .xp(35).tamingChance(0.05f),
            new GateSpawnData.Builder(0, 30).addToBiomeTag(40, BiomeTags.IS_FOREST, BiomeTags.IS_TAIGA));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityPalmCat>> MALM_TIGER = regMonster(EntityType.Builder.of(EntityPalmCat::new, MobCategory.MONSTER).sized(0.6f, 1.9f).clientTrackingRange(8), RuneCraftory.modRes("malm_tiger"),
            0x8596ae, 0x3a5573,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 29).putLevelGains(Attributes.MAX_HEALTH, 511)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 20).putLevelGains(Attributes.ATTACK_DAMAGE, 281)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 3).putLevelGains(ModAttributes.DEFENCE.asHolder(), 227)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 13).putLevelGains(ModAttributes.MAGIC.asHolder(), 230)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 4).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 238)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_EARTH.asHolder(), 20)
                    .putAttributes(ModAttributes.RES_WIND.asHolder(), 15)
                    .xp(35).tamingChance(0.05f),
            new GateSpawnData.Builder(0, 60).addToBiomeTag(20, BiomeTags.IS_FOREST, BiomeTags.IS_TAIGA, BiomeTags.IS_HILL, BiomeTags.IS_MOUNTAIN, RunecraftoryTags.IS_SNOWY));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityFlowerLily>> FLOWER_LILY = regMonster(EntityType.Builder.of(EntityFlowerLily::new, MobCategory.MONSTER).sized(0.75f, 1.65f).clientTrackingRange(8), RuneCraftory.modRes("flower_lily"),
            0xe8b3e7, 0x156e12,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 23).putLevelGains(Attributes.MAX_HEALTH, 513)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 16).putLevelGains(Attributes.ATTACK_DAMAGE, 273)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 1).putLevelGains(ModAttributes.DEFENCE.asHolder(), 248)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 14).putLevelGains(ModAttributes.MAGIC.asHolder(), 238)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 1).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 232)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_WIND.asHolder(), 7)
                    .putAttributes(ModAttributes.RES_EARTH.asHolder(), 7)
                    .putAttributes(ModAttributes.RES_FIRE.asHolder(), -10)
                    .xp(40).tamingChance(0.06f).setRideable()
                    .withRideActionCosts(new EntityRideActionCosts.Builder(0.5f, true).secondCost(0, false)),
            new GateSpawnData.Builder(0, 30).addToBiomeTag(60, RunecraftoryTags.IS_LUSH, RunecraftoryTags.IS_MAGICAL, BiomeTags.IS_JUNGLE));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityFlowerLion>> FLOWER_LION = regMonster(EntityType.Builder.of(EntityFlowerLion::new, MobCategory.MONSTER).sized(0.75f, 1.65f).clientTrackingRange(8), RuneCraftory.modRes("flower_lion"),
            0xf2ad7a, 0x893a1d,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 26).putLevelGains(Attributes.MAX_HEALTH, 520)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 18).putLevelGains(Attributes.ATTACK_DAMAGE, 285)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 2).putLevelGains(ModAttributes.DEFENCE.asHolder(), 258)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 16).putLevelGains(ModAttributes.MAGIC.asHolder(), 245)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 2).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 251)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_FIRE.asHolder(), -5)
                    .putAttributes(ModAttributes.RES_WIND.asHolder(), -5)
                    .xp(100).tamingChance(0.05f).setRideable()
                    .withRideActionCosts(new EntityRideActionCosts.Builder().secondCost(0.5f, true)
                            .thirdCost(0, false)),
            new GateSpawnData.Builder(0, 40).addToBiomeTag(60, RunecraftoryTags.IS_HOT, RunecraftoryTags.IS_SAVANNA));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityScorpion>> SCORPION = regMonster(EntityType.Builder.of(EntityScorpion::new, MobCategory.MONSTER).sized(1.1f, 0.6f).clientTrackingRange(8), RuneCraftory.modRes("scorpion"),
            0x606060, 0xacacac,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 21).putLevelGains(Attributes.MAX_HEALTH, 505)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 13).putLevelGains(Attributes.ATTACK_DAMAGE, 271)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 2).putLevelGains(ModAttributes.DEFENCE.asHolder(), 244)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 9).putLevelGains(ModAttributes.MAGIC.asHolder(), 220)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 0).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 238)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.POISON.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_FIRE.asHolder(), -10)
                    .xp(75).tamingChance(0.05f),
            new GateSpawnData.Builder(0, 20).addToBiomeTag(50, RunecraftoryTags.IS_HOT, RunecraftoryTags.IS_SAVANNA, RunecraftoryTags.IS_SANDY, BiomeTags.IS_BADLANDS));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityTroll>> TROLL = regMonster(EntityType.Builder.of(EntityTroll::new, MobCategory.MONSTER).sized(1.5f, 3f).clientTrackingRange(8), RuneCraftory.modRes("troll"),
            0xac924b, 0xcfcbbc,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 35).putLevelGains(Attributes.MAX_HEALTH, 540)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 21).putLevelGains(Attributes.ATTACK_DAMAGE, 290)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 3).putLevelGains(ModAttributes.DEFENCE.asHolder(), 245)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 4).putLevelGains(ModAttributes.MAGIC.asHolder(), 200)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 2).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 235)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 8)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .xp(100).tamingChance(0.05f),
            new GateSpawnData.Builder(0, 40).addToBiomeTag(40, BiomeTags.IS_MOUNTAIN, RunecraftoryTags.IS_SPARSE, RunecraftoryTags.IS_SLOPE, BiomeTags.IS_HILL, RunecraftoryTags.IS_DEAD));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityVeggieGhost>> TOMATO_GHOST = regMonster(EntityType.Builder.of(EntityVeggieGhost::new, MobCategory.MONSTER).sized(0.75f, 1.65f).clientTrackingRange(8), RuneCraftory.modRes("tomato_ghost"),
            0x902323, 0x85268b, true,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 22).putLevelGains(Attributes.MAX_HEALTH, 510)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 16).putLevelGains(Attributes.ATTACK_DAMAGE, 260)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 1).putLevelGains(ModAttributes.DEFENCE.asHolder(), 230)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 18).putLevelGains(ModAttributes.MAGIC.asHolder(), 260)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 2).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 230)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_WIND.asHolder(), -5)
                    .putAttributes(ModAttributes.RES_DARK.asHolder(), 10)
                    .putAttributes(ModAttributes.RES_LIGHT.asHolder(), 10)
                    .xp(100).tamingChance(0.05f).setRideable().setFlying()
                    .withRideActionCosts(new EntityRideActionCosts.Builder().secondCost(0.5f, true)),
            new GateSpawnData.Builder(0, 40).addToBiomeTag(75, RunecraftoryTags.IS_HOT, RunecraftoryTags.IS_DEAD, RunecraftoryTags.IS_WASTELAND, RunecraftoryTags.IS_MAGICAL));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityMage>> LITTLE_EMPEROR = regMonster(EntityType.Builder.of(EntityMage::new, MobCategory.MONSTER).sized(0.6f, 1.7f).clientTrackingRange(8), RuneCraftory.modRes("little_emperor"),
            0x49ab5f, 0xdedede,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 21).putLevelGains(Attributes.MAX_HEALTH, 520)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 13).putLevelGains(Attributes.ATTACK_DAMAGE, 258)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 1).putLevelGains(ModAttributes.DEFENCE.asHolder(), 225)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 19).putLevelGains(ModAttributes.MAGIC.asHolder(), 271)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 4).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 252)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_LIGHT.asHolder(), 15)
                    .putAttributes(ModAttributes.RES_DARK.asHolder(), 15)
                    .xp(35).tamingChance(0.05f),
            new GateSpawnData.Builder(0, 60).addToBiomeTag(40, RunecraftoryTags.IS_SPOOKY, RunecraftoryTags.IS_MAGICAL, BiomeTags.IS_MOUNTAIN));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityDemon>> DEMON = regMonster(EntityType.Builder.of(EntityDemon::new, MobCategory.MONSTER).sized(0.6f, 1.8f).clientTrackingRange(8), RuneCraftory.modRes("demon"),
            0xba8b84, 0x6a5450,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 27).putLevelGains(Attributes.MAX_HEALTH, 515)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 19).putLevelGains(Attributes.ATTACK_DAMAGE, 264)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 3).putLevelGains(ModAttributes.DEFENCE.asHolder(), 235)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 18).putLevelGains(ModAttributes.MAGIC.asHolder(), 269)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 4).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 235)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_LIGHT.asHolder(), -15)
                    .putAttributes(ModAttributes.RES_DARK.asHolder(), 15)
                    .xp(35).tamingChance(0.05f),
            new GateSpawnData.Builder(0, 45).addToBiomeTag(70, RunecraftoryTags.IS_SPOOKY, RunecraftoryTags.IS_MAGICAL, RunecraftoryTags.IS_DEAD, BiomeTags.IS_MOUNTAIN));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityDemon>> ARCH_DEMON = regMonster(EntityType.Builder.of(EntityDemon::new, MobCategory.MONSTER).sized(0.65f, 2.1f).clientTrackingRange(8), RuneCraftory.modRes("arch_demon"),
            0x9f6a63, 0x372321,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 40).putLevelGains(Attributes.MAX_HEALTH, 540)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 22).putLevelGains(Attributes.ATTACK_DAMAGE, 279)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 3).putLevelGains(ModAttributes.DEFENCE.asHolder(), 244)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 20).putLevelGains(ModAttributes.MAGIC.asHolder(), 284)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 5).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 244)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .putAttributes(ModAttributes.RES_LIGHT.asHolder(), -10)
                    .putAttributes(ModAttributes.RES_DARK.asHolder(), 10)
                    .xp(35).tamingChance(0.05f),
            new GateSpawnData.Builder(0, 50).addToBiomeTag(30, RunecraftoryTags.IS_SPOOKY, RunecraftoryTags.IS_MAGICAL, RunecraftoryTags.IS_DEAD, BiomeTags.IS_MOUNTAIN));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityMinotaur>> MINOTAUR = regMonster(EntityType.Builder.of(EntityMinotaur::new, MobCategory.MONSTER).sized(1.4f, 2.9f).clientTrackingRange(8), RuneCraftory.modRes("minotaur"),
            0x61423d, 0x2c2825,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 40).putLevelGains(Attributes.MAX_HEALTH, 525)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 23).putLevelGains(Attributes.ATTACK_DAMAGE, 290)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 4).putLevelGains(ModAttributes.DEFENCE.asHolder(), 245)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 14).putLevelGains(ModAttributes.MAGIC.asHolder(), 230)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 3).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 235)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .xp(35).tamingChance(0.05f),
            new GateSpawnData.Builder(0, 50).addToBiomeTag(60, BiomeTags.IS_MOUNTAIN));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityMinotaur>> MINOTAUR_KING = regMonster(EntityType.Builder.of(EntityMinotaur::new, MobCategory.MONSTER).sized(1.4f, 2.9f).clientTrackingRange(8), RuneCraftory.modRes("minotaur_king"),
            0x344a53, 0x907822,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 70).putLevelGains(Attributes.MAX_HEALTH, 550)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 30).putLevelGains(Attributes.ATTACK_DAMAGE, 305)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 5).putLevelGains(ModAttributes.DEFENCE.asHolder(), 256)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 14).putLevelGains(ModAttributes.MAGIC.asHolder(), 235)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 5).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 248)
                    .putAttributes(ModAttributes.DIZZY.asHolder(), 3)
                    .putAttributes(ModAttributes.CRIT.asHolder(), 1)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 5)
                    .xp(35).tamingChance(0.05f),
            new GateSpawnData.Builder(0, 75).addToBiomeTag(18, BiomeTags.IS_MOUNTAIN));

    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityChimera>> CHIMERA = regBoss(EntityType.Builder.of(EntityChimera::new, MobCategory.MONSTER).sized(1.45f, 1.45f).clientTrackingRange(8), RuneCraftory.modRes("chimera"),
            0x536983, 0xaaa7c8,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 315).putLevelGains(Attributes.MAX_HEALTH, 615)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 18).putLevelGains(Attributes.ATTACK_DAMAGE, 302)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 3).putLevelGains(ModAttributes.DEFENCE.asHolder(), 242)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 15).putLevelGains(ModAttributes.MAGIC.asHolder(), 289)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 2).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 248)
                    .putAttributes(ModAttributes.POISON.asHolder(), 2)
                    .putAttributes(ModAttributes.PARA.asHolder(), 2)
                    .putAttributes(ModAttributes.SEAL.asHolder(), 2)
                    .putAttributes(ModAttributes.RES_LOVE.asHolder(), 15)
                    .putAttributes(ModAttributes.RES_EARTH.asHolder(), -25)
                    .putAttributes(ModAttributes.RES_WIND.asHolder(), -25)
                    .putAttributes(ModAttributes.RES_FIRE.asHolder(), 50)
                    .putAttributes(ModAttributes.RES_WATER.asHolder(), 50)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 25)
                    .putAttributes(ModAttributes.RES_DRAIN.asHolder(), 50)
                    .putAttributes(ModAttributes.RES_DIZZY.asHolder(), 100)
                    .putAttributes(Attributes.KNOCKBACK_RESISTANCE, 1)
                    .putAttributes(ModAttributes.RES_STUN.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_PARA.asHolder(), 95)
                    .putAttributes(ModAttributes.RES_POISON.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_SEAL.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_SLEEP.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_FAT.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_COLD.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_FAINT.asHolder(), 100)
                    .xp(150).tamingChance(BOSS_TAMING_CHANCE).setBarnOccupancy(2).setRideable()
                    .withLevelIncrease(1, 5)
                    .withLevelIncrease(2, 1)
                    .withLevelIncrease(7, 3)
                    .withLevelIncrease(15, 5)
                    .setMinLevel(12)
                    .withSpawnerPredicate(LibAdvancements.playerAdvancementCheck(LibAdvancements.GREATER_DEMON))
                    .withRideActionCosts(new EntityRideActionCosts.Builder().secondCost(0.5f, true)
                            .thirdCost(0.5f, true)));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityRafflesia>> RAFFLESIA = regBoss(EntityType.Builder.of(EntityRafflesia::new, MobCategory.MONSTER).sized(1.15f, 2.8f).clientTrackingRange(8), RuneCraftory.modRes("rafflesia"),
            0x9b58ba, 0x0a8414,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 330).putLevelGains(Attributes.MAX_HEALTH, 625)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 14).putLevelGains(Attributes.ATTACK_DAMAGE, 271)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 3).putLevelGains(ModAttributes.DEFENCE.asHolder(), 238)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 17).putLevelGains(ModAttributes.MAGIC.asHolder(), 291)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 4).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 249)
                    .putAttributes(ModAttributes.RES_EARTH.asHolder(), 15)
                    .putAttributes(ModAttributes.RES_WATER.asHolder(), 15)
                    .putAttributes(ModAttributes.RES_FIRE.asHolder(), 15)
                    .putAttributes(ModAttributes.RES_WIND.asHolder(), -25)
                    .putAttributes(ModAttributes.RES_LIGHT.asHolder(), -15)
                    .putAttributes(ModAttributes.RES_LOVE.asHolder(), -25)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 25)
                    .putAttributes(ModAttributes.RES_DRAIN.asHolder(), 25)
                    .putAttributes(ModAttributes.RES_DIZZY.asHolder(), 100)
                    .putAttributes(Attributes.KNOCKBACK_RESISTANCE, 1)
                    .putAttributes(ModAttributes.RES_STUN.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_PARA.asHolder(), 97)
                    .putAttributes(ModAttributes.RES_POISON.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_SEAL.asHolder(), 80)
                    .putAttributes(ModAttributes.RES_SLEEP.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_FAT.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_COLD.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_FAINT.asHolder(), 100)
                    .xp(350).tamingChance(0).setBarnOccupancy(3).setRideable()
                    .withLevelIncrease(1, 7)
                    .withLevelIncrease(2, 1)
                    .withLevelIncrease(7, 3)
                    .withLevelIncrease(15, 5)
                    .setMinLevel(20)
                    .withSpawnerPredicate(LibAdvancements.playerAdvancementCheck(LibAdvancements.CHIMERA)));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityGrimoire>> GRIMOIRE = regBoss(EntityType.Builder.of(EntityGrimoire::new, MobCategory.MONSTER).sized(3.5f, 4).clientTrackingRange(8), RuneCraftory.modRes("grimoire"),
            0x3b785c, 0x2e4d3f,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 350).putLevelGains(Attributes.MAX_HEALTH, 625)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 20).putLevelGains(Attributes.ATTACK_DAMAGE, 292)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 5).putLevelGains(ModAttributes.DEFENCE.asHolder(), 253)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 18).putLevelGains(ModAttributes.MAGIC.asHolder(), 278)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 4).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 253)
                    .putAttributes(ModAttributes.RES_EARTH.asHolder(), 10)
                    .putAttributes(ModAttributes.RES_WIND.asHolder(), -100)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 25)
                    .putAttributes(ModAttributes.RES_DRAIN.asHolder(), 25)
                    .putAttributes(ModAttributes.RES_DIZZY.asHolder(), 100)
                    .putAttributes(Attributes.KNOCKBACK_RESISTANCE, 1)
                    .putAttributes(ModAttributes.RES_STUN.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_PARA.asHolder(), 97)
                    .putAttributes(ModAttributes.RES_POISON.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_SEAL.asHolder(), 80)
                    .putAttributes(ModAttributes.RES_SLEEP.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_FAT.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_COLD.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_FAINT.asHolder(), 100)
                    .xp(450).tamingChance(BOSS_TAMING_CHANCE).setBarnOccupancy(3).setRideable()
                    .withLevelIncrease(1, 10)
                    .withLevelIncrease(2, 1)
                    .withLevelIncrease(7, 3)
                    .withLevelIncrease(15, 5)
                    .setMinLevel(50)
                    .withSpawnerPredicate(LibAdvancements.playerAdvancementCheck(LibAdvancements.RAFFLESIA)));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityDeadTree>> DEAD_TREE = regBoss(EntityType.Builder.of(EntityDeadTree::new, MobCategory.MONSTER).sized(1.8f, 7f).clientTrackingRange(8), RuneCraftory.modRes("dead_tree"),
            0x3e4a40, 0x227904,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 300).putLevelGains(Attributes.MAX_HEALTH, 640)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 18).putLevelGains(Attributes.ATTACK_DAMAGE, 287)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 3).putLevelGains(ModAttributes.DEFENCE.asHolder(), 246)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 15).putLevelGains(ModAttributes.MAGIC.asHolder(), 282)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 3).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 246)
                    .putAttributes(ModAttributes.RES_FIRE.asHolder(), -100)
                    .putAttributes(ModAttributes.RES_EARTH.asHolder(), 50)
                    .putAttributes(ModAttributes.RES_EARTH.asHolder(), 50)
                    .putAttributes(ModAttributes.RES_WIND.asHolder(), -10)
                    .putAttributes(ModAttributes.RES_LOVE.asHolder(), 25)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 50)
                    .putAttributes(ModAttributes.RES_DRAIN.asHolder(), 20)
                    .putAttributes(ModAttributes.RES_DIZZY.asHolder(), 100)
                    .putAttributes(Attributes.KNOCKBACK_RESISTANCE, 1)
                    .putAttributes(ModAttributes.RES_STUN.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_PARA.asHolder(), 90)
                    .putAttributes(ModAttributes.RES_POISON.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_SEAL.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_SLEEP.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_FAT.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_COLD.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_FAINT.asHolder(), 100)
                    .xp(70).tamingChance(BOSS_TAMING_CHANCE).setBarnOccupancy(3).setRideable()
                    .withLevelIncrease(1, 3)
                    .withLevelIncrease(2, 1)
                    .withLevelIncrease(7, 3)
                    .withLevelIncrease(15, 5)
                    .setMinLevel(5)
                    .withRideActionCosts(new EntityRideActionCosts.Builder().secondCost(0.5f, true)
                            .thirdCost(0.5f, true)));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityRaccoon>> RACCOON = regBoss(EntityType.Builder.of(EntityRaccoon::new, MobCategory.MONSTER).sized(0.9f, 1.5f).clientTrackingRange(8), RuneCraftory.modRes("raccoon"),
            0xcb8055, 0x6d4342,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 290).putLevelGains(Attributes.MAX_HEALTH, 615)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 17).putLevelGains(Attributes.ATTACK_DAMAGE, 283)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 3).putLevelGains(ModAttributes.DEFENCE.asHolder(), 249)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 14).putLevelGains(ModAttributes.MAGIC.asHolder(), 278)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 2).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 255)
                    .putAttributes(ModAttributes.RES_EARTH.asHolder(), 25)
                    .putAttributes(ModAttributes.RES_WIND.asHolder(), 25)
                    .putAttributes(ModAttributes.RES_LOVE.asHolder(), -25)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 25)
                    .putAttributes(ModAttributes.RES_DRAIN.asHolder(), 10)
                    .putAttributes(ModAttributes.RES_DIZZY.asHolder(), 100)
                    .putAttributes(Attributes.KNOCKBACK_RESISTANCE, 1)
                    .putAttributes(ModAttributes.RES_STUN.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_PARA.asHolder(), 90)
                    .putAttributes(ModAttributes.RES_POISON.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_SEAL.asHolder(), 80)
                    .putAttributes(ModAttributes.RES_SLEEP.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_FAT.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_COLD.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_FAINT.asHolder(), 100)
                    .xp(70).tamingChance(BOSS_TAMING_CHANCE).setBarnOccupancy(2).setRideable()
                    .withLevelIncrease(1, 3)
                    .withLevelIncrease(2, 1)
                    .withLevelIncrease(7, 3)
                    .withLevelIncrease(15, 5)
                    .setMinLevel(5)
                    .withRideActionCosts(new EntityRideActionCosts.Builder().secondCost(10, false)
                            .thirdCost(0.5f, true)));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntitySkelefang>> SKELEFANG = regBoss(EntityType.Builder.of(EntitySkelefang::new, MobCategory.MONSTER).sized(1.95f, 3).clientTrackingRange(8), RuneCraftory.modRes("skelefang"),
            0x615237, 0xc2a982,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 275).putLevelGains(Attributes.MAX_HEALTH, 600)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 19).putLevelGains(Attributes.ATTACK_DAMAGE, 294)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 4).putLevelGains(ModAttributes.DEFENCE.asHolder(), 261)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 16).putLevelGains(ModAttributes.MAGIC.asHolder(), 264)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 3).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 237)
                    .putAttributes(ModAttributes.RES_FIRE.asHolder(), -25)
                    .putAttributes(ModAttributes.RES_EARTH.asHolder(), 50)
                    .putAttributes(ModAttributes.RES_DARK.asHolder(), 200)
                    .putAttributes(ModAttributes.RES_LIGHT.asHolder(), -50)
                    .putAttributes(ModAttributes.RES_LOVE.asHolder(), -25)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 25)
                    .putAttributes(ModAttributes.RES_DRAIN.asHolder(), 30)
                    .putAttributes(ModAttributes.RES_DIZZY.asHolder(), 100)
                    .putAttributes(Attributes.KNOCKBACK_RESISTANCE, 1)
                    .putAttributes(ModAttributes.RES_STUN.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_PARA.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_POISON.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_SEAL.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_SLEEP.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_FAT.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_COLD.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_FAINT.asHolder(), 100)
                    .xp(150).tamingChance(BOSS_TAMING_CHANCE).setBarnOccupancy(3).setRideable()
                    .withLevelIncrease(1, 5)
                    .withLevelIncrease(2, 1)
                    .withLevelIncrease(7, 3)
                    .withLevelIncrease(15, 5)
                    .setMinLevel(12)
                    .withSpawnerPredicate(LibAdvancements.playerAdvancementCheck(LibAdvancements.RACCOON)));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityAmbrosia>> AMBROSIA = regBoss(EntityType.Builder.of(EntityAmbrosia::new, MobCategory.MONSTER).sized(0.85f, 2.3f).clientTrackingRange(8), RuneCraftory.modRes("ambrosia"),
            0x00ff00, 0xe600e6,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 300).putLevelGains(Attributes.MAX_HEALTH, 630)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 16).putLevelGains(Attributes.ATTACK_DAMAGE, 275)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 3).putLevelGains(ModAttributes.DEFENCE.asHolder(), 230)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 14).putLevelGains(ModAttributes.MAGIC.asHolder(), 280)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 3).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 245)
                    .putAttributes(ModAttributes.RES_EARTH.asHolder(), 25)
                    .putAttributes(ModAttributes.RES_WIND.asHolder(), -25)
                    .putAttributes(ModAttributes.RES_LOVE.asHolder(), -25)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 25)
                    .putAttributes(ModAttributes.RES_DRAIN.asHolder(), 50)
                    .putAttributes(ModAttributes.RES_DIZZY.asHolder(), 100)
                    .putAttributes(Attributes.KNOCKBACK_RESISTANCE, 1)
                    .putAttributes(ModAttributes.RES_STUN.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_PARA.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_POISON.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_SEAL.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_SLEEP.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_FAT.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_COLD.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_FAINT.asHolder(), 100)
                    .xp(70).tamingChance(BOSS_TAMING_CHANCE).setBarnOccupancy(2).setRideable().setFlying()
                    .withLevelIncrease(1, 3)
                    .withLevelIncrease(2, 1)
                    .withLevelIncrease(7, 3)
                    .withLevelIncrease(15, 5)
                    .setMinLevel(5)
                    .withRideActionCosts(new EntityRideActionCosts.Builder().secondCost(0.5f, true)
                            .thirdCost(0.5f, true)));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityThunderbolt>> THUNDERBOLT = regBoss(EntityType.Builder.of(EntityThunderbolt::new, MobCategory.MONSTER).sized(1.6f, 1.8f).clientTrackingRange(8), RuneCraftory.modRes("thunderbolt"),
            0x212121, 0x2f1177,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 325).putLevelGains(Attributes.MAX_HEALTH, 610)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 20).putLevelGains(Attributes.ATTACK_DAMAGE, 280)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 3).putLevelGains(ModAttributes.DEFENCE.asHolder(), 240)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 16).putLevelGains(ModAttributes.MAGIC.asHolder(), 272)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 2).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 240)
                    .putAttributes(ModAttributes.RES_WIND.asHolder(), 50)
                    .putAttributes(ModAttributes.RES_EARTH.asHolder(), -25)
                    .putAttributes(ModAttributes.RES_LOVE.asHolder(), -25)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 25)
                    .putAttributes(ModAttributes.RES_DRAIN.asHolder(), 30)
                    .putAttributes(ModAttributes.RES_DIZZY.asHolder(), 100)
                    .putAttributes(Attributes.KNOCKBACK_RESISTANCE, 1)
                    .putAttributes(ModAttributes.RES_STUN.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_PARA.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_POISON.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_SEAL.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_SLEEP.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_FAT.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_COLD.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_FAINT.asHolder(), 100)
                    .xp(150).tamingChance(BOSS_TAMING_CHANCE).setBarnOccupancy(2).setRideable()
                    .withLevelIncrease(1, 5)
                    .withLevelIncrease(2, 1)
                    .withLevelIncrease(7, 3)
                    .withLevelIncrease(15, 5)
                    .setMinLevel(12)
                    .withSpawnerPredicate(LibAdvancements.playerAdvancementCheck(LibAdvancements.AMBROSIA))
                    .withRideActionCosts(new EntityRideActionCosts.Builder()
                            .thirdCost(0.5f, true)));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityMarionetta>> MARIONETTA = regBoss(EntityType.Builder.of(EntityMarionetta::new, MobCategory.MONSTER).sized(0.8f, 2.6f).clientTrackingRange(8), RuneCraftory.modRes("marionetta"),
            0xb86b13, 0xd8d7d7,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 320).putLevelGains(Attributes.MAX_HEALTH, 620)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 23).putLevelGains(Attributes.ATTACK_DAMAGE, 286)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 4).putLevelGains(ModAttributes.DEFENCE.asHolder(), 247)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 18).putLevelGains(ModAttributes.MAGIC.asHolder(), 279)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 3).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 247)
                    .putAttributes(ModAttributes.RES_DARK.asHolder(), 25)
                    .putAttributes(ModAttributes.RES_LIGHT.asHolder(), -25)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 25)
                    .putAttributes(ModAttributes.RES_DRAIN.asHolder(), 30)
                    .putAttributes(ModAttributes.RES_DIZZY.asHolder(), 100)
                    .putAttributes(Attributes.KNOCKBACK_RESISTANCE, 1)
                    .putAttributes(ModAttributes.RES_STUN.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_PARA.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_POISON.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_SEAL.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_SLEEP.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_FAT.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_COLD.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_FAINT.asHolder(), 100)
                    .xp(250).tamingChance(0.005f).setBarnOccupancy(2).setRideable()
                    .withLevelIncrease(1, 7)
                    .withLevelIncrease(2, 1)
                    .withLevelIncrease(7, 3)
                    .withLevelIncrease(15, 5)
                    .setMinLevel(20)
                    .withSpawnerPredicate(LibAdvancements.playerAdvancementCheck(LibAdvancements.THUNDERBOLT))
                    .withRideActionCosts(new EntityRideActionCosts.Builder()
                            .thirdCost(0.5f, true)));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityHandonetta>> HANDONETTA = regBoss(EntityType.Builder.of(EntityHandonetta::new, MobCategory.MONSTER).sized(2.3f, 2.8f).clientTrackingRange(8), RuneCraftory.modRes("handonetta"),
            0xffffff, 0x631123, true,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 300).putLevelGains(Attributes.MAX_HEALTH, 610)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 24).putLevelGains(Attributes.ATTACK_DAMAGE, 290)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 3).putLevelGains(ModAttributes.DEFENCE.asHolder(), 245)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 17).putLevelGains(ModAttributes.MAGIC.asHolder(), 276)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 3).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 245)
                    .putAttributes(ModAttributes.RES_DARK.asHolder(), 25)
                    .putAttributes(ModAttributes.RES_LIGHT.asHolder(), -25)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 25)
                    .putAttributes(ModAttributes.RES_DRAIN.asHolder(), 30)
                    .putAttributes(ModAttributes.RES_DIZZY.asHolder(), 100)
                    .putAttributes(Attributes.KNOCKBACK_RESISTANCE, 1)
                    .putAttributes(ModAttributes.RES_STUN.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_PARA.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_POISON.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_SEAL.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_SLEEP.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_FAT.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_COLD.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_FAINT.asHolder(), 100)
                    .xp(250).tamingChance(0.005f).setBarnOccupancy(2).setRideable().setFlying()
                    .withLevelIncrease(1, 7)
                    .withLevelIncrease(2, 1)
                    .withLevelIncrease(7, 3)
                    .withLevelIncrease(15, 5)
                    .setMinLevel(20)
                    .withSpawnerPredicate(LibAdvancements.playerAdvancementCheck(LibAdvancements.THUNDERBOLT))
                    .withRideActionCosts(new EntityRideActionCosts.Builder()
                            .thirdCost(0.5f, true)));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntitySano>> SANO = regBoss(EntityType.Builder.of(EntitySano::new, MobCategory.MONSTER).sized(3, 4.1f).clientTrackingRange(8), RuneCraftory.modRes("sano"),
            0xa18c4a, 0xa82626,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 280).putLevelGains(Attributes.MAX_HEALTH, 650)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 14).putLevelGains(Attributes.ATTACK_DAMAGE, 260)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 5).putLevelGains(ModAttributes.DEFENCE.asHolder(), 259)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 18).putLevelGains(ModAttributes.MAGIC.asHolder(), 282)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 4).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 249)
                    .putAttributes(ModAttributes.RES_FIRE.asHolder(), 50)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 25)
                    .putAttributes(ModAttributes.RES_DRAIN.asHolder(), 25)
                    .putAttributes(ModAttributes.RES_DIZZY.asHolder(), 100)
                    .putAttributes(Attributes.KNOCKBACK_RESISTANCE, 1)
                    .putAttributes(ModAttributes.RES_STUN.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_PARA.asHolder(), 97)
                    .putAttributes(ModAttributes.RES_POISON.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_SEAL.asHolder(), 80)
                    .putAttributes(ModAttributes.RES_SLEEP.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_FAT.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_COLD.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_FAINT.asHolder(), 100)
                    .xp(200).setBarnOccupancy(3).setRideable()
                    .withLevelIncrease(1, 6)
                    .withLevelIncrease(2, 1)
                    .withLevelIncrease(7, 3)
                    .withLevelIncrease(15, 5)
                    .setMinLevel(50)
                    .withSpawnerPredicate(LibAdvancements.playerAdvancementCheck(LibAdvancements.MARIONETTA)));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityUno>> UNO = regBoss(EntityType.Builder.of(EntityUno::new, MobCategory.MONSTER).sized(3, 4.1f).clientTrackingRange(8), RuneCraftory.modRes("uno"),
            0xa18c4a, 0x1b7f9c,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 280).putLevelGains(Attributes.MAX_HEALTH, 650)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 14).putLevelGains(Attributes.ATTACK_DAMAGE, 260)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 4).putLevelGains(ModAttributes.DEFENCE.asHolder(), 259)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 18).putLevelGains(ModAttributes.MAGIC.asHolder(), 282)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 5).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 249)
                    .putAttributes(ModAttributes.RES_WATER.asHolder(), 50)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 25)
                    .putAttributes(ModAttributes.RES_DRAIN.asHolder(), 25)
                    .putAttributes(ModAttributes.RES_DIZZY.asHolder(), 100)
                    .putAttributes(Attributes.KNOCKBACK_RESISTANCE, 1)
                    .putAttributes(ModAttributes.RES_STUN.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_PARA.asHolder(), 97)
                    .putAttributes(ModAttributes.RES_POISON.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_SEAL.asHolder(), 80)
                    .putAttributes(ModAttributes.RES_SLEEP.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_FAT.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_COLD.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_FAINT.asHolder(), 100)
                    .xp(200).setBarnOccupancy(3).setRideable()
                    .withLevelIncrease(1, 6)
                    .withLevelIncrease(2, 1)
                    .withLevelIncrease(7, 3)
                    .withLevelIncrease(15, 5)
                    .setMinLevel(50)
                    .withSpawnerPredicate(LibAdvancements.playerAdvancementCheck(LibAdvancements.MARIONETTA)));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<SanoAndUnoDuo>> SANO_AND_UNO = regEnsemble(EntityType.Builder.of(SanoAndUnoDuo::new, MobCategory.MISC).noSummon().noSave().sized(0.01f, 0.01f), RuneCraftory.modRes("sano_and_uno"), 0xa18c4a, 0xa236d9);
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntitySarcophagus>> SARCOPHAGUS = regBoss(EntityType.Builder.of(EntitySarcophagus::new, MobCategory.MONSTER).sized(1.1f, 3.5f).clientTrackingRange(8), RuneCraftory.modRes("sarcophagus"),
            0x482f27, 0xf3d07f,
            new EntityProperties.Builder()
                    .putAttributes(Attributes.MAX_HEALTH, 340).putLevelGains(Attributes.MAX_HEALTH, 633)
                    .putAttributes(Attributes.ATTACK_DAMAGE, 16).putLevelGains(Attributes.ATTACK_DAMAGE, 276)
                    .putAttributes(ModAttributes.DEFENCE.asHolder(), 5).putLevelGains(ModAttributes.DEFENCE.asHolder(), 243)
                    .putAttributes(ModAttributes.MAGIC.asHolder(), 20).putLevelGains(ModAttributes.MAGIC.asHolder(), 280)
                    .putAttributes(ModAttributes.MAGIC_DEFENCE.asHolder(), 5).putLevelGains(ModAttributes.MAGIC_DEFENCE.asHolder(), 243)
                    .putAttributes(ModAttributes.RES_LOVE.asHolder(), -25)
                    .putAttributes(ModAttributes.RES_CRIT.asHolder(), 25)
                    .putAttributes(ModAttributes.RES_DRAIN.asHolder(), 25)
                    .putAttributes(ModAttributes.RES_DIZZY.asHolder(), 100)
                    .putAttributes(Attributes.KNOCKBACK_RESISTANCE, 1)
                    .putAttributes(ModAttributes.RES_STUN.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_PARA.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_POISON.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_SEAL.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_SLEEP.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_FAT.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_COLD.asHolder(), 100)
                    .putAttributes(ModAttributes.RES_FAINT.asHolder(), 100)
                    .xp(450).tamingChance(BOSS_TAMING_CHANCE).setBarnOccupancy(2).setRideable()
                    .withLevelIncrease(1, 12)
                    .withLevelIncrease(2, 1)
                    .withLevelIncrease(7, 3)
                    .withLevelIncrease(15, 5)
                    .setMinLevel(70)
                    .withSpawnerPredicate(LibAdvancements.playerAdvancementCheck(LibAdvancements.MARIONETTA)));

    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityNPCBase>> NPC = npc(EntityType.Builder.of(EntityNPCBase::new, MobCategory.MISC).sized(0.6f, 1.8f).clientTrackingRange(8), RuneCraftory.modRes("npc"));

    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityTreasureChest>> TREASURE_CHEST = treasureChest(EntityType.Builder.of(EntityTreasureChest::new, MobCategory.MISC).sized(1, 1).clientTrackingRange(4), RuneCraftory.modRes("treasure_chest"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityMobArrow>> ARROW = reg(EntityType.Builder.<EntityMobArrow>of(EntityMobArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20), RuneCraftory.modRes("arrow"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntitySpore>> SPORE = reg(EntityType.Builder.<EntitySpore>of(EntitySpore::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(4), RuneCraftory.modRes("spore"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityWindGust>> GUST = reg(EntityType.Builder.<EntityWindGust>of(EntityWindGust::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(4), RuneCraftory.modRes("gust"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityStone>> STONE = reg(EntityType.Builder.<EntityStone>of(EntityStone::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("stone"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityStatusBall>> STATUS_BALL = reg(EntityType.Builder.<EntityStatusBall>of(EntityStatusBall::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(4), RuneCraftory.modRes("status_ball"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityAmbrosiaWave>> AMBROSIA_WAVE = reg(EntityType.Builder.<EntityAmbrosiaWave>of(EntityAmbrosiaWave::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4), RuneCraftory.modRes("ambrosia_wave"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityButterfly>> BUTTERFLY = reg(EntityType.Builder.<EntityButterfly>of(EntityButterfly::new, MobCategory.MISC).sized(0.2f, 0.2f).clientTrackingRange(4), RuneCraftory.modRes("butterfly"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityPollenPuff>> POLLEN_PUFF = reg(EntityType.Builder.<EntityPollenPuff>of(EntityPollenPuff::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("pollen_puff"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityPollen>> POLLEN = reg(EntityType.Builder.<EntityPollen>of(EntityPollen::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4), RuneCraftory.modRes("pollen"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityThiccLightningBolt>> LIGHTNING_ORB_BOLT = reg(EntityType.Builder.<EntityThiccLightningBolt>of(EntityThiccLightningBolt::new, MobCategory.MISC).sized(0.8f, 0.8f).clientTrackingRange(4), RuneCraftory.modRes("lightning_orb_bolt"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityThunderboltBeam>> LIGHTNING_BEAM = reg(EntityType.Builder.<EntityThunderboltBeam>of(EntityThunderboltBeam::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("lightning_beam"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityElementalTrail>> ELEMENTAL_TRAIL = reg(EntityType.Builder.<EntityElementalTrail>of(EntityElementalTrail::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("elemental_trail"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntitySpiderWeb>> SPIDER_WEB = reg(EntityType.Builder.<EntitySpiderWeb>of(EntitySpiderWeb::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(4), RuneCraftory.modRes("spider_web"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityDarkBeam>> DARK_BEAM = reg(EntityType.Builder.<EntityDarkBeam>of(EntityDarkBeam::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4), RuneCraftory.modRes("dark_beam"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityCards>> CARDS = reg(EntityType.Builder.<EntityCards>of(EntityCards::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(4), RuneCraftory.modRes("cards"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityFurniture>> FURNITURE = reg(EntityType.Builder.<EntityFurniture>of(EntityFurniture::new, MobCategory.MISC).sized(1f, 1f).clientTrackingRange(4), RuneCraftory.modRes("furniture"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityMarionettaTrap>> TRAP_CHEST = reg(EntityType.Builder.<EntityMarionettaTrap>of(EntityMarionettaTrap::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(4), RuneCraftory.modRes("trap_chest"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityElementalBall>> ELEMENTAL_BALL = reg(EntityType.Builder.<EntityElementalBall>of(EntityElementalBall::new, MobCategory.MISC).sized(0.2f, 0.2f).clientTrackingRange(4), RuneCraftory.modRes("elemental_ball"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityFireball>> FIRE_BALL = reg(EntityType.Builder.<EntityFireball>of(EntityFireball::new, MobCategory.MISC).sized(0.2f, 0.2f).clientTrackingRange(4), RuneCraftory.modRes("fireball"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityExplosionSpell>> EXPLOSION = reg(EntityType.Builder.<EntityExplosionSpell>of(EntityExplosionSpell::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("explosion"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityWaterLaser>> WATER_LASER = reg(EntityType.Builder.<EntityWaterLaser>of(EntityWaterLaser::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("water_laser"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityRockSpear>> ROCK_SPEAR = reg(EntityType.Builder.<EntityRockSpear>of(EntityRockSpear::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4), RuneCraftory.modRes("rock_spear"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityWindBlade>> WIND_BLADE = reg(EntityType.Builder.<EntityWindBlade>of(EntityWindBlade::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("wind_blade"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityLightBall>> LIGHT_BALL = reg(EntityType.Builder.<EntityLightBall>of(EntityLightBall::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(4), RuneCraftory.modRes("light_ball"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityDarkBall>> DARK_BALL = reg(EntityType.Builder.<EntityDarkBall>of(EntityDarkBall::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("dark_ball"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityDarkness>> DARKNESS = reg(EntityType.Builder.<EntityDarkness>of(EntityDarkness::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4), RuneCraftory.modRes("darkness"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityBigPlate>> BIG_PLATE = reg(EntityType.Builder.<EntityBigPlate>of(EntityBigPlate::new, MobCategory.MISC).sized(1.5f, 0.3f).clientTrackingRange(4), RuneCraftory.modRes("big_plate"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityDarkBullet>> DARK_BULLET = reg(EntityType.Builder.<EntityDarkBullet>of(EntityDarkBullet::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("dark_bullet"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityPoisonNeedle>> POISON_NEEDLE = reg(EntityType.Builder.<EntityPoisonNeedle>of(EntityPoisonNeedle::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("poison_needle"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntitySleepAura>> SLEEP_AURA = reg(EntityType.Builder.<EntitySleepAura>of(EntitySleepAura::new, MobCategory.MISC).sized(1.5f, 1).clientTrackingRange(4), RuneCraftory.modRes("sleep_aura"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityBullet>> CIRCLING_BULLET = reg(EntityType.Builder.<EntityBullet>of(EntityBullet::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("circling_bullet"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityThrownItem>> THROWN_ITEM = reg(EntityType.Builder.<EntityThrownItem>of(EntityThrownItem::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("thrown_item"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityAppleProjectile>> APPLE = reg(EntityType.Builder.<EntityAppleProjectile>of(EntityAppleProjectile::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("apple"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntitySlashResidue>> SLASH_RESIDUE = reg(EntityType.Builder.<EntitySlashResidue>of(EntitySlashResidue::new, MobCategory.MISC).sized(1.3f, 1.3f).clientTrackingRange(4), RuneCraftory.modRes("slash_residue"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntitySmallRaccoonLeaf>> SMALL_RACCOON_LEAF = reg(EntityType.Builder.<EntitySmallRaccoonLeaf>of(EntitySmallRaccoonLeaf::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("small_raccoon_leaf"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityBigRaccoonLeaf>> BIG_RACCOON_LEAF = reg(EntityType.Builder.<EntityBigRaccoonLeaf>of(EntityBigRaccoonLeaf::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(4), RuneCraftory.modRes("big_raccoon_leaf"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityBoneNeedle>> BONE_NEEDLE = reg(EntityType.Builder.<EntityBoneNeedle>of(EntityBoneNeedle::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("bone_needle"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityHomingEnergyOrb>> ENERGY_ORB = reg(EntityType.Builder.<EntityHomingEnergyOrb>of(EntityHomingEnergyOrb::new, MobCategory.MISC).sized(0.9f, 0.9f).clientTrackingRange(4), RuneCraftory.modRes("energy_orb"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntitySpike>> HOMING_SPIKES = reg(EntityType.Builder.<EntitySpike>of(EntitySpike::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("homing_spikes"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityPowerWave>> POWER_WAVE = reg(EntityType.Builder.<EntityPowerWave>of(EntityPowerWave::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("power_wave"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityGustRocks>> GUST_ROCK = reg(EntityType.Builder.<EntityGustRocks>of(EntityGustRocks::new, MobCategory.MISC).sized(0.01f, 0.01f).clientTrackingRange(4), RuneCraftory.modRes("gust_rocks"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityTornado>> TORNADO = reg(EntityType.Builder.<EntityTornado>of(EntityTornado::new, MobCategory.MISC).sized(1.5f, 4.5f).clientTrackingRange(4), RuneCraftory.modRes("tornado"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityLightBeam>> LIGHT_BEAM = reg(EntityType.Builder.<EntityLightBeam>of(EntityLightBeam::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4), RuneCraftory.modRes("light_beam"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityMissile>> MISSILE = reg(EntityType.Builder.<EntityMissile>of(EntityMissile::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("missile"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityStarfall>> STARFALL = reg(EntityType.Builder.<EntityStarfall>of(EntityStarfall::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("star_fall"));

    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityRuney>> RUNEY = reg(EntityType.Builder.of(EntityRuney::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("runey"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityRuneOrb>> STAT_BONUS = reg(EntityType.Builder.of(EntityRuneOrb::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), RuneCraftory.modRes("rune_orb"));

    public static final RegistryEntrySupplier<EntityType<?>, EntityType<SporeCircleSummoner>> SPORE_CIRCLE_SUMMONER = reg(EntityType.Builder.<SporeCircleSummoner>of(SporeCircleSummoner::new, MobCategory.MISC).sized(0.01f, 0.01f).noSummon().clientTrackingRange(4), RuneCraftory.modRes("spore_circle_summoner"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityButterflySummoner>> BUTTERFLY_SUMMONER = reg(EntityType.Builder.<EntityButterflySummoner>of(EntityButterflySummoner::new, MobCategory.MISC).sized(0.01f, 0.01f).noSummon().clientTrackingRange(4), RuneCraftory.modRes("butterfly_summoner"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityDarkBulletSummoner>> DARK_BULLET_SUMMONER = reg(EntityType.Builder.<EntityDarkBulletSummoner>of(EntityDarkBulletSummoner::new, MobCategory.MISC).sized(0.01f, 0.01f).noSummon().clientTrackingRange(4), RuneCraftory.modRes("dark_bullet_summoner"));
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

    public static final RegistryEntrySupplier<EntityType<?>, EntityType<SarcophagusTeleporter>> SARCOPHAGUS_TELEPORTER = reg(EntityType.Builder.of(SarcophagusTeleporter::new, MobCategory.MISC).sized(1f, 1f).clientTrackingRange(4), RuneCraftory.modRes("sarcophagus_teleporter"));

    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityCustomFishingHook>> FISHING_HOOK = reg(EntityType.Builder.<EntityCustomFishingHook>of(EntityCustomFishingHook::new, MobCategory.MISC).noSave().noSummon().sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(5), RuneCraftory.modRes("fishing_hook"));

    public static final RegistryEntrySupplier<EntityType<?>, EntityType<MultiPartEntity>> MULTIPART = reg(EntityType.Builder.<MultiPartEntity>of(MultiPartEntity::new, MobCategory.MISC).noSave().noSummon().sized(0.25F, 0.25F), RuneCraftory.modRes("multipart_entity"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityRafflesiaHorseTail>> RAFFLESIA_HORSETAIL = reg(EntityType.Builder.<EntityRafflesiaHorseTail>of(EntityRafflesiaHorseTail::new, MobCategory.MISC).noSummon().sized(0.5F, 2.1F), RuneCraftory.modRes("rafflesia_horse_tail"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityRafflesiaFlower>> RAFFLESIA_FLOWER = reg(EntityType.Builder.<EntityRafflesiaFlower>of(EntityRafflesiaFlower::new, MobCategory.MISC).noSummon().sized(0.5F, 1.2F), RuneCraftory.modRes("rafflesia_flower"));
    public static final RegistryEntrySupplier<EntityType<?>, EntityType<EntityRafflesiaPitcher>> RAFFLESIA_PITCHER = reg(EntityType.Builder.<EntityRafflesiaPitcher>of(EntityRafflesiaPitcher::new, MobCategory.MISC).noSummon().sized(0.5F, 1.8F), RuneCraftory.modRes("rafflesia_pitcher"));

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

        cons.accept(NPC.get(), EntityNPCBase.createAttributes());

        cons.accept(RAFFLESIA_HORSETAIL.get(), EntityRafflesiaPart.createAttributes());
        cons.accept(RAFFLESIA_FLOWER.get(), EntityRafflesiaPart.createAttributes());
        cons.accept(RAFFLESIA_PITCHER.get(), EntityRafflesiaPart.createAttributes());
    }

    public static <V extends Entity> RegistryEntrySupplier<EntityType<?>, EntityType<V>> reg(EntityType.Builder<V> v, ResourceLocation name) {
        return ENTITIES.register(name.getPath(), () -> v.build(name.getPath()));
    }

    public static <V extends Entity> RegistryEntrySupplier<EntityType<?>, EntityType<V>> regEnsemble(EntityType.Builder<V> v, ResourceLocation name, int primary, int secondary) {
        RegistryEntrySupplier<EntityType<?>, EntityType<V>> reg = reg(v, name);
        ModItems.ITEMS.register(name.getPath() + "_spawn_egg", () -> new EnsembleEggItem(reg, primary, secondary, new Item.Properties().tab(ModCreativeModTabs.MONSTERS)));
        return reg;
    }

    public static <V extends Mob> RegistryEntrySupplier<EntityType<?>, EntityType<V>> regWithEgg(EntityType.Builder<V> v, ResourceLocation name, int primary, int secondary) {
        RegistryEntrySupplier<EntityType<?>, EntityType<V>> reg = reg(v, name);
        ModItems.ITEMS.register(name.getPath() + "_spawn_egg", () -> new RuneCraftoryEggItem(reg, primary, secondary, new Item.Properties().tab(ModCreativeModTabs.MONSTERS)));
        return reg;
    }

    public static <V extends Entity> RegistryEntrySupplier<EntityType<?>, EntityType<V>> treasureChest(EntityType.Builder<V> v, ResourceLocation name) {
        RegistryEntrySupplier<EntityType<?>, EntityType<V>> reg = reg(v, name);
        ModItems.ITEMS.register(name.getPath() + "_spawn_egg", () -> new TreasureChestSpawnegg(reg, new Item.Properties().tab(ModCreativeModTabs.MONSTERS)));
        return reg;
    }

    public static <V extends Mob> RegistryEntrySupplier<EntityType<?>, EntityType<V>> npc(EntityType.Builder<V> v, ResourceLocation name) {
        RegistryEntrySupplier<EntityType<?>, EntityType<V>> reg = reg(v, name);
        ModItems.ITEMS.register(name.getPath() + "_spawn_egg", () -> new NPCSpawnEgg(reg, new Item.Properties().tab(ModCreativeModTabs.MONSTERS)));
        return reg;
    }

    public static <V extends BaseMonster> RegistryEntrySupplier<EntityType<?>, EntityType<V>> regMonster(EntityType.Builder<V> v, ResourceLocation name, int primary, int secondary, EntityProperties.Builder props) {
        return regMonster(v, name, primary, secondary, false, props);
    }

    public static <V extends BaseMonster> RegistryEntrySupplier<EntityType<?>, EntityType<V>> regBoss(EntityType.Builder<V> v, ResourceLocation name, int primary, int secondary, EntityProperties.Builder props) {
        return regBoss(v, name, primary, secondary, false, props);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <V extends BaseMonster> RegistryEntrySupplier<EntityType<?>, EntityType<V>> regBoss(EntityType.Builder<V> v, ResourceLocation name, int primary, int secondary, boolean flying, EntityProperties.Builder props) {
        RegistryEntrySupplier<EntityType<?>, EntityType<V>> sup = regMonster(v, name, primary, secondary, flying, props);
        if (Platform.INSTANCE.isDatagen())
            BOSSES.add((RegistryEntrySupplier) sup);
        return sup;
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <V extends BaseMonster> RegistryEntrySupplier<EntityType<?>, EntityType<V>> regMonster(EntityType.Builder<V> v, ResourceLocation name, int primary, int secondary, boolean flying, EntityProperties.Builder props) {
        RegistryEntrySupplier<EntityType<?>, EntityType<V>> sup = regWithEgg(v, name, primary, secondary);
        MONSTERS.add((RegistryEntrySupplier) sup);
        if (Platform.INSTANCE.isDatagen())
            DEFAULT_MOB_PROPERTIES.put(name, props);
        if (flying)
            FLYING_MONSTERS.add((RegistryEntrySupplier) sup);
        return sup;
    }

    public static <V extends BaseMonster> RegistryEntrySupplier<EntityType<?>, EntityType<V>> regMonster(EntityType.Builder<V> v, ResourceLocation name, int primary, int secondary, EntityProperties.Builder props, GateSpawnData.Builder builder) {
        return regMonster(v, name, primary, secondary, false, props, builder);
    }

    public static <V extends BaseMonster> RegistryEntrySupplier<EntityType<?>, EntityType<V>> regMonster(EntityType.Builder<V> v, ResourceLocation name, int primary, int secondary, boolean flying, EntityProperties.Builder props, GateSpawnData.Builder builder) {
        if (Platform.INSTANCE.isDatagen())
            DEFAULT_SPAWN_DATA.put(name, builder.build(name));
        return regMonster(v, name, primary, secondary, flying, props);
    }
}
