package io.github.flemmli97.runecraftory.common.commands.sub;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.components.StaffData;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryDataComponentTypes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.Set;

public class SpellCommand {

    public static <T extends ArgumentBuilder<CommandSourceStack, T>> void register(ArgumentBuilder<CommandSourceStack, T> builder, CommandBuildContext buildContext) {
        builder.then(Commands.literal("spell").requires(src -> src.hasPermission(2))
                .then(Commands.literal("cast")
                        .then(Commands.argument("spell", ResourceArgument.resource(buildContext, RuneCraftorySpells.SPELL_REGISTRY_KEY)).executes(ctx -> SpellCommand.castSpell(ctx, Set.of(ctx.getSource().getEntityOrException())))
                                .then(Commands.argument("as", EntityArgument.entities()).executes(ctx -> SpellCommand.castSpell(ctx, EntityArgument.getEntities(ctx, "as"))))))
                .then(Commands.literal("apply")
                        .then(Commands.argument("spell", ResourceArgument.resource(buildContext, RuneCraftorySpells.SPELL_REGISTRY_KEY))
                                .then(Commands.argument("tier", IntegerArgumentType.integer(1, 3)).executes(ctx -> SpellCommand.applySpellTo(ctx, Set.of(ctx.getSource().getEntityOrException())))
                                        .then(Commands.argument("as", EntityArgument.entities()).executes(ctx -> SpellCommand.applySpellTo(ctx, EntityArgument.getEntities(ctx, "as")))))))
        );
    }

    @SuppressWarnings("unchecked")
    private static int castSpell(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        int success = 0;
        Holder.Reference<Spell> spell = ResourceArgument.getResource(ctx, "spell", (ResourceKey<Registry<Spell>>) RuneCraftorySpells.SPELLS.registry().key());
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity living) {
                spell.value().use(living);
                success++;
            }
        }
        return success;
    }

    @SuppressWarnings("unchecked")
    private static int applySpellTo(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) throws CommandSyntaxException {
        int success = 0;
        Holder.Reference<Spell> spell = ResourceArgument.getResource(ctx, "spell", (ResourceKey<Registry<Spell>>) RuneCraftorySpells.SPELLS.registry().key());
        int tier = IntegerArgumentType.getInteger(ctx, "tier");
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity living) {
                ItemStack stack = living.getMainHandItem();
                if (stack.getItem() instanceof ItemStaffBase) {
                    StaffData data = stack.getOrDefault(RuneCraftoryDataComponentTypes.STAFF.get(), StaffData.DEFAULT);
                    stack.set(RuneCraftoryDataComponentTypes.STAFF.get(), switch (tier) {
                        case 3 -> data.setTier3Spell(spell);
                        case 2 -> data.setTier2Spell(spell);
                        default -> data.setTier1Spell(spell);
                    });
                    success++;
                }
            }
        }
        return success;
    }
}
