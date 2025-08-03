package io.github.flemmli97.runecraftory.neoforge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.provider.FarmlandHoeTileActionProvider;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class FarmlandHoeTileActionGen extends FarmlandHoeTileActionProvider {

    public FarmlandHoeTileActionGen(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
        super(packOutput, RuneCraftory.MODID, provider);
    }

    @Override
    protected void add(HolderLookup.Provider provider) {
        this.add(RuneCraftoryItems.WITHERED_GRASS.get(), 50);
        this.add(RuneCraftoryItems.CORN.get(), 180);
        this.add(RuneCraftoryItems.CORN_GIANT.get(), 180);
        this.add(RuneCraftoryItems.FOUR_LEAF_CLOVER.get(), 255);
        this.add(RuneCraftoryItems.FOUR_LEAF_CLOVER_GIANT.get(), 255);
    }
}
