package io.github.flemmli97.runecraftory.api.datapack;

import com.google.common.collect.ImmutableList;
import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public record ConversationContext(ResourceLocation key) {

    private static final Map<ResourceLocation, ConversationContext> CONTEXTS = new HashMap<>();

    public static final ConversationContext GREETING = register("greeting");
    public static final ConversationContext TALK = register("talk");
    public static final ConversationContext FOLLOW_YES = register("follow_yes");
    public static final ConversationContext FOLLOW_NO = register("follow_no");
    public static final ConversationContext FOLLOW_STOP = register("follow_stop");
    public static final ConversationContext ENGAGEMENT_ACCEPT = register("engagement_accept");
    public static final ConversationContext ENGAGEMENT_DENY = register("engagement_deny");
    public static final ConversationContext MARRIAGE_ACCEPT = register("marriage_accept");
    public static final ConversationContext MARRIAGE_DENY = register("marriage_deny");

    private static ConversationContext register(String key) {
        return register(new ResourceLocation(RuneCraftory.MODID, key));
    }

    public static ConversationContext register(ResourceLocation res) {
        ConversationContext convCtx = new ConversationContext(res);
        CONTEXTS.put(res, convCtx);
        return convCtx;
    }

    public static ConversationContext get(ResourceLocation id) {
        return CONTEXTS.get(id);
    }

    public static Collection<ConversationContext> getRegistered() {
        return ImmutableList.copyOf(CONTEXTS.values());
    }


    public static Collection<ConversationContext> dataGenVerify() {
        return ImmutableList.of(GREETING, TALK, FOLLOW_STOP, FOLLOW_NO, FOLLOW_YES);
    }

    @Override
    public String toString() {
        return String.format("Converation Context: %s", this.key);
    }
}
