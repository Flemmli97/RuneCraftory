package io.github.flemmli97.runecraftory.common.inventory.container;

import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.inventory.PlayerContainerInv;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.registry.ModCriteria;
import io.github.flemmli97.runecraftory.common.utils.CraftingUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class UpgradeOutputSlot extends Slot {

    private final PlayerContainerInv ingredientInv;
    private final ContainerUpgrade container;
    private int amountCrafted;

    public UpgradeOutputSlot(Container output, ContainerUpgrade container, PlayerContainerInv ingredientInv, int id, int x, int y) {
        super(output, id, x, y);
        this.ingredientInv = ingredientInv;
        this.container = container;
    }

    @Override
    protected void onQuickCraft(ItemStack stack, int amount) {
        this.amountCrafted += amount;
        this.checkTakeAchievements(stack);
    }

    @Override
    protected void onSwapCraft(int amount) {
        super.onSwapCraft(amount);
        this.amountCrafted += amount;
    }

    @Override
    protected void checkTakeAchievements(ItemStack stack) {
        Player player = this.ingredientInv.getPlayer();
        if (this.amountCrafted > 0) {
            stack.onCraftedBy(player.level, player, this.amountCrafted);
            Platform.INSTANCE.craftingEvent(player, stack, this.ingredientInv);
        }
        this.amountCrafted = 0;
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        this.checkTakeAchievements(stack);
        if (!(player instanceof ServerPlayer serverPlayer))
            return;
        ItemStack toUpgrade = this.ingredientInv.getItem(0);
        ItemStack material = this.ingredientInv.getItem(1);
        Platform.INSTANCE.getPlayerData(serverPlayer).ifPresent(data -> {
            ModCriteria.UPGRADE_ITEM.trigger(serverPlayer);
            if (ItemNBT.getElement(toUpgrade) != ItemNBT.getElement(stack))
                ModCriteria.CHANGE_ELEMENT.trigger(serverPlayer);
            if (stack.getItem() instanceof ItemStaffBase) {
                if (DataPackHandler.itemStatManager().get(material.getItem()).map(s -> s.getTier1Spell() != null || s.getTier2Spell() != null || s.getTier3Spell() != null).orElse(false))
                    ModCriteria.CHANGE_SPELL.trigger(serverPlayer);
            }
            data.decreaseRunePoints(player, this.container.rpCost(), true);
            switch (this.container.craftingType()) {
                case FORGE -> CraftingUtils.giveUpgradeXPTo(serverPlayer, data, EnumSkills.FORGING, toUpgrade, material);
                case ARMOR -> CraftingUtils.giveUpgradeXPTo(serverPlayer, data, EnumSkills.CRAFTING, toUpgrade, material);
            }
        });
        ItemStack ing1 = this.ingredientInv.getItem(0);
        ItemStack ing2 = this.ingredientInv.getItem(1);
        if (!ing1.isEmpty()) {
            this.ingredientInv.removeItem(0, 1);
            ing1 = this.ingredientInv.getItem(0);
        }
        if (!ing2.isEmpty()) {
            this.ingredientInv.removeItem(1, 1);
            ing2 = this.ingredientInv.getItem(1);
        }
        player.level.playSound(null, player.blockPosition(), SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1, 1);
        if (ing1.isEmpty() || ing2.isEmpty())
            this.container.slotsChanged(this.ingredientInv);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack remove(int amount) {
        if (this.hasItem()) {
            this.amountCrafted += Math.min(amount, this.getItem().getCount());
        }
        return super.remove(amount);
    }

    @Override
    public boolean mayPickup(Player player) {
        return CraftingUtils.canUpgrade(player, this.container.craftingType(), this.ingredientInv.getItem(0), this.ingredientInv.getItem(1))
                && (player.isCreative() || Platform.INSTANCE.getPlayerData(player).map(data -> data.getMaxRunePoints() >= this.container.rpCost()).orElse(false));
    }
}