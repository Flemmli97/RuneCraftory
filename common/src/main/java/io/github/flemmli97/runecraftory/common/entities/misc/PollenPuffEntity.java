package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class PollenPuffEntity extends BaseProjectile {

    public static final DustParticleOptions PARTICLE = new DustParticleOptions(Vec3.fromRGB24(0xd4fcd7).toVector3f(), 1.0f);

    public PollenPuffEntity(EntityType<? extends PollenPuffEntity> type, Level level) {
        super(type, level);
    }

    public PollenPuffEntity(Level level, LivingEntity shooter) {
        super(RuneCraftoryEntities.POLLEN_PUFF.get(), level, shooter);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            for (int i = 0; i < 3; i++)
                this.level().addParticle(PARTICLE, this.getRandomX(0.5), this.getY() + this.getBbHeight() * 0.5, this.getRandomZ(0.5), 0, 0, 0);
        }
    }

    @Override
    public int livingTickMax() {
        return 60;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        boolean att = CombatUtils.damageWithFaintAndCrit(this.getOwner(), result.getEntity(), new DynamicDamage.Builder(this, this.getOwner()).magic().noKnockback().hurtResistant(0).element(ItemElement.EARTH).hurtResistant(5), CombatUtils.getAttributeValue(this.getOwner(), RuneCraftoryAttributes.MAGIC_ATTACK.asHolder()) * this.damageMultiplier, null);
        this.discard();
        return att;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        this.discard();
    }

    @Override
    protected float getGravityVelocity() {
        return 0.02f;
    }
}
