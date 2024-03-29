package io.github.flemmli97.runecraftory.common.entities.monster.boss;


import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import com.flemmli97.tenshilib.api.entity.AnimationHandler;
import com.flemmli97.tenshilib.common.utils.RayTraceUtils;
import com.google.common.collect.ImmutableList;
import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityAmbrosiaSleep;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityAmbrosiaWave;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityButterfly;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityPollen;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.AmbrosiaAttackGoal;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

import java.util.List;

public class EntityAmbrosia extends BossMonster {
    //Tries kicking target 3 times in a row   
    public static final AnimatedAction kick_1 = new AnimatedAction(12, 6, "kick_1");
    public static final AnimatedAction kick_2 = new AnimatedAction(12, 6, "kick_2");
    public static final AnimatedAction kick_3 = new AnimatedAction(16, 6, "kick_3");

    //Sends a wave of hp-draining(hard) butterflies at target
    public static final AnimatedAction butterfly = new AnimatedAction(41, 5, "butterfly");
    //Shockwave kind of attack surrounding ambrosia
    public static final AnimatedAction wave = new AnimatedAction(45, 5, "wave");
    //Sleep balls
    public static final AnimatedAction sleep = new AnimatedAction(15, 5, "sleep");
    //2 spinning changing direction between them. also scatters earth damage pollen while doing it
    public static final AnimatedAction pollen = new AnimatedAction(15, 5, "pollen");
    public static final AnimatedAction pollen2 = new AnimatedAction(15, 5, "pollen_2", "pollen");

    public static final AnimatedAction defeat = new AnimatedAction(204, 150, "defeat");
    public static final AnimatedAction angry = new AnimatedAction(48, 0, "angry");

    private static final AnimatedAction[] anims = new AnimatedAction[]{kick_1, butterfly, wave, sleep, pollen, pollen2, kick_2, kick_3, defeat, angry};

    public static final ImmutableList<String> nonChoosableAttacks = ImmutableList.of(pollen2.getID(), kick_2.getID(), kick_3.getID());

    private final AnimationHandler<EntityAmbrosia> animationHandler = new AnimationHandler<>(this, anims)
            .setAnimationChangeFunc(anim -> anim == null && this.getAnimationHandler().isCurrentAnim(defeat.getID()));

    private static final List<Vector3f> pollenBase = RayTraceUtils.rotatedVecs(new Vector3d(1, 0, 0), new Vector3d(0, 1, 0), -180, 135, 45);
    private static final List<Vector3f> pollenInd = RayTraceUtils.rotatedVecs(new Vector3d(0.04, 0.07, 0), new Vector3d(0, 1, 0), -180, 160, 20);

    private double[] aiVarHelper;
    public final AmbrosiaAttackGoal attack = new AmbrosiaAttackGoal(this);

