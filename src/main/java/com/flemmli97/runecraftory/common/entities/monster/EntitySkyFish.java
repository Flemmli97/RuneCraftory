package com.flemmli97.runecraftory.common.entities.monster;

import com.flemmli97.runecraftory.common.entities.AnimationType;
import com.flemmli97.runecraftory.common.entities.BaseMonster;
import com.flemmli97.runecraftory.common.entities.SwimWalkMoveController;
import com.flemmli97.runecraftory.common.entities.monster.ai.AirWanderGoal;
import com.flemmli97.runecraftory.common.entities.monster.ai.AnimatedRangedGoal;
import com.flemmli97.runecraftory.common.entities.monster.ai.FloatingFlyNavigator;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class EntitySkyFish extends BaseMonster {

    public AnimatedRangedGoal<EntitySkyFish> rangedGoal = new AnimatedRangedGoal<>(this, 8, e -> true);

    public static final AnimatedAction slap = new AnimatedAction(11, 6, "slap");
    public static final AnimatedAction beam = new AnimatedAction(14, 7, "beam");
    public static final AnimatedAction swipe = new AnimatedAction(16, 5, "swipe");

    private static final AnimatedAction[] anims = new AnimatedAction[]{slap, beam, swipe};

    public EntitySkyFish(EntityType<? extends BaseMonster> type, World world) {
        super(type, world);
        this.goalSelector.removeGoal(this.wander);
        this.goalSelector.addGoal(2, this.wander = new AirWanderGoal(this));
        this.setPathPriority(PathNodeType.WATER, 0.0F);
        //this.setPathPriority(PathNodeType.OPEN, 0.5f);
        this.goalSelector.addGoal(2, this.rangedGoal);
        this.moveController = new FlySwimMoveController(this);
        this.goalSelector.removeGoal(this.swimGoal);
        this.wander.setExecutionChance(50);
        this.setNoGravity(true);
    }

    @Override
    protected PathNavigator createNavigator(World world) {
        return new FloatingFlyNavigator(this, world);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.1);
        super.applyAttributes();
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.RANGED) {
            return anim.getID().equals(beam.getID()) || anim.getID().equals(swipe.getID());
        }
        return type == AnimationType.MELEE && anim.getID().equals(slap.getID());
    }

    @Override
    public float attackChance(AnimationType type) {
        return 0.75f;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return anims;
    }

    @Override
    public void handleRidingCommand(int command) {

    }

    @Override
    public void livingTick() {
        super.livingTick();
        if (this.getAttackTarget() == null && !this.isInWater() && this.belowSold()) {
            Vector3d mot = this.getMotion();
            double newY = Math.max(0, mot.y);
            newY += 0.03;
            this.setMotion(mot.x, Math.min(0.3, newY), mot.z);
        }
    }

    private boolean belowSold() {
        BlockPos pos = this.getBlockPos().down();
        return this.world.getBlockState(pos).hasSolidTopSurface(this.world, pos, this);
    }

    @Override
    public boolean handleFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    //==========Water stuff

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean isPushedByWater() {
        return false;
    }

    @Override
    public boolean canBeRiddenInWater() {
        return false;
    }

    @Override
    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.WATER;
    }

    @Override
    public void travel(Vector3d vec) {
        this.handleWaterTravel(vec);
    }

    static class FlySwimMoveController extends SwimWalkMoveController {

        public FlySwimMoveController(MobEntity entity) {
            super(entity);
        }

        @Override
        protected void moveSpeed() {
        }

        @Override
        public void tick() {
            this.handleWaterMovement();
        }

    }
}
