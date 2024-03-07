package io.github.flemmli97.runecraftory.common.config;

import io.github.flemmli97.tenshilib.api.config.IConfigListValue;
import io.github.flemmli97.tenshilib.common.utils.SearchUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class DistanceZoningConfig implements IConfigListValue<DistanceZoningConfig> {

    private static final Pair<Float, Zone> DEFAULT_VAL = Pair.of(0f, new Zone(1, 0.01f));
    private final List<Pair<Float, Zone>> vals = new ArrayList<>(List.of(
            Pair.of(0f, new Zone(1, 0)),
            Pair.of(300f, new Zone(1, 25 / 2000f)),
            Pair.of(2300f, new Zone(25, 80 / 8000f)),
            Pair.of(8300f, new Zone(105, 0.015f))
    ));

    @Override
    public DistanceZoningConfig readFromString(List<String> ss) {
        this.vals.clear();
        List<Pair<Float, Zone>> list = new ArrayList<>();
        for (String s : ss) {
            String[] parts = s.split("-");
            if (parts.length != 3)
                continue;
            list.add(Pair.of(Float.parseFloat(parts[0]), new Zone((int) Float.parseFloat(parts[1]), Float.parseFloat(parts[2]))));
        }
        list.sort((o1, o2) -> Float.compare(o1.getLeft(), o2.getLeft()));
        this.vals.addAll(list);
        return this;
    }

    @Override
    public List<String> writeToString() {
        List<String> list = new ArrayList<>();
        this.vals.forEach(v -> list.add(v.getLeft() + "-" + v.getRight().start + "-" + v.getRight().increasePerBlock));
        return list;
    }

    public Pair<Float, Zone> get(float dist) {
        return SearchUtils.searchInfFunc(this.vals, v -> Float.compare(v.getLeft(), dist), DEFAULT_VAL);
    }

    public record Zone(int start, float increasePerBlock) {
    }
}