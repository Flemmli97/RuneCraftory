package com.flemmli97.runecraftory.common.registry;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.RFCreativeTabs;
import com.flemmli97.runecraftory.common.config.MobConfig;
import com.flemmli97.runecraftory.common.config.values.EntityProperties;
import com.flemmli97.runecraftory.common.entities.BaseMonster;
import com.flemmli97.runecraftory.common.entities.GateEntity;
import com.flemmli97.runecraftory.common.entities.monster.EntityAnt;
import com.flemmli97.runecraftory.common.entities.monster.EntityBeetle;
import com.flemmli97.runecraftory.common.entities.monster.EntityBigMuck;
import com.flemmli97.runecraftory.common.entities.monster.EntityBuffamoo;
import com.flemmli97.runecraftory.common.entities.monster.EntityChipsqueek;
import com.flemmli97.runecraftory.common.entities.monster.EntityCluckadoodle;
import com.flemmli97.runecraftory.common.entities.monster.EntityOrc;
import com.flemmli97.runecraftory.common.entities.monster.EntityOrcArcher;
import com.flemmli97.runecraftory.common.entities.monster.EntityPommePomme;
import com.flemmli97.runecraftory.common.entities.monster.EntityWooly;
import com.flemmli97.runecraftory.common.entities.monster.boss.EntityAmbrosia;
import com.flemmli97.runecraftory.common.entities.projectiles.EntityAmbrosiaSleep;
import com.flemmli97.runecraftory.common.entities.projectiles.EntityAmbrosiaWave;
import com.flemmli97.runecraftory.common.entities.projectiles.EntityButterfly;
import com.flemmli97.runecraftory.common.entities.projectiles.EntityMobArrow;
import com.flemmli97.runecraftory.common.items.RuneCraftoryEggItem;
import com.flemmli97.runecraftory.lib.LibAttributes;
import com.flemmli97.runecraftory.lib.LibEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
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

    public static final RegistryObject<EntityType<GateEntity>> gate = reg(EntityType.Builder.create(GateEntity::new, EntityClassification.MONSTER).size(1, 1).maxTrackingRange(8), LibEntities.gate);
    public static final RegistryObject<EntityType<EntityWooly>> wooly = regMonster(EntityType.Builder.create(EntityWooly::new, EntityClassification.MONSTER).size(0.7f, 1.55f).maxTrackingRange(8), LibEntities.wooly,
            0xffffcc, 0xffffff,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 20)
                    .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 3)
                    .putAttributes(LibAttributes.rf_defence, 1.5)
                    .putAttributes(LibAttributes.rf_magic, 2)
                    .putAttributes(LibAttributes.rf_magic_defence, 2)
                    .xp(2).money(3).tamingChance(0.6f).setRidable().build());
    public static final RegistryObject<EntityType<EntityOrc>> orc = regMonster(EntityType.Builder.create(EntityOrc::new, EntityClassification.MONSTER).size(0.73f, 2.4f).maxTrackingRange(8), LibEntities.orc,
            0x663300, 0xffbf80,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 20)
                    .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 4)
                    .putAttributes(LibAttributes.rf_defence, 2)
                    .putAttributes(LibAttributes.rf_magic, 3)
                    .putAttributes(LibAttributes.rf_magic_defence, 3)
                    .xp(2).money(3).tamingChance(0.6f).setRidable().build());
    public static final RegistryObject<EntityType<EntityOrcArcher>> orcArcher = regMonster(EntityType.Builder.create(EntityOrcArcher::new, EntityClassification.MONSTER).size(0.73f, 2.4f).maxTrackingRange(8), LibEntities.orcArcher,
            0x663300, 0xffbf80,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 20)
                    .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 6)
                    .putAttributes(LibAttributes.rf_defence, 1)
                    .putAttributes(LibAttributes.rf_magic, 3)
                    .putAttributes(LibAttributes.rf_magic_defence, 2)
                    .xp(2).money(3).tamingChance(0.6f).setRidable().build());

    public static final RegistryObject<EntityType<EntityAnt>> ant = regMonster(EntityType.Builder.create(EntityAnt::new, EntityClassification.MONSTER).size(1.1f, 0.44f).maxTrackingRange(8), LibEntities.ant,
            0x800000, 0x1a0000,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 16)
                    .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 6)
                    .putAttributes(LibAttributes.rf_defence, 2)
                    .putAttributes(LibAttributes.rf_magic, 4)
                    .putAttributes(LibAttributes.rf_magic_defence, 3)
                    .xp(2).money(3).tamingChance(0.6f).setRidable().build());
    public static final RegistryObject<EntityType<EntityBeetle>> beetle = regMonster(EntityType.Builder.create(EntityBeetle::new, EntityClassification.MONSTER).maxTrackingRange(8), LibEntities.beetle,
            0x000000, 0x800000,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 18)
                    .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 7)
                    .putAttributes(LibAttributes.rf_defence, 4)
                    .putAttributes(LibAttributes.rf_magic, 4)
                    .putAttributes(LibAttributes.rf_magic_defence, 4)
                    .xp(2).money(3).tamingChance(0.6f).setRidable().build());
    public static final RegistryObject<EntityType<EntityBigMuck>> big_muck = regMonster(EntityType.Builder.create(EntityBigMuck::new, EntityClassification.MONSTER).size(0.9f, 1.6f).maxTrackingRange(8), LibEntities.big_muck,
            0xd7ce4a, 0xad5c25,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 20)
                    .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 3)
                    .putAttributes(LibAttributes.rf_defence, 4)
                    .putAttributes(LibAttributes.rf_magic, 7)
                    .putAttributes(LibAttributes.rf_magic_defence, 5)
                    .xp(2).money(3).tamingChance(0.6f).setRidable().build());
    public static final RegistryObject<EntityType<EntityBuffamoo>> buffamoo = regMonster(EntityType.Builder.create(EntityBuffamoo::new, EntityClassification.MONSTER).size(1.1f, 1.45f).maxTrackingRange(8), LibEntities.buffamoo,
            0xd8d8d0, 0x4e4e4c,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 24)
                    .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 4)
                    .putAttributes(LibAttributes.rf_defence, 7)
                    .putAttributes(LibAttributes.rf_magic, 4)
                    .putAttributes(LibAttributes.rf_magic_defence, 5)
                    .xp(2).money(3).tamingChance(0.6f).setRidable().build());
    public static final RegistryObject<EntityType<EntityChipsqueek>> chipsqueek = regMonster(EntityType.Builder.create(EntityChipsqueek::new, EntityClassification.MONSTER).size(0.65f, 1.15f).maxTrackingRange(8), LibEntities.chipsqueek,
            0xff3b5b, 0xf9ffbb,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 16)
                    .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 5)
                    .putAttributes(LibAttributes.rf_defence, 4)
                    .putAttributes(LibAttributes.rf_magic, 5)
                    .putAttributes(LibAttributes.rf_magic_defence, 3)
                    .xp(2).money(3).tamingChance(0.6f).setRidable().build());
    public static final RegistryObject<EntityType<EntityCluckadoodle>> cluckadoodle = regMonster(EntityType.Builder.create(EntityCluckadoodle::new, EntityClassification.MONSTER).size(0.6f, 1.1f).maxTrackingRange(8), LibEntities.cluckadoodle,
            0xc2c2c2, 0xdc2121,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 14)
                    .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 4)
                    .putAttributes(LibAttributes.rf_defence, 4)
                    .putAttributes(LibAttributes.rf_magic, 3)
                    .putAttributes(LibAttributes.rf_magic_defence, 4)
                    .xp(2).money(3).tamingChance(0.6f).setRidable().build());
    public static final RegistryObject<EntityType<EntityPommePomme>> pomme_pomme = regMonster(EntityType.Builder.create(EntityPommePomme::new, EntityClassification.MONSTER).size(1.0f, 1.6f).maxTrackingRange(8), LibEntities.pomme_pomme,
            0xff1c2b, 0xf7b4b8,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 23)
                    .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 4)
                    .putAttributes(LibAttributes.rf_defence, 3)
                    .putAttributes(LibAttributes.rf_magic, 3)
                    .putAttributes(LibAttributes.rf_magic_defence, 4)
                    .xp(2).money(3).tamingChance(0.6f).setRidable().build());

    public static final RegistryObject<EntityType<EntityAmbrosia>> ambrosia = regMonster(EntityType.Builder.create(EntityAmbrosia::new, EntityClassification.MONSTER).size(0.85f, 2.3f).maxTrackingRange(8), LibEntities.ambrosia,
            0x00ff00, 0xe600e6,
            new EntityProperties.Builder()
                    .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 150)
                    .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 10)
                    .putAttributes(LibAttributes.rf_defence, 5)
                    .putAttributes(LibAttributes.rf_magic, 12)
                    .putAttributes(LibAttributes.rf_magic_defence, 4)
                    .putAttributes(LibAttributes.rf_res_earth, 25)
                    .xp(2).money(3).tamingChance(0.6f).setRidable().build());

    public static final RegistryObject<EntityType<EntityMobArrow>> arrow = reg(EntityType.Builder.<EntityMobArrow>create(EntityMobArrow::new, EntityClassification.MISC).size(0.5F, 0.5F).maxTrackingRange(4).trackingTickInterval(20), LibEntities.arrow);

    public static final RegistryObject<EntityType<EntityAmbrosiaSleep>> sleep_ball = reg(EntityType.Builder.<EntityAmbrosiaSleep>create(EntityAmbrosiaSleep::new, EntityClassification.MISC).size(0.6f, 1.2f).maxTrackingRange(4), LibEntities.ambrosia_sleep);
    public static final RegistryObject<EntityType<EntityAmbrosiaWave>> ambrosia_wave = reg(EntityType.Builder.<EntityAmbrosiaWave>create(EntityAmbrosiaWave::new, EntityClassification.MISC).size(0.05f, 0.05f).maxTrackingRange(4), LibEntities.ambrosia_wave);
    public static final RegistryObject<EntityType<EntityButterfly>> butterfly = reg(EntityType.Builder.<EntityButterfly>create(EntityButterfly::new, EntityClassification.MISC).size(0.2f, 0.2f).maxTrackingRange(4), LibEntities.butterfly);

    public static void registerAttributes() {
        GlobalEntityTypeAttributes.put(gate.get(), GateEntity.createAttributes().build());
        GlobalEntityTypeAttributes.put(wooly.get(), BaseMonster.createAttributes(ModAttributes.ATTRIBUTES.getEntries()).build());
        GlobalEntityTypeAttributes.put(orc.get(), BaseMonster.createAttributes(ModAttributes.ATTRIBUTES.getEntries()).build());
        GlobalEntityTypeAttributes.put(orcArcher.get(), BaseMonster.createAttributes(ModAttributes.ATTRIBUTES.getEntries()).build());
        GlobalEntityTypeAttributes.put(ant.get(), BaseMonster.createAttributes(ModAttributes.ATTRIBUTES.getEntries()).build());
        GlobalEntityTypeAttributes.put(beetle.get(), BaseMonster.createAttributes(ModAttributes.ATTRIBUTES.getEntries()).build());
        GlobalEntityTypeAttributes.put(big_muck.get(), BaseMonster.createAttributes(ModAttributes.ATTRIBUTES.getEntries()).build());
        GlobalEntityTypeAttributes.put(buffamoo.get(), BaseMonster.createAttributes(ModAttributes.ATTRIBUTES.getEntries()).build());
        GlobalEntityTypeAttributes.put(chipsqueek.get(), BaseMonster.createAttributes(ModAttributes.ATTRIBUTES.getEntries()).build());
        GlobalEntityTypeAttributes.put(cluckadoodle.get(), BaseMonster.createAttributes(ModAttributes.ATTRIBUTES.getEntries()).build());
        GlobalEntityTypeAttributes.put(pomme_pomme.get(), BaseMonster.createAttributes(ModAttributes.ATTRIBUTES.getEntries()).build());
        GlobalEntityTypeAttributes.put(ambrosia.get(), BaseMonster.createAttributes(ModAttributes.ATTRIBUTES.getEntries()).build());
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
}
