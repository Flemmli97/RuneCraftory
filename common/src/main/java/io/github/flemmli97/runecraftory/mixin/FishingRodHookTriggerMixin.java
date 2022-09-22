package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.common.entities.misc.EntityCustomFishingHook;
import io.github.flemmli97.runecraftory.mixinhelper.ExtendedFishingRodHookTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.FishingRodHookedTrigger;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Collection;

@Mixin(FishingRodHookedTrigger.class)
public abstract class FishingRodHookTriggerMixin extends SimpleCriterionTrigger<FishingRodHookedTrigger.TriggerInstance> implements ExtendedFishingRodHookTrigger {

    @Override
    public void customTrigger(ServerPlayer player, ItemStack rod, EntityCustomFishingHook entity, Collection<ItemStack> stacks) {
        LootContext lootContext = EntityPredicate.createContext(player, entity);
        this.trigger(player, triggerInstance -> triggerInstance.matches(rod, lootContext, stacks));
    }
}
