package com.flemmli97.runecraftory.mobs.entity.projectiles;

import com.flemmli97.runecraftory.lib.EnumElement;
import com.flemmli97.runecraftory.mobs.CustomDamage;
import com.flemmli97.runecraftory.mobs.MobUtils;
import com.flemmli97.runecraftory.mobs.entity.BaseMonster;
import com.flemmli97.runecraftory.registry.ModAttributes;
import com.flemmli97.runecraftory.registry.ModEntities;
import com.flemmli97.tenshilib.common.entity.EntityProjectile;
import com.google.common.collect.Lists;
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
import java.util.List;
import java.util.function.Predicate;

public class EntityButterfly extends EntityProjectile {
    private Vector3d targetPos;
    private double length;
    private boolean turn;
    private Predicate<LivingEntity> pred;
    private List<LivingEntity> hitEntities = Lists.newArrayList();

    public EntityButterfly(EntityType<? extends EntityButterfly> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityButterfly(World worldIn, double x, double y, double z) {
        super(ModEntities.butterfly, worldIn, x, y, z);
    }

    public EntityButterfly(World worldIn, LivingEntity thrower) {
        super(ModEntities.butterfly, worldIn, thrower);
        if (thrower instanceof BaseMonster)
            this.pred = ((BaseMonster) thrower).attackPred;
    }

    @Override
    public int livingTickMax() {
        return 50;
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
    protected void onEntityHit(EntityRayTraceResult result) {
        LivingEntity e;
        if (result.getEntity() instanceof LivingEntity && !this.world.isRemote && (e = (LivingEntity) result.getEntity()) != this.getShooter()
                && (this.pred == null || this.pred.test((LivingEntity) result.getEntity())) && !this.hitEntities.contains(e)) {
            if (MobUtils.handleMobAttack(result.getEntity(), CustomDamage.attack(this.getShooter(), EnumElement.NONE, CustomDamage.DamageType.NORMAL, CustomDamage.KnockBackType.BACK, 0.0f, 10), MobUtils.getAttributeValue(this.getShooter(), ModAttributes.RF_MAGIC, e)*0.3f)){//RFCalculations.getAttributeValue(this.getShooter(), ItemStatAttributes.RFMAGICATT, null, null) / 6.0f)) {
                e.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 60, 3));
                this.hitEntities.add(e);
                //this.setDead();
            }
        }
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
    public LivingEntity getShooter() {
        LivingEntity living = super.getShooter();
        if (living instanceof BaseMonster)
            this.pred = ((BaseMonster) living).attackPred;
        return living;
    }
}
