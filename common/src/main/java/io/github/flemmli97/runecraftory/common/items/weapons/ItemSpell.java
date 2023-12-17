package io.github.flemmli97.runecraftory.common.items.weapons;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.platform.Platform;
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
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (player instanceof ServerPlayer serverPlayer && this.useSpell(serverPlayer, player.getItemInHand(hand))) {
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        return InteractionResultHolder.fail(player.getItemInHand(hand));
    }

    public boolean useSpell(ServerPlayer player, ItemStack stack) {
        if (!this.getSpell().canUse(player.getLevel(), player, stack))
            return false;
        if (this.getSpell().useAction() != null) {
            return Platform.INSTANCE.getPlayerData(player)
                    .map(d -> {
                        if (player.getCooldowns().getCooldownPercent(this, 0) <= 0) {
                            return d.getWeaponHandler().doWeaponAttack(player, this.getSpell().useAction(), stack, WeaponHandler.simpleServersidedAttackExecuter(() -> {
                                if (this.spell.get().use(player.getLevel(), player, stack)) {
                                    player.getCooldowns().addCooldown(this, this.getSpell().coolDown());
                                    this.spell.get().levelSkill(player);
                                }
                            }));
                        }
                        return false;
                    }).orElse(false);
        } else {
            if (player.getCooldowns().getCooldownPercent(this, 0) <= 0 && this.spell.get().use(player.getLevel(), player, stack)) {
                player.getCooldowns().addCooldown(this, this.getSpell().coolDown());
                this.spell.get().levelSkill(player);
                return true;
            }
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(stack, world, list, flag);
        if (this.spell.get() == ModSpells.EMPTY.get())
            list.add(new TextComponent("WIP").withStyle(ChatFormatting.DARK_RED));
    }

    public Spell getSpell() {
        return this.spell.get();
    }
}
