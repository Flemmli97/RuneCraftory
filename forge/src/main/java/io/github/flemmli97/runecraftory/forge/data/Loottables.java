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
import io.github.flemmli97.runecraftory.common.entities.monster.EntityWooly;
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
                            new ItemLootData(ModItems.furSmall.get(), 0.6f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2))
                    .withPool(LootPool.lootPool().add(LootTableReference.lootTableReference(ModEntities.WOOLY.get().getDefaultLootTable()))));
            this.registerLootTable(ModEntities.WOOLY.get(), this.table(
                                    new ItemLootData(Items.SHEARS, 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 1))
                            .withPool(this.create().add(this.addWithCount(Items.MUTTON, -3, 1, 1))),
                    new TamedItemLootData(ModItems.furSmall.get(), 1, 0),
                    new TamedItemLootData(ModItems.furMedium.get(), 1, 5),
                    new TamedItemLootData(ModItems.furLarge.get(), 1, 8));
            this.registerLootTable(ModEntities.ANT.get(), this.table(
                            new ItemLootData(ModItems.carapaceInsect.get(), 0.5f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.carapacePretty.get(), 0.01f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 2),
                            new ItemLootData(ModItems.jawInsect.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(Items.OAK_LOG, 0.7f, 0.05f, 0.6f, 0, true)),
                    new TamedItemLootData(ModItems.carapaceInsect.get(), 1, 0));
            this.registerLootTable(ModEntities.ORC_ARCHER.get(), this.table(
                            new ItemLootData(ModItems.recoveryPotion.get(), 0.03f, RARE_LUCK_BONUS, LOOTING_BONUS, 1),
                            new ItemLootData(Items.GUNPOWDER, 0.6f, COMMON_LUCK_BONUS, 0.7f, 0, true),
                            new ItemLootData(ModItems.arrowHead.get(), 0.5f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0))
                    .withPool(this.create().add(this.addWithCount(Items.ARROW, -1, 1, 1))));
            this.registerLootTable(ModEntities.ORC.get(), this.table(
                    new ItemLootData(ModItems.cheapBracelet.get(), 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 1),
                    new ItemLootData(ModItems.clothCheap.get(), 0.5f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.glue.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.BEETLE.get(), this.table(
                            new ItemLootData(ModItems.carapaceInsect.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.carapacePretty.get(), 0.01f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.hornInsect.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(ModItems.hornInsect.get(), 1, 0));
            this.registerLootTable(ModEntities.BIG_MUCK.get(), this.table(
                            new ItemLootData(ModItems.spore.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.powderPoison.get(), 0.05f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.mushroom.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(ModItems.spore.get(), 1, 0));
            this.registerLootTable(ModEntities.BUFFAMOO.get(), this.table(
                                    new ItemLootData(ModItems.milkS.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0))
                            .withPool(this.create().add(this.addWithCount(Items.LEATHER, -4, 1, 1)))
                            .withPool(this.create().add(this.addWithCount(Items.BEEF, -5, 2, 1))),
                    new TamedItemLootData(ModItems.milkS.get(), 1, 0),
                    new TamedItemLootData(ModItems.milkM.get(), 1, 5),
                    new TamedItemLootData(ModItems.milkL.get(), 1, 8));
            this.registerLootTable(ModEntities.CHIPSQUEEK.get(), this.table(
                            new ItemLootData(ModItems.fur.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.furQuality.get(), 0.02f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(ModItems.fur.get(), 1, 0));
            this.registerLootTable(ModEntities.CLUCKADOODLE.get(), this.table(
                                    new ItemLootData(ModItems.eggS.get(), 0.45f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0))
                            .withPool(this.create().add(this.addWithCount(Items.CHICKEN, -4, 1, 0.5f)))
                            .withPool(this.create().add(this.addWithCount(Items.FEATHER, -3, 2, 1))),
                    new TamedItemLootData(ModItems.eggS.get(), 1, 0),
                    new TamedItemLootData(ModItems.eggM.get(), 1, 5),
                    new TamedItemLootData(ModItems.eggL.get(), 1, 8));
            this.registerLootTable(ModEntities.POMME_POMME.get(), this.table(
                            new ItemLootData(Items.APPLE, 0.7f, 0.05f, 0.7f, 2, true),
                            new ItemLootData(ModItems.bakedApple.get(), 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 1)),
                    new TamedItemLootData(Items.APPLE, 1, 0)); // + apple tree seed
            this.registerLootTable(ModEntities.TORTAS.get(), this.table(
                    new ItemLootData(ModItems.turtleShell.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(Items.IRON_INGOT, 0.65f, COMMON_LUCK_BONUS, 0.5f, 0),
                    new ItemLootData(Items.COPPER_INGOT, 0.45f, COMMON_LUCK_BONUS, 0.5f, 0)));
            this.registerLootTable(ModEntities.SKY_FISH.get(), this.table(
                    new ItemLootData(ModItems.fishFossil.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.can.get(), 0.03f, VERY_RARE_LUCK_BONUS, RARE_LOOTING_BONUS, 1),
                    new ItemLootData(ModItems.rareCan.get(), 0.005f, SUPER_RARE_LUCK_BONUS, RARE_LOOTING_BONUS, 1)));
            this.registerLootTable(ModEntities.WEAGLE.get(), this.table(
                            new ItemLootData(Items.FEATHER, 0.5f, COMMON_LUCK_BONUS, 0.55f, 0, true)),
                    new TamedItemLootData(Items.FEATHER, 1, 0)); // + shiny seed
            this.registerLootTable(ModEntities.GOBLIN.get(), this.table(
                    new ItemLootData(ModItems.bladeShard.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.glue.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.oldBandage.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.onigiri.get(), 0.07f, COMMON_LUCK_BONUS, LOOTING_BONUS, 1)));
            this.registerLootTable(ModEntities.GOBLIN_ARCHER.get(), this.table(
                            new ItemLootData(ModItems.arrowHead.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.oldBandage.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(Items.GUNPOWDER, 0.7f, 0.05f, 0.7f, 0, true),
                            new ItemLootData(ModItems.recoveryPotion.get(), 0.08f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2))
                    .withPool(this.create().add(this.addWithCount(Items.ARROW, -2, 2, 1))));
            this.registerLootTable(ModEntities.DUCK.get(), this.table(
                            new ItemLootData(ModItems.downYellow.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(Items.FEATHER, 0.8f, COMMON_LUCK_BONUS, 0.6f, 0, true),
                            new ItemLootData(ModItems.featherYellow.get(), 0.01f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(ModItems.downYellow.get(), 1, 0));
            this.registerLootTable(ModEntities.FAIRY.get(), this.table(
                            new ItemLootData(ModItems.fairyDust.get(), 0.5f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(ModItems.fairyDust.get(), 1, 0)); // + love potion, prelude to love
            this.registerLootTable(ModEntities.GHOST.get(), this.table(
                    new ItemLootData(ModItems.ghostHood.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.skull.get(), 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(Items.SKELETON_SKULL, 0.03f, RARE_LUCK_BONUS, LOOTING_BONUS, 1)));
            this.registerLootTable(ModEntities.SPIRIT.get(), this.table(
                    new ItemLootData(ModItems.crystalDark.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.crystalMagic.get(), 0.33f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.GHOST_RAY.get(), this.table(
                    new ItemLootData(ModItems.ghostHood.get(), 0.5f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.skull.get(), 0.075f, RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.stickThick.get(), 0.1f, RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(Items.SKELETON_SKULL, 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 1)));
            this.registerLootTable(ModEntities.SPIDER.get(), this.table(
                            new ItemLootData(ModItems.jawInsect.get(), 0.45f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.threadPretty.get(), 0.01f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(Items.STRING, 0.7f, COMMON_LUCK_BONUS, 0.75f, 0, true)),
                    new TamedItemLootData(Items.STRING, 1, 0));
            this.registerLootTable(ModEntities.SHADOW_PANTHER.get(), this.table(
                    new ItemLootData(ModItems.clawPanther.get(), 0.45f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.fur.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.furQuality.get(), 0.35f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.MONSTER_BOX.get(), this.table(
                    new ItemLootData(ModItems.brokenHilt.get(), 0.45f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.brokenBox.get(), 0.1f, RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.failedDish.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 1),
                    new ItemLootData(ModItems.disastrousDish.get(), 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 1)));
            this.registerLootTable(ModEntities.GOBBLE_BOX.get(), this.table(
                    new ItemLootData(ModItems.brokenHilt.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.brokenBox.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.failedDish.get(), 0.15f, COMMON_LUCK_BONUS, LOOTING_BONUS, 1),
                    new ItemLootData(ModItems.disastrousDish.get(), 0.1f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 1)));
            this.registerLootTable(ModEntities.KILLER_ANT.get(), this.table(
                            new ItemLootData(ModItems.carapaceInsect.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.carapacePretty.get(), 0.15f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.jawInsect.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(Items.OAK_LOG, 0.75f, 0.05f, 0.8f, 0, true)),
                    new TamedItemLootData(ModItems.carapacePretty.get(), 0.5f, 4));
            this.registerLootTable(ModEntities.ORC_HUNTER.get(), this.table(
                            new ItemLootData(Items.GUNPOWDER, 0.6f, COMMON_LUCK_BONUS, 1, 0, true),
                            new ItemLootData(ModItems.arrowHead.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0))
                    .withPool(this.create().add(this.addWithCount(Items.ARROW, -1, 1, 1))
                            .add(this.addWithCount(Items.STICK, 0, 4, 0.5f))));
            this.registerLootTable(ModEntities.HIGH_ORC.get(), this.table(
                            new ItemLootData(ModItems.cheapBracelet.get(), 0.09f, RARE_LUCK_BONUS, LOOTING_BONUS, 1),
                            new ItemLootData(ModItems.clothCheap.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.glue.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0))
                    .withPool(this.create().add(this.addWithCount(Items.STICK, 0, 4, 0.5f))));
            this.registerLootTable(ModEntities.HORNET.get(), this.table(
                            new ItemLootData(ModItems.carapaceInsect.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.carapacePretty.get(), 0.1f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.jawInsect.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(Items.HONEY_BOTTLE, 0.6f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(Items.HONEY_BOTTLE, 1, 0));
            this.registerLootTable(ModEntities.SILVER_WOLF.get(), this.table(
                            new ItemLootData(ModItems.fur.get(), 0.55f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.furQuality.get(), 0.15f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.fangWolf.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(ModItems.fangWolf.get(), 0.5f, 4));
            this.registerLootTable(ModEntities.LEAF_BALL.get(), this.table(
                    new ItemLootData(ModItems.plantStem.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.FURPY.get(), this.table(
                    new ItemLootData(ModItems.fur.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.furQuality.get(), 0.2f, RARE_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.PALM_CAT.get(), this.table(
                    new ItemLootData(ModItems.fur.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.clawPalm.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.MINO.get(), this.table(
                    new ItemLootData(ModItems.grapes.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.grapeJuice.get(), 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 1)));
            this.registerLootTable(ModEntities.TRICKY_MUCK.get(), this.table(
                            new ItemLootData(ModItems.spore.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.powderPoison.get(), 0.1f, RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.mushroom.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.monarchMushroom.get(), 0.008f, RARE_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(ModItems.powderPoison.get(), 0.7f, 0));
            this.registerLootTable(ModEntities.FLOWER_LILY.get(), this.table(
                    new ItemLootData(ModItems.plantStem.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.vine.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.lootTables.put(EntityKingWooly.KING_WOOLY_WOOLED_LOOT, this.table(
                            new ItemLootData(ModItems.furSmall.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2),
                            new ItemLootData(ModItems.furMedium.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2),
                            new ItemLootData(ModItems.furball.get(), 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 2),
                            new ItemLootData(Items.SHEARS, 0.01f, RARE_LUCK_BONUS, 0, 0))
                    .withPool(LootPool.lootPool().add(LootTableReference.lootTableReference(ModEntities.WOOLY.get().getDefaultLootTable()))));
            this.tamedDropTable(ModEntities.KING_WOOLY.get(), new TamedItemLootData(ModItems.furSmall.get(), 1, 0),
                    new TamedItemLootData(ModItems.furMedium.get(), 1, 5),
                    new TamedItemLootData(ModItems.furLarge.get(), 1, 8));
            this.registerLootTable(ModEntities.BUFFALOO.get(), this.table(
                                    new ItemLootData(ModItems.hornBull.get(), 0.27f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0))
                            .withPool(this.create().add(this.addWithCount(Items.LEATHER, -4, 2, 1)))
                            .withPool(this.create().add(this.addWithCount(Items.BEEF, -5, 3, 1))),
                    new TamedItemLootData(ModItems.hornBull.get(), 0.5f, 0));
            this.registerLootTable(ModEntities.GOBLIN_PIRATE.get(), this.table(
                    new ItemLootData(ModItems.clothQuality.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.oil.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.oldBandage.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.GOBLIN_GANGSTER.get(), this.table(
                    new ItemLootData(ModItems.clothQuality.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.oil.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.oldBandage.get(), 0.35f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.IGNIS.get(), this.table(
                            new ItemLootData(ModItems.crystalMagic.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.crystalFire.get(), 0.1f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(ModItems.crystalFire.get(), 0.4f, 0));
            this.registerLootTable(ModEntities.SCORPION.get(), this.table(
                            new ItemLootData(ModItems.tailScorpion.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                            new ItemLootData(ModItems.carapaceInsect.get(), 0.3f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)),
                    new TamedItemLootData(ModItems.tailScorpion.get(), 0.6f, 0));
            this.registerLootTable(ModEntities.TROLL.get(), this.table(
                    new ItemLootData(ModItems.giantsNail.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.gloveGiant.get(), 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.FLOWER_LION.get(), this.table(
                    new ItemLootData(ModItems.plantStem.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.vine.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.TOMATO_GHOST.get(), this.table(
                    new ItemLootData(ModItems.tomatoSeeds.get(), 0.1f, RARE_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.tomato.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.ghostHood.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0))); //Giant tomato

            this.registerLootTable(ModEntities.AMBROSIA.get(), this.table(
                    new ItemLootData(ModItems.ambrosiasThorns.get(), 0.65f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2, true, false),
                    new ItemLootData(ModItems.toyherb.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.plantStem.get(), 0.2f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.THUNDERBOLT.get(), this.table(
                    new ItemLootData(ModItems.lightningMane.get(), 0.65f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2, true, false),
                    new ItemLootData(ModItems.fur.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.furQuality.get(), 0.15f, RARE_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.MARIONETTA.get(), this.table(
                    new ItemLootData(ModItems.cursedDoll.get(), 0.65f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2, true, false),
                    new ItemLootData(ModItems.puppetryStrings.get(), 0.1f, COMMON_LUCK_BONUS, LOOTING_BONUS, 1),
                    new ItemLootData(ModItems.furSmall.get(), 0.8f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.furMedium.get(), 0.25f, RARE_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.DEAD_TREE.get(), this.table(
                    new ItemLootData(ModItems.movingBranch.get(), 0.6f, COMMON_LUCK_BONUS, RARE_LOOTING_BONUS, 2, true, false),
                    new ItemLootData(Items.APPLE, 0.8f, COMMON_LUCK_BONUS, 1, 0, true),
                    new ItemLootData(ModItems.crystalSmall.get(), 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 2)));
            this.registerLootTable(ModEntities.CHIMERA.get(), this.table(
                    new ItemLootData(ModItems.clawChimera.get(), 0.65f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2, true, false),
                    new ItemLootData(ModItems.tailChimera.get(), 0.05f, RARE_LUCK_BONUS, LOOTING_BONUS, 1),
                    new ItemLootData(ModItems.devilBlood.get(), 0.6f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.RACCOON.get(), this.table(
                    new ItemLootData(ModItems.raccoonLeaf.get(), 0.65f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2, true, false),
                    new ItemLootData(ModItems.udon.get(), 0.05f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 1),
                    new ItemLootData(ModItems.curryUdon.get(), 0.02f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 1),
                    new ItemLootData(ModItems.tempuraUdon.get(), 0.02f, VERY_RARE_LUCK_BONUS, LOOTING_BONUS, 1)));
            this.registerLootTable(ModEntities.SKELEFANG.get(), this.table(
                    new ItemLootData(ModItems.dragonBones.get(), 0.4f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2, true, false),
                    new ItemLootData(ModItems.fishFossil.get(), 0.7f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.skull.get(), 0.55f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0)));
            this.registerLootTable(ModEntities.RAFFLESIA.get(), this.table(
                    new ItemLootData(ModItems.root.get(), 0.65f, COMMON_LUCK_BONUS, LOOTING_BONUS, 0),
                    new ItemLootData(ModItems.rafflesiaPetal.get(), 0.6f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2, true, false)));

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
                        this.table(new ItemLootData(ModItems.crystalWater.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2));
                case EARTH ->
                        this.table(new ItemLootData(ModItems.crystalEarth.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2));
                case WIND ->
                        this.table(new ItemLootData(ModItems.crystalWind.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2));
                case FIRE ->
                        this.table(new ItemLootData(ModItems.crystalFire.get(), 0.25f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2));
                case LIGHT ->
                        this.table(new ItemLootData(ModItems.crystalLight.get(), 0.1f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2));
                case DARK ->
                        this.table(new ItemLootData(ModItems.crystalDark.get(), 0.1f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2));
                case LOVE ->
                        this.table(new ItemLootData(ModItems.crystalLove.get(), 0.05f, COMMON_LUCK_BONUS, LOOTING_BONUS, 2));
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
            b.otherwise(LootItem.lootTableItem(ModItems.furLarge.get()).when(FriendPointCondition.of(8))
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                    .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE)));
            b.otherwise(LootItem.lootTableItem(ModItems.furMedium.get()).when(FriendPointCondition.of(5))
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                    .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE)));
            b.otherwise(LootItem.lootTableItem(ModItems.furSmall.get()).when(FriendPointCondition.of(0))
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
            tier1.add(LootItem.lootTableItem(ModItems.forgingBread.get()));
            tier1.add(LootItem.lootTableItem(ModItems.armorBread.get()));
            tier1.add(LootItem.lootTableItem(ModItems.chemistryBread.get()));
            tier1.add(LootItem.lootTableItem(ModItems.cookingBread.get()));
            biConsumer.accept(LootTableResources.TIER_1_LOOT, LootTable.lootTable().withPool(tier1));

            LootPool.Builder tier2 = LootPool.lootPool().setRolls(UniformGenerator.between(2, 4));
            for (RegistryEntrySupplier<Item> item : ModItems.TIER_2_CHEST) {
                tier2.add(LootItem.lootTableItem(item.get()));
            }
            tier2.add(LootItem.lootTableItem(ModItems.forgingBread.get()));
            tier2.add(LootItem.lootTableItem(ModItems.armorBread.get()));
            tier2.add(LootItem.lootTableItem(ModItems.chemistryBread.get()));
            tier2.add(LootItem.lootTableItem(ModItems.cookingBread.get()));
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
                            .add(LootItem.lootTableItem(ModItems.hammerScrap.get())))
                    .withPool(LootPool.lootPool()
                            .add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))))
                            .add(LootItem.lootTableItem(Items.COPPER_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(6, 10))))));
            biConsumer.accept(QuestGen.TAMING, LootTable.lootTable().withPool(LootPool.lootPool()
                    .add(LootItem.lootTableItem(ModItems.brush.get()))));
            biConsumer.accept(QuestGen.SHIP_TURNIP, LootTable.lootTable().withPool(LootPool.lootPool()
                    .add(LootItem.lootTableItem(ModItems.turnipSeeds.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))))));

            LootPool.Builder spells = LootPool.lootPool().setRolls(UniformGenerator.between(-2, 1));
            spells.add(LootItem.lootTableItem(ModItems.fireBallSmall.get()).setWeight(140));
            spells.add(LootItem.lootTableItem(ModItems.fireBallBig.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(ModItems.explosion.get()).setWeight(20));
            spells.add(LootItem.lootTableItem(ModItems.waterLaser.get()).setWeight(110));
            spells.add(LootItem.lootTableItem(ModItems.parallelLaser.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(ModItems.deltaLaser.get()).setWeight(20));
            spells.add(LootItem.lootTableItem(ModItems.screwRock.get()).setWeight(110));
            spells.add(LootItem.lootTableItem(ModItems.earthSpike.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(ModItems.avengerRock.get()).setWeight(20));
            spells.add(LootItem.lootTableItem(ModItems.sonicWind.get()).setWeight(110));
            spells.add(LootItem.lootTableItem(ModItems.doubleSonic.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(ModItems.penetrateSonic.get()).setWeight(20));
            spells.add(LootItem.lootTableItem(ModItems.lightBarrier.get()).setWeight(90));
            spells.add(LootItem.lootTableItem(ModItems.shine.get()).setWeight(50));
            spells.add(LootItem.lootTableItem(ModItems.prism.get()).setWeight(15));
            spells.add(LootItem.lootTableItem(ModItems.darkSnake.get()).setWeight(100));
            spells.add(LootItem.lootTableItem(ModItems.darkBall.get()).setWeight(50));
            spells.add(LootItem.lootTableItem(ModItems.darkness.get()).setWeight(15));
            spells.add(LootItem.lootTableItem(ModItems.cure.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(ModItems.cureAll.get()).setWeight(30));
            spells.add(LootItem.lootTableItem(ModItems.cureMaster.get()).setWeight(10));
            spells.add(LootItem.lootTableItem(ModItems.mediPoison.get()).setWeight(80));
            spells.add(LootItem.lootTableItem(ModItems.mediPara.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(ModItems.mediSeal.get()).setWeight(10));

            spells.add(LootItem.lootTableItem(ModItems.powerWave.get()).setWeight(100));
            spells.add(LootItem.lootTableItem(ModItems.dashSlash.get()).setWeight(80));
            spells.add(LootItem.lootTableItem(ModItems.rushAttack.get()).setWeight(85));
            spells.add(LootItem.lootTableItem(ModItems.roundBreak.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(ModItems.mindThrust.get()).setWeight(85));
            spells.add(LootItem.lootTableItem(ModItems.gust.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(ModItems.storm.get()).setWeight(80));
            spells.add(LootItem.lootTableItem(ModItems.blitz.get()).setWeight(30));
            spells.add(LootItem.lootTableItem(ModItems.twinAttack.get()).setWeight(90));
            spells.add(LootItem.lootTableItem(ModItems.railStrike.get()).setWeight(50));
            spells.add(LootItem.lootTableItem(ModItems.windSlash.get()).setWeight(70));
            spells.add(LootItem.lootTableItem(ModItems.flashStrike.get()).setWeight(80));
            spells.add(LootItem.lootTableItem(ModItems.naiveBlade.get()).setWeight(70));
            spells.add(LootItem.lootTableItem(ModItems.steelHeart.get()).setWeight(40));
            spells.add(LootItem.lootTableItem(ModItems.deltaStrike.get()).setWeight(80));
            spells.add(LootItem.lootTableItem(ModItems.hurricane.get()).setWeight(90));
            spells.add(LootItem.lootTableItem(ModItems.reaperSlash.get()).setWeight(80));
            spells.add(LootItem.lootTableItem(ModItems.millionStrike.get()).setWeight(50));
            spells.add(LootItem.lootTableItem(ModItems.axelDisaster.get()).setWeight(50));
            spells.add(LootItem.lootTableItem(ModItems.stardustUpper.get()).setWeight(80));
            spells.add(LootItem.lootTableItem(ModItems.tornadoSwing.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(ModItems.grandImpact.get()).setWeight(70));
            spells.add(LootItem.lootTableItem(ModItems.gigaSwing.get()).setWeight(80));
            spells.add(LootItem.lootTableItem(ModItems.upperCut.get()).setWeight(100));
            spells.add(LootItem.lootTableItem(ModItems.doubleKick.get()).setWeight(90));
            spells.add(LootItem.lootTableItem(ModItems.straightPunch.get()).setWeight(80));
            spells.add(LootItem.lootTableItem(ModItems.nekoDamashi.get()).setWeight(100));
            spells.add(LootItem.lootTableItem(ModItems.rushPunch.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(ModItems.cyclone.get()).setWeight(60));
            spells.add(LootItem.lootTableItem(ModItems.rapidMove.get()).setWeight(90));
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

        protected static LootPool.Builder oreLootPool(EnumMineralTier tier) {
            LootPool.Builder build = LootPool.lootPool().setRolls(ConstantValue.exactly(1));
            switch (tier) {
                case IRON -> {
                    build.add(ore(100, 3, ModItems.scrap.get()));
                    build.add(ore(40, 17, Items.IRON_INGOT));
                    build.add(ore(5, 5, ModItems.invisStone.get()));
                    build.add(ore(10, 3, ModItems.invisStone.get(), 10));
                    build.add(ore(1, 7, ModItems.crystalSmall.get(), 40));
                }
                case BRONZE -> {
                    build.add(ore(100, 3, ModItems.scrap.get()));
                    build.add(ore(40, 15, Items.IRON_INGOT));
                    build.add(ore(25, 24, Items.COPPER_INGOT));
                    build.add(ore(4, 3, ModItems.invisStone.get(), 5));
                    build.add(ore(5, 3, ModItems.invisStone.get(), 20));
                    build.add(ore(3, 8, ModItems.crystalSmall.get(), 40));
                }
                case SILVER -> {
                    build.add(ore(100, 3, ModItems.scrap.get()));
                    build.add(ore(40, 12, Items.IRON_INGOT));
                    build.add(ore(20, 31, ModItems.silver.get()));
                    build.add(ore(2, 2, ModItems.invisStone.get(), 5));
                    build.add(ore(3, 2, ModItems.invisStone.get(), 10));
                    build.add(ore(3, 2, ModItems.invisStone.get(), 15));
                    build.add(ore(3, 8, ModItems.crystalSmall.get(), 40));
                }
                case GOLD -> {
                    build.add(ore(100, 3, ModItems.scrap.get()));
                    build.add(ore(50, 8, Items.IRON_INGOT));
                    build.add(ore(20, 31, Items.GOLD_INGOT));
                    build.add(ore(3, 8, ModItems.crystalSmall.get(), 40));
                }
                case DIAMOND -> {
                    build.add(ore(100, 3, ModItems.scrap.get()));
                    build.add(ore(5, 7, Items.DIAMOND));
                    build.add(ore(15, 27, Items.DIAMOND, 20));
                    build.add(ore(3, 8, ModItems.crystalSmall.get(), 40));
                }
                case PLATINUM -> {
                    build.add(ore(100, 3, ModItems.scrap.get()));
                    build.add(ore(4, 12, ModItems.platinum.get()));
                    build.add(ore(20, 29, ModItems.platinum.get(), 30));
                    build.add(ore(3, 8, ModItems.crystalSmall.get(), 40));
                }
                case ORICHALCUM -> {
                    build.add(ore(100, 3, ModItems.scrap.get()));
                    build.add(ore(4, 8, ModItems.orichalcum.get(), 20));
                    build.add(ore(10, 27, ModItems.orichalcum.get(), 40));
                    build.add(ore(5, 11, ModItems.crystalBig.get(), 40));
                }
                case DRAGONIC -> {
                    build.add(ore(100, 3, ModItems.scrap.get()));
                    build.add(ore(6, 9, ModItems.dragonic.get(), 20));
                    build.add(ore(10, 26, ModItems.dragonic.get(), 50));
                    build.add(ore(3, 9, ModItems.crystalSmall.get(), 40));
                }
                case AMETHYST -> {
                    build.add(ore(130, 3, ModItems.scrap.get()));
                    build.add(ore(40, 15, ModItems.amethyst.get()));
                    build.add(ore(15, 17, ModItems.crystalEarth.get()));
                    build.add(ore(1, 3, ModItems.lightOre.get()));
                    build.add(ore(5, 3, ModItems.lightOre.get(), 5));
                    build.add(ore(7, 5, ModItems.lightOre.get(), 15));
                }
                case AQUAMARINE -> {
                    build.add(ore(130, 3, ModItems.scrap.get()));
                    build.add(ore(40, 15, ModItems.aquamarine.get()));
                    build.add(ore(15, 17, ModItems.crystalWater.get()));
                    build.add(ore(1, 3, ModItems.lightOre.get()));
                    build.add(ore(5, 3, ModItems.lightOre.get(), 5));
                    build.add(ore(7, 5, ModItems.lightOre.get(), 15));
                }
                case RUBY -> {
                    build.add(ore(130, 3, ModItems.scrap.get()));
                    build.add(ore(40, 15, ModItems.ruby.get()));
                    build.add(ore(15, 17, ModItems.crystalFire.get()));
                    build.add(ore(1, 3, ModItems.lightOre.get()));
                    build.add(ore(5, 3, ModItems.lightOre.get(), 5));
                    build.add(ore(7, 5, ModItems.lightOre.get(), 15));
                }
                case EMERALD -> {
                    build.add(ore(130, 3, ModItems.scrap.get()));
                    build.add(ore(40, 15, Items.EMERALD));
                    build.add(ore(15, 17, ModItems.crystalWind.get()));
                    build.add(ore(1, 3, ModItems.lightOre.get()));
                    build.add(ore(5, 3, ModItems.lightOre.get(), 5));
                    build.add(ore(7, 5, ModItems.lightOre.get(), 15));
                }
                case SAPPHIRE -> {
                    build.add(ore(130, 3, ModItems.scrap.get()));
                    build.add(ore(50, 15, ModItems.sapphire.get()));
                    build.add(ore(3, 17, ModItems.crystalLove.get()));
                    build.add(ore(1, 3, ModItems.lightOre.get()));
                    build.add(ore(5, 3, ModItems.lightOre.get(), 5));
                    build.add(ore(7, 5, ModItems.lightOre.get(), 15));
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
            this.add(ModBlocks.mushroom.get(), LootTable.lootTable().withPool(herbLoot(ModItems.mushroom.get())));
            this.add(ModBlocks.monarchMushroom.get(), LootTable.lootTable().withPool(herbLoot(ModItems.monarchMushroom.get())));
            this.add(ModBlocks.elliLeaves.get(), LootTable.lootTable().withPool(herbLoot(ModItems.elliLeaves.get())));
            this.add(ModBlocks.witheredGrass.get(), LootTable.lootTable().withPool(herbLoot(ModItems.witheredGrass.get())));
            this.add(ModBlocks.weeds.get(), LootTable.lootTable().withPool(herbLoot(ModItems.weeds.get())));
            this.add(ModBlocks.whiteGrass.get(), LootTable.lootTable().withPool(herbLoot(ModItems.whiteGrass.get())));
            this.add(ModBlocks.indigoGrass.get(), LootTable.lootTable().withPool(herbLoot(ModItems.indigoGrass.get())));
            this.add(ModBlocks.purpleGrass.get(), LootTable.lootTable().withPool(herbLoot(ModItems.purpleGrass.get())));
            this.add(ModBlocks.greenGrass.get(), LootTable.lootTable().withPool(herbLoot(ModItems.greenGrass.get())));
            this.add(ModBlocks.blueGrass.get(), LootTable.lootTable().withPool(herbLoot(ModItems.blueGrass.get())));
            this.add(ModBlocks.yellowGrass.get(), LootTable.lootTable().withPool(herbLoot(ModItems.yellowGrass.get())));
            this.add(ModBlocks.redGrass.get(), LootTable.lootTable().withPool(herbLoot(ModItems.redGrass.get())));
            this.add(ModBlocks.orangeGrass.get(), LootTable.lootTable().withPool(herbLoot(ModItems.orangeGrass.get())));
            this.add(ModBlocks.blackGrass.get(), LootTable.lootTable().withPool(herbLoot(ModItems.blackGrass.get())));
            this.add(ModBlocks.antidoteGrass.get(), LootTable.lootTable().withPool(herbLoot(ModItems.antidoteGrass.get())));
            this.add(ModBlocks.medicinalHerb.get(), LootTable.lootTable().withPool(herbLoot(ModItems.medicinalHerb.get())));
            this.add(ModBlocks.bambooSprout.get(), LootTable.lootTable().withPool(herbLoot(ModItems.bambooSprout.get())));

            this.add(ModBlocks.forge.get(), block -> createSinglePropConditionTable(block, BlockCrafting.PART, BlockCrafting.EnumPart.LEFT));
            this.add(ModBlocks.cooking.get(), block -> createSinglePropConditionTable(block, BlockCrafting.PART, BlockCrafting.EnumPart.LEFT));
            this.add(ModBlocks.chemistry.get(), block -> createSinglePropConditionTable(block, BlockCrafting.PART, BlockCrafting.EnumPart.LEFT));
            this.add(ModBlocks.accessory.get(), block -> createSinglePropConditionTable(block, BlockCrafting.PART, BlockCrafting.EnumPart.LEFT));

            this.dropSelf(ModBlocks.shipping.get());
            this.dropSelf(ModBlocks.cashRegister.get());
            this.dropSelf(ModBlocks.monsterBarn.get());
            this.add(ModBlocks.questBoard.get(), block -> createSinglePropConditionTable(block, BlockQuestboard.PART, BlockQuestboard.Part.BOTTOM_LEFT));

            ModBlocks.crops.forEach(reg -> {
                Block block = reg.get();
                if (block instanceof BlockCrop)
                    this.add(reg.get(), LootTable.lootTable().withPool(cropLoot((BlockCrop) block)));
            });
            ModBlocks.flowers.forEach(reg -> {
                Block block = reg.get();
                if (block instanceof BlockCrop)
                    this.add(reg.get(), LootTable.lootTable().withPool(cropLoot((BlockCrop) block)));
            });
            ModBlocks.mineralMap.forEach((tier, reg) -> this.add(reg.get(), LootTable.lootTable().withPool(oreLootPool(tier))));

            this.add(ModBlocks.accessory.get(), block -> createSinglePropConditionTable(block, BlockCrafting.PART, BlockCrafting.EnumPart.LEFT));

            //Copy of snow layer
            this.add(ModBlocks.snow.get(), block -> LootTable.lootTable().withPool(LootPool.lootPool().when(LootItemEntityPropertyCondition.entityPresent(LootContext.EntityTarget.THIS)).add(AlternativesEntry.alternatives(AlternativesEntry.alternatives(new LootPoolEntryContainer.Builder[]{LootItem.lootTableItem(Items.SNOWBALL).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, 1))), (LootItem.lootTableItem(Items.SNOWBALL).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, 2)))).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))), (LootItem.lootTableItem(Items.SNOWBALL).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, 3)))).apply(SetItemCountFunction.setCount(ConstantValue.exactly(3.0F))), (LootItem.lootTableItem(Items.SNOWBALL).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, 4)))).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))), (LootItem.lootTableItem(Items.SNOWBALL).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, 5)))).apply(SetItemCountFunction.setCount(ConstantValue.exactly(5.0F))), (LootItem.lootTableItem(Items.SNOWBALL).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, 6)))).apply(SetItemCountFunction.setCount(ConstantValue.exactly(6.0F))), (LootItem.lootTableItem(Items.SNOWBALL).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, 7)))).apply(SetItemCountFunction.setCount(ConstantValue.exactly(7.0F))), LootItem.lootTableItem(Items.SNOWBALL).apply(SetItemCountFunction.setCount(ConstantValue.exactly(8.0F)))}).when(SILK_TOUCH), AlternativesEntry.alternatives(LootItem.lootTableItem(Blocks.SNOW).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, 1))), LootItem.lootTableItem(Blocks.SNOW).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, 2))), LootItem.lootTableItem(Blocks.SNOW).apply(SetItemCountFunction.setCount(ConstantValue.exactly(3.0F))).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, 3))), LootItem.lootTableItem(Blocks.SNOW).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, 4))), LootItem.lootTableItem(Blocks.SNOW).apply(SetItemCountFunction.setCount(ConstantValue.exactly(5.0F))).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, 5))), LootItem.lootTableItem(Blocks.SNOW).apply(SetItemCountFunction.setCount(ConstantValue.exactly(6.0F))).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, 6))), LootItem.lootTableItem(Blocks.SNOW).apply(SetItemCountFunction.setCount(ConstantValue.exactly(7.0F))).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, 7))), LootItem.lootTableItem(Blocks.SNOW_BLOCK))))));
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
