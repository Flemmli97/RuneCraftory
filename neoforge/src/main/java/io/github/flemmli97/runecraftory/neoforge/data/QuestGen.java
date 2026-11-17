package io.github.flemmli97.runecraftory.neoforge.data;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.npc.ConversationSet;
import io.github.flemmli97.runecraftory.api.datapack.provider.AdditionalLanguages;
import io.github.flemmli97.runecraftory.common.lib.LibAdvancements;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.quests.QuestHandler;
import io.github.flemmli97.runecraftory.common.quests.quest.NPCQuestDefinition;
import io.github.flemmli97.runecraftory.common.quests.quest.QuestDialogueEntry;
import io.github.flemmli97.runecraftory.common.quests.quest.QuestResponses;
import io.github.flemmli97.runecraftory.common.quests.tasks.DeliveryTask;
import io.github.flemmli97.runecraftory.common.quests.tasks.NPCBasedTask;
import io.github.flemmli97.runecraftory.common.quests.tasks.NPCTalkTask;
import io.github.flemmli97.runecraftory.common.quests.tasks.ShippingTask;
import io.github.flemmli97.runecraftory.common.quests.tasks.TamingTask;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import io.github.flemmli97.simplequests_api.datapack.provider.QuestProvider;
import io.github.flemmli97.simplequests_api.impls.quests.TasksQuestDefinition;
import io.github.flemmli97.simplequests_api.impls.tasks.AdvancementTask;
import io.github.flemmli97.simplequests_api.impls.tasks.BlockInteractTask;
import io.github.flemmli97.simplequests_api.quest.QuestCategory;
import io.github.flemmli97.simplequests_api.quest.QuestHolder;
import io.github.flemmli97.simplequests_api.quest.entry.QuestTask;
import io.github.flemmli97.simplequests_api.util.DescriptiveValue;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class QuestGen extends QuestProvider implements AdditionalLanguages {

    private final QuestCategory main = new QuestCategory.Builder(QuestHandler.QUEST_CATEGORY, RuneCraftory.MODID + ".quests.category")
            .needContexts(QuestHandler.QUEST_CONTEXT)
            .build();

    private final Map<String, String> translations = new LinkedHashMap<>();
    public final Map<ResourceKey<LootTable>, LootTable.Builder> loot = new HashMap<>();
    public final Map<ResourceLocation, Pair<String, ConversationSet.Builder>> questResponses = new HashMap<>();

    public QuestGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup, false);
    }

    @Override
    protected void add(HolderLookup.Provider provider) {
        // Tutorial Quests
        ResourceLocation first = this.addNPCQuest(id("first_quest"), "Welcome Gift!", "",
                List.of(id("general_store_owner/1")),
                LootTable.lootTable().withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(RuneCraftoryItems.BROAD_SWORD.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                        .add(LootItem.lootTableItem(RuneCraftoryItems.HOE_SCRAP.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                        .add(LootItem.lootTableItem(RuneCraftoryItems.WATERING_CAN_SCRAP.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))),
                builder -> {
                    builder.push(new ConversationSet.Builder(builder.translationKeyOf("start"), """
                                    Ah, I see you've been at the quest board and picked up your first quest! How exciting!
                                    The quest board is a wonderful place to find work and help out those in need.
                                    You can accept quests there, and if you ever change your mind, you can cancel them too.
                                    Of course if nobody is nearby there won't be a quest either. No one wants to walk into the middle of nowhere to post a quest afterall.
                                    
                                    But enough about that! Here, I have a little gift for you to help you get started."""),
                            null, null);
                    builder.addEntry(new NPCTalkTask());
                }, b -> b.global().setRepeatDelay(-1).withCategory(this.main));
        ResourceLocation socialize = this.addNPCQuest(id("socialize"), "Socialize!", "",
                List.of(id("general_store_owner/1")),
                LootTable.lootTable().withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(RuneCraftoryItems.RECOVERY_POTION.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(5))))),
                builder -> {
                    builder.push(new ConversationSet.Builder(builder.translationKeyOf("start"), """
                                    Ah, hello there! Say, have you had a chance to meet some of the people around here?
                                    One of the nicest things about living in a village is getting to know its people. Everyone has their own stories, dreams, and experiences. There's always something interesting to discover if you take the time to listen!
                                    Why not go around and talk to few other villagers? It's a lovely way to start becoming part of the community!
                                    
                                    Hmm let's see... maybe talking to 5 other people is a good start"""),
                            null, null);
                    builder.addEntry(new NPCTalkTask());
                    builder.push(null,
                            new ConversationSet.Builder(builder.translationKeyOf("active"), """
                                    Still making your rounds, I see! Don't be shy, %player%.
                                    There's plenty of interesting people around here, and I'm sure they'd appreciate a friendly introduction.
                                    Take your time, there's no rush and go talk to a few more people!"""),
                            new ConversationSet.Builder(builder.translationKeyOf("end"), """
                                    Well, look at you! Making friends already, are we?
                                    Getting to know the people around you is just as important as any adventure.
                                    You never know when a familiar face might come in handy. Well done, %player%!"""));
                    builder.addEntry(new NPCTalkTask(NPCBasedTask.ANY_NPC));
                    builder.addEntry(new NPCTalkTask(NPCBasedTask.ANY_NPC));
                    builder.addEntry(new NPCTalkTask(NPCBasedTask.ANY_NPC));
                    builder.addEntry(new NPCTalkTask(NPCBasedTask.ANY_NPC));
                    builder.addEntry(new NPCTalkTask(NPCBasedTask.ANY_NPC));
                }, b -> b.addParent(first).global().setRepeatDelay(-1).withCategory(this.main));
        ResourceLocation gifts = this.addNPCQuest(id("gifts"), "Presents!", "",
                List.of(id("general_store_owner/1")),
                LootTable.lootTable().withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(RuneCraftoryItems.MOONDROP_FLOWER.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 5))))),
                builder -> {
                    builder.push(new ConversationSet.Builder(builder.translationKeyOf("start"), """
                                    Ah, hello there, %player%! You know, there's more to building a community than simply getting to know the people around you.
                                    Sometimes, a small act of kindness can make someone's day!
                                    Try approaching a villager and pressing %key:key.drop% to give them something from your inventory. I'm sure it'll put a smile on their face!
                                    
                                    For demonstration here... hmm... lets see... I got it!
                                    %quest.target_npc:0% was asking for some %item:runecraftory:formular_a%. Can you bring this to them? Would be much appreciated!
                                    And do take good care of it! If you happen to lose it, I'm afraid you'll have to find another one yourself. Hah!"""),
                            null, null);
                    builder.addEntry(new NPCTalkTask(null, null, this.withLoot(id("quest_gifts_formular_a"), LootTable.lootTable().withPool(LootPool.lootPool()
                            .add(LootItem.lootTableItem(RuneCraftoryItems.FORMULAR_A.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))))));
                    builder.push(null,
                            new ConversationSet.Builder(builder.translationKeyOf("active"), """
                                    %quest.target_npc:0% is still asking for the %item:runecraftory:formular_a%.
                                    Remember: To give someone an item simply walk up to them and drop the item on them."""),
                            new ConversationSet.Builder(builder.translationKeyOf("end"), """
                                    Great! That's how you gift someone an items. Pretty simply and straigforward isn't that right?
                                    Everyone loves some gifts, though some people do like some things more than others.
                                    And here is another gift from me."""));
                    builder.addEntry("Deliver Task", desc -> new DeliveryTask(id("florist/1"),
                            builder.getConversation("deliver_task", new ConversationSet.Builder(builder.translationKeyOf("deliver_task"), """
                                    Huh? What's this...
                                    Oh yeah that's the %item:runecraftory:formular_a% I asked for from %quest_giver%.
                                    How come you are bringing this to me?
                                    ...
                                    Hmm I see. Well anyway please say thanks to %quest_giver% that I appreciate this. I needed this.""")),
                            DescriptiveValue.list(ItemPredicate.Builder.item().of(RuneCraftoryItems.FORMULAR_A.get()).build(), desc).build(), ConstantValue.exactly(1)));
                }, b -> b.addParent(socialize).global().setRepeatDelay(-1).withCategory(this.main));
        ResourceLocation shipItem = this.addNPCQuest(id("ship_turnip"), "First Shipment!", "",
                List.of(id("general_store_owner/1")),
                LootTable.lootTable().withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(RuneCraftoryItems.TURNIP_SEEDS.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 8))))),
                builder -> {
                    builder.push(new ConversationSet.Builder(builder.translationKeyOf("start"), """
                                    Oh, hello %player%! Have you ever wondered how to make a little money around here?
                                    Well, there are plenty of ways, but one of the simplest is selling the things you grow!
                                    You see, most villagers use a shipping bin to sell their goods. Simply place something you want to sell inside, and it'll be collected the following morning.
                                    
                                    Why not give it a try? I'll give you this turnip so go ahead and and put it in the shipping bin.
                                    Oh, and remember, once you've placed something inside, you'll have to wait until the next morning for it to be collected.
                                    
                                    How to get a shipping bin you ask? They seem to be fairly simple to craft."""),
                            null, null);
                    builder.addEntry(new NPCTalkTask(null, null, this.withLoot(id("quest_ship_turnip_turnip"), LootTable.lootTable().withPool(LootPool.lootPool()
                            .add(LootItem.lootTableItem(RuneCraftoryItems.TURNIP.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))))));
                    builder.push(null,
                            new ConversationSet.Builder(builder.translationKeyOf("active"), """
                                    Did you go ahead and shipped the turnip yet, %player%?
                                    It's a simple way to earn some extra money from all the things you grow. Just place a turnip in a shipping bin and it'll be collected the following morning.
                                    
                                    Go on, give it a try!"""),
                            new ConversationSet.Builder(builder.translationKeyOf("end"), """
                                    Well, look at that! You've shipped your first crop! Excellent work, %player%!
                                    You'll find that selling your harvest is a reliable way to earn some extra money. Just keep filling that shipping bin with things you grow or collect, and you'll have a nice little income before long.
                                    
                                    And remember, the bin gets emptied every morning, so don't worry if your money doesn't arrive immediately."""));
                    builder.addEntry("Ship %s turnips", desc -> new ShippingTask("", DescriptiveValue.list(ItemPredicate.Builder.item().of(RuneCraftoryItems.TURNIP.get()).build(), desc).build(), ConstantValue.exactly(1)));
                }, b -> b.addParent(socialize).global().setRepeatDelay(-1).withCategory(this.main));
        ResourceLocation buyItem = this.addNPCQuest(id("purchase_item"), "Shopping!", "",
                List.of(id("merchant/1")),
                LootTable.lootTable().withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(RuneCraftoryItems.TURNIP_SEEDS.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(12))))),
                builder -> {
                    builder.push(new ConversationSet.Builder(builder.translationKeyOf("start"), """
                                    Ah, %player%! Just the person I wanted to see! I've got a little business advice for you today.
                                    You see, a good adventurer needs more than a sharp sword and sturdy boots. Knowing where to find the right supplies is just as important!
                                    Have you visited the General Store yet? It's a wonderful place to pick up all sorts of everyday necessities.
                                    
                                    I could use a little <aqua>rice</aqua> myself. Why don't you for this task head over there and buy me some? Consider it your first lesson in the art of shopping!"""),
                            null, null);
                    builder.addEntry(new NPCTalkTask());
                    builder.push(null,
                            new ConversationSet.Builder(builder.translationKeyOf("active"), """
                                    Back already? Excellent! Did you manage to find the General Store? Go on, take a look around and buy some <aqua>rice</aqua>.
                                    It's a simple purchase, but every great merchant knows that even the smallest transactions keep the wheels of commerce turning!"""),
                            new ConversationSet.Builder(builder.translationKeyOf("end"), """
                                    I see you've got the rice! Well done, %player%! You've just completed your first proper shopping trip.
                                    Remember, friend, the General Store is your go-to place for everyday supplies. Basic food, seeds and a few tools can be found there too.
                                    Knowing where to shop can save you a great deal of trouble on your travels.
                                    
                                    Well here is a bit of a reward for saving me the trouble of going myself."""));
                    builder.addEntry(new DeliveryTask(DescriptiveValue.list(ItemPredicate.Builder.item().of(RuneCraftoryItems.RICE.get()).build()).build(), ConstantValue.exactly(1)));
                }, b -> b.addParent(shipItem).global().setRepeatDelay(-1).withCategory(this.main));
        ResourceLocation harvestCrop = this.addNPCQuest(id("harvest_1"), "Growing flowers", "",
                List.of(id("florist/1")),
                LootTable.lootTable().withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(RuneCraftoryItems.TOYHERB_SEEDS.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(12))))),
                builder -> {
                    builder.push(new ConversationSet.Builder(builder.translationKeyOf("start"), """
                                    Are you here for the quest %player%? Isn't it a lovely day? The sunshine, the fresh air, all the little flowers blooming... I just love it!
                                    You know, one of the most wonderful things about living here is being able to grow things yourself. There's something magical about planting a tiny seed and watching it slowly turn into something beautiful!
                                    
                                    If you've already tried growing crops, you'll be happy to know that flowers work exactly the same way!
                                    Prepare a little patch of soil, plant some seeds, and remember to water them every day. With a bit of patience and care, you'll soon have something lovely growing!
                                    
                                    Actually, I'd love to see what you can grow! How about bringing me <aqua>4 %item:runecraftory:toyherb%</aqua? I'd be absolutely delighted!"""),
                            null, null);
                    builder.addEntry(new NPCTalkTask());
                    builder.push(null,
                            new ConversationSet.Builder(builder.translationKeyOf("active"), """
                                    Hiii, %player%! How's your little garden coming along?
                                    Remember, flowers need a little water and care every day, just like crops! I'm sure they'll turn out beautifully.
                                    I'm still looking forward to those %quest.progress:0%! I can't wait to see what you've grown!"""),
                            new ConversationSet.Builder(builder.translationKeyOf("end"), """
                                    Oh my goodness! Look at these! They're beautiful, %player%!
                                    Thank you so much! See? Growing things yourself is such a lovely feeling, isn't it?
                                    With a little patience, some care, and a bit of sunshine, you can turn a tiny seed into something wonderful!
                                    I hope this is just the beginning of your gardening adventures. There's always room for a few more flowers in the world!"""));
                    builder.addEntry(new DeliveryTask(null, builder.getConversation("toyherb_progress", new ConversationSet.Builder(builder.translationKeyOf("toyherb_progress"), "Wow what beautiful flowers! I want %quest.progress:0% more of them.")),
                            DescriptiveValue.list(ItemPredicate.Builder.item().of(RuneCraftoryItems.TOYHERB.get()).build()).build(), ConstantValue.exactly(4)));
                }, b -> b.addParent(shipItem).global().setRepeatDelay(-1).withCategory(this.main));
        ResourceLocation fertilizer = this.addNPCQuest(id("fertilizer"), "Improve farmland", "",
                List.of(id("florist/1")),
                LootTable.lootTable().withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(RuneCraftoryItems.FORMULAR_A.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(3))))),
                builder -> {
                    builder.push(new ConversationSet.Builder(builder.translationKeyOf("start"), """
                            Oh, hello, %player%! I've got something lovely to show you today!
                            Have you ever tried using fertilizer in your crops?
                            No -> You haven't? You really should try them out. Fertilizer can really boost your farming experience you know?
                            Yes -> I see. Then you might already know about various benefits of using them right? Thats great!
                            
                            Here, take this! I've got a piece of %item:runecraftory:formular_a% you can try out.
                            This one increases the rate at which your crops grow.
                            Go on, give it a try! I'd love to hear what you think!"""), null, null);
                    builder.addEntry(new NPCTalkTask(null, null, this.withLoot(id("fertilizer_formular_a"), LootTable.lootTable().withPool(LootPool.lootPool()
                            .add(LootItem.lootTableItem(RuneCraftoryItems.FORMULAR_A.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))))));
                    builder.push(null,
                            new ConversationSet.Builder(builder.translationKeyOf("active"), """
                                    Hehe, have you tried the %item:runecraftory:formular_a% yet, %player%?
                                    Remember, you just need to apply it to some prepared farmland. Your plants will thank you for it!
                                    Give it a try when you get the chance!"""),
                            new ConversationSet.Builder(builder.translationKeyOf("end"), """
                                    Yay! You did it, %player%!
                                    See? A little fertilizer can go a long way! There are all sorts of fertilizers out there, each with its own benefits.
                                    Some help plants grow faster, while others can improve their size or quality.
                                    If you want to obtain more I sell some in my shop. Or you can also craft them yourself if you get a %item:runecraftory:chemistry%"""));
                    builder.addEntry("Use Fertilizer", d -> new BlockInteractTask(DescriptiveValue.list(BlockPredicate.Builder.block().of(RunecraftoryTags.Blocks.FARMLAND).build()).build(), DescriptiveValue.list(ItemPredicate.Builder.item().of(RuneCraftoryItems.FORMULAR_A.get()).build()).build(),
                            ConstantValue.exactly(1), false, false, false, d, null));
                }, b -> b.addParent(harvestCrop).global().setRepeatDelay(-1).withCategory(this.main));

        ResourceLocation mining = this.addNPCQuest(id("mining"), "Acquire Hardware??", "",
                List.of(id("blacksmith/1")),
                LootTable.lootTable().withPool(LootPool.lootPool()
                                .add(LootItem.lootTableItem(RuneCraftoryItems.HAMMER_SCRAP.get())))
                        .withPool(LootPool.lootPool()
                                .add(LootItem.lootTableItem(Items.RAW_IRON).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))))
                                .add(LootItem.lootTableItem(Items.RAW_COPPER).apply(SetItemCountFunction.setCount(UniformGenerator.between(6, 10))))),
                builder -> {
                    builder.push(new ConversationSet.Builder(builder.translationKeyOf("start"), """
                            There you are, %player%! Tell me, have you ever tried your hand at mining?
                            There's plenty of valuable stuff hidden throughout the world. Every now and then, you'll come across mineral deposits sticking out of the ground or walls. Break them open, and you might find some useful ores inside!
                            Your pickaxe should do the job just fine, though it'll take a bit of work.
                            
                            Go find and break 10 mineral deposits. Bring out that pickaxe and get to work! Hah!"""), null, null);
                    builder.addEntry(new NPCTalkTask());
                    builder.push(null,
                            new ConversationSet.Builder(builder.translationKeyOf("active"), """
                                    Still haven't finished breaking those minerals huh, %player%?
                                    Keep your eyes open while exploring. Mineral deposits can be found scattered throughout the world, and you never know what useful ores you might find inside.
                                    
                                    Ten deposits, remember! Shouldn't be too much trouble for someone with a pickaxe."""),
                            new ConversationSet.Builder(builder.translationKeyOf("end"), """
                                    Now that's what I like to see!
                                    You've got the basics down, but let me give you a little advice.
                                    You might have noticed that it was quite hard to break those minerals with your pick right?
                                    Well that is because if you really want to get the job done efficiently, you'll want a hammer.
                                    Where do you get a hammer? Well aren't you in luck today! Here, take this one.
                                    While its a bit shabby it's still a proper mining tool, built for breaking those mineral deposits. Give it a swing next time you're out there, and you'll see what I mean!"""));
                    builder.addEntry("Break %s mineral blocks", desc -> new BlockInteractTask(DescriptiveValue.list(BlockPredicate.Builder.block().of(RunecraftoryTags.Blocks.ORES).build(), desc).build(),
                            List.of(), ConstantValue.exactly(10), false, false, true, "", null));
                }, b -> b.addParent(socialize).global().setRepeatDelay(-1).withCategory(this.main));
        ResourceLocation monsterBarn = this.addNPCQuest(id("monster_barn"), "Monster housing", "",
                List.of(id("blacksmith/1")),
                LootTable.lootTable().withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(Items.HAY_BLOCK).apply(SetItemCountFunction.setCount(ConstantValue.exactly(32))))),
                builder -> {
                    builder.push(new ConversationSet.Builder(builder.translationKeyOf("start"), """
                            Hey! Got a minute, %player%? I've been thinking about something that might interest you.
                            Did you know you don't need to always fight every creature you come across? Monsters can actually be tamed! With a little patience, you can turn a wild beast into a useful companion.
                            Of course, you'll need somewhere to keep them. That's where a %item:runecraftory:monster_barn% comes in! It's a special building where you can house and look after your tamed monsters.
                            Why not build yourself a Monster Barn and get it set up? It'll take a bit of work, but having a few monsters around could prove mighty useful!
                            A %item:runecraftory:monster_barn% requires at least a 5x5 size cosy ground of %item:minecraft:hay_block% centered around the barn
                            
                            To get a %item:runecraftory:monster_barn% you can buy one from your local blacksmith. That is me!"""), null, null);
                    builder.addEntry(new NPCTalkTask());
                    builder.push(null,
                            new ConversationSet.Builder(builder.translationKeyOf("active"), """
                                    How's that %item:runecraftory:monster_barn% coming along, %player%?
                                    Remember, tamed monsters need somewhere safe to stay. A %item:runecraftory:monster_barn% will give them a proper home and keep them close by.
                                    Get that building set up, and you'll be ready to start raising some monsters of your own!"""),
                            new ConversationSet.Builder(builder.translationKeyOf("end"), """
                                    Well, look at that! A %item:runecraftory:monster_barn% of your very own! Not bad at all, %player%!
                                    Now you've got somewhere to keep all those creatures you manage to tame. Just remember, looking after monsters is a responsibility. They'll need proper care, just like any other livestock.
                                    Who knows? Before long, you might have quite a collection of beasts living there!
                                    
                                    Just don't let them chew on your tools. I've had enough trouble repairing things as it is! Hah!"""));
                    builder.addEntry(new AdvancementTask(DescriptiveValue.list(LibAdvancements.MONSTER_BARN).build(), false, "", null));
                }, b -> b.addParent(mining).global().setRepeatDelay(-1).withCategory(this.main));
        ResourceLocation taming = this.addNPCQuest(id("tame_monster"), "Tame a monster", "",
                List.of(id("blacksmith/1")),
                LootTable.lootTable().withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(RuneCraftoryItems.BRUSH.get()))),
                builder -> {
                    builder.push(new ConversationSet.Builder(builder.translationKeyOf("start"), """
                            Good day to you %player%. I see you've got yourself a Monster Barn now? Maybe even made your first monster companion?
                            If not I bet you want to know to to befriend one right?
                            Let me tell you then: Most monsters can be won over by offering them items to gain their affection. Every species has its own preferences, so you'll have to experiment a little to figure out what they enjoy. 
                            Give them something they like, and you'll have a much better chance of earning their trust!
                            I heard some of the more powerful creatures are a different matter entirely though. They're rather picky about what they accept, and will only consider their absolute favorite item. So don't expect to win them over with just anything!
                            For now, let's start with something a little more manageable. Find a monster, offer it something. It might start to like you that way.
                            
                            Oh and one more note: Of course your %item:runecraftory:monster_barn% needs enough space to house your companion."""), null, null);
                    builder.addEntry(new NPCTalkTask());
                    builder.push(null,
                            new ConversationSet.Builder(builder.translationKeyOf("active"), """
                                    No luck taming a monster yet huh, %player%?
                                    Remember, monsters have their own tastes. Try offering them different items and see how they react. Finding something they particularly enjoy will greatly improve your chances!
                                    Keep at it! I'm sure you'll find yourself a companion soon enough."""),
                            new ConversationSet.Builder(builder.translationKeyOf("end"), """
                                    Hah! You actually did it! You've managed to tame a monster!
                                    Well done, %player%! Now you've got yourself a proper companion for your %item:runecraftory:monster_barn%.
                                    Just remember what you've learned. Different monsters prefer different things, so keep experimenting with gifts if you want to tame more of them.
                                    Take good care of your new friend! A little trust goes a long way, whether you're dealing with people or monsters.
                                    
                                    Here take this %item:runecraftory:brush% from me. It should prove handy in raising the affection of your companions."""));
                    builder.addEntry("Tame a monster", desc -> new TamingTask("", DescriptiveValue.list(EntityPredicate.Builder.entity().build(), desc).build(), ConstantValue.exactly(1)));
                }, b -> b.addParent(monsterBarn).global().setRepeatDelay(-1).withCategory(this.main));
        ResourceLocation taming5 = this.addNPCQuest(id("tame_monster_5"), "Tame more monster", "",
                List.of(id("blacksmith/1")),
                LootTable.lootTable().withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(RuneCraftoryItems.MONSTER_BARN.get()))),
                builder -> {
                    builder.push(new ConversationSet.Builder(builder.translationKeyOf("start"), """
                            Well, look who's getting the hang of things! Seems you've developed quite a knack for handling monsters, %player%!
                            How about taking things a step further? I'd like to see you build up a proper little collection of companions!
                            Try taming five monsters this time. Now go and start befriending them!"""), null, null);
                    builder.addEntry(new NPCTalkTask());
                    builder.push(null,
                            new ConversationSet.Builder(builder.translationKeyOf("active"), """
                                    Hah! How's your little monster collection coming along, %player%?
                                    Five monsters, remember! There's no shortage of different creatures out there, so get exploring and see what you can find."""),
                            new ConversationSet.Builder(builder.translationKeyOf("end"), """
                                    Wow! Now that's what I call a collection!
                                    You've certainly come a long way, %player%. Not everyone has the patience to earn the trust of that many creatures.
                                    Just make sure you've got enough room in that %item:runecraftory:monster_barn%. Otherwise you won't be able to make more friends."""));
                    builder.addEntry("Tame 5 monster", desc -> new TamingTask("", DescriptiveValue.list(EntityPredicate.Builder.entity().build(), desc).build(), ConstantValue.exactly(5)));
                }, b -> b.addParent(monsterBarn).global().setRepeatDelay(-1).withCategory(this.main));
        ResourceLocation tamingBoss = this.addNPCQuest(id("tame_monster_boss"), "Tame a boss", "",
                List.of(id("blacksmith/1")),
                LootTable.lootTable().withPool(LootPool.lootPool()
                                .add(LootItem.lootTableItem(RuneCraftoryItems.MONSTER_BARN.get())))
                        .withPool(LootPool.lootPool()
                                .add(LootItem.lootTableItem(Items.HAY_BLOCK).apply(SetItemCountFunction.setCount(ConstantValue.exactly(32)))))
                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(2))
                                .add(LootItem.lootTableItem(Items.OAK_LOG).apply(SetItemCountFunction.setCount(ConstantValue.exactly(16))))
                                .add(LootItem.lootTableItem(Items.BIRCH_LOG).apply(SetItemCountFunction.setCount(ConstantValue.exactly(16))))
                                .add(LootItem.lootTableItem(Items.SPRUCE_LOG).apply(SetItemCountFunction.setCount(ConstantValue.exactly(16))))
                                .add(LootItem.lootTableItem(Items.JUNGLE_LOG).apply(SetItemCountFunction.setCount(ConstantValue.exactly(16))))
                                .add(LootItem.lootTableItem(Items.DARK_OAK_LOG).apply(SetItemCountFunction.setCount(ConstantValue.exactly(16))))
                                .add(LootItem.lootTableItem(Items.ACACIA_LOG).apply(SetItemCountFunction.setCount(ConstantValue.exactly(16))))),
                builder -> {
                    builder.push(new ConversationSet.Builder(builder.translationKeyOf("start"), """
                            Hah! Hello %player%, I've got a challenge for you. And this time, I mean a proper challenge!
                            You've already proven you can handle ordinary monsters, but there's a whole different class of creatures out there. 
                            Powerful beasts that make even experienced adventurers think twice before approaching them! You might have come across some already.
                            Now, here's something I've learned over the years: even these mighty creatures can be tamed!
                            But don't expect them to accept just any old gift. They're incredibly picky and will only accept their absolute favorite item.
                            
                            For this challenge I want you to tame one of those monsters.
                            Just between you and me: I've heard that %entity:runecraftory:ambrosia% has quite a liking for %item:runecraftory:ultra_toyherb%. Might be worth keeping that in mind!"""), null, null);
                    builder.addEntry(new NPCTalkTask());
                    builder.push(null,
                            new ConversationSet.Builder(builder.translationKeyOf("active"), """
                                    Hello, how's that hunt going, %player%? Found yourself a worthy challenge yet?
                                    Remember, these powerful creatures won't be impressed by ordinary gifts. You'll need to find their absolute favorite item if you want to earn their trust."""),
                            new ConversationSet.Builder(builder.translationKeyOf("end"), """
                                    Wow amazing! You actually did it?! Now that's something!
                                    You've managed to tame one of the mightiest creatures out there! I must admit, I'm impressed, %player%. It takes more than a strong arm to earn the trust of a beast like that.
                                    Well, I reckon you've earned yourself a reputation as quite the monster tamer."""));
                    builder.addEntry("Tame a boss", desc -> new TamingTask("Tame a boss", DescriptiveValue.list(EntityPredicate.Builder.entity().of(RunecraftoryTags.EntityTypes.BOSS_MONSTERS).build(), desc).build(), ConstantValue.exactly(1)));
                }, b -> b.addParent(monsterBarn).global().setRepeatDelay(-1).withCategory(this.main));

        // Make and craft from forge, cooking, chem, armor
        // upgrade weapon, upgrade armor, change weapon element, upgrade staff

        // Fishing quest
        // Harvest specific crop (advance shop seeds), fruit tree
        // skill level quest
        // heal farmland (till corn, etc.)

        // Repeatable quests
        // Need better state
        // ResourceLocation kill_monster = this.addNPCQuest(id("kill_monster"), "", "",
        //         List.of(any),
        //         LootTable.lootTable(),
        //         builder -> {
        //             builder.push(new ConversationSet.Builder(builder.translationKeyOf("start"), """
        //                     Got a little task for you, %player%! Could you take care of %quest.amount% %quest.target%? They've been causing a bit of trouble lately.

        //                     Think you can handle it?"""), null, null);
        //             builder.addEntry(new NPCTalkTask());
        //             builder.push(null,
        //                     new ConversationSet.Builder(builder.translationKeyOf("active"), """
        //                             Still got those %quest.target% to deal with, %player%? Don't forget about the task!"""),
        //                     new ConversationSet.Builder(builder.translationKeyOf("end"), """
        //                             Good work, %player%! Those %quest.target% won't be causing any more trouble. Here's your reward!"""));
        //             builder.addEntry("", desc -> new TamingTask("", DescriptiveValue.list(EntityPredicate.Builder.entity().build(), desc).build(), ConstantValue.exactly(1)));
        //         }, b -> b.addParent(monsterBarn).global().setRepeatDelay(72000).withCategory(this.main));
        // ResourceLocation ship_item = this.addNPCQuest(id("ship_item"), "", "",
        //         List.of(any),
        //         LootTable.lootTable(),
        //         builder -> {
        //             builder.push(new ConversationSet.Builder(builder.translationKeyOf("start"), """
        //                     Hey, %player%! Could you help me out with something? I'd like you to ship %quest.amount% %quest.target%. Think you can take care of that?"""), null, null);
        //             builder.addEntry(new NPCTalkTask());
        //             builder.push(null,
        //                     new ConversationSet.Builder(builder.translationKeyOf("active"), """
        //                             How's that shipping task coming along, %player%? Don't forget to put those %quest.target% in the shipping bin!"""),
        //                     new ConversationSet.Builder(builder.translationKeyOf("end"), """
        //                             Well done, %player%! You've shipped all %quest.amount% %quest.target%. Thanks for the help! Here's your reward."""));
        //             builder.addEntry("", desc -> new TamingTask("", DescriptiveValue.list(EntityPredicate.Builder.entity().build(), desc).build(), ConstantValue.exactly(1)));
        //         }, b -> b.addParent(monsterBarn).global().setRepeatDelay(72000).withCategory(this.main));
        // ResourceLocation deliver_item = this.addNPCQuest(id("deliver_item"), "", "",
        //         List.of(any),
        //         LootTable.lootTable(),
        //         builder -> {
        //             builder.push(new ConversationSet.Builder(builder.translationKeyOf("start"), """
        //                     Hey, %player%! Could you do me a favor? I could really use %quest.amount% %quest.target_item%. Would you mind bringing me some?"""), null, null);
        //             builder.addEntry(new NPCTalkTask());
        //             builder.push(null,
        //                     new ConversationSet.Builder(builder.translationKeyOf("active"), """
        //                             Found those %quest.target_item% yet, %player%? I'd really appreciate it if you could bring me %quest.amount%!"""),
        //                     new ConversationSet.Builder(builder.translationKeyOf("end"), """
        //                             Ah, just what I needed! Thanks for bringing me %quest.amount% %quest.target_item%, %player%! Here's a little something for your trouble!"""));
        //             builder.addEntry("", desc -> new TamingTask("", DescriptiveValue.list(EntityPredicate.Builder.entity().build(), desc).build(), ConstantValue.exactly(1)));
        //         }, b -> b.addParent(monsterBarn).global().setRepeatDelay(72000).withCategory(this.main));

