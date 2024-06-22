package io.github.flemmli97.runecraftory.common.entities.misc;

import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class EntityBullet extends BaseProjectile {

    private static final float[] SIN_POINTS = calcSinPoints();
    private static final EntityDataAccessor<Integer> ELEMENT_DATA = SynchedEntityData.defineId(EntityBullet.class, EntityDataSerializers.INT);

    private EnumElement element = EnumElement.NONE;
    private boolean straight;
    private boolean reverse;
    private Vec3 dir, side;

    public EntityBullet(EntityType<? extends EntityBullet> type, Level level) {
        super(type, level);
    }

    public EntityBullet(Level level, LivingEntity thrower) {
        super(ModEntities.CIRCLING_BULLET.get(), level, thrower);
    }

    private static float[] calcSinPoints() {
        float[] arr = new float[16];
        float step = 2 * Mth.PI / 16;
        for (int i = 0; i < 16; i++)
            arr[i] = Mth.cos((i + 8) * step) * 0.2f;
        return arr;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ELEMENT_DATA, 0);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (key.equals(ELEMENT_DATA)) {
            this.element = EnumElement.values()[this.entityData.get(ELEMENT_DATA)];
        }
        super.onSyncedDataUpdated(key);
    }

    public void setElement(EnumElement element) {
        this.element = element;
        this.entityData.set(ELEMENT_DATA, this.element.ordinal());
    }

    public EnumElement element() {
        return this.element;
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        super.shoot(x, y, z, velocity, inaccuracy);
        Vec3 up = this.calculateUpVector(-this.getViewXRot(1), -this.getViewYRot(1)).normalize();
        this.dir = this.getDeltaMovement();
        this.side = new Vec3(RayTraceUtils.rotatedAround(this.dir, new Vector3f(up), 90)).normalize();
    }

    public void reverseMovement() {
        this.reverse = !this.reverse;
    }

    public void setStraight() {
        this.straight = true;
    }

    @Override
    public int livingTickMax() {
        return this.straight ? 20 : 50;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            if (!this.straight && this.dir != null && this.side != null) {
                int t = this.livingTicks % 16;
                float sT = this.reverse ? -SIN_POINTS[t] : SIN_POINTS[t] * 2f;
                this.setDeltaMovement(this.dir.x + this.side.x * sT, this.dir.y + this.side.y * sT, this.dir.z + this.side.z * sT);
                this.hasImpulse = true;
            }
        }
    }

    @Override
    protected float getGravityVelocity() {
        return 0;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        boolean res = CombatUtils.damageWithFaintAndCrit(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).hurtResistant(0).element(this.element).projectile(), CombatUtils.getAttributeValue(this.getOwner(), Attributes.ATTACK_DAMAGE) * this.damageMultiplier, null);
        if (res)
            this.discard();
        return res;
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
        this.discard();
    }
}
