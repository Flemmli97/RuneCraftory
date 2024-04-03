package io.github.flemmli97.runecraftory.common.spells;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityMobArrow;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class TripleArrowSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        EntityMobArrow first = new EntityMobArrow(level, entity, 1);
        float f = 1;
        if (stack.getItem() instanceof ItemStaffBase)
            f = BowItem.getPowerForTime(72000 - entity.getUseItemRemainingTicks());
        first.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0.0F, f * 1.5F, 1.0F);
        first.setSecondsOnFire(ItemNBT.getElement(stack) == EnumElement.FIRE ? 200 : 0);
        level.addFreshEntity(first);

        Vec3 dir = entity.getLookAngle();
        Vec3 up = entity.getUpVector(1);
        for (float y = -15; y <= 15; y += 30) {
            Quaternion quaternion = new Quaternion(new Vector3f(up), y, true);
            Vector3f newDir = new Vector3f(dir);
            newDir.transform(quaternion);
            EntityMobArrow arrow = new EntityMobArrow(level, entity, 1);
            arrow.shoot(newDir.x(), newDir.y(), newDir.z(), f * 1.5F, 1.0F);
            arrow.setSecondsOnFire(ItemNBT.getElement(stack) == EnumElement.FIRE ? 200 : 0);
            level.addFreshEntity(arrow);
        }
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ARROW_SHOOT, entity.getSoundSource(), 1.0f, 1.0f / (level.getRandom().nextFloat() * 0.4f + 1.2f) + f * 0.5f);
        return true;
    }
}
