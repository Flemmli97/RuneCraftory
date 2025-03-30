package io.github.flemmli97.runecraftory.mixinhelper;

import io.github.flemmli97.tenshilib.client.model.ModelPartHandler;

public interface HumanoidMainHand {

    ModelPartHandler.ModelPartExtended runecraftory$getRightHandItem();

    ModelPartHandler.ModelPartExtended runecraftory$getLeftHandItem();

}
