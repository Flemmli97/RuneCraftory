package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityKingWooly extends EntityAggressiveWooly {

    public static final ResourceLocation KING_WOOLY_WOOLED_LOOT = RuneCraftory.modRes("entities/king_wooly/white");

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

    @Override
    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 0.6f;
    }

    @Override
    public float getSoundVolume() {
        return 1.15f;
    }

    @Override
    public Vec3 passengerOffset(Entity passenger) {
        return super.passengerOffset(passenger).scale(2.5f);
    }
}
