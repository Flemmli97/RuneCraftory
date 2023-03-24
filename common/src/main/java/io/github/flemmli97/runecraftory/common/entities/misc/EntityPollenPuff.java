package io.github.flemmli97.runecraftory.common.entities.misc;

import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class EntityPollenPuff extends BaseProjectile {

    public static final DustParticleOptions PARTICLE = new DustParticleOptions(new Vector3f(Vec3.fromRGB24(0xd4fcd7)), 1.0f);

    public EntityPollenPuff(EntityType<? extends EntityPollenPuff> type, Level world) {
        super(type, world);
    }

    public EntityPollenPuff(Level world, LivingEntity shooter) {
        super(ModEntities.POLLEN_PUFF.get(), world, shooter);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            for (int i = 0; i < 3; i++)
                this.level.addParticle(PARTICLE, this.getRandomX(0.5), this.getY() + this.getBbHeight() * 0.5, this.getRandomZ(0.5), 0, 0, 0);
        }
    }

    @Override
    public int livingTickMax() {
        return 60;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        boolean att = CombatUtils.damageWithFaintAndCrit(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).magic().noKnockback().hurtResistant(0).element(EnumElement.EARTH).hurtResistant(5), CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.MAGIC.get()) * this.damageMultiplier, null);
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
