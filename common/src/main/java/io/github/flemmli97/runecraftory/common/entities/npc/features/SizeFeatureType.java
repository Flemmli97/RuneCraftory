package io.github.flemmli97.runecraftory.common.entities.npc.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureHolder;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModNPCLooks;
import io.github.flemmli97.runecraftory.common.utils.CodecHelper;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public record SizeFeatureType(NumberProvider size) implements NPCFeatureHolder<SizeFeatureType.SizeFeature> {

    public static final Codec<SizeFeatureType> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(CodecHelper.NUMER_PROVIDER_CODEC.fieldOf("size").forGetter(d -> d.size)).apply(inst, SizeFeatureType::new));

    public static final float MIN = 0.2F;
    public static final float MAX = 10;

    @Override
    public SizeFeature create(EntityNPCBase npc) {
        return new SizeFeature(this.size.getFloat(RuneCraftory.createContext(npc)));
    }

    @Override
    public NPCFeatureType<SizeFeature> getType() {
        return ModNPCLooks.SIZE.get();
    }

    public static class SizeFeature implements NPCFeature {

        public final float size;

        public SizeFeature(FriendlyByteBuf buf) {
            this(buf.readFloat());
        }

        public SizeFeature(Tag tag) {
            this(((FloatTag) tag).getAsFloat());
        }

        public SizeFeature(float size) {
            this.size = Mth.clamp(size, MIN, MAX);
        }

        @Override
        public void writeToBuffer(FriendlyByteBuf buf) {
            buf.writeFloat(this.size);
        }

        @Override
        public Tag save() {
            return FloatTag.valueOf(this.size);
        }

        @Override
        public NPCFeatureType<SizeFeature> getType() {
            return ModNPCLooks.SIZE.get();
        }

    }
}