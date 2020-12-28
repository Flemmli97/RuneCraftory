package com.flemmli97.runecraftory.common.registry;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.Spell;
import com.flemmli97.runecraftory.common.spells.ArrowSpell;
import com.flemmli97.runecraftory.common.spells.EmptySpell;
import com.flemmli97.runecraftory.common.spells.EvokerFangSpell;
import com.flemmli97.runecraftory.common.spells.FireballSpell;
import com.flemmli97.runecraftory.common.spells.SnowballSpell;
import com.flemmli97.runecraftory.common.spells.WitherSkullSpell;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class ModSpells {

    public static final DeferredRegister<Spell> SPELLS = DeferredRegister.create(Spell.class, RuneCraftory.MODID);
    public static final RegistryObject<Spell> EMPTY = SPELLS.register("empty_spell", EmptySpell::new);

    public static final RegistryObject<Spell> ARROW = SPELLS.register("vanilla_arrow", ArrowSpell::new);
    public static final RegistryObject<Spell> WITHERSKULL = SPELLS.register("vanilla_wither_skull", WitherSkullSpell::new);
    public static final RegistryObject<Spell> EVOKERFANG = SPELLS.register("vanilla_evoker_fang", EvokerFangSpell::new);
    public static final RegistryObject<Spell> SNOWBALL = SPELLS.register("vanilla_snowball", SnowballSpell::new);

    public static final RegistryObject<Spell> FIREBALL = SPELLS.register("fireball", FireballSpell::new);

}
