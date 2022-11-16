package io.github.flemmli97.runecraftory.forge.loot;

import com.google.gson.JsonObject;
import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class CropLootModifier extends LootModifier {

    /**
     * The seed to remove
     */
    private final Item remove;

    public CropLootModifier(LootItemCondition[] conditionsIn, Item ignore) {
        super(conditionsIn);
        this.remove = ignore;
    }

    @Override
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
                if (ItemHandlerHelper.canItemStacksStack(s, stack)) {
                    return true;
                }
            }
            stack.setCount(prop.maxDrops());
            list.add(stack);
            return true;
        }
        return false;
    }

    public static class Serializer extends GlobalLootModifierSerializer<CropLootModifier> {

        @Override
        public CropLootModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {
            return new CropLootModifier(ailootcondition, ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(object, "exclude"))));
        }

        @Override
        public JsonObject write(CropLootModifier instance) {
            JsonObject obj = this.makeConditions(instance.conditions);
            obj.addProperty("exclude", instance.remove.getRegistryName().toString());
            return obj;
        }
    }
}
