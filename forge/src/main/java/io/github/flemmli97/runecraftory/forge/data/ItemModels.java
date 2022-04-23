package io.github.flemmli97.runecraftory.forge.data;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.items.consumables.ItemRecipeBread;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolAxe;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolHammer;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolHoe;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolSickle;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolWateringCan;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemGloveBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemSpearBase;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.forge.item.StaffItem;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TieredItem;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Map;

public class ItemModels extends ItemModelProvider {

    private final Map<RegistryEntrySupplier<Item>, ResourceLocation> dualItemMapping = this.getDualItemMapping();

    public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, RuneCraftory.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (RegistryEntrySupplier<Item> sup : ModItems.ITEMS.getEntries()) {
            if (sup == ModItems.medicinalHerb)
                continue;
            if (sup == ModItems.farmland)
                this.withExistingParent(sup.getID().getPath(), "block/farmland");
            else if (sup == ModItems.spawner)
                this.withExistingParent(sup.getID().getPath(), "block/spawner");
            else if (this.dualItemMapping.containsKey(sup))
                this.singleTexture(sup.getID().getPath(), this.mcLoc("item/handheld"), "layer0", new ResourceLocation(RuneCraftory.MODID, "item/" + sup.getID().getPath()))
                        .override().predicate(this.modLoc("held"), 1).model(this.getExistingFile(this.dualItemMapping.get(sup)));
            else if (sup.get() instanceof ItemGloveBase)
                this.singleTexture(sup.getID().getPath(), this.mcLoc("item/handheld"), "layer0", new ResourceLocation(RuneCraftory.MODID, "item/" + sup.getID().getPath()))
                        .override().predicate(this.modLoc("glove_held"), 2)
                        .model(this.singleTexture(sup.getID().getPath() + "_held_s", this.modLoc("item/fist_s"), "layer0", new ResourceLocation(RuneCraftory.MODID, "item/" + sup.getID().getPath() + "_held"))).end()
                        .override().predicate(this.modLoc("glove_held"), 1)
                        .model(this.singleTexture(sup.getID().getPath() + "_held", this.modLoc("item/fist"), "layer0", new ResourceLocation(RuneCraftory.MODID, "item/" + sup.getID().getPath() + "_held")));
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
            else if (sup.get() instanceof ItemRecipeBread)
                this.singleTexture(sup.getID().getPath(), this.mcLoc("item/generated"), "layer0", this.mcLoc("item/bread"));
            else if (sup.get() instanceof SpawnEgg)
                this.withExistingParent(sup.getID().getPath(), ModelLocationUtils.decorateItemModelLocation("template_spawn_egg"));
            else if (sup.get() instanceof TieredItem || sup.get() instanceof StaffItem || sup.get() instanceof ItemSpearBase)
                this.singleTexture(sup.getID().getPath(), this.mcLoc("item/handheld"), "layer0", new ResourceLocation(RuneCraftory.MODID, "item/" + sup.getID().getPath()));
            else if (sup.get() instanceof BlockItem blockItem && (sup.getID().getPath().startsWith("ore_") || sup == ModItems.shippingBin))
                this.withExistingParent(sup.getID().getPath(), new ResourceLocation(blockItem.getRegistryName().getNamespace(), "block/" + blockItem.getBlock().getRegistryName().getPath()));
            else
                this.singleTexture(sup.getID().getPath(), this.mcLoc("item/generated"), "layer0", new ResourceLocation(RuneCraftory.MODID, "item/" + sup.getID().getPath()));
        }
    }

    private Map<RegistryEntrySupplier<Item>, ResourceLocation> getDualItemMapping() {
        ImmutableMap.Builder<RegistryEntrySupplier<Item>, ResourceLocation> map = new ImmutableMap.Builder<>();
        map.put(ModItems.shortDagger, ModItems.broadSword.getID());
        return map.build();
    }
}
