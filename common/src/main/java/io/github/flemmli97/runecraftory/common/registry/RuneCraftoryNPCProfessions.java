package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.registry.NPCProfession;
import io.github.flemmli97.runecraftory.common.entities.npc.profession.BathhouseAttendant;
import io.github.flemmli97.runecraftory.common.entities.npc.profession.Blacksmith;
import io.github.flemmli97.runecraftory.common.entities.npc.profession.Chef;
import io.github.flemmli97.runecraftory.common.entities.npc.profession.Doctor;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;

import java.util.function.Supplier;

public class RuneCraftoryNPCProfessions {

    public static final ResourceKey<? extends Registry<NPCProfession>> PROFESSION_REGISTRY_KEY = ResourceKey.createRegistryKey(RuneCraftory.modRes("npc_professions"));
    public static final LoaderRegistryAccess.CustomLoaderRegistry<NPCProfession> PROFESSIONS = LoaderRegistryAccess.INSTANCE.newRegistry(PROFESSION_REGISTRY_KEY,
            RuneCraftory.modRes("jobless"), true, true);

    public static final RegistryEntrySupplier<NPCProfession, NPCProfession> NONE = register("jobless", () -> new NPCProfession(new NPCProfession.Builder(null).noShop().noWorkSchedule()));

    public static final RegistryEntrySupplier<NPCProfession, NPCProfession> GENERAL_STORE = register("general_store", () -> new NPCProfession(new NPCProfession.Builder(PoiTypes.FARMER).ownerTranslationKey("npc.profession.general_store.owner")));
    public static final RegistryEntrySupplier<NPCProfession, NPCProfession> FLORIST = register("florist", () -> new NPCProfession(new NPCProfession.Builder(PoiTypes.FARMER)));
    public static final RegistryEntrySupplier<NPCProfession, Blacksmith> BLACKSMITH = register("blacksmith", () -> new Blacksmith(new NPCProfession.Builder(PoiTypes.TOOLSMITH)));
    public static final RegistryEntrySupplier<NPCProfession, Doctor> DOCTOR = register("doctor", () -> new Doctor(new NPCProfession.Builder(PoiTypes.CLERIC)));
    public static final RegistryEntrySupplier<NPCProfession, Chef> CHEF = register("chef", () -> new Chef(new NPCProfession.Builder(PoiTypes.BUTCHER)));
    public static final RegistryEntrySupplier<NPCProfession, NPCProfession> SPELL_MERCHANT = register("spell_merchant", () -> new NPCProfession(new NPCProfession.Builder(RuneCraftoryPoiTypes.CASH_REGISTER.getKey())));
    public static final RegistryEntrySupplier<NPCProfession, NPCProfession> RUNE_ABILITIES_MERCHANT = register("rune_abilities_merchant", () -> new NPCProfession(new NPCProfession.Builder(RuneCraftoryPoiTypes.CASH_REGISTER.getKey())));
    public static final RegistryEntrySupplier<NPCProfession, NPCProfession> BATHHOUSE_ATTENDANT = register("bathhouse_attendant", () -> new BathhouseAttendant(new NPCProfession.Builder(RuneCraftoryPoiTypes.CASH_REGISTER.getKey()).noShop()));
    public static final RegistryEntrySupplier<NPCProfession, NPCProfession> TRAVELLING_MERCHANT = register("travelling_merchant", () -> new NPCProfession(new NPCProfession.Builder(null).noSchedule()));

    private static <T extends NPCProfession> RegistryEntrySupplier<NPCProfession, T> register(String name, Supplier<T> profession) {
        return PROFESSIONS.register().register(name, profession);
    }
}
