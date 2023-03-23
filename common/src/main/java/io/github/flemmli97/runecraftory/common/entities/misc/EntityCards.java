package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class EntityCards extends BaseProjectile {

    private static final EntityDataAccessor<Integer> CARD_TYPE = SynchedEntityData.defineId(EntityCards.class, EntityDataSerializers.INT);

    public EntityCards(EntityType<? extends EntityCards> type, Level world) {
        super(type, world);
        this.damageMultiplier = 0.6f;
    }

    public EntityCards(Level world, LivingEntity shooter, int type) {
        super(ModEntities.CARDS.get(), world, shooter);
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
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CARD_TYPE, 0);
    }

    @Override
    protected float getGravityVelocity() {
        return 0;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        boolean att = CombatUtils.damage(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).hurtResistant(1).element(EnumElement.LIGHT), false, false, CombatUtils.getAttributeValue(this.getOwner(), Attributes.ATTACK_DAMAGE) * this.damageMultiplier, null);
        this.discard();
        return att;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        this.discard();
    }
}
