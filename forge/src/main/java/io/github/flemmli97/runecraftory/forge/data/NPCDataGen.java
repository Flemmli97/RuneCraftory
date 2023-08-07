package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.NPCData;
import io.github.flemmli97.runecraftory.api.datapack.provider.NPCDataProvider;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.predicates.TimeCheck;
import net.minecraft.world.level.storage.loot.predicates.WeatherCheck;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class NPCDataGen extends NPCDataProvider {

    public NPCDataGen(DataGenerator gen) {
        super(gen, RuneCraftory.MODID);
    }

    @Override
    protected void add() {
        this.addNPCData("random_npc", new NPCData.Builder(50)
                        .addGiftResponse("dislike", new NPCData.Gift(null, "npc.generic.dislike", -10), "Eh... thanks I guess?")
                        .addGiftResponse("like", new NPCData.Gift(null, "npc.generic.like", 25), "Thanks %s for the gift. I really like this.")
                        .setNeutralGiftResponse("npc.generic.gift.default", "Thank you for your gift"),
                of(m -> {
                    m.put(NPCData.ConversationType.GREETING, new NPCData.ConversationSet.Builder("npc.generic.greeting.default", "Hello %s.")
                            .addConversation(new NPCData.Conversation("npc.generic.greeting.1", 0, 10, TimeCheck.time(IntRange.range(0, 12000)).build()), "Good day %s.")
                            .addConversation(new NPCData.Conversation("npc.generic.greeting.2", 0, 10, TimeCheck.time(IntRange.range(12000, 14000)).build()), "Good evening %s.")
                            .addConversation(new NPCData.Conversation("npc.generic.greeting.3", 0, 10), "Hi. How are you today %s?"));
                    m.put(NPCData.ConversationType.TALK, new NPCData.ConversationSet.Builder("npc.generic.talk.default", "...")
                            .addConversation(new NPCData.Conversation("npc.generic.talk.1", 0, 10), "On sunny days I like to go out and walk a lot.")
                            .addConversation(new NPCData.Conversation("npc.generic.talk.2", 0, 10), "I don't like working."));
                    m.put(NPCData.ConversationType.FOLLOWYES, new NPCData.ConversationSet.Builder("npc.generic.follow.yes", "Ok"));
                    m.put(NPCData.ConversationType.FOLLOWNO, new NPCData.ConversationSet.Builder("npc.generic.follow.no", "Sorry but I am busy right now."));
                    m.put(NPCData.ConversationType.FOLLOWSTOP, new NPCData.ConversationSet.Builder("npc.generic.follow.stop", "Ok. See you again."));
                }));

        this.addNPCData("random_npc_2", new NPCData.Builder(50)
                        .setNeutralGiftResponse("npc.generic.2.gift.default", "Thanks. I appreciate it"),
                of(m -> {
                    m.put(NPCData.ConversationType.GREETING, new NPCData.ConversationSet.Builder("npc.generic.2.greeting.default", "Hello %s.")
                            .addConversation(new NPCData.Conversation("npc.generic.2.greeting.1", 0, 10), "Howdy %s")
                            .addConversation(new NPCData.Conversation("npc.generic.2.greeting.2", 0, 10), "Hi. Whats up %s?"));
                    m.put(NPCData.ConversationType.TALK, new NPCData.ConversationSet.Builder("npc.generic.2.talk.default", "...")
                            .addConversation(new NPCData.Conversation("npc.generic.2.talk.1", 0, 10), "Did you know that upgrading a weapon with scrap metal + makes it do 1 damage?")
                            .addConversation(new NPCData.Conversation("npc.generic.2.talk.2", 0, 10), "Those villagers seem strange. Why do they have no hands?"));
                    m.put(NPCData.ConversationType.FOLLOWYES, new NPCData.ConversationSet.Builder("npc.generic.2.follow.yes", "Where are we going?"));
                    m.put(NPCData.ConversationType.FOLLOWNO, new NPCData.ConversationSet.Builder("npc.generic.2.follow.no", "Sorry but I'm have something to take care of."));
                    m.put(NPCData.ConversationType.FOLLOWSTOP, new NPCData.ConversationSet.Builder("npc.generic.2.follow.stop", "Ok. Cya."));
                }));

        this.addNPCData("random_npc_3", new NPCData.Builder(50)
                        .addGiftResponse("dislike", new NPCData.Gift(null, "npc.generic.3.dislike", -10), "What is this... Sorry but im not a fan of this")
                        .addGiftResponse("like", new NPCData.Gift(null, "npc.generic.3.like", 25), "Wow %s. Thanks for the gift. This is a nice present!")
                        .setNeutralGiftResponse("npc.generic.3.gift.default", "Thank you for your gift"),
                of(m -> {
                    m.put(NPCData.ConversationType.GREETING, new NPCData.ConversationSet.Builder("npc.generic.3.greeting.default", "Hi %s. How are you doing today?")
                            .addConversation(new NPCData.Conversation("npc.generic.3.greeting.1", 0, 10), "Nice to see you"));
                    m.put(NPCData.ConversationType.TALK, new NPCData.ConversationSet.Builder("npc.generic.3.talk.default", "...")
                            .addConversation(new NPCData.Conversation("npc.generic.3.talk.2", 0, 10), "A forge will allow you to craft your own weapons.")
                            .addConversation(new NPCData.Conversation("npc.generic.3.talk.1", 0, 10, WeatherCheck.weather().setRaining(true).build()), "I don't like the rain.")
                            .addConversation(new NPCData.Conversation("npc.generic.3.talk.2", 0, 10), "I've heard that there are places in this world where very strong monster appear."));
                    m.put(NPCData.ConversationType.FOLLOWYES, new NPCData.ConversationSet.Builder("npc.generic.3.follow.yes", "Sure. Where do you want to go?"));
                    m.put(NPCData.ConversationType.FOLLOWNO, new NPCData.ConversationSet.Builder("npc.generic.3.follow.no", "I think I still have some things to do first."));
                    m.put(NPCData.ConversationType.FOLLOWSTOP, new NPCData.ConversationSet.Builder("npc.generic.3.follow.stop", "Oh ok. Well then later."));
                }));

        //Test data using all possible fields
        /*this.addNPCDataAll("random_npc_all_test", new NPCData.Builder(50, "Name", "Surname", NPCData.Gender.UNDEFINED)
                        .setNeutralGiftResponse("npc.generic.2.gift.default", "Test")
                        .withLook(new ResourceLocation(RuneCraftory.MODID, "test_look"))
                        .withBirthday(com.mojang.datafixers.util.Pair.of(EnumSeason.SPRING, 1))
                        .setUnique(1)
                        .withProfession(ModNPCJobs.GENERAL.getSecond())
                        .addGiftResponse("gift", new NPCData.Gift(ModTags.SEEDS, "gift.response.test", 1), "Test")
                        .withSchedule(new NPCSchedule.Schedule(0, 1, 1, 1, 1, 1, 1, 1, EnumSet.noneOf(EnumDay.class)))
                        .setBaseStat(Attributes.MAX_HEALTH, 1)
                        .setStatIncrease(Attributes.MAX_HEALTH, 1)
                        .setBaseLevel(5),
                Map.of(NPCData.ConversationType.GREETING, new NPCData.ConversationSet.Builder("npc.generic.2.talk.default", "Test")
                                .addConversation(new NPCData.Conversation("npc.generic.2.greeting.1", 0, 10), "Test")
                                .addConversation(new NPCData.Conversation("npc.generic.2.greeting.2", 0, 10), "Test"),
                        NPCData.ConversationType.TALK, new NPCData.ConversationSet.Builder("npc.generic.2.talk.default", "Test")
                                .addConversation(new NPCData.Conversation("npc.generic.2.talk.1", 0, 10), "Test")
                                .addConversation(new NPCData.Conversation("npc.generic.2.talk.2", 0, 10), "Test"),
                        NPCData.ConversationType.FOLLOWYES, new NPCData.ConversationSet.Builder("npc.generic.2.follow.yes", "Test"),
                        NPCData.ConversationType.FOLLOWNO, new NPCData.ConversationSet.Builder("npc.generic.2.follow.no", "Test"),
                        NPCData.ConversationType.FOLLOWSTOP, new NPCData.ConversationSet.Builder("npc.generic.2.follow.stop", "Test")),
                new NPCData.NPCLook(NPCData.Gender.UNDEFINED, new ResourceLocation(RuneCraftory.MODID, "texture"), "Flemmli97", 0, List.of()));*/
    }

    //For consistent order
    private static <K, V> Map<K, V> of(Consumer<Map<K, V>> cons) {
        Map<K, V> map = new LinkedHashMap<>();
        cons.accept(map);
        return map;
    }

    private static TagKey<Item> giftTag(String tag) {
        return PlatformUtils.INSTANCE.itemTag(new ResourceLocation(RuneCraftory.MODID, "npc/" + tag));
    }

    private static ResourceLocation npcTexture(String texture) {
        return new ResourceLocation(RuneCraftory.MODID, "textures/entity/npc/" + texture + ".png");
    }
}
