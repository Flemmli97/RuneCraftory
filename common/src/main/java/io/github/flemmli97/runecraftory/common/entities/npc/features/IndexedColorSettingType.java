package io.github.flemmli97.runecraftory.common.entities.npc.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureHolder;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class IndexedColorSettingType implements NPCFeatureHolder<IndexedColorSettingType.IndexedColorFeature> {

    public static final Function<Supplier<NPCFeatureType<IndexedColorSettingType.IndexedColorFeature>>, Codec<IndexedColorSettingType>> CODEC = type -> RecordCodecBuilder.create(inst ->
            inst.group(
                    Codec.INT.listOf().fieldOf("indices").forGetter(d -> d.indices),
                    ColorSetting.CODEC.fieldOf("colors").forGetter(d -> d.color)
            ).apply(inst, (indices, color) -> new IndexedColorSettingType(indices, color, type)));

    private final List<Integer> indices;
    private final ColorSetting color;
    private final Supplier<NPCFeatureType<IndexedColorSettingType.IndexedColorFeature>> type;

    public IndexedColorSettingType(List<Integer> indices, ColorSetting setting, Supplier<NPCFeatureType<IndexedColorSettingType.IndexedColorFeature>> type) {
        this.indices = indices;
        this.color = setting;
        this.type = type;
    }

    @Override
    public IndexedColorSettingType.IndexedColorFeature create(EntityNPCBase npc) {
        int index = this.indices.isEmpty() ? 0 : this.indices.get(npc.getRandom().nextInt(this.indices.size()));
        return new IndexedColorSettingType.IndexedColorFeature(index, this.color.getRandom(npc.getRandom())) {
            @Override
            public NPCFeatureType<?> getType() {
                return IndexedColorSettingType.this.type.get();
            }
        };
    }

    @Override
    public NPCFeatureType<IndexedColorSettingType.IndexedColorFeature> getType() {
        return this.type.get();
    }

    public static NPCFeatureType<IndexedColorFeature> createSimple(Supplier<NPCFeatureType<IndexedColorFeature>> type) {
        return new NPCFeatureType<>(CODEC.apply(type),
                buf -> new IndexedColorFeature(buf) {
                    @Override
                    public NPCFeatureType<?> getType() {
                        return type.get();
                    }
                },
                tag -> new IndexedColorFeature(tag) {
                    @Override
                    public NPCFeatureType<?> getType() {
                        return type.get();
                    }
                });
    }

    public static abstract class IndexedColorFeature implements NPCFeature {

        public final int index, color;

        public IndexedColorFeature(FriendlyByteBuf buf) {
            this(buf.readInt(), buf.readInt());
        }

        public IndexedColorFeature(Tag tag) {
            this(((CompoundTag) tag).getInt("Type"), ((CompoundTag) tag).getInt("Color"));
        }

        public IndexedColorFeature(int index, int color) {
            this.index = index;
            this.color = color;
        }

        @Override
        public void writeToBuffer(FriendlyByteBuf buf) {
            buf.writeInt(this.index);
            buf.writeInt(this.color);
        }

        @Override
        public Tag save() {
            CompoundTag tag = new CompoundTag();
            tag.putInt("Index", this.index);
            tag.putInt("Color", this.color);
            return tag;
        }
    }
}