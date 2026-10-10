package io.github.flemmli97.runecraftory.common.entities.ai.sensors;

import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryMemoryTypes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.PredicateSensor;
import net.tslat.smartbrainlib.object.SquareRadius;
import net.tslat.smartbrainlib.registry.SBLSensors;
import net.tslat.smartbrainlib.util.BrainUtils;
import net.tslat.smartbrainlib.util.EntityRetrievalUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NearbyNPCSensor extends PredicateSensor<LivingEntity, NPCEntity> {

    private static final List<MemoryModuleType<?>> MEMORIES = ObjectArrayList.of(MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);

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

    @Override
    protected void doTick(ServerLevel level, NPCEntity entity) {
        List<NPCEntity> entities = EntityRetrievalUtil.getEntities(entity, this.radius.xzRadius(), this.radius.yRadius(), this.radius.xzRadius(), NPCEntity.class,
                livingEntity -> this.predicate().test(livingEntity, entity));
        entities.sort(Comparator.comparingDouble(entity::distanceToSqr));
        Map<ResourceLocation, List<NPCEntity>> npcs = new HashMap<>();
        entities.forEach(npc -> {
            if (npc != entity) {
                npcs.computeIfAbsent(npc.getDataID(), k -> new ArrayList<>()).add(npc);
            }
        });
        BrainUtils.setMemory(entity, RuneCraftoryMemoryTypes.NEARBY_NPCS.get(), npcs);
    }
}
