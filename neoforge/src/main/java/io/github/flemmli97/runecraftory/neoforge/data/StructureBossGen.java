package io.github.flemmli97.runecraftory.neoforge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.provider.FileVerifier;
import io.github.flemmli97.runecraftory.api.datapack.provider.StructureBossProvider;
import io.github.flemmli97.runecraftory.common.datapack.manager.StructureBossManager;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

public class StructureBossGen extends StructureBossProvider {

    public static final ResourceLocation FOREST_BOSSES = RuneCraftory.modRes("forest");
    public static final ResourceLocation WATER_RUIN_BOSSES = RuneCraftory.modRes("water_ruin");
    public static final ResourceLocation THEATER_RUIN_BOSSES = RuneCraftory.modRes("theater_ruin");
    public static final ResourceLocation PLAINS_BOSSES = RuneCraftory.modRes("plains");
    public static final ResourceLocation DESERT_BOSSES = RuneCraftory.modRes("desert");
    public static final ResourceLocation NETHER_BOSSES = RuneCraftory.modRes("nether");
    public static final ResourceLocation WIND_SHRINE_BOSSES = RuneCraftory.modRes("wind_shrine");
    public static final ResourceLocation LEON_KARNAK_BOSSES = RuneCraftory.modRes("leon_karnak");

    public StructureBossGen(PackOutput packOutput, FileVerifier verifier, CompletableFuture<HolderLookup.Provider> provider) {
        super(packOutput, RuneCraftory.MODID, verifier, provider);
    }

    @Override
    protected void add(HolderLookup.Provider provider) {
        this.addGateSpawn(FOREST_BOSSES, new StructureBossManager.BossSpawnList(SimpleWeightedRandomList
                .<EntityType<?>>builder().add(ModEntities.AMBROSIA.get(), 10)
                .add(ModEntities.DEAD_TREE.get(), 10).build()));
        this.addGateSpawn(WATER_RUIN_BOSSES, new StructureBossManager.BossSpawnList(SimpleWeightedRandomList
                .<EntityType<?>>builder().add(ModEntities.THUNDERBOLT.get(), 8)
                .add(ModEntities.CHIMERA.get(), 10).build()));
        this.addGateSpawn(THEATER_RUIN_BOSSES, new StructureBossManager.BossSpawnList(SimpleWeightedRandomList
                .<EntityType<?>>builder().add(ModEntities.MARIONETTA.get(), 10)
                .add(ModEntities.HANDONETTA.get(), 7).build()));
        this.addGateSpawn(PLAINS_BOSSES, new StructureBossManager.BossSpawnList(SimpleWeightedRandomList
                .<EntityType<?>>builder().add(ModEntities.RACCOON.get(), 10).build()));
        this.addGateSpawn(DESERT_BOSSES, new StructureBossManager.BossSpawnList(SimpleWeightedRandomList
                .<EntityType<?>>builder().add(ModEntities.SKELEFANG.get(), 10).build()));
        this.addGateSpawn(NETHER_BOSSES, new StructureBossManager.BossSpawnList(SimpleWeightedRandomList
                .<EntityType<?>>builder().add(ModEntities.RAFFLESIA.get(), 10).build()));
        this.addGateSpawn(WIND_SHRINE_BOSSES, new StructureBossManager.BossSpawnList(SimpleWeightedRandomList
                .<EntityType<?>>builder().add(ModEntities.GRIMOIRE.get(), 10).build()));
        this.addGateSpawn(LEON_KARNAK_BOSSES, new StructureBossManager.BossSpawnList(SimpleWeightedRandomList
                .<EntityType<?>>builder().add(ModEntities.SANO_AND_UNO.get(), 10).build()));
    }
}
