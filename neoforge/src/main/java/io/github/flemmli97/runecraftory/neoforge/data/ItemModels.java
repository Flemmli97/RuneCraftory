package io.github.flemmli97.runecraftory.neoforge.data;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ItemModelProps;
import io.github.flemmli97.runecraftory.common.items.consumables.ItemRecipeBread;
import io.github.flemmli97.runecraftory.common.items.creative.ItemProp;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolAxe;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolFishingRod;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolHammer;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolHoe;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolSickle;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolWateringCan;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemAxeBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemDualBladeBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemGloveBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemHammerBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemLongSwordBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemSpearBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TieredItem;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemModels extends ItemModelProvider {

    private final Map<RegistryEntrySupplier<Item, ?>, ResourceLocation> dualItemMapping = this.getDualItemMapping();
    private final Set<RegistryEntrySupplier<Item, ?>> existingSameGloveItems = this.generateSameGloveItemMapping();

    public ItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, RuneCraftory.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.withExistingParent("fist_s", this.modLoc("fist")).transforms()
                .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(-2.5f, 0, 0).scale(0.25f, 0.3f, 0.3f).translation(0, -1.86f, 1.6f).end()
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(-2.5f, 0, 0).scale(0.25f, 0.3f, 0.3f).translation(0, -1.86f, 1.6f).end();
        this.withExistingParent("fist_s_left", this.modLoc("fist_left")).transforms()
                .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(-2.5f, 0, 0).scale(0.25f, 0.3f, 0.3f).translation(0, -1.86f, 1.6f).end()
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(-2.5f, 0, 0).scale(0.25f, 0.3f, 0.3f).translation(0, -1.86f, 1.6f).end();
        List<RegistryEntrySupplier<Item, ?>> ribbons = ModItems.ribbons();
        List<RegistryEntrySupplier<Item, ?>> hats = ModItems.hatItems();

        for (RegistryEntrySupplier<Item, ?> sup : ModItems.ITEMS.getEntries()) {
            if (sup == ModItems.MEDICINAL_HERB || sup == ModItems.FORGE || sup == ModItems.ACCESSORY_WORKBENCH
                    || sup == ModItems.CHEMISTRY_SET || sup == ModItems.COOKING_TABLE || sup == ModItems.QUEST_BOARD
                    || sup == ModItems.ORC_MAZE
                    || sup == ModItems.STRAW_HAT || sup == ModItems.FANCY_HAT || hats.contains(sup))
                continue;
            if (ribbons.contains(sup)) {
                this.singleTexture(sup.getID().getPath(), this.mcLoc(this.folder + "/generated"), "layer0", RuneCraftory.modRes(this.folder + "/" + sup.getID().getPath()))
                        .transforms().transform(ItemDisplayContext.HEAD).rotation(0, 180, 0).translation(0, 5, -6.75f).scale(0.35f);
                //Left sided:
                //this.singleTexture(sup.getID().getPath(), this.mcLoc(this.folder + "/generated"), "layer0", RuneCraftory.modRes(this.folder + "/" + sup.getID().getPath()))
                //        .transforms().transform(ItemDisplayContext.HEAD).rotation(0, 180, -35).translation(-4.5f, 5, -6.75f).scale(0.35f);
                //Right Sided:
                //this.singleTexture(sup.getID().getPath(), this.mcLoc(this.folder + "/generated"), "layer0", RuneCraftory.modRes(this.folder + "/" + sup.getID().getPath()))
                //        .transforms().transform(ItemDisplayContext.HEAD).rotation(0, 180, 35).translation(4.5f, 5, -6.75f).scale(0.35f);*/
            } else if (sup.get() instanceof ShieldItem) {
                if (sup == ModItems.UMBRELLA) {
                    this.withExistingParent(sup.getID().getPath() + "_blocking", this.modLoc(sup.getID().getPath())).transforms()
                            .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(-21, 0, 17).translation(0, 0, 0).end()
                            .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(-21, 0, 17).translation(0, 0, 0).end()
                            .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(-44, 0, -20).translation(3, 4.5f, -3).end()
                            .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(-44, 0, -20).translation(3, 4.5f, -3).end();
                } else {
                    this.withExistingParent(sup.getID().getPath() + "_blocking", this.modLoc(sup.getID().getPath())).transforms()
                            .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(15, 0, 10).translation(0.5f, -4, 0).end()
                            .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(15, 0, 10).translation(0.5f, -4, 0).end()
                            .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(50, -45f, 0).translation(4.5f, -1.5f, -3).end()
                            .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(50, -45f, 0).translation(4.5f, -1.5f, -3).end();
                }
            } else if (sup == ModItems.SPAWNER)
                this.withExistingParent(sup.getID().getPath(), "block/spawner");
            else if (sup == ModItems.DEBUG)
                this.withExistingParent(sup.getID().getPath(), this.modLoc(this.folder + "/" + ModItems.UNKNOWN.getID().getPath()));
            else if (sup == ModItems.TAME)
                this.withExistingParent(sup.getID().getPath(), this.mcLoc(this.folder + "/template_spawn_egg"));
            else if (sup.get() instanceof ItemDualBladeBase) {
                if (this.dualItemMapping.containsKey(sup))
                    this.singleTexture(sup.getID().getPath(), this.mcLoc(this.folder + "/handheld"), "layer0", RuneCraftory.modRes(this.folder + "/" + sup.getID().getPath()))
                            .override().predicate(ItemModelProps.HELD_ID, 1).model(this.getExistingFile(this.dualItemMapping.get(sup)));
                else {
                    this.singleTexture(sup.getID().getPath() + "_single", this.mcLoc(this.folder + "/handheld"),
                            "layer0", this.modLoc(this.folder + "/" + sup.getID().getPath() + "_single"));
                    this.singleTexture(sup.getID().getPath(), this.mcLoc(this.folder + "/handheld"), "layer0", RuneCraftory.modRes(this.folder + "/" + sup.getID().getPath()))
                            .override().predicate(ItemModelProps.HELD_ID, 1).model(this.getExistingFile(ResourceLocation.fromNamespaceAndPath(sup.getID().getNamespace(), sup.getID().getPath() + "_single")));
                }
            } else if (sup.get() instanceof ItemGloveBase)
                this.createGloveModels(sup);
            else if (sup.get() instanceof ItemToolHammer)
                this.singleTexture(sup.getID().getPath(), RuneCraftory.modRes(this.folder + "/hammer_tool"), "layer0", this.modLoc(this.folder + "/" + sup.getID().getPath()));
            else if (sup.get() instanceof ItemToolSickle)
                this.singleTexture(sup.getID().getPath(), RuneCraftory.modRes(this.folder + "/sickle"), "layer0", this.modLoc(this.folder + "/" + sup.getID().getPath()));
            else if (sup.get() instanceof ItemToolWateringCan)
                this.singleTexture(sup.getID().getPath(), RuneCraftory.modRes(this.folder + "/watering_can"), "layer0", this.modLoc(this.folder + "/" + sup.getID().getPath()));
            else if (sup.get() instanceof ItemToolHoe)
                this.singleTexture(sup.getID().getPath(), RuneCraftory.modRes(this.folder + "/hoe"), "layer0", this.modLoc(this.folder + "/" + sup.getID().getPath()));
            else if (sup.get() instanceof ItemToolAxe)
                this.singleTexture(sup.getID().getPath(), RuneCraftory.modRes(this.folder + "/axe_tool"), "layer0", this.modLoc(this.folder + "/" + sup.getID().getPath()));
            else if (sup.get() instanceof ItemToolFishingRod)
                this.singleTexture(sup.getID().getPath(), ResourceLocation.withDefaultNamespace(this.folder + "/handheld_rod"), "layer0", this.modLoc(this.folder + "/" + sup.getID().getPath()))
                        .override().predicate(this.modLoc("fishing"), 1)
                        .model(this.singleTexture(sup.getID().getPath() + "_cast", ResourceLocation.withDefaultNamespace(this.folder + "/handheld_rod"), "layer0", this.modLoc(this.folder + "/" + sup.getID().getPath() + "_cast")));
            else if (sup.get() instanceof ItemRecipeBread)
                this.singleTexture(sup.getID().getPath(), this.mcLoc(this.folder + "/generated"), "layer0", this.mcLoc(this.folder + "/bread"));
            else if (sup.get() instanceof SpawnEgg)
                this.withExistingParent(sup.getID().getPath(), ModelLocationUtils.decorateItemModelLocation("template_spawn_egg"));
            else if (sup.get() instanceof ItemLongSwordBase)
                this.createBigWeaponModel(sup, this.modLoc(this.folder + "/handheld_long_sword"));
            else if (sup.get() instanceof ItemAxeBase || sup.get() instanceof ItemHammerBase)
                this.createBigWeaponModel(sup, this.modLoc(this.folder + "/handheld_big"));
            else if (sup.get() instanceof ItemSpearBase)
                this.createBigWeaponModel(sup, this.modLoc(this.folder + "/handheld_big"));
            else if (sup.get() instanceof TieredItem || sup.get() instanceof ItemStaffBase)
                this.singleTexture(sup.getID().getPath(), this.mcLoc(this.folder + "/handheld"), "layer0", RuneCraftory.modRes(this.folder + "/" + sup.getID().getPath()));
            else if (sup.get() instanceof BlockItem && (sup.getID().getPath().startsWith("ore_") || sup == ModItems.SHIPPING_BIN
                    || sup == ModItems.CASH_REGISTER || sup == ModItems.MONSTER_BARN))
                this.withExistingParent(sup.getID().getPath(), ResourceLocation.fromNamespaceAndPath(sup.getID().getNamespace(), "block/" + sup.getID().getPath()));
            else if (sup.get() instanceof ItemProp)
                this.singleTexture(sup.getID().getPath(), this.mcLoc(this.folder + "/generated"), "layer0", RuneCraftory.modRes(this.folder + "/" + ModItems.UNKNOWN.getID().getPath()));
            else if (ModItems.GIANT_CROPS.contains(sup))
                this.singleTexture(sup.getID().getPath(), this.modLoc(this.folder + "/double_sized_item"), "layer0", RuneCraftory.modRes(this.folder + "/" + sup.getID().getPath()));
            else if (sup == ModItems.NPC_BABY)
                this.singleTexture(sup.getID().getPath(), this.mcLoc(this.folder + "/generated"), "layer0", RuneCraftory.modRes(this.folder + "/" + sup.getID().getPath() + "_boy"))
                        .override().predicate(ItemModelProps.BABY_GENDER, 1)
                        .model(this.singleTexture(sup.getID().getPath() + "_girl", this.mcLoc(this.folder + "/generated"), "layer0", RuneCraftory.modRes(this.folder + "/" + sup.getID().getPath() + "_girl"))).end();
            else
                this.singleTexture(sup.getID().getPath(), this.mcLoc(this.folder + "/generated"), "layer0", RuneCraftory.modRes(this.folder + "/" + sup.getID().getPath()));
        }
    }

    private void createBigWeaponModel(RegistryEntrySupplier<Item, ?> sup, ResourceLocation heldModel) {
        this.singleTexture(sup.getID().getPath(), this.mcLoc(this.folder + "/handheld"), "layer0", RuneCraftory.modRes(this.folder + "/" + sup.getID().getPath()))
                .override()
                .predicate(ItemModelProps.HELD_ID, 1)
                .model(this.singleTexture(sup.getID().getPath() + "_held", heldModel, "layer0", RuneCraftory.modRes(this.folder + "/" + sup.getID().getPath() + "_held")))
                .end();
    }

    private Map<RegistryEntrySupplier<Item, ?>, ResourceLocation> getDualItemMapping() {
        ImmutableMap.Builder<RegistryEntrySupplier<Item, ?>, ResourceLocation> map = new ImmutableMap.Builder<>();
        map.put(ModItems.SHORT_DAGGER, ModItems.BROAD_SWORD.getID());
        map.put(ModItems.STEEL_EDGE, ModItems.STEEL_SWORD.getID());
        map.put(ModItems.IRON_EDGE, ModItems.STEEL_SWORD_PLUS.getID());
        map.put(ModItems.FROST_EDGE, ModItems.AQUA_SWORD.getID());
        return map.build();
    }

    private Set<RegistryEntrySupplier<Item, ?>> generateSameGloveItemMapping() {
        ImmutableSet.Builder<RegistryEntrySupplier<Item, ?>> builder = new ImmutableSet.Builder<>();
        builder.add(ModItems.BRASS_KNUCKLES);
        builder.add(ModItems.BEAR_CLAWS);
        builder.add(ModItems.DRAGON_CLAWS);
        return builder.build();
    }

    private void createGloveModels(RegistryEntrySupplier<Item, ?> sup) {
        if (this.existingSameGloveItems.contains(sup)) {
            ResourceLocation modelFile = this.modLoc(this.folder + "/" + sup.getID().getPath() + "_held");
            this.singleTexture(sup.getID().getPath(), this.mcLoc(this.folder + "/handheld"), "layer0", RuneCraftory.modRes(this.folder + "/" + sup.getID().getPath()))
                    .override().predicate(ItemModelProps.GLOVE_HELD_ID, 0.25f)
                    .model(this.getExistingFile(modelFile)).end()
                    .override().predicate(ItemModelProps.GLOVE_HELD_ID, 0.5f)
                    .model(this.withExistingParent(sup.getID().getPath() + "_held_left", modelFile)).end()
                    .override().predicate(ItemModelProps.GLOVE_HELD_ID, 0.75f)
                    .model(this.withExistingParent(sup.getID().getPath() + "_held_s", modelFile).transforms()
                            .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(-2.5f, 0, 0).scale(0.25f, 0.3f, 0.3f).translation(0, -1.86f, 1.6f).end()
                            .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(-2.5f, 0, 0).scale(0.25f, 0.3f, 0.3f).translation(0, -1.86f, 1.6f).end().end()).end()
                    .override().predicate(ItemModelProps.GLOVE_HELD_ID, 1)
                    .model(this.withExistingParent(sup.getID().getPath() + "_held_s_left", modelFile).transforms()
                            .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(-2.5f, 0, 0).scale(0.25f, 0.3f, 0.3f).translation(0, -1.86f, 1.6f).end()
                            .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(-2.5f, 0, 0).scale(0.25f, 0.3f, 0.3f).translation(0, -1.86f, 1.6f).end().end()).end();
        } else
            this.singleTexture(sup.getID().getPath(), this.mcLoc(this.folder + "/handheld"), "layer0", RuneCraftory.modRes(this.folder + "/" + sup.getID().getPath()))
                    .override().predicate(ItemModelProps.GLOVE_HELD_ID, 0.25f)
                    .model(this.singleTexture(sup.getID().getPath() + "_held", this.modLoc(this.folder + "/fist"), "layer0", RuneCraftory.modRes(this.folder + "/" + sup.getID().getPath() + "_held"))).end()
                    .override().predicate(ItemModelProps.GLOVE_HELD_ID, 0.5f)
                    .model(this.singleTexture(sup.getID().getPath() + "_held_left", this.modLoc(this.folder + "/fist_left"), "layer0", RuneCraftory.modRes(this.folder + "/" + sup.getID().getPath() + "_held"))).end()
                    .override().predicate(ItemModelProps.GLOVE_HELD_ID, 0.75f)
                    .model(this.singleTexture(sup.getID().getPath() + "_held_s", this.modLoc(this.folder + "/fist_s"), "layer0", RuneCraftory.modRes(this.folder + "/" + sup.getID().getPath() + "_held"))).end()
                    .override().predicate(ItemModelProps.GLOVE_HELD_ID, 1)
                    .model(this.singleTexture(sup.getID().getPath() + "_held_s_left", this.modLoc(this.folder + "/fist_s_left"), "layer0", RuneCraftory.modRes(this.folder + "/" + sup.getID().getPath() + "_held"))).end();
    }
}
