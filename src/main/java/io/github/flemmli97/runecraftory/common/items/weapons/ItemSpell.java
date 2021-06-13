package io.github.flemmli97.runecraftory.common.items.weapons;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.function.Supplier;

public class ItemSpell extends Item {

    private final Supplier<? extends Spell> spell;

    public ItemSpell(Supplier<? extends Spell> spell, Properties properties) {
        super(properties);
        this.spell = spell;
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<ITextComponent> list, ITooltipFlag flag) {
        super.addInformation(stack, world, list, flag);
        if (this.spell.get() == ModSpells.EMPTY.get())
            list.add(new StringTextComponent("WIP").mergeStyle(TextFormatting.DARK_RED));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (!player.world.isRemote && player.getCooldownTracker().getCooldown(this, 0) <= 0 && this.spell.get().use((ServerWorld) world, player, player.getHeldItem(hand))) {
            player.getCooldownTracker().setCooldown(this, this.getSpell().coolDown());
            this.spell.get().levelSkill((ServerPlayerEntity) player);
            return ActionResult.resultSuccess(player.getHeldItem(hand));
        }
        return ActionResult.resultFail(player.getHeldItem(hand));
    }

    public Spell getSpell() {
        return this.spell.get();
    }
}
