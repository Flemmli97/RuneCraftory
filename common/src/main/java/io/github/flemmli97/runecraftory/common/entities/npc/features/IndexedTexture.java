package io.github.flemmli97.runecraftory.common.entities.npc.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public record IndexedTexture(ResourceLocation texture, Optional<Integer> index) {

    public static MapCodec<IndexedTexture> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(ResourceLocation.CODEC.fieldOf("texture").forGetter(IndexedTexture::texture),
                    Codec.INT.optionalFieldOf("index").forGetter(IndexedTexture::index)
            ).apply(inst, IndexedTexture::new));

    public static final StreamCodec<ByteBuf, IndexedTexture> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, IndexedTexture::texture,
            ByteBufCodecs.optional(ByteBufCodecs.INT), IndexedTexture::index,
            IndexedTexture::new);

    public String indexStr() {
        if (this.index().isPresent()) {
            return this.index().get().toString();
        }
        return "";
    }

}
