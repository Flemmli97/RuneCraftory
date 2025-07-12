package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.TenshiLibCrossPlat;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModSounds {

    public static final LoaderRegister<SoundEvent> SOUND_EVENTS = LoaderRegistryAccess.INSTANCE.of(Registries.SOUND_EVENT, RuneCraftory.MODID);

    public static final Object2IntMap<ResourceLocation> VARIATIONS = new Object2IntArrayMap<>();
    public static final List<BGMHolder> BGM = new ArrayList<>();
    public static final Map<ResourceLocation, RegistryEntrySupplier<Item, ?>> BGM_RECORD = new HashMap<>();

    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_AMBROSIA_WAVE = register("entity.ambrosia.wave");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_CHIMERA_AMBIENT = register("entity.chimera.ambient", 2);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_CHIPSQUEEK_HURT = register("entity.chipsqueek.hurt", 2);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_DEAD_TREE_DEATH = register("entity.dead_tree.death");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_FAIRY_AMBIENT = register("entity.fairy.ambient");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_FLOWER_LILY_STEP = register("entity.flower_lily.step", 2);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_GENERIC_HEAVY_CHARGE = register("entity.generic.heavy_charge");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_GHOST_AMBIENT = register("entity.ghost.ambient", 3);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_GOBLING_AMBIENT = register("entity.goblin.ambient", 3);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_GOBLING_HURT = register("entity.goblin.hurt");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_GOBLIN_DEATH = register("entity.goblin.death");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_ORC_BONK = register("entity.orc.bonk");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_PANTHER_AMBIENT = register("entity.panther.ambient");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_PANTHER_HURT = register("entity.panther.hurt");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_RAFFLESIA_ANGRY = register("entity.rafflesia.angry");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_RAFFLESIA_DEATH = register("entity.rafflesia.death");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_SCORPION_STEP = register("entity.scorpion.step");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_SKELEFANG_ROAR = register("entity.skelefang.roar");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_THUNDERBOLT_NEIGH = register("entity.thunderbolt.neigh");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_WASP_BUZZ = register("entity.wasp.buzz", 3);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_WEAGLE_FLAP = register("entity.weagle.flap");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_WISP_AMBIENT = register("entity.wisp.ambient");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> PLAYER_ATTACK_SWOOSH = register("player.attack.swoosh", 4);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> PLAYER_ATTACK_SWOOSH_HEAVY = register("player.attack.swoosh_heavy");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> PLAYER_ATTACK_SWOOSH_LIGHT = register("player.attack.swoosh_light", 3);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> PLAYER_ARMOR_PIYO_CHIRP = register("player.armor.piyo_chirp", 4);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> SPELL_APPLE_RAIN = register("spell.apple_rain");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> SPELL_GENERIC_BUFF = register("spell.generic.buff");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> SPELL_GENERIC_DARK = register("spell.generic.dark");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> SPELL_GENERIC_ELECTRIC_ZAP = register("spell.generic.electric_zap");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> SPELL_GENERIC_FIRE_BALL = register("spell.generic.fireball");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> SPELL_GENERIC_HEAL = register("spell.generic.heal", 3);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> SPELL_GENERIC_LEAP = register("spell.generic.leap");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> SPELL_GENERIC_LIGHT = register("spell.generic.light");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> SPELL_GENERIC_POOF = register("spell.generic.poof");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> SPELL_GENERIC_POP = register("spell.generic.pop");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> SPELL_GENERIC_ROCKS = register("spell.generic.rocks");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> SPELL_GENERIC_WATER = register("spell.generic.wave");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> SPELL_GENERIC_WATERBUBBLE = register("spell.generic.water_bubble");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> SPELL_GENERIC_WIND = register("spell.generic.wind");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> SPELL_GENERIC_WIND_LONG = register("spell.generic.wind_long");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> SPELL_NAIVE_BLADE = register("spell.naive_blade");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> SPELL_STRAIGHT_PUNCH = register("spell.straight_punch");

    public static final ResourceKey<SoundEvent> BGM1 = bgmKey("kesshi-no-tatakai-ni-idomu-monotachi");
    public static final ResourceKey<SoundEvent> BGM2 = bgmKey("dragon");
    public static final ResourceKey<SoundEvent> BGM3 = bgmKey("battle-determination");
    public static final ResourceKey<SoundEvent> BGM4 = bgmKey("janguru-de-no-tatakai");
    public static final ResourceKey<SoundEvent> BGM5 = bgmKey("escape");
    public static final ResourceKey<SoundEvent> BGM6 = bgmKey("yami-no-sekai-no-tatakai");
    public static final ResourceKey<SoundEvent> BGM7 = bgmKey("tatakai-no-tabiji-o-seiku");
    public static final ResourceKey<SoundEvent> BGM8 = bgmKey("yurei");
    public static final ResourceKey<SoundEvent> BGM9 = bgmKey("gokumon-oni-rasetsu");
    public static final ResourceKey<SoundEvent> BGM10 = bgmKey("golem_battle");

    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> CHIMERA_FIGHT = registerBgm("bgm.chimera_fight", BGM1);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> RAFFLESIA_FIGHT = registerBgm("bgm.rafflesia_fight", BGM2);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> GRIMOIRE_FIGHT = registerBgm("bgm.grimoire_fight", BGM3);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> DEAD_TREE_FIGHT = registerBgm("bgm.dead_tree_fight", BGM4);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> RACCOON_FIGHT = registerBgm("bgm.raccoon_fight", BGM5);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> SKELEFANG_FIGHT = registerBgm("bgm.skelefang_fight", BGM6);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> AMBROSIA_FIGHT = registerBgm("bgm.ambrosia_fight", BGM7);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> THUNDERBOLT_FIGHT = registerBgm("bgm.thunderbolt_fight", BGM7);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> MARIONETTA_FIGHT = registerBgm("bgm.marionetta_fight", BGM8);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> HANDONETTA_FIGHT = registerBgm("bgm.handonetta_fight", BGM8);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> SANO_UNO_FIGHT = registerBgm("bgm.sano_uno_fight", BGM9);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> SARCOPHAGUS_FIGHT = registerBgm("bgm.sarcophagus_fight", BGM10);

    private static RegistryEntrySupplier<SoundEvent, SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(RuneCraftory.modRes(name)));
    }

    private static RegistryEntrySupplier<SoundEvent, SoundEvent> register(String name, int variations) {
        RegistryEntrySupplier<SoundEvent, SoundEvent> res = SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(RuneCraftory.modRes(name)));
        if (TenshiLibCrossPlat.INSTANCE.isDatagen())
            VARIATIONS.put(res.getID(), variations);
        return res;
    }

    private static RegistryEntrySupplier<SoundEvent, SoundEvent> registerBgm(String name, ResourceKey<SoundEvent> bgm) {
        RegistryEntrySupplier<SoundEvent, SoundEvent> res = SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(RuneCraftory.modRes(name)));
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

    private static ResourceKey<SoundEvent> bgmKey(String bgm) {
        return ResourceKey.create(Registries.SOUND_EVENT, RuneCraftory.modRes("bgm/" + bgm));
    }

    public record BGMHolder(RegistryEntrySupplier<SoundEvent, SoundEvent> sound, ResourceKey<SoundEvent> bgm) {

    }
}
