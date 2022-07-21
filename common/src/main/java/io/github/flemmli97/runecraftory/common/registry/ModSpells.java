package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityDarkBall;
import io.github.flemmli97.runecraftory.common.spells.ArrowSpell;
import io.github.flemmli97.runecraftory.common.spells.BaseStaffSpell;
import io.github.flemmli97.runecraftory.common.spells.DarkBallSpell;
import io.github.flemmli97.runecraftory.common.spells.DarknessSpell;
import io.github.flemmli97.runecraftory.common.spells.DoubleWaterLaserSpell;
import io.github.flemmli97.runecraftory.common.spells.DoubleWindBladeSpell;
import io.github.flemmli97.runecraftory.common.spells.EmptySpell;
import io.github.flemmli97.runecraftory.common.spells.EvokerFangSpell;
import io.github.flemmli97.runecraftory.common.spells.ExplosionSpell;
import io.github.flemmli97.runecraftory.common.spells.FireballSpell;
import io.github.flemmli97.runecraftory.common.spells.HealT1Spell;
import io.github.flemmli97.runecraftory.common.spells.HealT2Spell;
import io.github.flemmli97.runecraftory.common.spells.HealT3Spell;
import io.github.flemmli97.runecraftory.common.spells.LightBarrierSpell;
import io.github.flemmli97.runecraftory.common.spells.ParaHealSpell;
import io.github.flemmli97.runecraftory.common.spells.PenetrateWindBladeSpell;
import io.github.flemmli97.runecraftory.common.spells.PoisonHealSpell;
import io.github.flemmli97.runecraftory.common.spells.PrismSpell;
import io.github.flemmli97.runecraftory.common.spells.RockSpearSpell;
import io.github.flemmli97.runecraftory.common.spells.ShineSpell;
import io.github.flemmli97.runecraftory.common.spells.SnowballSpell;
import io.github.flemmli97.runecraftory.common.spells.TripleWaterLaserSpell;
import io.github.flemmli97.runecraftory.common.spells.UnsealSpell;
import io.github.flemmli97.runecraftory.common.spells.WaterLaserSpell;
import io.github.flemmli97.runecraftory.common.spells.WindBladeSpell;
import io.github.flemmli97.runecraftory.common.spells.WitherSkullSpell;
import io.github.flemmli97.runecraftory.platform.LazyGetter;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import io.github.flemmli97.tenshilib.platform.registry.SimpleRegistryWrapper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class ModSpells {

    public static final ResourceKey<? extends Registry<Spell>> SPELLREGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(RuneCraftory.MODID, "spells"));

    public static final PlatformRegistry<Spell> SPELLS = PlatformUtils.INSTANCE.customRegistry(Spell.class, SPELLREGISTRY_KEY, new ResourceLocation(RuneCraftory.MODID, "empty_spell"), true, true);
    public static final RegistryEntrySupplier<Spell> EMPTY = SPELLS.register("empty_spell", EmptySpell::new);
    public static final RegistryEntrySupplier<Spell> STAFFCAST = SPELLS.register("base_staff_spell", BaseStaffSpell::new);
    public static final RegistryEntrySupplier<Spell> ARROW = SPELLS.register("vanilla_arrow", ArrowSpell::new);
    public static final RegistryEntrySupplier<Spell> WITHERSKULL = SPELLS.register("vanilla_wither_skull", WitherSkullSpell::new);
    public static final RegistryEntrySupplier<Spell> EVOKERFANG = SPELLS.register("vanilla_evoker_fang", EvokerFangSpell::new);
    public static final RegistryEntrySupplier<Spell> SNOWBALL = SPELLS.register("vanilla_snowball", SnowballSpell::new);
    public static final RegistryEntrySupplier<Spell> FIREBALL = SPELLS.register("fireball", () -> new FireballSpell(false));
    public static final RegistryEntrySupplier<Spell> BIGFIREBALL = SPELLS.register("big_fireball", () -> new FireballSpell(true));
    public static final RegistryEntrySupplier<Spell> EXPLOSION = SPELLS.register("explosion", ExplosionSpell::new);
    public static final RegistryEntrySupplier<Spell> WATERLASER = SPELLS.register("water_laser", WaterLaserSpell::new);
    public static final RegistryEntrySupplier<Spell> PARALLELLASER = SPELLS.register("parallel_laser", DoubleWaterLaserSpell::new);
    public static final RegistryEntrySupplier<Spell> DELTALASER = SPELLS.register("delta_laser", TripleWaterLaserSpell::new);
    public static final RegistryEntrySupplier<Spell> SCREWROCK = SPELLS.register("screw_rock", () -> new RockSpearSpell(false));
    //public static final RegistryEntrySupplier<Spell> DELTALASER = SPELLS.register("delta_laser", TripleWaterLaserSpell::new);
    public static final RegistryEntrySupplier<Spell> AVENGERROCK = SPELLS.register("avenger_rock", () -> new RockSpearSpell(true));
    public static final RegistryEntrySupplier<Spell> SONIC = SPELLS.register("sonic", WindBladeSpell::new);
    public static final RegistryEntrySupplier<Spell> DOUBLESONIC = SPELLS.register("double_sonic", DoubleWindBladeSpell::new);
    public static final RegistryEntrySupplier<Spell> PENETRATESONIC = SPELLS.register("penetrate_sonic", PenetrateWindBladeSpell::new);
    public static final RegistryEntrySupplier<Spell> LIGHTBARRIER = SPELLS.register("light_barrier", LightBarrierSpell::new);
    public static final RegistryEntrySupplier<Spell> SHINE = SPELLS.register("shine", ShineSpell::new);
    public static final RegistryEntrySupplier<Spell> PRISM = SPELLS.register("prism", PrismSpell::new);
    public static final RegistryEntrySupplier<Spell> DARKSNAKE = SPELLS.register("dark_snake", () -> new DarkBallSpell(EntityDarkBall.Type.SNAKE));
    public static final RegistryEntrySupplier<Spell> DARKBALL = SPELLS.register("dark_ball", () -> new DarkBallSpell(EntityDarkBall.Type.BALL));
    public static final RegistryEntrySupplier<Spell> DARKNESS = SPELLS.register("darkness", DarknessSpell::new);
    public static final RegistryEntrySupplier<Spell> CURE = SPELLS.register("cure", HealT1Spell::new);
    public static final RegistryEntrySupplier<Spell> CUREALL = SPELLS.register("cure_all", HealT2Spell::new);
    public static final RegistryEntrySupplier<Spell> MASTERCURE = SPELLS.register("master_cure", HealT3Spell::new);
    public static final RegistryEntrySupplier<Spell> MEDIPOISON = SPELLS.register("medi_poison", PoisonHealSpell::new);
    public static final RegistryEntrySupplier<Spell> MEDIPARA = SPELLS.register("medi_paralysis", ParaHealSpell::new);
    public static final RegistryEntrySupplier<Spell> MEDISEAL = SPELLS.register("medi_seal", UnsealSpell::new);
    public static final Supplier<SimpleRegistryWrapper<Spell>> SPELLREGISTRY = new LazyGetter<>(() -> PlatformUtils.INSTANCE.registry(SPELLREGISTRY_KEY));

}
