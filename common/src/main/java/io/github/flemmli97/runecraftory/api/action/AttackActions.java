package io.github.flemmli97.runecraftory.api.action;

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
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class AttackActions {

    public static final AttackAction NONE = AttackAction.register("none", new AttackAction.Builder(null));

    //Short sword attack sequence
    public static final AttackAction SHORT_SWORD = AttackAction.register("short_sword", new AttackAction.Builder((entity, data) -> {
        int count = data.getCurrentCount() + 1;
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
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return AnimatedAction.builder(defaultLength + 1, "short_sword_" + count).speed(speed).marker(attack).build();
    }).allowSelfOverride((entity, w) -> switch (w.getCurrentCount()) {
                case 1, 2, 5, 3 -> w.getCurrentAnim().isPastTick(0.32);
                case 4 -> w.getCurrentAnim().isPastTick(0.24) && !w.getCurrentAnim().isPastTick(0.48);
                default -> false;
            }).doWhileAction(((entity, stack, handler, anim) -> {
                if (!entity.level.isClientSide) {
                    if (anim.canAttack()) {
                        AttackAction.attack(entity, stack);
                        entity.swing(InteractionHand.MAIN_HAND, true);
                    }
                    switch (handler.getCurrentCount()) {
                        case 1 -> {
                            if (anim.isAtTick(0.28)) {
                                entity.moveRelative(0.4f, new Vec3(0, 0, 1));
                                AttackAction.sendMotionUpdate(entity);
                            }
                        }
                        case 2 -> {
                            if (anim.isAtTick(0.16)) {
                                entity.moveRelative(0.4f, new Vec3(0, 0, 1));
                                AttackAction.sendMotionUpdate(entity);
                            }
                        }
                        case 3 -> {
                            if (anim.isAtTick(0.16)) {
                                entity.moveRelative(0.2f, new Vec3(0, 0, 1));
                                AttackAction.sendMotionUpdate(entity);
                            }
                        }
                        case 4 -> {
                            if (anim.isAtTick(0.2)) {
                                entity.moveRelative(0.8f, new Vec3(0, 2.2, 1.4));
                                AttackAction.sendMotionUpdate(entity);
                            }
                        }
                        case 5 -> {
                            if (anim.isAtTick(0.04)) {
                                entity.setDeltaMovement(new Vec3(0, -0.2, 0));
                                AttackAction.sendMotionUpdate(entity);
                            }

                        }
                        case 6 -> {
                        }
                    }
                }
            }))
            .setMaxConsecutive(entity -> AttackAction.canPerform(entity, EnumSkills.SHORTSWORD, 20) ? 6 : 5, e -> 0)
            .disableItemSwitch().disableMovement());
    public static final AttackAction SHORT_SWORD_USE = AttackAction.register("short_sword_use", new AttackAction.Builder((entity, data) -> new AnimatedAction(16 + 1, 6, "short_sword_use")).disableMovement());

    public static final AttackAction LONG_SWORD = AttackAction.register("long_sword", new AttackAction.Builder((entity, data) -> {
        int defaultLength = EnumWeaponType.LONGSWORD.defaultWeaponSpeed;
        int count = data.getCurrentCount() + 1;
        int attack = (int) Math.ceil(0.48 * 20);
        if (count == 3)
            attack = (int) Math.ceil(0.56 * 20);
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return AnimatedAction.builder(defaultLength + 1, "long_sword_" + count).speed(speed).marker(attack).build();
    }).allowSelfOverride((entity, w) -> switch (w.getCurrentCount()) {
                case 1, 2, 3 -> w.getCurrentAnim().isPastTick(0.56);
                default -> false;
            }).doWhileAction(((entity, stack, handler, anim) -> {
                if (!entity.level.isClientSide) {
                    if (anim.canAttack()) {
                        AttackAction.attack(entity, stack);
                        entity.swing(InteractionHand.MAIN_HAND, true);
                    }
                    switch (handler.getCurrentCount()) {
                        case 2 -> {
                            if (anim.isAtTick(0.4)) {
                                entity.moveRelative(0.6f, new Vec3(0, 0, 1));
                                AttackAction.sendMotionUpdate(entity);
                            }
                        }
                        case 3 -> {
                            if (anim.isAtTick(0.44)) {
                                entity.moveRelative(0.6f, new Vec3(0, 0, 1));
                                AttackAction.sendMotionUpdate(entity);
                            }
                        }
                        case 4 -> {
                        }
                    }
                }
            }))
            .setMaxConsecutive(entity -> AttackAction.canPerform(entity, EnumSkills.LONGSWORD, 20) ? 4 : 3, entity -> 0)
            .disableItemSwitch().disableMovement());
    public static final AttackAction LONGSWORD_USE = AttackAction.register("long_sword_use", new AttackAction.Builder((entity, data) -> new AnimatedAction(16 + 1, 5, "long_sword_use")).disableMovement());

    public static final AttackAction SPEAR = AttackAction.register("spear", new AttackAction.Builder((entity, data) -> {
        int defaultLength = EnumWeaponType.LONGSWORD.defaultWeaponSpeed;
        int count = data.getCurrentCount() + 1;
        int attack = (int) Math.ceil(0.36 * 20);
        if (count == 2) {
            defaultLength = (int) Math.ceil(0.52 * 20);
            attack = (int) Math.ceil(0.24 * 20);
        }
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return AnimatedAction.builder(defaultLength + 1, "spear_" + count).speed(speed).marker(attack).build();
    }).allowSelfOverride((entity, w) -> switch (w.getCurrentCount()) {
                case 1, 3, 4 -> w.getCurrentAnim().isPastTick(0.36);
                case 2 -> w.getCurrentAnim().isPastTick(0.32);
                default -> false;
            }).doWhileAction(((entity, stack, handler, anim) -> {
                if (!entity.level.isClientSide) {
                    if (anim.canAttack()) {
                        AttackAction.attack(entity, stack);
                        entity.swing(InteractionHand.MAIN_HAND, true);
                    }
                    switch (handler.getCurrentCount()) {
                        case 1, 3, 4 -> {
                            if (anim.isAtTick(0.28)) {
                                entity.moveRelative(0.2f, new Vec3(0, 0, 1));
                                AttackAction.sendMotionUpdate(entity);
                            }
                        }
                        case 2 -> {
                            if (anim.isAtTick(0.2)) {
                                entity.moveRelative(0.4f, new Vec3(0, 0, 1));
                                AttackAction.sendMotionUpdate(entity);
                            }
                        }
                        case 5 -> {
                        }
                    }
                }
            }))
            .setMaxConsecutive(entity -> AttackAction.canPerform(entity, EnumSkills.SPEAR, 20) ? 5 : 4, e -> 0)
            .disableItemSwitch().disableMovement());
    public static final AttackAction SPEAR_USE_FINISHER = AttackAction.register("spear_use_finisher", new AttackAction.Builder((entity, data) -> new AnimatedAction(16 + 1, 10, "spear_use_finisher")).doWhileAction(((entity, stack, handler, anim) -> {
        if (anim.canAttack() && entity instanceof ServerPlayer serverPlayer && stack.getItem() instanceof ItemSpearBase spear) {
            spear.useSpear(serverPlayer, stack);
        }
    })).disableMovement());
    public static final AttackAction SPEAR_USE = AttackAction.register("spear_use", new AttackAction.Builder((entity, data) -> {
        int count = data.getCurrentCount() + 1;
        return count > 1 ? new AnimatedAction((int) Math.ceil(0.4 * 20) + 1, 3, "spear_use_continue") : new AnimatedAction((int) Math.ceil(0.48 * 20) + 1, 3, "spear_use");
    }).allowSelfOverride((entity, w) -> {
        AnimatedAction anim = w.getCurrentAnim();
        return anim == null || (anim.getID().equals("spear_use_continue") ? anim.isPastTick(0.2) : anim.isPastTick(0.28));
    }).setFollowingAnim((e, a) -> SPEAR_USE_FINISHER).setMaxConsecutive(e -> 20, e -> 6).disableMovement());

    public static final AttackAction HAMMER_AXE = AttackAction.register("hammer_axe", new AttackAction.Builder((entity, data) -> {
        int defaultLength = EnumWeaponType.HAXE.defaultWeaponSpeed;
        int count = data.getCurrentCount() + 1;
        int attack = (int) Math.ceil(0.6 * 20);
        if (count == 2)
            attack = (int) Math.ceil(0.48 * 20);
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return AnimatedAction.builder(defaultLength + 1, "hammer_axe_" + count).speed(speed).marker(attack).build();
    }).allowSelfOverride((entity, w) -> switch (w.getCurrentCount()) {
                case 1, 2 -> w.getCurrentAnim().isPastTick(0.64);
                default -> false;
            }).doWhileAction(((entity, stack, handler, anim) -> {
                if (!entity.level.isClientSide) {
                    if (anim.canAttack()) {
                        AttackAction.attack(entity, stack);
                        entity.swing(InteractionHand.MAIN_HAND, true);
                    }
                    if (handler.getCurrentCount() == 3) {

                    }
                }
            }))
            .setMaxConsecutive(entity -> AttackAction.canPerform(entity, EnumSkills.HAMMERAXE, 20) ? 3 : 2, e -> 0)
            .disableItemSwitch().disableMovement());
    public static final AttackAction HAMMER_AXE_USE = AttackAction.register("hammer_axe_use", new AttackAction.Builder((entity, data) -> new AnimatedAction(20 + 1, 12, "hammer_axe_use")).disableMovement().doWhileAction((entity, stack, handler, anim) -> ItemAxeBase.moveEntity(entity).accept(anim)));

    public static final AttackAction DUAL_BLADES = AttackAction.register("dual_blades", new AttackAction.Builder((entity, data) -> {
        int defaultLength = EnumWeaponType.DUAL.defaultWeaponSpeed;
        int count = data.getCurrentCount() + 1;
        int attack = (int) Math.ceil(0.4 * 20);
        switch (count) {
            case 1 -> attack = (int) Math.ceil(0.16 * 20);
            case 2 -> attack = (int) Math.ceil(0.2 * 20);
            case 3, 4, 5, 6 -> attack = (int) Math.ceil(0.24 * 20);
            case 7 -> attack = (int) Math.ceil(0.4 * 20);
        }
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return AnimatedAction.builder(defaultLength + 1, "dual_blades_" + count).speed(speed).marker(attack).build();
    }).allowSelfOverride((entity, w) -> switch (w.getCurrentCount()) {
                case 1, 2, 3, 4, 5, 6 -> w.getCurrentAnim().isPastTick(0.28);
                default -> false;
            }).doWhileAction(((entity, stack, handler, anim) -> {
                if (!entity.level.isClientSide) {
                    if (anim.canAttack() && handler.getCurrentCount() != 5 && handler.getCurrentCount() != 6) {
                        AttackAction.attack(entity, stack);
                        entity.swing(InteractionHand.MAIN_HAND, true);
                    }
                    switch (handler.getCurrentCount()) {
                        case 1 -> {
                            if (anim.isAtTick(0.2)) {
                                entity.moveRelative(0.3f, new Vec3(0, 0, 1));
                                AttackAction.sendMotionUpdate(entity);
                            }
                        }
                        case 3, 4 -> {
                            if (anim.isAtTick(0.16)) {
                                entity.moveRelative(0.3f, new Vec3(0, 0, 1));
                                AttackAction.sendMotionUpdate(entity);
                            }
                        }
                        case 5 -> {
                            if (anim.isAtTick(0.2)) {
                                entity.moveRelative(0.3f, new Vec3(0, 0, 1));
                                handler.setSpinStartRot(entity.getYRot() + 20);
                                handler.resetHitEntityTracker();
                                AttackAction.sendMotionUpdate(entity);
                            }
                            if (anim.isAtTick(0.4)) {
                                handler.resetHitEntityTracker();
                            }
                            if (anim.isPastTick(0.2)) {
                                float len = (anim.getLength() - ((int) Math.ceil(0.2 * 20.0D))) / anim.getSpeed();
                                float f = (anim.getTick() - ((int) Math.ceil(0.2 * 20.0D))) / anim.getSpeed();
                                float angleInc = 360 / len;
                                float rot = handler.getSpinStartRot();
                                handler.addHitEntityTracker(CombatUtils.spinAttackHandler(entity, (rot + f * angleInc), (rot + (f + 1) * angleInc), e -> !handler.getHitEntityTracker().contains(e)));
                            }
                        }
                        case 6 -> {
                            if (anim.isAtTick(0.08)) {
                                handler.setSpinStartRot(entity.getYRot() + 20);
                                handler.resetHitEntityTracker();
                            }
                            if (anim.isAtTick(0.28)) {
                                entity.moveRelative(0.35f, new Vec3(0, 0, 1));
                                handler.resetHitEntityTracker();
                                AttackAction.sendMotionUpdate(entity);
                            }
                            if (anim.isPastTick(0.12)) {
                                float len = (anim.getLength() - ((int) Math.ceil(0.12 * 20.0D))) / anim.getSpeed();
                                float f = (anim.getTick() - ((int) Math.ceil(0.12 * 20.0D))) / anim.getSpeed();
                                float angleInc = 360 / len;
                                float rot = handler.getSpinStartRot();
                                handler.addHitEntityTracker(CombatUtils.spinAttackHandler(entity, (rot + f * angleInc), (rot + (f + 1) * angleInc), e -> !handler.getHitEntityTracker().contains(e)));
                                handler.addHitEntityTracker(CombatUtils.spinAttackHandler(entity, (rot + 180 + f * angleInc), (rot + 180 + (f + 1) * angleInc), e -> !handler.getHitEntityTracker().contains(e)));
                            }
                        }
                        case 7 -> {
                            if (!anim.isPastTick(0.4)) {
                                if (anim.isPastTick(0.24)) {
                                    entity.setDeltaMovement(AttackAction.fromRelativeVector(entity, new Vec3(0, -1, 0.6))
                                            .scale(3 / ((0.4 - 0.24) * 20 / anim.getSpeed())));
                                    AttackAction.sendMotionUpdate(entity);
                                    entity.resetFallDistance();
                                } else if (anim.isPastTick(0.08)) {
                                    entity.setDeltaMovement(AttackAction.fromRelativeVector(entity, new Vec3(0, 1, 0.5))
                                            .scale(2.6 / ((0.24 - 0.08) * 20 / anim.getSpeed())));
                                    AttackAction.sendMotionUpdate(entity);
                                }
                            } else {
                                entity.resetFallDistance();
                            }
                        }
                    }
                }
            }))
            .setMaxConsecutive(entity -> AttackAction.canPerform(entity, EnumSkills.DUAL, 20) ? 8 : 7, e -> 0)
            .disableItemSwitch().disableMovement());
    public static final AttackAction DUAL_USE = AttackAction.register("dual_blade_use", new AttackAction.Builder((entity, data) -> new AnimatedAction(15 + 1, 7, "dual_blades_use")).disableMovement());

    public static final AttackAction GLOVES = AttackAction.register("gloves", new AttackAction.Builder((entity, data) -> {
        int defaultLength = EnumWeaponType.GLOVE.defaultWeaponSpeed;
        int count = data.getCurrentCount() + 1;
        int attack = (int) Math.ceil(0.4 * 20);
        switch (count) {
            case 1 -> attack = (int) Math.ceil(0.28 * 20);
            case 2, 3 -> attack = (int) Math.ceil(0.24 * 20);
            case 4 -> {
                attack = (int) Math.ceil(0.4 * 20);
                defaultLength = (int) Math.ceil(0.64 * 20);
            }
        }
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return AnimatedAction.builder(defaultLength + 1, "glove_" + count).speed(speed).marker(attack).build();
    }).allowSelfOverride((entity, w) -> switch (w.getCurrentCount()) {
                case 1, 2, 3 -> w.getCurrentAnim().isPastTick(0.28);
                case 4 -> w.getCurrentAnim().isPastTick(0.48);
                default -> false;
            }).doWhileAction(((entity, stack, handler, anim) -> {
                if (!entity.level.isClientSide) {
                    if (anim.canAttack()) {
                        if (handler.getCurrentCount() != 4)
                            AttackAction.attack(entity, stack);
                        else
                            CombatUtils.attackInAABB(entity, new AABB(-1, -1, -1, 1, 1, 1).move(entity.position().add(0, 0.2, 0).add(entity.getDeltaMovement().normalize().scale(0.4))), null);
                        entity.swing(InteractionHand.MAIN_HAND, true);
                    }
                    switch (handler.getCurrentCount()) {
                        case 1 -> {
                            if (anim.isAtTick(0.24)) {
                                entity.moveRelative(0.3f, new Vec3(0, 0, 1));
                                AttackAction.sendMotionUpdate(entity);
                            }
                        }
                        case 2, 3 -> {
                            if (anim.isAtTick(0.24)) {
                                entity.moveRelative(0.1f, new Vec3(0, 0, 1));
                                AttackAction.sendMotionUpdate(entity);
                            }
                        }
                        case 4 -> {
                            if (!anim.isPastTick(0.44)) {
                                if (anim.isPastTick(0.14)) {
                                    entity.setDeltaMovement(AttackAction.fromRelativeVector(entity, new Vec3(0, -1, 0.6))
                                            .scale(4 / ((0.44 - 0.14) * 20 / anim.getSpeed())));
                                    AttackAction.sendMotionUpdate(entity);
                                    entity.resetFallDistance();
                                } else if (anim.isPastTick(0.04)) {
                                    entity.setDeltaMovement(AttackAction.fromRelativeVector(entity, new Vec3(0, 1, 0.3))
                                            .scale(3 / ((0.14 - 0.04) * 20 / anim.getSpeed())));
                                    AttackAction.sendMotionUpdate(entity);
                                }
                            } else if (entity.isOnGround()) {
                                entity.setDeltaMovement(entity.getDeltaMovement().scale(0.01));
                                AttackAction.sendMotionUpdate(entity);
                            }
                        }
                    }
                }
            }))
            .setMaxConsecutive(entity -> AttackAction.canPerform(entity, EnumSkills.FIST, 20) ? 5 : 4, e -> 0)
            .disableItemSwitch().disableMovement());
    public static final AttackAction GLOVE_USE = AttackAction.register("glove_use", new AttackAction.Builder((entity, data) -> new AnimatedAction(27 + 1, 4, "glove_use")).disableMovement().doAtStart((e, w) -> e.maxUpStep += 0.5).doAtEnd((e, w) -> e.maxUpStep -= 0.5).doWhileAction(((entity, stack, handler, anim) -> {
        if (entity instanceof ServerPlayer serverPlayer) {
            if (!handler.getCurrentAnim().isPastTick(0.16))
                return;
            Vec3 look = entity.getLookAngle();
            Vec3 move = new Vec3(look.x, 0.0, look.z).normalize().scale(entity.isOnGround() ? 0.5 : 0.3).add(0, entity.getDeltaMovement().y, 0);
            entity.setDeltaMovement(move);
            AttackAction.sendMotionUpdate(entity);
            if (anim.speedAdjustedTick() % 4 == 0) {
                List<LivingEntity> list = entity.level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(1.0));
                boolean flag = false;
                for (LivingEntity e : list) {
                    if (e != entity) {
                        CombatUtils.playerAttackWithItem(serverPlayer, e, stack, 0.5f, false, false, false);
                        flag = true;
                    }
                }
                if (flag) {
                    Platform.INSTANCE.getPlayerData(serverPlayer).ifPresent(data -> LevelCalc.levelSkill(serverPlayer, data, EnumSkills.DUAL, 2));
                }
            }
        }
    })));

    public static final AttackAction STAFF = AttackAction.register("staff", new AttackAction.Builder((entity, data) -> {
        int defaultLength = EnumWeaponType.STAFF.defaultWeaponSpeed;
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return AnimatedAction.builder(defaultLength + 1, "staff").speed(speed).marker(9).build();
    }).allowSelfOverride((entity, w) -> switch (w.getCurrentCount()) {
                case 1 -> w.getCurrentAnim().isPastTick(0.54);
                case 2 -> w.getCurrentAnim().isPastTick(0.60);
                case 3 -> w.getCurrentAnim().isPastTick(0.64);
                default -> false;
            }).doWhileAction(((entity, stack, handler, anim) -> {
                if (entity instanceof ServerPlayer serverPlayer) {
                    if (anim.canAttack() && stack.getItem() instanceof ItemStaffBase staff) {
                        EntityHitResult res = RayTraceUtils.calculateEntityFromLook(entity, staff.getRange(entity, stack));
                        if (res != null && res.getEntity() != null)
                            serverPlayer.attack(res.getEntity());
                        staff.castBaseSpell(stack, entity);
                        entity.swing(InteractionHand.MAIN_HAND, true);
                    }
                }
            }))
            .disableItemSwitch().disableMovement());

    public static final AttackAction STAFF_USE = AttackAction.register("staff_use", new AttackAction.Builder((entity, data) -> new AnimatedAction(16 + 1, 4, "staff_use")).disableMovement());

    public static final AttackAction TOOL_AXE_USE = AttackAction.register("tool_axe", new AttackAction.Builder((entity, data) -> new AnimatedAction(10 + 1, 2, "hammer_axe_use")).noAnimation().disableMovement().setMaxConsecutive(p -> 3, p -> 15));
    public static final AttackAction TOOL_HAMMER_USE = AttackAction.register("tool_hammer", new AttackAction.Builder((entity, data) -> new AnimatedAction(10 + 1, 2, "hammer_axe_use")).noAnimation().disableMovement().setMaxConsecutive(p -> 3, p -> 15));
    public static final AttackAction FIREBALL_USE = AttackAction.register("fireball_use", new AttackAction.Builder((entity, data) -> new AnimatedAction(16 + 1, 4, "staff_use")).allowSelfOverride((e, w) -> {
        AnimatedAction anim = w.getCurrentAnim();
        return anim == null || anim.isPastTick(anim.getAttackTime());
    }).disableMovement().setMaxConsecutive(p -> 3, p -> 8));
    public static final AttackAction FIREBALL_BIG_USE = AttackAction.register("fireball_big_use", new AttackAction.Builder((entity, data) -> new AnimatedAction(16 + 1, 4, "staff_use")).allowSelfOverride((e, w) -> {
        AnimatedAction anim = w.getCurrentAnim();
        return anim == null || anim.isPastTick(anim.getAttackTime());
    }).disableMovement().setMaxConsecutive(p -> 2, p -> 8));

    public static final AttackAction TOOL_ATTACK = AttackAction.register("tool_attack", new AttackAction.Builder((entity, data) -> new AnimatedAction(20, 1, "tool_attack")));
}
