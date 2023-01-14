package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.forge.data.worldgen.MainWorldGenData;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

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
            data.addProvider(new ItemModels(data, ignore));
            data.addProvider(new LangGen(data));
            data.addProvider(new ParticleGen(data));
        }
        if (event.includeServer()) {
            BlockTagGen blocks = new BlockTagGen(data, event.getExistingFileHelper());
            data.addProvider(blocks);
            data.addProvider(new ItemTagGen(data, blocks, event.getExistingFileHelper()));
            data.addProvider(new ItemStatGen(data));
            data.addProvider(new FoodGen(data));
            data.addProvider(new CropGen(data));
            data.addProvider(new GlobalLootModifierGen(data));
            data.addProvider(new RecipesGen(data));
            data.addProvider(new Loottables(data));
            data.addProvider(new BiomeTagGen(data, event.getExistingFileHelper()));
            data.addProvider(new MainWorldGenData(data));
            data.addProvider(new PatchouliGen(data));
            data.addProvider(new EntityTagGen(data, event.getExistingFileHelper()));
            data.addProvider(new ShopItemGen(data));
            data.addProvider(new AdvancementGen(data));
            data.addProvider(new NPCDataGen(data));
            data.addProvider(new QuestGen(data));
        }
    }

    protected static class IgnoreFileHelper extends ExistingFileHelper {

        private final ExistingFileHelper wrapper;

        public IgnoreFileHelper(ExistingFileHelper wrapper) {
            super(Collections.emptySet(), Collections.emptySet(), false, null, null);
            this.wrapper = wrapper;
        }

        @Override
        public boolean exists(ResourceLocation loc, PackType type, String pathSuffix, String pathPrefix) {
            return true;
        }

        @Override
        public Resource getResource(ResourceLocation loc, PackType type, String pathSuffix, String pathPrefix) throws IOException {
            return this.wrapper.getResource(loc, type, pathSuffix, pathPrefix);
        }

        @Override
        public boolean isEnabled() {
            return this.wrapper.isEnabled();
        }
    }
}
