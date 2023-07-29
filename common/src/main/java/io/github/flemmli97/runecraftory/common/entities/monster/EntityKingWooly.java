package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityKingWooly extends EntityAggressiveWooly {

    public static final ResourceLocation KING_WOOLY_WOOLED_LOOT = new ResourceLocation(RuneCraftory.MODID, "entities/king_wooly/white");

    public EntityKingWooly(EntityType<? extends EntityWooly> type, Level level) {
        super(type, level);
    }

    @Override
    protected ResourceLocation getDefaultLootTable() {
        if (this.isSheared())
            return super.getDefaultLootTable();
        else
            return KING_WOOLY_WOOLED_LOOT;
    }
}
