package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityPommePomme;
import net.minecraft.resources.ResourceLocation;

public class ModelMino<T extends EntityPommePomme> extends ModelPommePomme<T> {

    public static final ResourceLocation LOCATION = RuneCraftory.modRes("mino");

    public ModelMino() {
        super(LOCATION, ModelPommePomme.LOCATION);
    }
}