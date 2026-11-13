package io.github.flemmli97.runecraftory.common.registry;

import com.mojang.serialization.MapCodec;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.advancements.predicate.EntitySubPredicateList;
import io.github.flemmli97.runecraftory.common.advancements.predicate.EntityWetPredicate;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.core.registries.Registries;

public class RuneCraftorySubPredicates {

    public static final LoaderRegister<MapCodec<? extends EntitySubPredicate>> SUB_PREDICATES = LoaderRegistryAccess.INSTANCE.of(Registries.ENTITY_SUB_PREDICATE_TYPE, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<MapCodec<? extends EntitySubPredicate>, MapCodec<EntityWetPredicate>> WET = SUB_PREDICATES.register("wet", () -> EntityWetPredicate.CODEC);
    public static final RegistryEntrySupplier<MapCodec<? extends EntitySubPredicate>, MapCodec<EntitySubPredicateList>> SUB_PREDICATE_LIST = SUB_PREDICATES.register("sub_predicate_list", () -> EntitySubPredicateList.CODEC);

}
