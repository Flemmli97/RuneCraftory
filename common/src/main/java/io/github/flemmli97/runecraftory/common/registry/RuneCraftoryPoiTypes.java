package io.github.flemmli97.runecraftory.common.registry;

import com.google.common.collect.ImmutableSet;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;

public class RuneCraftoryPoiTypes {

    public static final LoaderRegister<PoiType> POI = LoaderRegistryAccess.INSTANCE.of(Registries.POINT_OF_INTEREST_TYPE, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<PoiType, PoiType> CASH_REGISTER = POI.register("cash_register", () -> new PoiType(ImmutableSet.copyOf(RuneCraftoryBlocks.CASH_REGISTER.get().getStateDefinition().getPossibleStates()), 1, 1));
}
