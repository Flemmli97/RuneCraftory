package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntitySmallRaccoonLeaf;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.MathsHelper;
import io.github.flemmli97.runecraftory.common.utils.ProjectileUtils;
import io.github.flemmli97.tenshilib.common.utils.math.MathUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

public class SmallLeafSpell extends Spell {

    private final int amount;

    public SmallLeafSpell(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        Vec3 pos = new EntitySmallRaccoonLeaf(level, entity).position();
        Vec3 target = ProjectileUtils.getAimTarget(entity, pos);
        Vec3 dir;
        if (target != null) {
            dir = target.subtract(pos);
        } else {
            dir = entity.getLookAngle();
        }
        float degs = this.amount < 5 ? 25 : 35;
        Vec3 up = MathsHelper.getUp(dir);
        for (Vector3d vec : MathUtils.rotatedVecs(new Vector3d(dir.x(), dir.y(), dir.z()), new Vector3d(up.x(), up.y(), up.z()), -degs, degs, degs * 2 / this.amount)) {
            EntitySmallRaccoonLeaf leaf = new EntitySmallRaccoonLeaf(level, entity);
            leaf.setPos(leaf.getX() + vec.x() * 0.1, leaf.getY() + vec.y() * 0.1, leaf.getZ() + vec.z() * 0.1);
            leaf.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.9f));
            leaf.shoot(vec.x(), vec.y(), vec.z(), 0.75f, 0);
            level.addFreshEntity(leaf);
        }
        playSound(entity, ModSounds.ENTITY_FLOWER_LILY_STEP.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.2f);
        return true;
    }
}
