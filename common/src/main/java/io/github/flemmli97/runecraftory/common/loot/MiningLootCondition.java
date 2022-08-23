package io.github.flemmli97.runecraftory.common.loot;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.registry.ModLootCondition;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.Set;

public class MiningLootCondition implements LootItemCondition {

    private final int min;

    public MiningLootCondition(int required) {
        this.min = required;
    }

    public static LootItemCondition.Builder get(int val) {
        return () -> new MiningLootCondition(val);
    }

    @Override
    public LootItemConditionType getType() {
        return ModLootCondition.INT_CHECK.get();
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.THIS_ENTITY);
    }

    @Override
    public boolean test(LootContext ctx) {
        int level = 0;
        if (ctx.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof Player player) {
            level = Platform.INSTANCE.getPlayerData(player).map(data -> data.getSkillLevel(EnumSkills.MINING).getLevel()).orElse(0);
        }
        return level >= this.min;
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<MiningLootCondition> {

        @Override
        public void serialize(JsonObject object, MiningLootCondition condition, JsonSerializationContext context) {
            object.addProperty("min_required_level", condition.min);
        }

        @Override
        public MiningLootCondition deserialize(JsonObject obj, JsonDeserializationContext context) {
            return new MiningLootCondition(GsonHelper.getAsInt(obj, "min_required_level", 0));
        }
    }
}
