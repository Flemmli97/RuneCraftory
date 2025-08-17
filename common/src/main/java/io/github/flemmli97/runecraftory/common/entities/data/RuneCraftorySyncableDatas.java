package io.github.flemmli97.runecraftory.common.entities.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.utils.MobAttackExt;
import io.github.flemmli97.tenshilib.common.entity.data.SyncableEntityData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

public class RuneCraftorySyncableDatas {

    public static final StreamCodec<RegistryFriendlyByteBuf, MobAttackExt.TargetPosition> TARGET_POSITION = new StreamCodec<>() {
        @Override
        public MobAttackExt.TargetPosition decode(RegistryFriendlyByteBuf buffer) {
            return new MobAttackExt.TargetPosition(new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble()),
                    buffer.readDouble(), buffer.readDouble());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, MobAttackExt.TargetPosition value) {
            buffer.writeDouble(value.position().x());
            buffer.writeDouble(value.position().y());
            buffer.writeDouble(value.position().z());
            buffer.writeDouble(value.minHeight());
            buffer.writeDouble(value.maxHeight());
        }
    };

    public static final SyncableEntityData.SyncedEntityData<MobAttackExt.TargetPosition> TARGET_POS = SyncableEntityData.register(RuneCraftory.modRes("target_position"), TARGET_POSITION);
}
