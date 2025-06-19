package io.github.flemmli97.runecraftory.common.entities.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.npc.job.NPCJob;
import io.github.flemmli97.runecraftory.common.entities.utils.MobAttackExt;
import io.github.flemmli97.runecraftory.common.registry.ModNPCJobs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

public class SyncableDatas {

    public static final StreamCodec<RegistryFriendlyByteBuf, Vec3> VEC3 = new StreamCodec<>() {
        @Override
        public Vec3 decode(RegistryFriendlyByteBuf buffer) {
            return new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, Vec3 value) {
            buffer.writeDouble(value.x());
            buffer.writeDouble(value.y());
            buffer.writeDouble(value.z());
        }
    };

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
    public static final SyncableEntityData.SyncedEntityData<Vec3> MOTION_DIR = SyncableEntityData.register(RuneCraftory.modRes("motion_direction"), VEC3);
    public static final SyncableEntityData.SyncedEntityData<NPCJob> NPC_JOB = SyncableEntityData.register(RuneCraftory.modRes("npc_job"), ByteBufCodecs.registry(ModNPCJobs.JOB_REGISTRY_KEY));
}
