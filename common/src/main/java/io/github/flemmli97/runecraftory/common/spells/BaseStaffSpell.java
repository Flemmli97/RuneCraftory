package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityBaseSpellBall;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class BaseStaffSpell extends Spell {

    private static final float[] OFFSET_TWO = {-0.6f, 0.6f};
    private static final float[] OFFSET_THREE = {-1.2f, 0, 1.2f};

    @Override
    public boolean use(ServerLevel world, LivingEntity entity, ItemStack stack) {
        return super.use(world, entity, stack, stack.getItem() instanceof ItemStaffBase staff && staff.amount <= 1);
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (stack.getItem() instanceof ItemStaffBase staff) {
            EnumElement element = ItemNBT.getElement(stack);
            if (element == EnumElement.NONE)
                return false;
            if (staff.amount == 1) {
                EntityBaseSpellBall ball = new EntityBaseSpellBall(level, entity, element);
                Vec3 look = entity.getLookAngle();
                ball.shoot(look.x, look.y, look.z, 1, 0);
                entity.level.addFreshEntity(ball);
            } else if (staff.amount == 2) {
                for (float offset : OFFSET_TWO) {
                    Vec3 side = MathUtils.rotate(MathUtils.normalY, MathUtils.normalX, -entity.getYRot() * Mth.DEG_TO_RAD);
                    Vec3 newPos = entity.position().add(side.scale(offset)).add(0, entity.getEyeHeight() - 0.1, 0);
                    EntityBaseSpellBall ball = new EntityBaseSpellBall(level, entity, element);
                    Vec3 look = entity.getLookAngle();
                    ball.shoot(look.x, look.y, look.z, 1, 0);
                    ball.setPos(newPos.x, newPos.y, newPos.z);
                    entity.level.addFreshEntity(ball);
                }
            } else {
                for (float offset : OFFSET_THREE) {
                    Vec3 side = MathUtils.rotate(MathUtils.normalY, MathUtils.normalX, -entity.getYRot() * Mth.DEG_TO_RAD);
                    Vec3 newPos = entity.position().add(side.scale(offset)).add(0, entity.getEyeHeight() - 0.1, 0);
                    EntityBaseSpellBall ball = new EntityBaseSpellBall(level, entity, element);
                    Vec3 look = entity.getLookAngle();
                    ball.shoot(look.x, look.y, look.z, 1, 0);
                    ball.setPos(newPos.x, newPos.y, newPos.z);
                    entity.level.addFreshEntity(ball);
                }
            }
            return true;
        }
        return false;
    }
}
