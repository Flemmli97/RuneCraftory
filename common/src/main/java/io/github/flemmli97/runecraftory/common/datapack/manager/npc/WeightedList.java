package io.github.flemmli97.runecraftory.common.datapack.manager.npc;


import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class WeightedList<T> {
    private int totalWeight;
    private List<WeightedEntry.Wrapper<T>> view = List.of();

    public void setList(List<WeightedEntry.Wrapper<T>> view) {
        this.view = view;
        this.totalWeight = WeightedRandom.getTotalWeight(this.view);
    }

    public T getRandom(Random random, T defaultVal) {
        if (this.totalWeight == 0 || this.view.isEmpty())
            return defaultVal;
        return WeightedRandom.getRandomItem(random, this.view, this.totalWeight).map(WeightedEntry.Wrapper::getData).orElse(defaultVal);
    }

    public T getRandom(Random random, T defaultVal, Predicate<T> filter) {
        if (this.totalWeight == 0 || this.view.isEmpty())
            return defaultVal;
        return WeightedRandom.getRandomItem(random, this.view.stream().filter(v -> filter.test(v.getData())).toList()).map(WeightedEntry.Wrapper::getData).orElse(defaultVal);
    }
}
