package com.flemmli97.runecraftory.common.events;

import com.flemmli97.runecraftory.api.Spell;
import com.flemmli97.runecraftory.common.registry.ModSpells;
import net.minecraftforge.eventbus.api.Event;

public class SpellRegisterEvent extends Event {

    public final ModSpells.WrappedMap<Spell> map;

    public SpellRegisterEvent(ModSpells.WrappedMap<Spell> map){
        this.map = map;
    }
}
