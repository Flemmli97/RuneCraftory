package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;

public class KingWooly extends AggressiveWooly {

    public static final ResourceKey<LootTable> KING_WOOLY_WOOLED_LOOT = ResourceKey.create(Registries.LOOT_TABLE, RuneCraftory.modRes("entities/king_wooly/white"));

    public KingWooly(EntityType<? extends Wooly> type, Level level) {
        super(type, level);
    }

    @Override
    protected ResourceKey<LootTable> getDefaultLootTable() {
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
}
