package com.flemmli97.runecraftory.common.loot;

import com.flemmli97.runecraftory.api.enums.EnumSkills;
import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.flemmli97.runecraftory.common.registry.ModLootModifier;
import com.flemmli97.tenshilib.common.utils.JsonUtils;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameter;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;

import java.util.Set;

public class MiningLootCondition implements ILootCondition {

    private final int min;

    public MiningLootCondition(int required) {
        this.min = required;
    }

    @Override
    public LootConditionType getConditionType() {
        return ModLootModifier.INT_CHECK;
    }

    @Override
    public Set<LootParameter<?>> getRequiredParameters() {
        return ImmutableSet.of(LootParameters.THIS_ENTITY);
    }

    @Override
    public boolean test(LootContext ctx) {
        int level = 0;
        if (ctx.get(LootParameters.THIS_ENTITY) instanceof PlayerEntity) {
            level = ctx.get(LootParameters.THIS_ENTITY).getCapability(CapabilityInsts.PlayerCap)
                    .map(cap -> cap.getSkillLevel(EnumSkills.MINING)[0]).orElse(0);
        }
        return level >= this.min;
    }

    public static ILootCondition.IBuilder get(int val) {
        return () -> new MiningLootCondition(val);
    }

    public static class Serializer implements ILootSerializer<MiningLootCondition> {

        @Override
        public void serialize(JsonObject object, MiningLootCondition condition, JsonSerializationContext context) {
            object.addProperty("min_required_level", condition.min);
        }

        @Override
        public MiningLootCondition deserialize(JsonObject obj, JsonDeserializationContext context) {
            return new MiningLootCondition(JsonUtils.get(obj, "min_required_level", 0));
        }
    }
}
