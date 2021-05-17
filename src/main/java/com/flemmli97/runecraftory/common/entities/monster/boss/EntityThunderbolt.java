package com.flemmli97.runecraftory.common.entities.monster.boss;

import com.flemmli97.runecraftory.common.entities.AnimationType;
import com.flemmli97.runecraftory.common.entities.BossMonster;
import com.flemmli97.runecraftory.common.entities.misc.EntityThiccLightningBolt;
import com.flemmli97.runecraftory.common.entities.misc.EntityThunderboltBeam;
import com.flemmli97.runecraftory.common.entities.monster.ai.ThunderboltAttackGoal;
import com.flemmli97.runecraftory.common.registry.ModSpells;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import com.flemmli97.tenshilib.common.utils.RayTraceUtils;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.concurrent.atomic.AtomicBoolean;

public class EntityThunderbolt extends BossMonster {

    //Turns and kicks with backlegs
    private static final AnimatedAction back_kick = new AnimatedAction(13, 7, "back_kick");
    //Shoots out 5 wind lazer beams in front
    private static final AnimatedAction laser_x5 = new AnimatedAction(29, 25, "laser_x5");
    //AOE around thunderbolt, throws player into air. after that tries to kick him away if players staggering
    private static final AnimatedAction stomp = new AnimatedAction(9, 6, "stomp");
    //Horn attack: throws player into air and then kicks him away
    private static final AnimatedAction horn_attack = new AnimatedAction(9, 5, "horn_attack");
    private static final AnimatedAction back_kick_horn = new AnimatedAction(13, 7, "back_kick_horn", "back_kick");

    //Charges 2 times at the player. 2-3 when enraged
    private static final AnimatedAction charge = new AnimatedAction(31, 9, "charge");
    private static final AnimatedAction charge_2 = new AnimatedAction(31, 9, "charge_2", "charge");
    private static final AnimatedAction charge_3 = new AnimatedAction(31, 9, "charge_3", "charge");

    //Shoots lazers in all directions. only enraged
    private static final AnimatedAction laser_aoe = new AnimatedAction(29, 25, "laser_aoe", "laser_x5");
    //Kicks with backlegs causes wind lazerbeam in looking direction. only enraged
    private static final AnimatedAction laser_kick = new AnimatedAction(16, 6, "laser_kick");
    private static final AnimatedAction laser_kick_2 = new AnimatedAction(16, 6, "laser_kick_2", "laser_kick");

    //Shoots 2 slight homing wind blades
    private static final AnimatedAction wind_blade = new AnimatedAction(15, 8, "wind_blade");
    //Used after feinting death
    private static final AnimatedAction laser_kick_3 = new AnimatedAction(16, 6, "laser_kick_3", "laser_kick");

    private static final AnimatedAction feint = new AnimatedAction(40, 2, "feint");
    private static final AnimatedAction defeat = new AnimatedAction(80, 60, "defeat");

    private static final AnimatedAction neigh = new AnimatedAction(24, 9, "neigh");

    public static final ImmutableList<String> nonChoosableAttacks = ImmutableList.of(charge_2.getID(), charge_3.getID(), laser_kick_2.getID(), laser_kick_3.getID(), back_kick_horn.getID());

    private static final AnimatedAction[] anims = new AnimatedAction[]{back_kick, laser_x5, stomp, horn_attack, back_kick_horn, charge, charge_2, charge_3,
            laser_aoe, laser_kick, laser_kick_2, wind_blade, laser_kick_3, feint, defeat, neigh};

    private static final DataParameter<Float> lockedYaw = EntityDataManager.createKey(EntityThunderbolt.class, DataSerializers.FLOAT);

    public final ThunderboltAttackGoal attack = new ThunderboltAttackGoal(this);

    protected boolean feintedDeath, hornAttackSuccess, chargeAttackSuccess;
    private double[] chargeMotion;

