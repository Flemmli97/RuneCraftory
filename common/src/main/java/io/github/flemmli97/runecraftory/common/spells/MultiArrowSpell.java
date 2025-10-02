package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.MobArrowEntity;
import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemComponentUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

public class MultiArrowSpell extends Spell {

    public final int amount;
    public final float angle;

    public MultiArrowSpell(int amount, float angle) {
        this.amount = amount;
        this.angle = angle;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        Vec3 pos = entity.position().add(0, entity.getEyeHeight() - 0.1, 0);
        Vec3 dir;
        float f = 1;
        if (stack.getItem() instanceof ItemStaffBase)
            f = BowItem.getPowerForTime(72000 - entity.getUseItemRemainingTicks());
        if (entity instanceof Mob mob && mob.getTarget() != null) {
            Vec3 targetPos = EntityUtils.getStraightProjectileTarget(pos, mob.getTarget());
            dir = new Vec3(targetPos.x() - pos.x(), targetPos.y() - pos.y(), targetPos.z() - pos.z());
        } else {
            dir = entity.getViewVector(1);
        }
        Vec3 up = new Vec3(0, 1, 0);
        Vector3d dir3d = new Vector3d(dir.x(), dir.y(), dir.z());
        float angle = -this.angle;
        float inc = (this.angle * 2) / (this.amount - 1);
        for (float y = angle; y <= this.angle; y += inc) {
            Vector3d newDir = dir3d.rotateAxis(y * Mth.DEG_TO_RAD, up.x(), up.y(), up.z(), new Vector3d());
            MobArrowEntity arrow = new MobArrowEntity(level, entity, CombatUtils.getAbilityDamageBonus(lvl, this));
            arrow.setRemainingFireTicks(ItemComponentUtils.getElement(stack) == ItemElement.FIRE ? 200 : 0);
            arrow.shoot(newDir.x(), newDir.y(), newDir.z(), f * 1.5F, 1.0F);
            level.addFreshEntity(arrow);
        }
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ARROW_SHOOT, entity.getSoundSource(), 1.0f, 1.0f / (level.getRandom().nextFloat() * 0.4f + 1.2f) + f * 0.5f);
        return true;
    }
}
