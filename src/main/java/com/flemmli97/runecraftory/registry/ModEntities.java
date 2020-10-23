package com.flemmli97.runecraftory.registry;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.mobs.config.EntityProperties;
import com.flemmli97.runecraftory.mobs.config.MobConfig;
import com.flemmli97.runecraftory.mobs.entity.BaseMonster;
import com.flemmli97.runecraftory.mobs.entity.GateEntity;
import com.flemmli97.runecraftory.mobs.entity.monster.EntityAnt;
import com.flemmli97.runecraftory.mobs.entity.monster.EntityBeetle;
import com.flemmli97.runecraftory.mobs.entity.monster.EntityBigMuck;
import com.flemmli97.runecraftory.mobs.entity.monster.EntityBuffamoo;
import com.flemmli97.runecraftory.mobs.entity.monster.EntityChipsqueek;
import com.flemmli97.runecraftory.mobs.entity.monster.EntityCluckadoodle;
import com.flemmli97.runecraftory.mobs.entity.monster.EntityOrc;
import com.flemmli97.runecraftory.mobs.entity.monster.EntityPommePomme;
import com.flemmli97.runecraftory.mobs.entity.monster.EntityWooly;
import com.flemmli97.runecraftory.mobs.entity.monster.boss.EntityAmbrosia;
import com.flemmli97.runecraftory.mobs.entity.projectiles.EntityAmbrosiaSleep;
import com.flemmli97.runecraftory.mobs.entity.projectiles.EntityAmbrosiaWave;
import com.flemmli97.runecraftory.mobs.entity.projectiles.EntityButterfly;
import com.flemmli97.runecraftory.mobs.libs.LibAttributes;
import com.flemmli97.runecraftory.mobs.libs.LibEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RuneCraftory.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {

    public static EntityType<GateEntity> gate;
    public static EntityType<EntityWooly> wooly;
    public static EntityType<EntityOrc> orc;
    public static EntityType<EntityAnt> ant;
    public static EntityType<EntityBeetle> beetle;
    public static EntityType<EntityBigMuck> big_muck;
    public static EntityType<EntityBuffamoo> buffamoo;
    public static EntityType<EntityChipsqueek> chipsqueek;
    public static EntityType<EntityCluckadoodle> cluckadoodle;
    public static EntityType<EntityPommePomme> pomme_pomme;

    public static EntityType<EntityAmbrosia> ambrosia;

    public static EntityType<EntityAmbrosiaSleep> sleep_ball;
    public static EntityType<EntityAmbrosiaWave> ambrosia_wave;
    public static EntityType<EntityButterfly> butterfly;

    @SubscribeEvent
    public static void reg(RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().registerAll(
                gate = reg(EntityType.Builder.create(GateEntity::new, EntityClassification.MONSTER).size(1, 1).maxTrackingRange(8), LibEntities.gate),

                wooly = regMonster(EntityType.Builder.create(EntityWooly::new, EntityClassification.MONSTER).size(0.7f, 1.55f).maxTrackingRange(8), LibEntities.wooly,
                        new EntityProperties.Builder()
                                .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 20)
                                .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 3)
                                .putAttributes(LibAttributes.rf_defence, 1.5)
                                .putAttributes(LibAttributes.rf_magic, 2)
                                .putAttributes(LibAttributes.rf_magic_defence, 2)
                                .xp(2).money(3).tamingChance(0.6f).setRidable().build()),
                orc = regMonster(EntityType.Builder.create(EntityOrc::new, EntityClassification.MONSTER).size(0.73f, 2.4f).maxTrackingRange(8), LibEntities.orc,
                        new EntityProperties.Builder()
                                .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 20)
                                .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 4)
                                .putAttributes(LibAttributes.rf_defence, 2)
                                .putAttributes(LibAttributes.rf_magic, 3)
                                .putAttributes(LibAttributes.rf_magic_defence, 3)
                                .xp(2).money(3).tamingChance(0.6f).setRidable().build()),
                ant = regMonster(EntityType.Builder.create(EntityAnt::new, EntityClassification.MONSTER).size(1.1f, 0.44f).maxTrackingRange(8), LibEntities.ant,
                        new EntityProperties.Builder()
                                .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 16)
                                .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 6)
                                .putAttributes(LibAttributes.rf_defence, 2)
                                .putAttributes(LibAttributes.rf_magic, 4)
                                .putAttributes(LibAttributes.rf_magic_defence, 3)
                                .xp(2).money(3).tamingChance(0.6f).setRidable().build()),
                beetle = regMonster(EntityType.Builder.create(EntityBeetle::new, EntityClassification.MONSTER).maxTrackingRange(8), LibEntities.beetle,
                        new EntityProperties.Builder()
                                .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 18)
                                .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 7)
                                .putAttributes(LibAttributes.rf_defence, 4)
                                .putAttributes(LibAttributes.rf_magic, 4)
                                .putAttributes(LibAttributes.rf_magic_defence, 4)
                                .xp(2).money(3).tamingChance(0.6f).setRidable().build()),
                big_muck = regMonster(EntityType.Builder.create(EntityBigMuck::new, EntityClassification.MONSTER).size(0.9f, 1.6f).maxTrackingRange(8), LibEntities.big_muck,
                        new EntityProperties.Builder()
                                .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 20)
                                .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 3)
                                .putAttributes(LibAttributes.rf_defence, 4)
                                .putAttributes(LibAttributes.rf_magic, 7)
                                .putAttributes(LibAttributes.rf_magic_defence, 5)
                                .xp(2).money(3).tamingChance(0.6f).setRidable().build()),
                buffamoo = regMonster(EntityType.Builder.create(EntityBuffamoo::new, EntityClassification.MONSTER).size(1.1f, 1.45f).maxTrackingRange(8), LibEntities.buffamoo,
                        new EntityProperties.Builder()
                                .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 24)
                                .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 4)
                                .putAttributes(LibAttributes.rf_defence, 7)
                                .putAttributes(LibAttributes.rf_magic, 4)
                                .putAttributes(LibAttributes.rf_magic_defence, 5)
                                .xp(2).money(3).tamingChance(0.6f).setRidable().build()),
                chipsqueek = regMonster(EntityType.Builder.create(EntityChipsqueek::new, EntityClassification.MONSTER).size(0.65f, 1.15f).maxTrackingRange(8), LibEntities.chipsqueek,
                        new EntityProperties.Builder()
                                .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 16)
                                .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 5)
                                .putAttributes(LibAttributes.rf_defence, 4)
                                .putAttributes(LibAttributes.rf_magic, 5)
                                .putAttributes(LibAttributes.rf_magic_defence, 3)
                                .xp(2).money(3).tamingChance(0.6f).setRidable().build()),
                cluckadoodle = regMonster(EntityType.Builder.create(EntityCluckadoodle::new, EntityClassification.MONSTER).size(0.6f, 1.1f).maxTrackingRange(8), LibEntities.cluckadoodle,
                        new EntityProperties.Builder()
                                .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 14)
                                .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 4)
                                .putAttributes(LibAttributes.rf_defence, 4)
                                .putAttributes(LibAttributes.rf_magic, 3)
                                .putAttributes(LibAttributes.rf_magic_defence, 4)
                                .xp(2).money(3).tamingChance(0.6f).setRidable().build()),
                pomme_pomme = regMonster(EntityType.Builder.create(EntityPommePomme::new, EntityClassification.MONSTER).size(1.0f, 1.6f).maxTrackingRange(8), LibEntities.pomme_pomme,
                        new EntityProperties.Builder()
                                .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 23)
                                .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 4)
                                .putAttributes(LibAttributes.rf_defence, 3)
                                .putAttributes(LibAttributes.rf_magic, 3)
                                .putAttributes(LibAttributes.rf_magic_defence, 4)
                                .xp(2).money(3).tamingChance(0.6f).setRidable().build()),

                ambrosia = regMonster(EntityType.Builder.create(EntityAmbrosia::new, EntityClassification.MONSTER).size(0.85f, 2.3f).maxTrackingRange(8), LibEntities.ambrosia,
                        new EntityProperties.Builder()
                                .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 150)
                                .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 10)
                                .putAttributes(LibAttributes.rf_defence, 5)
                                .putAttributes(LibAttributes.rf_magic, 12)
                                .putAttributes(LibAttributes.rf_magic_defence, 4)
                                .putAttributes(LibAttributes.rf_res_earth, 25)
                                .xp(2).money(3).tamingChance(0.6f).setRidable().build()),

                sleep_ball = reg(EntityType.Builder.<EntityAmbrosiaSleep>create(EntityAmbrosiaSleep::new, EntityClassification.MISC).size(0.6f, 1.2f).maxTrackingRange(4), LibEntities.ambrosia_sleep),
                ambrosia_wave = reg(EntityType.Builder.<EntityAmbrosiaWave>create(EntityAmbrosiaWave::new, EntityClassification.MISC).size(0.05f, 0.05f).maxTrackingRange(4), LibEntities.ambrosia_wave),
                butterfly = reg(EntityType.Builder.<EntityButterfly>create(EntityButterfly::new, EntityClassification.MISC).size(0.2f, 0.2f).maxTrackingRange(4), LibEntities.butterfly)
        );
        registerAttributes();

        EntitySpawnPlacementRegistry.register(gate, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GateEntity::canSpawnAt);
    }


    private static void registerAttributes() {
        GlobalEntityTypeAttributes.put(gate, GateEntity.createAttributes().build());
        GlobalEntityTypeAttributes.put(wooly, BaseMonster.createAttributes(ModAttributes.modAttributes).build());
        GlobalEntityTypeAttributes.put(orc, BaseMonster.createAttributes(ModAttributes.modAttributes).build());
        GlobalEntityTypeAttributes.put(ant, BaseMonster.createAttributes(ModAttributes.modAttributes).build());
        GlobalEntityTypeAttributes.put(beetle, BaseMonster.createAttributes(ModAttributes.modAttributes).build());
        GlobalEntityTypeAttributes.put(big_muck, BaseMonster.createAttributes(ModAttributes.modAttributes).build());
        GlobalEntityTypeAttributes.put(buffamoo, BaseMonster.createAttributes(ModAttributes.modAttributes).build());
        GlobalEntityTypeAttributes.put(chipsqueek, BaseMonster.createAttributes(ModAttributes.modAttributes).build());
        GlobalEntityTypeAttributes.put(cluckadoodle, BaseMonster.createAttributes(ModAttributes.modAttributes).build());
        GlobalEntityTypeAttributes.put(pomme_pomme, BaseMonster.createAttributes(ModAttributes.modAttributes).build());
        GlobalEntityTypeAttributes.put(ambrosia, BaseMonster.createAttributes(ModAttributes.modAttributes).build());
    }

    public static <V extends Entity> EntityType<V> reg(EntityType.Builder<V> v, ResourceLocation name) {
        EntityType<V> type = v.build(name.getPath());
        type.setRegistryName(name);
        return type;
    }

    public static <V extends BaseMonster> EntityType<V> regMonster(EntityType.Builder<V> v, ResourceLocation name, EntityProperties props) {
        EntityType<V> type = v.build(name.getPath());
        type.setRegistryName(name);
        MobConfig.propertiesMap.put(type, props);
        return type;
    }}
