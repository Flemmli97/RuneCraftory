package io.github.flemmli97.runecraftory.common.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.npc.job.BathhouseAttendant;
import io.github.flemmli97.runecraftory.common.entities.npc.job.Cook;
import io.github.flemmli97.runecraftory.common.entities.npc.job.NPCJob;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

/**
 * Simple Registry for NPCJobs
 */
public class ModNPCJobs {

    public static final Codec<NPCJob> CODEC = ResourceLocation.CODEC.flatXmap(res -> getOptional(res).map(DataResult::success).orElse(DataResult.error("Unknown job: " + res)), val -> getOptionalIDFrom(val).map(DataResult::success).orElseGet(() -> DataResult.error("Job not registered:" + val)));

    public static final List<ResourceLocation> DEFAULT_JOB_ID = new ArrayList<>();

    private static int I = 0;
    private static final BiMap<ResourceLocation, NPCJob> JOBREGISTRY = HashBiMap.create();
    private static final BiMap<Integer, NPCJob> JOBREGISTRY_ID = HashBiMap.create();

    private static final ResourceLocation DEFAULT_KEY = new ResourceLocation(RuneCraftory.MODID, "jobless");

    public static final Pair<ResourceLocation, NPCJob> NONE = register(DEFAULT_KEY, new NPCJob(new NPCJob.Builder(null).noShop().noWorkSchedule()));

    public static final Pair<ResourceLocation, NPCJob> GENERAL = register("general", new NPCJob(new NPCJob.Builder(() -> PoiType.FARMER)));
    public static final Pair<ResourceLocation, NPCJob> FLOWER = register("flowers", new NPCJob(new NPCJob.Builder(() -> PoiType.FARMER)));
    public static final Pair<ResourceLocation, NPCJob> WEAPON = register("smith", new NPCJob(new NPCJob.Builder(() -> PoiType.TOOLSMITH)));
    public static final Pair<ResourceLocation, NPCJob> CLINIC = register("doctor", new NPCJob(new NPCJob.Builder(() -> PoiType.CLERIC)));
    public static final Pair<ResourceLocation, NPCJob> FOOD = register("cook", new Cook(new NPCJob.Builder(() -> PoiType.BUTCHER)));
    public static final Pair<ResourceLocation, NPCJob> MAGIC = register("magic", new NPCJob(new NPCJob.Builder(ModPoiTypes.CASH_REGISTER)));
    public static final Pair<ResourceLocation, NPCJob> RUNESKILL = register("rune_skills", new NPCJob(new NPCJob.Builder(ModPoiTypes.CASH_REGISTER)));
    public static final Pair<ResourceLocation, NPCJob> BATHHOUSE = register("bath_house", new BathhouseAttendant(new NPCJob.Builder(ModPoiTypes.CASH_REGISTER).noShop()));
    public static final Pair<ResourceLocation, NPCJob> RANDOM = register("random", new NPCJob(new NPCJob.Builder(null).noSchedule()));

    private static List<NPCJob> ALLJOBS;

    private static Pair<ResourceLocation, NPCJob> register(String name, NPCJob job) {
        ResourceLocation res = new ResourceLocation(RuneCraftory.MODID, name);
        Pair<ResourceLocation, NPCJob> pair = register(res, job);
        DEFAULT_JOB_ID.add(res);
        return pair;
    }

    public static Pair<ResourceLocation, NPCJob> register(ResourceLocation res, NPCJob job) {
        if (JOBREGISTRY.containsKey(res))
            throw new IllegalStateException("An entry with key " + res + " is already registered");
        JOBREGISTRY.put(res, job);
        JOBREGISTRY_ID.put(I++, job);
        return Pair.of(res, job);
    }

    public static NPCJob getFromID(ResourceLocation res) {
        return JOBREGISTRY.getOrDefault(res, NONE.getSecond());
    }

    public static Optional<NPCJob> getOptional(ResourceLocation res) {
        return Optional.ofNullable(JOBREGISTRY.get(res));
    }

    public static ResourceLocation getIDFrom(NPCJob job) {
        ResourceLocation res = JOBREGISTRY.inverse().get(job);
        return res == null ? JOBREGISTRY.inverse().get(job) : res;
    }

    public static Optional<ResourceLocation> getOptionalIDFrom(NPCJob job) {
        return Optional.ofNullable(JOBREGISTRY.inverse().get(job));
    }

    public static List<NPCJob> allJobs() {
        if (ALLJOBS == null)
            ALLJOBS = JOBREGISTRY_ID.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getKey))
                    .map(Map.Entry::getValue).toList();
        return ALLJOBS;
    }

    public static List<NPCJob> jobsWithShops() {
        return allJobs().stream().filter(j -> j.hasShop).toList();
    }

    public static NPCJob getRandomJob(Random random) {
        return allJobs().get(random.nextInt(allJobs().size()));
    }

    /**
     * Used for networking
     *
     * @return the NPCJob from the id or the default job {@link ModNPCJobs#NONE} if not found
     */
    public static NPCJob getFromSyncID(int id) {
        return JOBREGISTRY_ID.getOrDefault(id, NONE.getSecond());
    }

    /**
     * Used for networking
     *
     * @return the id from the given Job or -1 if not found
     */
    public static int getSyncIDFrom(NPCJob job) {
        return JOBREGISTRY_ID.inverse().getOrDefault(job, -1);
    }

    /**
     * for legacy player data. keep for a few versions
     */
    public static ResourceLocation legacyOfString(String string) {
        return switch (string) {
            case "NONE" -> NONE.getFirst();
            case "GENERAL" -> GENERAL.getFirst();
            case "FLOWER" -> FLOWER.getFirst();
            case "WEAPON" -> WEAPON.getFirst();
            case "CLINIC" -> CLINIC.getFirst();
            case "FOOD" -> FOOD.getFirst();
            case "MAGIC" -> MAGIC.getFirst();
            case "RUNESKILL" -> RUNESKILL.getFirst();
            case "RANDOM" -> RANDOM.getFirst();
            default -> DEFAULT_KEY;
        };
    }

    /**
     * for legacy npc data. keep for a few versions
     */
    public static ResourceLocation legacyOfTag(Tag tag) {
        if (tag == null)
            return DEFAULT_KEY;
        if (tag.getId() == Tag.TAG_STRING)
            return new ResourceLocation(tag.getAsString());
        if (tag.getId() == Tag.TAG_ANY_NUMERIC) {
            int id = ((NumericTag) tag).getAsInt();
            return switch (id) {
                case 0 -> NONE.getFirst();
                case 1 -> GENERAL.getFirst();
                case 2 -> FLOWER.getFirst();
                case 3 -> WEAPON.getFirst();
                case 4 -> CLINIC.getFirst();
                case 5 -> FOOD.getFirst();
                case 6 -> MAGIC.getFirst();
                case 7 -> RUNESKILL.getFirst();
                case 8 -> RANDOM.getFirst();
                default -> DEFAULT_KEY;
            };
        }
        return DEFAULT_KEY;
    }
}
