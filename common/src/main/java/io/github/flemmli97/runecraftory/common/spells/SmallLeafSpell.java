package io.github.flemmli97.runecraftory.common.spells;

import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.entities.misc.EntitySmallRaccoonLeaf;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class SmallLeafSpell extends Spell {

    private final int amount;

    public SmallLeafSpell(int amount) {
        this.amount = amount;
    }

    @Override
    public void update(Player player, ItemStack stack) {
    }

    @Override
    public void levelSkill(ServerPlayer player) {
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.EARTH, 4));
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
            Vec3 pos = new EntitySmallRaccoonLeaf(level, entity).position();
            direct = EntityUtil.getStraightProjectileTarget(pos, mob.getTarget()).subtract(pos);
        } else
            direct = Vec3.directionFromRotation(entity.getXRot(), entity.getYRot());
        float degs = this.amount < 5 ? 25 : 35;
        for (Vector3f vec : RayTraceUtils.rotatedVecs(direct, MathUtils.normalY, -degs, degs, degs * 2 / this.amount)) {
            EntitySmallRaccoonLeaf leaf = new EntitySmallRaccoonLeaf(level, entity);
            leaf.setPos(leaf.getX() + vec.x() * 0.1, leaf.getY() + vec.y() * 0.1, leaf.getZ() + vec.z() * 0.1);
            leaf.setDamageMultiplier(0.85f + lvl * 0.1f);
            leaf.shoot(vec.x(), vec.y(), vec.z(), 0.75f, 0);
            level.addFreshEntity(leaf);
        }
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ARROW_SHOOT, entity.getSoundSource(), 1.0f, 1.2f + level.getRandom().nextFloat() * 0.1f);
        return true;
    }

    @Override
    public int rpCost() {
        return 120;
    }
}
