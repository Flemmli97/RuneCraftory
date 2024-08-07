package io.github.flemmli97.runecraftory.forge.data;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.ConversationContext;
import io.github.flemmli97.runecraftory.api.datapack.NPCData;
import io.github.flemmli97.runecraftory.api.datapack.provider.NPCDataProvider;
import io.github.flemmli97.runecraftory.api.registry.NPCFeature;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureHolder;
import io.github.flemmli97.runecraftory.api.registry.NPCFeatureType;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.AttackMeleeAction;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.NPCAttackActions;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.PartyTargetAction;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.RunAwayAction;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.SpellAttackAction;
import io.github.flemmli97.runecraftory.common.entities.npc.features.BlushFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.ColorSetting;
import io.github.flemmli97.runecraftory.common.entities.npc.features.HairFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.IndexRange;
import io.github.flemmli97.runecraftory.common.entities.npc.features.IndexedColorSettingType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.OutfitFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.SimpleHatFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.SizeFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.SlimLookFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.TypedIndexRange;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.ModNPCJobs;
import io.github.flemmli97.runecraftory.common.registry.ModNPCLooks;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.predicates.TimeCheck;
import net.minecraft.world.level.storage.loot.predicates.WeatherCheck;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class NPCDataGen extends NPCDataProvider {

    public NPCDataGen(DataGenerator gen) {
        super(gen, RuneCraftory.MODID);
    }

    @Override
    protected void add() {
        ResourceLocation genericAttack = this.addAttackActions(new ResourceLocation(RuneCraftory.MODID, "generic_melee_attack"), new NPCAttackActions.Builder()
                .addAction(new NPCAttackActions.ActionBuilder(1)
                        .action(new AttackMeleeAction(UniformGenerator.between(30, 80)))));

        ResourceLocation meleeAndFireball = this.addAttackActions(new ResourceLocation(RuneCraftory.MODID, "melee_fireball_attack"), new NPCAttackActions.Builder()
                .addAction(new NPCAttackActions.ActionBuilder(5)
                        .action(new AttackMeleeAction(UniformGenerator.between(30, 80))))
                .addAction(new NPCAttackActions.ActionBuilder(1)
                        .action(new RunAwayAction(UniformGenerator.between(25, 40), 7))
                        .action(new SpellAttackAction(ModSpells.FIREBALL.get(), 8, false, UniformGenerator.between(20, 40), UniformGenerator.between(10, 20)))));
        ResourceLocation meleeAndHeal = this.addAttackActions(new ResourceLocation(RuneCraftory.MODID, "melee_fireball_attack"), new NPCAttackActions.Builder()
                .addAction(new NPCAttackActions.ActionBuilder(5)
                        .action(new AttackMeleeAction(UniformGenerator.between(30, 80))))
                .addAction(new NPCAttackActions.ActionBuilder(1)
                        .action(new RunAwayAction(UniformGenerator.between(25, 40), 7))
                        .action(new PartyTargetAction(ModSpells.CURE_ALL.get(), false, UniformGenerator.between(20, 40)))));
        ResourceLocation meleeAndWater = this.addAttackActions(new ResourceLocation(RuneCraftory.MODID, "melee_fireball_attack"), new NPCAttackActions.Builder()
                .addAction(new NPCAttackActions.ActionBuilder(5)
                        .action(new AttackMeleeAction(UniformGenerator.between(30, 80))))
                .addAction(new NPCAttackActions.ActionBuilder(1)
                        .action(new RunAwayAction(UniformGenerator.between(25, 40), 7))
                        .action(new SpellAttackAction(ModSpells.WATER_LASER.get(), 8, false, UniformGenerator.between(20, 40), UniformGenerator.between(10, 20)))));

        this.addLook(new ResourceLocation(RuneCraftory.MODID, "generic/male_1"), new NPCData.NPCLook(NPCData.Gender.MALE,
                null, 50, defaultNPCFeatures(false, m -> m.put(ModNPCLooks.OUTFIT.get(), new OutfitFeatureType(new TypedIndexRange(List.of(WeightedEntry.wrap(Pair.of("generic", new IndexRange.FirstNIndices(3)), 1))))))));
        this.addLook(new ResourceLocation(RuneCraftory.MODID, "generic/female_1"), new NPCData.NPCLook(NPCData.Gender.FEMALE,
                null, 50, defaultNPCFeatures(true, m -> m.put(ModNPCLooks.OUTFIT.get(), new OutfitFeatureType(new TypedIndexRange(List.of(WeightedEntry.wrap(Pair.of("generic", new IndexRange.FirstNIndices(3)), 1))))))));

        this.addNPCData("random_npc_1", new NPCData.Builder(50)
                        .addGiftResponse("hate", new NPCData.Gift(RunecraftoryTags.GENERIC_TRASH, "npc.generic.1.hate", -15), "What... why would you give me this?")
                        .addGiftResponse("dislike", new NPCData.Gift(null, "npc.generic.1.dislike", -10), "Uh, thanks... but I’m not really into this.")
                        .addGiftResponse("like", new NPCData.Gift(null, "npc.generic.1.like", 20), "Thanks %player% for the gift. I really like this.")
                        .setNeutralGiftResponse("npc.generic.1.gift.default", "Not bad, thanks. I’ll find a use for it.")
                        .withCombatActions(genericAttack, meleeAndHeal, meleeAndHeal),
                of(m -> {
                    m.put(ConversationContext.FIRST_TALK, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.first.1"), "Hello there! Haven't seen you around before. Are you new here?")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.first.2"), "Greetings, adventurer. What brings you to this place?")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.first.3"), "Ah, a new face! Welcome, stranger."));
                    m.put(ConversationContext.GREETING, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.greeting.1").addCondition(TimeCheck.time(IntRange.range(0, 12000)).build()), "Good day %player%. What can I do for you today?")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.greeting.2").addCondition(TimeCheck.time(IntRange.range(12000, 14000)).build()), "Good evening %player%. How was your day?")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.greeting.3"), "Hi. How are you today %player%?")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.greeting.4"), "Hello %player%, always a pleasure to see you."));
                    m.put(ConversationContext.TALK, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.talk.1").addCondition(WeatherCheck.weather().setRaining(false).build())
                                    .addAction(new NPCData.ConversationActionHolder("npc.generic.1.talk.1.answer.same", NPCData.ConversationAction.ANSWER, "npc.generic.1.talk.1.response.same", 0), "Same")
                                    .addAction(new NPCData.ConversationActionHolder("npc.generic.1.talk.1.answer.ok", NPCData.ConversationAction.ANSWER, "npc.generic.1.talk.1.response.ok", 0), "Ok..."), "On sunny days I like to go out and walk a lot.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.talk.1.response.same").setAnswer(), "It's nice right?")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.talk.1.response.ok").setAnswer(), "You don't?")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.talk.2"), "I’ve been hearing some strange tales from the travelers passing through. Stories of a distant land where the sky is always dark and tall people appear there. Wonder what's that about.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.talk.rain.1").addCondition(WeatherCheck.weather().setRaining(true).build()), "Rainy days like this always make me reflective. What’s on your mind?")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.talk.rain.2").addCondition(WeatherCheck.weather().setRaining(true).build()), "Quite the downpour, isn’t it? What can I do for you while we wait it out?"));
                    m.put(ConversationContext.FOLLOW_YES, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.follow.yes.1"), "Lead the way, I'm right behind you.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.follow.yes.2"), "Ready when you are. Let's move.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.follow.yes.3"), "You can count on me. Let's go!"));
                    m.put(ConversationContext.FOLLOW_NO, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.follow.no.1"), "I'm afraid I can't right now. Perhaps another time?")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.follow.no.2"), "Now's not a good time. Maybe later?")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.follow.no.3"), "Sorry but I am busy right now."));
                    m.put(ConversationContext.FOLLOW_STOP, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.follow.stop.1"), "Got it. See you then.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.follow.stop.2"), "Ok. See you again."));
                    m.put(ConversationContext.DATING_ACCEPT, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.dating.yes.1"), "I would love that. Let's enjoy our time together.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.dating.yes.2"), "Yes, I think this could be the start of something special.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.dating.yes.3"), "I... didn't think that day would come but yes!"));
                    m.put(ConversationContext.DATING_DENY, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.dating.no.1"), "I don't think that's the best idea. Let's keep things as they are.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.dating.no.2"), "I appreciate the offer, but I don't feel that way.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.dating.no.3"), "Sorry but i don't feel that way"));
                    m.put(ConversationContext.MARRIAGE_ACCEPT, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.marry.yes.1"), "This is the happiest day of my life! Of course, I'll marry you.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.marry.yes.2"), "Yes, I can't imagine spending my life with anyone else.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.marry.yes.3"), "Yes of course! You can't imagine how happy I am!"));
                    m.put(ConversationContext.MARRIAGE_DENY, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.marry.no.1"), "Wait, what? Marriage? I didn’t think we were even at that stage...")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.marry.no.2"), "Uh, did I miss something? We were talking about something else, right?")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.marry.no.3"), "I'm sorry, but I don’t see us taking that step together."));
                    m.put(ConversationContext.DIVORCE, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.divorce.success.1"), "You’re really doing this? After everything we’ve been through?")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.divorce.success.2"), "I can’t believe you! Just like that, you want to throw it all away?")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.divorce.success.3"), "After everything... I see... Goodbye then..."));
                    m.put(ConversationContext.DIVORCE_ERROR, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.divorce.fail.1"), "Are you feeling alright? We've never been married.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.divorce.fail.2"), "I think you’re a bit confused. We’re not married, remember?")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.divorce.fail.3"), "Huh? Why are you giving me that?"));
                    m.put(ConversationContext.PROCREATION_COOLDOWN, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.procreation.fail.1"), "I understand, but I’m not sure I want that right now.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.procreation.fail.2"), "It's a big decision, and I’m not comfortable with it at this time.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.1.procreation.fail.3"), "I don't feel like we're in the right place for that yet."));
                }),
                of(m -> {
                    m.put(QuestGen.TAMING, new QuestResponseBuilder(
                            new NPCData.ConversationSet.Builder("npc.generic.quest.tame_monster.start", """
                                    Did you know that you can tame the monsters in this world?
                                    You would need to setup a barn first and then just give them an item.

                                    With that said I would like you to tame a monster."""),
                            new NPCData.ConversationSet.Builder("npc.generic.quest.tame_monster.active", "You still need to tame a monster.\n" +
                                    "Some monsters prefer certain items more."),
                            new NPCData.ConversationSet.Builder("npc.generic.quest.tame_monster.end", "I see you've successfully tamed a monster. Congrats!")
                    ));
                }));
        this.addNPCData("random_npc_2", new NPCData.Builder(50)
                        .addGiftResponse("hate", new NPCData.Gift(RunecraftoryTags.GENERIC_TRASH, "npc.generic.2.hate", -15), "Oh... I’m sorry, but I really don’t like this.")
                        .addGiftResponse("dislike", new NPCData.Gift(null, "npc.generic.2.dislike", -10), "I... I appreciate it, but I’m not sure what to do with this.")
                        .addGiftResponse("like", new NPCData.Gift(null, "npc.generic.2.like", 20), "Oh, this is... really nice. Thank you %player%.")
                        .setNeutralGiftResponse("npc.generic.2.gift.default", "This is okay... Thanks for thinking of me.")
                        .withCombatActions(genericAttack, meleeAndHeal, meleeAndHeal),
                of(m -> {
                    m.put(ConversationContext.FIRST_TALK, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.first.1"), "Um, welcome. I'm ... %npc%. I hope you’ll like it here.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.first.2"), "Hi... I’m not used to seeing new faces around here. I'm %npc%... nice to meet you.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.first.3"), "Oh, um, hello... I didn’t expect to meet someone new today..."));
                    m.put(ConversationContext.GREETING, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.greeting.1"), "Hi %player%... Um, did you need something??")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.greeting.2"), "Oh, it’s you... I’m glad you’re back.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.greeting.3"), "Oh, hi %player% again... It’s nice to see you..."));
                    m.put(ConversationContext.TALK, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.talk.1"), "Life here is... quiet. I like it, but sometimes I wish for more.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.talk.2"), "I... um, I saw some bunnies the other day. They were... very cute")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.talk.3"), "Um, I was thinking... Do you ever wonder what’s out there beyond the village?"));
                    m.put(ConversationContext.FOLLOW_YES, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.follow.yes.1"), "Alright... I’ll be right behind you.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.follow.yes.2"), "Sure... I’ll go with you. Just... stay close, okay?"));
                    m.put(ConversationContext.FOLLOW_NO, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.follow.no.1"), "I appreciate the offer, but... I should stay here for now.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.follow.no.2"), "I’m sorry... I can’t, not right now."));
                    m.put(ConversationContext.FOLLOW_STOP, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.follow.stop.1"), "Ok... see you then. Be careful, okay?")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.follow.stop.2"), "Alright... I’ll stay here until you need me."));
                    m.put(ConversationContext.DATING_ACCEPT, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.dating.yes.1"), "I... I never thought you’d ask. Yes, let’s give it a try.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.dating.yes.2"), "Oh... I didn’t expect that. But, yes, I’d love to.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.dating.yes.3"), "R-really? Um, yes, I’d like that..."));
                    m.put(ConversationContext.DATING_DENY, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.dating.no.1"), "I’m not sure I can do that... I’m sorry.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.dating.no.2"), "Oh... I’m sorry, but I can’t. I hope you’re not upset.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.dating.no.3"), "I’m... I’m really sorry, but I don’t think I’m ready for that."));
                    m.put(ConversationContext.MARRIAGE_ACCEPT, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.marry.yes.1"), "Oh my... Yes, of course! I’ve dreamed of this...")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.marry.yes.2"), "I... I can’t believe you asked. Yes, I’ll be with you forever.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.marry.yes.3"), "Y-yes... I can’t believe this is happening. I’d love to marry you."));
                    m.put(ConversationContext.MARRIAGE_DENY, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.marry.no.1"), "Oh... I wish I could, but I don’t think I can.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.marry.no.2"), "This is hard to say, but... no, I’m not ready for that.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.marry.no.3"), "I’m sorry... I can’t. I’m not ready for something so big."));
                    m.put(ConversationContext.DIVORCE, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.divorce.success.1"), "This is really hard, but... I’ll let you go if that’s what you need.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.divorce.success.2"), "It’s... it’s over then. I’ll always remember what we had.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.divorce.success.3"), "Oh... I didn’t think this would happen. I guess... this is goodbye."));
                    m.put(ConversationContext.DIVORCE_ERROR, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.divorce.fail.1"), "I... I’m not sure what you mean. We were never together.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.divorce.fail.2"), "I think there’s a misunderstanding... We were never married.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.divorce.fail.3"), "Wait... What? We were never married..."));
                    m.put(ConversationContext.PROCREATION_COOLDOWN, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.procreation.fail.1"), "I... I don’t think having a child is right for me, not now.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.procreation.fail.2"), "I’ve thought about it, but... I don't think now is the right time.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.procreation.fail.3"), "I... I’m not ready for that kind of responsibility. I’m sorry."));
                }),
                Map.of());
        this.addNPCData("random_npc_3", new NPCData.Builder(50)
                        .addGiftResponse("hate", new NPCData.Gift(RunecraftoryTags.GENERIC_TRASH, "npc.generic.3.hate", -15), "Uhh... what should I do with this?")
                        .addGiftResponse("dislike", new NPCData.Gift(null, "npc.generic.3.dislike", -10), "Sorry... but this isn't really my thing.")
                        .addGiftResponse("like", new NPCData.Gift(null, "npc.generic.3.like", 20), "Oh, nice... this is something I can appreciate.")
                        .setNeutralGiftResponse("npc.generic.3.gift.default", "Oh... thanks... it’s... alright.")
                        .withCombatActions(genericAttack, meleeAndHeal, meleeAndHeal),
                of(m -> {
                    m.put(ConversationContext.FIRST_TALK, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.first.1"), "Zzzz... huh? Oh a new face! I... wasn't sleeping no. Anyway Im' %npc%. Nice to meet you")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.first.2"), "Oh welcome. I'm %npc%. Nice to ... yawn... meet you")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.first.3"), "Yawn... oh. Haven't seen you around here before. Hello... I'm  %npc%."));
                    m.put(ConversationContext.GREETING, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.greeting.1"), "Zzzzz... ! Oh hi. How are you today %player%?")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.greeting.2"), "Hey there %player%... I wish the day was over already?"));
                    m.put(ConversationContext.TALK, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.talk.1"), "Those sheep look so fluffy... I bet you can fall asleep instantly if you lie on them")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.talk.2"), "Some people have so much energy during the day. I wonder where they get all of it from...")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.talk.3")
                                    .addAction(new NPCData.ConversationActionHolder("npc.generic.3.talk.3.answer.nice", NPCData.ConversationAction.ANSWER, "npc.generic.3.talk.3.response.nice", 0), "Sounds great!")
                                    .addAction(new NPCData.ConversationActionHolder("npc.generic.3.talk.3.answer.uhh", NPCData.ConversationAction.ANSWER, "npc.generic.3.talk.3.response.uhh", 0), "..."), "If I bring a pillow with me i could nap everywhere I want. What do you think?")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.talk.3.response.nice"), "Yeah I know right?")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.talk.3.response.uhh"), "Come on... You don't think so?"));
                    m.put(ConversationContext.FOLLOW_YES, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.follow.yes.1"), "Mmm... alright, I’ll follow... just don’t expect me to keep up if we’re running.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.follow.yes.2"), "I’m in... as long as we can stop for naps along the way.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.follow.yes.3"), "Sure. But can we take it slowly?"));
                    m.put(ConversationContext.FOLLOW_NO, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.follow.no.1"), "I would, but... this spot is so comfy, I might just stay put.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.follow.no.2"), "Sorry, but my bed is calling... maybe another time?")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.follow.no.3"), "Mmm... maybe later... I think I’ll just rest here for now."));
                    m.put(ConversationContext.FOLLOW_STOP, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.follow.stop.1"), "Oh... stopping already? I was just getting used to the pace...")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.follow.stop.2"), "Mmm... okay, I’ll stay here... don’t worry, I’ll find a good spot to nap."));
                    m.put(ConversationContext.DATING_ACCEPT, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.dating.yes.1"), "Yeah... let’s see where this goes... maybe we can nap together sometime.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.dating.yes.2"), "I’d like that... as long as we can keep it low-key and relaxed.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.dating.yes.3"), "Mmm... dating sounds nice... as long as our dates are cozy."));
                    m.put(ConversationContext.DATING_DENY, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.dating.no.1"), "Dating sounds... like a lot of work... and I’m just so sleepy.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.dating.no.2"), "Mmm... I’m flattered, but... I don’t think I’m up for that kind of energy.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.dating.no.3"), "Thanks, but... I’m not sure I can keep up with all that."));
                    m.put(ConversationContext.MARRIAGE_ACCEPT, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.marry.yes.1"), "Marriage? Mmm... yeah, I think I’d like that... as long as we keep it chill.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.marry.yes.2"), "Sure... as long as we can take naps together... I’m in.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.marry.yes.3"), "Marriage sounds nice... as long as we can keep things laid-back."));
                    m.put(ConversationContext.MARRIAGE_DENY, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.marry.no.1"), "Marriage is... a bit too much for me right now.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.marry.no.2"), "I appreciate it, but... I’m not really ready for that step.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.marry.no.3"), "Mmm... I don’t think marriage is in the cards for me... too much commitment."));
                    m.put(ConversationContext.DIVORCE, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.divorce.success.1"), "Mmm... so that’s it? I guess I shouldn’t have expected more...")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.divorce.success.2"), "What? Divorce? Mmm... that’s... really frustrating, you know.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.divorce.success.3"), "Divorce? Just like that? That’s... really disappointing."));
                    m.put(ConversationContext.DIVORCE_ERROR, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.divorce.fail.1"), "Mmm... divorce? Wait, did I sleep through the wedding?")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.divorce.fail.2"), "I think you’re confused... we’re not even married, remember?")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.divorce.fail.3"), "You want to divorce me? But... I’m pretty sure we’re not married."));
                    m.put(ConversationContext.PROCREATION_COOLDOWN, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.procreation.fail.1"), "A kid? I’m too tired just thinking about it...")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.procreation.fail.2"), "I’m sorry, but... I don’t think I can handle that right now.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.procreation.fail.3"), "Mmm... a child? I don’t think I have the energy for that right now."));
                }),
                Map.of());

        ResourceLocation shopMale = this.addLook(new ResourceLocation(RuneCraftory.MODID, "shop_owner/male_1"), new NPCData.NPCLook(NPCData.Gender.MALE, null, 0, defaultNPCFeatures(false, m -> m.put(ModNPCLooks.OUTFIT.get(), new OutfitFeatureType(new TypedIndexRange(List.of(WeightedEntry.wrap(Pair.of("shop", new IndexRange.FirstNIndices(3)), 1))))))));
        ResourceLocation shopFemale = this.addLook(new ResourceLocation(RuneCraftory.MODID, "shop_owner/female_1"), new NPCData.NPCLook(NPCData.Gender.FEMALE, null, 0, defaultNPCFeatures(true, m -> m.put(ModNPCLooks.OUTFIT.get(), new OutfitFeatureType(new TypedIndexRange(List.of(WeightedEntry.wrap(Pair.of("shop", new IndexRange.FirstNIndices(3)), 1))))))));

        this.addNPCData("shop_owner/1", new NPCData.Builder(50)
                        .withLook(new NPCData.NPCLookId(shopMale, NPCData.Gender.MALE), new NPCData.NPCLookId(shopFemale, NPCData.Gender.FEMALE))
                        .withProfession(ModNPCJobs.GENERAL.getSecond(), ModNPCJobs.FLOWER.getSecond())
                        .addGiftResponse("hate", new NPCData.Gift(RunecraftoryTags.GENERIC_TRASH, "npc.shop_owner.1.hate", -15), "Uhh... what should I do with this?")
                        .addGiftResponse("dislike", new NPCData.Gift(null, "npc.shop_owner.1.dislike", -10), "Sorry... but this isn't really my thing.")
                        .addGiftResponse("like", new NPCData.Gift(null, "npc.shop_owner.1.like", 20), "Oh, nice... this is something I can appreciate.")
                        .setNeutralGiftResponse("npc.shop_owner.1.gift.default", "Oh... thanks... it’s... alright.")
                        .withCombatActions(genericAttack),
                of(m -> {
                    m.put(ConversationContext.FIRST_TALK, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.1.first.1"), "Ah, someone new! Let me know if you need any help finding something. I manage a general store so you should find something useful there. I'm %npc% by the way.")
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.1.first.2"), "Hello there, traveler! You seem to be new here. Whats your name? ... %player% I see. I'm %npc% and I have a general store where i sell various goods there.")
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.1.first.3"), "Ah, a new face in town! Welcome. I'm %npc% and I manage a general store. You should come by if you have some time"));
                    m.put(ConversationContext.GREETING, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.1.greeting.1").addCondition(TimeCheck.time(IntRange.range(6000, 12000)).build()), "Goood morning %player%! How are you today")
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.1.greeting.2"), "Good day to you %player%! How are you today"));
                    m.put(ConversationContext.TALK, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.1.talk.1"), "Running this store keeps me busy, but I wouldn’t trade it for anything. Seeing so many people needing this store makes me happy.")
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.1.talk.2"), "")
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.1.talk.3"), ""));
                    m.put(ConversationContext.FOLLOW_YES, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.1.follow.yes.1"), "Sure. Where are we going?"));
                    m.put(ConversationContext.FOLLOW_NO, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.1.follow.no.1"), "Ah sorry but this is not possible right now."));
                    m.put(ConversationContext.FOLLOW_STOP, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.1.follow.stop.1"), "Got it. See ya then!"));
                    m.put(ConversationContext.DATING_ACCEPT, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.1.dating.yes.1"), "I didn’t see that coming, but yes, I’d love to.")
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.1.dating.yes.2"), "I’ve been hoping you’d ask. Yes, I’d love to."));
                    m.put(ConversationContext.DATING_DENY, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.1.dating.no.1"), "Sorry. But I don't see you that way.")
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.1.dating.no.2"), "I appreciate your feelings, but I don’t think it’s a good idea."));
                    m.put(ConversationContext.MARRIAGE_ACCEPT, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.1.marry.yes.1"), "This is the happiest moment of my life. Yes, I’ll marry you!")
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.1.marry.yes.2"), "This is everything I hoped for. Yes, let’s get married!"));
                    m.put(ConversationContext.MARRIAGE_DENY, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.1.marry.no.1"), "I’m sorry, but I’m not ready for that kind of commitment.")
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.1.marry.no.2"), "I care about you deeply, but I’m not ready for marriage yet."));
                    m.put(ConversationContext.DIVORCE, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.1.divorce.success.1"), "You’re just going to walk away like that? After everything?")
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.1.divorce.success.2"), "I can’t believe this. You’re just going to throw it all away?"));
                    m.put(ConversationContext.DIVORCE_ERROR, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.1.divorce.fail.1"), "Wait, what? We’re not even married!")
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.1.divorce.fail.2"), "You can’t divorce someone you were never married to, you know."));
                    m.put(ConversationContext.PROCREATION_COOLDOWN, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.1.procreation.fail.1"), "Now is not the time for that. Sorry.")
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.1.procreation.fail.2"), "I think we shoul wait a bit more before that step."));
                }),
                of(m -> {
                    m.put(QuestGen.SHIP_TURNIP, new QuestResponseBuilder(
                            new NPCData.ConversationSet.Builder("npc.shop_owner.quest.ship_turnip.start", """
                                    Are you here for my request?
                                    I will show you how to ship items to make money: Shipping items is very simple. First you need a shipping bin. It can hold any shippable items in it.
                                    Put the items you want to ship in it and everyday in the morning your items will be automatically shipped. Lets try it out now: I want you to ship a turnip."""),
                            new NPCData.ConversationSet.Builder("npc.shop_owner.quest.ship_turnip.active", "Please ship a turnip."),
                            new NPCData.ConversationSet.Builder("npc.shop_owner.quest.ship_turnip.end", "Great. There are a lot of items you can ship to make money. Here take" +
                                    "these turnip seeds. It should come in handy.")
                    ));
                }));

        ResourceLocation smithMale = this.addLook(new ResourceLocation(RuneCraftory.MODID, "smith/male_1"), new NPCData.NPCLook(NPCData.Gender.MALE, null, 0, defaultNPCFeatures(false, m -> m.put(ModNPCLooks.OUTFIT.get(), new OutfitFeatureType(new TypedIndexRange(List.of(WeightedEntry.wrap(Pair.of("smith", new IndexRange.FirstNIndices(3)), 1))))))));
        ResourceLocation smithFemale = this.addLook(new ResourceLocation(RuneCraftory.MODID, "smith/female_1"), new NPCData.NPCLook(NPCData.Gender.FEMALE, null, 0, defaultNPCFeatures(true, m -> m.put(ModNPCLooks.OUTFIT.get(), new OutfitFeatureType(new TypedIndexRange(List.of(WeightedEntry.wrap(Pair.of("smith", new IndexRange.FirstNIndices(3)), 1))))))));

        this.addNPCData("smith/1", new NPCData.Builder(50)
                        .withLook(new NPCData.NPCLookId(smithMale, NPCData.Gender.MALE), new NPCData.NPCLookId(smithFemale, NPCData.Gender.FEMALE))
                        .withProfession(ModNPCJobs.GENERAL.getSecond(), ModNPCJobs.SMITH.getSecond())
                        .addGiftResponse("dislike", new NPCData.Gift(RunecraftoryTags.SMITH_TRASH, "npc.smith.2.dislike", -10), "Hey! I'm not your trashcan!")
                        .addGiftResponse("like", new NPCData.Gift(RunecraftoryTags.MINERALS, "npc.smith.2.like", 25), "Wow thanks! I can make something great using this")
                        .setNeutralGiftResponse("npc.smith.2.gift.default", "Thanks. Did you know ores are one of my favorite things?")
                        .withCombatActions(meleeAndFireball),
                of(m -> {
                    m.put(ConversationContext.FIRST_TALK, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.smith.1.first.1"), "Are you new here? I see. My name is %npc% and operate a forge here. Swing by if you want to have a good weapon."));
                    m.put(ConversationContext.GREETING, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.smith.1.greeting.1"), "Hello %player%. Let's do our best again today!"));
                    m.put(ConversationContext.TALK, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.smith.1.talk.1"), "Nothing beats the clanging sound of a working forge.")
                            .addConversation(new NPCData.Conversation.Builder("npc.smith.1.talk.2"), "There are multiple different weapon types. You should try them all out to find the one that suits you the best.")
                            .addConversation(new NPCData.Conversation.Builder("npc.smith.1.talk.3"), "Working as a blacksmith is hard but very fun.")
                            .addConversation(new NPCData.Conversation.Builder("npc.smith.1.talk.4"), "Someday I would like to make some kind of legendary weapon."));
                    m.put(ConversationContext.FOLLOW_YES, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.smith.1.follow.yes.1"), "Ok. Let's go then!"));
                    m.put(ConversationContext.FOLLOW_NO, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.smith.1.follow.no.1"), "Hmm sorry but the forge calls for me."));
                    m.put(ConversationContext.FOLLOW_STOP, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.smith.1.follow.stop.1"), "Okay, bye then. Let me know if you need me again."));
                    m.put(ConversationContext.DATING_ACCEPT, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.smith.1.dating.yes.1"), "You know what? Yes, I’d like to get to know you better.")
                            .addConversation(new NPCData.Conversation.Builder("npc.smith.1.dating.yes.2"), "I think we’d have a great time. Yes, I’m in."));
                    m.put(ConversationContext.DATING_DENY, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.smith.1.dating.no.1"), "Thank you for asking, but I’m not looking for that kind of relationship.")
                            .addConversation(new NPCData.Conversation.Builder("npc.smith.1.dating.no.2"), "I’m sorry, but I don’t think I’m ready for a relationship."));
                    m.put(ConversationContext.MARRIAGE_ACCEPT, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.smith.1.marry.yes.1"), "Yes! Let us walk the path of the forge together.")
                            .addConversation(new NPCData.Conversation.Builder("npc.smith.1.marry.yes.2"), "I never imagined something like this would happen, but yes, I’ll marry you."));
                    m.put(ConversationContext.MARRIAGE_DENY, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.smith.1.marry.no.1"), "This is difficult for me, but I have to say no.")
                            .addConversation(new NPCData.Conversation.Builder("npc.smith.1.marry.no.2"), "I’ve thought about it, but I don’t think I can say yes for now."));
                    m.put(ConversationContext.DIVORCE, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.smith.1.divorce.success.1"), "Fine. If this is what you want, just don’t expect me to forget.")
                            .addConversation(new NPCData.Conversation.Builder("npc.smith.1.divorce.success.2"), "If this is what you want, so be it. But don’t think I’ll forgive easily."));
                    m.put(ConversationContext.DIVORCE_ERROR, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.smith.1.divorce.fail.1"), "Huh what is this? I don't think i have any use for this?")
                            .addConversation(new NPCData.Conversation.Builder("npc.smith.1.divorce.fail.2"), "Uhh are you ok? We are not married or anything you know..."));
                    m.put(ConversationContext.PROCREATION_COOLDOWN, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.smith.1.procreation.fail.1"), "This is a huge step, and i feel like now is not the right time for that"));
                }),
                of(m -> {
                    m.put(QuestGen.MINING, new QuestResponseBuilder(
                            new NPCData.ConversationSet.Builder("npc.smith.quest.mining.start", """
                                    You saw my request? Great!
                                    You might have noticed various strange stones around the world. Those are minerals and they provide various different ores.
                                    I want you to mine 10 of them for me."""),
                            new NPCData.ConversationSet.Builder("npc.smith.quest.mining.active", "To mine minerals you need atleast an iron pickaxe or a hammer. " +
                                    "I want you to mine 10 mineral blocks for me."),
                            new NPCData.ConversationSet.Builder("npc.smith.quest.mining.end", "Nice! Mining ores increases your mining level. " +
                                    "With higher level you can get better ores from minerals. Here take this hammer, it should make mining minerals a bit easier.")
                    ));
                }));

        ResourceLocation doctorMale = this.addLook(new ResourceLocation(RuneCraftory.MODID, "doctor/male_1"), new NPCData.NPCLook(NPCData.Gender.MALE, null, 0, defaultNPCFeatures(false, m -> m.put(ModNPCLooks.OUTFIT.get(), new OutfitFeatureType(new TypedIndexRange(List.of(WeightedEntry.wrap(Pair.of("doctor", new IndexRange.FirstNIndices(1)), 1))))))));
        ResourceLocation doctorFemale = this.addLook(new ResourceLocation(RuneCraftory.MODID, "doctor/female_1"), new NPCData.NPCLook(NPCData.Gender.FEMALE, null, 0, defaultNPCFeatures(true, m -> m.put(ModNPCLooks.OUTFIT.get(), new OutfitFeatureType(new TypedIndexRange(List.of(WeightedEntry.wrap(Pair.of("doctor", new IndexRange.FirstNIndices(1)), 1))))))));

        this.addNPCData("doctor/1", new NPCData.Builder(50)
                        .withLook(new NPCData.NPCLookId(doctorMale, NPCData.Gender.MALE), new NPCData.NPCLookId(doctorFemale, NPCData.Gender.FEMALE))
                        .withProfession(ModNPCJobs.DOCTOR.getSecond())
                        .addGiftResponse("dislike", new NPCData.Gift(null, "npc.doctor.2.dislike", -10), "I can't use this...")
                        .addGiftResponse("like", new NPCData.Gift(null, "npc.doctor.2.like", 25), "Thanks! I really like this!")
                        .setNeutralGiftResponse("npc.doctor.2.gift.default", "Oh? this might be useful. Thanks")
                        .withCombatActions(meleeAndFireball),
                of(m -> {
                    m.put(ConversationContext.FIRST_TALK, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.doctor.1.first.1"), "Hello! Who are you?\n...\nI see. I'm %npc% and I run a clinic here. If you ever need help feel free to drop by there."));
                    m.put(ConversationContext.GREETING, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.doctor.1.greeting.1"), "Good day %player%. How are you today?"));
                    m.put(ConversationContext.TALK, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.doctor.1.talk.1"), "Medicine isn’t just a job to me; it’s a passion. Every patient has a story, and sometimes, understanding that story is the key to their treatment.")
                            .addConversation(new NPCData.Conversation.Builder("npc.doctor.1.talk.2"), "Whenever you have a status condition you let me check that out. They can be quite detrimental. Though i heard a good night's rest also helps with that.")
                            .addConversation(new NPCData.Conversation.Builder("npc.doctor.1.talk.3"), "Various herbs and grass are very useful in medicine")
                            .addConversation(new NPCData.Conversation.Builder("npc.doctor.1.talk.4"), "Being a doctor lots of people come to me for a variety of problems. Helping them with it is my duty."));
                    m.put(ConversationContext.FOLLOW_YES, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.doctor.1.follow.yes.1"), "Sure. But don't overexert yourself!"));
                    m.put(ConversationContext.FOLLOW_NO, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.doctor.1.follow.no.1"), "Ah sorry but I'm quite busy right now."));
                    m.put(ConversationContext.FOLLOW_STOP, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.doctor.1.follow.stop.1"), "Ok. Take care of yourself!"));
                    m.put(ConversationContext.DATING_ACCEPT, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.doctor.1.dating.yes.1"), "That sounds wonderful. I’d love to.")
                            .addConversation(new NPCData.Conversation.Builder("npc.doctor.1.dating.yes.2"), "I’d like that. Let’s see where this goes."));
                    m.put(ConversationContext.DATING_DENY, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.doctor.1.dating.no.1"), "I’m flattered, but I don't see us in that way."));
                    m.put(ConversationContext.MARRIAGE_ACCEPT, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.doctor.1.marry.yes.1"), "Wow. I would've never expected that I would walk down this path. Yes lets do it!"));
                    m.put(ConversationContext.MARRIAGE_DENY, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.doctor.1.marry.no.1"), "That’s a big step, and I don’t think I’m ready to take it."));
                    m.put(ConversationContext.DIVORCE, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.doctor.1.divorce.success.1"), "I... was never expecting this. Especially from you..."));
                    m.put(ConversationContext.DIVORCE_ERROR, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.doctor.1.divorce.fail.1"), "Hmm... why are you giving me this?"));
                    m.put(ConversationContext.PROCREATION_COOLDOWN, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.doctor.1.procreation.fail.1"), "We should take this more slowly and not rush things."));
                }),
                Map.of());

        ResourceLocation cookMale = this.addLook(new ResourceLocation(RuneCraftory.MODID, "cook/male_1"), new NPCData.NPCLook(NPCData.Gender.MALE, null, 0, defaultNPCFeatures(false, m -> {
            m.put(ModNPCLooks.HAT.get(), new SimpleHatFeatureType(List.of("chef_hat")));
            m.put(ModNPCLooks.OUTFIT.get(), new OutfitFeatureType(new TypedIndexRange(List.of(WeightedEntry.wrap(Pair.of("doctor", new IndexRange.FirstNIndices(1)), 1)))));
        })));
        ResourceLocation cookFemale = this.addLook(new ResourceLocation(RuneCraftory.MODID, "cook/female_1"), new NPCData.NPCLook(NPCData.Gender.FEMALE, null, 0, defaultNPCFeatures(true, m -> {
            m.put(ModNPCLooks.HAT.get(), new SimpleHatFeatureType(List.of("chef_hat")));
            m.put(ModNPCLooks.OUTFIT.get(), new OutfitFeatureType(new TypedIndexRange(List.of(WeightedEntry.wrap(Pair.of("doctor", new IndexRange.FirstNIndices(1)), 1)))));
        })));

        this.addNPCData("cook/1", new NPCData.Builder(50)
                        .withLook(new NPCData.NPCLookId(cookMale, NPCData.Gender.MALE), new NPCData.NPCLookId(cookFemale, NPCData.Gender.FEMALE))
                        .withProfession(ModNPCJobs.COOK.getSecond())
                        .addGiftResponse("dislike", new NPCData.Gift(null, "npc.cook.2.dislike", -10), "This can't even be used in my dishes...")
                        .addGiftResponse("like", new NPCData.Gift(null, "npc.cook.2.like", 25), "Wow thanks! I really like this!")
                        .setNeutralGiftResponse("npc.cook.2.gift.default", "It’s nice of you to think of me. I’ll find a use for this.")
                        .withCombatActions(meleeAndFireball),
                of(m -> {
                    m.put(ConversationContext.FIRST_TALK, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.cook.1.first.1"), "I don’t think we’ve met. I’m %npc%, the chef around here. Making sure folks are well-fed is what I do.")
                            .addConversation(new NPCData.Conversation.Builder("npc.cook.1.first.2"), "Ah, a new face! I’m the cook here. If you are hungry you should swing by. I run a restaurant here serving various delicous dishes."));
                    m.put(ConversationContext.GREETING, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.cook.1.greeting.1"), "Good to see you %player%! What can I cook up for you today?"));
                    m.put(ConversationContext.TALK, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.cook.1.talk.1"), "You know, people say cooking is just a job, but for me, it’s a passion. There's something magical about turning simple ingredients into something that brings folks together.")
                            .addConversation(new NPCData.Conversation.Builder("npc.cook.1.talk.2"), "You should try out cooking too. Sometimes a homecooked meal can beat any other dishes. And who knows? Maybe it will awaken the culinary flame in you too.")
                            .addConversation(new NPCData.Conversation.Builder("npc.cook.1.talk.3"), "Seeing people eat the food I cook gives me an indescribable feeling of satisfaction."));
                    m.put(ConversationContext.FOLLOW_YES, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.cook.1.follow.yes.1"), "Sure. Where are we going?"));
                    m.put(ConversationContext.FOLLOW_NO, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.cook.1.follow.no.1"), "Sorry but I have a lot on my plate right now."));
                    m.put(ConversationContext.FOLLOW_STOP, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.cook.1.follow.stop.1"), "Stopping here? Alright, I’ll head back to the kitchen then."));
                    m.put(ConversationContext.DATING_ACCEPT, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.cook.1.dating.yes.1"), "You know, I’ve always had a soft spot for someone who appreciates good food. Let’s give it a shot!"));
                    m.put(ConversationContext.DATING_DENY, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.cook.1.dating.no.1"), "I’m honored you’d ask, but my passion for cooking leaves little room for romance."));
                    m.put(ConversationContext.MARRIAGE_ACCEPT, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.cook.1.marry.yes.1"), "Marriage? Wow... I never thought I’d find someone who loves my cooking and me! Yes, let’s get married.")
                            .addConversation(new NPCData.Conversation.Builder("npc.cook.1.marry.yes.2"), "This is the sweetest proposal I’ve ever had—yes, I’ll marry you, and I promise to keep your plate full of love."));
                    m.put(ConversationContext.MARRIAGE_DENY, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.cook.1.marry.no.1"), "You’re wonderful, but I’m not ready to commit to marriage yet. Let’s just keep enjoying each other’s company—and my cooking.")
                            .addConversation(new NPCData.Conversation.Builder("npc.cook.1.marry.no.2"), "Marriage is a big step, and I’m not sure I’m ready to share my kitchen—or my life—that way."));
                    m.put(ConversationContext.DIVORCE, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.cook.1.divorce.success.1"), "You want to divorce me? After everything we’ve shared—meals, memories, and more? I can’t believe this!")
                            .addConversation(new NPCData.Conversation.Builder("npc.cook.1.divorce.success.2"), "So you’ve had your fill and now you’re done with me? I never thought it would come to this."));
                    m.put(ConversationContext.DIVORCE_ERROR, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.cook.1.divorce.fail.1"), "Divorce? You must have eaten something that’s gone bad—we’re not even married!")
                            .addConversation(new NPCData.Conversation.Builder("npc.cook.1.divorce.fail.2"), "Wait, divorce? But we never got married! Maybe you need to spend more time in the kitchen clearing your head."));
                    m.put(ConversationContext.PROCREATION_COOLDOWN, new NPCData.ConversationSet.Builder()
                            .addConversation(new NPCData.Conversation.Builder("npc.cook.1.procreation.fail.1"), "I’ve thought about it, but I don’t think kids are in the cards for me right now."));
                }),
                Map.of());

        //Test data using all possible fields
        /*ResourceLocation attackAll = this.addAttackActions(new ResourceLocation(RuneCraftory.MODID, "attack_all"), new NPCAttackActions.Builder()
                .addAction(new NPCAttackActions.ActionBuilder(1)
                        .action(new AttackMeleeAction(ConstantValue.exactly(40), ConstantValue.exactly(0)))
                        .action(new RunToLeadAction(ConstantValue.exactly(40), ConstantValue.exactly(0))))
                .addAction(new NPCAttackActions.ActionBuilder(1)
                        .action(new FoodThrowAction(List.of(new ItemStack(Items.APPLE)), ConstantValue.exactly(40), ConstantValue.exactly(0))))
                .addAction(new NPCAttackActions.ActionBuilder(1)
                        .action(new RunAwayAction(ConstantValue.exactly(40), ConstantValue.exactly(0), 6)))
                .addAction(new NPCAttackActions.ActionBuilder(1)
                        .action(new SpellAttackAction(ModSpells.FIREBALL.get(), 8, false, ConstantValue.exactly(40), ConstantValue.exactly(0))))
                .addAction(new NPCAttackActions.ActionBuilder(1)
                        .action(new PartyTargetAction(ModSpells.CURE_ALL.get(), false, ConstantValue.exactly(0)))));
        this.addNPCDataAll("random_npc_all_test", new NPCData.Builder(50, "Name", "Surname", NPCData.Gender.UNDEFINED)
                        .setNeutralGiftResponse("npc.test.all.gift.default", "Test")
                        .withBirthday(com.mojang.datafixers.util.Pair.of(EnumSeason.SPRING, 1))
                        .setUnique(1)
                        .withProfession(ModNPCJobs.GENERAL.getSecond())
                        .addGiftResponse("gift", new NPCData.Gift(RunecraftoryTags.SEEDS, "gift.response.test", 1), "Test")
                        .withSchedule(new NPCSchedule.Schedule(0, 1, 1, 1, 1, 1, 1, 1, EnumSet.noneOf(EnumDay.class)))
                        .setBaseStat(Attributes.MAX_HEALTH, 1)
                        .setStatIncrease(Attributes.MAX_HEALTH, 1)
                        .setBaseLevel(5).withCombatActions(attackAll),
                of(m -> {
                    for (ConversationContext ctx : ConversationContext.getRegistered()) {
                        m.put(ctx, new NPCData.ConversationSet.Builder("npc.test.all" + ctx.key(), "Test"));
                    }
                    m.put(ConversationContext.GREETING, new NPCData.ConversationSet.Builder("npc.test.all.talk.default", "Test")
                            .addConversation(new NPCData.Conversation.Builder("npc.test.all.greeting.1"), "Test")
                            .addConversation(new NPCData.Conversation.Builder("npc.test.all.greeting.2"), "Test"));
                    m.put(ConversationContext.TALK, new NPCData.ConversationSet.Builder("npc.test.talk.default", "Test")
                            .addConversation(new NPCData.Conversation.Builder("npc.test.all.talk.1"), "Test")
                            .addConversation(new NPCData.Conversation.Builder("npc.test.all.talk.2"), "Test"));
                }),
                Pair.of(new ResourceLocation(RuneCraftory.MODID, "test_look"), new NPCData.NPCLook(NPCData.Gender.UNDEFINED, "Flemmli97", 0, Map.of())), Map.of());*/
    }

    private static <T extends NPCFeature> Map<NPCFeatureType<?>, NPCFeatureHolder<?>> defaultNPCFeatures(boolean female, Consumer<FeatureBuilderHelper> cons) {
        Map<NPCFeatureType<?>, NPCFeatureHolder<?>> map = new LinkedHashMap<>();
        // Just for generic sanity check
        FeatureBuilderHelper builder = map::put;
        builder.put(ModNPCLooks.SKIN.get(), new IndexedColorSettingType(List.of(0, 1, 2), ColorSetting.SKIN_COLOR_RANGE, ModNPCLooks.SKIN));
        builder.put(ModNPCLooks.IRIS.get(), new IndexedColorSettingType(List.of(0, 1), ColorSetting.EYE_COLOR_RANGE, ModNPCLooks.IRIS));
        builder.put(ModNPCLooks.SCLERA.get(), new IndexedColorSettingType(List.of(0, 1), ColorSetting.DEFAULT, ModNPCLooks.SCLERA));
        builder.put(ModNPCLooks.EYEBROWS.get(), new IndexedColorSettingType(List.of(0, 1), ColorSetting.EYEBROW_COLOR_RANGE, ModNPCLooks.EYEBROWS));
        if (female) {
            builder.put(ModNPCLooks.SLIM.get(), SlimLookFeatureType.INSTANCE);
            builder.put(ModNPCLooks.SIZE.get(), new SizeFeatureType(UniformGenerator.between(0.9f, 1.05f)));
            builder.put(ModNPCLooks.BLUSH.get(), new BlushFeatureType(0.5f, ColorSetting.BLUSH_COLOR_RANGE));
            builder.put(ModNPCLooks.HAIR.get(), new HairFeatureType(new TypedIndexRange(List.of(WeightedEntry.wrap(Pair.of("long", new IndexRange.FirstNIndices(5)), 1))), ColorSetting.HAIR_COLOR_RANGE));
        } else {
            builder.put(ModNPCLooks.SIZE.get(), new SizeFeatureType(UniformGenerator.between(0.95f, 1.1f)));
            builder.put(ModNPCLooks.HAIR.get(), new HairFeatureType(new TypedIndexRange(List.of(WeightedEntry.wrap(Pair.of("short", new IndexRange.FirstNIndices(5)), 1))), ColorSetting.HAIR_COLOR_RANGE));
        }
        cons.accept(builder);
        return map;
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

    private interface FeatureBuilderHelper {

        <T extends NPCFeature> void put(NPCFeatureType<T> type, NPCFeatureHolder<T> holder);
    }
}
