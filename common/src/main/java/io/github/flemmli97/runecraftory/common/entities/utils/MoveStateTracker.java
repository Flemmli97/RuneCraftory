package io.github.flemmli97.runecraftory.common.entities.utils;

import net.minecraft.util.Mth;

import java.util.function.Supplier;

public class MoveStateTracker {

    private final int transitionTime;
    private final Supplier<MoveType> currentState;
    private final int[] states = new int[MoveType.values().length];
    private int genericMoveTick;

    public MoveStateTracker(int transitionTime, Supplier<MoveType> currentState) {
        this.transitionTime = transitionTime;
        this.currentState = currentState;
    }

    public void tick() {
        MoveType current = this.currentState.get();
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

    public float interpolatedMoveTickOf(MoveType moveType, float partialTicks) {
        int tick = this.states[moveType.ordinal()];
        if (tick == -1)
            return 0;
        MoveType current = this.currentState.get();
        return Mth.clamp((tick + (current == moveType ? partialTicks : -partialTicks)) / (float) this.transitionTime, 0, 1);
    }

    public float interpolatedMoveTick(float partialTicks) {
        if (this.genericMoveTick == -1)
            return 0;
        MoveType current = this.currentState.get();
        return Mth.clamp((this.genericMoveTick + (current != MoveType.NONE ? partialTicks : -partialTicks)) / (float) this.transitionTime, 0, 1);
    }
}
