package io.github.flemmli97.runecraftory.common.loot;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import io.github.flemmli97.runecraftory.common.registry.ModLootRegistries;
import io.github.flemmli97.runecraftory.common.world.WorldHandler;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.Set;

public record SeasonLootCondition(EnumSeason season) implements LootItemCondition {

    public static LootItemCondition.Builder get(EnumSeason season) {
        return () -> new SeasonLootCondition(season);
    }

    @Override
    public LootItemConditionType getType() {
        return ModLootRegistries.SEASONTYPE.get();
    }

    @Override
    public boolean test(LootContext ctx) {
        return WorldHandler.get(ctx.getLevel().getServer()).currentSeason() == this.season;
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<SeasonLootCondition> {

        @Override
        public void serialize(JsonObject object, SeasonLootCondition condition, JsonSerializationContext context) {
            object.addProperty("season", condition.season.name());
        }

        @Override
        public SeasonLootCondition deserialize(JsonObject obj, JsonDeserializationContext context) {
            String s = GsonHelper.getAsString(obj, "season");
            try {
                return new SeasonLootCondition(EnumSeason.valueOf(s));
            } catch (IllegalArgumentException e) {
                throw new JsonSyntaxException("No such season for " + s);
            }
        }
    }
}
