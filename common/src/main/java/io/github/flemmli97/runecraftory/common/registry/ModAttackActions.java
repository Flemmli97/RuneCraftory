package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.common.attackactions.AxelDisasterAttack;
import io.github.flemmli97.runecraftory.common.attackactions.CycloneAttack;
import io.github.flemmli97.runecraftory.common.attackactions.DashSlashAttack;
import io.github.flemmli97.runecraftory.common.attackactions.DeltaStrikeAttack;
import io.github.flemmli97.runecraftory.common.attackactions.DoubleKickAttack;
import io.github.flemmli97.runecraftory.common.attackactions.DualBladeAttack;
import io.github.flemmli97.runecraftory.common.attackactions.EmptyAction;
import io.github.flemmli97.runecraftory.common.attackactions.FireballUseAttack;
import io.github.flemmli97.runecraftory.common.attackactions.FlashStrikeAttack;
import io.github.flemmli97.runecraftory.common.attackactions.GigaSwingAttack;
import io.github.flemmli97.runecraftory.common.attackactions.GloveAttack;
import io.github.flemmli97.runecraftory.common.attackactions.GloveUseAttack;
import io.github.flemmli97.runecraftory.common.attackactions.GrandImpactAttack;
import io.github.flemmli97.runecraftory.common.attackactions.GustAttack;
import io.github.flemmli97.runecraftory.common.attackactions.HammerAxeAttack;
import io.github.flemmli97.runecraftory.common.attackactions.HammerAxeUseAttack;
import io.github.flemmli97.runecraftory.common.attackactions.HurricaneAttack;
import io.github.flemmli97.runecraftory.common.attackactions.LongSwordAttack;
import io.github.flemmli97.runecraftory.common.attackactions.MillionStrikeAttack;
import io.github.flemmli97.runecraftory.common.attackactions.MindThrustAttack;
import io.github.flemmli97.runecraftory.common.attackactions.NaiveBladeAttack;
import io.github.flemmli97.runecraftory.common.attackactions.NekoDamashiAttack;
import io.github.flemmli97.runecraftory.common.attackactions.PowerWaveAttack;
import io.github.flemmli97.runecraftory.common.attackactions.RailStrikeAttack;
import io.github.flemmli97.runecraftory.common.attackactions.RapidMoveAttack;
import io.github.flemmli97.runecraftory.common.attackactions.ReaperSlashAttack;
import io.github.flemmli97.runecraftory.common.attackactions.RoundBreakAttack;
import io.github.flemmli97.runecraftory.common.attackactions.RushAttack;
import io.github.flemmli97.runecraftory.common.attackactions.RushPunchAttack;
import io.github.flemmli97.runecraftory.common.attackactions.SelfBuffSpell;
import io.github.flemmli97.runecraftory.common.attackactions.ShortSwordAttack;
import io.github.flemmli97.runecraftory.common.attackactions.SpearAttack;
import io.github.flemmli97.runecraftory.common.attackactions.SpearUseAttack;
import io.github.flemmli97.runecraftory.common.attackactions.SpellUseAttack;
import io.github.flemmli97.runecraftory.common.attackactions.StaffAttack;
import io.github.flemmli97.runecraftory.common.attackactions.StardustUpperAttack;
import io.github.flemmli97.runecraftory.common.attackactions.StormAttack;
import io.github.flemmli97.runecraftory.common.attackactions.StraightPunchAttack;
import io.github.flemmli97.runecraftory.common.attackactions.TimedUseAttack;
import io.github.flemmli97.runecraftory.common.attackactions.ToolHammerUse;
import io.github.flemmli97.runecraftory.common.attackactions.TornadoSwingAttack;
import io.github.flemmli97.runecraftory.common.attackactions.TwinAttack;
import io.github.flemmli97.runecraftory.common.attackactions.UpperCutAttack;
import io.github.flemmli97.runecraftory.common.attackactions.WaterLaserAttack;
import io.github.flemmli97.runecraftory.common.attackactions.WindSlashAttack;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemDualBladeBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemLongSwordBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemShortSwordBase;
import io.github.flemmli97.tenshilib.common.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

public class ModAttackActions {

    public static final ResourceKey<? extends Registry<AttackAction>> ATTACK_ACTION_KEY = ResourceKey.createRegistryKey(RuneCraftory.modRes("attack_action"));
    public static final LoaderRegistryAccess.CustomLoaderRegistry<AttackAction> ATTACK_ACTIONS = LoaderRegistryAccess.INSTANCE.newRegistry(ATTACK_ACTION_KEY, RuneCraftory.modRes("none"), true, true);

