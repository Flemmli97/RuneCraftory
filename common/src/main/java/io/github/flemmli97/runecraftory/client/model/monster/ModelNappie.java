package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityNappie;
import net.minecraft.resources.ResourceLocation;

public class ModelNappie<T extends EntityNappie> extends ModelPommePomme<T> {

    public static final ResourceLocation LOCATION = RuneCraftory.modRes("nappie");

    public ModelNappie() {
        super(LOCATION, LOCATION);
    }
}