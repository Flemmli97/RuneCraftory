package io.github.flemmli97.runecraftory.forge.data;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ItemModelProps;
import io.github.flemmli97.runecraftory.common.items.consumables.ItemGiantCrops;
import io.github.flemmli97.runecraftory.common.items.consumables.ItemRecipeBread;
import io.github.flemmli97.runecraftory.common.items.creative.ItemProp;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolAxe;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolFishingRod;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolHammer;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolHoe;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolSickle;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolWateringCan;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemAxeBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemGloveBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemHammerBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemLongSwordBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemSpearBase;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.forge.item.StaffItem;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TieredItem;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class ItemModels extends ItemModelProvider {

    private final Map<RegistryEntrySupplier<Item>, ResourceLocation> dualItemMapping = this.getDualItemMapping();
    private final Map<RegistryEntrySupplier<Item>, Supplier<ItemModelBuilder>> dualItemGenMapping = this.generateDualItemMapping();
    private final Set<RegistryEntrySupplier<Item>> existingSameGloveItems = this.generateSameGloveItemMapping();


    public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, RuneCraftory.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.withExistingParent("fist_s", this.modLoc("fist")).transforms()
                .transform(ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND).rotation(-2.5f, 0, 0).scale(0.25f, 0.3f, 0.3f).translation(0, -1.86f, 1.6f).end()
                .transform(ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND).rotation(-2.5f, 0, 0).scale(0.25f, 0.3f, 0.3f).translation(0, -1.86f, 1.6f).end();
        this.withExistingParent("fist_s_left", this.modLoc("fist_left")).transforms()
                .transform(ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND).rotation(-2.5f, 0, 0).scale(0.25f, 0.3f, 0.3f).translation(0, -1.86f, 1.6f).end()
                .transform(ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND).rotation(-2.5f, 0, 0).scale(0.25f, 0.3f, 0.3f).translation(0, -1.86f, 1.6f).end();
        List<RegistryEntrySupplier<Item>> ribbons = ModItems.ribbons();

        for (RegistryEntrySupplier<Item> sup : ModItems.ITEMS.getEntries()) {
            if (sup == ModItems.MEDICINAL_HERB || sup == ModItems.ITEM_BLOCK_FORGE || sup == ModItems.ITEM_BLOCK_ACCESS
                    || sup == ModItems.ITEM_BLOCK_CHEM || sup == ModItems.ITEM_BLOCK_COOKING || sup == ModItems.QUEST_BOARD
                    || sup == ModItems.ORC_MAZE)
                continue;
            if (ribbons.contains(sup)) {
                this.singleTexture(sup.getID().getPath(), this.mcLoc("item/generated"), "layer0", new ResourceLocation(RuneCraftory.MODID, "item/" + sup.getID().getPath()))
                        .transforms().transform(ItemTransforms.TransformType.HEAD).rotation(0, 180, 0).translation(0, 5, -6.75f).scale(0.35f);
                //Left sided:
                //this.singleTexture(sup.getID().getPath(), this.mcLoc("item/generated"), "layer0", new ResourceLocation(RuneCraftory.MODID, "item/" + sup.getID().getPath()))
                //        .transforms().transform(ItemTransforms.TransformType.HEAD).rotation(0, 180, -35).translation(-4.5f, 5, -6.75f).scale(0.35f);
                //Right Sided:
                //this.singleTexture(sup.getID().getPath(), this.mcLoc("item/generated"), "layer0", new ResourceLocation(RuneCraftory.MODID, "item/" + sup.getID().getPath()))
                //        .transforms().transform(ItemTransforms.TransformType.HEAD).rotation(0, 180, 35).translation(4.5f, 5, -6.75f).scale(0.35f);*/
            } else if (sup.get() instanceof ShieldItem) {
                if (sup == ModItems.UMBRELLA)
                    continue;
                this.withExistingParent(sup.getID().getPath() + "_blocking", this.modLoc(sup.getID().getPath())).transforms()
                        .transform(ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND).rotation(9.25f, 0.37f, 8).translation(-0.5f, -4.25f, 0).end()
                        .transform(ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND).rotation(9.25f, 0.37f, 8).translation(-0.5f, -4.25f, 0).end()
                        .transform(ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND).rotation(55, -47.5f, 0).translation(6, -1.25f, -4).end()
                        .transform(ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND).rotation(55, -47.5f, 0).translation(6, -1.25f, -4).end();
            } else if (sup == ModItems.SPAWNER)
                this.withExistingParent(sup.getID().getPath(), "block/spawner");
            else if (sup == ModItems.DEBUG)
                this.withExistingParent(sup.getID().getPath(), this.modLoc("item/" + ModItems.UNKNOWN.getID().getPath()));
            else if (sup == ModItems.TAME)
                this.withExistingParent(sup.getID().getPath(), this.mcLoc("item/template_spawn_egg"));
            else if (sup == ModItems.SEA_CUTTER)
                this.createBigWeaponModel(sup, new ResourceLocation(RuneCraftory.MODID, "item/handheld_long_sword_reverse"));
            else if (sup == ModItems.CUTLASS)
                this.singleTexture(sup.getID().getPath(), new ResourceLocation(RuneCraftory.MODID, "item/handheld_reverse"), "layer0", this.modLoc("item/" + sup.getID().getPath()));
            else if (this.dualItemGenMapping.containsKey(sup)) {
                this.dualItemGenMapping.get(sup).get();
                this.singleTexture(sup.getID().getPath(), this.mcLoc("item/handheld"), "layer0", new ResourceLocation(RuneCraftory.MODID, "item/" + sup.getID().getPath()))
                        .override().predicate(ItemModelProps.HELD_ID, 1).model(this.getExistingFile(new ResourceLocation(sup.getID().getNamespace(), sup.getID().getPath() + "_single")));
            } else if (this.dualItemMapping.containsKey(sup))
                this.singleTexture(sup.getID().getPath(), this.mcLoc("item/handheld"), "layer0", new ResourceLocation(RuneCraftory.MODID, "item/" + sup.getID().getPath()))
                        .override().predicate(ItemModelProps.HELD_ID, 1).model(this.getExistingFile(this.dualItemMapping.get(sup)));
            else if (sup.get() instanceof ItemGloveBase)
                this.createGloveModels(sup);
            else if (sup.get() instanceof ItemToolHammer)
                this.singleTexture(sup.getID().getPath(), new ResourceLocation(RuneCraftory.MODID, "item/hammer_tool"), "layer0", this.modLoc("item/" + sup.getID().getPath()));
            else if (sup.get() instanceof ItemToolSickle)
                this.singleTexture(sup.getID().getPath(), new ResourceLocation(RuneCraftory.MODID, "item/sickle"), "layer0", this.modLoc("item/" + sup.getID().getPath()));
            else if (sup.get() instanceof ItemToolWateringCan)
                this.singleTexture(sup.getID().getPath(), new ResourceLocation(RuneCraftory.MODID, "item/watering_can"), "layer0", this.modLoc("item/" + sup.getID().getPath()));
            else if (sup.get() instanceof ItemToolHoe)
                this.singleTexture(sup.getID().getPath(), new ResourceLocation(RuneCraftory.MODID, "item/hoe"), "layer0", this.modLoc("item/" + sup.getID().getPath()));
            else if (sup.get() instanceof ItemToolAxe)
                this.singleTexture(sup.getID().getPath(), new ResourceLocation(RuneCraftory.MODID, "item/axe_tool"), "layer0", this.modLoc("item/" + sup.getID().getPath()));
            else if (sup.get() instanceof ItemToolFishingRod)
                this.singleTexture(sup.getID().getPath(), new ResourceLocation("item/handheld_rod"), "layer0", this.modLoc("item/" + sup.getID().getPath()))
                        .override().predicate(this.modLoc("fishing"), 1)
                        .model(this.singleTexture(sup.getID().getPath() + "_cast", new ResourceLocation("item/handheld_rod"), "layer0", this.modLoc("item/" + sup.getID().getPath() + "_cast")));
            else if (sup.get() instanceof ItemRecipeBread)
                this.singleTexture(sup.getID().getPath(), this.mcLoc("item/generated"), "layer0", this.mcLoc("item/bread"));
            else if (sup.get() instanceof SpawnEgg)
                this.withExistingParent(sup.getID().getPath(), ModelLocationUtils.decorateItemModelLocation("template_spawn_egg"));
            else if (sup.get() instanceof ItemLongSwordBase)
                this.createBigWeaponModel(sup, this.modLoc("item/handheld_long_sword"));
            else if (sup.get() instanceof ItemAxeBase || sup.get() instanceof ItemHammerBase)
                this.createBigWeaponModel(sup, this.modLoc("item/handheld_big"));
            else if (sup.get() instanceof ItemSpearBase)
                this.createBigWeaponModel(sup, this.modLoc("item/handheld_big"));
            else if (sup.get() instanceof TieredItem || sup.get() instanceof StaffItem)
                this.singleTexture(sup.getID().getPath(), this.mcLoc("item/handheld"), "layer0", new ResourceLocation(RuneCraftory.MODID, "item/" + sup.getID().getPath()));
            else if (sup.get() instanceof BlockItem blockItem && (sup.getID().getPath().startsWith("ore_") || sup == ModItems.SHIPPING_BIN
                    || sup == ModItems.CASH_REGISTER || sup == ModItems.MONSTER_BARN))
                this.withExistingParent(sup.getID().getPath(), new ResourceLocation(blockItem.getRegistryName().getNamespace(), "block/" + blockItem.getBlock().getRegistryName().getPath()));
            else if (sup.get() instanceof ItemProp)
                this.singleTexture(sup.getID().getPath(), this.mcLoc("item/generated"), "layer0", new ResourceLocation(RuneCraftory.MODID, "item/" + ModItems.UNKNOWN.getID().getPath()));
            else if (sup.get() instanceof ItemGiantCrops)
                this.singleTexture(sup.getID().getPath(), this.modLoc("item/double_sized_item"), "layer0", new ResourceLocation(RuneCraftory.MODID, "item/" + sup.getID().getPath()));
            else if (sup == ModItems.NPC_BABY)
                this.singleTexture(sup.getID().getPath(), this.mcLoc("item/generated"), "layer0", new ResourceLocation(RuneCraftory.MODID, "item/" + sup.getID().getPath() + "_boy"))
                        .override().predicate(ItemModelProps.BABY_GENDER, 1)
                        .model(this.singleTexture(sup.getID().getPath() + "_girl", this.mcLoc("item/generated"), "layer0", new ResourceLocation(RuneCraftory.MODID, "item/" + sup.getID().getPath() + "_girl"))).end();
            else
                this.singleTexture(sup.getID().getPath(), this.mcLoc("item/generated"), "layer0", new ResourceLocation(RuneCraftory.MODID, "item/" + sup.getID().getPath()));
        }
    }

    private void createBigWeaponModel(RegistryEntrySupplier<Item> sup, ResourceLocation heldModel) {
        this.singleTexture(sup.getID().getPath(), this.mcLoc("item/handheld"), "layer0", new ResourceLocation(RuneCraftory.MODID, "item/" + sup.getID().getPath()))
                .override()
                .predicate(ItemModelProps.HELD_ID, 1)
                .model(this.singleTexture(sup.getID().getPath() + "_held", heldModel, "layer0", new ResourceLocation(RuneCraftory.MODID, "item/" + sup.getID().getPath() + "_held")))
                .end();
    }

    private Map<RegistryEntrySupplier<Item>, ResourceLocation> getDualItemMapping() {
        ImmutableMap.Builder<RegistryEntrySupplier<Item>, ResourceLocation> map = new ImmutableMap.Builder<>();
        map.put(ModItems.SHORT_DAGGER, ModItems.BROAD_SWORD.getID());
        map.put(ModItems.STEEL_EDGE, ModItems.STEEL_SWORD_PLUS.getID());
        map.put(ModItems.IRON_EDGE, ModItems.STEEL_SWORD.getID());
        map.put(ModItems.FROST_EDGE, ModItems.AQUA_SWORD.getID());
        return map.build();
    }

    private Map<RegistryEntrySupplier<Item>, Supplier<ItemModelBuilder>> generateDualItemMapping() {
        ImmutableMap.Builder<RegistryEntrySupplier<Item>, Supplier<ItemModelBuilder>> map = new ImmutableMap.Builder<>();
        map.put(ModItems.THIEF_KNIFE, () -> this.singleTexture(ModItems.THIEF_KNIFE.getID().getPath() + "_single", new ResourceLocation(RuneCraftory.MODID, "item/handheld_reverse"),
                "layer0", this.modLoc("item/" + ModItems.THIEF_KNIFE.getID().getPath() + "_single")));
        map.put(ModItems.WIND_EDGE, () -> this.singleTexture(ModItems.WIND_EDGE.getID().getPath() + "_single", new ResourceLocation(RuneCraftory.MODID, "item/handheld_reverse"),
                "layer0", this.modLoc("item/" + ModItems.WIND_EDGE.getID().getPath() + "_single")));
        return map.build();
    }

    private Set<RegistryEntrySupplier<Item>> generateSameGloveItemMapping() {
        ImmutableSet.Builder<RegistryEntrySupplier<Item>> builder = new ImmutableSet.Builder<>();
        builder.add(ModItems.BRASS_KNUCKLES);
        builder.add(ModItems.BEAR_CLAWS);
        return builder.build();
    }

    private void createGloveModels(RegistryEntrySupplier<Item> sup) {
        if (this.existingSameGloveItems.contains(sup)) {
            ResourceLocation modelFile = this.modLoc("item/" + sup.getID().getPath() + "_held");
            this.singleTexture(sup.getID().getPath(), this.mcLoc("item/handheld"), "layer0", new ResourceLocation(RuneCraftory.MODID, "item/" + sup.getID().getPath()))
                    .override().predicate(ItemModelProps.GLOVE_HELD_ID, 0.25f)
                    .model(this.getExistingFile(modelFile)).end()
                    .override().predicate(ItemModelProps.GLOVE_HELD_ID, 0.5f)
                    .model(this.withExistingParent(sup.getID().getPath() + "_held_left", modelFile)).end()
                    .override().predicate(ItemModelProps.GLOVE_HELD_ID, 0.75f)
                    .model(this.withExistingParent(sup.getID().getPath() + "_held_s", modelFile).transforms()
                            .transform(ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND).rotation(-2.5f, 0, 0).scale(0.25f, 0.3f, 0.3f).translation(0, -1.86f, 1.6f).end()
                            .transform(ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND).rotation(-2.5f, 0, 0).scale(0.25f, 0.3f, 0.3f).translation(0, -1.86f, 1.6f).end().end()).end()
                    .override().predicate(ItemModelProps.GLOVE_HELD_ID, 1)
                    .model(this.withExistingParent(sup.getID().getPath() + "_held_s_left", modelFile).transforms()
                            .transform(ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND).rotation(-2.5f, 0, 0).scale(0.25f, 0.3f, 0.3f).translation(0, -1.86f, 1.6f).end()
                            .transform(ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND).rotation(-2.5f, 0, 0).scale(0.25f, 0.3f, 0.3f).translation(0, -1.86f, 1.6f).end().end()).end();
        } else
            this.singleTexture(sup.getID().getPath(), this.mcLoc("item/handheld"), "layer0", new ResourceLocation(RuneCraftory.MODID, "item/" + sup.getID().getPath()))
                    .override().predicate(ItemModelProps.GLOVE_HELD_ID, 0.25f)
                    .model(this.singleTexture(sup.getID().getPath() + "_held", this.modLoc("item/fist"), "layer0", new ResourceLocation(RuneCraftory.MODID, "item/" + sup.getID().getPath() + "_held"))).end()
                    .override().predicate(ItemModelProps.GLOVE_HELD_ID, 0.5f)
                    .model(this.singleTexture(sup.getID().getPath() + "_held_left", this.modLoc("item/fist_left"), "layer0", new ResourceLocation(RuneCraftory.MODID, "item/" + sup.getID().getPath() + "_held"))).end()
                    .override().predicate(ItemModelProps.GLOVE_HELD_ID, 0.75f)
                    .model(this.singleTexture(sup.getID().getPath() + "_held_s", this.modLoc("item/fist_s"), "layer0", new ResourceLocation(RuneCraftory.MODID, "item/" + sup.getID().getPath() + "_held"))).end()
                    .override().predicate(ItemModelProps.GLOVE_HELD_ID, 1)
                    .model(this.singleTexture(sup.getID().getPath() + "_held_s_left", this.modLoc("item/fist_s_left"), "layer0", new ResourceLocation(RuneCraftory.MODID, "item/" + sup.getID().getPath() + "_held"))).end();
    }
}
