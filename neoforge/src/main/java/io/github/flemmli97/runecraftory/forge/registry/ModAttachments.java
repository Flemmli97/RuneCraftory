package io.github.flemmli97.runecraftory.forge.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.forge.attachment.PlayerDataAttachment;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, RuneCraftory.MODID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<EntityData>> ENTITY_DATA = ATTACHMENT_TYPES.register("entity_data", () -> AttachmentType.builder(EntityData::new).build());
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerDataAttachment>> PLAYER_DATA = ATTACHMENT_TYPES.register("player_data", () -> AttachmentType.builder().serializable(PlayerDataAttachment::new).build());

}
