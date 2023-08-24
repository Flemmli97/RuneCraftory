package io.github.flemmli97.runecraftory.common.attachment.player;

import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.api.enums.EnumWeaponType;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemAxeBase;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.item.IAOEWeapon;
import io.github.flemmli97.tenshilib.common.utils.AOEWeaponHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class AttackAction {

    private static final Map<String, AttackAction> MAP = new HashMap<>();

    public static final AttackAction NONE = register("none", new Builder(null));

    public static final AttackAction SHORT_SWORD_USE = register("short_sword_use", new Builder((player, data) -> new AnimatedAction(16 + 1, 6, "short_sword")).doAtStart(swing(false)).disableMovement());
    public static final AttackAction LONGSWORD_USE = register("long_sword_use", new Builder((player, data) -> new AnimatedAction(16 + 1, 5, "long_sword")).doAtStart(swing(false)).disableMovement());
    //
    public static final AttackAction SPEAR_USE_FINISHER = register("spear_use_finisher", new Builder((player, data) -> new AnimatedAction(10 + 1, 5, "spear")).doAtStart(swing(false)).doWhileAction(simpleSwingAttackExecuter()).noAnimation().disableMovement());
    public static final AttackAction SPEAR_USE = register("spear_use", new Builder((player, data) -> new AnimatedAction(5 + 1, 3, "spear")).doAtStart(swing(false)).allowSelfOverride((p, w) -> {
        AnimatedAction anim = w.getCurrentAnim();
        return anim == null || anim.getTick() >= 5;
    }).setFollowingAnim((p, a) -> SPEAR_USE_FINISHER).noAnimation().setMaxConsecutive(p -> 20, p -> 6).disableMovement());

    public static final AttackAction HAMMER_AXE_USE = register("hammer_axe_use", new Builder((player, data) -> new AnimatedAction(20 + 1, 12, "hammer_axe")).doAtStart(swing(false)).disableMovement().doWhileAction((player, stack, data, anim) -> ItemAxeBase.movePlayer(player).accept(anim)));
    public static final AttackAction DUAL_USE = register("dual_blade_use", new Builder((player, data) -> new AnimatedAction(19 + 1, 7, "dual_blades")).doAtStart(swing(true)).disableMovement());
    public static final AttackAction GLOVE_USE = register("glove_use", new Builder((player, data) -> new AnimatedAction(50 + 1, 4, "glove")).noAnimation().disableMovement().doAtStart(p -> p.maxUpStep += 0.5).doAtEnd(p -> p.maxUpStep -= 0.5).doWhileAction(((player, stack, data, anim) -> {
        if (player instanceof ServerPlayer serverPlayer) {
            Vec3 look = player.getLookAngle();
            Vec3 move = new Vec3(look.x, 0.0, look.z).normalize().scale(player.isOnGround() ? 0.6 : 0.3).add(0, player.getDeltaMovement().y, 0);
            player.setDeltaMovement(move);
            player.hurtMarked = true;
            if (anim.getTick() % 4 == 0) {
                List<LivingEntity> list = player.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(1.0));
                boolean flag = false;
                for (LivingEntity e : list) {
                    if (e != player) {
                        CombatUtils.playerAttackWithItem(player, e, stack, 0.5f, false, false, false);
                        flag = true;
                    }
                }
                if (flag) {
                    LevelCalc.levelSkill(serverPlayer, data, EnumSkills.DUAL, 2);
                }
            }
        }
    })));
    public static final AttackAction STAFF_USE = register("staff_use", new Builder((player, data) -> new AnimatedAction(16 + 1, 4, "staff")).disableMovement());

    public static final AttackAction TOOL_AXE_USE = register("tool_axe", new Builder((player, data) -> new AnimatedAction(10 + 1, 2, "tool_axe")).doAtStart(swing(false)).noAnimation().disableMovement().setMaxConsecutive(p -> 3, p -> 15));
    public static final AttackAction TOOL_HAMMER_USE = register("tool_hammer", new Builder((player, data) -> new AnimatedAction(10 + 1, 2, "tool_hammer")).doAtStart(swing(false)).noAnimation().disableMovement().setMaxConsecutive(p -> 3, p -> 15));
    public static final AttackAction FIREBALL_USE = register("fireball_use", new Builder((player, data) -> new AnimatedAction(16 + 1, 4, "staff")).doAtStart(swing(false)).allowSelfOverride((p, w) -> {
        AnimatedAction anim = w.getCurrentAnim();
        return anim == null || anim.getTick() >= anim.getAttackTime();
    }).disableMovement().setMaxConsecutive(p -> 3, p -> 8));
    public static final AttackAction FIREBALL_BIG_USE = register("fireball_big_use", new Builder((player, data) -> new AnimatedAction(16 + 1, 4, "staff")).doAtStart(swing(false)).allowSelfOverride((p, w) -> {
        AnimatedAction anim = w.getCurrentAnim();
        return anim == null || anim.getTick() >= anim.getAttackTime();
    }).disableMovement().setMaxConsecutive(p -> 2, p -> 8));

    public static final AttackAction SHORT_SWORD = register("short_sword", new Builder((player, data) -> {
        int defaultLength = EnumWeaponType.SHORTSWORD.defaultWeaponSpeed;
        float cooldown = ItemNBT.cooldown(player, player.getMainHandItem());
        float speed = cooldown / defaultLength;
        return AnimatedAction.builder(defaultLength + 1, "short_sword").speed(speed).marker(4).build();
    }).doWhileAction(((player, stack, data, anim) -> {
                if (!player.level.isClientSide) {
                    if (anim.canAttack() && stack.getItem() instanceof IAOEWeapon weapon) {
                        AOEWeaponHandler.onAOEWeaponSwing(player, stack, weapon);
                    }
                    if (anim.getTick() > anim.getAttackTime()) {
                        switch (data.getWeaponHandler().getCurrentCount()) {
                            case 1, 2 -> player.moveRelative(0.07f, new Vec3(0, 0, 1));
                            case 4 -> player.moveRelative(0.14f, new Vec3(0, 2.5, 1));
                            default -> {
                            }
                        }
                        player.hurtMarked = true;
                    }
                }
            })).doAtEnd(p -> {
                if (Platform.INSTANCE.getPlayerData(p).map(d -> d.getWeaponHandler().getCurrentCount() < 4).orElse(true))
                    p.setDeltaMovement(new Vec3(0, p.getDeltaMovement().y, 0));
                p.hurtMarked = true;
            }).doAtStart(swing(false))
            .setMaxConsecutive(p -> Platform.INSTANCE.getPlayerData(p).map(d -> d.getSkillLevel(EnumSkills.SHORTSWORD).getLevel() >= 20 ? 6 : 5).orElse(5), p -> 10)
            .disableItemSwitch().disableMovement());
    public static final AttackAction LONG_SWORD = register("long_sword", new Builder((player, data) -> {
        int defaultLength = EnumWeaponType.SHORTSWORD.defaultWeaponSpeed;
        float cooldown = ItemNBT.cooldown(player, player.getMainHandItem());
        float speed = cooldown / defaultLength;
        return AnimatedAction.builder(defaultLength + 1, "long_sword").speed(speed).marker(4).build();
    }).doWhileAction(((player, stack, data, anim) -> {
                if (!player.level.isClientSide) {
                    if (anim.canAttack() && stack.getItem() instanceof IAOEWeapon weapon) {
                        AOEWeaponHandler.onAOEWeaponSwing(player, stack, weapon);
                    }
                    if (anim.getTick() > anim.getAttackTime()) {
                        switch (data.getWeaponHandler().getCurrentCount()) {
                            case 1, 2 -> player.moveRelative(0.07f, new Vec3(0, 0, 1));
                            case 4 -> player.moveRelative(0.16f, new Vec3(0, 2, 1));
                            default -> {
                            }
                        }
                        player.hurtMarked = true;
                    }
                }
            })).doAtEnd(p -> {
                if (Platform.INSTANCE.getPlayerData(p).map(d -> d.getWeaponHandler().getCurrentCount() < 4).orElse(true))
                    p.setDeltaMovement(new Vec3(0, p.getDeltaMovement().y, 0));
                p.hurtMarked = true;
            }).doAtStart(swing(false))
            .setMaxConsecutive(p -> Platform.INSTANCE.getPlayerData(p).map(d -> d.getSkillLevel(EnumSkills.LONGSWORD).getLevel() >= 20 ? 4 : 3).orElse(3), p -> 10)
            .disableItemSwitch().disableMovement());

    public final BiFunction<Player, WeaponHandler, AnimatedAction> anim;
    public final BiFunction<Player, WeaponHandler, AttackAction> nextAction;
    /**
     * Static handler for this action
     */
    public final ActiveActionHandler attackExecuter;
    public final Consumer<Player> onStart;
    public final Consumer<Player> onEnd;

    public final Function<Player, Integer> maxConsecutive, timeFrame;
    public final boolean disableItemSwitch, disableMovement;
    //Here for now till all have animations
    public final boolean disableAnimation;
    public final BiFunction<Player, WeaponHandler, Boolean> canOverride;

    private final String id;

    private AttackAction(BiFunction<Player, WeaponHandler, AnimatedAction> anim, ActiveActionHandler attackExecuter, Consumer<Player> onStart, Consumer<Player> onEnd,
                         Function<Player, Integer> maxConsecutive, Function<Player, Integer> timeFrame, boolean disableItemSwitch, boolean disableMovement, boolean disableAnimation,
                         BiFunction<Player, WeaponHandler, Boolean> canOverride, String id, BiFunction<Player, WeaponHandler, AttackAction> nextAction) {
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
        this.nextAction = nextAction;
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

    public static AttackAction.ActiveActionHandler simpleSwingAttackExecuter() {
        return (player, stack, data, anim) -> {
            if (!player.level.isClientSide && anim.canAttack() && stack.getItem() instanceof IAOEWeapon weapon)
                AOEWeaponHandler.onAOEWeaponSwing(player, stack, weapon);
        };
    }

    public static Consumer<Player> swing(boolean bothHands) {
        return player -> {
            player.swing(InteractionHand.MAIN_HAND);
            if (bothHands)
                player.swing(InteractionHand.OFF_HAND);
            player.resetAttackStrengthTicker();
        };
    }

    public String getId() {
        return this.id;
    }

    public interface ActiveActionHandler {
        /**
         * @param player The player
         * @param stack  The itemstack which the player used to activate this action
         * @param data   The players playerdata (So don't need to fetch again)
         * @param anim   Active animation
         */
        void handle(Player player, ItemStack stack, PlayerData data, AnimatedAction anim);
    }

    public static class Builder {

        private final BiFunction<Player, WeaponHandler, AnimatedAction> anim;
        private BiFunction<Player, WeaponHandler, AttackAction> nextAction;
        private ActiveActionHandler attackExecuter;
        private Consumer<Player> onStart;
        private Consumer<Player> onEnd;
        private BiFunction<Player, WeaponHandler, Boolean> canOverride;
        private Function<Player, Integer> maxConsecutive;
        private Function<Player, Integer> timeFrame;
        private boolean disableItemSwitch, disableMovement, disableAnimation;

        public Builder(BiFunction<Player, WeaponHandler, AnimatedAction> anim) {
            this.anim = anim;
        }

        public Builder doWhileAction(ActiveActionHandler attackExecuter) {
            this.attackExecuter = attackExecuter;
            return this;
        }

        public Builder doAtStart(Consumer<Player> onStart) {
            this.onStart = onStart;
            return this;
        }

        public Builder doAtEnd(Consumer<Player> onEnd) {
            this.onEnd = onEnd;
            return this;
        }

        public Builder setMaxConsecutive(Function<Player, Integer> amount, Function<Player, Integer> timeFrame) {
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

        public Builder allowSelfOverride(BiFunction<Player, WeaponHandler, Boolean> canOverride) {
            this.canOverride = canOverride;
            return this;
        }

        public Builder setFollowingAnim(BiFunction<Player, WeaponHandler, AttackAction> nextAction) {
            this.nextAction = nextAction;
            return this;
        }

        private AttackAction build(String id) {
            return new AttackAction(this.anim, this.attackExecuter, this.onStart, this.onEnd, this.maxConsecutive, this.timeFrame, this.disableItemSwitch, this.disableMovement, this.disableAnimation, this.canOverride, id, this.nextAction);
        }
    }
}
