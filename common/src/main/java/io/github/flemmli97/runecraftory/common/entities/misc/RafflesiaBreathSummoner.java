package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.entities.monster.boss.rafflesia.EntityRafflesia;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class RafflesiaBreathSummoner extends ProjectileSummonHelperEntity {

    protected static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(RafflesiaBreathSummoner.class, EntityDataSerializers.INT);

    private EntityStatusBall.Type type = EntityStatusBall.Type.RAFFLESIA_SLEEP;
    private boolean reversed;

    public RafflesiaBreathSummoner(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public RafflesiaBreathSummoner(Level level, LivingEntity caster, EntityStatusBall.Type element) {
        super(ModEntities.RAFFLESIA_BREATH_SUMMONER.get(), level, caster);
        this.type = element;
        this.entityData.set(TYPE, this.type.ordinal());
        this.damageMultiplier = 0.7f;
        this.maxLivingTicks = 16;
        if (caster instanceof EntityRafflesia rafflesia) {
            this.reversed = rafflesia.mirrorAttack();
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (key == TYPE) {
            int i = this.entityData.get(TYPE);
            if (i < EntityStatusBall.Type.values().length)
                this.type = EntityStatusBall.Type.values()[i];
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TYPE, 0);
    }

    @Override
    protected void summonProjectiles() {
        float add = (this.tickCount * 70f / this.maxLivingTicks - 35f);
        float rot = Mth.wrapDegrees(this.getYRot() + (this.reversed ? add : -add));
        EntityStatusBall ball = new EntityStatusBall(this.level, this.getOwner());
        ball.setType(this.type);
        ball.setDamageMultiplier(this.damageMultiplier);
        ball.shootFromRotation(this, this.getXRot(), rot, 0, 0.25f, 0);
        Vec3 delta = ball.getDeltaMovement().normalize().scale(0.5);
        ball.setPos(ball.getX() + delta.x(), this.getY(), ball.getZ() + delta.z());
        this.level.addFreshEntity(ball);
    }
}
