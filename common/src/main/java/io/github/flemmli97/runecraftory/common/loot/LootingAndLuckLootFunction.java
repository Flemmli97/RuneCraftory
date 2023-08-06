package io.github.flemmli97.runecraftory.common.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import io.github.flemmli97.runecraftory.common.registry.ModLootCondition;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class LootingAndLuckLootFunction extends LootItemConditionalFunction {

    private final NumberProvider baseChance;
    private final NumberProvider lootingBonus;
    private final NumberProvider luckBonus;
    private final int limit;

    private LootingAndLuckLootFunction(LootItemCondition[] conditions, NumberProvider baseChance, NumberProvider luckBonus, NumberProvider lootingBonus, int limit) {
        super(conditions);
        this.baseChance = baseChance;
        this.luckBonus = luckBonus;
        this.lootingBonus = lootingBonus;
        this.limit = limit;
    }

    @Override
    public LootItemFunctionType getType() {
        return ModLootCondition.LUCK_AND_LOOTING.get();
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext ctx) {
        Entity entity = ctx.getParamOrNull(LootContextParams.KILLER_ENTITY);
        float luck = ctx.getLuck();
        int looting = 0;
        if (entity instanceof LivingEntity) {
            looting = Platform.INSTANCE.getLootingFromCtx(ctx);
        }
        float chance = (this.baseChance.getFloat(ctx) + this.luckBonus.getFloat(ctx) * luck) * (1 + this.lootingBonus.getFloat(ctx) * looting);
        if (chance >= 1) {
            int uniform = (int) chance * 2;
            float left = chance - uniform;
            int amount = ctx.getRandom().nextInt(uniform + 1);
            if (ctx.getRandom().nextFloat() < left) {
                amount++;
            }
            if (this.limit > 0)
                amount = Math.min(this.limit, amount);
            stack.setCount(amount);
            return stack;
        } else if (ctx.getRandom().nextFloat() < chance) {
            return stack;
        }
        return ItemStack.EMPTY;
    }

    public static class Builder extends LootItemConditionalFunction.Builder<LootingAndLuckLootFunction.Builder> {

        private final NumberProvider baseChance;
        private NumberProvider lootingBonus = ConstantValue.exactly(0);
        private NumberProvider luckBonus = ConstantValue.exactly(0);
        private int limit = 0;

        public Builder(NumberProvider baseChance) {
            this.baseChance = baseChance;
        }

        @Override
        protected LootingAndLuckLootFunction.Builder getThis() {
            return this;
        }

        public LootingAndLuckLootFunction.Builder withLuckBonus(NumberProvider luckBonus) {
            this.luckBonus = luckBonus;
            return this;
        }

        public LootingAndLuckLootFunction.Builder withLootingBonus(NumberProvider lootingBonus) {
            this.lootingBonus = lootingBonus;
            return this;
        }

        public LootingAndLuckLootFunction.Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        @Override
        public LootItemFunction build() {
            return new LootingAndLuckLootFunction(this.getConditions(), this.baseChance, this.luckBonus, this.lootingBonus, this.limit);
        }
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<LootingAndLuckLootFunction> {

        @Override
        public void serialize(JsonObject obj, LootingAndLuckLootFunction func, JsonSerializationContext context) {
            super.serialize(obj, func, context);
            obj.add("baseChance", context.serialize(func.baseChance));
            obj.add("luckBonus", context.serialize(func.luckBonus));
            obj.add("lootingBonus", context.serialize(func.lootingBonus));
            obj.addProperty("limit", func.limit);
        }

        @Override
        public LootingAndLuckLootFunction deserialize(JsonObject obj, JsonDeserializationContext ctx, LootItemCondition[] conditions) {
            return new LootingAndLuckLootFunction(conditions,
                    GsonHelper.getAsObject(obj, "baseChance", ctx, NumberProvider.class),
                    GsonHelper.getAsObject(obj, "luckBonus", ctx, NumberProvider.class),
                    GsonHelper.getAsObject(obj, "lootingBonus", ctx, NumberProvider.class),
                    GsonHelper.getAsInt(obj, "limit", 0));
        }
    }
}