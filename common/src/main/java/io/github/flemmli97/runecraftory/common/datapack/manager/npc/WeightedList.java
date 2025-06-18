package io.github.flemmli97.runecraftory.common.datapack.manager.npc;

import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class WeightedList<T> {

    private int totalWeight;
    private List<WeightedEntry.Wrapper<T>> view = List.of();

    public void setList(List<WeightedEntry.Wrapper<T>> view) {
        this.view = view;
        this.totalWeight = WeightedRandom.getTotalWeight(this.view);
    }

    public T getRandom(RandomSource random, T defaultVal, @Nullable Predicate<T> filter, @Nullable Predicate<T> second) {
        if (this.totalWeight == 0 || this.view.isEmpty())
            return defaultVal;
        List<WeightedEntry.Wrapper<T>> list = this.view;
        if (filter != null) {
            list = this.view.stream().filter(v -> filter.test(v.data())).toList();
        }
        if (second != null) {
            List<WeightedEntry.Wrapper<T>> sub = list.stream().filter(v -> second.test(v.data())).toList();
            if (!sub.isEmpty())
                list = sub;
        }
        return WeightedRandom.getRandomItem(random, list).map(WeightedEntry.Wrapper::data).orElse(defaultVal);
    }
}
