package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryParticles;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.tenshilib.common.particle.AdvancedParticleContainer;
import io.github.flemmli97.tenshilib.common.particle.data.ColorData;
import io.github.flemmli97.tenshilib.common.particle.data.MotionData;
import io.github.flemmli97.tenshilib.common.particle.data.ParticleMetaData;
import io.github.flemmli97.tenshilib.common.particle.data.ScaleData;
import io.github.flemmli97.tenshilib.loader.registry.AttachmentRegister;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class RuneOrbEntity extends Entity {

    private static final EntityDataAccessor<Boolean> LEVELSTATS = SynchedEntityData.defineId(RuneOrbEntity.class, EntityDataSerializers.BOOLEAN);

    private int ticksExisted;

    public RuneOrbEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();
        this.ticksExisted++;
        if (!this.level().isClientSide) {
            if (this.ticksExisted > 6000)
                this.discard();
        } else {
            AdvancedParticleContainer.make(RuneCraftoryParticles.LIGHT.get())
                    .addData(new ColorData(120 / 255F, 120 / 255F, 170 / 255F, 0.4f))
                    .addData(new MotionData(this.random.nextGaussian() * 0.01, Math.abs(this.random.nextGaussian() * 0.02), this.random.nextGaussian() * 0.01))
                    .addData(new ScaleData(0.25f))
                    .addData(new ParticleMetaData(10, false, 0))
                    .add(this.level(), this.getX(),
                            this.getY() + this.getBbHeight() * 0.2,
                            this.getZ());
        }
    }

    @Override
    public void playerTouch(Player player) {
        if (this.level().isClientSide) {
            return;
        }
        this.discard();
        Skills randomSkill = Skills.values()[player.getRandom().nextInt(Skills.values().length)];
        PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(player);
        if (this.entityData.get(LEVELSTATS))
            data.increaseSkill(randomSkill, LevelCalc.xpAmountForSkillLevelUp(randomSkill, data.getSkillLevel(randomSkill).getLevel()) - data.getSkillLevel(randomSkill).getXp());
        data.regenRunePoints(150);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(LEVELSTATS, true);
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
}
