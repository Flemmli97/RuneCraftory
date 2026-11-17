package io.github.flemmli97.runecraftory.common.commands.sub;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.github.flemmli97.runecraftory.api.calendar.Weather;
import io.github.flemmli97.runecraftory.common.world.data.Calendar;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

import java.util.stream.Stream;

public class WeatherCommand {

    public static <T extends ArgumentBuilder<CommandSourceStack, T>> void register(ArgumentBuilder<CommandSourceStack, T> builder) {
        builder.then(Commands.literal("weather").requires(src -> src.hasPermission(2))
                .then(Commands.argument("weather", StringArgumentType.string()).suggests((context, sb) -> SharedSuggestionProvider.suggest(Stream.of(Weather.values()).map(Object::toString), sb))
                        .executes(WeatherCommand::setWeather)));
    }

    private static int setWeather(CommandContext<CommandSourceStack> ctx) {
        Weather weather;
        String s = StringArgumentType.getString(ctx, "weather");
        try {
            weather = Weather.valueOf(s);
        } catch (IllegalArgumentException e) {
            ctx.getSource().sendFailure(Component.translatable("runecraftory.command.weather.no", s));
            return 0;
        }
        Calendar.get(ctx.getSource().getLevel()).updateWeatherTo(ctx.getSource().getLevel(), weather);
        ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.set.weather", Component.translatable(weather.translation)), false);
        return 1;
    }
}
