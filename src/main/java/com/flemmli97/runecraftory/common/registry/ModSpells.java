package com.flemmli97.runecraftory.common.registry;

import com.flemmli97.runecraftory.api.Spell;
import com.flemmli97.runecraftory.common.events.SpellRegisterEvent;
import com.flemmli97.runecraftory.common.spells.EmptySpell;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.HashMap;
import java.util.Map;

public class ModSpells {

    private static final Map<ResourceLocation, Spell> spellRegistry = new HashMap<>();

    public static final Spell EMPTY = new EmptySpell();

    private static Spell register(ResourceLocation res, Spell spell) {
        spellRegistry.put(res, spell.setRegistryName(res));
        return spell;
    }

    public static Spell getSpell(ResourceLocation res) {
        return spellRegistry.getOrDefault(res, EMPTY);
    }

    public static void register() {

        MinecraftForge.EVENT_BUS.post(new SpellRegisterEvent(new WrappedMap<>(spellRegistry)));
    }

    public static class WrappedMap<T extends IForgeRegistryEntry<T>> {

        private final Map<ResourceLocation, T> map;

        public WrappedMap(Map<ResourceLocation, T> map) {
            this.map = map;
        }

        public T register(ResourceLocation res, T spell) {
            this.map.put(res, spell.setRegistryName(res));
            return spell;
        }

    }
}
