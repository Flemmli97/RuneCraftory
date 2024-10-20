package io.github.flemmli97.runecraftory.forge.event;

import com.google.common.collect.Multimap;
import io.github.flemmli97.runecraftory.client.ClientCalls;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.events.EntityCalls;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.forge.events.AOEAttackEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
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
        boolean rev = Platform.INSTANCE.getPlayerData(event.getOriginal()).isPresent();
        if (!rev)
            event.getOriginal().reviveCaps();
        EntityCalls.clone(event.getOriginal(), event.getPlayer(), event.isWasDeath());
        if (!rev)
            event.getOriginal().invalidateCaps();
    }

    @SubscribeEvent
    public void updateLivingTick(LivingEvent.LivingUpdateEvent event) {
        EntityCalls.updateLivingTick(event.getEntityLiving());
        if (event.getEntityLiving().level.isClientSide)
            ClientCalls.tick(event.getEntityLiving());
    }

    @SubscribeEvent
    public void foodHandling(LivingEntityUseItemEvent.Finish event) {
        EntityCalls.foodHandling(event.getEntityLiving(), event.getItem());
    }

    @SubscribeEvent
    public void sleep(SleepingTimeCheckEvent event) {
        if (GeneralConfig.modifyBed)
            event.setResult(Event.Result.ALLOW);
    }

    @SubscribeEvent
    public void wakeUp(PlayerWakeUpEvent event) {
        if (!event.wakeImmediately() && !event.updateWorld())
            EntityCalls.wakeUp(event.getPlayer());
    }

    @SubscribeEvent
    public void disableNatural(LivingSpawnEvent.CheckSpawn event) {
        if (EntityCalls.disableNatural(event.getSpawnReason(), event.getEntity().getType()))
            event.setResult(Event.Result.DENY);
    }

    @SubscribeEvent
    public void livingAttack(LivingAttackEvent event) {
        event.setCanceled(EntityCalls.cancelLivingAttack(event.getSource(), event.getEntity(), event.getAmount()));
    }

    @SubscribeEvent
    public void damageCalculation(LivingHurtEvent event) {
        event.setAmount(EntityCalls.damageCalculation(event.getEntityLiving(), event.getSource(), event.getAmount()));
    }

    @SubscribeEvent
    public void postDamageCalculation(LivingDamageEvent event) {
        EntityCalls.postDamage(event.getEntityLiving(), event.getSource(), event.getAmount());
    }

    @SubscribeEvent
    public void joinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof LivingEntity living)
            EntityCalls.onLoadEntity(living);
    }

    @SubscribeEvent
    public void cropHarvest(PlayerInteractEvent.RightClickBlock event) {
        if (event.getUseBlock() != Event.Result.DENY)
            EntityCalls.cropRightClickHarvest(event.getPlayer(), event.getEntity().level.getBlockState(event.getHitVec().getBlockPos()), event.getHitVec().getBlockPos(), event.getHand());
    }

    @SubscribeEvent
    public void bonemeal(BonemealEvent event) {
        if (EntityCalls.onTryBonemeal(event.getWorld(), event.getStack(), event.getBlock(), event.getPos(), event.getPlayer())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void itemStackAttributes(ItemAttributeModifierEvent event) {
        Multimap<Attribute, AttributeModifier> map = ItemNBT.getStatsAttributeMap(event.getItemStack(), event.getModifiers(), event.getSlotType());
        if (map != event.getOriginalModifiers()) {
            event.clearModifiers();
            map.forEach(event::addModifier);
        }
    }

    @SubscribeEvent
    public void farmlandTrample(BlockEvent.FarmlandTrampleEvent event) {
        if (EntityCalls.shouldPreventFarmlandTrample(event.getEntity(), event.getWorld()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void playerUseItem(PlayerInteractEvent.RightClickItem event) {
        if (!EntityCalls.onPlayerUseItem(event.getPlayer())) {
            event.setCanceled(true);
        }
    }
}