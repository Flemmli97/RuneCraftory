package io.github.flemmli97.runecraftory.forge.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.api.enums.EnumMineralTier;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.blocks.BlockCrafting;
import io.github.flemmli97.runecraftory.common.blocks.BlockCrop;
import io.github.flemmli97.runecraftory.common.blocks.BlockGiantCrop;
import io.github.flemmli97.runecraftory.common.blocks.BlockQuestboard;
import io.github.flemmli97.runecraftory.common.entities.GateEntity;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityKingWooly;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityMineralSqueek;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityWooly;
import io.github.flemmli97.runecraftory.common.loot.CropWeaponLootFunction;
import io.github.flemmli97.runecraftory.common.loot.FirstKillCondition;
import io.github.flemmli97.runecraftory.common.loot.FriendPointCondition;
import io.github.flemmli97.runecraftory.common.loot.ItemLevelLootFunction;
import io.github.flemmli97.runecraftory.common.loot.LootingAndLuckLootFunction;
import io.github.flemmli97.runecraftory.common.loot.SkillLevelCondition;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.utils.LootTableResources;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Loottables extends LootTableProvider {

    private static final float COMMON_LUCK_BONUS = 0.05f;
    private static final float RARE_LUCK_BONUS = 0.01f;
    private static final float VERY_RARE_LUCK_BONUS = 0.005f;
    private static final float SUPER_RARE_LUCK_BONUS = 0.001f;

    private static final float LOOTING_BONUS = 0.2f;
    private static final float RARE_LOOTING_BONUS = 0.1f;

    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> loot =
            ImmutableList.of(Pair.of(EntityLoot::new, LootContextParamSets.ENTITY),
                    Pair.of(WoolyShearedEntityLoot::new, LootContextParamSets.FISHING),
                    Pair.of(BlockLootData::new, LootContextParamSets.BLOCK),
                    Pair.of(FishingLootData::new, LootContextParamSets.FISHING),
                    Pair.of(ChestLoots::new, LootContextParamSets.CHEST));

    public Loottables(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return this.loot;
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
    }

    static class EntityLoot implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {

        protected final Map<ResourceLocation, LootTable.Builder> lootTables = new HashMap<>();

        protected void init() {
            this.lootTables.put(LootTableResources.WOOLED_WHITE_LOOT, this.table(
                            new ItemLootData(ModItems.FUR_SMALL.get(), 0.6f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2))
                    .withPool(LootPool.lootPool().add(LootTableReference.lootTableReference(ModEntities.WOOLY.get().getDefaultLootTable()))));
            this.registerLootTable(ModEntities.WOOLY.get(), this.table(
                                    new ItemLootData(Items.SHEARS, 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 1))
                            .withPool(this.create().add(this.addWithCount(Items.MUTTON, -3, 1, 1))),
                    new TamedItemLootData(ModItems.FUR_SMALL.get(), 1, 0),
                    new TamedItemLootData(ModItems.FUR_MEDIUM.get(), 1, 5),
                    new TamedItemLootData(ModItems.FUR_LARGE.get(), 1, 8));
            this.registerLootTable(ModEntities.ANT.get(), this.table(
                            new ItemLootData(ModItems.CARAPACE_INSECT.get(), 0.5f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.CARAPACE_PRETTY.get(), 0.01f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 2),
                            new ItemLootData(ModItems.JAW_INSECT.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(Items.OAK_LOG, 0.7f, 0.05f, 0.6f, 0, true)),
                    new TamedItemLootData(ModItems.CARAPACE_INSECT.get(), 1, 0));
            this.registerLootTable(ModEntities.ORC_ARCHER.get(), this.table(
                            new ItemLootData(ModItems.RECOVERY_POTION.get(), 0.03f, RARE_LUCK_BONUS, LOOTING_BONUS, 1),
                            new ItemLootData(Items.GUNPOWDER, 0.6f, COMMON_LUCK_BONUS, 0.7f, 0, true),
                            new ItemLootData(ModItems.ARROW_HEAD.get(), 0.5f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0))
                    .withPool(this.create().add(this.addWithCount(Items.ARROW, -1, 1, 1))));
            this.registerLootTable(ModEntities.ORC.get(), this.table(
                    new ItemLootData(ModItems.CHEAP_BRACELET.get(), 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 1),
                    new ItemLootData(ModItems.CLOTH_CHEAP.get(), 0.5f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.GLUE.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.BEETLE.get(), this.table(
                            new ItemLootData(ModItems.CARAPACE_INSECT.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.CARAPACE_PRETTY.get(), 0.01f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.HORN_INSECT.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(ModItems.HORN_INSECT.get(), 1, 0));
            this.registerLootTable(ModEntities.BIG_MUCK.get(), this.table(
                            new ItemLootData(ModItems.SPORE.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.POWDER_POISON.get(), 0.05f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.MUSHROOM.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(ModItems.SPORE.get(), 1, 0));
            this.registerLootTable(ModEntities.BUFFAMOO.get(), this.table(
                                    new ItemLootData(ModItems.MILK_S.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0))
                            .withPool(this.create().add(this.addWithCount(Items.LEATHER, -4, 1, 1)))
                            .withPool(this.create().add(this.addWithCount(Items.BEEF, -5, 2, 1))),
                    new TamedItemLootData(ModItems.MILK_S.get(), 1, 0),
                    new TamedItemLootData(ModItems.MILK_M.get(), 1, 5),
                    new TamedItemLootData(ModItems.MILK_L.get(), 1, 8));
            this.registerLootTable(ModEntities.CHIPSQUEEK.get(), this.table(
                            new ItemLootData(ModItems.FUR.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.FUR_QUALITY.get(), 0.02f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(ModItems.FUR.get(), 1, 0));
            this.registerLootTable(ModEntities.CLUCKADOODLE.get(), this.table(
                                    new ItemLootData(ModItems.EGG_S.get(), 0.45f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0))
                            .withPool(this.create().add(this.addWithCount(Items.CHICKEN, -4, 1, 0.5f)))
                            .withPool(this.create().add(this.addWithCount(Items.FEATHER, -3, 2, 1))),
                    new TamedItemLootData(ModItems.EGG_S.get(), 1, 0),
                    new TamedItemLootData(ModItems.EGG_M.get(), 1, 5),
                    new TamedItemLootData(ModItems.EGG_L.get(), 1, 8));
            this.registerLootTable(ModEntities.POMME_POMME.get(), this.table(
                            new ItemLootData(Items.APPLE, 0.7f, COMMON_LUCK_BONUS, 0.7f, 2, true),
                            new ItemLootData(ModItems.BAKED_APPLE.get(), 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 1),
                            new ItemLootData(ModItems.APPLE_SAPLING.get(), 0.05f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(Items.APPLE, 1, 0));
            this.registerLootTable(ModEntities.TORTAS.get(), this.table(
                    new ItemLootData(ModItems.TURTLE_SHELL.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(Items.IRON_INGOT, 0.65f, COMMON_LUCK_BONUS, 0.5f, 0),
                    new ItemLootData(Items.COPPER_INGOT, 0.45f, COMMON_LUCK_BONUS, 0.5f, 0)));
            this.registerLootTable(ModEntities.SKY_FISH.get(), this.table(
                    new ItemLootData(ModItems.FISH_FOSSIL.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.CAN.get(), 0.03f, VERY_RARE_LUCK_BONUS, RARE_LOOTING_BONUS, 1),
                    new ItemLootData(ModItems.RARE_CAN.get(), 0.005f, SUPER_RARE_LUCK_BONUS, RARE_LOOTING_BONUS, 1)));
            this.registerLootTable(ModEntities.WEAGLE.get(), this.table(
                            new ItemLootData(Items.FEATHER, 0.5f, COMMON_LUCK_BONUS, 0.55f, 0, true)),
                    new TamedItemLootData(Items.FEATHER, 1, 0)); // + shiny seed
            this.registerLootTable(ModEntities.GOBLIN.get(), this.table(
                    new ItemLootData(ModItems.BLADE_SHARD.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.GLUE.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.OLD_BANDAGE.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.ONIGIRI.get(), 0.07f, COMMON_LUCK_BONUS, LOOTING_BONUS, 1)));
            this.registerLootTable(ModEntities.GOBLIN_ARCHER.get(), this.table(
                            new ItemLootData(ModItems.ARROW_HEAD.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.OLD_BANDAGE.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(Items.GUNPOWDER, 0.7f, 0.05f, 0.7f, 0, true),
                            new ItemLootData(ModItems.RECOVERY_POTION.get(), 0.08f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2))
                    .withPool(this.create().add(this.addWithCount(Items.ARROW, -2, 2, 1))));
            this.registerLootTable(ModEntities.DUCK.get(), this.table(
                            new ItemLootData(ModItems.DOWN_YELLOW.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(Items.FEATHER, 0.8f, COMMON_LUCK_BONUS, 0.6f, 0, true),
                            new ItemLootData(ModItems.FEATHER_YELLOW.get(), 0.01f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(ModItems.DOWN_YELLOW.get(), 1, 0));
            this.registerLootTable(ModEntities.FAIRY.get(), this.table(
                            new ItemLootData(ModItems.FAIRY_DUST.get(), 0.5f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(ModItems.FAIRY_DUST.get(), 1, 0)); // + love potion, prelude to love
            this.registerLootTable(ModEntities.GHOST.get(), this.table(
                    new ItemLootData(ModItems.GHOST_HOOD.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.SKULL.get(), 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(Items.SKELETON_SKULL, 0.03f, RARE_LUCK_BONUS, LOOTING_BONUS, 1)));
            this.registerLootTable(ModEntities.SPIRIT.get(), this.table(
                    new ItemLootData(ModItems.CRYSTAL_DARK.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.CRYSTAL_MAGIC.get(), 0.33f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.GHOST_RAY.get(), this.table(
                    new ItemLootData(ModItems.GHOST_HOOD.get(), 0.5f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.SKULL.get(), 0.075f, RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.STICK_THICK.get(), 0.1f, RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(Items.SKELETON_SKULL, 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 1)));
            this.registerLootTable(ModEntities.SPIDER.get(), this.table(
                            new ItemLootData(ModItems.JAW_INSECT.get(), 0.45f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.THREAD_PRETTY.get(), 0.01f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(Items.STRING, 0.7f, COMMON_LUCK_BONUS, 0.75f, 0, true)),
                    new TamedItemLootData(Items.STRING, 1, 0));
            this.registerLootTable(ModEntities.SHADOW_PANTHER.get(), this.table(
                    new ItemLootData(ModItems.CLAW_PANTHER.get(), 0.45f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.FUR.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.FUR_QUALITY.get(), 0.35f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.MONSTER_BOX.get(), this.table(
                    new ItemLootData(ModItems.BROKEN_HILT.get(), 0.45f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.BROKEN_BOX.get(), 0.1f, RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.FAILED_DISH.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 1),
                    new ItemLootData(ModItems.DISASTROUS_DISH.get(), 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 1)));
            this.registerLootTable(ModEntities.GOBBLE_BOX.get(), this.table(
                    new ItemLootData(ModItems.BROKEN_HILT.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.BROKEN_BOX.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.FAILED_DISH.get(), 0.15f, COMMON_LUCK_BONUS, LOOTING_BONUS, 1),
                    new ItemLootData(ModItems.DISASTROUS_DISH.get(), 0.1f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 1)));
            this.registerLootTable(ModEntities.KILLER_ANT.get(), this.table(
                            new ItemLootData(ModItems.CARAPACE_INSECT.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.CARAPACE_PRETTY.get(), 0.15f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.JAW_INSECT.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(Items.OAK_LOG, 0.75f, 0.05f, 0.8f, 0, true)),
                    new TamedItemLootData(ModItems.CARAPACE_PRETTY.get(), 0.5f, 4));
            this.registerLootTable(ModEntities.ORC_HUNTER.get(), this.table(
                            new ItemLootData(Items.GUNPOWDER, 0.6f, COMMON_LUCK_BONUS, 1, 0, true),
                            new ItemLootData(ModItems.ARROW_HEAD.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0))
                    .withPool(this.create().add(this.addWithCount(Items.ARROW, -1, 1, 1))
                            .add(this.addWithCount(Items.STICK, 0, 4, 0.5f))));
            this.registerLootTable(ModEntities.HIGH_ORC.get(), this.table(
                            new ItemLootData(ModItems.CHEAP_BRACELET.get(), 0.09f, RARE_LUCK_BONUS, LOOTING_BONUS, 1),
                            new ItemLootData(ModItems.CLOTH_CHEAP.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.GLUE.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0))
                    .withPool(this.create().add(this.addWithCount(Items.STICK, 0, 4, 0.5f))));
            this.registerLootTable(ModEntities.HORNET.get(), this.table(
                            new ItemLootData(ModItems.CARAPACE_INSECT.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.CARAPACE_PRETTY.get(), 0.1f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.JAW_INSECT.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(Items.HONEY_BOTTLE, 0.6f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(Items.HONEY_BOTTLE, 1, 0));
            this.registerLootTable(ModEntities.SILVER_WOLF.get(), this.table(
                            new ItemLootData(ModItems.FUR.get(), 0.55f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.FUR_QUALITY.get(), 0.15f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.FANG_WOLF.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(ModItems.FANG_WOLF.get(), 0.5f, 4));
            this.registerLootTable(ModEntities.LEAF_BALL.get(), this.table(
                    new ItemLootData(ModItems.PLANT_STEM.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.FURPY.get(), this.table(
                    new ItemLootData(ModItems.FUR.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.FUR_QUALITY.get(), 0.2f, RARE_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.PALM_CAT.get(), this.table(
                    new ItemLootData(ModItems.FUR.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.CLAW_PALM.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.MINO.get(), this.table(
                    new ItemLootData(ModItems.GRAPES.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.GRAPE_JUICE.get(), 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 1)));
            this.registerLootTable(ModEntities.TRICKY_MUCK.get(), this.table(
                            new ItemLootData(ModItems.SPORE.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.POWDER_POISON.get(), 0.1f, RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.MUSHROOM.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.MONARCH_MUSHROOM.get(), 0.008f, RARE_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(ModItems.POWDER_POISON.get(), 0.7f, 0));
            this.registerLootTable(ModEntities.FLOWER_LILY.get(), this.table(
                    new ItemLootData(ModItems.PLANT_STEM.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.VINE.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.lootTables.put(EntityKingWooly.KING_WOOLY_WOOLED_LOOT, this.table(
                            new ItemLootData(ModItems.FUR_SMALL.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2),
                            new ItemLootData(ModItems.FUR_MEDIUM.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2),
                            new ItemLootData(ModItems.FURBALL.get(), 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 2),
                            new ItemLootData(Items.SHEARS, 0.01f, RARE_LUCK_BONUS, 0, 0))
                    .withPool(LootPool.lootPool().add(LootTableReference.lootTableReference(ModEntities.WOOLY.get().getDefaultLootTable()))));
            this.tamedDropTable(ModEntities.KING_WOOLY.get(), new TamedItemLootData(ModItems.FUR_SMALL.get(), 1, 0),
                    new TamedItemLootData(ModItems.FUR_MEDIUM.get(), 1, 5),
                    new TamedItemLootData(ModItems.FUR_LARGE.get(), 1, 8));
            this.registerLootTable(ModEntities.BUFFALOO.get(), this.table(
                                    new ItemLootData(ModItems.HORN_BULL.get(), 0.27f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0))
                            .withPool(this.create().add(this.addWithCount(Items.LEATHER, -4, 2, 1)))
                            .withPool(this.create().add(this.addWithCount(Items.BEEF, -5, 3, 1))),
                    new TamedItemLootData(ModItems.HORN_BULL.get(), 0.5f, 0));
            this.registerLootTable(ModEntities.GOBLIN_PIRATE.get(), this.table(
                    new ItemLootData(ModItems.CLOTH_QUALITY.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.OIL.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.OLD_BANDAGE.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.GOBLIN_GANGSTER.get(), this.table(
                    new ItemLootData(ModItems.CLOTH_QUALITY.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.OIL.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.OLD_BANDAGE.get(), 0.35f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.IGNIS.get(), this.table(
                            new ItemLootData(ModItems.CRYSTAL_MAGIC.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.CRYSTAL_FIRE.get(), 0.1f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(ModItems.CRYSTAL_FIRE.get(), 0.4f, 0));
            this.registerLootTable(ModEntities.SCORPION.get(), this.table(
                            new ItemLootData(ModItems.TAIL_SCORPION.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.CARAPACE_INSECT.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(ModItems.TAIL_SCORPION.get(), 0.6f, 0));
            this.registerLootTable(ModEntities.TROLL.get(), this.table(
                    new ItemLootData(ModItems.GIANTS_NAIL.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.GLOVE_GIANT.get(), 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.FLOWER_LION.get(), this.table(
                    new ItemLootData(ModItems.PLANT_STEM.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.VINE.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.TOMATO_GHOST.get(), this.table(
                    new ItemLootData(ModItems.TOMATO_SEEDS.get(), 0.1f, RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.TOMATO.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.GHOST_HOOD.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0))); //Giant tomato
            this.registerLootTable(ModEntities.GOBLIN_CAPTAIN.get(), this.table(
                    new ItemLootData(ModItems.CLOTH_QUALITY.get(), 0.1f, RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.WARRIORS_PROOF.get(), 0.1f, RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.SKULL.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.CLOTH_SILK.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.WINE.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.GOBLIN_DON.get(), this.table(
                    new ItemLootData(ModItems.OLD_BANDAGE.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.PROOF_OF_RANK.get(), 0.1f, RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.SKULL.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.WINE.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.lootTables.put(EntityMineralSqueek.MINERAL_SQUEEK_HURT, new LootTable.Builder()
                    .withPool(LootPool.lootPool().add(LootItem.lootTableItem(ModItems.STEEL_DOUBLE.get())
                                    .setWeight(40))
                            .add(LootItem.lootTableItem(ModItems.STEEL_TEN.get())
                                    .setWeight(1))));
            this.registerLootTable(ModEntities.NAPPIE.get(), this.table(
                            new ItemLootData(ModItems.PINEAPPLE.get(), 0.6f, COMMON_LUCK_BONUS, 0.5f, 2, true),
                            new ItemLootData(ModItems.PINEAPPLE_JUICE.get(), 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 1),
                            new ItemLootData(ModItems.PINEAPPLE_SEEDS.get(), 0.05f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(ModItems.PINEAPPLE.get(), 0.5f, 0));
            this.registerLootTable(ModEntities.MALM_TIGER.get(), this.table(
                            new ItemLootData(ModItems.PINEAPPLE.get(), 0.6f, COMMON_LUCK_BONUS, 0.5f, 2, true),
                            new ItemLootData(ModItems.PINEAPPLE_JUICE.get(), 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 1),
                            new ItemLootData(ModItems.PINEAPPLE_SEEDS.get(), 0.05f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(ModItems.PINEAPPLE.get(), 0.5f, 0));
            this.registerLootTable(ModEntities.MALM_TIGER.get(), this.table(
                    new ItemLootData(ModItems.FUR.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.FUR_QUALITY.get(), 0.2f, RARE_LOOTING_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.CLAW_MALM.get(), 0.3f, RARE_LOOTING_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.LITTLE_EMPEROR.get(), this.table(
                    new ItemLootData(ModItems.CRYSTAL_MAGIC.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.POWDER_MAGIC.get(), 0.2f, RARE_LOOTING_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.CRYSTAL_SMALL.get(), 0.05f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.DEMON.get(), this.table(
                    new ItemLootData(ModItems.DEVIL_BLOOD.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.ARCH_DEMON.get(), this.table(
                    new ItemLootData(ModItems.DEVIL_BLOOD.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.HORN_DEVIL.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.MINOTAUR.get(), this.table(
                    new ItemLootData(ModItems.HORN_DEVIL.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.HAMMER_PIECE.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.PROTEIN.get(), 0.01f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.MINOTAUR_KING.get(), this.table(
                    new ItemLootData(ModItems.HAMMER_PIECE.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.PROTEIN.get(), 0.01f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 0)));

            this.registerLootTable(ModEntities.AMBROSIA.get(), this.table(
                    new ItemLootData(ModItems.AMBROSIAS_THORNS.get(), 0.65f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2, true, false),
                    new ItemLootData(ModItems.TOYHERB.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.PLANT_STEM.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.THUNDERBOLT.get(), this.table(
                    new ItemLootData(ModItems.LIGHTNING_MANE.get(), 0.65f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2, true, false),
                    new ItemLootData(ModItems.FUR.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.FUR_QUALITY.get(), 0.15f, RARE_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.MARIONETTA.get(), this.table(
                    new ItemLootData(ModItems.CURSED_DOLL.get(), 0.65f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2, true, false),
                    new ItemLootData(ModItems.PUPPETRY_STRINGS.get(), 0.1f, COMMON_LUCK_BONUS, LOOTING_BONUS, 1),
                    new ItemLootData(ModItems.FUR_SMALL.get(), 0.8f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.FUR_MEDIUM.get(), 0.25f, RARE_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.DEAD_TREE.get(), this.table(
                    new ItemLootData(ModItems.MOVING_BRANCH.get(), 0.6f, COMMON_LUCK_BONUS, RARE_LOOTING_BONUS, 2, true, false),
                    new ItemLootData(Items.APPLE, 0.8f, COMMON_LUCK_BONUS, 1, 0, true),
                    new ItemLootData(ModItems.CRYSTAL_SMALL.get(), 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 2)));
            this.registerLootTable(ModEntities.CHIMERA.get(), this.table(
                    new ItemLootData(ModItems.CLAW_CHIMERA.get(), 0.65f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2, true, false),
                    new ItemLootData(ModItems.TAIL_CHIMERA.get(), 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 1),
                    new ItemLootData(ModItems.DEVIL_BLOOD.get(), 0.6f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.RACCOON.get(), this.table(
                    new ItemLootData(ModItems.RACCOON_LEAF.get(), 0.65f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2, true, false),
                    new ItemLootData(ModItems.UDON.get(), 0.05f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 1),
                    new ItemLootData(ModItems.CURRY_UDON.get(), 0.02f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 1),
                    new ItemLootData(ModItems.TEMPURA_UDON.get(), 0.02f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 1)));
            this.registerLootTable(ModEntities.SKELEFANG.get(), this.table(
                    new ItemLootData(ModItems.DRAGON_BONES.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2, true, false),
                    new ItemLootData(ModItems.FISH_FOSSIL.get(), 0.7f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.SKULL.get(), 0.55f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.RAFFLESIA.get(), this.table(
                    new ItemLootData(ModItems.ROOT.get(), 0.65f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.RAFFLESIA_PETAL.get(), 0.6f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2, true, false)));
            this.registerLootTable(ModEntities.GRIMOIRE.get(), this.table(
                    new ItemLootData(ModItems.DRAGONS_FANG.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.SCALE_GRIMOIRE.get(), 0.6f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2, true, false)));
            this.registerLootTable(ModEntities.SANO.get(), this.table(
                            new ItemLootData(ModItems.ROCK_SHARD_LEFT.get(), 0.5f, COMMON_LUCK_BONUS, LOOTING_BONUS, 1, true, false))
                    .withPool(this.create().add(this.addWithCount(Items.STONE, 0, 5, 1))));
            this.registerLootTable(ModEntities.UNO.get(), this.table(
                            new ItemLootData(ModItems.ROCK_SHARD_RIGHT.get(), 0.5f, COMMON_LUCK_BONUS, LOOTING_BONUS, 1, true, false))
                    .withPool(this.create().add(this.addWithCount(Items.STONE, 0, 5, 1))));
            this.registerLootTable(ModEntities.SARCOPHAGUS.get(), this.table(
                    new ItemLootData(ModItems.CRYSTAL_MAGIC.get(), 0.6f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.CRYSTAL_SKULL.get(), 0.5f, COMMON_LUCK_BONUS, LOOTING_BONUS, 1, true, false)));

            this.registerGateLoot();
        }

        private void registerGateLoot() {
            for (EnumElement element : EnumElement.values()) {
                this.lootTables.put(GateEntity.getGateLootLocation(element), this.gateLoot(element));
            }
        }

        private LootTable.Builder gateLoot(EnumElement element) {
            return switch (element) {
                case WATER ->
                        this.table(new ItemLootData(ModItems.CRYSTAL_WATER.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2));
                case EARTH ->
                        this.table(new ItemLootData(ModItems.CRYSTAL_EARTH.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2));
                case WIND ->
                        this.table(new ItemLootData(ModItems.CRYSTAL_WIND.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2));
                case FIRE ->
                        this.table(new ItemLootData(ModItems.CRYSTAL_FIRE.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2));
                case LIGHT ->
                        this.table(new ItemLootData(ModItems.CRYSTAL_LIGHT.get(), 0.1f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2));
                case DARK ->
                        this.table(new ItemLootData(ModItems.CRYSTAL_DARK.get(), 0.1f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2));
                case LOVE ->
                        this.table(new ItemLootData(ModItems.CRYSTAL_LOVE.get(), 0.05f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2));
                default -> this.table();
            };
        }

        private LootPool.Builder create() {
            return LootPool.lootPool().setRolls(ConstantValue.exactly(1));
        }

        protected void registerLootTable(EntityType<?> type, LootTable.Builder builder) {
            this.lootTables.put(type.getDefaultLootTable(), builder);
        }

        protected void registerLootTable(EntityType<?> type, LootTable.Builder builder, TamedItemLootData... datas) {
            this.lootTables.put(type.getDefaultLootTable(), builder);
            this.tamedDropTable(type, datas);
        }

        private LootTable.Builder table(ItemLootData... datas) {
            LootTable.Builder builder = new LootTable.Builder();
            for (ItemLootData data : datas) {
                LootPoolSingletonContainer.Builder<?> b = LootItem.lootTableItem(data.item);
                if (data.defaultLooting) {
                    b.when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(data.chance, data.luckBonus))
                            .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, data.lootingBonus)).setLimit(data.max));
                } else {
                    b.apply(new LootingAndLuckLootFunction.Builder(ConstantValue.exactly(data.chance))
                            .withLuckBonus(ConstantValue.exactly(data.luckBonus))
                            .withLootingBonus(ConstantValue.exactly(data.lootingBonus))
                            .limit(data.max));
                }
                if (data.guaranteeFirst) {
                    builder.withPool(this.create()
                            .add(LootItem.lootTableItem(data.item).when(FirstKillCondition::new).otherwise(b)));
                } else {
                    builder.withPool(this.create()
                            .add(b));
                }
            }
            return builder;
        }

        private LootPoolSingletonContainer.Builder<?> guaranteeOnFirstKill(ItemLike item, float min, float max, float lootingCountBonus) {
            return LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)))
                    .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, lootingCountBonus)));
        }

        private LootPoolSingletonContainer.Builder<?> addWithCount(ItemLike item, float min, float max, float lootingCountBonus) {
            return LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)))
                    .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, lootingCountBonus)));
        }

        private void tamedDropTable(EntityType<?> entity, TamedItemLootData... datas) {
            ResourceLocation def = entity.getDefaultLootTable();
            this.tamedDropTable(new ResourceLocation(def.getNamespace(), def.getPath() + "_tamed_drops"), datas);
        }

        private void tamedDropTable(ResourceLocation res, TamedItemLootData... datas) {
            if (datas.length > 1) {
                LootPoolEntryContainer.Builder<?> builder = AlternativesEntry.alternatives();
                List<TamedItemLootData> sorted = Arrays.stream(datas).sorted((f, s) -> Integer.compare(s.friendPoints, f.friendPoints)).toList();
                for (TamedItemLootData data : sorted) {
                    LootPoolSingletonContainer.Builder<?> b = LootItem.lootTableItem(data.item()).when(FriendPointCondition.of(data.friendPoints()));
                    if (data.chance != 1)
                        b.when(LootItemRandomChanceCondition.randomChance(data.chance()));
                    builder.otherwise(b);
                }
                this.lootTables.put(res, LootTable.lootTable().withPool(LootPool.lootPool().add(builder)));
            } else if (datas.length == 1) {
                TamedItemLootData data = datas[0];
                LootPoolEntryContainer.Builder<?> builder = LootItem.lootTableItem(data.item()).when(FriendPointCondition.of(data.friendPoints()));
                if (data.chance != 1)
                    builder.when(LootItemRandomChanceCondition.randomChance(data.chance()));
                this.lootTables.put(res, LootTable.lootTable().withPool(LootPool.lootPool().add(builder)));
            }
        }

        @Override
        public void accept(BiConsumer<ResourceLocation, LootTable.Builder> cons) {
            this.init();
            this.lootTables.forEach(cons);
        }

        record ItemLootData(ItemLike item, float chance, float luckBonus, float lootingBonus,
                            int max, boolean guaranteeFirst, boolean defaultLooting) {

            public ItemLootData(ItemLike item, float chance, float lootingBonus, float lootingCountBonus,
                                int lootingCountMax, boolean defaultLooting) {
                this(item, chance, lootingBonus, lootingCountBonus, lootingCountMax, false, defaultLooting);
            }

            public ItemLootData(ItemLike item, float chance, float lootingBonus, float lootingCountBonus,
                                int lootingCountMax) {
                this(item, chance, lootingBonus, lootingCountBonus, lootingCountMax, false);
            }
        }

        record TamedItemLootData(ItemLike item, float chance, int friendPoints) {
        }
    }

    /**
     * Different loot parameter sets
     */
    static class WoolyShearedEntityLoot extends EntityLoot {

        @Override
        protected void init() {
            LootPoolEntryContainer.Builder<?> b = AlternativesEntry.alternatives();
            b.otherwise(LootItem.lootTableItem(ModItems.FUR_LARGE.get()).when(FriendPointCondition.of(8))
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                    .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE)));
            b.otherwise(LootItem.lootTableItem(ModItems.FUR_MEDIUM.get()).when(FriendPointCondition.of(5))
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                    .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE)));
            b.otherwise(LootItem.lootTableItem(ModItems.FUR_SMALL.get()).when(FriendPointCondition.of(0))
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                    .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE)));

            this.lootTables.put(EntityWooly.shearedLootTable(LootTableResources.WOOLED_WHITE_LOOT), LootTable.lootTable().withPool(LootPool.lootPool().add(b)));
        }
    }

    static class ChestLoots implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {

        @Override
        public void accept(BiConsumer<ResourceLocation, LootTable.Builder> biConsumer) {
            LootPool.Builder tier1 = LootPool.lootPool().setRolls(UniformGenerator.between(2, 4));
            for (RegistryEntrySupplier<Item> item : ModItems.TIER_1_CHEST) {
                tier1.add(LootItem.lootTableItem(item.get()));
            }
            tier1.add(LootItem.lootTableItem(ModItems.FORGING_BREAD.get()));
            tier1.add(LootItem.lootTableItem(ModItems.ARMOR_BREAD.get()));
            tier1.add(LootItem.lootTableItem(ModItems.CHEMISTRY_BREAD.get()));
            tier1.add(LootItem.lootTableItem(ModItems.COOKING_BREAD.get()));
            biConsumer.accept(LootTableResources.TIER_1_LOOT, LootTable.lootTable().withPool(tier1));

            LootPool.Builder tier2 = LootPool.lootPool().setRolls(UniformGenerator.between(2, 4));
            for (RegistryEntrySupplier<Item> item : ModItems.TIER_2_CHEST) {
                tier2.add(LootItem.lootTableItem(item.get()));
            }
            tier2.add(LootItem.lootTableItem(ModItems.FORGING_BREAD.get()));
            tier2.add(LootItem.lootTableItem(ModItems.ARMOR_BREAD.get()));
            tier2.add(LootItem.lootTableItem(ModItems.CHEMISTRY_BREAD.get()));
            tier2.add(LootItem.lootTableItem(ModItems.COOKING_BREAD.get()));
            biConsumer.accept(LootTableResources.TIER_2_LOOT, LootTable.lootTable().withPool(tier2));

            LootPool.Builder tier3 = LootPool.lootPool().setRolls(UniformGenerator.between(1, 2));
            for (RegistryEntrySupplier<Item> item : ModItems.TIER_3_CHEST) {
                tier3.add(LootItem.lootTableItem(item.get()));
            }
            biConsumer.accept(LootTableResources.TIER_3_LOOT, LootTable.lootTable().withPool(tier3));

            LootPool.Builder tier4 = LootPool.lootPool().setRolls(UniformGenerator.between(1, 2));
            for (RegistryEntrySupplier<Item> item : ModItems.TIER_4_CHEST) {
                tier4.add(LootItem.lootTableItem(item.get()));
            }
            biConsumer.accept(LootTableResources.TIER_4_LOOT, LootTable.lootTable().withPool(tier4));

            biConsumer.accept(QuestGen.MINING, LootTable.lootTable().withPool(LootPool.lootPool()
                            .add(LootItem.lootTableItem(ModItems.HAMMER_SCRAP.get())))
                    .withPool(LootPool.lootPool()
                            .add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))))
                            .add(LootItem.lootTableItem(Items.COPPER_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(6, 10))))));
            biConsumer.accept(QuestGen.TAMING, LootTable.lootTable().withPool(LootPool.lootPool()
                    .add(LootItem.lootTableItem(ModItems.BRUSH.get()))));
            biConsumer.accept(QuestGen.SHIP_TURNIP, LootTable.lootTable().withPool(LootPool.lootPool()
                    .add(LootItem.lootTableItem(ModItems.TURNIP_SEEDS.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))))));

            LootPool.Builder spells = LootPool.lootPool().setRolls(UniformGenerator.between(-2, 1));
            spells.add(LootItem.lootTableItem(ModItems.FIRE_BALL_SMALL.get()).setWeight(140));
            spells.add(LootItem.lootTableItem(ModItems.FIRE_BALL_BIG.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(ModItems.EXPLOSION.get()).setWeight(20));
            spells.add(LootItem.lootTableItem(ModItems.WATER_LASER.get()).setWeight(110));
            spells.add(LootItem.lootTableItem(ModItems.PARALLEL_LASER.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(ModItems.DELTA_LASER.get()).setWeight(20));
            spells.add(LootItem.lootTableItem(ModItems.SCREW_ROCK.get()).setWeight(110));
            spells.add(LootItem.lootTableItem(ModItems.EARTH_SPIKE.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(ModItems.AVENGER_ROCK.get()).setWeight(20));
            spells.add(LootItem.lootTableItem(ModItems.SONIC_WIND.get()).setWeight(110));
            spells.add(LootItem.lootTableItem(ModItems.DOUBLE_SONIC.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(ModItems.PENETRATE_SONIC.get()).setWeight(20));
            spells.add(LootItem.lootTableItem(ModItems.LIGHT_BARRIER.get()).setWeight(90));
            spells.add(LootItem.lootTableItem(ModItems.SHINE.get()).setWeight(50));
            spells.add(LootItem.lootTableItem(ModItems.PRISM.get()).setWeight(15));
            spells.add(LootItem.lootTableItem(ModItems.DARK_SNAKE.get()).setWeight(100));
            spells.add(LootItem.lootTableItem(ModItems.DARK_BALL.get()).setWeight(50));
            spells.add(LootItem.lootTableItem(ModItems.DARKNESS.get()).setWeight(15));
            spells.add(LootItem.lootTableItem(ModItems.CURE.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(ModItems.CURE_ALL.get()).setWeight(30));
            spells.add(LootItem.lootTableItem(ModItems.CURE_MASTER.get()).setWeight(10));
            spells.add(LootItem.lootTableItem(ModItems.MEDI_POISON.get()).setWeight(80));
            spells.add(LootItem.lootTableItem(ModItems.MEDI_PARA.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(ModItems.MEDI_SEAL.get()).setWeight(10));

            spells.add(LootItem.lootTableItem(ModItems.POWER_WAVE.get()).setWeight(100));
            spells.add(LootItem.lootTableItem(ModItems.DASH_SLASH.get()).setWeight(80));
            spells.add(LootItem.lootTableItem(ModItems.RUSH_ATTACK.get()).setWeight(85));
            spells.add(LootItem.lootTableItem(ModItems.ROUND_BREAK.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(ModItems.MIND_THRUST.get()).setWeight(85));
            spells.add(LootItem.lootTableItem(ModItems.GUST.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(ModItems.STORM.get()).setWeight(80));
            spells.add(LootItem.lootTableItem(ModItems.BLITZ.get()).setWeight(30));
            spells.add(LootItem.lootTableItem(ModItems.TWIN_ATTACK.get()).setWeight(90));
            spells.add(LootItem.lootTableItem(ModItems.RAIL_STRIKE.get()).setWeight(50));
            spells.add(LootItem.lootTableItem(ModItems.WIND_SLASH.get()).setWeight(70));
            spells.add(LootItem.lootTableItem(ModItems.FLASH_STRIKE.get()).setWeight(80));
            spells.add(LootItem.lootTableItem(ModItems.NAIVE_BLADE.get()).setWeight(70));
            spells.add(LootItem.lootTableItem(ModItems.STEEL_HEART.get()).setWeight(40));
            spells.add(LootItem.lootTableItem(ModItems.DELTA_STRIKE.get()).setWeight(80));
            spells.add(LootItem.lootTableItem(ModItems.HURRICANE.get()).setWeight(90));
            spells.add(LootItem.lootTableItem(ModItems.REAPER_SLASH.get()).setWeight(80));
            spells.add(LootItem.lootTableItem(ModItems.MILLION_STRIKE.get()).setWeight(50));
            spells.add(LootItem.lootTableItem(ModItems.AXEL_DISASTER.get()).setWeight(50));
            spells.add(LootItem.lootTableItem(ModItems.STARDUST_UPPER.get()).setWeight(80));
            spells.add(LootItem.lootTableItem(ModItems.TORNADO_SWING.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(ModItems.GRAND_IMPACT.get()).setWeight(70));
            spells.add(LootItem.lootTableItem(ModItems.GIGA_SWING.get()).setWeight(80));
            spells.add(LootItem.lootTableItem(ModItems.UPPER_CUT.get()).setWeight(100));
            spells.add(LootItem.lootTableItem(ModItems.DOUBLE_KICK.get()).setWeight(90));
            spells.add(LootItem.lootTableItem(ModItems.STRAIGHT_PUNCH.get()).setWeight(80));
            spells.add(LootItem.lootTableItem(ModItems.NEKO_DAMASHI.get()).setWeight(100));
            spells.add(LootItem.lootTableItem(ModItems.RUSH_PUNCH.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(ModItems.CYCLONE.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(ModItems.RAPID_MOVE.get()).setWeight(90));
            biConsumer.accept(LootTableResources.CHEST_LOOT_SPELLS, LootTable.lootTable().withPool(spells));
        }
    }

    static class BlockLootData extends BlockLoot {

        private static final LootItemCondition.Builder SILK_TOUCH = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));
        private final Map<ResourceLocation, LootTable.Builder> loots = new HashMap<>();

        protected static LootPool.Builder herbLoot(ItemLike item) {
            LootPool.Builder build = LootPool.lootPool().setRolls(ConstantValue.exactly(1));
            build.add(LootItem.lootTableItem(item).apply(ItemLevelLootFunction.getDef()));
            return build;
        }

        protected static LootPool.Builder cropLoot(BlockCrop block) {
            LootPool.Builder build = LootPool.lootPool().setRolls(ConstantValue.exactly(1));
            if (block instanceof BlockGiantCrop)
                build.add(LootItem.lootTableItem(block.getCrop()).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BlockGiantCrop.DIRECTION, Direction.NORTH))));
            else
                build.add(LootItem.lootTableItem(block.getCrop()));
            return build;
        }

        protected static LootPool.Builder cropWeaponLoot(BlockCrop block) {
            LootPool.Builder build = LootPool.lootPool().setRolls(ConstantValue.exactly(1));
            build.add(LootItem.lootTableItem(block.getCrop())
                    .apply(new CropWeaponLootFunction.Builder()));
            return build;
        }

        protected static LootPool.Builder oreLootPool(EnumMineralTier tier) {
            LootPool.Builder build = LootPool.lootPool().setRolls(ConstantValue.exactly(1));
            switch (tier) {
                case IRON -> {
                    build.add(ore(100, 3, ModItems.SCRAP.get()));
                    build.add(ore(40, 17, Items.IRON_INGOT));
                    build.add(ore(5, 5, ModItems.INVIS_STONE.get()));
                    build.add(ore(10, 3, ModItems.INVIS_STONE.get(), 10));
                    build.add(ore(1, 7, ModItems.CRYSTAL_SMALL.get(), 40));
                }
                case BRONZE -> {
                    build.add(ore(100, 3, ModItems.SCRAP.get()));
                    build.add(ore(40, 15, Items.IRON_INGOT));
                    build.add(ore(25, 24, Items.COPPER_INGOT));
                    build.add(ore(4, 3, ModItems.INVIS_STONE.get(), 5));
                    build.add(ore(5, 3, ModItems.INVIS_STONE.get(), 20));
                    build.add(ore(3, 8, ModItems.CRYSTAL_SMALL.get(), 40));
                }
                case SILVER -> {
                    build.add(ore(100, 3, ModItems.SCRAP.get()));
                    build.add(ore(40, 12, Items.IRON_INGOT));
                    build.add(ore(20, 31, ModItems.SILVER.get()));
                    build.add(ore(2, 2, ModItems.INVIS_STONE.get(), 5));
                    build.add(ore(3, 2, ModItems.INVIS_STONE.get(), 10));
                    build.add(ore(3, 2, ModItems.INVIS_STONE.get(), 15));
                    build.add(ore(3, 8, ModItems.CRYSTAL_SMALL.get(), 40));
                }
                case GOLD -> {
                    build.add(ore(100, 3, ModItems.SCRAP.get()));
                    build.add(ore(50, 8, Items.IRON_INGOT));
                    build.add(ore(20, 31, Items.GOLD_INGOT));
                    build.add(ore(3, 8, ModItems.CRYSTAL_SMALL.get(), 40));
                }
                case DIAMOND -> {
                    build.add(ore(100, 3, ModItems.SCRAP.get()));
                    build.add(ore(5, 7, Items.DIAMOND));
                    build.add(ore(15, 27, Items.DIAMOND, 20));
                    build.add(ore(3, 8, ModItems.CRYSTAL_SMALL.get(), 40));
                }
                case PLATINUM -> {
                    build.add(ore(100, 3, ModItems.SCRAP.get()));
                    build.add(ore(4, 12, ModItems.PLATINUM.get()));
                    build.add(ore(20, 29, ModItems.PLATINUM.get(), 30));
                    build.add(ore(3, 8, ModItems.CRYSTAL_SMALL.get(), 40));
                }
                case ORICHALCUM -> {
                    build.add(ore(100, 3, ModItems.SCRAP.get()));
                    build.add(ore(4, 8, ModItems.ORICHALCUM.get(), 20));
                    build.add(ore(10, 27, ModItems.ORICHALCUM.get(), 40));
                    build.add(ore(5, 11, ModItems.CRYSTAL_BIG.get(), 40));
                }
                case DRAGONIC -> {
                    build.add(ore(100, 3, ModItems.SCRAP.get()));
                    build.add(ore(6, 9, ModItems.DRAGONIC.get(), 20));
                    build.add(ore(10, 26, ModItems.DRAGONIC.get(), 50));
                    build.add(ore(3, 9, ModItems.CRYSTAL_SMALL.get(), 40));
                }
                case AMETHYST -> {
                    build.add(ore(130, 3, ModItems.SCRAP.get()));
                    build.add(ore(40, 15, ModItems.AMETHYST.get()));
                    build.add(ore(15, 17, ModItems.CRYSTAL_EARTH.get()));
                    build.add(ore(1, 3, ModItems.LIGHT_ORE.get()));
                    build.add(ore(5, 3, ModItems.LIGHT_ORE.get(), 5));
                    build.add(ore(7, 5, ModItems.LIGHT_ORE.get(), 15));
                }
                case AQUAMARINE -> {
                    build.add(ore(130, 3, ModItems.SCRAP.get()));
                    build.add(ore(40, 15, ModItems.AQUAMARINE.get()));
                    build.add(ore(15, 17, ModItems.CRYSTAL_WATER.get()));
                    build.add(ore(1, 3, ModItems.LIGHT_ORE.get()));
                    build.add(ore(5, 3, ModItems.LIGHT_ORE.get(), 5));
                    build.add(ore(7, 5, ModItems.LIGHT_ORE.get(), 15));
                }
                case RUBY -> {
                    build.add(ore(130, 3, ModItems.SCRAP.get()));
                    build.add(ore(40, 15, ModItems.RUBY.get()));
                    build.add(ore(15, 17, ModItems.CRYSTAL_FIRE.get()));
                    build.add(ore(1, 3, ModItems.LIGHT_ORE.get()));
                    build.add(ore(5, 3, ModItems.LIGHT_ORE.get(), 5));
                    build.add(ore(7, 5, ModItems.LIGHT_ORE.get(), 15));
                }
                case EMERALD -> {
                    build.add(ore(130, 3, ModItems.SCRAP.get()));
                    build.add(ore(40, 15, Items.EMERALD));
                    build.add(ore(15, 17, ModItems.CRYSTAL_WIND.get()));
                    build.add(ore(1, 3, ModItems.LIGHT_ORE.get()));
                    build.add(ore(5, 3, ModItems.LIGHT_ORE.get(), 5));
                    build.add(ore(7, 5, ModItems.LIGHT_ORE.get(), 15));
                }
                case SAPPHIRE -> {
                    build.add(ore(130, 3, ModItems.SCRAP.get()));
                    build.add(ore(50, 15, ModItems.SAPPHIRE.get()));
                    build.add(ore(3, 17, ModItems.CRYSTAL_LOVE.get()));
                    build.add(ore(1, 3, ModItems.LIGHT_ORE.get()));
                    build.add(ore(5, 3, ModItems.LIGHT_ORE.get(), 5));
                    build.add(ore(7, 5, ModItems.LIGHT_ORE.get(), 15));
                }
            }
            build.apply(ItemLevelLootFunction.getDef());
            return build;
        }

        private static LootPoolSingletonContainer.Builder<?> ore(int weight, int quality, ItemLike item) {
            return LootItem.lootTableItem(item).setWeight(weight).setQuality(quality);
        }

        private static LootPoolSingletonContainer.Builder<?> ore(int weight, int quality, ItemLike item, int minMiningLevel) {
            return ore(weight, quality, item).when(SkillLevelCondition.get(EnumSkills.MINING, minMiningLevel));
        }

        @Override
        public void accept(BiConsumer<ResourceLocation, LootTable.Builder> cons) {
            this.add(ModBlocks.MUSHROOM.get(), LootTable.lootTable().withPool(herbLoot(ModItems.MUSHROOM.get())));
            this.add(ModBlocks.MONARCH_MUSHROOM.get(), LootTable.lootTable().withPool(herbLoot(ModItems.MONARCH_MUSHROOM.get())));
            this.add(ModBlocks.ELLI_LEAVES.get(), LootTable.lootTable().withPool(herbLoot(ModItems.ELLI_LEAVES.get())));
            this.add(ModBlocks.WITHERED_GRASS.get(), LootTable.lootTable().withPool(herbLoot(ModItems.WITHERED_GRASS.get())));
            this.add(ModBlocks.WEEDS.get(), LootTable.lootTable().withPool(herbLoot(ModItems.WEEDS.get())));
            this.add(ModBlocks.WHITE_GRASS.get(), LootTable.lootTable().withPool(herbLoot(ModItems.WHITE_GRASS.get())));
            this.add(ModBlocks.INDIGO_GRASS.get(), LootTable.lootTable().withPool(herbLoot(ModItems.INDIGO_GRASS.get())));
            this.add(ModBlocks.PURPLE_GRASS.get(), LootTable.lootTable().withPool(herbLoot(ModItems.PURPLE_GRASS.get())));
            this.add(ModBlocks.GREEN_GRASS.get(), LootTable.lootTable().withPool(herbLoot(ModItems.GREEN_GRASS.get())));
            this.add(ModBlocks.BLUE_GRASS.get(), LootTable.lootTable().withPool(herbLoot(ModItems.BLUE_GRASS.get())));
            this.add(ModBlocks.YELLOW_GRASS.get(), LootTable.lootTable().withPool(herbLoot(ModItems.YELLOW_GRASS.get())));
            this.add(ModBlocks.RED_GRASS.get(), LootTable.lootTable().withPool(herbLoot(ModItems.RED_GRASS.get())));
            this.add(ModBlocks.ORANGE_GRASS.get(), LootTable.lootTable().withPool(herbLoot(ModItems.ORANGE_GRASS.get())));
            this.add(ModBlocks.BLACK_GRASS.get(), LootTable.lootTable().withPool(herbLoot(ModItems.BLACK_GRASS.get())));
            this.add(ModBlocks.ANTIDOTE_GRASS.get(), LootTable.lootTable().withPool(herbLoot(ModItems.ANTIDOTE_GRASS.get())));
            this.add(ModBlocks.MEDICINAL_HERB.get(), LootTable.lootTable().withPool(herbLoot(ModItems.MEDICINAL_HERB.get())));
            this.add(ModBlocks.BAMBOO_SPROUT.get(), LootTable.lootTable().withPool(herbLoot(ModItems.BAMBOO_SPROUT.get())));

            this.add(ModBlocks.FORGE.get(), block -> createSinglePropConditionTable(block, BlockCrafting.PART, BlockCrafting.EnumPart.LEFT));
            this.add(ModBlocks.COOKING.get(), block -> createSinglePropConditionTable(block, BlockCrafting.PART, BlockCrafting.EnumPart.LEFT));
            this.add(ModBlocks.CHEMISTRY.get(), block -> createSinglePropConditionTable(block, BlockCrafting.PART, BlockCrafting.EnumPart.LEFT));
            this.add(ModBlocks.ACCESSORY.get(), block -> createSinglePropConditionTable(block, BlockCrafting.PART, BlockCrafting.EnumPart.LEFT));

            this.dropSelf(ModBlocks.SHIPPING.get());
            this.dropSelf(ModBlocks.CASH_REGISTER.get());
            this.dropSelf(ModBlocks.MONSTER_BARN.get());
            this.add(ModBlocks.QUEST_BOARD.get(), block -> createSinglePropConditionTable(block, BlockQuestboard.PART, BlockQuestboard.Part.BOTTOM_LEFT));

            for (RegistryEntrySupplier<Block> reg : ModBlocks.CROPS) {
                Block block = reg.get();
                if (block instanceof BlockCrop)
                    this.add(reg.get(), LootTable.lootTable().withPool(cropLoot((BlockCrop) block)));
            }
            for (RegistryEntrySupplier<Block> reg : ModBlocks.FLOWERS) {
                if (reg == ModBlocks.SWORD_CROP || reg == ModBlocks.SHIELD_CROP) {
                    Block block = reg.get();
                    if (block instanceof BlockCrop)
                        this.add(reg.get(), LootTable.lootTable().withPool(cropWeaponLoot((BlockCrop) block)));
                    continue;
                }
                Block block = reg.get();
                if (block instanceof BlockCrop)
                    this.add(reg.get(), LootTable.lootTable().withPool(cropLoot((BlockCrop) block)));
            }
            ModBlocks.MINERAL_MAP.forEach((tier, reg) -> this.add(reg.get(), LootTable.lootTable().withPool(oreLootPool(tier))));

            this.add(ModBlocks.ACCESSORY.get(), block -> createSinglePropConditionTable(block, BlockCrafting.PART, BlockCrafting.EnumPart.LEFT));

            //Copy of snow layer
            this.add(ModBlocks.SNOW.get(), block -> LootTable.lootTable().withPool(LootPool.lootPool().when(LootItemEntityPropertyCondition.entityPresent(LootContext.EntityTarget.THIS)).add(AlternativesEntry.alternatives(AlternativesEntry.alternatives(new LootPoolEntryContainer.Builder[]{LootItem.lootTableItem(Items.SNOWBALL).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, 1))), (LootItem.lootTableItem(Items.SNOWBALL).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, 2)))).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))), (LootItem.lootTableItem(Items.SNOWBALL).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, 3)))).apply(SetItemCountFunction.setCount(ConstantValue.exactly(3.0F))), (LootItem.lootTableItem(Items.SNOWBALL).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, 4)))).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))), (LootItem.lootTableItem(Items.SNOWBALL).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, 5)))).apply(SetItemCountFunction.setCount(ConstantValue.exactly(5.0F))), (LootItem.lootTableItem(Items.SNOWBALL).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, 6)))).apply(SetItemCountFunction.setCount(ConstantValue.exactly(6.0F))), (LootItem.lootTableItem(Items.SNOWBALL).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, 7)))).apply(SetItemCountFunction.setCount(ConstantValue.exactly(7.0F))), LootItem.lootTableItem(Items.SNOWBALL).apply(SetItemCountFunction.setCount(ConstantValue.exactly(8.0F)))}).when(SILK_TOUCH), AlternativesEntry.alternatives(LootItem.lootTableItem(Blocks.SNOW).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, 1))), LootItem.lootTableItem(Blocks.SNOW).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, 2))), LootItem.lootTableItem(Blocks.SNOW).apply(SetItemCountFunction.setCount(ConstantValue.exactly(3.0F))).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, 3))), LootItem.lootTableItem(Blocks.SNOW).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, 4))), LootItem.lootTableItem(Blocks.SNOW).apply(SetItemCountFunction.setCount(ConstantValue.exactly(5.0F))).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, 5))), LootItem.lootTableItem(Blocks.SNOW).apply(SetItemCountFunction.setCount(ConstantValue.exactly(6.0F))).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, 6))), LootItem.lootTableItem(Blocks.SNOW).apply(SetItemCountFunction.setCount(ConstantValue.exactly(7.0F))).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, 7))), LootItem.lootTableItem(Blocks.SNOW_BLOCK))))));
            this.loots.forEach(cons);
        }

        @Override
        public void add(Block block, Function<Block, LootTable.Builder> function) {
            this.add(block, function.apply(block));
        }

        @Override
        public void add(Block block, LootTable.Builder builder) {
            this.loots.put(block.getLootTable(), builder);
        }

        protected void registerLootTable(ResourceLocation s, LootTable.Builder builder) {
            this.loots.put(s, builder);
        }
    }

    static class FishingLootData implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {

        @Override
        public void accept(BiConsumer<ResourceLocation, LootTable.Builder> biConsumer) {
            //For now delegate to default table till fish get textures
            biConsumer.accept(LootTableResources.FISHING, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                    .add(LootTableReference.lootTableReference(BuiltInLootTables.FISHING))));
            biConsumer.accept(LootTableResources.SAND_FISHING, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(Items.SAND))));
        }
    }
}
