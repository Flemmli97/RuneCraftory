package io.github.flemmli97.runecraftory.common.entities.npc.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.RandomSource;

import java.util.List;

public record IndexedColorSetting(List<Integer> indices, ColorSetting color) {

    public static final Codec<IndexedColorSetting> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    Codec.INT.listOf().fieldOf("indices").forGetter(d -> d.indices),
                    ColorSetting.CODEC.fieldOf("colors").forGetter(d -> d.color)
            ).apply(inst, IndexedColorSetting::new));

    public ResolvedIndexColor resolve(RandomSource random) {
        int index = this.indices.isEmpty() ? 0 : this.indices.get(random.nextInt(this.indices.size()));
        return new ResolvedIndexColor(index, this.color.getRandom(random));
    }

    public record ResolvedIndexColor(int index, int color) {

        public ResolvedIndexColor(FriendlyByteBuf buf) {
            this(buf.readInt(), buf.readInt());
        }

        public ResolvedIndexColor(CompoundTag tag) {
            this(tag.getInt("Type"), tag.getInt("Color"));
        }

        public void writeToBuffer(FriendlyByteBuf buf) {
            buf.writeInt(this.index);
            buf.writeInt(this.color);
        }

        public CompoundTag save() {
            CompoundTag tag = new CompoundTag();
            tag.putInt("Index", this.index);
            tag.putInt("Color", this.color);
            return tag;
        }
    }
}
