package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {

    public static final PlatformRegistry<SoundEvent> SOUND_EVENTS = PlatformUtils.INSTANCE.of(Registry.SOUND_EVENT_REGISTRY, RuneCraftory.MODID);
    public static final Object2IntMap<ResourceLocation> VARIATIONS = new Object2IntArrayMap<>();

    public static final RegistryEntrySupplier<SoundEvent> ENTITY_AMBROSIA_WAVE = register("entity.ambrosia.wave");
    public static final RegistryEntrySupplier<SoundEvent> ENTITY_CHIMERA_AMBIENT = register("entity.chimera.ambient", 2);
    public static final RegistryEntrySupplier<SoundEvent> ENTITY_CHIPSQUEEK_HURT = register("entity.chipsqueek.hurt", 2);
    public static final RegistryEntrySupplier<SoundEvent> ENTITY_DEAD_TREE_DEATH = register("entity.dead_tree.death");
    public static final RegistryEntrySupplier<SoundEvent> ENTITY_FAIRY_AMBIENT = register("entity.fairy.ambient");
    public static final RegistryEntrySupplier<SoundEvent> ENTITY_FLOWER_LILY_STEP = register("entity.flower_lily.step", 2);
    public static final RegistryEntrySupplier<SoundEvent> ENTITY_GHOST_AMBIENT = register("entity.ghost.ambient", 3);
    public static final RegistryEntrySupplier<SoundEvent> ENTITY_GOBLING_AMBIENT = register("entity.goblin.ambient", 3);
    public static final RegistryEntrySupplier<SoundEvent> ENTITY_GOBLING_HURT = register("entity.goblin.hurt");
    public static final RegistryEntrySupplier<SoundEvent> ENTITY_GOBLIN_DEATH = register("entity.goblin.death");
    public static final RegistryEntrySupplier<SoundEvent> ENTITY_ORC_BONK = register("entity.orc.bonk");
    public static final RegistryEntrySupplier<SoundEvent> ENTITY_PANTHER_AMBIENT = register("entity.panther.ambient");
    public static final RegistryEntrySupplier<SoundEvent> ENTITY_PANTHER_HURT = register("entity.panther.hurt");
    public static final RegistryEntrySupplier<SoundEvent> ENTITY_RAFFLESIA_ANGRY = register("entity.rafflesia.angry");
    public static final RegistryEntrySupplier<SoundEvent> ENTITY_RAFFLESIA_DEATH = register("entity.rafflesia.death");
    public static final RegistryEntrySupplier<SoundEvent> ENTITY_SCORPION_STEP = register("entity.scorpion.step");
    public static final RegistryEntrySupplier<SoundEvent> ENTITY_SKELEFANG_ROAR = register("entity.skelefang.roar");
    public static final RegistryEntrySupplier<SoundEvent> ENTITY_SKELEFANG_CHARGE = register("entity.skelefang.charge");
    public static final RegistryEntrySupplier<SoundEvent> ENTITY_THUNDERBOLT_NEIGH = register("entity.thunderbolt.neigh");
    public static final RegistryEntrySupplier<SoundEvent> ENTITY_WASP_BUZZ = register("entity.wasp.buzz", 3);
    public static final RegistryEntrySupplier<SoundEvent> ENTITY_WEAGLE_FLAP = register("entity.weagle.flap");
    public static final RegistryEntrySupplier<SoundEvent> ENTITY_WISP_AMBIENT = register("entity.wisp.ambient");
    public static final RegistryEntrySupplier<SoundEvent> PLAYER_ATTACK_SWOOSH = register("player.attack.swoosh", 4);
    public static final RegistryEntrySupplier<SoundEvent> PLAYER_ATTACK_SWOOSH_HEAVY = register("player.attack.swoosh_heavy");
    public static final RegistryEntrySupplier<SoundEvent> PLAYER_ATTACK_SWOOSH_LIGHT = register("player.attack.swoosh_light", 3);
    public static final RegistryEntrySupplier<SoundEvent> PLAYER_ARMOR_PIYO_CHIRP = register("player.armor.piyo_chirp", 4);
    public static final RegistryEntrySupplier<SoundEvent> SPELL_APPLE_RAIN = register("spell.apple_rain");
    public static final RegistryEntrySupplier<SoundEvent> SPELL_GENERIC_BUFF = register("spell.generic.buff");
    public static final RegistryEntrySupplier<SoundEvent> SPELL_GENERIC_DARK = register("spell.generic.dark");
    public static final RegistryEntrySupplier<SoundEvent> SPELL_GENERIC_ELECTRIC_ZAP = register("spell.generic.electric_zap");
    public static final RegistryEntrySupplier<SoundEvent> SPELL_GENERIC_FIRE_BALL = register("spell.generic.fireball");
    public static final RegistryEntrySupplier<SoundEvent> SPELL_GENERIC_HEAL = register("spell.generic.heal", 3);
    public static final RegistryEntrySupplier<SoundEvent> SPELL_GENERIC_LEAP = register("spell.generic.leap");
    public static final RegistryEntrySupplier<SoundEvent> SPELL_GENERIC_LIGHT = register("spell.generic.light");
    public static final RegistryEntrySupplier<SoundEvent> SPELL_GENERIC_POOF = register("spell.generic.poof");
    public static final RegistryEntrySupplier<SoundEvent> SPELL_GENERIC_POP = register("spell.generic.pop");
    public static final RegistryEntrySupplier<SoundEvent> SPELL_GENERIC_ROCKS = register("spell.generic.rocks");
    public static final RegistryEntrySupplier<SoundEvent> SPELL_GENERIC_WATER = register("spell.generic.wave");
    public static final RegistryEntrySupplier<SoundEvent> SPELL_GENERIC_WATERBUBBLE = register("spell.generic.water_bubble");
    public static final RegistryEntrySupplier<SoundEvent> SPELL_GENERIC_WIND = register("spell.generic.wind");
    public static final RegistryEntrySupplier<SoundEvent> SPELL_GENERIC_WIND_LONG = register("spell.generic.wind_long");
    public static final RegistryEntrySupplier<SoundEvent> SPELL_NAIVE_BLADE = register("spell.naive_blade");
    public static final RegistryEntrySupplier<SoundEvent> SPELL_STRAIGHT_PUNCH = register("spell.straight_punch");

    private static RegistryEntrySupplier<SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(RuneCraftory.MODID, name)));
    }

    private static RegistryEntrySupplier<SoundEvent> register(String name, int variations) {
        RegistryEntrySupplier<SoundEvent> res = SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(RuneCraftory.MODID, name)));
        if (Platform.INSTANCE.isDatagen())
            VARIATIONS.put(res.getID(), variations);
        return res;
    }

}