    public EntityAmbrosia(EntityType<? extends EntityAmbrosia> type, World world) {
        super(type, world);
        if (!world.isRemote)
            this.goalSelector.addGoal(1, this.attack);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.29);
        super.applyAttributes();
    }

    @Override
    public float attackChance(AnimationType type) {
        return 1;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return (!this.getAnimationHandler().hasAnimation() || !(this.getAnimationHandler().isCurrentAnim(wave.getID(), angry.getID()))) && super.attackEntityFrom(source, amount);
    }

    @Override
    public void setEnraged(boolean flag, boolean load) {
        super.setEnraged(flag, load);
        if (flag && !load)
            this.getAnimationHandler().setAnimation(angry);
    }

    public void setAiVarHelper(double[] aiVarHelper) {
        this.aiVarHelper = aiVarHelper;
    }

    public void summonButterfly(double x, double y, double z) {
        for (int i = 0; i < 1; ++i) {
            if (!this.world.isRemote) {
                EntityButterfly fly = new EntityButterfly(this.world, this);
                fly.setPosition(fly.getPosX() + this.rand.nextFloat() * 2 - 1, fly.getPosY() + this.rand.nextFloat() * 0.5 + 0.25, fly.getPosZ() + this.rand.nextFloat() * 2 - 1);
                fly.shootAtPosition(x, y, z, 0.3f, 5.0f);
                this.world.addEntity(fly);
            }
        }
    }

    public void summonWave(int duration) {
        if (!this.world.isRemote) {
            EntityAmbrosiaWave wave = new EntityAmbrosiaWave(this.world, this, duration);
            wave.setPosition(wave.getPosX(), wave.getPosY() + 0.2, wave.getPosZ());
            this.world.addEntity(wave);
        }
    }

    public void summonSleepBalls() {
        if (!this.world.isRemote) {
            for (int i = 0; i < 4; ++i) {
                double angle = i / 4.0 * 3.141592653589793 * 2.0 + Math.toRadians(this.rotationYaw);
                double x = Math.cos(angle) * 1.3;
                double z = Math.sin(angle) * 1.3;
                EntityAmbrosiaSleep wave = new EntityAmbrosiaSleep(this.world, this);
                wave.setPosition(this.getPosX() + x, this.getPosY() + 0.4, this.getPosZ() + z);
                this.world.addEntity(wave);
            }
        }
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        LivingEntity target = this.getAttackTarget();
        if (target != null) {
            if (!anim.getID().equals(pollen.getID()))
                this.faceEntity(target, 180.0f, 50.0f);
        }
        switch (anim.getID()) {
            case "butterfly":
                if (anim.getTick() > anim.getAttackTime()) {
                    this.summonButterfly(this.aiVarHelper[0], this.aiVarHelper[1], this.aiVarHelper[2]);
                }
                break;
            case "kick_1":
            case "kick_2":
            case "kick_3":
                if (target != null) {
                    this.getNavigator().tryMoveToEntityLiving(target, 1.0);
                }
                if (anim.canAttack()) {
                    this.mobAttack(anim, target, this::attackEntityAsMob);
                }
                break;
            case "sleep":
                this.getNavigator().clearPath();
                if (anim.canAttack())
                    this.summonSleepBalls();
                break;
            case "wave":
                this.getNavigator().clearPath();
                if (anim.canAttack())
                    this.summonWave(anim.getLength() - anim.getAttackTime());
                break;
            case "pollen":
            case "pollen_2":
                if (this.aiVarHelper == null)
                    return;
                this.setMotion(new Vector3d(this.aiVarHelper[0], 0, this.aiVarHelper[2]));
                if (anim.canAttack()) {
                    this.getNavigator().clearPath();
                    EntityPollen pollen = new EntityPollen(this.world, this);
                    pollen.setPosition(pollen.getPosX(), pollen.getPosY() + 0.5, pollen.getPosZ());
                    this.world.addEntity(pollen);
                }
                break;
        }
    }

    @Override
    public void applyEntityCollision(Entity entityIn) {
        if (this.getAnimationHandler().isCurrentAnim(pollen.getID()))
            return;
        super.applyEntityCollision(entityIn);
    }

    @Override
    public AxisAlignedBB calculateAttackAABB(AnimatedAction anim, LivingEntity target) {
        if (anim.getID().equals(pollen.getID())) {
            return this.getBoundingBox().grow(2.0);
        }
        return super.calculateAttackAABB(anim, target);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    @Override
    public AnimationHandler<EntityAmbrosia> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    protected void playDeathAnimation() {
        this.getAnimationHandler().setAnimation(defeat);
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (anim.getID().equals(defeat.getID()) || anim.getID().equals(angry.getID()))
            return type == AnimationType.IDLE;
        if (type == AnimationType.GENERICATTACK)
            return this.isEnraged() || !anim.getID().equals(pollen.getID());
        return false;
    }

    public boolean isAnimEqual(String prev, AnimatedAction other) {
        if (other == null)
            return true;
        if (prev.equals(pollen2.getID()))
            return other.getID().equals(pollen.getID());
        if (prev.equals(kick_3.getID()))
            return other.getID().equals(kick_1.getID());
        return prev.equals(other.getID());
    }

    public AnimatedAction chainAnim(String prev) {
        switch (prev) {
            case "kick_1":
                return kick_2;
            case "kick_2":
                return kick_3;
            case "pollen":
                return pollen2;
        }
        return null;
    }

    @Override
    public int animationCooldown(AnimatedAction anim) {
        int diffAdd = this.difficultyCooldown();
        if (anim != null)
            switch (anim.getID()) {
                case "kick_1":
                case "pollen":
                case "kick_2":
                    return 3;
            }
        return 34 + this.getRNG().nextInt(22) - (this.isEnraged() ? 20 : 0) + diffAdd;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2)
                this.getAnimationHandler().setAnimation(wave);
            else if (command == 1)
                this.getAnimationHandler().setAnimation(sleep);
            else
                this.getAnimationHandler().setAnimation(kick_1);
        }
    }

    @Override
    public double getMountedYOffset() {
        return this.getHeight() * 0.85D;
    }
}
