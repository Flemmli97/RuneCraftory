package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
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
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.util.function.Predicate;

public class EntitySpiderWeb extends EntityProjectile {

    private Predicate<LivingEntity> pred;
    private float damageMultiplier = 1;

    public EntitySpiderWeb(EntityType<? extends EntitySpiderWeb> type, Level level) {
        super(type, level);
    }

    public EntitySpiderWeb(Level level, LivingEntity shooter) {
        super(ModEntities.spiderWeb.get(), level, shooter);
        if (shooter instanceof BaseMonster)
            this.pred = ((BaseMonster) shooter).hitPred;
    }

    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    @Override
    public int livingTickMax() {
        return 200;
    }

    @Override
    protected boolean canHit(Entity entity) {
        return (!(entity instanceof LivingEntity) || this.pred == null || this.pred.test((LivingEntity) entity)) && super.canHit(entity);
    }

    @Override
    protected float getGravityVelocity() {
        return 0.025f;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        boolean att = CombatUtils.damage(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).hurtResistant(5).get(), CombatUtils.getAttributeValueRaw(this.getOwner(), Attributes.ATTACK_DAMAGE) * this.damageMultiplier, null);
        if (att && result.getEntity() instanceof LivingEntity living)
            living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 3));
        this.remove(RemovalReason.KILLED);
        return att;
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
        this.remove(RemovalReason.KILLED);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.damageMultiplier = compound.getFloat("DamageMultiplier");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("DamageMultiplier", this.damageMultiplier);
    }

    @Override
    public Entity getOwner() {
        Entity owner = super.getOwner();
        if (owner instanceof BaseMonster)
            this.pred = ((BaseMonster) owner).hitPred;
        return owner;
    }
}
