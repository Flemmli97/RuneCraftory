package io.github.flemmli97.runecraftory.common.entities.npc.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureHolder;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModNPCLooks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public class BlushFeatureType implements NPCFeatureHolder<BlushFeatureType.BlushFeature> {

    public static Codec<BlushFeatureType> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    Codec.FLOAT.fieldOf("chance").forGetter(d -> d.chance),
                    ColorSetting.CODEC.fieldOf("colors").forGetter(d -> d.setting)
            ).apply(inst, BlushFeatureType::new));

    private final float chance;
    private final ColorSetting setting;

    public BlushFeatureType(float chance, ColorSetting setting) {
        this.chance = chance;
        this.setting = setting;
    }

    @Override
    public BlushFeature create(EntityNPCBase npc) {
        return new BlushFeature(this.setting.getRandom(npc.getRandom()), npc.getRandom().nextFloat() < this.chance);
    }

    @Override
    public NPCFeatureType<BlushFeature> getType() {
        return ModNPCLooks.BLUSH.get();
    }

    public static class BlushFeature implements NPCFeature {

        public final boolean blush;
        public final int color;

        public BlushFeature(FriendlyByteBuf buf) {
            this(buf.readInt(), buf.readBoolean());
        }

        public BlushFeature(Tag tag) {
            this(((CompoundTag) tag).getInt("Color"), ((CompoundTag) tag).getBoolean("Blush"));
        }

        public BlushFeature(int color, boolean blush) {
            this.color = color;
            this.blush = blush;
        }

        @Override
        public void writeToBuffer(FriendlyByteBuf buf) {
            buf.writeInt(this.color);
            buf.writeBoolean(this.blush);
        }

        @Override
        public Tag save() {
            CompoundTag tag = new CompoundTag();
            tag.putInt("Color", this.color);
            tag.putBoolean("Blush", this.blush);
            return tag;
        }

        @Override
        public NPCFeatureType<?> getType() {
            return ModNPCLooks.BLUSH.get();
        }
    }
}
