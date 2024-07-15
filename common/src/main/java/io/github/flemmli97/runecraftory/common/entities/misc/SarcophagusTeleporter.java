package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SarcophagusTeleporter extends Entity {

    private int spawnedDay;

    public SarcophagusTeleporter(EntityType<?> entityType, Level level) {
        super(entityType, level);
        if (!level.isClientSide)
            this.spawnedDay = WorldUtils.day(level);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.spawnedDay = compound.getInt("SpawnedDay");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("SpawnedDay", this.spawnedDay);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            if (this.random.nextInt(3) == 0) {
                this.level.addParticle(new ColoredParticleData(ModParticles.LIGHT.get(), 49 / 255f, 103 / 255f, 189 / 255f, 1, 2.2f), this.getX() + this.random.nextGaussian() * 0.15, this.getY() + 0.35 + this.random.nextGaussian() * 0.07, this.getZ() + this.random.nextGaussian() * 0.15, this.random.nextGaussian() * 0.01, Math.abs(this.random.nextGaussian() * 0.03), this.random.nextGaussian() * 0.01);
            }
        } else {
            int day = WorldUtils.day(this.level);
            if (this.spawnedDay != day) {
                this.discard();
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source == DamageSource.OUT_OF_WORLD) {
            this.discard();
            return true;
        }
        return super.hurt(source, amount);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (!player.level.isClientSide && hand == InteractionHand.MAIN_HAND) {
            player.sendMessage(new TranslatableComponent("runecraftory.coming.soon").withStyle(ChatFormatting.DARK_RED), Util.NIL_UUID);
        }
        // TODO: teleport to boss room
        return super.interact(player, hand);
    }

    @Override
    public boolean isPickable() {
        return true;
    }
}
