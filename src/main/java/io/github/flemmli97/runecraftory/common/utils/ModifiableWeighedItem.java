package io.github.flemmli97.runecraftory.common.utils;

import net.minecraft.util.WeightedRandom;

public class ModifiableWeighedItem<T> extends WeightedRandom.Item {


    public ModifiableWeighedItem(int weight, int bonus, T item) {
        super(weight);
    }
}
