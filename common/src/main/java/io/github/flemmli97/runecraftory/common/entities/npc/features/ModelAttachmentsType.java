package io.github.flemmli97.runecraftory.common.entities.npc.features;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCLooks;
import io.github.flemmli97.runecraftory.common.utils.StreamCodecUtils;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public record ModelAttachmentsType(
        List<ModelAttachment> attachments) implements NPCFeature.NPCFeatureHolder<ModelAttachmentsType.ModelAttachmentsFeature> {

    public static final MapCodec<ModelAttachmentsType> TYPE_CODEC = ModelAttachment.CODEC.listOf().fieldOf("attachments")
            .xmap(ModelAttachmentsType::new, ModelAttachmentsType::attachments);
    public static MapCodec<ModelAttachmentsFeature> CODEC = ModelAttachment.CODEC.listOf().fieldOf("attachments")
            .xmap(ModelAttachmentsFeature::new, ModelAttachmentsFeature::attachments);
    public static final StreamCodec<ByteBuf, ModelAttachmentsFeature> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.collection(ArrayList::new, ModelAttachment.STREAM_CODEC), ModelAttachmentsFeature::attachments,
            ModelAttachmentsFeature::new);

    @Override
    public ModelAttachmentsFeature create(NPCEntity npc) {
        if (this.attachments().isEmpty())
            throw new IllegalStateException("No models defined!");
        return new ModelAttachmentsFeature(List.copyOf(this.attachments()));
    }

    @Override
    public NPCFeatureType<ModelAttachmentsFeature> getType() {
        return RuneCraftoryNPCLooks.MODEL_ATTACHMENT.get();
    }

    public static class ModelAttachmentsFeature implements NPCFeature {

        private final List<ModelAttachment> attachments;

        private Set<Location> hidden;

        public ModelAttachmentsFeature(List<ModelAttachment> attachments) {
            this.attachments = attachments;
        }

        public List<ModelAttachment> attachments() {
            return this.attachments;
        }

        public Set<Location> hidden() {
            if (this.hidden == null) {
                EnumSet<Location> set = EnumSet.noneOf(Location.class);
                this.attachments().forEach(attachment -> set.addAll(attachment.hidden()));
                this.hidden = Sets.immutableEnumSet(set);
            }
            return this.hidden;
        }

        @Override
        public NPCFeatureType<ModelAttachmentsFeature> type() {
            return RuneCraftoryNPCLooks.MODEL_ATTACHMENT.get();
        }
    }

    public record ModelAttachment(ResourceLocation model, ResourceLocation texture,
                                  Location location, Set<Location> hidden) {

        public static Codec<ModelAttachment> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(ResourceLocation.CODEC.fieldOf("model").forGetter(ModelAttachment::model),
                                ResourceLocation.CODEC.fieldOf("texture").forGetter(ModelAttachment::texture),
                                CodecUtils.stringEnumCodec(Location.class, null).fieldOf("location").forGetter(ModelAttachment::location),
                                CodecUtils.stringEnumCodec(Location.class, null).listOf().fieldOf("hidden").forGetter(d -> List.copyOf(d.hidden())))
                        .apply(instance, (model, texture, location, hidden) -> new ModelAttachment(model, texture, location, Set.copyOf(hidden))));
        public static final StreamCodec<ByteBuf, ModelAttachment> STREAM_CODEC = StreamCodec.composite(ResourceLocation.STREAM_CODEC, ModelAttachment::model,
                ResourceLocation.STREAM_CODEC, ModelAttachment::texture,
                StreamCodecUtils.ofEnum(Location.class), ModelAttachment::location,
                ByteBufCodecs.collection(i -> EnumSet.noneOf(Location.class), StreamCodecUtils.ofEnum(Location.class)), ModelAttachment::hidden,
                ModelAttachment::new);

    }

    public enum Location {
        HEAD,
        BODY,
        LEFT_ARM,
        RIGHT_ARM,
        LEGS,
        LEFT_LEG,
        RIGHT_LEG
    }
}