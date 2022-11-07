package io.github.flemmli97.runecraftory.forge.data;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

public class AdvancementGen implements DataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final DataGenerator generator;

    public AdvancementGen(DataGenerator dataGenerator) {
        this.generator = dataGenerator;
    }

    public List<Consumer<Consumer<Advancement>>> advancements() {
        Consumer<Consumer<Advancement>> c = cons->{
            Advancement root = Advancement.Builder.advancement().display(ModItems.medicinalHerb.get(), new TranslatableComponent("runecraftory.advancements.root.title"), new TranslatableComponent("runecraftory.advancements.root.description"), new ResourceLocation("textures/block/dirt.png"), FrameType.TASK, false, false, false).save(cons, id("root"));
        };
        return List.of(c);
    }

    @Override
    public void run(HashCache cache) {
        Path path = this.generator.getOutputFolder();
        HashSet<ResourceLocation> set = Sets.newHashSet();
        Consumer<Advancement> consumer = advancement -> {
            if (!set.add(advancement.getId())) {
                throw new IllegalStateException("Duplicate advancement " + advancement.getId());
            }
            Path path2 = createPath(path, advancement);
            try {
                DataProvider.save(GSON, cache, advancement.deconstruct().serializeToJson(), path2);
            }
            catch (IOException iOException) {
                RuneCraftory.logger.error("Couldn't save advancement {}", path2, iOException);
            }
        };
        for (Consumer<Consumer<Advancement>> consumer2 : this.advancements()) {
            consumer2.accept(consumer);
        }
    }

    private static Path createPath(Path path, Advancement advancement) {
        return path.resolve("data/" + advancement.getId().getNamespace() + "/advancements/" + advancement.getId().getPath() + ".json");
    }

    private static String id(String id) {
        return RuneCraftory.MODID + ":" + id;
    }

    @Override
    public String getName() {
        return "Advancements";
    }
}
