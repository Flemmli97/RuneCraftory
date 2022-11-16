package io.github.flemmli97.runecraftory.fabric.loot;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CropLootModifier {

    public static final Serializer SERIALIZER = new Serializer();

    protected final LootItemCondition[] conditions;
    private final Predicate<LootContext> combinedConditions;

    /**
     * The seed to remove
     */
    private final Item remove;

    public CropLootModifier(LootItemCondition[] conditions, Item ignore) {
        this.conditions = conditions;
        this.combinedConditions = LootItemConditions.andConditions(conditions);
        this.remove = ignore;
    }

    @NotNull
    public final List<ItemStack> apply(List<ItemStack> generatedLoot, LootContext context) {
        return this.combinedConditions.test(context) ? this.doApply(generatedLoot, context) : generatedLoot;
    }

    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        if (!GeneralConfig.disableCropSystem) {
            List<ItemStack> mod = new ArrayList<>();
            generatedLoot.removeIf(stack -> stack.getItem() == this.remove);
            generatedLoot.removeIf(stack -> this.insertMerge(mod, stack));
            generatedLoot.addAll(mod);
        }
        return generatedLoot;
    }

    private boolean insertMerge(List<ItemStack> list, ItemStack stack) {
        if (this.remove == null || this.remove == Items.AIR)
            return false;
        CropProperties prop = DataPackHandler.cropManager().get(this.remove);
        if (prop != null) {
            for (ItemStack s : list) {
                if (ItemStack.isSameItemSameTags(s, stack)) {
                    return true;
                }
            }
            stack.setCount(prop.maxDrops());
            list.add(stack);
            return true;
        }
        return false;
    }

    public static class Serializer {

        private static final Gson GSON_INSTANCE = Deserializers.createFunctionSerializer().create();

        public CropLootModifier read(ResourceLocation id, JsonObject object) {
            LootItemCondition[] lootConditions = GSON_INSTANCE.fromJson(object.get("conditions"), LootItemCondition[].class);
            return new CropLootModifier(lootConditions, PlatformUtils.INSTANCE.items().getFromId(new ResourceLocation(GsonHelper.getAsString(object, "exclude"))));
        }

        public JsonObject write(CropLootModifier instance) {
            JsonObject obj = new JsonObject();
            obj.add("conditions", SerializationContext.INSTANCE.serializeConditions(instance.conditions));
            obj.addProperty("exclude", PlatformUtils.INSTANCE.items().getIDFrom(instance.remove).toString());
            return obj;
        }
    }
}
