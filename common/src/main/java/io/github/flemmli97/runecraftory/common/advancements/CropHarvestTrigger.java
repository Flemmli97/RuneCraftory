package io.github.flemmli97.runecraftory.common.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.registry.ModCriteria;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class CropHarvestTrigger extends SimpleCriterionTrigger<CropHarvestTrigger.TriggerInstance> {

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, BlockState state) {
        this.trigger(player, inst -> inst.matches(state));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, HolderSet<Block> block,
                                  Optional<StatePropertiesPredicate> predicate) implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                RegistryCodecs.homogeneousList(Registries.BLOCK, Block.CODEC.codec()).fieldOf("block").forGetter(d -> d.block),
                StatePropertiesPredicate.CODEC.optionalFieldOf("predicate").forGetter((d) -> d.predicate)
        ).apply(inst, TriggerInstance::new));

        public static Criterion<TriggerInstance> harvest(TagKey<Block> tag) {
            return ModCriteria.HARVEST_CROP.get().createCriterion(new TriggerInstance(Optional.empty(),
                    BuiltInRegistries.BLOCK.getOrCreateTag(tag), Optional.empty()));
        }

        public boolean matches(BlockState state) {
            if (!(state.getBlock() instanceof CropBlock crop) || !crop.isMaxAge(state))
                return false;
            if (!state.is(this.block)) {
                return false;
            }
            return this.predicate.map(p -> p.matches(state)).orElse(true);
        }
    }
}
