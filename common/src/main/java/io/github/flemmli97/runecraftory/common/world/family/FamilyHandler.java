package io.github.flemmli97.runecraftory.common.world.family;

import io.github.flemmli97.runecraftory.api.datapack.NPCData;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FamilyHandler extends SavedData {

    private static final String ID = "RunecraftoryFamilyHandler";

    private final Map<UUID, FamilyEntry> families = new HashMap<>();

    public FamilyHandler() {
    }

    private FamilyHandler(CompoundTag tag) {
        this.load(tag);
    }

    public static FamilyHandler get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(FamilyHandler::new, FamilyHandler::new, ID);
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

    public void load(CompoundTag compoundNBT) {
        CompoundTag data = compoundNBT.getCompound("Families");
        data.getAllKeys().forEach((id) -> {
            this.families.put(UUID.fromString(id), new FamilyEntry(this, data.getCompound(id)));
        });
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        CompoundTag data = new CompoundTag();
        this.families.forEach((uuid, entry) -> {
            if (entry.shouldPersist())
                data.put(uuid.toString(), entry.save());
        });
        compoundTag.put("Families", data);
        return compoundTag;
    }
}
