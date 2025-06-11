package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.npc.job.BathhouseAttendant;
import io.github.flemmli97.runecraftory.common.entities.npc.job.Cook;
import io.github.flemmli97.runecraftory.common.entities.npc.job.Doctor;
import io.github.flemmli97.runecraftory.common.entities.npc.job.NPCJob;
import io.github.flemmli97.runecraftory.common.entities.npc.job.Smith;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;

import java.util.function.Supplier;

/**
 * Simple Registry for NPCJobs
 */
public class ModNPCJobs {

    public static final ResourceKey<? extends Registry<NPCJob>> JOB_REGISTRY_KEY = ResourceKey.createRegistryKey(RuneCraftory.modRes("npc_actions"));
    public static final LoaderRegistryAccess.CustomLoaderRegistry<NPCJob> JOBS = LoaderRegistryAccess.INSTANCE.newRegistry(JOB_REGISTRY_KEY,
            RuneCraftory.modRes("jobless"), true, false);

    public static final RegistryEntrySupplier<NPCJob, NPCJob> NONE = register("jobless", () -> new NPCJob(new NPCJob.Builder(null).noShop().noWorkSchedule()));

    public static final RegistryEntrySupplier<NPCJob, NPCJob> GENERAL = register("general_store", () -> new NPCJob(new NPCJob.Builder(PoiTypes.FARMER)));
    public static final RegistryEntrySupplier<NPCJob, NPCJob> FLOWER = register("florist", () -> new NPCJob(new NPCJob.Builder(PoiTypes.FARMER)));
    public static final RegistryEntrySupplier<NPCJob, Smith> SMITH = register("smith", () -> new Smith(new NPCJob.Builder(PoiTypes.TOOLSMITH)));
    public static final RegistryEntrySupplier<NPCJob, Doctor> DOCTOR = register("doctor", () -> new Doctor(new NPCJob.Builder(PoiTypes.CLERIC)));
    public static final RegistryEntrySupplier<NPCJob, Cook> COOK = register("cook", () -> new Cook(new NPCJob.Builder(PoiTypes.BUTCHER)));
    public static final RegistryEntrySupplier<NPCJob, NPCJob> MAGIC = register("magicskill_merchant", () -> new NPCJob(new NPCJob.Builder(ModPoiTypes.CASH_REGISTER.getKey())));
    public static final RegistryEntrySupplier<NPCJob, NPCJob> RUNE_SKILLS = register("runeskill_merchant", () -> new NPCJob(new NPCJob.Builder(ModPoiTypes.CASH_REGISTER.getKey())));
    public static final RegistryEntrySupplier<NPCJob, NPCJob> BATHHOUSE = register("bathhouse_attendant", () -> new BathhouseAttendant(new NPCJob.Builder(ModPoiTypes.CASH_REGISTER.getKey()).noShop()));
    public static final RegistryEntrySupplier<NPCJob, NPCJob> RANDOM = register("travelling_merchant", () -> new NPCJob(new NPCJob.Builder(null).noSchedule()));

    private static <T extends NPCJob> RegistryEntrySupplier<NPCJob, T> register(String name, Supplier<T> job) {
        return JOBS.register().register(name, job);
    }
}
