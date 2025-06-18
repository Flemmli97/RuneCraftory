package io.github.flemmli97.runecraftory.common.entities.npc.job;

import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModNPCJobs;
import io.github.flemmli97.runecraftory.common.registry.ModPoiTypes;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class NPCJob {

    public final boolean hasShop, hasSchedule, hasWorkSchedule;

    public final ResourceKey<PoiType> poiType;
    @Nullable
    public final Predicate<Holder<PoiType>> predicate;

    private String translationKey;

    public NPCJob(NPCJob.Builder builder) {
        this.hasShop = builder.hasShop;
        this.hasSchedule = builder.hasSchedule;
        this.hasWorkSchedule = builder.hasWorkSchedule;
        this.poiType = builder.poiType;
        if (this.poiType != null) {
            if (!builder.allowCashPOI || this.poiType == ModPoiTypes.CASH_REGISTER)
                this.predicate = t -> t.is(this.poiType);
            else
                this.predicate = t -> t.is(this.poiType) || t.is(ModPoiTypes.CASH_REGISTER.getKey());
        } else
            this.predicate = null;
    }

    public boolean hasShop(EntityNPCBase npc, Player player) {
        return this.hasShop;
    }

    public void handleAction(EntityNPCBase npc, Player player, String action) {
    }

    public Map<String, List<Component>> actions(EntityNPCBase entity, ServerPlayer player) {
        return Map.of();
    }

    public String getTranslationKey() {
        if (this.translationKey == null)
            this.translationKey = Util.makeDescriptionId("npc.job", ModNPCJobs.JOBS.registry().getKey(this));
        return this.translationKey;
    }

    public static class Builder {

        private final ResourceKey<PoiType> poiType;

        private boolean allowCashPOI = true;
        private boolean hasShop = true;
        private boolean hasSchedule = true;
        private boolean hasWorkSchedule = true;

        public Builder(ResourceKey<PoiType> poiType) {
            this.poiType = poiType;
        }

        public Builder ignoreCashRegisterPOI() {
            this.allowCashPOI = false;
            return this;
        }

        public Builder noShop() {
            this.hasShop = false;
            return this;
        }

        public Builder noSchedule() {
            this.hasSchedule = false;
            this.hasWorkSchedule = false;
            return this;
        }

        public Builder noWorkSchedule() {
            this.hasWorkSchedule = false;
            return this;
        }
    }
}
