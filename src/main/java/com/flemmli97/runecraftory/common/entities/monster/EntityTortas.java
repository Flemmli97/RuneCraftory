package com.flemmli97.runecraftory.common.entities.monster;

import com.flemmli97.runecraftory.common.entities.AnimationType;
import com.flemmli97.runecraftory.common.entities.ChargingMonster;
import com.flemmli97.runecraftory.common.entities.NewMoveController;
import com.flemmli97.runecraftory.common.entities.monster.ai.ChargeAttackGoal;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.pathfinding.WalkAndSwimNodeProcessor;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class EntityTortas extends ChargingMonster {

    public final ChargeAttackGoal<EntityTortas> ai = new ChargeAttackGoal<>(this);
    public static final AnimatedAction bite = new AnimatedAction(11, 6, "bite");
    public static final AnimatedAction swim = new AnimatedAction(32, 6, "swim");
    public static final AnimatedAction walk = new AnimatedAction(21, 5, "walk");
    public static final AnimatedAction spin = new AnimatedAction(51, 0, "spin");

    private static final AnimatedAction[] anims = new AnimatedAction[]{bite, spin};

    public EntityTortas(EntityType<? extends EntityTortas> type, World world) {
        super(type, world);
        this.setPathPriority(PathNodeType.WATER, 0.0F);
        this.goalSelector.addGoal(2, this.ai);
        this.moveController = new EntityTortas.MoveHelperController(this);
        this.goalSelector.removeGoal(this.swimGoal);
    }

    @Override
    protected PathNavigator createNavigator(World world) {
        return new Navigator(this, world);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.16);
        super.applyAttributes();
    }

    @Override
    public float attackChance(AnimationType type) {
        return 0.7f;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return anims;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean isPushedByWater() {
        return false;
    }

    @Override
    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.WATER;
    }

    @Override
    public void travel(Vector3d vec) {
        if (this.isServerWorld() && this.isInWater()) {
            this.moveRelative(0.1F, vec);
            this.move(MoverType.SELF, this.getMotion());
            this.setMotion(this.getMotion().scale(0.9D));
        } else {
            super.travel(vec);
        }

    }

    @Override
    public void doWhileCharge() {
        if (this.ticksExisted % 15 == 0 && this.hitEntity != null) {
            this.hitEntity.clear();
        }
            ;//this.world.playSound(null, this.getBlockPos(), SoundEvents.ENTITY_COW_STEP, SoundCategory.HOSTILE, 1, this.getRNG().nextFloat() * 0.2F);
    }

    @Override
    public void handleChargeMovement(){
        if(this.getAttackTarget() != null) {
            Vector3d mot = this.getAttackTarget().getPositionVec().subtract(this.getPositionVec()).normalize().scale(0.3);
            this.setMotion(mot.x, this.getMotion().y, mot.z);
        }
        else
            this.setMotion(this.chargeMotion[0], this.getMotion().y, this.chargeMotion[2]);
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 1;
    }

    @Override
    public float chargingLength() {
        return 9;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.CHARGE) {
            return anim.getID().equals(spin.getID());
        }
        return type == AnimationType.MELEE && anim.getID().equals(bite.getID());
    }

    static class Navigator extends SwimmerPathNavigator{

        public Navigator(MobEntity entity, World world) {
            super(entity, world);
        }

        @Override
        protected PathFinder getPathFinder(int range) {
            this.nodeProcessor = new WalkAndSwimNodeProcessor();
            return new PathFinder(this.nodeProcessor, range);
        }
    }

    static class MoveHelperController extends NewMoveController {

        private final EntityTortas tortas;

        public MoveHelperController(EntityTortas tortas) {
            super(tortas);
            this.tortas = tortas;
        }

        private void moveSpeed() {
            if (this.tortas.isInWater()) {
                this.tortas.setMotion(this.tortas.getMotion().add(0.0D, 0.005D, 0.0D));
            } else if (this.tortas.onGround) {
                this.tortas.setAIMoveSpeed(Math.max(this.tortas.getAIMoveSpeed() / 2.0F, 0.06F));
            }
        }

        @Override
        public void tick() {
            this.moveSpeed();
            if (this.action == Action.MOVE_TO && !this.tortas.getNavigator().noPath()) {
                double d0 = this.posX - this.tortas.getX();
                double d1 = this.posY - this.tortas.getY();
                double d2 = this.posZ - this.tortas.getZ();
                double d3 = (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                d1 = d1 / d3;
                float f = (float)(MathHelper.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
                this.tortas.rotationYaw = this.limitAngle(this.tortas.rotationYaw, f, 90.0F);
                this.tortas.renderYawOffset = this.tortas.rotationYaw;
                float f1 = (float)(this.speed * this.tortas.getAttributeValue(Attributes.GENERIC_MOVEMENT_SPEED));
                this.tortas.setAIMoveSpeed(MathHelper.lerp(0.125F, this.tortas.getAIMoveSpeed(), f1));
                this.tortas.setMotion(this.tortas.getMotion().add(0.0D, (double)this.tortas.getAIMoveSpeed() * d1 * 0.1D, 0.0D));
            } else {
                this.tortas.setAIMoveSpeed(0.0F);
            }
        }
    }
}
