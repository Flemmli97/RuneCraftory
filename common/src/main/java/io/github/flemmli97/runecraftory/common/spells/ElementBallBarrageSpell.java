package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.entities.DelayedAttacker;
import io.github.flemmli97.runecraftory.common.entities.misc.ElementBallBarrageSummoner;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ElementBallBarrageSpell extends Spell {

    private final EnumElement element;

    public ElementBallBarrageSpell(EnumElement element) {
        this.element = element;
    }

    @Override
    public void update(Player player, ItemStack stack) {

    }

    @Override
    public void levelSkill(ServerPlayer player) {
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
            EnumSkills skill = LevelCalc.getSkillFromElement(this.element);
            if (skill != null)
                LevelCalc.levelSkill(player, data, EnumSkills.DARK, 12);
        });
    }

    @Override
    public int coolDown() {
        return 10;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        boolean rp = !(entity instanceof Player player) || Platform.INSTANCE.getPlayerData(player).map(data -> LevelCalc.useRP(player, data, this.rpCost(), stack.getItem() instanceof ItemStaffBase, false, true, EnumSkills.DARK)).orElse(false);
        if (!rp)
            return false;
        ElementBallBarrageSummoner summoner = new ElementBallBarrageSummoner(level, entity, this.element);
        Vec3 eye = entity.getEyePosition();
        float dirScale = 5;
        Vec3 dir = entity instanceof Mob mob && mob.getTarget() != null ? mob.getTarget().getEyePosition().subtract(eye).normalize().scale(3)
                : entity.getLookAngle().scale(dirScale);
        if (entity instanceof Mob mob) {
            Vec3 delayedPos;
            if (mob instanceof DelayedAttacker delayed && (delayedPos = delayed.targetPosition(summoner.position())) != null) {
                dir = delayedPos.subtract(eye.x, delayedPos.y, eye.z).normalize().scale(dirScale);
            } else if (mob.getTarget() != null) {
                dir = mob.getTarget().getEyePosition().subtract(eye).normalize().scale(dirScale);
            }
        } else
            dir = entity.getLookAngle().scale(dirScale);
        Vec3 off = dir.normalize().scale(-dirScale + 3);
        summoner.setPos(eye.x + off.x, eye.y, eye.z + off.z);
        summoner.setTarget(eye.x + dir.x, eye.y + dir.y, eye.z + dir.z);
        level.addFreshEntity(summoner);
        return true;
    }

    @Override
    public int rpCost() {
        return 7;
    }
}
