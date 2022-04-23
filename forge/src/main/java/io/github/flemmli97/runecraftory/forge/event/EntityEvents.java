package io.github.flemmli97.runecraftory.forge.event;

import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.events.EntityCalls;
import io.github.flemmli97.tenshilib.forge.events.AOEAttackEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EntityEvents {

    @SubscribeEvent
    public void join(PlayerEvent.PlayerLoggedInEvent event) {
        EntityCalls.joinPlayer(event.getPlayer());
    }

    @SubscribeEvent
    public void trackEntity(PlayerEvent.StartTracking event) {
        EntityCalls.trackEntity(event.getPlayer(), event.getTarget());
    }

    @SubscribeEvent
    public void updateEquipment(LivingEquipmentChangeEvent event) {
        EntityCalls.updateEquipment(event.getEntityLiving(), event.getSlot());
    }

    @SubscribeEvent
    public void playerAttack(AttackEntityEvent event) {
        if (EntityCalls.playerAttack(event.getPlayer(), event.getTarget()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void playerAoeAttack(AOEAttackEvent event) {
        if (EntityCalls.playerAoeAttack(event.getPlayer(), event.usedItem, event.attackList()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void playerDeath(LivingDeathEvent event) {
        if (EntityCalls.playerDeath(event.getEntityLiving(), event.getSource()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void clone(PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps();
        EntityCalls.clone(event.getOriginal(), event.getPlayer(), event.isWasDeath());
    }

    @SubscribeEvent
    public void updateLivingTick(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof Player player) {
            EntityCalls.updateLivingTick(player);
        }
    }

    @SubscribeEvent
    public void foodHandling(LivingEntityUseItemEvent.Finish event) {
        EntityCalls.foodHandling(event.getEntityLiving(), event.getItem());
    }

    @SubscribeEvent
    public void hoeTill(BlockEvent.BlockToolModificationEvent event) {
        if (event.isSimulated())
            event.setFinalState(EntityCalls.hoeTill(() -> ToolActions.DEFAULT_HOE_ACTIONS.contains(ToolActions.HOE_DIG), event.getFinalState()));
    }

    @SubscribeEvent
    public void sleep(SleepingTimeCheckEvent event) {
        if (GeneralConfig.modifyBed)
            event.setResult(Event.Result.ALLOW);
    }

    @SubscribeEvent
    public void wakeUp(PlayerWakeUpEvent event) {
        if (GeneralConfig.modifyBed)
            EntityCalls.wakeUp(event.getPlayer());
    }

    @SubscribeEvent
    public void disableNatural(LivingSpawnEvent.CheckSpawn event) {
        if (EntityCalls.disableNatural(event.getSpawnReason(), event.getEntity().getType()))
            event.setResult(Event.Result.DENY);
    }

    @SubscribeEvent
    public void damageCalculation(LivingHurtEvent event) {
        event.setAmount(EntityCalls.damageCalculation(event.getEntityLiving(), event.getSource(), event.getAmount()));
    }

    @SubscribeEvent
    public void onSpawn(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof LivingEntity living)
            EntityCalls.onSpawn(living);
    }
}