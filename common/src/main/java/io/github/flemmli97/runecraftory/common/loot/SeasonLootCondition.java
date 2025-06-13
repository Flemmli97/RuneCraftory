package io.github.flemmli97.runecraftory.common.loot;

import com.mojang.serialization.MapCodec;
import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import io.github.flemmli97.runecraftory.common.registry.ModLootRegistries;
import io.github.flemmli97.runecraftory.common.world.WorldHandler;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public record SeasonLootCondition(EnumSeason season) implements LootItemCondition {

    public static final MapCodec<SeasonLootCondition> CODEC = CodecUtils.stringEnumCodec(EnumSeason.class, null).fieldOf("count")
            .xmap(SeasonLootCondition::new, SeasonLootCondition::season);

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
}