    public static final RegistryEntrySupplier<AttackAction, EmptyAction> NONE = register("none", EmptyAction::new);

    //Short sword attack sequence
    public static final RegistryEntrySupplier<AttackAction, ShortSwordAttack> SHORT_SWORD = register("short_sword", ShortSwordAttack::new);
    public static final RegistryEntrySupplier<AttackAction, TimedUseAttack> SHORT_SWORD_USE = register("short_sword_use", () -> new TimedUseAttack(PlayerModelAnimations.SHORT_SWORD_USE, ItemShortSwordBase::delayedRightClickAction));

    public static final RegistryEntrySupplier<AttackAction, LongSwordAttack> LONG_SWORD = register("long_sword", LongSwordAttack::new);
    public static final RegistryEntrySupplier<AttackAction, TimedUseAttack> LONGSWORD_USE = register("long_sword_use", () -> new TimedUseAttack(PlayerModelAnimations.LONG_SWORD_USE, ItemLongSwordBase::delayedRightClickAction));

    public static final RegistryEntrySupplier<AttackAction, SpearAttack> SPEAR = register("spear", SpearAttack::new);
    public static final RegistryEntrySupplier<AttackAction, SpearUseAttack> SPEAR_USE = register("spear_use", SpearUseAttack::new);

    public static final RegistryEntrySupplier<AttackAction, HammerAxeAttack> HAMMER_AXE = register("hammer_axe", HammerAxeAttack::new);
    public static final RegistryEntrySupplier<AttackAction, HammerAxeUseAttack> HAMMER_AXE_USE = register("hammer_axe_use", HammerAxeUseAttack::new);

    public static final RegistryEntrySupplier<AttackAction, DualBladeAttack> DUAL_BLADES = register("dual_blades", DualBladeAttack::new);
    public static final RegistryEntrySupplier<AttackAction, TimedUseAttack> DUAL_USE = register("dual_blade_use", () -> new TimedUseAttack(PlayerModelAnimations.DUAL_BLADES_USE, false, ItemDualBladeBase::delayedRightClickAction, true));

    public static final RegistryEntrySupplier<AttackAction, GloveAttack> GLOVES = register("gloves", GloveAttack::new);
    public static final RegistryEntrySupplier<AttackAction, GloveUseAttack> GLOVE_USE = register("glove_use", GloveUseAttack::new);

    public static final RegistryEntrySupplier<AttackAction, StaffAttack> STAFF = register("staff", StaffAttack::new);

    public static final RegistryEntrySupplier<AttackAction, SpellUseAttack> STAFF_USE = register("staff_use", SpellUseAttack::new);

    public static final RegistryEntrySupplier<AttackAction, TimedUseAttack> TOOL_AXE_USE = register("tool_axe", () -> new TimedUseAttack(PlayerModelAnimations.HAMME_AXE_USE.create(1.1f), true, (entity, stack) -> {
        //TODO
    }, false));
    public static final RegistryEntrySupplier<AttackAction, ToolHammerUse> TOOL_HAMMER_USE = register("tool_hammer", ToolHammerUse::new);
    public static final RegistryEntrySupplier<AttackAction, FireballUseAttack> FIREBALL_USE = register("fireball_use", () -> new FireballUseAttack(false));
    public static final RegistryEntrySupplier<AttackAction, FireballUseAttack> FIREBALL_BIG_USE = register("fireball_big_use", () -> new FireballUseAttack(true));

    public static final RegistryEntrySupplier<AttackAction, WaterLaserAttack> WATER_LASER_USE = register("water_laser_use", () -> new WaterLaserAttack(0));
    public static final RegistryEntrySupplier<AttackAction, WaterLaserAttack> DOUBLE_WATER_LASER_USE = register("double_water_laser_use", () -> new WaterLaserAttack(1));
    public static final RegistryEntrySupplier<AttackAction, WaterLaserAttack> TRIPLE_WATER_LASER_USE = register("triple_water_laser_use", () -> new WaterLaserAttack(2));

    public static final RegistryEntrySupplier<AttackAction, TimedUseAttack> TOOL_ATTACK = register("tool_attack", () -> new TimedUseAttack(AnimatedAction.builder(1, "tool_attack").build(), true, (entity, stack) -> {
        //TODO
    }, false));

