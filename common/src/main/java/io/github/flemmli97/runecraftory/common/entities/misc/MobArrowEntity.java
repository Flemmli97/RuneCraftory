package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class MobArrowEntity extends BaseProjectile {

    public MobArrowEntity(EntityType<? extends MobArrowEntity> type, Level level) {
        super(type, level);
    }

    public MobArrowEntity(Level level, LivingEntity shooter, float dmgMulti) {
        super(RuneCraftoryEntities.ARROW.get(), level, shooter);
        this.damageMultiplier = dmgMulti;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult res) {
        if (CombatUtils.damageWithFaintAndCrit(this.getOwner(), res.getEntity(), new DynamicDamage.Builder(this, this.getOwner()).projectile(), CombatUtils.getAttributeValue(this.getOwner(), Attributes.ATTACK_DAMAGE) * this.damageMultiplier, null)) {
            if (res.getEntity() instanceof LivingEntity livingentity) {
                livingentity.setArrowCount(livingentity.getArrowCount() + 1);
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
