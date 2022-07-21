package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntityMarionettaTrap extends Entity implements OwnableEntity, IAnimated {

    private static final AnimatedAction[] anims = new AnimatedAction[0];
    private final List<LivingEntity> caughtEntities = new ArrayList<>();
    private final AnimationHandler<EntityMarionettaTrap> animationHandler = new AnimationHandler<>(this, anims);
    private int tickLeft = 100;
    private LivingEntity shooter;
    private UUID shooterUUID;

    public EntityMarionettaTrap(EntityType<? extends EntityMarionettaTrap> entityType, Level level) {
        super(entityType, level);
    }

    public EntityMarionettaTrap(Level world, LivingEntity shooter) {
        this(ModEntities.trapChest.get(), world);
        this.shooter = shooter;
        this.shooterUUID = shooter.getUUID();
        this.setPos(shooter.getX(), shooter.getEyeY(), shooter.getZ());
    }

    public static double horizontalMag(Vec3 vec) {
        return vec.x * vec.x + vec.z * vec.z;
    }

    public void addCaughtEntity(LivingEntity entity) {
        this.caughtEntities.add(entity);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.onGround) {
            Vec3 motion = this.getDeltaMovement();
            double f = Math.sqrt(horizontalMag(motion));
            this.setYRot(this.updateRotation(this.yRotO, (float) (Mth.atan2(motion.x, motion.z) * 57.29577951308232D)));
            this.setXRot(this.updateRotation(this.xRotO, (float) (Mth.atan2(motion.y, f) * 57.2957763671875D)));
            boolean water = this.isInWater();
            float friction = water ? 0.8F : 0.85F;
            this.setDeltaMovement(motion.scale(friction).subtract(0.0D, 0.05f, 0.0D));
            this.move(MoverType.SELF, this.getDeltaMovement());
        }

        --this.tickLeft;
        this.caughtEntities.forEach(e -> {
            if (e.isAlive()) {
                if (e instanceof ServerPlayer player)
                    player.moveTo(this.getX(), this.getY() + this.getBbHeight() + 0.2, this.getZ());
                else
                    e.setPos(this.getX(), this.getY() + this.getBbHeight() + 0.2, this.getZ());
            }
        });
        if (!this.level.isClientSide) {
            if (this.tickLeft <= 21 && this.tickLeft >= 9) {
                if (this.getOwner() != null && this.tickLeft % 3 == 0)
                    this.caughtEntities.forEach(e -> {
                        CustomDamage source = CombatUtils.build(this.getOwner(), e, new CustomDamage.Builder(this)).hurtResistant(this.tickLeft == 7 ? 10 : 0).get();
                        CombatUtils.mobAttack(this.getOwner(), e, source, CombatUtils.getAttributeValue(this, Attributes.ATTACK_DAMAGE, e) * 0.7f);
                    });
            }
            if (this.tickLeft <= 0)
                this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    public int getTickLeft() {
        return this.tickLeft;
    }

    private float updateRotation(float prev, float current) {
        while (current - prev < -180.0F) {
            prev -= 360.0F;
        }

        while (current - prev >= 180.0F) {
            prev += 360.0F;
        }

        return Mth.lerp(0.2F, prev, current);
    }

    @Override
    public UUID getOwnerUUID() {
        return this.shooterUUID;
    }

    @Override
    @Nullable
    public LivingEntity getOwner() {
        if (this.shooter == null || this.shooter.isRemoved()) {
            UUID uuid = this.getOwnerUUID();
            if (uuid != null)
                this.shooter = EntityUtil.findFromUUID(LivingEntity.class, this.level, uuid);
        }
        return this.shooter;
    }

    @Override
    public AnimationHandler<?> getAnimationHandler() {
        return this.animationHandler;
    }
}
