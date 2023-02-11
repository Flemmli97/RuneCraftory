package io.github.flemmli97.runecraftory.common.registry;

import com.google.common.collect.ImmutableList;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.RFCreativeTabs;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.config.SpawnConfig;
import io.github.flemmli97.runecraftory.common.config.values.EntityProperties;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.GateEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityAmbrosiaSleep;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityAmbrosiaWave;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityBaseSpellBall;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityBigPlate;
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
import io.github.flemmli97.runecraftory.common.entities.misc.EntityPollen;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityPollenPuff;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityRockSpear;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityRuneOrb;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityRuney;
import io.github.flemmli97.runecraftory.common.entities.misc.EntitySpiderWeb;
import io.github.flemmli97.runecraftory.common.entities.misc.EntitySpore;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityStone;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityThiccLightningBolt;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityThunderboltBeam;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityTreasureChest;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWaterLaser;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWindBlade;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWindGust;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWispFlame;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityAnt;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityBeetle;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityBigMuck;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityBuffamoo;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityChipsqueek;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityCluckadoodle;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityDuck;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityFairy;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityGhost;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityGhostRay;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityGoblin;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityGoblinArcher;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityMimic;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityOrc;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityOrcArcher;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityPanther;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityPommePomme;
import io.github.flemmli97.runecraftory.common.entities.monster.EntitySkyFish;
import io.github.flemmli97.runecraftory.common.entities.monster.EntitySpider;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityTortas;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityWeagle;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityWooly;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityAmbrosia;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityMarionetta;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityThunderbolt;
import io.github.flemmli97.runecraftory.common.entities.monster.wisp.EntitySpirit;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.items.NPCSpawnEgg;
import io.github.flemmli97.runecraftory.common.items.RuneCraftoryEggItem;
import io.github.flemmli97.runecraftory.common.items.TreasureChestSpawnegg;
import io.github.flemmli97.runecraftory.common.lib.LibAttributes;
import io.github.flemmli97.runecraftory.common.lib.LibEntities;
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
import java.util.List;
import java.util.function.BiConsumer;

public class ModEntities {

    public static final PlatformRegistry<EntityType<?>> ENTITIES = PlatformUtils.INSTANCE.of(Registry.ENTITY_TYPE_REGISTRY, RuneCraftory.MODID);
    public static final RegistryEntrySupplier<EntityType<GateEntity>> gate = reg(EntityType.Builder.of(GateEntity::new, MobCategory.MONSTER).sized(0.9f, 0.9f).clientTrackingRange(8), LibEntities.gate);

