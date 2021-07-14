package io.github.flemmli97.runecraftory.common.entities.monster;


import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import com.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWindGust;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.AirWanderGoal;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.AnimatedRangedGoal;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.FloatingFlyNavigator;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class EntityWeagle extends BaseMonster {

    public AnimatedRangedGoal<EntityWeagle> rangedGoal = new AnimatedRangedGoal<>(this, 8, e -> true);
    public static final AnimatedAction gale = new AnimatedAction(19, 5, "gale");
    public static final AnimatedAction peck = new AnimatedAction(11, 4, "peck");
    public static final AnimatedAction swoop = new AnimatedAction(14, 4, "swoop");

    private static final AnimatedAction[] anims = new AnimatedAction[]{gale, swoop};

    private final AnimationHandler<EntityWeagle> animationHandler = new AnimationHandler<>(this, anims)
            .setAnimationChangeCons(anim -> this.hitEntity = null);

    protected List<LivingEntity> hitEntity;

    public EntityWeagle(EntityType<? extends BaseMonster> type, World world) {
        super(type, world);
        this.goalSelector.removeGoal(this.wander);
        this.goalSelector.addGoal(2, this.wander = new AirWanderGoal(this));
        this.goalSelector.addGoal(2, this.rangedGoal);
        this.moveController = new FlyingMovementController(this, 20, true);
        this.setNoGravity(true);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(32);
        super.applyAttributes();
    }

    @Override
    protected PathNavigator createNavigator(World world) {
        return new FloatingFlyNavigator(this, world);
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.RANGED) {
            return anim.getID().equals(gale.getID());
        }
        return type == AnimationType.MELEE && (anim.getID().equals(peck.getID()) || anim.getID().equals(swoop.getID()));
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.getID().equals(gale.getID())) {
            if (anim.canAttack()) {
                EntityWindGust gust = new EntityWindGust(this.world, this);
                gust.setPosition(gust.getPosX(), gust.getPosY() + 1.5, gust.getPosZ());
                if (this.getAttackTarget() != null) {
                    LivingEntity target = this.getAttackTarget();
                    Vector3d dir = target.getPositionVec().subtract(this.getPositionVec());
                    double len = dir.length();
                    if (len > 6)
                        len = 1;
                    dir = new Vector3d(dir.getX(), 0, dir.getZ()).normalize().scale(len);
                    gust.setRotationTo(target.getPosX() + dir.getX(), target.getPosY() + target.getHeight() * 0.3, target.getPosZ() + dir.getZ(), 0);
                }
                this.world.addEntity(gust);
            }
        } else if (anim.getID().equals(swoop.getID())) {
            if (this.hitEntity == null)
                this.hitEntity = new ArrayList<>();
            Vector3d dir;
            if (this.getAttackTarget() != null) {
                dir = this.getAttackTarget().getPositionVec().subtract(this.getPositionVec()).normalize();
            } else {
                dir = this.getLookVec();
            }
            dir = new Vector3d(dir.getX(), -1.5, dir.getZ()).normalize().scale(0.5);
            if (anim.getTick() > anim.getAttackTime() && anim.getLength() - anim.getTick() > 3) {
                this.setMotion(dir.scale(0.55));
                this.mobAttack(anim, null, e -> {
                    if (!this.hitEntity.contains(e)) {
                        this.hitEntity.add(e);
                        this.attackEntityAsMob(e);
                    }
                });
            } else {
                this.setMotion(dir.mul(-0.3, -0.5, -0.3));
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public AxisAlignedBB calculateAttackAABB(AnimatedAction anim, LivingEntity target) {
        if (anim.getID().equals(swoop.getID()))
            return super.calculateAttackAABB(anim, target).offset(this.getMotion());
        return super.calculateAttackAABB(anim, target);
    }

    @Override
    public float attackChance(AnimationType type) {
        return 1;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 1.5;
    }

    @Override
    public AnimationHandler<EntityWeagle> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            switch (command) {
                case 2:
                    this.getAnimationHandler().setAnimation(gale);
                    break;
                case 1:
                    this.getAnimationHandler().setAnimation(swoop);
                    break;
                default:
                    this.getAnimationHandler().setAnimation(peck);
                    break;
            }
        }
    }

    @Override
    protected void updateFallState(double dist, boolean groundLogic, BlockState state, BlockPos pos) {
    }

    @Override
    public void travel(Vector3d vec) {
        if (this.isServerWorld() && this.isBeingRidden() && this.canBeSteered() && this.getControllingPassenger() instanceof LivingEntity) {
            this.handleWaterTravel(vec);
        } else {
            super.travel(vec);
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }
}
