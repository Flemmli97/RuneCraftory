package io.github.flemmli97.runecraftory.common.items.weapons;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Supplier;

public class ItemSpell extends Item {

    private final Supplier<? extends Spell> spell;

    public ItemSpell(Supplier<? extends Spell> spell, Properties properties) {
        super(properties);
        this.spell = spell;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(stack, world, list, flag);
        if (this.spell.get() == ModSpells.EMPTY.get())
            list.add(new TextComponent("WIP").withStyle(ChatFormatting.DARK_RED));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (player instanceof ServerPlayer serverPlayer && player.getCooldowns().getCooldownPercent(this, 0) <= 0 && this.spell.get().use(serverPlayer.getLevel(), player, player.getItemInHand(hand))) {
            player.getCooldowns().addCooldown(this, this.getSpell().coolDown());
            this.spell.get().levelSkill(serverPlayer);
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        return InteractionResultHolder.fail(player.getItemInHand(hand));
    }

    public Spell getSpell() {
        return this.spell.get();
    }
}
