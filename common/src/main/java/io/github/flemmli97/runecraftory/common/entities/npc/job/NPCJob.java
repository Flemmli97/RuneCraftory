package io.github.flemmli97.runecraftory.common.entities.npc.job;

import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModNPCJobs;
import io.github.flemmli97.runecraftory.common.registry.ModPoiTypes;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class NPCJob {

    public final boolean hasShop, hasSchedule, hasWorkSchedule;

    public final Supplier<PoiType> poiType;
    @Nullable
    public final Predicate<PoiType> predicate;

    private String translationKey;

    public NPCJob(NPCJob.Builder builder) {
        this.hasShop = builder.hasShop;
        this.hasSchedule = builder.hasSchedule;
        this.hasWorkSchedule = builder.hasWorkSchedule;
        this.poiType = builder.poiType;
        if (this.poiType != null) {
            if (!builder.allowCashPOI || this.poiType == ModPoiTypes.CASH_REGISTER)
                this.predicate = t -> this.poiType.get().getPredicate().test(t);
            else
                this.predicate = t -> this.poiType.get().getPredicate().test(t) || ModPoiTypes.CASH_REGISTER.get().getPredicate().test(t);
        } else
            this.predicate = null;
    }

    public boolean hasShop(EntityNPCBase npc, Player player) {
        return this.hasShop;
    }

    public void handleAction(EntityNPCBase npc, Player player, String action) {
    }

    public List<String> actions() {
        return List.of();
    }

    public String getTranslationKey() {
        if (this.translationKey == null)
            this.translationKey = "npc.job." + ModNPCJobs.getIDFrom(this);
        return this.translationKey;
    }

    public static class Builder {

        private final Supplier<PoiType> poiType;

        private boolean allowCashPOI = true;
        private boolean hasShop = true;
        private boolean hasSchedule = true;
        private boolean hasWorkSchedule = true;

        public Builder(Supplier<PoiType> poiType) {
            this.poiType = Objects.requireNonNullElseGet(poiType, () -> () -> null);
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
