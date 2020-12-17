package com.flemmli97.runecraftory.common.loot;

import com.flemmli97.runecraftory.common.registry.ModLootModifier;
import com.flemmli97.runecraftory.common.utils.ItemNBT;
import com.flemmli97.tenshilib.common.utils.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.BinomialRange;
import net.minecraft.loot.IRandomRange;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.RandomRanges;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.functions.ILootFunction;

public class ItemLevelLootFunction extends LootFunction {

    private final IRandomRange range;
    private final int shift;

    private ItemLevelLootFunction(ILootCondition[] conditions, IRandomRange range, int shift){
        super(conditions);
        this.range = range;
        this.shift = shift;
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
        return ItemNBT.getLeveledItem(stack, level);
    }

    public int getLevel(LootContext ctx){
        return range.generateInt(ctx.getRandom())+shift;
    }

    public static LootFunction.Builder<ItemLevelLootFunction.Builder> getDef() {
        return new Builder(new BinomialRange(10, 0.7f));
    }

    public static class Builder extends LootFunction.Builder<ItemLevelLootFunction.Builder> {
        private final IRandomRange range;
        private int shift;

        public Builder(IRandomRange range) {
            this.range = range;
        }

        @Override
        protected ItemLevelLootFunction.Builder doCast() {
            return this;
        }

        public ItemLevelLootFunction.Builder shift(int shift) {
            this.shift = shift;
            return this;
        }

        @Override
        public ILootFunction build() {
            return new ItemLevelLootFunction(this.getConditions(), this.range, this.shift);
        }
    }

    public static class Serializer  extends LootFunction.Serializer<ItemLevelLootFunction>{

        @Override
        public ItemLevelLootFunction deserialize(JsonObject obj, JsonDeserializationContext ctx, ILootCondition[] conditions) {
            IRandomRange irandomrange = RandomRanges.deserialize(obj.get("level"), ctx);
            return new ItemLevelLootFunction(conditions, irandomrange, JsonUtils.get(obj, "level_shift", 0));
        }

        @Override
        public void toJson(JsonObject obj, ItemLevelLootFunction func, JsonSerializationContext context) {
            super.toJson(obj, func, context);
            obj.add("level", RandomRanges.serialize(func.range, context));
            obj.add("level_shift", context.serialize(func.shift));
        }
    }
}
