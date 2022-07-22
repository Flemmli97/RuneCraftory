package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.entity.EntityProjectile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;
import java.util.function.Predicate;

public class EntityButterfly extends EntityProjectile {

    private static final UUID attributeMod = UUID.fromString("5c8e5c2d-1eb0-434a-858f-8ab81f51832c");

    private Vec3 targetPos;
    private double length;
    private boolean turn;
    private Predicate<LivingEntity> pred;
    private int upDelay = 30;
    private float damageMultiplier = 0.15f;

    public EntityButterfly(EntityType<? extends EntityButterfly> type, Level level) {
        super(type, level);
    }

    public EntityButterfly(Level level, double x, double y, double z) {
        super(ModEntities.butterfly.get(), level, x, y, z);
    }

    public EntityButterfly(Level level, LivingEntity thrower) {
        super(ModEntities.butterfly.get(), level, thrower);
        if (thrower instanceof BaseMonster)
            this.pred = ((BaseMonster) thrower).hitPred;
    }

    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    public void setUpDelay(int delay) {
        this.upDelay = delay;
    }

    @Override
    public boolean isPiercing() {
        return true;
    }

    @Override
    public int livingTickMax() {
        return 50;
    }

    @Override
    public void tick() {
        if (this.upDelay-- < 0) {
            double motionY = this.getDeltaMovement().y;
            if (motionY < -0.7)
                motionY += 0.25;
            this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, motionY + 0.02, this.getDeltaMovement().z));
        }
        super.tick();
    }

    @Override
    protected boolean canHit(Entity entity) {
        return super.canHit(entity) && (this.pred == null || (entity instanceof LivingEntity && this.pred.test((LivingEntity) entity)));
    }

    @Override
    protected float getGravityVelocity() {
        return 0.0f;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        Entity owner = this.getOwner();
        boolean living = owner instanceof LivingEntity;
        if (living)
            ((LivingEntity) owner).getAttribute(ModAttributes.RFDRAIN.get())
                    .addTransientModifier(new AttributeModifier(attributeMod, "butterfly_mod", 100, AttributeModifier.Operation.ADDITION));
        if (CombatUtils.damage(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).hurtResistant(3).get(), CombatUtils.getAttributeValueRaw(this.getOwner(), ModAttributes.RF_MAGIC.get()) * this.damageMultiplier, null)) {
            if (living)
                ((LivingEntity) owner).getAttribute(ModAttributes.RFDRAIN.get()).removeModifier(attributeMod);
            if (result.getEntity() instanceof LivingEntity)
                ((LivingEntity) result.getEntity()).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 3));
            return true;
        }
        return false;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockRayTraceResult) {
        this.upDelay = 0;
    }

    @Override
    public Entity getOwner() {
        Entity owner = super.getOwner();
        if (owner instanceof BaseMonster)
            this.pred = ((BaseMonster) owner).hitPred;
        return owner;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.damageMultiplier = compound.getFloat("DamageMultiplier");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("DamageMultiplier", this.damageMultiplier);
    }
}
