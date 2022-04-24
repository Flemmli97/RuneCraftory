package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.RFCreativeTabs;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import xyz.brassgoggledcoders.patchouliprovider.BookBuilder;
import xyz.brassgoggledcoders.patchouliprovider.EntryBuilder;
import xyz.brassgoggledcoders.patchouliprovider.PatchouliBookProvider;

import java.util.Comparator;
import java.util.function.Consumer;

public class PatchouliGen extends PatchouliBookProvider {

    public PatchouliGen(DataGenerator gen) {
        super(gen, RuneCraftory.MODID, "en_us");
    }

    @Override
    protected void addBooks(Consumer<BookBuilder> consumer) {
        EntryBuilder builder;
        BookBuilder book = (builder = this.createBookBuilder("runecraftory_book", "runecraftory_book", "runecraftory.patchouli.landing")
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
                .addSpotlightPage(new ItemStack(ModBlocks.accessory.get()))
                .setText("runecraftory.patchouli.entry.crafting.accessory")
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
                .setSortnum(1)
                .addEntry("entry.entities", "runecraftory.patchouli.entry.entities", ModItems.spawner.get().getRegistryName().toString()))
                .build()
                .build();

        for (RegistryEntrySupplier<EntityType<?>> sup : ModEntities.getMonsters()
                .stream().sorted(Comparator.comparing(RegistryEntrySupplier::getID)).toList()) {
            builder.addEntityPage(sup.getID())
                    .setScale(0.7f)
                    .build()
                    .addSimpleTextPage("runecraftory.patchouli.entry.entity." + sup.getID());
        }
        consumer.accept(book);
    }
}
