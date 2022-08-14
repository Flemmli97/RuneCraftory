package io.github.flemmli97.runecraftory.forge.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.api.enums.EnumMineralTier;
import io.github.flemmli97.runecraftory.common.blocks.BlockCrafting;
import io.github.flemmli97.runecraftory.common.blocks.BlockCrop;
import io.github.flemmli97.runecraftory.common.loot.GiantLootCondition;
import io.github.flemmli97.runecraftory.common.loot.ItemLevelLootFunction;
import io.github.flemmli97.runecraftory.common.loot.MiningLootCondition;
import io.github.flemmli97.runecraftory.common.loot.VanillaDropCondition;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Loottables extends LootTableProvider {

    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> loot = ImmutableList.of(Pair.of(EntityLoot::new, LootContextParamSets.ENTITY), Pair.of(BlockLootData::new, LootContextParamSets.BLOCK));

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

        private final Map<ResourceLocation, LootTable.Builder> lootTables = new HashMap<>();

        private void init() {
            this.registerLootTable(ModEntities.wooly.get(), LootTable.lootTable()
                    .withPool(this.create().add(this.add(ModItems.furSmall.get(), 0.3f, 0.1f, 2, 1, 10, 5))
                            .add(this.addVanilla(Items.WHITE_WOOL, 1, 1, 0.5f))
                            .add(this.addVanilla(Items.MUTTON, 0, 2, 1))));
            this.registerLootTable(ModEntities.ant.get(), LootTable.lootTable()
                    .withPool(this.create().add(this.add(ModItems.carapaceInsect.get(), 0.3f, 0.2f, 1, 1, 15, 2))
                            .add(this.add(ModItems.jawInsect.get(), 0.2f, 0.2f, 1, 1, 10, 5))));
            this.registerLootTable(ModEntities.orcArcher.get(), LootTable.lootTable()
                    .withPool(this.create().add(this.add(ModItems.bladeShard.get(), 0.4f, 0.2f, 1, 1, 10, 5))
                            .add(this.add(ModItems.clothCheap.get(), 0.6f, 0.3f, 1, 0, 15, 2))
                            .add(this.addVanilla(Items.ARROW, -1, 2, 1))));
            this.registerLootTable(ModEntities.orc.get(), LootTable.lootTable()
                    .withPool(this.create()//.add(this.add(ModItems.cheapBracelet.get(), 0.05f, 0.05f, 0, 1, 10, 5))
                            .add(this.add(ModItems.glue.get(), 0.6f, 0.3f, 1, 1, 15, 2))));
            this.registerLootTable(ModEntities.beetle.get(), LootTable.lootTable()
                    .withPool(this.create().add(this.add(ModItems.hornInsect.get(), 0.2f, 0.1f, 1, 1, 10, 5))));
            this.registerLootTable(ModEntities.big_muck.get(), LootTable.lootTable()
                    .withPool(this.create().add(this.add(ModItems.spore.get(), 0.2f, 0.05f, 1, 1, 10, 5))));
            this.registerLootTable(ModEntities.buffamoo.get(), LootTable.lootTable()
                    .withPool(this.create().add(this.add(ModItems.milkS.get(), 0.2f, 0.05f, 2, 1, 10, 5))
                            .add(this.addVanilla(Items.LEATHER, -1, 1, 1))
                            .add(this.addVanilla(Items.BEEF, -1, 2, 1))));
            this.registerLootTable(ModEntities.chipsqueek.get(), LootTable.lootTable()
                    .withPool(this.create().add(this.add(ModItems.fur.get(), 0.2f, 0.05f, 1, 1, 10, 5))));
            this.registerLootTable(ModEntities.cluckadoodle.get(), LootTable.lootTable()
                    .withPool(this.create().add(this.add(ModItems.eggS.get(), 0.2f, 0.05f, 2, 1, 10, 5))
                            .add(this.addVanilla(Items.CHICKEN, 0, 1, 0.7f))
                            .add(this.addVanilla(Items.FEATHER, 0, 2, 1))));
            this.registerLootTable(ModEntities.pomme_pomme.get(), LootTable.lootTable()
                    .withPool(this.create().add(this.add(Items.APPLE, 0.2f, 0.05f, 1, 3, 10, 5))));
            this.registerLootTable(ModEntities.tortas.get(), LootTable.lootTable()
                    .withPool(this.create().add(this.add(ModItems.turtleShell.get(), 0.2f, 0.05f, 1, 1, 10, 5))));
            this.registerLootTable(ModEntities.sky_fish.get(), LootTable.lootTable()
                    .withPool(this.create().add(this.add(ModItems.fishFossil.get(), 0.2f, 0.05f, 1, 1, 10, 5))));
            this.registerLootTable(ModEntities.weagle.get(), LootTable.lootTable()
                    .withPool(this.create().add(this.add(Items.FEATHER, 0.2f, 0.05f, 2, 1, 10, 5))));
            this.registerLootTable(ModEntities.goblin.get(), LootTable.lootTable()
                    .withPool(this.create().add(this.add(ModItems.bladeShard.get(), 0.2f, 0.05f, 1, 1, 10, 5))));
            this.registerLootTable(ModEntities.goblinArcher.get(), LootTable.lootTable()
                    .withPool(this.create().add(this.add(ModItems.arrowHead.get(), 0.2f, 0.05f, 1, 1, 10, 5))
                            .add(this.addVanilla(Items.ARROW, -1, 2, 1))));
            this.registerLootTable(ModEntities.duck.get(), LootTable.lootTable()
                    .withPool(this.create().add(this.add(ModItems.downYellow.get(), 0.4f, 0.05f, 1, 1, 10, 5))));
            this.registerLootTable(ModEntities.fairy.get(), LootTable.lootTable()
                    .withPool(this.create().add(this.add(ModItems.fairyDust.get(), 0.2f, 0.05f, 1, 1, 10, 5))));
            this.registerLootTable(ModEntities.ghost.get(), LootTable.lootTable()
                    .withPool(this.create().add(this.add(ModItems.ghostHood.get(), 0.3f, 0.05f, 1, 1, 15, 5))
                            .add(this.add(ModItems.stickThick.get(), 0.5f, 0.05f, 1, 1, 20, 2))
                            .add(this.add(ModItems.skull.get(), 0.3f, 0.05f, 1, 1, 15, 5))));
            this.registerLootTable(ModEntities.spirit.get(), LootTable.lootTable()
                    .withPool(this.create().add(this.add(ModItems.crystalDark.get(), 0.15f, 0.05f, 1, 1, 15, 5))
                            .add(this.add(ModItems.crystalMagic.get(), 0.1f, 0.05f, 1, 1, 10, 2))));
            this.registerLootTable(ModEntities.ghostRay.get(), LootTable.lootTable()
                    .withPool(this.create().add(this.add(ModItems.ghostHood.get(), 0.33f, 0.05f, 1, 1, 15, 5))
                            .add(this.add(ModItems.stickThick.get(), 0.5f, 0.05f, 1, 1, 20, 2))));
            this.registerLootTable(ModEntities.spider.get(), LootTable.lootTable()
                    .withPool(this.create().add(this.add(ModItems.jawInsect.get(), 0.23f, 0.05f, 1, 1, 15, 5))
                            .add(this.add(Items.STRING, 0.4f, 0.05f, 1, 1, 20, 2))));
            this.registerLootTable(ModEntities.shadowPanther.get(), LootTable.lootTable()
                    .withPool(this.create().add(this.add(ModItems.clawPanther.get(), 0.23f, 0.05f, 1, 1, 15, 5))
                            .add(this.add(ModItems.furQuality.get(), 0.3f, 0.05f, 1, 1, 20, 2))));
            this.registerLootTable(ModEntities.monsterBox.get(), LootTable.lootTable()
                    .withPool(this.create().add(this.add(ModItems.brokenHilt.get(), 0.25f, 0.05f, 1, 1, 15, 5))
                            .add(this.add(ModItems.failedDish.get(), 0.2f, 0.05f, 1, 1, 20, 2))
                            .add(this.add(ModItems.disastrousDish.get(), 0.05f, 0.03f, 1, 1, 20, 2))));
            this.registerLootTable(ModEntities.gobbleBox.get(), LootTable.lootTable()
                    .withPool(this.create().add(this.add(ModItems.brokenHilt.get(), 0.4f, 0.05f, 1, 1, 15, 5))
                            .add(this.add(ModItems.failedDish.get(), 0.25f, 0.05f, 1, 1, 20, 2))
                            .add(this.add(ModItems.disastrousDish.get(), 0.1f, 0.03f, 1, 1, 20, 2))
                            .add(this.add(ModItems.brokenBox.get(), 0.25f, 0.07f, 1, 1, 20, 2))));

            this.registerLootTable(ModEntities.ambrosia.get(), LootTable.lootTable()
                    .withPool(this.create().add(this.add(ModItems.ambrosiasThorns.get(), 0.65f, 0.1f, 1, 1, 10, 5))));
            this.registerLootTable(ModEntities.thunderbolt.get(), LootTable.lootTable()
                    .withPool(this.create().add(this.add(ModItems.lightningMane.get(), 0.65f, 0.1f, 1, 1, 10, 5))));
            this.registerLootTable(ModEntities.marionetta.get(), LootTable.lootTable()
                    .withPool(this.create().add(this.add(ModItems.cursedDoll.get(), 0.65f, 0.1f, 1, 1, 15, 5))
                            .add(this.add(ModItems.puppetryStrings.get(), 0.2f, 0.1f, 1, 1, 20, 2))
                            .add(this.add(ModItems.furSmall.get(), 0.5f, 0.05f, 1, 1, 20, 2))
                            .add(this.add(ModItems.furMedium.get(), 0.5f, 0.05f, 1, 1, 20, 2))));

        }

        private LootPool.Builder create() {
            return LootPool.lootPool().setRolls(ConstantValue.exactly(1));
        }

        protected void registerLootTable(EntityType<?> type, LootTable.Builder builder) {
            this.lootTables.put(type.getDefaultLootTable(), builder);
        }

        private LootPoolSingletonContainer.Builder<?> add(ItemLike item, float chance, float lootingBonus, float lootingCountBonus, int lootingCountMax, int weight, int quality) {
            return LootItem.lootTableItem(item)
                    .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, lootingCountBonus)).setLimit(lootingCountMax))
                    .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(chance, lootingBonus))
                    .setWeight(weight).setQuality(quality);
        }

        private LootPoolSingletonContainer.Builder<?> addVanilla(ItemLike item, float min, float max, float lootingCountBonus) {
            return LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)))
                    .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, lootingCountBonus)))
                    .when(VanillaDropCondition.get());
        }

        @Override
        public void accept(BiConsumer<ResourceLocation, LootTable.Builder> cons) {
            this.init();
            this.lootTables.forEach(cons);
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
            build.add(LootItem.lootTableItem(block.getCrop()).when(GiantLootCondition.get(false)));
            build.add(LootItem.lootTableItem(block.getGiantCrop()).when(GiantLootCondition.get(true)));
            return build;
        }

        protected static LootPool.Builder oreLootPool(EnumMineralTier tier) {
            LootPool.Builder build = LootPool.lootPool().setRolls(ConstantValue.exactly(1));
            switch (tier) {
                case IRON -> {
                    build.add(ore(100, 3, ModItems.scrap.get()));
                    build.add(ore(40, 10, Items.IRON_INGOT));
                    build.add(ore(1, 3, ModItems.crystalSmall.get(), 40));
                }
                case BRONZE -> {
                    build.add(ore(100, 3, ModItems.scrap.get()));
                    build.add(ore(40, 10, Items.IRON_INGOT));
                    build.add(ore(25, 10, Items.COPPER_INGOT));
                    build.add(ore(3, 5, ModItems.crystalSmall.get(), 40));
                }
                case SILVER -> {
                    build.add(ore(100, 3, ModItems.scrap.get()));
                    build.add(ore(45, 7, Items.IRON_INGOT));
                    build.add(ore(20, 15, ModItems.silver.get()));
                    build.add(ore(3, 5, ModItems.crystalSmall.get(), 40));
                }
                case GOLD -> {
                    build.add(ore(100, 3, ModItems.scrap.get()));
                    build.add(ore(50, 8, Items.IRON_INGOT));
                    build.add(ore(20, 10, Items.GOLD_INGOT));
                    build.add(ore(3, 5, ModItems.crystalSmall.get(), 40));
                }
                case DIAMOND -> {
                    build.add(ore(100, 3, ModItems.scrap.get()));
                    build.add(ore(3, 3, Items.DIAMOND));
                    build.add(ore(10, 5, Items.DIAMOND, 20));
                    build.add(ore(3, 5, ModItems.crystalSmall.get(), 40));
                }
                case PLATINUM -> {
                    build.add(ore(100, 3, ModItems.scrap.get()));
                    build.add(ore(4, 5, ModItems.platinum.get()));
                    build.add(ore(20, 5, ModItems.platinum.get(), 30));
                    build.add(ore(3, 5, ModItems.crystalSmall.get(), 40));
                }
                case ORICHALCUM -> {
                    build.add(ore(100, 3, ModItems.scrap.get()));
                    build.add(ore(4, 5, ModItems.orichalcum.get(), 20));
                    build.add(ore(10, 5, ModItems.orichalcum.get(), 40));
                    build.add(ore(5, 5, ModItems.crystalBig.get(), 40));
                }
                case DRAGONIC -> {
                    build.add(ore(100, 3, ModItems.scrap.get()));
                    build.add(ore(6, 4, ModItems.dragonic.get(), 20));
                    build.add(ore(10, 5, ModItems.dragonic.get(), 50));
                    build.add(ore(3, 5, ModItems.crystalSmall.get(), 40));
                }
                case AMETHYST -> {
                    build.add(ore(100, 3, ModItems.scrap.get()));
                    build.add(ore(40, 10, ModItems.amethyst.get()));
                    build.add(ore(10, 7, ModItems.crystalEarth.get()));
                }
                case AQUAMARINE -> {
                    build.add(ore(100, 3, ModItems.scrap.get()));
                    build.add(ore(40, 10, ModItems.aquamarine.get()));
                    build.add(ore(10, 7, ModItems.crystalWater.get()));
                }
                case RUBY -> {
                    build.add(ore(100, 3, ModItems.scrap.get()));
                    build.add(ore(40, 10, ModItems.ruby.get()));
                    build.add(ore(10, 7, ModItems.crystalFire.get()));
                }
                case EMERALD -> {
                    build.add(ore(100, 3, ModItems.scrap.get()));
                    build.add(ore(40, 10, Items.EMERALD));
                    build.add(ore(10, 7, ModItems.crystalWind.get()));
                }
                case SAPPHIRE -> {
                    build.add(ore(100, 3, ModItems.scrap.get()));
                    build.add(ore(50, 10, ModItems.sapphire.get()));
                    build.add(ore(3, 7, ModItems.crystalLove.get()));
                }
            }
            build.apply(ItemLevelLootFunction.getDef());
            return build;
        }

        private static LootPoolSingletonContainer.Builder<?> ore(int weight, int quality, ItemLike item) {
            return LootItem.lootTableItem(item).setWeight(weight).setQuality(quality);
        }

        private static LootPoolSingletonContainer.Builder<?> ore(int weight, int quality, ItemLike item, int minMiningLevel) {
            return ore(weight, quality, item).when(MiningLootCondition.get(minMiningLevel));
        }

        @Override
        public void accept(BiConsumer<ResourceLocation, LootTable.Builder> cons) {
            this.dropOther(ModBlocks.farmland.get(), Blocks.DIRT);
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

            this.dropSelf(ModBlocks.board.get());
            this.dropSelf(ModBlocks.shipping.get());

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
}
