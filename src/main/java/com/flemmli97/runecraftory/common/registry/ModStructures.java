package com.flemmli97.runecraftory.common.registry;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.world.structure.AmbrosiaForestStructure;
import com.flemmli97.runecraftory.common.world.structure.piece.AmbrosiaForestPiece;
import com.flemmli97.runecraftory.mixin.DimStrucSetAccess;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModStructures {

    public static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, RuneCraftory.MODID);

    public static final RegistryObject<Structure<NoFeatureConfig>> AMBROSIA_FOREST = register("ambrosia_forest", () -> new AmbrosiaForestStructure(NoFeatureConfig.CODEC), null);

    public static IStructurePieceType AMBROSIA_PIECE;

    public static <T extends IFeatureConfig> RegistryObject<Structure<T>> register(String name, Supplier<Structure<T>> sup, StructureSeparationSettings settings){
        return STRUCTURES.register(name, sup);
    }

    public static void setup(){
        Registry.register(Registry.STRUCTURE_PIECE, RuneCraftory.MODID+":ambrosia_piece", AMBROSIA_PIECE = AmbrosiaForestPiece.Piece::new);

        Structure.STRUCTURES.put(AMBROSIA_FOREST.get().getRegistryName().toString(), AMBROSIA_FOREST.get());
        ImmutableMap<Structure<?>, StructureSeparationSettings> map = ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                .putAll(DimensionStructuresSettings.DEFAULT_STRUCTURES)
                .put(AMBROSIA_FOREST.get(), new StructureSeparationSettings(3, 1, 34645653))
                .build();
        DimStrucSetAccess.setDEFAULT_STRUCTURES(map);
    }
}
