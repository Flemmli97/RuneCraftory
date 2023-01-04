package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.entity.EntityProjectile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.util.function.Predicate;

public class EntityMobArrow extends EntityProjectile {

    private Predicate<LivingEntity> pred;
    private float damageMultiplier = 1;

    public EntityMobArrow(EntityType<? extends EntityMobArrow> type, Level world) {
        super(type, world);
    }

    public EntityMobArrow(Level world, LivingEntity shooter, float dmgMulti) {
        super(ModEntities.arrow.get(), world, shooter);
        this.damageMultiplier = dmgMulti;
        if (shooter instanceof BaseMonster)
            this.pred = ((BaseMonster) shooter).hitPred;
    }

    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    @Override
    protected boolean canHit(Entity entity) {
        return (!(entity instanceof LivingEntity) || this.pred == null || this.pred.test((LivingEntity) entity)) && super.canHit(entity);
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult res) {
        if (CombatUtils.damage(this.getOwner(), res.getEntity(), (CustomDamage) CombatUtils.build(this.getOwner(), res.getEntity(), new CustomDamage.Builder(this, this.getOwner()), false, true).get().setProjectile(), CombatUtils.getAttributeValue(this.getOwner(), Attributes.ATTACK_DAMAGE) * this.damageMultiplier, null)) {
            if (res.getEntity() instanceof LivingEntity livingentity) {
                livingentity.setArrowCount(livingentity.getArrowCount() + 1);
                EnchantmentHelper.doPostHurtEffects(livingentity, this.getOwner());
                if (this.getOwner() instanceof LivingEntity owner)
                    EnchantmentHelper.doPostDamageEffects(owner, livingentity);

                if (livingentity instanceof Player && this.getOwner() instanceof ServerPlayer serverPlayer && !this.isSilent()) {
                    serverPlayer.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
                }
            }
            this.remove(RemovalReason.KILLED);
            return true;
        }
        return false;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockRayTraceResult) {
        this.remove(RemovalReason.KILLED);
    }

    @Override
    public Entity getOwner() {
        Entity owner = super.getOwner();
        if (owner instanceof BaseMonster)
            this.pred = ((BaseMonster) owner).hitPred;
        return owner;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.damageMultiplier = compound.getFloat("DamageMultiplier");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("DamageMultiplier", this.damageMultiplier);
    }
}
