package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.tenshilib.common.attachment.AttachmentType;
import io.github.flemmli97.tenshilib.loader.registry.AttachmentRegister;
import net.minecraft.world.entity.player.Player;

import java.util.function.Function;
import java.util.function.Supplier;

public class RunecraftoryAttachments {

    public static final AttachmentRegister.AttachmentRegistry ATTACHMENTS = AttachmentRegister.INSTANCE.of(RuneCraftory.MODID);

    public static final Supplier<AttachmentType<EntityData>> ENTITY_DATA = ATTACHMENTS.register("entity_data", AttachmentType.builder(castTo(EntityData::new)));
    public static final Supplier<AttachmentType<PlayerData>> PLAYER_DATA = ATTACHMENTS.register("player_data", AttachmentType.builder(RunecraftoryAttachments.<Player, PlayerData>castTo(PlayerData::new))
            .transferHandler(((from, targetHolder, wasDead) -> new PlayerData(tryCastTo(targetHolder), from, wasDead))));

    private static <H, T> Function<Object, T> castTo(Function<H, T> func) {
        return obj -> func.apply(tryCastTo(obj));
    }

    @SuppressWarnings("unchecked")
    private static <T> T tryCastTo(Object holder) {
        try {
            return (T) holder;
        } catch (ClassCastException e) {
            throw new IllegalStateException("Attachment not supported for holder " + holder);
        }
    }
}
