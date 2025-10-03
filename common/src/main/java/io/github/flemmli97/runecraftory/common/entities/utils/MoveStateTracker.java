package io.github.flemmli97.runecraftory.common.entities.utils;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Supplier;

public class MoveStateTracker {

    private final LivingEntity entity;
    private final int transitionTime;
    private final EntityDataAccessor<Byte> moveFlagData;
    private final Supplier<MoveType> calculateState;
    private final int[] states = new int[MoveType.values().length];
    private int genericMoveTick;

    public MoveStateTracker(LivingEntity entity, int transitionTime, EntityDataAccessor<Byte> moveFlagData, Supplier<MoveType> calculateState) {
        this.entity = entity;
        this.transitionTime = transitionTime;
        this.moveFlagData = moveFlagData;
        this.calculateState = calculateState;
    }

    private MoveType getCurrent() {
        return MoveType.values()[this.entity.getEntityData().get(this.moveFlagData)];
    }

    public void tick() {
        if (!this.entity.level().isClientSide) {
            MoveType moveType = this.calculateState.get();
            switch (moveType) {
                case NONE -> {
                    this.entity.setShiftKeyDown(false);
                    this.entity.setSprinting(false);
                }
                case SNEAK -> this.entity.setShiftKeyDown(false);
                case RUN -> this.entity.setSprinting(false);
            }
            this.entity.getEntityData().set(this.moveFlagData, (byte) moveType.ordinal());
        }
        MoveType current = this.getCurrent();
        for (int i = 0; i < this.states.length; i++) {
            if (i == current.ordinal()) {
                this.states[i] = Math.min(this.transitionTime, ++this.states[i]);
            } else {
                this.states[i] = Math.max(-1, --this.states[i]);
            }
        }
        if (current != MoveType.NONE) {
            this.genericMoveTick = Math.min(this.transitionTime, ++this.genericMoveTick);
        } else {
            this.genericMoveTick = Math.max(-1, --this.genericMoveTick);
        }
    }

    public float interpolatedMoveTick(float partialTicks) {
        if (this.genericMoveTick == -1)
            return 0;
        float speedMod = Mth.clamp(this.entity.walkAnimation.speed(partialTicks) / 0.25f, 0, 1);
        MoveType current = this.getCurrent();
        return Mth.clamp((this.genericMoveTick + (current != MoveType.NONE ? partialTicks : -partialTicks)) / (float) this.transitionTime, 0, 1) * speedMod;
    }

    public float interpolatedMoveTickOf(MoveType moveType, float partialTicks) {
        int tick = this.states[moveType.ordinal()];
        if (tick == -1)
            return 0;
        float speedMod = moveType.speedDependent ? Mth.clamp(this.entity.walkAnimation.speed(partialTicks) / 0.25f, 0, 1) : 1;
        MoveType current = this.getCurrent();
        return Mth.clamp((tick + (current == moveType ? partialTicks : -partialTicks)) / (float) this.transitionTime, 0, 1) * speedMod;
    }
}
