package io.github.flemmli97.runecraftory.forge.attachment;

import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class PlayerDataAttachment extends PlayerData implements INBTSerializable<CompoundTag> {

    public PlayerDataAttachment(IAttachmentHolder player) {
        super(tryCastTo(player));
    }

    private static Player tryCastTo(IAttachmentHolder holder) {
        if (holder instanceof Player player)
            return player;
        throw new IllegalStateException("Attachment only supported for player");
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return this.writeToNBTPlain(new CompoundTag());
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        this.readFromNBT(tag, null);
    }
}