package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.NPCData;
import io.github.flemmli97.runecraftory.api.datapack.provider.NPCDataProvider;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.Map;

public class NPCDataGen extends NPCDataProvider {

    public NPCDataGen(DataGenerator gen) {
        super(gen, RuneCraftory.MODID);
    }

    @Override
    protected void add() {
        this.addNPCData("random_npc", new NPCData.Builder(50)
                        .addGiftResponse("dislike", new NPCData.Gift(null, "npc.generic.dislike", -10))
                        .addGiftResponse("like", new NPCData.Gift(null, "npc.generic.like", 25))
                        .setNeutralGiftResponse("npc.generic.gift.default"),
                Map.of(NPCData.ConversationType.GREETING, new NPCData.ConversationSet.Builder("npc.generic.greeting.default")
                                .addConversation(new NPCData.Conversation("npc.generic.greeting.1", 0, 10))
                                .addConversation(new NPCData.Conversation("npc.generic.greeting.2", 0, 10)),
                        NPCData.ConversationType.TALK, new NPCData.ConversationSet.Builder("npc.generic.talk.default")
                                .addConversation(new NPCData.Conversation("npc.generic.talk.1", 0, 10))
                                .addConversation(new NPCData.Conversation("npc.generic.talk.2", 0, 10)),
                        NPCData.ConversationType.FOLLOWYES, new NPCData.ConversationSet.Builder("npc.generic.follow.yes"),
                        NPCData.ConversationType.FOLLOWNO, new NPCData.ConversationSet.Builder("npc.generic.follow.no"),
                        NPCData.ConversationType.FOLLOWSTOP, new NPCData.ConversationSet.Builder("npc.generic.follow.stop")));

        this.addNPCData("random_npc_2", new NPCData.Builder(50)
                        .setNeutralGiftResponse("npc.generic.2.gift.default"),
                Map.of(NPCData.ConversationType.GREETING, new NPCData.ConversationSet.Builder("npc.generic.2.talk.default")
                                .addConversation(new NPCData.Conversation("npc.generic.2.greeting.1", 0, 10))
                                .addConversation(new NPCData.Conversation("npc.generic.2.greeting.2", 0, 10)),
                        NPCData.ConversationType.TALK, new NPCData.ConversationSet.Builder("npc.generic.2.talk.default")
                                .addConversation(new NPCData.Conversation("npc.generic.2.talk.1", 0, 10))
                                .addConversation(new NPCData.Conversation("npc.generic.2.talk.2", 0, 10)),
                        NPCData.ConversationType.FOLLOWYES, new NPCData.ConversationSet.Builder("npc.generic.2.follow.yes"),
                        NPCData.ConversationType.FOLLOWNO, new NPCData.ConversationSet.Builder("npc.generic.2.follow.no"),
                        NPCData.ConversationType.FOLLOWSTOP, new NPCData.ConversationSet.Builder("npc.generic.2.follow.stop")));

        //Test data using all possible fields
        /*this.addNPCDataAll("random_npc_all_test", new NPCData.Builder(50, "Name", "Surname", NPCData.Gender.UNDEFINED)
                        .setNeutralGiftResponse("npc.generic.2.gift.default")
                        .withLook(new ResourceLocation(RuneCraftory.MODID, "test_look"))
                        .withBirthday(com.mojang.datafixers.util.Pair.of(EnumSeason.SPRING, 1))
                        .setUnique(1)
                        .withProfession(ModNPCJobs.GENERAL.getSecond())
                        .addGiftResponse("gift", new NPCData.Gift(ModTags.SEEDS, "gift.response.test", 1))
                        .withSchedule(new NPCSchedule.Schedule(0, 1, 1, 1, 1, 1, 1, 1, EnumSet.noneOf(EnumDay.class)))
                        .setBaseStat(Attributes.MAX_HEALTH, 1)
                        .setStatIncrease(Attributes.MAX_HEALTH, 1)
                        .setBaseLevel(5),
                Map.of(NPCData.ConversationType.GREETING, new NPCData.ConversationSet.Builder("npc.generic.2.talk.default")
                                .addConversation(new NPCData.Conversation("npc.generic.2.greeting.1", 0, 10))
                                .addConversation(new NPCData.Conversation("npc.generic.2.greeting.2", 0, 10)),
                        NPCData.ConversationType.TALK, new NPCData.ConversationSet.Builder("npc.generic.2.talk.default")
                                .addConversation(new NPCData.Conversation("npc.generic.2.talk.1", 0, 10))
                                .addConversation(new NPCData.Conversation("npc.generic.2.talk.2", 0, 10)),
                        NPCData.ConversationType.FOLLOWYES, new NPCData.ConversationSet.Builder("npc.generic.2.follow.yes"),
                        NPCData.ConversationType.FOLLOWNO, new NPCData.ConversationSet.Builder("npc.generic.2.follow.no"),
                        NPCData.ConversationType.FOLLOWSTOP, new NPCData.ConversationSet.Builder("npc.generic.2.follow.stop")),
                new NPCData.NPCLook(NPCData.Gender.UNDEFINED, new ResourceLocation(RuneCraftory.MODID, "texture"), "Flemmli97", 0, List.of()));*/
    }

    private static TagKey<Item> giftTag(String tag) {
        return PlatformUtils.INSTANCE.itemTag(new ResourceLocation(RuneCraftory.MODID, "npc/" + tag));
    }

    private static ResourceLocation npcTexture(String texture) {
        return new ResourceLocation(RuneCraftory.MODID, "textures/entity/npc/" + texture + ".png");
    }
}
