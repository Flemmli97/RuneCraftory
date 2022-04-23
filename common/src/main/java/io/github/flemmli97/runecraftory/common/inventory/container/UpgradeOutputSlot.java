package io.github.flemmli97.runecraftory.common.inventory.container;

import io.github.flemmli97.runecraftory.api.datapack.ItemStat;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.inventory.PlayerContainerInv;
import io.github.flemmli97.runecraftory.common.utils.CraftingUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class UpgradeOutputSlot extends Slot {

    private int amountCrafted;
    private final PlayerContainerInv ingredientInv;
    private final ContainerUpgrade container;

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
    protected void checkTakeAchievements(ItemStack stack) {
        Player player = this.ingredientInv.getPlayer();
        if (this.amountCrafted > 0) {
            stack.onCraftedBy(player.level, player, this.amountCrafted);
            Platform.INSTANCE.craftingEvent(player, stack, this.ingredientInv);
        }
        this.amountCrafted = 0;
    }

    @Override
    protected void onSwapCraft(int amount) {
        super.onSwapCraft(amount);
        this.amountCrafted += amount;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public boolean mayPickup(Player player) {
        return CraftingUtils.canUpgrade(player, this.container.craftingType(), this.ingredientInv.getItem(6), this.ingredientInv.getItem(7))
                && (player.isCreative() || Platform.INSTANCE.getPlayerData(player).map(data -> data.getMaxRunePoints() >= this.container.rpCost()).orElse(false));
    }

    @Override
    public ItemStack remove(int amount) {
        if (this.hasItem()) {
            this.amountCrafted += Math.min(amount, this.getItem().getCount());
        }
        return super.remove(amount);
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        this.checkTakeAchievements(stack);
        if (!(player instanceof ServerPlayer serverPlayer))
            return;
        ItemStack ing1 = this.ingredientInv.getItem(6);
        ItemStack ing2 = this.ingredientInv.getItem(7);
        Platform.INSTANCE.getPlayerData(serverPlayer).ifPresent(data -> {
            data.decreaseRunePoints(player, this.container.rpCost(), true);
            switch (this.container.craftingType()) {
                case FORGE -> LevelCalc.levelSkill(serverPlayer, data, EnumSkills.FORGING, 1.5f + Math.min(0, DataPackHandler.getStats(this.ingredientInv.getItem(7).getItem()).map(ItemStat::getDiff).orElse(0) - data.getSkillLevel(EnumSkills.FORGING)[0]) * 0.3f);
                case ARMOR -> LevelCalc.levelSkill(serverPlayer, data, EnumSkills.CRAFTING, 1.5f + Math.min(0, DataPackHandler.getStats(this.ingredientInv.getItem(7).getItem()).map(ItemStat::getDiff).orElse(0) - data.getSkillLevel(EnumSkills.CRAFTING)[0]) * 0.3f);
                case CHEM -> LevelCalc.levelSkill(serverPlayer, data, EnumSkills.CHEMISTRY, 1.5f + Math.min(0, DataPackHandler.getStats(this.ingredientInv.getItem(7).getItem()).map(ItemStat::getDiff).orElse(0) - data.getSkillLevel(EnumSkills.CHEMISTRY)[0]) * 0.3f);
                case COOKING -> LevelCalc.levelSkill(serverPlayer, data, EnumSkills.COOKING, 1.5f + Math.min(0, DataPackHandler.getStats(this.ingredientInv.getItem(7).getItem()).map(ItemStat::getDiff).orElse(0) - data.getSkillLevel(EnumSkills.COOKING)[0]) * 0.3f);
            }
        });
        if (!ing1.isEmpty()) {
            this.ingredientInv.removeItem(6, 1);
            ing1 = this.ingredientInv.getItem(6);
        }
        if (!ing2.isEmpty()) {
            this.ingredientInv.removeItem(7, 1);
            ing2 = this.ingredientInv.getItem(7);
        }
        player.level.playSound(null, player.blockPosition(), SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1, 1);
        if (ing1.isEmpty() || ing2.isEmpty())
            this.container.slotsChanged(this.ingredientInv);
    }
}