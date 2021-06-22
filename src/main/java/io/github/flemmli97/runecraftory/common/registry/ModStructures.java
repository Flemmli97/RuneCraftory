package io.github.flemmli97.runecraftory.common.registry;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.world.structure.AmbrosiaForestStructure;
import io.github.flemmli97.runecraftory.common.world.structure.ThunderboltRuinsStructure;
import io.github.flemmli97.runecraftory.common.world.structure.piece.AmbrosiaForestPiece;
import io.github.flemmli97.runecraftory.common.world.structure.piece.ThunderboltRuinsPiece;
import io.github.flemmli97.runecraftory.mixin.DimStrucSetAccess;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModStructures {

    public static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, RuneCraftory.MODID);

    public static final RegistryObject<Structure<NoFeatureConfig>> AMBROSIA_FOREST = register("ambrosia_forest", () -> new AmbrosiaForestStructure(NoFeatureConfig.CODEC), null);
    public static final RegistryObject<Structure<NoFeatureConfig>> THUNDERBOLT_RUINS = register("thunderbolt_ruins", () -> new ThunderboltRuinsStructure(NoFeatureConfig.CODEC), null);

    public static IStructurePieceType AMBROSIA_PIECE;
    public static IStructurePieceType THUNDERBOLT_PIECE;

    public static <T extends IFeatureConfig> RegistryObject<Structure<T>> register(String name, Supplier<Structure<T>> sup, StructureSeparationSettings settings) {
        return STRUCTURES.register(name, sup);
    }

    public static void setup() {
        Registry.register(Registry.STRUCTURE_PIECE, RuneCraftory.MODID + ":ambrosia_piece", AMBROSIA_PIECE = AmbrosiaForestPiece.Piece::new);
        Structure.NAME_STRUCTURE_BIMAP.put(AMBROSIA_FOREST.get().getRegistryName().toString(), AMBROSIA_FOREST.get());

        Registry.register(Registry.STRUCTURE_PIECE, RuneCraftory.MODID + ":thunderbolt_piece", THUNDERBOLT_PIECE = ThunderboltRuinsPiece.Piece::new);
        Structure.NAME_STRUCTURE_BIMAP.put(THUNDERBOLT_RUINS.get().getRegistryName().toString(), THUNDERBOLT_RUINS.get());

        Map<Structure<?>, StructureSeparationSettings> register = new HashMap<>();
        register.put(AMBROSIA_FOREST.get(), new StructureSeparationSettings(25, 15, 34645653));
        register.put(THUNDERBOLT_RUINS.get(), new StructureSeparationSettings(40, 32, 34645653));


        ImmutableMap<Structure<?>, StructureSeparationSettings> map = ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                .putAll(DimensionStructuresSettings.field_236191_b_)
                .putAll(register)
                .build();
        DimStrucSetAccess.setDEFAULT_STRUCTURES(map);

        WorldGenRegistries.NOISE_SETTINGS.getEntries().forEach(settings -> {
            Map<Structure<?>, StructureSeparationSettings> structureMap = settings.getValue().getStructures().func_236195_a_();
            Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(structureMap);
            tempMap.putAll(register);
            ((DimStrucSetAccess) settings.getValue().getStructures()).setStructures(tempMap);
        });
    }
}
