package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class EntityMobArrow extends BaseProjectile {

    public EntityMobArrow(EntityType<? extends EntityMobArrow> type, Level world) {
        super(type, world);
    }

    public EntityMobArrow(Level world, LivingEntity shooter, float dmgMulti) {
        super(ModEntities.ARROW.get(), world, shooter);
        this.damageMultiplier = dmgMulti;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult res) {
        if (CombatUtils.damageWithFaintAndCrit(this.getOwner(), res.getEntity(), new CustomDamage.Builder(this, this.getOwner()).projectile(), CombatUtils.getAttributeValue(this.getOwner(), Attributes.ATTACK_DAMAGE) * this.damageMultiplier, null)) {
            if (res.getEntity() instanceof LivingEntity livingentity) {
                livingentity.setArrowCount(livingentity.getArrowCount() + 1);
                EnchantmentHelper.doPostHurtEffects(livingentity, this.getOwner());
                if (this.getOwner() instanceof LivingEntity owner)
                    EnchantmentHelper.doPostDamageEffects(owner, livingentity);

                if (livingentity instanceof Player && this.getOwner() instanceof ServerPlayer serverPlayer && !this.isSilent()) {
                    serverPlayer.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
                }
            }
            this.discard();
            return true;
        }
        return false;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockRayTraceResult) {
        this.discard();
    }
}
