package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.entity.EntityBeam;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class EntityDarkBeam extends EntityBeam {

    private Predicate<LivingEntity> pred;

    public EntityDarkBeam(EntityType<? extends EntityDarkBeam> type, Level world) {
        super(type, world);
    }

    public EntityDarkBeam(Level world, LivingEntity shooter) {
        super(ModEntities.darkBeam.get(), world, shooter);
        if (shooter instanceof BaseMonster)
            this.pred = ((BaseMonster) shooter).hitPred;
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
    public int attackCooldown() {
        return this.livingTickMax();
    }

    @Override
    protected boolean check(Entity e, Vec3 from, Vec3 to) {
        return (!(e instanceof LivingEntity) || this.pred == null || this.pred.test((LivingEntity) e)) && super.check(e, from, to);
    }

    @Override
    public void onImpact(EntityHitResult res) {
        CombatUtils.damage(this.getOwner(), res.getEntity(), new CustomDamage.Builder(this, this.getOwner()).hurtResistant(10).element(EnumElement.DARK).get(), CombatUtils.getAttributeValueRaw(this.getOwner(), ModAttributes.RF_MAGIC.get()), null);
    }

    @Override
    public Entity getOwner() {
        Entity owner = super.getOwner();
        if (owner instanceof BaseMonster)
            this.pred = ((BaseMonster) owner).hitPred;
        return owner;
    }
}
