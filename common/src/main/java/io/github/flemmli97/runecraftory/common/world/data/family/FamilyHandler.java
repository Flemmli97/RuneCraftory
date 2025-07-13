package io.github.flemmli97.runecraftory.common.world.data.family;

import io.github.flemmli97.runecraftory.api.datapack.npc.NPCData;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FamilyHandler extends SavedData {

    private static final String IDENTIFIER = "RunecraftoryFamilyHandler";
    private static final SavedData.Factory<FamilyHandler> FACTORY = new Factory<>(FamilyHandler::new, FamilyHandler::new, DataFixTypes.LEVEL);

    private final Map<UUID, FamilyEntry> families = new HashMap<>();

    private FamilyHandler() {
    }

    private FamilyHandler(CompoundTag tag, HolderLookup.Provider provider) {
        this.load(tag, provider);
    }

    public static FamilyHandler get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(FACTORY, IDENTIFIER);
    }

    public Optional<FamilyEntry> getFamily(UUID uuid) {
        return Optional.ofNullable(this.families.get(uuid));
    }

    public FamilyEntry getOrCreateEntry(Player player) {
        FamilyEntry entry = this.families.computeIfAbsent(player.getUUID(), id -> new FamilyEntry(this,
                player.getUUID(), player.getName(),
                NPCData.Gender.UNDEFINED, true));
        this.setDirty();
        return entry;
    }

    public FamilyEntry getOrCreateEntry(EntityNPCBase npc) {
        FamilyEntry entry = this.families.computeIfAbsent(npc.getUUID(), id -> new FamilyEntry(this,
                npc.getUUID(), npc.getName(),
                npc.isMale() ? NPCData.Gender.MALE : NPCData.Gender.FEMALE, false));
        this.setDirty();
        return entry;
    }

    public void load(CompoundTag tag, HolderLookup.Provider provider) {
        CompoundTag data = tag.getCompound("Families");
        data.getAllKeys().forEach((id) -> this.families.put(UUID.fromString(id), new FamilyEntry(this, data.getCompound(id), provider)));
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        CompoundTag data = new CompoundTag();
        this.families.forEach((uuid, entry) -> {
            if (entry.shouldPersist())
                data.put(uuid.toString(), entry.save(provider));
        });
        tag.put("Families", data);
        return tag;
    }
}
