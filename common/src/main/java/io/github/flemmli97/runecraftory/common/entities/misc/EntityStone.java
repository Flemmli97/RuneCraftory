package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.entity.EntityProjectile;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.util.function.Predicate;

public class EntityStone extends EntityProjectile {

    public EntityStone(EntityType<? extends EntityProjectile> type, Level world) {
        super(type, world);
    }

    public EntityStone(Level world, LivingEntity shooter) {
        super(ModEntities.stone.get(), world, shooter);
        if (shooter instanceof BaseMonster)
            this.pred = ((BaseMonster) shooter).hitPred;
    }    private Predicate<LivingEntity> pred = (e) -> !e.equals(this.getOwner());

    @Override
    public int livingTickMax() {
        return 8;
    }

    @Override
    protected boolean canHit(Entity entity) {
        return super.canHit(entity) && (this.pred == null || (entity instanceof LivingEntity && this.pred.test((LivingEntity) entity)));
    }

    @Override
    protected float getGravityVelocity() {
        return 0.01f;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult result) {
        boolean att = CombatUtils.damage(this.getOwner(), result.getEntity(), new CustomDamage.Builder(this, this.getOwner()).hurtResistant(5).get(), CombatUtils.getAttributeValueRaw(this.getOwner(), Attributes.ATTACK_DAMAGE) * 0.8f, null);
        this.remove(RemovalReason.KILLED);
        return att;
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
        this.remove(RemovalReason.KILLED);
    }

    @Override
    public Entity getOwner() {
        Entity living = super.getOwner();
        if (living instanceof BaseMonster)
            this.pred = ((BaseMonster) living).hitPred;
        return living;
    }




}
