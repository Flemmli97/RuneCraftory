package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityStatusBall;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PoisonBallSpell extends Spell {

    @Override
    public void update(Player player, ItemStack stack) {

    }

    @Override
    public void levelSkill(ServerPlayer player) {

    }

    @Override
    public int coolDown() {
        return 80;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        boolean rp = !(entity instanceof Player player) || Platform.INSTANCE.getPlayerData(player).map(data -> LevelCalc.useRP(player, data, this.rpCost(), stack.getItem() instanceof ItemStaffBase, false, true, EnumSkills.EARTH)).orElse(false);
        if (!rp)
            return false;
        EntityStatusBall ball = new EntityStatusBall(level, entity);
        ball.setType(EntityStatusBall.Type.MUSHROOM_POISON);
        ball.setDamageMultiplier(0.65f + lvl * 0.05f);
        ball.setPos(entity.getX(), entity.getY() + 0.4, entity.getZ());
        if (entity instanceof Mob mob && mob.getTarget() != null) {
            ball.shootAtEntity(mob.getTarget(), 0.1f, 7 - level.getDifficulty().getId() * 2, 0.1f);
        } else {
            ball.shootFromRotation(entity, entity.getXRot() + 5, entity.getYRot(), 0.0F, 0.1f, 1.0F);
        }
        level.addFreshEntity(ball);
        return true;
    }

    @Override
    public int rpCost() {
        return 30;
    }
}
