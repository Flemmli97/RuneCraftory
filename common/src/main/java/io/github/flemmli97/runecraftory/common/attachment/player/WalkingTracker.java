package io.github.flemmli97.runecraftory.common.attachment.player;

import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.stats.Stats;

import java.util.HashMap;
import java.util.Map;

public class WalkingTracker {

    private static final Map<ResourceLocation, Float> relevantStats = relevantStatMap();

    private final Map<ResourceLocation, Integer> lastCheck = new HashMap<>();

    private static Map<ResourceLocation, Float> relevantStatMap() {
        Map<ResourceLocation, Float> map = new HashMap<>();
        map.put(Stats.WALK_ONE_CM, 0.8f);
        map.put(Stats.CROUCH_ONE_CM, 1.25f);
        map.put(Stats.SPRINT_ONE_CM, 0.3f);
        map.put(Stats.WALK_ON_WATER_ONE_CM, 0.9f);
        map.put(Stats.WALK_UNDER_WATER_ONE_CM, 1.0f);
        map.put(Stats.SWIM_ONE_CM, 0.65f);
        map.put(Stats.FLY_ONE_CM, 0.15f);
        map.put(Stats.AVIATE_ONE_CM, 0.07f);
        return map;
    }

    public void tickWalkingTracker(ServerPlayer player) {
        ServerStatsCounter counter = player.getStats();
        float mult = 0;
        for (Map.Entry<ResourceLocation, Float> e : relevantStats.entrySet()) {
            mult += this.calcMultiplier(e.getKey(), counter, e.getValue());
        }
        if (mult != 0) {
            float finalMult = mult * 0.5f;
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.WALKING, finalMult));
        }
    }

    private float calcMultiplier(ResourceLocation res, ServerStatsCounter counter, float weight) {
        int last = this.lastCheck.getOrDefault(res, 0);
        int current = counter.getValue(Stats.CUSTOM.get(res));
        if (current > last) {
            float m = Math.min(current - last, 1000) * weight * 0.005f;
            this.lastCheck.put(res, current);
            return m;
        }
        return 0;
    }

    public CompoundTag save() {
        CompoundTag compound = new CompoundTag();
        this.lastCheck.forEach((res, v) -> compound.putInt(res.toString(), v));
        return compound;
    }

    public void read(CompoundTag compound) {
        compound.getAllKeys().forEach(key -> this.lastCheck.put(new ResourceLocation(key), compound.getInt(key)));
    }
}
