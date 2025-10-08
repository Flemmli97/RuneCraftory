package io.github.flemmli97.runecraftory.common.entities.npc.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.npc.NPCLook;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class NPCFeatureContainer implements Iterable<NPCFeature> {

    private final Map<NPCFeatureType<?>, FeatureData> map = new HashMap<>();
    private final Map<NPCFeatureType<?>, FeatureData> view = Collections.unmodifiableMap(this.map);

    private Set<NPCFeature.NPCFeatureHolder<?>> conditionals = new HashSet<>();

    @SuppressWarnings("unchecked")
    public <T extends NPCFeature> T getFeature(NPCFeatureType<T> type) {
        FeatureData data = this.map.get(type);
        return data != null ? (T) data.getFeature() : null;
    }

    public <T extends NPCFeature> boolean contains(NPCFeatureType<T> type) {
        return this.map.containsKey(type);
    }

    public void buildFromLooks(NPCEntity npc, NPCLook look) {
        if (!(npc.level() instanceof ServerLevel serverLevel))
            return;
        this.map.clear();
        look.additionalFeatures().values().forEach(feat -> this.map.put(feat.getType(), new FeatureData(feat.create(npc))));
        this.conditionals.clear();
        look.conditionalFeatures().forEach(feat -> {
            if (feat.predicate().matches(serverLevel, npc.position(), npc)) {
                this.conditionals.add(feat.feature());
                this.withFeatureOverride(feat.feature().getType(), feat.feature().create(npc));
            }
        });
    }

    public boolean updateLooks(NPCEntity npc, NPCLook look) {
        if (!(npc.level() instanceof ServerLevel serverLevel))
            return false;
        if (npc.tickCount % 20 != 0 || look.conditionalFeatures().isEmpty())
            return false;
        Set<NPCFeature.NPCFeatureHolder<?>> changes = new HashSet<>();
        look.conditionalFeatures().forEach(feat -> {
            if (feat.predicate().matches(serverLevel, npc.position(), npc)) {
                changes.add(feat.feature());
            }
        });
        // Equals is fine cause the holder instances are the same
        if (!changes.equals(this.conditionals)) {
            this.conditionals.forEach(h -> this.withFeatureOverride(h.getType(), null));
            this.conditionals = changes;
            this.conditionals.forEach(feat -> this.withFeatureOverride(feat.getType(), feat.create(npc)));
            return true;
        }
        return false;
    }

    private void withFeatureOverride(NPCFeatureType<?> type, @Nullable NPCFeature override) {
        if (override == null) {
            FeatureData current = this.map.get(type);
            if (current != null) {
                if (current.base == null) {
                    this.map.remove(type);
                } else {
                    current.setOverride(null);
                }
            }
        } else {
            this.map.computeIfAbsent(type, k -> new FeatureData(null))
                    .setOverride(override);
        }
    }

    public Tag save(HolderLookup.Provider provider) {
        return FeatureData.CODEC.listOf().encodeStart(provider.createSerializationContext(NbtOps.INSTANCE),
                List.copyOf(this.map.values())).getOrThrow();
    }

    public NPCFeatureContainer read(Tag tag, HolderLookup.Provider provider) {
        this.map.clear();
        FeatureData.CODEC.listOf().parse(provider.createSerializationContext(NbtOps.INSTANCE), tag)
                .promotePartial(RuneCraftory.LOGGER::error).getOrThrow()
                .forEach(feature -> this.map.put(feature.getFeature().type(), feature));
        return this;
    }

    public void toBuffer(RegistryFriendlyByteBuf buf) {
        buf.writeInt(this.map.size());
        this.map.forEach((type, feat) -> {
            buf.writeBoolean(feat.base != null);
            if (feat.base != null) {
                NPCFeature.STREAM_CODEC.encode(buf, feat.base);
            }
            buf.writeBoolean(feat.override != null);
            if (feat.override != null) {
                NPCFeature.STREAM_CODEC.encode(buf, feat.override);
            }
        });
    }

    public NPCFeatureContainer fromBuffer(RegistryFriendlyByteBuf buf) {
        this.map.clear();
        int size = buf.readInt();
        for (int i = 0; i < size; ++i) {
            FeatureData featureData = new FeatureData(buf.readBoolean() ? NPCFeature.STREAM_CODEC.decode(buf) : null);
            if (buf.readBoolean()) {
                featureData.setOverride(NPCFeature.STREAM_CODEC.decode(buf));
            }
            this.map.put(featureData.getFeature().type(), featureData);
        }
        return this;
    }

    public void with(NPCFeatureContainer other) {
        this.map.clear();
        this.map.putAll(other.map);
    }

    @Override
    public Iterator<NPCFeature> iterator() {
        Iterator<Map.Entry<NPCFeatureType<?>, FeatureData>> it = this.view.entrySet().iterator();
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public NPCFeature next() {
                return it.next().getValue().getFeature();
            }
        };
    }

    private static class FeatureData {

        private static final Codec<FeatureData> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(NPCFeature.FEATURE_CODEC.optionalFieldOf("base").forGetter(d -> Optional.ofNullable(d.base)),
                        NPCFeature.FEATURE_CODEC.optionalFieldOf("override").forGetter(d -> Optional.ofNullable(d.override))
                ).apply(inst, (b, o) -> new FeatureData(b.orElse(null))
                        .setOverride(o.orElse(null))));

        @Nullable
        private final NPCFeature base;
        @Nullable
        private NPCFeature override;

        private FeatureData(@Nullable NPCFeature base) {
            this.base = base;
        }

        public FeatureData setOverride(@Nullable NPCFeature override) {
            this.override = override;
            return this;
        }

        public NPCFeature getFeature() {
            if (this.override != null)
                return this.override;
            return this.base;
        }
    }
}
