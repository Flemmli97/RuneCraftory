package io.github.flemmli97.runecraftory.common.attachment.player;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class QuestTracker {

    private final Map<ResourceLocation, Map<String, Integer>> tamingCounter = new HashMap<>();
    private final Map<ResourceLocation, Map<String, Integer>> shippingCounter = new HashMap<>();

    public void onComplete(ResourceLocation quest, String task, TrackedTypes type) {
        switch (type) {
            case SHIPPING -> {
                Map<String, Integer> counter = this.shippingCounter.get(quest);
                if (counter != null) {
                    counter.remove(task);
                    if (counter.isEmpty())
                        this.shippingCounter.remove(quest);
                }
            }
            case TAMING -> {
                Map<String, Integer> counter = this.tamingCounter.get(quest);
                if (counter != null) {
                    counter.remove(task);
                    if (counter.isEmpty())
                        this.tamingCounter.remove(quest);
                }
            }
        }
    }

    public int getTrackedAndIncrease(ResourceLocation quest, String task, TrackedTypes type, int increase) {
        return switch (type) {
            case SHIPPING -> this.shippingCounter.computeIfAbsent(quest, key -> new HashMap<>())
                    .compute(task, (key, old) -> old == null ? increase : old + increase);
            case TAMING -> this.tamingCounter.computeIfAbsent(quest, key -> new HashMap<>())
                    .compute(task, (key, old) -> old == null ? increase : old + increase);
        };
    }

    public void load(CompoundTag tag) {
        CompoundTag shipping = tag.getCompound("ShippingTracker");
        shipping.getAllKeys().forEach(key -> {
            ResourceLocation quest = new ResourceLocation(key);
            Map<String, Integer> map = new HashMap<>();
            CompoundTag tasks = shipping.getCompound(key);
            tasks.getAllKeys().forEach(taskName -> map.put(taskName, tasks.getInt(taskName)));
            this.shippingCounter.put(quest, map);
        });
        CompoundTag taming = tag.getCompound("TamingTracker");
        taming.getAllKeys().forEach(key -> {
            ResourceLocation quest = new ResourceLocation(key);
            Map<String, Integer> map = new HashMap<>();
            CompoundTag tasks = taming.getCompound(key);
            tasks.getAllKeys().forEach(taskName -> map.put(taskName, tasks.getInt(taskName)));
            this.tamingCounter.put(quest, map);
        });
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        CompoundTag shipping = new CompoundTag();
        this.shippingCounter.forEach((res, map) -> {
            CompoundTag tasks = new CompoundTag();
            map.forEach(tasks::putInt);
            shipping.put(res.toString(), tasks);
        });
        tag.put("ShippingTracker", shipping);
        CompoundTag taming = new CompoundTag();
        this.tamingCounter.forEach((res, map) -> {
            CompoundTag tasks = new CompoundTag();
            map.forEach(tasks::putInt);
            taming.put(res.toString(), tasks);
        });
        tag.put("TamingTracker", taming);
        return tag;
    }

    public enum TrackedTypes {
        SHIPPING,
        TAMING
    }
}
