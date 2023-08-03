package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EntitySleepAura extends BaseDamageCloud {

    private final Set<UUID> hitEntities = new HashSet<>();

    public EntitySleepAura(EntityType<? extends EntitySleepAura> type, Level world) {
        super(type, world);
    }

    public EntitySleepAura(Level world, LivingEntity shooter) {
        super(ModEntities.SLEEP_AURA.get(), world, shooter);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            for (int i = 0; i < 12; i++) {
                double x = this.getRandomX(0.9);
                double y = this.getRandomY();
                double z = this.getRandomZ(0.9);
                this.level.addAlwaysVisibleParticle(ParticleTypes.ENTITY_EFFECT, x, y, z, 161 / 255f, 201 / 255f, 195 / 255f);
            }
        }
    }

    @Override
    protected boolean canHit(LivingEntity e) {
        return super.canHit(e) && !this.hitEntities.contains(e.getUUID());
    }

    @Override
    protected boolean damageEntity(LivingEntity target) {
        if (CombatUtils.damageWithFaintAndCrit(this.getOwner(), target, new CustomDamage.Builder(this, this.getOwner()).hurtResistant(4).magic().element(EnumElement.EARTH).withChangedAttribute(ModAttributes.SLEEP.get(), 100), CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.MAGIC.get()) * this.damageMultiplier, null)) {
            this.hitEntities.add(target.getUUID());
            return true;
        }
        return false;
    }
}
