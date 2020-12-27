package com.flemmli97.runecraftory.common.entities.projectiles;

import com.flemmli97.runecraftory.api.enums.EnumElement;
import com.flemmli97.runecraftory.common.registry.ModAttributes;
import com.flemmli97.runecraftory.common.registry.ModEntities;
import com.flemmli97.runecraftory.common.utils.CombatUtils;
import com.flemmli97.runecraftory.common.utils.CustomDamage;
import com.flemmli97.tenshilib.common.entity.EntityProjectile;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;

public class EntityFireball extends EntityProjectile {

    public EntityFireball(EntityType<? extends EntityFireball> type, World world) {
        super(type, world);
    }

    public EntityFireball(World world, LivingEntity shooter) {
        super(ModEntities.fireBall.get(), world, shooter);
    }

    @Override
    protected boolean onEntityHit(EntityRayTraceResult result) {
        boolean att = CombatUtils.damage(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).element(EnumElement.FIRE).hurtResistant(5).get(), CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.RF_MAGIC.get(), result.getEntity()), null);
        this.world.playSound(null, result.getEntity().getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1.0f, 1.0f);
        this.remove();
        return att;
    }

    @Override
    protected void onBlockHit(BlockRayTraceResult result) {
        this.world.playSound(null, result.getHitVec().x, result.getHitVec().y, result.getHitVec().z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1.0f, 1.0f);
        this.remove();
    }

    @Override
    protected float getGravityVelocity() {
        return 0.0025f;
    }
}
