package io.github.flemmli97.runecraftory.common.loot;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.common.registry.ModLootRegistries;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.Set;

public class SkillLevelCondition implements LootItemCondition {

    public static final MapCodec<SkillLevelCondition> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(CodecUtils.stringEnumCodec(Skills.class, null).fieldOf("skill").forGetter(d -> d.skill),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("minimum_level").forGetter(d -> d.min)
            ).apply(inst, SkillLevelCondition::new));

    private final Skills skill;
    private final int min;

    public SkillLevelCondition(Skills skills, int required) {
        this.skill = skills;
        this.min = required;
    }

    public static LootItemCondition.Builder get(Skills skill, int val) {
        return () -> new SkillLevelCondition(skill, val);
    }

    @Override
    public LootItemConditionType getType() {
        return ModLootRegistries.SKILL_CHECK.get();
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.THIS_ENTITY);
    }

    @Override
    public boolean test(LootContext ctx) {
        if (ctx.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof Player player) {
            return Platform.INSTANCE.getPlayerData(player).getSkillLevel(this.skill).getLevel() >= this.min;
        }
        return false;
    }
}
