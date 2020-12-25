package com.flemmli97.runecraftory.common.entities.projectiles;

import com.flemmli97.runecraftory.common.entities.BaseMonster;
import com.flemmli97.runecraftory.common.registry.ModEntities;
import com.flemmli97.runecraftory.common.utils.CustomDamage;
import com.flemmli97.runecraftory.common.utils.MobUtils;
import com.flemmli97.tenshilib.common.entity.EntityProjectile;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SChangeGameStatePacket;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityMobArrow extends EntityProjectile {

    private final float damageMultiplier;

    public EntityMobArrow(EntityType<? extends EntityMobArrow> type, World world) {
        super(type, world);
        this.damageMultiplier = 1;
    }

    public EntityMobArrow(World world, LivingEntity shooter, float dmgMulti) {
        super(ModEntities.arrow.get(), world, shooter);
        this.damageMultiplier = dmgMulti;
    }

    @Override
    protected boolean onEntityHit(EntityRayTraceResult res) {
        if (!(this.getOwner() instanceof BaseMonster) || !(res.getEntity() instanceof LivingEntity) || ((BaseMonster) this.getOwner()).attackPred.test((LivingEntity) res.getEntity())) {
            if (MobUtils.handleMobAttack(res.getEntity(), new CustomDamage.Builder(this, this.getOwner()).get().setProjectile(), MobUtils.getAttributeValue(this.getOwner(), Attributes.GENERIC_ATTACK_DAMAGE, res.getEntity()) * this.damageMultiplier)) {
                if (res.getEntity() instanceof LivingEntity) {
                    LivingEntity livingentity = (LivingEntity) res.getEntity();
                    livingentity.setArrowCountInEntity(livingentity.getArrowCountInEntity() + 1);
                    EnchantmentHelper.applyThornEnchantments(livingentity, this.getOwner());
                    EnchantmentHelper.applyArthropodEnchantments(this.getOwner(), livingentity);

                    if (livingentity instanceof PlayerEntity && this.getOwner() instanceof ServerPlayerEntity && !this.isSilent()) {
                        ((ServerPlayerEntity) this.getOwner()).connection.sendPacket(new SChangeGameStatePacket(SChangeGameStatePacket.PROJECTILE_HIT_PLAYER, 0.0F));
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
