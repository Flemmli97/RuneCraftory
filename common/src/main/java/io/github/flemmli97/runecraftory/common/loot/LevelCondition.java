package io.github.flemmli97.runecraftory.common.loot;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryLootRegistries;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.Set;

public class LevelCondition implements LootItemCondition {

    public static final MapCodec<LevelCondition> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(ExtraCodecs.NON_NEGATIVE_INT.fieldOf("minimum_level").forGetter(d -> d.min)
            ).apply(inst, LevelCondition::new));

    private final int min;

    public LevelCondition(int required) {
        this.min = required;
    }

    public static Builder get(int val) {
        return () -> new LevelCondition(val);
    }

    @Override
    public LootItemConditionType getType() {
        return RuneCraftoryLootRegistries.LEVEL_CHECK.get();
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.THIS_ENTITY);
    }

    @Override
    public boolean test(LootContext ctx) {
        if (ctx.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof Player player) {
            return RunecraftoryAttachments.PLAYER_DATA.get().get(player).getPlayerLevel().getLevel() >= this.min;
        }
        return false;
    }
}
