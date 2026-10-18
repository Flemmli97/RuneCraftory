package io.github.flemmli97.runecraftory.common.entities.ai.memory;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;

public record MeetingPoint(GlobalPos pos, ResourceKey<PoiType> poi, boolean aquired) {

    public static final Codec<MeetingPoint> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(GlobalPos.CODEC.fieldOf("pos").forGetter(MeetingPoint::pos),
                    ResourceKey.codec(Registries.POINT_OF_INTEREST_TYPE).fieldOf("poi").forGetter(MeetingPoint::poi),
                    Codec.BOOL.fieldOf("aquired").forGetter(MeetingPoint::aquired)
            ).apply(inst, MeetingPoint::new));

}
