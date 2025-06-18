package io.github.flemmli97.runecraftory.mixinhelper;

import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;

public interface HumanoidMainHand {

    ModelPartsContainer.ModelPartExtended runecraftory$getRightHandItem();

    ModelPartsContainer.ModelPartExtended runecraftory$getLeftHandItem();
}
