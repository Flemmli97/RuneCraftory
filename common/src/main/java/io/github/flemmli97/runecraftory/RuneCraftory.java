package io.github.flemmli97.runecraftory;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RuneCraftory {

    public static final String MODID = "runecraftory";
    public static final Logger LOGGER = LogManager.getLogger(RuneCraftory.MODID);

    public static boolean simpleQuests, iris;

    /**
     * Simple loot context. Needs server side entity
     */
    public static LootContext createContext(LivingEntity entity) {
        return new LootContext.Builder((ServerLevel) entity.getLevel()).withParameter(LootContextParams.THIS_ENTITY, entity)
                .withParameter(LootContextParams.ORIGIN, entity.position()).withRandom(entity.getRandom())
                .create(LootContextParamSets.ADVANCEMENT_ENTITY);
    }
}
