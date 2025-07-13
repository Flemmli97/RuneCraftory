package io.github.flemmli97.runecraftory.neoforge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.provider.FileVerifier;
import io.github.flemmli97.runecraftory.neoforge.data.book.BookContentGen;
import io.github.flemmli97.runecraftory.neoforge.data.book.BookGen;
import io.github.flemmli97.runecraftory.neoforge.data.tags.AttributeTagGen;
import io.github.flemmli97.runecraftory.neoforge.data.tags.BiomeTagGen;
import io.github.flemmli97.runecraftory.neoforge.data.tags.BlockTagGen;
import io.github.flemmli97.runecraftory.neoforge.data.tags.DamageTypeTagGen;
import io.github.flemmli97.runecraftory.neoforge.data.tags.EntityTagGen;
import io.github.flemmli97.runecraftory.neoforge.data.tags.FluidTagGen;
import io.github.flemmli97.runecraftory.neoforge.data.tags.ItemTagGen;
import io.github.flemmli97.runecraftory.neoforge.data.worldgen.FeatureWorldGen;
import io.github.flemmli97.runecraftory.neoforge.data.worldgen.StructureWorldGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = RuneCraftory.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataEvent {

    @SubscribeEvent
    public static void data(GatherDataEvent event) {
        DataGenerator data = event.getGenerator();
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        NPCDataGen npcDataGen;
        QuestGen questGen;
        FileVerifier verifier = new FileVerifier() {
            @Override
            public boolean exists(ResourceLocation loc, PackType packType, String prefix) {
                return fileHelper.exists(loc, packType, ".json", prefix);
            }

            @Override
            public void track(ResourceLocation loc, PackType packType, String prefix) {
                fileHelper.trackGenerated(loc, packType, ".json", prefix);
            }
        };
        data.addProvider(true, questGen = new QuestGen(packOutput, provider));
        data.addProvider(true, npcDataGen = new NPCDataGen(packOutput, verifier, provider, questGen));

        IgnoreFileHelper ignore = new IgnoreFileHelper(fileHelper);

        data.addProvider(true, new AttributeTagGen(packOutput, provider));
        data.addProvider(true, new BiomeTagGen(packOutput, provider, fileHelper));
        BlockTagGen blocks = new BlockTagGen(packOutput, provider, fileHelper);
        data.addProvider(true, blocks);
        data.addProvider(true, new DamageTypeGen(packOutput, provider, fileHelper));
        data.addProvider(true, new DamageTypeTagGen(packOutput, provider, fileHelper));
        data.addProvider(true, new EntityTagGen(packOutput, provider, fileHelper));
        data.addProvider(true, new FluidTagGen(packOutput, provider, fileHelper));
        data.addProvider(true, new ItemTagGen(packOutput, provider, blocks.contentsGetter(), fileHelper));

        data.addProvider(true, new AdvancementGen(packOutput, provider, fileHelper));
        data.addProvider(true, new AnimationDefinitionGen(packOutput, provider));
        data.addProvider(true, new BlockStatesGen(packOutput, ignore));
        data.addProvider(true, new CropGen(packOutput, provider));
        data.addProvider(true, new FoodGen(packOutput, provider));
        data.addProvider(true, new GateSpawnGen(packOutput, provider));

        data.addProvider(true, new ItemModels(packOutput, ignore));

        data.addProvider(true, new ItemStatGen(packOutput, provider));
        data.addProvider(true, new LangGen(packOutput, npcDataGen, questGen));
        data.addProvider(true, new LootModifierGen(packOutput, provider));
        data.addProvider(true, new Loottables(packOutput, provider, questGen));
        data.addProvider(true, new MobPropertiesgen(packOutput, provider));
        data.addProvider(true, new NPCDialogLangGen(packOutput, npcDataGen));
        data.addProvider(true, new NPCNameGen(packOutput));
        data.addProvider(true, new ParticleGen(packOutput, fileHelper));
        data.addProvider(true, new RecipesGen(packOutput, provider));
        data.addProvider(true, new ShopItemGen(packOutput, provider));
        data.addProvider(true, new SkillPropertiesgen(packOutput));
        data.addProvider(true, new SoundGen(packOutput, fileHelper));
        data.addProvider(true, new SpellPropertiesgen(packOutput));
        data.addProvider(true, new StructureBossGen(packOutput, verifier, provider));
        data.addProvider(true, new StructureWorldGen(packOutput, provider, verifier));
        data.addProvider(true, new FeatureWorldGen(packOutput, provider));
        data.addProvider(true, new BookGen(provider, packOutput));
        data.addProvider(true, new BookContentGen(provider, packOutput));
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
        public Resource getResource(ResourceLocation loc, PackType type, String pathSuffix, String pathPrefix) throws FileNotFoundException {
            return this.wrapper.getResource(loc, type, pathSuffix, pathPrefix);
        }

        @Override
        public boolean isEnabled() {
            return this.wrapper.isEnabled();
        }
    }
}
