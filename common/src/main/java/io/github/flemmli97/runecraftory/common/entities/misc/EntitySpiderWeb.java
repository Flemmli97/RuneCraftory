package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class EntitySpiderWeb extends BaseProjectile {

    public EntitySpiderWeb(EntityType<? extends EntitySpiderWeb> type, Level level) {
        super(type, level);
    }

    public EntitySpiderWeb(Level level, LivingEntity shooter) {
        super(ModEntities.SPIDER_WEB.get(), level, shooter);
    }

    @Override
    public int livingTickMax() {
        return 200;
    }

    @Override
    protected float getGravityVelocity() {
        return 0.025f;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        boolean att = CombatUtils.damage(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).hurtResistant(5), false, false, CombatUtils.getAttributeValue(this.getOwner(), Attributes.ATTACK_DAMAGE) * this.damageMultiplier, null);
        if (att && result.getEntity() instanceof LivingEntity living)
            living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 3));
        this.discard();
        return att;
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
        this.discard();
    }
}