    public EntityThunderbolt(EntityType<? extends BossMonster> type, World world) {
        super(type, world);
        this.bossInfo.setColor(BossInfo.Color.BLUE);
        if (!world.isRemote)
            this.goalSelector.addGoal(1, this.attack);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.31);
        super.applyAttributes();
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(lockedYaw, 0f);
    }

    @Override
    public void setEnraged(boolean flag, boolean load) {
        if (flag && !load) {
            if (!this.isEnraged())
                this.setAnimation(neigh);
            else
                this.setAnimation(defeat);
        }
        super.setEnraged(flag, load);
    }

    public boolean isRunning() {
        return this.getMoveFlag() == 2;
    }

    @Override
    public void setMoving(boolean flag) {
        if (flag && (this.moveController.getSpeed() > 1 || this.isBeingRidden()))
            this.setMoveFlag(2);
        else
            super.setMoving(flag);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return (this.getAnimation() == null || !(this.getAnimation().getID().equals(feint.getID()) || this.getAnimation().getID().equals(defeat.getID())) || this.getAnimation().getID().equals(neigh.getID())) && super.attackEntityFrom(source, amount);
    }

    @Override
    protected boolean checkRage() {
        if (this.getHealth() / this.getMaxHealth() < 0.3)
            return !this.feintedDeath;
        if (this.getHealth() / this.getMaxHealth() < 0.7)
            return !this.isEnraged();
        return false;
    }

    @Override
    public void livingTick() {
        super.livingTick();
        if (!this.world.isRemote && this.getAnimation() != null && this.getAnimation().getID().equals(defeat.getID()) && !this.feintedDeath) {
            if (this.getAnimation().getTick() > this.getAnimation().getLength()) {
                this.feintedDeath = true;
                this.setAnimation(feint);
            }
        }
        if (this.getAnimation() != null &&
                (this.getAnimation().getID().equals(charge.getID())
                        || this.getAnimation().getID().equals(charge_2.getID())
                        || this.getAnimation().getID().equals(charge_3.getID()))) {
            this.rotationPitch = 0;
            this.rotationYaw = this.dataManager.get(lockedYaw);
        }
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (anim.getID().equals(neigh.getID()) || anim.getID().equals(feint.getID()) || anim.getID().equals(defeat.getID()))
            return type == AnimationType.IDLE;
        if (type == AnimationType.GENERICATTACK)
            return this.isEnraged() ? !anim.getID().equals(laser_x5.getID()) : !anim.getID().equals(laser_aoe.getID()) && !anim.getID().equals(laser_kick.getID());
        return false;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (!state.getMaterial().isLiquid()) {
            BlockState blockstate = this.world.getBlockState(pos.up());
            SoundType soundtype = state.getSoundType(world, pos, this);
            if (blockstate.isIn(Blocks.SNOW)) {
                soundtype = blockstate.getSoundType(world, pos, this);
            }
            this.playSound(SoundEvents.ENTITY_HORSE_GALLOP, soundtype.getVolume() * 0.15F, soundtype.getPitch());
        }
    }

    private void summonLaserx5() {
        for (Vector3f vec : RayTraceUtils.rotatedVecs(this.getLookVec(), new Vector3d(0, 1, 0), -50, 50, 25)) {
            EntityThunderboltBeam beam = new EntityThunderboltBeam(this.world, this);
            beam.setRotationToDir(vec.getX(), vec.getY(), vec.getZ(), 0);
            this.world.addEntity(beam);
        }
    }

    private void summonLaserAOE() {
        for (Vector3f vec : RayTraceUtils.rotatedVecs(this.getLookVec(), new Vector3d(0, 1, 0), -180, 140, 40)) {
            EntityThunderboltBeam beam = new EntityThunderboltBeam(this.world, this);
            beam.setRotationToDir(vec.getX(), vec.getY(), vec.getZ(), 0);
            this.world.addEntity(beam);
        }
    }

    private void summonLaserBolt(LivingEntity target) {
        if (!this.world.isRemote) {
            EntityThiccLightningBolt bolt = new EntityThiccLightningBolt(this.world, this);
            if (target != null) {
                double y = -MathHelper.sin(17 * 0.017453292F);
                Vector3d dir = target.getPositionVec().subtract(this.getPositionVec()).normalize();
                bolt.shoot(dir.getX(), y, dir.getZ(), 0.15f, 0);
            } else
                bolt.shoot(this, 17, this.rotationYaw, 0, 0.15f, 0);
            this.world.addEntity(bolt);
        }
    }

    private void summonWind(LivingEntity target) {
        if (!this.world.isRemote) {
            ModSpells.DOUBLESONIC.get().use((ServerWorld) this.world, this, null);
        }
    }

    public void setChargeMotion(double[] charge) {
        this.chargeMotion = charge;
    }

    public double[] getChargeTo(AnimatedAction anim, Vector3d dir) {
        int length = anim.getLength() - anim.getAttackTime() - 6; //stop charging 6 ticks earlier
        Vector3d vec = dir.normalize().scale(7);
        return new double[]{vec.x / length, this.getY(), vec.z / length};
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        LivingEntity target = this.getAttackTarget();
        if (target != null) {
            this.getNavigator().clearPath();
            this.getLookController().setLookPositionWithEntity(target, 30.0f, 30.0f);
        }
        switch (anim.getID()) {
            case "back_kick":
            case "back_kick_horn":
                if (anim.canAttack()) {
                    this.mobAttack(anim, target, e -> {
                        if (this.attackEntityAsMob(e)) {
                            e.setMotion(0, 0.05, 0);
                            e.isAirBorne = true;
                            e.takeKnockback(0.7f, this.getX() - e.getX(), this.getZ() - e.getZ());
                        }
                    });
                }
                break;
            case "horn_attack":
                if (anim.canAttack()) {
                    AtomicBoolean bool = new AtomicBoolean(false);
                    this.mobAttack(anim, target, e -> {
                        if (this.attackEntityAsMob(e)) {
                            if (!bool.get())
                                bool.set(true);
                            e.setMotion(0, 0.45, 0);
                            e.isAirBorne = true;
                        }
                    });
                    if (bool.get() && !this.isBeingRidden()) {
                        this.hornAttackSuccess = true;
                        this.attack.setIddleTime(1);
                    }
                }
                break;
            case "stomp":
                if (anim.canAttack()) {
                    this.mobAttack(anim, target, this::attackEntityAsMob);
                }
                break;
            case "wind_blade":
                if (anim.canAttack()) {
                    this.summonWind(target);
                }
                break;
            case "laser_x5":
                if (anim.canAttack()) {
                    this.summonLaserx5();
                }
                break;
            case "laser_aoe":
                if (anim.canAttack()) {
                    this.summonLaserAOE();
                }
                break;
            case "laser_kick":
            case "laser_kick_2":
            case "laser_kick_3":
                if (anim.canAttack()) {
                    this.summonLaserBolt(target);
                }
                break;
            case "charge":
            case "charge_2":
            case "charge_3":
                if ((anim.getTick() < anim.getLength() - 6 && anim.getTick() > anim.getAttackTime()) && !this.chargeAttackSuccess) {
                    if (this.chargeMotion != null) {
                        this.setMotion(this.chargeMotion[0], this.getMotion().y, this.chargeMotion[2]);
                    }
                    this.mobAttack(anim, null, e -> {
                        if (this.attackEntityAsMob(e)) {
                            this.chargeAttackSuccess = true;
                            this.attack.setIddleTime(this.animationCooldown(null));
                        }
                    });
                }
                break;
        }
    }

    @Override
    public AxisAlignedBB calculateAttackAABB(AnimatedAction anim, LivingEntity target) {
        if (anim.getID().equals(stomp.getID())) {
            return this.getBoundingBox().grow(1.5, -0.4, 1.5);
        } else
            return super.calculateAttackAABB(anim, target);
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return this.getWidth() * 0.8;
    }

    @Override
    public float attackChance(AnimationType type) {
        return 1;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return anims;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (this.getAnimation() == null) {
            if (command == 2)
                this.setAnimation(laser_x5);
            else if (command == 1)
                this.setAnimation(stomp);
            else
                this.setAnimation(horn_attack);
        }
    }

    @Override
    protected void playDeathAnimation() {
        this.setAnimation(defeat);
    }

    @Override
    public int animationCooldown(AnimatedAction anim) {
        int diffAdd = this.difficultyCooldown() / 2;
        if (anim != null)
            switch (anim.getID()) {
                case "laser_kick_2":
                case "laser_kick_3":
                case "charge":
                case "charge_2":
                case "charge_3":
                    return 1;
            }
        return 18 + this.getRNG().nextInt(22) - (this.isEnraged() ? 12 : 0) + diffAdd;
    }

    public boolean isAnimEqual(String prev, AnimatedAction other) {
        if (other == null)
            return true;
        if (prev.equals(charge_2.getID()) || prev.equals(charge_3.getID()))
            return other.getID().equals(charge.getID());
        if (prev.equals(laser_kick_2.getID()) || prev.equals(laser_kick_3.getID()))
            return other.getID().equals(laser_kick.getID());
        if (prev.equals(back_kick_horn.getID()))
            return other.getID().equals(horn_attack.getID());
        return prev.equals(other.getID());
    }

    public AnimatedAction chainAnim(String prev) {
        switch (prev) {
            case "laser_kick":
                return laser_kick_2;
            case "laser_kick_2":
                return this.feintedDeath ? laser_kick_3 : null;
            case "horn_attack":
                return this.hornAttackSuccess ? back_kick_horn : null;
            case "charge":
                return this.chargeAttackSuccess ? null : charge_2;
            case "charge_2":
                return this.isEnraged() && !this.chargeAttackSuccess ? charge_3 : null;
        }
        return null;
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("Feint", this.feintedDeath);
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.feintedDeath = compound.getBoolean("Feint");
    }

    @Override
    public double ridingSpeedModifier() {
        return 1.1;
    }

    @Override
    public double getMountedYOffset() {

        return this.getHeight() * 0.825D;
    }

    public void lockYaw(float yaw) {
        this.dataManager.set(lockedYaw, yaw);
    }

    @Override
    public void setAnimation(AnimatedAction anim) {
        if (this.getAnimation() != null && this.getAnimation().getID().equals(defeat.getID()) && anim == null) {
        } else {
            if (!this.world.isRemote) {
                if (this.hornAttackSuccess && anim != null) {
                    this.hornAttackSuccess = false;
                }
                if (this.chargeAttackSuccess && anim != null)
                    this.chargeAttackSuccess = false;
            }
            super.setAnimation(anim);
        }
    }
}