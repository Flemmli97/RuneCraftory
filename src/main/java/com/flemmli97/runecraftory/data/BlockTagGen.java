package com.flemmli97.runecraftory.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeBlockTagsProvider;

public class BlockTagGen extends ForgeBlockTagsProvider {

    public BlockTagGen(DataGenerator gen, ExistingFileHelper existingFileHelper) {
        super(gen, existingFileHelper);
    }

    @Override
    public void registerTags() {

    }
}