    public static final RegistryEntrySupplier<AttackAction, PowerWaveAttack> POWER_WAVE = register("power_wave", PowerWaveAttack::new);
    public static final RegistryEntrySupplier<AttackAction, DashSlashAttack> DASH_SLASH = register("dash_slash", DashSlashAttack::new);
    public static final RegistryEntrySupplier<AttackAction, RushAttack> RUSH_ATTACK = register("rush_attack", RushAttack::new);
    public static final RegistryEntrySupplier<AttackAction, RoundBreakAttack> ROUND_BREAK = register("round_break", RoundBreakAttack::new);
    public static final RegistryEntrySupplier<AttackAction, MindThrustAttack> MIND_THRUST = register("mind_thrust", MindThrustAttack::new);

    public static final RegistryEntrySupplier<AttackAction, SelfBuffSpell> BLITZ = register("blitz", SelfBuffSpell::new);
    public static final RegistryEntrySupplier<AttackAction, TwinAttack> TWIN_ATTACK = register("twin_attack", TwinAttack::new);
    public static final RegistryEntrySupplier<AttackAction, StormAttack> STORM = register("storm", StormAttack::new);
    public static final RegistryEntrySupplier<AttackAction, GustAttack> GUST = register("gust", GustAttack::new);
    public static final RegistryEntrySupplier<AttackAction, RailStrikeAttack> RAIL_STRIKE = register("rail_strike", RailStrikeAttack::new);

    public static final RegistryEntrySupplier<AttackAction, WindSlashAttack> WIND_SLASH = register("wind_slash", WindSlashAttack::new);
    public static final RegistryEntrySupplier<AttackAction, FlashStrikeAttack> FLASH_STRIKE = register("flash_strike", FlashStrikeAttack::new);
    public static final RegistryEntrySupplier<AttackAction, SelfBuffSpell> STEEL_HEART = register("steel_heart", SelfBuffSpell::new);
    public static final RegistryEntrySupplier<AttackAction, DeltaStrikeAttack> DELTA_STRIKE = register("delta_strike", DeltaStrikeAttack::new);
    public static final RegistryEntrySupplier<AttackAction, NaiveBladeAttack> NAIVE_BLADE = register("naive_blade", NaiveBladeAttack::new);

    public static final RegistryEntrySupplier<AttackAction, HurricaneAttack> HURRICANE = register("hurricane", HurricaneAttack::new);
    public static final RegistryEntrySupplier<AttackAction, ReaperSlashAttack> REAPER_SLASH = register("reaper_slash", ReaperSlashAttack::new);
    public static final RegistryEntrySupplier<AttackAction, MillionStrikeAttack> MILLION_STRIKE = register("million_strike", MillionStrikeAttack::new);
    public static final RegistryEntrySupplier<AttackAction, AxelDisasterAttack> AXEL_DISASTER = register("axel_disaster", AxelDisasterAttack::new);

    public static final RegistryEntrySupplier<AttackAction, StardustUpperAttack> STARDUST_UPPER = register("stardust_upper", StardustUpperAttack::new);
    public static final RegistryEntrySupplier<AttackAction, GrandImpactAttack> GRAND_IMPACT = register("grand_impact", GrandImpactAttack::new);
    public static final RegistryEntrySupplier<AttackAction, TornadoSwingAttack> TORNADO_SWING = register("tornado_swing", TornadoSwingAttack::new);
    public static final RegistryEntrySupplier<AttackAction, GigaSwingAttack> GIGA_SWING = register("giga_swing", GigaSwingAttack::new);

    public static final RegistryEntrySupplier<AttackAction, UpperCutAttack> UPPER_CUT = register("upper_cut", UpperCutAttack::new);
    public static final RegistryEntrySupplier<AttackAction, DoubleKickAttack> DOUBLE_KICK = register("double_kick", DoubleKickAttack::new);
    public static final RegistryEntrySupplier<AttackAction, StraightPunchAttack> STRAIGHT_PUNCH = register("straight_punch", StraightPunchAttack::new);
    public static final RegistryEntrySupplier<AttackAction, NekoDamashiAttack> NEKO_DAMASHI = register("neko_damashi", NekoDamashiAttack::new);
    public static final RegistryEntrySupplier<AttackAction, RushPunchAttack> RUSH_PUNCH = register("rush_punch", RushPunchAttack::new);
    public static final RegistryEntrySupplier<AttackAction, CycloneAttack> CYCLONE = register("cyclone", CycloneAttack::new);
    public static final RegistryEntrySupplier<AttackAction, RapidMoveAttack> RAPID_MOVE = register("rapid_move", RapidMoveAttack::new);

    public static <T extends AttackAction> RegistryEntrySupplier<AttackAction, T> register(String id, Supplier<T> action) {
        return ATTACK_ACTIONS.register().register(id, action);
    }
}
