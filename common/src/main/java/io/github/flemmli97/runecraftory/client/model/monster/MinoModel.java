package io.github.flemmli97.runecraftory.client.model.monster;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.monster.PommePomme;
import net.minecraft.resources.ResourceLocation;

public class MinoModel<T extends PommePomme> extends PommePommeModel<T> {

    public static final ResourceLocation LOCATION = RuneCraftory.modRes("entity/mino");

    public MinoModel() {
        super(LOCATION, PommePommeModel.LOCATION);
    }
}