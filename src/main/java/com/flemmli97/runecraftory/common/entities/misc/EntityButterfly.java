package com.flemmli97.runecraftory.common.entities.misc;

import com.flemmli97.runecraftory.common.entities.BaseMonster;
import com.flemmli97.runecraftory.common.registry.ModAttributes;
import com.flemmli97.runecraftory.common.registry.ModEntities;
import com.flemmli97.runecraftory.common.utils.CombatUtils;
import com.flemmli97.runecraftory.common.utils.CustomDamage;
import com.flemmli97.tenshilib.common.entity.EntityProjectile;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
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
    private int upDelay = 30;

    public EntityButterfly(EntityType<? extends EntityButterfly> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityButterfly(World worldIn, double x, double y, double z) {
        super(ModEntities.butterfly.get(), worldIn, x, y, z);
    }

    public EntityButterfly(World worldIn, LivingEntity thrower) {
        super(ModEntities.butterfly.get(), worldIn, thrower);
        if (thrower instanceof BaseMonster)
            this.pred = ((BaseMonster) thrower).hitPred;
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

    public void setUpDelay(int delay) {
        this.upDelay = delay;
    }

    @Override
    public void tick() {
        if (this.upDelay-- < 0) {
            double motionY = this.getMotion().y;
            if (motionY < -0.7)
                motionY += 0.25;
            this.setMotion(new Vector3d(this.getMotion().x, motionY + 0.02, this.getMotion().z));
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
            if (CombatUtils.damage(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).hurtResistant(10).get(), CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.RF_MAGIC.get(), e) * 0.1f, null)) {//RFCalculations.getAttributeValue(this.getShooter(), ItemStatAttributes.RFMAGICATT, null, null) / 6.0f)) {
                e.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 60, 3));
                return true;
            }
        }
        return false;
    }

    public EntityRayTraceResult res(Vector3d from, Vector3d to) {
        return ProjectileHelper.rayTraceEntities(this.world, this, from, to, this.getBoundingBox().expand(this.getMotion()).grow(1.0D), e -> true);
    }

    @Override
    protected void onBlockHit(BlockRayTraceResult blockRayTraceResult) {
        this.upDelay = 0;
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
            this.pred = ((BaseMonster) living).hitPred;
        return living;
    }
}
