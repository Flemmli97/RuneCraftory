package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class EntityDarkBeam extends BaseBeam {

    public EntityDarkBeam(EntityType<? extends EntityDarkBeam> type, Level world) {
        super(type, world);
    }

    public EntityDarkBeam(Level world, LivingEntity shooter) {
        super(ModEntities.DARK_BEAM.get(), world, shooter);
    }

    @Override
    public float getRange() {
        return 8;
    }

    @Override
    public float radius() {
        return 0.9f;
    }

    @Override
    public boolean piercing() {
        return true;
    }

    @Override
    public int livingTickMax() {
        return 25;
    }

    @Override
    public void onImpact(EntityHitResult res) {
        CombatUtils.damageWithFaintAndCrit(this.getOwner(), res.getEntity(), new CustomDamage.Builder(this, this.getOwner()).magic().noKnockback().hurtResistant(10).element(EnumElement.DARK), CombatUtils.getAttributeValue(this.getOwner(), ModAttributes.MAGIC.get()) * this.damageMultiplier, null);
    }

    @Override
    public boolean canStartDamage() {
        return this.livingTicks == 1;
    }
}
