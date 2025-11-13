package io.github.flemmli97.runecraftory.neoforge.event;

import io.github.flemmli97.runecraftory.client.ClientCalls;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.events.EntityCalls;
import io.github.flemmli97.runecraftory.common.utils.ItemComponentUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.BonemealEvent;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class EntityEvents {

    @SubscribeEvent
    public void join(PlayerEvent.PlayerLoggedInEvent event) {
        EntityCalls.joinPlayer(event.getEntity());
    }

    @SubscribeEvent
    public void playerAttack(AttackEntityEvent event) {
        if (EntityCalls.playerAttack(event.getEntity(), event.getTarget()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void playerDeath(LivingDeathEvent event) {
        if (EntityCalls.playerDeath(event.getEntity(), event.getSource()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void updateLivingTick(EntityTickEvent.Pre event) {
        if (event.getEntity() instanceof LivingEntity living) {
            EntityCalls.updateLivingTick(living);
            if (living.level().isClientSide)
                ClientCalls.tick(living);
        }
    }

    @SubscribeEvent
    public void foodHandling(LivingEntityUseItemEvent.Finish event) {
        EntityCalls.foodHandling(event.getEntity(), event.getItem());
    }

    @SubscribeEvent
    public void sleep(CanPlayerSleepEvent event) {
        if (GeneralConfig.modifyBed)
            event.setProblem(null);
    }

    @SubscribeEvent
    public void wakeUp(PlayerWakeUpEvent event) {
        if (!event.wakeImmediately() && !event.updateLevel())
            EntityCalls.wakeUp(event.getEntity());
    }

    @SubscribeEvent
    public void disableNatural(MobSpawnEvent.SpawnPlacementCheck event) {
        if (EntityCalls.disableNatural(event.getSpawnType(), event.getEntityType()))
            event.setResult(MobSpawnEvent.SpawnPlacementCheck.Result.FAIL);
    }

    @SubscribeEvent
    public void livingAttack(LivingIncomingDamageEvent event) {
        event.setCanceled(EntityCalls.cancelLivingAttack(event.getSource(), event.getEntity(), event.getAmount()));
    }

    @SubscribeEvent
    public void damageCalculation(LivingDamageEvent.Pre event) {
        event.setNewDamage(EntityCalls.damageCalculation(event.getEntity(), event.getSource(), event.getNewDamage()));
    }

    @SubscribeEvent
    public void postDamageCalculation(LivingDamageEvent.Post event) {
        EntityCalls.postDamage(event.getEntity(), event.getSource(), event.getNewDamage());
    }

    @SubscribeEvent
    public void joinWorld(EntityJoinLevelEvent event) {
        if (event.getEntity().getClass().equals(ItemEntity.class)) {
            if (EntityCalls.handleItemJoinLevel((ItemEntity) event.getEntity()))
                event.setCanceled(true);
        }
        if (event.getEntity() instanceof LivingEntity living)
            EntityCalls.onLoadEntity(living);
    }

    @SubscribeEvent
    public void cropHarvest(PlayerInteractEvent.RightClickBlock event) {
        if (event.getUseBlock() != TriState.FALSE && event.getHand() == InteractionHand.MAIN_HAND)
            EntityCalls.cropRightClickHarvest(event.getEntity(), event.getEntity().level().getBlockState(event.getHitVec().getBlockPos()), event.getHitVec().getBlockPos(), event.getHand());
    }

    @SubscribeEvent
    public void bonemeal(BonemealEvent event) {
        if (EntityCalls.onTryBonemeal(event.getLevel(), event.getStack(), event.getState(), event.getPos(), event.getPlayer())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void itemStackAttributes(ItemAttributeModifierEvent event) {
        ItemComponentUtils.modifyAttribute(event.getItemStack(),
                entry -> event.removeModifier(entry.attribute(), entry.modifier().id()),
                entry -> event.addModifier(entry.attribute(), entry.modifier(), entry.slot()));
    }

    @SubscribeEvent
    public void farmlandTrample(BlockEvent.FarmlandTrampleEvent event) {
        if (EntityCalls.shouldPreventFarmlandTrample(event.getEntity(), event.getLevel()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void playerUseItem(PlayerInteractEvent.RightClickItem event) {
        if (!EntityCalls.onPlayerUseItem(event.getEntity(), event.getHand())) {
            event.setCanceled(true);
        }
    }
}