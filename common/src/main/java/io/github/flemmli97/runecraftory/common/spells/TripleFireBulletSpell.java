package io.github.flemmli97.runecraftory.common.spells;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityBullet;
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

public class TripleFireBulletSpell extends Spell {

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
        boolean rp = !(entity instanceof Player player) || Platform.INSTANCE.getPlayerData(player).map(data -> LevelCalc.useRP(player, data, this.rpCost(), stack.getItem() instanceof ItemStaffBase, false, true)).orElse(false);
        if (!rp)
            return false;
        EntityBullet projectile = new EntityBullet(level, entity);
        Vec3 dir;
        if (entity instanceof Mob mob && mob.getTarget() != null) {
            dir = (new Vec3(mob.getTarget().getX() - mob.getX(), mob.getTarget().getY(0.2) - mob.getY(), mob.getTarget().getZ() - mob.getZ()));
        } else {
            dir = entity.getLookAngle();
        }
        projectile.setElement(EnumElement.FIRE);
        projectile.setStraight();
        projectile.shoot(dir.x, dir.y, dir.z, 1, 0);
        projectile.setDamageMultiplier(0.9f + lvl * 0.05f);
        level.addFreshEntity(projectile);

        Vec3 up = entity.getUpVector(1);
        for (float y = -15; y <= 15; y += 30) {
            Quaternion quaternion = new Quaternion(new Vector3f(up), y, true);
            Vector3f newDir = new Vector3f(dir);
            newDir.transform(quaternion);
            EntityBullet other = new EntityBullet(level, entity);
            other.setStraight();
            other.setElement(EnumElement.FIRE);
            other.setDamageMultiplier(0.9f + lvl * 0.05f);
            other.shoot(newDir.x(), newDir.y(), newDir.z(), 1, 0);
            level.addFreshEntity(other);
        }
        return true;
    }

    @Override
    public int rpCost() {
        return 0;
    }
}