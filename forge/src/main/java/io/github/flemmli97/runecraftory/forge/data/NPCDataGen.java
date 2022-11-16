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
                .addGreeting(new NPCData.Conversation("npc.generic.greeting.1", 0, 10))
                .addGreeting(new NPCData.Conversation("npc.generic.greeting.2", 0, 10))
                .addConversation(new NPCData.Conversation("npc.generic.talk.1", 0, 10))
                .addConversation(new NPCData.Conversation("npc.generic.talk.2", 0, 10))
                .addGiftResponse("dislike", new NPCData.Gift(null, "npc.generic.dislike", -10))
                .addGiftResponse("like", new NPCData.Gift(null, "npc.generic.like", 25))
                .withDefaultConvosOfName("generic").build());

        this.addNPCData("random_npc_2", new NPCData.Builder(50)
                .addGreeting(new NPCData.Conversation("npc.generic.2.greeting.1", 0, 10))
                .addGreeting(new NPCData.Conversation("npc.generic.2.greeting.2", 0, 10))
                .addConversation(new NPCData.Conversation("npc.generic.2.talk.1", 0, 10))
                .addConversation(new NPCData.Conversation("npc.generic.2.talk.2", 0, 10))
                .withDefaultConvosOfName("generic.2").build());
    }

    private static TagKey<Item> giftTag(String tag) {
        return PlatformUtils.INSTANCE.itemTag(new ResourceLocation(RuneCraftory.MODID, "npc/" + tag));
    }

    private static ResourceLocation npcTexture(String texture) {
        return new ResourceLocation(RuneCraftory.MODID, "textures/entity/npc/" + texture + ".png");
    }
}
