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
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModSounds {

    public static final PlatformRegistry<SoundEvent> SOUND_EVENTS = PlatformUtils.INSTANCE.of(Registry.SOUND_EVENT_REGISTRY, RuneCraftory.MODID);
    public static final Object2IntMap<ResourceLocation> VARIATIONS = new Object2IntArrayMap<>();
    public static final List<BGMHolder> BGM = new ArrayList<>();
    public static final Map<ResourceLocation, RegistryEntrySupplier<Item>> BGM_RECORD = new HashMap<>();

    public static final RegistryEntrySupplier<SoundEvent> ENTITY_AMBROSIA_WAVE = register("entity.ambrosia.wave");
    public static final RegistryEntrySupplier<SoundEvent> ENTITY_CHIMERA_AMBIENT = register("entity.chimera.ambient", 2);
    public static final RegistryEntrySupplier<SoundEvent> ENTITY_CHIPSQUEEK_HURT = register("entity.chipsqueek.hurt", 2);
    public static final RegistryEntrySupplier<SoundEvent> ENTITY_DEAD_TREE_DEATH = register("entity.dead_tree.death");
    public static final RegistryEntrySupplier<SoundEvent> ENTITY_FAIRY_AMBIENT = register("entity.fairy.ambient");
    public static final RegistryEntrySupplier<SoundEvent> ENTITY_FLOWER_LILY_STEP = register("entity.flower_lily.step", 2);
    public static final RegistryEntrySupplier<SoundEvent> ENTITY_GENERIC_HEAVY_CHARGE = register("entity.generic.heavy_charge");
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

    public static final ResourceLocation BGM_1 = new ResourceLocation(RuneCraftory.MODID, "bgm/aiwa-konomunede-kuchiteyuku");
    public static final ResourceLocation BGM_2 = new ResourceLocation(RuneCraftory.MODID, "bgm/area-12");
    public static final ResourceLocation BGM_3 = new ResourceLocation(RuneCraftory.MODID, "bgm/catch-them-all");
    public static final ResourceLocation BGM_4 = new ResourceLocation(RuneCraftory.MODID, "bgm/cruising-down-8bit-lane");
    public static final ResourceLocation BGM_5 = new ResourceLocation(RuneCraftory.MODID, "bgm/yami-no-sekai-no-tatakai");
    public static final ResourceLocation BGM_6 = new ResourceLocation(RuneCraftory.MODID, "bgm/yurei");
    public static final ResourceLocation BGM_7 = new ResourceLocation(RuneCraftory.MODID, "bgm/golem_battle");

    public static final RegistryEntrySupplier<SoundEvent> AMBROSIA_FIGHT = registerBgm("bgm.ambrosia_fight", BGM_4);
    public static final RegistryEntrySupplier<SoundEvent> CHIMERA_FIGHT = registerBgm("bgm.chimera_fight", BGM_2);
    public static final RegistryEntrySupplier<SoundEvent> DEAD_TREE_FIGHT = registerBgm("bgm.dead_tree_fight", BGM_2);
    public static final RegistryEntrySupplier<SoundEvent> MARIONETTA_FIGHT = registerBgm("bgm.marionetta_fight", BGM_6);
    public static final RegistryEntrySupplier<SoundEvent> HANDONETTA_FIGHT = registerBgm("bgm.handonetta_fight", BGM_6);
    public static final RegistryEntrySupplier<SoundEvent> RACCOON_FIGHT = registerBgm("bgm.raccoon_fight", BGM_1);
    public static final RegistryEntrySupplier<SoundEvent> SKELEFANG_FIGHT = registerBgm("bgm.skelefang_fight", BGM_5);
    public static final RegistryEntrySupplier<SoundEvent> RAFFLESIA_FIGHT = registerBgm("bgm.rafflesia_fight", BGM_3);
    public static final RegistryEntrySupplier<SoundEvent> THUNDERBOLT_FIGHT = registerBgm("bgm.thunderbolt_fight", BGM_4);
    public static final RegistryEntrySupplier<SoundEvent> GRIMOIRE_FIGHT = registerBgm("bgm.grimoire_fight", BGM_2);
    public static final RegistryEntrySupplier<SoundEvent> SANO_UNO_FIGHT = registerBgm("bgm.sano_uno_fight", BGM_4);
    public static final RegistryEntrySupplier<SoundEvent> SARCOPHAGUS_FIGHT = registerBgm("bgm.sarcophagus_fight", BGM_7);

    private static RegistryEntrySupplier<SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(RuneCraftory.MODID, name)));
    }

    private static RegistryEntrySupplier<SoundEvent> register(String name, int variations) {
        RegistryEntrySupplier<SoundEvent> res = SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(RuneCraftory.MODID, name)));
        if (Platform.INSTANCE.isDatagen())
            VARIATIONS.put(res.getID(), variations);
        return res;
    }

    private static RegistryEntrySupplier<SoundEvent> registerBgm(String name, ResourceLocation bgm) {
        RegistryEntrySupplier<SoundEvent> res = SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(RuneCraftory.MODID, name)));
        // For now not added
        //        if (BGM.stream().noneMatch(h -> h.bgm.equals(bgm))) {
        //            String music = bgm.getPath().replace("bgm/", "");
        //            RegistryEntrySupplier<Item> record = ModItems.ITEMS.register("music_disc_" + music.replace("-", "_"), Platform.INSTANCE.registerRecord(5, res, new Item.Properties()
        //                    .stacksTo(1).rarity(Rarity.RARE)));
        //            ModItems.NOTEX.add(record);
        //            BGM_RECORD.put(bgm, record);
        //        }
        BGM.add(new BGMHolder(res, bgm));
        return res;
    }

    public record BGMHolder(RegistryEntrySupplier<SoundEvent> sound, ResourceLocation bgm) {
    }
}
