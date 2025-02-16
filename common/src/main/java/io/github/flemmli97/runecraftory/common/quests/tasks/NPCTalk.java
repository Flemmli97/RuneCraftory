package io.github.flemmli97.runecraftory.common.quests.tasks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.quests.NPCQuest;
import io.github.flemmli97.runecraftory.common.world.WorldHandler;
import io.github.flemmli97.simplequests_api.player.PlayerQuestData;
import io.github.flemmli97.simplequests_api.quest.QuestBase;
import io.github.flemmli97.simplequests_api.quest.entry.QuestEntry;
import io.github.flemmli97.simplequests_api.quest.entry.QuestEntryKey;
import io.github.flemmli97.simplequests_api.util.JsonCodecs;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;
import java.util.UUID;

public class NPCTalk implements QuestEntry {

    public static final QuestEntryKey<NPCTalk> ID = new QuestEntryKey<>(new ResourceLocation(RuneCraftory.MODID, "npc_talk"));
    public static final Codec<NPCTalk> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(ResourceLocation.CODEC.optionalFieldOf("target_npcid").forGetter(d -> Optional.ofNullable(d.targetNPCId)),
                            JsonCodecs.ENTITY_PREDICATE_CODEC.optionalFieldOf("predicate").forGetter(d -> d.predicate == EntityPredicate.ANY ? Optional.empty() : Optional.of(d.predicate)),
                            Codec.STRING.optionalFieldOf("target_npc").forGetter(d -> d.targetNPC != null ? Optional.of(d.targetNPC.toString()) : Optional.empty()))
                    .apply(instance, (generic, predicate, target) -> new NPCTalk(generic.orElse(null), predicate.orElse(EntityPredicate.ANY), target.map(UUID::fromString).orElse(null))));

    private final ResourceLocation targetNPCId;

    private final UUID targetNPC;
    private final EntityPredicate predicate;
    private EntityNPCBase npc;

    public NPCTalk(ResourceLocation generic) {
        this(generic, EntityPredicate.ANY, null);
    }

    public NPCTalk(ResourceLocation generic, EntityPredicate predicate) {
        this(generic, predicate, null);
    }

    protected NPCTalk(ResourceLocation generic, EntityPredicate predicate, UUID targetNPC) {
        this.targetNPCId = generic;
        this.predicate = predicate;
        this.targetNPC = targetNPC;
    }

    @Override
    public boolean submit(ServerPlayer player) {
        return false;
    }

    @Override
    public QuestEntryKey<?> getId() {
        return ID;
    }

    @Override
    public MutableComponent translation(ServerPlayer player) {
        if (this.npc == null || !this.npc.isAlive()) {
            if (this.targetNPC == null)
                this.npc = null;
            else
                this.npc = EntityUtil.findFromUUID(EntityNPCBase.class, player.getLevel(), this.targetNPC);
        }
        Component name;
        if (this.npc == null) {
            name = this.targetNPC != null ? WorldHandler.get(player.getServer())
                    .npcHandler.getName(this.targetNPC) : null;
        } else {
            name = this.npc.getName();
        }
        if (name != null)
            return new TranslatableComponent(this.getId().toString(), name);
        return new TranslatableComponent(this.getId().toString() + ".generic");
    }

    public boolean trySubmit(ServerPlayer player, EntityNPCBase npc) {
        return npc.getUUID().equals(this.targetNPC) && this.predicate.matches(player, npc);
    }

    @Override
    public QuestEntry resolve(PlayerQuestData data, QuestBase quest) {
        if (quest instanceof NPCQuest npcQuest)
            return new NPCTalk(this.targetNPCId, this.predicate, npcQuest.getNpcUuid());
        return new NPCTalk(this.targetNPCId, this.predicate, this.targetNPC);
    }
}
