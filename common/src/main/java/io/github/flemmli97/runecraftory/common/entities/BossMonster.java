package io.github.flemmli97.runecraftory.common.entities;

import com.google.common.collect.Sets;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.tenshilib.api.entity.IOverlayEntityRender;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Set;

public abstract class BossMonster extends BaseMonster implements IOverlayEntityRender {

    private static final EntityDataAccessor<Boolean> enraged = SynchedEntityData.defineId(BossMonster.class, EntityDataSerializers.BOOLEAN);
    protected final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.PROGRESS);

    public BossMonster(EntityType<? extends BossMonster> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(enraged, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            if (!this.isTamed() && this.isAlive()) {
                this.updateplayers();
                this.updateBossBar();
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
                    this.level.addParticle(new ColoredParticleData(ModParticles.blink.get(), 71 / 255F, 237 / 255F, 255 / 255F, 1),
                            this.getX() + (this.random.nextDouble() - 0.5D) * (this.getBbWidth()),
                            this.getY() + this.random.nextDouble() * (this.getBbHeight()),
                            this.getZ() + (this.random.nextDouble() - 0.5D) * (this.getBbWidth()),
                            this.random.nextGaussian() * 0.02D,
                            this.random.nextGaussian() * 0.02D,
                            this.random.nextGaussian() * 0.02D);
            } else if (this.deathTime < 80) {
                if (this.deathTime % 2 == 0)
                    this.level.addParticle(new ColoredParticleData(ModParticles.blink.get(), 71 / 255F, 237 / 255F, 255 / 255F, 1),
                            this.getX() + (this.random.nextDouble() - 0.5D) * (this.getBbWidth() + 2),
                            this.getY() + this.random.nextDouble() * (this.getBbHeight() + 1),
                            this.getZ() + (this.random.nextDouble() - 0.5D) * (this.getBbWidth() + 2),
                            this.random.nextGaussian() * 0.02D,
                            this.random.nextGaussian() * 0.02D,
                            this.random.nextGaussian() * 0.02D);
            } else {
                int amount = (this.deathTime - 80) / 10;
                for (int i = 0; i < amount; i++) {
                    this.level.addParticle(new ColoredParticleData(ModParticles.blink.get(), 71 / 255F, 237 / 255F, 255 / 255F, 1),
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
        return this.isAlive() && !this.isTamed() && this.entityData.get(enraged);
    }

    public void setEnraged(boolean flag, boolean load) {
        this.entityData.set(enraged, flag);
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

    protected boolean checkRage() {
        return this.getHealth() / this.getMaxHealth() < 0.5 && !this.isEnraged();
    }

    private void updateplayers() {
        Set<ServerPlayer> set = new HashSet<>();
        for (ServerPlayer entityplayermp : this.level.getEntitiesOfClass(ServerPlayer.class, this.getBoundingBox().inflate(10.0))) {
            this.bossInfo.addPlayer(entityplayermp);
            set.add(entityplayermp);
        }
        Set<ServerPlayer> set2 = Sets.newHashSet(this.bossInfo.getPlayers());
        set2.removeAll(set);
        for (ServerPlayer entityplayermp2 : set2) {
            this.bossInfo.removePlayer(entityplayermp2);
        }
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        super.remove(reason);
        for (ServerPlayer entityplayermp1 : this.bossInfo.getPlayers()) {
            this.stopSeenByPlayer(entityplayermp1);
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
    }

    @Override
    public int overlayU(int orig) {
        return this.isEnraged() ? (int) (Math.sin(this.tickCount / 7F) * 5 + 5) : orig;
    }

    @Override
    public int overlayV(int orig) {
        return this.isEnraged() ? 0 : orig;
    }
}
