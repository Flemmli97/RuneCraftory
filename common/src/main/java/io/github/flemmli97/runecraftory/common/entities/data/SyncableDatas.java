package io.github.flemmli97.runecraftory.common.entities.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.utils.MobAttackExt;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class SyncableDatas {

    public static final EntityDataSerializer<Vec3> VEC3 = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buffer, Vec3 value) {
            buffer.writeDouble(value.x());
            buffer.writeDouble(value.y());
            buffer.writeDouble(value.z());
        }

        @Override
        public Vec3 read(FriendlyByteBuf buffer) {
            return new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        }

        @Override
        public Vec3 copy(Vec3 value) {
            return new Vec3(value.x(), value.y(), value.z());
        }
    };

    public static final EntityDataSerializer<MobAttackExt.TargetPosition> TARGET_POSITION = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buffer, MobAttackExt.TargetPosition value) {
            buffer.writeDouble(value.position().x());
            buffer.writeDouble(value.position().y());
            buffer.writeDouble(value.position().z());
            buffer.writeDouble(value.minHeight());
            buffer.writeDouble(value.maxHeight());
        }

        @Override
        public MobAttackExt.TargetPosition read(FriendlyByteBuf buffer) {
            return new MobAttackExt.TargetPosition(new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble()),
                    buffer.readDouble(), buffer.readDouble());
        }

        @Override
        public MobAttackExt.TargetPosition copy(MobAttackExt.TargetPosition value) {
            return new MobAttackExt.TargetPosition(value.position(), value.minHeight(), value.maxHeight());
        }
    };

    public static final SyncableEntityData.SyncedEntityData<MobAttackExt.TargetPosition> TARGET_POS = SyncableEntityData.register(new ResourceLocation(RuneCraftory.MODID, "target_position"), TARGET_POSITION);
    public static final SyncableEntityData.SyncedEntityData<Vec3> MOTION_DIR = SyncableEntityData.register(new ResourceLocation(RuneCraftory.MODID, "motion_direction"), VEC3);

}
