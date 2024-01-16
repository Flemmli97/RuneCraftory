package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.NPCData;
import io.github.flemmli97.runecraftory.api.datapack.provider.NPCDataProvider;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.AttackMeleeAction;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.NPCAttackActions;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.RunAwayAction;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.actions.SpellAttackAction;
import io.github.flemmli97.runecraftory.common.registry.ModNPCJobs;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
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
                        .action(new AttackMeleeAction(UniformGenerator.between(30, 70)))));

        ResourceLocation meleeAndFireball = this.addAttackActions(new ResourceLocation(RuneCraftory.MODID, "melee_fireball_attack"), new NPCAttackActions.Builder()
                .addAction(new NPCAttackActions.ActionBuilder(1)
                        .action(new AttackMeleeAction(UniformGenerator.between(30, 70))))
                .addAction(new NPCAttackActions.ActionBuilder(1)
                        .action(new RunAwayAction(UniformGenerator.between(20, 30)))
                        .action(new SpellAttackAction(ModSpells.FIREBALL.get(), 8, false, UniformGenerator.between(20, 40), UniformGenerator.between(10, 20)))));

        this.addNPCData("random_npc", new NPCData.Builder(50)
                        .addGiftResponse("dislike", new NPCData.Gift(null, "npc.generic.dislike", -10), "Eh... thanks I guess?")
                        .addGiftResponse("like", new NPCData.Gift(null, "npc.generic.like", 25), "Thanks %s for the gift. I really like this.")
                        .setNeutralGiftResponse("npc.generic.gift.default", "Thank you for your gift")
                        .withCombatAction(genericAttack)
                        .addTranslation(QuestGen.getTask(QuestGen.TAMING), "Tame a monster")
                        .addTranslation(QuestGen.getDescription(QuestGen.TAMING), "I need you to tame a monster. Come see me."),
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
                    m.put(QuestGen.TAMING, new QuestResponseBuilder(
                            new NPCData.ConversationSet.Builder("npc.generic.quest.tame_monster.start", "Did you know that you can tame the monsters in this world?\n" +
                                    "You would need to setup a barn first and then just give them an item.\n\nWith that said I would like you to tame a monster."),
                            new NPCData.ConversationSet.Builder("npc.generic.quest.tame_monster.active", "You still need to tame a monster.\n" +
                                    "Some monsters prefer certain items more."),
                            new NPCData.ConversationSet.Builder("npc.generic.quest.tame_monster.end", "I see you've successfully tamed a monster. Congrats!")
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
                Map.of());

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
                Map.of());

        this.addNPCDataAll("smith/male_1", new NPCData.Builder(50, null, NPCData.Gender.MALE)
                        .withLook(new ResourceLocation(RuneCraftory.MODID, "smith/male_1"))
                        .withProfession(ModNPCJobs.SMITH.getSecond())
                        .addGiftResponse("dislike", new NPCData.Gift(ModTags.SMITH_TRASH, "npc.smith.male.1.dislike", -10), "Hmm you better get rid of your trash yourself next time.")
                        .addGiftResponse("like", new NPCData.Gift(ModTags.MINERALS, "npc.smith.male.1.like", 25), "You're giving this to me? Thanks! I will make good use of this")
                        .setNeutralGiftResponse("npc.smith.male.1.gift.default", "Thank you for your gift. I really like ores")
                        .withCombatAction(meleeAndFireball)
                        .addTranslation(QuestGen.getTask(QuestGen.MINING), "Acquire Hardware??")
                        .addTranslation(QuestGen.getDescription(QuestGen.MINING), "Come see me."),
                of(m -> {
                    m.put(NPCData.ConversationType.GREETING, new NPCData.ConversationSet.Builder("npc.smith.male.1.greeting.default", "Hello %s. Let's do our best again today!"));
                    m.put(NPCData.ConversationType.TALK, new NPCData.ConversationSet.Builder("npc.smith.male.1.talk.default", "...")
                            .addConversation(new NPCData.Conversation.Builder("npc.smith.male.1.talk.1", 0, 10), "Nothing beats the clanging sound of a working forge.")
                            .addConversation(new NPCData.Conversation.Builder("npc.smith.male.1.talk.2", 0, 10), "There are multiple different weapon types. You should try them all out to find the one that suits you the best."));
                    m.put(NPCData.ConversationType.FOLLOWYES, new NPCData.ConversationSet.Builder("npc.smith.male.1.follow.yes", "Ok. Where are we going?"));
                    m.put(NPCData.ConversationType.FOLLOWNO, new NPCData.ConversationSet.Builder("npc.smith.male.1.follow.no", "Hmm sorry but I can't right now."));
                    m.put(NPCData.ConversationType.FOLLOWSTOP, new NPCData.ConversationSet.Builder("npc.smith.male.1.follow.stop", "Cya then!"));
                }),
                new NPCData.NPCLook(NPCData.Gender.MALE, npcTexture("smith/male_1"), null, 0, List.of()),
                of(m -> {
                    m.put(QuestGen.MINING, new QuestResponseBuilder(
                            new NPCData.ConversationSet.Builder("npc.generic.quest.mining.start", "You saw my request? Great!\n" +
                                    "You might have noticed various strange stones around the world. Those are minerals and they provide various different ores.\n" +
                                    "I want you to mine 10 of them for me."),
                            new NPCData.ConversationSet.Builder("npc.generic.quest.mining.active", "To mine minerals you need atleast an iron pickaxe or a hammer. " +
                                    "I want you to mine 10 mineral blocks for me."),
                            new NPCData.ConversationSet.Builder("npc.generic.quest.mining.end", "Nice! Mining ores increases your mining level. " +
                                    "With higher level you can get better ores from minerals. Here take this hammer, it should make mining minerals a bit easier.")
                    ));
                }));
        this.addNPCDataAll("smith/female_1", new NPCData.Builder(50, null, NPCData.Gender.FEMALE)
                        .withLook(new ResourceLocation(RuneCraftory.MODID, "smith/female_1"))
                        .withProfession(ModNPCJobs.SMITH.getSecond())
                        .addGiftResponse("dislike", new NPCData.Gift(ModTags.SMITH_TRASH, "npc.smith.female.1.dislike", -10), "Hey! I'm not your trashcan!")
                        .addGiftResponse("like", new NPCData.Gift(ModTags.MINERALS, "npc.smith.female.1.like", 25), "Wow thanks! I can make something great using this")
                        .setNeutralGiftResponse("npc.smith.female.1.gift.default", "Thanks. Did you know ores are one of my favorite things?")
                        .withCombatAction(meleeAndFireball),
                of(m -> {
                    m.put(NPCData.ConversationType.GREETING, new NPCData.ConversationSet.Builder("npc.smith.female.1.greeting.default", "Hello %s. Today is a nice day!"));
                    m.put(NPCData.ConversationType.TALK, new NPCData.ConversationSet.Builder("npc.smith.female.1.talk.default", "...")
                            .addConversation(new NPCData.Conversation.Builder("npc.smith.female.1.talk.1", 0, 10), "Working as a blacksmith is hard but very fun.")
                            .addConversation(new NPCData.Conversation.Builder("npc.smith.female.1.talk.2", 0, 10), "Someday I would like to make some kind of legendary weapon."));
                    m.put(NPCData.ConversationType.FOLLOWYES, new NPCData.ConversationSet.Builder("npc.smith.female.1.follow.yes", "Ok. Where are we going?"));
                    m.put(NPCData.ConversationType.FOLLOWNO, new NPCData.ConversationSet.Builder("npc.smith.female.1.follow.no", "Sorry but I'm busy right now."));
                    m.put(NPCData.ConversationType.FOLLOWSTOP, new NPCData.ConversationSet.Builder("npc.smith.female.1.follow.stop", "Bye! See you next time"));
                }),
                new NPCData.NPCLook(NPCData.Gender.FEMALE, npcTexture("smith/female_1"), null, 0, List.of(new NPCData.LookFeature(NPCData.StaticLookTypes.SLIM_MODEL))),
                of(m -> {
                    m.put(QuestGen.MINING, new QuestResponseBuilder(
                            new NPCData.ConversationSet.Builder("npc.generic.quest.mining.start", "You saw my request? Great!\n" +
                                    "You might have noticed various strange stones around the world. Those are minerals and they provide various different ores.\n" +
                                    "I want you to mine 10 of them for me."),
                            new NPCData.ConversationSet.Builder("npc.generic.quest.mining.active", "To mine minerals you need atleast an iron pickaxe or a hammer. " +
                                    "I want you to mine 10 mineral blocks for me."),
                            new NPCData.ConversationSet.Builder("npc.generic.quest.mining.end", "Nice! Mining ores increases your mining level. " +
                                    "With higher level you can get better ores from minerals. Here take this hammer, it should make mining minerals a bit easier.")
                    ));
                }));

        this.addNPCDataAll("shop_owner/male_1", new NPCData.Builder(50, null, NPCData.Gender.MALE)
                        .withLook(new ResourceLocation(RuneCraftory.MODID, "shop_owner/male_1"))
                        .withProfession(ModNPCJobs.GENERAL.getSecond(), ModNPCJobs.FLOWER.getSecond())
                        .addGiftResponse("dislike", new NPCData.Gift(ModTags.GENERIC_TRASH, "npc.shop_owner.male.1.dislike", -10), "Eh... what is this?")
                        .addGiftResponse("like", new NPCData.Gift(ModTags.CROPS, "npc.shop_owner.male.1.like", 25), "Thank you. Appreciate it.")
                        .setNeutralGiftResponse("npc.shop_owner.male.1.gift.default", "Thanks. I would really like some crops.")
                        .withCombatAction(genericAttack)
                        .addTranslation(QuestGen.getTask(QuestGen.SHIP_TURNIP), "Acquire Hardware??")
                        .addTranslation(QuestGen.getDescription(QuestGen.SHIP_TURNIP), "Come see me."),
                of(m -> {
                    m.put(NPCData.ConversationType.GREETING, new NPCData.ConversationSet.Builder("npc.shop_owner.male.1.greeting.default", "Hey. Nice to see you %s."));
                    m.put(NPCData.ConversationType.TALK, new NPCData.ConversationSet.Builder("npc.shop_owner.male.1.talk.default", "...")
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.male.1.talk.1", 0, 10), "You should totally check out my shop.")
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.male.1.talk.2", 0, 10), "Not watering crops will make them wilt so be sure to water them everyday."));
                    m.put(NPCData.ConversationType.FOLLOWYES, new NPCData.ConversationSet.Builder("npc.shop_owner.male.1.follow.yes", "Where are we going?"));
                    m.put(NPCData.ConversationType.FOLLOWNO, new NPCData.ConversationSet.Builder("npc.shop_owner.male.1.follow.no", "Hmm... maybe another time"));
                    m.put(NPCData.ConversationType.FOLLOWSTOP, new NPCData.ConversationSet.Builder("npc.shop_owner.male.1.follow.stop", "Ok Bye!"));
                }),
                new NPCData.NPCLook(NPCData.Gender.MALE, npcTexture("shop_owner/male_1"), null, 0, List.of()),
                of(m -> {
                    m.put(QuestGen.SHIP_TURNIP, new QuestResponseBuilder(
                            new NPCData.ConversationSet.Builder("npc.generic.quest.ship_turnip.start", "Are you here for my request?\n" +
                                    "I will show you how to ship items to make money: Shipping items is very simple. First you need a shipping bin. It can hold any shippable items in it.\n" +
                                    "Put the items you want to ship in it and everyday in the morning your items will be automatically shipped. Lets try it out now: I want you to ship a turnip."),
                            new NPCData.ConversationSet.Builder("npc.generic.quest.ship_turnip.active", "Please ship a turnip."),
                            new NPCData.ConversationSet.Builder("npc.generic.quest.ship_turnip.end", "Great. There are a lot of items you can ship to make money. Here take" +
                                    "these turnip seeds. It should come in handy.")
                    ));
                }));
        this.addNPCDataAll("shop_owner/female_1", new NPCData.Builder(50, null, NPCData.Gender.FEMALE)
                        .withLook(new ResourceLocation(RuneCraftory.MODID, "shop_owner/female_1"))
                        .withProfession(ModNPCJobs.GENERAL.getSecond(), ModNPCJobs.FLOWER.getSecond())
                        .addGiftResponse("dislike", new NPCData.Gift(ModTags.GENERIC_TRASH, "npc.shop_owner.female.1.dislike", -10), "Why would you give this to me???")
                        .addGiftResponse("like", new NPCData.Gift(ModTags.CROPS, "npc.shop_owner.female.1.like", 25), "Thanks! I can totally sell this... Huh? I said nothing.")
                        .setNeutralGiftResponse("npc.shop_owner.female.1.gift.default", "Thank you. Come crops would be nice.")
                        .withCombatAction(genericAttack),
                of(m -> {
                    m.put(NPCData.ConversationType.GREETING, new NPCData.ConversationSet.Builder("npc.shop_owner.female.1.greeting.default", "Heya %s. It's good to see you!"));
                    m.put(NPCData.ConversationType.TALK, new NPCData.ConversationSet.Builder("npc.shop_owner.female.1.talk.default", "...")
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.female.1.talk.1", 0, 10), "Buy something from my shop will ya!")
                            .addConversation(new NPCData.Conversation.Builder("npc.shop_owner.female.1.talk.2", 0, 10), "I heard crops can sometimes grow to very big sizes."));
                    m.put(NPCData.ConversationType.FOLLOWYES, new NPCData.ConversationSet.Builder("npc.shop_owner.female.1.follow.yes", "Where do you want to go?"));
                    m.put(NPCData.ConversationType.FOLLOWNO, new NPCData.ConversationSet.Builder("npc.shop_owner.female.1.follow.no", "I have something to do right now. Maybe next time..."));
                    m.put(NPCData.ConversationType.FOLLOWSTOP, new NPCData.ConversationSet.Builder("npc.shop_owner.female.1.follow.stop", "See ya next time. Bye!"));
                }),
                new NPCData.NPCLook(NPCData.Gender.FEMALE, npcTexture("shop_owner/female_1"), null, 0, List.of(new NPCData.LookFeature(NPCData.StaticLookTypes.SLIM_MODEL))),
                of(m -> {
                    m.put(QuestGen.SHIP_TURNIP, new QuestResponseBuilder(
                            new NPCData.ConversationSet.Builder("npc.generic.quest.ship_turnip.start", "Are you here for my request?\n" +
                                    "I will show you how to ship items to make money: Shipping items is very simple. First you need a shipping bin. It can hold any shippable items in it.\n" +
                                    "Put the items you want to ship in it and everyday in the morning your items will be automatically shipped. Lets try it out now: I want you to ship a turnip."),
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
