package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryParticles;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import io.github.flemmli97.tenshilib.common.particle.AdvancedParticleContainer;
import io.github.flemmli97.tenshilib.common.particle.data.ColorData;
import io.github.flemmli97.tenshilib.common.particle.data.MotionData;
import io.github.flemmli97.tenshilib.common.particle.data.ParticleMetaData;
import io.github.flemmli97.tenshilib.common.particle.data.ScaleData;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
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
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
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
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            if (this.random.nextInt(3) == 0) {
                AdvancedParticleContainer.make(RuneCraftoryParticles.LIGHT.get())
                        .addData(new ColorData(49 / 255f, 103 / 255f, 189 / 255f))
                        .addData(new MotionData(this.random.nextGaussian() * 0.02, Math.abs(this.random.nextGaussian() * 0.02), this.random.nextGaussian() * 0.02))
                        .addData(new ScaleData(0.4f))
                        .addData(new ParticleMetaData(10, false, 0))
                        .build().add(this.level(), this.getX(),
                                this.getY() + this.getBbHeight() * 0.5,
                                this.getZ());
            }
        } else {
            int day = WorldUtils.day(this.level());
            if (this.spawnedDay != day) {
                this.discard();
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            this.discard();
            return true;
        }
        return super.hurt(source, amount);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (!player.level().isClientSide && hand == InteractionHand.MAIN_HAND) {
            player.displayClientMessage(Component.translatable("runecraftory.misc.sarcophagus.coming.soon").withStyle(ChatFormatting.DARK_RED), false);
        }
        // TODO: teleport to boss room
        return super.interact(player, hand);
    }

    @Override
    public boolean isPickable() {
        return true;
    }
}
