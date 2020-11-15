package com.flemmli97.runecraftory.mobs.entity.projectiles;

import com.flemmli97.runecraftory.mobs.CustomDamage;
import com.flemmli97.runecraftory.mobs.MobUtils;
import com.flemmli97.runecraftory.mobs.entity.BaseMonster;
import com.flemmli97.runecraftory.registry.ModAttributes;
import com.flemmli97.runecraftory.registry.ModEntities;
import com.flemmli97.tenshilib.common.entity.EntityProjectile;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SChangeGameStatePacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Arrays;

public class EntityMobArrow extends EntityProjectile {

    private final float damageMultiplier;

    public EntityMobArrow(EntityType<? extends EntityMobArrow> type, World world) {
        super(type, world);
        this.damageMultiplier = 1;
    }

    public EntityMobArrow(World world, LivingEntity shooter, float dmgMulti) {
        super(ModEntities.arrow, world, shooter);
        this.damageMultiplier = dmgMulti;
    }

    @Override
    protected boolean onEntityHit(EntityRayTraceResult res) {
        if (!(this.getShooter() instanceof BaseMonster) || !(res.getEntity() instanceof LivingEntity) || ((BaseMonster) this.getShooter()).attackPred.test((LivingEntity) res.getEntity())) {
            if (MobUtils.handleMobAttack(res.getEntity(), new CustomDamage.Builder(this).trueSource(this.getShooter()).get().setProjectile(), MobUtils.getAttributeValue(this.getShooter(), Attributes.GENERIC_ATTACK_DAMAGE, res.getEntity()) * this.damageMultiplier)) {
                if (res.getEntity() instanceof LivingEntity) {
                    LivingEntity livingentity = (LivingEntity)res.getEntity();
                    livingentity.setArrowCountInEntity(livingentity.getArrowCountInEntity() + 1);
                    EnchantmentHelper.applyThornEnchantments(livingentity, this.getShooter());
                    EnchantmentHelper.applyArthropodEnchantments(this.getShooter(), livingentity);

                    if (livingentity instanceof PlayerEntity && this.getShooter() instanceof ServerPlayerEntity && !this.isSilent()) {
                        ((ServerPlayerEntity)this.getShooter()).connection.sendPacket(new SChangeGameStatePacket(SChangeGameStatePacket.PROJECTILE_HIT_PLAYER, 0.0F));
                    }
                }
                this.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onBlockHit(BlockRayTraceResult blockRayTraceResult) {
        this.remove();
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
