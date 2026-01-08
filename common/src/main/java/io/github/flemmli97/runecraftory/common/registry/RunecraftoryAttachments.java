package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.tenshilib.common.attachment.AttachmentType;
import io.github.flemmli97.tenshilib.loader.registry.AttachmentRegister;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.function.Supplier;

public class RunecraftoryAttachments {

    public static final AttachmentRegister.AttachmentRegistry ATTACHMENTS = AttachmentRegister.INSTANCE.of(RuneCraftory.MODID);

    public static final Supplier<AttachmentType<LivingEntity, EntityData>> ENTITY_DATA = ATTACHMENTS.register("entity_data", AttachmentType.builder(EntityData::new));
    public static final Supplier<AttachmentType<Player, PlayerData>> PLAYER_DATA = ATTACHMENTS.register("player_data", AttachmentType.<Player, PlayerData>builder(PlayerData::new)
            .transferHandler(((from, targetHolder, wasDead) -> new PlayerData(targetHolder, from, wasDead))));
}
