package io.github.flemmli97.runecraftory.common.entities.npc;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.BooleanSupplier;

public class EyeState {

    public static final int INTERPOLATION = 2;

    private final LivingEntity entity;
    private final BooleanSupplier shouldCloseEyes;

    private boolean closing;
    private int blinkTick, progress;

    public EyeState(LivingEntity entity, BooleanSupplier shouldCloseEyes) {
        this.entity = entity;
        this.shouldCloseEyes = shouldCloseEyes;
    }

    public void tick() {
        boolean shouldCloseEyes = this.shouldCloseEyes.getAsBoolean();
        if (this.blinkTick > 0 || shouldCloseEyes) {
            this.closing = true;
            this.progress = Math.min(this.progress + 1, INTERPOLATION);
        } else {
            this.closing = false;
            this.progress = Math.max(this.progress - 1, -1);
        }
        this.blinkTick--;
        if (this.blinkTick < -60 && this.entity.getRandom().nextInt(75) == 0) {
            this.blinkTick = 5;
        }
    }

    public float progress(float partialTicks) {
        if (this.progress == -1)
            return 0;
        partialTicks = this.closing ? partialTicks : -partialTicks;
        return Mth.clamp((this.progress + partialTicks) / INTERPOLATION, 0, 1);
    }
}
