package io.github.flemmli97.runecraftory.common.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryCriteria;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;

import java.util.Optional;

public class SkillLevelTrigger extends SimpleCriterionTrigger<SkillLevelTrigger.TriggerInstance> {

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, Skills skill) {
        this.trigger(player, inst -> inst.matches(player, skill));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<Skills> skill,
                                  int level) implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                CodecUtils.stringEnumCodec(Skills.class, null).optionalFieldOf("skill").forGetter(TriggerInstance::skill),
                ExtraCodecs.POSITIVE_INT.fieldOf("level").forGetter(TriggerInstance::level)
        ).apply(inst, TriggerInstance::new));

        public static Criterion<TriggerInstance> of(int level) {
            return RuneCraftoryCriteria.SKILL_LEVEL_TRIGGER.get().createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), level));
        }

        public static Criterion<TriggerInstance> of(Skills skill, int level) {
            return RuneCraftoryCriteria.SKILL_LEVEL_TRIGGER.get().createCriterion(new TriggerInstance(Optional.empty(), Optional.ofNullable(skill), level));
        }

        public boolean matches(ServerPlayer player, Skills skill) {
            PlayerData data = Platform.INSTANCE.getPlayerData(player);
            return this.skill.map(s -> s == skill).orElse(true)
                    && data.getSkillLevel(skill).getLevel() >= this.level;
        }
    }
}
