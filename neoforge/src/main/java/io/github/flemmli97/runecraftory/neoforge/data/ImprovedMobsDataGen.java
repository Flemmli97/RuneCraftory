package io.github.flemmli97.runecraftory.neoforge.data;

import io.github.flemmli97.improvedmobs.api.DifficultyFeatures;
import io.github.flemmli97.improvedmobs.api.datapack.DifficultyAttributeProperty;
import io.github.flemmli97.improvedmobs.api.datapack.EntityConfigProperties;
import io.github.flemmli97.improvedmobs.api.datapack.EntityTypeValue;
import io.github.flemmli97.improvedmobs.api.datapack.provider.EntityOverridesProvider;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;

import java.util.EnumSet;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ImprovedMobsDataGen extends EntityOverridesProvider {

    public ImprovedMobsDataGen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, RuneCraftory.MODID, provider);
    }

    @Override
    protected void add(HolderLookup.Provider provider) {
        for (RegistryEntrySupplier<EntityType<?>, ?> type : RuneCraftoryEntities.getMonsters()) {
            this.add(type.getID(), new EntityConfigProperties(EntityTypeValue.ofType(type.get()),
                    Optional.of(EnumSet.of(DifficultyFeatures.BLOCKBREAK, DifficultyFeatures.LADDER)),
                    new EntityConfigProperties.ConfigurableProperty<>(HolderSet.empty(), false),
                    new EntityConfigProperties.ConfigurableProperty<>(DifficultyAttributeProperty.builder().build(), false)));
        }
    }
}
