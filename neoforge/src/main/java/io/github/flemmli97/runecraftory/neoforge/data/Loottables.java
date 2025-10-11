package io.github.flemmli97.runecraftory.neoforge.data;

import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.common.blocks.CraftingBlock;
import io.github.flemmli97.runecraftory.common.blocks.ExtendedCropBlock;
import io.github.flemmli97.runecraftory.common.blocks.GiantCropBlock;
import io.github.flemmli97.runecraftory.common.blocks.QuestboardBlock;
import io.github.flemmli97.runecraftory.common.blocks.util.MineralBlockTier;
import io.github.flemmli97.runecraftory.common.entities.GateEntity;
import io.github.flemmli97.runecraftory.common.entities.monster.KingWooly;
import io.github.flemmli97.runecraftory.common.entities.monster.MineralSqueek;
import io.github.flemmli97.runecraftory.common.entities.monster.Wooly;
import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.lib.LootTableResources;
import io.github.flemmli97.runecraftory.common.loot.CropWeaponLootFunction;
import io.github.flemmli97.runecraftory.common.loot.FirstKillCondition;
import io.github.flemmli97.runecraftory.common.loot.FriendPointCondition;
import io.github.flemmli97.runecraftory.common.loot.ItemLevelLootFunction;
import io.github.flemmli97.runecraftory.common.loot.LootCtxParameters;
import io.github.flemmli97.runecraftory.common.loot.LootingAndLuckLootFunction;
import io.github.flemmli97.runecraftory.common.loot.LuckBonusNumberProvider;
import io.github.flemmli97.runecraftory.common.loot.SkillLevelCondition;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryBlocks;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Loottables extends LootTableProvider {

    private static final float COMMON_LUCK_BONUS = 0.05f;
    private static final float RARE_LUCK_BONUS = 0.01f;
    private static final float VERY_RARE_LUCK_BONUS = 0.005f;
    private static final float SUPER_RARE_LUCK_BONUS = 0.001f;

    private static final float LOOTING_BONUS = 0.2f;
    private static final float RARE_LOOTING_BONUS = 0.1f;

    public Loottables(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, QuestGen questGen) {
        super(output, Set.of(), List.of(
                new SubProviderEntry(EntityLoot::new, LootContextParamSets.ENTITY),
                new SubProviderEntry(BlockLootData::new, LootContextParamSets.BLOCK),
                new SubProviderEntry(ChestLoots::new, LootContextParamSets.CHEST),
                new SubProviderEntry(WoolyShearedEntityLoot::new, LootCtxParameters.MONSTER_INTERACTION),
                new SubProviderEntry(provider -> new QuestLootData(provider, questGen), LootContextParamSets.CHEST),
                new SubProviderEntry(FishingLootData::new, LootContextParamSets.FISHING)
        ), registries);
    }

    @Override
    protected void validate(WritableRegistry<LootTable> writableregistry, ValidationContext validationcontext, ProblemReporter.Collector problemreporter$collector) {
    }

    static class EntityLoot implements LootTableSubProvider {

        protected final Map<ResourceKey<LootTable>, LootTable.Builder> lootTables = new HashMap<>();

        protected final HolderLookup.Provider provider;

        EntityLoot(HolderLookup.Provider provider) {
            this.provider = provider;
        }

        protected void init() {
            this.lootTables.put(LootTableResources.WOOLY_WHITE, this.table(
                            new ItemLootData(RuneCraftoryItems.FUR_SMALL.get(), 0.6f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2))
                    .withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(RuneCraftoryEntities.WOOLY.get().getDefaultLootTable()))));
            this.registerLootTable(RuneCraftoryEntities.WOOLY.get(), this.table(
                                    new ItemLootData(Items.SHEARS, 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 1))
                            .withPool(this.create().add(this.addWithCount(Items.MUTTON, -3, 1, 1))),
                    new TamedItemLootData(RuneCraftoryItems.FUR_SMALL.get(), 1, 0),
                    new TamedItemLootData(RuneCraftoryItems.FUR_MEDIUM.get(), 1, 5),
                    new TamedItemLootData(RuneCraftoryItems.FUR_LARGE.get(), 1, 8));
            this.registerLootTable(RuneCraftoryEntities.ANT.get(), this.table(
                            new ItemLootData(RuneCraftoryItems.CARAPACE_INSECT.get(), 0.5f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(RuneCraftoryItems.CARAPACE_PRETTY.get(), 0.01f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 2),
                            new ItemLootData(RuneCraftoryItems.JAW_INSECT.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(Items.OAK_LOG, 0.7f, 0.05f, 0.6f, 0, true)),
                    new TamedItemLootData(RuneCraftoryItems.CARAPACE_INSECT.get(), 1, 0));
            this.registerLootTable(RuneCraftoryEntities.ORC_ARCHER.get(), this.table(
                            new ItemLootData(RuneCraftoryItems.RECOVERY_POTION.get(), 0.03f, RARE_LUCK_BONUS, LOOTING_BONUS, 1),
                            new ItemLootData(Items.GUNPOWDER, 0.6f, COMMON_LUCK_BONUS, 0.7f, 0, true),
                            new ItemLootData(RuneCraftoryItems.ARROW_HEAD.get(), 0.5f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0))
                    .withPool(this.create().add(this.addWithCount(Items.ARROW, -1, 1, 1))));
            this.registerLootTable(RuneCraftoryEntities.ORC.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.CHEAP_BRACELET.get(), 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 1),
                    new ItemLootData(RuneCraftoryItems.CLOTH_CHEAP.get(), 0.5f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.GLUE.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(RuneCraftoryEntities.BEETLE.get(), this.table(
                            new ItemLootData(RuneCraftoryItems.CARAPACE_INSECT.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(RuneCraftoryItems.CARAPACE_PRETTY.get(), 0.01f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(RuneCraftoryItems.HORN_INSECT.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(RuneCraftoryItems.HORN_INSECT.get(), 1, 0));
            this.registerLootTable(RuneCraftoryEntities.BIG_MUCK.get(), this.table(
                            new ItemLootData(RuneCraftoryItems.SPORE.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(RuneCraftoryItems.POWDER_POISON.get(), 0.05f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(RuneCraftoryItems.MUSHROOM.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(RuneCraftoryItems.SPORE.get(), 1, 0));
            this.registerLootTable(RuneCraftoryEntities.BUFFAMOO.get(), this.table(
                                    new ItemLootData(RuneCraftoryItems.MILK_S.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0))
                            .withPool(this.create().add(this.addWithCount(Items.LEATHER, -4, 1, 1)))
                            .withPool(this.create().add(this.addWithCount(Items.BEEF, -5, 2, 1))),
                    new TamedItemLootData(RuneCraftoryItems.MILK_S.get(), 1, 0),
                    new TamedItemLootData(RuneCraftoryItems.MILK_M.get(), 1, 5),
                    new TamedItemLootData(RuneCraftoryItems.MILK_L.get(), 1, 8));
            this.registerLootTable(RuneCraftoryEntities.CHIPSQUEEK.get(), this.table(
                            new ItemLootData(RuneCraftoryItems.FUR.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(RuneCraftoryItems.FUR_QUALITY.get(), 0.02f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(RuneCraftoryItems.FUR.get(), 1, 0));
            this.registerLootTable(RuneCraftoryEntities.CLUCKADOODLE.get(), this.table(
                                    new ItemLootData(RuneCraftoryItems.EGG_S.get(), 0.45f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0))
                            .withPool(this.create().add(this.addWithCount(Items.CHICKEN, -4, 1, 0.5f)))
                            .withPool(this.create().add(this.addWithCount(Items.FEATHER, -3, 2, 1))),
                    new TamedItemLootData(RuneCraftoryItems.EGG_S.get(), 1, 0),
                    new TamedItemLootData(RuneCraftoryItems.EGG_M.get(), 1, 5),
                    new TamedItemLootData(RuneCraftoryItems.EGG_L.get(), 1, 8));
            this.registerLootTable(RuneCraftoryEntities.POMME_POMME.get(), this.table(
                            new ItemLootData(Items.APPLE, 0.7f, COMMON_LUCK_BONUS, 0.7f, 2, true),
                            new ItemLootData(RuneCraftoryItems.BAKED_APPLE.get(), 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 1),
                            new ItemLootData(RuneCraftoryItems.APPLE_SAPLING.get(), 0.05f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(Items.APPLE, 1, 0));
            this.registerLootTable(RuneCraftoryEntities.TORTAS.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.TURTLE_SHELL.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(Items.IRON_INGOT, 0.65f, COMMON_LUCK_BONUS, 0.5f, 0),
                    new ItemLootData(Items.COPPER_INGOT, 0.45f, COMMON_LUCK_BONUS, 0.5f, 0)));
            this.registerLootTable(RuneCraftoryEntities.SKY_FISH.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.FISH_FOSSIL.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.CAN.get(), 0.03f, VERY_RARE_LUCK_BONUS, RARE_LOOTING_BONUS, 1),
                    new ItemLootData(RuneCraftoryItems.RARE_CAN.get(), 0.005f, SUPER_RARE_LUCK_BONUS, RARE_LOOTING_BONUS, 1)));
            this.registerLootTable(RuneCraftoryEntities.WEAGLE.get(), this.table(
                            new ItemLootData(Items.FEATHER, 0.5f, COMMON_LUCK_BONUS, 0.55f, 0, true)),
                    new TamedItemLootData(Items.FEATHER, 1, 0)); // + shiny seed
            this.registerLootTable(RuneCraftoryEntities.GOBLIN.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.BLADE_SHARD.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.GLUE.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.OLD_BANDAGE.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.ONIGIRI.get(), 0.07f, COMMON_LUCK_BONUS, LOOTING_BONUS, 1)));
            this.registerLootTable(RuneCraftoryEntities.GOBLIN_ARCHER.get(), this.table(
                            new ItemLootData(RuneCraftoryItems.ARROW_HEAD.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(RuneCraftoryItems.OLD_BANDAGE.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(Items.GUNPOWDER, 0.7f, 0.05f, 0.7f, 0, true),
                            new ItemLootData(RuneCraftoryItems.RECOVERY_POTION.get(), 0.08f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2))
                    .withPool(this.create().add(this.addWithCount(Items.ARROW, -2, 2, 1))));
            this.registerLootTable(RuneCraftoryEntities.DUCK.get(), this.table(
                            new ItemLootData(RuneCraftoryItems.DOWN_YELLOW.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(Items.FEATHER, 0.8f, COMMON_LUCK_BONUS, 0.6f, 0, true),
                            new ItemLootData(RuneCraftoryItems.FEATHER_YELLOW.get(), 0.01f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(RuneCraftoryItems.DOWN_YELLOW.get(), 1, 0));
            this.registerLootTable(RuneCraftoryEntities.FAIRY.get(), this.table(
                            new ItemLootData(RuneCraftoryItems.FAIRY_DUST.get(), 0.5f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(RuneCraftoryItems.FAIRY_DUST.get(), 1, 0)); // + love potion, prelude to love
            this.registerLootTable(RuneCraftoryEntities.GHOST.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.GHOST_HOOD.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.SKULL.get(), 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(Items.SKELETON_SKULL, 0.03f, RARE_LUCK_BONUS, LOOTING_BONUS, 1)));
            this.registerLootTable(RuneCraftoryEntities.SPIRIT.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.CRYSTAL_DARK.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.CRYSTAL_MAGIC.get(), 0.33f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(RuneCraftoryEntities.GHOST_RAY.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.GHOST_HOOD.get(), 0.5f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.SKULL.get(), 0.075f, RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.STICK_THICK.get(), 0.1f, RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(Items.SKELETON_SKULL, 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 1)));
            this.registerLootTable(RuneCraftoryEntities.SPIDER.get(), this.table(
                            new ItemLootData(RuneCraftoryItems.JAW_INSECT.get(), 0.45f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(RuneCraftoryItems.THREAD_PRETTY.get(), 0.01f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(Items.STRING, 0.7f, COMMON_LUCK_BONUS, 0.75f, 0, true)),
                    new TamedItemLootData(Items.STRING, 1, 0));
            this.registerLootTable(RuneCraftoryEntities.SHADOW_PANTHER.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.CLAW_PANTHER.get(), 0.45f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.FUR.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.FUR_QUALITY.get(), 0.35f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(RuneCraftoryEntities.MONSTER_BOX.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.BROKEN_HILT.get(), 0.45f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.BROKEN_BOX.get(), 0.1f, RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.FAILED_DISH.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 1),
                    new ItemLootData(RuneCraftoryItems.DISASTROUS_DISH.get(), 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 1)));
            this.registerLootTable(RuneCraftoryEntities.GOBBLE_BOX.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.BROKEN_HILT.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.BROKEN_BOX.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.FAILED_DISH.get(), 0.15f, COMMON_LUCK_BONUS, LOOTING_BONUS, 1),
                    new ItemLootData(RuneCraftoryItems.DISASTROUS_DISH.get(), 0.1f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 1)));
            this.registerLootTable(RuneCraftoryEntities.KILLER_ANT.get(), this.table(
                            new ItemLootData(RuneCraftoryItems.CARAPACE_INSECT.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(RuneCraftoryItems.CARAPACE_PRETTY.get(), 0.15f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(RuneCraftoryItems.JAW_INSECT.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(Items.OAK_LOG, 0.75f, 0.05f, 0.8f, 0, true)),
                    new TamedItemLootData(RuneCraftoryItems.CARAPACE_PRETTY.get(), 0.5f, 4));
            this.registerLootTable(RuneCraftoryEntities.ORC_HUNTER.get(), this.table(
                            new ItemLootData(Items.GUNPOWDER, 0.6f, COMMON_LUCK_BONUS, 1, 0, true),
                            new ItemLootData(RuneCraftoryItems.ARROW_HEAD.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0))
                    .withPool(this.create().add(this.addWithCount(Items.ARROW, -1, 1, 1))
                            .add(this.addWithCount(Items.STICK, 0, 4, 0.5f))));
            this.registerLootTable(RuneCraftoryEntities.HIGH_ORC.get(), this.table(
                            new ItemLootData(RuneCraftoryItems.CHEAP_BRACELET.get(), 0.09f, RARE_LUCK_BONUS, LOOTING_BONUS, 1),
                            new ItemLootData(RuneCraftoryItems.CLOTH_CHEAP.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(RuneCraftoryItems.GLUE.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0))
                    .withPool(this.create().add(this.addWithCount(Items.STICK, 0, 4, 0.5f))));
            this.registerLootTable(RuneCraftoryEntities.HORNET.get(), this.table(
                            new ItemLootData(RuneCraftoryItems.CARAPACE_INSECT.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(RuneCraftoryItems.CARAPACE_PRETTY.get(), 0.1f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(RuneCraftoryItems.JAW_INSECT.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(Items.HONEY_BOTTLE, 0.6f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(Items.HONEY_BOTTLE, 1, 0));
            this.registerLootTable(RuneCraftoryEntities.SILVER_WOLF.get(), this.table(
                            new ItemLootData(RuneCraftoryItems.FUR.get(), 0.55f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(RuneCraftoryItems.FUR_QUALITY.get(), 0.15f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(RuneCraftoryItems.FANG_WOLF.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(RuneCraftoryItems.FANG_WOLF.get(), 0.5f, 4));
            this.registerLootTable(RuneCraftoryEntities.LEAF_BALL.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.PLANT_STEM.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(RuneCraftoryEntities.FURPY.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.FUR.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.FUR_QUALITY.get(), 0.2f, RARE_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(RuneCraftoryEntities.PALM_CAT.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.FUR.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.CLAW_PALM.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(RuneCraftoryEntities.MINO.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.GRAPES.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.GRAPE_JUICE.get(), 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 1)));
            this.registerLootTable(RuneCraftoryEntities.TRICKY_MUCK.get(), this.table(
                            new ItemLootData(RuneCraftoryItems.SPORE.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(RuneCraftoryItems.POWDER_POISON.get(), 0.1f, RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(RuneCraftoryItems.MUSHROOM.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(RuneCraftoryItems.MONARCH_MUSHROOM.get(), 0.008f, RARE_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(RuneCraftoryItems.POWDER_POISON.get(), 0.7f, 0));
            this.registerLootTable(RuneCraftoryEntities.FLOWER_LILY.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.PLANT_STEM.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.VINE.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.lootTables.put(KingWooly.KING_WOOLY_WOOLED_LOOT, this.table(
                            new ItemLootData(RuneCraftoryItems.FUR_SMALL.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2),
                            new ItemLootData(RuneCraftoryItems.FUR_MEDIUM.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2),
                            new ItemLootData(RuneCraftoryItems.FURBALL.get(), 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 2),
                            new ItemLootData(Items.SHEARS, 0.01f, RARE_LUCK_BONUS, 0, 0))
                    .withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(RuneCraftoryEntities.WOOLY.get().getDefaultLootTable()))));
            this.tamedDropTable(RuneCraftoryEntities.KING_WOOLY.get(), new TamedItemLootData(RuneCraftoryItems.FUR_SMALL.get(), 1, 0),
                    new TamedItemLootData(RuneCraftoryItems.FUR_MEDIUM.get(), 1, 5),
                    new TamedItemLootData(RuneCraftoryItems.FUR_LARGE.get(), 1, 8));
            this.registerLootTable(RuneCraftoryEntities.BUFFALOO.get(), this.table(
                                    new ItemLootData(RuneCraftoryItems.HORN_BULL.get(), 0.27f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0))
                            .withPool(this.create().add(this.addWithCount(Items.LEATHER, -4, 2, 1)))
                            .withPool(this.create().add(this.addWithCount(Items.BEEF, -5, 3, 1))),
                    new TamedItemLootData(RuneCraftoryItems.HORN_BULL.get(), 0.5f, 0));
            this.registerLootTable(RuneCraftoryEntities.GOBLIN_PIRATE.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.CLOTH_QUALITY.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.OIL.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.OLD_BANDAGE.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(RuneCraftoryEntities.GOBLIN_GANGSTER.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.CLOTH_QUALITY.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.OIL.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.OLD_BANDAGE.get(), 0.35f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(RuneCraftoryEntities.IGNIS.get(), this.table(
                            new ItemLootData(RuneCraftoryItems.CRYSTAL_MAGIC.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(RuneCraftoryItems.CRYSTAL_FIRE.get(), 0.1f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(RuneCraftoryItems.CRYSTAL_FIRE.get(), 0.4f, 0));
            this.registerLootTable(RuneCraftoryEntities.SCORPION.get(), this.table(
                            new ItemLootData(RuneCraftoryItems.TAIL_SCORPION.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(RuneCraftoryItems.CARAPACE_INSECT.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(RuneCraftoryItems.TAIL_SCORPION.get(), 0.6f, 0));
            this.registerLootTable(RuneCraftoryEntities.TROLL.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.GIANTS_NAIL.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.GLOVE_GIANT.get(), 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(RuneCraftoryEntities.FLOWER_LION.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.PLANT_STEM.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.VINE.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(RuneCraftoryEntities.TOMATO_GHOST.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.TOMATO_SEEDS.get(), 0.1f, RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.TOMATO.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.GHOST_HOOD.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0))); //Giant tomato
            this.registerLootTable(RuneCraftoryEntities.GOBLIN_CAPTAIN.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.CLOTH_QUALITY.get(), 0.1f, RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.WARRIORS_PROOF.get(), 0.1f, RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.SKULL.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.CLOTH_SILK.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.WINE.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(RuneCraftoryEntities.GOBLIN_DON.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.OLD_BANDAGE.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.PROOF_OF_RANK.get(), 0.1f, RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.SKULL.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.WINE.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.lootTables.put(MineralSqueek.MINERAL_SQUEEK_HURT, new LootTable.Builder()
                    .withPool(LootPool.lootPool().add(LootItem.lootTableItem(RuneCraftoryItems.STEEL_DOUBLE.get())
                                    .setWeight(40))
                            .add(LootItem.lootTableItem(RuneCraftoryItems.STEEL_TEN.get())
                                    .setWeight(1))));
            this.registerLootTable(RuneCraftoryEntities.NAPPIE.get(), this.table(
                            new ItemLootData(RuneCraftoryItems.PINEAPPLE.get(), 0.6f, COMMON_LUCK_BONUS, 0.5f, 2, true),
                            new ItemLootData(RuneCraftoryItems.PINEAPPLE_JUICE.get(), 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 1),
                            new ItemLootData(RuneCraftoryItems.PINEAPPLE_SEEDS.get(), 0.05f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(RuneCraftoryItems.PINEAPPLE.get(), 0.5f, 0));
            this.registerLootTable(RuneCraftoryEntities.MALM_TIGER.get(), this.table(
                            new ItemLootData(RuneCraftoryItems.PINEAPPLE.get(), 0.6f, COMMON_LUCK_BONUS, 0.5f, 2, true),
                            new ItemLootData(RuneCraftoryItems.PINEAPPLE_JUICE.get(), 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 1),
                            new ItemLootData(RuneCraftoryItems.PINEAPPLE_SEEDS.get(), 0.05f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(RuneCraftoryItems.PINEAPPLE.get(), 0.5f, 0));
            this.registerLootTable(RuneCraftoryEntities.MALM_TIGER.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.FUR.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.FUR_QUALITY.get(), 0.2f, RARE_LOOTING_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.CLAW_MALM.get(), 0.3f, RARE_LOOTING_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(RuneCraftoryEntities.LITTLE_EMPEROR.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.CRYSTAL_MAGIC.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.POWDER_MAGIC.get(), 0.2f, RARE_LOOTING_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.CRYSTAL_SMALL.get(), 0.05f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(RuneCraftoryEntities.DEMON.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.DEVIL_BLOOD.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(RuneCraftoryEntities.ARCH_DEMON.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.DEVIL_BLOOD.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.HORN_DEVIL.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(RuneCraftoryEntities.MINOTAUR.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.HORN_DEVIL.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.HAMMER_PIECE.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.PROTEIN.get(), 0.01f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(RuneCraftoryEntities.MINOTAUR_KING.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.HAMMER_PIECE.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.PROTEIN.get(), 0.01f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 0)));

            this.registerLootTable(RuneCraftoryEntities.AMBROSIA.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.AMBROSIAS_THORNS.get(), 0.65f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2, true, false),
                    new ItemLootData(RuneCraftoryItems.TOYHERB.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.PLANT_STEM.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(RuneCraftoryEntities.THUNDERBOLT.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.LIGHTNING_MANE.get(), 0.65f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2, true, false),
                    new ItemLootData(RuneCraftoryItems.FUR.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.FUR_QUALITY.get(), 0.15f, RARE_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(RuneCraftoryEntities.MARIONETTA.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.CURSED_DOLL.get(), 0.65f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2, true, false),
                    new ItemLootData(RuneCraftoryItems.FUR_SMALL.get(), 0.8f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.FUR_MEDIUM.get(), 0.25f, RARE_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(RuneCraftoryEntities.HANDONETTA.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.PUPPETRY_STRINGS.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2, true, false),
                    new ItemLootData(RuneCraftoryItems.FUR_SMALL.get(), 0.8f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.FUR_MEDIUM.get(), 0.25f, RARE_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(RuneCraftoryEntities.DEAD_TREE.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.MOVING_BRANCH.get(), 0.6f, COMMON_LUCK_BONUS, RARE_LOOTING_BONUS, 2, true, false),
                    new ItemLootData(Items.APPLE, 0.8f, COMMON_LUCK_BONUS, 1, 0, true),
                    new ItemLootData(RuneCraftoryItems.CRYSTAL_SMALL.get(), 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 2)));
            this.registerLootTable(RuneCraftoryEntities.CHIMERA.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.CLAW_CHIMERA.get(), 0.65f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2, true, false),
                    new ItemLootData(RuneCraftoryItems.TAIL_CHIMERA.get(), 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 1),
                    new ItemLootData(RuneCraftoryItems.DEVIL_BLOOD.get(), 0.6f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(RuneCraftoryEntities.RACCOON.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.RACCOON_LEAF.get(), 0.65f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2, true, false),
                    new ItemLootData(RuneCraftoryItems.UDON.get(), 0.05f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 1),
                    new ItemLootData(RuneCraftoryItems.CURRY_UDON.get(), 0.02f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 1),
                    new ItemLootData(RuneCraftoryItems.TEMPURA_UDON.get(), 0.02f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 1)));
            this.registerLootTable(RuneCraftoryEntities.SKELEFANG.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.DRAGON_BONES.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2, true, false),
                    new ItemLootData(RuneCraftoryItems.FISH_FOSSIL.get(), 0.7f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.SKULL.get(), 0.55f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(RuneCraftoryEntities.RAFFLESIA.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.ROOT.get(), 0.65f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.RAFFLESIA_PETAL.get(), 0.6f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2, true, false)));
            this.registerLootTable(RuneCraftoryEntities.GRIMOIRE.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.FANG_DRAGON.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.SCALE_GRIMOIRE.get(), 0.6f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2, true, false)));
            this.registerLootTable(RuneCraftoryEntities.SANO.get(), this.table(
                            new ItemLootData(RuneCraftoryItems.ROCK_SHARD_LEFT.get(), 0.5f, COMMON_LUCK_BONUS, LOOTING_BONUS, 1, true, false))
                    .withPool(this.create().add(this.addWithCount(Items.STONE, 0, 5, 1))));
            this.registerLootTable(RuneCraftoryEntities.UNO.get(), this.table(
                            new ItemLootData(RuneCraftoryItems.ROCK_SHARD_RIGHT.get(), 0.5f, COMMON_LUCK_BONUS, LOOTING_BONUS, 1, true, false))
                    .withPool(this.create().add(this.addWithCount(Items.STONE, 0, 5, 1))));
            this.registerLootTable(RuneCraftoryEntities.SARCOPHAGUS.get(), this.table(
                    new ItemLootData(RuneCraftoryItems.CRYSTAL_MAGIC.get(), 0.6f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(RuneCraftoryItems.CRYSTAL_SKULL.get(), 0.5f, COMMON_LUCK_BONUS, LOOTING_BONUS, 1, true, false)));

            this.registerGateLoot();
        }

        private void registerGateLoot() {
            for (ItemElement element : ItemElement.values()) {
                this.lootTables.put(GateEntity.getGateLootLocation(element), this.gateLoot(element));
            }
        }

        private LootTable.Builder gateLoot(ItemElement element) {
            return switch (element) {
                case WATER ->
                        this.table(new ItemLootData(RuneCraftoryItems.CRYSTAL_WATER.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2));
                case EARTH ->
                        this.table(new ItemLootData(RuneCraftoryItems.CRYSTAL_EARTH.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2));
                case WIND ->
                        this.table(new ItemLootData(RuneCraftoryItems.CRYSTAL_WIND.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2));
                case FIRE ->
                        this.table(new ItemLootData(RuneCraftoryItems.CRYSTAL_FIRE.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2));
                case LIGHT ->
                        this.table(new ItemLootData(RuneCraftoryItems.CRYSTAL_LIGHT.get(), 0.1f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2));
                case DARK ->
                        this.table(new ItemLootData(RuneCraftoryItems.CRYSTAL_DARK.get(), 0.1f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2));
                case LOVE ->
                        this.table(new ItemLootData(RuneCraftoryItems.CRYSTAL_LOVE.get(), 0.05f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2));
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
                    b.when(LootItemRandomChanceCondition.randomChance(new LuckBonusNumberProvider(ConstantValue.exactly(data.chance), data.luckBonus)))
                            .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.provider, UniformGenerator.between(0, data.lootingBonus)).setLimit(data.max));
                } else {
                    b.apply(new LootingAndLuckLootFunction.Builder(ConstantValue.exactly(data.chance))
                            .withLuckBonus(ConstantValue.exactly(data.luckBonus))
                            .withLootingBonus(this.provider, ConstantValue.exactly(data.lootingBonus))
                            .limit(data.max));
                }
                if (data.guaranteeFirst) {
                    builder.withPool(this.create()
                            .add(LootItem.lootTableItem(data.item).when(() -> FirstKillCondition.INSTANCE).otherwise(b)));
                } else {
                    builder.withPool(this.create()
                            .add(b));
                }
            }
            return builder;
        }

        private LootPoolSingletonContainer.Builder<?> guaranteeOnFirstKill(ItemLike item, float min, float max, float lootingCountBonus) {
            return LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)))
                    .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.provider, UniformGenerator.between(0, lootingCountBonus)));
        }

        private LootPoolSingletonContainer.Builder<?> addWithCount(ItemLike item, float min, float max, float lootingCountBonus) {
            return LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)))
                    .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.provider, UniformGenerator.between(0, lootingCountBonus)));
        }

        private void tamedDropTable(EntityType<?> entity, TamedItemLootData... datas) {
            ResourceKey<LootTable> def = entity.getDefaultLootTable();
            this.tamedDropTable(ResourceKey.create(Registries.LOOT_TABLE,
                    ResourceLocation.fromNamespaceAndPath(def.location().getNamespace(), def.location().getPath() + "_tamed_drops")), datas);
        }

        private void tamedDropTable(ResourceKey<LootTable> res, TamedItemLootData... datas) {
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
        public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
            this.init();
            this.lootTables.forEach(output);
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

        WoolyShearedEntityLoot(HolderLookup.Provider provider) {
            super(provider);
        }

        @Override
        protected void init() {
            LootPoolEntryContainer.Builder<?> b = AlternativesEntry.alternatives();
            b.otherwise(LootItem.lootTableItem(RuneCraftoryItems.FUR_LARGE.get()).when(FriendPointCondition.of(8))
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                    .apply(ApplyBonusCount.addUniformBonusCount(this.provider.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE))));
            b.otherwise(LootItem.lootTableItem(RuneCraftoryItems.FUR_MEDIUM.get()).when(FriendPointCondition.of(5))
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                    .apply(ApplyBonusCount.addUniformBonusCount(this.provider.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE))));
            b.otherwise(LootItem.lootTableItem(RuneCraftoryItems.FUR_SMALL.get()).when(FriendPointCondition.of(0))
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                    .apply(ApplyBonusCount.addUniformBonusCount(this.provider.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE))));

            this.lootTables.put(Wooly.shearedLootTable(LootTableResources.WOOLY_WHITE), LootTable.lootTable().withPool(LootPool.lootPool().add(b)));
        }
    }

    static class ChestLoots implements LootTableSubProvider {

        private final HolderLookup.Provider provider;

        ChestLoots(HolderLookup.Provider provider) {
            this.provider = provider;
        }

        @Override
        public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
            LootPool.Builder tier1 = LootPool.lootPool().setRolls(UniformGenerator.between(2, 4));
            for (RegistryEntrySupplier<Item, ?> item : RuneCraftoryItems.TIER_1_CHEST) {
                tier1.add(LootItem.lootTableItem(item.get()));
            }
            tier1.add(LootItem.lootTableItem(RuneCraftoryItems.FORGING_BREAD.get()));
            tier1.add(LootItem.lootTableItem(RuneCraftoryItems.ACCESSORY_BREAD.get()));
            tier1.add(LootItem.lootTableItem(RuneCraftoryItems.MEDICINE_BREAD.get()));
            tier1.add(LootItem.lootTableItem(RuneCraftoryItems.COOKING_BREAD.get()));
            output.accept(LootTableResources.TIER_1_LOOT, LootTable.lootTable().withPool(tier1));

            LootPool.Builder tier2 = LootPool.lootPool().setRolls(UniformGenerator.between(2, 4));
            for (RegistryEntrySupplier<Item, ?> item : RuneCraftoryItems.TIER_2_CHEST) {
                tier2.add(LootItem.lootTableItem(item.get()));
            }
            tier2.add(LootItem.lootTableItem(RuneCraftoryItems.FORGING_BREAD.get()));
            tier2.add(LootItem.lootTableItem(RuneCraftoryItems.ACCESSORY_BREAD.get()));
            tier2.add(LootItem.lootTableItem(RuneCraftoryItems.MEDICINE_BREAD.get()));
            tier2.add(LootItem.lootTableItem(RuneCraftoryItems.COOKING_BREAD.get()));
            output.accept(LootTableResources.TIER_2_LOOT, LootTable.lootTable().withPool(tier2));

            LootPool.Builder tier3 = LootPool.lootPool().setRolls(UniformGenerator.between(1, 2));
            for (RegistryEntrySupplier<Item, ?> item : RuneCraftoryItems.TIER_3_CHEST) {
                tier3.add(LootItem.lootTableItem(item.get()));
            }
            output.accept(LootTableResources.TIER_3_LOOT, LootTable.lootTable().withPool(tier3));

            LootPool.Builder tier4 = LootPool.lootPool().setRolls(UniformGenerator.between(1, 2));
            for (RegistryEntrySupplier<Item, ?> item : RuneCraftoryItems.TIER_4_CHEST) {
                tier4.add(LootItem.lootTableItem(item.get()));
            }
            output.accept(LootTableResources.TIER_4_LOOT, LootTable.lootTable().withPool(tier4));

//            biConsumer.accept(QuestGen.MINING, LootTable.lootTable().withPool(LootPool.lootPool()
//                            .add(LootItem.lootTableItem(ModItems.HAMMER_SCRAP.get())))
//                    .withPool(LootPool.lootPool()
//                            .add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))))
//                            .add(LootItem.lootTableItem(Items.COPPER_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(6, 10))))));
//            biConsumer.accept(QuestGen.TAMING, LootTable.lootTable().withPool(LootPool.lootPool()
//                    .add(LootItem.lootTableItem(ModItems.BRUSH.get()))));
//            biConsumer.accept(QuestGen.SHIP_TURNIP, LootTable.lootTable().withPool(LootPool.lootPool()
//                    .add(LootItem.lootTableItem(ModItems.TURNIP_SEEDS.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))))));

            LootPool.Builder spells = LootPool.lootPool().setRolls(UniformGenerator.between(-2, 1));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.FIRE_BALL_SMALL.get()).setWeight(140));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.FIRE_BALL_BIG.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.EXPLOSION.get()).setWeight(20));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.WATER_LASER.get()).setWeight(110));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.PARALLEL_LASER.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.DELTA_LASER.get()).setWeight(20));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.SCREW_ROCK.get()).setWeight(110));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.EARTH_SPIKE.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.AVENGER_ROCK.get()).setWeight(20));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.SONIC_WIND.get()).setWeight(110));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.DOUBLE_SONIC.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.PENETRATE_SONIC.get()).setWeight(20));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.LIGHT_BARRIER.get()).setWeight(90));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.SHINE.get()).setWeight(50));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.PRISM.get()).setWeight(15));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.DARK_SNAKE.get()).setWeight(100));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.DARK_BALL.get()).setWeight(50));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.DARKNESS.get()).setWeight(15));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.CURE.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.CURE_ALL.get()).setWeight(30));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.CURE_MASTER.get()).setWeight(10));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.MEDI_POISON.get()).setWeight(80));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.MEDI_PARA.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.MEDI_SEAL.get()).setWeight(10));

            spells.add(LootItem.lootTableItem(RuneCraftoryItems.POWER_WAVE.get()).setWeight(100));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.DASH_SLASH.get()).setWeight(80));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.RUSH_ATTACK.get()).setWeight(85));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.ROUND_BREAK.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.MIND_THRUST.get()).setWeight(85));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.GUST.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.STORM.get()).setWeight(80));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.BLITZ.get()).setWeight(30));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.TWIN_ATTACK.get()).setWeight(90));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.RAIL_STRIKE.get()).setWeight(50));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.WIND_SLASH.get()).setWeight(70));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.FLASH_STRIKE.get()).setWeight(80));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.NAIVE_BLADE.get()).setWeight(70));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.STEEL_HEART.get()).setWeight(40));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.DELTA_STRIKE.get()).setWeight(80));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.HURRICANE.get()).setWeight(90));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.REAPER_SLASH.get()).setWeight(80));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.MILLION_STRIKE.get()).setWeight(50));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.AXEL_DISASTER.get()).setWeight(50));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.STARDUST_UPPER.get()).setWeight(80));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.TORNADO_SWING.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.GRAND_IMPACT.get()).setWeight(70));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.GIGA_SWING.get()).setWeight(80));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.UPPER_CUT.get()).setWeight(100));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.DOUBLE_KICK.get()).setWeight(90));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.STRAIGHT_PUNCH.get()).setWeight(80));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.NEKO_DAMASHI.get()).setWeight(100));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.RUSH_PUNCH.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.CYCLONE.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(RuneCraftoryItems.RAPID_MOVE.get()).setWeight(90));
            output.accept(LootTableResources.CHEST_LOOT_SPELLS, LootTable.lootTable().withPool(spells));
        }
    }

    static class BlockLootData extends BlockLootSubProvider {

        private final Map<ResourceKey<LootTable>, LootTable.Builder> loots = new HashMap<>();

        protected BlockLootData(HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlagSet.of(), registries);
        }

        protected static LootPool.Builder herbLoot(ItemLike item) {
            LootPool.Builder build = LootPool.lootPool().setRolls(ConstantValue.exactly(1));
            build.add(LootItem.lootTableItem(item).apply(ItemLevelLootFunction.defaultFunc()));
            return build;
        }

        protected static LootPool.Builder cropLoot(HolderLookup.Provider provider, ExtendedCropBlock block) {
            LootPool.Builder build = LootPool.lootPool().setRolls(ConstantValue.exactly(1));
            if (block instanceof GiantCropBlock) {
                build.add(LootItem.lootTableItem(block.getCrop(provider)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(GiantCropBlock.DIRECTION, Direction.NORTH)
                                .hasProperty(GiantCropBlock.HALF, Half.BOTTOM)
                                .hasProperty(GiantCropBlock.AGE, block.getMaxAge()))));
            } else {
                build.add(LootItem.lootTableItem(block.getCrop(provider)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(GiantCropBlock.AGE, block.getMaxAge())))
                        .otherwise(LootItem.lootTableItem(block.getCrop(provider)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(GiantCropBlock.AGE, block.getGiantAge())))));
            }
            return build;
        }

        protected static LootPool.Builder cropWeaponLoot(HolderLookup.Provider provider, ExtendedCropBlock block) {
            LootPool.Builder build = LootPool.lootPool().setRolls(ConstantValue.exactly(1));
            build.add(LootItem.lootTableItem(block.getCrop(provider))
                    .apply(new CropWeaponLootFunction.Builder()));
            return build;
        }

        protected static LootPool.Builder oreLootPool(MineralBlockTier tier) {
            LootPool.Builder build = LootPool.lootPool().setRolls(ConstantValue.exactly(1));
            switch (tier) {
                case IRON -> {
                    build.add(ore(100, 3, RuneCraftoryItems.SCRAP.get()));
                    build.add(ore(40, 17, Items.IRON_INGOT));
                    build.add(ore(5, 5, RuneCraftoryItems.INVIS_STONE.get()));
                    build.add(ore(10, 3, RuneCraftoryItems.INVIS_STONE.get(), 10));
                    build.add(ore(1, 7, RuneCraftoryItems.CRYSTAL_SMALL.get(), 40));
                }
                case TIN -> {
                    build.add(ore(100, 3, RuneCraftoryItems.SCRAP.get()));
                    build.add(ore(40, 15, Items.IRON_INGOT));
                    build.add(ore(25, 24, Items.COPPER_INGOT));
                    build.add(ore(4, 3, RuneCraftoryItems.INVIS_STONE.get(), 5));
                    build.add(ore(5, 3, RuneCraftoryItems.INVIS_STONE.get(), 20));
                    build.add(ore(3, 8, RuneCraftoryItems.CRYSTAL_SMALL.get(), 40));
                }
                case SILVER -> {
                    build.add(ore(100, 3, RuneCraftoryItems.SCRAP.get()));
                    build.add(ore(40, 12, Items.IRON_INGOT));
                    build.add(ore(20, 31, RuneCraftoryItems.RAW_SILVER.get()));
                    build.add(ore(2, 2, RuneCraftoryItems.INVIS_STONE.get(), 5));
                    build.add(ore(3, 2, RuneCraftoryItems.INVIS_STONE.get(), 10));
                    build.add(ore(3, 2, RuneCraftoryItems.INVIS_STONE.get(), 15));
                    build.add(ore(3, 8, RuneCraftoryItems.CRYSTAL_SMALL.get(), 40));
                }
                case GOLD -> {
                    build.add(ore(100, 3, RuneCraftoryItems.SCRAP.get()));
                    build.add(ore(50, 8, Items.IRON_INGOT));
                    build.add(ore(20, 31, Items.GOLD_INGOT));
                    build.add(ore(3, 8, RuneCraftoryItems.CRYSTAL_SMALL.get(), 40));
                }
                case DIAMOND -> {
                    build.add(ore(100, 3, RuneCraftoryItems.SCRAP.get()));
                    build.add(ore(5, 7, Items.DIAMOND));
                    build.add(ore(15, 27, Items.DIAMOND, 20));
                    build.add(ore(3, 8, RuneCraftoryItems.CRYSTAL_SMALL.get(), 40));
                }
                case PLATINUM -> {
                    build.add(ore(100, 3, RuneCraftoryItems.SCRAP.get()));
                    build.add(ore(4, 12, RuneCraftoryItems.RAW_PLATINUM.get()));
                    build.add(ore(20, 29, RuneCraftoryItems.RAW_PLATINUM.get(), 30));
                    build.add(ore(3, 8, RuneCraftoryItems.CRYSTAL_SMALL.get(), 40));
                }
                case ORICHALCUM -> {
                    build.add(ore(100, 3, RuneCraftoryItems.SCRAP.get()));
                    build.add(ore(4, 8, RuneCraftoryItems.ORICHALCUM.get(), 20));
                    build.add(ore(10, 27, RuneCraftoryItems.ORICHALCUM.get(), 40));
                    build.add(ore(5, 11, RuneCraftoryItems.CRYSTAL_BIG.get(), 40));
                }
                case DRAGONIC -> {
                    build.add(ore(100, 3, RuneCraftoryItems.SCRAP.get()));
                    build.add(ore(6, 9, RuneCraftoryItems.DRAGONIC.get(), 20));
                    build.add(ore(10, 26, RuneCraftoryItems.DRAGONIC.get(), 50));
                    build.add(ore(3, 9, RuneCraftoryItems.CRYSTAL_SMALL.get(), 40));
                }
                case AMETHYST -> {
                    build.add(ore(130, 3, RuneCraftoryItems.SCRAP.get()));
                    build.add(ore(40, 15, RuneCraftoryItems.AMETHYST.get()));
                    build.add(ore(15, 17, RuneCraftoryItems.CRYSTAL_EARTH.get()));
                    build.add(ore(1, 3, RuneCraftoryItems.LIGHT_ORE.get()));
                    build.add(ore(5, 3, RuneCraftoryItems.LIGHT_ORE.get(), 5));
                    build.add(ore(7, 5, RuneCraftoryItems.LIGHT_ORE.get(), 15));
                }
                case AQUAMARINE -> {
                    build.add(ore(130, 3, RuneCraftoryItems.SCRAP.get()));
                    build.add(ore(40, 15, RuneCraftoryItems.AQUAMARINE.get()));
                    build.add(ore(15, 17, RuneCraftoryItems.CRYSTAL_WATER.get()));
                    build.add(ore(1, 3, RuneCraftoryItems.LIGHT_ORE.get()));
                    build.add(ore(5, 3, RuneCraftoryItems.LIGHT_ORE.get(), 5));
                    build.add(ore(7, 5, RuneCraftoryItems.LIGHT_ORE.get(), 15));
                }
                case RUBY -> {
                    build.add(ore(130, 3, RuneCraftoryItems.SCRAP.get()));
                    build.add(ore(40, 15, RuneCraftoryItems.RUBY.get()));
                    build.add(ore(15, 17, RuneCraftoryItems.CRYSTAL_FIRE.get()));
                    build.add(ore(1, 3, RuneCraftoryItems.LIGHT_ORE.get()));
                    build.add(ore(5, 3, RuneCraftoryItems.LIGHT_ORE.get(), 5));
                    build.add(ore(7, 5, RuneCraftoryItems.LIGHT_ORE.get(), 15));
                }
                case EMERALD -> {
                    build.add(ore(130, 3, RuneCraftoryItems.SCRAP.get()));
                    build.add(ore(40, 15, Items.EMERALD));
                    build.add(ore(15, 17, RuneCraftoryItems.CRYSTAL_WIND.get()));
                    build.add(ore(1, 3, RuneCraftoryItems.LIGHT_ORE.get()));
                    build.add(ore(5, 3, RuneCraftoryItems.LIGHT_ORE.get(), 5));
                    build.add(ore(7, 5, RuneCraftoryItems.LIGHT_ORE.get(), 15));
                }
                case SAPPHIRE -> {
                    build.add(ore(130, 3, RuneCraftoryItems.SCRAP.get()));
                    build.add(ore(50, 15, RuneCraftoryItems.SAPPHIRE.get()));
                    build.add(ore(3, 17, RuneCraftoryItems.CRYSTAL_LOVE.get()));
                    build.add(ore(1, 3, RuneCraftoryItems.LIGHT_ORE.get()));
                    build.add(ore(5, 3, RuneCraftoryItems.LIGHT_ORE.get(), 5));
                    build.add(ore(7, 5, RuneCraftoryItems.LIGHT_ORE.get(), 15));
                }
            }
            build.apply(ItemLevelLootFunction.defaultFunc());
            return build;
        }

        private static LootPoolSingletonContainer.Builder<?> ore(int weight, int quality, ItemLike item) {
            return LootItem.lootTableItem(item).setWeight(weight).setQuality(quality);
        }

        private static LootPoolSingletonContainer.Builder<?> ore(int weight, int quality, ItemLike item, int minMiningLevel) {
            return ore(weight, quality, item).when(SkillLevelCondition.get(Skills.MINING, minMiningLevel));
        }

        @Override
        protected void generate() {
        }

        @Override
        public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> biConsumer) {
            this.add(RuneCraftoryBlocks.MUSHROOM.get(), LootTable.lootTable().withPool(herbLoot(RuneCraftoryItems.MUSHROOM.get())));
            this.add(RuneCraftoryBlocks.MONARCH_MUSHROOM.get(), LootTable.lootTable().withPool(herbLoot(RuneCraftoryItems.MONARCH_MUSHROOM.get())));
            this.add(RuneCraftoryBlocks.ELLI_LEAVES.get(), LootTable.lootTable().withPool(herbLoot(RuneCraftoryItems.ELLI_LEAVES.get())));
            this.add(RuneCraftoryBlocks.WITHERED_GRASS.get(), LootTable.lootTable().withPool(herbLoot(RuneCraftoryItems.WITHERED_GRASS.get())));
            this.add(RuneCraftoryBlocks.WEEDS.get(), LootTable.lootTable().withPool(herbLoot(RuneCraftoryItems.WEEDS.get())));
            this.add(RuneCraftoryBlocks.WHITE_GRASS.get(), LootTable.lootTable().withPool(herbLoot(RuneCraftoryItems.WHITE_GRASS.get())));
            this.add(RuneCraftoryBlocks.INDIGO_GRASS.get(), LootTable.lootTable().withPool(herbLoot(RuneCraftoryItems.INDIGO_GRASS.get())));
            this.add(RuneCraftoryBlocks.PURPLE_GRASS.get(), LootTable.lootTable().withPool(herbLoot(RuneCraftoryItems.PURPLE_GRASS.get())));
            this.add(RuneCraftoryBlocks.GREEN_GRASS.get(), LootTable.lootTable().withPool(herbLoot(RuneCraftoryItems.GREEN_GRASS.get())));
            this.add(RuneCraftoryBlocks.BLUE_GRASS.get(), LootTable.lootTable().withPool(herbLoot(RuneCraftoryItems.BLUE_GRASS.get())));
            this.add(RuneCraftoryBlocks.YELLOW_GRASS.get(), LootTable.lootTable().withPool(herbLoot(RuneCraftoryItems.YELLOW_GRASS.get())));
            this.add(RuneCraftoryBlocks.RED_GRASS.get(), LootTable.lootTable().withPool(herbLoot(RuneCraftoryItems.RED_GRASS.get())));
            this.add(RuneCraftoryBlocks.ORANGE_GRASS.get(), LootTable.lootTable().withPool(herbLoot(RuneCraftoryItems.ORANGE_GRASS.get())));
            this.add(RuneCraftoryBlocks.BLACK_GRASS.get(), LootTable.lootTable().withPool(herbLoot(RuneCraftoryItems.BLACK_GRASS.get())));
            this.add(RuneCraftoryBlocks.ANTIDOTE_GRASS.get(), LootTable.lootTable().withPool(herbLoot(RuneCraftoryItems.ANTIDOTE_GRASS.get())));
            this.add(RuneCraftoryBlocks.MEDICINAL_HERB.get(), LootTable.lootTable().withPool(herbLoot(RuneCraftoryItems.MEDICINAL_HERB.get())));
            this.add(RuneCraftoryBlocks.BAMBOO_SPROUT.get(), LootTable.lootTable().withPool(herbLoot(RuneCraftoryItems.BAMBOO_SPROUT.get())));

            this.add(RuneCraftoryBlocks.FORGE.get(), block -> this.createSinglePropConditionTable(block, CraftingBlock.PART, CraftingBlock.EnumPart.LEFT));
            this.add(RuneCraftoryBlocks.COOKING_TABLE.get(), block -> this.createSinglePropConditionTable(block, CraftingBlock.PART, CraftingBlock.EnumPart.LEFT));
            this.add(RuneCraftoryBlocks.CHEMISTRY_SET.get(), block -> this.createSinglePropConditionTable(block, CraftingBlock.PART, CraftingBlock.EnumPart.LEFT));
            this.add(RuneCraftoryBlocks.ACCESSORY_WORKBENCH.get(), block -> this.createSinglePropConditionTable(block, CraftingBlock.PART, CraftingBlock.EnumPart.LEFT));

            this.dropSelf(RuneCraftoryBlocks.SHIPPING.get());
            this.dropSelf(RuneCraftoryBlocks.CASH_REGISTER.get());
            this.dropSelf(RuneCraftoryBlocks.MONSTER_BARN.get());
            this.add(RuneCraftoryBlocks.QUEST_BOARD.get(), block -> this.createSinglePropConditionTable(block, QuestboardBlock.PART, QuestboardBlock.Part.BOTTOM_LEFT));

            for (RegistryEntrySupplier<Block, ?> reg : RuneCraftoryBlocks.CROPS) {
                Block block = reg.get();
                if (block instanceof ExtendedCropBlock)
                    this.add(reg.get(), LootTable.lootTable().withPool(cropLoot(this.registries, (ExtendedCropBlock) block)));
            }
            for (RegistryEntrySupplier<Block, ?> reg : RuneCraftoryBlocks.FLOWERS) {
                if (reg == RuneCraftoryBlocks.SWORD_CROP || reg == RuneCraftoryBlocks.SHIELD_CROP) {
                    Block block = reg.get();
                    if (block instanceof ExtendedCropBlock)
                        this.add(reg.get(), LootTable.lootTable().withPool(cropWeaponLoot(this.registries, (ExtendedCropBlock) block)));
                    continue;
                }
                Block block = reg.get();
                if (block instanceof ExtendedCropBlock)
                    this.add(reg.get(), LootTable.lootTable().withPool(cropLoot(this.registries, (ExtendedCropBlock) block)));
            }
            RuneCraftoryBlocks.MINERAL_MAP.forEach((tier, reg) -> this.add(reg.get(), LootTable.lootTable().withPool(oreLootPool(tier))));

            this.add(RuneCraftoryBlocks.ACCESSORY_WORKBENCH.get(), block -> this.createSinglePropConditionTable(block, CraftingBlock.PART, CraftingBlock.EnumPart.LEFT));

            this.dropOther(RuneCraftoryBlocks.APPLE_TREE.get(), Blocks.OAK_LOG);
            this.dropOther(RuneCraftoryBlocks.ORANGE_TREE.get(), Blocks.BIRCH_LOG);
            this.dropOther(RuneCraftoryBlocks.GRAPE_TREE.get(), Blocks.SPRUCE_LOG);
            this.dropOther(RuneCraftoryBlocks.APPLE_WOOD.get(), Blocks.OAK_LOG);
            this.dropOther(RuneCraftoryBlocks.ORANGE_WOOD.get(), Blocks.BIRCH_LOG);
            this.dropOther(RuneCraftoryBlocks.GRAPE_WOOD.get(), Blocks.SPRUCE_LOG);
            this.add(RuneCraftoryBlocks.APPLE_LEAVES.get(), this.simpleLeaves(Blocks.OAK_LEAVES));
            this.add(RuneCraftoryBlocks.ORANGE_LEAVES.get(), this.simpleLeaves(Blocks.BIRCH_LEAVES));
            this.add(RuneCraftoryBlocks.GRAPE_LEAVES.get(), this.simpleLeaves(Blocks.DARK_OAK_LEAVES));
            this.add(RuneCraftoryBlocks.APPLE.get(), this.simpleLeaves(Blocks.OAK_LEAVES));
            this.add(RuneCraftoryBlocks.ORANGE.get(), this.simpleLeaves(Blocks.BIRCH_LEAVES));
            this.add(RuneCraftoryBlocks.GRAPE.get(), this.simpleLeaves(Blocks.DARK_OAK_LEAVES));
            this.loots.forEach(biConsumer);
        }

        private LootTable.Builder simpleLeaves(Block leaveBlock) {
            return new LootTable.Builder().withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .when(HAS_SHEARS.or(this.hasSilkTouch()).invert())
                    .add(this.applyExplosionDecay(Blocks.OAK_LEAVES, LootItem.lootTableItem(Items.STICK)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))))
                    .when(BonusLevelTableCondition.bonusLevelFlatChance(this.registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE), 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F)));
        }

        @Override
        public void add(Block block, Function<Block, LootTable.Builder> function) {
            this.add(block, function.apply(block));
        }

        @Override
        public void add(Block block, LootTable.Builder builder) {
            this.loots.put(block.getLootTable(), builder);
        }
    }

    static class FishingLootData implements LootTableSubProvider {

        private final HolderLookup.Provider provider;

        FishingLootData(HolderLookup.Provider provider) {
            this.provider = provider;
        }

        @Override
        public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
            //For now delegate to default table till fish get textures
            output.accept(LootTableResources.FISHING, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                    .add(NestedLootTable.lootTableReference(BuiltInLootTables.FISHING))));
            output.accept(LootTableResources.SAND_FISHING, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(Items.SAND))));
        }
    }

    static class QuestLootData implements LootTableSubProvider {

        private final HolderLookup.Provider provider;
        private final QuestGen questGen;

        QuestLootData(HolderLookup.Provider provider, QuestGen questGen) {
            this.provider = provider;
            this.questGen = questGen;
        }

        @Override
        public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
            this.questGen.loot.forEach(output);
        }
    }
}
