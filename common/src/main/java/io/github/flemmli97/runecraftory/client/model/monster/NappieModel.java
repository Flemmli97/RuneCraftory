package io.github.flemmli97.runecraftory.client.model.monster;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.monster.Nappie;
import net.minecraft.resources.ResourceLocation;

public class NappieModel<T extends Nappie> extends PommePommeModel<T> {

    public static final ResourceLocation LOCATION = RuneCraftory.modRes("entity/nappie");

    public NappieModel() {
        super(LOCATION, LOCATION);
    }
}