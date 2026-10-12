package io.github.flemmli97.runecraftory.common.entities.ai.sensors;

import com.google.common.collect.ImmutableList;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryMemoryTypes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.PredicateSensor;
import net.tslat.smartbrainlib.object.SquareRadius;
import net.tslat.smartbrainlib.registry.SBLSensors;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class NearbyNPCSensor extends PredicateSensor<LivingEntity, NPCEntity> {

    private static final List<MemoryModuleType<?>> MEMORIES = ObjectArrayList.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            RuneCraftoryMemoryTypes.NEARBY_NPCS.get());

    protected final SquareRadius radius = new SquareRadius(5, 4);

    public NearbyNPCSensor() {
        super((target, entity) -> target != entity && target.isAlive());
    }

    @Override
    public List<MemoryModuleType<?>> memoriesUsed() {
        return MEMORIES;
    }

    @Override
    public SensorType<? extends ExtendedSensor<?>> type() {
        return SBLSensors.NEARBY_LIVING_ENTITY.get();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected void doTick(ServerLevel level, NPCEntity entity) {
        NearestVisibleLivingEntities entities = BrainUtils.getMemory(entity, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
        List<NPCEntity> npcs = ImmutableList.copyOf((Iterable) entities.findAll(e -> e instanceof NPCEntity));
        BrainUtils.setMemory(entity, RuneCraftoryMemoryTypes.NEARBY_NPCS.get(), npcs);
    }
}
