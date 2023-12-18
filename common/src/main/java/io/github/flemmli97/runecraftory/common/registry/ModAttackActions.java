package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.action.AttackAction;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemAxeBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemSpearBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.LazyGetter;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import io.github.flemmli97.tenshilib.platform.registry.SimpleRegistryWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Supplier;

public class ModAttackActions {

    public static final ResourceKey<? extends Registry<AttackAction>> ATTACK_ACTION_KEY = ResourceKey.createRegistryKey(new ResourceLocation(RuneCraftory.MODID, "attack_action"));
    public static final Supplier<SimpleRegistryWrapper<AttackAction>> ATTACK_ACTION_REGISTRY = new LazyGetter<>(() -> PlatformUtils.INSTANCE.registry(ATTACK_ACTION_KEY));
    public static final PlatformRegistry<AttackAction> ATTACK_ACTIONS = PlatformUtils.INSTANCE.customRegistry(AttackAction.class, ATTACK_ACTION_KEY, new ResourceLocation(RuneCraftory.MODID, "none"), true, true);

    public static final RegistryEntrySupplier<AttackAction> NONE = register("none", () -> new AttackAction.Builder(null));

    //Short sword attack sequence
    public static final RegistryEntrySupplier<AttackAction> SHORT_SWORD = register("short_sword", () -> new AttackAction.Builder((entity, count) -> {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.SHORT_SWORD.get(count).create(speed);
    }).allowSelfOverride((entity, w) -> switch (w.getCurrentCount()) {
                case 1, 2, 5, 3 -> w.getCurrentAnim().isPastTick(0.32);
                case 4 -> w.getCurrentAnim().isPastTick(0.24) && !w.getCurrentAnim().isPastTick(0.48);
                default -> false;
            }).doWhileAction(((entity, stack, handler, anim) -> {
                if (!entity.level.isClientSide) {
                    if (anim.canAttack() && handler.getCurrentCount() != 6) {
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
                            if (anim.isAtTick(0.24)) {
                                handler.setSpinStartRot(entity.getYRot() + 30);
                                handler.resetHitEntityTracker();
                            }
                            if (anim.isAtTick(0.48) || anim.isAtTick(0.72)) {
                                handler.resetHitEntityTracker();
                            }
                            if (anim.isPastTick(0.24) && !anim.isPastTick(0.96)) {
                                entity.setDeltaMovement(new Vec3(0, 0.09, 0));
                                AttackAction.sendMotionUpdate(entity);
                                int start = Mth.ceil(0.24 * 20.0D);
                                int end = Mth.ceil(1 * 20.0D);
                                float len = (end - start) / anim.getSpeed();
                                float f = (anim.getTick() - start) / anim.getSpeed();
                                float angleInc = -1080 / len;
                                float rot = handler.getSpinStartRot();
                                handler.addHitEntityTracker(CombatUtils.spinAttackHandler(entity, (rot + f * angleInc), (rot + (f + 1) * angleInc), 0.5f, e -> !handler.getHitEntityTracker().contains(e)));
                            }
                        }
                    }
                }
            }))
            .doAtStart((e, w) -> {
                if (w.getCurrentCount() == 6 && e instanceof ServerPlayer player)
                    Platform.INSTANCE.getPlayerData(player).ifPresent(d -> LevelCalc.useRP(player, d, GeneralConfig.shortSwordUltimate, true, false, false));
            })
            .setMaxConsecutive(entity -> AttackAction.canPerform(entity, EnumSkills.SHORTSWORD, 20) ? 6 : 5, e -> 0)
            .setInvulnerability((e, w) -> w.getCurrentCount() == 6)
            .disableItemSwitch().disableMovement());
    public static final RegistryEntrySupplier<AttackAction> SHORT_SWORD_USE = register("short_sword_use", () -> new AttackAction.Builder((entity, count) -> new AnimatedAction(16 + 1, 6, "short_sword_use")).disableMovement());

    public static final RegistryEntrySupplier<AttackAction> LONG_SWORD = register("long_sword", () -> new AttackAction.Builder((entity, count) -> {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.LONG_SWORD.get(count).create(speed);
    }).allowSelfOverride((entity, w) -> switch (w.getCurrentCount()) {
                case 1, 2, 3 -> w.getCurrentAnim().isPastTick(0.56);
                default -> false;
            }).doWhileAction(((entity, stack, handler, anim) -> {
                if (!entity.level.isClientSide) {
                    if (anim.canAttack() && handler.getCurrentCount() != 4) {
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
                            if (anim.isAtTick(0.2)) {
                                handler.setSpinStartRot(entity.getYRot() + 150);
                                handler.resetHitEntityTracker();
                            }
                            if (anim.isAtTick(0.68)) {
                                handler.resetHitEntityTracker();
                            }
                            if (anim.isPastTick(0.2)) {
                                int start = Mth.ceil(0.2 * 20.0D);
                                int end = Mth.ceil(1.15 * 20.0D);
                                float len = (end - start) / anim.getSpeed();
                                float f = (anim.getTick() - start) / anim.getSpeed();
                                float angleInc = -690 / len;
                                float rot = handler.getSpinStartRot();
                                handler.addHitEntityTracker(CombatUtils.spinAttackHandler(entity, (rot + f * angleInc), (rot + (f + 1) * angleInc), 0.25f, e -> !handler.getHitEntityTracker().contains(e)));
                            }
                        }
                    }
                }
            }))
            .doAtStart((e, w) -> {
                if (w.getCurrentCount() == 4 && e instanceof ServerPlayer player)
                    Platform.INSTANCE.getPlayerData(player).ifPresent(d -> LevelCalc.useRP(player, d, GeneralConfig.longSwordUltimate, true, false, false));
            })
            .setMaxConsecutive(entity -> AttackAction.canPerform(entity, EnumSkills.LONGSWORD, 20) ? 4 : 3, entity -> 0)
            .setInvulnerability((e, w) -> w.getCurrentCount() == 4)
            .disableItemSwitch().disableMovement());
    public static final RegistryEntrySupplier<AttackAction> LONGSWORD_USE = register("long_sword_use", () -> new AttackAction.Builder((entity, count) -> new AnimatedAction(16 + 1, 5, "long_sword_use")).disableMovement());

    public static final RegistryEntrySupplier<AttackAction> SPEAR = register("spear", () -> new AttackAction.Builder((entity, count) -> {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.SPEAR.get(count).create(speed);
    }).allowSelfOverride((entity, w) -> switch (w.getCurrentCount()) {
                case 1, 3, 4 -> w.getCurrentAnim().isPastTick(0.36);
                case 2 -> w.getCurrentAnim().isPastTick(0.32);
                default -> false;
            }).doWhileAction(((entity, stack, handler, anim) -> {
                if (!entity.level.isClientSide) {
                    if (anim.canAttack() && handler.getCurrentCount() != 5) {
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
                            if (anim.isAtTick(0.12)) {
                                handler.setSpinStartRot(entity.getYRot() + 180);
                                handler.resetHitEntityTracker();
                            }
                            if (anim.isAtTick(0.6)) {
                                handler.resetHitEntityTracker();
                            }
                            if (anim.isPastTick(0.12)) {
                                int start = Mth.ceil(0.12 * 20.0D);
                                int end = Mth.ceil(1.08 * 20.0D);
                                float len = (end - start) / anim.getSpeed();
                                float f = (anim.getTick() - start) / anim.getSpeed();
                                float angleInc = 720 / len;
                                float rot = handler.getSpinStartRot();
                                handler.addHitEntityTracker(CombatUtils.spinAttackHandler(entity, (rot + f * angleInc), (rot + (f + 1) * angleInc), 0, e -> !handler.getHitEntityTracker().contains(e)));
                            }
                            if (!anim.isPastTick(1.52)) {
                                if (anim.isPastTick(1.4)) {
                                    entity.setDeltaMovement(AttackAction.fromRelativeVector(entity, new Vec3(0, -0.8, 1.1))
                                            .scale(1.9 / ((1.52 - 1.4) * 20 / anim.getSpeed())));
                                    AttackAction.sendMotionUpdate(entity);
                                    entity.resetFallDistance();
                                } else if (anim.isPastTick(1.2)) {
                                    entity.setDeltaMovement(AttackAction.fromRelativeVector(entity, new Vec3(0, 0.8, 1.15))
                                            .scale(1.9 / ((1.4 - 1.2) * 20 / anim.getSpeed())));
                                    AttackAction.sendMotionUpdate(entity);
                                }
                            }
                            if (anim.isAtTick(1.63)) {
                                Vec3 look = entity.getLookAngle();
                                look = new Vec3(look.x(), 0, look.z()).scale(1.2);
                                Vec3 attackPos = entity.position().add(0, 0.2, 0).add(look);
                                CombatUtils.attackInAABB(entity, new AABB(-0.8, -1.2, -0.8, 0.8, 1.2, 0.8).move(attackPos), null);
                                Vec3 pos = entity.position().add(0, -1, 0);
                                BlockPos.MutableBlockPos mut = new BlockPos.MutableBlockPos();
                                Vec3 axis = new Vec3(0, 1, 0);
                                Vec3 dir = new Vec3(0, 0, 1).scale(1);
                                for (int i = -180; i < 180; i += 15) {
                                    Vec3 scaled = MathUtils.rotate(axis, dir, i);
                                    mut.set(Mth.floor(pos.x() + dir.x()), Mth.floor(pos.y()), Mth.floor(pos.z() + dir.z()));
                                    BlockState state = entity.level.getBlockState(mut);
                                    if (state.getRenderShape() != RenderShape.INVISIBLE)
                                        ((ServerLevel) entity.getLevel()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state), attackPos.x() + scaled.x() + entity.getDeltaMovement().x(), entity.getY() + 0.1, attackPos.z() + scaled.z() + entity.getDeltaMovement().z(), 0, (float) scaled.x(), 1.5f, (float) scaled.z(), 1);
                                }
                            }
                        }
                    }
                }
                if (handler.getCurrentCount() == 5) {
                    handler.lockLook(anim.isPastTick(01.16) && !anim.isPastTick(1.6));
                }
            }))
            .doAtStart((e, w) -> {
                if (w.getCurrentCount() == 5 && e instanceof ServerPlayer player)
                    Platform.INSTANCE.getPlayerData(player).ifPresent(d -> LevelCalc.useRP(player, d, GeneralConfig.spearUltimate, true, false, false));
            })
            .setMaxConsecutive(entity -> AttackAction.canPerform(entity, EnumSkills.SPEAR, 20) ? 5 : 4, e -> 0)
            .setInvulnerability((e, w) -> w.getCurrentCount() == 5)
            .disableItemSwitch().disableMovement());
    public static final RegistryEntrySupplier<AttackAction> SPEAR_USE = register("spear_use", () -> new AttackAction.Builder((entity, count) -> count > 1 ? new AnimatedAction((int) Math.ceil(1.12 * 20) + 1, 3, "spear_use_continue") : new AnimatedAction((int) Math.ceil(1.2 * 20) + 1, 5, "spear_use")).allowSelfOverride((entity, w) -> {
        AnimatedAction anim = w.getCurrentAnim();
        return anim == null || (anim.getID().equals("spear_use_continue") ? (anim.isPastTick(0.16) && !anim.isPastTick(0.40)) : (anim.isPastTick(0.28) && !anim.isPastTick(0.44)));
    }).doWhileAction(((entity, stack, handler, anim) -> {
        boolean canAttack = anim.canAttack() || (anim.getID().equals("spear_use_continue") ? anim.isAtTick(0.88) : anim.isAtTick(0.96));
        if (canAttack && entity instanceof ServerPlayer serverPlayer && stack.getItem() instanceof ItemSpearBase spear) {
            spear.useSpear(serverPlayer, stack);
        }
    })).setMaxConsecutive(e -> 20, e -> 6).disableMovement());

    public static final RegistryEntrySupplier<AttackAction> HAMMER_AXE = register("hammer_axe", () -> new AttackAction.Builder((entity, count) -> {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.HAMMER_AXE.get(count).create(speed);
    }).allowSelfOverride((entity, w) -> switch (w.getCurrentCount()) {
                case 1, 2 -> w.getCurrentAnim().isPastTick(0.64);
                default -> false;
            }).doWhileAction(((entity, stack, handler, anim) -> {
                if (!entity.level.isClientSide) {
                    if (anim.canAttack() && handler.getCurrentCount() != 3) {
                        AttackAction.attack(entity, stack);
                        entity.swing(InteractionHand.MAIN_HAND, true);
                    }
                    if (handler.getCurrentCount() == 3) {
                        if (anim.isAtTick(0.12)) {
                            handler.setSpinStartRot(entity.getYRot());
                            handler.resetHitEntityTracker();
                        }
                        if (anim.isAtTick(0.64)) {
                            handler.resetHitEntityTracker();
                        }
                        if (anim.isPastTick(0.12) && !anim.isPastTick(1.28)) {
                            Vec3 dir = AttackAction.fromRelativeVector(handler.getSpinStartRot(), new Vec3(0, 0, 1)).scale(6 / ((1.28 - 0.12) * 20 / anim.getSpeed()));
                            if (!anim.isPastTick(0.56))
                                dir = dir.add(0, entity.getDeltaMovement().y + 0.14, 0);
                            else if (anim.isPastTick(0.76))
                                dir = dir.add(0, entity.getDeltaMovement().y - 0.1, 0);
                            entity.setDeltaMovement(dir);
                            AttackAction.sendMotionUpdate(entity);
                            entity.resetFallDistance();
                            handler.addHitEntityTracker(CombatUtils.attackInAABB(entity, entity.getBoundingBox().inflate(0.5), e -> !handler.getHitEntityTracker().contains(e)));
                        }
                    }
                }
                if (handler.getCurrentCount() == 3) {
                    handler.lockLook(anim.isPastTick(0.12) && !anim.isPastTick(1.28));
                }
            }))
            .doAtStart((e, w) -> {
                if (w.getCurrentCount() == 3 && e instanceof ServerPlayer player)
                    Platform.INSTANCE.getPlayerData(player).ifPresent(d -> LevelCalc.useRP(player, d, GeneralConfig.hammerAxeUltimate, true, false, false));
            })
            .setMaxConsecutive(entity -> AttackAction.canPerform(entity, EnumSkills.HAMMERAXE, 20) ? 3 : 2, e -> 0)
            .setInvulnerability((e, w) -> w.getCurrentCount() == 3)
            .disableItemSwitch().disableMovement());
    public static final RegistryEntrySupplier<AttackAction> HAMMER_AXE_USE = register("hammer_axe_use", () -> new AttackAction.Builder((entity, count) -> new AnimatedAction(20 + 1, 12, "hammer_axe_use")).disableMovement().doWhileAction((entity, stack, handler, anim) -> ItemAxeBase.moveEntity(entity).accept(anim)));

    public static final RegistryEntrySupplier<AttackAction> DUAL_BLADES = register("dual_blades", () -> new AttackAction.Builder((entity, count) -> {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.DUAL_BLADES.get(count).create(speed);
    }).allowSelfOverride((entity, w) -> switch (w.getCurrentCount()) {
                case 1, 2, 3, 4, 5, 6 -> w.getCurrentAnim().isPastTick(0.28);
                case 7 -> w.getCurrentAnim().isPastTick(0.40);
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
                                int start = Mth.ceil(0.2 * 20.0D);
                                int end = anim.getLength();
                                float len = (end - start) / anim.getSpeed();
                                float f = (anim.getTick() - start) / anim.getSpeed();
                                float angleInc = 360 / len;
                                float rot = handler.getSpinStartRot();
                                handler.addHitEntityTracker(CombatUtils.spinAttackHandler(entity, (rot + f * angleInc), (rot + (f + 1) * angleInc), 0, e -> !handler.getHitEntityTracker().contains(e)));
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
                                int start = Mth.ceil(0.12 * 20.0D);
                                int end = anim.getLength();
                                float len = (end - start) / anim.getSpeed();
                                float f = (anim.getTick() - start) / anim.getSpeed();
                                float angleInc = 360 / len;
                                float rot = handler.getSpinStartRot();
                                handler.addHitEntityTracker(CombatUtils.spinAttackHandler(entity, (rot + f * angleInc), (rot + (f + 1) * angleInc), 0, e -> !handler.getHitEntityTracker().contains(e)));
                                handler.addHitEntityTracker(CombatUtils.spinAttackHandler(entity, (rot + 180 + f * angleInc), (rot + 180 + (f + 1) * angleInc), 0, e -> !handler.getHitEntityTracker().contains(e)));
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
                        case 8 -> {
                            if (anim.isAtTick(0.24)) {
                                handler.setSpinStartRot(entity.getYRot() + 20);
                                handler.resetHitEntityTracker();
                            }
                            if (anim.isAtTick(0.48) || anim.isAtTick(0.68) || anim.isAtTick(0.92)) {
                                handler.resetHitEntityTracker();
                            }
                            if (anim.isPastTick(0.24) && !anim.isPastTick(1.12)) {
                                int start = Mth.ceil(0.28 * 20.0D);
                                int end = Mth.ceil(1.12 * 20.0D);
                                float len = (end - start) / anim.getSpeed();
                                float f = (anim.getTick() - start) / anim.getSpeed();
                                float angleInc = -1440 / len;
                                float rot = handler.getSpinStartRot();
                                handler.addHitEntityTracker(CombatUtils.spinAttackHandler(entity, (rot + f * angleInc), (rot + (f + 1) * angleInc), 0, e -> !handler.getHitEntityTracker().contains(e)));
                            }
                        }
                    }
                }
            }))
            .doAtStart((e, w) -> {
                if (w.getCurrentCount() == 8 && e instanceof ServerPlayer player)
                    Platform.INSTANCE.getPlayerData(player).ifPresent(d -> LevelCalc.useRP(player, d, GeneralConfig.dualBladeUltimate, true, false, false));
            })
            .setMaxConsecutive(entity -> AttackAction.canPerform(entity, EnumSkills.DUAL, 20) ? 8 : 7, e -> 0)
            .setInvulnerability((e, w) -> w.getCurrentCount() == 8)
            .disableItemSwitch().disableMovement());
    public static final RegistryEntrySupplier<AttackAction> DUAL_USE = register("dual_blade_use", () -> new AttackAction.Builder((entity, count) -> new AnimatedAction(15 + 1, 7, "dual_blades_use")).disableMovement());

    public static final RegistryEntrySupplier<AttackAction> GLOVES = register("gloves", () -> new AttackAction.Builder((entity, count) -> {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.GLOVES.get(count).create(speed);
    }).allowSelfOverride((entity, w) -> switch (w.getCurrentCount()) {
                case 1, 2, 3 -> w.getCurrentAnim().isPastTick(0.28);
                case 4 -> w.getCurrentAnim().isPastTick(0.48);
                default -> false;
            }).doWhileAction(((entity, stack, handler, anim) -> {
                if (!entity.level.isClientSide) {
                    if (anim.canAttack() && handler.getCurrentCount() != 5) {
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
                        case 5 -> {
                            if (anim.isAtTick(0.16)) {
                                handler.setSpinStartRot(entity.getYRot());
                                handler.resetHitEntityTracker();
                            }
                            if (anim.isPastTick(0.16) && !anim.isPastTick(1.12)) {
                                Vec3 dir = AttackAction.fromRelativeVector(handler.getSpinStartRot(), new Vec3(0, 0, 1)).scale(6.5 / ((1.12 - 0.16) * 20 / anim.getSpeed()));
                                if (!anim.isPastTick(0.4))
                                    dir = dir.add(0, entity.getDeltaMovement().y + 0.25, 0);
                                else if (anim.isPastTick(1.0))
                                    dir = dir.add(0, entity.getDeltaMovement().y - 0.22, 0);
                                entity.setDeltaMovement(dir);
                                AttackAction.sendMotionUpdate(entity);
                                entity.resetFallDistance();
                            }
                            if (anim.isPastTick(0.2) && !anim.isPastTick(1.08)) {
                                handler.addHitEntityTracker(CombatUtils.attackInAABB(entity, entity.getBoundingBox().inflate(0.5), e -> !handler.getHitEntityTracker().contains(e)));
                            }
                        }
                    }
                }
                if (handler.getCurrentCount() == 5) {
                    handler.lockLook(anim.isPastTick(0.08) && !anim.isPastTick(1.2));
                }
            }))
            .doAtStart((e, w) -> {
                if (w.getCurrentCount() == 5 && e instanceof ServerPlayer player)
                    Platform.INSTANCE.getPlayerData(player).ifPresent(d -> LevelCalc.useRP(player, d, GeneralConfig.gloveUltimate, true, false, false));
            })
            .setMaxConsecutive(entity -> AttackAction.canPerform(entity, EnumSkills.FIST, 20) ? 5 : 4, e -> 0)
            .setInvulnerability((e, w) -> w.getCurrentCount() == 5)
            .disableItemSwitch().disableMovement()
            .withPose((e, w) -> {
                if (w.getCurrentCount() == 5 && w.getCurrentAnim().isPastTick(0.24) && !w.getCurrentAnim().isPastTick(1.04))
                    return Pose.SPIN_ATTACK;
                return null;
            }));
    public static final RegistryEntrySupplier<AttackAction> GLOVE_USE = register("glove_use", () -> new AttackAction.Builder((entity, count) -> new AnimatedAction(27 + 1, 4, "glove_use")).disableMovement().doAtStart((e, w) -> e.maxUpStep += 0.5).doAtEnd((e, w) -> e.maxUpStep -= 0.5).doWhileAction(((entity, stack, handler, anim) -> {
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
    })).withPose((e, w) -> {
        if (w.getCurrentAnim().isPastTick(0.2) && !w.getCurrentAnim().isPastTick(1.16))
            return Pose.SPIN_ATTACK;
        return null;
    }));

    public static final RegistryEntrySupplier<AttackAction> STAFF = register("staff", () -> new AttackAction.Builder((entity, count) -> {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.STAFF.create(speed);
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

    public static final RegistryEntrySupplier<AttackAction> STAFF_USE = register("staff_use", () -> new AttackAction.Builder((entity, count) -> PlayerModelAnimations.STAFF_USE.create()).disableMovement());

    public static final RegistryEntrySupplier<AttackAction> TOOL_AXE_USE = register("tool_axe", () -> new AttackAction.Builder((entity, count) -> AnimatedAction.builder(20 + 1, "hammer_axe_use").marker(12).speed(1.3f).build()).allowSelfOverride((entity, w) -> w.getCurrentAnim().isPastTick(w.getCurrentAnim().getAttackTime())).disableMovement().setMaxConsecutive(p -> 3, p -> 15));
    public static final RegistryEntrySupplier<AttackAction> TOOL_HAMMER_USE = register("tool_hammer", () -> new AttackAction.Builder((entity, count) -> AnimatedAction.builder(20 + 1, "hammer_axe_use").marker(12).speed(1.3f).build()).allowSelfOverride((entity, w) -> w.getCurrentAnim().isPastTick(w.getCurrentAnim().getAttackTime())).disableMovement().setMaxConsecutive(p -> 3, p -> 15));
    public static final RegistryEntrySupplier<AttackAction> FIREBALL_USE = register("fireball_use", () -> new AttackAction.Builder((entity, count) -> PlayerModelAnimations.STAFF_USE.create()).allowSelfOverride((e, w) -> {
        AnimatedAction anim = w.getCurrentAnim();
        return anim == null || anim.isPastTick(anim.getAttackTime());
    }).disableMovement().setMaxConsecutive(p -> 3, p -> 8));
    public static final RegistryEntrySupplier<AttackAction> FIREBALL_BIG_USE = register("fireball_big_use", () -> new AttackAction.Builder((entity, count) -> PlayerModelAnimations.STAFF_USE.create()).allowSelfOverride((e, w) -> {
        AnimatedAction anim = w.getCurrentAnim();
        return anim == null || anim.isPastTick(anim.getAttackTime());
    }).disableMovement().setMaxConsecutive(p -> 2, p -> 8));

    public static final RegistryEntrySupplier<AttackAction> TOOL_ATTACK = register("tool_attack", () -> new AttackAction.Builder((entity, count) -> new AnimatedAction(20, 1, "tool_attack")));

    public static final RegistryEntrySupplier<AttackAction> POWER_WAVE = register("power_wave", () -> new AttackAction.Builder((entity, count) -> {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.POWER_WAVE.create(speed);
    }).doWhileAction(((entity, stack, handler, anim) -> {
                if (anim.isAtTick(0.4)) {
                    entity.setDeltaMovement(AttackAction.fromRelativeVector(entity, new Vec3(0, 0, 1)).scale(0.3));
                    AttackAction.sendMotionUpdate(entity);
                }
            }))
            .disableItemSwitch().disableMovement());
    public static final RegistryEntrySupplier<AttackAction> DASH_SLASH = register("dash_slash", () -> new AttackAction.Builder((entity, count) -> {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.DASH_SLASH.get(count).create(speed);
    }).allowSelfOverride((entity, w) -> w.getCurrentCount() == 1 && w.getCurrentAnim().isPastTick(0.36)).doWhileAction(((entity, stack, handler, anim) -> {
                if (handler.getCurrentCount() == 2) {
                    entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.8, 1, 0.8));
                    AttackAction.sendMotionUpdate(entity);
                    if (handler.getCurrentAnim().canAttack()) {
                        Vec3 attackPos = entity.position().add(0, 0.2, 0).add(entity.getLookAngle().scale(0.5));
                        CombatUtils.attackInAABB(entity, new AABB(-0.5, -1, -0.8, 0.8, 1, 0.5).move(attackPos), null);
                    }
                } else {
                    if (anim.isPastTick(0.2)) {
                        Vec3 dir = AttackAction.fromRelativeVector(entity, new Vec3(0, 0, 1)).scale(0.4);
                        if (anim.isAtTick(0.2) || entity.isOnGround()) {
                            entity.setDeltaMovement(dir.add(0, 0.3, 0));
                        } else if (anim.isPastTick(0.28)) {
                            entity.setDeltaMovement(dir);
                        } else {
                            entity.setDeltaMovement(dir.add(0, entity.getDeltaMovement().y(), 0));
                        }
                        AttackAction.sendMotionUpdate(entity);
                        if (!anim.isPastTick(0.72)) {
                            double range = entity.getAttributeValue(ModAttributes.ATTACK_RANGE.get());
                            dir = dir.normalize().scale(range);
                            List<LivingEntity> entites = entity.level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(1).expandTowards(dir),
                                    target -> target != entity && !handler.getHitEntityTracker().contains(target) && !target.isAlliedTo(entity) && target.isPickable());
                            handler.addHitEntityTracker(entites);
                            for (LivingEntity entite : entites) {
                                if (entity instanceof Player player)
                                    CombatUtils.playerAttackWithItem(player, entite, false, true, false);
                                else if (entity instanceof Mob mob)
                                    mob.doHurtTarget(entite);
                            }
                        }
                    }
                }
            }))
            .setMaxConsecutive(entity -> 2, e -> 0)
            .disableItemSwitch().disableMovement());
    public static final RegistryEntrySupplier<AttackAction> RUSH_ATTACK = register("rush_attack", () -> new AttackAction.Builder((entity, count) -> {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        if (count < 6)
            count = 0;
        else
            count = 1;
        return PlayerModelAnimations.RUSH_ATTACK.get(count).create(speed);
    }).allowSelfOverride((entity, w) -> switch (w.getCurrentCount()) {
                case 1, 2, 3, 4, 5 -> (!w.getCurrentAnim().isPastTick(0.88) && w.getCurrentAnim().isPastTick(0.6)) || w.getCurrentAnim().isPastTick(1.28);
                case 6 -> w.getCurrentAnim().isPastTick(1.42);
                default -> false;
            }).doWhileAction(((entity, stack, handler, anim) -> {
                if (handler.getCurrentCount() == 7) {
                    if (anim.isAtTick(0.28)) {
                        entity.moveRelative(1.1f, new Vec3(0, 0.3, 1.7));
                        AttackAction.sendMotionUpdate(entity);
                    }
                    entity.fallDistance = 0;
                    if (anim.canAttack()) {
                        CombatUtils.spinAttackHandler(entity, entity.getLookAngle(), CombatUtils.getAOE(entity, stack, 10), 0.5f, null);
                        entity.swing(InteractionHand.MAIN_HAND, true);
                    }
                } else {
                    if (anim.isAtTick(0.32) || anim.isAtTick(0.48)) {
                        entity.moveRelative(0.2f, new Vec3(0, 0, 1));
                        AttackAction.sendMotionUpdate(entity);
                    } else if (anim.isAtTick(0.92)) {
                        entity.moveRelative(0.6f, new Vec3(0, 2.5, 1.4));
                        AttackAction.sendMotionUpdate(entity);
                    }
                    if (anim.isPastTick(0.92) && !anim.isPastTick(1.38)) {
                        entity.setDeltaMovement(entity.getDeltaMovement().x, entity.getDeltaMovement().y + 0.03, entity.getDeltaMovement().z);
                        AttackAction.sendMotionUpdate(entity);
                    }
                    entity.fallDistance = 0;
                    if (anim.canAttack() || anim.isAtTick(0.52) || anim.isAtTick(1.08)) {
                        CombatUtils.spinAttackHandler(entity, entity.getLookAngle(), CombatUtils.getAOE(entity, stack, 10), 0.5f, null);
                        entity.swing(InteractionHand.MAIN_HAND, true);
                    }
                }
            }))
            .withCountAdjuster((e, handler) -> handler.getCurrentAnim() != null && handler.getCurrentCount() < 7 && handler.getCurrentAnim().isPastTick(1.32) ? 6 : handler.getCurrentCount())
            .setMaxConsecutive(entity -> 7, e -> 0)
            .disableItemSwitch().disableMovement());
    public static final RegistryEntrySupplier<AttackAction> ROUND_BREAK = register("round_break", () -> new AttackAction.Builder((entity, count) -> {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.ROUND_BREAK.create(speed);
    }).doWhileAction(((entity, stack, handler, anim) -> {
                if (anim.isAtTick(0.24)) {
                    entity.moveRelative(0.3f, new Vec3(0, 1.3, 1.1));
                    AttackAction.sendMotionUpdate(entity);
                }
                if (anim.isPastTick(0.24) && !anim.isPastTick(0.6)) {
                    int start = Mth.ceil(0.24 * 20.0D);
                    int end = Mth.ceil(0.6 * 20.0D);
                    float len = (end - start) / anim.getSpeed();
                    float f = (anim.getTick() - start) / anim.getSpeed();
                    float angleInc = -360 / len;
                    float rot = handler.getSpinStartRot();
                    handler.addHitEntityTracker(CombatUtils.spinAttackHandler(entity, (rot + f * angleInc), (rot + (f + 1) * angleInc), 0.5f, e -> !handler.getHitEntityTracker().contains(e)));
                }
            }))
            .disableItemSwitch().disableMovement());
    public static final RegistryEntrySupplier<AttackAction> MIND_THRUST = register("mind_thrust", () -> new AttackAction.Builder((entity, count) -> {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.MIND_THRUST.create(speed);
    }).doWhileAction(((entity, stack, handler, anim) -> {
                if (anim.isAtTick(0.68)) {
                    entity.moveRelative(0.45f, new Vec3(0, 0, 1));
                    AttackAction.sendMotionUpdate(entity);
                }
                if (anim.canAttack())
                    CombatUtils.spinAttackHandler(entity, entity.getLookAngle(), CombatUtils.getAOE(entity, stack, 0), 0.5f, null);
            }))
            .disableItemSwitch().disableMovement());

    public static RegistryEntrySupplier<AttackAction> register(String id, Supplier<AttackAction.Builder> builder) {
        return ATTACK_ACTIONS.register(id, () -> builder.get().build());
    }
}
