package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityBaseSpellBall;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class BaseStaffSpell extends Spell {

    private static final float[] offsetTwo = {-0.6f, 0.6f};
    private static final float[] offsetThree = {-1.2f, 0, 1.2f};

    @Override
    public void update(Player player, ItemStack stack) {
    }

    @Override
    public void levelSkill(ServerPlayer player) {
    }

    @Override
    public int coolDown() {
        return 0;
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
                for (float offset : offsetTwo) {
                    Vec3 side = MathUtils.rotate(MathUtils.normalY, MathUtils.normalX, -entity.getYRot() * Mth.DEG_TO_RAD);
                    Vec3 newPos = entity.position().add(side.scale(offset)).add(0, entity.getEyeHeight() - 0.1, 0);
                    EntityBaseSpellBall ball = new EntityBaseSpellBall(level, entity, element);
                    Vec3 look = entity.getLookAngle();
                    ball.shoot(look.x, look.y, look.z, 1, 0);
                    ball.setPos(newPos.x, newPos.y, newPos.z);
                    entity.level.addFreshEntity(ball);
                }
            } else {
                for (float offset : offsetThree) {
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

    @Override
    public int rpCost() {
        return 0;
    }
}
