package com.flemmli97.runecraftory.data;

import com.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.IResource;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import java.io.IOException;
import java.util.Collections;

@Mod.EventBusSubscriber(modid = RuneCraftory.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataEvent {

    @SubscribeEvent
    public static void data(GatherDataEvent event) {
        DataGenerator data = event.getGenerator();
        IgnoreFileHelper ignore = new IgnoreFileHelper(event.getExistingFileHelper());
        if (event.includeClient()) {
            data.addProvider(new BlockStatesGen(data, ignore));
            data.addProvider(new ItemModels(data, event.getExistingFileHelper()));
            data.addProvider(new LangGen(data, event.getExistingFileHelper()));
            data.addProvider(new Loottables(data));
        }
        if (event.includeServer()) {
            BlockTagGen blocks = new BlockTagGen(data, event.getExistingFileHelper());
            data.addProvider(blocks);
            data.addProvider(new ItemTagGen(data, blocks, event.getExistingFileHelper()));
            data.addProvider(new ItemStatGen(data));
            data.addProvider(new FoodGen(data));
            data.addProvider(new CropGen(data));
            data.addProvider(new GlobalLootModifierGen(data));
        }
    }

    protected static class IgnoreFileHelper extends ExistingFileHelper {

        private final ExistingFileHelper wrapper;

        public IgnoreFileHelper(ExistingFileHelper wrapper) {
            super(Collections.EMPTY_SET, false);
            this.wrapper = wrapper;
        }

        @Override
        public boolean exists(ResourceLocation loc, ResourcePackType type, String pathSuffix, String pathPrefix) {
            return true;
        }

        @Override
        public IResource getResource(ResourceLocation loc, ResourcePackType type, String pathSuffix, String pathPrefix) throws IOException {
            return this.wrapper.getResource(loc, type, pathSuffix, pathPrefix);
        }

        @Override
        public boolean isEnabled() {
            return this.wrapper.isEnabled();
        }
    }
}
