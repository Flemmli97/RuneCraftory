package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.NPCData;
import io.github.flemmli97.runecraftory.api.datapack.provider.NPCDataProvider;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class NPCDataGen extends NPCDataProvider {

    public NPCDataGen(DataGenerator gen) {
        super(gen, RuneCraftory.MODID);
    }

    @Override
    protected void add() {
        this.addNPCData("random_npc", new NPCData.Builder(50)
                .addInteraction(NPCData.ConversationType.GREETING, new NPCData.ConversationSet.Builder("npc.generic.greeting.default")
                        .addConversation(new NPCData.Conversation("npc.generic.greeting.1", 0, 10))
                        .addConversation(new NPCData.Conversation("npc.generic.greeting.2", 0, 10)))
                .addInteraction(NPCData.ConversationType.TALK, new NPCData.ConversationSet.Builder("npc.generic.talk.default")
                        .addConversation(new NPCData.Conversation("npc.generic.talk.1", 0, 10))
                        .addConversation(new NPCData.Conversation("npc.generic.talk.2", 0, 10)))
                .addInteraction(NPCData.ConversationType.FOLLOWYES, new NPCData.ConversationSet.Builder("npc.generic.follow.yes"))
                .addInteraction(NPCData.ConversationType.FOLLOWNO, new NPCData.ConversationSet.Builder("npc.generic.follow.no"))
                .addInteraction(NPCData.ConversationType.FOLLOWSTOP, new NPCData.ConversationSet.Builder("npc.generic.follow.stop"))
                .addGiftResponse("dislike", new NPCData.Gift(null, "npc.generic.dislike", -10))
                .addGiftResponse("like", new NPCData.Gift(null, "npc.generic.like", 25))
                .setNeutralGiftResponse("npc.generic.gift.default").build());

        this.addNPCData("random_npc_2", new NPCData.Builder(50)
                .addInteraction(NPCData.ConversationType.GREETING, new NPCData.ConversationSet.Builder("npc.generic.2.talk.default")
                        .addConversation(new NPCData.Conversation("npc.generic.2.greeting.1", 0, 10))
                        .addConversation(new NPCData.Conversation("npc.generic.2.greeting.2", 0, 10)))
                .addInteraction(NPCData.ConversationType.TALK, new NPCData.ConversationSet.Builder("npc.generic.2.talk.default")
                        .addConversation(new NPCData.Conversation("npc.generic.2.talk.1", 0, 10))
                        .addConversation(new NPCData.Conversation("npc.generic.2.talk.2", 0, 10)))
                .addInteraction(NPCData.ConversationType.FOLLOWYES, new NPCData.ConversationSet.Builder("npc.generic.2.follow.yes"))
                .addInteraction(NPCData.ConversationType.FOLLOWNO, new NPCData.ConversationSet.Builder("npc.generic.2.follow.no"))
                .addInteraction(NPCData.ConversationType.FOLLOWSTOP, new NPCData.ConversationSet.Builder("npc.generic.2.follow.stop"))
                .setNeutralGiftResponse("npc.generic.2.gift.default").build());
    }

    private static TagKey<Item> giftTag(String tag) {
        return PlatformUtils.INSTANCE.itemTag(new ResourceLocation(RuneCraftory.MODID, "npc/" + tag));
    }

    private static ResourceLocation npcTexture(String texture) {
        return new ResourceLocation(RuneCraftory.MODID, "textures/entity/npc/" + texture + ".png");
    }
}
