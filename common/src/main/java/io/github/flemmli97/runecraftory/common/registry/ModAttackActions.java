package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.action.AttackAction;
import io.github.flemmli97.runecraftory.common.attackactions.DashSlashAttack;
import io.github.flemmli97.runecraftory.common.attackactions.DualBladeAttack;
import io.github.flemmli97.runecraftory.common.attackactions.EmptyAction;
import io.github.flemmli97.runecraftory.common.attackactions.FireballUseAttack;
import io.github.flemmli97.runecraftory.common.attackactions.GloveAttack;
import io.github.flemmli97.runecraftory.common.attackactions.GloveUseAttack;
import io.github.flemmli97.runecraftory.common.attackactions.GustAttack;
import io.github.flemmli97.runecraftory.common.attackactions.HammerAxeAttack;
import io.github.flemmli97.runecraftory.common.attackactions.HammerAxeUseAttack;
import io.github.flemmli97.runecraftory.common.attackactions.LongSwordAttack;
import io.github.flemmli97.runecraftory.common.attackactions.MindThrustAttack;
import io.github.flemmli97.runecraftory.common.attackactions.PowerWaveAttack;
import io.github.flemmli97.runecraftory.common.attackactions.RailStrikeAttack;
import io.github.flemmli97.runecraftory.common.attackactions.RoundBreakAttack;
import io.github.flemmli97.runecraftory.common.attackactions.RushAttack;
import io.github.flemmli97.runecraftory.common.attackactions.SelfBuffSpell;
import io.github.flemmli97.runecraftory.common.attackactions.ShortSwordAttack;
import io.github.flemmli97.runecraftory.common.attackactions.SpearAttack;
import io.github.flemmli97.runecraftory.common.attackactions.SpearUseAttack;
import io.github.flemmli97.runecraftory.common.attackactions.SpellUseAttack;
import io.github.flemmli97.runecraftory.common.attackactions.StaffAttack;
import io.github.flemmli97.runecraftory.common.attackactions.StormAttack;
import io.github.flemmli97.runecraftory.common.attackactions.TimedUseAttack;
import io.github.flemmli97.runecraftory.common.attackactions.ToolHammerUse;
import io.github.flemmli97.runecraftory.common.attackactions.TwinAttack;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemDualBladeBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemLongSwordBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemShortSwordBase;
import io.github.flemmli97.runecraftory.platform.LazyGetter;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import io.github.flemmli97.tenshilib.platform.registry.SimpleRegistryWrapper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class ModAttackActions {

    public static final ResourceKey<? extends Registry<AttackAction>> ATTACK_ACTION_KEY = ResourceKey.createRegistryKey(new ResourceLocation(RuneCraftory.MODID, "attack_action"));
    public static final Supplier<SimpleRegistryWrapper<AttackAction>> ATTACK_ACTION_REGISTRY = new LazyGetter<>(() -> PlatformUtils.INSTANCE.registry(ATTACK_ACTION_KEY));
    public static final PlatformRegistry<AttackAction> ATTACK_ACTIONS = PlatformUtils.INSTANCE.customRegistry(AttackAction.class, ATTACK_ACTION_KEY, new ResourceLocation(RuneCraftory.MODID, "none"), true, true);

    public static final RegistryEntrySupplier<AttackAction> NONE = register("none", EmptyAction::new);

    //Short sword attack sequence
    public static final RegistryEntrySupplier<AttackAction> SHORT_SWORD = register("short_sword", ShortSwordAttack::new);
    public static final RegistryEntrySupplier<AttackAction> SHORT_SWORD_USE = register("short_sword_use", () -> new TimedUseAttack(() -> new AnimatedAction(16 + 1, 6, "short_sword_use"), ItemShortSwordBase::delayedRightClickAction));

    public static final RegistryEntrySupplier<AttackAction> LONG_SWORD = register("long_sword", LongSwordAttack::new);
    public static final RegistryEntrySupplier<AttackAction> LONGSWORD_USE = register("long_sword_use", () -> new TimedUseAttack(() -> new AnimatedAction(16 + 1, 5, "long_sword_use"), ItemLongSwordBase::delayedRightClickAction));

    public static final RegistryEntrySupplier<AttackAction> SPEAR = register("spear", SpearAttack::new);
    public static final RegistryEntrySupplier<AttackAction> SPEAR_USE = register("spear_use", SpearUseAttack::new);

    public static final RegistryEntrySupplier<AttackAction> HAMMER_AXE = register("hammer_axe", HammerAxeAttack::new);
    public static final RegistryEntrySupplier<AttackAction> HAMMER_AXE_USE = register("hammer_axe_use", HammerAxeUseAttack::new);

    public static final RegistryEntrySupplier<AttackAction> DUAL_BLADES = register("dual_blades", DualBladeAttack::new);
    public static final RegistryEntrySupplier<AttackAction> DUAL_USE = register("dual_blade_use", () -> new TimedUseAttack(() -> new AnimatedAction(15 + 1, 7, "dual_blades_use"), ItemDualBladeBase::delayedRightClickAction));

    public static final RegistryEntrySupplier<AttackAction> GLOVES = register("gloves", GloveAttack::new);
    public static final RegistryEntrySupplier<AttackAction> GLOVE_USE = register("glove_use", GloveUseAttack::new);

    public static final RegistryEntrySupplier<AttackAction> STAFF = register("staff", StaffAttack::new);

    public static final RegistryEntrySupplier<AttackAction> STAFF_USE = register("staff_use", SpellUseAttack::new);

    public static final RegistryEntrySupplier<AttackAction> TOOL_AXE_USE = register("tool_axe", () -> new TimedUseAttack(() -> AnimatedAction.builder(20 + 1, "hammer_axe_use").marker(12).speed(1.3f).build(), (entity, stack) -> {
        //TODO
    }));
    public static final RegistryEntrySupplier<AttackAction> TOOL_HAMMER_USE = register("tool_hammer", ToolHammerUse::new);
    public static final RegistryEntrySupplier<AttackAction> FIREBALL_USE = register("fireball_use", () -> new FireballUseAttack(false));
    public static final RegistryEntrySupplier<AttackAction> FIREBALL_BIG_USE = register("fireball_big_use", () -> new FireballUseAttack(true));

    public static final RegistryEntrySupplier<AttackAction> TOOL_ATTACK = register("tool_attack", () -> new TimedUseAttack(() -> AnimatedAction.builder(20, "tool_attack").marker(7).build(), (entity, stack) -> {
        //TODO
    }));

    public static final RegistryEntrySupplier<AttackAction> POWER_WAVE = register("power_wave", PowerWaveAttack::new);
    public static final RegistryEntrySupplier<AttackAction> DASH_SLASH = register("dash_slash", DashSlashAttack::new);
    public static final RegistryEntrySupplier<AttackAction> RUSH_ATTACK = register("rush_attack", RushAttack::new);
    public static final RegistryEntrySupplier<AttackAction> ROUND_BREAK = register("round_break", RoundBreakAttack::new);
    public static final RegistryEntrySupplier<AttackAction> MIND_THRUST = register("mind_thrust", MindThrustAttack::new);

    public static final RegistryEntrySupplier<AttackAction> BLITZ = register("blitz", SelfBuffSpell::new);
    public static final RegistryEntrySupplier<AttackAction> TWIN_ATTACK = register("twin_attack", TwinAttack::new);
    public static final RegistryEntrySupplier<AttackAction> STORM = register("storm", StormAttack::new);
    public static final RegistryEntrySupplier<AttackAction> GUST = register("gust", GustAttack::new);
    public static final RegistryEntrySupplier<AttackAction> RAIL_STRIKE = register("rail_strike", RailStrikeAttack::new);

    public static RegistryEntrySupplier<AttackAction> register(String id, Supplier<AttackAction> action) {
        return ATTACK_ACTIONS.register(id, action);
    }
}
