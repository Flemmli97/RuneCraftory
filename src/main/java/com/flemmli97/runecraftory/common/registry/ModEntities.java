package com.flemmli97.runecraftory.common.registry;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.RFCreativeTabs;
import com.flemmli97.runecraftory.common.config.MobConfig;
import com.flemmli97.runecraftory.common.config.SpawnConfig;
import com.flemmli97.runecraftory.common.config.values.EntityProperties;
import com.flemmli97.runecraftory.common.entities.BaseMonster;
import com.flemmli97.runecraftory.common.entities.GateEntity;
import com.flemmli97.runecraftory.common.entities.misc.EntityAmbrosiaSleep;
import com.flemmli97.runecraftory.common.entities.misc.EntityAmbrosiaWave;
import com.flemmli97.runecraftory.common.entities.misc.EntityButterfly;
import com.flemmli97.runecraftory.common.entities.misc.EntityFireball;
import com.flemmli97.runecraftory.common.entities.misc.EntityMobArrow;
import com.flemmli97.runecraftory.common.entities.misc.EntityPollen;
import com.flemmli97.runecraftory.common.entities.misc.EntitySpore;
import com.flemmli97.runecraftory.common.entities.misc.EntityThiccLightningBolt;
import com.flemmli97.runecraftory.common.entities.misc.EntityThunderboltBeam;
import com.flemmli97.runecraftory.common.entities.misc.EntityWaterLaser;
import com.flemmli97.runecraftory.common.entities.misc.EntityWindBlade;
import com.flemmli97.runecraftory.common.entities.misc.EntityWindGust;
import com.flemmli97.runecraftory.common.entities.monster.EntityAnt;
import com.flemmli97.runecraftory.common.entities.monster.EntityBeetle;
import com.flemmli97.runecraftory.common.entities.monster.EntityBigMuck;
import com.flemmli97.runecraftory.common.entities.monster.EntityBuffamoo;
import com.flemmli97.runecraftory.common.entities.monster.EntityChipsqueek;
import com.flemmli97.runecraftory.common.entities.monster.EntityCluckadoodle;
import com.flemmli97.runecraftory.common.entities.monster.EntityGoblin;
import com.flemmli97.runecraftory.common.entities.monster.EntityGoblinArcher;
import com.flemmli97.runecraftory.common.entities.monster.EntityOrc;
import com.flemmli97.runecraftory.common.entities.monster.EntityOrcArcher;
import com.flemmli97.runecraftory.common.entities.monster.EntityPommePomme;
import com.flemmli97.runecraftory.common.entities.monster.EntitySkyFish;
import com.flemmli97.runecraftory.common.entities.monster.EntityTortas;
import com.flemmli97.runecraftory.common.entities.monster.EntityWeagle;
import com.flemmli97.runecraftory.common.entities.monster.EntityWooly;
import com.flemmli97.runecraftory.common.entities.monster.boss.EntityAmbrosia;
import com.flemmli97.runecraftory.common.entities.monster.boss.EntityThunderbolt;
import com.flemmli97.runecraftory.common.items.RuneCraftoryEggItem;
import com.flemmli97.runecraftory.common.lib.LibAttributes;
import com.flemmli97.runecraftory.common.lib.LibEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = RuneCraftory.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, RuneCraftory.MODID);

    public static final RegistryObject<EntityType<GateEntity>> gate = reg(EntityType.Builder.create(GateEntity::new, EntityClassification.MONSTER).size(1, 1).trackingRange(8), LibEntities.gate);
    public static final RegistryObject<EntityType<EntityWooly>> wooly = regMonster(EntityType.Builder.create(EntityWooly::new, EntityClassification.MONSTER).size(0.7f, 1.55f).trackingRange(8), LibEntities.wooly,
            0xffffcc, 0xffffff,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 12).putLevelGains(LibAttributes.GENERIC_MAX_HEALTH, 5)
                    .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 2).putLevelGains(LibAttributes.GENERIC_ATTACK_DAMAGE, 2)
                    .putAttributes(LibAttributes.rf_defence, 0).putLevelGains(LibAttributes.rf_defence, 2.3)
                    .putAttributes(LibAttributes.rf_magic, 2).putLevelGains(LibAttributes.rf_magic, 0.2)
                    .putAttributes(LibAttributes.rf_magic_defence, 0).putLevelGains(LibAttributes.rf_magic_defence, 1)
                    .xp(1).money(3).tamingChance(0.1f).setRidable().build(),
            new SpawnConfig.SpawnData.Builder(0).addToBiomeTypes(50,
                    "PLAINS", "BEACH", "CONIFEROUS", "FOREST", "HILLS", "MAGICAL", "MOUNTAIN", "SAVANNA"));
    public static final RegistryObject<EntityType<EntityOrc>> orc = regMonster(EntityType.Builder.create(EntityOrc::new, EntityClassification.MONSTER).size(0.73f, 2.4f).trackingRange(8), LibEntities.orc,
            0x663300, 0xffbf80,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 20).putLevelGains(LibAttributes.GENERIC_MAX_HEALTH, 4)
                    .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 4).putLevelGains(LibAttributes.GENERIC_ATTACK_DAMAGE, 3.4)
                    .putAttributes(LibAttributes.rf_defence, 0).putLevelGains(LibAttributes.rf_defence, 1.45)
                    .putAttributes(LibAttributes.rf_magic, 3).putLevelGains(LibAttributes.rf_magic, 0.5)
                    .putAttributes(LibAttributes.rf_magic_defence, 0).putLevelGains(LibAttributes.rf_magic_defence, 0.8)
                    .xp(2).money(4).tamingChance(0.05f).setRidable().build(),
            new SpawnConfig.SpawnData.Builder(0).addToBiomeTypes(50,
                    "PLAINS", "BEACH", "CONIFEROUS", "FOREST", "HILLS", "MAGICAL", "MOUNTAIN", "SAVANNA"));
    public static final RegistryObject<EntityType<EntityOrcArcher>> orcArcher = regMonster(EntityType.Builder.create(EntityOrcArcher::new, EntityClassification.MONSTER).size(0.73f, 2.4f).trackingRange(8), LibEntities.orcArcher,
            0x663300, 0xffbf80,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 20).putLevelGains(LibAttributes.GENERIC_MAX_HEALTH, 7)
                    .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 6).putLevelGains(LibAttributes.GENERIC_ATTACK_DAMAGE, 2.9)
                    .putAttributes(LibAttributes.rf_defence, 0).putLevelGains(LibAttributes.rf_defence, 0.7)
                    .putAttributes(LibAttributes.rf_magic, 3).putLevelGains(LibAttributes.rf_magic, 0.4)
                    .putAttributes(LibAttributes.rf_magic_defence, 0).putLevelGains(LibAttributes.rf_magic_defence, 0.9)
                    .xp(2).money(4).tamingChance(0.05f).setRidable().build(),
            new SpawnConfig.SpawnData.Builder(0).addToBiomeTypes(50,
                    "PLAINS", "BEACH", "CONIFEROUS", "FOREST", "HILLS", "MAGICAL", "MOUNTAIN", "SAVANNA"));

    public static final RegistryObject<EntityType<EntityAnt>> ant = regMonster(EntityType.Builder.create(EntityAnt::new, EntityClassification.MONSTER).size(1.1f, 0.44f).trackingRange(8), LibEntities.ant,
            0x800000, 0x1a0000,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 10).putLevelGains(LibAttributes.GENERIC_MAX_HEALTH, 3)
                    .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 6).putLevelGains(LibAttributes.GENERIC_ATTACK_DAMAGE, 2)
                    .putAttributes(LibAttributes.rf_defence, 0).putLevelGains(LibAttributes.rf_defence, 1.8)
                    .putAttributes(LibAttributes.rf_magic, 4).putLevelGains(LibAttributes.rf_defence, 0.5)
                    .putAttributes(LibAttributes.rf_magic_defence, 0).putLevelGains(LibAttributes.rf_defence, 1.1)
                    .xp(2).money(5).tamingChance(0.02f).build(),
            new SpawnConfig.SpawnData.Builder(0).addToBiomeTypes(50,
                    "PLAINS", "CONIFEROUS", "FOREST", "HILLS", "SAVANNA", "LUSH"));
    public static final RegistryObject<EntityType<EntityBeetle>> beetle = regMonster(EntityType.Builder.create(EntityBeetle::new, EntityClassification.MONSTER).trackingRange(8), LibEntities.beetle,
            0x000000, 0x800000,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 18).putLevelGains(LibAttributes.GENERIC_MAX_HEALTH, 3)
                    .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 7).putLevelGains(LibAttributes.GENERIC_ATTACK_DAMAGE, 3)
                    .putAttributes(LibAttributes.rf_defence, 0).putLevelGains(LibAttributes.rf_defence, 0.9)
                    .putAttributes(LibAttributes.rf_magic, 4).putLevelGains(LibAttributes.rf_magic, 1)
                    .putAttributes(LibAttributes.rf_magic_defence, 0).putLevelGains(LibAttributes.rf_magic_defence, 0.75)
                    .xp(2).money(3).tamingChance(0.6f).setRidable().setFlying().build(),
            new SpawnConfig.SpawnData.Builder(0).addToBiomeTypes(50,
                    "PLAINS", "FOREST", "HILLS", "LUSH"));
    public static final RegistryObject<EntityType<EntityBigMuck>> big_muck = regMonster(EntityType.Builder.create(EntityBigMuck::new, EntityClassification.MONSTER).size(0.9f, 1.6f).trackingRange(8), LibEntities.big_muck,
            0xd7ce4a, 0xad5c25,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 16).putLevelGains(LibAttributes.GENERIC_MAX_HEALTH, 4)
                    .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 3).putLevelGains(LibAttributes.GENERIC_ATTACK_DAMAGE, 1.4)
                    .putAttributes(LibAttributes.rf_defence, 0).putLevelGains(LibAttributes.rf_defence, 0.7)
                    .putAttributes(LibAttributes.rf_magic, 7).putLevelGains(LibAttributes.rf_magic, 3)
                    .putAttributes(LibAttributes.rf_magic_defence, 0).putLevelGains(LibAttributes.rf_magic_defence, 1)
                    .xp(2).money(3).tamingChance(0.03f).setRidable().build(),
            new SpawnConfig.SpawnData.Builder(0).addToBiomeTypes(50,
                    "PLAINS", "CONIFEROUS", "FOREST", "HILLS", "MAGICAL", "MUSHROOM"));
    public static final RegistryObject<EntityType<EntityBuffamoo>> buffamoo = regMonster(EntityType.Builder.create(EntityBuffamoo::new, EntityClassification.MONSTER).size(1.1f, 1.45f).trackingRange(8), LibEntities.buffamoo,
            0xd8d8d0, 0x4e4e4c,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 14).putLevelGains(LibAttributes.GENERIC_MAX_HEALTH, 7.5)
                    .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 4).putLevelGains(LibAttributes.GENERIC_ATTACK_DAMAGE, 2.3)
                    .putAttributes(LibAttributes.rf_defence, 0).putLevelGains(LibAttributes.rf_defence, 1.8)
                    .putAttributes(LibAttributes.rf_magic, 4).putLevelGains(LibAttributes.rf_magic, 2)
                    .putAttributes(LibAttributes.rf_magic_defence, 0).putLevelGains(LibAttributes.rf_magic_defence, 0.9)
                    .xp(1).money(6).tamingChance(0.1f).setRidable().build(),
            new SpawnConfig.SpawnData.Builder(0).addToBiomeTypes(50,
                    "PLAINS", "FOREST", "HILLS", "MOUNTAIN", "SAVANNA"));
    public static final RegistryObject<EntityType<EntityChipsqueek>> chipsqueek = regMonster(EntityType.Builder.create(EntityChipsqueek::new, EntityClassification.MONSTER).size(0.65f, 1.05f).trackingRange(8), LibEntities.chipsqueek,
            0xff3b5b, 0xf9ffbb,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 7).putLevelGains(LibAttributes.GENERIC_MAX_HEALTH, 1.2)
                    .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 5).putLevelGains(LibAttributes.GENERIC_ATTACK_DAMAGE, 1)
                    .putAttributes(LibAttributes.rf_defence, 0).putLevelGains(LibAttributes.rf_defence, 0.95)
                    .putAttributes(LibAttributes.rf_magic, 5).putLevelGains(LibAttributes.rf_magic, 1.2)
                    .putAttributes(LibAttributes.rf_magic_defence, 0).putLevelGains(LibAttributes.rf_magic_defence, 0.93)
                    .xp(3).money(1).tamingChance(0.08f).build(),
            new SpawnConfig.SpawnData.Builder(0).addToBiomeTypes(50,
                    "PLAINS", "FOREST", "HILLS", "MOUNTAIN", "SAVANNA", "LUSH"));
    public static final RegistryObject<EntityType<EntityCluckadoodle>> cluckadoodle = regMonster(EntityType.Builder.create(EntityCluckadoodle::new, EntityClassification.MONSTER).size(0.6f, 1.1f).trackingRange(8), LibEntities.cluckadoodle,
            0xc2c2c2, 0xdc2121,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 6).putLevelGains(LibAttributes.GENERIC_MAX_HEALTH, 2.15)
                    .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 5).putLevelGains(LibAttributes.GENERIC_ATTACK_DAMAGE, 3)
                    .putAttributes(LibAttributes.rf_defence, 0).putLevelGains(LibAttributes.rf_defence, 0.8)
                    .putAttributes(LibAttributes.rf_magic, 3).putLevelGains(LibAttributes.rf_magic, 1)
                    .putAttributes(LibAttributes.rf_magic_defence, 0).putLevelGains(LibAttributes.rf_magic_defence, 0.7)
                    .xp(3).money(2).tamingChance(0.1f).build(),
            new SpawnConfig.SpawnData.Builder(0).addToBiomeTypes(50,
                    "PLAINS", "FOREST", "HILLS", "MOUNTAIN"));
    public static final RegistryObject<EntityType<EntityPommePomme>> pomme_pomme = regMonster(EntityType.Builder.create(EntityPommePomme::new, EntityClassification.MONSTER).size(1.0f, 1.6f).trackingRange(8), LibEntities.pomme_pomme,
            0xff1c2b, 0xf7b4b8,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 13).putLevelGains(LibAttributes.GENERIC_MAX_HEALTH, 5.4)
                    .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 4).putLevelGains(LibAttributes.GENERIC_ATTACK_DAMAGE, 1.6)
                    .putAttributes(LibAttributes.rf_defence, 0).putLevelGains(LibAttributes.rf_defence, 2.4)
                    .putAttributes(LibAttributes.rf_magic, 2).putLevelGains(LibAttributes.rf_magic, 1)
                    .putAttributes(LibAttributes.rf_magic_defence, 0).putLevelGains(LibAttributes.rf_magic_defence, 0.8)
                    .xp(2).money(4).tamingChance(0.06f).setRidable().build(),
            new SpawnConfig.SpawnData.Builder(0).addToBiomeTypes(50,
                    "PLAINS", "FOREST", "HILLS", "MAGICAL", "JUNGLE"));
    public static final RegistryObject<EntityType<EntityTortas>> tortas = regMonster(EntityType.Builder.create(EntityTortas::new, EntityClassification.MONSTER).size(1.4f, 0.70f).trackingRange(8), LibEntities.tortas,
            0x5c6682, 0xa5848c,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 15).putLevelGains(LibAttributes.GENERIC_MAX_HEALTH, 4.5)
                    .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 7).putLevelGains(LibAttributes.GENERIC_ATTACK_DAMAGE, 3.3)
                    .putAttributes(LibAttributes.rf_defence, 0).putLevelGains(LibAttributes.rf_defence, 6.2)
                    .putAttributes(LibAttributes.rf_magic, 2).putLevelGains(LibAttributes.rf_magic, 0.6)
                    .putAttributes(LibAttributes.rf_magic_defence, 0).putLevelGains(LibAttributes.rf_magic_defence, 2)
                    .xp(6).money(10).tamingChance(0.01f).setRidable().build(),
            new SpawnConfig.SpawnData.Builder(0).addToBiomeTypes(50,
                    "BEACH", "WATER"));
    public static final RegistryObject<EntityType<EntitySkyFish>> sky_fish = regMonster(EntityType.Builder.create(EntitySkyFish::new, EntityClassification.MONSTER).size(1.2f, 0.7f).trackingRange(8), LibEntities.sky_fish,
            0xffffff, 0xffffff,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 17).putLevelGains(LibAttributes.GENERIC_MAX_HEALTH, 5.5)
                    .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 3).putLevelGains(LibAttributes.GENERIC_ATTACK_DAMAGE, 2.4)
                    .putAttributes(LibAttributes.rf_defence, 0).putLevelGains(LibAttributes.rf_defence, 1.3)
                    .putAttributes(LibAttributes.rf_magic, 6).putLevelGains(LibAttributes.rf_magic, 2.65)
                    .putAttributes(LibAttributes.rf_magic_defence, 0).putLevelGains(LibAttributes.rf_magic_defence, 2.4)
                    .xp(2).money(6).tamingChance(0.02f).setRidable().setFlying().build(),
            new SpawnConfig.SpawnData.Builder(0).addToBiomeTypes(50,
                    "BEACH", "WATER"));
    public static final RegistryObject<EntityType<EntityWeagle>> weagle = regMonster(EntityType.Builder.create(EntityWeagle::new, EntityClassification.MONSTER).size(0.8f, 1.1f).trackingRange(8), LibEntities.weagle,
            0xffffff, 0xffffff,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 14).putLevelGains(LibAttributes.GENERIC_MAX_HEALTH, 6)
                    .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 4).putLevelGains(LibAttributes.GENERIC_ATTACK_DAMAGE, 1.25)
                    .putAttributes(LibAttributes.rf_defence, 0).putLevelGains(LibAttributes.rf_defence, 1.2)
                    .putAttributes(LibAttributes.rf_magic, 5).putLevelGains(LibAttributes.rf_magic, 1.5)
                    .putAttributes(LibAttributes.rf_magic_defence, 0).putLevelGains(LibAttributes.rf_magic_defence, 1)
                    .xp(5).money(4).tamingChance(0.06f).setRidable().setFlying().build(),
            new SpawnConfig.SpawnData.Builder(0).addToBiomeTypes(50,
                    "PLAINS", "HILLS", "PLATEAU", "MOUNTAIN", "SAVANNA"));
    public static final RegistryObject<EntityType<EntityGoblin>> goblin = regMonster(EntityType.Builder.create(EntityGoblin::new, EntityClassification.MONSTER).size(0.6f, 1.5f).trackingRange(8), LibEntities.goblin,
            0xffffff, 0xffffff,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 19).putLevelGains(LibAttributes.GENERIC_MAX_HEALTH, 5.5)
                    .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 6).putLevelGains(LibAttributes.GENERIC_ATTACK_DAMAGE, 4)
                    .putAttributes(LibAttributes.rf_defence, 0).putLevelGains(LibAttributes.rf_defence, 3)
                    .putAttributes(LibAttributes.rf_magic, 4).putLevelGains(LibAttributes.rf_magic, 4)
                    .putAttributes(LibAttributes.rf_magic_defence, 0).putLevelGains(LibAttributes.rf_magic_defence, 4.5)
                    .xp(7).money(8).tamingChance(0.009f).setRidable().build(),
            new SpawnConfig.SpawnData.Builder(0).addToBiomeTypes(50,
                    "PLAINS", "FOREST", "HILLS", "MOUNTAIN", "SAVANNA"));
    public static final RegistryObject<EntityType<EntityGoblinArcher>> goblinArcher = regMonster(EntityType.Builder.create(EntityGoblinArcher::new, EntityClassification.MONSTER).size(0.6f, 1.5f).trackingRange(8), LibEntities.goblinArcher,
            0xffffff, 0xffffff,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 19).putLevelGains(LibAttributes.GENERIC_MAX_HEALTH, 5.4)
                    .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 6).putLevelGains(LibAttributes.GENERIC_ATTACK_DAMAGE, 4)
                    .putAttributes(LibAttributes.rf_defence, 0).putLevelGains(LibAttributes.rf_defence, 2.7)
                    .putAttributes(LibAttributes.rf_magic, 4).putLevelGains(LibAttributes.rf_magic, 4)
                    .putAttributes(LibAttributes.rf_magic_defence, 0).putLevelGains(LibAttributes.rf_magic_defence, 5.4)
                    .xp(7).money(8).tamingChance(0.009f).setRidable().build(),
            new SpawnConfig.SpawnData.Builder(0).addToBiomeTypes(50,
                    "PLAINS", "FOREST", "HILLS", "MOUNTAIN", "SAVANNA", "SANDY"));

    public static final RegistryObject<EntityType<EntityAmbrosia>> ambrosia = regMonster(EntityType.Builder.create(EntityAmbrosia::new, EntityClassification.MONSTER).size(0.85f, 2.3f).trackingRange(8), LibEntities.ambrosia,
            0x00ff00, 0xe600e6,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 150).putLevelGains(LibAttributes.GENERIC_MAX_HEALTH, 21)
                    .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 10).putLevelGains(LibAttributes.GENERIC_ATTACK_DAMAGE, 8.6)
                    .putAttributes(LibAttributes.rf_defence, 2).putLevelGains(LibAttributes.rf_defence, 6)
                    .putAttributes(LibAttributes.rf_magic, 12).putLevelGains(LibAttributes.rf_magic, 9.5)
                    .putAttributes(LibAttributes.rf_magic_defence, 3).putLevelGains(LibAttributes.rf_magic_defence, 5.4)
                    .putAttributes(LibAttributes.rf_res_earth, 25)
                    .xp(21).money(50).tamingChance(0.001f).setRidable().setFlying().build());
    public static final RegistryObject<EntityType<EntityThunderbolt>> thunderbolt = regMonster(EntityType.Builder.create(EntityThunderbolt::new, EntityClassification.MONSTER).size(1.6f, 1.8f).trackingRange(8), LibEntities.thunderbolt,
            0xffffff, 0xffffff,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 211).putLevelGains(LibAttributes.rf_magic_defence, 23)
                    .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 14).putLevelGains(LibAttributes.rf_magic_defence, 10.4)
                    .putAttributes(LibAttributes.rf_defence, 4).putLevelGains(LibAttributes.rf_magic_defence, 6.3)
                    .putAttributes(LibAttributes.rf_magic, 9).putLevelGains(LibAttributes.rf_magic_defence, 8.7)
                    .putAttributes(LibAttributes.rf_magic_defence, 2).putLevelGains(LibAttributes.rf_magic_defence, 5.2)
                    .putAttributes(LibAttributes.rf_res_wind, 25)
                    .xp(25).money(75).tamingChance(0.0008f).setRidable().build());

    public static final RegistryObject<EntityType<EntityMobArrow>> arrow = reg(EntityType.Builder.<EntityMobArrow>create(EntityMobArrow::new, EntityClassification.MISC).size(0.5F, 0.5F).trackingRange(4).updateInterval(20), LibEntities.arrow);
    public static final RegistryObject<EntityType<EntitySpore>> spore = reg(EntityType.Builder.<EntitySpore>create(EntitySpore::new, EntityClassification.MISC).size(0.5f, 0.5f).trackingRange(4), LibEntities.spore);
    public static final RegistryObject<EntityType<EntityWindGust>> gust = reg(EntityType.Builder.<EntityWindGust>create(EntityWindGust::new, EntityClassification.MISC).size(0.5f, 0.5f).trackingRange(4), LibEntities.gust);

    public static final RegistryObject<EntityType<EntityAmbrosiaSleep>> sleep_ball = reg(EntityType.Builder.<EntityAmbrosiaSleep>create(EntityAmbrosiaSleep::new, EntityClassification.MISC).size(0.4f, 0.6f).trackingRange(4), LibEntities.ambrosia_sleep);
    public static final RegistryObject<EntityType<EntityAmbrosiaWave>> ambrosia_wave = reg(EntityType.Builder.<EntityAmbrosiaWave>create(EntityAmbrosiaWave::new, EntityClassification.MISC).size(0.05f, 0.05f).trackingRange(4), LibEntities.ambrosia_wave);
    public static final RegistryObject<EntityType<EntityButterfly>> butterfly = reg(EntityType.Builder.<EntityButterfly>create(EntityButterfly::new, EntityClassification.MISC).size(0.2f, 0.2f).trackingRange(4), LibEntities.butterfly);
    public static final RegistryObject<EntityType<EntityPollen>> pollen = reg(EntityType.Builder.<EntityPollen>create(EntityPollen::new, EntityClassification.MISC).size(0.05f, 0.05f).trackingRange(4), LibEntities.pollen);
    public static final RegistryObject<EntityType<EntityThiccLightningBolt>> lightningOrbBolt = reg(EntityType.Builder.<EntityThiccLightningBolt>create(EntityThiccLightningBolt::new, EntityClassification.MISC).size(0.8f, 0.8f).trackingRange(4), LibEntities.lightningOrbBolt);
    public static final RegistryObject<EntityType<EntityThunderboltBeam>> lightningBeam = reg(EntityType.Builder.<EntityThunderboltBeam>create(EntityThunderboltBeam::new, EntityClassification.MISC).size(0.25f, 0.25f).trackingRange(4), LibEntities.lightningBeam);

    public static final RegistryObject<EntityType<EntityWindBlade>> windBlade = reg(EntityType.Builder.<EntityWindBlade>create(EntityWindBlade::new, EntityClassification.MISC).size(0.05f, 0.05f).trackingRange(4), LibEntities.windBlade);
    public static final RegistryObject<EntityType<EntityFireball>> fireBall = reg(EntityType.Builder.<EntityFireball>create(EntityFireball::new, EntityClassification.MISC).size(0.2f, 0.2f).trackingRange(4), LibEntities.fireball);
    public static final RegistryObject<EntityType<EntityWaterLaser>> waterLaser = reg(EntityType.Builder.<EntityWaterLaser>create(EntityWaterLaser::new, EntityClassification.MISC).size(0.05f, 0.05f).trackingRange(4), LibEntities.waterLaser);

    public static void registerAttributes() {
        GlobalEntityTypeAttributes.put(gate.get(), GateEntity.createAttributes().create());
        GlobalEntityTypeAttributes.put(wooly.get(), BaseMonster.createAttributes(ModAttributes.ATTRIBUTES.getEntries()).create());
        GlobalEntityTypeAttributes.put(orc.get(), BaseMonster.createAttributes(ModAttributes.ATTRIBUTES.getEntries()).create());
        GlobalEntityTypeAttributes.put(orcArcher.get(), BaseMonster.createAttributes(ModAttributes.ATTRIBUTES.getEntries()).create());
        GlobalEntityTypeAttributes.put(ant.get(), BaseMonster.createAttributes(ModAttributes.ATTRIBUTES.getEntries()).create());
        GlobalEntityTypeAttributes.put(beetle.get(), BaseMonster.createAttributes(ModAttributes.ATTRIBUTES.getEntries()).create());
        GlobalEntityTypeAttributes.put(big_muck.get(), BaseMonster.createAttributes(ModAttributes.ATTRIBUTES.getEntries()).create());
        GlobalEntityTypeAttributes.put(buffamoo.get(), BaseMonster.createAttributes(ModAttributes.ATTRIBUTES.getEntries()).create());
        GlobalEntityTypeAttributes.put(chipsqueek.get(), BaseMonster.createAttributes(ModAttributes.ATTRIBUTES.getEntries()).create());
        GlobalEntityTypeAttributes.put(cluckadoodle.get(), BaseMonster.createAttributes(ModAttributes.ATTRIBUTES.getEntries()).create());
        GlobalEntityTypeAttributes.put(pomme_pomme.get(), BaseMonster.createAttributes(ModAttributes.ATTRIBUTES.getEntries()).create());
        GlobalEntityTypeAttributes.put(tortas.get(), BaseMonster.createAttributes(ModAttributes.ATTRIBUTES.getEntries()).create());
        GlobalEntityTypeAttributes.put(sky_fish.get(), BaseMonster.createAttributes(ModAttributes.ATTRIBUTES.getEntries()).create());
        GlobalEntityTypeAttributes.put(weagle.get(), BaseMonster.createAttributes(ModAttributes.ATTRIBUTES.getEntries()).createMutableAttribute(Attributes.FLYING_SPEED).create());
        GlobalEntityTypeAttributes.put(goblin.get(), BaseMonster.createAttributes(ModAttributes.ATTRIBUTES.getEntries()).create());
        GlobalEntityTypeAttributes.put(goblinArcher.get(), BaseMonster.createAttributes(ModAttributes.ATTRIBUTES.getEntries()).create());

        GlobalEntityTypeAttributes.put(ambrosia.get(), BaseMonster.createAttributes(ModAttributes.ATTRIBUTES.getEntries()).create());
        GlobalEntityTypeAttributes.put(thunderbolt.get(), BaseMonster.createAttributes(ModAttributes.ATTRIBUTES.getEntries()).create());
    }

    public static <V extends Entity> RegistryObject<EntityType<V>> reg(EntityType.Builder<V> v, ResourceLocation name) {
        return ENTITIES.register(name.getPath(), () -> v.build(name.getPath()));
    }

    public static <V extends Entity> RegistryObject<EntityType<V>> regWithEgg(EntityType.Builder<V> v, ResourceLocation name, int primary, int secondary) {
        RegistryObject<EntityType<V>> reg = reg(v, name);
        ModItems.ITEMS.register(name.getPath() + "_spawn_egg", () -> new RuneCraftoryEggItem(reg, primary, secondary, new Item.Properties().group(RFCreativeTabs.monsters)));
        return reg;
    }

    public static <V extends BaseMonster> RegistryObject<EntityType<V>> regMonster(EntityType.Builder<V> v, ResourceLocation name, int primary, int secondary, EntityProperties props) {
        MobConfig.propertiesMap.put(name, props);
        return regWithEgg(v, name, primary, secondary);
    }

    public static <V extends BaseMonster> RegistryObject<EntityType<V>> regMonster(EntityType.Builder<V> v, ResourceLocation name, int primary, int secondary, EntityProperties props, SpawnConfig.SpawnData.Builder builder) {
        SpawnConfig.addDefaultData(builder.build(name));
        return regMonster(v, name, primary, secondary, props);
    }
}
