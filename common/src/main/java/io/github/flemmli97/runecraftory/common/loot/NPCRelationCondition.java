package io.github.flemmli97.runecraftory.common.loot;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.MapCodec;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModLootRegistries;
import io.github.flemmli97.runecraftory.common.world.family.FamilyEntry;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.Set;
import java.util.UUID;

public record NPCRelationCondition(FamilyEntry.Relationship relation) implements LootItemCondition {

    public static final MapCodec<NPCRelationCondition> CODEC = CodecUtils.stringEnumCodec(FamilyEntry.Relationship.class, null).fieldOf("relation")
            .xmap(NPCRelationCondition::new, NPCRelationCondition::relation);

    public static LootItemCondition.Builder of(FamilyEntry.Relationship relation) {
        return () -> new NPCRelationCondition(relation);
    }

    @Override
    public LootItemConditionType getType() {
        return ModLootRegistries.INTERACTINGPLAYER.get();
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.THIS_ENTITY, LootCtxParameters.UUID_CONTEXT);
    }

    @Override
    public boolean test(LootContext ctx) {
        if (ctx.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof EntityNPCBase npc) {
            UUID player = ctx.getParamOrNull(LootCtxParameters.UUID_CONTEXT);
            if (player != null)
                return npc.relationFor(player) == this.relation;
        }
        return false;
    }
}