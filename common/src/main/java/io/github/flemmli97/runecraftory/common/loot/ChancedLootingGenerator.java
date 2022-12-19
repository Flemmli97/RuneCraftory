package io.github.flemmli97.runecraftory.common.loot;

import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import io.github.flemmli97.runecraftory.common.registry.ModLootCondition;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

import java.util.Random;
import java.util.Set;

public class ChancedLootingGenerator implements NumberProvider {

    protected final NumberProvider max;
    protected final NumberProvider p;

    private ChancedLootingGenerator(NumberProvider max, NumberProvider chance) {
        this.max = max;
        this.p = chance;
    }

    @Override
    public LootNumberProviderType getType() {
        return ModLootCondition.BINOMIAL_LOOTING.get();
    }

    @Override
    public int getInt(LootContext lootContext) {
        int max = this.max.getInt(lootContext);
        if (this.max.getType() == NumberProviders.CONSTANT && max == 0)
            max = Integer.MAX_VALUE;
        int i = Math.min(Platform.INSTANCE.getLootingFromCtx(lootContext), max);
        float chance = this.p.getFloat(lootContext);
        Random random = lootContext.getRandom();
        int j = 0;
        for (int k = 0; k < i; ++k) {
            if (random.nextFloat() < chance) {
                ++j;
            } else
                break;
        }
        return j;
    }

    @Override
    public float getFloat(LootContext lootContext) {
        return this.getInt(lootContext);
    }

    public static ChancedLootingGenerator chance(int max, float p) {
        return new ChancedLootingGenerator(ConstantValue.exactly(max), ConstantValue.exactly(p));
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return Sets.union(this.max.getReferencedContextParams(), this.p.getReferencedContextParams());
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<ChancedLootingGenerator> {

        @Override
        public ChancedLootingGenerator deserialize(JsonObject jsonObject, JsonDeserializationContext context) {
            NumberProvider numberProvider = GsonHelper.getAsObject(jsonObject, "max", context, NumberProvider.class);
            NumberProvider numberProvider2 = GsonHelper.getAsObject(jsonObject, "chance", context, NumberProvider.class);
            return new ChancedLootingGenerator(numberProvider, numberProvider2);
        }

        @Override
        public void serialize(JsonObject jsonObject, ChancedLootingGenerator val, JsonSerializationContext context) {
            jsonObject.add("max", context.serialize(val.max));
            jsonObject.add("chance", context.serialize(val.p));
        }
    }
}
