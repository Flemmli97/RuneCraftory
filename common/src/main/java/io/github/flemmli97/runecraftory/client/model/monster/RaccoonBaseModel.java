package io.github.flemmli97.runecraftory.client.model.monster;

import io.github.flemmli97.runecraftory.common.entities.monster.boss.Raccoon;
import io.github.flemmli97.tenshilib.client.model.ExtendedModel;
import io.github.flemmli97.tenshilib.client.model.RideableModel;
import net.minecraft.client.model.EntityModel;

public abstract class RaccoonBaseModel<T extends Raccoon> extends EntityModel<T> implements ExtendedModel, RideableModel<T> {

    public RaccoonBaseModel() {
        super();
    }
}
