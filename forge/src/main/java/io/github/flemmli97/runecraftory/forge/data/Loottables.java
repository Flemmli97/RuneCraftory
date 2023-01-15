package io.github.flemmli97.runecraftory.forge.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.api.enums.EnumMineralTier;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.blocks.BlockCashRegister;
import io.github.flemmli97.runecraftory.common.blocks.BlockCrafting;
import io.github.flemmli97.runecraftory.common.blocks.BlockCrop;
import io.github.flemmli97.runecraftory.common.blocks.BlockMonsterBarn;
import io.github.flemmli97.runecraftory.common.blocks.BlockQuestboard;
import io.github.flemmli97.runecraftory.common.entities.GateEntity;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityWooly;
import io.github.flemmli97.runecraftory.common.loot.ChancedLootingGenerator;
import io.github.flemmli97.runecraftory.common.loot.FriendPointCondition;
import io.github.flemmli97.runecraftory.common.loot.GiantLootCondition;
import io.github.flemmli97.runecraftory.common.loot.ItemLevelLootFunction;
import io.github.flemmli97.runecraftory.common.loot.SkillLevelCondition;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
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
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
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

    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> loot = ImmutableList.of(Pair.of(EntityLoot::new, LootContextParamSets.ENTITY), Pair.of(WoolyShearedEntityLoot::new, LootContextParamSets.FISHING), Pair.of(BlockLootData::new, LootContextParamSets.BLOCK));

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
            this.lootTables.put(EntityWooly.WOOLED_LOOT, this.table(
                            new ItemLootData(ModItems.furSmall.get(), 0.5f, 0.05f, 0.4f, 2),
                            new ItemLootData(Items.SHEARS, 0.05f, 0.01f, 0, 0))
                    .withPool(this.create().add(this.addWithCount(Items.WHITE_WOOL, -2, 1, 0)))
                    .withPool(this.create().add(this.addWithCount(Items.MUTTON, -4, 1, 1))));
            this.registerLootTable(ModEntities.wooly.get(), this.table(
                            new ItemLootData(Items.SHEARS, 0.05f, 0.01f, 0, 0))
                    .withPool(this.create().add(this.addWithCount(Items.MUTTON, -4, 1, 1))));
            this.registerLootTable(ModEntities.ant.get(), this.table(
                    new ItemLootData(ModItems.carapaceInsect.get(), 0.4f, 0.05f, 0.4f, 0),
                    new ItemLootData(ModItems.carapacePretty.get(), 0.02f, 0.01f, 0.25f, 2),
                    new ItemLootData(ModItems.jawInsect.get(), 0.3f, 0.05f, 0.4f, 0),
                    new ItemLootData(Items.OAK_LOG, 0.7f, 0.05f, 0.6f, 0, true)));
            this.registerLootTable(ModEntities.orcArcher.get(), this.table(
                            new ItemLootData(ModItems.recoveryPotion.get(), 0.03f, 0.03f, 0, 0),
                            new ItemLootData(Items.GUNPOWDER, 0.6f, 0.05f, 0.7f, 0),
                            new ItemLootData(ModItems.arrowHead.get(), 0.4f, 0.05f, 0.4f, 0))
                    .withPool(this.create().add(this.addWithCount(Items.ARROW, -1, 1, 1))));
            this.registerLootTable(ModEntities.orc.get(), this.table(
                    new ItemLootData(ModItems.cheapBracelet.get(), 0.05f, 0.03f, 0, 0),
                    new ItemLootData(ModItems.clothCheap.get(), 0.4f, 0.05f, 0.4f, 0),
                    new ItemLootData(ModItems.glue.get(), 0.3f, 0.05f, 0.4f, 0)));
            this.registerLootTable(ModEntities.beetle.get(), this.table(
                    new ItemLootData(ModItems.carapaceInsect.get(), 0.4f, 0.05f, 0.4f, 0),
                    new ItemLootData(ModItems.carapacePretty.get(), 0.02f, 0.01f, 0.25f, 0),
                    new ItemLootData(ModItems.hornInsect.get(), 0.3f, 0.05f, 0.4f, 0)));
            this.registerLootTable(ModEntities.big_muck.get(), this.table(
                    new ItemLootData(ModItems.spore.get(), 0.4f, 0.05f, 0.4f, 0),
                    new ItemLootData(ModItems.powderPoison.get(), 0.03f, 0.01f, 0.2f, 0),
                    new ItemLootData(ModItems.mushroom.get(), 0.4f, 0.05f, 0.5f, 0)));
            this.registerLootTable(ModEntities.buffamoo.get(), this.table(
                            new ItemLootData(ModItems.milkS.get(), 0.4f, 0.05f, 0.4f, 0))
                    .withPool(this.create().add(this.addWithCount(Items.LEATHER, -4, 1, 1)))
                    .withPool(this.create().add(this.addWithCount(Items.BEEF, -5, 2, 1))));
            this.registerLootTable(ModEntities.chipsqueek.get(), this.table(
                    new ItemLootData(ModItems.fur.get(), 0.4f, 0.05f, 0.4f, 0),
                    new ItemLootData(ModItems.furQuality.get(), 0.05f, 0.015f, 0.25f, 0)));
            this.registerLootTable(ModEntities.cluckadoodle.get(), this.table(
                            new ItemLootData(ModItems.eggS.get(), 0.4f, 0.05f, 0.4f, 0))
                    .withPool(this.create().add(this.addWithCount(Items.CHICKEN, -4, 1, 0.5f)))
                    .withPool(this.create().add(this.addWithCount(Items.FEATHER, -3, 2, 1))));
            this.registerLootTable(ModEntities.pomme_pomme.get(), this.table(
                    new ItemLootData(Items.APPLE, 0.7f, 0.05f, 0.7f, 2, true),
                    new ItemLootData(ModItems.bakedApple.get(), 0.03f, 0.01f, 0, 0))); // + apple tree seed
            this.registerLootTable(ModEntities.tortas.get(), this.table(
                    new ItemLootData(ModItems.turtleShell.get(), 0.4f, 0.05f, 0.4f, 0),
                    new ItemLootData(Items.IRON_INGOT, 0.6f, 0.05f, 0.5f, 0),
                    new ItemLootData(Items.COPPER_INGOT, 0.25f, 0.05f, 0.3f, 0)));
            this.registerLootTable(ModEntities.sky_fish.get(), this.table(
                    new ItemLootData(ModItems.fishFossil.get(), 0.4f, 0.05f, 0.4f, 0),
                    new ItemLootData(ModItems.can.get(), 0.05f, 0.03f, 0, 0),
                    new ItemLootData(ModItems.rareCan.get(), 0.01f, 0.005f, 0, 0)));
            this.registerLootTable(ModEntities.weagle.get(), this.table(
                    new ItemLootData(Items.FEATHER, 0.5f, 0.05f, 0.55f, 0))); // + shiny seed
            this.registerLootTable(ModEntities.goblin.get(), this.table(
                    new ItemLootData(ModItems.bladeShard.get(), 0.4f, 0.05f, 0.4f, 0),
                    new ItemLootData(ModItems.glue.get(), 0.1f, 0.05f, 0.3f, 0),
                    new ItemLootData(ModItems.oldBandage.get(), 0.3f, 0.05f, 0.3f, 0),
                    new ItemLootData(ModItems.onigiri.get(), 0.15f, 0.05f, 0, 0)));
            this.registerLootTable(ModEntities.goblinArcher.get(), this.table(
                            new ItemLootData(ModItems.arrowHead.get(), 0.2f, 0.05f, 0.4f, 0),
                            new ItemLootData(ModItems.oldBandage.get(), 0.4f, 0.05f, 0.5f, 0),
                            new ItemLootData(Items.GUNPOWDER, 0.7f, 0.05f, 0.7f, 0, true),
                            new ItemLootData(ModItems.recoveryPotion.get(), 0.15f, 0.05f, 0, 0))
                    .withPool(this.create().add(this.addWithCount(Items.ARROW, -2, 2, 1))));
            this.registerLootTable(ModEntities.duck.get(), this.table(
                    new ItemLootData(ModItems.downYellow.get(), 0.4f, 0.05f, 0.4f, 0),
                    new ItemLootData(Items.FEATHER, 0.7f, 0.05f, 0.6f, 0),
                    new ItemLootData(ModItems.featherYellow.get(), 0.03f, 0.025f, 0.2f, 0)));
            this.registerLootTable(ModEntities.fairy.get(), this.table(
                    new ItemLootData(ModItems.fairyDust.get(), 0.4f, 0.05f, 0.4f, 0))); // + love potion, prelude to love
            this.registerLootTable(ModEntities.ghost.get(), this.table(
                    new ItemLootData(ModItems.ghostHood.get(), 0.4f, 0.05f, 0.4f, 0),
                    new ItemLootData(ModItems.skull.get(), 0.05f, 0.01f, 0, 0),
                    new ItemLootData(Items.SKELETON_SKULL, 0.02f, 0.01f, 0, 0)));
            this.registerLootTable(ModEntities.spirit.get(), this.table(
                    new ItemLootData(ModItems.crystalDark.get(), 0.35f, 0.05f, 0.4f, 0),
                    new ItemLootData(ModItems.crystalMagic.get(), 0.3f, 0.05f, 0.3f, 0)));
            this.registerLootTable(ModEntities.ghostRay.get(), this.table(
                    new ItemLootData(ModItems.ghostHood.get(), 0.5f, 0.05f, 0.5f, 0),
                    new ItemLootData(ModItems.skull.get(), 0.075f, 0.01f, 0, 0),
                    new ItemLootData(ModItems.stickThick.get(), 0.1f, 0.03f, 0.25f, 0),
                    new ItemLootData(Items.SKELETON_SKULL, 0.02f, 0.01f, 0, 0)));
            this.registerLootTable(ModEntities.spider.get(), this.table(
                    new ItemLootData(ModItems.jawInsect.get(), 0.45f, 0.05f, 0.5f, 0),
                    new ItemLootData(ModItems.threadPretty.get(), 0.03f, 0.01f, 0.15f, 0),
                    new ItemLootData(Items.STRING, 0.7f, 0.05f, 0.6f, 0)));
            this.registerLootTable(ModEntities.shadowPanther.get(), this.table(
                    new ItemLootData(ModItems.clawPanther.get(), 0.4f, 0.05f, 0.5f, 0),
                    new ItemLootData(ModItems.fur.get(), 0.1f, 0.05f, 0.45f, 0),
                    new ItemLootData(ModItems.furQuality.get(), 0.3f, 0.05f, 0.4f, 0)));
            this.registerLootTable(ModEntities.monsterBox.get(), this.table(
                    new ItemLootData(ModItems.brokenHilt.get(), 0.45f, 0.05f, 0.35f, 0),
                    new ItemLootData(ModItems.brokenBox.get(), 0.1f, 0.025f, 0.3f, 0),
                    new ItemLootData(ModItems.failedDish.get(), 0.35f, 0.05f, 0, 0),
                    new ItemLootData(ModItems.disastrousDish.get(), 0.05f, 0.01f, 0, 0)));
            this.registerLootTable(ModEntities.gobbleBox.get(), this.table(
                    new ItemLootData(ModItems.brokenHilt.get(), 0.25f, 0.05f, 0.4f, 0),
                    new ItemLootData(ModItems.brokenBox.get(), 0.45f, 0.05f, 0.5f, 0),
                    new ItemLootData(ModItems.failedDish.get(), 0.15f, 0.05f, 0, 0),
                    new ItemLootData(ModItems.disastrousDish.get(), 0.15f, 0.03f, 0, 0)));

            this.registerLootTable(ModEntities.ambrosia.get(), this.table(
                    new ItemLootData(ModItems.ambrosiasThorns.get(), 0.65f, 0.1f, 0.33f, 1),
                    new ItemLootData(ModItems.toyherb.get(), 0.2f, 0.05f, 0.3f, 1),
                    new ItemLootData(ModItems.plantStem.get(), 0.2f, 0.05f, 0.3f, 1)));
            this.registerLootTable(ModEntities.thunderbolt.get(), this.table(
                    new ItemLootData(ModItems.lightningMane.get(), 0.65f, 0.1f, 0.33f, 1),
                    new ItemLootData(ModItems.fur.get(), 0.4f, 0.03f, 0.2f, 0),
                    new ItemLootData(ModItems.furQuality.get(), 0.15f, 0.01f, 0.2f, 0)));
            this.registerLootTable(ModEntities.marionetta.get(), this.table(
                    new ItemLootData(ModItems.cursedDoll.get(), 0.65f, 0.1f, 0.33f, 1),
                    new ItemLootData(ModItems.puppetryStrings.get(), 0.2f, 0.05f, 0.15f, 1),
                    new ItemLootData(ModItems.furSmall.get(), 0.5f, 0.05f, 0.6f, 2, true),
                    new ItemLootData(ModItems.furMedium.get(), 0.25f, 0.025f, 0.3f, 1)));

            this.registerGateLoot();

            this.tamedDropTable(ModEntities.wooly.get(),
                    new TamedItemLootData(ModItems.furSmall.get(), 1, 0),
                    new TamedItemLootData(ModItems.furMedium.get(), 1, 5),
                    new TamedItemLootData(ModItems.furLarge.get(), 1, 8));

            this.tamedDropTable(ModEntities.ant.get(),
                    new TamedItemLootData(ModItems.carapaceInsect.get(), 1, 0));
            this.tamedDropTable(ModEntities.beetle.get(),
                    new TamedItemLootData(ModItems.hornInsect.get(), 1, 0));
            this.tamedDropTable(ModEntities.big_muck.get(),
                    new TamedItemLootData(ModItems.spore.get(), 1, 0));
            this.tamedDropTable(ModEntities.buffamoo.get(),
                    new TamedItemLootData(ModItems.milkS.get(), 1, 0),
                    new TamedItemLootData(ModItems.milkM.get(), 1, 5),
                    new TamedItemLootData(ModItems.milkL.get(), 1, 8));
            this.tamedDropTable(ModEntities.chipsqueek.get(),
                    new TamedItemLootData(ModItems.fur.get(), 1, 0));
            this.tamedDropTable(ModEntities.cluckadoodle.get(),
                    new TamedItemLootData(ModItems.eggS.get(), 1, 0),
                    new TamedItemLootData(ModItems.eggM.get(), 1, 5),
                    new TamedItemLootData(ModItems.eggL.get(), 1, 8));
            this.tamedDropTable(ModEntities.pomme_pomme.get(),
                    new TamedItemLootData(Items.APPLE, 1, 0));
            this.tamedDropTable(ModEntities.weagle.get(),
                    new TamedItemLootData(Items.FEATHER, 1, 0));
            this.tamedDropTable(ModEntities.duck.get(),
                    new TamedItemLootData(ModItems.downYellow.get(), 1, 0));
            this.tamedDropTable(ModEntities.fairy.get(),
                    new TamedItemLootData(ModItems.fairyDust.get(), 1, 0));
            this.tamedDropTable(ModEntities.spider.get(),
                    new TamedItemLootData(Items.STRING, 1, 0));
        }

        private void registerGateLoot() {
            for (EnumElement element : EnumElement.values()) {
                this.lootTables.put(GateEntity.getGateLootLocation(element), this.gateLoot(element));
            }
        }

        private LootTable.Builder gateLoot(EnumElement element) {
            return switch (element) {
                case WATER -> this.table(new ItemLootData(ModItems.crystalWater.get(), 0.25f, 0.1f, 0.35f, 2));
                case EARTH -> this.table(new ItemLootData(ModItems.crystalEarth.get(), 0.25f, 0.1f, 0.35f, 2));
                case WIND -> this.table(new ItemLootData(ModItems.crystalWind.get(), 0.25f, 0.1f, 0.35f, 2));
                case FIRE -> this.table(new ItemLootData(ModItems.crystalFire.get(), 0.25f, 0.1f, 0.35f, 2));
                case LIGHT -> this.table(new ItemLootData(ModItems.crystalLight.get(), 0.1f, 0.05f, 0.3f, 2));
                case DARK -> this.table(new ItemLootData(ModItems.crystalDark.get(), 0.1f, 0.05f, 0.3f, 2));
                case LOVE -> this.table(new ItemLootData(ModItems.crystalLove.get(), 0.05f, 0.05f, 0.3f, 2));
                default -> this.table();
            };
        }

        private LootPool.Builder create() {
            return LootPool.lootPool().setRolls(ConstantValue.exactly(1));
        }

        protected void registerLootTable(EntityType<?> type, LootTable.Builder builder) {
            this.lootTables.put(type.getDefaultLootTable(), builder);
        }

        private LootTable.Builder table(ItemLootData... datas) {
            LootTable.Builder builder = new LootTable.Builder();
            for (ItemLootData data : datas) {
                LootPoolSingletonContainer.Builder<?> b = LootItem.lootTableItem(data.item)
                        .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(data.chance, data.lootingBonus));
                if (data.lootingCountBonus > 0) {
                    if (data.uniform)
                        b.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, data.lootingCountBonus)).setLimit(data.lootingCountMax));
                    else
                        b.apply(SetItemCountFunction.setCount(ChancedLootingGenerator.chance(data.lootingCountMax, data.lootingCountBonus), true));
                }
                builder.withPool(this.create()
                        .add(b));
            }
            return builder;
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

        record ItemLootData(ItemLike item, float chance, float lootingBonus, float lootingCountBonus,
                            int lootingCountMax, boolean uniform) {

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

            this.lootTables.put(EntityWooly.shearedLootTable(EntityWooly.WOOLED_LOOT), LootTable.lootTable().withPool(LootPool.lootPool().add(b)));
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
                    build.add(ore(40, 17, Items.IRON_INGOT));
                    build.add(ore(1, 7, ModItems.crystalSmall.get(), 40));
                }
                case BRONZE -> {
                    build.add(ore(100, 3, ModItems.scrap.get()));
                    build.add(ore(40, 15, Items.IRON_INGOT));
                    build.add(ore(25, 24, Items.COPPER_INGOT));
                    build.add(ore(3, 8, ModItems.crystalSmall.get(), 40));
                }
                case SILVER -> {
                    build.add(ore(100, 3, ModItems.scrap.get()));
                    build.add(ore(40, 12, Items.IRON_INGOT));
                    build.add(ore(20, 31, ModItems.silver.get()));
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
                    build.add(ore(3, 7, Items.DIAMOND));
                    build.add(ore(10, 27, Items.DIAMOND, 20));
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
                    build.add(ore(100, 3, ModItems.scrap.get()));
                    build.add(ore(40, 15, ModItems.amethyst.get()));
                    build.add(ore(10, 17, ModItems.crystalEarth.get()));
                }
                case AQUAMARINE -> {
                    build.add(ore(100, 3, ModItems.scrap.get()));
                    build.add(ore(40, 15, ModItems.aquamarine.get()));
                    build.add(ore(10, 17, ModItems.crystalWater.get()));
                }
                case RUBY -> {
                    build.add(ore(100, 3, ModItems.scrap.get()));
                    build.add(ore(40, 15, ModItems.ruby.get()));
                    build.add(ore(10, 17, ModItems.crystalFire.get()));
                }
                case EMERALD -> {
                    build.add(ore(100, 3, ModItems.scrap.get()));
                    build.add(ore(40, 15, Items.EMERALD));
                    build.add(ore(10, 17, ModItems.crystalWind.get()));
                }
                case SAPPHIRE -> {
                    build.add(ore(100, 3, ModItems.scrap.get()));
                    build.add(ore(50, 15, ModItems.sapphire.get()));
                    build.add(ore(3, 17, ModItems.crystalLove.get()));
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
}
