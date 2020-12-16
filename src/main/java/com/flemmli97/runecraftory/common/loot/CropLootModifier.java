package com.flemmli97.runecraftory.common.loot;

import com.flemmli97.runecraftory.api.datapack.CropProperties;
import com.flemmli97.runecraftory.common.datapack.DataPackHandler;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class CropLootModifier extends LootModifier {

    private final Item remove;
    public CropLootModifier(ILootCondition[] conditionsIn, Item ignore) {
        super(conditionsIn);
        this.remove = ignore;
    }

    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        List<ItemStack> mod = Lists.newArrayList();
        generatedLoot.removeIf(stack->stack.getItem() == this.remove);
        generatedLoot.removeIf(stack->this.insertMerge(mod, stack));
        generatedLoot.addAll(mod);
        return generatedLoot;
    }

    private boolean insertMerge(List<ItemStack> list, ItemStack stack){
        CropProperties prop = DataPackHandler.getCropStat(stack.getItem());
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

    public static class Serializer extends GlobalLootModifierSerializer<CropLootModifier>{

        @Override
        public CropLootModifier read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition) {
            return new CropLootModifier(ailootcondition, ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getString(object, "exclude"))));
        }

        @Override
        public JsonObject write(CropLootModifier instance) {
            JsonObject obj = this.makeConditions(instance.conditions);
            obj.addProperty("exclude", instance.remove.getRegistryName().toString());
            return obj;
        }
    }
}
