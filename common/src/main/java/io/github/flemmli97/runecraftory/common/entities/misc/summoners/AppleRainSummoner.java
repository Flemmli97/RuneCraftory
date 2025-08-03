package io.github.flemmli97.runecraftory.common.entities.misc.summoners;

import io.github.flemmli97.runecraftory.common.entities.misc.AppleProjectileEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.ProjectileSummonHelperEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.spells.AppleRain;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class AppleRainSummoner extends ProjectileSummonHelperEntity {

    private static final Direction[] HORIZONTAL = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    private static final int RANGE = 12;
    private static final double SPACING = 2;

    private AppleRain.Type type;

    private Direction summonDirection = Direction.NORTH;
    private double summonOffset = -RANGE;

    public AppleRainSummoner(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public AppleRainSummoner(Level level, LivingEntity caster, AppleRain.Type type) {
        super(RuneCraftoryEntities.APPLE_RAIN_SUMMONER.get(), level, caster);
        this.setPos(this.getX(), Math.max(caster.getY() + caster.getBbHeight() + 2, caster.getY() + 6), this.getZ());
        this.type = type;
        this.summonDirection = Util.getRandom(HORIZONTAL, this.getRandom());
        this.maxLivingTicks = Mth.ceil(8 * (RANGE * 2. / SPACING + 1));
    }

    @Override
    protected void summonProjectiles() {
        if (this.ticksExisted % 8 == 0) {
            double x = this.getX() + this.summonDirection.getStepX() * this.summonOffset;
            double y = this.getY();
            double z = this.getZ() + this.summonDirection.getStepZ() * this.summonOffset;
            Direction side = this.summonDirection.getClockWise();
            for (double d = -RANGE; d < RANGE; d += SPACING) {
                double posX = x + side.getStepX() * d;
                double posZ = z + side.getStepZ() * d;
                this.spawnAppleAt(posX, y, posZ);
            }
            this.summonOffset += SPACING;
            this.playSound(RuneCraftorySounds.SPELL_APPLE_RAIN.get(), 1, (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2f + 1.2f);
        }
    }

    private void spawnAppleAt(double x, double y, double z) {
        AppleProjectileEntity apple = new AppleProjectileEntity(this.level(), this.getOwner());
        if (this.type == AppleRain.Type.BIG) {
            apple.withSizeInc(2.2f);
        } else {
            apple.withSizeInc(1.2f);
        }
        apple.setDamageMultiplier(this.damageMultiplier);
        apple.setPos(x, y, z);
        this.level().addFreshEntity(apple);
        if (this.type == AppleRain.Type.LOTS) {
            AppleProjectileEntity apple2 = new AppleProjectileEntity(this.level(), this.getOwner());
            apple2.setDamageMultiplier(this.damageMultiplier);
            apple2.setPos(x, y + 3, z);
            this.level().addFreshEntity(apple2);
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.type = AppleRain.Type.values()[compound.getInt("SummonType")];
        this.summonDirection = Direction.values()[compound.getInt("SummonDirection")];
        this.summonOffset = compound.getDouble("SummonOffset");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("SummonType", this.type.ordinal());
        compound.putInt("SummonDirection", this.summonDirection.ordinal());
        compound.putDouble("SummonOffset", this.summonOffset);
    }
}