//            this.addNPCQuest(id("test_quest"), "Test Quest", "A test quest",
//                List.of(id("random_npc_1")),
//                LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.STONE))),
//                builder -> {
//                    builder.push(new ConversationSet.Builder(builder.translationKeyOf("start"), """
//                            Start dialogue. Now go tame a monster"""), null, null);
//                    builder.addEntry(new NPCTalkTask());
//                    // Generic task test
//                    builder.push(null,
//                            new ConversationSet.Builder(builder.translationKeyOf("active"), "Active dialogue tame task. Tame a monster"),
//                            new ConversationSet.Builder(builder.translationKeyOf("end"), "End dialogue tame task"));
//                    builder.addEntry("Tame a monster", desc -> new TamingTask("", DescriptiveValue.list(EntityPredicate.Builder.entity().build(), desc).build(), ConstantValue.exactly(1)));
//                    // Test talking to other npc
//                    builder.push(new ConversationSet.Builder(builder.translationKeyOf("start_1"), """
//                                    Talk to %quest.target_npc:0%"""),
//                            new ConversationSet.Builder(builder.translationKeyOf("active_1"), "Active dialogue talk task. Talk to %quest.target_npc:0%"),
//                            new ConversationSet.Builder(builder.translationKeyOf("end_1"), "End dialogue talk task. Quest done"));
//                    builder.addEntry(new NPCTalkTask(id("random_npc_1"), builder.getConversation("talk_random_npc", new ConversationSet.Builder(builder.translationKeyOf("talk_random_npc"), "I am responding to a talk task"))));
//
//                    // Deliver item test. To self and other npc
//                    builder.push(new ConversationSet.Builder(builder.translationKeyOf("start_2"), """
//                                    Bring me %quest.progress:0%"""),
//                            new ConversationSet.Builder(builder.translationKeyOf("active_2"), "Active dialogue deliver self task. You still need %quest.progress:0%"),
//                            new ConversationSet.Builder(builder.translationKeyOf("end_2"), "End dialogue deliver self task"));
//                    builder.addEntry(new DeliveryTask(List.of(DescriptiveValue.of(ItemPredicate.Builder.item().of(Items.APPLE).build(), builder.defineTranslation("apples", "Apples"))), ConstantValue.exactly(5)));
//                    builder.push(new ConversationSet.Builder(builder.translationKeyOf("start_3"), """
//                                    Now bring %quest.target_npc:0% 5 apples"""),
//                            new ConversationSet.Builder(builder.translationKeyOf("active_3"), "Active dialogue deliver other task. Bring %quest.progress:0% to %quest.target_npc:0%"),
//                            new ConversationSet.Builder(builder.translationKeyOf("end_3"), "End dialogue deliver other task. Quest done"));
//                    builder.addEntry(new DeliveryTask(id("random_npc_1"),
//                            builder.getConversation("deliver_random_npc", new ConversationSet.Builder(builder.translationKeyOf("deliver_random_npc"), "Thanks for the apples")),
//                            List.of(DescriptiveValue.of(ItemPredicate.Builder.item().of(Items.APPLE).build(), builder.defineTranslation("apples", "Apples"))),
//                            ConstantValue.exactly(5)));
//                }, b -> b.setRepeatDelay(-1).withCategory(this.main));
    }

    private ResourceLocation addNPCQuest(ResourceLocation id, String task, String description,
                                         List<ResourceLocation> npcs, LootTable.Builder loot,
                                         Consumer<SubQuestBuilder> tasks, Consumer<NPCQuestDefinition.Builder> builderConsumer) {
        NPCQuestDefinition.Builder builder = new NPCQuestDefinition.Builder(getTask(id), npcs, id);
        this.translations.put(getTask(id), task);
        this.translations.put(getDescription(id), description);
        this.loot.put(ResourceKey.create(Registries.LOOT_TABLE, id), loot);
        SubQuestBuilder subQuests = new SubQuestBuilder(id);
        tasks.accept(subQuests);
        for (SubQuestEntry subQuest : subQuests.entries) {
            TasksQuestDefinition.Builder taskQuest = new TasksQuestDefinition.Builder("", BuiltInLootTables.EMPTY.location());
            subQuest.tasks.forEach(taskQuest::addTaskEntry);
            builder.withQuests(new QuestDialogueEntry(new QuestHolder(taskQuest.build()),
                    new QuestResponses(Optional.ofNullable(subQuest.start), Optional.ofNullable(subQuest.active), Optional.ofNullable(subQuest.end))));
        }
        builderConsumer.accept(builder);
        this.addQuest(id, builder);
        return id;
    }

    private ResourceLocation withLoot(ResourceLocation id, LootTable.Builder loot) {
        this.loot.put(ResourceKey.create(Registries.LOOT_TABLE, id), loot);
        return id;
    }

    public static String getTask(ResourceLocation id) {
        return String.format("%s.quest.%s", id.getNamespace(), id.getPath());
    }

    public static String getDescription(ResourceLocation id) {
        return String.format("%s.quest.%s.description", id.getNamespace(), id.getPath());
    }

    public static String getTaskDescription(ResourceLocation id, int sub, int task) {
        return String.format("%s.quest.%s.sub.%s.task.%s", id.getNamespace(), id.getPath(), sub, task);
    }

    public static String getComponent(ResourceLocation id, int sub, String key) {
        return String.format("%s.quest.%s.sub.%s.task.%s", id.getNamespace(), id.getPath(), sub, key);
    }

    private static ResourceLocation id(String name) {
        return RuneCraftory.modRes(name);
    }

    @Override
    public Map<String, String> translations() {
        return this.translations;
    }

    private class SubQuestBuilder {

        private final List<SubQuestEntry> entries = new ArrayList<>();
        private final ResourceLocation id;

        private SubQuestBuilder(ResourceLocation id) {
            this.id = id;
        }

        public String translationKeyOf(String key) {
            return String.format("quest.conversation.%s.%s.%s", this.id, this.entries.size(), key);
        }

        public String defineTranslation(String key, String translation) {
            key = getComponent(this.id, this.entries.size() - 1, key);
            QuestGen.this.translations.put(key, translation);
            return key;
        }

        private String translationPath() {
            return "quests/" + this.id.getPath();
        }

        public ResourceLocation getConversation(String idSuffix, ConversationSet.Builder conv) {
            ResourceLocation id = this.id.withPath(p -> "quest/" + p + "/" + idSuffix);
            if (QuestGen.this.questResponses.put(id, Pair.of(this.translationPath(), conv)) != null) {
                throw new IllegalStateException("Conversation exists already with id " + id);
            }
            return id;
        }

        void addEntry(QuestTask<?> entry) {
            this.addEntry("", _n -> entry);
        }

        void addEntry(String translation, Function<String, QuestTask<?>> entry) {
            if (this.entries.isEmpty()) {
                throw new IllegalStateException("You need to push first");
            }
            int subIdx = this.entries.size() - 1;
            SubQuestEntry current = this.entries.get(subIdx);
            int taskIdx = current.tasks().size();
            String desc = getTaskDescription(this.id, subIdx, taskIdx);
            current.tasks().put(taskIdx + "", entry.apply(desc));
            if (!translation.isEmpty()) {
                QuestGen.this.translations.put(desc, translation);
            }
        }

        void push(ConversationSet.Builder start, ConversationSet.Builder active, ConversationSet.Builder end) {
            int idx = this.entries.size();
            ResourceLocation startID = null;
            if (start != null) {
                startID = this.id.withPath(p -> "quest/" + p + "/start_" + idx);
                QuestGen.this.questResponses.put(startID, Pair.of(this.translationPath(), start));
            }
            ResourceLocation activeID = null;
            if (active != null) {
                activeID = this.id.withPath(p -> "quest/" + p + "/active_" + idx);
                QuestGen.this.questResponses.put(activeID, Pair.of(this.translationPath(), active));
            }
            ResourceLocation endID = null;
            if (end != null) {
                endID = this.id.withPath(p -> "quest/" + p + "/end_" + idx);
                QuestGen.this.questResponses.put(endID, Pair.of(this.translationPath(), end));
            }
            this.entries.add(new SubQuestEntry(startID, activeID, endID, new HashMap<>()));
        }
    }

    private record SubQuestEntry(ResourceLocation start, ResourceLocation active, ResourceLocation end,
                                 Map<String, QuestTask<?>> tasks) {
    }

}
