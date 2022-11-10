package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.items.tools.ItemStatIncrease;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EntityRuneOrb extends Entity {

    private static final EntityDataAccessor<Boolean> LEVELSTATS = SynchedEntityData.defineId(EntityRuneOrb.class, EntityDataSerializers.BOOLEAN);

    private int ticksExisted;

    public EntityRuneOrb(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();
        this.ticksExisted++;
        if (!this.level.isClientSide) {
            if (this.ticksExisted > 6000)
                this.discard();
        } else {
            this.level.addParticle(new ColoredParticleData(ModParticles.light.get(), 120 / 255F, 120 / 255F, 170 / 255F, 0.2f, 2f), this.getX() + this.random.nextGaussian() * 0.1, this.getY() + 0.1 + this.random.nextGaussian() * 0.02, this.getZ() + this.random.nextGaussian() * 0.1, this.random.nextGaussian() * 0.005, Math.abs(this.random.nextGaussian() * 0.01), this.random.nextGaussian() * 0.005);
        }
    }

    @Override
    public void playerTouch(Player player) {
        if (this.level.isClientSide) {
            return;
        }
        this.discard();
        ItemStatIncrease.Stat stat = switch (player.getRandom().nextInt(3)) {
            case 0 -> ItemStatIncrease.Stat.STR;
            case 1 -> ItemStatIncrease.Stat.INT;
            default -> ItemStatIncrease.Stat.VIT;
        };
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
            if (this.entityData.get(LEVELSTATS))
                data.consumeStatBoostItem(player, stat);
            data.refreshRunePoints(player, 150);
        });
        player.level.playSound(null, player.blockPosition(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1, 0.5f);
    }

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(LEVELSTATS, true);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.ticksExisted = compound.getInt("TicksExisted");
        this.entityData.set(LEVELSTATS, compound.getBoolean("LevelStats"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("TicksExisted", this.ticksExisted);
        compound.putBoolean("LevelStats", this.entityData.get(LEVELSTATS));
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
}
