package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.TenshiLibCrossPlat;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuneCraftorySounds {

    public static final LoaderRegister<SoundEvent> SOUND_EVENTS = LoaderRegistryAccess.INSTANCE.of(Registries.SOUND_EVENT, RuneCraftory.MODID);

    public static final Map<ResourceLocation, SoundHolder> SOUND_DATA = new HashMap<>();
    public static final List<BGMHolder> BGM = new ArrayList<>();

    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_AMBROSIA_ANGRY = register("entity.ambrosia.angry", null, ResourceLocation.withDefaultNamespace("mob/enderdragon/growl"), 2, 1.5f);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_AMBROSIA_SPAWN = register("entity.ambrosia.spawn");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_AMBROSIA_WAVE = register("entity.ambrosia.wave", "Ambrosia humming");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_BUFFAMOO_DEATH = register("entity.buffamoo.death", null, ResourceLocation.withDefaultNamespace("mob/cow/hurt"), 3, 0.8f);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_BUFFAMOO_HURT = register("entity.buffamoo.hurt", null, ResourceLocation.withDefaultNamespace("mob/cow/hurt"), 3, 0.8f);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_BUFFAMOO_STEP = register("entity.buffamoo.step", null, ResourceLocation.withDefaultNamespace("mob/cow/step"), 4, 0.2f);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_CHIMERA_AMBIENT = register("entity.chimera.ambient", "Chimera growl", 2);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_CHIMERA_ROAR = register("entity.chimera.roar", null, ResourceLocation.withDefaultNamespace("mob/enderdragon/growl"), 2, 1.5f);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_CHIPSQUEEK_HURT = register("entity.chipsqueek.hurt", "Chipsqueek squeak", 2);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_CLUCKADOODLE_DEATH = register("entity.cluckadoodle.death", null, ResourceLocation.withDefaultNamespace("mob/chicken/hurt"), 2, 0.8f);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_CLUCKADOODLE_HURT = register("entity.cluckadoodle.hurt", null, ResourceLocation.withDefaultNamespace("mob/chicken/hurt"), 2, 0.8f);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_DEAD_TREE_DEATH = register("entity.dead_tree.death", "Dead tree dies");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_DEAD_TREE_HURT = register("entity.dead_tree.hurt", null, ResourceLocation.withDefaultNamespace("mob/zombie/wood"), 4);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_DEAD_TREE_ROAR = register("entity.dead_tree.roar", null, ResourceLocation.withDefaultNamespace("mob/enderdragon/growl"), 2, 1.5f);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_FAIRY_AMBIENT = register("entity.fairy.ambient", "Fairy shimmer");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_FLOWER_LILY_STEP = register("entity.flower_lily.step", "", 2);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_GENERIC_HEAVY_CHARGE = register("entity.generic.heavy_charge", "Entity charge");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_GHOST_AMBIENT = register("entity.ghost.ambient", null, 3);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_GOBLING_AMBIENT = register("entity.goblin.ambient", null, 3);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_GOBLING_HURT = register("entity.goblin.hurt", null);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_GOBLIN_DEATH = register("entity.goblin.death", null);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_GRIMOIRE_ROAR = register("entity.grimoire.roar", null, ResourceLocation.withDefaultNamespace("mob/enderdragon/growl"), 2, 1.5f);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_HANDONETTA_ROAR = register("entity.handonetta.roar", null, ResourceLocation.withDefaultNamespace("mob/enderdragon/growl"), 2, 1.5f);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_HORNET_BUZZ = register("entity.hornet.buzz", 3);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_HORNET_DEATH = register("entity.hornet.death", null, ResourceLocation.withDefaultNamespace("mob/bee/death"), 2, 0.7f);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_HORNET_HURT = register("entity.hornet.hurt", null, ResourceLocation.withDefaultNamespace("mob/bee/hurt"), 3, 0.8f);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_MARIONETTA_ROAR = register("entity.marionetta.roar", null, ResourceLocation.withDefaultNamespace("mob/enderdragon/growl"), 2, 1.5f);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_ORC_AMBIENT = register("entity.orc.ambient", "Orc grunts", ResourceLocation.withDefaultNamespace("mob/piglin_brute/idle"), 7);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_ORC_BONK = register("entity.orc.bonk", "Maze bonk");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_ORC_DEATH = register("entity.orc.death", null, ResourceLocation.withDefaultNamespace("mob/piglin_brute/death"), 3);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_ORC_HURT = register("entity.orc.hurt", null, ResourceLocation.withDefaultNamespace("mob/piglin_brute/hurt"), 4);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_PANTHER_AMBIENT = register("entity.panther.ambient", null);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_PANTHER_HURT = register("entity.panther.hurt", null);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_RACCOON_ROAR = register("entity.raccoon.roar", null, ResourceLocation.withDefaultNamespace("mob/enderdragon/growl"), 2, 1.5f);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_RAFFLESIA_ANGRY = register("entity.rafflesia.angry", "Rafflesia hiss");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_RAFFLESIA_DEATH = register("entity.rafflesia.death", null);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_SARCOPHAGUS_ROAR = register("entity.sarcophagus.roar", null, ResourceLocation.withDefaultNamespace("mob/enderdragon/growl"), 2, 1.5f);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_SCORPION_DEATH = register("entity.scorpion.death", null, ResourceLocation.withDefaultNamespace("mob/spider/death"), 1, 1.5f);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_SCORPION_HURT = register("entity.scorpion.hurt", null, ResourceLocation.withDefaultNamespace("mob/spider/say"), 4, 1.5f);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_SCORPION_STEP = register("entity.scorpion.step", "Scorpion rustle");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_SKELEFANG_ROAR = register("entity.skelefang.roar", null);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_SKELEFANG_SWEEP = register("entity.skelefang.sweep", null, ResourceLocation.withDefaultNamespace("entity/player/attack/sweep"), 7, 0.7f);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_THUNDERBOLT_AMBIENT = register("entity.thunderbolt.ambient", "Thunderbolt neighs", ResourceLocation.withDefaultNamespace("mob/horse/idle"), 3, 0.8f);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_THUNDERBOLT_GALLOP = register("entity.thunderbolt.gallop", null, ResourceLocation.withDefaultNamespace("mob/horse/gallop"), 4);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_THUNDERBOLT_HURT = register("entity.thunderbolt.hurt", null, ResourceLocation.withDefaultNamespace("mob/horse/hit"), 4, 0.8f);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_THUNDERBOLT_NEIGH = register("entity.thunderbolt.neigh", null);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_WEAGLE_FLAP = register("entity.weagle.flap");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_WISP_AMBIENT = register("entity.wisp.ambient");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_BOSS_DEFEAT = register("entity.boss.defeat", null, ResourceLocation.withDefaultNamespace("mob/wither/death"));

    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_ATTACK_BLOCKED = register("entity.generic.attack_blocked", "Attack blocked", ResourceLocation.withDefaultNamespace("random/anvil_land"));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_BIG_PLATE_LAND = register("entity.big_plate.land", null, ResourceLocation.withDefaultNamespace("random/anvil_land"));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_EARTH_SPIKE_ATTACK = register("entity.earth_spike.attack", null, ResourceLocation.withDefaultNamespace("random/eat"), 3);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_ELEMENTAL_TRAIL_EARTH = register("entity.elemental_trail.earth.ambient", null, SoundEvents.ROOTED_DIRT_BREAK.getLocation(), 4);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_ELEMENTAL_TRAIL_WATER = register("entity.elemental_trail.water.ambient", null, ResourceLocation.withDefaultNamespace("random/glass"), 3);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_ENERGY_ORB_AMBIENT = register("entity.energy_orb.ambient", "Energy Orb", ResourceLocation.withDefaultNamespace("mob/guardian/attack_loop"));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_FIREWALL_SPAWN = register("entity.fire_wall.spawn", null, ResourceLocation.withDefaultNamespace("mob/ghast/fireball4"));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> ENTITY_RUNEY_COLLECT = register("entity.runey.collect", null, ResourceLocation.withDefaultNamespace("random/glass"), 3);

    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> PLAYER_ARMOR_PIYO_CHIRP = register("player.armor.piyo_chirp", "Piyo Sandal chirp", 4);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> PLAYER_ATTACK_SWOOSH = register("player.attack.swoosh", 4);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> PLAYER_ATTACK_SWOOSH_HEAVY = register("player.attack.swoosh_heavy");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> PLAYER_ATTACK_SWOOSH_LIGHT = register("player.attack.swoosh_light", 3);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> PLAYER_BRUSH = register("player.brush", "Player brushing", ResourceLocation.withDefaultNamespace("mob/horse/leather"));

    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> SPELL_APPLE_RAIN = register("spell.apple_rain");
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> SPELL_GENERIC_BUFF = register("spell.generic.buff", "Spell buff");
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

    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> GENERIC_SUCCESS = register("generic.success", "Action success", ResourceLocation.withDefaultNamespace("mob/villager/yes"), 3);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> GENERIC_DENY = register("generic.deny", "Action denied", ResourceLocation.withDefaultNamespace("mob/villager/no"), 3);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> GENERIC_TELEPORT = register("generic.teleport", "Entity teleports", ResourceLocation.withDefaultNamespace("mob/endermen/portal"));
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> GENERIC_FARM_LAND_WATER = register("generic.farmland.water", "Watering Farmland", SoundEvents.BOAT_PADDLE_WATER.getLocation(), 8);
    public static final RegistryEntrySupplier<SoundEvent, SoundEvent> GENERIC_ITEM_STAT_CONSUME = register("generic.item_stat.consume", "Drinking", SoundEvents.BREWING_STAND_BREW.getLocation(), 2);

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
        return register(name, null, 1);
    }

    private static RegistryEntrySupplier<SoundEvent, SoundEvent> register(String name, String translation) {
        return register(name, translation, 1);
    }

    private static RegistryEntrySupplier<SoundEvent, SoundEvent> register(String name, int variations) {
        return register(name, null, variations);
    }

    private static RegistryEntrySupplier<SoundEvent, SoundEvent> register(String name, String translation, int variations) {
        RegistryEntrySupplier<SoundEvent, SoundEvent> res = SOUND_EVENTS.register(name, SoundEvent::createVariableRangeEvent);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen()) {
            SOUND_DATA.put(res.getID(), new SoundHolder(res.getID(), variations, 1, translation));
        }
        return res;
    }

    private static RegistryEntrySupplier<SoundEvent, SoundEvent> register(String name, String translation, ResourceLocation location) {
        return register(name, translation, location, 1);
    }

    private static RegistryEntrySupplier<SoundEvent, SoundEvent> register(String name, String translation, ResourceLocation location, int amount) {
        return register(name, translation, location, amount, 1);
    }

    private static RegistryEntrySupplier<SoundEvent, SoundEvent> register(String name, String translation, ResourceLocation location, int amount, float pitch) {
        RegistryEntrySupplier<SoundEvent, SoundEvent> res = SOUND_EVENTS.register(name, SoundEvent::createVariableRangeEvent);
        if (TenshiLibCrossPlat.INSTANCE.isDatagen()) {
            SOUND_DATA.put(res.getID(), new SoundHolder(location, amount, pitch, translation));
        }
        return res;
    }

    private static RegistryEntrySupplier<SoundEvent, SoundEvent> registerBgm(String name, ResourceKey<SoundEvent> bgm) {
        RegistryEntrySupplier<SoundEvent, SoundEvent> res = SOUND_EVENTS.register(name, SoundEvent::createVariableRangeEvent);
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

    public record SoundHolder(ResourceLocation location, int amount, float pitch, @Nullable String defaultTranslation) {
    }
}
