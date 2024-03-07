package io.github.flemmli97.runecraftory.common.advancements;

import com.google.gson.JsonObject;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;

public class SkillLevelTrigger extends SimpleCriterionTrigger<SkillLevelTrigger.TriggerInstance> {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "skill_level_trigger");

    @Override
    protected SkillLevelTrigger.TriggerInstance createInstance(JsonObject json, EntityPredicate.Composite player, DeserializationContext context) {
        String s = GsonHelper.getAsString(json, "skill", "*");
        EnumSkills skill = null;
        if (!s.equals("*")) {
            try {
                skill = EnumSkills.valueOf(s);
            } catch (IllegalArgumentException e) {
                RuneCraftory.LOGGER.error("Error with skill level trigger. No such skill " + s);
            }
        }
        return new SkillLevelTrigger.TriggerInstance(player, GsonHelper.getAsInt(json, "level", 1), skill);
    }

    public void trigger(ServerPlayer player, EnumSkills skill) {
        this.trigger(player, inst -> inst.matches(player, skill));
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final int level;
        private final EnumSkills skill;

        public TriggerInstance(EntityPredicate.Composite composite, int amount, EnumSkills skill) {
            super(ID, composite);
            this.level = Math.max(1, amount);
            this.skill = skill;
        }

        public static SkillLevelTrigger.TriggerInstance of(int amount) {
            return new SkillLevelTrigger.TriggerInstance(EntityPredicate.Composite.ANY, amount, null);
        }

        public static SkillLevelTrigger.TriggerInstance of(int amount, EnumSkills skill) {
            return new SkillLevelTrigger.TriggerInstance(EntityPredicate.Composite.ANY, amount, skill);
        }

        public boolean matches(ServerPlayer player, EnumSkills skill) {
            return Platform.INSTANCE.getPlayerData(player).map(d -> (this.skill == null || skill == this.skill) && d.getSkillLevel(skill).getLevel() >= this.level).orElse(false);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context) {
            JsonObject obj = super.serializeToJson(context);
            obj.addProperty("level", this.level);
            obj.addProperty("skill", this.skill == null ? "*" : this.skill.name());
            return obj;
        }
    }
}
