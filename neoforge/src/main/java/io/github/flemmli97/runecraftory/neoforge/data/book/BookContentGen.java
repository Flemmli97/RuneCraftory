package io.github.flemmli97.runecraftory.neoforge.data.book;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryBlocks;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.favouriteless.modopedia.api.datagen.BookContentOutput;
import net.favouriteless.modopedia.api.datagen.builders.CategoryBuilder;
import net.favouriteless.modopedia.api.datagen.builders.EntryBuilder;
import net.favouriteless.modopedia.api.datagen.builders.PageBuilder;
import net.favouriteless.modopedia.api.datagen.builders.page_components.components.HeaderBuilder;
import net.favouriteless.modopedia.api.datagen.builders.page_components.components.ImageBuilder;
import net.favouriteless.modopedia.api.datagen.builders.page_components.components.ItemGalleryBuilder;
import net.favouriteless.modopedia.api.datagen.builders.page_components.components.SeparatorBuilder;
import net.favouriteless.modopedia.api.datagen.builders.page_components.components.ShowcaseBuilder;
import net.favouriteless.modopedia.api.datagen.builders.page_components.components.TextBuilder;
import net.favouriteless.modopedia.api.datagen.builders.templates.FramedItemGalleryBuilder;
import net.favouriteless.modopedia.api.datagen.builders.templates.page.CraftingPageBuilder;
import net.favouriteless.modopedia.api.datagen.builders.templates.page.DoubleCraftingPageBuilder;
import net.favouriteless.modopedia.api.datagen.builders.templates.page.HeaderedTextBuilder;
import net.favouriteless.modopedia.api.datagen.builders.templates.recipes.CraftingRecipeBuilder;
import net.favouriteless.modopedia.api.datagen.providers.ContentSetProvider;
import net.favouriteless.modopedia.client.page_components.item_displays.GridItemDisplay;
import net.favouriteless.modopedia.client.page_components.item_displays.SimpleItemDisplay;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class BookContentGen extends ContentSetProvider {

    private final Map<String, String> translations = new HashMap<>();

    public BookContentGen(CompletableFuture<HolderLookup.Provider> registries, PackOutput output) {
        super(RuneCraftory.MODID, "runepedia", "en_us", registries, output);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        this.createTranslations();
        return super.run(output);
    }

    @Override
    public void buildEntries(HolderLookup.Provider registries, BookContentOutput output) {
        EntryBuilder.of(this.get("runecraftory.book.entry.runepoints"))
                .icon(new ItemStack(RuneCraftoryItems.MYSTERY_POTION.get()))
                .page(HeaderedTextBuilder.of(this.get("runecraftory.book.entry.runepoints"), this.get("runecraftory.book.entry.runepoints.1")))
                .page(TextBuilder.of(this.get("runecraftory.book.entry.runepoints.2")))
                .build("entry_runepoints", output, "category_main");
        EntryBuilder.of(this.get("runecraftory.book.entry.calendar"))
                .icon(new ItemStack(RuneCraftoryItems.RED_GRASS.get()))
                .page(HeaderedTextBuilder.of(this.get("runecraftory.book.entry.calendar"), this.get("runecraftory.book.entry.calendar.1")))
                .page(TextBuilder.of(this.get("runecraftory.book.entry.calendar.2")))
                .build("entry_calendar", output, "category_main");
        EntryBuilder.of(this.get("runecraftory.book.entry.crafting"))
                .icon(new ItemStack(RuneCraftoryItems.FORGE.get()))
                .page(HeaderedTextBuilder.of(this.get("runecraftory.book.entry.crafting"), this.get("runecraftory.book.entry.crafting.1")))
                .page(TextBuilder.of(this.get("runecraftory.book.entry.crafting.2")),
                        ItemGalleryBuilder.of(new GridItemDisplay(List.of(new SimpleItemDisplay(new ItemStack(RuneCraftoryItems.FORGING_BREAD.get())),
                                        new SimpleItemDisplay(new ItemStack(RuneCraftoryItems.ACCESSORY_BREAD.get())),
                                        new SimpleItemDisplay(new ItemStack(RuneCraftoryItems.MEDICINE_BREAD.get())),
                                        new SimpleItemDisplay(new ItemStack(RuneCraftoryItems.COOKING_BREAD.get()))), 4, 18, true))
                                .x(50)
                                .y(70))
                .page(TextBuilder.of(this.get("runecraftory.book.entry.crafting.3")))
                .pages(this.displayItem("runecraftory.book.entry.crafting.forge.title", new ItemStack(RuneCraftoryBlocks.FORGE.get()),
                        "runecraftory.book.entry.crafting.forge"))
                .pages(this.displayItem("runecraftory.book.entry.crafting.accessory_workbench.title", new ItemStack(RuneCraftoryBlocks.ACCESSORY_WORKBENCH.get()),
                        "runecraftory.book.entry.crafting.accessory_workbench"))
                .pages(this.displayItem("runecraftory.book.entry.crafting.chemistry_set.title", new ItemStack(RuneCraftoryBlocks.CHEMISTRY_SET.get()),
                        "runecraftory.book.entry.crafting.chemistry_set"))
                .pages(this.displayItem("runecraftory.book.entry.crafting.cooking_table.title", new ItemStack(RuneCraftoryBlocks.COOKING_TABLE.get()),
                        "runecraftory.book.entry.crafting.cooking_table"))
                .build("entry_crafting", output, "category_main");
        EntryBuilder.of(this.get("runecraftory.book.entry.minerals"))
                .icon(new ItemStack(RuneCraftoryItems.MINERAL_IRON.get()))
                .page(HeaderedTextBuilder.of(this.get("runecraftory.book.entry.minerals"), this.get("runecraftory.book.entry.minerals.1")))
                .page(TextBuilder.of(this.get("runecraftory.book.entry.minerals.2")),
                        ShowcaseBuilder.of(RuneCraftoryBlocks.MINERAL_MAP
                                        .entrySet().stream().sorted(Comparator.comparing(e -> e.getKey().ordinal()))
                                        .map(e -> new ItemStack(e.getValue().get()))
                                        .toArray(ItemStack[]::new)).height(30)
                                .y(90).scale(0.5f))
                .build("entry_minerals", output, "category_main");
        EntryBuilder.of(this.get("runecraftory.book.entry.shipping"))
                .icon(new ItemStack(RuneCraftoryItems.SHIPPING_BIN.get()))
                .page(HeaderedTextBuilder.of(this.get("runecraftory.book.entry.shipping"), this.get("runecraftory.book.entry.shipping.1")))
                .page(CraftingPageBuilder.of(RuneCraftory.modRes("shipping_bin")))
                .build("entry_shipping", output, "category_main");
        EntryBuilder.of(this.get("runecraftory.book.entry.entities"))
                .icon(SpawnEgg.fromType(RuneCraftoryEntities.WOOLY.get()).map(ItemStack::new).orElseThrow())
                .page(HeaderedTextBuilder.of(this.get("runecraftory.book.entry.entities"), this.get("runecraftory.book.entry.entities.1")))
                .page(TextBuilder.of(this.get("runecraftory.book.entry.entities.2")))
                .build("entry_entities", output, "category_main");
        EntryBuilder.of(this.get("runecraftory.book.entry.taming"))
                .icon(new ItemStack(RuneCraftoryItems.MONSTER_BARN.get()))
                .page(HeaderedTextBuilder.of(this.get("runecraftory.book.entry.taming"), this.get("runecraftory.book.entry.taming.1")),
                        ShowcaseBuilder.of(new ItemStack(RuneCraftoryItems.MONSTER_BARN.get()))
                                .width(90).y(60).height(90).scale(0.8f))
                .page(TextBuilder.of(this.get("runecraftory.book.entry.taming.2")))
                .page(TextBuilder.of(this.get("runecraftory.book.entry.taming.3")))
                .page(TextBuilder.of(this.get("runecraftory.book.entry.taming.4")))
                .page(TextBuilder.of(this.get("runecraftory.book.entry.taming.5")))
                .page(TextBuilder.of(this.get("runecraftory.book.entry.taming.6")))
                .page(TextBuilder.of(this.get("runecraftory.book.entry.taming.7")))
                .page(TextBuilder.of(this.get("runecraftory.book.entry.taming.8")))
                .build("entry_taming", output, "category_main");
        EntryBuilder.of(this.get("runecraftory.book.entry.entities"));
        EntryBuilder.of(this.get("runecraftory.book.entry.party"))
                .icon(new ItemStack(Items.LEAD))
                .page(HeaderedTextBuilder.of(this.get("runecraftory.book.entry.party"), this.get("runecraftory.book.entry.party.1")))
                .build("entry_party", output, "category_main");

        EntryBuilder.of(this.get("runecraftory.book.entry.farming"))
                .icon(new ItemStack(RuneCraftoryItems.TURNIP_SEEDS.get()))
                .page(HeaderedTextBuilder.of(this.get("runecraftory.book.entry.farming"), this.get("runecraftory.book.entry.farming.1")))
                .page(TextBuilder.of(this.get("runecraftory.book.entry.farming.2")))
                .page(TextBuilder.of(this.get("runecraftory.book.entry.farming.3")))
                .page(TextBuilder.of(this.get("runecraftory.book.entry.farming.4")))
                .page(TextBuilder.of(this.get("runecraftory.book.entry.farming.5")),
                        ImageBuilder.of(RuneCraftory.modRes("textures/book/magnifying_example.png"))
                                .width(100)
                                .height(58)
                                .y(75))
                .build("entry_farming", output, "category_farming");
        EntryBuilder.of(this.get("runecraftory.book.entry.trees"))
                .icon(new ItemStack(RuneCraftoryItems.APPLE_SAPLING.get()))
                .page(HeaderBuilder.of(this.get("runecraftory.book.entry.trees")),
                        SeparatorBuilder.of().y(10),
                        ShowcaseBuilder.of(new ItemStack(RuneCraftoryItems.APPLE_SAPLING.get()),
                                        new ItemStack(RuneCraftoryItems.GRAPE_SAPLING.get()),
                                        new ItemStack(RuneCraftoryItems.ORANGE_SAPLING.get()))
                                .y(22))
                .page(TextBuilder.of(this.get("runecraftory.book.entry.trees.1")))
                .build("entry_trees", output, "category_farming");
        EntryBuilder.of(this.get("runecraftory.book.entry.fertilizer"))
                .icon(new ItemStack(RuneCraftoryItems.FORMULAR_A.get()))
                .page(HeaderedTextBuilder.of(this.get("runecraftory.book.entry.fertilizer"), this.get("runecraftory.book.entry.fertilizer.1")))
                .page(TextBuilder.of(this.get("runecraftory.book.entry.fertilizer.2")))
                .build("entry_fertilizer", output, "category_farming");
        EntryBuilder.of(this.get("runecraftory.book.entry.weather"))
                .icon(new ItemStack(Items.SUNFLOWER))
                .page(HeaderedTextBuilder.of(this.get("runecraftory.book.entry.weather"), this.get("runecraftory.book.entry.weather.1")))
                .page(TextBuilder.of(this.get("runecraftory.book.entry.weather.2")))
                .build("entry_weather", output, "category_farming");

        EntryBuilder.of(this.get("runecraftory.book.entry.weapon"))
                .icon(new ItemStack(RuneCraftoryItems.SHORT_DAGGER.get()))
                .page(HeaderedTextBuilder.of(this.get("runecraftory.book.entry.weapon"), this.get("runecraftory.book.entry.weapon.1")))
                .pages(this.framedItem("runecraftory.book.entry.weapon.2.title", new ItemStack(RuneCraftoryItems.BROAD_SWORD.get()),
                        "runecraftory.book.entry.weapon.2"))
                .pages(this.framedItem("runecraftory.book.entry.weapon.3.title", new ItemStack(RuneCraftoryItems.CLAYMORE.get()),
                        "runecraftory.book.entry.weapon.3"))
                .pages(this.framedItem("runecraftory.book.entry.weapon.4.title", new ItemStack(RuneCraftoryItems.SPEAR.get()),
                        "runecraftory.book.entry.weapon.4"))
                .pages(this.framedItem("runecraftory.book.entry.weapon.5.title", new ItemStack(RuneCraftoryItems.BATTLE_AXE.get()),
                        "runecraftory.book.entry.weapon.5"))
                .pages(this.framedItem("runecraftory.book.entry.weapon.6.title", new ItemStack(RuneCraftoryItems.SHORT_DAGGER.get()),
                        "runecraftory.book.entry.weapon.6"))
                .pages(this.framedItem("runecraftory.book.entry.weapon.7.title", new ItemStack(RuneCraftoryItems.LEATHER_GLOVE.get()),
                        "runecraftory.book.entry.weapon.7"))
                .pages(this.framedItem("runecraftory.book.entry.weapon.8.title", new ItemStack(RuneCraftoryItems.ROD.get()),
                        "runecraftory.book.entry.weapon.8"))
                .build("entry_weapon", output, "category_equipment");
        EntryBuilder.of(this.get("runecraftory.book.entry.tools"))
                .icon(new ItemStack(RuneCraftoryItems.HOE_SCRAP.get()))
                .page(HeaderedTextBuilder.of(this.get("runecraftory.book.entry.tools"), this.get("runecraftory.book.entry.tools.1")))
                .pages(this.framedItem("runecraftory.book.entry.tools.2.title", new ItemStack(RuneCraftoryItems.HOE_SCRAP.get()),
                        "runecraftory.book.entry.tools.2"))
                .pages(this.framedItem("runecraftory.book.entry.tools.3.title", new ItemStack(RuneCraftoryItems.WATERING_CAN_SCRAP.get()),
                        "runecraftory.book.entry.tools.3"))
                .pages(this.framedItem("runecraftory.book.entry.tools.4.title", new ItemStack(RuneCraftoryItems.SICKLE_SCRAP.get()),
                        "runecraftory.book.entry.tools.4"))
                .pages(this.framedItem("runecraftory.book.entry.tools.5.title", new ItemStack(RuneCraftoryItems.HAMMER_SCRAP.get()),
                        "runecraftory.book.entry.tools.5"))
                .pages(this.framedItem("runecraftory.book.entry.tools.6.title", new ItemStack(RuneCraftoryItems.AXE_SCRAP.get()),
                        "runecraftory.book.entry.tools.6"))
                .pages(this.framedItem("runecraftory.book.entry.tools.7.title", new ItemStack(RuneCraftoryItems.FISHING_ROD_SCRAP.get()),
                        "runecraftory.book.entry.tools.7"))
                .pages(this.framedItem("runecraftory.book.entry.tools.8.title", new ItemStack(RuneCraftoryItems.GLASS.get()),
                        "runecraftory.book.entry.tools.8"))
                .build("entry_tools", output, "category_equipment");
        EntryBuilder.of(this.get("runecraftory.book.entry.spellskills"))
                .icon(new ItemStack(RuneCraftoryItems.TELEPORT.get()))
                .page(HeaderedTextBuilder.of(this.get("runecraftory.book.entry.spellskills"), this.get("runecraftory.book.entry.spellskills.1")))
                .page(DoubleCraftingPageBuilder.of(RuneCraftory.modRes("fireball"), RuneCraftory.modRes("teleport")))
                .build("entry_spellskills", output, "category_equipment");

        EntryBuilder.of(this.get("runecraftory.book.entry.npc"))
                .icon(new ItemStack(Items.PLAYER_HEAD))
                .page(HeaderedTextBuilder.of(this.get("runecraftory.book.entry.npc"), this.get("runecraftory.book.entry.npc.1")),
                        ImageBuilder.of(RuneCraftory.modRes("textures/book/money.png"))
                                .width(15).height(15)
                                .y(70).x(80))
                .page(TextBuilder.of(this.get("runecraftory.book.entry.npc.2")))
                .page(TextBuilder.of(this.get("runecraftory.book.entry.npc.3")))
                .page(TextBuilder.of(this.get("runecraftory.book.entry.npc.4")))
                .build("entry_npc", output, "category_npc");
        EntryBuilder.of(this.get("runecraftory.book.entry.npc.professions"))
                .icon(new ItemStack(RuneCraftoryItems.CASH_REGISTER.get()))
                .page(HeaderedTextBuilder.of(this.get("runecraftory.book.entry.npc.professions.1.title"), this.get("runecraftory.book.entry.npc.professions.1")))
                .page(HeaderedTextBuilder.of(this.get("runecraftory.book.entry.npc.professions.2.title"), this.get("runecraftory.book.entry.npc.professions.2")))
                .page(HeaderedTextBuilder.of(this.get("runecraftory.book.entry.npc.professions.3.title"), this.get("runecraftory.book.entry.npc.professions.3")))
                .page(HeaderedTextBuilder.of(this.get("runecraftory.book.entry.npc.professions.4.title"), this.get("runecraftory.book.entry.npc.professions.4")))
                .page(HeaderedTextBuilder.of(this.get("runecraftory.book.entry.npc.professions.5.title"), this.get("runecraftory.book.entry.npc.professions.5")))
                .page(HeaderedTextBuilder.of(this.get("runecraftory.book.entry.npc.professions.6.title"), this.get("runecraftory.book.entry.npc.professions.6")))
                .page(HeaderedTextBuilder.of(this.get("runecraftory.book.entry.npc.professions.7.title"), this.get("runecraftory.book.entry.npc.professions.7")))
                .page(HeaderedTextBuilder.of(this.get("runecraftory.book.entry.npc.professions.8.title"), this.get("runecraftory.book.entry.npc.professions.8")))
                .page(HeaderedTextBuilder.of(this.get("runecraftory.book.entry.npc.professions.9.title"), this.get("runecraftory.book.entry.npc.professions.9")))
                .build("entry_npc_professions", output, "category_npc");
        EntryBuilder.of(this.get("runecraftory.book.entry.quests"))
                .icon(new ItemStack(RuneCraftoryItems.QUEST_BOARD.get()))
                .page(HeaderedTextBuilder.of(this.get("runecraftory.book.entry.quests"), this.get("runecraftory.book.entry.quests.1")))
                .page(CraftingRecipeBuilder.of(RuneCraftory.modRes("quest_board")))
                .build("entry_quests", output, "category_npc");
    }

    protected PageBuilder displayItem(String header, ItemStack stack, String text) {
        int y = 0;
        return PageBuilder.of()
                .components(HeaderBuilder.of(this.get(header)),
                        SeparatorBuilder.of().y(y += 10),
                        ShowcaseBuilder.of(stack).height(50)
                                .y(y += 12).scale(0.8f),
                        TextBuilder.of(this.get(text))
                                .y(y + 50 + 6));
    }

    protected PageBuilder framedItem(String header, ItemStack stack, String text) {
        int y = 0;
        return PageBuilder.of()
                .components(HeaderBuilder.of(this.get(header)),
                        SeparatorBuilder.of().y(y += 10),
                        FramedItemGalleryBuilder.of(new SimpleItemDisplay(stack)).x(50 - 8)
                                .y(y += 12),
                        TextBuilder.of(this.get(text))
                                .y(y + 16 + 6));
    }

    @Override
    public void buildCategories(HolderLookup.Provider registries, BookContentOutput output) {
        int sort = 0;
        CategoryBuilder.of(this.get("runecraftory.book.category.main"))
                .landingText(this.get("runecraftory.book.category.main.desc"))
                .icon(new ItemStack(RuneCraftoryItems.ICON_0.get()))
                .sortNum(sort++)
                .build("category_main", output);

        CategoryBuilder.of(this.get("runecraftory.book.category.farming"))
                .landingText(this.get("runecraftory.book.category.farming.desc"))
                .icon(new ItemStack(Blocks.FARMLAND))
                .sortNum(sort++)
                .build("category_farming", output);

        CategoryBuilder.of(this.get("runecraftory.book.category.equipment"))
                .landingText(this.get("runecraftory.book.category.equipment.desc"))
                .icon(new ItemStack(RuneCraftoryItems.SHORT_DAGGER.get()))
                .sortNum(sort++)
                .build("category_equipment", output);

        CategoryBuilder.of(this.get("runecraftory.book.category.npc"))
                .landingText(this.get("runecraftory.book.category.npc.desc"))
                .icon(new ItemStack(Items.PLAYER_HEAD))
                .sortNum(sort)
                .build("category_npc", output);
    }

    protected void createTranslations() {
        this.add("runecraftory.book.category.main", "Introduction");
        this.add("runecraftory.book.category.main.desc", "This section provides a guide of core mechanics of this mod");
        this.add("runecraftory.book.entry.runepoints", "Rune points");
        this.add("runecraftory.book.entry.runepoints.1", """
                $(c:blue)Rune points (RP)$() can be seen as your stamina or mana. Nearly all actions require rune points to use. Keep track of your current rune points when \
                performing actions as running out of rune points will make use of your $(c:darkred)HP$()!
                
                There are multiple ways of replenishing runepoints. Most notable ones include resting and eating food.""");
        this.add("runecraftory.book.entry.runepoints.2", """
                Increasing your max runepoints can be done by increasing your skills. Some food also give a temporary boost to your runepoints.""");

        this.add("runecraftory.book.entry.calendar", "Calendar");
        this.add("runecraftory.book.entry.calendar.1", """
                RuneCraftory adds an advancing calendar to the game. A date is defined by the day and a season. \
                A year is divided by 4 seasons each with 30 days. Keeping track of the season is important in order to successfully \
                grow crops.""");
        this.add("runecraftory.book.entry.calendar.2", """
                While crops won't die if grown out of season their growth speed are affected majorly depending on the current season and their preference.
                If its getting colder biomes which usually don't have snow can experience snowfall too.""");

        this.add("runecraftory.book.entry.crafting", "Crafting");
        this.add("runecraftory.book.entry.crafting.1", """
                This mod adds 4 additional blocks that are used to craft various items.
                Every crafting process requires a certain amount of $(c:blue)RP$(). Recipes that you haven't unlocked yet will require more $(c:blue)RP$() than those you have.\
                You will not be able to craft an item if the required $(c:blue)RP$() is higher than your total $(c:blue)RP$().""");
        this.add("runecraftory.book.entry.crafting.2", """
                To unlock recipe you need to eat recipe breads. You can also just try experimenting as crafting a locked recipe will unlock it.
                """);
        this.add("runecraftory.book.entry.crafting.3", """
                Weapon and tools can be upgraded in the forge while armor can be upgraded in the accessory workbench.
                Open the upgrade gui by shift right clicking on the crafting device. An item can be upgraded a maximum of $(c:red)9$() times. Holding shift while hovering over an item will tell you \
                what stat it gives. Using the same item multiple times causes a $(c:red)diminishing$() returns effect so try use different items!""");
        this.add("runecraftory.book.entry.crafting.forge.title", "Forge");
        this.add("runecraftory.book.entry.crafting.forge", "The forge is used to craft all weapons and tools. During crafting you can add up to 3 bonus items that will " +
                "act the same as if you upgraded the weapon/tool with it.");
        this.add("runecraftory.book.entry.crafting.accessory_workbench.title", "Accessory Workbench");
        this.add("runecraftory.book.entry.crafting.accessory_workbench", "Use the accessory workbench to make various armor pieces. Same as the forge you can use up to 3 additional items here " +
                "to increase the stats.");
        this.add("runecraftory.book.entry.crafting.chemistry_set.title", "Chemistry Set");
        this.add("runecraftory.book.entry.crafting.chemistry_set", "A chemistry set allows you to create potions and other pharmacy items.");
        this.add("runecraftory.book.entry.crafting.cooking_table.title", "Cooking Table");
        this.add("runecraftory.book.entry.crafting.cooking_table", "The cooking table is as the name implies used to make all kinds of food. Some items can also be used as additional items for a better end product.");

        this.add("runecraftory.book.entry.minerals", "Minerals");
        this.add("runecraftory.book.entry.minerals.1", "Cluster of minerals spawn all over the world. These mineral clusters can be mined with an iron pickaxe or above but its best mined with " +
                "the $(c:darkpurple)mining hammers$() from this mod. Better hammers and mining skill decrease the chance of the mineral breaking and increase the chance to get rarer materials from it.");
        this.add("runecraftory.book.entry.minerals.2", """
                Some materials even are impossible to get unless you have a high enough mining skill.
                Minerals regenerate after a day has passed.
                If you want to completly get rid of a mineral mine a broken one while shifting.""");

        this.add("runecraftory.book.entry.shipping", "Shipping Items");
        this.add("runecraftory.book.entry.shipping.1", """
                The shipping bin is your method of earning money. Crafting it allows you to sell items.
                Every $(c:gold)$(b)morning$() all sellable items in the shipping bin will be sold.
                The shipping bin inventory is global for each player just like an enderchest although storing items is $(c:red)not$() recommended in it!""");

        this.add("runecraftory.book.entry.entities", "Monsters");
        this.add("runecraftory.book.entry.entities.1", """
                The mobs in this mod don't spawn by themself but through $(c:lightpurple)gates$() that appear through the world.
                Gates are ripples in space that connects this world to the forest of beginnings, the home of monsters. \
                
                The type of monsters a gate spawns depends on the biome and a gate will continue to spawn monsters till it is destroyed.""");
        this.add("runecraftory.book.entry.entities.2", """
                Scattered throughough the world are also particularly $(c:red)strong monsters$() that can be fought once per day.
                Word has it that defeating these monsters causes the gates around the player to get stronger.
                
                Monsters are strangely averse to loud noises as placing a $(c:darkpurple)bell block$() will prevent gates from appearing in a 48 block radius around it.""");

        this.add("runecraftory.book.entry.taming", "Taming");
        this.add("runecraftory.book.entry.taming.1", """
                It is possible to tame monsters and the process is fairly straight forward.
                First you need to setup a place for your monsters to live. A $(c:#db8c04)monster barn$() is the perfect place for that and can be bought by blacksmith npcs.""");
        this.add("runecraftory.book.entry.taming.2", """
                Simply placing down the block isn't enough though. The barn requires a minimum ground of 5x5 (and up to 11x11) of $(c:#db8c04)haybales$() with it in the center.
                The bigger this area the more space it will have. Most monsters additionally require a roof over their head. Any block with a collision count.
                The roof needs to be at least $(c:darkred)3 blocks high$() and be able to $(c:darkred)fit$() the monster you want to house.""");
        this.add("runecraftory.book.entry.taming.3", """
                If you have your monster barn setup you can then go ahead and try and tame the monster. For that simply throw an item at them. \
                This will consume the item and after a while particles appear indicating the success (or not).
                Monsters might have one or more favorite items that results in a higher chance of success and bosses \
                can $(c:red)$(b)only$() be tamed by giving them their favorite items.""");
        this.add("runecraftory.book.entry.taming.4", """
                You can further increase the chance with various other methods. One simple way is using a $(c:darkpurple)brush$() on the monster.
                
                If you finally managed to tame the monster there are various things you can do now:
                 ▶ Shift-right-clicking opens up a menu for the monster.
                 ▶ When ridden pressing any spell keys will perform an attack using it.""");
        this.add("runecraftory.book.entry.taming.5", """
                 ▶ Brushing your monster daily helps in raising your friendship.
                 ▶ You can strengthen your monster by giving them an item everyday. This also raises your friendship.
                
                Giving your monster food will have them obtain the same benefit as with a player making it an easy way to heal or buff your monsters.""");
        this.add("runecraftory.book.entry.taming.6", """
                A tamed monster will $(c:darkpurple)faint$() instead of dying when reaching critical health and healing them through any means will bring them back up.
                
                Lastly $(b)shift-right-click$() a tamed monster with a vanilla stick will release them again.""");
        this.add("runecraftory.book.entry.taming.7", """
                Tamed monster can also help you with $(c:darkgreen)farming crops$().
                Upon setting them into farming mode they will tend to the crops in a certain radius around the initial position.
                The $(c:darkpurple)nearest inventory$() block will also be bound to their action and they will deposit harvested crops into that inventory and if seeds are in it they can also plant them.""");
        this.add("runecraftory.book.entry.taming.8", """
                While holding a $(c:darkpurple)monster command staff$() and opening the monster menu you can configure additional things like area of actions and inventory for the entity.
                Right clicking a block while e.g. configuring the home position will set the home position to that place.
                
                Do note that farming is a taxing task and without help they won't be able to keep going forever!""");

        this.add("runecraftory.book.entry.party", "Party System");
        this.add("runecraftory.book.entry.party.1", """
                You might have noticed that sometimes you can't make monsters or npc follow you. \
                This is due to the $(c:darkpurple)party system$() which allows only a maximum of 3 entities (monster + npc) to follow you at the same time.
                Party members will follow you and teleport if too far regardless of where you are, even across dimensions.""");

        this.add("runecraftory.book.category.farming", "Agriculture");
        this.add("runecraftory.book.category.farming.desc", "An overview and guide about the agricultural aspects");

        this.add("runecraftory.book.entry.farming", "Getting Started");
        this.add("runecraftory.book.entry.farming.1", """
                To get started with growing crops you first need a $(c:darkpurple)hoe$(), a $(c:darkpurple)watering can$() and of course $(c:darkpurple)crop seeds$() to plant. \
                Then simply till the land to turn it into farmland.
                After that plant the crops on the farmland and water it with a watering can. Unlike vanilla having water near farmland will $(c:red)not$() irrigate it!
                Dry farmland will also not turn back into dirt.""");
        this.add("runecraftory.book.entry.farming.2", """
                The crops will grow every day and you will also need to keep watering them each day till they are fully grown. Crops can $(b)wilt$() if you forget to water them and \
                by not watering wilted crops they will turn into $(c:#a85207)withered grass$() so make sure to keep them hydrated.
                After a few days the crops will mature and are then ready for harvest. As a QOL you can simply $(c:darkpurple)right click$() the crops to harvest them.
                """);
        this.add("runecraftory.book.entry.farming.3", """
                Crops will get a growth bonus if they are planted in their $(c:gold)preferred$() season and if planted in the $(c:red)wrong$() season will grow slower.
                Note that affected crops will not grow the vanilla way!
                You can see if they are affected by simply look if they have additional tooltip info attached to them.""");
        this.add("runecraftory.book.entry.farming.4", """
                Additionally crops will grow REGARDLESS of if the chunk is loaded or not. While this means you don't need to be nearby for the crops to grow \
                it also means that unless regularly watering them they have a high chance to wilt if you are gone for a while.
                You can mitigate this problem by having your $(c:blue)$(el:entry_taming)monster companions$() help you out.""");
        this.add("runecraftory.book.entry.farming.5", """
                Lastly a $(c:darkpurple)magnifying glass$() is a very important tool as it allows you to see the stats of the farmland.
                You can use items to increase the soil quality (see $(el:blue)$(el:entry_fertilizer)here$()).""");

        this.add("runecraftory.book.entry.trees", "Trees");
        this.add("runecraftory.book.entry.trees.1", """
                You might come across some special saplings that are unlike the other saplings. These saplings act similar to $(c:darkgreen)crops$() instead and need to be planted on farmland.
                The saplings take a long time to grow but once fully grown will bear fruit everyday you can harvest.
                Another major difference is that these trees are $(c:red)unbreakable$() except for the base of the tree.
                Breaking the base will also remove the tree!""");

        this.add("runecraftory.book.entry.fertilizer", "Fertilizer");
        this.add("runecraftory.book.entry.fertilizer.1", """
                There are various items to improve your farming efficiency.
                These items can either be bought at $(c:darkpurple)shops$() or you can craft them yourself using a $(c:darkpurple)chemistry set$().
                Vanilla bonemeal will $(c:red)not$() work like normal and grow the crops, instead it will work as a very weak growth increaser for the soil.
                """);
        this.add("runecraftory.book.entry.fertilizer.2", """
                ▶ $(c:darkgreen)Formular a, b and c$() increases the growth rate of the soil with a being the weakest and c the strongest.
                ▶ $(c:darkgreen)Wettable powder$() increases the soils defence. If the defence is 0 storms have a chance to destroy the crop.
                ▶ $(c:darkgreen)Giantizer/Minimizer$() are used to grow giant crops.
                ▶ $(c:darkgreen)Greenifier$() increases soil level and as such also crop level (Not implemented $(b)ATM$()).""");

        this.add("runecraftory.book.entry.weather", "Weather");
        this.add("runecraftory.book.entry.weather.1", """
                The weather is an important factor in agriculture.
                Unlike vanilla the weather now  only changes during certain times of the day.
                
                There are 4 weather types with their effects listed below:
                 ▶ $(c:green)Sunny$(): Normal sunny day without any special properties equivalent to vanillas normal weather""");
        this.add("runecraftory.book.entry.weather.2", """
                 ▶ $(c:blue)Rain$(): Farmland will automatically get watered
                 ▶ $(c:darkaqua)Storming$(): Farmland will automatically get watered but the defence gets reduced over time. If defence drops to 0 the crops can get destroyed by the storm so pay attention
                 ▶ $(c:gold)Runey$(): Like sunny days but crops get get a growth spurt at the start.
                
                If it gets cold enough it might also snow in places where it normally wouldn't.""");

        this.add("runecraftory.book.category.equipment", "Equipments");
        this.add("runecraftory.book.category.equipment.desc", """
                RuneCraftory not only ads more equipment to the game but each of them have special properties too.
                You can check out what they all do in this section""");
        this.add("runecraftory.book.entry.weapon", "Weapons");
        this.add("runecraftory.book.entry.weapon.1", """
                The mod adds a plethora of weapons and you will find a short explanation of each weapon type here.
                By reaching at least level 5 for a weapon type you are able to use a charge attack. \
                Simply hold right click and release after a while to use it. This will consume a bit of runepoints though.""");
        this.add("runecraftory.book.entry.weapon.2.title", "Short Swords");
        this.add("runecraftory.book.entry.weapon.2", "Weapons with a shorter reach and attack power but quite fast with a small attack area. The closest to vanilla swords.");
        this.add("runecraftory.book.entry.weapon.3.title", "Long Swords");
        this.add("runecraftory.book.entry.weapon.3", "Longer reach and more attack power but kinda slow. Has a decent attack area.");
        this.add("runecraftory.book.entry.weapon.4.title", "Spears");
        this.add("runecraftory.book.entry.weapon.4", "Long reach and fairly quick with a special charge attack: After charging and releasing repeatedly right click to keep attacking");
        this.add("runecraftory.book.entry.weapon.5.title", "Axe/Hammers");
        this.add("runecraftory.book.entry.weapon.5", """
                Slow but strong and with bigger reach. Not to be confused with their tool variants though.
                Axes usually have a high crit rate while hammers a high stun chance.""");
        this.add("runecraftory.book.entry.weapon.6.title", "Dual Blades");
        this.add("runecraftory.book.entry.weapon.6", """
                Dual blades are two handed weapons with fast attack speed.
                Since these require both of your hands you will not be able to use offhand items or profit from their bonuses.""");
        this.add("runecraftory.book.entry.weapon.7.title", "Fists");
        this.add("runecraftory.book.entry.weapon.7", "Dual weapons. Fast but with shorter reach. Charge attack will push you into the direction you are looking and during that hitting any mobs in your way");
        this.add("runecraftory.book.entry.weapon.8.title", "Staffs");
        this.add("runecraftory.book.entry.weapon.8", "A magic weapon. Each staff has a base spell used per weapon swing. Additionally upgrading a staff with items can give it spells too. \nTo use them simply hold right click. " +
                "A staff can have a maximum of 3 spells attached to it.");
        this.add("runecraftory.book.entry.tools", "Tools");
        this.add("runecraftory.book.entry.tools.1", "Here is an overview of the tools from this mod. You can use tools as weapons but they are noticeably weaker. The higher tier the tool is the more powerful the " +
                "charge ability will be.");
        this.add("runecraftory.book.entry.tools.2.title", "Hoe");
        this.add("runecraftory.book.entry.tools.2", "Used to till the earth turning it into farmland to grow crops just like their vanilla counterparts");
        this.add("runecraftory.book.entry.tools.3.title", "Wateringcan");
        this.add("runecraftory.book.entry.tools.3", "You need a watering can to water farmland. Right click on water blocks to fill it up. Without this tool farmlands will always be dry (unless the rain blesses you)");
        this.add("runecraftory.book.entry.tools.4.title", "Sickle");
        this.add("runecraftory.book.entry.tools.4", "Can be used to clear out grass more easily");
        this.add("runecraftory.book.entry.tools.5.title", "Hammer");
        this.add("runecraftory.book.entry.tools.5", "Acts like a pickaxe but you can also use it to flatten farmland turning it back into dirt. It also gets additional benefits when breaking minerals.");
        this.add("runecraftory.book.entry.tools.6.title", "Axe");
        this.add("runecraftory.book.entry.tools.6", "For now acts just like vanilla axes");
        this.add("runecraftory.book.entry.tools.7.title", "Fishing Rod");
        this.add("runecraftory.book.entry.tools.7", "Similiar to vanilla fishing rod. Throw it into a body of water to start fishing. The body of water needs to be at least $(c:darkgreen)2 blocks deep and 3x3 wide$(). Higher tier fishing rods increases the speed to catch" +
                " a fish while the more charge it has the easier it is to catch a fish.");
        this.add("runecraftory.book.entry.tools.8.title", "Magnifying Glass");
        this.add("runecraftory.book.entry.tools.8", "Useful to inspect the quality of the soil. If used as upgrade material will pass on its function to the upgraded tool.");
        this.add("runecraftory.book.entry.spellskills", "Spells and Skills");
        this.add("runecraftory.book.entry.spellskills.1", """
                A plethora of spells and rune abilities can be found all over the world. Every spell and rune ability will require runepoints to use them.
                Putting them in the spell slots allows you to easily cast them by pressing the corresponding spell key.
                Holding a mismatched weapon when using rune abilities reduces their efficiency""");

        this.add("runecraftory.book.category.npc", "Villagers");
        this.add("runecraftory.book.category.npc.desc", "Villagers? They should be familiar... or are they? Somehow they look different than.");
        this.add("runecraftory.book.entry.npc", "Villagers");
        this.add("runecraftory.book.entry.npc.1", """
                Like their big nosed fellows villagers appear in villages. Similarly you can obtain various goods from them albeit using money instead of emerald.
                
                
                Unlike their vanilla counterpart you won't be able to buy products all the time though.""");
        this.add("runecraftory.book.entry.npc.2", """
                For a villager to operate a shop they require a $(c:blue)bed$() and a $(c:blue)workplace$() that is not too far away from it.
                Additionally a shop is only open during opening hours. You can check the requirements by simply talking to them.
                Hovering over their $(c:red)profession$() will highlight what the villager is missing.
                
                These villagers are also much more capable than usual.""");
        this.add("runecraftory.book.entry.npc.3", """
                By interacting with them you will notice that they offer much more than simple trades.
                You can converse with them and even give them gifts to befriend them. Both of these action increases your friendship with them but note that it only works $(c:red)once$() per day!
                To give a gift to a villager simply $(c:darkpurple)throw$() the item at them.
                By gifting them equipment they will automatically equip them too.""");
        this.add("runecraftory.book.entry.npc.4", """
                Befriending them allows you to make them follow you around and provide assistant in combat.
                
                Additionally it also allows you to develope a relationship with them.
                If you think your relationship has progressed far enough you can make a $(c:#cd38e0)love letter$() to start dating them or \
                an $(c:gold)engagement ring$() to propose to them.""");

        this.add("runecraftory.book.entry.npc.professions", "Professions");
        this.add("runecraftory.book.entry.npc.professions.1.title", "General Store");
        this.add("runecraftory.book.entry.npc.professions.1", "The general store sells mostly seeds, vegetables and fruits");
        this.add("runecraftory.book.entry.npc.professions.2.title", "Florist");
        this.add("runecraftory.book.entry.npc.professions.2", "Here you can buy flowers and seeds an also fertilizer for your crops");
        this.add("runecraftory.book.entry.npc.professions.3.title", "Blacksmith");
        this.add("runecraftory.book.entry.npc.professions.3", "The blacksmith sells weapons, tools and armor as well as monster barns");
        this.add("runecraftory.book.entry.npc.professions.4.title", "Doctor");
        this.add("runecraftory.book.entry.npc.professions.4", "You can buy medicine and herbs here. Doctors can also cure you of status conditions if you have any");
        this.add("runecraftory.book.entry.npc.professions.5.title", "Cook");
        this.add("runecraftory.book.entry.npc.professions.5", "Buy food and recipe breads here. The cook only has a certain amount of recipe breads each day");
        this.add("runecraftory.book.entry.npc.professions.6.title", "Magic Shop");
        this.add("runecraftory.book.entry.npc.professions.6", "Sells various spells here.");
        this.add("runecraftory.book.entry.npc.professions.7.title", "Rune ability Store");
        this.add("runecraftory.book.entry.npc.professions.7", "Sells various rune abilities here.");
        this.add("runecraftory.book.entry.npc.professions.8.title", "Bath house");
        this.add("runecraftory.book.entry.npc.professions.8", """
                Bath houses offer a way to regen your $(c:darkred)HP$() and $(c:blue)RP$().
                By talking to the bath house attendant you can obtain an effect which will regen $(c:darkred)HP$() & $(c:blue)RP$() if you go into hot water.
                
                Going out of the water will remove the effect immediately!""");
        this.add("runecraftory.book.entry.npc.professions.9.title", "Travelling merchant");
        this.add("runecraftory.book.entry.npc.professions.9", "Sells miscellaneous items. WIP");

        this.add("runecraftory.book.entry.quests", "Quests");
        this.add("runecraftory.book.entry.quests.1", """
                Crafting a quest board and placing it in the world will allow nearby NPCs to post quests for you to complete. Try doing them as they usually give you useful information and/or rewards.
                After accepting a quest talk to the NPC to progress through it.
                Some NPCs also require you to complete certain quests before being able to marry them. (WIP ATM)""");

        this.add("runecraftory.book.category.entities", "Monsters");
        this.add("runecraftory.book.category.entities.desc", "List of all monsters");

        List<RegistryEntrySupplier<EntityType<? extends Entity>, ?>> entities = new ArrayList<>();
        this.entityDesc(entities, RuneCraftoryEntities.WOOLY, "Sheep like creature that is rather passive. Shearable.");
        this.entityDesc(entities, RuneCraftoryEntities.ORC_ARCHER, "An orc but with a bow");
        this.entityDesc(entities, RuneCraftoryEntities.BIG_MUCK, "Mushroom like create that attacks using spores");

        this.entityDesc(entities, RuneCraftoryEntities.AMBROSIA, "Butterfly boss monster");
        this.entityDesc(entities, RuneCraftoryEntities.THUNDERBOLT, "Horse said to be as fast as lightning");
        this.entityDesc(entities, RuneCraftoryEntities.MARIONETTA, "Spooky old doll");
        this.entityDesc(entities, RuneCraftoryEntities.HANDONETTA, "Whose hand is this???");

        for (RegistryEntrySupplier<EntityType<?>, ?> sup : RuneCraftoryEntities.getMonsters()) {
            if (entities.contains(sup))
                continue;
            this.add("runecraftory.book.entry.entity." + sup.getID(), "");
        }
    }

    public void add(String key, String value) {
        if (this.translations.put(key, value) != null)
            throw new IllegalStateException("Duplicate translation key " + key);
    }

    protected String get(String key) {
        String val = this.translations.get(key);
        if (val == null)
            throw new IllegalStateException("No translation for " + key);
        return val;
    }

    public void entityDesc(List<RegistryEntrySupplier<EntityType<?>, ?>> list, RegistryEntrySupplier<EntityType<?>, ?> sup, String value) {
        list.add(sup);
        this.add("runecraftory.book.entry.entity." + sup.getID(), value);
    }
}
