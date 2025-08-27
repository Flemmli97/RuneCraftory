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
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TieredItem;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.loaders.SeparateTransformsModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemModels extends ItemModelProvider {

    private final Map<RegistryEntrySupplier<Item, ?>, ResourceLocation> dualItemMapping = this.getDualItemMapping();
    private final Set<RegistryEntrySupplier<Item, ?>> customGloveModel = this.customGloveModels();
    private final Set<RegistryEntrySupplier<Item, ?>> sameLeftGloveModel = this.sameLeftGloveModels();

    public ItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, RuneCraftory.MODID, existingFileHelper);
    }

    private Map<RegistryEntrySupplier<Item, ?>, ResourceLocation> getDualItemMapping() {
        ImmutableMap.Builder<RegistryEntrySupplier<Item, ?>, ResourceLocation> map = new ImmutableMap.Builder<>();
        map.put(RuneCraftoryItems.SHORT_DAGGER, RuneCraftoryItems.BROAD_SWORD.getID());
        map.put(RuneCraftoryItems.STEEL_EDGE, RuneCraftoryItems.STEEL_SWORD.getID());
        map.put(RuneCraftoryItems.IRON_EDGE, RuneCraftoryItems.STEEL_SWORD_PLUS.getID());
        map.put(RuneCraftoryItems.FROST_EDGE, RuneCraftoryItems.AQUA_SWORD.getID());
        return map.build();
    }

    private Set<RegistryEntrySupplier<Item, ?>> customGloveModels() {
        ImmutableSet.Builder<RegistryEntrySupplier<Item, ?>> builder = new ImmutableSet.Builder<>();
        builder.add(RuneCraftoryItems.BRASS_KNUCKLES);
        builder.add(RuneCraftoryItems.BEAR_CLAWS);
        builder.add(RuneCraftoryItems.DRAGON_CLAWS);
        return builder.build();
    }

    private Set<RegistryEntrySupplier<Item, ?>> sameLeftGloveModels() {
        ImmutableSet.Builder<RegistryEntrySupplier<Item, ?>> builder = new ImmutableSet.Builder<>();
        builder.add(RuneCraftoryItems.BRASS_KNUCKLES);
        builder.add(RuneCraftoryItems.BEAR_CLAWS);
        builder.add(RuneCraftoryItems.DRAGON_CLAWS);
        return builder.build();
    }

    @Override
    protected void registerModels() {
        this.withExistingParent("fist_slim", this.modLoc("fist")).transforms()
                .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).scale(0.25f, 0.3f, 0.3f).translation(0, -1.86f, 1.6f).end()
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).scale(0.25f, 0.3f, 0.3f).translation(0, -1.86f, 1.6f).end();
        this.withExistingParent("fist_slim_left", this.modLoc("fist_left")).transforms()
                .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).scale(0.25f, 0.3f, 0.3f).translation(0, -1.86f, 1.6f).end()
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).scale(0.25f, 0.3f, 0.3f).translation(0, -1.86f, 1.6f).end();
        List<RegistryEntrySupplier<Item, ?>> ribbons = RuneCraftoryItems.ribbons();
        List<RegistryEntrySupplier<Item, ?>> hats = RuneCraftoryItems.hatItems();

        for (RegistryEntrySupplier<Item, ?> sup : RuneCraftoryItems.ITEMS.getEntries()) {
            if (sup == RuneCraftoryItems.MEDICINAL_HERB || sup == RuneCraftoryItems.FORGE || sup == RuneCraftoryItems.ACCESSORY_WORKBENCH
                    || sup == RuneCraftoryItems.CHEMISTRY_SET || sup == RuneCraftoryItems.COOKING_TABLE || sup == RuneCraftoryItems.QUEST_BOARD
                    || sup == RuneCraftoryItems.ORC_MAZE
                    || sup == RuneCraftoryItems.STRAW_HAT || sup == RuneCraftoryItems.FANCY_HAT || hats.contains(sup))
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
                if (sup == RuneCraftoryItems.UMBRELLA) {
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
            } else if (sup == RuneCraftoryItems.SPAWNER)
                this.withExistingParent(sup.getID().getPath(), "block/spawner");
            else if (sup == RuneCraftoryItems.DEBUG)
                this.withExistingParent(sup.getID().getPath(), this.modLoc(this.folder + "/" + RuneCraftoryItems.UNKNOWN.getID().getPath()));
            else if (sup == RuneCraftoryItems.TAME)
                this.withExistingParent(sup.getID().getPath(), this.mcLoc(this.folder + "/template_spawn_egg"));
            else if (sup.get() instanceof ItemDualBladeBase) {
                if (this.dualItemMapping.containsKey(sup)) {
                    this.createDualBladeModel(sup, this.nested().parent(this.getExistingFile(this.mcLoc(this.folder + "/handheld")))
                                    .texture("layer0", RuneCraftory.modRes(this.folder + "/" + sup.getID().getPath())),
                            this.nested().parent(this.getExistingFile(this.dualItemMapping.get(sup))));
                } else {
                    ItemModelBuilder single = this.singleTexture(sup.getID().getPath() + "_single", this.mcLoc(this.folder + "/handheld"),
                            "layer0", this.modLoc(this.folder + "/" + sup.getID().getPath() + "_single"));
                    this.createDualBladeModel(sup, this.nested().parent(this.getExistingFile(this.mcLoc(this.folder + "/handheld")))
                                    .texture("layer0", RuneCraftory.modRes(this.folder + "/" + sup.getID().getPath())),
                            this.nested().parent(single));
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
            else if (sup.get() instanceof ItemLongSwordBase || sup.get() instanceof ItemAxeBase
                    || sup.get() instanceof ItemHammerBase || sup.get() instanceof ItemSpearBase)
                this.withInventoryVariant(sup, "handheld_32x32");
            else if (sup.get() instanceof TieredItem || sup.get() instanceof ItemStaffBase)
                this.singleTexture(sup.getID().getPath(), this.mcLoc(this.folder + "/handheld"), "layer0", RuneCraftory.modRes(this.folder + "/" + sup.getID().getPath()));
            else if (sup.get() instanceof BlockItem && (sup.getID().getPath().startsWith("ore_") || sup == RuneCraftoryItems.SHIPPING_BIN
                    || sup == RuneCraftoryItems.CASH_REGISTER || sup == RuneCraftoryItems.MONSTER_BARN))
                this.withExistingParent(sup.getID().getPath(), ResourceLocation.fromNamespaceAndPath(sup.getID().getNamespace(), "block/" + sup.getID().getPath()));
            else if (sup.get() instanceof ItemProp)
                this.singleTexture(sup.getID().getPath(), this.mcLoc(this.folder + "/generated"), "layer0", RuneCraftory.modRes(this.folder + "/" + RuneCraftoryItems.UNKNOWN.getID().getPath()));
            else if (RuneCraftoryItems.GIANT_CROPS.contains(sup))
                this.singleTexture(sup.getID().getPath(), this.modLoc(this.folder + "/double_sized_item"), "layer0", RuneCraftory.modRes(this.folder + "/" + sup.getID().getPath()));
            else if (sup == RuneCraftoryItems.NPC_BABY)
                this.singleTexture(sup.getID().getPath(), this.mcLoc(this.folder + "/generated"), "layer0", RuneCraftory.modRes(this.folder + "/" + sup.getID().getPath() + "_boy"))
                        .override().predicate(ItemModelProps.BABY_GENDER, 1)
                        .model(this.singleTexture(sup.getID().getPath() + "_girl", this.mcLoc(this.folder + "/generated"), "layer0", RuneCraftory.modRes(this.folder + "/" + sup.getID().getPath() + "_girl"))).end();
            else
                this.singleTexture(sup.getID().getPath(), this.mcLoc(this.folder + "/generated"), "layer0", RuneCraftory.modRes(this.folder + "/" + sup.getID().getPath()));
        }
    }

    private ItemModelBuilder withInventoryVariant(RegistryEntrySupplier<Item, ?> reg, String parent) {
        return this.getBuilder(reg.getID().getPath()).guiLight(BlockModel.GuiLight.FRONT)
                .customLoader(SeparateTransformsModelBuilder::begin)
                .base(this.nested().parent(this.getExistingFile(RuneCraftory.modRes(this.folder + "/" + parent)))
                        .texture("layer0", reg.getID().withPath(s -> this.folder + "/" + s + "_held")))
                .perspective(ItemDisplayContext.GUI, this.nested().parent(this.getExistingFile(this.mcLoc(this.folder + "/handheld")))
                        .texture("layer0", reg.getID().withPath(s -> this.folder + "/" + s)))
                .perspective(ItemDisplayContext.FIXED, this.nested().parent(this.getExistingFile(this.mcLoc(this.folder + "/handheld")))
                        .texture("layer0", reg.getID().withPath(s -> this.folder + "/" + s)))
                .end();
    }

    private void createDualBladeModel(RegistryEntrySupplier<Item, ?> sup, ItemModelBuilder base, ItemModelBuilder inHand) {
        this.getBuilder(sup.getID().getPath())
                .guiLight(BlockModel.GuiLight.FRONT)
                .customLoader(SeparateTransformsModelBuilder::begin)
                .base(base)
                .perspective(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, inHand)
                .perspective(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, inHand)
                .perspective(ItemDisplayContext.THIRD_PERSON_LEFT_HAND, inHand)
                .perspective(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, inHand);
    }

    private void createGloveModels(RegistryEntrySupplier<Item, ?> sup) {
        ItemModelBuilder base = this.nested().parent(this.getExistingFile(this.mcLoc(this.folder + "/handheld")))
                .texture("layer0", RuneCraftory.modRes(this.folder + "/" + sup.getID().getPath()));
        ItemModelBuilder slim = this.getBuilder(sup.getID().getPath() + "_slim")
                .guiLight(BlockModel.GuiLight.FRONT)
                .customLoader(SeparateTransformsModelBuilder::begin)
                .base(base)
                .perspective(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, this.slimSubModel(sup, ItemDisplayContext.FIRST_PERSON_LEFT_HAND))
                .perspective(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, this.slimSubModel(sup, ItemDisplayContext.FIRST_PERSON_RIGHT_HAND))
                .perspective(ItemDisplayContext.THIRD_PERSON_LEFT_HAND, this.slimSubModel(sup, ItemDisplayContext.THIRD_PERSON_LEFT_HAND))
                .perspective(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, this.slimSubModel(sup, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND))
                .end();
        ItemModelBuilder heldLeft = this.gloveSubModel(sup, true);
        ItemModelBuilder heldRight = this.gloveSubModel(sup, false);
        this.getBuilder(sup.getID().getPath())
                .guiLight(BlockModel.GuiLight.FRONT)
                .customLoader(SeparateTransformsModelBuilder::begin)
                .base(base)
                .perspective(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, heldLeft)
                .perspective(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, heldRight)
                .perspective(ItemDisplayContext.THIRD_PERSON_LEFT_HAND, heldLeft)
                .perspective(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, heldRight)
                .end()
                .override().predicate(ItemModelProps.SLIM_PLAYER_ID, 1)
                .model(slim)
                .end();
    }

    private ItemModelBuilder slimSubModel(RegistryEntrySupplier<Item, ?> sup, ItemDisplayContext context) {
        return switch (context) {
            case FIRST_PERSON_LEFT_HAND -> this.gloveSubModel(sup, true)
                    .transforms()
                    .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(45, 0, 0).translation(0.5f, 3, 3).scale(0.25f, 0.3f, 0.3f).end()
                    .end();
            case FIRST_PERSON_RIGHT_HAND -> this.gloveSubModel(sup, false)
                    .transforms()
                    .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(45, 0, 0).translation(0.5f, 3, 3).scale(0.25f, 0.3f, 0.3f).end()
                    .end();
            case THIRD_PERSON_LEFT_HAND -> this.gloveSubModel(sup, true)
                    .transforms()
                    .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).translation(0, -2, 1.6f).scale(0.25f, 0.3f, 0.3f).end()
                    .end();
            case THIRD_PERSON_RIGHT_HAND -> this.gloveSubModel(sup, false)
                    .transforms()
                    .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).translation(0, -2, 1.6f).scale(0.25f, 0.3f, 0.3f).end()
                    .end();
            default -> throw new IllegalStateException("Unsupported context");
        };
    }

    private ItemModelBuilder gloveSubModel(RegistryEntrySupplier<Item, ?> sup, boolean left) {
        if (this.customGloveModel.contains(sup)) {
            if (left && !this.sameLeftGloveModel.contains(sup))
                return this.nested().parent(this.getExistingFile(this.modLoc(this.folder + "/" + sup.getID().getPath() + "_held_left")))
                        .texture("layer0", RuneCraftory.modRes(this.folder + "/" + sup.getID().getPath() + "_held"));
            return this.nested().parent(this.getExistingFile(this.modLoc(this.folder + "/" + sup.getID().getPath() + "_held")))
                    .texture("layer0", RuneCraftory.modRes(this.folder + "/" + sup.getID().getPath() + "_held"));
        }
        if (left)
            return this.nested().parent(this.getExistingFile(this.modLoc(this.folder + "/fist_left")))
                    .texture("layer0", RuneCraftory.modRes(this.folder + "/" + sup.getID().getPath() + "_held"));
        return this.nested().parent(this.getExistingFile(this.modLoc(this.folder + "/fist")))
                .texture("layer0", RuneCraftory.modRes(this.folder + "/" + sup.getID().getPath() + "_held"));
    }
}
