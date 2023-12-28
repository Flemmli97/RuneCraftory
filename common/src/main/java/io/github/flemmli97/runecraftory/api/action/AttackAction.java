package io.github.flemmli97.runecraftory.api.action;

import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.item.IAOEWeapon;
import io.github.flemmli97.tenshilib.common.utils.AOEWeaponHandler;
import io.github.flemmli97.tenshilib.platform.registry.CustomRegistryEntry;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public abstract class AttackAction extends CustomRegistryEntry<AttackAction> {

    public static void moveRelative(LivingEntity entity, float amount, Vec3 relative, float speed, boolean overwrite) {
        if (overwrite)
            entity.setDeltaMovement(Vec3.ZERO);
        entity.moveRelative(amount * speed, relative);
    }

    public static Vec3 fromRelativeVector(Entity entity, Vec3 relative) {
        return fromRelativeVector(entity.getYRot(), relative);
    }

    public static Vec3 fromRelativeVector(float yRot, Vec3 relative) {
        Vec3 vec3 = relative.normalize();
        float f = Mth.sin(yRot * Mth.DEG_TO_RAD);
        float g = Mth.cos(yRot * Mth.DEG_TO_RAD);
        return new Vec3(vec3.x * g - vec3.z * f, vec3.y, vec3.z * g + vec3.x * f);
    }

    public static void sendMotionUpdate(LivingEntity entity) {
        if (entity instanceof ServerPlayer serverPlayer)
            serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(entity));
    }

    public static boolean canPerform(LivingEntity entity, EnumSkills skill, int requiredLvl) {
        if (!(entity instanceof Player player))
            return false;
        return player.isCreative() || Platform.INSTANCE.getPlayerData(player).map(d -> d.getSkillLevel(skill).getLevel() >= requiredLvl).orElse(false);
    }

    public static void attack(LivingEntity entity, ItemStack stack) {
        if (entity instanceof Player player) {
            if (stack.getItem() instanceof IAOEWeapon weapon)
                AOEWeaponHandler.onAOEWeaponSwing(player, stack, weapon);
        }
    }

    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        return null;
    }

    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {

    }

    public void onSetup(LivingEntity entity, WeaponHandler handler) {

    }

    public void onStart(LivingEntity entity, WeaponHandler handler) {

    }

    public void onEnd(LivingEntity entity, WeaponHandler handler) {

    }

    public boolean canOverride(LivingEntity entity, WeaponHandler handler) {
        return false;
    }

    public boolean isInvulnerable(LivingEntity entity, WeaponHandler handler) {
        return false;
    }

    public AttackChain attackChain(LivingEntity entity, int chain) {
        return AttackChain.DEFAULT;
    }

    public boolean disableItemSwitch() {
        return true;
    }

    public boolean disableMovement() {
        return true;
    }

    public Pose getPose(LivingEntity entity, WeaponHandler handler) {
        return null;
    }

    /**
     * Record for handling attack chains
     *
     * @param maxChains      Max amount of possible chains
     * @param chainFrameTime Timeframe after finished animation to click for next chain attack
     */
    public record AttackChain(int maxChains, int chainFrameTime) {
        public static final AttackChain DEFAULT = new AttackChain(1, 0);
    }
}
