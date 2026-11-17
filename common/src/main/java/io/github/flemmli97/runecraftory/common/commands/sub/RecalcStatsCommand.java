package io.github.flemmli97.runecraftory.common.commands.sub;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.Collection;

public class RecalcStatsCommand {

    public static <T extends ArgumentBuilder<CommandSourceStack, T>> void register(ArgumentBuilder<CommandSourceStack, T> builder) {
        builder.then(Commands.literal("recalcStats").requires(src -> src.hasPermission(2))
                .then(Commands.argument("entities", EntityArgument.entities()).executes(RecalcStatsCommand::recalcStats)));
    }

    private static int recalcStats(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Collection<? extends Entity> entities = EntityArgument.getEntities(ctx, "entities");
        int i = 0;
        for (Entity e : entities) {
            if (e instanceof ServerPlayer player) {
                RunecraftoryAttachments.PLAYER_DATA.get().get(player).recalculateStats(false);
                i++;
            } else if (e instanceof NPCEntity npc) {
                npc.recalcStatsFull();
                i++;
            } else if (e instanceof BaseMonster monster) {
                monster.recalcStatsFull();
                i++;
            }
        }
        int affected = i;
        ctx.getSource().sendSuccess(() -> Component.translatable("runecraftory.command.recalc.stats", affected), false);
        return i;
    }
}
