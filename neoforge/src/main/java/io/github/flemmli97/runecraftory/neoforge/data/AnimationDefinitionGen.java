package io.github.flemmli97.runecraftory.neoforge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.tenshilib.common.data.provider.AnimationDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class AnimationDefinitionGen extends AnimationDataProvider {

    public AnimationDefinitionGen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, RuneCraftory.MODID, provider);
    }

    @Override
    protected void add(HolderLookup.Provider provider) {

    }
}
