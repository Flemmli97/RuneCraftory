package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.RFCreativeTabs;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
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
                .setCreativeTab(RFCreativeTabs.weaponToolTab.getRecipeFolderName())
                .setVersion("1.0")
                .setI18n(true)
                .setShowProgress(false)
                .addCategory("category.main", "runecraftory.patchouli.main.start", "runecraftory.patchouli.main.start.desc", new ItemStack(ModItems.icon0.get()))
                .setSortnum(0)

                .addEntry("entry.crafting", "runecraftory.patchouli.entry.crafting", ModBlocks.forge.get().getRegistryName().toString())
                .setSortnum(0)
                .addSimpleTextPage("runecraftory.patchouli.entry.crafting.1")
                .addSpotlightPage(new ItemStack(ModBlocks.forge.get()))
                .setText("runecraftory.patchouli.entry.crafting.forge")
                .build()
                .addSpotlightPage(new ItemStack(ModBlocks.cooking.get()))
                .setText("runecraftory.patchouli.entry.crafting.cooking")
                .build()
                .addSpotlightPage(new ItemStack(ModBlocks.chemistry.get()))
                .setText("runecraftory.patchouli.entry.crafting.chemistry")
                .build()
                .addSpotlightPage(new ItemStack(ModBlocks.accessory.get()))
                .setText("runecraftory.patchouli.entry.crafting.armor")
                .build()
                .build()

                .addEntry("entry.minerals", "runecraftory.patchouli.entry.minerals", new ItemStack(ModItems.mineralIron.get()))
                .setSortnum(1)
                .addSimpleTextPage("runecraftory.patchouli.entry.minerals.1")
                .addSimpleTextPage("runecraftory.patchouli.entry.minerals.2")
                .build()

                .addEntry("entry.shipping", "runecraftory.patchouli.entry.shipping", new ItemStack(ModItems.shippingBin.get()))
                .setSortnum(2)
                .addSimpleTextPage("runecraftory.patchouli.entry.shipping.1")
                .build()

                .addEntry("entry.entities", "runecraftory.patchouli.entry.entities", SpawnEgg.fromType(ModEntities.wooly.get()).map(ItemStack::new).orElse(ItemStack.EMPTY))
                .setSortnum(3)
                .addSimpleTextPage("runecraftory.patchouli.entry.entities.1")
                .addSimpleTextPage("runecraftory.patchouli.entry.entities.2")
                .addSimpleTextPage("runecraftory.patchouli.entry.entities.3")
                .addSimpleTextPage("runecraftory.patchouli.entry.entities.4")
                .build()

                .addEntry("entry.equipment", "runecraftory.patchouli.entry.equipment", new ItemStack(ModItems.shortDagger.get()))
                .setSortnum(4)
                .addSimpleTextPage("runecraftory.patchouli.entry.equipment.1")
                .build()

                .addEntry("entry.spellskills", "runecraftory.patchouli.entry.spellskills", new ItemStack(ModItems.teleport.get()))
                .setSortnum(5)
                .addSimpleTextPage("runecraftory.patchouli.entry.spellskills.1")
                .build()

                .addEntry("entry.farming", "runecraftory.patchouli.entry.farming", new ItemStack(ModItems.turnipSeeds.get()))
                .setSortnum(6)
                .addSimpleTextPage("runecraftory.patchouli.entry.farming.1")
                .build()

                .setSortnum(1)
                .build();

        CategoryBuilder cat = book.addCategory("entry.entities", "runecraftory.patchouli.entry.entities", "runecraftory.patchouli.entry.entities.desc", ModItems.spawner.get().getRegistryName().toString())
                .setSortnum(1);

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
