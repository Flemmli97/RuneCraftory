package io.github.flemmli97.runecraftory.common.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryLootRegistries;
import io.github.flemmli97.runecraftory.common.utils.ItemComponentUtils;
import io.github.flemmli97.runecraftory.common.world.data.farming.FarmlandData;
import io.github.flemmli97.runecraftory.common.world.data.farming.FarmlandHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

import java.util.List;

public class ItemLevelLootFunction extends LootItemConditionalFunction {

    public static final MapCodec<ItemLevelLootFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> commonFields(instance)
                    .and(NumberProviders.CODEC.fieldOf("level").forGetter(d -> d.level))
                    .apply(instance, ItemLevelLootFunction::new)
    );

    private final NumberProvider level;

    private ItemLevelLootFunction(List<LootItemCondition> conditions, NumberProvider level) {
        super(conditions);
        this.level = level;
    }

    public static Builder defaultFunc() {
        return new Builder().with(ConstantValue.exactly(1));
    }

    public static Builder with(NumberProvider provider) {
        return new Builder().with(provider);
    }

    @Override
    public LootItemFunctionType<ItemLevelLootFunction> getType() {
        return RuneCraftoryLootRegistries.ITEM_LEVEL.get();
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext ctx) {
        int level = 0;
        if (ctx.hasParam(LootCtxParameters.ITEM_LEVEL_CONTEXT))
            level = ctx.getParam(LootCtxParameters.ITEM_LEVEL_CONTEXT);
        else {
            if (ctx.hasParam(LootContextParams.BLOCK_STATE) && ctx.hasParam(LootContextParams.ORIGIN)) {
                BlockPos blockPos = BlockPos.containing(ctx.getParam(LootContextParams.ORIGIN));
                level = FarmlandHandler.get(ctx.getLevel().getServer())
                        .getData(ctx.getLevel(), blockPos)
                        .map(FarmlandData::getCropLevel).orElse(0);
            }
        }
        if (level == 0)
            level = this.getLevel(ctx);
        return ItemComponentUtils.getLeveledItem(stack, Math.abs(level));
    }

    public int getLevel(LootContext ctx) {
        return this.level.getInt(ctx);
    }

    public static class Builder extends LootItemConditionalFunction.Builder<ItemLevelLootFunction.Builder> {

        private NumberProvider level = ConstantValue.exactly(1);

        @Override
        protected ItemLevelLootFunction.Builder getThis() {
            return this;
        }

        public ItemLevelLootFunction.Builder with(NumberProvider level) {
            this.level = level;
            return this;
        }

        @Override
        public LootItemFunction build() {
            return new ItemLevelLootFunction(this.getConditions(), this.level);
        }
    }
}
