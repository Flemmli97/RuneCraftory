package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.RFCreativeTabs;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import xyz.brassgoggledcoders.patchouliprovider.BookBuilder;
import xyz.brassgoggledcoders.patchouliprovider.CategoryBuilder;
import xyz.brassgoggledcoders.patchouliprovider.PatchouliBookProvider;

import java.util.Comparator;
import java.util.function.Consumer;

public class PatchouliGen extends PatchouliBookProvider {

    public PatchouliGen(DataGenerator gen) {
        super(gen, RuneCraftory.MODID, "en_us");
    }

    @Override
    protected void addBooks(Consumer<BookBuilder> consumer) {
        BookBuilder book = this.createBookBuilder("runecraftory_book", "runecraftory_book", "runecraftory.patchouli.landing")
                //.setSubtitle("runecraftory.patchouli.subtitle")
                .setCreativeTab(RFCreativeTabs.WEAPON_TOOL_TAB.getRecipeFolderName())
                .setVersion("1.0")
                .setI18n(true)
                .setShowProgress(false);

        book.addCategory("category.main", "runecraftory.patchouli.category.main", "runecraftory.patchouli.category.main.desc", new ItemStack(ModItems.ICON_0.get()))
                .setSortnum(0)

                .addEntry("entry.crafting", "runecraftory.patchouli.entry.crafting", new ItemStack(ModBlocks.FORGE.get()))
                .setSortnum(0)
                .addSimpleTextPage("runecraftory.patchouli.entry.crafting.1")
                .addSimpleTextPage("runecraftory.patchouli.entry.crafting.2")
                .addSpotlightPage(new ItemStack(ModBlocks.FORGE.get()))
                .setText("runecraftory.patchouli.entry.crafting.forge")
                .build()
                .addSpotlightPage(new ItemStack(ModBlocks.COOKING.get()))
                .setText("runecraftory.patchouli.entry.crafting.cooking")
                .build()
                .addSpotlightPage(new ItemStack(ModBlocks.CHEMISTRY.get()))
                .setText("runecraftory.patchouli.entry.crafting.chemistry")
                .build()
                .addSpotlightPage(new ItemStack(ModBlocks.ACCESSORY.get()))
                .setText("runecraftory.patchouli.entry.crafting.armor")
                .build()
                .build()

                .addEntry("entry.minerals", "runecraftory.patchouli.entry.minerals", new ItemStack(ModItems.MINERAL_IRON.get()))
                .setSortnum(1)
                .addSimpleTextPage("runecraftory.patchouli.entry.minerals.1")
                .addSimpleTextPage("runecraftory.patchouli.entry.minerals.2")
                .build()

                .addEntry("entry.shipping", "runecraftory.patchouli.entry.shipping", new ItemStack(ModItems.SHIPPING_BIN.get()))
                .setSortnum(2)
                .addSimpleTextPage("runecraftory.patchouli.entry.shipping.1")
                .addCraftingPage(new ResourceLocation(RuneCraftory.MODID, "shipping_bin"))
                .build()
                .build()

                .addEntry("entry.entities", "runecraftory.patchouli.entry.entities", SpawnEgg.fromType(ModEntities.WOOLY.get()).map(ItemStack::new).orElse(ItemStack.EMPTY))
                .setSortnum(3)
                .addSimpleTextPage("runecraftory.patchouli.entry.entities.1")
                .addSimpleTextPage("runecraftory.patchouli.entry.entities.2")
                .addSimpleTextPage("runecraftory.patchouli.entry.entities.3")
                .addSimpleTextPage("runecraftory.patchouli.entry.entities.4")
                .addSimpleTextPage("runecraftory.patchouli.entry.entities.5")
                .addSimpleTextPage("runecraftory.patchouli.entry.entities.6")
                .addSpotlightPage(new ItemStack(ModItems.MONSTER_BARN.get()))
                .setAnchor("barn")
                .setText("runecraftory.patchouli.entry.entities.7")
                .build()
                .addSimpleTextPage("runecraftory.patchouli.entry.entities.8")
                .build()

                .addEntry("entry.party", "runecraftory.patchouli.entry.party", new ItemStack(Items.LEAD))
                .setSortnum(4)
                .addSimpleTextPage("runecraftory.patchouli.entry.party.1")
                .build()

                .addEntry("entry.quests", "runecraftory.patchouli.entry.quests", new ItemStack(ModItems.QUEST_BOARD.get()))
                .setSortnum(5)
                .addSimpleTextPage("runecraftory.patchouli.entry.quests.1")
                .build()
                .build();

        book.addCategory("category.farming", "runecraftory.patchouli.category.farming", "runecraftory.patchouli.category.farming.desc", new ItemStack(Blocks.FARMLAND))
                .setSortnum(1)

                .addEntry("entry.farming", "runecraftory.patchouli.entry.farming", new ItemStack(ModItems.TURNIP_SEEDS.get()))
                .setSortnum(0)
                .addSimpleTextPage("runecraftory.patchouli.entry.farming.1")
                .addSimpleTextPage("runecraftory.patchouli.entry.farming.2")
                .addSimpleTextPage("runecraftory.patchouli.entry.farming.3")
                .build()

                .addEntry("entry.fertilizer", "runecraftory.patchouli.entry.fertilizer", new ItemStack(ModItems.FORMULAR_A.get()))
                .setSortnum(1)
                .addTextPage("runecraftory.patchouli.entry.fertilizer.1")
                .setAnchor("p1")
                .build()
                .addSimpleTextPage("runecraftory.patchouli.entry.fertilizer.2")
                .build()

                .addEntry("entry.weather", "runecraftory.patchouli.entry.weather", new ItemStack(Items.SUNFLOWER))
                .setSortnum(2)
                .addSimpleTextPage("runecraftory.patchouli.entry.weather.1")
                .addSimpleTextPage("runecraftory.patchouli.entry.weather.2")
                .build()
                .build();

        book.addCategory("category.equipment", "runecraftory.patchouli.category.equipment", "runecraftory.patchouli.category.equipment.desc", new ItemStack(ModItems.SHORT_DAGGER.get()))
                .setSortnum(2)

                .addEntry("entry.weapon", "runecraftory.patchouli.entry.weapon", new ItemStack(ModItems.SHORT_DAGGER.get()))
                .setSortnum(0)
                .addSimpleTextPage("runecraftory.patchouli.entry.weapon.1")
                .addSpotlightPage(new ItemStack(ModItems.BROAD_SWORD.get()))
                .setTitle("runecraftory.patchouli.entry.weapon.2.title")
                .setText("runecraftory.patchouli.entry.weapon.2")
                .build()
                .addSpotlightPage(new ItemStack(ModItems.CLAYMORE.get()))
                .setTitle("runecraftory.patchouli.entry.weapon.3.title")
                .setText("runecraftory.patchouli.entry.weapon.3")
                .build()
                .addSpotlightPage(new ItemStack(ModItems.SPEAR.get()))
                .setTitle("runecraftory.patchouli.entry.weapon.4.title")
                .setText("runecraftory.patchouli.entry.weapon.4")
                .build()
                .addSpotlightPage(new ItemStack(ModItems.BATTLE_AXE.get()))
                .setTitle("runecraftory.patchouli.entry.weapon.5.title")
                .setText("runecraftory.patchouli.entry.weapon.5")
                .build()
                .addSpotlightPage(new ItemStack(ModItems.SHORT_DAGGER.get()))
                .setTitle("runecraftory.patchouli.entry.weapon.6.title")
                .setText("runecraftory.patchouli.entry.weapon.6")
                .build()
                .addSpotlightPage(new ItemStack(ModItems.LEATHER_GLOVE.get()))
                .setTitle("runecraftory.patchouli.entry.weapon.7.title")
                .setText("runecraftory.patchouli.entry.weapon.7")
                .build()
                .addSpotlightPage(new ItemStack(ModItems.ROD.get()))
                .setTitle("runecraftory.patchouli.entry.weapon.8.title")
                .setText("runecraftory.patchouli.entry.weapon.8")
                .build()
                .build()

                .addEntry("entry.tools", "runecraftory.patchouli.entry.tools", new ItemStack(ModItems.HOE_SCRAP.get()))
                .setSortnum(1)
                .addSimpleTextPage("runecraftory.patchouli.entry.tools.1")
                .addSpotlightPage(new ItemStack(ModItems.HOE_SCRAP.get()))
                .setTitle("runecraftory.patchouli.entry.tools.2.title")
                .setText("runecraftory.patchouli.entry.tools.2")
                .build()
                .addSpotlightPage(new ItemStack(ModItems.WATERING_CAN_SCRAP.get()))
                .setTitle("runecraftory.patchouli.entry.tools.3.title")
                .setText("runecraftory.patchouli.entry.tools.3")
                .build()
                .addSpotlightPage(new ItemStack(ModItems.SICKLE_SCRAP.get()))
                .setTitle("runecraftory.patchouli.entry.tools.4.title")
                .setText("runecraftory.patchouli.entry.tools.4")
                .build()
                .addSpotlightPage(new ItemStack(ModItems.HAMMER_SCRAP.get()))
                .setTitle("runecraftory.patchouli.entry.tools.5.title")
                .setText("runecraftory.patchouli.entry.tools.5")
                .build()
                .addSpotlightPage(new ItemStack(ModItems.AXE_SCRAP.get()))
                .setTitle("runecraftory.patchouli.entry.tools.6.title")
                .setText("runecraftory.patchouli.entry.tools.6")
                .build()
                .addSpotlightPage(new ItemStack(ModItems.FISHING_ROD_SCRAP.get()))
                .setTitle("runecraftory.patchouli.entry.tools.7.title")
                .setText("runecraftory.patchouli.entry.tools.7")
                .build()
                .addSpotlightPage(new ItemStack(ModItems.GLASS.get()))
                .setText("runecraftory.patchouli.entry.tools.8")
                .build()
                .build()

                .addEntry("entry.spellskills", "runecraftory.patchouli.entry.spellskills", new ItemStack(ModItems.TELEPORT.get()))
                .setSortnum(2)
                .addSimpleTextPage("runecraftory.patchouli.entry.spellskills.1")
                .addCraftingPage(new ResourceLocation(RuneCraftory.MODID, "fireball"))
                .setRecipe2(new ResourceLocation(RuneCraftory.MODID, "teleport"))
                .build()
                .addSimpleTextPage("runecraftory.patchouli.entry.spellskills.3")
                .build();

        book.addCategory("category.npc", "runecraftory.patchouli.category.npc", "runecraftory.patchouli.category.npc.desc", new ItemStack(Items.PLAYER_HEAD))
                .setSortnum(3)
                .addEntry("entry.npc", "runecraftory.patchouli.entry.npc", new ItemStack(Items.PLAYER_HEAD))
                .setSortnum(0)
                .build()

                .addEntry("entry.npc.jobs", "runecraftory.patchouli.entry.npc.jobs", new ItemStack(Items.PLAYER_HEAD))
                .setSortnum(1)
                .addSimpleTextPage("runecraftory.patchouli.entry.npc.jobs.1", "runecraftory.patchouli.entry.npc.jobs.1.title")
                .addSimpleTextPage("runecraftory.patchouli.entry.npc.jobs.2", "runecraftory.patchouli.entry.npc.jobs.2.title")
                .addSimpleTextPage("runecraftory.patchouli.entry.npc.jobs.3", "runecraftory.patchouli.entry.npc.jobs.3.title")
                .addSimpleTextPage("runecraftory.patchouli.entry.npc.jobs.4", "runecraftory.patchouli.entry.npc.jobs.4.title")
                .addSimpleTextPage("runecraftory.patchouli.entry.npc.jobs.5", "runecraftory.patchouli.entry.npc.jobs.5.title")
                .addSimpleTextPage("runecraftory.patchouli.entry.npc.jobs.6", "runecraftory.patchouli.entry.npc.jobs.6.title")
                .addSimpleTextPage("runecraftory.patchouli.entry.npc.jobs.7", "runecraftory.patchouli.entry.npc.jobs.7.title")
                .addSimpleTextPage("runecraftory.patchouli.entry.npc.jobs.8", "runecraftory.patchouli.entry.npc.jobs.8.title")
                .addSimpleTextPage("runecraftory.patchouli.entry.npc.jobs.9", "runecraftory.patchouli.entry.npc.jobs.9.title")
                .build();

        CategoryBuilder cat = book.addCategory("category.entities", "runecraftory.patchouli.category.entities", "runecraftory.patchouli.category.entities.desc", new ItemStack(ModItems.SPAWNER.get()))
                .setSortnum(4);

        for (RegistryEntrySupplier<EntityType<?>> sup : ModEntities.getMonsters()
                .stream().sorted(Comparator.comparing(RegistryEntrySupplier::getID)).toList()) {
            cat.addEntry("entry.entity." + sup.getID().getPath(),
                            sup.get().getDescriptionId(), SpawnEgg.fromType(sup.get()).map(ItemStack::new).orElse(ItemStack.EMPTY))
                    .addEntityPage(sup.getID())
                    .setScale(0.7f)
                    .build()
                    .addSimpleTextPage("runecraftory.patchouli.entry.entity." + sup.getID());
        }
        consumer.accept(book);
    }
}
