package com.flemmli97.runecraftory.data;

import com.flemmli97.runecraftory.api.enums.EnumMineralTier;
import com.flemmli97.runecraftory.common.blocks.BlockCrop;
import com.flemmli97.runecraftory.common.loot.GiantLootCondition;
import com.flemmli97.runecraftory.common.loot.ItemLevelLootFunction;
import com.flemmli97.runecraftory.common.loot.MiningLootCondition;
import com.flemmli97.runecraftory.common.loot.VanillaDropCondition;
import com.flemmli97.runecraftory.common.registry.ModBlocks;
import com.flemmli97.runecraftory.common.registry.ModEntities;
import com.flemmli97.runecraftory.common.registry.ModItems;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.StandaloneLootEntry;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.MatchTool;
import net.minecraft.loot.conditions.RandomChanceWithLooting;
import net.minecraft.loot.functions.LootingEnchantBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ForgeLootTableProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Loottables extends ForgeLootTableProvider {

    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> loot = ImmutableList.of(Pair.of(EntityLoot::new, LootParameterSets.ENTITY), Pair.of(BlockLoot::new, LootParameterSets.BLOCK));

    public Loottables(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return this.loot;
    }

    static class EntityLoot implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {

        private final Map<ResourceLocation, LootTable.Builder> lootTables = new HashMap<>();

        private void init() {
            this.registerLootTable(ModEntities.wooly.get(), LootTable.builder()
                    .addLootPool(this.create().addEntry(this.add(ModItems.furSmall.get(), 0.3f, 0.1f, 2, 1, 10, 5))
                            .addEntry(this.addVanilla(Items.WHITE_WOOL, 1, 1, 0.5f))
                            .addEntry(this.addVanilla(Items.MUTTON, 0, 2, 1))));
            this.registerLootTable(ModEntities.ant.get(), LootTable.builder()
                    .addLootPool(this.create().addEntry(this.add(ModItems.carapaceInsect.get(), 0.3f, 0.2f, 1, 1, 15, 2))
                            .addEntry(this.add(ModItems.jawInsect.get(), 0.2f, 0.2f, 1, 1, 10, 5))));
            this.registerLootTable(ModEntities.orcArcher.get(), LootTable.builder()
                    .addLootPool(this.create().addEntry(this.add(ModItems.bladeShard.get(), 0.4f, 0.2f, 1, 1, 10, 5))
                            .addEntry(this.add(ModItems.clothCheap.get(), 0.6f, 0.3f, 1, 0, 15, 2))
                            .addEntry(this.addVanilla(Items.ARROW, -1, 2, 1))));
            this.registerLootTable(ModEntities.orc.get(), LootTable.builder()
                    .addLootPool(this.create().addEntry(this.add(ModItems.cheapBracelet.get(), 0.05f, 0.05f, 0, 1, 10, 5))
                            .addEntry(this.add(ModItems.glue.get(), 0.6f, 0.3f, 1, 1, 15, 2))));
            this.registerLootTable(ModEntities.beetle.get(), LootTable.builder()
                    .addLootPool(this.create().addEntry(this.add(ModItems.hornInsect.get(), 0.2f, 0.1f, 1, 1, 10, 5))));
            this.registerLootTable(ModEntities.big_muck.get(), LootTable.builder()
                    .addLootPool(this.create().addEntry(this.add(ModItems.spore.get(), 0.2f, 0.05f, 1, 1, 10, 5))));
            this.registerLootTable(ModEntities.buffamoo.get(), LootTable.builder()
                    .addLootPool(this.create().addEntry(this.add(ModItems.milkS.get(), 0.2f, 0.05f, 2, 1, 10, 5))
                            .addEntry(this.addVanilla(Items.LEATHER, -1, 1, 1))
                            .addEntry(this.addVanilla(Items.BEEF, -1, 2, 1))));
            this.registerLootTable(ModEntities.chipsqueek.get(), LootTable.builder()
                    .addLootPool(this.create().addEntry(this.add(ModItems.fur.get(), 0.2f, 0.05f, 1, 1, 10, 5))));
            this.registerLootTable(ModEntities.cluckadoodle.get(), LootTable.builder()
                    .addLootPool(this.create().addEntry(this.add(ModItems.eggS.get(), 0.2f, 0.05f, 2, 1, 10, 5))
                            .addEntry(this.addVanilla(Items.CHICKEN, 0, 1, 0.7f))
                            .addEntry(this.addVanilla(Items.FEATHER, 0, 2, 1))));
            this.registerLootTable(ModEntities.pomme_pomme.get(), LootTable.builder()
                    .addLootPool(this.create().addEntry(this.add(Items.APPLE, 0.2f, 0.05f, 1, 3, 10, 5))));
            this.registerLootTable(ModEntities.tortas.get(), LootTable.builder()
                    .addLootPool(this.create().addEntry(this.add(ModItems.turtleShell.get(), 0.2f, 0.05f, 1, 1, 10, 5))));
            this.registerLootTable(ModEntities.sky_fish.get(), LootTable.builder()
                    .addLootPool(this.create().addEntry(this.add(ModItems.fishFossil.get(), 0.2f, 0.05f, 1, 1, 10, 5))));
            this.registerLootTable(ModEntities.weagle.get(), LootTable.builder()
                    .addLootPool(this.create().addEntry(this.add(Items.FEATHER, 0.2f, 0.05f, 2, 1, 10, 5))));
            this.registerLootTable(ModEntities.goblin.get(), LootTable.builder()
                    .addLootPool(this.create().addEntry(this.add(ModItems.bladeShard.get(), 0.2f, 0.05f, 1, 1, 10, 5))));
            this.registerLootTable(ModEntities.goblinArcher.get(), LootTable.builder()
                    .addLootPool(this.create().addEntry(this.add(ModItems.arrowHead.get(), 0.2f, 0.05f, 1, 1, 10, 5))
                            .addEntry(this.addVanilla(Items.ARROW, -1, 2, 1))));

            this.registerLootTable(ModEntities.ambrosia.get(), LootTable.builder()
                    .addLootPool(this.create().addEntry(this.add(ModItems.ambrosiasThorns.get(), 0.65f, 0.1f, 1, 1, 10, 5))));
            this.registerLootTable(ModEntities.thunderbolt.get(), LootTable.builder()
                    .addLootPool(this.create().addEntry(this.add(ModItems.lightningMane.get(), 0.65f, 0.1f, 1, 1, 10, 5))));
        }

        private LootPool.Builder create() {
            return LootPool.builder().rolls(ConstantRange.of(1));
        }

        protected void registerLootTable(EntityType<?> type, LootTable.Builder builder) {
            this.lootTables.put(type.getLootTable(), builder);
        }

        private StandaloneLootEntry.Builder<?> add(IItemProvider item, float chance, float lootingBonus, float lootingCountBonus, int lootingCountMax, int weight, int quality) {
            return ItemLootEntry.builder(item)
                    .acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0, lootingCountBonus)).func_216072_a(lootingCountMax))
                    .acceptCondition(RandomChanceWithLooting.builder(chance, lootingBonus))
                    .weight(weight).quality(quality);
        }

        private StandaloneLootEntry.Builder<?> addVanilla(IItemProvider item, float min, float max, float lootingCountBonus) {
            return ItemLootEntry.builder(item).acceptFunction(SetCount.builder(new RandomValueRange(min, max)))
                    .acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0, lootingCountBonus)))
                    .acceptCondition(VanillaDropCondition.get());
        }

        @Override
        public void accept(BiConsumer<ResourceLocation, LootTable.Builder> cons) {
            this.init();
            this.lootTables.forEach(cons);
        }
    }

    static class BlockLoot extends BlockLootTables {

        private final Map<ResourceLocation, LootTable.Builder> loots = new HashMap<>();

        private static final ILootCondition.IBuilder SILK_TOUCH = MatchTool.builder(ItemPredicate.Builder.create().enchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast(1))));

        @Override
        public void accept(BiConsumer<ResourceLocation, LootTable.Builder> cons) {
            this.registerDropSelfLootTable(ModBlocks.farmland.get());
            this.registerLootTable(ModBlocks.mushroom.get(), LootTable.builder().addLootPool(herbLoot(ModItems.mushroom.get())));
            this.registerLootTable(ModBlocks.monarchMushroom.get(), LootTable.builder().addLootPool(herbLoot(ModItems.monarchMushroom.get())));
            this.registerLootTable(ModBlocks.elliLeaves.get(), LootTable.builder().addLootPool(herbLoot(ModItems.elliLeaves.get())));
            this.registerLootTable(ModBlocks.witheredGrass.get(), LootTable.builder().addLootPool(herbLoot(ModItems.witheredGrass.get())));
            this.registerLootTable(ModBlocks.weeds.get(), LootTable.builder().addLootPool(herbLoot(ModItems.weeds.get())));
            this.registerLootTable(ModBlocks.whiteGrass.get(), LootTable.builder().addLootPool(herbLoot(ModItems.whiteGrass.get())));
            this.registerLootTable(ModBlocks.indigoGrass.get(), LootTable.builder().addLootPool(herbLoot(ModItems.indigoGrass.get())));
            this.registerLootTable(ModBlocks.purpleGrass.get(), LootTable.builder().addLootPool(herbLoot(ModItems.purpleGrass.get())));
            this.registerLootTable(ModBlocks.greenGrass.get(), LootTable.builder().addLootPool(herbLoot(ModItems.greenGrass.get())));
            this.registerLootTable(ModBlocks.blueGrass.get(), LootTable.builder().addLootPool(herbLoot(ModItems.blueGrass.get())));
            this.registerLootTable(ModBlocks.yellowGrass.get(), LootTable.builder().addLootPool(herbLoot(ModItems.yellowGrass.get())));
            this.registerLootTable(ModBlocks.redGrass.get(), LootTable.builder().addLootPool(herbLoot(ModItems.redGrass.get())));
            this.registerLootTable(ModBlocks.orangeGrass.get(), LootTable.builder().addLootPool(herbLoot(ModItems.orangeGrass.get())));
            this.registerLootTable(ModBlocks.blackGrass.get(), LootTable.builder().addLootPool(herbLoot(ModItems.blackGrass.get())));
            this.registerLootTable(ModBlocks.antidoteGrass.get(), LootTable.builder().addLootPool(herbLoot(ModItems.antidoteGrass.get())));
            this.registerLootTable(ModBlocks.medicinalHerb.get(), LootTable.builder().addLootPool(herbLoot(ModItems.medicinalHerb.get())));
            this.registerLootTable(ModBlocks.bambooSprout.get(), LootTable.builder().addLootPool(herbLoot(ModItems.bambooSprout.get())));

            ModBlocks.crops.forEach(reg -> {
                Block block = reg.get();
                if (block instanceof BlockCrop)
                    this.registerLootTable(reg.get(), LootTable.builder().addLootPool(cropLoot((BlockCrop) block)));
            });
            ModBlocks.flowers.forEach(reg -> {
                Block block = reg.get();
                if (block instanceof BlockCrop)
                    this.registerLootTable(reg.get(), LootTable.builder().addLootPool(cropLoot((BlockCrop) block)));
            });
            ModBlocks.mineralMap.forEach((tier, reg) -> this.registerLootTable(reg.get(), LootTable.builder().addLootPool(oreLootPool(tier))));
            this.loots.forEach(cons);
        }

        protected static LootPool.Builder herbLoot(IItemProvider item) {
            LootPool.Builder build = LootPool.builder().rolls(ConstantRange.of(1));
            build.addEntry(ItemLootEntry.builder(item).acceptFunction(ItemLevelLootFunction.getDef()));
            return build;
        }

        protected static LootPool.Builder cropLoot(BlockCrop block) {
            LootPool.Builder build = LootPool.builder().rolls(ConstantRange.of(1));
            build.addEntry(ItemLootEntry.builder(block.getCrop()).acceptCondition(GiantLootCondition.get(false)));
            build.addEntry(ItemLootEntry.builder(block.getGiantCrop()).acceptCondition(GiantLootCondition.get(true)));
            return build;
        }

        protected static LootPool.Builder oreLootPool(EnumMineralTier tier) {
            LootPool.Builder build = LootPool.builder().rolls(ConstantRange.of(1));
            switch (tier) {
                case IRON:
                    build.addEntry(ore(70, 0, ModItems.scrap.get()));
                    build.addEntry(ore(40, 10, Items.IRON_INGOT));
                    build.addEntry(ore(1, 3, ModItems.crystalSmall.get(), 40));
                    break;
                case BRONZE:
                    build.addEntry(ore(60, 0, ModItems.scrap.get()));
                    build.addEntry(ore(40, 10, Items.IRON_INGOT));
                    build.addEntry(ore(30, 10, ModItems.bronze.get()));
                    build.addEntry(ore(2, 4, ModItems.crystalSmall.get(), 40));
                    build.addEntry(ore(1, 3, ModItems.crystalSmall.get(), 40));
                    break;
                case SILVER:
                    build.addEntry(ore(50, 0, ModItems.scrap.get()));
                    build.addEntry(ore(70, 0, Items.IRON_INGOT));
                    build.addEntry(ore(40, 10, ModItems.silver.get()));
                    build.addEntry(ore(1, 3, ModItems.crystalSmall.get(), 40));
                    break;
                case GOLD:
                    build.addEntry(ore(50, 0, ModItems.scrap.get()));
                    build.addEntry(ore(50, 0, Items.IRON_INGOT));
                    build.addEntry(ore(20, 10, Items.GOLD_INGOT));
                    build.addEntry(ore(1, 3, ModItems.crystalSmall.get(), 40));
                    break;
                case DIAMOND:
                    build.addEntry(ore(50, 0, ModItems.scrap.get()));
                    build.addEntry(ore(10, 10, Items.DIAMOND, 20));
                    build.addEntry(ore(1, 3, ModItems.crystalSmall.get(), 40));
                    break;
                case PLATINUM:
                    build.addEntry(ore(70, 0, ModItems.scrap.get()));
                    build.addEntry(ore(20, 10, ModItems.platinum.get(), 30));
                    build.addEntry(ore(1, 3, ModItems.crystalSmall.get(), 40));
                    break;
                case ORICHALCUM:
                    build.addEntry(ore(70, 0, ModItems.scrap.get()));
                    build.addEntry(ore(10, 10, ModItems.orichalcum.get(), 40));
                    build.addEntry(ore(5, 5, ModItems.crystalBig.get(), 40));
                    break;
                case DRAGONIC:
                    build.addEntry(ore(90, 0, ModItems.scrap.get()));
                    build.addEntry(ore(10, 10, ModItems.dragonic.get(), 50));
                    build.addEntry(ore(1, 3, ModItems.crystalSmall.get(), 40));
                    break;
                case AMETHYST:
                    build.addEntry(ore(70, 0, ModItems.scrap.get()));
                    build.addEntry(ore(40, 10, ModItems.amethyst.get()));
                    build.addEntry(ore(10, 10, ModItems.crystalEarth.get()));
                    break;
                case AQUAMARINE:
                    build.addEntry(ore(70, 0, ModItems.scrap.get()));
                    build.addEntry(ore(40, 10, ModItems.aquamarine.get()));
                    build.addEntry(ore(10, 10, ModItems.crystalWater.get()));
                    break;
                case RUBY:
                    build.addEntry(ore(70, 0, ModItems.scrap.get()));
                    build.addEntry(ore(40, 10, ModItems.ruby.get()));
                    build.addEntry(ore(10, 10, ModItems.crystalFire.get()));
                    break;
                case EMERALD:
                    build.addEntry(ore(70, 0, ModItems.scrap.get()));
                    build.addEntry(ore(40, 10, Items.EMERALD));
                    build.addEntry(ore(10, 10, ModItems.crystalWind.get()));
                    break;
                case SAPPHIRE:
                    build.addEntry(ore(90, 0, ModItems.scrap.get()));
                    build.addEntry(ore(50, 10, ModItems.sapphire.get()));
                    build.addEntry(ore(3, 10, ModItems.crystalLove.get()));
                    break;
            }
            build.acceptFunction(ItemLevelLootFunction.getDef());
            return build;
        }

        private static StandaloneLootEntry.Builder<?> ore(int weight, int quality, IItemProvider item) {
            return ItemLootEntry.builder(item).weight(weight).quality(quality);
        }

        private static StandaloneLootEntry.Builder<?> ore(int weight, int quality, IItemProvider item, int minMiningLevel) {
            return ore(weight, quality, item).acceptCondition(MiningLootCondition.get(minMiningLevel));
        }

        @Override
        protected void registerLootTable(Block block, Function<Block, LootTable.Builder> function) {
            this.registerLootTable(block, function.apply(block));
        }

        @Override
        protected void registerLootTable(Block block, LootTable.Builder builder) {
            this.loots.put(block.getLootTable(), builder);
        }

        protected void registerLootTable(ResourceLocation s, LootTable.Builder builder) {
            this.loots.put(s, builder);
        }
    }
}
