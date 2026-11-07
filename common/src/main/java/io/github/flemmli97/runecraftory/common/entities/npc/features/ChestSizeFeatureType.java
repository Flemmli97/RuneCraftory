package io.github.flemmli97.runecraftory.common.entities.npc.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCLooks;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

public record ChestSizeFeatureType(
        NumberProvider size) implements NPCFeature.NPCFeatureHolder<ChestSizeFeatureType.SizeFeature> {

    public static final MapCodec<ChestSizeFeatureType> TYPE_CODEC = NumberProviders.CODEC.fieldOf("size").xmap(ChestSizeFeatureType::new, ChestSizeFeatureType::size);
    public static final MapCodec<SizeFeature> CODEC = Codec.FLOAT.fieldOf("size").xmap(SizeFeature::new, SizeFeature::size);
    public static final StreamCodec<ByteBuf, SizeFeature> STREAM_CODEC = ByteBufCodecs.FLOAT.map(SizeFeature::new, f -> f.size);

    public static final float MIN = 0;
    public static final float MAX = 1;

    @Override
    public SizeFeature create(NPCEntity npc) {
        return new SizeFeature(this.size.getFloat(RuneCraftory.createContext(npc)));
    }

    @Override
    public NPCFeatureType<SizeFeature> getType() {
        return RuneCraftoryNPCLooks.CHEST.get();
    }

    public record SizeFeature(float size) implements NPCFeature {

        public SizeFeature(float size) {
            this.size = Mth.clamp(size, MIN, MAX);
        }

        @Override
        public NPCFeatureType<SizeFeature> type() {
            return RuneCraftoryNPCLooks.CHEST.get();
        }
    }
}