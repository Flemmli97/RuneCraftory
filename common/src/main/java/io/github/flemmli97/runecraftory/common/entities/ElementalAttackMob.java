package io.github.flemmli97.runecraftory.common.entities;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;

public interface ElementalAttackMob {

    EnumElement getAttackElement(AnimatedAction anim);
}
