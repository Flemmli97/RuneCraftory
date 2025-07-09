package io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class InvalidatePOITask<E extends Mob> extends ExtendedBehaviour<E> {

    protected final MemoryModuleType<GlobalPos> poiMemory;

    protected final Predicate<Holder<PoiType>> predicate;
    protected final Consumer<E> onInvalidate;
    protected BiPredicate<E, BlockPos> isStillValid = (e, pos) -> true;

    public InvalidatePOITask(MemoryModuleType<GlobalPos> poiMemory, ResourceKey<PoiType> poi, Consumer<E> onInvalidate) {
        this(poiMemory, h -> h.is(poi), onInvalidate);
    }

    public InvalidatePOITask(MemoryModuleType<GlobalPos> poiMemory, @Nullable Predicate<Holder<PoiType>> predicate, Consumer<E> onInvalidate) {
        this.poiMemory = poiMemory;
        this.predicate = predicate;
        this.onInvalidate = onInvalidate;
        this.cooldownFor(e -> 10);
        this.entryCondition.put(poiMemory, MemoryStatus.VALUE_PRESENT);
    }

    public InvalidatePOITask<E> isStillValid(BiPredicate<E, BlockPos> isStillValid) {
        this.isStillValid = isStillValid;
        return this;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of();
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        return this.predicate != null;
    }

    @Override
    protected void start(E entity) {
        GlobalPos pos = BrainUtils.getMemory(entity, this.poiMemory);
        if (pos == null || entity.level().dimension() != pos.dimension())
            return;
        ServerLevel poiLevel = (ServerLevel) entity.level();
        if (!poiLevel.getPoiManager().exists(pos.pos(), this.predicate) || !this.isStillValid.test(entity, pos.pos())) {
            this.onInvalidate.accept(entity);
        }
    }
}
