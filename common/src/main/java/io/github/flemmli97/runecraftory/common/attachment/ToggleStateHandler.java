package io.github.flemmli97.runecraftory.common.attachment;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class ToggleStateHandler {

    private final Mob mob;
    private final Consumer<Boolean> onToggle;

    private boolean defaultedValue;
    private final Set<ResourceLocation> disabledSources = new HashSet<>();

    private boolean updating;

    public ToggleStateHandler(Mob mob, boolean defaultedValue, Consumer<Boolean> onToggle) {
        this.mob = mob;
        this.defaultedValue = defaultedValue;
        this.onToggle = onToggle;
    }

    public void updateDefaultedValue(boolean value) {
        if (this.mob.level().isClientSide || this.updating)
            return;
        this.defaultedValue = value;
    }

    public void addSource(ResourceLocation source) {
        if (this.mob.level().isClientSide)
            return;
        this.disabledSources.add(source);
        this.updateState();
    }

    public void removeSource(ResourceLocation source) {
        if (this.mob.level().isClientSide)
            return;
        this.disabledSources.remove(source);
        this.updateState();
    }

    private void updateState() {
        this.updating = true;
        if (!this.disabledSources.isEmpty()) {
            this.onToggle.accept(true);
        } else {
            this.onToggle.accept(this.defaultedValue);
        }
        this.updating = false;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        ListTag sources = new ListTag();
        this.disabledSources.forEach(res -> sources.add(StringTag.valueOf(res.toString())));
        tag.put("Sources", sources);
        tag.putBoolean("Defaulted", this.defaultedValue);
        return tag;
    }

    public void read(CompoundTag tag) {
        this.disabledSources.clear();
        ListTag sources = tag.getList("Sources", Tag.TAG_STRING);
        sources.forEach(t -> this.disabledSources.add(ResourceLocation.parse(t.getAsString())));
        this.defaultedValue = tag.getBoolean("Defaulted");
        this.updateState();
    }
}
