package io.github.flemmli97.runecraftory.common.entities.misc;

import com.flemmli97.tenshilib.common.entity.EntityProjectile;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SChangeGameStatePacket;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class EntityMobArrow extends EntityProjectile {

    private final float damageMultiplier;
    private Predicate<LivingEntity> pred;

    public EntityMobArrow(EntityType<? extends EntityMobArrow> type, World world) {
        super(type, world);
        this.damageMultiplier = 1;
    }

    public EntityMobArrow(World world, LivingEntity shooter, float dmgMulti) {
        super(ModEntities.arrow.get(), world, shooter);
        this.damageMultiplier = dmgMulti;
        if (shooter instanceof BaseMonster)
            this.pred = ((BaseMonster) shooter).hitPred;
    }

    @Override
    protected boolean canHit(Entity entity) {
        return (!(entity instanceof LivingEntity) || this.pred == null || this.pred.test((LivingEntity) entity)) && super.canHit(entity);
    }

    @Override
    protected boolean onEntityHit(EntityRayTraceResult res) {
        if (CombatUtils.damage(this.getOwner(), res.getEntity(), (CustomDamage) new CustomDamage.Builder(this, this.getOwner()).get().setProjectile(), CombatUtils.getAttributeValueRaw(this.getOwner(), Attributes.ATTACK_DAMAGE) * this.damageMultiplier, null)) {
            if (res.getEntity() instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity) res.getEntity();
                livingentity.setArrowCountInEntity(livingentity.getArrowCountInEntity() + 1);
                EnchantmentHelper.applyThornEnchantments(livingentity, this.getOwner());
                EnchantmentHelper.applyArthropodEnchantments(this.getOwner(), livingentity);

                if (livingentity instanceof PlayerEntity && this.getOwner() instanceof ServerPlayerEntity && !this.isSilent()) {
                    ((ServerPlayerEntity) this.getOwner()).connection.sendPacket(new SChangeGameStatePacket(SChangeGameStatePacket.HIT_PLAYER_ARROW, 0.0F));
                }
            }
            this.remove();
            return true;
        }
        return false;
    }

    @Override
    protected void onBlockHit(BlockRayTraceResult blockRayTraceResult) {
        this.remove();
    }

    @Override
    public LivingEntity getOwner() {
        LivingEntity owner = super.getOwner();
        if (owner instanceof BaseMonster)
            this.pred = ((BaseMonster) owner).hitPred;
        return owner;
    }
}
