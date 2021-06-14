package io.github.flemmli97.runecraftory.data;

import com.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ModelsResourceUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModels extends ItemModelProvider {

    public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, RuneCraftory.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.singleTexture(ModItems.armorBread.get().getRegistryName().getPath(), this.mcLoc("item/generated"), "layer0", this.mcLoc("item/bread"));
        this.singleTexture(ModItems.forgingBread.get().getRegistryName().getPath(), this.mcLoc("item/generated"), "layer0", this.mcLoc("item/bread"));
        this.singleTexture(ModItems.chemistryBread.get().getRegistryName().getPath(), this.mcLoc("item/generated"), "layer0", this.mcLoc("item/bread"));
        this.singleTexture(ModItems.cookingBread.get().getRegistryName().getPath(), this.mcLoc("item/generated"), "layer0", this.mcLoc("item/bread"));

        this.singleTexture(ModItems.unknown.get().getRegistryName().getPath(), this.mcLoc("item/generated"), "layer0", new ResourceLocation(RuneCraftory.MODID, "items/unknown"));

        this.withExistingParent(ModItems.farmland.get().getRegistryName().getPath(), "block/farmland");
        this.withExistingParent(ModItems.spawner.get().getRegistryName().getPath(), "block/spawner");

        for (SpawnEgg egg : SpawnEgg.getEggs())
            this.withExistingParent(egg.getRegistryName().getPath(), ModelsResourceUtil.func_240224_b_("template_spawn_egg"));
    }
}
