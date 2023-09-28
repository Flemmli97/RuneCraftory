package io.github.flemmli97.runecraftory.api.action;

import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.item.IAOEWeapon;
import io.github.flemmli97.tenshilib.common.utils.AOEWeaponHandler;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class AttackAction {

    private static final Map<String, AttackAction> MAP = new HashMap<>();

    public final BiFunction<LivingEntity, Integer, AnimatedAction> anim;
    /**
     * Static handler for this action
     */
    public final ActiveActionHandler attackExecuter;
    public final BiConsumer<LivingEntity, WeaponHandler> onStart;
    public final BiConsumer<LivingEntity, WeaponHandler> onEnd;

    public final BiFunction<LivingEntity, WeaponHandler, Boolean> isInvulnerable;

    public final Function<LivingEntity, Integer> maxConsecutive, timeFrame;
    public final boolean disableItemSwitch, disableMovement;
    //Here for now till all have animations
    public final boolean disableAnimation;
    public final BiFunction<LivingEntity, WeaponHandler, Boolean> canOverride;

    public final BiFunction<LivingEntity, WeaponHandler, Pose> withPose;

    private final String id;

    private AttackAction(BiFunction<LivingEntity, Integer, AnimatedAction> anim, ActiveActionHandler attackExecuter, BiConsumer<LivingEntity, WeaponHandler> onStart, BiConsumer<LivingEntity, WeaponHandler> onEnd,
                         Function<LivingEntity, Integer> maxConsecutive, Function<LivingEntity, Integer> timeFrame, boolean disableItemSwitch, boolean disableMovement, boolean disableAnimation,
                         BiFunction<LivingEntity, WeaponHandler, Boolean> canOverride, String id, BiFunction<LivingEntity, WeaponHandler, Boolean> isInvulnerable, BiFunction<LivingEntity, WeaponHandler, Pose> withPose) {
        this.anim = anim == null ? (player, data) -> null : anim;
        this.attackExecuter = attackExecuter;
        this.onStart = onStart;
        this.onEnd = onEnd;
        this.maxConsecutive = maxConsecutive == null ? p -> 1 : maxConsecutive;
        this.timeFrame = timeFrame;
        this.disableItemSwitch = disableItemSwitch;
        this.disableMovement = disableMovement;
        this.disableAnimation = disableAnimation;
        this.canOverride = canOverride;
        this.id = id;
        this.isInvulnerable = isInvulnerable;
        this.withPose = withPose;
    }

    public static AttackAction register(String id, AttackAction.Builder builder) {
        AttackAction action = builder.build(id);
        MAP.put(id, action);
        return action;
    }

    @Nullable
    public static AttackAction get(String id) {
        return MAP.get(id);
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

    public String getId() {
        return this.id;
    }

    public interface ActiveActionHandler {
        /**
         * @param player        The player
         * @param stack         The itemstack which the player used to activate this action
         * @param weaponHandler The players playerdata (So don't need to fetch again)
         * @param anim          Active animation
         */
        void handle(LivingEntity player, ItemStack stack, WeaponHandler weaponHandler, AnimatedAction anim);
    }

    public static class Builder {

        private final BiFunction<LivingEntity, Integer, AnimatedAction> anim;
        private ActiveActionHandler attackExecuter;
        private BiConsumer<LivingEntity, WeaponHandler> onStart;
        private BiConsumer<LivingEntity, WeaponHandler> onEnd;
        private BiFunction<LivingEntity, WeaponHandler, Boolean> canOverride;
        private Function<LivingEntity, Integer> maxConsecutive;
        private Function<LivingEntity, Integer> timeFrame;
        private boolean disableItemSwitch, disableMovement, disableAnimation;
        private BiFunction<LivingEntity, WeaponHandler, Boolean> isInvulnerable;
        private BiFunction<LivingEntity, WeaponHandler, Pose> withPose;

        public Builder(BiFunction<LivingEntity, Integer, AnimatedAction> anim) {
            this.anim = anim;
        }

        public Builder doWhileAction(ActiveActionHandler attackExecuter) {
            this.attackExecuter = attackExecuter;
            return this;
        }

        public Builder doAtStart(BiConsumer<LivingEntity, WeaponHandler> onStart) {
            this.onStart = onStart;
            return this;
        }

        public Builder doAtEnd(BiConsumer<LivingEntity, WeaponHandler> onEnd) {
            this.onEnd = onEnd;
            return this;
        }

        public Builder setMaxConsecutive(Function<LivingEntity, Integer> amount, Function<LivingEntity, Integer> timeFrame) {
            this.maxConsecutive = amount;
            this.timeFrame = timeFrame;
            return this;
        }

        public Builder disableItemSwitch() {
            this.disableItemSwitch = true;
            return this;
        }

        public Builder disableMovement() {
            this.disableMovement = true;
            return this;
        }

        public Builder noAnimation() {
            this.disableAnimation = true;
            return this;
        }

        public Builder allowSelfOverride(BiFunction<LivingEntity, WeaponHandler, Boolean> canOverride) {
            this.canOverride = canOverride;
            return this;
        }

        public Builder setInvulnerability(BiFunction<LivingEntity, WeaponHandler, Boolean> isInvulnerable) {
            this.isInvulnerable = isInvulnerable;
            return this;
        }

        public Builder withPose(BiFunction<LivingEntity, WeaponHandler, Pose> poseHandler) {
            this.withPose = poseHandler;
            return this;
        }

        private AttackAction build(String id) {
            return new AttackAction(this.anim, this.attackExecuter, this.onStart, this.onEnd, this.maxConsecutive, this.timeFrame, this.disableItemSwitch, this.disableMovement, this.disableAnimation, this.canOverride, id, this.isInvulnerable, this.withPose);
        }
    }
}
