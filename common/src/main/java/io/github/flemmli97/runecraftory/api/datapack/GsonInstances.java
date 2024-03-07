package io.github.flemmli97.runecraftory.api.datapack;

import com.google.common.base.Suppliers;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class GsonInstances {

    public static final Gson ATTRIBUTE_SPELLS = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().disableHtmlEscaping()
            .registerTypeAdapter(Attribute.class, new RegistryObjectSerializer<>(Suppliers.memoize(PlatformUtils.INSTANCE::attributes)))
            .registerTypeAdapter(Spell.class, new RegistryObjectSerializer<>(Suppliers.memoize(ModSpells.SPELL_REGISTRY::get))).create();


    public static final Gson ATTRIBUTE_EFFECTS = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting()
            .registerTypeAdapter(Attribute.class, new RegistryObjectSerializer<>(Suppliers.memoize(PlatformUtils.INSTANCE::attributes)))
            .registerTypeAdapter(MobEffect.class, new RegistryObjectSerializer<>(Suppliers.memoize(PlatformUtils.INSTANCE::effects))).create();
}
