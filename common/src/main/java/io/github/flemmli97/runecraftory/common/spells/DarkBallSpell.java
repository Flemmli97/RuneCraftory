package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityDarkBall;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class DarkBallSpell extends Spell {

    private final EntityDarkBall.Type type;

    public DarkBallSpell(EntityDarkBall.Type type) {
        this.type = type;
    }

    @Override
    public void update(Player player, ItemStack stack) {

    }

    @Override
    public void levelSkill(ServerPlayer player) {
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.DARK, this.type == EntityDarkBall.Type.SNAKE ? 8 : 6));
    }

    @Override
    public int coolDown() {
        return 20;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        boolean rp = !(entity instanceof Player player) || Platform.INSTANCE.getPlayerData(player).map(data -> LevelCalc.useRP(player, data, this.rpCost(), stack.getItem() instanceof ItemStaffBase, false, true, EnumSkills.DARK)).orElse(false);
        if (!rp)
            return false;
        EntityDarkBall ball = new EntityDarkBall(level, entity, this.type);
        ball.setDamageMultiplier(0.9f + lvl * 0.1f);
        float vel = this.type == EntityDarkBall.Type.BALL ? 0.09f : 0.23f;
        if (entity instanceof Mob mob && mob.getTarget() != null) {
            ball.shootAtEntity(mob.getTarget(), vel * 1.3f, 0, 0.2f, 0.3);
        } else
            ball.shoot(entity, entity.getXRot(), entity.getYRot(), 0, vel, 0);
        level.addFreshEntity(ball);
        return true;
    }

    @Override
    public int rpCost() {
        return this.type == EntityDarkBall.Type.BALL ? 15 : 10;
    }
}
