package io.github.flemmli97.runecraftory.common.registry;

import com.google.common.collect.ImmutableSet;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.state.BlockState;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class ModPoiTypes {

    public static final PlatformRegistry<PoiType> POI = PlatformUtils.INSTANCE.of(Registry.POINT_OF_INTEREST_TYPE_REGISTRY, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<PoiType> CASH_REGISTER = POI.register("cash_register", () -> createPoiType("cash_register",
            ImmutableSet.copyOf(ModBlocks.cashRegister.get().getStateDefinition().getPossibleStates()), 1, 1));

    private static PoiType createPoiType(String string, Set<BlockState> set, int i, int j) {
        try {
            Constructor<PoiType> cons = PoiType.class.getDeclaredConstructor(String.class, Set.class, int.class, int.class);
            cons.setAccessible(true);
            return cons.newInstance(RuneCraftory.MODID + ":" + string, set, i, j);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
