package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
        if (!this.level().isClientSide) {
            if (this.ticksExisted > 6000)
                this.discard();
        } else {
            this.level().addParticle(new ColoredParticleData(ModParticles.LIGHT.get(), 120 / 255F, 120 / 255F, 170 / 255F, 0.2f, 2f), this.getX() + this.random.nextGaussian() * 0.1, this.getY() + 0.1 + this.random.nextGaussian() * 0.02, this.getZ() + this.random.nextGaussian() * 0.1, this.random.nextGaussian() * 0.005, Math.abs(this.random.nextGaussian() * 0.01), this.random.nextGaussian() * 0.005);
        }
    }

    @Override
    public void playerTouch(Player player) {
        if (this.level().isClientSide) {
            return;
        }
        this.discard();
        EnumSkills randomSkill = EnumSkills.values()[player.getRandom().nextInt(EnumSkills.values().length)];
        PlayerData data = Platform.INSTANCE.getPlayerData(player);
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
