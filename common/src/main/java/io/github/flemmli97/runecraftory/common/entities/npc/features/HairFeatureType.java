package io.github.flemmli97.runecraftory.common.entities.npc.features;

import com.mojang.datafixers.util.Pair;
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

public record HairFeatureType(TypedIndexRange types,
                              ColorSetting color) implements NPCFeatureHolder<HairFeatureType.HairFeature> {

    public static final Codec<HairFeatureType> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    TypedIndexRange.CODEC.fieldOf("styles").forGetter(d -> d.types),
                    ColorSetting.CODEC.fieldOf("colors").forGetter(d -> d.color)
            ).apply(inst, HairFeatureType::new));

    @Override
    public HairFeature create(EntityNPCBase npc) {
        Pair<String, Integer> style = this.types.getRandom(npc.getRandom());
        return new HairFeature(style.getFirst(), style.getSecond(), this.color.getRandom(npc.getRandom()));
    }

    @Override
    public NPCFeatureType<HairFeature> getType() {
        return ModNPCLooks.HAIR.get();
    }

    public static class HairFeature implements NPCFeature {

        public final String type;
        public final int index, color;

        public HairFeature(FriendlyByteBuf buf) {
            this(buf.readUtf(), buf.readInt(), buf.readInt());
        }

        public HairFeature(Tag tag) {
            this(((CompoundTag) tag).getString("Type"), ((CompoundTag) tag).getInt("Index"), ((CompoundTag) tag).getInt("Color"));
        }

        public HairFeature(String type, int index, int color) {
            this.type = type;
            this.index = index;
            this.color = color;
        }

        @Override
        public void writeToBuffer(FriendlyByteBuf buf) {
            buf.writeUtf(this.type);
            buf.writeInt(this.index);
            buf.writeInt(this.color);
        }

        @Override
        public Tag save() {
            CompoundTag tag = new CompoundTag();
            tag.putString("Type", this.type);
            tag.putInt("Index", this.index);
            tag.putInt("Color", this.color);
            return tag;
        }

        @Override
        public NPCFeatureType<HairFeature> getType() {
            return ModNPCLooks.HAIR.get();
        }
    }
}