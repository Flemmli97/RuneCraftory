package io.github.flemmli97.runecraftory.fabric.loot;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.github.flemmli97.runecraftory.RuneCraftory;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.List;
import java.util.Map;

public class CropLootModifiers extends SimpleJsonResourceReloadListener implements IdentifiableResourceReloadListener {

    private static final Gson GSON = Deserializers.createFunctionSerializer().create();
    public static final CropLootModifiers INSTANCE = new CropLootModifiers();
    private final ResourceLocation id = new ResourceLocation(RuneCraftory.MODID, "crop_modifiers");
    private Map<ResourceLocation, CropLootModifier> modifiers = ImmutableMap.of();

    public CropLootModifiers() {
        super(GSON, "loot_modifiers");
    }

    public static void modify(LootTable table, List<ItemStack> list, LootContext ctx, ResourceLocation res) {
        if (ctx.getLevel() != null) {
            for (Map.Entry<ResourceLocation, CropLootModifier> s : INSTANCE.modifiers.entrySet()) {
                list = s.getValue().apply(list, ctx);
            }
        }
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager resourceManager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, CropLootModifier> builder = ImmutableMap.builder();
        data.forEach((fres, el) -> {
            try {
                JsonObject obj = el.getAsJsonObject();
                String type = GsonHelper.getAsString(obj, "type", "none");
                if (type.equals("runecraftory:crop_modifier")) { //Only care about crop loot modifier. Not implementing the whole forge system
                    builder.put(fres, CropLootModifier.SERIALIZER.read(fres, obj));
                }
            } catch (JsonSyntaxException | IllegalStateException ex) {
                RuneCraftory.logger.error("Couldnt parse crop modifier from json {}", fres);
                ex.fillInStackTrace();
            }
        });
        this.modifiers = builder.build();
    }

    @Override
    public ResourceLocation getFabricId() {
        return this.id;
    }
}
