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
        this.getOrCreateBuilder(ModTags.minerals)
                .addItemEntry(ModItems.scrapPlus.get())
                .addTags(Tags.Items.INGOTS_IRON, Tags.Items.INGOTS_GOLD)
                .addItemEntry(ModItems.bronze.get())
                .addItemEntry(ModItems.silver.get())
                .addItemEntry(ModItems.platinum.get())
                .addItemEntry(ModItems.orichalcum.get())
                .addItemEntry(ModItems.dragonic.get());

        this.getOrCreateBuilder(ModTags.bronze)
                .addItemEntry(ModItems.bronze.get());
        this.getOrCreateBuilder(ModTags.silver)
                .addItemEntry(ModItems.silver.get());
        this.getOrCreateBuilder(ModTags.platinum)
                .addItemEntry(ModItems.platinum.get());
        this.getOrCreateBuilder(ModTags.orichalcum)
                .addItemEntry(ModItems.orichalcum.get());
        this.getOrCreateBuilder(ModTags.dragonic)
                .addItemEntry(ModItems.dragonic.get());
    }
}
