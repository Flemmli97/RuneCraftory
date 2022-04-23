package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.common.spells.ArrowSpell;
import io.github.flemmli97.runecraftory.common.spells.BaseStaffSpell;
import io.github.flemmli97.runecraftory.common.spells.DoubleWindBladeSpell;
import io.github.flemmli97.runecraftory.common.spells.EmptySpell;
import io.github.flemmli97.runecraftory.common.spells.EvokerFangSpell;
import io.github.flemmli97.runecraftory.common.spells.FireballSpell;
import io.github.flemmli97.runecraftory.common.spells.ParaHealSpell;
import io.github.flemmli97.runecraftory.common.spells.PoisonHealSpell;
import io.github.flemmli97.runecraftory.common.spells.ShortWaterLaserSpell;
import io.github.flemmli97.runecraftory.common.spells.SnowballSpell;
import io.github.flemmli97.runecraftory.common.spells.UnsealSpell;
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
    public static final Supplier<SimpleRegistryWrapper<Spell>> SPELLREGISTRY = new LazyGetter<>(() -> PlatformUtils.INSTANCE.registry(SPELLREGISTRY_KEY));
    public static final RegistryEntrySupplier<Spell> EMPTY = SPELLS.register("empty_spell", EmptySpell::new);

    public static final RegistryEntrySupplier<Spell> STAFFCAST = SPELLS.register("base_staff_spell", BaseStaffSpell::new);

    public static final RegistryEntrySupplier<Spell> ARROW = SPELLS.register("vanilla_arrow", ArrowSpell::new);
    public static final RegistryEntrySupplier<Spell> WITHERSKULL = SPELLS.register("vanilla_wither_skull", WitherSkullSpell::new);
    public static final RegistryEntrySupplier<Spell> EVOKERFANG = SPELLS.register("vanilla_evoker_fang", EvokerFangSpell::new);
    public static final RegistryEntrySupplier<Spell> SNOWBALL = SPELLS.register("vanilla_snowball", SnowballSpell::new);

    public static final RegistryEntrySupplier<Spell> FIREBALL = SPELLS.register("fireball", FireballSpell::new);
    public static final RegistryEntrySupplier<Spell> WATERLASER = SPELLS.register("water_laser", ShortWaterLaserSpell::new);
    public static final RegistryEntrySupplier<Spell> DOUBLESONIC = SPELLS.register("double_sonic", DoubleWindBladeSpell::new);

    public static final RegistryEntrySupplier<Spell> MEDIPOISON = SPELLS.register("medi_poison", PoisonHealSpell::new);
    public static final RegistryEntrySupplier<Spell> MEDIPARA = SPELLS.register("medi_paralysis", ParaHealSpell::new);
    public static final RegistryEntrySupplier<Spell> MEDISEAL = SPELLS.register("medi_seal", UnsealSpell::new);

}
