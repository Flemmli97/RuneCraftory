package io.github.flemmli97.runecraftory.client.model.monster;

import io.github.flemmli97.runecraftory.common.entities.monster.boss.Raccoon;
import io.github.flemmli97.tenshilib.client.model.ExtendedEntityModel;
import io.github.flemmli97.tenshilib.client.model.RideableModel;

public abstract class RaccoonBaseModel<T extends Raccoon> extends ExtendedEntityModel<T> implements RideableModel<T> {

    public RaccoonBaseModel() {
        super();
    }
}
