package io.github.flemmli97.runecraftory.common.entities.npc.features;

import com.mojang.serialization.Codec;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureHolder;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModNPCLooks;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.List;

public record SimpleHatFeatureType(
        List<String> hats) implements NPCFeatureHolder<SimpleHatFeatureType.SimpleHatFeature> {

    public static final Codec<SimpleHatFeatureType> CODEC = Codec.STRING.listOf().xmap(SimpleHatFeatureType::new, SimpleHatFeatureType::hats);

    @Override
    public SimpleHatFeature create(EntityNPCBase npc) {
        return new SimpleHatFeature(this.hats.isEmpty() ? "" : this.hats.get(npc.getRandom().nextInt(this.hats.size())));
    }

    @Override
    public NPCFeatureType<SimpleHatFeature> getType() {
        return ModNPCLooks.HAT.get();
    }

    public static class SimpleHatFeature implements NPCFeature {

        public final String hat;

        public SimpleHatFeature(FriendlyByteBuf buf) {
            this(buf.readUtf());
        }

        public SimpleHatFeature(Tag tag) {
            this(tag.getAsString());
        }

        public SimpleHatFeature(String hat) {
            this.hat = hat;
        }

        @Override
        public void writeToBuffer(FriendlyByteBuf buf) {
            buf.writeUtf(this.hat);
        }

        @Override
        public Tag save() {
            return StringTag.valueOf(this.hat);
        }

        @Override
        public NPCFeatureType<SimpleHatFeature> getType() {
            return ModNPCLooks.HAT.get();
        }
    }
}