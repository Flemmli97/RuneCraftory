package com.flemmli97.runecraftory.common.entities.misc;

import com.flemmli97.runecraftory.common.entities.BaseMonster;
import com.flemmli97.runecraftory.common.registry.ModAttributes;
import com.flemmli97.runecraftory.common.registry.ModEntities;
import com.flemmli97.runecraftory.common.utils.CombatUtils;
import com.flemmli97.runecraftory.common.utils.CustomDamage;
import com.flemmli97.tenshilib.common.entity.EntityProjectile;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.IPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class EntityButterfly extends EntityProjectile {
    private Vector3d targetPos;
    private double length;
    private boolean turn;
    private Predicate<LivingEntity> pred;

    public EntityButterfly(EntityType<? extends EntityButterfly> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityButterfly(World worldIn, double x, double y, double z) {
        super(ModEntities.butterfly.get(), worldIn, x, y, z);
    }

    public EntityButterfly(World worldIn, LivingEntity thrower) {
        super(ModEntities.butterfly.get(), worldIn, thrower);
        if (thrower instanceof BaseMonster)
            this.pred = ((BaseMonster) thrower).attackPred;
    }

    @Override
    public int livingTickMax() {
        return 50;
    }

    @Override
    public boolean isPiercing() {
        return true;
    }

    @Override
    public void shootAtPosition(double x, double y, double z, float velocity, float inaccuracy) {
        Vector3d dir = new Vector3d(x - this.getX(), y - this.getY(), z - this.getZ());
        this.length = dir.length();
        dir = dir.scale(1 / velocity);
        this.shoot(dir.x, dir.y, dir.z, velocity, inaccuracy);
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        super.shoot(x, y, z, velocity, inaccuracy);
        this.targetPos = this.getMotion().scale(this.length).add(this.getX(), this.getY(), this.getZ());//.addVector(0, -2, 0);
    }

    @Override
    public void tick() {
        if (this.targetPos != null && this.getDistanceSq(this.targetPos.x, this.targetPos.y, this.targetPos.z) < 7)
            this.turn = true;
        if (this.turn && this.getMotion().y < 0.5) {
            double motionY = this.getMotion().y;
            if (motionY < -2.5)
                motionY = -2.5;
            this.setMotion(new Vector3d(this.getMotion().x, motionY + 0.005, this.getMotion().z));
        }
        super.tick();
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected boolean onEntityHit(EntityRayTraceResult result) {
        LivingEntity e;
        if (result.getEntity() instanceof LivingEntity && (e = (LivingEntity) result.getEntity()) != this.getOwner()
                && (this.pred == null || this.pred.test((LivingEntity) result.getEntity()))) {
            if (CombatUtils.damage(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).hurtResistant(10).get(), CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.RF_MAGIC.get(), e), null)) {//RFCalculations.getAttributeValue(this.getShooter(), ItemStatAttributes.RFMAGICATT, null, null) / 6.0f)) {
                e.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 60, 3));
                //this.setDead();
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onBlockHit(BlockRayTraceResult blockRayTraceResult) {
    }

    @Override
    protected float getGravityVelocity() {
        return 0.0f;
    }

    @Nullable
    @Override
    public LivingEntity getOwner() {
        LivingEntity living = super.getOwner();
        if (living instanceof BaseMonster)
            this.pred = ((BaseMonster) living).attackPred;
        return living;
    }
}
