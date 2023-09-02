package io.github.flemmli97.runecraftory.common.attachment.player;

import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.api.enums.EnumWeaponType;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemAxeBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemSpearBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.item.IAOEWeapon;
import io.github.flemmli97.tenshilib.common.utils.AOEWeaponHandler;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
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

    //Short sword attack sequence
    public static final AttackAction SHORT_SWORD = register("short_sword", new Builder((player, data) -> {
        int count = Platform.INSTANCE.getPlayerData(player).map(d -> d.getWeaponHandler().getCurrentCount()).orElse(1) + 1;
        int attack = 4;
        int defaultLength = EnumWeaponType.SHORTSWORD.defaultWeaponSpeed;
        switch (count) {
            case 1, 3 -> attack = (int) Math.ceil(0.32 * 20);
            case 2 -> attack = (int) Math.ceil(0.24 * 20);
            case 4 -> {
                defaultLength = (int) Math.ceil(0.6 * 20);
                attack = (int) Math.ceil(0.2 * 20);
            }
            case 5 -> attack = (int) Math.ceil(0.28 * 20);
            case 6 -> {
            }
        }
        float speed = (float) (ItemNBT.attackSpeedModifier(player));
        return AnimatedAction.builder(defaultLength + 1, "short_sword_" + count).speed(speed).marker(attack).build();
    }).allowSelfOverride((p, w) -> switch (w.getCurrentCount()) {
                case 1, 2, 5, 3 -> w.getCurrentAnim().isPastTick(0.32);
                case 4 -> w.getCurrentAnim().isPastTick(0.24) && !w.getCurrentAnim().isPastTick(0.48);
                default -> false;
            }).doWhileAction(((player, stack, data, anim) -> {
                if (!player.level.isClientSide) {
                    if (anim.canAttack() && stack.getItem() instanceof IAOEWeapon weapon) {
                        AOEWeaponHandler.onAOEWeaponSwing(player, stack, weapon);
                        player.swing(InteractionHand.MAIN_HAND, true);
                    }
                    switch (data.getWeaponHandler().getCurrentCount()) {
                        case 1 -> {
                            if (anim.isAtTick(0.28)) {
                                player.moveRelative(0.4f, new Vec3(0, 0, 1));
                                sendMotionUpdate(player);
                            }
                        }
                        case 2 -> {
                            if (anim.isAtTick(0.16)) {
                                player.moveRelative(0.4f, new Vec3(0, 0, 1));
                                sendMotionUpdate(player);
                            }
                        }
                        case 3 -> {
                            if (anim.isAtTick(0.16)) {
                                player.moveRelative(0.2f, new Vec3(0, 0, 1));
                                sendMotionUpdate(player);
                            }
                        }
                        case 4 -> {
                            if (anim.isAtTick(0.2)) {
                                player.moveRelative(0.8f, new Vec3(0, 2.2, 1.4));
                                sendMotionUpdate(player);
                            }
                        }
                        case 5 -> {
                            if (anim.isAtTick(0.04)) {
                                player.setDeltaMovement(new Vec3(0, -0.2, 0));
                                sendMotionUpdate(player);
                            }

                        }
                        case 6 -> {
                        }
                    }
                }
            }))
            .setMaxConsecutive(p -> Platform.INSTANCE.getPlayerData(p).map(d -> d.getSkillLevel(EnumSkills.SHORTSWORD).getLevel() >= 20 ? 6 : 5).orElse(5), p -> 0)
            .disableItemSwitch().disableMovement());
    public static final AttackAction SHORT_SWORD_USE = register("short_sword_use", new Builder((player, data) -> new AnimatedAction(16 + 1, 6, "short_sword_use")).disableMovement());

    public static final AttackAction LONG_SWORD = register("long_sword", new Builder((player, data) -> {
        int defaultLength = EnumWeaponType.LONGSWORD.defaultWeaponSpeed;
        int count = Platform.INSTANCE.getPlayerData(player).map(d -> d.getWeaponHandler().getCurrentCount()).orElse(1) + 1;
        int attack = (int) Math.ceil(0.48 * 20);
        if (count == 3)
            attack = (int) Math.ceil(0.56 * 20);
        float speed = (float) (ItemNBT.attackSpeedModifier(player));
        return AnimatedAction.builder(defaultLength + 1, "long_sword_" + count).speed(speed).marker(attack).build();
    }).allowSelfOverride((p, w) -> switch (w.getCurrentCount()) {
                case 1, 2, 3 -> w.getCurrentAnim().isPastTick(0.56);
                default -> false;
            }).doWhileAction(((player, stack, data, anim) -> {
                if (!player.level.isClientSide) {
                    if (anim.canAttack() && stack.getItem() instanceof IAOEWeapon weapon) {
                        AOEWeaponHandler.onAOEWeaponSwing(player, stack, weapon);
                        player.swing(InteractionHand.MAIN_HAND, true);
                    }
                    switch (data.getWeaponHandler().getCurrentCount()) {
                        case 2 -> {
                            if (anim.isAtTick(0.4)) {
                                player.moveRelative(0.6f, new Vec3(0, 0, 1));
                                sendMotionUpdate(player);
                            }
                        }
                        case 3 -> {
                            if (anim.isAtTick(0.44)) {
                                player.moveRelative(0.6f, new Vec3(0, 0, 1));
                                sendMotionUpdate(player);
                            }
                        }
                        case 4 -> {
                        }
                    }
                }
            }))
            .setMaxConsecutive(p -> Platform.INSTANCE.getPlayerData(p).map(d -> d.getSkillLevel(EnumSkills.LONGSWORD).getLevel() >= 20 ? 4 : 3).orElse(3), p -> 0)
            .disableItemSwitch().disableMovement());
    public static final AttackAction LONGSWORD_USE = register("long_sword_use", new Builder((player, data) -> new AnimatedAction(16 + 1, 5, "long_sword_use")).disableMovement());

    public static final AttackAction SPEAR = register("spear", new Builder((player, data) -> {
        int defaultLength = EnumWeaponType.LONGSWORD.defaultWeaponSpeed;
        int count = Platform.INSTANCE.getPlayerData(player).map(d -> d.getWeaponHandler().getCurrentCount()).orElse(1) + 1;
        int attack = (int) Math.ceil(0.36 * 20);
        if (count == 2) {
            defaultLength = (int) Math.ceil(0.52 * 20);
            attack = (int) Math.ceil(0.24 * 20);
        }
        float speed = (float) (ItemNBT.attackSpeedModifier(player));
        return AnimatedAction.builder(defaultLength + 1, "spear_" + count).speed(speed).marker(attack).build();
    }).allowSelfOverride((p, w) -> switch (w.getCurrentCount()) {
                case 1, 3, 4 -> w.getCurrentAnim().isPastTick(0.36);
                case 2 -> w.getCurrentAnim().isPastTick(0.32);
                default -> false;
            }).doWhileAction(((player, stack, data, anim) -> {
                if (!player.level.isClientSide) {
                    if (anim.canAttack() && stack.getItem() instanceof IAOEWeapon weapon) {
                        AOEWeaponHandler.onAOEWeaponSwing(player, stack, weapon);
                        player.swing(InteractionHand.MAIN_HAND, true);
                    }
                    switch (data.getWeaponHandler().getCurrentCount()) {
                        case 1, 3, 4 -> {
                            if (anim.isAtTick(0.28)) {
                                player.moveRelative(0.2f, new Vec3(0, 0, 1));
                                sendMotionUpdate(player);
                            }
                        }
                        case 2 -> {
                            if (anim.isAtTick(0.2)) {
                                player.moveRelative(0.4f, new Vec3(0, 0, 1));
                                sendMotionUpdate(player);
                            }
                        }
                        case 5 -> {
                        }
                    }
                }
            }))
            .setMaxConsecutive(p -> Platform.INSTANCE.getPlayerData(p).map(d -> d.getSkillLevel(EnumSkills.SPEAR).getLevel() >= 20 ? 5 : 4).orElse(4), p -> 0)
            .disableItemSwitch().disableMovement());
    public static final AttackAction SPEAR_USE_FINISHER = register("spear_use_finisher", new Builder((player, data) -> new AnimatedAction(16 + 1, 10, "spear_use_finisher")).doWhileAction(((player, stack, data, anim) -> {
        if (anim.canAttack() && player instanceof ServerPlayer serverPlayer && stack.getItem() instanceof ItemSpearBase spear) {
            spear.useSpear(serverPlayer, data, stack);
        }
    })).disableMovement());
    public static final AttackAction SPEAR_USE = register("spear_use", new Builder((player, data) -> {
        int count = Platform.INSTANCE.getPlayerData(player).map(d -> d.getWeaponHandler().getCurrentCount()).orElse(1) + 1;
        return count > 1 ? new AnimatedAction((int) Math.ceil(0.4 * 20) + 1, 3, "spear_use_continue") : new AnimatedAction((int) Math.ceil(0.48 * 20) + 1, 3, "spear_use");
    }).allowSelfOverride((p, w) -> {
        AnimatedAction anim = w.getCurrentAnim();
        return anim == null || (anim.getID().equals("spear_use_continue") ? anim.isPastTick(0.2) : anim.isPastTick(0.28));
    }).setFollowingAnim((p, a) -> SPEAR_USE_FINISHER).setMaxConsecutive(p -> 20, p -> 6).disableMovement());

    public static final AttackAction HAMMER_AXE = register("hammer_axe", new Builder((player, data) -> {
        int defaultLength = EnumWeaponType.HAXE.defaultWeaponSpeed;
        int count = Platform.INSTANCE.getPlayerData(player).map(d -> d.getWeaponHandler().getCurrentCount()).orElse(1) + 1;
        int attack = (int) Math.ceil(0.6 * 20);
        if (count == 2)
            attack = (int) Math.ceil(0.48 * 20);
        float speed = (float) (ItemNBT.attackSpeedModifier(player));
        return AnimatedAction.builder(defaultLength + 1, "hammer_axe_" + count).speed(speed).marker(attack).build();
    }).allowSelfOverride((p, w) -> switch (w.getCurrentCount()) {
                case 1, 2 -> w.getCurrentAnim().isPastTick(0.64);
                default -> false;
            }).doWhileAction(((player, stack, data, anim) -> {
                if (!player.level.isClientSide) {
                    if (anim.canAttack() && stack.getItem() instanceof IAOEWeapon weapon) {
                        AOEWeaponHandler.onAOEWeaponSwing(player, stack, weapon);
                        player.swing(InteractionHand.MAIN_HAND, true);
                    }
                    if (data.getWeaponHandler().getCurrentCount() == 3) {

                    }
                }
            }))
            .setMaxConsecutive(p -> Platform.INSTANCE.getPlayerData(p).map(d -> d.getSkillLevel(EnumSkills.HAMMERAXE).getLevel() >= 20 ? 3 : 2).orElse(2), p -> 0)
            .disableItemSwitch().disableMovement());
    public static final AttackAction HAMMER_AXE_USE = register("hammer_axe_use", new Builder((player, data) -> new AnimatedAction(20 + 1, 12, "hammer_axe_use")).disableMovement().doWhileAction((player, stack, data, anim) -> ItemAxeBase.movePlayer(player).accept(anim)));

    public static final AttackAction DUAL_BLADES = register("dual_blades", new Builder((player, data) -> {
        int defaultLength = EnumWeaponType.DUAL.defaultWeaponSpeed;
        int count = Platform.INSTANCE.getPlayerData(player).map(d -> d.getWeaponHandler().getCurrentCount()).orElse(1) + 1;
        int attack = (int) Math.ceil(0.4 * 20);
        switch (count) {
            case 1 -> attack = (int) Math.ceil(0.16 * 20);
            case 2 -> attack = (int) Math.ceil(0.2 * 20);
            case 3, 4, 5, 6 -> attack = (int) Math.ceil(0.24 * 20);
            case 7 -> attack = (int) Math.ceil(0.4 * 20);
        }
        float speed = (float) (ItemNBT.attackSpeedModifier(player));
        return AnimatedAction.builder(defaultLength + 1, "dual_blades_" + count).speed(speed).marker(attack).build();
    }).allowSelfOverride((p, w) -> switch (w.getCurrentCount()) {
                case 1, 2, 3, 4, 5, 6 -> w.getCurrentAnim().isPastTick(0.28);
                default -> false;
            }).doWhileAction(((player, stack, data, anim) -> {
                if (!player.level.isClientSide) {
                    if (anim.canAttack() && stack.getItem() instanceof IAOEWeapon weapon && data.getWeaponHandler().getCurrentCount() != 5 && data.getWeaponHandler().getCurrentCount() != 6) {
                        AOEWeaponHandler.onAOEWeaponSwing(player, stack, weapon);
                        player.swing(InteractionHand.MAIN_HAND, true);
                    }
                    switch (data.getWeaponHandler().getCurrentCount()) {
                        case 1 -> {
                            if (anim.isAtTick(0.2)) {
                                player.moveRelative(0.3f, new Vec3(0, 0, 1));
                                sendMotionUpdate(player);
                            }
                        }
                        case 3, 4 -> {
                            if (anim.isAtTick(0.16)) {
                                player.moveRelative(0.3f, new Vec3(0, 0, 1));
                                sendMotionUpdate(player);
                            }
                        }
                        case 5 -> {
                            if (anim.isAtTick(0.2)) {
                                player.moveRelative(0.3f, new Vec3(0, 0, 1));
                                data.getWeaponHandler().setSpinStartRot(player.getYRot() + 20);
                                data.getWeaponHandler().resetHitEntityTracker();
                                sendMotionUpdate(player);
                            }
                            if (anim.isAtTick(0.4)) {
                                data.getWeaponHandler().resetHitEntityTracker();
                            }
                            if (anim.isPastTick(0.2)) {
                                float len = (anim.getLength() - ((int) Math.ceil(0.2 * 20.0D))) / anim.getSpeed();
                                float f = (anim.getTick() - ((int) Math.ceil(0.2 * 20.0D))) / anim.getSpeed();
                                float angleInc = 360 / len;
                                float rot = data.getWeaponHandler().getSpinStartRot();
                                data.getWeaponHandler().addHitEntityTracker(CombatUtils.spinAttackHandler(player, (rot + f * angleInc), (rot + (f + 1) * angleInc), e -> !data.getWeaponHandler().getHitEntityTracker().contains(e)));
                            }
                        }
                        case 6 -> {
                            if (anim.isAtTick(0.08)) {
                                data.getWeaponHandler().setSpinStartRot(player.getYRot() + 20);
                                data.getWeaponHandler().resetHitEntityTracker();
                            }
                            if (anim.isAtTick(0.28)) {
                                player.moveRelative(0.35f, new Vec3(0, 0, 1));
                                data.getWeaponHandler().resetHitEntityTracker();
                                sendMotionUpdate(player);
                            }
                            if (anim.isPastTick(0.12)) {
                                float len = (anim.getLength() - ((int) Math.ceil(0.12 * 20.0D))) / anim.getSpeed();
                                float f = (anim.getTick() - ((int) Math.ceil(0.12 * 20.0D))) / anim.getSpeed();
                                float angleInc = 360 / len;
                                float rot = data.getWeaponHandler().getSpinStartRot();
                                data.getWeaponHandler().addHitEntityTracker(CombatUtils.spinAttackHandler(player, (rot + f * angleInc), (rot + (f + 1) * angleInc), e -> !data.getWeaponHandler().getHitEntityTracker().contains(e)));
                                data.getWeaponHandler().addHitEntityTracker(CombatUtils.spinAttackHandler(player, (rot + 180 + f * angleInc), (rot + 180 + (f + 1) * angleInc), e -> !data.getWeaponHandler().getHitEntityTracker().contains(e)));
                            }
                        }
                        case 7 -> {
                            if (!anim.isPastTick(0.4)) {
                                if (anim.isPastTick(0.24)) {
                                    player.setDeltaMovement(fromRelativeVector(player, new Vec3(0, -1, 0.6))
                                            .scale(3 / ((0.4 - 0.24) * 20 / anim.getSpeed())));
                                    sendMotionUpdate(player);
                                    player.resetFallDistance();
                                } else if (anim.isPastTick(0.08)) {
                                    player.setDeltaMovement(fromRelativeVector(player, new Vec3(0, 1, 0.5))
                                            .scale(2.6 / ((0.24 - 0.08) * 20 / anim.getSpeed())));
                                    sendMotionUpdate(player);
                                }
                            } else {
                                player.resetFallDistance();
                            }
                        }
                    }
                }
            }))
            .setMaxConsecutive(p -> Platform.INSTANCE.getPlayerData(p).map(d -> d.getSkillLevel(EnumSkills.DUAL).getLevel() >= 20 ? 8 : 7).orElse(7), p -> 0)
            .disableItemSwitch().disableMovement());
    public static final AttackAction DUAL_USE = register("dual_blade_use", new Builder((player, data) -> new AnimatedAction(15 + 1, 7, "dual_blades_use")).disableMovement());

    public static final AttackAction GLOVES = register("gloves", new Builder((player, data) -> {
        int defaultLength = EnumWeaponType.GLOVE.defaultWeaponSpeed;
        int count = Platform.INSTANCE.getPlayerData(player).map(d -> d.getWeaponHandler().getCurrentCount()).orElse(1) + 1;
        int attack = (int) Math.ceil(0.4 * 20);
        switch (count) {
            case 1 -> attack = (int) Math.ceil(0.28 * 20);
            case 2, 3 -> attack = (int) Math.ceil(0.24 * 20);
            case 4 -> {
                attack = (int) Math.ceil(0.4 * 20);
                defaultLength = (int) Math.ceil(0.64 * 20);
            }
        }
        float speed = (float) (ItemNBT.attackSpeedModifier(player));
        return AnimatedAction.builder(defaultLength + 1, "glove_" + count).speed(speed).marker(attack).build();
    }).allowSelfOverride((p, w) -> switch (w.getCurrentCount()) {
                case 1, 2, 3 -> w.getCurrentAnim().isPastTick(0.28);
                case 4 -> w.getCurrentAnim().isPastTick(0.48);
                default -> false;
            }).doWhileAction(((player, stack, data, anim) -> {
                if (!player.level.isClientSide) {
                    if (anim.canAttack() && stack.getItem() instanceof IAOEWeapon weapon) {
                        if (data.getWeaponHandler().getCurrentCount() != 4)
                            AOEWeaponHandler.onAOEWeaponSwing(player, stack, weapon);
                        else
                            CombatUtils.attackInAABB(player, new AABB(-1, -1, -1, 1, 1, 1).move(player.position().add(0, 0.2, 0).add(player.getDeltaMovement().normalize().scale(0.4))), null);
                        player.swing(InteractionHand.MAIN_HAND, true);
                    }
                    switch (data.getWeaponHandler().getCurrentCount()) {
                        case 1 -> {
                            if (anim.isAtTick(0.24)) {
                                player.moveRelative(0.3f, new Vec3(0, 0, 1));
                                sendMotionUpdate(player);
                            }
                        }
                        case 2, 3 -> {
                            if (anim.isAtTick(0.24)) {
                                player.moveRelative(0.1f, new Vec3(0, 0, 1));
                                sendMotionUpdate(player);
                            }
                        }
                        case 4 -> {
                            if (!anim.isPastTick(0.44)) {
                                if (anim.isPastTick(0.14)) {
                                    player.setDeltaMovement(fromRelativeVector(player, new Vec3(0, -1, 0.6))
                                            .scale(4 / ((0.44 - 0.14) * 20 / anim.getSpeed())));
                                    sendMotionUpdate(player);
                                    player.resetFallDistance();
                                } else if (anim.isPastTick(0.04)) {
                                    player.setDeltaMovement(fromRelativeVector(player, new Vec3(0, 1, 0.3))
                                            .scale(3 / ((0.14 - 0.04) * 20 / anim.getSpeed())));
                                    sendMotionUpdate(player);
                                }
                            } else if (player.isOnGround()) {
                                player.setDeltaMovement(player.getDeltaMovement().scale(0.01));
                                sendMotionUpdate(player);
                            }
                        }
                    }
                }
            }))
            .setMaxConsecutive(p -> Platform.INSTANCE.getPlayerData(p).map(d -> d.getSkillLevel(EnumSkills.FIST).getLevel() >= 20 ? 5 : 4).orElse(4), p -> 0)
            .disableItemSwitch().disableMovement());
    public static final AttackAction GLOVE_USE = register("glove_use", new Builder((player, data) -> new AnimatedAction(27 + 1, 4, "glove_use")).disableMovement().doAtStart(p -> p.maxUpStep += 0.5).doAtEnd(p -> p.maxUpStep -= 0.5).doWhileAction(((player, stack, data, anim) -> {
        if (player instanceof ServerPlayer serverPlayer) {
            if (!data.getWeaponHandler().getCurrentAnim().isPastTick(0.16))
                return;
            Vec3 look = player.getLookAngle();
            Vec3 move = new Vec3(look.x, 0.0, look.z).normalize().scale(player.isOnGround() ? 0.5 : 0.3).add(0, player.getDeltaMovement().y, 0);
            player.setDeltaMovement(move);
            sendMotionUpdate(player);
            if (anim.speedAdjustedTick() % 4 == 0) {
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

    public static final AttackAction STAFF = register("staff", new Builder((player, data) -> {
        int defaultLength = EnumWeaponType.STAFF.defaultWeaponSpeed;
        float speed = (float) (ItemNBT.attackSpeedModifier(player));
        return AnimatedAction.builder(defaultLength + 1, "staff").speed(speed).marker(9).build();
    }).allowSelfOverride((p, w) -> switch (w.getCurrentCount()) {
                case 1 -> w.getCurrentAnim().isPastTick(0.54);
                case 2 -> w.getCurrentAnim().isPastTick(0.60);
                case 3 -> w.getCurrentAnim().isPastTick(0.64);
                default -> false;
            }).doWhileAction(((player, stack, data, anim) -> {
                if (!player.level.isClientSide) {
                    if (anim.canAttack() && stack.getItem() instanceof ItemStaffBase staff) {
                        EntityHitResult res = RayTraceUtils.calculateEntityFromLook(player, staff.getRange(player, stack));
                        if (res != null && res.getEntity() != null)
                            player.attack(res.getEntity());
                        staff.castBaseSpell(stack, player);
                        player.swing(InteractionHand.MAIN_HAND, true);
                    }
                }
            }))
            .disableItemSwitch().disableMovement());

    public static final AttackAction STAFF_USE = register("staff_use", new Builder((player, data) -> new AnimatedAction(16 + 1, 4, "staff_use")).disableMovement());

    public static final AttackAction TOOL_AXE_USE = register("tool_axe", new Builder((player, data) -> new AnimatedAction(10 + 1, 2, "hammer_axe_use")).noAnimation().disableMovement().setMaxConsecutive(p -> 3, p -> 15));
    public static final AttackAction TOOL_HAMMER_USE = register("tool_hammer", new Builder((player, data) -> new AnimatedAction(10 + 1, 2, "hammer_axe_use")).noAnimation().disableMovement().setMaxConsecutive(p -> 3, p -> 15));
    public static final AttackAction FIREBALL_USE = register("fireball_use", new Builder((player, data) -> new AnimatedAction(16 + 1, 4, "staff_use")).allowSelfOverride((p, w) -> {
        AnimatedAction anim = w.getCurrentAnim();
        return anim == null || anim.isPastTick(anim.getAttackTime());
    }).disableMovement().setMaxConsecutive(p -> 3, p -> 8));
    public static final AttackAction FIREBALL_BIG_USE = register("fireball_big_use", new Builder((player, data) -> new AnimatedAction(16 + 1, 4, "staff_use")).allowSelfOverride((p, w) -> {
        AnimatedAction anim = w.getCurrentAnim();
        return anim == null || anim.isPastTick(anim.getAttackTime());
    }).disableMovement().setMaxConsecutive(p -> 2, p -> 8));

    public static final AttackAction TOOL_ATTACK = register("tool_attack", new Builder((player, data) -> new AnimatedAction(20, 1, "tool_attack")));

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

    public static Vec3 fromRelativeVector(Entity entity, Vec3 relative) {
        Vec3 vec3 = relative.normalize();
        float f = Mth.sin(entity.getYRot() * Mth.DEG_TO_RAD);
        float g = Mth.cos(entity.getYRot() * Mth.DEG_TO_RAD);
        return new Vec3(vec3.x * g - vec3.z * f, vec3.y, vec3.z * g + vec3.x * f);
    }

    public static void sendMotionUpdate(Player player) {
        if (player instanceof ServerPlayer serverPlayer)
            serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(player));

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
