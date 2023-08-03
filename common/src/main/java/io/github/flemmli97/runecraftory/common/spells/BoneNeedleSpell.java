package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityBoneNeedle;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class BoneNeedleSpell extends Spell {

    @Override
    public void update(Player player, ItemStack stack) {
    }

    @Override
    public void levelSkill(ServerPlayer player) {
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
        Vec3 direct;
        if (entity instanceof Mob mob && mob.getTarget() != null) {
            Vec3 pos = new EntityBoneNeedle(level, entity).position().add(0, -entity.getBbHeight() * 0.15, 0);
            direct = EntityUtil.getStraightProjectileTarget(pos, mob.getTarget()).subtract(pos);
        } else
            direct = Vec3.directionFromRotation(entity.getXRot(), entity.yBodyRot);
        Vec3 side = Vec3.directionFromRotation(0, entity.yBodyRot - 90);
        side = new Vec3(side.x(), 0, side.z()).normalize().add(0, -entity.getBbHeight() * 0.15, 0);
        double offset = -2;
        double max = Math.abs(offset);
        double inc = max * 2 / 16;
        while (offset <= max) {
            EntityBoneNeedle needle = new EntityBoneNeedle(level, entity);
            Vec3 direction = MathUtils.rotate(MathUtils.normalY, direct, (float) (Mth.DEG_TO_RAD * 9 * offset));
            needle.shoot(direction.x(), direction.y(), direction.z(), entity.getRandom().nextFloat() * 0.1f + 0.9f, 2);
            Vec3 random = needle.position().add(side.multiply(offset * 1.2, 1, offset * 1.2));
            offset += entity.getRandom().nextDouble() * inc * 0.5 + inc * 0.5;
            needle.setPos(random);
            needle.setDamageMultiplier(0.8f + lvl * 0.1f);
            level.addFreshEntity(needle);
        }
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ARROW_SHOOT, entity.getSoundSource(), 1.0f, 1.2f + level.getRandom().nextFloat() * 0.1f);
        return true;
    }

    @Override
    public int rpCost() {
        return 220;
    }
}
