package io.github.flemmli97.runecraftory.api.registry;

import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCProfessions;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryPoiTypes;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class NPCProfession {

    public final boolean hasShop, hasSchedule, hasWorkSchedule;

    @Nullable
    private final ResourceKey<PoiType> poiType;
    @Nullable
    private final Predicate<Holder<PoiType>> predicate;

    private String translationKey;
    private String ownerTranslationKey;

    public NPCProfession(NPCProfession.Builder builder) {
        this.hasShop = builder.hasShop;
        this.hasSchedule = builder.hasSchedule;
        this.hasWorkSchedule = builder.hasWorkSchedule;
        this.poiType = builder.poiType;
        this.ownerTranslationKey = builder.ownerTranslationKey;
        if (this.poiType != null) {
            if (!builder.allowCashPOI || RuneCraftoryPoiTypes.CASH_REGISTER.asHolder().is(this.poiType))
                this.predicate = t -> t.is(this.poiType);
            else
                this.predicate = t -> t.is(this.poiType) || t.is(RuneCraftoryPoiTypes.CASH_REGISTER.getKey());
        } else
            this.predicate = null;
    }

    public boolean hasShop(NPCEntity npc, Player player) {
        return this.hasShop;
    }

    public boolean hasPoi() {
        return this.predicate != null;
    }

    public boolean matches(Holder<PoiType> poi) {
        return this.predicate != null && this.predicate.test(poi);
    }

    public Set<BlockState> matchingStates(HolderLookup.Provider provider) {
        Optional<Holder.Reference<PoiType>> type = provider.lookupOrThrow(Registries.POINT_OF_INTEREST_TYPE).get(this.poiType);
        Set<BlockState> set = new HashSet<>();
        type.ifPresent(ref -> set.addAll(ref.value().matchingStates()));
        if (this.matches(RuneCraftoryPoiTypes.CASH_REGISTER.asHolder()))
            set.addAll(RuneCraftoryPoiTypes.CASH_REGISTER.get().matchingStates());
        return set;
    }

    public void handleAction(NPCEntity npc, Player player, String action) {
    }

    public Map<String, List<Component>> actions(NPCEntity entity, ServerPlayer player) {
        return Map.of();
    }

    public String getTranslationKey() {
        if (this.translationKey == null)
            this.translationKey = Util.makeDescriptionId("npc.profession", RuneCraftoryNPCProfessions.PROFESSIONS.registry().getKey(this));
        return this.translationKey;
    }

    public String getOwnerTranslationKey() {
        if (this.ownerTranslationKey == null)
            this.ownerTranslationKey = this.getTranslationKey();
        return this.ownerTranslationKey;
    }

    public static class Builder {

        private final ResourceKey<PoiType> poiType;

        private boolean allowCashPOI = true;
        private boolean hasShop = true;
        private boolean hasSchedule = true;
        private boolean hasWorkSchedule = true;

        private String ownerTranslationKey;

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

        public Builder ownerTranslationKey(String ownerTranslationKey) {
            this.ownerTranslationKey = ownerTranslationKey;
            return this;
        }

        public Builder noWorkSchedule() {
            this.hasWorkSchedule = false;
            return this;
        }
    }
}
