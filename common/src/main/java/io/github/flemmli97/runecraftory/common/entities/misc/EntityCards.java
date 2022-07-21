package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.entity.EntityProjectile;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.util.function.Predicate;

public class EntityCards extends EntityProjectile {

    private static final EntityDataAccessor<Integer> cardType = SynchedEntityData.defineId(EntityCards.class, EntityDataSerializers.INT);

    private float damageMultiplier;

    private Predicate<LivingEntity> pred;

    public EntityCards(EntityType<? extends EntityCards> type, Level world) {
        super(type, world);
    }

    public EntityCards(Level world, LivingEntity shooter, int type) {
        super(ModEntities.cards.get(), world, shooter);
        if (shooter instanceof BaseMonster)
            this.pred = ((BaseMonster) shooter).hitPred;
    }

    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    @Override
    protected boolean canHit(Entity e) {
        return (!(e instanceof LivingEntity) || this.pred == null || this.pred.test((LivingEntity) e)) && super.canHit(e);
    }

    @Override
    public int livingTickMax() {
        return 60;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(cardType, 0);
    }

    @Override
    protected float getGravityVelocity() {
        return 0;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        boolean att = CombatUtils.damage(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).hurtResistant(5).get(), CombatUtils.getAttributeValueRaw(this.getOwner(), ModAttributes.RF_MAGIC.get()) * this.damageMultiplier, null);
        this.remove(RemovalReason.KILLED);
        return att;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        this.remove(RemovalReason.KILLED);
    }

    @Override
    public Entity getOwner() {
        Entity owner = super.getOwner();
        if (owner instanceof BaseMonster)
            this.pred = ((BaseMonster) owner).hitPred;
        return owner;
    }

    public int getCardType() {
        return this.entityData.get(cardType);
    }
}
