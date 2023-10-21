package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.NPCData;
import io.github.flemmli97.runecraftory.api.datapack.provider.NPCDataProvider;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.AttackMeleeAction;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.NPCAttackActions;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.predicates.TimeCheck;
import net.minecraft.world.level.storage.loot.predicates.WeatherCheck;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.LinkedHashMap;
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
                        .action(new AttackMeleeAction(UniformGenerator.between(30, 70)))));

        this.addNPCData("random_npc", new NPCData.Builder(50)
                        .addGiftResponse("dislike", new NPCData.Gift(null, "npc.generic.dislike", -10), "Eh... thanks I guess?")
                        .addGiftResponse("like", new NPCData.Gift(null, "npc.generic.like", 25), "Thanks %s for the gift. I really like this.")
                        .setNeutralGiftResponse("npc.generic.gift.default", "Thank you for your gift")
                        .withCombatAction(genericAttack),
                of(m -> {
                    m.put(NPCData.ConversationType.GREETING, new NPCData.ConversationSet.Builder("npc.generic.greeting.default", "Hello %s.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.greeting.1", 0, 10).addCondition(TimeCheck.time(IntRange.range(0, 12000)).build()), "Good day %s.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.greeting.2", 0, 10).addCondition(TimeCheck.time(IntRange.range(12000, 14000)).build()), "Good evening %s.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.greeting.3", 0, 10), "Hi. How are you today %s?"));
                    m.put(NPCData.ConversationType.TALK, new NPCData.ConversationSet.Builder("npc.generic.talk.default", "...")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.talk.1", 0, 10)
                                    .addAction(new NPCData.ConversationActionHolder("npc.generic.talk.1.answer.same", NPCData.ConversationAction.ANSWER, "npc.generic.talk.1.response.same", 0), "Same")
                                    .addAction(new NPCData.ConversationActionHolder("npc.generic.talk.1.answer.ok", NPCData.ConversationAction.ANSWER, "npc.generic.talk.1.response.ok", 0), "Ok..."), "On sunny days I like to go out and walk a lot.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.talk.1.response.same", 0, 10).setAnswer(), "It's nice right?")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.talk.1.response.ok", 0, 10).setAnswer(), "You don't?")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.talk.2", 0, 10), "I don't like working."));
                    m.put(NPCData.ConversationType.FOLLOWYES, new NPCData.ConversationSet.Builder("npc.generic.follow.yes", "Ok"));
                    m.put(NPCData.ConversationType.FOLLOWNO, new NPCData.ConversationSet.Builder("npc.generic.follow.no", "Sorry but I am busy right now."));
                    m.put(NPCData.ConversationType.FOLLOWSTOP, new NPCData.ConversationSet.Builder("npc.generic.follow.stop", "Ok. See you again."));
                }),
                of(m -> {
                    m.put(new ResourceLocation(RuneCraftory.MODID, "tame_monster"), new QuestResponseBuilder(
                            new NPCData.ConversationSet.Builder("npc.generic.quest.tame_monster.start", "Please tame a monster."),
                            new NPCData.ConversationSet.Builder("npc.generic.quest.tame_monster.active", "You still need to tame a monster."),
                            new NPCData.ConversationSet.Builder("npc.generic.quest.tame_monster.end", "Thank you for taming a monster.")
                    ));
                }));

        this.addNPCData("random_npc_2", new NPCData.Builder(50)
                        .setNeutralGiftResponse("npc.generic.2.gift.default", "Thanks. I appreciate it")
                        .withCombatAction(genericAttack),
                of(m -> {
                    m.put(NPCData.ConversationType.GREETING, new NPCData.ConversationSet.Builder("npc.generic.2.greeting.default", "Hello %s.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.greeting.1", 0, 10), "Howdy %s")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.greeting.2", 0, 10), "Hi. Whats up %s?"));
                    m.put(NPCData.ConversationType.TALK, new NPCData.ConversationSet.Builder("npc.generic.2.talk.default", "...")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.talk.1", 0, 10), "Did you know that upgrading a weapon with scrap metal + makes it do 1 damage?")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.talk.2", 0, 10), "Those villagers seem strange. Why do they have no hands?"));
                    m.put(NPCData.ConversationType.FOLLOWYES, new NPCData.ConversationSet.Builder("npc.generic.2.follow.yes", "Where are we going?"));
                    m.put(NPCData.ConversationType.FOLLOWNO, new NPCData.ConversationSet.Builder("npc.generic.2.follow.no", "Sorry but I'm have something to take care of."));
                    m.put(NPCData.ConversationType.FOLLOWSTOP, new NPCData.ConversationSet.Builder("npc.generic.2.follow.stop", "Ok. Cya."));
                }),
                of(m -> {

                }));

        this.addNPCData("random_npc_3", new NPCData.Builder(50)
                        .addGiftResponse("dislike", new NPCData.Gift(null, "npc.generic.3.dislike", -10), "What is this... Sorry but im not a fan of this")
                        .addGiftResponse("like", new NPCData.Gift(null, "npc.generic.3.like", 25), "Wow %s. Thanks for the gift. This is a nice present!")
                        .setNeutralGiftResponse("npc.generic.3.gift.default", "Thank you for your gift")
                        .withCombatAction(genericAttack),
                of(m -> {
                    m.put(NPCData.ConversationType.GREETING, new NPCData.ConversationSet.Builder("npc.generic.3.greeting.default", "Hi %s. How are you doing today?")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.greeting.1", 0, 10), "Nice to see you"));
                    m.put(NPCData.ConversationType.TALK, new NPCData.ConversationSet.Builder("npc.generic.3.talk.default", "...")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.talk.2", 0, 10), "A forge will allow you to craft your own weapons.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.talk.1", 0, 10).addCondition(WeatherCheck.weather().setRaining(true).build()), "I don't like the rain.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.talk.2", 0, 10), "I've heard that there are places in this world where very strong monster appear."));
                    m.put(NPCData.ConversationType.FOLLOWYES, new NPCData.ConversationSet.Builder("npc.generic.3.follow.yes", "Sure. Where do you want to go?"));
                    m.put(NPCData.ConversationType.FOLLOWNO, new NPCData.ConversationSet.Builder("npc.generic.3.follow.no", "I think I still have some things to do first."));
                    m.put(NPCData.ConversationType.FOLLOWSTOP, new NPCData.ConversationSet.Builder("npc.generic.3.follow.stop", "Oh ok. Well then later."));
                }),
                of(m -> {

                }));

        /*this.addNPCData("random_npc", new NPCData.Builder(50)
                        .addGiftResponse("dislike", new NPCData.Gift(null, "npc.generic.dislike", -10), "Eh... thanks I guess?")
                        .addGiftResponse("like", new NPCData.Gift(null, "npc.generic.like", 25), "Thanks %s for the gift. I really like this.")
                        .setNeutralGiftResponse("npc.generic.gift.default", "Thank you for your gift")
                        .withProfession(ModNPCJobs.GENERAL.getSecond())
                        .withCombatAction(genericAttack),
                of(m -> {
                    m.put(NPCData.ConversationType.GREETING, new NPCData.ConversationSet.Builder("npc.generic.greeting.default", "Hello %s.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.greeting.1", 0, 10).addCondition(TimeCheck.time(IntRange.range(0, 12000)).build()), "Good day %s.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.greeting.2", 0, 10).addCondition(TimeCheck.time(IntRange.range(12000, 14000)).build()), "Good evening %s.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.greeting.3", 0, 10), "Hi. How are you today %s?"));
                    m.put(NPCData.ConversationType.TALK, new NPCData.ConversationSet.Builder("npc.generic.talk.default", "...")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.talk.1", 0, 10)
                                    .addAction(new NPCData.ConversationActionHolder("npc.generic.talk.1.answer.same", NPCData.ConversationAction.ANSWER, "npc.generic.talk.1.response.same", 0), "Same")
                                    .addAction(new NPCData.ConversationActionHolder("npc.generic.talk.1.answer.ok", NPCData.ConversationAction.ANSWER, "npc.generic.talk.1.response.ok", 0), "Ok..."), "On sunny days I like to go out and walk a lot.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.talk.1.response.same", 0, 10).setAnswer(), "It's nice right?")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.talk.1.response.ok", 0, 10).setAnswer(), "You don't?")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.talk.2", 0, 10), "I don't like working."));
                    m.put(NPCData.ConversationType.FOLLOWYES, new NPCData.ConversationSet.Builder("npc.generic.follow.yes", "Ok"));
                    m.put(NPCData.ConversationType.FOLLOWNO, new NPCData.ConversationSet.Builder("npc.generic.follow.no", "Sorry but I am busy right now."));
                    m.put(NPCData.ConversationType.FOLLOWSTOP, new NPCData.ConversationSet.Builder("npc.generic.follow.stop", "Ok. See you again."));
                }),
                of(m -> {
                    m.put(new ResourceLocation(RuneCraftory.MODID, "tame_monster"), new QuestResponseBuilder(
                            new NPCData.ConversationSet.Builder("npc.generic.quest.tame_monster.start", "Please tame a monster."),
                            new NPCData.ConversationSet.Builder("npc.generic.quest.tame_monster.active", "You still need to tame a monster."),
                            new NPCData.ConversationSet.Builder("npc.generic.quest.tame_monster.end", "Thank you for taming a monster.")
                    ));
                }));*/

        //Test data using all possible fields
        /*ResourceLocation attackAll = this.addAttackActions(new ResourceLocation(RuneCraftory.MODID, "attack_all"), new NPCAttackActions.Builder()
                .addAction(new NPCAttackActions.ActionBuilder(1)
                        .action(new AttackMeleeAction(ConstantValue.exactly(40), ConstantValue.exactly(0)))
                        .action(new RunToLeadAction(ConstantValue.exactly(40), ConstantValue.exactly(0))))
                .addAction(new NPCAttackActions.ActionBuilder(1)
                        .action(new FoodThrowAction(List.of(new ItemStack(Items.APPLE)), ConstantValue.exactly(40), ConstantValue.exactly(0))))
                .addAction(new NPCAttackActions.ActionBuilder(1)
                        .action(new RunAwayAction(ConstantValue.exactly(40), ConstantValue.exactly(0))))
                .addAction(new NPCAttackActions.ActionBuilder(1)
                        .action(new SpellAttackAction(ModSpells.FIREBALL.get(), 8, false, ConstantValue.exactly(40), ConstantValue.exactly(0))))
                .addAction(new NPCAttackActions.ActionBuilder(1)
                        .action(new PartyTargetAction(ModSpells.CURE_ALL.get(), false, ConstantValue.exactly(0)))));
        this.addNPCDataAll("random_npc_all_test", new NPCData.Builder(50, "Name", "Surname", NPCData.Gender.UNDEFINED)
                        .setNeutralGiftResponse("npc.generic.all.gift.default", "Test")
                        .withLook(new ResourceLocation(RuneCraftory.MODID, "test_look"))
                        .withBirthday(com.mojang.datafixers.util.Pair.of(EnumSeason.SPRING, 1))
                        .setUnique(1)
                        .withProfession(ModNPCJobs.GENERAL.getSecond())
                        .addGiftResponse("gift", new NPCData.Gift(ModTags.SEEDS, "gift.response.test", 1), "Test")
                        .withSchedule(new NPCSchedule.Schedule(0, 1, 1, 1, 1, 1, 1, 1, EnumSet.noneOf(EnumDay.class)))
                        .setBaseStat(Attributes.MAX_HEALTH, 1)
                        .setStatIncrease(Attributes.MAX_HEALTH, 1)
                        .setBaseLevel(5).withCombatAction(attackAll),
                of(m -> {
                    m.put(NPCData.ConversationType.GREETING, new NPCData.ConversationSet.Builder("npc.generic.all.talk.default", "Test")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.all.greeting.1", 0, 10), "Test")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.all.greeting.2", 0, 10), "Test"));
                    m.put(NPCData.ConversationType.TALK, new NPCData.ConversationSet.Builder("npc.generic.2.talk.default", "Test")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.all.talk.1", 0, 10), "Test")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.all.talk.2", 0, 10), "Test"));
                    m.put(NPCData.ConversationType.FOLLOWYES, new NPCData.ConversationSet.Builder("npc.generic.all.follow.yes", "Test"));
                    m.put(NPCData.ConversationType.FOLLOWNO, new NPCData.ConversationSet.Builder("npc.generic.all.follow.no", "Test"));
                    m.put(NPCData.ConversationType.FOLLOWSTOP, new NPCData.ConversationSet.Builder("npc.generic.all.follow.stop", "Test"));
                }),
                new NPCData.NPCLook(NPCData.Gender.UNDEFINED, new ResourceLocation(RuneCraftory.MODID, "texture"), "Flemmli97", 0, List.of()),
                Map.of());*/
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
