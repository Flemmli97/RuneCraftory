package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemTagGen extends ItemTagsProvider {

    public ItemTagGen(DataGenerator generator, BlockTagsProvider provider, ExistingFileHelper existingFileHelper) {
        super(generator, provider, RuneCraftory.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(ModTags.iron)
                .add(Items.IRON_INGOT)
                .addOptional(Tags.Items.INGOTS_IRON.location());
        this.tag(ModTags.gold)
                .add(Items.GOLD_INGOT)
                .addOptional(Tags.Items.INGOTS_GOLD.location());

        this.tag(ModTags.minerals)
                .add(ModItems.scrapPlus.get())
                .addTag(ModTags.iron)
                .addTag(ModTags.gold)
                .addTag(ModTags.bronze)
                .addTag(ModTags.silver)
                .addTag(ModTags.platinum)
                .addTag(ModTags.orichalcum)
                .addTag(ModTags.dragonic);

        this.tag(ModTags.bronze)
                .add(ModItems.bronze.get())
                .addOptionalTag(ModTags.bronzeF.location());
        this.tag(ModTags.silver)
                .add(ModItems.silver.get())
                .addOptionalTag(ModTags.silverF.location());
        this.tag(ModTags.platinum)
                .add(ModItems.platinum.get())
                .addOptionalTag(ModTags.platinumF.location());
        this.tag(ModTags.orichalcum)
                .add(ModItems.orichalcum.get());
        this.tag(ModTags.dragonic)
                .add(ModItems.dragonic.get());
    }
}
