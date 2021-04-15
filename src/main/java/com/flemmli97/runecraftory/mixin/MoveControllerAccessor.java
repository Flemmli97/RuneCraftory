package com.flemmli97.runecraftory.mixin;

import net.minecraft.entity.ai.controller.MovementController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MovementController.class)
public interface MoveControllerAccessor {

    @Accessor("action")
    MovementController.Action getAction();

}
