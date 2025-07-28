package io.github.flemmli97.runecraftory.common.items.weapons;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
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
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, list, tooltipFlag);
        if (this.spell.get() == RuneCraftorySpells.EMPTY.get())
            list.add(Component.literal("WIP").withStyle(ChatFormatting.DARK_RED));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (player instanceof ServerPlayer serverPlayer && this.useSpell(serverPlayer, player.getItemInHand(hand))) {
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        return InteractionResultHolder.fail(player.getItemInHand(hand));
    }

    public boolean useSpell(ServerPlayer player, ItemStack stack) {
        if (!this.getSpell().canUse(player.serverLevel(), player, stack))
            return false;
        if (this.getSpell().useAction() != null) {
            PlayerData data = Platform.INSTANCE.getPlayerData(player);
            if (player.getCooldowns().getCooldownPercent(this, 0) <= 0) {
                return data.getWeaponHandler().doWeaponAttack(this.getSpell().useAction(), stack, this.getSpell());
            }
            return false;
        } else {
            if (player.getCooldowns().getCooldownPercent(this, 0) <= 0 && this.spell.get().use(player.serverLevel(), player, stack)) {
                player.getCooldowns().addCooldown(this, this.getSpell().properties().cooldown());
                this.spell.get().levelSkill(player);
                return true;
            }
        }
        return false;
    }

    public Spell getSpell() {
        return this.spell.get();
    }
}
