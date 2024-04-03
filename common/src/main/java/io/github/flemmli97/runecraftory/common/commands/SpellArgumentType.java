package io.github.flemmli97.runecraftory.common.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class SpellArgumentType implements ArgumentType<ResourceLocation> {
    private static final Collection<String> EXAMPLES = Arrays.asList("");

    public SpellArgumentType() {
    }

    public static SpellArgumentType spell() {
        return new SpellArgumentType();
    }

    @Override
    public ResourceLocation parse(StringReader reader) throws CommandSyntaxException {
        return ResourceLocation.read(reader);
    }

    public static <S> Spell getSpell(CommandContext<S> context, String name) {
        ResourceLocation id = context.getArgument(name, ResourceLocation.class);
        return ModSpells.SPELL_REGISTRY.get().getFromId(id);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContext, SuggestionsBuilder suggestionsBuilder) {
        return SharedSuggestionProvider.suggestResource(ModSpells.SPELL_REGISTRY.get().keys(), suggestionsBuilder);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
