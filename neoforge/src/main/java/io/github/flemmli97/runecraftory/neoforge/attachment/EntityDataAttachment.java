package io.github.flemmli97.runecraftory.neoforge.attachment;

import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.IAttachmentHolder;

public class EntityDataAttachment extends EntityData {

    public EntityDataAttachment(IAttachmentHolder holder) {
        super(tryCastTo(holder));
    }

    private static LivingEntity tryCastTo(IAttachmentHolder holder) {
        if (holder instanceof LivingEntity entity)
            return entity;
        throw new IllegalStateException("Attachment only supported for entities");
    }
}
