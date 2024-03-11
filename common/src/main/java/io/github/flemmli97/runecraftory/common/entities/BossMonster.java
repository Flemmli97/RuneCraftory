package io.github.flemmli97.runecraftory.common.entities;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.IOverlayEntityRender;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class BossMonster extends BaseMonster implements IOverlayEntityRender {

    private static final EntityDataAccessor<Boolean> ENRAGED = SynchedEntityData.defineId(BossMonster.class, EntityDataSerializers.BOOLEAN);
    protected final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.PROGRESS);

    private int noPlayerTick, noPlayerRegenTick;

    public BossMonster(EntityType<? extends BossMonster> type, Level level) {
        super(type, level);
    }

    public static <T extends BaseMonster> ImmutableMap<String, BiConsumer<AnimatedAction, T>> createAnimationHandler(Consumer<ImmutableMap.Builder<AnimatedAction, BiConsumer<AnimatedAction, T>>> cons) {
        ImmutableMap.Builder<AnimatedAction, BiConsumer<AnimatedAction, T>> builder = ImmutableMap.builder();
        cons.accept(builder);
        return builder.build().entrySet().stream().collect(ImmutableMap.toImmutableMap(e -> e.getKey().getID(), Map.Entry::getValue));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ENRAGED, false);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.level.isClientSide) {
            if (!this.isTamed() && this.isAlive()) {
                this.updatePlayers();
                this.updateBossBar();
                if (this.bossInfo.getPlayers().isEmpty() && ++this.noPlayerTick > 200) {
                    if (++this.noPlayerRegenTick > 40) {
                        this.heal(this.getMaxHealth() * 0.1f);
                        this.noPlayerRegenTick = 0;
                    }
                } else {
                    this.noPlayerTick = 0;
                    this.noPlayerRegenTick = 0;
                }
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Enraged", this.isEnraged());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setEnraged(compound.getBoolean("Enraged"), true);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public void setOwner(Player player) {
        super.setOwner(player);
        if (player != null) {
            for (ServerPlayer sPlayer : this.bossInfo.getPlayers()) {
                this.bossInfo.removePlayer(sPlayer);
            }
        }
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
        if (!this.level.isClientSide && this.deathTime == 1)
            this.updateBossBar();
        if (this.level.isClientSide && this.deathTime > 1) {
            if (this.deathTime < 40) {
                if (this.deathTime % 10 == 0)
                    this.level.addParticle(new ColoredParticleData(ModParticles.BLINK.get(), 71 / 255F, 237 / 255F, 255 / 255F, 1),
                            this.getX() + (this.random.nextDouble() - 0.5D) * (this.getBbWidth()),
                            this.getY() + this.random.nextDouble() * (this.getBbHeight()),
                            this.getZ() + (this.random.nextDouble() - 0.5D) * (this.getBbWidth()),
                            this.random.nextGaussian() * 0.02D,
                            this.random.nextGaussian() * 0.02D,
                            this.random.nextGaussian() * 0.02D);
            } else if (this.deathTime < 80) {
                if (this.deathTime % 2 == 0)
                    this.level.addParticle(new ColoredParticleData(ModParticles.BLINK.get(), 71 / 255F, 237 / 255F, 255 / 255F, 1),
                            this.getX() + (this.random.nextDouble() - 0.5D) * (this.getBbWidth() + 2),
                            this.getY() + this.random.nextDouble() * (this.getBbHeight() + 1),
                            this.getZ() + (this.random.nextDouble() - 0.5D) * (this.getBbWidth() + 2),
                            this.random.nextGaussian() * 0.02D,
                            this.random.nextGaussian() * 0.02D,
                            this.random.nextGaussian() * 0.02D);
            } else {
                int amount = (this.deathTime - 80) / 10;
                for (int i = 0; i < amount; i++) {
                    this.level.addParticle(new ColoredParticleData(ModParticles.BLINK.get(), 71 / 255F, 237 / 255F, 255 / 255F, 1),
                            this.getX() + (this.random.nextDouble() - 0.5D) * (this.getBbWidth() + 3),
                            this.getY() + this.random.nextDouble() * (this.getBbHeight() + 1),
                            this.getZ() + (this.random.nextDouble() - 0.5D) * (this.getBbWidth() + 3),
                            this.random.nextGaussian() * 0.02D,
                            this.random.nextGaussian() * 0.02D,
                            this.random.nextGaussian() * 0.02D);
                }
            }
        }
    }

    @Override
    public int maxDeathTime() {
        return 200;
    }

    @Override
    protected float tamingMultiplier(ItemStack stack) {
        boolean flag = stack.is(this.tamingItem());
        return flag ? 1 : 0;
    }

    @Override
    protected void tameEntity(Player owner) {
        super.tameEntity(owner);
        this.setEnraged(false, false);
    }

    public boolean isEnraged() {
        return this.isAlive() && !this.isTamed() && this.entityData.get(ENRAGED);
    }

    public void setEnraged(boolean flag, boolean load) {
        this.entityData.set(ENRAGED, flag);
        if (flag && !load && this.isAlive())
            this.playAngrySound();
    }

    @Override
    public int animationCooldown(AnimatedAction anim) {
        int diffAdd = this.difficultyCooldown();
        return 30 + this.getRandom().nextInt(this.isEnraged() ? 25 : 40) + diffAdd;
    }

    protected void updateBossBar() {
        this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        if (!this.isTamed() && this.checkRage()) {
            this.setEnraged(true, false);
        }
    }

    @Override
    public void onDeathDamageRecord(ServerPlayer player, DamageSource source, float damage) {
        super.onDeathDamageRecord(player, source, damage);
        if (!this.isTamed() && damage > this.getMaxHealth() * 0.05) {
            // Killing player already gets awarded via vanilla
            if (this.deathScore <= 0 || player != this.getKillCredit()) {
                player.awardKillScore(this, this.deathScore, source);
            }
        }
    }

    protected boolean checkRage() {
        return this.getHealth() / this.getMaxHealth() < 0.5 && !this.isEnraged();
    }

    private void updatePlayers() {
        Set<ServerPlayer> set = new HashSet<>();
        for (ServerPlayer serverPlayer : this.level.getEntitiesOfClass(ServerPlayer.class, this.arenaAABB())) {
            this.bossInfo.addPlayer(serverPlayer);
            set.add(serverPlayer);
        }
        Set<ServerPlayer> set2 = Sets.newHashSet(this.bossInfo.getPlayers());
        set2.removeAll(set);
        for (ServerPlayer serverPlayer : set2) {
            this.bossInfo.removePlayer(serverPlayer);
        }
    }

    public AABB arenaAABB() {
        if (this.hasRestriction()) {
            return new AABB(this.getRestrictCenter()).inflate(this.getRestrictRadius());
        }
        return this.getBoundingBox().inflate(12.0);
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        super.remove(reason);
        for (ServerPlayer player : this.bossInfo.getPlayers()) {
            this.stopSeenByPlayer(player);
        }
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
        // If boss killed all players (or every nearby simply player died) heal it back to full
        if (!this.isTamed() && player.isRemoved() && this.bossInfo.getPlayers().isEmpty()) {
            this.heal(this.getMaxHealth());
        }
    }

    @Override
    public int overlayU(int orig) {
        return this.isEnraged() ? (int) (Math.sin(this.tickCount / 7F) * 5 + 5) : orig;
    }

    @Override
    public int overlayV(int orig) {
        return this.isEnraged() ? 0 : orig;
    }

    public void playAngrySound() {
        this.playSound(SoundEvents.PARROT_IMITATE_ENDER_DRAGON, 1.3f, (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2f + 0.8f);
    }
}
