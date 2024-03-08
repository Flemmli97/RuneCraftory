package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.ConversationContext;
import io.github.flemmli97.runecraftory.api.datapack.NPCData;
import io.github.flemmli97.runecraftory.api.datapack.provider.NPCDataProvider;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.AttackMeleeAction;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.NPCAttackActions;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.RunAwayAction;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.SpellAttackAction;
import io.github.flemmli97.runecraftory.common.entities.npc.features.SlimLookFeature;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.ModNPCJobs;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
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

        this.addNPCData("random_npc", new NPCData.Builder(50)
                        .addGiftResponse("dislike", new NPCData.Gift(null, "npc.generic.dislike", -10), "Eh... thanks I guess?")
                        .addGiftResponse("like", new NPCData.Gift(null, "npc.generic.like", 25), "Thanks %player% for the gift. I really like this.")
                        .setNeutralGiftResponse("npc.generic.gift.default", "Thank you for your gift")
                        .withCombatAction(genericAttack)
                        .addTranslation(QuestGen.getTask(QuestGen.TAMING), "Tame a monster")
                        .addTranslation(QuestGen.getDescription(QuestGen.TAMING), "I need you to tame a monster. Come see me."),
                of(m -> {
                    m.put(ConversationContext.GREETING, new NPCData.ConversationSet.Builder("npc.generic.greeting.default", "Hello %player%.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.greeting.1", 0, 10).addCondition(TimeCheck.time(IntRange.range(0, 12000)).build()), "Good day %player%.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.greeting.2", 0, 10).addCondition(TimeCheck.time(IntRange.range(12000, 14000)).build()), "Good evening %player%.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.greeting.3", 0, 10), "Hi. How are you today %player%?"));
                    m.put(ConversationContext.TALK, new NPCData.ConversationSet.Builder("npc.generic.talk.default", "...")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.talk.1", 0, 10)
                                    .addAction(new NPCData.ConversationActionHolder("npc.generic.talk.1.answer.same", NPCData.ConversationAction.ANSWER, "npc.generic.talk.1.response.same", 0), "Same")
                                    .addAction(new NPCData.ConversationActionHolder("npc.generic.talk.1.answer.ok", NPCData.ConversationAction.ANSWER, "npc.generic.talk.1.response.ok", 0), "Ok..."), "On sunny days I like to go out and walk a lot.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.talk.1.response.same", 0, 10).setAnswer(), "It's nice right?")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.talk.1.response.ok", 0, 10).setAnswer(), "You don't?")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.talk.2", 0, 10), "I don't like working."));
                    m.put(ConversationContext.FOLLOW_YES, new NPCData.ConversationSet.Builder("npc.generic.follow.yes", "Ok"));
                    m.put(ConversationContext.FOLLOW_NO, new NPCData.ConversationSet.Builder("npc.generic.follow.no", "Sorry but I am busy right now."));
                    m.put(ConversationContext.FOLLOW_STOP, new NPCData.ConversationSet.Builder("npc.generic.follow.stop", "Ok. See you again."));
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
                        .setNeutralGiftResponse("npc.generic.2.gift.default", "Thanks. I appreciate it")
                        .withCombatAction(genericAttack),
                of(m -> {
                    m.put(ConversationContext.GREETING, new NPCData.ConversationSet.Builder("npc.generic.2.greeting.default", "Hello %player%.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.greeting.1", 0, 10), "Howdy %player%")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.greeting.2", 0, 10), "Hi. Whats up %player%?"));
                    m.put(ConversationContext.TALK, new NPCData.ConversationSet.Builder("npc.generic.2.talk.default", "...")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.talk.1", 0, 10), "Did you know that upgrading a weapon with scrap metal + makes it do 1 damage?")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.2.talk.2", 0, 10), "Those villagers seem strange. Why do they have no hands?"));
                    m.put(ConversationContext.FOLLOW_YES, new NPCData.ConversationSet.Builder("npc.generic.2.follow.yes", "Where are we going?"));
                    m.put(ConversationContext.FOLLOW_NO, new NPCData.ConversationSet.Builder("npc.generic.2.follow.no", "Sorry but I'm have something to take care of."));
                    m.put(ConversationContext.FOLLOW_STOP, new NPCData.ConversationSet.Builder("npc.generic.2.follow.stop", "Ok. Cya."));
                }),
                Map.of());

        this.addNPCData("random_npc_3", new NPCData.Builder(50)
                        .addGiftResponse("dislike", new NPCData.Gift(null, "npc.generic.3.dislike", -10), "What is this... Sorry but im not a fan of this")
                        .addGiftResponse("like", new NPCData.Gift(null, "npc.generic.3.like", 25), "Wow %player%. Thanks for the gift. This is a nice present!")
                        .setNeutralGiftResponse("npc.generic.3.gift.default", "Thank you for your gift")
                        .withCombatAction(genericAttack),
                of(m -> {
                    m.put(ConversationContext.GREETING, new NPCData.ConversationSet.Builder("npc.generic.3.greeting.default", "Hi %player%. How are you doing today?")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.greeting.1", 0, 10), "Nice to see you"));
                    m.put(ConversationContext.TALK, new NPCData.ConversationSet.Builder("npc.generic.3.talk.default", "...")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.talk.2", 0, 10), "A forge will allow you to craft your own weapons.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.talk.2", 0, 10).addCondition(WeatherCheck.weather().setRaining(true).build()), "I don't like the rain.")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.3.talk.2", 0, 10), "I've heard that there are places in this world where very strong monster appear."));
                    m.put(ConversationContext.FOLLOW_YES, new NPCData.ConversationSet.Builder("npc.generic.3.follow.yes", "Sure. Where do you want to go?"));
                    m.put(ConversationContext.FOLLOW_NO, new NPCData.ConversationSet.Builder("npc.generic.3.follow.no", "I think I still have some things to do first."));
                    m.put(ConversationContext.FOLLOW_STOP, new NPCData.ConversationSet.Builder("npc.generic.3.follow.stop", "Oh ok. Well then later."));
                }),
                Map.of());

        this.addLook(new ResourceLocation(RuneCraftory.MODID, "generic/male_1"), new NPCData.NPCLook(NPCData.Gender.MALE,
                npcTexture("generic/male_1"), null, 50, List.of()));
        this.addLook(new ResourceLocation(RuneCraftory.MODID, "generic/male_2"), new NPCData.NPCLook(NPCData.Gender.MALE,
                npcTexture("generic/male_2"), null, 50, List.of()));
        this.addLook(new ResourceLocation(RuneCraftory.MODID, "generic/male_3"), new NPCData.NPCLook(NPCData.Gender.MALE,
                npcTexture("generic/male_3"), null, 50, List.of()));
        this.addLook(new ResourceLocation(RuneCraftory.MODID, "generic/male_4"), new NPCData.NPCLook(NPCData.Gender.MALE,
                npcTexture("generic/male_4"), null, 50, List.of()));
        this.addLook(new ResourceLocation(RuneCraftory.MODID, "generic/female_1"), new NPCData.NPCLook(NPCData.Gender.FEMALE,
                npcTexture("generic/female_1"), null, 50, List.of()));
        this.addLook(new ResourceLocation(RuneCraftory.MODID, "generic/female_2"), new NPCData.NPCLook(NPCData.Gender.FEMALE,
                npcTexture("generic/female_2"), null, 50, List.of()));
        this.addLook(new ResourceLocation(RuneCraftory.MODID, "generic/female_3"), new NPCData.NPCLook(NPCData.Gender.FEMALE,
                npcTexture("generic/female_3"), null, 50, List.of()));
        this.addLook(new ResourceLocation(RuneCraftory.MODID, "generic/female_4"), new NPCData.NPCLook(NPCData.Gender.FEMALE,
                npcTexture("generic/female_4"), null, 50, List.of()));

        this.addNPCDataAll("smith/male_1", new NPCData.Builder(50, null, NPCData.Gender.MALE)
                        .withLook(new ResourceLocation(RuneCraftory.MODID, "smith/male_1"))
                        .withProfession(ModNPCJobs.SMITH.getSecond())
                        .addGiftResponse("dislike", new NPCData.Gift(RunecraftoryTags.SMITH_TRASH, "npc.smith.male.1.dislike", -10), "Hmm you better get rid of your trash yourself next time.")
                        .addGiftResponse("like", new NPCData.Gift(RunecraftoryTags.MINERALS, "npc.smith.male.1.like", 25), "You're giving this to me? Thanks! I will make good use of this")
                        .setNeutralGiftResponse("npc.smith.male.1.gift.default", "Thank you for your gift. I really like ores")
                        .withCombatAction(meleeAndFireball)
                        .addTranslation(QuestGen.getTask(QuestGen.MINING), "Acquire Hardware??")
                        .addTranslation(QuestGen.getDescription(QuestGen.MINING), "Come see me."),
                of(m -> {
                    m.put(ConversationContext.GREETING, new NPCData.ConversationSet.Builder("npc.smith.male.1.greeting.default", "Hello %player%. Let's do our best again today!"));
                    m.put(ConversationContext.TALK, new NPCData.ConversationSet.Builder("npc.smith.male.1.talk.default", "...")
                            .addConversation(new NPCData.Conversation.Builder("npc.smith.male.1.talk.1", 0, 10), "Nothing beats the clanging sound of a working forge.")
                            .addConversation(new NPCData.Conversation.Builder("npc.smith.male.1.talk.2", 0, 10), "There are multiple different weapon types. You should try them all out to find the one that suits you the best.")
                            .addConversation(new NPCData.Conversation.Builder("npc.smith.male.1.talk.thunder", 0, 10)
                                    .addCondition(WeatherCheck.weather().setThundering(true).build()), "It is really pouring down huh?"));
                    m.put(ConversationContext.FOLLOW_YES, new NPCData.ConversationSet.Builder("npc.smith.male.1.follow.yes", "Ok. Where are we going?"));
                    m.put(ConversationContext.FOLLOW_NO, new NPCData.ConversationSet.Builder("npc.smith.male.1.follow.no", "Hmm sorry but I can't right now."));
                    m.put(ConversationContext.FOLLOW_STOP, new NPCData.ConversationSet.Builder("npc.smith.male.1.follow.stop", "Cya then!"));
                }),
                new NPCData.NPCLook(NPCData.Gender.MALE, npcTexture("smith/male_1"), null, 0, List.of()),
                of(m -> {
                    m.put(QuestGen.MINING, new QuestResponseBuilder(
                            new NPCData.ConversationSet.Builder("npc.generic.quest.mining.start", """
                                    You saw my request? Great!
                                    You might have noticed various strange stones around the world. Those are minerals and they provide various different ores.
                                    I want you to mine 10 of them for me."""),
                            new NPCData.ConversationSet.Builder("npc.generic.quest.mining.active", "To mine minerals you need atleast an iron pickaxe or a hammer. " +
                                    "I want you to mine 10 mineral blocks for me."),
                            new NPCData.ConversationSet.Builder("npc.generic.quest.mining.end", "Nice! Mining ores increases your mining level. " +
                                    "With higher level you can get better ores from minerals. Here take this hammer, it should make mining minerals a bit easier.")
                    ));
                }));
        this.addNPCDataAll("smith/female_1", new NPCData.Builder(50, null, NPCData.Gender.FEMALE)
                        .withLook(new ResourceLocation(RuneCraftory.MODID, "smith/female_1"))
                        .withProfession(ModNPCJobs.SMITH.getSecond())
                        .addGiftResponse("dislike", new NPCData.Gift(RunecraftoryTags.SMITH_TRASH, "npc.smith.female.1.dislike", -10), "Hey! I'm not your trashcan!")
                        .addGiftResponse("like", new NPCData.Gift(RunecraftoryTags.MINERALS, "npc.smith.female.1.like", 25), "Wow thanks! I can make something great using this")
                        .setNeutralGiftResponse("npc.smith.female.1.gift.default", "Thanks. Did you know ores are one of my favorite things?")
                        .withCombatAction(meleeAndFireball),
                of(m -> {
                    m.put(ConversationContext.GREETING, new NPCData.ConversationSet.Builder("npc.smith.female.1.greeting.default", "Hello %player%. Today is a nice day!"));
                    m.put(ConversationContext.TALK, new NPCData.ConversationSet.Builder("npc.smith.female.1.talk.default", "...")
                            .addConversation(new NPCData.Conversation.Builder("npc.smith.female.1.talk.1", 0, 10), "Working as a blacksmith is hard but very fun.")
                            .addConversation(new NPCData.Conversation.Builder("npc.smith.female.1.talk.2", 0, 10), "Someday I would like to make some kind of legendary weapon.")
                            .addConversation(new NPCData.Conversation.Builder("npc.smith.female.1.talk.height", 0, 10)
                                    .addCondition(LocationCheck.checkLocation(new LocationPredicate.Builder().setY(MinMaxBounds.Doubles.atLeast(150))).build()), "Urgh. I'm no good with this height..."));
                    m.put(ConversationContext.FOLLOW_YES, new NPCData.ConversationSet.Builder("npc.smith.female.1.follow.yes", "Ok. Where are we going?"));
                    m.put(ConversationContext.FOLLOW_NO, new NPCData.ConversationSet.Builder("npc.smith.female.1.follow.no", "Sorry but I'm busy right now."));
                    m.put(ConversationContext.FOLLOW_STOP, new NPCData.ConversationSet.Builder("npc.smith.female.1.follow.stop", "Bye! See you next time"));
                }),
                new NPCData.NPCLook(NPCData.Gender.FEMALE, npcTexture("smith/female_1"), null, 0, List.of(new SlimLookFeature())),
                of(m -> {
                    m.put(QuestGen.MINING, new QuestResponseBuilder(
                            new NPCData.ConversationSet.Builder("npc.generic.quest.mining.start", """
                                    You saw my request? Great!
                                    You might have noticed various strange stones around the world. Those are minerals and they provide various different ores.
                                    I want you to mine 10 of them for me."""),
                            new NPCData.ConversationSet.Builder("npc.generic.quest.mining.active", "To mine minerals you need atleast an iron pickaxe or a hammer. " +
                                    "I want you to mine 10 mineral blocks for me."),
                            new NPCData.ConversationSet.Builder("npc.generic.quest.mining.end", "Nice! Mining ores increases your mining level. " +
                                    "With higher level you can get better ores from minerals. Here take this hammer, it should make mining minerals a bit easier.")
                    ));
                }));

        this.addNPCDataAll("shop_owner/male_1", new NPCData.Builder(50, null, NPCData.Gender.MALE)
                        .withLook(new ResourceLocation(RuneCraftory.MODID, "shop_owner/male_1"))
                        .withProfession(ModNPCJobs.GENERAL.getSecond(), ModNPCJobs.FLOWER.getSecond())
                        .addGiftResponse("dislike", new NPCData.Gift(RunecraftoryTags.GENERIC_TRASH, "npc.shop_owner.male.1.dislike", -10), "Eh... what is this?")
                        .addGiftResponse("like", new NPCData.Gift(RunecraftoryTags.CROPS, "npc.shop_owner.male.1.like", 25), "Thank you. Appreciate it.")
                        .setNeutralGiftResponse("npc.shop_owner.male.1.gift.default", "Thanks. I would really like some crops.")
                        .withCombatAction(genericAttack)
                        .addTranslation(QuestGen.getTask(QuestGen.SHIP_TURNIP), "First Shipment")
                        .addTranslation(QuestGen.getDescription(QuestGen.SHIP_TURNIP), "Come see me."),
                of(m -> {
                    m.put(ConversationContext.GREETING, new NPCData.ConversationSet.Builder("npc.shop_owner.male.1.greeting.default", "Hey. Nice to see you %player%."));
                    m.put(ConversationContext.TALK, new NPCData.ConversationSet.Builder("npc.shop_owner.male.1.talk.default", "...")
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.male.1.talk.1", 0, 10), "You should totally check out my shop.")
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.male.1.talk.2", 0, 10), "Not watering crops will make them wilt so be sure to water them everyday."));
                    m.put(ConversationContext.FOLLOW_YES, new NPCData.ConversationSet.Builder("npc.shop_owner.male.1.follow.yes", "Where are we going?"));
                    m.put(ConversationContext.FOLLOW_NO, new NPCData.ConversationSet.Builder("npc.shop_owner.male.1.follow.no", "Hmm... maybe another time"));
                    m.put(ConversationContext.FOLLOW_STOP, new NPCData.ConversationSet.Builder("npc.shop_owner.male.1.follow.stop", "Ok Bye!"));
                }),
                new NPCData.NPCLook(NPCData.Gender.MALE, npcTexture("shop_owner/male_1"), null, 0, List.of()),
                of(m -> {
                    m.put(QuestGen.SHIP_TURNIP, new QuestResponseBuilder(
                            new NPCData.ConversationSet.Builder("npc.generic.quest.ship_turnip.start", """
                                    Are you here for my request?
                                    I will show you how to ship items to make money: Shipping items is very simple. First you need a shipping bin. It can hold any shippable items in it.
                                    Put the items you want to ship in it and everyday in the morning your items will be automatically shipped. Lets try it out now: I want you to ship a turnip."""),
                            new NPCData.ConversationSet.Builder("npc.generic.quest.ship_turnip.active", "Please ship a turnip."),
                            new NPCData.ConversationSet.Builder("npc.generic.quest.ship_turnip.end", "Great. There are a lot of items you can ship to make money. Here take" +
                                    "these turnip seeds. It should come in handy.")
                    ));
                }));
        this.addNPCDataAll("shop_owner/female_1", new NPCData.Builder(50, null, NPCData.Gender.FEMALE)
                        .withLook(new ResourceLocation(RuneCraftory.MODID, "shop_owner/female_1"))
                        .withProfession(ModNPCJobs.GENERAL.getSecond(), ModNPCJobs.FLOWER.getSecond())
                        .addGiftResponse("dislike", new NPCData.Gift(RunecraftoryTags.GENERIC_TRASH, "npc.shop_owner.female.1.dislike", -10), "Why would you give this to me???")
                        .addGiftResponse("like", new NPCData.Gift(RunecraftoryTags.CROPS, "npc.shop_owner.female.1.like", 25), "Thanks! I can totally sell this... Huh? I said nothing.")
                        .setNeutralGiftResponse("npc.shop_owner.female.1.gift.default", "Thank you. Come crops would be nice.")
                        .withCombatAction(genericAttack),
                of(m -> {
                    m.put(ConversationContext.GREETING, new NPCData.ConversationSet.Builder("npc.shop_owner.female.1.greeting.default", "Heya %player%. It's good to see you!"));
                    m.put(ConversationContext.TALK, new NPCData.ConversationSet.Builder("npc.shop_owner.female.1.talk.default", "...")
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.female.1.talk.1", 0, 10), "Buy something from my shop will ya!")
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.female.1.talk.2", 0, 10), "I heard crops can sometimes grow to very big sizes."));
                    m.put(ConversationContext.FOLLOW_YES, new NPCData.ConversationSet.Builder("npc.shop_owner.female.1.follow.yes", "Where do you want to go?"));
                    m.put(ConversationContext.FOLLOW_NO, new NPCData.ConversationSet.Builder("npc.shop_owner.female.1.follow.no", "I have something to do right now. Maybe next time..."));
                    m.put(ConversationContext.FOLLOW_STOP, new NPCData.ConversationSet.Builder("npc.shop_owner.female.1.follow.stop", "See ya next time. Bye!"));
                }),
                new NPCData.NPCLook(NPCData.Gender.FEMALE, npcTexture("shop_owner/female_1"), null, 0, List.of(new SlimLookFeature())),
                of(m -> {
                    m.put(QuestGen.SHIP_TURNIP, new QuestResponseBuilder(
                            new NPCData.ConversationSet.Builder("npc.generic.quest.ship_turnip.start", """
                                    Are you here for my request?
                                    I will show you how to ship items to make money: Shipping items is very simple. First you need a shipping bin. It can hold any shippable items in it.
                                    Put the items you want to ship in it and everyday in the morning your items will be automatically shipped. Lets try it out now: I want you to ship a turnip."""),
                            new NPCData.ConversationSet.Builder("npc.generic.quest.ship_turnip.active", "Please ship a turnip."),
                            new NPCData.ConversationSet.Builder("npc.generic.quest.ship_turnip.end", "Great. There are a lot of items you can ship to make money. Here take" +
                                    "these turnip seeds. It should come in handy.")
                    ));
                }));

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
                    m.put(ConversationContext.GREETING, new NPCData.ConversationSet.Builder("npc.generic.all.talk.default", "Test")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.all.greeting.1", 0, 10), "Test")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.all.greeting.2", 0, 10), "Test"));
                    m.put(ConversationContext.TALK, new NPCData.ConversationSet.Builder("npc.generic.2.talk.default", "Test")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.all.talk.1", 0, 10), "Test")
                            .addConversation(new NPCData.Conversation.Builder("npc.generic.all.talk.2", 0, 10), "Test"));
                    m.put(ConversationContext.FOLLOWYES, new NPCData.ConversationSet.Builder("npc.generic.all.follow.yes", "Test"));
                    m.put(ConversationContext.FOLLOW_NO, new NPCData.ConversationSet.Builder("npc.generic.all.follow.no", "Test"));
                    m.put(ConversationContext.FOLLOW_STOP, new NPCData.ConversationSet.Builder("npc.generic.all.follow.stop", "Test"));
                }),
                new NPCData.NPCLook(NPCData.Gender.UNDEFINED, new ResourceLocation(RuneCraftory.MODID, "texture"), "Flemmli97", 0, List.of(
                        new NPCData.LookFeature(NPCData.StaticLookTypes.SLIM_MODEL),
                        new NPCData.LookFeature(Pair.of(new ResourceLocation(RuneCraftory.MODID, "test_feature"), new ResourceLocation(RuneCraftory.MODID, "test_value")))
                )),
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
