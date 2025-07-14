package io.github.flemmli97.runecraftory.neoforge.data.book;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryCreativeRuneCraftoryTabs;
import net.favouriteless.modopedia.api.book.Book;
import net.favouriteless.modopedia.api.datagen.builders.BookBuilder;
import net.favouriteless.modopedia.api.datagen.providers.BookProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class BookGen extends BookProvider {

    public BookGen(CompletableFuture<HolderLookup.Provider> registries, PackOutput output) {
        super(RuneCraftory.MODID, registries, output);
    }

    @Override
    protected void build(HolderLookup.Provider provider, BiConsumer<String, Book> biConsumer) {
        BookBuilder.of("runecraftory.book.title")
                .landingText("runecraftory.book.landing")
                .tab(RuneCraftoryCreativeRuneCraftoryTabs.WEAPON_TOOL_TAB.getID())
                .build("runepedia", biConsumer);
    }
}
