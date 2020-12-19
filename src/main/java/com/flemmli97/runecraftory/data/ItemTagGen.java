package com.flemmli97.runecraftory.data;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.registry.ModItems;
import com.flemmli97.runecraftory.common.registry.ModTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemTagGen extends ItemTagsProvider {

    public ItemTagGen(DataGenerator generator, BlockTagsProvider provider, ExistingFileHelper existingFileHelper) {
        super(generator, provider, RuneCraftory.MODID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        this.getOrCreateTagBuilder(ModTags.minerals)
                .add(ModItems.scrapPlus.get())
                .addOptionalTag(Tags.Items.INGOTS_IRON.getId())
                .addOptionalTag(Tags.Items.INGOTS_GOLD.getId())
                .add(ModItems.bronze.get())
                .add(ModItems.silver.get())
                .add(ModItems.platinum.get())
                .add(ModItems.orichalcum.get())
                .add(ModItems.dragonic.get());
    }
}
