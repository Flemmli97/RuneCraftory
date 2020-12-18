package com.flemmli97.runecraftory.common.loot;

import com.flemmli97.runecraftory.common.registry.ModLootModifier;
import com.flemmli97.runecraftory.common.utils.ItemNBT;
import com.flemmli97.tenshilib.common.utils.JsonUtils;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.functions.ILootFunction;

import java.util.Set;

public class ItemLevelLootFunction extends LootFunction {

    private final Set<WeightedLevel> levels;

    private ItemLevelLootFunction(ILootCondition[] conditions, Set<WeightedLevel> levels){
        super(conditions);
        this.levels = levels;
    }

    @Override
    public LootFunctionType getType() {
        return ModLootModifier.ITEM_LEVEL;
    }

    @Override
    protected ItemStack doApply(ItemStack stack, LootContext ctx) {
        int level;
        BlockState state = ctx.get(LootParameters.BLOCK_STATE);
        if(state != null && state.getBlock() instanceof IBlockModifyLevel) {
            level = ((IBlockModifyLevel) state.getBlock()).getLevel(state, ctx.get(LootParameters.BLOCK_ENTITY), this, ctx);
        }
        else
            level = this.getLevel(ctx);

        return ItemNBT.getLeveledItem(stack, Math.abs(level));
    }

    public int getLevel(LootContext ctx){
        return getRandomItem(this.levels, ctx.getLuck());
    }

    public static LootFunction.Builder<ItemLevelLootFunction.Builder> getDef() {
        return new Builder().add(1, 20, 0)
                .add(2, 19, 1).add(3, 16, 2)
                .add(4, 14, 3).add(5, 12, 4)
                .add(6, 10, 5).add(7, 8, 6)
                .add(8, 6, 7).add(9, 4, 8)
                .add(10, 2, 9);
    }

    public static class Builder extends LootFunction.Builder<ItemLevelLootFunction.Builder> {

        private final Set<WeightedLevel> levels = Sets.newHashSet();

        @Override
        protected ItemLevelLootFunction.Builder doCast() {
            return this;
        }

        public ItemLevelLootFunction.Builder add(int level, int weight, int bonus) {
            this.levels.add(new WeightedLevel(weight, bonus, level));
            return this;
        }

        @Override
        public ILootFunction build() {
            return new ItemLevelLootFunction(this.getConditions(), this.levels);
        }
    }

    public static class Serializer  extends LootFunction.Serializer<ItemLevelLootFunction>{

        @Override
        public ItemLevelLootFunction deserialize(JsonObject obj, JsonDeserializationContext ctx, ILootCondition[] conditions) {
            return new ItemLevelLootFunction(conditions, ItemLevelLootFunction.deserialize(obj.get("level_weight")));
        }

        @Override
        public void toJson(JsonObject obj, ItemLevelLootFunction func, JsonSerializationContext context) {
            super.toJson(obj, func, context);
            obj.add("level_weight", ItemLevelLootFunction.serialize(func.levels));
        }
    }

    public static class WeightedLevel {

        private final int weight, bonus, level;

        public WeightedLevel(int weight, int bonus, int level) {
            this.weight = weight;
            this.bonus = bonus;
            this.level = level;
        }

        public int getWeight(float modifier){
            return this.weight + (int)(this.bonus * modifier);
        }

        @Override
        public int hashCode() {
            return this.weight;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == this)
                return true;
            if(obj instanceof WeightedLevel)
                return ((WeightedLevel) obj).weight == this.weight;
            return false;
        }
    }

    public static int totalWeight(Set<WeightedLevel> Set, float modifier) {
        return Set.stream().mapToInt(w->w.getWeight(modifier)).sum();
    }

    public static int getRandomItem(Set<WeightedLevel> Set, float modifier) {
        int total = totalWeight(Set, modifier);
        if(total <=0)
            throw new IllegalArgumentException();
        for(WeightedLevel w : Set){
            total-=w.getWeight(modifier);
            if(total < 0)
                return w.level;
        }
        return 1;
    }

    public static JsonArray serialize(Set<WeightedLevel> Set){
        JsonArray arr = new JsonArray();
        Set.forEach(w->{
            JsonObject obj = new JsonObject();
            obj.addProperty("weight", w.weight);
            obj.addProperty("luck_bonus", w.bonus);
            obj.addProperty("level", w.level);
            arr.add(obj);
        });
        return arr;
    }

    public static Set<WeightedLevel> deserialize(JsonElement element){
        Set<WeightedLevel> Set = Sets.newHashSet();
        if(!element.isJsonArray())
            throw new JsonParseException("Expected a json array for " + element);
        element.getAsJsonArray().forEach(el->{
            if(!el.isJsonObject())
                throw new JsonParseException("Expected a json object for " + el);
            JsonObject obj = (JsonObject) el;
            Set.add(new WeightedLevel(JsonUtils.get(obj, "weight", 1),JsonUtils.get(obj, "luck_bonus", 0), JsonUtils.get(obj, "level", 1)));
        });
        return Set;
    }
}
