package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWindGust;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.AirWanderGoal;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.AnimatedRangedGoal;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.FloatingFlyNavigator;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

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

    public EntityWeagle(EntityType<? extends BaseMonster> type, Level world) {
        super(type, world);
        this.goalSelector.removeGoal(this.wander);
        this.goalSelector.addGoal(2, this.wander = new AirWanderGoal(this));
        this.goalSelector.addGoal(2, this.rangedGoal);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setNoGravity(true);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(32);
        super.applyAttributes();
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new FloatingFlyNavigator(this, level);
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
                EntityWindGust gust = new EntityWindGust(this.level, this);
                gust.setPos(gust.getX(), gust.getY() + 1.5, gust.getZ());
                if (this.getTarget() != null) {
                    LivingEntity target = this.getTarget();
                    Vec3 dir = target.position().subtract(this.position());
                    double len = dir.length();
                    if (len > 6)
                        len = 1;
                    dir = new Vec3(dir.x(), 0, dir.z()).normalize().scale(len);
                    gust.setRotationTo(target.getX() + dir.x(), target.getY() + target.getBbHeight() * 0.3, target.getZ() + dir.z(), 0);
                }
                this.level.addFreshEntity(gust);
            }
        } else if (anim.getID().equals(swoop.getID())) {
            if (this.hitEntity == null)
                this.hitEntity = new ArrayList<>();
            Vec3 dir;
            if (this.getTarget() != null) {
                dir = this.getTarget().position().subtract(this.position()).normalize();
            } else {
                dir = this.getLookAngle();
            }
            dir = new Vec3(dir.x(), -1.5, dir.z()).normalize().scale(0.5);
            if (anim.getTick() > anim.getAttackTime() && anim.getLength() - anim.getTick() > 3) {
                this.setDeltaMovement(dir.scale(0.55));
                this.mobAttack(anim, null, e -> {
                    if (!this.hitEntity.contains(e)) {
                        this.hitEntity.add(e);
                        this.doHurtTarget(e);
                    }
                });
            } else {
                this.setDeltaMovement(dir.multiply(-0.3, -0.5, -0.3));
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public AABB calculateAttackAABB(AnimatedAction anim, LivingEntity target) {
        if (anim.getID().equals(swoop.getID()))
            return super.calculateAttackAABB(anim, target).move(this.getDeltaMovement());
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
                case 2 -> this.getAnimationHandler().setAnimation(gale);
                case 1 -> this.getAnimationHandler().setAnimation(swoop);
                default -> this.getAnimationHandler().setAnimation(peck);
            }
        }
    }

    @Override
    protected void checkFallDamage(double dist, boolean groundLogic, BlockState state, BlockPos pos) {
    }

    @Override
    public void travel(Vec3 vec) {
        if (this.isEffectiveAi() && this.isVehicle() && this.canBeControlledByRider() && this.getControllingPassenger() instanceof LivingEntity) {
            this.handleWaterTravel(vec);
        } else {
            super.travel(vec);
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }
}