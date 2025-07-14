package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class CardsEntity extends BaseProjectile {

    private static final EntityDataAccessor<Integer> CARD_TYPE = SynchedEntityData.defineId(CardsEntity.class, EntityDataSerializers.INT);

    public CardsEntity(EntityType<? extends CardsEntity> type, Level world) {
        super(type, world);
        this.damageMultiplier = 0.6f;
    }

    public CardsEntity(Level world, LivingEntity shooter, int type) {
        super(RuneCraftoryEntities.CARDS.get(), world, shooter);
        this.damageMultiplier = 0.6f;
    }

    public int getCardType() {
        return this.entityData.get(CARD_TYPE);
    }

    @Override
    public int livingTickMax() {
        return 60;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CARD_TYPE, 0);
    }

    @Override
    protected float getGravityVelocity() {
        return 0;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        boolean att = CombatUtils.damageWithFaintAndCrit(this.getOwner(), result.getEntity(), new DynamicDamage.Builder(this, this.getOwner()).noKnockback().hurtResistant(1).element(ItemElement.LIGHT).projectile(), CombatUtils.getAttributeValue(this.getOwner(), Attributes.ATTACK_DAMAGE) * this.damageMultiplier, null);
        this.discard();
        return att;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        this.discard();
    }
}
