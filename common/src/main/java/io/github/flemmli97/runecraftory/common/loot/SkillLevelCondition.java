package io.github.flemmli97.runecraftory.common.loot;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
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

public class SkillLevelCondition implements LootItemCondition {

    private final EnumSkills skill;
    private final int min;

    public SkillLevelCondition(EnumSkills skills, int required) {
        this.skill = skills;
        this.min = required;
    }

    public static LootItemCondition.Builder get(EnumSkills skill, int val) {
        return () -> new SkillLevelCondition(skill, val);
    }

    @Override
    public LootItemConditionType getType() {
        return ModLootCondition.SKILL_CHECK.get();
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.THIS_ENTITY);
    }

    @Override
    public boolean test(LootContext ctx) {
        int level = 0;
        if (ctx.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof Player player) {
            level = Platform.INSTANCE.getPlayerData(player).map(data -> data.getSkillLevel(this.skill).getLevel()).orElse(0);
        }
        return level >= this.min;
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<SkillLevelCondition> {

        @Override
        public void serialize(JsonObject object, SkillLevelCondition condition, JsonSerializationContext context) {
            object.addProperty("skill", condition.skill.name());
            object.addProperty("min_required_level", condition.min);
        }

        @Override
        public SkillLevelCondition deserialize(JsonObject obj, JsonDeserializationContext context) {
            String skillString = GsonHelper.getAsString(obj, "skill");
            try {
                return new SkillLevelCondition(EnumSkills.valueOf(skillString), GsonHelper.getAsInt(obj, "min_required_level", 0));
            } catch (IllegalArgumentException e) {
                throw new JsonSyntaxException("Unknown skill '" + skillString + "'");
            }
        }
    }
}
