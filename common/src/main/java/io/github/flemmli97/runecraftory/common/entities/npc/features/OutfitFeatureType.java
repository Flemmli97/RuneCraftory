package io.github.flemmli97.runecraftory.common.entities.npc.features;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureHolder;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModNPCLooks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public record OutfitFeatureType(TypedIndexRange types) implements NPCFeatureHolder<OutfitFeatureType.OutfitFeature> {

    public static final Codec<OutfitFeatureType> CODEC = TypedIndexRange.CODEC.fieldOf("outfits").xmap(OutfitFeatureType::new, OutfitFeatureType::types).codec();

    @Override
    public OutfitFeature create(EntityNPCBase npc) {
        Pair<String, Integer> style = this.types.getRandom(npc.getRandom());
        return new OutfitFeature(style.getFirst(), style.getSecond());
    }

    @Override
    public NPCFeatureType<OutfitFeature> getType() {
        return ModNPCLooks.OUTFIT.get();
    }

    public static class OutfitFeature implements NPCFeature {

        public final String type;
        public final int index;

        public OutfitFeature(FriendlyByteBuf buf) {
            this(buf.readUtf(), buf.readInt());
        }

        public OutfitFeature(Tag tag) {
            this(((CompoundTag) tag).getString("Type"), ((CompoundTag) tag).getInt("Index"));
        }

        public OutfitFeature(String type, int index) {
            this.type = type;
            this.index = index;
        }

        @Override
        public void writeToBuffer(FriendlyByteBuf buf) {
            buf.writeUtf(this.type);
            buf.writeInt(this.index);
        }

        @Override
        public Tag save() {
            CompoundTag tag = new CompoundTag();
            tag.putString("Type", this.type);
            tag.putInt("Index", this.index);
            return tag;
        }

        @Override
        public NPCFeatureType<OutfitFeature> getType() {
            return ModNPCLooks.OUTFIT.get();
        }
    }
}