    private static final List<RegistryEntrySupplier<EntityType<?>>> MONSTERS = new ArrayList<>();
    public static final RegistryEntrySupplier<EntityType<EntityWooly>> wooly = regMonster(EntityType.Builder.of(EntityWooly::new, MobCategory.MONSTER).sized(0.7f, 1.55f).clientTrackingRange(8), LibEntities.wooly,
            0xffffcc, 0xffffff,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 14).putLevelGains(LibAttributes.MAX_HEALTH, 16)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 3).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.6)
                    .putAttributes(LibAttributes.MAGIC, 2).putLevelGains(LibAttributes.MAGIC, 1.2)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.3)
                    .xp(25).money(3).tamingChance(0.25f).setRidable().build(),
            new SpawnConfig.SpawnData.Builder(0, 0).addToBiomeTag(60, ModTags.IS_PLAINS, ModTags.IS_BEACH, BiomeTags.IS_FOREST, BiomeTags.IS_HILL,
                    ModTags.IS_MAGICAL).addToBiomeTag(44, ModTags.IS_SAVANNA, BiomeTags.IS_MOUNTAIN));
    public static final RegistryEntrySupplier<EntityType<EntityOrc>> orc = regMonster(EntityType.Builder.of(EntityOrc::new, MobCategory.MONSTER).sized(0.73f, 2.4f).clientTrackingRange(8), LibEntities.orc,
            0x663300, 0xffbf80,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 22).putLevelGains(LibAttributes.MAX_HEALTH, 14)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 4.5).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.3)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.45)
                    .putAttributes(LibAttributes.MAGIC, 3).putLevelGains(LibAttributes.MAGIC, 0.9)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.2)
                    .xp(35).money(4).tamingChance(0.15f).setRidable().build(),
            new SpawnConfig.SpawnData.Builder(0, 0).addToBiomeTag(50, ModTags.IS_PLAINS, ModTags.IS_BEACH, BiomeTags.IS_FOREST, BiomeTags.IS_HILL,
                    ModTags.IS_MAGICAL).addToBiomeTag(44, ModTags.IS_SAVANNA, BiomeTags.IS_MOUNTAIN));
    public static final RegistryEntrySupplier<EntityType<EntityOrcArcher>> orcArcher = regMonster(EntityType.Builder.of(EntityOrcArcher::new, MobCategory.MONSTER).sized(0.73f, 2.4f).clientTrackingRange(8), LibEntities.orcArcher,
            0x663300, 0xffbf80,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 22).putLevelGains(LibAttributes.MAX_HEALTH, 14.5)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 6).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.2)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.3)
                    .putAttributes(LibAttributes.MAGIC, 3).putLevelGains(LibAttributes.MAGIC, 1)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.2)
                    .xp(35).money(4).tamingChance(0.15f).setRidable().build(),
            new SpawnConfig.SpawnData.Builder(0, 0).addToBiomeTag(50, ModTags.IS_PLAINS, ModTags.IS_BEACH, BiomeTags.IS_FOREST, BiomeTags.IS_HILL,
                    ModTags.IS_MAGICAL, BiomeTags.IS_MOUNTAIN, ModTags.IS_SAVANNA));
    public static final RegistryEntrySupplier<EntityType<EntityAnt>> ant = regMonster(EntityType.Builder.of(EntityAnt::new, MobCategory.MONSTER).sized(1.1f, 0.44f).clientTrackingRange(8), LibEntities.ant,
            0x800000, 0x1a0000,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 18).putLevelGains(LibAttributes.MAX_HEALTH, 13)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 6).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.1)
                    .putAttributes(LibAttributes.DEFENCE, 1).putLevelGains(LibAttributes.DEFENCE, 1.35)
                    .putAttributes(LibAttributes.MAGIC, 4).putLevelGains(LibAttributes.MAGIC, 1)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.2)
                    .xp(35).money(5).tamingChance(0.1f).build(),
            new SpawnConfig.SpawnData.Builder(0, 0).addToBiomeTag(50, ModTags.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, ModTags.IS_LUSH, ModTags.IS_SAVANNA));
    public static final RegistryEntrySupplier<EntityType<EntityBeetle>> beetle = regMonster(EntityType.Builder.of(EntityBeetle::new, MobCategory.MONSTER).clientTrackingRange(8), LibEntities.beetle,
            0x000000, 0x800000,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 24).putLevelGains(LibAttributes.MAX_HEALTH, 14)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 7).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.5)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.22)
                    .putAttributes(LibAttributes.MAGIC, 4).putLevelGains(LibAttributes.MAGIC, 1.2)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.25)
                    .xp(40).money(3).tamingChance(0.08f).setRidable().setFlying().build(),
            new SpawnConfig.SpawnData.Builder(0, 0).addToBiomeTag(50, ModTags.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, ModTags.IS_LUSH));
    public static final RegistryEntrySupplier<EntityType<EntityBigMuck>> big_muck = regMonster(EntityType.Builder.of(EntityBigMuck::new, MobCategory.MONSTER).sized(0.9f, 1.6f).clientTrackingRange(8), LibEntities.big_muck,
            0xd7ce4a, 0xad5c25,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 19).putLevelGains(LibAttributes.MAX_HEALTH, 17)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 3).putLevelGains(LibAttributes.ATTACK_DAMAGE, 1.3)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.2)
                    .putAttributes(LibAttributes.MAGIC, 7).putLevelGains(LibAttributes.MAGIC, 2.4)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 1).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.4)
                    .xp(40).money(3).tamingChance(0.06f).setRidable().build(),
            new SpawnConfig.SpawnData.Builder(0, 0).addToBiomeTag(40, ModTags.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, ModTags.IS_LUSH, ModTags.IS_MAGICAL, ModTags.IS_MUSHROOM));
    public static final RegistryEntrySupplier<EntityType<EntityBuffamoo>> buffamoo = regMonster(EntityType.Builder.of(EntityBuffamoo::new, MobCategory.MONSTER).sized(1.1f, 1.45f).clientTrackingRange(8), LibEntities.buffamoo,
            0xd8d8d0, 0x4e4e4c,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 15).putLevelGains(LibAttributes.MAX_HEALTH, 18)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 5).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.8)
                    .putAttributes(LibAttributes.MAGIC, 4).putLevelGains(LibAttributes.MAGIC, 1.5)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.4)
                    .xp(40).money(6).tamingChance(0.15f).setRidable().build(),
            new SpawnConfig.SpawnData.Builder(0, 0).addToBiomeTag(50, ModTags.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, BiomeTags.IS_MOUNTAIN, ModTags.IS_LUSH));
    public static final RegistryEntrySupplier<EntityType<EntityChipsqueek>> chipsqueek = regMonster(EntityType.Builder.of(EntityChipsqueek::new, MobCategory.MONSTER).sized(0.65f, 1.05f).clientTrackingRange(8), LibEntities.chipsqueek,
            0xff3b5b, 0xf9ffbb,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 18).putLevelGains(LibAttributes.MAX_HEALTH, 10)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 5).putLevelGains(LibAttributes.ATTACK_DAMAGE, 1.25)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.2)
                    .putAttributes(LibAttributes.MAGIC, 5).putLevelGains(LibAttributes.MAGIC, 1.25)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.2)
                    .xp(35).money(1).tamingChance(0.12f).build(),
            new SpawnConfig.SpawnData.Builder(0, 0).addToBiomeTag(40, ModTags.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, BiomeTags.IS_MOUNTAIN, ModTags.IS_LUSH, ModTags.IS_SAVANNA));
    public static final RegistryEntrySupplier<EntityType<EntityCluckadoodle>> cluckadoodle = regMonster(EntityType.Builder.of(EntityCluckadoodle::new, MobCategory.MONSTER).sized(0.6f, 1.1f).clientTrackingRange(8), LibEntities.cluckadoodle,
            0xc2c2c2, 0xdc2121,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 16).putLevelGains(LibAttributes.MAX_HEALTH, 12)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 5).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.1)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 0.8)
                    .putAttributes(LibAttributes.MAGIC, 3).putLevelGains(LibAttributes.MAGIC, 1)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 0.7)
                    .xp(35).money(2).tamingChance(0.15f).build(),
            new SpawnConfig.SpawnData.Builder(0, 0).addToBiomeTag(50, ModTags.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, BiomeTags.IS_MOUNTAIN));
    public static final RegistryEntrySupplier<EntityType<EntityPommePomme>> pomme_pomme = regMonster(EntityType.Builder.of(EntityPommePomme::new, MobCategory.MONSTER).sized(1.0f, 1.6f).clientTrackingRange(8), LibEntities.pomme_pomme,
            0xff1c2b, 0xf7b4b8,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 22).putLevelGains(LibAttributes.MAX_HEALTH, 13)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 4).putLevelGains(LibAttributes.ATTACK_DAMAGE, 1.4)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.9)
                    .putAttributes(LibAttributes.MAGIC, 2).putLevelGains(LibAttributes.MAGIC, 1.2)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.2)
                    .xp(40).money(4).tamingChance(0.09f).setRidable().build(),
            new SpawnConfig.SpawnData.Builder(0, 0).addToBiomeTag(50, ModTags.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, ModTags.IS_MAGICAL));
    public static final RegistryEntrySupplier<EntityType<EntityTortas>> tortas = regMonster(EntityType.Builder.of(EntityTortas::new, MobCategory.MONSTER).sized(1.4f, 0.70f).clientTrackingRange(8), LibEntities.tortas,
            0x5c6682, 0xa5848c,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 27).putLevelGains(LibAttributes.MAX_HEALTH, 14)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 7).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.3)
                    .putAttributes(LibAttributes.DEFENCE, 1.2).putLevelGains(LibAttributes.DEFENCE, 2)
                    .putAttributes(LibAttributes.MAGIC, 2).putLevelGains(LibAttributes.MAGIC, 1)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.4)
                    .putAttributes(LibAttributes.RES_FIRE, -25)
                    .xp(65).money(10).tamingChance(0.03f).setRidable().build(),
            new SpawnConfig.SpawnData.Builder(0, 4).addToBiomeTag(50, ModTags.IS_BEACH, ModTags.IS_WATER));
    public static final RegistryEntrySupplier<EntityType<EntitySkyFish>> sky_fish = regMonster(EntityType.Builder.of(EntitySkyFish::new, MobCategory.MONSTER).sized(1.2f, 0.7f).clientTrackingRange(8), LibEntities.sky_fish,
            0x8fa4c5, 0x5a3536,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 18).putLevelGains(LibAttributes.MAX_HEALTH, 12)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 3).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.3)
                    .putAttributes(LibAttributes.MAGIC, 6).putLevelGains(LibAttributes.MAGIC, 2.65)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.7)
                    .putAttributes(LibAttributes.RES_FIRE, -25)
                    .xp(65).money(6).tamingChance(0.02f).setRidable().setFlying().build(),
            new SpawnConfig.SpawnData.Builder(0, 5).addToBiomeTag(40, ModTags.IS_BEACH, ModTags.IS_WATER));
    public static final RegistryEntrySupplier<EntityType<EntityWeagle>> weagle = regMonster(EntityType.Builder.of(EntityWeagle::new, MobCategory.MONSTER).sized(0.8f, 1.1f).clientTrackingRange(8), LibEntities.weagle,
            0x8e127b, 0xdb9dd2,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 15).putLevelGains(LibAttributes.MAX_HEALTH, 16)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 4).putLevelGains(LibAttributes.ATTACK_DAMAGE, 1.4)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.2)
                    .putAttributes(LibAttributes.MAGIC, 5).putLevelGains(LibAttributes.MAGIC, 1.5)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1)
                    .xp(65).money(4).tamingChance(0.03f).setRidable().setFlying().build(),
            new SpawnConfig.SpawnData.Builder(500, 7).addToBiomeTag(30, ModTags.IS_PLAINS, BiomeTags.IS_MOUNTAIN, BiomeTags.IS_HILL));
    public static final RegistryEntrySupplier<EntityType<EntityGoblin>> goblin = regMonster(EntityType.Builder.of(EntityGoblin::new, MobCategory.MONSTER).sized(0.6f, 1.5f).clientTrackingRange(8), LibEntities.goblin,
            0x21b322, 0x462f2a,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 21).putLevelGains(LibAttributes.MAX_HEALTH, 13)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 6).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.4)
                    .putAttributes(LibAttributes.DEFENCE, 0.5).putLevelGains(LibAttributes.DEFENCE, 1.4)
                    .putAttributes(LibAttributes.MAGIC, 4).putLevelGains(LibAttributes.MAGIC, 2)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.3)
                    .xp(70).money(8).tamingChance(0.08f).setRidable().build(),
            new SpawnConfig.SpawnData.Builder(500, 7).addToBiomeTag(30, ModTags.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, BiomeTags.IS_MOUNTAIN, ModTags.IS_SAVANNA, ModTags.IS_SANDY));
    public static final RegistryEntrySupplier<EntityType<EntityGoblinArcher>> goblinArcher = regMonster(EntityType.Builder.of(EntityGoblinArcher::new, MobCategory.MONSTER).sized(0.6f, 1.5f).clientTrackingRange(8), LibEntities.goblinArcher,
            0x21b322, 0x462f2a,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 21).putLevelGains(LibAttributes.MAX_HEALTH, 15)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 6).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.5)
                    .putAttributes(LibAttributes.DEFENCE, 0.5).putLevelGains(LibAttributes.DEFENCE, 1.3)
                    .putAttributes(LibAttributes.MAGIC, 4).putLevelGains(LibAttributes.MAGIC, 1.8)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.2)
                    .xp(70).money(8).tamingChance(0.08f).setRidable().build(),
            new SpawnConfig.SpawnData.Builder(500, 7).addToBiomeTag(30, ModTags.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_HILL, BiomeTags.IS_MOUNTAIN, ModTags.IS_SAVANNA, ModTags.IS_SANDY));
    public static final RegistryEntrySupplier<EntityType<EntityDuck>> duck = regMonster(EntityType.Builder.of(EntityDuck::new, MobCategory.MONSTER).sized(0.65f, 1.45f).clientTrackingRange(8), LibEntities.duck,
            0xdabf33, 0x845242,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 19).putLevelGains(LibAttributes.MAX_HEALTH, 19)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 6).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.8)
                    .putAttributes(LibAttributes.MAGIC, 2).putLevelGains(LibAttributes.MAGIC, 2)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.8)
                    .xp(55).money(5).tamingChance(0.11f).setRidable().build(),
            new SpawnConfig.SpawnData.Builder(0, 0).addToBiomeTag(30, ModTags.IS_PLAINS, ModTags.IS_WATER, BiomeTags.IS_BEACH));
    public static final RegistryEntrySupplier<EntityType<EntityFairy>> fairy = regMonster(EntityType.Builder.of(EntityFairy::new, MobCategory.MONSTER).sized(0.45f, 1.1f).clientTrackingRange(8), LibEntities.fairy,
            0x4dad2a, 0xcdc41f,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 17).putLevelGains(LibAttributes.MAX_HEALTH, 11)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 3).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.5)
                    .putAttributes(LibAttributes.MAGIC, 7.3).putLevelGains(LibAttributes.MAGIC, 2.6)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 1).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.8)
                    .xp(66).money(6).tamingChance(0.06f).setFlying().build(),
            new SpawnConfig.SpawnData.Builder(0, 4).addToBiomeTag(30, ModTags.IS_PLAINS, BiomeTags.IS_FOREST, ModTags.IS_MAGICAL));
    public static final RegistryEntrySupplier<EntityType<EntityGhost>> ghost = regMonster(EntityType.Builder.of(EntityGhost::new, MobCategory.MONSTER).sized(0.8f, 2.1f).clientTrackingRange(8), LibEntities.ghost,
            0x4d3d35, 0x838383,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 22).putLevelGains(LibAttributes.MAX_HEALTH, 14)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 11.6).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.5)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.7)
                    .putAttributes(LibAttributes.MAGIC, 5).putLevelGains(LibAttributes.MAGIC, 2.9)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 2)
                    .xp(70).money(7).tamingChance(0.04f).setFlying().build(),
            new SpawnConfig.SpawnData.Builder(0, 10).addToBiomeTag(60, ModTags.IS_SPOOKY, ModTags.IS_DEAD, ModTags.IS_SWAMP));
    public static final RegistryEntrySupplier<EntityType<EntitySpirit>> spirit = regMonster(EntityType.Builder.of(EntitySpirit::new, MobCategory.MONSTER).sized(0.5f, 0.5f).clientTrackingRange(8), LibEntities.spirit,
            0xfdfdfd, 0xc3f8f7,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 18).putLevelGains(LibAttributes.MAX_HEALTH, 13)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 4).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.6)
                    .putAttributes(LibAttributes.MAGIC, 7.1).putLevelGains(LibAttributes.MAGIC, 2.9)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 2.1)
                    .xp(75).money(5).tamingChance(0.02f).setFlying().build(),
            new SpawnConfig.SpawnData.Builder(500, 3).addToBiomeTag(50, ModTags.IS_SPOOKY, ModTags.IS_DEAD, ModTags.IS_SWAMP, ModTags.IS_MAGICAL, ModTags.IS_END));
    public static final RegistryEntrySupplier<EntityType<EntityGhostRay>> ghostRay = regMonster(EntityType.Builder.of(EntityGhostRay::new, MobCategory.MONSTER).sized(1f, 3.2f).clientTrackingRange(8), LibEntities.ghostRay,
            0x552217, 0x905a5a,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 32).putLevelGains(LibAttributes.MAX_HEALTH, 19)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 14.3).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.9)
                    .putAttributes(LibAttributes.DEFENCE, 1.5).putLevelGains(LibAttributes.DEFENCE, 2.1)
                    .putAttributes(LibAttributes.MAGIC, 9).putLevelGains(LibAttributes.MAGIC, 3.1)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 1.5).putLevelGains(LibAttributes.MAGIC_DEFENCE, 2.1)
                    .xp(150).money(10).tamingChance(0.04f).setBarnOccupancy(2).setFlying().build(),
            new SpawnConfig.SpawnData.Builder(500, 10).addToBiomeTag(10, ModTags.IS_SPOOKY, ModTags.IS_DEAD, ModTags.IS_SWAMP));
    public static final RegistryEntrySupplier<EntityType<EntitySpider>> spider = regMonster(EntityType.Builder.of(EntitySpider::new, MobCategory.MONSTER).sized(0.9f, 0.7f).clientTrackingRange(8), LibEntities.spider,
            0x6f6751, 0x404148,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 17).putLevelGains(LibAttributes.MAX_HEALTH, 11)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 8.5).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.2)
                    .putAttributes(LibAttributes.DEFENCE, 0).putLevelGains(LibAttributes.DEFENCE, 1.5)
                    .putAttributes(LibAttributes.MAGIC, 4).putLevelGains(LibAttributes.MAGIC, 2)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.4)
                    .xp(65).money(8).tamingChance(0.07f).build(),
            new SpawnConfig.SpawnData.Builder(0, 0).addToBiomeTag(60, ModTags.IS_SPOOKY, BiomeTags.IS_FOREST, BiomeTags.IS_JUNGLE, ModTags.IS_LUSH));
    public static final RegistryEntrySupplier<EntityType<EntityPanther>> shadowPanther = regMonster(EntityType.Builder.of(EntityPanther::new, MobCategory.MONSTER).sized(1.3f, 2.2f).clientTrackingRange(8), LibEntities.shadowPanther,
            0x27375b, 0x733838,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 26).putLevelGains(LibAttributes.MAX_HEALTH, 17.6)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 11.5).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.66)
                    .putAttributes(LibAttributes.DEFENCE, 2).putLevelGains(LibAttributes.DEFENCE, 1.8)
                    .putAttributes(LibAttributes.MAGIC, 3).putLevelGains(LibAttributes.MAGIC, 1.9)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 2).putLevelGains(LibAttributes.MAGIC_DEFENCE, 1.6)
                    .xp(100).money(9).tamingChance(0.02f).setRidable().build(),
            new SpawnConfig.SpawnData.Builder(500, 9).addToBiomeTag(10, ModTags.IS_SPOOKY, ModTags.IS_SWAMP, ModTags.IS_PEAK, ModTags.IS_SLOPE));
    public static final RegistryEntrySupplier<EntityType<EntityMimic>> monsterBox = regMonster(EntityType.Builder.of(EntityMimic::new, MobCategory.MONSTER).sized(1, 1).clientTrackingRange(8), LibEntities.monsterBox,
            0xac935e, 0x462f10,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 25).putLevelGains(LibAttributes.MAX_HEALTH, 13)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 6.1).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.4)
                    .putAttributes(LibAttributes.DEFENCE, 3).putLevelGains(LibAttributes.DEFENCE, 2.1)
                    .putAttributes(LibAttributes.MAGIC, 3).putLevelGains(LibAttributes.MAGIC, 2)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0).putLevelGains(LibAttributes.MAGIC_DEFENCE, 2)
                    .xp(300).money(9).tamingChance(0.02f).build(),
            new SpawnConfig.SpawnData.Builder(0, 0));
    public static final RegistryEntrySupplier<EntityType<EntityMimic>> gobbleBox = regMonster(EntityType.Builder.of(EntityMimic::new, MobCategory.MONSTER).sized(1, 1).clientTrackingRange(8), LibEntities.gobbleBox,
            0x8f9cc4, 0x343843,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 30).putLevelGains(LibAttributes.MAX_HEALTH, 17)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 8.3).putLevelGains(LibAttributes.ATTACK_DAMAGE, 2.5)
                    .putAttributes(LibAttributes.DEFENCE, 3.5).putLevelGains(LibAttributes.DEFENCE, 2.15)
                    .putAttributes(LibAttributes.MAGIC, 4).putLevelGains(LibAttributes.MAGIC, 2.3)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 0.5).putLevelGains(LibAttributes.MAGIC_DEFENCE, 2.1)
                    .xp(500).money(9).tamingChance(0.015f).build(),
            new SpawnConfig.SpawnData.Builder(0, 10));

    public static final RegistryEntrySupplier<EntityType<EntityAmbrosia>> ambrosia = regMonster(EntityType.Builder.of(EntityAmbrosia::new, MobCategory.MONSTER).sized(0.85f, 2.3f).clientTrackingRange(8), LibEntities.ambrosia,
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
                    .putAttributes(LibAttributes.RES_PARA, 80)
                    .putAttributes(LibAttributes.RES_POISON, 90)
                    .putAttributes(LibAttributes.RES_SEAL, 90)
                    .putAttributes(LibAttributes.RES_SLEEP, 100)
                    .putAttributes(LibAttributes.RES_DIZZY, 70)
                    .putAttributes(LibAttributes.RES_CRIT, 40)
                    .putAttributes(LibAttributes.RES_STUN, 60)
                    .putAttributes(LibAttributes.RES_FAINT, 95)
                    .putAttributes(LibAttributes.RES_DRAIN, 30)
                    .xp(500).money(50).tamingChance(0.008f).setBarnOccupancy(2).setRidable().setFlying().build());
    public static final RegistryEntrySupplier<EntityType<EntityThunderbolt>> thunderbolt = regMonster(EntityType.Builder.of(EntityThunderbolt::new, MobCategory.MONSTER).sized(1.6f, 1.8f).clientTrackingRange(8), LibEntities.thunderbolt,
            0x212121, 0x2f1177,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 250).putLevelGains(LibAttributes.MAX_HEALTH, 27)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 16).putLevelGains(LibAttributes.ATTACK_DAMAGE, 3.45)
                    .putAttributes(LibAttributes.DEFENCE, 4).putLevelGains(LibAttributes.DEFENCE, 2.3)
                    .putAttributes(LibAttributes.MAGIC, 13).putLevelGains(LibAttributes.MAGIC, 3.1)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 2).putLevelGains(LibAttributes.MAGIC_DEFENCE, 2.1)
                    .putAttributes(LibAttributes.RES_WIND, 25)
                    .putAttributes(LibAttributes.RES_PARA, 85)
                    .putAttributes(LibAttributes.RES_POISON, 90)
                    .putAttributes(LibAttributes.RES_SEAL, 90)
                    .putAttributes(LibAttributes.RES_SLEEP, 100)
                    .putAttributes(LibAttributes.RES_DIZZY, 60)
                    .putAttributes(LibAttributes.RES_CRIT, 40)
                    .putAttributes(LibAttributes.RES_STUN, 50)
                    .putAttributes(LibAttributes.RES_FAINT, 95)
                    .putAttributes(LibAttributes.RES_DRAIN, 25)
                    .xp(650).money(75).tamingChance(0.008f).setBarnOccupancy(2).setRidable().build());
    public static final RegistryEntrySupplier<EntityType<EntityMarionetta>> marionetta = regMonster(EntityType.Builder.of(EntityMarionetta::new, MobCategory.MONSTER).sized(0.8f, 2.5f).clientTrackingRange(8), LibEntities.marionetta,
            0xb86b13, 0xd8d7d7,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.MAX_HEALTH, 300).putLevelGains(LibAttributes.MAX_HEALTH, 30.5)
                    .putAttributes(LibAttributes.ATTACK_DAMAGE, 18).putLevelGains(LibAttributes.ATTACK_DAMAGE, 3.3)
                    .putAttributes(LibAttributes.DEFENCE, 5).putLevelGains(LibAttributes.DEFENCE, 2.3)
                    .putAttributes(LibAttributes.MAGIC, 13.3).putLevelGains(LibAttributes.MAGIC, 3.3)
                    .putAttributes(LibAttributes.MAGIC_DEFENCE, 3.9).putLevelGains(LibAttributes.MAGIC_DEFENCE, 2.3)
                    .putAttributes(LibAttributes.RES_DARK, 25)
                    .putAttributes(LibAttributes.RES_PARA, 75)
                    .putAttributes(LibAttributes.RES_POISON, 95)
                    .putAttributes(LibAttributes.RES_SEAL, 90)
                    .putAttributes(LibAttributes.RES_SLEEP, 100)
                    .putAttributes(LibAttributes.RES_DIZZY, 65)
                    .putAttributes(LibAttributes.RES_CRIT, 35)
                    .putAttributes(LibAttributes.RES_STUN, 60)
                    .putAttributes(LibAttributes.RES_FAINT, 95)
                    .putAttributes(LibAttributes.RES_DRAIN, 20)
                    .xp(900).money(75).tamingChance(0.008f).setBarnOccupancy(2).setRidable().build());

    public static final RegistryEntrySupplier<EntityType<EntityNPCBase>> npc = npc(EntityType.Builder.of(EntityNPCBase::new, MobCategory.MISC).sized(0.6f, 1.8f).clientTrackingRange(8), LibEntities.npc);

    public static final RegistryEntrySupplier<EntityType<EntityTreasureChest>> treasureChest = treasureChest(EntityType.Builder.of(EntityTreasureChest::new, MobCategory.MISC).sized(1, 1).clientTrackingRange(4), LibEntities.treasureChest);
    public static final RegistryEntrySupplier<EntityType<EntityMobArrow>> arrow = reg(EntityType.Builder.<EntityMobArrow>of(EntityMobArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20), LibEntities.arrow);
    public static final RegistryEntrySupplier<EntityType<EntitySpore>> spore = reg(EntityType.Builder.<EntitySpore>of(EntitySpore::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(4), LibEntities.spore);
    public static final RegistryEntrySupplier<EntityType<EntityWindGust>> gust = reg(EntityType.Builder.<EntityWindGust>of(EntityWindGust::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(4), LibEntities.gust);
    public static final RegistryEntrySupplier<EntityType<EntityStone>> stone = reg(EntityType.Builder.<EntityStone>of(EntityStone::new, MobCategory.MISC).sized(0.05f, 0.05f).clientTrackingRange(4), LibEntities.stone);
    public static final RegistryEntrySupplier<EntityType<EntityAmbrosiaSleep>> sleepBall = reg(EntityType.Builder.<EntityAmbrosiaSleep>of(EntityAmbrosiaSleep::new, MobCategory.MISC).sized(0.4f, 0.6f).clientTrackingRange(4), LibEntities.ambrosia_sleep);
    public static final RegistryEntrySupplier<EntityType<EntityAmbrosiaWave>> ambrosiaWave = reg(EntityType.Builder.<EntityAmbrosiaWave>of(EntityAmbrosiaWave::new, MobCategory.MISC).sized(0.05f, 0.05f).clientTrackingRange(4), LibEntities.ambrosia_wave);
    public static final RegistryEntrySupplier<EntityType<EntityButterfly>> butterfly = reg(EntityType.Builder.<EntityButterfly>of(EntityButterfly::new, MobCategory.MISC).sized(0.2f, 0.2f).clientTrackingRange(4), LibEntities.butterfly);
    public static final RegistryEntrySupplier<EntityType<EntityPollenPuff>> pollenPuff = reg(EntityType.Builder.<EntityPollenPuff>of(EntityPollenPuff::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), LibEntities.pollenPuff);
    public static final RegistryEntrySupplier<EntityType<EntityPollen>> pollen = reg(EntityType.Builder.<EntityPollen>of(EntityPollen::new, MobCategory.MISC).sized(0.05f, 0.05f).clientTrackingRange(4), LibEntities.pollen);
    public static final RegistryEntrySupplier<EntityType<EntityThiccLightningBolt>> lightningOrbBolt = reg(EntityType.Builder.<EntityThiccLightningBolt>of(EntityThiccLightningBolt::new, MobCategory.MISC).sized(0.8f, 0.8f).clientTrackingRange(4), LibEntities.lightningOrbBolt);
    public static final RegistryEntrySupplier<EntityType<EntityThunderboltBeam>> lightningBeam = reg(EntityType.Builder.<EntityThunderboltBeam>of(EntityThunderboltBeam::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), LibEntities.lightningBeam);
    public static final RegistryEntrySupplier<EntityType<EntityWispFlame>> wispFlame = reg(EntityType.Builder.<EntityWispFlame>of(EntityWispFlame::new, MobCategory.MISC).sized(0.05f, 0.05f).clientTrackingRange(4), LibEntities.wispFlame);
    public static final RegistryEntrySupplier<EntityType<EntitySpiderWeb>> spiderWeb = reg(EntityType.Builder.<EntitySpiderWeb>of(EntitySpiderWeb::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(4), LibEntities.spiderWeb);
    public static final RegistryEntrySupplier<EntityType<EntityDarkBeam>> darkBeam = reg(EntityType.Builder.<EntityDarkBeam>of(EntityDarkBeam::new, MobCategory.MISC).sized(0.05f, 0.05f).clientTrackingRange(4), LibEntities.darkBeam);
    public static final RegistryEntrySupplier<EntityType<EntityCards>> cards = reg(EntityType.Builder.<EntityCards>of(EntityCards::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(4), LibEntities.cards);
    public static final RegistryEntrySupplier<EntityType<EntityFurniture>> furniture = reg(EntityType.Builder.<EntityFurniture>of(EntityFurniture::new, MobCategory.MISC).sized(0.9f, 0.9f).clientTrackingRange(4), LibEntities.furniture);
    public static final RegistryEntrySupplier<EntityType<EntityMarionettaTrap>> trapChest = reg(EntityType.Builder.<EntityMarionettaTrap>of(EntityMarionettaTrap::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(4), LibEntities.trapChest);
    public static final RegistryEntrySupplier<EntityType<EntityBaseSpellBall>> staffThrown = reg(EntityType.Builder.<EntityBaseSpellBall>of(EntityBaseSpellBall::new, MobCategory.MISC).sized(0.2f, 0.2f).clientTrackingRange(4), LibEntities.baseStaffThrown);
    public static final RegistryEntrySupplier<EntityType<EntityFireball>> fireBall = reg(EntityType.Builder.<EntityFireball>of(EntityFireball::new, MobCategory.MISC).sized(0.2f, 0.2f).clientTrackingRange(4), LibEntities.fireball);
    public static final RegistryEntrySupplier<EntityType<EntityExplosionSpell>> explosion = reg(EntityType.Builder.<EntityExplosionSpell>of(EntityExplosionSpell::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), LibEntities.explosion);
    public static final RegistryEntrySupplier<EntityType<EntityWaterLaser>> waterLaser = reg(EntityType.Builder.<EntityWaterLaser>of(EntityWaterLaser::new, MobCategory.MISC).sized(0.05f, 0.05f).clientTrackingRange(4), LibEntities.waterLaser);
    public static final RegistryEntrySupplier<EntityType<EntityRockSpear>> rockSpear = reg(EntityType.Builder.<EntityRockSpear>of(EntityRockSpear::new, MobCategory.MISC).sized(0.05f, 0.05f).clientTrackingRange(4), LibEntities.rockSpear);
    public static final RegistryEntrySupplier<EntityType<EntityWindBlade>> windBlade = reg(EntityType.Builder.<EntityWindBlade>of(EntityWindBlade::new, MobCategory.MISC).sized(0.05f, 0.05f).clientTrackingRange(4), LibEntities.windBlade);
    public static final RegistryEntrySupplier<EntityType<EntityLightBall>> lightBall = reg(EntityType.Builder.<EntityLightBall>of(EntityLightBall::new, MobCategory.MISC).sized(0.05f, 0.05f).clientTrackingRange(4), LibEntities.lightBall);
    public static final RegistryEntrySupplier<EntityType<EntityDarkBall>> darkBall = reg(EntityType.Builder.<EntityDarkBall>of(EntityDarkBall::new, MobCategory.MISC).sized(0.05f, 0.05f).clientTrackingRange(4), LibEntities.darkBall);
    public static final RegistryEntrySupplier<EntityType<EntityDarkness>> darkness = reg(EntityType.Builder.<EntityDarkness>of(EntityDarkness::new, MobCategory.MISC).sized(0.05f, 0.05f).clientTrackingRange(4), LibEntities.darkness);
    public static final RegistryEntrySupplier<EntityType<EntityBigPlate>> plate = reg(EntityType.Builder.<EntityBigPlate>of(EntityBigPlate::new, MobCategory.MISC).sized(1.5f, 0.3f).clientTrackingRange(4), LibEntities.plate);
    public static final RegistryEntrySupplier<EntityType<EntityDarkBullet>> darkBullet = reg(EntityType.Builder.<EntityDarkBullet>of(EntityDarkBullet::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), LibEntities.darkBullet);

    public static final RegistryEntrySupplier<EntityType<EntityRuney>> runey = reg(EntityType.Builder.of(EntityRuney::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), LibEntities.runey);
    public static final RegistryEntrySupplier<EntityType<EntityRuneOrb>> statBonus = reg(EntityType.Builder.of(EntityRuneOrb::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4), LibEntities.runeOrb);

    public static final RegistryEntrySupplier<EntityType<EntityButterflySummoner>> butterflySummoner = reg(EntityType.Builder.<EntityButterflySummoner>of(EntityButterflySummoner::new, MobCategory.MISC).sized(0.01f, 0.01f).clientTrackingRange(4), LibEntities.butterflySummoner);
    public static final RegistryEntrySupplier<EntityType<EntityDarkBulletSummoner>> darkBulletSummoner = reg(EntityType.Builder.<EntityDarkBulletSummoner>of(EntityDarkBulletSummoner::new, MobCategory.MISC).sized(0.01f, 0.01f).clientTrackingRange(4), LibEntities.darkBulletSummoner);

    public static final RegistryEntrySupplier<EntityType<EntityCustomFishingHook>> fishingHook = reg(EntityType.Builder.<EntityCustomFishingHook>of(EntityCustomFishingHook::new, MobCategory.MISC).noSave().noSummon().sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(5), LibEntities.fishingHook);

    public static List<RegistryEntrySupplier<EntityType<?>>> getMonsters() {
        return ImmutableList.copyOf(MONSTERS);
    }

    public static void registerAttributes(BiConsumer<EntityType<? extends LivingEntity>, AttributeSupplier.Builder> cons) {
        cons.accept(gate.get(), GateEntity.createAttributes());
        cons.accept(wooly.get(), BaseMonster.createAttributes(ModAttributes.ENTITY_ATTRIBUTES));
        cons.accept(orc.get(), BaseMonster.createAttributes(ModAttributes.ENTITY_ATTRIBUTES));
        cons.accept(orcArcher.get(), BaseMonster.createAttributes(ModAttributes.ENTITY_ATTRIBUTES));
        cons.accept(ant.get(), BaseMonster.createAttributes(ModAttributes.ENTITY_ATTRIBUTES));
        cons.accept(beetle.get(), BaseMonster.createAttributes(ModAttributes.ENTITY_ATTRIBUTES));
        cons.accept(big_muck.get(), BaseMonster.createAttributes(ModAttributes.ENTITY_ATTRIBUTES));
        cons.accept(buffamoo.get(), BaseMonster.createAttributes(ModAttributes.ENTITY_ATTRIBUTES));
        cons.accept(chipsqueek.get(), BaseMonster.createAttributes(ModAttributes.ENTITY_ATTRIBUTES));
        cons.accept(cluckadoodle.get(), BaseMonster.createAttributes(ModAttributes.ENTITY_ATTRIBUTES));
        cons.accept(pomme_pomme.get(), BaseMonster.createAttributes(ModAttributes.ENTITY_ATTRIBUTES));
        cons.accept(tortas.get(), BaseMonster.createAttributes(ModAttributes.ENTITY_ATTRIBUTES));
        cons.accept(sky_fish.get(), BaseMonster.createAttributes(ModAttributes.ENTITY_ATTRIBUTES));
        cons.accept(weagle.get(), BaseMonster.createAttributes(ModAttributes.ENTITY_ATTRIBUTES).add(Attributes.FLYING_SPEED));
        cons.accept(goblin.get(), BaseMonster.createAttributes(ModAttributes.ENTITY_ATTRIBUTES));
        cons.accept(goblinArcher.get(), BaseMonster.createAttributes(ModAttributes.ENTITY_ATTRIBUTES));
        cons.accept(duck.get(), BaseMonster.createAttributes(ModAttributes.ENTITY_ATTRIBUTES));
        cons.accept(fairy.get(), BaseMonster.createAttributes(ModAttributes.ENTITY_ATTRIBUTES).add(Attributes.FLYING_SPEED));
        cons.accept(ghost.get(), BaseMonster.createAttributes(ModAttributes.ENTITY_ATTRIBUTES).add(Attributes.FLYING_SPEED));
        cons.accept(spirit.get(), BaseMonster.createAttributes(ModAttributes.ENTITY_ATTRIBUTES).add(Attributes.FLYING_SPEED));
        cons.accept(ghostRay.get(), BaseMonster.createAttributes(ModAttributes.ENTITY_ATTRIBUTES).add(Attributes.FLYING_SPEED));
        cons.accept(spider.get(), BaseMonster.createAttributes(ModAttributes.ENTITY_ATTRIBUTES));
        cons.accept(shadowPanther.get(), BaseMonster.createAttributes(ModAttributes.ENTITY_ATTRIBUTES));
        cons.accept(monsterBox.get(), BaseMonster.createAttributes(ModAttributes.ENTITY_ATTRIBUTES));
        cons.accept(gobbleBox.get(), BaseMonster.createAttributes(ModAttributes.ENTITY_ATTRIBUTES));

        cons.accept(ambrosia.get(), BaseMonster.createAttributes(ModAttributes.ENTITY_ATTRIBUTES));
        cons.accept(thunderbolt.get(), BaseMonster.createAttributes(ModAttributes.ENTITY_ATTRIBUTES));
        cons.accept(marionetta.get(), BaseMonster.createAttributes(ModAttributes.ENTITY_ATTRIBUTES));

        cons.accept(npc.get(), EntityNPCBase.createAttributes(ModAttributes.ENTITY_ATTRIBUTES));
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

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <V extends BaseMonster> RegistryEntrySupplier<EntityType<V>> regMonster(EntityType.Builder<V> v, ResourceLocation name, int primary, int secondary, EntityProperties props) {
        MobConfig.propertiesMap.put(name, props);
        RegistryEntrySupplier<EntityType<V>> sup = regWithEgg(v, name, primary, secondary);
        MONSTERS.add((RegistryEntrySupplier) sup);
        return sup;
    }

    public static <V extends BaseMonster> RegistryEntrySupplier<EntityType<V>> regMonster(EntityType.Builder<V> v, ResourceLocation name, int primary, int secondary, EntityProperties props, SpawnConfig.SpawnData.Builder builder) {
        SpawnConfig.addDefaultData(builder.build(name));
        return regMonster(v, name, primary, secondary, props);
    }
}
